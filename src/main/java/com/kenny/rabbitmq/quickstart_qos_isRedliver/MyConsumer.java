package com.kenny.rabbitmq.quickstart_qos_isRedliver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyConsumer extends DefaultConsumer {
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */

    private Channel channel;
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("------------------consume message-----------------------");
        System.out.println("consumerTag: " + consumerTag);
        System.out.println("envelope: " + envelope);
        System.out.println("properties: " + properties);
        System.out.println("body: " + new String(body));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long deliveryTag = envelope.getDeliveryTag();
        if (properties.getHeaders() != null && properties.getHeaders().get("num").equals(0)) {
            System.err.println("------requeue-------");
            if (envelope.isRedeliver()) {
                System.err.println("Message has been redelivered before, rejecting it!");
                channel.basicNack(deliveryTag, false, false); // Reject and discard the message
            } else {
                channel.basicNack(deliveryTag, false, true); // Requeue for the first time
            }
        } else {
            channel.basicAck(deliveryTag, false);
        }

    }
}
