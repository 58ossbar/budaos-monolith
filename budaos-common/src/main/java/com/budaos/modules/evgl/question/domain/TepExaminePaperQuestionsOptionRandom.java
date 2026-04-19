package com.budaos.modules.evgl.question.domain;

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

public class TepExaminePaperQuestionsOptionRandom extends BaseDomain<TepExaminePaperQuestionsOptionRandom>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExaminePaperQuestionsOptionRandom";
	public static final String ALIAS_DY_ID = "动态试卷表主键id";
	public static final String ALIAS_RD_ID = "主键id";
	public static final String ALIAS_OPTION_ID = "选项id";
	public static final String ALIAS_QUESTION_ID = "题目id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_OPTION_NUM = "题号";
	

    /**
     * 动态试卷表主键id       db_column: dy_id 
     */	
 	@NotNull(msg="动态试卷表主键id不能为空")
 	@MaxLength(value=32, msg="字段[动态试卷表主键id]超出最大长度[32]")
	private java.lang.String dyId;
    /**
     * 主键id       db_column: rd_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String rdId;
    /**
     * 选项id       db_column: option_id 
     */	
 	@NotNull(msg="选项id不能为空")
 	@MaxLength(value=32, msg="字段[选项id]超出最大长度[32]")
	private java.lang.String optionId;
    /**
     * 题目id       db_column: question_id 
     */	
 	@NotNull(msg="题目id不能为空")
 	@MaxLength(value=32, msg="字段[题目id]超出最大长度[32]")
	private java.lang.String questionId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 题号       db_column: option_num 
     */	
 	@NotNull(msg="题号不能为空")
 	@MaxLength(value=10, msg="字段[题号]超出最大长度[10]")
	private java.lang.Integer optionNum;
	//columns END

	public TepExaminePaperQuestionsOptionRandom(){
	}

	public TepExaminePaperQuestionsOptionRandom(
		java.lang.String rdId
	){
		this.rdId = rdId;
	}

	public void setDyId(java.lang.String value) {
		this.dyId = value;
	}
	public java.lang.String getDyId() {
		return this.dyId;
	}
	public void setRdId(java.lang.String value) {
		this.rdId = value;
	}
	public java.lang.String getRdId() {
		return this.rdId;
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
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setOptionNum(java.lang.Integer value) {
		this.optionNum = value;
	}
	public java.lang.Integer getOptionNum() {
		return this.optionNum;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

