package com.budaos.modules.im.core.handler;

import com.budaos.utils.rabbitmq.RabbitMqProducer;
import com.budaos.utils.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 消息队列处理类
 * @author zhuq
 *
 */
@Component
public class CBMqHandler {

	@Value("${com.budaos.mq-type:}")
	private String mqType;//redis、rabbit、kafka等消息队列

	@Autowired(required = false)
	private RabbitMqProducer rabbitMqProducer;
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * 发送消息到消息队列
	 * @param topic 消息队列名称
	 * @param msg 消息内容
	 */
	public void sendMsgToMq(String topic, Object msg) {
		if("rabbit".equals(mqType)) {//如果消息队列是使用的rabbitmq
			if (!ObjectUtils.isEmpty(rabbitMqProducer)) {
				rabbitMqProducer.send(topic, msg);
			}
		}else if("redis".equals(mqType)){//否则默认为redis消息队列
			redisUtils.sendTopicMsg(topic, msg);
		}
	}
}
