package com.budaos.modules.im.core.annotation;

import com.budaos.modules.im.core.entity.MsgType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息队列消息者注解
 * @author zhuq
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CBImConsumer {
	/**
	 * 与消息队列中的消息的业务类型busitype对应
	 * @return
	 */
	String value();
	/**
	 * 消息内容类型，与前端websocket请求消息内容中的msgtype字段映射
	 * @return
	 */
	String msgType() default MsgType.OTHER;
}
