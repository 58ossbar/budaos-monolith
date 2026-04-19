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

public class TevglForumAttention extends BaseDomain<TevglForumAttention>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglForumAttention";
	public static final String ALIAS_ATTENTION_ID = "主键ID";
	public static final String ALIAS_TRAINEE_ID = "用户ID";
	public static final String ALIAS_FOLLOW_ID = "粉丝ID";
	public static final String ALIAS_ATTENTION_TYPE = "所属类型(1社区2博客)";
	public static final String ALIAS_STATE = "状态(Y关注N取消关注)";
	public static final String ALIAS_ATTENTION_TIME = "关注时间";
	public static final String ALIAS_CANCEL_TIME = "取消关注时间";
	

    /**
     * 主键ID       db_column: attention_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String attentionId;
    /**
     * 用户ID       db_column: trainee_id 
     */	
 	@NotNull(msg="用户ID不能为空")
 	@MaxLength(value=32, msg="字段[用户ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 粉丝ID       db_column: follow_id 
     */	
 	@NotNull(msg="粉丝ID不能为空")
 	@MaxLength(value=32, msg="字段[粉丝ID]超出最大长度[32]")
	private java.lang.String followId;
    /**
     * 所属类型(1社区2博客)       db_column: attention_type 
     */	
 	@NotNull(msg="所属类型(1社区2博客)不能为空")
 	@MaxLength(value=5, msg="字段[所属类型(1社区2博客)]超出最大长度[5]")
	private java.lang.String attentionType;
    /**
     * 状态(Y关注N取消关注)       db_column: state 
     */	
 	@NotNull(msg="状态(Y关注N取消关注)不能为空")
 	@MaxLength(value=1, msg="字段[状态(Y关注N取消关注)]超出最大长度[1]")
	private java.lang.String state;
    /**
     * 关注时间       db_column: attention_time 
     */	
 	@NotNull(msg="关注时间不能为空")
 	@MaxLength(value=20, msg="字段[关注时间]超出最大长度[20]")
	private java.lang.String attentionTime;
    /**
     * 取消关注时间       db_column: cancel_time 
     */	
 	@NotNull(msg="取消关注时间不能为空")
 	@MaxLength(value=20, msg="字段[取消关注时间]超出最大长度[20]")
	private java.lang.String cancelTime;
	//columns END

	public TevglForumAttention(){
	}

	public TevglForumAttention(
		java.lang.String attentionId
	){
		this.attentionId = attentionId;
	}

	public void setAttentionId(java.lang.String value) {
		this.attentionId = value;
	}
	public java.lang.String getAttentionId() {
		return this.attentionId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setFollowId(java.lang.String value) {
		this.followId = value;
	}
	public java.lang.String getFollowId() {
		return this.followId;
	}
	public void setAttentionType(java.lang.String value) {
		this.attentionType = value;
	}
	public java.lang.String getAttentionType() {
		return this.attentionType;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setAttentionTime(java.lang.String value) {
		this.attentionTime = value;
	}
	public java.lang.String getAttentionTime() {
		return this.attentionTime;
	}
	public void setCancelTime(java.lang.String value) {
		this.cancelTime = value;
	}
	public java.lang.String getCancelTime() {
		return this.cancelTime;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

