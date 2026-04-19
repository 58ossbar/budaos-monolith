package com.budaos.modules.im.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * websocket处理类注解
 * @author zhuq
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CBImService {
	/**
	 * 相当于spring mvc中的value与前端websocket请求消息内容中的busitype字段映射
	 * @return
	 */
	String value() default "";
}
