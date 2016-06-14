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
    private final static String QUEUE_NAME = "hello2";//之前的queue,hello是非持久化的,不能更改,所以要换一个queueName
    private final static String EXCHANGE_NAME = "exchange1";
    private final static String EXCHANGE_TYPE = "direct";
    private final static String ROUTING_KEY = "key1";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("vhost1");
        factory.setUsername("tom");
        factory.setPassword("123456");
        factory.setHost("192.168.80.22");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        //String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);
        //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //String queue = channel.queueDeclare().getQueue();
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        boolean autoAck = false;
        String consumerTag = "myConsumerTag";
        channel.basicConsume(QUEUE_NAME, autoAck, consumerTag, new DefaultConsumer(channel) {
            @Override public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                String message = new String(body, "UTF-8");
                System.out.println(deliveryTag + " [x] Received '" + message + "' , "+ routingKey+" , "+contentType);
                channel.basicAck(deliveryTag, false);
            }
        });
    }
}
