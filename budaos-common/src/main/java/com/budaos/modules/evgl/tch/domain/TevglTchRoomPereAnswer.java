package com.budaos.modules.evgl.tch.domain;

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

public class TevglTchRoomPereAnswer extends BaseDomain<TevglTchRoomPereAnswer>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchRoomPereAnswer";
	public static final String ALIAS_ANSWER_ID = "answerId";
	public static final String ALIAS_ANSWER_NUM = "answerNum";
	public static final String ALIAS_CT_ID = "ctId";
	public static final String ALIAS_EMPIRICAL_VALUE = "empiricalValue";
	public static final String ALIAS_STATE = "state";
	

    /**
     * answerId       db_column: answer_id 
     */	
 	@NotNull(msg="answerId不能为空")
 	@MaxLength(value=32, msg="字段[answerId]超出最大长度[32]")
	private java.lang.String answerId;
 	/**
     * answerTitle       db_column: answer_title 
     */	
 	@NotNull(msg="answerTitle不能为空")
 	@MaxLength(value=32, msg="字段[answerTitle]超出最大长度[32]")
	private java.lang.String answerTitle;
    /**
     * answerNum       db_column: answer_num 
     */	
 	@NotNull(msg="answerNum不能为空")
 	@MaxLength(value=10, msg="字段[answerNum]超出最大长度[10]")
	private java.lang.Integer answerNum;
    /**
     * ctId       db_column: ct_id 
     */	
 	@NotNull(msg="ctId不能为空")
 	@MaxLength(value=32, msg="字段[ctId]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * empiricalValue       db_column: empirical_value 
     */	
 	@NotNull(msg="empiricalValue不能为空")
 	@MaxLength(value=10, msg="字段[empiricalValue]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
    /**
     * state       db_column: state 
     */	
 	@NotNull(msg="state不能为空")
 	@MaxLength(value=2, msg="字段[state]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 教学包id       db_column: pkg_id 
     */	
 	@NotNull(msg="教学包id不能为空")
 	@MaxLength(value=32, msg="字段[教学包id]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * 分组id       db_column: resgroup_id 
     */	
 	@NotNull(msg="分组id不能为空")
 	@MaxLength(value=32, msg="字段[分组id]超出最大长度[32]")
	private java.lang.String resgroupId;
    /**
     * 类型：1 抢答、2 随机选人、3 手动选人       db_column: type 
     */	
 	@NotNull(msg="类型：1 抢答、2 随机选人、3 手动选人不能为空")
 	@MaxLength(value=32, msg="字段[类型：1 抢答、2 随机选人、3 手动选人]超出最大长度[32]")
	private java.lang.Integer type;
	//columns END
 	
 	private java.lang.String activityStateActual; // 实际活动状态
 	private java.lang.String activityBeginTime; // 实际活动开始时间
 	private java.lang.String activityEndTime; // 实际活动结束时间

	public java.lang.String getActivityStateActual() {
		return activityStateActual;
	}

	public void setActivityStateActual(java.lang.String activityStateActual) {
		this.activityStateActual = activityStateActual;
	}

	public java.lang.String getActivityBeginTime() {
		return activityBeginTime;
	}

	public void setActivityBeginTime(java.lang.String activityBeginTime) {
		this.activityBeginTime = activityBeginTime;
	}

	public java.lang.String getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(java.lang.String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public TevglTchRoomPereAnswer(){
	}

	public TevglTchRoomPereAnswer(
		java.lang.String answerId
	){
		this.answerId = answerId;
	}

	public void setAnswerId(java.lang.String value) {
		this.answerId = value;
	}
	public java.lang.String getAnswerId() {
		return this.answerId;
	}
	
	public java.lang.String getAnswerTitle() {
		return answerTitle;
	}

	public void setAnswerTitle(java.lang.String answerTitle) {
		this.answerTitle = answerTitle;
	}

	public void setAnswerNum(java.lang.Integer value) {
		this.answerNum = value;
	}
	public java.lang.Integer getAnswerNum() {
		return this.answerNum;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setEmpiricalValue(java.lang.Integer value) {
		this.empiricalValue = value;
	}
	public java.lang.Integer getEmpiricalValue() {
		return this.empiricalValue;
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

	public java.lang.String getResgroupId() {
		return resgroupId;
	}

	public void setResgroupId(java.lang.String resgroupId) {
		this.resgroupId = resgroupId;
	}
	
	public java.lang.Integer getType() {
		return type;
	}

	public void setType(java.lang.Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

