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

public class TimGroupState extends BaseDomain<TimGroupState>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimGroupState";
	public static final String ALIAS_STATE_ID = "消息状态ID";
	public static final String ALIAS_MSG_ID = "消息ID";
	public static final String ALIAS_GROUPUSER_ID = "成员ID";
	public static final String ALIAS_READ_TIME = "阅读时间";
	public static final String ALIAS_READ_STATE = "阅读状态(1.未读;2.已读)";
	public static final String ALIAS_USER_ID = "userId";
	

    /**
     * 消息状态ID       db_column: state_id 
     */	
 	//@NotNull(msg="消息状态ID不能为空")
 	@MaxLength(value=32, msg="字段[消息状态ID]超出最大长度[32]")
	private java.lang.String stateId;
    /**
     * 消息ID       db_column: msg_id 
     */	
 	//@NotNull(msg="消息ID不能为空")
 	@MaxLength(value=32, msg="字段[消息ID]超出最大长度[32]")
	private java.lang.String msgId;
    /**
     * 成员ID       db_column: groupuser_id 
     */	
 	//@NotNull(msg="成员ID不能为空")
 	@MaxLength(value=32, msg="字段[成员ID]超出最大长度[32]")
	private java.lang.String groupuserId;
    /**
     * 阅读时间       db_column: read_time 
     */	
 	//@NotNull(msg="阅读时间不能为空")
 	@MaxLength(value=19, msg="字段[阅读时间]超出最大长度[19]")
	private java.util.Date readTime;
    /**
     * 阅读状态(1.未读;2.已读)       db_column: read_state 
     */	
 	//@NotNull(msg="阅读状态(1.未读;2.已读)不能为空")
 	@MaxLength(value=10, msg="字段[阅读状态(1.未读;2.已读)]超出最大长度[10]")
	private java.lang.String readState;
    /**
     * userId       db_column: user_id 
     */	
 	//@NotNull(msg="userId不能为空")
 	@MaxLength(value=32, msg="字段[userId]超出最大长度[32]")
	private java.lang.String userId;
	//columns END

	public TimGroupState(){
	}

	public TimGroupState(
		java.lang.String stateId
	){
		this.stateId = stateId;
	}

	public void setStateId(java.lang.String value) {
		this.stateId = value;
	}
	public java.lang.String getStateId() {
		return this.stateId;
	}
	public void setMsgId(java.lang.String value) {
		this.msgId = value;
	}
	public java.lang.String getMsgId() {
		return this.msgId;
	}
	public void setGroupuserId(java.lang.String value) {
		this.groupuserId = value;
	}
	public java.lang.String getGroupuserId() {
		return this.groupuserId;
	}
	
	public java.util.Date getReadTime() {
		return readTime;
	}

	public void setReadTime(java.util.Date readTime) {
		this.readTime = readTime;
	}

	public void setReadState(java.lang.String value) {
		this.readState = value;
	}
	public java.lang.String getReadState() {
		return this.readState;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	public java.lang.String getUserId() {
		return this.userId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

