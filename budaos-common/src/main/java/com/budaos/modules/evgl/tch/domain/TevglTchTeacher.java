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

public class TevglTchTeacher extends BaseDomain<TevglTchTeacher>{
	private static final long serialVersionUID = 1L;
	 
	//alias
	public static final String TABLE_ALIAS = "TevglTchTeacher";
	public static final String ALIAS_TEACHER_ID = "布道师主键ID";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_TEACHER_NAME = "教师名称";
	public static final String ALIAS_TEACHER_POST = "职位";
	public static final String ALIAS_TEACHER_PIC = "教师头像";
	public static final String ALIAS_TEACHER_DESC = "老师简介(文本)";
	public static final String ALIAS_TEACHER_REMARK = "教师介绍(富文本)";
	public static final String ALIAS_USER_ID = "账号id";
	public static final String ALIAS_USERNAME = "账号名称";
	public static final String ALIAS_TRAINEE_ID = "粉丝id";
	public static final String ALIAS_STATE = "教师状态(Y有效N无效)";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_ORG_ID_DEPARTMENT = "所在院系";
	public static final String ALIAS_TEACHER_ERTIFICATE_NUMBER = "教师证件号";
	public static final String ALIAS_JOB_NUMBER = "工号/学号";
	public static final String ALIAS_SEX = "性别";

    /**
     * 布道师主键ID       db_column: teacher_id 
     */	
 	@NotNull(msg="布道师主键ID不能为空")
 	@MaxLength(value=32, msg="字段[布道师主键ID]超出最大长度[32]")
	private java.lang.String teacherId;
    /**
     * 机构ID       db_column: org_id 
     */	
 	@NotNull(msg="机构ID不能为空")
 	@MaxLength(value=32, msg="字段[机构ID]超出最大长度[32]")
	private java.lang.String orgId;
    /**
     * 教师名称       db_column: teacher_name 
     */	
 	@NotNull(msg="教师名称不能为空")
 	@MaxLength(value=50, msg="字段[教师名称]超出最大长度[50]")
	private java.lang.String teacherName;
    /**
     * 职位       db_column: teacher_post 
     */	
 	//@NotNull(msg="职位不能为空")
 	@MaxLength(value=32, msg="字段[职位]超出最大长度[32]")
	private java.lang.String teacherPost;
    /**
     * 教师头像       db_column: teacher_pic 
     */	
 	//@NotNull(msg="教师头像不能为空")
 	@MaxLength(value=300, msg="字段[教师头像]超出最大长度[300]")
	private java.lang.String teacherPic;
    /**
     * 老师简介(文本)       db_column: teacher_desc 
     */	
 	//@NotNull(msg="老师简介(文本)不能为空")
 	@MaxLength(value=500, msg="字段[老师简介(文本)]超出最大长度[500]")
	private java.lang.String teacherDesc;
    /**
     * 教师介绍(富文本)       db_column: teacher_remark 
     */	
 	//@NotNull(msg="教师介绍(富文本)不能为空")
 	@MaxLength(value=2147483647, msg="字段[教师介绍(富文本)]超出最大长度[2147483647]")
	private java.lang.String teacherRemark;
    /**
     * 账号id       db_column: user_id 
     */	
 	//@NotNull(msg="账号id不能为空")
 	@MaxLength(value=32, msg="字段[账号id]超出最大长度[32]")
	private java.lang.String userId;
    /**
     * 账号名称       db_column: username 
     */	
 	//@NotNull(msg="账号名称不能为空")
 	@MaxLength(value=50, msg="字段[账号名称]超出最大长度[50]")
	private java.lang.String username;
    /**
     * 粉丝id       db_column: trainee_id 
     */	
 	//@NotNull(msg="粉丝id不能为空")
 	@MaxLength(value=32, msg="字段[粉丝id]超出最大长度[32]")
	private java.lang.String traineeId;
 	/**
 	 * 关联职业课程路径ID db_column: major_id 
 	 */
 	//@NotNull(msg="关联职业课程路径ID")
 	@MaxLength(value=500, msg="字段[关联职业课程路径ID]超出最大长度[500]")
	private java.lang.String majorId; // 多个以逗号隔开
 	/**
 	 * 是否推荐到首页(Y是N否) db_column: show_index 
 	 */
 	//@NotNull(msg="是否推荐到首页(Y是N否)")
 	@MaxLength(value=2, msg="字段[是否推荐到首页(Y是N否)]超出最大长度[2]")
	private java.lang.String showIndex;
    /**
     * 教师状态(Y有效N无效)       db_column: state 
     */	
 	//@NotNull(msg="教师状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[教师状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 排序号       db_column: sort_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
 	/**
     * 所在院系       db_column: org_id_department 
     */	
 	//@NotNull(msg="所在院系不能为空")
 	@MaxLength(value=50, msg="字段[所在院系]超出最大长度[50]")
	private java.lang.String orgIdDepartment;
    /**
     * 教师证件号       db_column: teacher_ertificate_number 
     */	
 	//@NotNull(msg="教师证件号不能为空")
 	@MaxLength(value=50, msg="字段[教师证件号]超出最大长度[50]")
	private java.lang.String teacherErtificateNumber;
    /**
     * 工号/学号       db_column: job_number 
     */	
 	//@NotNull(msg="工号/学号不能为空")
 	@MaxLength(value=50, msg="字段[工号/学号]超出最大长度[50]")
	private java.lang.String jobNumber;
 	/**
     * 性别       db_column: sex 
     */	
 	//@NotNull(msg="性别不能为空")
 	@MaxLength(value=2, msg="字段[性别]超出最大长度[2]")
	private java.lang.String sex;
 	
 	
 	
