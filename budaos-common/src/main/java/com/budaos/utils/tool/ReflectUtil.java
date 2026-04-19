package com.budaos.utils.tool;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.*;
import java.util.*;

/**
 * <p> Title:类反射工具类</p>
 * <p> Description:扩展springside</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:hihsoft.co.,ltd </p>
 *  @author hihsoft.co.,ltd
 * @version 1.0
 */
public final class ReflectUtil {
	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	/**
	 * @param obj
	 * @param cmpModifier
	 * @return
	 */
	private static String[] getFieldsName(Object obj, int cmpModifier) {
		String[] retValue;
		Class<?> objClass = obj.getClass();
		Field[] fields = objClass.getDeclaredFields();

		if (fields != null && !(fields.length < 1)) {
			int j = 0;

			for (int i = 0; i < fields.length; i++) {
				int modifier = fields[i].getModifiers();
				if (modifier == cmpModifier)
					j++;
			}

			retValue = new String[j];

			j = 0;
			for (int i = 0; i < fields.length; i++) {
				int modifier = fields[i].getModifiers();
				if (modifier == cmpModifier) {
					retValue[j] = fields[i].getName();
					j++;
				}
			}

			return retValue;
		} else
			return null;
	}

	public static final String[] getPrivateFieldsName(Object obj) {
		return getFieldsName(obj, Modifier.PRIVATE);
	}

	public static final String[] getProtectedFieldsName(Object obj) {
		return getFieldsName(obj, Modifier.PROTECTED);
	}

	public static final String[] getPublicFieldsName(Object obj) {
		return getFieldsName(obj, Modifier.PUBLIC);
	}

	public static final String[] getAllFieldsName(Object obj) {
		String[] retValue;
		Class<?> objClass = obj.getClass();
		Field[] fields = objClass.getDeclaredFields();

		if (fields != null && !(fields.length < 1)) {

			retValue = new String[fields.length];

			for (int i = 0; i < fields.length; i++) {
				retValue[i] = fields[i].getName();
			}

			return retValue;
		} else
			return null;
	}

	private static String[] getMethodsName(Object obj, int cmpModifier,
			String prefix) {
		String[] retValue;
		Class<?> objClass = obj.getClass();
		Method[] methods = objClass.getDeclaredMethods();

		if (methods != null && !(methods.length < 1)) {
			int j = 0;

			for (int i = 0; i < methods.length; i++) {
				int modifier = methods[i].getModifiers();

				if (prefix != null && !prefix.equals("")) {
					if (modifier == cmpModifier
							&& methods[i].getName().startsWith(prefix))
						j++;
				} else if (modifier == cmpModifier)
					j++;
			}

			retValue = new String[j];

			j = 0;
			for (int i = 0; i < methods.length; i++) {
				int modifier = methods[i].getModifiers();
				if (prefix != null && !prefix.equals("")) {
					if (modifier == cmpModifier
							&& methods[i].getName().startsWith(prefix)) {
						retValue[j] = methods[i].getName();
						j++;
					}
				} else if (modifier == cmpModifier) {
					retValue[j] = methods[i].getName();
					j++;
				}

			}

			return retValue;
		} else
			return null;
	}

	public static final String[] getPrivateMethodsName(Object obj) {
		return getMethodsName(obj, Modifier.PRIVATE, null);
	}

	public static final String[] getProtectedMethodsName(Object obj) {
		return getMethodsName(obj, Modifier.PROTECTED, null);
	}

	public static final String[] getPublicMethodsName(Object obj) {
		return getMethodsName(obj, Modifier.PUBLIC, null);
	}

	public static final String[] filterPublicMethodsName(Object obj,
			String prefix) {
		return getMethodsName(obj, Modifier.PUBLIC, prefix);
	}

	private static Method[] getMethods(Object obj, int cmpModifier,
			String prefix) {
		Method[] retMethod = null;

		Class<?> objClass = obj.getClass();
		Method[] methods = objClass.getDeclaredMethods();

		if (methods != null && !(methods.length < 1)) {

			int j = 0;

			for (int i = 0; i < methods.length; i++) {
				int modifier = methods[i].getModifiers();
				if (prefix != null && !prefix.equals("")) {
					if (modifier == cmpModifier
							&& methods[i].getName().startsWith(prefix))
						j++;
				} else if (modifier == cmpModifier)
					j++;
			}
			retMethod = new Method[j];

			j = 0;
			for (int i = 0; i < methods.length; i++) {
				int modifier = methods[i].getModifiers();
				if (prefix != null && !prefix.equals("")) {
					if (modifier == cmpModifier
							&& methods[i].getName().startsWith(prefix)) {
						retMethod[j] = methods[i];
						j++;
					}
				} else if (modifier == cmpModifier)
					retMethod[j] = methods[i];
			}

			return retMethod;
		} else
			return null;
	}

