package com.kenny.rabbitmq.direct_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerDirectExchange {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.74.144");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";

        channel.exchangeDeclare(exchangeName, "direct", true);

        for (int i = 0; i < 100; i++) {
            String msg = "Hello World RabbitMQ Direct Exchange Message !!! - " + i;
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

            System.out.println("Sent message: " + i + " - " + msg);
            Thread.sleep(500);
        }

        channel.close();
        connection.close();
    }
}
