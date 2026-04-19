package com.budaos.modules.evgl.question.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 题目选项表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglQuestionChose extends BaseDomain<TevglQuestionChose>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglQuestionChose";
	public static final String ALIAS_OPTION_ID = "选项ID(主键ID)";
	public static final String ALIAS_QUESTIONS_ID = "题目ID";
	public static final String ALIAS_CODE = "题目选项编码";
	public static final String ALIAS_CONTENT = "选项内容";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_SORT_NUM = "填空题时严格按照顺序的正确答案";
	
	 /**
     * 题目ID       db_column: questions_id 
     */	
 	@NotNull(msg="题目ID不能为空")
 	@MaxLength(value=32, msg="字段[题目ID]超出最大长度[32]")
	private java.lang.String optionId;
 	
    /**
     * 题目ID       db_column: questions_id 
     */	
 	@NotNull(msg="题目ID不能为空")
 	@MaxLength(value=32, msg="字段[题目ID]超出最大长度[32]")
	private java.lang.String questionsId;
    /**
     * 题目选项编码       db_column: code 
     */	
 	@NotNull(msg="题目选项编码不能为空")
 	@MaxLength(value=2, msg="字段[题目选项编码]超出最大长度[2]")
	private java.lang.String code;
    /**
     * 选项内容       db_column: content 
     */	
 	@NotNull(msg="选项内容不能为空")
 	@MaxLength(value=2147483647, msg="字段[选项内容]超出最大长度[2147483647]")
	private java.lang.String content;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 填空题时严格按照顺序的正确答案       db_column: sort_num 
     */	
 	@NotNull(msg="填空题时严格按照顺序的正确答案不能为空")
 	@MaxLength(value=10, msg="字段[填空题时严格按照顺序的正确答案]超出最大长度[10]")
	private java.lang.Integer sortNum;
 	
 	private boolean isModelAnswer;
 	
	//columns END

	public TevglQuestionChose(){
	}

	public TevglQuestionChose(
		java.lang.String optionId
	){
		this.optionId = optionId;
	}

	public void setOptionId(java.lang.String value) {
		this.optionId = value;
	}
	public java.lang.String getOptionId() {
		return this.optionId;
	}
	public void setQuestionsId(java.lang.String value) {
		this.questionsId = value;
	}
	public java.lang.String getQuestionsId() {
		return this.questionsId;
	}
	public void setCode(java.lang.String value) {
		this.code = value;
	}
	public java.lang.String getCode() {
		return this.code;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public java.lang.Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(java.lang.Integer sortNum) {
		this.sortNum = sortNum;
	}
	public boolean isModelAnswer() {
		return isModelAnswer;
	}
	public void setModelAnswer(boolean isModelAnswer) {
		this.isModelAnswer = isModelAnswer;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

