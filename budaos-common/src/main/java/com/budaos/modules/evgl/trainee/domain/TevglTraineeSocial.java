package com.budaos.modules.evgl.trainee.domain;

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

public class TevglTraineeSocial extends BaseDomain<TevglTraineeSocial>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTraineeSocial";
	public static final String ALIAS_SOCIAL_ID = "关联主键ID";
	public static final String ALIAS_TRAINEE_ID = "粉丝ID";
	public static final String ALIAS_OPEN_ID = "社交平台开放ID";
	public static final String ALIAS_CHANNEL_ID = "渠道ID(1QQ2新浪微薄3网站微信4小程序)";
	public static final String ALIAS_NICK_NAME = "昵称";
	public static final String ALIAS_PARAM1 = "参数1";
	public static final String ALIAS_PARAM2 = "参数2";
	public static final String ALIAS_PARAM3 = "参数3";
	public static final String ALIAS_REMARK = "备注";
	

    /**
     * 关联主键ID       db_column: social_id 
     */	
 	//@NotNull(msg="关联主键ID不能为空")
 	@MaxLength(value=32, msg="字段[关联主键ID]超出最大长度[32]")
	private java.lang.String socialId;
    /**
     * 粉丝ID       db_column: trainee_id 
     */	
 	//@NotNull(msg="粉丝ID不能为空")
 	@MaxLength(value=32, msg="字段[粉丝ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 社交平台开放ID       db_column: open_id 
     */	
 	//@NotNull(msg="社交平台开放ID不能为空")
 	@MaxLength(value=100, msg="字段[社交平台开放ID]超出最大长度[100]")
	private java.lang.String openId;
    /**
     * 渠道ID(1QQ2新浪微薄3网站微信4小程序)       db_column: channel_id 
     */	
 	//@NotNull(msg="渠道ID(1QQ2新浪微薄3网站微信4小程序)不能为空")
 	@MaxLength(value=2, msg="字段[渠道ID]超出最大长度[2]")
	private java.lang.String channelId;
    /**
     * 昵称       db_column: nick_name 
     */	
 	//@NotNull(msg="昵称不能为空")
 	@MaxLength(value=100, msg="字段[昵称]超出最大长度[100]")
	private java.lang.String nickName;
    /**
     * 参数1       db_column: param1 
     */	
 	//@NotNull(msg="参数1不能为空")
 	@MaxLength(value=100, msg="字段[参数1]超出最大长度[100]")
	private java.lang.String param1;
    /**
     * 参数2       db_column: param2 
     */	
 	//@NotNull(msg="参数2不能为空")
 	@MaxLength(value=100, msg="字段[参数2]超出最大长度[100]")
	private java.lang.String param2;
    /**
     * 参数3       db_column: param3 
     */	
 	//@NotNull(msg="参数3不能为空")
 	@MaxLength(value=100, msg="字段[参数3]超出最大长度[100]")
	private java.lang.String param3;
    /**
     * 备注       db_column: remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=1000, msg="字段[备注]超出最大长度[1000]")
	private java.lang.String remark;
	//columns END

	public TevglTraineeSocial(){
	}

	public TevglTraineeSocial(
		java.lang.String socialId
	){
		this.socialId = socialId;
	}

	public void setSocialId(java.lang.String value) {
		this.socialId = value;
	}
	public java.lang.String getSocialId() {
		return this.socialId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setOpenId(java.lang.String value) {
		this.openId = value;
	}
	public java.lang.String getOpenId() {
		return this.openId;
	}
	public void setChannelId(java.lang.String value) {
		this.channelId = value;
	}
	public java.lang.String getChannelId() {
		return this.channelId;
	}
	public void setNickName(java.lang.String value) {
		this.nickName = value;
	}
	public java.lang.String getNickName() {
		return this.nickName;
	}
	public void setParam1(java.lang.String value) {
		this.param1 = value;
	}
	public java.lang.String getParam1() {
		return this.param1;
	}
	public void setParam2(java.lang.String value) {
		this.param2 = value;
	}
	public java.lang.String getParam2() {
		return this.param2;
	}
	public void setParam3(java.lang.String value) {
		this.param3 = value;
	}
	public java.lang.String getParam3() {
		return this.param3;
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

