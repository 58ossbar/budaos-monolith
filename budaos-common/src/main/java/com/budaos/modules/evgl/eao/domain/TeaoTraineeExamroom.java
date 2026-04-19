package com.budaos.modules.evgl.eao.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TeaoTraineeExamroom extends BaseDomain<TeaoTraineeExamroom>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TeaoTraineeExamroom";
	public static final String ALIAS_ER_ID = "主键";
	public static final String ALIAS_SUBJECT_ID = "考核课程";
	public static final String ALIAS_ER_ADDR = "考核教室";
	public static final String ALIAS_ER_STIME = "考核开始时间";
	public static final String ALIAS_ER_ETIME = "考核结束时间";
	public static final String ALIAS_STATE = "考核结果(0未开始1正在考核2考核结束))";
	public static final String ALIAS_REMARK = "备注";
	/**
     * 主键       db_column: er_id 
     */	
 	//@NotNull(msg="主键不能为空")
 	@MaxLength(value=32, msg="主键")
	private java.lang.String erId;

    /**
     * 考核课程       db_column: subject_id 
     */	
 	//@NotNull(msg="考核课程不能为空")
 	@MaxLength(value=32, msg="字段[考核课程]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 考核教室       db_column: er_addr 
     */	
 	//@NotNull(msg="考核教室不能为空")
 	@MaxLength(value=100, msg="字段[考核教室]超出最大长度[100]")
	private java.lang.String erAddr;
    /**
     * 考核开始时间       db_column: er_stime 
     */	
 	//@NotNull(msg="考核开始时间不能为空")
 	@MaxLength(value=20, msg="字段[考核开始时间]超出最大长度[20]")
	private java.lang.String erStime;
    /**
     * 考核结束时间       db_column: er_etime 
     */	
 	//@NotNull(msg="考核结束时间不能为空")
 	@MaxLength(value=20, msg="字段[考核结束时间]超出最大长度[20]")
	private java.lang.String erEtime;
    /**
     * 考核结果(0正在考核1考核结束)       db_column: state 
     */	
 	//@NotNull(msg="考核结果(0正在考核1考核结束)不能为空")
 	@MaxLength(value=10, msg="字段[考核结果(0正在考核1考核结束)]超出最大长度[10]")
	private java.lang.String state;
 	/**
     * 备注       db_column: remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=255, msg="字段[备注]超出最大长度[255]")
	private java.lang.String remark;
 	/**
     * 考核班级       db_column: classIds 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=255, msg="字段[考核班级]超出最大长度[330]")
	private java.lang.String classIds;
 	/**
     * 活动标题       db_column: activity_title 
     */	
 	@NotNull(msg="活动标题不能为空")
 	@MaxLength(value=100, msg="字段[活动标题]超出最大长度[100]")
	private java.lang.String activityTitle;
 	/**
     * 参与可获得经验值       db_column: empirical_value 
     */	
 	@NotNull(msg="参与可获得经验值不能为空")
 	@MaxLength(value=10, msg="字段[参与可获得经验值]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
 	/**
     * 用途(来源字典)       db_column: purpose 
     */	
 	//@NotNull(msg="用途(来源字典)不能为空")
 	@MaxLength(value=2, msg="字段[用途(来源字典)]超出最大长度[2]")
	private java.lang.String purpose;
 
 	// 课程名称
 	private java.lang.String subjectName;
 	// 自评成绩
  	private java.lang.String mySelfGiveScore;
  	// 互评成绩
  	private java.lang.String myStudentGiveScore;
  	// 师评成绩
   	private java.lang.String myTeacherGiveScore;
 	// 学员
	private List<Map<String, Object>> studentList;
	
	// 实际活动状态
	private java.lang.String activityStateActual;
	private java.lang.String activityBeginTime;
	private java.lang.String activityEndTime;
	
	//columns END

	public TeaoTraineeExamroom(){
	}

	public java.lang.String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}

	public java.lang.String getMySelfGiveScore() {
		return mySelfGiveScore;
	}

	public void setMySelfGiveScore(java.lang.String mySelfGiveScore) {
		this.mySelfGiveScore = mySelfGiveScore;
	}

	public java.lang.String getMyStudentGiveScore() {
		return myStudentGiveScore;
	}

	public void setMyStudentGiveScore(java.lang.String myStudentGiveScore) {
		this.myStudentGiveScore = myStudentGiveScore;
	}

	public java.lang.String getMyTeacherGiveScore() {
		return myTeacherGiveScore;
	}

	public void setMyTeacherGiveScore(java.lang.String myTeacherGiveScore) {
		this.myTeacherGiveScore = myTeacherGiveScore;
	}

	public TeaoTraineeExamroom(
		java.lang.String erId
	){
		this.erId = erId;
	}

	public void setErId(java.lang.String value) {
		this.erId = value;
	}
	public java.lang.String getErId() {
		return this.erId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setErAddr(java.lang.String value) {
		this.erAddr = value;
	}
	public java.lang.String getErAddr() {
		return this.erAddr;
	}
	public void setErStime(java.lang.String value) {
		this.erStime = value;
	}
	public java.lang.String getErStime() {
		return this.erStime;
	}
	public void setErEtime(java.lang.String value) {
		this.erEtime = value;
	}
	public java.lang.String getErEtime() {
		return this.erEtime;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	public java.lang.String getClassIds() {
		return classIds;
	}
	public void setClassIds(java.lang.String classIds) {
		this.classIds = classIds;
	}

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

	public java.lang.String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(java.lang.String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public java.lang.Integer getEmpiricalValue() {
		return empiricalValue;
	}

	public void setEmpiricalValue(java.lang.Integer empiricalValue) {
		this.empiricalValue = empiricalValue;
	}

	public java.lang.String getPurpose() {
		return purpose;
	}

	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}

	public List<Map<String, Object>> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Map<String, Object>> studentList) {
		this.studentList = studentList;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

