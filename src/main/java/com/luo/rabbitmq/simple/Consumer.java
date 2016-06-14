package com.luo.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author luohui
 *         Date: 16/6/3
 *         Time: 下午7:18
 *
 *         1/持久化策略
 *         2/调度策略,exchange把消息发给哪个queue
 *         3/分配策略,queue把消息分给哪个consumer
 *         4/状态反馈
 */
public class Consumer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.80.21");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //不知道哪边先启动,最好两边都declare
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);


        /**
         *
         * 一次性取多条,改善性能
         * 多个consumer才能看到效果,consumer1取到20条,而consumer2只有0条?
         *
         */
        int prefetchCount = 20;
        channel.basicQos(prefetchCount);

        /**
         *
         * 接受消息
         *
         */
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            @Override public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body,"UTF-8"));
            }
        };

        /**
         *
         * ack为false时,一旦有consumer请求,那么queue会一次性吐出很多消息
         * ack默认为true,一次只吐出一条
         *
         */
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME,autoAck, consumer);
    }
}
