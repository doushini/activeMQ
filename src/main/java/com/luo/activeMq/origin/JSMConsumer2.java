package com.luo.activeMq.origin;

import java.io.IOException;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

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
public class JSMConsumer2 {

	public static void main(String[] args) throws JMSException, IOException {
		String brokerUrl = "tcp://127.0.0.1:61616";
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		ActiveMQConnection conn = (ActiveMQConnection) connectionFactory.createConnection();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Destination destination = session.createQueue("jms.queue");
		MessageConsumer messageConsumer = session.createConsumer(destination);
		//主动的去接受消息
		Message message = messageConsumer.receive();
		System.out.println("here");
		if(message instanceof TextMessage){
			TextMessage msg = (TextMessage) message;
			System.out.println(msg);
		}
		conn.start();
		
		System.in.read();
		messageConsumer.close();
		session.close();
		conn.close();
	}

}
