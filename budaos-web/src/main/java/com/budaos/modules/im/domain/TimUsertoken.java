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

public class TimUsertoken extends BaseDomain<TimUsertoken>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimUsertoken";
	public static final String ALIAS_UT_ID = "主键ID";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_CHANNEL = "授权渠道";
	public static final String ALIAS_TOKEN = "授权码";
	

    /**
     * 主键ID       db_column: ut_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String utId;
    /**
     * 用户ID       db_column: user_id 
     */	
 	@NotNull(msg="用户ID不能为空")
 	@MaxLength(value=32, msg="字段[用户ID]超出最大长度[32]")
	private java.lang.String userId;
    /**
     * 授权渠道       db_column: channel 
     */	
 	@NotNull(msg="授权渠道不能为空")
 	@MaxLength(value=10, msg="字段[授权渠道]超出最大长度[10]")
	private java.lang.String channel;
    /**
     * 授权码       db_column: token 
     */	
 	@NotNull(msg="授权码不能为空")
 	@MaxLength(value=100, msg="字段[授权码]超出最大长度[100]")
	private java.lang.String token;
	//columns END

	public TimUsertoken(){
	}

	public TimUsertoken(
		java.lang.String utId
	){
		this.utId = utId;
	}

	public void setUtId(java.lang.String value) {
		this.utId = value;
	}
	public java.lang.String getUtId() {
		return this.utId;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setChannel(java.lang.String value) {
		this.channel = value;
	}
	public java.lang.String getChannel() {
		return this.channel;
	}
	public void setToken(java.lang.String value) {
		this.token = value;
	}
	public java.lang.String getToken() {
		return this.token;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

