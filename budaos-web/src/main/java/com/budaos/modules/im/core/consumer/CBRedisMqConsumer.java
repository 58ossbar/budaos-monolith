package com.budaos.modules.im.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.im.core.annotation.RedisListener;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.handler.CBWsMsgHandler;
import com.budaos.utils.tool.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * redis订阅者处理类
 * @author zhuq
 *
 */
@Component
@ConditionalOnExpression("'${com.budaos.mq-type:}'=='redis'")
@RedisListener(topic = Const.TOPIC_CHAT_MSG)
public class CBRedisMqConsumer implements MessageListener {

	private static Logger log = LoggerFactory.getLogger(CBRedisMqConsumer.class);
	
	/**
	 * 消费数据处理方法
	 * 
	 * @param msg 从消息队列中取出来的数据
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
        log.info("redis message received: {}", message);
        try {
        	CBWsMsgHandler handler = SpringContextUtils.getBean(CBWsMsgHandler.class);
			handler.handlerMsg(JSONObject.parseObject(message.toString()));
		}catch(Exception e) {
			e.printStackTrace();
			log.error("redis message received: {}", e.getMessage());
		}
    }
	
}
