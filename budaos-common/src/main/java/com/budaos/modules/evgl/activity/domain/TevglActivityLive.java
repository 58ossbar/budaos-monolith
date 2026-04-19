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

public class TevglActivityLive extends BaseDomain<TevglActivityLive>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityLive";
	public static final String ALIAS_ACTIVITY_ID = "活动（直播id）";
	public static final String ALIAS_ACTIVITY_TITLE = "标题";
	public static final String ALIAS_CONTENT = "内容";
	public static final String ALIAS_ACTIVITY_BEGIN_TIME = "活动开始时间";
	public static final String ALIAS_ACTIVITY_END_TIME = "活动结束时间";
	public static final String ALIAS_ACTIVITY_STATE = "活动状态";
	public static final String ALIAS_EMPIRICAL_VALUE = "参与可获得经验值";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_RESGROUP_ID = "所属分组";
	public static final String ALIAS_CHAPTER_ID = "所属章节";
	

    /**
     * 活动（直播id）       db_column: activity_id 
     */	
 	@NotNull(msg="活动（直播id）不能为空")
 	@MaxLength(value=32, msg="字段[活动（直播id）]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 标题       db_column: activity_title 
     */	
 	@NotNull(msg="标题不能为空")
 	@MaxLength(value=50, msg="字段[标题]超出最大长度[50]")
	private java.lang.String activityTitle;
    /**
     * 内容       db_column: content 
     */	
 	@NotNull(msg="内容不能为空")
 	@MaxLength(value=65535, msg="字段[内容]超出最大长度[65535]")
	private java.lang.String content;
    /**
     * 活动开始时间       db_column: activity_begin_time 
     */	
 	@NotNull(msg="活动开始时间不能为空")
 	@MaxLength(value=20, msg="字段[活动开始时间]超出最大长度[20]")
	private java.lang.String activityBeginTime;
    /**
     * 活动结束时间       db_column: activity_end_time 
     */	
 	@NotNull(msg="活动结束时间不能为空")
 	@MaxLength(value=20, msg="字段[活动结束时间]超出最大长度[20]")
	private java.lang.String activityEndTime;
    /**
     * 活动状态       db_column: activity_state 
     */	
 	@NotNull(msg="活动状态不能为空")
 	@MaxLength(value=2, msg="字段[活动状态]超出最大长度[2]")
	private java.lang.String activityState;
    /**
     * 参与可获得经验值       db_column: empirical_value 
     */	
 	@NotNull(msg="参与可获得经验值不能为空")
 	@MaxLength(value=10, msg="字段[参与可获得经验值]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 所属分组       db_column: resgroup_id 
     */	
 	@NotNull(msg="所属分组不能为空")
 	@MaxLength(value=2, msg="字段[所属分组]超出最大长度[2]")
	private java.lang.String resgroupId;
    /**
     * 所属章节       db_column: chapter_id 
     */	
 	@NotNull(msg="所属章节不能为空")
 	@MaxLength(value=10, msg="字段[所属章节]超出最大长度[10]")
	private java.lang.Integer chapterId;
	//columns END

 	private String pkgId;
 	private String activityStateActual; // 实际活动状态
 	
	public TevglActivityLive(){
	}

	public TevglActivityLive(
		java.lang.String activityId
	){
		this.activityId = activityId;
	}

	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public void setActivityTitle(java.lang.String value) {
		this.activityTitle = value;
	}
	public java.lang.String getActivityTitle() {
		return this.activityTitle;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setActivityBeginTime(java.lang.String value) {
		this.activityBeginTime = value;
	}
	public java.lang.String getActivityBeginTime() {
		return this.activityBeginTime;
	}
	public void setActivityEndTime(java.lang.String value) {
		this.activityEndTime = value;
	}
	public java.lang.String getActivityEndTime() {
		return this.activityEndTime;
	}
	public void setActivityState(java.lang.String value) {
		this.activityState = value;
	}
	public java.lang.String getActivityState() {
		return this.activityState;
	}
	public void setEmpiricalValue(java.lang.Integer value) {
		this.empiricalValue = value;
	}
	public java.lang.Integer getEmpiricalValue() {
		return this.empiricalValue;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setResgroupId(java.lang.String value) {
		this.resgroupId = value;
	}
	public java.lang.String getResgroupId() {
		return this.resgroupId;
	}
	public void setChapterId(java.lang.Integer value) {
		this.chapterId = value;
	}
	public java.lang.Integer getChapterId() {
		return this.chapterId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getActivityStateActual() {
		return activityStateActual;
	}

	public void setActivityStateActual(String activityStateActual) {
		this.activityStateActual = activityStateActual;
	}
}

