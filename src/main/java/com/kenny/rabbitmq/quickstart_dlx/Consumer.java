package com.kenny.rabbitmq.quickstart_dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

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

            String exchangeName = "test_dlx_exchange";
            String routingKey = "dlx.#";
            String queueName = "test_dlx_queue";


            channel.exchangeDeclare(exchangeName, "topic", true, false, null);

            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", "dlx.exchange");

            channel.queueDeclare(queueName, true, false, false, arguments);
            channel.queueBind(queueName, exchangeName, routingKey);

            channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
            channel.queueDeclare("dlx.queue", true, false, false, null);
            channel.queueBind("dlx.queue", "dlx.exchange", "#");


            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                System.out.println("Consumer Received: " + message);
            };

            channel.basicQos(0,2,false);
            channel.basicConsume("dlx.queue", true, new MyConsumer(channel));

            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
