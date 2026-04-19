package com.budaos.modules.common.annotation;

/**
 * 演示账号（用于限制一些功能使用，如新增修改删除）
 * @author huj
 * @create 2022-01-05 15:35
 * @email hujun@budaos.com
 */
public @interface DemoAccountLimit {
	
	String msg() default "";
	
}
