package com.luo.rabbitmq.simple;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * @author luohui
 *         Date: 16/6/3
 *         Time: 下午6:53
 *
 *
 *         1/持久化策略
 *         2/调度策略,exchange把消息发给哪个queue
 *         3/分配策略,queue把消息分给哪个consumer
 *
 */
public class Producer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.80.21");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         *
         * 如果采用标准的 AMQP 协议，则唯一能够保证消息不会丢失的方式是利用事务机制
         * -- 令 channel 处于 transactional 模式、向其 publish 消息、执行 commit 动作。
         * 在这种方式下，事务机制会带来大量的多余开销，并会导致吞吐量下降 250% 。
         * 为了补救事务带来的问题，引入了 confirmation 机制（即 Publisher Confirm）。
         *
         * 为了使能 confirm 机制，client 首先要发送 confirm.select 方法帧。
         * 取决于是否设置了 no-wait 属性，broker 会相应的判定是否以 confirm.select-ok 进行应答。
         * 一旦在 channel 上使用 confirm.select方法，channel 就将处于 confirm 模式。
         * 处于 transactional 模式的 channel 不能再被设置成 confirm 模式，反之亦然
         *
         */
        channel.confirmSelect();

        exchangeDeclare(channel);
        queueDeclare(channel);

        String msg = "hello boy";
        channel.basicPublish("",QUEUE_NAME,new AMQP.BasicProperties().builder().deliveryMode(2).build(),msg.getBytes());//消息的持久化
        System.out.println("send ok");
        channel.close();
        connection.close();
    }

    private static void exchangeDeclare(Channel channel) throws IOException {
        /**
         * fanout广播
         *
         */
        channel.exchangeDeclare("exchange1","fanout",true,false,null);
        channel.queueDeclare("A",true,false,false,null);
        channel.queueDeclare("B",true,false,false,null);
        channel.queueDeclare("C",true,false,false,null);

        String routingKey = "";
        channel.queueBind("A","exchange1",routingKey);
        channel.queueBind("B","exchange1",routingKey);

        //C没有绑定,只有AB收到消息

        /**
         * direct要和routingKey匹配
         */
        channel.exchangeDeclare("exchange2","direct",true);
        channel.queueDeclare("E",true,false,false,null);
        channel.queueDeclare("F",true,false,false,null);
        channel.queueBind("E","exchange2","jack");
        channel.queueBind("F","exchange2","tom");
        channel.basicPublish("exchange2","tom",null,"hello".getBytes());

        /**
         *
         * topic
         * routingKey="tom.*.*"
         * routingKey="tom.#"
         *
         */

        /**
         *
         * header
         * 自定义key-value匹配
         *
         */


    }

    private static void queueDeclare(Channel channel) throws IOException {
        //不知道哪边先启动,最好两边都declare
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);//queue的持久化,queue一旦配置好就无法修改
        //除非先删除
        //channel.queueDelete(QUEUE_NAME);
    }
}