	public static final Method[] getPrivateMethods(Object obj, String prefix) {
		return getMethods(obj, Modifier.PRIVATE, prefix);
	}

	public static final Method[] getProtectedMethods(Object obj, String prefix) {
		return getMethods(obj, Modifier.PROTECTED, prefix);
	}

	public static final Method[] getPublicMethods(Object obj, String prefix) {
		return getMethods(obj, Modifier.PUBLIC, prefix);
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object object,
			final String fieldName) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 */
	public static Object invokeMethod(final Object object,
			final String methodName, final Class<?>[] parameterTypes,
			final Object[] parameters) {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null)
			throw new IllegalArgumentException("Could not find method ["
					+ methodName + "] on target [" + object + "]");

		method.setAccessible(true);

		try {
			return method.invoke(object, parameters);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	protected static Field getDeclaredField(final Object object,
			final String fieldName) {
		Assert.notNull(object, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 强行设置Field可访问.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	protected static Method getDeclaredMethod(Object object, String methodName,
			Class<?>[] parameterTypes) {
		Assert.notNull(object, "object不能为空");

		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(Class<?> clazz) {
		return (Class<T>) getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */

	public static Class<?> getSuperClassGenricType(final Class<?> clazz,
			final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			// logger.warn(clazz.getSimpleName() +
			// "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of "
					+ clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class<?>) params[index];
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成List.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	public static List<Object> convertElementPropertyToList(
			final Collection<?> collection, final String propertyName) {
		List<Object> list = new ArrayList<Object>();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	public static String convertElementPropertyToString(
			final Collection<?> collection, final String propertyName,
			final String separator) {
		List<Object> list = convertElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换字符串类型到clazz的property类型的值.
	 * 
	 * @param value 待转换的字符串
	 * @param clazz 提供类型信息的Class
	 * @param propertyName 提供类型信息的Class的属性.
	 */
	public static Object convertValue(Object value, Class<?> toType) {
		try {
			DateConverter dc = new DateConverter();
			dc.setUseLocaleFormat(true);
			dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
			ConvertUtils.register(dc, Date.class);
			return ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(
			Exception e) {
		if (e instanceof IllegalAccessException
				|| e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException
				|| e instanceof InvocationTargetException)
			return new IllegalArgumentException("Reflection Exception.", e);
		else
			return new RuntimeException(e);
	}
	/**
	 * 获得泛型定义类型
	 * @param obj
	 * @param fieldName
	 * @return
	 * @author Xiaojf
	 * @since 2011-9-9
	 */
	public static Class<?> getGenericType(Class<?> obj, String fieldName) {
		try {
			Field field = getDeclaredField(obj, fieldName);
			if (field != null) {
				ParameterizedType pt = (ParameterizedType) field.getGenericType();
				if (pt != null && pt.getActualTypeArguments() != null) {
					Type[] types = pt.getActualTypeArguments();
					if (types.length > 0) {
						return (Class<?>) types[0];
					}
				}
			}
		} catch (Exception e) {}
		return null;
	}
	
	public static String getRootClassName(Class<?> clazz) {
		String name = clazz.getName();
		int index = name.lastIndexOf(".");
		return name.substring(index + 1);
	}
	/**
	 * 对象属性转换为字段  例如：userName to user_name
	 * @param property 字段名
	 * @return
	 */
	public static String propertyToField(String property) {
		if (null == property) {
			return "";
		}
		char[] chars = property.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char c : chars) {
			if (CharUtils.isAsciiAlphaUpper(c)) {
				sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 字段转换成对象属性 例如：user_name to userName
	 * @param field
	 * @return
	 */
	public static String fieldToProperty(String field) {
		if (null == field) {
			return "";
		}
		char[] chars = field.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
					i++;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	   public static Set<Field> getFieldsByClass(@SuppressWarnings("rawtypes") Class cls) {
	        Set<Field> fieldSet = new HashSet<>();
	        for (Class<?> clazz = cls; clazz != Object.class; clazz = clazz.getSuperclass()) {
	            Field[] fields = clazz.getDeclaredFields();
	            if (CommonUtil.isNull(fields)) {
	                continue;
	            }
	            for (Field field : fields) {
	                if (!field.getName().equals("class") && !field.getName().equals("serialVersionUID")) {
	                    fieldSet.add(field);
	                }
	            }
	        }
	        return fieldSet;
	    }
}
