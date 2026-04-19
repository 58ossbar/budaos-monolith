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

public class TepExaminePaperScoreGapAmend extends BaseDomain<TepExaminePaperScoreGapAmend>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExaminePaperScoreGapAmend";
	public static final String ALIAS_AM_ID = "amId";
	public static final String ALIAS_SCORE_ID = "关联得分表主键id";
	public static final String ALIAS_SCORE = "老师人工给的分值";
	public static final String ALIAS_REASON = "给出的原因";
	

    /**
     * amId       db_column: am_id 
     */	
 	@NotNull(msg="amId不能为空")
 	@MaxLength(value=32, msg="字段[amId]超出最大长度[32]")
	private java.lang.String amId;
    /**
     * 关联得分表主键id       db_column: score_id 
     */	
 	@NotNull(msg="关联得分表主键id不能为空")
 	@MaxLength(value=32, msg="字段[关联得分表主键id]超出最大长度[32]")
	private java.lang.String scoreId;
    /**
     * 老师人工给的分值       db_column: score 
     */	
 	@NotNull(msg="老师人工给的分值不能为空")
 	@MaxLength(value=5, msg="字段[老师人工给的分值]超出最大长度[5]")
	private java.lang.String score;
    /**
     * 给出的原因       db_column: reason 
     */	
 	@NotNull(msg="给出的原因不能为空")
 	@MaxLength(value=500, msg="字段[给出的原因]超出最大长度[500]")
	private java.lang.String reason;
	//columns END

	public TepExaminePaperScoreGapAmend(){
	}

	public TepExaminePaperScoreGapAmend(
		java.lang.String amId
	){
		this.amId = amId;
	}

	public void setAmId(java.lang.String value) {
		this.amId = value;
	}
	public java.lang.String getAmId() {
		return this.amId;
	}
	public void setScoreId(java.lang.String value) {
		this.scoreId = value;
	}
	public java.lang.String getScoreId() {
		return this.scoreId;
	}
	public void setScore(java.lang.String value) {
		this.score = value;
	}
	public java.lang.String getScore() {
		return this.score;
	}
	public void setReason(java.lang.String value) {
		this.reason = value;
	}
	public java.lang.String getReason() {
		return this.reason;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

