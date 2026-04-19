package com.budaos.modules.evgl.activity.domain;

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

public class TevglActivityTaskGroupMember extends BaseDomain<TevglActivityTaskGroupMember>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityTaskGroupMember";
	public static final String ALIAS_MEMBER_ID = "成员id(就已学员id作为主键)";
	public static final String ALIAS_CT_ID = "关联课堂id";
	public static final String ALIAS_ACTIVITY_ID = "activityId";
	public static final String ALIAS_GROUP_ID = "小组id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_CONTENT = "作答内容";
	public static final String ALIAS_FINAL_SCORE = "最终得分";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 成员id(就已学员id作为主键)       db_column: member_id 
     */	
 	@NotNull(msg="成员id(就已学员id作为主键)不能为空")
 	@MaxLength(value=32, msg="字段[成员id(就已学员id作为主键)]超出最大长度[32]")
	private java.lang.String memberId;
    /**
     * 关联课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="关联课堂id不能为空")
 	@MaxLength(value=32, msg="字段[关联课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * activityId       db_column: activity_id 
     */	
 	@NotNull(msg="activityId不能为空")
 	@MaxLength(value=32, msg="字段[activityId]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 小组id       db_column: group_id 
     */	
 	@NotNull(msg="小组id不能为空")
 	@MaxLength(value=32, msg="字段[小组id]超出最大长度[32]")
	private java.lang.String groupId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 作答内容       db_column: content 
     */	
 	@NotNull(msg="作答内容不能为空")
 	@MaxLength(value=65535, msg="字段[作答内容]超出最大长度[65535]")
	private java.lang.String content;
    /**
     * 最终得分       db_column: final_score 
     */	
 	@NotNull(msg="最终得分不能为空")
 	@MaxLength(value=5, msg="字段[最终得分]超出最大长度[5]")
	private java.math.BigDecimal finalScore;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglActivityTaskGroupMember(){
	}

	public TevglActivityTaskGroupMember(
		java.lang.String memberId
	){
		this.memberId = memberId;
	}

	public void setMemberId(java.lang.String value) {
		this.memberId = value;
	}
	public java.lang.String getMemberId() {
		return this.memberId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public void setGroupId(java.lang.String value) {
		this.groupId = value;
	}
	public java.lang.String getGroupId() {
		return this.groupId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setFinalScore(java.math.BigDecimal value) {
		this.finalScore = value;
	}
	public java.math.BigDecimal getFinalScore() {
		return this.finalScore;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	
	private TevglActivityTaskGroup tevglActivityTaskGroup;
	
	public void setTevglActivityTaskGroup(TevglActivityTaskGroup tevglActivityTaskGroup){
		this.tevglActivityTaskGroup = tevglActivityTaskGroup;
	}
	
	public TevglActivityTaskGroup getTevglActivityTaskGroup() {
		return tevglActivityTaskGroup;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

