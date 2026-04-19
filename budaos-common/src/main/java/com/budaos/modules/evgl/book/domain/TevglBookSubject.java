package com.budaos.modules.evgl.book.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;

/**
 * <p> Title: 课程教材信息</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglBookSubject extends BaseDomain<TevglBookSubject>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglBookSubject";
	public static final String ALIAS_SUBJECT_ID = "课程主键ID";
	public static final String ALIAS_ORG_ID = "所属院校";
	public static final String ALIAS_SUBJECT_NO = "课程编号";
	public static final String ALIAS_SUBJECT_NAME = "课程名称";
	public static final String ALIAS_SUBJECT_AUTHOR = "作者";
	public static final String ALIAS_PUBLISHING_HOUSE = "出版社";
	public static final String ALIAS_SUBJECT_DESC = "课程简介(文本)";
	public static final String ALIAS_SUBJECT_REMARK = "课程详细描述(富文本)";
	public static final String ALIAS_SUBJECT_TAG = "课程关键字标签";
	public static final String ALIAS_SUBJECT_LOGO = "课程logo";
	public static final String ALIAS_SUBJECT_TYPE = "课程类型(来源字典:学校，平台等)";
	public static final String ALIAS_SUBJECT_VERSION = "课程版本";
	public static final String ALIAS_LAST_VERSION_ID = "上一版本ID";
	public static final String ALIAS_SUBJECT_PRICE = "课程原价";
	public static final String ALIAS_CLASS_SCORE = "总学分";
	public static final String ALIAS_CLASS_HOUR = "总课时";
	public static final String ALIAS_SUBJECT_REF = "来源课程";
	public static final String ALIAS_DEPLOY_USER_ID = "发布人";
	public static final String ALIAS_DEPLOY_TIME = "发布时间";
	public static final String ALIAS_DISPLAY = "可见性(来源字典:私有or公有)";
	public static final String ALIAS_STATE = "状态(Y已发布N未发布)";
	

    /**
     * 课程主键ID       db_column: subject_id 
     */	
 	@NotNull(msg="课程主键ID不能为空")
 	@MaxLength(value=32, msg="字段[课程主键ID]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 所属院校       db_column: org_id 
     */	
 	//@NotNull(msg="所属院校不能为空")
 	@MaxLength(value=32, msg="字段[所属院校]超出最大长度[32]")
	private java.lang.String orgId;
    /**
     * 课程编号       db_column: subject_no 
     */	
 	//@NotNull(msg="课程编号不能为空")
 	@MaxLength(value=10, msg="字段[课程编号]超出最大长度[10]")
	private java.lang.String subjectNo;
    /**
     * 课程名称       db_column: subject_name 
     */	
 	@NotNull(msg="课程名称不能为空")
 	@MaxLength(value=50, msg="字段[课程名称]超出最大长度[50]")
	private java.lang.String subjectName;
    /**
     * 作者       db_column: subject_author 
     */	
 	//@NotNull(msg="作者不能为空")
 	@MaxLength(value=50, msg="字段[作者]超出最大长度[50]")
	private java.lang.String subjectAuthor;
    /**
     * 出版社       db_column: publishing_house 
     */	
 	//@NotNull(msg="出版社不能为空")
 	@MaxLength(value=50, msg="字段[出版社]超出最大长度[50]")
	private java.lang.String publishingHouse;
    /**
     * 课程简介(文本)       db_column: subject_desc 
     */	
 	//@NotNull(msg="课程简介(文本)不能为空")
 	@MaxLength(value=1000, msg="字段[课程简介(文本)]超出最大长度[1000]")
	private java.lang.String subjectDesc;
    /**
     * 课程详细描述(富文本)       db_column: subject_remark 
     */	
 	//@NotNull(msg="课程详细描述(富文本)不能为空")
 	@MaxLength(value=2147483647, msg="字段[课程详细描述(富文本)]超出最大长度[2147483647]")
	private java.lang.String subjectRemark;
    /**
     * 课程关键字标签       db_column: subject_tag 
     */	
 	//@NotNull(msg="课程关键字标签不能为空")
 	@MaxLength(value=1000, msg="字段[课程关键字标签]超出最大长度[1000]")
	private java.lang.String subjectTag;
    /**
     * 课程logo       db_column: subject_logo 
     */	
 	//@NotNull(msg="课程logo不能为空")
 	@MaxLength(value=300, msg="字段[课程logo]超出最大长度[300]")
	private java.lang.String subjectLogo;
    /**
     * 课程类型(来源字典:学校，平台等)       db_column: subject_type 
     */	
 	//@NotNull(msg="课程类型(来源字典:学校，平台等)不能为空")
 	@MaxLength(value=32, msg="字段[课程类型(来源字典:学校，平台等)]超出最大长度[32]")
	private java.lang.String subjectType;
    /**
     * 课程版本       db_column: subject_version 
     */	
 	//@NotNull(msg="课程版本不能为空")
 	@MaxLength(value=10, msg="字段[课程版本]超出最大长度[10]")
	private java.lang.String subjectVersion;
    /**
     * 上一版本ID       db_column: last_version_id 
     */	
 	//@NotNull(msg="上一版本ID不能为空")
 	@MaxLength(value=32, msg="字段[上一版本ID]超出最大长度[32]")
	private java.lang.String lastVersionId;
    /**
     * 课程原价       db_column: subject_price 
     */	
 	//@NotNull(msg="课程原价不能为空")
 	@MaxLength(value=10, msg="字段[课程原价]超出最大长度[10]")
	private java.math.BigDecimal subjectPrice;
    /**
     * 总学分       db_column: class_score 
     */	
 	//@NotNull(msg="总学分不能为空")
 	@MaxLength(value=3, msg="字段[总学分]超出最大长度[3]")
	private java.math.BigDecimal classScore;
    /**
     * 总课时       db_column: class_hour 
     */	
 	//@NotNull(msg="总课时不能为空")
 	@MaxLength(value=10, msg="字段[总课时]超出最大长度[10]")
	private java.math.BigDecimal classHour;
    /**
     * 来源课程       db_column: subject_ref 
     */	
 	//@NotNull(msg="来源课程不能为空")
 	@MaxLength(value=32, msg="字段[来源课程]超出最大长度[32]")
	private java.lang.String subjectRef;
    /**
     * 发布人       db_column: deploy_user_id 
     */	
 	//@NotNull(msg="发布人不能为空")
 	@MaxLength(value=32, msg="字段[发布人]超出最大长度[32]")
	private java.lang.String deployUserId;
    /**
     * 发布时间       db_column: deploy_time 
     */	
 	//@NotNull(msg="发布时间不能为空")
 	@MaxLength(value=20, msg="字段[发布时间]超出最大长度[20]")
	private java.lang.String deployTime;
    /**
     * 可见性(来源字典:私有or公有)       db_column: display 
     */	
 	//@NotNull(msg="可见性(来源字典:私有or公有)不能为空")
 	@MaxLength(value=32, msg="字段[可见性(来源字典:私有or公有)]超出最大长度[32]")
	private java.lang.String display;
    /**
     * 状态(Y已发布N未发布)       db_column: state 
     */	
 	@NotNull(msg="状态(Y已发布N未发布)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y已发布N未发布)]超出最大长度[2]")
	private java.lang.String state;
 	
 	/**
     * 所属技术(来源字典)       db_column: subject_technology 
     */	
 	//@NotNull(msg="所属技术不能为空")
 	@MaxLength(value=500, msg="字段[所属技术(来源字典)]超出最大长度[500]")
	private java.lang.String subjectTechnology;
 	/**
     * 阅读量       db_column: view_num 
     */	
 	//@NotNull(msg="阅读量不能为空")
 	@MaxLength(value=11, msg="阅读量超出最大长度[11]")
	private java.lang.Integer viewNum;
 	/**
     * 星级       db_column: star_level 
     */	
 	//@NotNull(msg="星级不能为空")
 	@MaxLength(value=11, msg="星级超出最大长度[11]")
	private java.lang.Integer starLevel;
 	/**
     * 收藏数      db_column: store_num 
     */	
 	//@NotNull(msg="收藏数不能为空")
 	@MaxLength(value=11, msg="收藏数超出最大长度[11]")
	private java.lang.Integer storeNum;
 	/**
     * 点赞数      db_column: like_num 
     */	
 	//@NotNull(msg="点赞数不能为空")
 	@MaxLength(value=11, msg="点赞数超出最大长度[11]")
	private java.lang.Integer likeNum;
 	/**
     * 举报数       db_column: report_num 
     */	
 	//@NotNull(msg="点赞数不能为空")
 	@MaxLength(value=11, msg="举报数超出最大长度[11]")
	private java.lang.Integer reportNum;
 	
 	private List<TevglBookChapter> children; // 子数据
 	
 	private String majorId; // 专业ID
 	private String attachId; // 附件关联ID，用于图片上传
 	private List<String> majorIdList; // 用于存储多个职业路径Id
 	private String term; // 用于所属学期
 	private String subjectProperty; // 课程属性选修or必修
 	
	//columns END
 	
 	/**
 	 * 教学包主键（非数据库字段）
 	 */
 	private String pkgId;

	public TevglBookSubject(){
	}

	public TevglBookSubject(
		java.lang.String subjectId
	){
		this.subjectId = subjectId;
	}

	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setSubjectNo(java.lang.String value) {
		this.subjectNo = value;
	}
	public java.lang.String getSubjectNo() {
		return this.subjectNo;
	}
	public void setSubjectName(java.lang.String value) {
		this.subjectName = value;
	}
	public java.lang.String getSubjectName() {
		return this.subjectName;
	}
	public void setSubjectAuthor(java.lang.String value) {
		this.subjectAuthor = value;
	}
	public java.lang.String getSubjectAuthor() {
		return this.subjectAuthor;
	}
	public void setPublishingHouse(java.lang.String value) {
		this.publishingHouse = value;
	}
	public java.lang.String getPublishingHouse() {
		return this.publishingHouse;
	}
	public void setSubjectDesc(java.lang.String value) {
		this.subjectDesc = value;
	}
	public java.lang.String getSubjectDesc() {
		return this.subjectDesc;
	}
	public void setSubjectRemark(java.lang.String value) {
		this.subjectRemark = value;
	}
	public java.lang.String getSubjectRemark() {
		return this.subjectRemark;
	}
	public void setSubjectTag(java.lang.String value) {
		this.subjectTag = value;
	}
	public java.lang.String getSubjectTag() {
		return this.subjectTag;
	}
	public void setSubjectLogo(java.lang.String value) {
		this.subjectLogo = value;
	}
	public java.lang.String getSubjectLogo() {
		return this.subjectLogo;
	}
	public void setSubjectType(java.lang.String value) {
		this.subjectType = value;
	}
	public java.lang.String getSubjectType() {
		return this.subjectType;
	}
	public void setSubjectVersion(java.lang.String value) {
		this.subjectVersion = value;
	}
	public java.lang.String getSubjectVersion() {
		return this.subjectVersion;
	}
	public void setLastVersionId(java.lang.String value) {
		this.lastVersionId = value;
	}
	public java.lang.String getLastVersionId() {
		return this.lastVersionId;
	}
	public void setSubjectPrice(java.math.BigDecimal value) {
		this.subjectPrice = value;
	}
	public java.math.BigDecimal getSubjectPrice() {
		return this.subjectPrice;
	}
	public void setClassScore(java.math.BigDecimal value) {
		this.classScore = value;
	}
	public java.math.BigDecimal getClassScore() {
		return this.classScore;
	}
	public void setClassHour(java.math.BigDecimal value) {
		this.classHour = value;
	}
	public java.math.BigDecimal getClassHour() {
		return this.classHour;
	}
	public void setSubjectRef(java.lang.String value) {
		this.subjectRef = value;
	}
	public java.lang.String getSubjectRef() {
		return this.subjectRef;
	}
	public void setDeployUserId(java.lang.String value) {
		this.deployUserId = value;
	}
	public java.lang.String getDeployUserId() {
		return this.deployUserId;
	}
	public void setDeployTime(java.lang.String value) {
		this.deployTime = value;
	}
	public java.lang.String getDeployTime() {
		return this.deployTime;
	}
	public void setDisplay(java.lang.String value) {
		this.display = value;
	}
	public java.lang.String getDisplay() {
		return this.display;
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

	public List<TevglBookChapter> getChildren() {
		return children;
	}

	public void setChildren(List<TevglBookChapter> children) {
		this.children = children;
	}

	public java.lang.String getSubjectTechnology() {
		return subjectTechnology;
	}

	public void setSubjectTechnology(java.lang.String subjectTechnology) {
		this.subjectTechnology = subjectTechnology;
	}

	public java.lang.Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(java.lang.Integer viewNum) {
		this.viewNum = viewNum;
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public List<String> getMajorIdList() {
		return majorIdList;
	}

	public void setMajorIdList(List<String> majorIdList) {
		this.majorIdList = majorIdList;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getSubjectProperty() {
		return subjectProperty;
	}

	public void setSubjectProperty(String subjectProperty) {
		this.subjectProperty = subjectProperty;
	}

	public java.lang.Integer getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(java.lang.Integer starLevel) {
		this.starLevel = starLevel;
	}

	public java.lang.Integer getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(java.lang.Integer storeNum) {
		this.storeNum = storeNum;
	}

	public java.lang.Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(java.lang.Integer likeNum) {
		this.likeNum = likeNum;
	}

	public java.lang.Integer getReportNum() {
		return reportNum;
	}

	public void setReportNum(java.lang.Integer reportNum) {
		this.reportNum = reportNum;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}
	
}

