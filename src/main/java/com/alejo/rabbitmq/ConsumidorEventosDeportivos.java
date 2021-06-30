package com.alejo.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventosDeportivos {

    public static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Abrir conexion AMQ
        Connection connection = connectionFactory.newConnection();

        //Establlecer canal
        Channel channel = connection.createChannel();

        //Declarar Exchanges eventos
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

        //crear cola y asociarla al exchanges "eventos"
        String queueName= channel.queueDeclare().getQueue();

        // patron routin key -> pais,depor, event
        //*-> identifica una palabra
        //#-> multiples
        //eventos italia = italia.#
        //eventos tenis = *.tenis.*
        System.out.println("ingrese el routing key");
        Scanner teclado= new Scanner(System.in);
        String routingKey = teclado.nextLine();

        channel.queueBind(queueName, EXCHANGE, routingKey);

        //Crear subscripcion a ina cola asociada al exchanges "eventos"
        channel.basicConsume(
                queueName,
                true,
                (consumerTag, message)->{
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje recibido: "+ messageBody);
                    System.out.println("Routing key: "+ message.getEnvelope().getRoutingKey());
                },
                consumerTag -> {
                    System.out.println("Consumidor "+ consumerTag + " cancelado");
                });
    }
}
