package com.budaos.modules.evgl.question.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TepExamineDynamicQuestionsOptions extends BaseDomain<TepExamineDynamicQuestionsOptions>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExamineDynamicQuestionsOptions";
	public static final String ALIAS_DQO_ID = "dqo_id(主键ID)";
	public static final String ALIAS_DY_ID = "dy_id";
	public static final String ALIAS_QUESTIONS_ID = "题目ID";
	public static final String ALIAS_REPLYS_ID = "选项表主键iD(,隔开)";
	public static final String ALIAS_QUESTIONS_NUMBER = "题号";
	public static final String ALIAS_QUESTIONS_SCORE = "题目分数";
	

    /**
     * dqo_id(主键ID)       db_column: dqo_id 
     */	
 	//@NotNull(msg="dqo_id(主键ID)不能为空")
 	@MaxLength(value=32, msg="字段[dqo_id(主键ID)]超出最大长度[32]")
	private java.lang.String dqoId;
    /**
     * dy_id       db_column: dy_id 
     */	
 	//@NotNull(msg="dy_id不能为空")
 	@MaxLength(value=32, msg="字段[dy_id]超出最大长度[32]")
	private java.lang.String dyId;
    /**
     * 题目ID       db_column: questions_id 
     */	
 	//@NotNull(msg="题目ID不能为空")
 	@MaxLength(value=32, msg="字段[题目ID]超出最大长度[32]")
	private java.lang.String questionsId;
    /**
     * 选项表主键iD(,隔开)       db_column: replys_id 
     */	
 	//@NotNull(msg="选项表主键iD(,隔开)不能为空")
 	@MaxLength(value=500, msg="字段[选项表主键iD(,隔开)]超出最大长度[500]")
	private java.lang.String replysId;
    /**
     * 题号       db_column: questions_number 
     */	
 	//@NotNull(msg="题号不能为空")
 	@MaxLength(value=5, msg="字段[题号]超出最大长度[5]")
	private java.lang.String questionsNumber;
    /**
     * 题目分数       db_column: questions_score 
     */	
 	//@NotNull(msg="题目分数不能为空")
 	@MaxLength(value=5, msg="字段[题目分数]超出最大长度[5]")
	private java.lang.String questionsScore;
	//columns END

	public TepExamineDynamicQuestionsOptions(){
	}

	public TepExamineDynamicQuestionsOptions(
		java.lang.String dqoId
	){
		this.dqoId = dqoId;
	}

	public void setDqoId(java.lang.String value) {
		this.dqoId = value;
	}
	public java.lang.String getDqoId() {
		return this.dqoId;
	}
	public void setDyId(java.lang.String value) {
		this.dyId = value;
	}
	public java.lang.String getDyId() {
		return this.dyId;
	}
	public void setQuestionsId(java.lang.String value) {
		this.questionsId = value;
	}
	public java.lang.String getQuestionsId() {
		return this.questionsId;
	}
	public void setReplysId(java.lang.String value) {
		this.replysId = value;
	}
	public java.lang.String getReplysId() {
		return this.replysId;
	}
	public void setQuestionsNumber(java.lang.String value) {
		this.questionsNumber = value;
	}
	public java.lang.String getQuestionsNumber() {
		return this.questionsNumber;
	}
	public void setQuestionsScore(java.lang.String value) {
		this.questionsScore = value;
	}
	public java.lang.String getQuestionsScore() {
		return this.questionsScore;
	}
	
	private TepExamineDynamicPaper tepExamineDynamicPaper;
	
	public void setTepExamineDynamicPaper(TepExamineDynamicPaper tepExamineDynamicPaper){
		this.tepExamineDynamicPaper = tepExamineDynamicPaper;
	}
	
	public TepExamineDynamicPaper getTepExamineDynamicPaper() {
		return tepExamineDynamicPaper;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

