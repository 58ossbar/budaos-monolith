package com.budaos.common.utils;

import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.tool.SpringContextUtils;
import com.budaos.utils.tool.StrUtils;
import org.springframework.stereotype.Component;

@Component
public class ServiceLoginUtil {

	/**
	 * <p>获取当前登陆用户</p>
	 * @author zhuq
	 * @data 2019年6月18日
	 * @return 返回当前登陆用户对象
	 */
	public TsysUserinfo getLoginUser() {
		TsysUserinfo userInfo = null;
		try {
			Object loginUtils = SpringContextUtils.getBean("loginUtils");
			Class<?> cls = Class.forName("com.budaos.modules.core.common.utils.LoginUtils");
			Object sUserInfo = cls.getDeclaredMethod("getLoginUser").invoke(loginUtils);
			if (!StrUtils.isNull(sUserInfo)) {
				userInfo = (TsysUserinfo)sUserInfo;
			}
		} catch (Exception e) {
			// Dubbo RpcContext已移除，fallback返回null
		}
		return userInfo;
	}
	
	/**
	 * <p>获取当前登陆用户ID</p>
	 * @author zhuq
	 * @data 2019年6月18日
	 * @return 返回当前登陆用户ID
	 */
	public String getLoginUserId() {
		String userId = null;
		try {
			Object loginUtils = SpringContextUtils.getBean("loginUtils");
			Class<?> cls = Class.forName("com.budaos.modules.core.common.utils.LoginUtils");
			Object sUserId = cls.getDeclaredMethod("getLoginUserId").invoke(loginUtils);
			if (!StrUtils.isNull(sUserId)) {
				userId = sUserId.toString();
			}
		} catch (Exception e) {
			// Dubbo RpcContext已移除，fallback返回null
		}
		return userId;
	}
}
