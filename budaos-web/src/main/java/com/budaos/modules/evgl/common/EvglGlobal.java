package com.budaos.modules.evgl.common;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2018 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 * @date 2018年7月17日 上午10:48:46
 */
public class EvglGlobal {
	//会话登陆session的key
	public static String LOGIN_SESSION_KEY = "evgl_userinfo";
	//会话登陆session的登陆状态
	public static String LOGIN_SESSION_ISLOGIN = "evgl_islogin";
	//会话登陆session的绑定状态
	public static String LOGIN_SESSION_ISBIND = "evgl_isbind";
	//会话登陆标识
	public static String LOGIN_SESSION_TOKEN = "evgl_token";
	
	/**
	 * 未登录的提示语
	 */
	public static String UN_LOGIN_MESSAGE = "登录信息已无效，请重新登录";
	
	/**
	 * 是否多用户登录(Y/N)
	 */
	public static String MULTI_USER_LOGIN = "Y";

	/**
	 * 布道师的token名称，注意！其它所有使用了业务基础的项目，不要与业务基础平台的一致！（业务基础平台的名称已经占用了token这个单词）
	 */
	public static final String TOKEN_KEY_NAME = "evglToken";
	
	/**
	 * 注册时，短信验证码，存在redis里面key前缀
	 */
	public static final String CODE_REGISTER = "regMobile_";
	
	/**
	 * 绑定时，短信验证码，存在redis里面key前缀
	 */
	public static final String CODE_BIND = "bindMobile_";
}
