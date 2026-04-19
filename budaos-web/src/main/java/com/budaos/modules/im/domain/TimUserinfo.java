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

public class TimUserinfo extends BaseDomain<TimUserinfo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimUserinfo";
	public static final String ALIAS_USER_ID = "userId";
	public static final String ALIAS_RELATE_ID = "关联ID";
	public static final String ALIAS_RELATE_TYPE = "关联类型";
	public static final String ALIAS_USERNAME = "用户名";
	public static final String ALIAS_PASSWORD = "密码";
	public static final String ALIAS_SALT = "盐";
	public static final String ALIAS_USER_REALNAME = "真实姓名";
	public static final String ALIAS_USERIMG = "用户头像";
	public static final String ALIAS_SEX = "性别";
	public static final String ALIAS_MOBILE = "手机号";
	public static final String ALIAS_USER_CARD = "身份证号码";
	public static final String ALIAS_BIRTHDAY = "出生年月";
	public static final String ALIAS_EMAIL = "邮箱";
	public static final String ALIAS_ADDRESS = "家庭住址";
	public static final String ALIAS_STATUS = "状态  0：禁用   1：正常";
	

    /**
     * userId       db_column: user_id 
     */	
 	//@NotNull(msg="userId不能为空")
 	@MaxLength(value=32, msg="字段[userId]超出最大长度[32]")
	private java.lang.String userId;
    /**
     * 关联ID       db_column: relate_id 
     */	
 	//@NotNull(msg="关联ID不能为空")
 	@MaxLength(value=32, msg="字段[关联ID]超出最大长度[32]")
	private java.lang.String relateId;
    /**
     * 关联类型       db_column: relate_type 
     */	
 	//@NotNull(msg="关联类型不能为空")
 	@MaxLength(value=2, msg="字段[关联类型]超出最大长度[2]")
	private java.lang.String relateType;
    /**
     * 用户名       db_column: username 
     */	
 	//@NotNull(msg="用户名不能为空")
 	@MaxLength(value=50, msg="字段[用户名]超出最大长度[50]")
	private java.lang.String username;
    /**
     * 密码       db_column: password 
     */	
 	//@NotNull(msg="密码不能为空")
 	@MaxLength(value=100, msg="字段[密码]超出最大长度[100]")
	private java.lang.String password;
    /**
     * 盐       db_column: salt 
     */	
 	//@NotNull(msg="盐不能为空")
 	@MaxLength(value=20, msg="字段[盐]超出最大长度[20]")
	private java.lang.String salt;
    /**
     * 真实姓名       db_column: user_realname 
     */	
 	//@NotNull(msg="真实姓名不能为空")
 	@MaxLength(value=50, msg="字段[真实姓名]超出最大长度[50]")
	private java.lang.String userRealname;
    /**
     * 用户头像       db_column: userimg 
     */	
 	//@NotNull(msg="用户头像不能为空")
 	@MaxLength(value=255, msg="字段[用户头像]超出最大长度[255]")
	private java.lang.String userimg;
    /**
     * 性别       db_column: sex 
     */	
 	//@NotNull(msg="性别不能为空")
 	@MaxLength(value=4, msg="字段[性别]超出最大长度[4]")
	private java.lang.String sex;
    /**
     * 手机号       db_column: mobile 
     */	
 	//@NotNull(msg="手机号不能为空")
 	@MaxLength(value=100, msg="字段[手机号]超出最大长度[100]")
	private java.lang.String mobile;
    /**
     * 身份证号码       db_column: user_card 
     */	
 	//@NotNull(msg="身份证号码不能为空")
 	@MaxLength(value=18, msg="字段[身份证号码]超出最大长度[18]")
	private java.lang.String userCard;
    /**
     * 出生年月       db_column: birthday 
     */	
 	//@NotNull(msg="出生年月不能为空")
 	@MaxLength(value=20, msg="字段[出生年月]超出最大长度[20]")
	private java.lang.String birthday;
    /**
     * 邮箱       db_column: email 
     */	
 	//@NotNull(msg="邮箱不能为空")
 	@MaxLength(value=100, msg="字段[邮箱]超出最大长度[100]")
	private java.lang.String email;
    /**
     * 家庭住址       db_column: address 
     */	
 	//@NotNull(msg="家庭住址不能为空")
 	@MaxLength(value=255, msg="字段[家庭住址]超出最大长度[255]")
	private java.lang.String address;
    /**
     * 状态  0：禁用   1：正常       db_column: status 
     */	
 	//@NotNull(msg="状态  0：禁用   1：正常不能为空")
 	@MaxLength(value=3, msg="字段[状态  0：禁用   1：正常]超出最大长度[3]")
	private java.lang.String status;
	//columns END

	public TimUserinfo(){
	}

	public TimUserinfo(
		java.lang.String userId
	){
		this.userId = userId;
	}

	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setRelateId(java.lang.String value) {
		this.relateId = value;
	}
	public java.lang.String getRelateId() {
		return this.relateId;
	}
	public void setRelateType(java.lang.String value) {
		this.relateType = value;
	}
	public java.lang.String getRelateType() {
		return this.relateType;
	}
	public void setUsername(java.lang.String value) {
		this.username = value;
	}
	public java.lang.String getUsername() {
		return this.username;
	}
	public void setPassword(java.lang.String value) {
		this.password = value;
	}
	public java.lang.String getPassword() {
		return this.password;
	}
	public void setSalt(java.lang.String value) {
		this.salt = value;
	}
	public java.lang.String getSalt() {
		return this.salt;
	}
	public void setUserRealname(java.lang.String value) {
		this.userRealname = value;
	}
	public java.lang.String getUserRealname() {
		return this.userRealname;
	}
	public void setUserimg(java.lang.String value) {
		this.userimg = value;
	}
	public java.lang.String getUserimg() {
		return this.userimg;
	}
	public void setSex(java.lang.String value) {
		this.sex = value;
	}
	public java.lang.String getSex() {
		return this.sex;
	}
	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}
	public java.lang.String getMobile() {
		return this.mobile;
	}
	public void setUserCard(java.lang.String value) {
		this.userCard = value;
	}
	public java.lang.String getUserCard() {
		return this.userCard;
	}
	public void setBirthday(java.lang.String value) {
		this.birthday = value;
	}
	public java.lang.String getBirthday() {
		return this.birthday;
	}
	public void setEmail(java.lang.String value) {
		this.email = value;
	}
	public java.lang.String getEmail() {
		return this.email;
	}
	public void setAddress(java.lang.String value) {
		this.address = value;
	}
	public java.lang.String getAddress() {
		return this.address;
	}
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	public java.lang.String getStatus() {
		return this.status;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