 	private String majorName; // 专业
 	private String postName; // 职位
 	private String traineeName; //关联粉丝名称
 	private java.lang.String attachId; // 附件id 
 	
	//columns END

	public TevglTchTeacher(){
	}

	public TevglTchTeacher(
		java.lang.String teacherId
	){
		this.teacherId = teacherId;
	}

	public void setTeacherId(java.lang.String value) {
		this.teacherId = value;
	}
	public java.lang.String getTeacherId() {
		return this.teacherId;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setTeacherName(java.lang.String value) {
		this.teacherName = value;
	}
	public java.lang.String getTeacherName() {
		return this.teacherName;
	}
	public void setTeacherPost(java.lang.String value) {
		this.teacherPost = value;
	}
	public java.lang.String getTeacherPost() {
		return this.teacherPost;
	}
	public void setTeacherPic(java.lang.String value) {
		this.teacherPic = value;
	}
	public java.lang.String getTeacherPic() {
		return this.teacherPic;
	}
	public void setTeacherDesc(java.lang.String value) {
		this.teacherDesc = value;
	}
	public java.lang.String getTeacherDesc() {
		return this.teacherDesc;
	}
	public void setTeacherRemark(java.lang.String value) {
		this.teacherRemark = value;
	}
	public java.lang.String getTeacherRemark() {
		return this.teacherRemark;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setUsername(java.lang.String value) {
		this.username = value;
	}
	public java.lang.String getUsername() {
		return this.username;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public java.lang.String getMajorId() {
		return majorId;
	}

	public void setMajorId(java.lang.String majorId) {
		this.majorId = majorId;
	}

	public java.lang.String getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(java.lang.String showIndex) {
		this.showIndex = showIndex;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getTraineeName() {
		return traineeName;
	}

	public void setTraineeName(String traineeName) {
		this.traineeName = traineeName;
	}

	public java.lang.Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(java.lang.Integer sortNum) {
		this.sortNum = sortNum;
	}
	public void setOrgIdDepartment(java.lang.String value) {
		this.orgIdDepartment = value;
	}
	public java.lang.String getOrgIdDepartment() {
		return this.orgIdDepartment;
	}
	public void setTeacherErtificateNumber(java.lang.String value) {
		this.teacherErtificateNumber = value;
	}
	public java.lang.String getTeacherErtificateNumber() {
		return this.teacherErtificateNumber;
	}
	public void setJobNumber(java.lang.String value) {
		this.jobNumber = value;
	}
	public java.lang.String getJobNumber() {
		return this.jobNumber;
	}

	public java.lang.String getAttachId() {
		return attachId;
	}

	public void setAttachId(java.lang.String attachId) {
		this.attachId = attachId;
	}

	public java.lang.String getSex() {
		return sex;
	}

	public void setSex(java.lang.String sex) {
		this.sex = sex;
	}
}

