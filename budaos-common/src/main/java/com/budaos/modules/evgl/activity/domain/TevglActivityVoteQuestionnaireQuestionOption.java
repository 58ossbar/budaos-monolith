package com.budaos.modules.evgl.activity.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 活动-投票/问卷 -> 题目选项</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglActivityVoteQuestionnaireQuestionOption extends BaseDomain<TevglActivityVoteQuestionnaireQuestionOption>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityVoteQuestionnaireQuestionOption";
	public static final String ALIAS_OPTION_ID = "选项ID";
	public static final String ALIAS_QUESTION_ID = "题目ID";
	public static final String ALIAS_OPTION_CODE = "选项编码";
	public static final String ALIAS_OPTION_NAME = "选项名称";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_CAN_FILL = "作答时，是否可在后面填写内容";
	

    /**
     * 选项ID       db_column: option_id 
     */	
 	@NotNull(msg="选项ID不能为空")
 	@MaxLength(value=32, msg="字段[选项ID]超出最大长度[32]")
	private java.lang.String optionId;
    /**
     * 题目ID       db_column: question_id 
     */	
 	@NotNull(msg="题目ID不能为空")
 	@MaxLength(value=32, msg="字段[题目ID]超出最大长度[32]")
	private java.lang.String questionId;
    /**
     * 选项编码       db_column: option_code 
     */	
 	@NotNull(msg="选项编码不能为空")
 	@MaxLength(value=2, msg="字段[选项编码]超出最大长度[2]")
	private java.lang.String optionCode;
    /**
     * 选项名称       db_column: option_name 
     */	
 	@NotNull(msg="选项名称不能为空")
 	@MaxLength(value=65535, msg="字段[选项名称]超出最大长度[65535]")
	private java.lang.String optionName;
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
 	 /**
     * 是否为正确选项(Y/N)      db_column: is_right 
     */	
 	//@NotNull(msg="是否为正确选项(Y/N)不能为空")
 	@MaxLength(value=2, msg="字段[是否为正确选项(Y/N)]超出最大长度[2]")
	private java.lang.String isRight;
 	/**
     * 作答时，是否可在后面填写内容       db_column: can_fill 
     */	
 	@NotNull(msg="作答时，是否可在后面填写内容不能为空")
 	@MaxLength(value=2, msg="字段[作答时，是否可在后面填写内容]超出最大长度[2]")
	private java.lang.String canFill;
	//columns END

	public TevglActivityVoteQuestionnaireQuestionOption(){
	}

	public TevglActivityVoteQuestionnaireQuestionOption(
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
	public void setQuestionId(java.lang.String value) {
		this.questionId = value;
	}
	public java.lang.String getQuestionId() {
		return this.questionId;
	}
	public void setOptionCode(java.lang.String value) {
		this.optionCode = value;
	}
	public java.lang.String getOptionCode() {
		return this.optionCode;
	}
	public void setOptionName(java.lang.String value) {
		this.optionName = value;
	}
	public java.lang.String getOptionName() {
		return this.optionName;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	private TevglActivityVoteQuestionnaireQuestion tevglActivityVoteQuestionnaireQuestion;
	
	public void setTevglActivityVoteQuestionnaireQuestion(TevglActivityVoteQuestionnaireQuestion tevglActivityVoteQuestionnaireQuestion){
		this.tevglActivityVoteQuestionnaireQuestion = tevglActivityVoteQuestionnaireQuestion;
	}
	
	public TevglActivityVoteQuestionnaireQuestion getTevglActivityVoteQuestionnaireQuestion() {
		return tevglActivityVoteQuestionnaireQuestion;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.String getIsRight() {
		return isRight;
	}

	public void setIsRight(java.lang.String isRight) {
		this.isRight = isRight;
	}

	public java.lang.String getCanFill() {
		return canFill;
	}

	public void setCanFill(java.lang.String canFill) {
		this.canFill = canFill;
	}

}

