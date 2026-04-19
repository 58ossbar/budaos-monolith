package com.budaos.modules.evgl.tch.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglTchClassroomRole extends BaseDomain<TevglTchClassroomRole>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchClassroomRole";
	public static final String ALIAS_ROLE_ID = "roleId";
	public static final String ALIAS_ROLE_NAME = "roleName";
	public static final String ALIAS_REMARK = "remark";
	

    /**
     * roleId       db_column: role_id 
     */	
 	@NotNull(msg="roleId不能为空")
 	@MaxLength(value=32, msg="字段[roleId]超出最大长度[32]")
	private java.lang.String roleId;
    /**
     * roleName       db_column: role_name 
     */	
 	@NotNull(msg="roleName不能为空")
 	@MaxLength(value=50, msg="字段[roleName]超出最大长度[50]")
	private java.lang.String roleName;
    /**
     * remark       db_column: remark 
     */	
 	@NotNull(msg="remark不能为空")
 	@MaxLength(value=200, msg="字段[remark]超出最大长度[200]")
	private java.lang.String remark;
	//columns END

	public TevglTchClassroomRole(){
	}

	public TevglTchClassroomRole(
		java.lang.String roleId
	){
		this.roleId = roleId;
	}

	public void setRoleId(java.lang.String value) {
		this.roleId = value;
	}
	public java.lang.String getRoleId() {
		return this.roleId;
	}
	public void setRoleName(java.lang.String value) {
		this.roleName = value;
	}
	public java.lang.String getRoleName() {
		return this.roleName;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

