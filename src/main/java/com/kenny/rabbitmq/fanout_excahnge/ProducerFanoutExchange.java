package com.kenny.rabbitmq.fanout_excahnge;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerFanoutExchange {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.74.144");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("password");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        // 4. Declare exchange (fanout type)
        String exchangeName = "test_fanout_exchange";

        // Declare the exchange with type 'fanout'
        channel.exchangeDeclare(exchangeName, "fanout", true);

        // 5. Send messages (broadcast to all bound queues)
        for (int i = 0; i < 10; i++) {
            String msg = "Hello World RabbitMQ & FANOUT Exchange Message ... " + i;
            channel.basicPublish(exchangeName, "", null, msg.getBytes());
        }

        // Close connection
        channel.close();
        connection.close();
    }
}
