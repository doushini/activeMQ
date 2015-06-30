package com.luo.activeMq.origin;

import java.io.IOException;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

/**
 *      1. 创建连接工厂
        2. 创建连接
        3. 创建会话
        4. 创建目的地
        5. 创建生产者或消费者
        6. 生产或消费消息
        7. 关闭生产或消费者、关闭会话、关闭连接
 * @author hui.luo
 *
 */
public class JMSSender {

	public static void main(String[] args) throws JMSException, IOException {
		String brokerUrl = "tcp://127.0.0.1:61616";
//		String brokerUrl = "tcp://127.0.0.1:61616?jms.useAsyncSend=true";//使用异步发送
//		String brokerUrl = "tcp://127.0.0.1:61616?jms.useAsyncSend=true&jms.producerWindowSize=1024000";//使用异步发送，并控制异步发送量
		/**
		 * 当使用异步传送的时候，可以设置 jms.producerWindowSize（单位为字节） 属性，
		 * 当生产者中等待发送的信息量到达设置的值时，即使没有收到 broker 的应答消息，生产者同样会把这些消息发给 broker
		 */		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		ActiveMQConnection conn = (ActiveMQConnection) connectionFactory.createConnection();
		Session session = conn.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);
//		Session session = conn.createSession(false, Session.SESSION_TRANSACTED);//事务commit则自动触发确认
//		Session session = conn.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);		
		/**
		 * ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE非jms规范，是activeMq特定的，用来确认一个单独的消息
		 * 在ActiveMQSession.CLIENT_ACKNOWLEDGE 模式下，调用消息的 acknowledge() 方法会确认由此 session 消费的所有消息，
		 * 而在 ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE 模式下，仅会确认调用 acknowledge() 方法的消息
		 */
		Destination destination = session.createQueue("jms.queue");
		MessageProducer messageProducer = session.createProducer(destination);
		/**
		 * 设置消息不需持久化。默认消息需要持久化,并且持久化到KahaDB（见配置文件），也可以存储到LevelDB
		 * 【同步发送】使用持久化，则send()方法会阻塞，直到 broker 发送一个确认消息给生产者，
		 * 已经成功地将它发送的消息路由到目标目的，并把消息保存到二级存储中
		 * 但有一个例外，当发送方法在一个事物上下文中时，被阻塞的 是 commit 方法而不是 send 方法
		 */
		messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		TextMessage message = session.createTextMessage("hello");
		messageProducer.send(message);
		conn.start();
		
		System.in.read();
		messageProducer.close();
		session.close();
		conn.close();
	}

}
