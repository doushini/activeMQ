package com.luo.activeMq.consumer;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class MessageConsumer {
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="queueDestination")
	private Destination destination;
	
	public void receive(String name){
		while(true){
			TextMessage message = (TextMessage)jmsTemplate.receive(destination);
			System.out.println("client"+name+" received :"+ message);
		}
	}
}
