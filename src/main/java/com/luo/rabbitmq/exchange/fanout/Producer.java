package com.luo.rabbitmq.exchange.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author luohui
 *         Date: 16/6/14
 *         Time: 上午10:16
 */
public class Producer {
    private final static String EXCHANGE_NAME = "exchange2";
    private final static String EXCHANGE_TYPE = "fanout";
    private final static String ROUTING_KEY = "key1";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("vhost1");
        factory.setUsername("tom");
        factory.setPassword("123456");
        factory.setHost("192.168.80.22");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);
        //这里exchange不和queue绑定,由consumer来bind

        String message = "Hello World!";
        //String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
