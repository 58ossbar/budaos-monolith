package com.budaos.modules.evgl.trainee.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 学员经验获取记录日志表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglTraineeEmpiricalValueLog extends BaseDomain<TevglTraineeEmpiricalValueLog>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTraineeEmpiricalValueLog";
	public static final String ALIAS_EV_ID = "主键ID";
	public static final String ALIAS_TYPE = "获取方式(来源字典1投票问卷2头脑风暴3答疑讨论4测试活动5作业6作业/任务7课堂表现)";
	public static final String ALIAS_TRAINEE_ID = "学员ID";
	public static final String ALIAS_CT_ID = "课堂ID";
	public static final String ALIAS_ACTIVITY_ID = "活动ID";
	public static final String ALIAS_EMPOROCAL_VALUE = "经验值";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_PARAMS1 = "参数1";
	public static final String ALIAS_PARAMS2 = "参数2";
	public static final String ALIAS_PARAMS3 = "参数3";
	public static final String ALIAS_MSG = "用于显示文本";
	

    /**
     * 主键ID       db_column: ev_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String evId;
    /**
     * 获取方式(来源字典1投票问卷2头脑风暴3答疑讨论4测试活动5作业6作业/任务7课堂表现)       db_column: type 
     */	
 	@NotNull(msg="获取方式(来源字典1投票问卷2头脑风暴3答疑讨论4测试活动5作业6作业/任务7课堂表现)不能为空")
 	@MaxLength(value=2, msg="字段[获取方式(来源字典1投票问卷2头脑风暴3答疑讨论4测试活动5作业6作业/任务7课堂表现)]超出最大长度[2]")
	private java.lang.String type;
    /**
     * 学员ID       db_column: trainee_id 
     */	
 	@NotNull(msg="学员ID不能为空")
 	@MaxLength(value=32, msg="字段[学员ID]超出最大长度[32]")
	private java.lang.String traineeId;
 	/**
     * 课堂ID       db_column: ct_id 
     */	
 	@NotNull(msg="课堂ID不能为空")
 	@MaxLength(value=32, msg="字段[课堂ID]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 活动ID       db_column: activity_id 
     */	
 	@NotNull(msg="活动ID不能为空")
 	@MaxLength(value=32, msg="字段[活动ID]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 经验值       db_column: empiric_value 
     */	
 	@NotNull(msg="经验值不能为空")
 	@MaxLength(value=10, msg="字段[经验值]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 参数1       db_column: params1 
     */	
 	@NotNull(msg="参数1不能为空")
 	@MaxLength(value=32, msg="字段[参数1]超出最大长度[32]")
	private java.lang.String params1;
    /**
     * 参数2       db_column: params2 
     */	
 	@NotNull(msg="参数2不能为空")
 	@MaxLength(value=32, msg="字段[参数2]超出最大长度[32]")
	private java.lang.String params2;
    /**
     * 参数3       db_column: params3 
     */	
 	@NotNull(msg="参数3不能为空")
 	@MaxLength(value=32, msg="字段[参数3]超出最大长度[32]")
	private java.lang.String params3;
 	/**
     * 用于显示文本       db_column: msg 
     */	
 	//@NotNull(msg="用于显示文本不能为空")
 	@MaxLength(value=1000, msg="字段[用于显示文本]超出最大长度[1000]")
	private java.lang.String msg;
 	
 	private java.lang.String params17;
	//columns END

	public TevglTraineeEmpiricalValueLog(){
	}

	public void setEvId(java.lang.String value) {
		this.evId = value;
	}
	public java.lang.String getEvId() {
		return this.evId;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public java.lang.String getCtId() {
		return ctId;
	}
	public void setCtId(java.lang.String ctId) {
		this.ctId = ctId;
	}
	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public java.lang.Integer getEmpiricalValue() {
		return empiricalValue;
	}
	public void setEmpiricalValue(java.lang.Integer empiricalValue) {
		this.empiricalValue = empiricalValue;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
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
	public java.lang.String getMsg() {
		return msg;
	}
	public void setMsg(java.lang.String msg) {
		this.msg = msg;
	}
	public java.lang.String getParams17() {
		return params17;
	}
	public void setParams17(java.lang.String params17) {
		this.params17 = params17;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

