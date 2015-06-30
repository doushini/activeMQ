package com.luo.activeMq.consumer;

import java.util.concurrent.TimeUnit;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
/**
 * @author hui.luo
 * @version 2015年5月21日 下午2:33:57
 */
import org.springframework.stereotype.Component;

@Component
public class ConsumerMessageListener implements MessageListener{

	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("消费者A受到了消息：" + msg.getText() );
			TimeUnit.SECONDS.sleep(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
