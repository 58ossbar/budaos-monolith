package com.budaos.modules.im.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis消息订阅监听注解(使用该注解的类，同时需要实现MessageListener接口)
 * @author zhuq
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisListener {
	/**
	 * 需要订阅的topic即channel
	 * @return
	 */
	String topic();
}
