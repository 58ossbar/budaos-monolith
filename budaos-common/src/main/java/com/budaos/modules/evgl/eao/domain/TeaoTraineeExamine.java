package com.budaos.modules.evgl.eao.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;
import com.budaos.modules.evgl.book.domain.TevglBookRegular;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;

import java.util.List;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2018 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TeaoTraineeExamine extends BaseDomain<TeaoTraineeExamine>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TeaoTraineeExamine";
	public static final String ALIAS_EXAMINE_ID = "examineId";
	public static final String ALIAS_TRAINEE_ID = "学员标识ID";
	public static final String ALIAS_SUBJECT_ID = "课程标识ID";
	public static final String ALIAS_REGULAR_ID = "regularId";
	public static final String ALIAS_REGULAR_SJNUM = "规则实际得分";
	public static final String ALIAS_EXAMINE_TIME = "考核时间";
	public static final String ALIAS_TRAINEE_NAME = "学员姓名";

	/**
     * examineId       db_column: examine_id 
     */	
 	@NotNull(msg="examineId不能为空")
 	@MaxLength(value=32, msg="字段[examineId]超出最大长度[32]")
	private java.lang.String examineId;
 	/**
     *        db_column: er_id 
     */	
 	@MaxLength(value=32, msg="")
	private java.lang.String erId;
    /**
     * 学员标识ID       db_column: trainee_id 
     */	
 	@NotNull(msg="学员标识ID不能为空")
 	@MaxLength(value=32, msg="字段[学员标识ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 课程标识ID       db_column: subject_id 
     */	
 	@NotNull(msg="课程标识ID不能为空")
 	@MaxLength(value=32, msg="字段[课程标识ID]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * regularId       db_column: regular_id 
     */	
 	@NotNull(msg="regularId不能为空")
 	@MaxLength(value=32, msg="字段[regularId]超出最大长度[32]")
	private java.lang.String regularId;
    /**
     * 规则实际得分       db_column: regular_sjnum 
     */	
 	@NotNull(msg="规则实际得分不能为空")
 	@MaxLength(value=5, msg="字段[规则实际得分]超出最大长度[5]")
	private java.math.BigDecimal regularSjnum;
    /**
     * 考核时间       db_column: examine_time 
     */	
 	@NotNull(msg="考核时间不能为空")
 	@MaxLength(value=20, msg="字段[考核时间]超出最大长度[20]")
	private java.lang.String examineTime;
    /**
     * 评分类型(1自评2互评3教师4临时)       db_column: examine_type 
     */	
 	@NotNull(msg="评分类型(1自评2互评3教师4临时)不能为空")
 	@MaxLength(value=20, msg="字段[评分类型(1自评2互评3教师4临时)]超出最大长度[20]")
	private java.lang.String examineType;
    /**
     * 填表人姓名       db_column: create_user_name 
     */	
 	@NotNull(msg="填表人姓名不能为空")
 	@MaxLength(value=100, msg="字段[填表人姓名]超出最大长度[100]")
	private java.lang.String createUserName;
    /**
     * 确认状态(Y/N)       db_column: state 
     */	
 	@NotNull(msg="确认状态(Y/N)不能为空")
 	@MaxLength(value=10, msg="字段[确认状态(Y/N)]超出最大长度[10]")
	private java.lang.String state;

 	private java.lang.String traineeName;
 	private java.lang.String regularName;
 	private java.lang.String subjectName;
 	private java.math.BigDecimal totalScore;
 	private java.math.BigDecimal maxNum;
 	private java.lang.String classId;
 	
 	private List<TevglBookRegular> list;
 	
	private TevglBookSubject tevglBookSubject;
	
	private TevglBookRegular tevglBookRegular;
	
	private TevglTraineeInfo tevglTraineeInfo;
	
	
 	
	//columns END
 	
	public java.lang.String getExamineId() {
		return examineId;
	}
	public void setExamineId(java.lang.String examineId) {
		this.examineId = examineId;
	}
	public java.lang.String getErId() {
		return erId;
	}
	public void setErId(java.lang.String erId) {
		this.erId = erId;
	}
	public java.lang.String getTraineeId() {
		return traineeId;
	}
	public void setTraineeId(java.lang.String traineeId) {
		this.traineeId = traineeId;
	}
	public java.lang.String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(java.lang.String subjectId) {
		this.subjectId = subjectId;
	}
	public java.lang.String getRegularId() {
		return regularId;
	}
	public void setRegularId(java.lang.String regularId) {
		this.regularId = regularId;
	}
	public java.math.BigDecimal getRegularSjnum() {
		return regularSjnum;
	}
	public void setRegularSjnum(java.math.BigDecimal regularSjnum) {
		this.regularSjnum = regularSjnum;
	}
	public java.lang.String getExamineTime() {
		return examineTime;
	}
	public void setExamineTime(java.lang.String examineTime) {
		this.examineTime = examineTime;
	}
	public java.lang.String getExamineType() {
		return examineType;
	}
	public void setExamineType(java.lang.String examineType) {
		this.examineType = examineType;
	}
	public java.lang.String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(java.lang.String createUserName) {
		this.createUserName = createUserName;
	}
	public java.lang.String getState() {
		return state;
	}
	public void setState(java.lang.String state) {
		this.state = state;
	}
	public List<TevglBookRegular> getList() {
		return list;
	}
	public void setList(List<TevglBookRegular> list) {
		this.list = list;
	}
	public TevglBookSubject getTevglBookSubject() {
		return tevglBookSubject;
	}
	public void setTevglBookSubject(TevglBookSubject tevglBookSubject) {
		this.tevglBookSubject = tevglBookSubject;
	}
	public TevglBookRegular getTevglBookRegular() {
		return tevglBookRegular;
	}
	public void setTevglBookRegular(TevglBookRegular tevglBookRegular) {
		this.tevglBookRegular = tevglBookRegular;
	}
	public TevglTraineeInfo getTevglTraineeInfo() {
		return tevglTraineeInfo;
	}
	public void setTevglTraineeInfo(TevglTraineeInfo tevglTraineeInfo) {
		this.tevglTraineeInfo = tevglTraineeInfo;
	}
	public java.lang.String getTraineeName() {
		return traineeName;
	}
	public void setTraineeName(java.lang.String traineeName) {
		this.traineeName = traineeName;
	}
	public java.lang.String getRegularName() {
		return regularName;
	}
	public void setRegularName(java.lang.String regularName) {
		this.regularName = regularName;
	}
	public java.lang.String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	public java.math.BigDecimal getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(java.math.BigDecimal totalScore) {
		this.totalScore = totalScore;
	}
	public java.math.BigDecimal getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(java.math.BigDecimal maxNum) {
		this.maxNum = maxNum;
	}
	public java.lang.String getClassId() {
		return classId;
	}
	public void setClassId(java.lang.String classId) {
		this.classId = classId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

