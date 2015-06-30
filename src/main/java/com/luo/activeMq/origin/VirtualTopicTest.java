package com.luo.activeMq.origin;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * @author hui.luo
 *
 */
public class VirtualTopicTest {
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		ActiveMQConnection conn = (ActiveMQConnection) connectionFactory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("jms.queue");
		
		MessageConsumer consumer1 = session.createConsumer(destination);
		
		MessageProducer messageProducer = session.createProducer(destination);
		TextMessage message = session.createTextMessage("hello");
		messageProducer.send(message);
		
		messageProducer.close();
		session.close();
		conn.close();
	}
}
