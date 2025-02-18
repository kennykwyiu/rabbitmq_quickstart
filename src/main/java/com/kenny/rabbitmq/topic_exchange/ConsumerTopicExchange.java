package com.kenny.rabbitmq.topic_exchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerTopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.74.144");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");
        connectionFactory.setRequestedHeartbeat(10000);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            String exchangeName = "test_topic_exchange";
            String exchangeType = "topic";
            String queueName = "test_topic_queue";
            String routingKey = "user.#";

            channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);

            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);

            DeliverCallback deliverCallback = new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery delivery) throws IOException {
                    String message = new String(delivery.getBody());
                    System.out.println("Received message: " + message);
                }
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });

            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
