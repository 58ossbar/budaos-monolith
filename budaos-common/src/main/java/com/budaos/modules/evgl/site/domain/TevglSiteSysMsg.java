package com.budaos.modules.evgl.site.domain;

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

public class TevglSiteSysMsg extends BaseDomain<TevglSiteSysMsg>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteSysMsg";
	public static final String ALIAS_MSG_ID = "主键id";
	public static final String ALIAS_TO_TRAINEE_ID = "接收者";
	public static final String ALIAS_MSG_TITLE = "标题";
	public static final String ALIAS_MSG_CONTENT = "内容";
	public static final String ALIAS_MSG_TYPE = "消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到";
	public static final String ALIAS_MSG_PIC = "图片（存值从/uploads开始存）";
	public static final String ALIAS_MSG_FILE = "文件（存值从/uploads开始存）";
	public static final String ALIAS_READ_STATE = "是否已读(Y已读N未读)";
	public static final String ALIAS_PARAMS1 = "参数1，当msg_type为01课堂时，该字段存的是课堂主键";
	public static final String ALIAS_PARAMS2 = "参数2，当msg_type为1至8时，该字段存的是活动主键";
	public static final String ALIAS_PARAMS3 = "参数3，预留字段";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_PKGID = "教学包id";
	

    /**
     * 主键id       db_column: msg_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String msgId;
    /**
     * 接收者       db_column: to_trainee_id 
     */	
 	@NotNull(msg="接收者不能为空")
 	@MaxLength(value=32, msg="字段[接收者]超出最大长度[32]")
	private java.lang.String toTraineeId;
    /**
     * 标题       db_column: msg_title 
     */	
 	@NotNull(msg="标题不能为空")
 	@MaxLength(value=200, msg="字段[标题]超出最大长度[200]")
	private java.lang.String msgTitle;
    /**
     * 内容       db_column: msg_content 
     */	
 	@NotNull(msg="内容不能为空")
 	@MaxLength(value=500, msg="字段[内容]超出最大长度[500]")
	private java.lang.String msgContent;
    /**
     * 消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到       db_column: msg_type 
     */	
 	@NotNull(msg="消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到不能为空")
 	@MaxLength(value=2, msg="字段[消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到]超出最大长度[2]")
	private java.lang.String msgType;
    /**
     * 图片（存值从/uploads开始存）       db_column: msg_pic 
     */	
 	@NotNull(msg="图片（存值从/uploads开始存）不能为空")
 	@MaxLength(value=500, msg="字段[图片（存值从/uploads开始存）]超出最大长度[500]")
	private java.lang.String msgPic;
    /**
     * 文件（存值从/uploads开始存）       db_column: msg_file 
     */	
 	@NotNull(msg="文件（存值从/uploads开始存）不能为空")
 	@MaxLength(value=500, msg="字段[文件（存值从/uploads开始存）]超出最大长度[500]")
	private java.lang.String msgFile;
    /**
     * 是否已读(Y已读N未读)       db_column: read_state 
     */	
 	@NotNull(msg="是否已读(Y已读N未读)不能为空")
 	@MaxLength(value=2, msg="字段[是否已读(Y已读N未读)]超出最大长度[2]")
	private java.lang.String readState;
    /**
     * 参数1，当msg_type为01课堂时，该字段存的是课堂主键       db_column: params1 
     */	
 	@NotNull(msg="参数1，当msg_type为01课堂时，该字段存的是课堂主键不能为空")
 	@MaxLength(value=32, msg="字段[参数1，当msg_type为01课堂时，该字段存的是课堂主键]超出最大长度[32]")
	private java.lang.String params1;
    /**
     * 参数2，当msg_type为1至8时，该字段存的是活动主键       db_column: params2 
     */	
 	@NotNull(msg="参数2，当msg_type为1至8时，该字段存的是活动主键不能为空")
 	@MaxLength(value=32, msg="字段[参数2，当msg_type为1至8时，该字段存的是活动主键]超出最大长度[32]")
	private java.lang.String params2;
    /**
     * 参数3，预留字段       db_column: params3 
     */	
 	@NotNull(msg="参数3，预留字段不能为空")
 	@MaxLength(value=32, msg="字段[参数3，预留字段]超出最大长度[32]")
	private java.lang.String params3;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 教学包ID       db_column: pkg_id 
     */	
 	//@NotNull(msg="教学包ID 不能为空")
 	@MaxLength(value=32, msg="字段[教学包ID]超出最大长度[32]")
	private java.lang.String pkgId;
	//columns END

	public TevglSiteSysMsg(){
	}

	public TevglSiteSysMsg(
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
	public void setMsgType(java.lang.String value) {
		this.msgType = value;
	}
	public java.lang.String getMsgType() {
		return this.msgType;
	}
	public void setMsgPic(java.lang.String value) {
		this.msgPic = value;
	}
	public java.lang.String getMsgPic() {
		return this.msgPic;
	}
	public void setMsgFile(java.lang.String value) {
		this.msgFile = value;
	}
	public java.lang.String getMsgFile() {
		return this.msgFile;
	}
	public void setReadState(java.lang.String value) {
		this.readState = value;
	}
	public java.lang.String getReadState() {
		return this.readState;
	}
	public void setParams1(java.lang.String value) {
		this.params1 = value;
	}
	public java.lang.String getParams1() {
		return this.params1;
	}
	public void setParams2(java.lang.String value) {
		this.params2 = value;
	}
	public java.lang.String getParams2() {
		return this.params2;
	}
	public void setParams3(java.lang.String value) {
		this.params3 = value;
	}
	public java.lang.String getParams3() {
		return this.params3;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public java.lang.String getPkgId() {
		return pkgId;
	}

	public void setPkgId(java.lang.String pkgId) {
		this.pkgId = pkgId;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

