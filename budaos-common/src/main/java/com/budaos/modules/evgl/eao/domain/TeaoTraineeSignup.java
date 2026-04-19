package com.budaos.modules.evgl.eao.domain;

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

@SuppressWarnings("deprecation")
public class TeaoTraineeSignup extends BaseDomain<TeaoTraineeSignup>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TeaoTraineeSignup";
	public static final String ALIAS_SIGNUP_ID = "报名标识ID";
	public static final String ALIAS_TRAINEE_ID = "学员标识ID";
	public static final String ALIAS_CLASS_ID = "班级标识ID";
	public static final String ALIAS_ORG_ID = "所属教育中心";
	public static final String ALIAS_SIGNUP_TELPHONE = "联系电话";
	public static final String ALIAS_SIGNUP_QQ = "QQ号码";
	public static final String ALIAS_SIGNUP_TIME = "报名时间";
	public static final String ALIAS_CHANNEL = "报名渠道:1、线下报名 2、线上预订 3、朋友推荐";
	public static final String ALIAS_MOBILE = "用户手动输入留下的手机号码";
	public static final String ALIAS_WECHAT_NUMBER = "微信号码";
	

    /**
     * 报名标识ID       db_column: signup_id 
     */	
	 private String signupId;
    /**
     * 学员标识ID       db_column: trainee_id 
     */	
	 private String traineeId;
    /**
     * 班级标识ID       db_column: class_id 
     */	
	 private String classId;
    /**
     * 所属教育中心       db_column: org_id 
     */	
	 private String orgId;
    /**
     * 联系电话       db_column: signup_telphone 
     */	
	 private String signupTelphone;
    /**
     * QQ号码       db_column: signup_qq 
     */	
	 private String signupQq;
    /**
     * 报名时间       db_column: signup_time 
     */	
	 private String signupTime;
    /**
     * 报名渠道:1、线下报名 2、线上预订 3、朋友推荐       db_column: channel 
     */	
	 private String channel;
 	 private String ispx;
 	
 	 
 	private String issingupfrompublic; // 是否来自于公开课的报名
 	
 	/**
     * 用户手动输入留下的手机号码       db_column: mobile 
     */	
 	//@NotNull(msg="用户手动输入留下的手机号码不能为空")
 	//@MaxLength(value=32, msg="字段[用户手动输入留下的手机号码]超出最大长度[32]")
	private java.lang.String mobile;
    /**
     * 微信号码       db_column: wechat_number 
     */	
 	//@NotNull(msg="微信号码不能为空")
 	//@MaxLength(value=32, msg="字段[微信号码]超出最大长度[32]")
	private java.lang.String wechatNumber;
 	
 	public String getIssingupfrompublic() {
		return issingupfrompublic;
	}

	public void setIssingupfrompublic(String issingupfrompublic) {
		this.issingupfrompublic = issingupfrompublic;
	}
//columns END
private String traineeEducation;
	public String getTraineeEducation() {
	return traineeEducation;
}
 
public void setTraineeEducation(String traineeEducation) {
	this.traineeEducation = traineeEducation;
}
public String traineeName;
	public String getTraineeName() {
	return traineeName;
}

public void setTraineeName(String traineeName) {
	this.traineeName = traineeName;
}

	public TeaoTraineeSignup(){
	}

	public TeaoTraineeSignup(
		String signupId
	){
		this.signupId = signupId;
	}

	public void setSignupId(String value) {
		this.signupId = value;
	}
	public String getSignupId() {
		return this.signupId;
	}
	public void setTraineeId(String value) {
		this.traineeId = value;
	}
	public String getTraineeId() {
		return this.traineeId;
	}
	public void setClassId(String value) {
		this.classId = value;
	}
	public String getClassId() {
		return this.classId;
	}
	public void setOrgId(String value) {
		this.orgId = value;
	}
	public String getOrgId() {
		return this.orgId;
	}
	public void setSignupTelphone(String value) {
		this.signupTelphone = value;
	}
	public String getSignupTelphone() {
		return this.signupTelphone;
	}
	public void setSignupQq(String value) {
		this.signupQq = value;
	}
	public String getSignupQq() {
		return this.signupQq;
	}
	public void setSignupTime(String value) {
		this.signupTime = value;
	}
	public String getSignupTime() {
		return this.signupTime;
	}
	public void setChannel(String value) {
		this.channel = value;
	}
	public String getChannel() {
		return this.channel;
	}


	public String getIspx() {
		return ispx;
	}

	public void setIspx(String ispx) {
		this.ispx = ispx;
	}
	
	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}
	public java.lang.String getMobile() {
		return this.mobile;
	}
	public void setWechatNumber(java.lang.String value) {
		this.wechatNumber = value;
	}
	public java.lang.String getWechatNumber() {
		return this.wechatNumber;
	}
	
}

