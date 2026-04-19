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

public class TevglQuestionsRecoveryError extends BaseDomain<TevglQuestionsRecoveryError>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglQuestionsRecoveryError";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_QUESTION_ID = "被纠错的题目id";
	public static final String ALIAS_CONTENT = "纠错理由";
	public static final String ALIAS_STATE = "处理状态（Y已处理N未处理）";
	

    /**
     * id       db_column: id 
     */	
 	@NotNull(msg="id不能为空")
 	@MaxLength(value=32, msg="字段[id]超出最大长度[32]")
	private java.lang.String id;
    /**
     * 被纠错的题目id       db_column: question_id 
     */	
 	@NotNull(msg="被纠错的题目id不能为空")
 	@MaxLength(value=32, msg="字段[被纠错的题目id]超出最大长度[32]")
	private java.lang.String questionId;
    /**
     * 纠错理由       db_column: content 
     */	
 	@NotNull(msg="纠错理由不能为空")
 	@MaxLength(value=65535, msg="字段[纠错理由]超出最大长度[65535]")
	private java.lang.String content;
    /**
     * 处理状态（Y已处理N未处理）       db_column: state 
     */	
 	@NotNull(msg="处理状态（Y已处理N未处理）不能为空")
 	@MaxLength(value=2, msg="字段[处理状态（Y已处理N未处理）]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglQuestionsRecoveryError(){
	}

	public TevglQuestionsRecoveryError(
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
	public void setQuestionId(java.lang.String value) {
		this.questionId = value;
	}
	public java.lang.String getQuestionId() {
		return this.questionId;
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
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

