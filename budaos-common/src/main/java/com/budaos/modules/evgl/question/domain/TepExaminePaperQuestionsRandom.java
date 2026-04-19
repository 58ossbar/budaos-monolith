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

public class TepExaminePaperQuestionsRandom extends BaseDomain<TepExaminePaperQuestionsRandom>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExaminePaperQuestionsRandom";
	public static final String ALIAS_RD_ID = "主键id";
	public static final String ALIAS_DY_ID = "动态试卷表主键id";
	public static final String ALIAS_DETAIL_ID = "题目id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_QUESTIONS_NUM = "题号";
	

    /**
     * 主键id       db_column: rd_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String rdId;
    /**
     * 动态试卷表主键id       db_column: dy_id 
     */	
 	@NotNull(msg="动态试卷表主键id不能为空")
 	@MaxLength(value=32, msg="字段[动态试卷表主键id]超出最大长度[32]")
	private java.lang.String dyId;
    /**
     * 题目id       db_column: detail_id 
     */	
 	@NotNull(msg="题目id不能为空")
 	@MaxLength(value=32, msg="字段[题目id]超出最大长度[32]")
	private java.lang.String detailId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=0, msg="字段[学员id]超出最大长度[0]")
	private java.lang.String traineeId;
    /**
     * 题号       db_column: questions_num 
     */	
 	@NotNull(msg="题号不能为空")
 	@MaxLength(value=10, msg="字段[题号]超出最大长度[10]")
	private java.lang.Integer questionsNum;
	//columns END

	public TepExaminePaperQuestionsRandom(){
	}

	public TepExaminePaperQuestionsRandom(
		java.lang.String rdId
	){
		this.rdId = rdId;
	}

	public void setRdId(java.lang.String value) {
		this.rdId = value;
	}
	public java.lang.String getRdId() {
		return this.rdId;
	}
	public void setDyId(java.lang.String value) {
		this.dyId = value;
	}
	public java.lang.String getDyId() {
		return this.dyId;
	}
	public void setDetailId(java.lang.String value) {
		this.detailId = value;
	}
	public java.lang.String getDetailId() {
		return this.detailId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setQuestionsNum(java.lang.Integer value) {
		this.questionsNum = value;
	}
	public java.lang.Integer getQuestionsNum() {
		return this.questionsNum;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

