package com.budaos.modules.evgl.tch.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglTchSchedule extends BaseDomain<TevglTchSchedule>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchSchedule";
	public static final String ALIAS_SCHEDULE_ID = "主键id";
	public static final String ALIAS_CLASS_ID = "关联班级id";
	public static final String ALIAS_TEACHER_ID = "关联教师id";
	public static final String ALIAS_SUBJECT_ID = "关联课程id";
	public static final String ALIAS_ROOM_ID = "关联所属教室";
	public static final String ALIAS_TIME_QUANTUM_ID = "timeQuantumId";
	public static final String ALIAS_SCHEDULE = "上课日期";
	public static final String ALIAS_SCHEDULE_STATE = "日程状态";
	

    /**
     * 主键id       db_column: schedule_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String scheduleId;
    /**
     * 关联班级id       db_column: class_id 
     */	
 	@NotNull(msg="关联班级id不能为空")
 	@MaxLength(value=32, msg="字段[关联班级id]超出最大长度[32]")
	private java.lang.String classId;
    /**
     * 关联教师id       db_column: teacher_id 
     */	
 	@NotNull(msg="关联教师id不能为空")
 	@MaxLength(value=32, msg="字段[关联教师id]超出最大长度[32]")
	private java.lang.String teacherId;
    /**
     * 关联课程id       db_column: subject_id 
     */	
 	@NotNull(msg="关联课程id不能为空")
 	@MaxLength(value=32, msg="字段[关联课程id]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 关联所属教室       db_column: room_id 
     */	
 	@NotNull(msg="关联所属教室不能为空")
 	@MaxLength(value=32, msg="字段[关联所属教室]超出最大长度[32]")
	private java.lang.String roomId;
    /**
     * timeQuantumId       db_column: time_quantum_id 
     */	
 	@NotNull(msg="timeQuantumId不能为空")
 	@MaxLength(value=32, msg="字段[timeQuantumId]超出最大长度[32]")
	private java.lang.String timeQuantumId;
    /**
     * 上课日期       db_column: schedule 
     */	
 	@NotNull(msg="上课日期不能为空")
 	@MaxLength(value=20, msg="字段[上课日期]超出最大长度[20]")
	private java.lang.String schedule;
    /**
     * 日程状态       db_column: schedule_state 
     */	
 	@NotNull(msg="日程状态不能为空")
 	@MaxLength(value=2, msg="字段[日程状态]超出最大长度[2]")
	private java.lang.String scheduleState;
 	/**
     * 状态       db_column: state 
     */	
 	@NotNull(msg="状态不能为空")
 	@MaxLength(value=2, msg="字段[状态]超出最大长度[2]")
	private java.lang.String state;
	//columns END

 	private List<Map<String, Object>> list;
 	
 	private Boolean disabled;
 	
 	/**
 	 * 标识当前记录是否能删除
 	 */
 	private Boolean hasDeleteIcon;
 	
 	/**
	 * 教室（实训室）
	 */
	private String roomName;
	/** 某节课开始时间 */
	private String beginTime;
	/** 某节课结束时间 */
	private String endTime;
	/** 班级名称 */
	private String className;
	/** 老师名称 */
	private String teacherName;
	/** 课程名称 */
	private String subjectName;
	/** 类型名称 */
	private String scheduleStateName;
 	
	public TevglTchSchedule(){
	}

	public TevglTchSchedule(
		java.lang.String scheduleId
	){
		this.scheduleId = scheduleId;
	}

	public void setScheduleId(java.lang.String value) {
		this.scheduleId = value;
	}
	public java.lang.String getScheduleId() {
		return this.scheduleId;
	}
	public void setClassId(java.lang.String value) {
		this.classId = value;
	}
	public java.lang.String getClassId() {
		return this.classId;
	}
	public void setTeacherId(java.lang.String value) {
		this.teacherId = value;
	}
	public java.lang.String getTeacherId() {
		return this.teacherId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setRoomId(java.lang.String value) {
		this.roomId = value;
	}
	public java.lang.String getRoomId() {
		return this.roomId;
	}
	public void setTimeQuantumId(java.lang.String value) {
		this.timeQuantumId = value;
	}
	public java.lang.String getTimeQuantumId() {
		return this.timeQuantumId;
	}
	public void setSchedule(java.lang.String value) {
		this.schedule = value;
	}
	public java.lang.String getSchedule() {
		return this.schedule;
	}
	public void setScheduleState(java.lang.String value) {
		this.scheduleState = value;
	}
	public java.lang.String getScheduleState() {
		return this.scheduleState;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getHasDeleteIcon() {
		return hasDeleteIcon;
	}

	public void setHasDeleteIcon(Boolean hasDeleteIcon) {
		this.hasDeleteIcon = hasDeleteIcon;
	}

	public String getScheduleStateName() {
		return scheduleStateName;
	}

	public void setScheduleStateName(String scheduleStateName) {
		this.scheduleStateName = scheduleStateName;
	}

	public java.lang.String getState() {
		return state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}
	
}

