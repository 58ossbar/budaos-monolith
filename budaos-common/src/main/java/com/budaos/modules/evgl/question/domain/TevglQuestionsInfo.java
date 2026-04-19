package com.budaos.modules.evgl.question.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;

/**
 * <p> Title: 题目基本信息</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglQuestionsInfo extends BaseDomain<TevglQuestionsInfo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglQuestionsInfo";
	public static final String ALIAS_QUESTIONS_ID = "题目ID(主键ID)";
	public static final String ALIAS_SUBJECT_ID = "所属课程";
	public static final String ALIAS_CHAPTERS_ID = "所属章节";
	public static final String ALIAS_KNOWLEDGE_ID = "所属知识点";
	public static final String ALIAS_QUESTIONS_NAME = "题目名称";
	public static final String ALIAS_QUESTIONS_PARSE = "题目解析";
	public static final String ALIAS_REPLY_IDS = "题目正确答案";
	public static final String ALIAS_QUESTIONS_COMPLEXITY = "难易程度(容易、普通、困难)";
	public static final String ALIAS_QUESTIONS_STAR = "题目星级";
	public static final String ALIAS_QUESTIONS_STATE = "题目状态(Y启用N停用)";
	public static final String ALIAS_QUESTIONS_CONSTRUCTING_NUM = "组卷次数";
	public static final String ALIAS_QUESTIONS_TYPE = "题目类型(字典:1选择2判断等)";
	public static final String ALIAS_QUESTIONS_ACCURACY = "正确率";
	public static final String ALIAS_QUESTIONS_STORE_NUM = "题目收藏数";
	public static final String ALIAS_QUESTIONS_ANSWER_NUM = "题目作答数";
	public static final String ALIAS_QUESTIONS_CORRECT_NUM = "题目正确数";
	public static final String ALIAS_QUESTIONS_ERROR_NUM = "题目错误数";
	public static final String ALIAS_QUESTIONS_STYLE = "实训、开源或等";
	public static final String ALIAS_FROM_TYPE = "为空表示是在题库录入的题目，值为1表示是测试活动那边录入的";
	public static final String ALIAS_TAG = "值为img表示题目名称是一张图片，需要去拼接地址";
	public static final String ALIAS_PARENT_ID = "题目类型为6复合题时，关联的父题目";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_SHOW_TYPE = "0不显示1显示 从测试活动录入的题目，也就是from_type为1时，根据此值是否在题库显示";

    /**
     * 题目ID(主键ID)       db_column: questions_id 
     */	
 	@NotNull(msg="题目ID(主键ID)不能为空")
 	@MaxLength(value=32, msg="字段[题目ID(主键ID)]超出最大长度[32]")
	private java.lang.String questionsId;
 	/**
     * 专业方向id       db_column: major_id 
     */	
 	@NotNull(msg="专业方向id不能为空")
 	@MaxLength(value=32, msg="字段[专业方向id]超出最大长度[32]")
	private java.lang.String majorId;
    /**
     * 所属课程       db_column: subject_id 
     */	
 	@NotNull(msg="所属课程不能为空")
 	@MaxLength(value=32, msg="字段[所属课程]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 所属章节       db_column: chapters_id 
     */	
 	//@NotNull(msg="所属章节不能为空")
 	@MaxLength(value=32, msg="字段[所属章节]超出最大长度[32]")
	private java.lang.String chaptersId;
    /**
     * 所属知识点       db_column: knowledge_id 
     */	
 	//@NotNull(msg="所属知识点不能为空")
 	@MaxLength(value=32, msg="字段[所属知识点]超出最大长度[32]")
	private java.lang.String knowledgeId;
    /**
     * 题目名称       db_column: questions_name 
     */	
 	@NotNull(msg="题目名称不能为空")
 	@MaxLength(value=2147483647, msg="字段[题目名称]超出最大长度[2147483647]")
	private java.lang.String questionsName;
    /**
     * 题目解析       db_column: questions_parse 
     */	
 	//@NotNull(msg="题目解析不能为空")
 	@MaxLength(value=2147483647, msg="字段[题目解析]超出最大长度[2147483647]")
	private java.lang.String questionsParse;
    /**
     * 题目正确答案       db_column: reply_ids 
     */	
 	//@NotNull(msg="题目正确答案不能为空")
 	@MaxLength(value=500, msg="字段[题目正确答案]超出最大长度[500]")
	private java.lang.String replyIds;
    /**
     * 难易程度(容易、普通、困难)       db_column: questions_complexity 
     */	
 	//@NotNull(msg="难易程度(容易、普通、困难)不能为空")
 	@MaxLength(value=8, msg="字段[难易程度(容易、普通、困难)]超出最大长度[8]")
	private java.lang.String questionsComplexity;
    /**
     * 题目星级       db_column: questions_star 
     */	
 	//@NotNull(msg="题目星级不能为空")
 	@MaxLength(value=32, msg="字段[题目星级]超出最大长度[32]")
	private java.lang.String questionsStar;
    /**
     * 题目状态(Y启用N停用)       db_column: questions_state 
     */	
 	//@NotNull(msg="题目状态(Y启用N停用)不能为空")
 	@MaxLength(value=2, msg="字段[题目状态(Y启用N停用)]超出最大长度[2]")
	private java.lang.String questionsState;
    /**
     * 组卷次数       db_column: questions_constructing_num 
     */	
 	//@NotNull(msg="组卷次数不能为空")
 	@MaxLength(value=10, msg="字段[组卷次数]超出最大长度[10]")
	private java.lang.Integer questionsConstructingNum;
    /**
     * 题目类型(字典:1选择2判断等)       db_column: questions_type 
     */	
 	//@NotNull(msg="题目类型(字典:1选择2判断等)不能为空")
 	@MaxLength(value=8, msg="字段[题目类型(字典:1选择2判断等)]超出最大长度[8]")
	private java.lang.String questionsType;
    /**
     * 正确率       db_column: questions_accuracy 
     */	
 	//@NotNull(msg="正确率不能为空")
 	@MaxLength(value=8, msg="字段[正确率]超出最大长度[8]")
 	private java.math.BigDecimal questionsAccuracy;
    /**
     * 题目收藏数       db_column: questions_store_num 
     */	
 	//@NotNull(msg="题目收藏数不能为空")
 	@MaxLength(value=10, msg="字段[题目收藏数]超出最大长度[10]")
	private java.lang.Integer questionsStoreNum;
    /**
     * 题目作答数       db_column: questions_answer_num 
     */	
 	//@NotNull(msg="题目作答数不能为空")
 	@MaxLength(value=10, msg="字段[题目作答数]超出最大长度[10]")
	private java.lang.Integer questionsAnswerNum;
    /**
     * 题目正确数       db_column: questions_correct_num 
     */	
 	//@NotNull(msg="题目正确数不能为空")
 	@MaxLength(value=10, msg="字段[题目正确数]超出最大长度[10]")
	private java.lang.Integer questionsCorrectNum;
    /**
     * 题目错误数       db_column: questions_error_num 
     */	
 	//@NotNull(msg="题目错误数不能为空")
 	@MaxLength(value=10, msg="字段[题目错误数]超出最大长度[10]")
	private java.lang.Integer questionsErrorNum;
    /**
     * 实训、开源或等       db_column: questions_style 
     */	
 	//@NotNull(msg="实训、开源或等不能为空")
 	@MaxLength(value=2, msg="字段[实训、开源或等]超出最大长度[2]")
	private java.lang.String questionsStyle;
 	
 	/**
     * 为空表示是在题库录入的题目，值为1表示是测试活动那边录入的       db_column: from_type 
     */	
 	//@NotNull(msg="为空表示是在题库录入的题目，值为1表示是测试活动那边录入的不能为空")
 	@MaxLength(value=2, msg="字段[为空表示是在题库录入的题目，值为1表示是测试活动那边录入的]超出最大长度[2]")
	private java.lang.String fromType;
 	/**
     * 值为img表示题目名称是一张图片，需要去拼接地址       db_column: tag 
     */	
 	//@NotNull(msg="值为img表示题目名称是一张图片，需要去拼接地址不能为空")
 	@MaxLength(value=30, msg="字段[值为img表示题目名称是一张图片，需要去拼接地址]超出最大长度[30]")
	private java.lang.String tag;
 	/**
     * 当题型为6复合题时       db_column: parent_id 
     */	
 	//@NotNull(msg="当题型为6复合题时不能为空")
 	@MaxLength(value=32, msg="字段[当题型为6复合题时]超出最大长度[32]")
	private java.lang.String parentId;
 	/**
     * 排序号       db_column: sort_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
 	/**
     * 0不显示1显示 从测试活动录入的题目，也就是from_type为1时，根据此值是否在题库显示       db_column: show_type 
     */	
 	//@NotNull(msg="0不显示1显示 从测试活动录入的题目，也就是from_type为1时，根据此值是否在题库显示不能为空")
 	@MaxLength(value=2, msg="字段[0不显示1显示 从测试活动录入的题目，也就是from_type为1时，根据此值是否在题库显示]超出最大长度[2]")
	private java.lang.String showType;
 	
 	private java.lang.String json; // 用于存储题目选项等数据
 	
 	private List<TevglQuestionsInfo> children; // 复合题的子题目集合
 	private List<TevglQuestionChose> optionList; // 题目选项集合
 	
 	private Boolean hasEditPermission;
 	
	//columns END

	public TevglQuestionsInfo(){
	}

	public TevglQuestionsInfo(
		java.lang.String questionsId
	){
		this.questionsId = questionsId;
	}

	public void setQuestionsId(java.lang.String value) {
		this.questionsId = value;
	}
	public java.lang.String getQuestionsId() {
		return this.questionsId;
	}
	public java.lang.String getMajorId() {
		return majorId;
	}
	public void setMajorId(java.lang.String majorId) {
		this.majorId = majorId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setChaptersId(java.lang.String value) {
		this.chaptersId = value;
	}
	public java.lang.String getChaptersId() {
		return this.chaptersId;
	}
	public void setKnowledgeId(java.lang.String value) {
		this.knowledgeId = value;
	}
	public java.lang.String getKnowledgeId() {
		return this.knowledgeId;
	}
	public void setQuestionsName(java.lang.String value) {
		this.questionsName = value;
	}
	public java.lang.String getQuestionsName() {
		return this.questionsName;
	}
	public void setQuestionsParse(java.lang.String value) {
		this.questionsParse = value;
	}
	public java.lang.String getQuestionsParse() {
		return this.questionsParse;
	}
	public void setReplyIds(java.lang.String value) {
		this.replyIds = value;
	}
	public java.lang.String getReplyIds() {
		return this.replyIds;
	}
	public void setQuestionsComplexity(java.lang.String value) {
		this.questionsComplexity = value;
	}
	public java.lang.String getQuestionsComplexity() {
		return this.questionsComplexity;
	}
	public void setQuestionsStar(java.lang.String value) {
		this.questionsStar = value;
	}
	public java.lang.String getQuestionsStar() {
		return this.questionsStar;
	}
	public void setQuestionsState(java.lang.String value) {
		this.questionsState = value;
	}
	public java.lang.String getQuestionsState() {
		return this.questionsState;
	}
	public void setQuestionsConstructingNum(java.lang.Integer value) {
		this.questionsConstructingNum = value;
	}
	public java.lang.Integer getQuestionsConstructingNum() {
		return this.questionsConstructingNum;
	}
	public void setQuestionsType(java.lang.String value) {
		this.questionsType = value;
	}
	public java.lang.String getQuestionsType() {
		return this.questionsType;
	}
	
	public java.math.BigDecimal getQuestionsAccuracy() {
		return questionsAccuracy;
	}

	public void setQuestionsAccuracy(java.math.BigDecimal questionsAccuracy) {
		this.questionsAccuracy = questionsAccuracy;
	}

	public void setQuestionsStoreNum(java.lang.Integer value) {
		this.questionsStoreNum = value;
	}
	public java.lang.Integer getQuestionsStoreNum() {
		return this.questionsStoreNum;
	}
	public void setQuestionsAnswerNum(java.lang.Integer value) {
		this.questionsAnswerNum = value;
	}
	public java.lang.Integer getQuestionsAnswerNum() {
		return this.questionsAnswerNum;
	}
	public void setQuestionsCorrectNum(java.lang.Integer value) {
		this.questionsCorrectNum = value;
	}
	public java.lang.Integer getQuestionsCorrectNum() {
		return this.questionsCorrectNum;
	}
	public void setQuestionsErrorNum(java.lang.Integer value) {
		this.questionsErrorNum = value;
	}
	public java.lang.Integer getQuestionsErrorNum() {
		return this.questionsErrorNum;
	}
	public void setQuestionsStyle(java.lang.String value) {
		this.questionsStyle = value;
	}
	public java.lang.String getQuestionsStyle() {
		return this.questionsStyle;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.String getJson() {
		return json;
	}

	public void setJson(java.lang.String json) {
		this.json = json;
	}

	public List<TevglQuestionChose> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<TevglQuestionChose> optionList) {
		this.optionList = optionList;
	}

	public java.lang.String getFromType() {
		return fromType;
	}

	public void setFromType(java.lang.String fromType) {
		this.fromType = fromType;
	}

	public java.lang.String getTag() {
		return tag;
	}

	public void setTag(java.lang.String tag) {
		this.tag = tag;
	}

	public java.lang.String getParentId() {
		return parentId;
	}

	public void setParentId(java.lang.String parentId) {
		this.parentId = parentId;
	}

	public List<TevglQuestionsInfo> getChildren() {
		return children;
	}

	public void setChildren(List<TevglQuestionsInfo> children) {
		this.children = children;
	}

	public java.lang.Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(java.lang.Integer sortNum) {
		this.sortNum = sortNum;
	}

	public java.lang.String getShowType() {
		return showType;
	}

	public void setShowType(java.lang.String showType) {
		this.showType = showType;
	}

	public Boolean getHasEditPermission() {
		return hasEditPermission;
	}

	public void setHasEditPermission(Boolean hasEditPermission) {
		this.hasEditPermission = hasEditPermission;
	}

}

