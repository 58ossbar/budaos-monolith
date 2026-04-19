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

public class TevglActivityTaskRelevanceGroup extends BaseDomain<TevglActivityTaskRelevanceGroup>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityTaskRelevanceGroup";
	public static final String ALIAS_RELEVANCE_ID = "主键id";
	public static final String ALIAS_CT_ID = "关联课堂id";
	public static final String ALIAS_ACTIVITY_ID = "活动id";
	public static final String ALIAS_GROUP_ID = "小组id";
	

    /**
     * 主键id       db_column: relevance_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String relevanceId;
    /**
     * 关联课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="关联课堂id不能为空")
 	@MaxLength(value=32, msg="字段[关联课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 活动id       db_column: activity_id 
     */	
 	@NotNull(msg="活动id不能为空")
 	@MaxLength(value=32, msg="字段[活动id]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 小组id       db_column: group_id 
     */	
 	@NotNull(msg="小组id不能为空")
 	@MaxLength(value=32, msg="字段[小组id]超出最大长度[32]")
	private java.lang.String groupId;
	//columns END

	public TevglActivityTaskRelevanceGroup(){
	}

	public TevglActivityTaskRelevanceGroup(
		java.lang.String relevanceId
	){
		this.relevanceId = relevanceId;
	}

	public void setRelevanceId(java.lang.String value) {
		this.relevanceId = value;
	}
	public java.lang.String getRelevanceId() {
		return this.relevanceId;
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
	
	private TevglActivityTask tevglActivityTask;
	
	public void setTevglActivityTask(TevglActivityTask tevglActivityTask){
		this.tevglActivityTask = tevglActivityTask;
	}
	
	public TevglActivityTask getTevglActivityTask() {
		return tevglActivityTask;
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

