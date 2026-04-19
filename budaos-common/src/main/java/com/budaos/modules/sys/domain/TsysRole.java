package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;

//import org.hibernate.validator.constraints.NotBlank;

public class TsysRole extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;
	// alias
	public static final String TABLE_ALIAS = "TsysRole";
	public static final String ALIAS_ROLE_ID = "roleId";
	public static final String ALIAS_ROLE_NAME = "角色名称";
	public static final String ALIAS_REMARK = "备注";
	public static final String ALIAS_CREATE_USER_ID = "创建者ID";
	public static final String ALIAS_CREATE_TIME = "创建时间";
	public static final String ALIAS_ROLE_TYPE = "角色类型：公有、私有";
	public static final String ALIAS_ORG_ID = "所属机构";
	public static final String ALIAS_DATA_SCOPE = "数据范围:01所有数据 02所在机构及以下数据; 03本级数据 04自定义明细 05所在部门数据06所在部门及以下数据 07本人数据";
	public static final String ALIAS_STATUS = "角色状态：启用、禁用";
	/**
	 * 角色ID
	 */
	private String roleId;
	private String id;

	/**
	 * 角色名称
	 */
	//@NotBlank(message = "角色名称不能为空")
	private String roleName;

	/**
	 * 备注
	 */
	private String remark;

	private List<String> roleIdList; // 角色ID集

	private List<String> menuIdList;

	private List<String> orgIdList;

	private List<String> userIdList;

	/**
	 * 角色类型：公有、私有 db_column: role_type
	 */
	private java.lang.String roleType;
	/**
	 * 所属机构 db_column: org_id
	 */
	private java.lang.String orgId;
	/**
	 * 数据范围:01所有数据 02所在机构及以下数据; 03本级数据 04自定义明细 05所在部门数据06所在部门及以下数据 07本人数据 db_column:
	 * data_scope
	 */
	private java.lang.String dataScope;
	/**
	 * 角色状态：启用、禁用 db_column: status
	 */
	private java.lang.String status;

	/**
	 * 设置：
	 * 
	 * @param roleId
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * 设置：角色名称
	 * 
	 * @param roleName
	 *            角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 获取：角色名称
	 * 
	 * @return String
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * 设置：备注
	 * 
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：备注
	 * 
	 * @return String
	 */
	public String getRemark() {
		return remark;
	}



	public List<String> getMenuIdList() {
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		this.menuIdList = menuIdList;
	}


	public java.lang.String getRoleType() {
		return roleType;
	}

	public void setRoleType(java.lang.String roleType) {
		this.roleType = roleType;
	}

	public java.lang.String getOrgId() {
		return orgId;
	}

	public void setOrgId(java.lang.String orgId) {
		this.orgId = orgId;
	}

	public java.lang.String getDataScope() {
		return dataScope;
	}

	public void setDataScope(java.lang.String dataScope) {
		this.dataScope = dataScope;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public List<String> getOrgIdList() {
		return orgIdList;
	}

	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}
	
}
