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

public class TepExaminePaperScoreGapfilling extends BaseDomain<TepExaminePaperScoreGapfilling>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExaminePaperScoreGapfilling";
	public static final String ALIAS_ID = "主键id";
	public static final String ALIAS_HISTORY_ID = "历史试卷id";
	public static final String ALIAS_SCORE_ID = "得分id";
	public static final String ALIAS_QUESTIONS_ID = "题目id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_CONTENT = "填空题单空作答记录";
	public static final String ALIAS_SORT_NUM = "填空作答的顺序";
	

    /**
     * 主键id       db_column: id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String id;
    /**
     * 历史试卷id       db_column: history_id 
     */	
 	@NotNull(msg="历史试卷id不能为空")
 	@MaxLength(value=32, msg="字段[历史试卷id]超出最大长度[32]")
	private java.lang.String historyId;
    /**
     * 得分id       db_column: score_id 
     */	
 	@NotNull(msg="得分id不能为空")
 	@MaxLength(value=32, msg="字段[得分id]超出最大长度[32]")
	private java.lang.String scoreId;
    /**
     * 题目id       db_column: questions_id 
     */	
 	@NotNull(msg="题目id不能为空")
 	@MaxLength(value=32, msg="字段[题目id]超出最大长度[32]")
	private java.lang.String questionsId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 填空题单空作答记录       db_column: content 
     */	
 	@NotNull(msg="填空题单空作答记录不能为空")
 	@MaxLength(value=500, msg="字段[填空题单空作答记录]超出最大长度[500]")
	private java.lang.String content;
    /**
     * 填空作答的顺序       db_column: sort_num 
     */	
 	@NotNull(msg="填空作答的顺序不能为空")
 	@MaxLength(value=10, msg="字段[填空作答的顺序]超出最大长度[10]")
	private java.lang.Integer sortNum;
	//columns END

	public TepExaminePaperScoreGapfilling(){
	}

	public TepExaminePaperScoreGapfilling(
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
	public void setHistoryId(java.lang.String value) {
		this.historyId = value;
	}
	public java.lang.String getHistoryId() {
		return this.historyId;
	}
	public void setScoreId(java.lang.String value) {
		this.scoreId = value;
	}
	public java.lang.String getScoreId() {
		return this.scoreId;
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
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

