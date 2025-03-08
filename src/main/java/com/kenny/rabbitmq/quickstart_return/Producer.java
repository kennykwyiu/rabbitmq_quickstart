package com.kenny.rabbitmq.quickstart_return;

import com.rabbitmq.client.*;

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
        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "invalid.key.save";

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

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("------------handle return-------------");
                System.out.println("replyCode: " + replyCode);
                System.out.println("replyText: " + replyText);
                System.out.println("exchange: " + exchange);
                System.out.println("routingKey: " + routingKey);
                System.out.println("properties: " + properties);
                System.out.println("body: " + new String(body));
            }
        });


        for (int i = 0; i < 100; i++) {
            String msg = "Hello RabbitMQ Return Message!" + i;
            channel.basicPublish(exchangeName, routingKeyError, true, null, msg.getBytes());
            Thread.sleep(1000);
        }


        channel.waitForConfirmsOrDie();
        System.out.println("All messages acknowledged by RabbitMQ!");

        channel.close();
        connection.close();
    }
}
