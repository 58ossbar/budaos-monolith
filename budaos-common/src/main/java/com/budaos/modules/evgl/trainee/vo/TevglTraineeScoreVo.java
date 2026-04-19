package com.budaos.modules.evgl.trainee.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 返回给前端渲染的学生成绩vo
 * @author huj
 *
 */
public class TevglTraineeScoreVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;

	private String erId;
	
	private String traineeId;
	
	private String traineeName;
	
	private String ctId;
	
	private String pkgId;
	
	private String roomName;

	/**
	 * 教材id
	 */
	private String subjectId;
	
	/**
	 * 课程id
	 */
	private String subjectRef;
	
	/**
	 * 课程名称
	 */
	private String subjectName;
	
	/**
	 * 考试时间
	 */
	private String examTime;
	
	/**
	 * 授课老师id
	 */
	private String teacherId;
	
	/**
	 * 授课老师名称
	 */
	private String teacherName;
	
	/**
	 * 最终成绩
	 */
	private BigDecimal finalScore;
	
	/**
	 * 考试类型（4测试活动，9实践考核）
	 */
	private String type;
	
	/**
	 * 考试类型名称
	 */
	private String typeName;

	private String classIds;

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getExamTime() {
		return examTime;
	}

	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public BigDecimal getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(BigDecimal finalScore) {
		this.finalScore = finalScore;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public String getErId() {
		return erId;
	}

	public void setErId(String erId) {
		this.erId = erId;
	}

	public String getClassIds() {
		return classIds;
	}

	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}

	public String getSubjectRef() {
		return subjectRef;
	}

	public void setSubjectRef(String subjectRef) {
		this.subjectRef = subjectRef;
	}
}
