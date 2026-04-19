package com.budaos.modules.im.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.handler.CBWsMsgHandler;
import com.budaos.utils.tool.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * rabbitmq消息队列消费者示例模板
 * 
 * @author zhuq
 *
 */
@Component
@ConditionalOnExpression("'${com.budaos.mq-type:}'=='rabbit'")
@RabbitListener(queues = Const.TOPIC_CHAT_MSG)
public class CBRabbitMqConsumer {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 消费数据处理方法
	 * 
	 * @param msg 从消息队列中取出来的数据
	 */
	@RabbitHandler
	public void process(Object message) {
		byte[] body = ((Message)message).getBody();
		String msg = new String(body);
		log.debug("rabbitmq message received: {}", msg);
		try {
			CBWsMsgHandler handler = SpringContextUtils.getBean(CBWsMsgHandler.class);
			handler.handlerMsg(JSONObject.parseObject(msg));
		}catch(Exception e) {
			e.printStackTrace();
			log.error("rabbitmq message received: {}", e.getMessage());
		}
	}
}
