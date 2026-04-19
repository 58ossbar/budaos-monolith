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

public class TevglActivityTaskScore extends BaseDomain<TevglActivityTaskScore>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityTaskScore";
	public static final String ALIAS_SCORE_ID = "评分id";
	public static final String ALIAS_CT_ID = "关联课堂id";
	public static final String ALIAS_TYPE = "类型（1表示小组2表示人）";
	public static final String ALIAS_GROUP_ID = "小组id";
	public static final String ALIAS_MEMBER_ID = "成员id";
	public static final String ALIAS_ACTIVITY_ID = "activityId";
	public static final String ALIAS_SCORE = "评分分值";
	public static final String ALIAS_USER_TYPE = "评分人类型1小组2人";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 评分id       db_column: score_id 
     */	
 	@NotNull(msg="评分id不能为空")
 	@MaxLength(value=32, msg="字段[评分id]超出最大长度[32]")
	private java.lang.String scoreId;
    /**
     * 关联课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="关联课堂id不能为空")
 	@MaxLength(value=32, msg="字段[关联课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 类型（1表示小组2表示人）       db_column: type 
     */	
 	@NotNull(msg="类型（1表示小组2表示人）不能为空")
 	@MaxLength(value=32, msg="字段[类型（1表示小组2表示人）]超出最大长度[32]")
	private java.lang.String type;
    /**
     * 小组id       db_column: group_id 
     */	
 	@NotNull(msg="小组id不能为空")
 	@MaxLength(value=32, msg="字段[小组id]超出最大长度[32]")
	private java.lang.String groupId;
    /**
     * 成员id       db_column: member_id 
     */	
 	@NotNull(msg="成员id不能为空")
 	@MaxLength(value=32, msg="字段[成员id]超出最大长度[32]")
	private java.lang.String memberId;
    /**
     * activityId       db_column: activity_id 
     */	
 	@NotNull(msg="activityId不能为空")
 	@MaxLength(value=32, msg="字段[activityId]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 评分分值       db_column: score 
     */	
 	@NotNull(msg="评分分值不能为空")
 	@MaxLength(value=5, msg="字段[评分分值]超出最大长度[5]")
	private java.math.BigDecimal score;
    /**
     * 评分人类型1小组2人       db_column: user_type 
     */	
 	@NotNull(msg="评分人类型1小组2人不能为空")
 	@MaxLength(value=2, msg="字段[评分人类型1小组2人]超出最大长度[2]")
	private java.lang.String userType;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglActivityTaskScore(){
	}

	public TevglActivityTaskScore(
		java.lang.String scoreId
	){
		this.scoreId = scoreId;
	}

	public void setScoreId(java.lang.String value) {
		this.scoreId = value;
	}
	public java.lang.String getScoreId() {
		return this.scoreId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setGroupId(java.lang.String value) {
		this.groupId = value;
	}
	public java.lang.String getGroupId() {
		return this.groupId;
	}
	public void setMemberId(java.lang.String value) {
		this.memberId = value;
	}
	public java.lang.String getMemberId() {
		return this.memberId;
	}
	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public void setScore(java.math.BigDecimal value) {
		this.score = value;
	}
	public java.math.BigDecimal getScore() {
		return this.score;
	}
	public void setUserType(java.lang.String value) {
		this.userType = value;
	}
	public java.lang.String getUserType() {
		return this.userType;
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

