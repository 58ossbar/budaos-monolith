package com.budaos.modules.common.config;

import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

@Component
public class SeriaNoUtils {
	@Autowired
	private HashOperations<String, String, Object> hashOperations;
	@Autowired
	public RedisTemplate<String, String> redisTemplate;
	/**
	 * 生成订单，退款单，支付单的编号
	 * 
	 * @param key
	 * @return
	 */
	public String getSeriaNo(String key){
		Date date=new Date();//取时间 
    	String nowDate = new SimpleDateFormat("yyyyMMdd").format(date);
    	int num = 0;
    	try{
	    	if(!redisTemplate.hasKey(MyRedisKeyConfig.SERAI_KEY+nowDate)){
	        	num = hashOperations.increment(MyRedisKeyConfig.SERAI_KEY+nowDate, key, 1).intValue();
	        	Calendar calendar = new GregorianCalendar(); 
	            calendar.setTime(date); 
	            calendar.add(calendar.DATE,2);//把日期往后增加一天.整数往后推,负数往前移动 
	            date=calendar.getTime();
	        	redisTemplate.expireAt(MyRedisKeyConfig.SERAI_KEY+nowDate, date);
	    	}else{
	    		num = hashOperations.increment(MyRedisKeyConfig.SERAI_KEY+nowDate, key, 1).intValue();
	    	}
		}catch(Exception e){
			num = new Random().nextInt(9000000)+1000000;
			System.err.println("redis服务器异常，请联系管理员");
		}
    	return nowDate+ StrUtils.apadLeft(String.valueOf(num), "0", 7);
	}
	/**
	 * 生成订单，退款单，支付单的编号
	 * 
	 * @param key
	 * @return
	 */
	public String getSeriaNoWithRandom(String key){
		Date date=new Date();//取时间 
    	String nowDate = new SimpleDateFormat("yyyyMMdd").format(date);
    	int num = 0;
    	try{
	    	if(!redisTemplate.hasKey(MyRedisKeyConfig.SERAI_KEY+nowDate)){
	        	num = hashOperations.increment(MyRedisKeyConfig.SERAI_KEY+nowDate, key, 1).intValue();
	        	Calendar calendar = new GregorianCalendar(); 
	            calendar.setTime(date); 
	            calendar.add(calendar.DATE,2);//把日期往后增加一天.整数往后推,负数往前移动 
	            date=calendar.getTime();
	        	redisTemplate.expireAt(MyRedisKeyConfig.SERAI_KEY+nowDate, date);
	    	}else{
	    		num = hashOperations.increment(MyRedisKeyConfig.SERAI_KEY+nowDate, key, 1).intValue();
	    	}
    	}catch(Exception e){
    		num = new Random().nextInt(9000000)+1000000;
			System.err.println("redis服务器异常，请联系管理员");
    	}
    	return nowDate+StrUtils.apadLeft(String.valueOf(num), "0", 7)+Integer.toString(new Random().nextInt(9000)+1000);
	}
}
