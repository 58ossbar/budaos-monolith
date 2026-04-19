package com.budaos.modules.evgl.activity.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 作业/小组任务</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglActivityTask extends BaseDomain<TevglActivityTask>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityTask";
	public static final String ALIAS_ACTIVITY_ID = "活动ID";
	public static final String ALIAS_RESGROUP_ID = "所属资源分组";
	public static final String ALIAS_ACTIVITY_TITLE = "活动标题";
	public static final String ALIAS_CONTENT = "任务详情(内容)";
	public static final String ALIAS_ACTIVITY_PIC = "活动封面";
	public static final String ALIAS_DIVIDE_GROUP_TYPE = "任务小组划分方式(1不划分2随机划分3线下划分4使用成员小组方案)";
	public static final String ALIAS_SCORE_TYPE = "评分方式、作业分值及评分点(1老师评分2置顶助教/学生评分3学生互评4老师评分，组间评分，组内互评)";
	public static final String ALIAS_IS_SET_LASTEST_SUBMISSION_TIME = "是否设置最晚提交时间(Y/N)";
	public static final String ALIAS_LASTEST_SUBMISSION_TIME = "最晚提交时间";
	public static final String ALIAS_IS_ALLOW_EXCEED_TIME = "是否允许超时提交作业(Y/N)";
	public static final String ALIAS_REFERENCE_ANSWER = "参考答案";
	public static final String ALIAS_EMPIRICAL_VALUE = "参与可获得经验值";
	public static final String ALIAS_ACTIVITY_BEGIN_TIME = "活动开始时间";
	public static final String ALIAS_ACTIVITY_END_TIME = "活动结束时间";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_ACTIVITY_STATE = "活动状态(0未开始1进行中2已结束)";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_ACTIVITY_TYPE = "活动类型(固定值5作业/小组任务)";
	public static final String ALIAS_PURPOSE = "用途(来源字典)";
	public static final String ALIAS_CHAPTER_ID = "所属章节";
	

    /**
     * 活动ID       db_column: activity_id 
     */	
 	@NotNull(msg="活动ID不能为空")
 	@MaxLength(value=32, msg="字段[活动ID]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 所属资源分组       db_column: resgroup_id 
     */	
 	@NotNull(msg="所属资源分组不能为空")
 	@MaxLength(value=32, msg="字段[所属资源分组]超出最大长度[32]")
	private java.lang.String resgroupId;
    /**
     * 活动标题       db_column: activity_title 
     */	
 	@NotNull(msg="活动标题不能为空")
 	@MaxLength(value=100, msg="字段[活动标题]超出最大长度[100]")
	private java.lang.String activityTitle;
    /**
     * 任务详情(内容)       db_column: content 
     */	
 	@NotNull(msg="任务详情(内容)不能为空")
 	@MaxLength(value=65535, msg="字段[任务详情(内容)]超出最大长度[65535]")
	private java.lang.String content;
    /**
     * 活动封面       db_column: activity_pic 
     */	
 	@NotNull(msg="活动封面不能为空")
 	@MaxLength(value=500, msg="字段[活动封面]超出最大长度[500]")
	private java.lang.String activityPic;
    /**
     * 任务小组划分方式(1不划分2随机划分3线下划分4使用成员小组方案)       db_column: divide_group_type 
     */	
 	@NotNull(msg="任务小组划分方式(1不划分2随机划分3线下划分4使用成员小组方案)不能为空")
 	@MaxLength(value=2, msg="字段[任务小组划分方式(1不划分2随机划分3线下划分4使用成员小组方案)]超出最大长度[2]")
	private java.lang.String divideGroupType;
    /**
     * 评分方式、作业分值及评分点(1老师评分2置顶助教/学生评分3学生互评4老师评分，组间评分，组内互评)       db_column: score_type 
     */	
 	@NotNull(msg="评分方式、作业分值及评分点(1老师评分2置顶助教/学生评分3学生互评4老师评分，组间评分，组内互评)不能为空")
 	@MaxLength(value=2, msg="字段[评分方式、作业分值及评分点(1老师评分2置顶助教/学生评分3学生互评4老师评分，组间评分，组内互评)]超出最大长度[2]")
	private java.lang.String scoreType;
    /**
     * 是否设置最晚提交时间(Y/N)       db_column: is_set_lastest_submission_time 
     */	
 	@NotNull(msg="是否设置最晚提交时间(Y/N)不能为空")
 	@MaxLength(value=2, msg="字段[是否设置最晚提交时间(Y/N)]超出最大长度[2]")
	private java.lang.String isSetLastestSubmissionTime;
    /**
     * 最晚提交时间       db_column: lastest_submission_time 
     */	
 	@NotNull(msg="最晚提交时间不能为空")
 	@MaxLength(value=20, msg="字段[最晚提交时间]超出最大长度[20]")
	private java.lang.String lastestSubmissionTime;
    /**
     * 是否允许超时提交作业(Y/N)       db_column: is_allow_exceed_time 
     */	
 	@NotNull(msg="是否允许超时提交作业(Y/N)不能为空")
 	@MaxLength(value=2, msg="字段[是否允许超时提交作业(Y/N)]超出最大长度[2]")
	private java.lang.String isAllowExceedTime;
    /**
     * 参考答案       db_column: reference_answer 
     */	
 	@NotNull(msg="参考答案不能为空")
 	@MaxLength(value=65535, msg="字段[参考答案]超出最大长度[65535]")
	private java.lang.String referenceAnswer;
    /**
     * 参与可获得经验值       db_column: empirical_value 
     */	
 	@NotNull(msg="参与可获得经验值不能为空")
 	@MaxLength(value=10, msg="字段[参与可获得经验值]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
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
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 活动状态(0未开始1进行中2已结束)       db_column: activity_state 
     */	
 	@NotNull(msg="活动状态(0未开始1进行中2已结束)不能为空")
 	@MaxLength(value=2, msg="字段[活动状态(0未开始1进行中2已结束)]超出最大长度[2]")
	private java.lang.String activityState;
 	private java.lang.String activityStateActual; // 实际活动状态
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 活动类型(固定值5作业/小组任务)       db_column: activity_type 
     */	
 	@NotNull(msg="活动类型(固定值5作业/小组任务)不能为空")
 	@MaxLength(value=2, msg="字段[活动类型(固定值5作业/小组任务)]超出最大长度[2]")
	private java.lang.String activityType;
    /**
     * 用途(来源字典)       db_column: purpose 
     */	
 	@NotNull(msg="用途(来源字典)不能为空")
 	@MaxLength(value=2, msg="字段[用途(来源字典)]超出最大长度[2]")
	private java.lang.String purpose;
    /**
     * 所属章节       db_column: chapter_id 
     */	
 	@NotNull(msg="所属章节不能为空")
 	@MaxLength(value=32, msg="字段[所属章节]超出最大长度[32]")
	private java.lang.String chapterId;
 	
 	private java.lang.String media_id;
 	
 	private java.lang.String anMedia_id;
 	
 	/**
 	 * 所属教学包,非数据库字段
 	 */
 	private java.lang.String pkgId;
 	
	//columns END

	public TevglActivityTask(){
	}

	public TevglActivityTask(
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
	public void setResgroupId(java.lang.String value) {
		this.resgroupId = value;
	}
	public java.lang.String getResgroupId() {
		return this.resgroupId;
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
	public void setActivityPic(java.lang.String value) {
		this.activityPic = value;
	}
	public java.lang.String getActivityPic() {
		return this.activityPic;
	}
	public void setDivideGroupType(java.lang.String value) {
		this.divideGroupType = value;
	}
	public java.lang.String getDivideGroupType() {
		return this.divideGroupType;
	}
	public void setScoreType(java.lang.String value) {
		this.scoreType = value;
	}
	public java.lang.String getScoreType() {
		return this.scoreType;
	}
	public void setIsSetLastestSubmissionTime(java.lang.String value) {
		this.isSetLastestSubmissionTime = value;
	}
	public java.lang.String getIsSetLastestSubmissionTime() {
		return this.isSetLastestSubmissionTime;
	}
	public void setLastestSubmissionTime(java.lang.String value) {
		this.lastestSubmissionTime = value;
	}
	public java.lang.String getLastestSubmissionTime() {
		return this.lastestSubmissionTime;
	}
	public void setIsAllowExceedTime(java.lang.String value) {
		this.isAllowExceedTime = value;
	}
	public java.lang.String getIsAllowExceedTime() {
		return this.isAllowExceedTime;
	}
	public void setReferenceAnswer(java.lang.String value) {
		this.referenceAnswer = value;
	}
	public java.lang.String getReferenceAnswer() {
		return this.referenceAnswer;
	}
	public void setEmpiricalValue(java.lang.Integer value) {
		this.empiricalValue = value;
	}
	public java.lang.Integer getEmpiricalValue() {
		return this.empiricalValue;
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
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setActivityState(java.lang.String value) {
		this.activityState = value;
	}
	public java.lang.String getActivityState() {
		return this.activityState;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setActivityType(java.lang.String value) {
		this.activityType = value;
	}
	public java.lang.String getActivityType() {
		return this.activityType;
	}
	public void setPurpose(java.lang.String value) {
		this.purpose = value;
	}
	public java.lang.String getPurpose() {
		return this.purpose;
	}
	public void setChapterId(java.lang.String value) {
		this.chapterId = value;
	}
	public java.lang.String getChapterId() {
		return this.chapterId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.String getActivityStateActual() {
		return activityStateActual;
	}

	public void setActivityStateActual(java.lang.String activityStateActual) {
		this.activityStateActual = activityStateActual;
	}

	public java.lang.String getPkgId() {
		return pkgId;
	}

	public void setPkgId(java.lang.String pkgId) {
		this.pkgId = pkgId;
	}

	public java.lang.String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(java.lang.String media_id) {
		this.media_id = media_id;
	}

	public java.lang.String getAnMedia_id() {
		return anMedia_id;
	}

	public void setAnMedia_id(java.lang.String anMedia_id) {
		this.anMedia_id = anMedia_id;
	}
	
	
}

