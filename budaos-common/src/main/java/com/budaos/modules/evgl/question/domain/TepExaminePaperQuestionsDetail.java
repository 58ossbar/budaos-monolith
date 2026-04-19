package com.budaos.modules.evgl.question.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 试卷题目详情表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TepExaminePaperQuestionsDetail extends BaseDomain<TepExaminePaperQuestionsDetail>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExaminePaperQuestionsDetail";
	public static final String ALIAS_DETAIL_ID = "detail_id(主键ID)";
	public static final String ALIAS_PAPER_ID = "试卷ID";
	public static final String ALIAS_QUESTIONS_ID = "题目ID";
	public static final String ALIAS_QUESTIONS_SCORE = "题目分值";
	public static final String ALIAS_QUESTIONS_NUMBER = "题号";
	public static final String ALIAS_PARENT_ID = "复合题时，对应的主题目";
	

    /**
     * detail_id(主键ID)       db_column: detail_id 
     */	
 	//@NotNull(msg="detail_id(主键ID)不能为空")
 	@MaxLength(value=32, msg="字段[detail_id(主键ID)]超出最大长度[32]")
	private java.lang.String detailId;
    /**
     * 试卷ID       db_column: paper_id 
     */	
 	//@NotNull(msg="试卷ID不能为空")
 	@MaxLength(value=32, msg="字段[试卷ID]超出最大长度[32]")
	private java.lang.String paperId;
    /**
     * 题目ID       db_column: questions_id 
     */	
 	//@NotNull(msg="题目ID不能为空")
 	@MaxLength(value=32, msg="字段[题目ID]超出最大长度[32]")
	private java.lang.String questionsId;
    /**
     * 题目分数       db_column: questions_score 
     */	
 	//@NotNull(msg="题目分数不能为空")
 	@MaxLength(value=5, msg="字段[题目分数]超出最大长度[5]")
	private java.lang.String questionsScore;
    /**
     * 题号       db_column: questions_number 
     */	
 	//@NotNull(msg="题号不能为空")
 	@MaxLength(value=5, msg="字段[题号]超出最大长度[5]")
	private java.lang.Integer questionsNumber;
 	/**
     * 复合题时，对应的主题目       db_column: parent_id 
     */	
 	//@NotNull(msg="复合题时，对应的主题目不能为空")
 	@MaxLength(value=0, msg="字段[复合题时，对应的主题目]超出最大长度[0]")
	private java.lang.String parentId;
	//columns END

	public TepExaminePaperQuestionsDetail(){
	}

	public TepExaminePaperQuestionsDetail(
		java.lang.String detailId
	){
		this.detailId = detailId;
	}

	public void setDetailId(java.lang.String value) {
		this.detailId = value;
	}
	public java.lang.String getDetailId() {
		return this.detailId;
	}
	public void setPaperId(java.lang.String value) {
		this.paperId = value;
	}
	public java.lang.String getPaperId() {
		return this.paperId;
	}
	public void setQuestionsId(java.lang.String value) {
		this.questionsId = value;
	}
	public java.lang.String getQuestionsId() {
		return this.questionsId;
	}
	public void setQuestionsScore(java.lang.String value) {
		this.questionsScore = value;
	}
	public java.lang.String getQuestionsScore() {
		return this.questionsScore;
	}
	public void setQuestionsNumber(java.lang.Integer value) {
		this.questionsNumber = value;
	}
	public java.lang.Integer getQuestionsNumber() {
		return this.questionsNumber;
	}
	public java.lang.String getParentId() {
		return parentId;
	}
	public void setParentId(java.lang.String parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

