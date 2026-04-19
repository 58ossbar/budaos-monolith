package com.budaos.modules.evgl.examine.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 评测中心-试卷成绩表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglExaminePaperScore extends BaseDomain<TevglExaminePaperScore>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglExaminePaperScore";
	public static final String ALIAS_SCORE_ID = "主键ID";
	public static final String ALIAS_HISTORY_ID = "history_id(主键ID)";
	public static final String ALIAS_QUESTIONS_ID = "题目ID";
	public static final String ALIAS_TRAINEE_ID = "学员ID";
	public static final String ALIAS_REPLY_ID = "学员作题答案";
	public static final String ALIAS_QUESTIONS_SCORE = "题目实际得分（题目每题分值）";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_TRAINEE_TYPE = "学员类型";
	public static final String ALIAS_IS_CORRECT = "回答是否正确(Y/N)";
	public static final String ALIAS_QUESTIONS_NUM = "题目序号";
	public static final String ALIAS_CONTENT = "简答题作答内容";
	public static final String ALIAS_SCORE = "学员简答题得分";
	

    /**
     * 主键ID       db_column: score_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String scoreId;
    /**
     * history_id(主键ID)       db_column: history_id 
     */	
 	@NotNull(msg="history_id(主键ID)不能为空")
 	@MaxLength(value=32, msg="字段[history_id(主键ID)]超出最大长度[32]")
	private java.lang.String historyId;
    /**
     * 题目ID       db_column: questions_id 
     */	
 	@NotNull(msg="题目ID不能为空")
 	@MaxLength(value=32, msg="字段[题目ID]超出最大长度[32]")
	private java.lang.String questionsId;
    /**
     * 学员ID       db_column: trainee_id 
     */	
 	@NotNull(msg="学员ID不能为空")
 	@MaxLength(value=32, msg="字段[学员ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 学员作题答案       db_column: reply_id 
     */	
 	@NotNull(msg="学员作题答案不能为空")
 	@MaxLength(value=200, msg="字段[学员作题答案]超出最大长度[200]")
	private java.lang.String replyId;
    /**
     * 题目实际得分（题目每题分值）       db_column: questions_score 
     */	
 	@NotNull(msg="题目实际得分（题目每题分值）不能为空")
 	@MaxLength(value=5, msg="字段[题目实际得分（题目每题分值）]超出最大长度[5]")
	private java.lang.String questionsScore;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 学员类型       db_column: trainee_type 
     */	
 	@NotNull(msg="学员类型不能为空")
 	@MaxLength(value=10, msg="字段[学员类型]超出最大长度[10]")
	private java.lang.String traineeType;
    /**
     * 回答是否正确(Y/N)       db_column: is_correct 
     */	
 	@NotNull(msg="回答是否正确(Y/N)不能为空")
 	@MaxLength(value=10, msg="字段[回答是否正确(Y/N)]超出最大长度[10]")
	private java.lang.String isCorrect;
    /**
     * 题目序号       db_column: questions_num 
     */	
 	@NotNull(msg="题目序号不能为空")
 	@MaxLength(value=10, msg="字段[题目序号]超出最大长度[10]")
	private java.lang.String questionsNum;
    /**
     * 简答题作答内容       db_column: content 
     */	
 	@NotNull(msg="简答题作答内容不能为空")
 	@MaxLength(value=65535, msg="字段[简答题作答内容]超出最大长度[65535]")
	private java.lang.String content;
    /**
     * 学员简答题得分       db_column: score 
     */	
 	@NotNull(msg="学员简答题得分不能为空")
 	@MaxLength(value=10, msg="字段[学员简答题得分]超出最大长度[10]")
	private java.lang.String score;
	//columns END
 	
 	// {"id": "", "content": ""}
  	private List<Map<String, Object>> gapFillingList;

	public TevglExaminePaperScore(){
	}

	public TevglExaminePaperScore(
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
	public void setHistoryId(java.lang.String value) {
		this.historyId = value;
	}
	public java.lang.String getHistoryId() {
		return this.historyId;
	}
	public void setQuestionsId(java.lang.String value) {
		this.questionsId = value;
	}
	public java.lang.String getQuestionsId() {
		return this.questionsId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setReplyId(java.lang.String value) {
		this.replyId = value;
	}
	public java.lang.String getReplyId() {
		return this.replyId;
	}
	public void setQuestionsScore(java.lang.String value) {
		this.questionsScore = value;
	}
	public java.lang.String getQuestionsScore() {
		return this.questionsScore;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setTraineeType(java.lang.String value) {
		this.traineeType = value;
	}
	public java.lang.String getTraineeType() {
		return this.traineeType;
	}
	public void setIsCorrect(java.lang.String value) {
		this.isCorrect = value;
	}
	public java.lang.String getIsCorrect() {
		return this.isCorrect;
	}
	public void setQuestionsNum(java.lang.String value) {
		this.questionsNum = value;
	}
	public java.lang.String getQuestionsNum() {
		return this.questionsNum;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setScore(java.lang.String value) {
		this.score = value;
	}
	public java.lang.String getScore() {
		return this.score;
	}
	public List<Map<String, Object>> getGapFillingList() {
		return gapFillingList;
	}

	public void setGapFillingList(List<Map<String, Object>> gapFillingList) {
		this.gapFillingList = gapFillingList;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

