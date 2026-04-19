package com.budaos.modules.evgl.activity.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 活动之头脑风暴---学员作答信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglActivityBrainstormingTraineeAnswer extends BaseDomain<TevglActivityBrainstormingTraineeAnswer>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityBrainstormingTraineeAnswer";
	public static final String ALIAS_AN_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_ACTIVITY_ID = "活动id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_CONTENT = "作答内容";
	public static final String ALIAS_FILE_IMAGE = "图片附件";
	public static final String ALIAS_FILE_VIDEO = "视频附件";
	public static final String ALIAS_FILE_OTHER = "其它附件";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 主键id       db_column: an_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String anId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 活动id       db_column: activity_id 
     */	
 	@NotNull(msg="活动id不能为空")
 	@MaxLength(value=32, msg="字段[活动id]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 作答内容       db_column: content 
     */	
 	@NotNull(msg="作答内容不能为空")
 	@MaxLength(value=65535, msg="字段[作答内容]超出最大长度[65535]")
	private java.lang.String content;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 点赞数       db_column: like_num 
     */	
 	//@NotNull(msg="点赞数不能为空")
 	@MaxLength(value=10, msg="字段[点赞数]超出最大长度[10]")
	private java.lang.Integer likeNum;
	//columns END

	public TevglActivityBrainstormingTraineeAnswer(){
	}

	public TevglActivityBrainstormingTraineeAnswer(
		java.lang.String anId
	){
		this.anId = anId;
	}

	public void setAnId(java.lang.String value) {
		this.anId = value;
	}
	public java.lang.String getAnId() {
		return this.anId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	
	private TevglActivityBrainstorming tevglActivityBrainstorming;
	
	public void setTevglActivityBrainstorming(TevglActivityBrainstorming tevglActivityBrainstorming){
		this.tevglActivityBrainstorming = tevglActivityBrainstorming;
	}
	
	public TevglActivityBrainstorming getTevglActivityBrainstorming() {
		return tevglActivityBrainstorming;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(java.lang.Integer likeNum) {
		this.likeNum = likeNum;
	}
}

