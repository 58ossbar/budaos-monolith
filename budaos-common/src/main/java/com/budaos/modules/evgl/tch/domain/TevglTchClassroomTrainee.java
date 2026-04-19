package com.budaos.modules.evgl.tch.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 课堂成员</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglTchClassroomTrainee extends BaseDomain<TevglTchClassroomTrainee>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchClassroomTrainee";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_CT_ID = "课堂ID";
	public static final String ALIAS_TRAINEE_ID = "学员ID";
	public static final String ALIAS_CLASS_ID = "班级ID";
	public static final String ALIAS_JOIN_DATE = "加入时间";
	public static final String ALIAS_STATE = "state";
	

    /**
     * 主键       db_column: id 
     */	
 	@NotNull(msg="主键不能为空")
 	@MaxLength(value=32, msg="字段[主键]超出最大长度[32]")
	private java.lang.String id;
    /**
     * 课堂ID       db_column: ct_id 
     */	
 	@NotNull(msg="课堂ID不能为空")
 	@MaxLength(value=32, msg="字段[课堂ID]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 学员ID       db_column: trainee_id 
     */	
 	@NotNull(msg="学员ID不能为空")
 	@MaxLength(value=32, msg="字段[学员ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 班级ID       db_column: class_id 
     */	
 	//@NotNull(msg="班级ID不能为空")
 	@MaxLength(value=32, msg="字段[班级ID]超出最大长度[32]")
	private java.lang.String classId;
    /**
     * 加入时间       db_column: join_date 
     */	
 	@NotNull(msg="加入时间不能为空")
 	@MaxLength(value=10, msg="字段[加入时间]超出最大长度[10]")
	private java.lang.String joinDate;
    /**
     * state       db_column: state 
     */	
 	@NotNull(msg="state不能为空")
 	@MaxLength(value=2, msg="字段[state]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglTchClassroomTrainee(){
	}

	public TevglTchClassroomTrainee(
		java.lang.String id
	){
		this.id = id;
	}

	public void setId(java.lang.String value) {
		this.id = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setClassId(java.lang.String value) {
		this.classId = value;
	}
	public java.lang.String getClassId() {
		return this.classId;
	}
	public void setJoinDate(java.lang.String value) {
		this.joinDate = value;
	}
	public java.lang.String getJoinDate() {
		return this.joinDate;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

