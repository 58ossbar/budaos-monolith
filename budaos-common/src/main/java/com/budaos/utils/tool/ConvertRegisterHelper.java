package com.budaos.utils.tool;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * 注册Converter, 用于apache commons BeanUtils.copyProperties()方法中的class类型转换;
 * 可以修改此处代码以添加新的Converter
 * 
 */
public class ConvertRegisterHelper {

	private ConvertRegisterHelper(){}
	
	public static void registerConverters() {
		ConvertUtils.register(new StringConverter(), String.class);
		//date 
		ConvertUtils.register(new DateConverter(null),java.util.Date.class);
        ConvertUtils.register(new SqlDateConverter(null),java.sql.Date.class);
		ConvertUtils.register(new SqlTimeConverter(null),Time.class);
		ConvertUtils.register(new SqlTimestampConverter(null),Timestamp.class);
		//number
		ConvertUtils.register(new BooleanConverter(null), Boolean.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new FloatConverter(null), Float.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class); 
		ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);	
	}

//	public static void registerConverters(ConvertUtilsBean convertUtils) {
//		registerConverters(convertUtils,new String[] {"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS","HH:mm:ss"});
//	}
	
	public static void registerConverters(ConvertUtilsBean convertUtils,String[] datePatterns) {
		convertUtils.register(new StringConverter(), String.class);
		//date 
		convertUtils.register(ConvertRegisterHelper.setPatterns(new DateConverter(null),datePatterns),java.util.Date.class);
		convertUtils.register(ConvertRegisterHelper.setPatterns(new SqlDateConverter(null),datePatterns),java.sql.Date.class);
		convertUtils.register(ConvertRegisterHelper.setPatterns(new SqlTimeConverter(null),datePatterns),Time.class);
		convertUtils.register(ConvertRegisterHelper.setPatterns(new SqlTimestampConverter(null),datePatterns),Timestamp.class);
		//number
		convertUtils.register(new BooleanConverter(null), Boolean.class);
		convertUtils.register(new ShortConverter(null), Short.class);
		convertUtils.register(new IntegerConverter(null), Integer.class);
		convertUtils.register(new LongConverter(null), Long.class);
		convertUtils.register(new FloatConverter(null), Float.class);
		convertUtils.register(new DoubleConverter(null), Double.class);
		convertUtils.register(new BigDecimalConverter(null), BigDecimal.class); 
		convertUtils.register(new BigIntegerConverter(null), BigInteger.class);	
	}
	
	public static <T extends DateTimeConverter> T setPatterns(T converter ,String... patterns) {
		converter.setPatterns(patterns);
		return converter;
	}
	
}
