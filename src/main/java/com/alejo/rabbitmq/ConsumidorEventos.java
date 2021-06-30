package com.alejo.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventos {

    public static final String EVENTOS = "eventos";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Abrir conexion AMQ
        Connection connection = connectionFactory.newConnection();

        //Establlecer canal
        Channel channel = connection.createChannel();

        //Declarar Exchanges eventos
        channel.exchangeDeclare(EVENTOS, BuiltinExchangeType.FANOUT);

        //crear cola y asociarla al exchanges "eventos"
        String queueName= channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EVENTOS,"");

        //Crear subscripcion a ina cola asociada al exchanges "eventos"
        channel.basicConsume(
                queueName,
                true,
                (consumerTag, message)->{
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje recibido: "+ messageBody);
                },
                consumerTag -> {
                    System.out.println("Consumidor "+ consumerTag + " cancelado");
                });
    }
}
