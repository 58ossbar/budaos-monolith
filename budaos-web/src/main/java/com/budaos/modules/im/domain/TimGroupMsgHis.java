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

public class TimGroupMsgHis extends BaseDomain<TimGroupMsgHis>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimGroupMsgHis";
	public static final String ALIAS_MSG_ID = "消息ID";
	public static final String ALIAS_GROUPUSER_ID = "发送人";
	public static final String ALIAS_MSG_CONTENT = "消息内容";
	public static final String ALIAS_MSG_SEND_TIME = "发送时间";
	public static final String ALIAS_MSG_TYPE = "消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)";
	public static final String ALIAS_UNREAD_NUM = "应阅读人数";
	public static final String ALIAS_READ_NUM = "实际已阅读人数";
	public static final String ALIAS_MSG_SEQ = "消息序列,时间戳+2位序号";
	

    /**
     * 消息ID       db_column: msg_id 
     */	
 	//@NotNull(msg="消息ID不能为空")
 	@MaxLength(value=32, msg="字段[消息ID]超出最大长度[32]")
	private java.lang.String msgId;
    /**
     * 发送人       db_column: groupuser_id 
     */	
 	//@NotNull(msg="发送人不能为空")
 	@MaxLength(value=32, msg="字段[发送人]超出最大长度[32]")
	private java.lang.String groupuserId;
    /**
     * 消息内容       db_column: msg_content 
     */	
 	//@NotNull(msg="消息内容不能为空")
 	@MaxLength(value=1000, msg="字段[消息内容]超出最大长度[1000]")
	private java.lang.String msgContent;
    /**
     * 发送时间       db_column: msg_send_time 
     */	
 	//@NotNull(msg="发送时间不能为空")
 	@MaxLength(value=19, msg="字段[发送时间]超出最大长度[19]")
	private java.sql.Timestamp msgSendTime;
    /**
     * 消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)       db_column: msg_type 
     */	
 	//@NotNull(msg="消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)不能为空")
 	@MaxLength(value=10, msg="字段[消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)]超出最大长度[10]")
	private java.lang.String msgType;
    /**
     * 应阅读人数       db_column: unread_num 
     */	
 	//@NotNull(msg="应阅读人数不能为空")
 	@MaxLength(value=10, msg="字段[应阅读人数]超出最大长度[10]")
	private java.lang.Integer unreadNum;
    /**
     * 实际已阅读人数       db_column: read_num 
     */	
 	//@NotNull(msg="实际已阅读人数不能为空")
 	@MaxLength(value=10, msg="字段[实际已阅读人数]超出最大长度[10]")
	private java.lang.Integer readNum;
    /**
     * 消息序列,时间戳+2位序号       db_column: msg_seq 
     */	
 	//@NotNull(msg="消息序列,时间戳+2位序号不能为空")
 	@MaxLength(value=50, msg="字段[消息序列,时间戳+2位序号]超出最大长度[50]")
	private java.lang.String msgSeq;
	//columns END

	public TimGroupMsgHis(){
	}

	public TimGroupMsgHis(
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
	public void setGroupuserId(java.lang.String value) {
		this.groupuserId = value;
	}
	public java.lang.String getGroupuserId() {
		return this.groupuserId;
	}
	public void setMsgContent(java.lang.String value) {
		this.msgContent = value;
	}
	public java.lang.String getMsgContent() {
		return this.msgContent;
	}
	public void setMsgSendTime(java.sql.Timestamp value) {
		this.msgSendTime = value;
	}
	public java.sql.Timestamp getMsgSendTime() {
		return this.msgSendTime;
	}
	public void setMsgType(java.lang.String value) {
		this.msgType = value;
	}
	public java.lang.String getMsgType() {
		return this.msgType;
	}
	public void setUnreadNum(java.lang.Integer value) {
		this.unreadNum = value;
	}
	public java.lang.Integer getUnreadNum() {
		return this.unreadNum;
	}
	public void setReadNum(java.lang.Integer value) {
		this.readNum = value;
	}
	public java.lang.Integer getReadNum() {
		return this.readNum;
	}
	public void setMsgSeq(java.lang.String value) {
		this.msgSeq = value;
	}
	public java.lang.String getMsgSeq() {
		return this.msgSeq;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

