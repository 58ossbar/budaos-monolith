package com.budaos.modules.evgl.question.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 学员答卷历史表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TepExamineHistoryPaper extends BaseDomain<TepExamineHistoryPaper>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExamineHistoryPaper";
	public static final String ALIAS_HISTORY_ID = "history_id(主键ID)";
	public static final String ALIAS_DY_ID = "dy_id(动态试卷表主键ID)";
	public static final String ALIAS_TRAINEE_ID = "学员ID";
	public static final String ALIAS_PAPER_BEGIN_TIME = "学员答卷时间(开卷时间)";
	public static final String ALIAS_PAPER_END_TIME = "学员交卷时间";
	public static final String ALIAS_PAPER_FINAL_SCORE = "学员试卷总得分";
	public static final String ALIAS_PAPER_ACCURACY = "学员该试卷正确率";
	

    /**
     * history_id(主键ID)       db_column: history_id 
     */	
 	//@NotNull(msg="history_id(主键ID)不能为空")
 	@MaxLength(value=32, msg="字段[history_id(主键ID)]超出最大长度[32]")
	private java.lang.String historyId;
    /**
     * dy_id(动态试卷表主键ID)       db_column: dy_id 
     */	
 	//@NotNull(msg="dy_id(动态试卷表主键ID)不能为空")
 	@MaxLength(value=32, msg="字段[dy_id(动态试卷表主键ID)]超出最大长度[32]")
	private java.lang.String dyId;
    /**
     * 学员ID       db_column: trainee_id 
     */	
 	//@NotNull(msg="学员ID不能为空")
 	@MaxLength(value=32, msg="字段[学员ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 学员答卷时间(开卷时间)       db_column: paper_begin_time 
     */	
 	//@NotNull(msg="学员答卷时间(开卷时间)不能为空")
 	@MaxLength(value=20, msg="字段[学员答卷时间(开卷时间)]超出最大长度[20]")
	private java.lang.String paperBeginTime;
    /**
     * 学员交卷时间       db_column: paper_end_time 
     */	
 	//@NotNull(msg="学员交卷时间不能为空")
 	@MaxLength(value=20, msg="字段[学员交卷时间]超出最大长度[20]")
	private java.lang.String paperEndTime;
    /**
     * 学员试卷总得分       db_column: paper_final_score 
     */	
 	//@NotNull(msg="学员试卷总得分不能为空")
 	@MaxLength(value=6, msg="字段[学员试卷总得分]超出最大长度[6]")
	private java.lang.String paperFinalScore;
    /**
     * 学员该试卷正确率       db_column: paper_accuracy 
     */	
 	//@NotNull(msg="学员该试卷正确率不能为空")
 	@MaxLength(value=6, msg="字段[学员该试卷正确率]超出最大长度[6]")
	private java.lang.String paperAccuracy;
	//columns END

	public TepExamineHistoryPaper(){
	}

	public TepExamineHistoryPaper(
		java.lang.String historyId
	){
		this.historyId = historyId;
	}

	public void setHistoryId(java.lang.String value) {
		this.historyId = value;
	}
	public java.lang.String getHistoryId() {
		return this.historyId;
	}
	public void setDyId(java.lang.String value) {
		this.dyId = value;
	}
	public java.lang.String getDyId() {
		return this.dyId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setPaperBeginTime(java.lang.String value) {
		this.paperBeginTime = value;
	}
	public java.lang.String getPaperBeginTime() {
		return this.paperBeginTime;
	}
	public void setPaperEndTime(java.lang.String value) {
		this.paperEndTime = value;
	}
	public java.lang.String getPaperEndTime() {
		return this.paperEndTime;
	}
	public void setPaperFinalScore(java.lang.String value) {
		this.paperFinalScore = value;
	}
	public java.lang.String getPaperFinalScore() {
		return this.paperFinalScore;
	}
	public void setPaperAccuracy(java.lang.String value) {
		this.paperAccuracy = value;
	}
	public java.lang.String getPaperAccuracy() {
		return this.paperAccuracy;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

