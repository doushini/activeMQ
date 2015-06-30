package com.luo.activeMq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Server {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-producer.xml");
		MessageProducer messageProducer = applicationContext.getBean("messageProducer",MessageProducer.class);
		messageProducer.sendMessage();
	}
}
