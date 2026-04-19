package com.budaos.modules.common;

import java.util.Arrays;
import java.util.List;

public class ConstantProd {
	
	/**
	 * 布道师的token名称，注意！其它所有使用了业务基础的项目，不要与业务基础平台的一致！（业务基础平台的名称已经占用了token这个单词）
	 */
	public static final String TOKEN_KEY_NAME = "evglToken";
	
	/**
	 * 生产环境下，需求变更引起的专升本，教学包的数据问题，用于兼容
	 */
	public static List<String> prodPkgIdList = Arrays.asList("bdd533929612421dae212d4b1950d64d", "c0ea18bb858547a39283053169c03270", "1a3286faaead47d29731e1b316b7c6ed");
	
	/**
	 * 用于区分网站用户和管理端用户
	 */
	public static String ADMINISTRATOR = "administrator!@#$";
	
	/**
	 * 教师演示账号
	 */
	public static List<String> teacherDemoAccout = Arrays.asList("teacher");
	
	/**
	 * 学生演示账号
	 */
	public static List<String> studentDemoAccout = Arrays.asList("student");
	
}
