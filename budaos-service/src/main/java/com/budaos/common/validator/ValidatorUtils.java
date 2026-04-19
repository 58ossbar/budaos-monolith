package com.budaos.common.validator;

import com.budaos.common.validator.core.ValidateCache;
import com.budaos.common.validator.core.ValidateHandler;
import com.budaos.core.baseclass.annotation.validator.*;
import com.budaos.utils.tool.CommonUtil;
import com.budaos.utils.tool.ReflectUtil;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Title:hibernate-validator校验工具类
 * Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public class ValidatorUtils {
	
	/*
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    */
    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws BudaosException  校验不通过，则报BudaosException异常
     */
	/*
	public static void validateEntity(Object object, Class<?>... groups)
            throws BudaosException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
        	ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            throw new BudaosException(constraint.getMessage());
        }
    }
    */
    private Object value;

    private ValidatorUtils(Object value) {
        this.value = value;
    }

    /**
     * 新建校验实例，传入目标对象
     *
     * @param value 校验对象
     * @return ValidatorUtils
     */
    public static ValidatorUtils is(Object value) {
        return new ValidatorUtils(value);
    }

    /**
     * 切换目标对象，不重新创建实例
     *
     * @param value 校验对象
     * @return ValidatorUtils
     */
    public ValidatorUtils and(Object value) {
        this.value = value;
        return this;
    }

    /**
     * 非空校验
     *
     * @return ValidatorUtils
     */
    public ValidatorUtils notNull() {
        return notNull(null);
    }

    /**
     * 非空校验
     *
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils notNull(String msg) {
        ValidateHandler.notNull(value, msg);
        return this;
    }

    /**
     * 正则校验
     *
     * @param regex 正则表达式
     * @return ValidatorUtils
     */
    public ValidatorUtils regex(String regex) {
        return regex(regex, null);
    }

    /**
     * 正则校验
     *
     * @param regex 正则表达式
     * @param msg   错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils regex(String regex, String msg) {
        ValidateHandler.regex(regex, value, msg);
        return this;
    }

    /**
     * 最大值校验
     *
     * @param max 最大值
     * @return ValidatorUtils
     */
    public ValidatorUtils max(Number max) {
        return max(max, null);
    }

    /**
     * 最大值校验
     *
     * @param max 最大值
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils max(Number max, String msg) {
        ValidateHandler.max(max, value, msg);
        return this;
    }

    /**
     * 最小值校验
     *
     * @param min 最小值
     * @return ValidatorUtils
     */
    public ValidatorUtils min(Number min) {
        return min(min, null);
    }

    /**
     * 最小值校验
     *
     * @param min 最小值
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils min(Number min, String msg) {
        ValidateHandler.min(min, value, msg);
        return this;
    }

    /**
     * 最大长度校验
     *
     * @param max 最大长度
     * @return ValidatorUtils
     */
    public ValidatorUtils maxLength(int max) {
        return maxLength(max, null);
    }

    /**
     * 最大长度校验
     *
     * @param max 最大长度
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils maxLength(int max, String msg) {
        ValidateHandler.maxLength(max, value, msg);
        return this;
    }

    /**
     * 最小长度校验
     *
     * @param min 最小长度
     * @return ValidatorUtils
     */
    public ValidatorUtils minLength(int min) {
        return minLength(min, null);
    }

    /**
     * 最小长度校验
     *
     * @param min 最小长度
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils minLength(int min, String msg) {
        ValidateHandler.minLength(min, value, msg);
        return this;
    }

    /**
     * 中文校验
     *
     * @return ValidatorUtils
     */
    public ValidatorUtils chinese() {
        return chinese(null);
    }

    /**
     * 中文校验
     *
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils chinese(String msg) {
        ValidateHandler.chinese(value, msg);
        return this;
    }

    /**
     * 英文校验
     *
     * @return ValidatorUtils
     */
    public ValidatorUtils english() {
        return english(null);
    }

    /**
     * 英文校验
     *
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils english(String msg) {
        ValidateHandler.english(value, msg);
        return this;
    }

    /**
     * 手机号校验
     *
     * @return ValidatorUtils
     */
    public ValidatorUtils phone() {
        return phone(null);
    }

    /**
     * 手机号校验
     *
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils phone(String msg) {
        ValidateHandler.phone(value, msg);
        return this;
    }

    /**
     * 邮箱校验
     *
     * @return ValidatorUtils
     */
    public ValidatorUtils email() {
        return email(null);
    }

    /**
     * 邮箱校验
     *
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils email(String msg) {
        ValidateHandler.email(value, msg);
        return this;
    }

    /**
     * 自定义日期格式校验
     *
     * @param format 格式
     * @return ValidatorUtils
     */
    public ValidatorUtils date(String format) {
        return date(format, null);
    }

    /**
     * 自定义日期格式校验
     *
     * @param format 格式
     * @param msg    错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils date(String format, String msg) {
        ValidateHandler.date(format, value, msg);
        return this;
    }

    /**
     * 身份证校验
     *
     * @return ValidatorUtils
     */
    public ValidatorUtils idCard() {
        return idCard(null);
    }

    /**
     * 身份证校验
     *
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils idCard(String msg) {
        ValidateHandler.idCard(value, msg);
        return this;
    }

    /**
     * IP地址校验
     *
     * @return ValidatorUtils
     */
    public ValidatorUtils ip() {
        return ip(null);
    }

    /**
     * IP地址校验
     *
     * @param msg 错误信息
     * @return ValidatorUtils
     */
    public ValidatorUtils ip(String msg) {
        ValidateHandler.ip(value, msg);
        return this;
    }

    /**
     * 对象校验（通过注解）
     *
     * @param value 校验对象
     * @return ValidatorUtils
     */
    public static ValidatorUtils check(Object value) {
        ValidatorUtils ValidatorUtils = new ValidatorUtils(value);
        ValidatorUtils.notNull();
        Class<?> classType = value.getClass();
        Set<Field> fieldSet = ValidateCache.getInstance().getFieldsByClass(classType);
        if (null == fieldSet) {
            fieldSet = ReflectUtil.getFieldsByClass(value.getClass());
            ValidateCache.getInstance().setClassFields(classType, fieldSet);
        }
        if (CommonUtil.isNull(fieldSet)) {
            return ValidatorUtils;
        }
        for (Field field : fieldSet) {
            Annotation[] annotations = ValidateCache.getInstance().getAnnotationsByField(field);
            if (null == annotations) {
                annotations = field.getAnnotations();
                ValidateCache.getInstance().setFieldAnnotations(field, annotations);
            }
            if (CommonUtil.isNull(annotations)) {
            	continue;
            }
            Object fieldValue;
            try {
                fieldValue = PropertyUtils.getProperty(value, field.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for (Annotation annotation : annotations) {
                if (annotation instanceof NotNull) {
                    ValidatorUtils.and(fieldValue).notNull(((NotNull) annotation).msg());
                } else if (annotation instanceof Max) {
                    Max max = (Max) annotation;
                    ValidatorUtils.and(fieldValue).max(max.value(), max.msg());
                } else if (annotation instanceof Min) {
                    Min min = (Min) annotation;
                    ValidatorUtils.and(fieldValue).min(min.value(), min.msg());
                } else if (annotation instanceof MaxLength) {
                    MaxLength maxLength = (MaxLength) annotation;
                    ValidatorUtils.and(fieldValue).maxLength(maxLength.value(), maxLength.msg());
                } else if (annotation instanceof MinLength) {
                    MinLength minLength = (MinLength) annotation;
                    ValidatorUtils.and(fieldValue).minLength(minLength.value(), minLength.msg());
                } else if (annotation instanceof Email) {
                    ValidatorUtils.and(fieldValue).email(((Email) annotation).msg());
                } else if (annotation instanceof Phone) {
                    ValidatorUtils.and(fieldValue).phone(((Phone) annotation).msg());
                } else if (annotation instanceof IdCard) {
                    ValidatorUtils.and(fieldValue).idCard(((IdCard) annotation).msg());
                } else if (annotation instanceof Regex) {
                    Regex regex = (Regex) annotation;
                    ValidatorUtils.and(fieldValue).regex(regex.value(), regex.msg());
                } else if (annotation instanceof Date) {
                    Date date = (Date) annotation;
                    String format = date.format();
                    ValidatorUtils.and(fieldValue).date(format, date.msg());
                } else if (annotation instanceof Chinese) {
                    ValidatorUtils.and(fieldValue).chinese(((Chinese) annotation).msg());
                } else if (annotation instanceof English) {
                    ValidatorUtils.and(fieldValue).english(((English) annotation).msg());
                } else if (annotation instanceof IP) {
                    ValidatorUtils.and(fieldValue).ip(((IP) annotation).msg());
                }
            }
        }
        return ValidatorUtils;
    }

}
