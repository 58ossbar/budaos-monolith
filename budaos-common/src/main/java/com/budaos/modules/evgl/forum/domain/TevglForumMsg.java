package com.budaos.modules.evgl.forum.domain;

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

public class TevglForumMsg extends BaseDomain<TevglForumMsg>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglForumMsg";
	public static final String ALIAS_MSG_ID = "主键ID";
	public static final String ALIAS_TO_TRAINEE_ID = "消息接收人";
	public static final String ALIAS_FROM_TRAINEE_ID = "消息推送人";
	public static final String ALIAS_THIRD_TRAINEE_ID = "消息第三者";
	public static final String ALIAS_MSG_TYPE = "消息类型(字典：forum_msg_type)";
	public static final String ALIAS_MSG_TITLE = "消息标题";
	public static final String ALIAS_MSG_CONTENT = "消息内容";
	public static final String ALIAS_MSG_TARGET_ID = "消息目标ID";
	public static final String ALIAS_MSG_IS_READ = "是否已阅读";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 主键ID       db_column: msg_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String msgId;
    /**
     * 消息接收人       db_column: to_trainee_id 
     */	
 	@NotNull(msg="消息接收人不能为空")
 	@MaxLength(value=32, msg="字段[消息接收人]超出最大长度[32]")
	private java.lang.String toTraineeId;
    /**
     * 消息推送人       db_column: from_trainee_id 
     */	
 	@NotNull(msg="消息推送人不能为空")
 	@MaxLength(value=32, msg="字段[消息推送人]超出最大长度[32]")
	private java.lang.String fromTraineeId;
    /**
     * 消息第三者       db_column: third_trainee_id 
     */	
 	@NotNull(msg="消息第三者不能为空")
 	@MaxLength(value=32, msg="字段[消息第三者]超出最大长度[32]")
	private java.lang.String thirdTraineeId;
    /**
     * 消息类型(字典：forum_msg_type)       db_column: msg_type 
     */	
 	@NotNull(msg="消息类型(字典：forum_msg_type)不能为空")
 	@MaxLength(value=2, msg="字段[消息类型(字典：forum_msg_type)]超出最大长度[2]")
	private java.lang.String msgType;
    /**
     * 消息标题       db_column: msg_title 
     */	
 	@NotNull(msg="消息标题不能为空")
 	@MaxLength(value=100, msg="字段[消息标题]超出最大长度[100]")
	private java.lang.String msgTitle;
    /**
     * 消息内容       db_column: msg_content 
     */	
 	@NotNull(msg="消息内容不能为空")
 	@MaxLength(value=2147483647, msg="字段[消息内容]超出最大长度[2147483647]")
	private java.lang.String msgContent;
    /**
     * 消息目标ID       db_column: msg_target_id 
     */	
 	@NotNull(msg="消息目标ID不能为空")
 	@MaxLength(value=32, msg="字段[消息目标ID]超出最大长度[32]")
	private java.lang.String msgTargetId;
    /**
     * 是否已阅读       db_column: msg_is_read 
     */	
 	@NotNull(msg="是否已阅读不能为空")
 	@MaxLength(value=1, msg="字段[是否已阅读]超出最大长度[1]")
	private java.lang.String msgIsRead;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=1, msg="字段[状态(Y有效N无效)]超出最大长度[1]")
	private java.lang.String state;
	//columns END

	public TevglForumMsg(){
	}

	public TevglForumMsg(
		java.lang.String msgId
	){
		this.msgId = msgId;
	}

	public void setMsgId(java.lang.String value) {
		this.msgId = value;
	}
	public java.lang.String getMsgId() {
		return this.msgId;
	}
	public void setToTraineeId(java.lang.String value) {
		this.toTraineeId = value;
	}
	public java.lang.String getToTraineeId() {
		return this.toTraineeId;
	}
	public void setFromTraineeId(java.lang.String value) {
		this.fromTraineeId = value;
	}
	public java.lang.String getFromTraineeId() {
		return this.fromTraineeId;
	}
	public void setThirdTraineeId(java.lang.String value) {
		this.thirdTraineeId = value;
	}
	public java.lang.String getThirdTraineeId() {
		return this.thirdTraineeId;
	}
	public void setMsgType(java.lang.String value) {
		this.msgType = value;
	}
	public java.lang.String getMsgType() {
		return this.msgType;
	}
	public void setMsgTitle(java.lang.String value) {
		this.msgTitle = value;
	}
	public java.lang.String getMsgTitle() {
		return this.msgTitle;
	}
	public void setMsgContent(java.lang.String value) {
		this.msgContent = value;
	}
	public java.lang.String getMsgContent() {
		return this.msgContent;
	}
	public void setMsgTargetId(java.lang.String value) {
		this.msgTargetId = value;
	}
	public java.lang.String getMsgTargetId() {
		return this.msgTargetId;
	}
	public void setMsgIsRead(java.lang.String value) {
		this.msgIsRead = value;
	}
	public java.lang.String getMsgIsRead() {
		return this.msgIsRead;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

