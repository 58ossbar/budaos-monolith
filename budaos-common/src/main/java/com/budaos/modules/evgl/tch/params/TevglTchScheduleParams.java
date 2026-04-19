package com.budaos.modules.evgl.tch.params;

import java.io.Serializable;

/**
 * 网站端、小程序、查询课表专用查询参数
 * @author huj
 *
 */
public class TevglTchScheduleParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String scheduleId;
	
	/**
	 * 开始日期，格式要求 yyyy-MM-dd
	 */
	private String beginDate;
	
	/**
	 * 结束日期，格式要求 yyyy-MM-dd
	 */
	private String endDate;
	
	/**
	 * 上课教室
	 */
	private String roomId;
	
	/**
	 * 上课班级
	 */
	private String classId;
	
	/**
	 * 上课老师
	 */
	private String teacherId;
	
	/**
	 * 上课课程
	 */
	private String subjectId;
	
	/**
	 * 状态类型
	 */
	private String scheduleState;
	
	/**
	 * 年月（格式要求为yyyy-MM）
	 */
	private String yearMonth;

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	public String getScheduleState() {
		return scheduleState;
	}

	public void setScheduleState(String scheduleState) {
		this.scheduleState = scheduleState;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Override
	public String toString() {
		return "TevglTchScheduleParams [beginDate=" + beginDate + ", endDate=" + endDate + ", roomId=" + roomId
				+ ", classId=" + classId + ", teacherId=" + teacherId + ", subjectId=" + subjectId + ", scheduleState="
				+ scheduleState + "]";
	}
}
