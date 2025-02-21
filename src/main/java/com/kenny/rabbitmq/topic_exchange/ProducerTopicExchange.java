package com.kenny.rabbitmq.topic_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerTopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.74.144");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.update";
        String routingKey2 = "user.delete";
        String routingKey3 = "order.create";

        channel.exchangeDeclare(exchangeName, "topic", true);

        for (int i = 0; i < 100; i++) {
            String msg = "Hello World RabbitMQ Topic Exchange Message !!! - " + i;
            channel.basicPublish(exchangeName, routingKey1, null, msg.getBytes());
            channel.basicPublish(exchangeName, routingKey2, null, msg.getBytes());
            channel.basicPublish(exchangeName, routingKey3, null, msg.getBytes());

            System.out.println("Sent message: " + i + " - " + msg);
            Thread.sleep(500);
        }

        // Close connection
        channel.close();
        connection.close();
    }
}
