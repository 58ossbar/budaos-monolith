package com.budaos.modules.core.common.cbsecurity.repeatsubmit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Title: 基于令牌 Copyright: Copyright (c) 2017 
 * Company:budaos.co.,ltd
 * @author budaos.co.,ltd
 * @version 1.0
 * 防止重复提交注解，用于方法上.
 * 使用方法:
 * 在新建页面方法上,设置save()为true,此时拦截器会在Session中保存一个token随机码,
 * 同时需要在新建的页面中添加
 * <input type="hidden" name="_token" value="${token}">
 * 保存方法需要验证重复提交的，设置remove()为true
 * 此时会在拦截器中验证是否重复提交,验证通过后删除，
 * 当再次点击保存时由于服务器端的Session中已经不存在了,所有无法验证通过.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatToken {
	// 生成 Token 标志
	boolean save() default false;
	// 移除 Token 值
	boolean remove() default false;
}