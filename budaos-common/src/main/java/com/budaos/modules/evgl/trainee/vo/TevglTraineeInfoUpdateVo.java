package com.budaos.modules.evgl.trainee.vo;

import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;

import java.io.Serializable;

/**
 * 面向前面修改个人信息时，提交的数据
 * @author huj
 *
 */
public class TevglTraineeInfoUpdateVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
     * 粉丝主键ID       db_column: trainee_id 
     */
	@NotNull(msg="主键不能为空")
	private String traineeId;

	/**
     * 姓名       db_column: trainee_name 
     */
	@NotNull(msg="姓名不能为空")
	@MaxLength(value=20, msg="字段[姓名]超出最大长度[20]")
	private String traineeName;
	
	/**
	 * 教师名称
	 */
	@NotNull(msg="姓名不能为空")
	@MaxLength(value=20, msg="字段[姓名]超出最大长度[20]")
	private String teacherName;
	
	/**
     * 昵称       db_column: nick_name 
     */
	@NotNull(msg="昵称不能为空")
	@MaxLength(value=20, msg="字段[昵称]超出最大长度[20]")
	private String nickName;
	
	/**
     * 性别       db_column: trainee_sex 
     */
	@MaxLength(value=1, msg="字段[性别]超出最大长度[1]")
	private String traineeSex;
	
	/**
     * 手机号码       db_column: mobile 
     */
	@MaxLength(value=11, msg="字段[手机号码]超出最大长度[11]")
	private String mobile;
	
	/**
     * 证件照       db_column: trainee_pic 
     */
	@MaxLength(value=300, msg="字段[证件照]超出最大长度[300]")
	private String traineePic;
    
    /**
     * qq号码       db_column: trainee_qq 
     */
	@MaxLength(value=20, msg="字段[qq号码]超出最大长度[20]")
	private String traineeQq;
    /**
     * 学历       db_column: trainee_education 
     */
	@MaxLength(value=20, msg="字段[学历]超出最大长度[20]")
	private String traineeEducation;

    /**
     * 用户邮箱       db_column: email 
     */
	@MaxLength(value=50, msg="字段[用户邮箱]超出最大长度[50]")
	private String email;
 	
 	/**
     * 工号/学号       db_column: job_number 
     */
	@MaxLength(value=50, msg="字段[工号/学号]超出最大长度[50]")
	private String jobNumber;

	/**
	 * 教师资格证
	 */
	@MaxLength(value=50, msg="字段[工号/学号]超出最大长度[50]")
	private String teacherErtificateNumber;

	public String getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(String traineeId) {
		this.traineeId = traineeId;
	}

	public String getTraineeName() {
		return traineeName;
	}

	public void setTraineeName(String traineeName) {
		this.traineeName = traineeName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTraineeSex() {
		return traineeSex;
	}

	public void setTraineeSex(String traineeSex) {
		this.traineeSex = traineeSex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTraineePic() {
		return traineePic;
	}

	public void setTraineePic(String traineePic) {
		this.traineePic = traineePic;
	}

	public String getTraineeQq() {
		return traineeQq;
	}

	public void setTraineeQq(String traineeQq) {
		this.traineeQq = traineeQq;
	}

	public String getTraineeEducation() {
		return traineeEducation;
	}

	public void setTraineeEducation(String traineeEducation) {
		this.traineeEducation = traineeEducation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getTeacherErtificateNumber() {
		return teacherErtificateNumber;
	}

	public void setTeacherErtificateNumber(String teacherErtificateNumber) {
		this.teacherErtificateNumber = teacherErtificateNumber;
	}


	@Override
	public String toString() {
		return "TevglTraineeInfoUpdateVo{" +
				"traineeId='" + traineeId + '\'' +
				", traineeName='" + traineeName + '\'' +
				", teacherName='" + teacherName + '\'' +
				", nickName='" + nickName + '\'' +
				", traineeSex='" + traineeSex + '\'' +
				", mobile='" + mobile + '\'' +
				", traineePic='" + traineePic + '\'' +
				", traineeQq='" + traineeQq + '\'' +
				", traineeEducation='" + traineeEducation + '\'' +
				", email='" + email + '\'' +
				", jobNumber='" + jobNumber + '\'' +
				", teacherErtificateNumber='" + teacherErtificateNumber + '\'' +
				'}';
	}
}
