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

public class TevglTchRoomPereTraineeAnswer extends BaseDomain<TevglTchRoomPereTraineeAnswer>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchRoomPereTraineeAnswer";
	public static final String ALIAS_TRAINEE_ANSWER_ID = "学员抢答id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_ANSER_ID = "抢答id";
	public static final String ALIAS_TRAINEE_EMPIRICAL_VALUE = "经验值";
	public static final String ALIAS_SORT_NUM = "排序号";
	

    /**
     * 学员抢答id       db_column: trainee_answer_id 
     */	
 	@NotNull(msg="学员抢答id不能为空")
 	@MaxLength(value=32, msg="字段[学员抢答id]超出最大长度[32]")
	private java.lang.String traineeAnswerId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 抢答id       db_column: anser_id 
     */	
 	@NotNull(msg="抢答id不能为空")
 	@MaxLength(value=32, msg="字段[抢答id]超出最大长度[32]")
	private java.lang.String anserId;
    /**
     * 经验值       db_column: trainee_empirical_value 
     */	
 	@NotNull(msg="经验值不能为空")
 	@MaxLength(value=10, msg="字段[经验值]超出最大长度[10]")
	private java.lang.Integer traineeEmpiricalValue;
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
	//columns END
 	
 	private java.lang.Integer empiricalValue;

	public TevglTchRoomPereTraineeAnswer(){
	}

	public TevglTchRoomPereTraineeAnswer(
		java.lang.String traineeAnswerId
	){
		this.traineeAnswerId = traineeAnswerId;
	}

	public void setTraineeAnswerId(java.lang.String value) {
		this.traineeAnswerId = value;
	}
	public java.lang.String getTraineeAnswerId() {
		return this.traineeAnswerId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setAnserId(java.lang.String value) {
		this.anserId = value;
	}
	public java.lang.String getAnserId() {
		return this.anserId;
	}
	public void setTraineeEmpiricalValue(java.lang.Integer value) {
		this.traineeEmpiricalValue = value;
	}
	public java.lang.Integer getTraineeEmpiricalValue() {
		return this.traineeEmpiricalValue;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public java.lang.Integer getEmpiricalValue() {
		return empiricalValue;
	}

	public void setEmpiricalValue(java.lang.Integer empiricalValue) {
		this.empiricalValue = empiricalValue;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

