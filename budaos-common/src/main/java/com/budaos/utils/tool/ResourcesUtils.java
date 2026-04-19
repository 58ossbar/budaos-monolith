package com.budaos.utils.tool;

import java.io.File;
import java.util.ResourceBundle;

/**
 * <p> Title:资源文件操作类</p>
 * <p> Description:读取资源属性文件</p>
 * <p> Copyright: Copyright (c) 2016 </p>
 * <p> Company:hihsoft.co.,ltd </p>
 *  @author hihsoft.co.,ltd
 * @version 1.0
 */
public class ResourcesUtils {
	//默认配置文件的路径前缀
	private static String prefixPath = "properties";
	//默认系统配置文件
	private static String defaultProp = prefixPath + File.separator + "budaos-config";
	//默认子工程配置文件
	private static String customProp = prefixPath + File.separator + "custom-config";
	/**
	 * 根据属性名称读取属性值(默认配置文件中)
	 * @param propertyName 属性名称
	 * @return String 属性值
	 */
	public static String getByName(String propertyName) {
		String resultM = "";
		try{
			ResourceBundle bundle = ResourceBundle.getBundle(defaultProp);
			if (bundle.containsKey(propertyName)){
				resultM = bundle.getString(propertyName);
			}
		}catch(Exception e){
			System.err.println("配置文件【"+defaultProp+"】不存在");
		}
		return resultM;
	}
	/**
	 * 根据属性名称读取属性值(子工程自定义配置文件中)
	 * @param propertyName 属性名称
	 * @return String 属性值
	 */
	public static String getByNameCustom(String propertyName) {
		String resultM = "";
		try{
			ResourceBundle bundle = ResourceBundle.getBundle(customProp);
			if (bundle.containsKey(propertyName)){
				resultM = bundle.getString(propertyName);
			}
		}catch(Exception e){
			System.err.println("配置文件【"+customProp+"】不存在");
		}
		return resultM;
	}
	/**
	 * 根据配置文件名称及属性名称读取属性值
	 * @param propertyFile 配置文件名称
	 * @param propertyName 属性名称
	 * @return String 属性值
	 */
	public static String getByName(String propertyFile, String propertyName) {
		String resultM = "";
		try{
			ResourceBundle bundle = ResourceBundle.getBundle(propertyFile);
			if (bundle.containsKey(propertyName)){
				resultM = bundle.getString(propertyName);
			}
		}catch(Exception e){
			System.err.println("配置文件【"+propertyFile+"】不存在");
		}
		return resultM;
	}
	/**
	 * 根据配置文件名称及属性名称读取属性值(默认路径)
	 * @param propertyFile 配置文件名称
	 * @param propertyName 属性名称
	 * @return String 属性值
	 */
	public static String getByNameWithDefaultPath(String propertyFile, String propertyName) {
		String resultM = "";//返回结果
		try{
			ResourceBundle bundle = ResourceBundle.getBundle(prefixPath + File.separator + propertyFile);
			if (bundle.containsKey(propertyName)){
				resultM = bundle.getString(propertyName);
			}
		}catch(Exception e){
			System.err.println("配置文件【"+prefixPath + File.separator + propertyFile+"】不存在");
		}
		return resultM;
	}
}