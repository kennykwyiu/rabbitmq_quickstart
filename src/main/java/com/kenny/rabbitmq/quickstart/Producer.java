package com.kenny.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.74.144");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        for (int i = 0; i < 100; i++) {
            String msg = "Hello RabbitMQ!" + i;
            channel.basicPublish("", "test001", null, msg.getBytes());
            Thread.sleep(1000);
        }

        channel.close();
        connection.close();
    }
}
