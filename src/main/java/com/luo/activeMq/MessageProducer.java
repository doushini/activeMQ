package com.luo.activeMq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
/**
 * @author hui.luo
 *
 * @version 2015年5月21日 下午1:46:01
 */
public class MessageProducer {
	private JmsTemplate jmsTemplate;
	private Destination destination;
	private ExecutorService es = Executors.newCachedThreadPool();
	
	public void sendMessage() {
		for (int i = 0; i < 100; i++) {
			final String message = "hello "+i;
			es.submit(new Runnable() {
				public void run() {
					System.out.println("---------------生产者发了一个消息："+message);  
					jmsTemplate.send(destination, new MessageCreator() {			
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(message);
						}
					});
				}
			});
		}
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}
