package com.budaos.modules.im.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
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

public class TimGroupUser extends BaseDomain<TimGroupUser>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimGroupUser";
	public static final String ALIAS_GROUPUSER_ID = "成员ID";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_GROUP_ID = "组ID";
	public static final String ALIAS_GROUPUSER_REMARK = "备注";
	public static final String ALIAS_GROUPUSER_DESCRIPTION = "描述";
	public static final String ALIAS_GROUPUSER_ADMIN = "是否群主(1.非群主;2.群主)";
	

    /**
     * 成员ID       db_column: groupuser_id 
     */	
 	//@NotNull(msg="成员ID不能为空")
 	@MaxLength(value=32, msg="字段[成员ID]超出最大长度[32]")
	private java.lang.String groupuserId;
    /**
     * 用户ID       db_column: user_id 
     */	
 	//@NotNull(msg="用户ID不能为空")
 	@MaxLength(value=32, msg="字段[用户ID]超出最大长度[32]")
	private java.lang.String userId;
    /**
     * 组ID       db_column: group_id 
     */	
 	//@NotNull(msg="组ID不能为空")
 	@MaxLength(value=32, msg="字段[组ID]超出最大长度[32]")
	private java.lang.String groupId;
    /**
     * 备注       db_column: groupuser_remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=30, msg="字段[备注]超出最大长度[30]")
	private java.lang.String groupuserRemark;
    /**
     * 描述       db_column: groupuser_description 
     */	
 	//@NotNull(msg="描述不能为空")
 	@MaxLength(value=200, msg="字段[描述]超出最大长度[200]")
	private java.lang.String groupuserDescription;
    /**
     * 是否群主(1.非群主;2.群主)       db_column: groupuser_admin 
     */	
 	//@NotNull(msg="是否群主(1.非群主;2.群主)不能为空")
 	@MaxLength(value=10, msg="字段[是否群主(1.非群主;2.群主)]超出最大长度[10]")
	private java.lang.String groupuserAdmin;
	//columns END

	public TimGroupUser(){
	}

	public TimGroupUser(
		java.lang.String groupuserId
	){
		this.groupuserId = groupuserId;
	}

	public void setGroupuserId(java.lang.String value) {
		this.groupuserId = value;
	}
	public java.lang.String getGroupuserId() {
		return this.groupuserId;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setGroupId(java.lang.String value) {
		this.groupId = value;
	}
	public java.lang.String getGroupId() {
		return this.groupId;
	}
	public void setGroupuserRemark(java.lang.String value) {
		this.groupuserRemark = value;
	}
	public java.lang.String getGroupuserRemark() {
		return this.groupuserRemark;
	}
	public void setGroupuserDescription(java.lang.String value) {
		this.groupuserDescription = value;
	}
	public java.lang.String getGroupuserDescription() {
		return this.groupuserDescription;
	}
	public void setGroupuserAdmin(java.lang.String value) {
		this.groupuserAdmin = value;
	}
	public java.lang.String getGroupuserAdmin() {
		return this.groupuserAdmin;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

