package com.kenny.rabbitmq.quickstart_return;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.74.144");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");
        connectionFactory.setRequestedHeartbeat(10000);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            String exchangeName = "test_return_exchange";
            String routingKey = "return.#";
            String queueName = "test_return_queue";


            channel.exchangeDeclare(exchangeName, "topic", true, false, null);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                System.out.println("Consumer Received: " + message);
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });

            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
