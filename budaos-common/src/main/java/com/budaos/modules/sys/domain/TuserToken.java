package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

public class TuserToken extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	private String userId;
	//token
	private String token;
	//过期时间
	private String expireTime;
	//更新时间
	private String updateTime;
	//用户系统皮肤
	private String userTheme;

	public String getUserTheme() {
		return userTheme;
	}
	public void setUserTheme(String userTheme) {
		this.userTheme = userTheme;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 获取：token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * 设置：过期时间
	 */
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	/**
	 * 获取：过期时间
	 */
	public String getExpireTime() {
		return expireTime;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public String getUpdateTime() {
		return updateTime;
	}
}
