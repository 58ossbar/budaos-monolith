package com.budaos.modules.im.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TimPrivateMsgHis extends BaseDomain<TimPrivateMsgHis>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimPrivateMsgHis";
	public static final String ALIAS_MSG_ID = "消息ID";
	public static final String ALIAS_RECEIVE_ID = "接收者ID";
	public static final String ALIAS_READ_STATE = "阅读状态(1.未读;2.已读)";
	public static final String ALIAS_READ_TIME = "阅读时间";
	public static final String ALIAS_SEND_ID = "发送者ID";
	public static final String ALIAS_SEND_TIME = "发送时间";
	public static final String ALIAS_MSG_CONTENT = "消息内容";
	public static final String ALIAS_MSG_TYPE = "消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)";
	public static final String ALIAS_MSG_SEQ = "消息序列,时间戳+2位序号";
	

    /**
     * 消息ID       db_column: msg_id 
     */	
 	//@NotNull(msg="消息ID不能为空")
 	@MaxLength(value=32, msg="字段[消息ID]超出最大长度[32]")
	private java.lang.String msgId;
    /**
     * 接收者ID       db_column: receive_id 
     */	
 	//@NotNull(msg="接收者ID不能为空")
 	@MaxLength(value=32, msg="字段[接收者ID]超出最大长度[32]")
	private java.lang.String receiveId;
    /**
     * 阅读状态(1.未读;2.已读)       db_column: read_state 
     */	
 	//@NotNull(msg="阅读状态(1.未读;2.已读)不能为空")
 	@MaxLength(value=10, msg="字段[阅读状态(1.未读;2.已读)]超出最大长度[10]")
	private java.lang.String readState;
    /**
     * 阅读时间       db_column: read_time 
     */	
 	//@NotNull(msg="阅读时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
 	@MaxLength(value=19, msg="字段[阅读时间]超出最大长度[20]")
	private java.util.Date readTime;
    /**
     * 发送者ID       db_column: send_id 
     */	
 	//@NotNull(msg="发送者ID不能为空")
 	@MaxLength(value=32, msg="字段[发送者ID]超出最大长度[32]")
	private java.lang.String sendId;
    /**
     * 发送时间       db_column: send_time 
     */	
 	//@NotNull(msg="发送时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
 	@MaxLength(value=19, msg="字段[发送时间]超出最大长度[20]")
	private java.util.Date sendTime;
    /**
     * 消息内容       db_column: msg_content 
     */	
 	//@NotNull(msg="消息内容不能为空")
 	@MaxLength(value=1000, msg="字段[消息内容]超出最大长度[1000]")
	private java.lang.String msgContent;
    /**
     * 消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)       db_column: msg_type 
     */	
 	//@NotNull(msg="消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)不能为空")
 	@MaxLength(value=10, msg="字段[消息类型(1.文本;2.图片;3.文件;4.音频;5.视频)]超出最大长度[10]")
	private java.lang.String msgType;
    /**
     * 消息序列,时间戳+2位序号       db_column: msg_seq 
     */	
 	//@NotNull(msg="消息序列,时间戳+2位序号不能为空")
 	@MaxLength(value=50, msg="字段[消息序列,时间戳+2位序号]超出最大长度[50]")
	private java.lang.String msgSeq;
	//columns END

	public TimPrivateMsgHis(){
	}

	public TimPrivateMsgHis(
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
	public void setReceiveId(java.lang.String value) {
		this.receiveId = value;
	}
	public java.lang.String getReceiveId() {
		return this.receiveId;
	}
	public void setReadState(java.lang.String value) {
		this.readState = value;
	}
	public java.lang.String getReadState() {
		return this.readState;
	}
	public void setReadTime(java.util.Date value) {
		this.readTime = value;
	}
	public java.util.Date getReadTime() {
		return this.readTime;
	}
	public void setSendId(java.lang.String value) {
		this.sendId = value;
	}
	public java.lang.String getSendId() {
		return this.sendId;
	}
	public void setSendTime(java.util.Date value) {
		this.sendTime = value;
	}
	public java.util.Date getSendTime() {
		return this.sendTime;
	}
	public void setMsgContent(java.lang.String value) {
		this.msgContent = value;
	}
	public java.lang.String getMsgContent() {
		return this.msgContent;
	}
	public void setMsgType(java.lang.String value) {
		this.msgType = value;
	}
	public java.lang.String getMsgType() {
		return this.msgType;
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

