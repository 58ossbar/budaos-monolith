package com.budaos.modules.evgl.question.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p>
 * Title:试卷表基本信息
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TepExaminePaperInfo extends BaseDomain<TepExaminePaperInfo> {
	private static final long serialVersionUID = 1L;

	// alias
	public static final String TABLE_ALIAS = "TepExaminePaperInfo";
	public static final String ALIAS_PAPER_ID = "试卷ID(主键ID)";
	public static final String ALIAS_PAPER_NAME = "试卷名称";
	public static final String ALIAS_PAPER_TYPE = "试卷类型";
	public static final String ALIAS_PAPER_TOTAL_SCORE = "试卷总分";
	public static final String ALIAS_PAPER_PRACTICE_TIME = "试卷作答数(练习次数)";
	public static final String ALIAS_PAPER_STATE = "试卷状态(Y有效N无效)";
	public static final String ALIAS_PAPER_SOURCE = "试卷来源";
	public static final String ALIAS_PAPER_IS_RANDOM = "是否随机(Y是N否)";
	public static final String ALIAS_PAPER_BEGIN_TIME = "试卷开卷时间";
	public static final String ALIAS_PAPER_EXAMINERS_ID = "阅卷人";
	public static final String ALIAS_PAPER_CONFINEMENT_TIME = "试卷约束时间";
	public static final String ALIAS_PAPER_SURFACE_IMAGE = "试卷封面";
	public static final String ALIAS_PAPER_REMARKS = "备注";
	public static final String ALIAS_REDO_NUM = "重做次数(-1不限制0不允许重做1对应允许重做一次2对应两次，依此类推)";
	public static final String ALIAS_VIEW_RESULT_TIME = "查看答案时机（1测试活动结束之后查看2交卷之后查看答案）";
	public static final String ALIAS_CHAPTER_ID = "章节id";
	public static final String ALIAS_RESGROUP_ID = "所属分组";
	public static final String ALIAS_EMPIRICAL_VALUE = "可获得经验值";
	public static final String ALIAS_PURPOSE = "用途";
	public static final String ALIAS_FROM_TYPE = "来源类型，值为空或空字符，标识是测试活动那边组的卷，值为1表示是评测中心这边组的卷";
	public static final String ALIAS_QUESTION_NUMBER = "本试卷共有多少道题目";
	public static final String ALIAS_SINGLE_CHOICE_SCORE = "单选题每题多少分";
	public static final String ALIAS_MULTIPLE_CHOICE_SCORE = "多选题每题多少分";
	public static final String ALIAS_JUDGE_SCORE = "判断题每题多少分";
	public static final String ALIAS_SHORT_ANSWER_SCORE = "问答题每题多少分";
	public static final String ALIAS_GAP_FILLING = "填空题分值";
	public static final String ALIAS_GAP_FILLING_SCORE_STANDARD = "填空得分规则1每空得分，2题目填空全部答对得分";
	public static final String ALIAS_COMPOSITE_SCORE = "复合题每小题多少分";
	public static final String ALIAS_SINGLE_CHOICE_NUM = "单选题个数";
	public static final String ALIAS_MULTIPLE_CHOICE_NUM = "多选题个数";
	public static final String ALIAS_JUDGE_NUM = "判断题个数";
	public static final String ALIAS_SHORT_ANSWER_NUM = "简答题个数";
	public static final String ALIAS_GAP_FILLING_NUM = "填空题个数";
	public static final String ALIAS_COMPOSITE_NUM = "复合题个数";
	public static final String ALIAS_MAJOR_ID = "职业路径，多个以英文逗号隔开";
	public static final String ALIAS_SUBJECT_ID = "课程体系，多个以英文逗号隔开";

	/**
	 * 试卷ID(主键ID) db_column: paper_id
	 */
	// @NotNull(msg="试卷ID(主键ID)不能为空")
	@MaxLength(value = 32, msg = "字段[试卷ID(主键ID)]超出最大长度[32]")
	private java.lang.String paperId;
	/**
	 * 试卷名称 db_column: paper_name
	 */
	// @NotNull(msg="试卷名称不能为空")
	@MaxLength(value = 32, msg = "字段[试卷名称]超出最大长度[32]")
	private java.lang.String paperName;
	/**
	 * 试卷类型 db_column: paper_type
	 */
	// @NotNull(msg="试卷类型不能为空")
	@MaxLength(value = 5, msg = "字段[试卷类型]超出最大长度[5]")
	private java.lang.String paperType;
	/**
	 * 试卷总分 db_column: paper_total_score
	 */
	// @NotNull(msg="试卷总分不能为空")
	@MaxLength(value = 5, msg = "字段[试卷总分]超出最大长度[5]")
	private java.lang.String paperTotalScore;
	/**
	 * 试卷作答数(练习次数) db_column: paper_practice_time
	 */
	// @NotNull(msg="试卷作答数(练习次数)不能为空")
	@MaxLength(value = 11, msg = "字段[试卷作答数(练习次数)]超出最大长度[11]")
	private java.lang.Integer paperPracticeTime;
	/**
	 * 试卷状态(Y有效N无效) db_column: paper_state
	 */
	// @NotNull(msg="试卷状态(Y有效N无效)不能为空")
	@MaxLength(value = 5, msg = "字段[试卷状态(Y有效N无效)]超出最大长度[5]")
	private java.lang.String paperState;
	/**
	 * 试卷来源 db_column: paper_source
	 */
	// @NotNull(msg="试卷来源不能为空")
	@MaxLength(value = 32, msg = "字段[试卷来源]超出最大长度[32]")
	private java.lang.String paperSource;
	/**
	 * 是否随机(Y是N否) db_column: paper_is_random
	 */
	// @NotNull(msg="是否随机(Y是N否)不能为空")
	@MaxLength(value = 2, msg = "字段[是否随机(Y是N否)]超出最大长度[2]")
	private java.lang.String paperIsRandom;
	/**
	 * 试卷开卷时间 db_column: paper_begin_time
	 */
	// @NotNull(msg="试卷开卷时间不能为空")
	@MaxLength(value = 20, msg = "字段[试卷开卷时间]超出最大长度[20]")
	private java.lang.String paperBeginTime;
	/**
	 * 阅卷人 db_column: paper_examiners_id
	 */
	// @NotNull(msg="阅卷人不能为空")
	@MaxLength(value = 32, msg = "字段[阅卷人]超出最大长度[32]")
	private java.lang.String paperExaminersId;
	/**
	 * 试卷约束时间 db_column: paper_confinement_time
	 */
	// @NotNull(msg="试卷约束时间不能为空")
	@MaxLength(value = 20, msg = "字段[试卷约束时间]超出最大长度[20]")
	private java.lang.String paperConfinementTime;
	// columns END
	@MaxLength(value = 150, msg = "字段[试卷封面图]超出最大长度[100]")
	private String paperSurfaceImage;
	@MaxLength(value = 32, msg = "字段[试卷备注字段]超出最大长度[32]")
	private String paperRemarks;
	/**
     * 重做次数(-1不限制0不允许重做1对应允许重做一次，依此类推)       db_column: redo_num 
     */	
 	//@NotNull(msg="重做次数(-1不限制0不允许重做1对应允许重做一次，依此类推)不能为空")
 	@MaxLength(value=10, msg="字段[重做次数(-1不限制0不允许重做1对应允许重做一次，依此类推)]超出最大长度[10]")
	private java.lang.Integer redoNum;
    /**
     * 查看答案时机（1测试活动结束之后查看2交卷之后查看答案）       db_column: view_result_time 
     */	
 	//@NotNull(msg="查看答案时机（1测试活动结束之后查看2交卷之后查看答案）不能为空")
 	@MaxLength(value=2, msg="字段[查看答案时机（1测试活动结束之后查看2交卷之后查看答案）]超出最大长度[2]")
	private java.lang.String viewResultTime;
 	/**
     * 用途(来源字典)       db_column: purpose 
     */	
 	//@NotNull(msg="用途(来源字典)不能为空")
 	@MaxLength(value=2, msg="字段[用途(来源字典)]超出最大长度[2]")
	private java.lang.String purpose;
 	/**
     * 所属章节       db_column: chapter_id 
     */	
 	//@NotNull(msg="所属章节不能为空")
 	@MaxLength(value=32, msg="字段[所属章节]超出最大长度[32]")
	private java.lang.String chapterId;
 	/**
     * 所属资源分组       db_column: resgroup_id 
     */	
 	//@NotNull(msg="所属资源分组不能为空")
 	@MaxLength(value=32, msg="字段[所属资源分组]超出最大长度[32]")
	private java.lang.String resgroupId;
 	/**
     * 参与可获得经验值       db_column: empirical_value 
     */	
 	//@NotNull(msg="参与可获得经验值不能为空")
 	@MaxLength(value=10, msg="字段[参与可获得经验值]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
 	 /**
     * 本试卷有多少道题目       db_column: question_number 
     */	
 	//@NotNull(msg="本试卷有多少道题目不能为空")
 	@MaxLength(value=10, msg="字段[本试卷有多少道题目]超出最大长度[10]")
	private java.lang.Integer questionNumber;
 	/**
     * 单选题每题多少分       db_column: single_choice_score 
     */	
 	//@NotNull(msg="单选题每题多少分不能为空")
 	@MaxLength(value=10, msg="字段[单选题每题多少分]超出最大长度[10]")
	private java.lang.String singleChoiceScore;
    /**
     * 多选题每题多少分       db_column: multiple_choice_score 
     */	
 	//@NotNull(msg="多选题每题多少分不能为空")
 	@MaxLength(value=10, msg="字段[多选题每题多少分]超出最大长度[10]")
	private java.lang.String multipleChoiceScore;
    /**
     * 判断题每题多少分       db_column: judge_score 
     */	
 	//@NotNull(msg="判断题每题多少分不能为空")
 	@MaxLength(value=10, msg="字段[判断题每题多少分]超出最大长度[10]")
	private java.lang.String judgeScore;
    /**
     * 问答题每题多少分       db_column: short_answer_score 
     */	
 	//@NotNull(msg="问答题每题多少分不能为空")
 	@MaxLength(value=10, msg="字段[问答题每题多少分]超出最大长度[10]")
	private java.lang.String shortAnswerScore;
 	/**
     * 填空题分值       db_column: gap_filling 
     */	
 	//@NotNull(msg="填空题分值不能为空")
 	@MaxLength(value=10, msg="字段[填空题分值]超出最大长度[10]")
	private java.lang.String gapFilling;
    /**
     * 填空得分规则1每空得分，2题目填空全部答对得分       db_column: gap_filling_score_standard 
     */	
 	//@NotNull(msg="填空得分规则1每空得分，2题目填空全部答对得分不能为空")
 	@MaxLength(value=255, msg="字段[填空得分规则1每空得分，2题目填空全部答对得分]超出最大长度[255]")
	private java.lang.String gapFillingScoreStandard;
 	/**
     * 来源类型，值为空或空字符，标识是测试活动那边组的卷，值为1表示是评测中心这边组的卷       db_column: from_type 
     */	
 	//@NotNull(msg="来源类型，值为空或空字符，标识是测试活动那边组的卷，值为1表示是评测中心这边组的卷不能为空")
 	@MaxLength(value=2, msg="字段[来源类型，值为空或空字符，标识是测试活动那边组的卷，值为1表示是评测中心这边组的卷]超出最大长度[2]")
	private java.lang.String fromType;
    /**
     * 复合题每小题多少分       db_column: composite_score 
     */	
 	//@NotNull(msg="复合题每小题多少分不能为空")
 	@MaxLength(value=10, msg="字段[复合题每小题多少分]超出最大长度[10]")
	private java.lang.String compositeScore;
 	/**
     * 单选题个数       db_column: single_choice_num 
     */	
 	//@NotNull(msg="单选题个数不能为空")
 	@MaxLength(value=10, msg="字段[单选题个数]超出最大长度[10]")
	private java.lang.Integer singleChoiceNum;
    /**
     * 多选题个数       db_column: multiple_choice_num 
     */	
 	//@NotNull(msg="多选题个数不能为空")
 	@MaxLength(value=10, msg="字段[多选题个数]超出最大长度[10]")
	private java.lang.Integer multipleChoiceNum;
    /**
     * 判断题个数       db_column: judge_num 
     */	
 	//@NotNull(msg="判断题个数不能为空")
 	@MaxLength(value=10, msg="字段[判断题个数]超出最大长度[10]")
	private java.lang.Integer judgeNum;
    /**
     * 简答题个数       db_column: short_answer_num 
     */	
 	//@NotNull(msg="简答题个数不能为空")
 	@MaxLength(value=10, msg="字段[简答题个数]超出最大长度[10]")
	private java.lang.Integer shortAnswerNum;
    /**
     * 填空题个数       db_column: gap_filling_num 
     */	
 	//@NotNull(msg="填空题个数不能为空")
 	@MaxLength(value=10, msg="字段[填空题个数]超出最大长度[10]")
	private java.lang.Integer gapFillingNum;
    /**
     * 复合题个数       db_column: composite_num 
     */	
 	//@NotNull(msg="复合题个数不能为空")
 	@MaxLength(value=10, msg="字段[复合题个数]超出最大长度[10]")
	private java.lang.Integer compositeNum;
 	
 	/**
     * 职业路径，多个以英文逗号隔开       db_column: major_id 
     */	
 	//@NotNull(msg="职业路径，多个以英文逗号隔开不能为空")
 	@MaxLength(value=1000, msg="字段[职业路径，多个以英文逗号隔开]超出最大长度[1000]")
	private java.lang.String majorId;
    /**
     * 课程体系，多个以英文逗号隔开       db_column: subject_id 
     */	
 	//@NotNull(msg="课程体系，多个以英文逗号隔开不能为空")
 	@MaxLength(value=1000, msg="字段[课程体系，多个以英文逗号隔开]超出最大长度[1000]")
	private java.lang.String subjectId;
 	
 	private java.lang.String activityStateActual; // 实际活动状态
 	private java.lang.String activityBeginTime; // 实际活动开始时间
 	private java.lang.String activityEndTime; // 实际活动结束时间
 	
 	private String oldActivityId;
 	
	public String getPaperRemarks() {
		return paperRemarks;
	}

	public void setPaperRemarks(String paperRemarks) {
		this.paperRemarks = paperRemarks;
	}

	public String getPaperSurfaceImage() {
		return paperSurfaceImage;
	}

	public void setPaperSurfaceImage(String paperSurfaceImage) {
		this.paperSurfaceImage = paperSurfaceImage;
	}

	public TepExaminePaperInfo() {
	}

	public TepExaminePaperInfo(java.lang.String paperId) {
		this.paperId = paperId;
	}

	public void setPaperId(java.lang.String value) {
		this.paperId = value;
	}

	public java.lang.String getPaperId() {
		return this.paperId;
	}

	public void setPaperName(java.lang.String value) {
		this.paperName = value;
	}

	public java.lang.String getPaperName() {
		return this.paperName;
	}

	public void setPaperType(java.lang.String value) {
		this.paperType = value;
	}

	public java.lang.String getPaperType() {
		return this.paperType;
	}

	public void setPaperTotalScore(java.lang.String value) {
		this.paperTotalScore = value;
	}

	public java.lang.String getPaperTotalScore() {
		return this.paperTotalScore;
	}

	public void setPaperPracticeTime(java.lang.Integer value) {
		this.paperPracticeTime = value;
	}

	public java.lang.Integer getPaperPracticeTime() {
		return this.paperPracticeTime;
	}

	public void setPaperState(java.lang.String value) {
		this.paperState = value;
	}

	public java.lang.String getPaperState() {
		return this.paperState;
	}

	public void setPaperSource(java.lang.String value) {
		this.paperSource = value;
	}

	public java.lang.String getPaperSource() {
		return this.paperSource;
	}

	public void setPaperIsRandom(java.lang.String value) {
		this.paperIsRandom = value;
	}

	public java.lang.String getPaperIsRandom() {
		return this.paperIsRandom;
	}

	public void setPaperBeginTime(java.lang.String value) {
		this.paperBeginTime = value;
	}

	public java.lang.String getPaperBeginTime() {
		return this.paperBeginTime;
	}

	public void setPaperExaminersId(java.lang.String value) {
		this.paperExaminersId = value;
	}

	public java.lang.String getPaperExaminersId() {
		return this.paperExaminersId;
	}

	public void setPaperConfinementTime(java.lang.String value) {
		this.paperConfinementTime = value;
	}

	public java.lang.String getPaperConfinementTime() {
		return this.paperConfinementTime;
	}

	public java.lang.Integer getRedoNum() {
		return redoNum;
	}

	public void setRedoNum(java.lang.Integer redoNum) {
		this.redoNum = redoNum;
	}

	public java.lang.String getViewResultTime() {
		return viewResultTime;
	}

	public void setViewResultTime(java.lang.String viewResultTime) {
		this.viewResultTime = viewResultTime;
	}

	public java.lang.String getChapterId() {
		return chapterId;
	}

	public void setChapterId(java.lang.String chapterId) {
		this.chapterId = chapterId;
	}

	public java.lang.String getResgroupId() {
		return resgroupId;
	}

	public void setResgroupId(java.lang.String resgroupId) {
		this.resgroupId = resgroupId;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.String getActivityStateActual() {
		return activityStateActual;
	}

	public void setActivityStateActual(java.lang.String activityStateActual) {
		this.activityStateActual = activityStateActual;
	}

	public java.lang.Integer getEmpiricalValue() {
		return empiricalValue;
	}

	public void setEmpiricalValue(java.lang.Integer empiricalValue) {
		this.empiricalValue = empiricalValue;
	}

	public java.lang.Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(java.lang.Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	public java.lang.String getSingleChoiceScore() {
		return singleChoiceScore;
	}

	public void setSingleChoiceScore(java.lang.String singleChoiceScore) {
		this.singleChoiceScore = singleChoiceScore;
	}

	public java.lang.String getMultipleChoiceScore() {
		return multipleChoiceScore;
	}

	public void setMultipleChoiceScore(java.lang.String multipleChoiceScore) {
		this.multipleChoiceScore = multipleChoiceScore;
	}

	public java.lang.String getJudgeScore() {
		return judgeScore;
	}

	public void setJudgeScore(java.lang.String judgeScore) {
		this.judgeScore = judgeScore;
	}

	public java.lang.String getShortAnswerScore() {
		return shortAnswerScore;
	}

	public void setShortAnswerScore(java.lang.String shortAnswerScore) {
		this.shortAnswerScore = shortAnswerScore;
	}

	public java.lang.String getPurpose() {
		return purpose;
	}

	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}

	public java.lang.String getActivityBeginTime() {
		return activityBeginTime;
	}

	public void setActivityBeginTime(java.lang.String activityBeginTime) {
		this.activityBeginTime = activityBeginTime;
	}

	public java.lang.String getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(java.lang.String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public java.lang.String getGapFilling() {
		return gapFilling;
	}

	public void setGapFilling(java.lang.String gapFilling) {
		this.gapFilling = gapFilling;
	}

	public java.lang.String getGapFillingScoreStandard() {
		return gapFillingScoreStandard;
	}

	public void setGapFillingScoreStandard(java.lang.String gapFillingScoreStandard) {
		this.gapFillingScoreStandard = gapFillingScoreStandard;
	}

	public java.lang.String getFromType() {
		return fromType;
	}

	public void setFromType(java.lang.String fromType) {
		this.fromType = fromType;
	}

	public java.lang.String getCompositeScore() {
		return compositeScore;
	}

	public void setCompositeScore(java.lang.String compositeScore) {
		this.compositeScore = compositeScore;
	}

	public java.lang.Integer getSingleChoiceNum() {
		return singleChoiceNum;
	}

	public void setSingleChoiceNum(java.lang.Integer singleChoiceNum) {
		this.singleChoiceNum = singleChoiceNum;
	}

	public java.lang.Integer getMultipleChoiceNum() {
		return multipleChoiceNum;
	}

	public void setMultipleChoiceNum(java.lang.Integer multipleChoiceNum) {
		this.multipleChoiceNum = multipleChoiceNum;
	}

	public java.lang.Integer getJudgeNum() {
		return judgeNum;
	}

	public void setJudgeNum(java.lang.Integer judgeNum) {
		this.judgeNum = judgeNum;
	}

	public java.lang.Integer getShortAnswerNum() {
		return shortAnswerNum;
	}

	public void setShortAnswerNum(java.lang.Integer shortAnswerNum) {
		this.shortAnswerNum = shortAnswerNum;
	}

	public java.lang.Integer getGapFillingNum() {
		return gapFillingNum;
	}

	public void setGapFillingNum(java.lang.Integer gapFillingNum) {
		this.gapFillingNum = gapFillingNum;
	}

	public java.lang.Integer getCompositeNum() {
		return compositeNum;
	}

	public void setCompositeNum(java.lang.Integer compositeNum) {
		this.compositeNum = compositeNum;
	}

	public String getOldActivityId() {
		return oldActivityId;
	}

	public void setOldActivityId(String oldActivityId) {
		this.oldActivityId = oldActivityId;
	}

	public java.lang.String getMajorId() {
		return majorId;
	}

	public void setMajorId(java.lang.String majorId) {
		this.majorId = majorId;
	}

	public java.lang.String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(java.lang.String subjectId) {
		this.subjectId = subjectId;
	}

}
