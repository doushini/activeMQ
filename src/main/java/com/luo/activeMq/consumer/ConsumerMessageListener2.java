package com.luo.activeMq.consumer;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;
/**
 * @author hui.luo
 *
 * @version 2015年5月21日 下午2:33:57
 */
public class ConsumerMessageListener2 implements SessionAwareMessageListener<Message>{
	@Resource(name="queueDestination")
	private Destination destination;

	public void onMessage(Message message, Session session) throws JMSException {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("消费者B受到了消息：" + msg.getText() );
			//回复消息
//			MessageProducer producer = session.createProducer(destination);
//			Message pmsg = session.createTextMessage("我收到消息啦");
//			producer.send(destination,pmsg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}
