package com.budaos.modules.im.domain;

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

public class TimGroup extends BaseDomain<TimGroup>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimGroup";
	public static final String ALIAS_GROUP_ID = "组ID";
	public static final String ALIAS_GROUP_NOTICE = "公告信息";
	public static final String ALIAS_GROUP_NUM = "成员数量";
	public static final String ALIAS_GROUP_IMG = "群组头像";
	public static final String ALIAS_GROUP_TITLE = "群组名称";
	public static final String ALIAS_CREATE_USER = "创建人";
	public static final String ALIAS_GROUP_CODE = "编号";
	public static final String ALIAS_GROUP_NICKNAME = "昵称";
	public static final String ALIAS_GROUP_INTRODUCTION = "群组简介";
	public static final String ALIAS_GROUP_TYPE = "群组类别";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";

    /**
     * 组ID       db_column: group_id 
     */	
 	//@NotNull(msg="组ID不能为空")
 	@MaxLength(value=32, msg="字段[组ID]超出最大长度[32]")
	private java.lang.String groupId;
    /**
     * 公告信息       db_column: group_notice 
     */	
 	//@NotNull(msg="公告信息不能为空")
 	@MaxLength(value=200, msg="字段[公告信息]超出最大长度[200]")
	private java.lang.String groupNotice;
    /**
     * 成员数量       db_column: group_num 
     */	
 	//@NotNull(msg="成员数量不能为空")
 	@MaxLength(value=10, msg="字段[成员数量]超出最大长度[10]")
	private java.lang.Integer groupNum;
 	/**
     * 群组头像       db_column: group_img 
     */	
 	//@NotNull(msg="群组头像不能为空")
 	@MaxLength(value=65535, msg="字段[群组头像]超出最大长度[65535]")
	private java.lang.Object groupImg;
    /**
     * 群组名称       db_column: group_title 
     */	
 	//@NotNull(msg="群组名称不能为空")
 	@MaxLength(value=50, msg="字段[群组名称]超出最大长度[50]")
	private java.lang.String groupTitle;
    /**
     * 创建人       db_column: create_user 
     */	
 	//@NotNull(msg="创建人不能为空")
 	@MaxLength(value=32, msg="字段[创建人]超出最大长度[32]")
	private java.lang.String createUser;
    /**
     * 编号       db_column: group_code 
     */	
 	//@NotNull(msg="编号不能为空")
 	@MaxLength(value=50, msg="字段[编号]超出最大长度[50]")
	private java.lang.String groupCode;
    /**
     * 昵称       db_column: group_nickname 
     */	
 	//@NotNull(msg="昵称不能为空")
 	@MaxLength(value=50, msg="字段[昵称]超出最大长度[50]")
	private java.lang.String groupNickname;
    /**
     * 群组简介       db_column: group_introduction 
     */	
 	//@NotNull(msg="群组简介不能为空")
 	@MaxLength(value=500, msg="字段[群组简介]超出最大长度[500]")
	private java.lang.String groupIntroduction;
    /**
     * 群组类别       db_column: group_type 
     */	
 	//@NotNull(msg="群组类别不能为空")
 	@MaxLength(value=10, msg="字段[群组类别]超出最大长度[10]")
	private java.lang.String groupType;
 	/**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 教学包主键ID       db_column: pkg_id 
     */	
 	//@NotNull(msg="教学包主键ID不能为空")
 	@MaxLength(value=32, msg="字段[教学包主键ID]超出最大长度[32]")
	private java.lang.String pkgId;
	//columns END

	public TimGroup(){
	}

	public TimGroup(
		java.lang.String groupId
	){
		this.groupId = groupId;
	}

	public void setGroupId(java.lang.String value) {
		this.groupId = value;
	}
	public java.lang.String getGroupId() {
		return this.groupId;
	}
	public void setGroupNotice(java.lang.String value) {
		this.groupNotice = value;
	}
	public java.lang.String getGroupNotice() {
		return this.groupNotice;
	}
	public void setGroupNum(java.lang.Integer value) {
		this.groupNum = value;
	}
	public java.lang.Integer getGroupNum() {
		return this.groupNum;
	}
	public void setGroupTitle(java.lang.String value) {
		this.groupTitle = value;
	}
	public java.lang.String getGroupTitle() {
		return this.groupTitle;
	}
	public void setCreateUser(java.lang.String value) {
		this.createUser = value;
	}
	public java.lang.String getCreateUser() {
		return this.createUser;
	}
	public void setGroupCode(java.lang.String value) {
		this.groupCode = value;
	}
	public java.lang.String getGroupCode() {
		return this.groupCode;
	}
	public void setGroupNickname(java.lang.String value) {
		this.groupNickname = value;
	}
	public java.lang.String getGroupNickname() {
		return this.groupNickname;
	}
	public void setGroupIntroduction(java.lang.String value) {
		this.groupIntroduction = value;
	}
	public java.lang.String getGroupIntroduction() {
		return this.groupIntroduction;
	}
	public void setGroupType(java.lang.String value) {
		this.groupType = value;
	}
	public java.lang.String getGroupType() {
		return this.groupType;
	}
	public java.lang.Object getGroupImg() {
		return groupImg;
	}

	public void setGroupImg(java.lang.Object groupImg) {
		this.groupImg = groupImg;
	}

	public java.lang.String getState() {
		return state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.String getPkgId() {
		return pkgId;
	}

	public void setPkgId(java.lang.String pkgId) {
		this.pkgId = pkgId;
	}
}

