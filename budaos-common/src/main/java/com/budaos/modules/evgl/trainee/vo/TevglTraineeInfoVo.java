package com.budaos.modules.evgl.trainee.vo;

import com.budaos.core.baseclass.domain.BaseDomain;
import com.budaos.modules.common.annotation.Excel;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;

/**
 * 学员实体类，干掉密码等敏感字段
 * @author huj
 *
 */
public class TevglTraineeInfoVo extends BaseDomain<TevglTraineeInfo> {

	private static final long serialVersionUID = 1L;
	
	/**
     * 粉丝主键ID       db_column: trainee_id 
     */	
	private java.lang.String traineeId;

	/**
     * 姓名       db_column: trainee_name 
     */
	@Excel(name = "学员姓名", sort = 1)
	private java.lang.String traineeName;
	
	/**
     * 昵称       db_column: nick_name 
     */	
	private java.lang.String nickName;
	
	/**
     * 性别       db_column: trainee_sex 
     */	
	//@Excel(name = "性别", sort = 3, dictType = "sex")
	private java.lang.String traineeSex;
	
	/**
     * 手机号码       db_column: mobile 
     */
	@Excel(name = "手机号码", sort = 2)
	private java.lang.String mobile;
	
	/**
     * 状态       db_column: trainee_state 
     */	
	private java.lang.String traineeState;
	
	 /**
     * 在校专业       db_column: major 
     */
	@Excel(name = "所学专业", sort = 6)
	private java.lang.String major;
	
	/**
     * 就职公司       db_column: company 
     */
	private java.lang.String company;
	
	/**
     * 就读院校       db_column: trainee_school 
     */
	@Excel(name = "所在学校", sort = 5)
	private java.lang.String traineeSchool;
	
	/**
     * 证件照       db_column: trainee_pic 
     */	
	private java.lang.String traineePic;
    /**
     * 头像       db_column: trainee_head 
     */	
	private java.lang.String traineeHead;
    /**
     * qq号码       db_column: trainee_qq 
     */	
	private java.lang.String traineeQq;
    /**
     * 学历       db_column: trainee_education 
     */	
	private java.lang.String traineeEducation;
    /**
     * 用户邮箱       db_column: email 
     */	
 	@Excel(name = "邮箱", sort = 4)
	private java.lang.String email;
 	
 	/**
     * 用户类型       db_column: trainee_type 
     */	
	private java.lang.String traineeType;
 	/**
     * 总经验值       db_column: empirical_value 
     */	
	private java.lang.Integer empiricalValue;
    /**
     * 博客总数量       db_column: blogs_num 
     */	
	private java.lang.Integer blogsNum;
    /**
     * 开设课堂总数量       db_column: classroom_num 
     */	
	private java.lang.Integer classroomNum;
 	/**
     * 工号/学号       db_column: job_number 
     */	
 	@Excel(name = "学号", sort = 7)
	private java.lang.String jobNumber;
 	
 	/**
     * 粉丝数       db_column: fans_num 
     */	
	private java.lang.Integer fansNum;
 	
 	/**
     * 关注数       db_column: follow_num 
     */	
	private java.lang.Integer followNum;
 	
 	 /**
     * 博客背景图       db_column: blog_bg_pic 
     */	
	private java.lang.String blogBgPic;
 	/**
     * 博客个人描述       db_column: blog_remark 
     */	
	private java.lang.String blogRemark;
 	
 	/**
 	 * 班级id
 	 */
 	private String classId;
 	
 	/**
 	 * 班级名称
 	 */
 	@Excel(name = "所属班级", sort = 8)
 	private String className;
 	
 	private String token;

	public java.lang.String getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(java.lang.String traineeId) {
		this.traineeId = traineeId;
	}

	public java.lang.String getTraineeName() {
		return traineeName;
	}

	public void setTraineeName(java.lang.String traineeName) {
		this.traineeName = traineeName;
	}

	public java.lang.String getNickName() {
		return nickName;
	}

	public void setNickName(java.lang.String nickName) {
		this.nickName = nickName;
	}

	public java.lang.String getTraineeSex() {
		return traineeSex;
	}

	public void setTraineeSex(java.lang.String traineeSex) {
		this.traineeSex = traineeSex;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getTraineeState() {
		return traineeState;
	}

	public void setTraineeState(java.lang.String traineeState) {
		this.traineeState = traineeState;
	}

	public java.lang.String getMajor() {
		return major;
	}

	public void setMajor(java.lang.String major) {
		this.major = major;
	}

	public java.lang.String getCompany() {
		return company;
	}

	public void setCompany(java.lang.String company) {
		this.company = company;
	}

	public java.lang.String getTraineeSchool() {
		return traineeSchool;
	}

	public void setTraineeSchool(java.lang.String traineeSchool) {
		this.traineeSchool = traineeSchool;
	}

	public java.lang.String getTraineePic() {
		return traineePic;
	}

	public void setTraineePic(java.lang.String traineePic) {
		this.traineePic = traineePic;
	}

	public java.lang.String getTraineeHead() {
		return traineeHead;
	}

	public void setTraineeHead(java.lang.String traineeHead) {
		this.traineeHead = traineeHead;
	}

	public java.lang.String getTraineeQq() {
		return traineeQq;
	}

	public void setTraineeQq(java.lang.String traineeQq) {
		this.traineeQq = traineeQq;
	}

	public java.lang.String getTraineeEducation() {
		return traineeEducation;
	}

	public void setTraineeEducation(java.lang.String traineeEducation) {
		this.traineeEducation = traineeEducation;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getTraineeType() {
		return traineeType;
	}

	public void setTraineeType(java.lang.String traineeType) {
		this.traineeType = traineeType;
	}

	public java.lang.Integer getEmpiricalValue() {
		return empiricalValue;
	}

	public void setEmpiricalValue(java.lang.Integer empiricalValue) {
		this.empiricalValue = empiricalValue;
	}

	public java.lang.Integer getBlogsNum() {
		return blogsNum;
	}

	public void setBlogsNum(java.lang.Integer blogsNum) {
		this.blogsNum = blogsNum;
	}

	public java.lang.Integer getClassroomNum() {
		return classroomNum;
	}

	public void setClassroomNum(java.lang.Integer classroomNum) {
		this.classroomNum = classroomNum;
	}

	public java.lang.String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(java.lang.String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public java.lang.Integer getFansNum() {
		return fansNum;
	}

	public void setFansNum(java.lang.Integer fansNum) {
		this.fansNum = fansNum;
	}

	public java.lang.Integer getFollowNum() {
		return followNum;
	}

	public void setFollowNum(java.lang.Integer followNum) {
		this.followNum = followNum;
	}

	public java.lang.String getBlogBgPic() {
		return blogBgPic;
	}

	public void setBlogBgPic(java.lang.String blogBgPic) {
		this.blogBgPic = blogBgPic;
	}

	public java.lang.String getBlogRemark() {
		return blogRemark;
	}

	public void setBlogRemark(java.lang.String blogRemark) {
		this.blogRemark = blogRemark;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
 	
 	
}
