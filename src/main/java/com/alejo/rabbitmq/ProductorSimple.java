package com.alejo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ProductorSimple {
    public static void main(String[] args) throws IOException, TimeoutException {
        String message = "Hola a todos";

        //Abrir conexion AMQ y Establecer canal
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try ( Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()){

            //Crear cola
            String primeraCola = "primeraCola";
            channel.queueDeclare(primeraCola, false, false, false, null);

            //Enviar mensaje al exchange
            channel.basicPublish("", primeraCola, null, message.getBytes());
        }
    }
}
