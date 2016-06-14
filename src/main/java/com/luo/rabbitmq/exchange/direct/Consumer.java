package com.luo.rabbitmq.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author luohui
 *         Date: 16/6/14
 *         Time: 上午10:16
 */
public class Consumer {
    private final static String EXCHANGE_NAME = "exchange4";
    private final static String EXCHANGE_TYPE = "direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("vhost1");
        factory.setUsername("tom");
        factory.setPassword("123456");
        factory.setHost("192.168.80.22");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "key1");
        channel.queueBind(queue, EXCHANGE_NAME, "key2");
        channel.queueBind(queue, EXCHANGE_NAME, "key3");

        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                String message = new String(body, "UTF-8");
                System.out.println(deliveryTag + " [x] Received '" + message + "' , "+ routingKey+" , "+contentType);
            }
        });
    }
}
