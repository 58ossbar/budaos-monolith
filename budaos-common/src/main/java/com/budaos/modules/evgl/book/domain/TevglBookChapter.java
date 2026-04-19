package com.budaos.modules.evgl.book.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;

import java.util.List;

/**
 * <p> Title: 章节</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglBookChapter extends BaseDomain<TevglBookChapter>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglBookChapter";
	public static final String ALIAS_CHAPTER_ID = "章节主键ID";
	public static final String ALIAS_SUBJECT_ID = "所属课程ID";
	public static final String ALIAS_CHAPTER_NO = "章节编号";
	public static final String ALIAS_PARENT_ID = "父章节ID";
	public static final String ALIAS_LEVEL = "层级";
	public static final String ALIAS_CHAPTER_NAME = "章节名称";
	public static final String ALIAS_CHAPTER_ICON = "章节小图标";
	public static final String ALIAS_CHAPTER_DESC = "章节简介(文本)";
	public static final String ALIAS_CHAPTER_CONTENT = "章节内容(富文本)";
	public static final String ALIAS_CLASS_HOUR = "章节课时";
	public static final String ALIAS_ORDER_NUM = "排序号";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 章节主键ID       db_column: chapter_id 
     */	
 	@NotNull(msg="章节主键ID不能为空")
 	@MaxLength(value=32, msg="字段[章节主键ID]超出最大长度[32]")
	private java.lang.String chapterId;
    /**
     * 所属课程ID       db_column: subject_id 
     */	
 	@NotNull(msg="所属课程ID不能为空")
 	@MaxLength(value=32, msg="字段[所属课程ID]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 章节编号       db_column: chapter_no 
     */	
 	//@NotNull(msg="章节编号不能为空")
 	@MaxLength(value=20, msg="字段[章节编号]超出最大长度[20]")
	private java.lang.String chapterNo;
    /**
     * 父章节ID       db_column: parent_id 
     */	
 	//@NotNull(msg="父章节ID不能为空")
 	@MaxLength(value=32, msg="字段[父章节ID]超出最大长度[32]")
	private java.lang.String parentId;
    /**
     * 层级       db_column: level 
     */	
 	//@NotNull(msg="层级不能为空")
 	@MaxLength(value=10, msg="字段[层级]超出最大长度[10]")
	private java.lang.Integer level;
    /**
     * 章节名称       db_column: chapter_name 
     */	
 	@NotNull(msg="章节名称不能为空")
 	@MaxLength(value=50, msg="字段[章节名称]超出最大长度[50]")
	private java.lang.String chapterName;
    /**
     * 章节小图标       db_column: chapter_icon 
     */	
 	//@NotNull(msg="章节小图标不能为空")
 	@MaxLength(value=300, msg="字段[章节小图标]超出最大长度[300]")
	private java.lang.String chapterIcon;
    /**
     * 章节简介(文本)       db_column: chapter_desc 
     */	
 	//@NotNull(msg="章节简介(文本)不能为空")
 	@MaxLength(value=1000, msg="字段[章节简介(文本)]超出最大长度[1000]")
	private java.lang.String chapterDesc;
    /**
     * 章节内容(富文本)       db_column: chapter_content 
     */	
 	//@NotNull(msg="章节内容(富文本)不能为空")
 	@MaxLength(value=2147483647, msg="字段[章节内容(富文本)]超出最大长度[2147483647]")
	private java.lang.String chapterContent;
    /**
     * 章节课时       db_column: class_hour 
     */	
 	//@NotNull(msg="章节课时不能为空")
 	@MaxLength(value=10, msg="字段[章节课时]超出最大长度[10]")
	private java.math.BigDecimal classHour;
    /**
     * 排序号       db_column: order_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer orderNum;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 对学员是否可见(Y/N)       db_column: is_trainees_visible 
     */	
 	//@NotNull(msg="对学员是否可见(Y/N)不能为空")
 	@MaxLength(value=2, msg="字段[对学员是否可见(Y/N)]超出最大长度[2]")
 	private java.lang.String isTraineesVisible;
 	
 	private List<TevglBookChapter> children; // 章节
 	private List<TevglPkgResgroup> pkgResGroupList; // 资源分组目录
 	
 	private List<TevglQuestionsInfo> questionList; // 章节下题目集合
 	
 	private String pkgId;
 	
 	private String newChapterId; // 用于复制章节时的存值，存当前被复制的章节id
 	
 	/**
 	 * 操作标识，appendPeerNode为追降价节点
 	 */
 	private String operationType;
 	/**
 	 * 在哪个节点下追加节点
 	 */
 	private String previousChapterId;
 	
	//columns END

	public TevglBookChapter(){
	}

	public TevglBookChapter(
		java.lang.String chapterId
	){
		this.chapterId = chapterId;
	}

	public void setChapterId(java.lang.String value) {
		this.chapterId = value;
	}
	public java.lang.String getChapterId() {
		return this.chapterId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setChapterNo(java.lang.String value) {
		this.chapterNo = value;
	}
	public java.lang.String getChapterNo() {
		return this.chapterNo;
	}
	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}
	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setLevel(java.lang.Integer value) {
		this.level = value;
	}
	public java.lang.Integer getLevel() {
		return this.level;
	}
	public void setChapterName(java.lang.String value) {
		this.chapterName = value;
	}
	public java.lang.String getChapterName() {
		return this.chapterName;
	}
	public void setChapterIcon(java.lang.String value) {
		this.chapterIcon = value;
	}
	public java.lang.String getChapterIcon() {
		return this.chapterIcon;
	}
	public void setChapterDesc(java.lang.String value) {
		this.chapterDesc = value;
	}
	public java.lang.String getChapterDesc() {
		return this.chapterDesc;
	}
	public void setChapterContent(java.lang.String value) {
		this.chapterContent = value;
	}
	public java.lang.String getChapterContent() {
		return this.chapterContent;
	}
	public void setClassHour(java.math.BigDecimal value) {
		this.classHour = value;
	}
	public java.math.BigDecimal getClassHour() {
		return this.classHour;
	}
	public void setOrderNum(java.lang.Integer value) {
		this.orderNum = value;
	}
	public java.lang.Integer getOrderNum() {
		return this.orderNum;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	
	private TevglBookSubject tevglBookSubject;
	
	public void setTevglBookSubject(TevglBookSubject tevglBookSubject){
		this.tevglBookSubject = tevglBookSubject;
	}
	
	public TevglBookSubject getTevglBookSubject() {
		return tevglBookSubject;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public List<TevglBookChapter> getChildren() {
		return children;
	}

	public void setChildren(List<TevglBookChapter> children) {
		this.children = children;
	}

	public List<TevglPkgResgroup> getPkgResGroupList() {
		return pkgResGroupList;
	}

	public void setPkgResGroupList(List<TevglPkgResgroup> pkgResGroupList) {
		this.pkgResGroupList = pkgResGroupList;
	}

	public List<TevglQuestionsInfo> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<TevglQuestionsInfo> questionList) {
		this.questionList = questionList;
	}

	public java.lang.String getIsTraineesVisible() {
		return isTraineesVisible;
	}

	public void setIsTraineesVisible(java.lang.String isTraineesVisible) {
		this.isTraineesVisible = isTraineesVisible;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getNewChapterId() {
		return newChapterId;
	}

	public void setNewChapterId(String newChapterId) {
		this.newChapterId = newChapterId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getPreviousChapterId() {
		return previousChapterId;
	}

	public void setPreviousChapterId(String previousChapterId) {
		this.previousChapterId = previousChapterId;
	}


}

