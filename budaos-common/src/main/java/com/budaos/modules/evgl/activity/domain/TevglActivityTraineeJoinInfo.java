package com.budaos.modules.evgl.activity.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 学员活动参与情况信息</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglActivityTraineeJoinInfo extends BaseDomain<TevglActivityTraineeJoinInfo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityTraineeJoinInfo";
	public static final String ALIAS_AJ_ID = "ajId";
	public static final String ALIAS_TRAINEE_ID = "traineeId";
	public static final String ALIAS_ACTIVITY_ID = "activityId";
	public static final String ALIAS_ACTIVITY_TYPE = "活动类型(固定标识1投票问卷2头脑风暴3等等)";
	public static final String ALIAS_JOIN_TIME = "joinTime";
	public static final String ALIAS_STATE = "state";
	

    /**
     * ajId       db_column: aj_id 
     */	
 	@NotNull(msg="ajId不能为空")
 	@MaxLength(value=32, msg="字段[ajId]超出最大长度[32]")
	private java.lang.String ajId;
    /**
     * traineeId       db_column: trainee_id 
     */	
 	@NotNull(msg="traineeId不能为空")
 	@MaxLength(value=32, msg="字段[traineeId]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * activityId       db_column: activity_id 
     */	
 	@NotNull(msg="activityId不能为空")
 	@MaxLength(value=32, msg="字段[activityId]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 活动类型(固定标识1投票问卷2头脑风暴3等等)       db_column: activity_type 
     */	
 	@NotNull(msg="活动类型(固定标识1投票问卷2头脑风暴3等等)不能为空")
 	@MaxLength(value=2, msg="字段[活动类型(固定标识1投票问卷2头脑风暴3等等)]超出最大长度[2]")
	private java.lang.String activityType;
    /**
     * joinTime       db_column: join_time 
     */	
 	@NotNull(msg="joinTime不能为空")
 	@MaxLength(value=20, msg="字段[joinTime]超出最大长度[20]")
	private java.lang.String joinTime;
    /**
     * state       db_column: state 
     */	
 	@NotNull(msg="state不能为空")
 	@MaxLength(value=2, msg="字段[state]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglActivityTraineeJoinInfo(){
	}

	public TevglActivityTraineeJoinInfo(
		java.lang.String ajId
	){
		this.ajId = ajId;
	}

	public void setAjId(java.lang.String value) {
		this.ajId = value;
	}
	public java.lang.String getAjId() {
		return this.ajId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public void setActivityType(java.lang.String value) {
		this.activityType = value;
	}
	public java.lang.String getActivityType() {
		return this.activityType;
	}
	public void setJoinTime(java.lang.String value) {
		this.joinTime = value;
	}
	public java.lang.String getJoinTime() {
		return this.joinTime;
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

