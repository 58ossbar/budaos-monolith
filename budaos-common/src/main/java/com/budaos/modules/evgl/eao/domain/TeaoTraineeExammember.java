package com.budaos.modules.evgl.eao.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TeaoTraineeExammember extends BaseDomain<TeaoTraineeExammember>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TeaoTraineeExammember";
	public static final String ALIAS_ET_ID = "主键";
	public static final String ALIAS_ER_ID = "考核教室id";
	public static final String ALIAS_TRAINEE_ID = "成员id";
	public static final String ALIAS_TRAINEE_NAME = "成员姓名";
	public static final String ALIAS_TRAINEE_HEADIMG = "成员头像";
	public static final String ALIAS_CLASS_ID = "学员所在班级";
	public static final String ALIAS_SUBJECT_ID = "考核课程";
	public static final String ALIAS_TRAINEE_TYPE = "成员类型(1学员，2考核负责人，3参与考核教师，4外部专家)";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_REMARK = "备注";
	

    /**
     * 主键       db_column: et_id 
     */	
 	//@NotNull(msg="主键不能为空")
 	@MaxLength(value=32, msg="字段[主键]超出最大长度[32]")
	private java.lang.String etId;
    /**
     * 考核教室id       db_column: er_id 
     */	
 	//@NotNull(msg="考核教室id不能为空")
 	@MaxLength(value=32, msg="字段[考核教室id]超出最大长度[32]")
	private java.lang.String erId;
    /**
     * 成员id       db_column: trainee_id 
     */	
 	//@NotNull(msg="成员id不能为空")
 	@MaxLength(value=32, msg="字段[成员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 成员姓名       db_column: trainee_name 
     */	
 	//@NotNull(msg="成员姓名不能为空")
 	@MaxLength(value=50, msg="字段[成员姓名]超出最大长度[50]")
	private java.lang.String traineeName;
    /**
     * 成员头像       db_column: trainee_headimg 
     */	
 	//@NotNull(msg="成员头像不能为空")
 	@MaxLength(value=1000, msg="字段[成员头像]超出最大长度[1000]")
	private java.lang.String traineeHeadimg;
    /**
     * 学员所在班级       db_column: class_id 
     */	
 	//@NotNull(msg="学员所在班级不能为空")
 	@MaxLength(value=32, msg="字段[学员所在班级]超出最大长度[32]")
	private java.lang.String classId;
    /**
     * 考核课程       db_column: subject_id 
     */	
 	//@NotNull(msg="考核课程不能为空")
 	@MaxLength(value=32, msg="字段[考核课程]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 成员类型(1学员，2考核负责人，3参与考核教师，4外部专家)       db_column: trainee_type 
     */	
 	//@NotNull(msg="成员类型(1学员，2考核负责人，3参与考核教师，4外部专家)不能为空")
 	@MaxLength(value=10, msg="字段[成员类型(1学员，2考核负责人，3参与考核教师，4外部专家)]超出最大长度[10]")
	private java.lang.String traineeType;
    /**
     * 排序号       db_column: sort_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 备注       db_column: remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=255, msg="字段[备注]超出最大长度[255]")
	private java.lang.String remark;
 	
 	private java.lang.String state;//是否提交了考核(Y/N)
 	
 	// 不存入数据库的属性
 	private java.lang.String subjectName; // 临时存储课程名称
   	private java.math.BigDecimal mySelfGiveScore;  // 存储自评成绩
   	private java.math.BigDecimal myStudentGiveScore;// 存储互评成绩
    private java.math.BigDecimal myTeacherGiveScore; // 存储师评成绩
 	private java.math.BigDecimal finalScore; // 存储考核课程最终成绩
 	private java.lang.String examineTime; // 考核时间
 	
	//columns END

	public TeaoTraineeExammember(){
	}

	public TeaoTraineeExammember(
		java.lang.String etId
	){
		this.etId = etId;
	}

	public void setEtId(java.lang.String value) {
		this.etId = value;
	}
	public java.lang.String getEtId() {
		return this.etId;
	}
	public void setErId(java.lang.String value) {
		this.erId = value;
	}
	public java.lang.String getErId() {
		return this.erId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setTraineeName(java.lang.String value) {
		this.traineeName = value;
	}
	public java.lang.String getTraineeName() {
		return this.traineeName;
	}
	public void setTraineeHeadimg(java.lang.String value) {
		this.traineeHeadimg = value;
	}
	public java.lang.String getTraineeHeadimg() {
		return this.traineeHeadimg;
	}
	public void setClassId(java.lang.String value) {
		this.classId = value;
	}
	public java.lang.String getClassId() {
		return this.classId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setTraineeType(java.lang.String value) {
		this.traineeType = value;
	}
	public java.lang.String getTraineeType() {
		return this.traineeType;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	
	public java.lang.String getState() {
		return state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	public java.lang.String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}

	public java.math.BigDecimal getMySelfGiveScore() {
		return mySelfGiveScore;
	}

	public void setMySelfGiveScore(java.math.BigDecimal mySelfGiveScore) {
		this.mySelfGiveScore = mySelfGiveScore;
	}

	public java.math.BigDecimal getMyStudentGiveScore() {
		return myStudentGiveScore;
	}

	public void setMyStudentGiveScore(java.math.BigDecimal myStudentGiveScore) {
		this.myStudentGiveScore = myStudentGiveScore;
	}

	public java.math.BigDecimal getMyTeacherGiveScore() {
		return myTeacherGiveScore;
	}

	public void setMyTeacherGiveScore(java.math.BigDecimal myTeacherGiveScore) {
		this.myTeacherGiveScore = myTeacherGiveScore;
	}

	public java.math.BigDecimal getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(java.math.BigDecimal finalScore) {
		this.finalScore = finalScore;
	}

	public java.lang.String getExamineTime() {
		return examineTime;
	}

	public void setExamineTime(java.lang.String examineTime) {
		this.examineTime = examineTime;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

