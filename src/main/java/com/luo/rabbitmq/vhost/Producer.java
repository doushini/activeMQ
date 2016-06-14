package com.luo.rabbitmq.vhost;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author luohui
 *         Date: 16/6/14
 *         Time: 上午10:16
 */
public class Producer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUri("amqp://userName:password@hostName:portNumber/virtualHost");
        factory.setVirtualHost("vhost1");
        factory.setUsername("tom");
        factory.setPassword("123456");
        factory.setHost("192.168.80.22");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";

        //String exchange, String routingKey, BasicProperties props, byte[] body
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
