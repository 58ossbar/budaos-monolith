package com.budaos.modules.evgl.activity.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 活动之投票/问卷---学员作答信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglActivityVoteQuestionnaireTraineeAnswer extends BaseDomain<TevglActivityVoteQuestionnaireTraineeAnswer>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityVoteQuestionnaireTraineeAnswer";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_CT_ID = "ctId";
	public static final String ALIAS_ACTIVITY_ID = "activityId";
	public static final String ALIAS_TRAINEE_ID = "traineeId";
	public static final String ALIAS_QUESTION_ID = "questionId";
	public static final String ALIAS_OPTION_ID = "optionId";
	public static final String ALIAS_CONTENT = "content";
	public static final String ALIAS_QUESTION_TYPE = "questionType";
	

    /**
     * id       db_column: id 
     */	
 	@NotNull(msg="id不能为空")
 	@MaxLength(value=32, msg="字段[id]超出最大长度[32]")
	private java.lang.String id;
    /**
     * ctId       db_column: ct_id 
     */	
 	@NotNull(msg="ctId不能为空")
 	@MaxLength(value=32, msg="字段[ctId]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * activityId       db_column: activity_id 
     */	
 	@NotNull(msg="activityId不能为空")
 	@MaxLength(value=32, msg="字段[activityId]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * traineeId       db_column: trainee_id 
     */	
 	@NotNull(msg="traineeId不能为空")
 	@MaxLength(value=32, msg="字段[traineeId]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * questionId       db_column: question_id 
     */	
 	@NotNull(msg="questionId不能为空")
 	@MaxLength(value=32, msg="字段[questionId]超出最大长度[32]")
	private java.lang.String questionId;
    /**
     * optionId       db_column: option_id 
     */	
 	@NotNull(msg="optionId不能为空")
 	@MaxLength(value=1000, msg="字段[optionId]超出最大长度[1000]")
	private java.lang.String optionId;
    /**
     * content       db_column: content 
     */	
 	@NotNull(msg="content不能为空")
 	@MaxLength(value=65535, msg="字段[content]超出最大长度[65535]")
	private java.lang.String content;
    /**
     * questionType       db_column: question_type 
     */	
 	@NotNull(msg="questionType不能为空")
 	@MaxLength(value=2, msg="字段[questionType]超出最大长度[2]")
	private java.lang.String questionType;
	//columns END

	public TevglActivityVoteQuestionnaireTraineeAnswer(){
	}

	public TevglActivityVoteQuestionnaireTraineeAnswer(
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
	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setQuestionId(java.lang.String value) {
		this.questionId = value;
	}
	public java.lang.String getQuestionId() {
		return this.questionId;
	}
	public void setOptionId(java.lang.String value) {
		this.optionId = value;
	}
	public java.lang.String getOptionId() {
		return this.optionId;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setQuestionType(java.lang.String value) {
		this.questionType = value;
	}
	public java.lang.String getQuestionType() {
		return this.questionType;
	}
	
	private TevglActivityVoteQuestionnaire tevglActivityVoteQuestionnaire;
	
	public void setTevglActivityVoteQuestionnaire(TevglActivityVoteQuestionnaire tevglActivityVoteQuestionnaire){
		this.tevglActivityVoteQuestionnaire = tevglActivityVoteQuestionnaire;
	}
	
	public TevglActivityVoteQuestionnaire getTevglActivityVoteQuestionnaire() {
		return tevglActivityVoteQuestionnaire;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

