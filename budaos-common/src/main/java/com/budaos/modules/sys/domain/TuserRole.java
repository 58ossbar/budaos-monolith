package com.budaos.modules.sys.domain;


import com.budaos.core.baseclass.domain.BaseDomain;


public class TuserRole extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;
	
	private String id;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 角色ID
	 */
	private String roleId;

	/**
	 * 设置：
	 * @param id 
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取：
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置：用户ID
	 * @param userId 用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取：用户ID
	 * @return String
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * 设置：角色ID
	 * @param roleId 角色ID
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取：角色ID
	 * @return String
	 */
	public String getRoleId() {
		return roleId;
	}
	
}
