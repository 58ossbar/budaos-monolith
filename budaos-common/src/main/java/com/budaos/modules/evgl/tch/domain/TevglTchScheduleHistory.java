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

public class TevglTchScheduleHistory extends BaseDomain<TevglTchScheduleHistory>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchScheduleHistory";
	public static final String ALIAS_SCHEDULE_ID_LATEST = "新授课记录主键id";
	public static final String ALIAS_SCHEDULE_ID = "主键id";
	public static final String ALIAS_TEACHER_ID = "关联教师id";
	public static final String ALIAS_SUBJECT_ID = "关联课程id";
	public static final String ALIAS_ROOM_ID = "关联所属教室";
	public static final String ALIAS_TIME_QUANTUM_ID = "timeQuantumId";
	public static final String ALIAS_SCHEDULE = "上课日期";
	public static final String ALIAS_SCHEDULE_STATE = "日程状态";
	public static final String ALIAS_CHECK_STATE = "审核状态(0待审核1通过2未通过)";
	public static final String ALIAS_REASON = "未通过原因";
	public static final String ALIAS_MESSAGE = "message";
	

    /**
     * 新授课记录主键id       db_column: schedule_id_latest 
     */	
 	@NotNull(msg="新授课记录主键id不能为空")
 	@MaxLength(value=32, msg="字段[新授课记录主键id]超出最大长度[32]")
	private java.lang.String scheduleIdLatest;
    /**
     * 主键id       db_column: schedule_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String scheduleId;
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
     * 审核状态(0待审核1通过2未通过)       db_column: check_state 
     */	
 	@NotNull(msg="审核状态(0待审核1通过2未通过)不能为空")
 	@MaxLength(value=2, msg="字段[审核状态(0待审核1通过2未通过)]超出最大长度[2]")
	private java.lang.String checkState;
    /**
     * 未通过原因       db_column: reason 
     */	
 	@NotNull(msg="未通过原因不能为空")
 	@MaxLength(value=500, msg="字段[未通过原因]超出最大长度[500]")
	private java.lang.String reason;
    /**
     * message       db_column: message 
     */	
 	@NotNull(msg="message不能为空")
 	@MaxLength(value=600, msg="字段[message]超出最大长度[600]")
	private java.lang.String message;
 	/**
	 * checkUserId       db_column: check_user_id
	 */
	@NotNull(msg="审核人不能为空")
	@MaxLength(value=600, msg="字段[check_user_id]超出最大长度[32]")
	private String checkUserId;
	//columns END

	public TevglTchScheduleHistory(){
	}

	public TevglTchScheduleHistory(
		java.lang.String scheduleIdLatest
	){
		this.scheduleIdLatest = scheduleIdLatest;
	}

	public void setScheduleIdLatest(java.lang.String value) {
		this.scheduleIdLatest = value;
	}
	public java.lang.String getScheduleIdLatest() {
		return this.scheduleIdLatest;
	}
	public void setScheduleId(java.lang.String value) {
		this.scheduleId = value;
	}
	public java.lang.String getScheduleId() {
		return this.scheduleId;
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
	public void setCheckState(java.lang.String value) {
		this.checkState = value;
	}
	public java.lang.String getCheckState() {
		return this.checkState;
	}
	public void setReason(java.lang.String value) {
		this.reason = value;
	}
	public java.lang.String getReason() {
		return this.reason;
	}
	public void setMessage(java.lang.String value) {
		this.message = value;
	}
	public java.lang.String getMessage() {
		return this.message;
	}
	public String getCheckUserId() {
		return checkUserId;
	}
	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

