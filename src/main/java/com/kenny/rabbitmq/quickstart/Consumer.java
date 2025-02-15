package com.kenny.rabbitmq.quickstart;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.74.144");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");

        try (Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {

           String queueName = "test001";
           channel.queueDeclare(queueName, true, false, false, null);

           DeliverCallback deliverCallback = (consumerTag, delivery) -> {
               String message = new String(delivery.getBody());
               System.out.println("Received: " + message);
           };

           channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
           });

           Thread.sleep(Long.MAX_VALUE);
        }
    }
}
