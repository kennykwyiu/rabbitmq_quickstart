package com.kenny.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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
        channel.confirmSelect();
        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("✅ ACK received! DeliveryTag: " + deliveryTag + ", Multiple: " + multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("❌ NACK received! DeliveryTag: " + deliveryTag + ", Multiple: " + multiple);
            }
        });


        for (int i = 0; i < 100; i++) {
            String msg = "Hello RabbitMQ!" + i;
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
            Thread.sleep(1000);
        }

        channel.close();
        connection.close();
    }
}
