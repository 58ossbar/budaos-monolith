package com.budaos.modules.evgl.common;

import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.StrUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2018 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 * @date 2018年7月17日 上午10:50:16
 */
public class LoginUtils {
	
	/**
	 * 检查是否已经登陆(不一定绑定了手机号码)
	 * @param request
	 * @return
	 */
	public static boolean checkIsLogin(HttpServletRequest request) {
		return StrUtils.notNull(request.getSession().getAttribute(EvglGlobal.LOGIN_SESSION_KEY));
	}
	
	/**
	 * 检查是否已经登陆并绑定了手机号码
	 * @param request
	 * @return
	 */
	public static boolean checkIsLoginAndBind(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userObject = session.getAttribute(EvglGlobal.LOGIN_SESSION_KEY);
		if(userObject == null) {
			return false;
		}
		TevglTraineeInfo userInfo = (TevglTraineeInfo)userObject;
		return StrUtils.isNotEmpty(userInfo.getMobile());
	}
	/**
	 * 获取当前登陆用户
	 * @param request
	 * @return
	 */
	public static TevglTraineeInfo getLoginUser(HttpServletRequest request) {
		if(checkIsLogin(request)) {
			return (TevglTraineeInfo)request.getSession().getAttribute(EvglGlobal.LOGIN_SESSION_KEY);
		}else {
			return null;
		}
	}
}
