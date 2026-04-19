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

public class TimAttachHis extends BaseDomain<TimAttachHis>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimAttachHis";
	public static final String ALIAS_ATTACH_ID = "附件ID";
	public static final String ALIAS_ACCESS_URL = "访问路径";
	public static final String ALIAS_PATH = "实际路径";
	public static final String ALIAS_ATTACH_TYPE = "类型(2.图片;3.文件;4.音频;5.视频)";
	public static final String ALIAS_MSG_ID = "消息ID(群消息ID或私聊 ID)";
	public static final String ALIAS_ATTACH_SOURCE = "来源(1.群组;2.私聊)";
	public static final String ALIAS_FILE_SIZE = "文件大小";
	public static final String ALIAS_DURATION_TIME = "视频/音频的时长";
	public static final String ALIAS_FILE_SUFFIX = "文件后缀";
	public static final String ALIAS_FILE_NAME = "文件源名称";
	

    /**
     * 附件ID       db_column: attach_id 
     */	
 	//@NotNull(msg="附件ID不能为空")
 	@MaxLength(value=32, msg="字段[附件ID]超出最大长度[32]")
	private java.lang.String attachId;
    /**
     * 访问路径       db_column: access_url 
     */	
 	//@NotNull(msg="访问路径不能为空")
 	@MaxLength(value=200, msg="字段[访问路径]超出最大长度[200]")
	private java.lang.String accessUrl;
    /**
     * 实际路径       db_column: path 
     */	
 	//@NotNull(msg="实际路径不能为空")
 	@MaxLength(value=200, msg="字段[实际路径]超出最大长度[200]")
	private java.lang.String path;
    /**
     * 类型(2.图片;3.文件;4.音频;5.视频)       db_column: attach_type 
     */	
 	//@NotNull(msg="类型(2.图片;3.文件;4.音频;5.视频)不能为空")
 	@MaxLength(value=10, msg="字段[类型(2.图片;3.文件;4.音频;5.视频)]超出最大长度[10]")
	private java.lang.String attachType;
    /**
     * 消息ID(群消息ID或私聊 ID)       db_column: msg_id 
     */	
 	//@NotNull(msg="消息ID(群消息ID或私聊 ID)不能为空")
 	@MaxLength(value=32, msg="字段[消息ID(群消息ID或私聊 ID)]超出最大长度[32]")
	private java.lang.String msgId;
    /**
     * 来源(1.群组;2.私聊)       db_column: attach_source 
     */	
 	//@NotNull(msg="来源(1.群组;2.私聊)不能为空")
 	@MaxLength(value=10, msg="字段[来源(1.群组;2.私聊)]超出最大长度[10]")
	private java.lang.String attachSource;
    /**
     * 文件大小       db_column: file_size 
     */	
 	//@NotNull(msg="文件大小不能为空")
 	@MaxLength(value=10, msg="字段[文件大小]超出最大长度[10]")
	private java.lang.Long fileSize;
    /**
     * 视频/音频的时长       db_column: duration_time 
     */	
 	//@NotNull(msg="视频/音频的时长不能为空")
 	@MaxLength(value=10, msg="字段[视频/音频的时长]超出最大长度[10]")
	private java.lang.Long durationTime;
    /**
     * 文件后缀       db_column: file_suffix 
     */	
 	//@NotNull(msg="文件后缀不能为空")
 	@MaxLength(value=20, msg="字段[文件后缀]超出最大长度[20]")
	private java.lang.String fileSuffix;
    /**
     * 文件源名称       db_column: file_name 
     */	
 	//@NotNull(msg="文件源名称不能为空")
 	@MaxLength(value=255, msg="字段[文件源名称]超出最大长度[255]")
	private java.lang.String fileName;
	//columns END

	public TimAttachHis(){
	}

	public TimAttachHis(
		java.lang.String attachId
	){
		this.attachId = attachId;
	}

	public void setAttachId(java.lang.String value) {
		this.attachId = value;
	}
	public java.lang.String getAttachId() {
		return this.attachId;
	}
	public void setAccessUrl(java.lang.String value) {
		this.accessUrl = value;
	}
	public java.lang.String getAccessUrl() {
		return this.accessUrl;
	}
	public void setPath(java.lang.String value) {
		this.path = value;
	}
	public java.lang.String getPath() {
		return this.path;
	}
	public void setAttachType(java.lang.String value) {
		this.attachType = value;
	}
	public java.lang.String getAttachType() {
		return this.attachType;
	}
	public void setMsgId(java.lang.String value) {
		this.msgId = value;
	}
	public java.lang.String getMsgId() {
		return this.msgId;
	}
	public void setAttachSource(java.lang.String value) {
		this.attachSource = value;
	}
	public java.lang.String getAttachSource() {
		return this.attachSource;
	}
	public void setFileSize(java.lang.Long value) {
		this.fileSize = value;
	}
	public java.lang.Long getFileSize() {
		return this.fileSize;
	}
	public void setDurationTime(java.lang.Long value) {
		this.durationTime = value;
	}
	public java.lang.Long getDurationTime() {
		return this.durationTime;
	}
	public void setFileSuffix(java.lang.String value) {
		this.fileSuffix = value;
	}
	public java.lang.String getFileSuffix() {
		return this.fileSuffix;
	}
	public void setFileName(java.lang.String value) {
		this.fileName = value;
	}
	public java.lang.String getFileName() {
		return this.fileName;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

