package com.luo.activeMq.consumer;

/**
 * 不用实现MessageListenerAdapter接口，也就不需要onMessage方法，定义自己的方法，然后再xml中由defaultListenerMethod指定
 * MessageListenerAdapter 可以把
 * TextMessage转换为String对象；
   BytesMessage转换为byte数组；
   MapMessage转换为Map对象；
   ObjectMessage转换为对应的Serializable对象
   ================MessageListenerAdapter除了会自动的把一个普通Java类当做MessageListener来处理接收到的消息之外，其另外一个主要的功能是可以自动的发送返回消息。
 * @author hui.luo
 *
 * @version 2015年5月21日 下午2:33:57
 */
public class ConsumerMessageListener3 {

	public void handleMessage( String msg ) {
		System.out.println("消费者受到了消息3：" + msg);
	}
}
