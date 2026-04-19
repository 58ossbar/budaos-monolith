package com.budaos.modules.evgl.forum.domain;

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

public class TevglForumBlogerInfo extends BaseDomain<TevglForumBlogerInfo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglForumBlogerInfo";
	public static final String ALIAS_BLOG_ID = "主键ID";
	public static final String ALIAS_TRAINEE_ID = "博主ID";
	public static final String ALIAS_TRAINEE_SEX = "博主性别";
	public static final String ALIAS_TRAINEE_AGE = "博主年龄";
	public static final String ALIAS_TRAINEE_NAME = "博主姓名";
	public static final String ALIAS_TRAINEE_HEADIMG = "博主头像";
	public static final String ALIAS_CLASS_ID = "博主所在班级";
	public static final String ALIAS_CLASS_NAME = "班级名称";
	public static final String ALIAS_BLOG_NAME = "博客名称";
	public static final String ALIAS_BLOG_SUMMAR = "博客简介";
	public static final String ALIAS_BLOG_LOGO = "博客主题logo";
	public static final String ALIAS_BLOG_ATTENTION_NUM = "博客总关注数";
	public static final String ALIAS_BLOG_FANS_NUM = "博客总粉丝数";
	public static final String ALIAS_BLOG_TOPIC_NUM = "博客总文章数";
	public static final String ALIAS_BLOG_REPLY_NUM = "博客总回复数";
	public static final String ALIAS_BLOG_ACCESS_NUM = "博客总访问量";
	public static final String ALIAS_REGISTER_TIME = "注册时间";
	public static final String ALIAS_STATE = "状态(Y正常N注销)";
	

    /**
     * 主键ID       db_column: blog_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String blogId;
    /**
     * 博主ID       db_column: trainee_id 
     */	
 	@NotNull(msg="博主ID不能为空")
 	@MaxLength(value=32, msg="字段[博主ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 博主性别       db_column: trainee_sex 
     */	
 	@NotNull(msg="博主性别不能为空")
 	@MaxLength(value=2, msg="字段[博主性别]超出最大长度[2]")
	private java.lang.String traineeSex;
    /**
     * 博主年龄       db_column: trainee_age 
     */	
 	@NotNull(msg="博主年龄不能为空")
 	@MaxLength(value=10, msg="字段[博主年龄]超出最大长度[10]")
	private java.lang.Integer traineeAge;
    /**
     * 博主姓名       db_column: trainee_name 
     */	
 	@NotNull(msg="博主姓名不能为空")
 	@MaxLength(value=50, msg="字段[博主姓名]超出最大长度[50]")
	private java.lang.String traineeName;
    /**
     * 博主头像       db_column: trainee_headimg 
     */	
 	@NotNull(msg="博主头像不能为空")
 	@MaxLength(value=1000, msg="字段[博主头像]超出最大长度[1000]")
	private java.lang.String traineeHeadimg;
    /**
     * 博主所在班级       db_column: class_id 
     */	
 	@NotNull(msg="博主所在班级不能为空")
 	@MaxLength(value=32, msg="字段[博主所在班级]超出最大长度[32]")
	private java.lang.String classId;
    /**
     * 班级名称       db_column: class_name 
     */	
 	@NotNull(msg="班级名称不能为空")
 	@MaxLength(value=100, msg="字段[班级名称]超出最大长度[100]")
	private java.lang.String className;
    /**
     * 博客名称       db_column: blog_name 
     */	
 	@NotNull(msg="博客名称不能为空")
 	@MaxLength(value=100, msg="字段[博客名称]超出最大长度[100]")
	private java.lang.String blogName;
    /**
     * 博客简介       db_column: blog_summar 
     */	
 	@NotNull(msg="博客简介不能为空")
 	@MaxLength(value=200, msg="字段[博客简介]超出最大长度[200]")
	private java.lang.String blogSummar;
    /**
     * 博客主题logo       db_column: blog_logo 
     */	
 	@NotNull(msg="博客主题logo不能为空")
 	@MaxLength(value=1000, msg="字段[博客主题logo]超出最大长度[1000]")
	private java.lang.String blogLogo;
    /**
     * 博客总关注数       db_column: blog_attention_num 
     */	
 	@NotNull(msg="博客总关注数不能为空")
 	@MaxLength(value=10, msg="字段[博客总关注数]超出最大长度[10]")
	private java.lang.Integer blogAttentionNum;
    /**
     * 博客总粉丝数       db_column: blog_fans_num 
     */	
 	@NotNull(msg="博客总粉丝数不能为空")
 	@MaxLength(value=10, msg="字段[博客总粉丝数]超出最大长度[10]")
	private java.lang.Integer blogFansNum;
    /**
     * 博客总文章数       db_column: blog_topic_num 
     */	
 	@NotNull(msg="博客总文章数不能为空")
 	@MaxLength(value=10, msg="字段[博客总文章数]超出最大长度[10]")
	private java.lang.Integer blogTopicNum;
    /**
     * 博客总回复数       db_column: blog_reply_num 
     */	
 	@NotNull(msg="博客总回复数不能为空")
 	@MaxLength(value=10, msg="字段[博客总回复数]超出最大长度[10]")
	private java.lang.Integer blogReplyNum;
    /**
     * 博客总访问量       db_column: blog_access_num 
     */	
 	@NotNull(msg="博客总访问量不能为空")
 	@MaxLength(value=10, msg="字段[博客总访问量]超出最大长度[10]")
	private java.lang.Integer blogAccessNum;
    /**
     * 注册时间       db_column: register_time 
     */	
 	@NotNull(msg="注册时间不能为空")
 	@MaxLength(value=20, msg="字段[注册时间]超出最大长度[20]")
	private java.lang.String registerTime;
    /**
     * 状态(Y正常N注销)       db_column: state 
     */	
 	@NotNull(msg="状态(Y正常N注销)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y正常N注销)]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglForumBlogerInfo(){
	}

	public TevglForumBlogerInfo(
		java.lang.String blogId
	){
		this.blogId = blogId;
	}

	public void setBlogId(java.lang.String value) {
		this.blogId = value;
	}
	public java.lang.String getBlogId() {
		return this.blogId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setTraineeSex(java.lang.String value) {
		this.traineeSex = value;
	}
	public java.lang.String getTraineeSex() {
		return this.traineeSex;
	}
	public void setTraineeAge(java.lang.Integer value) {
		this.traineeAge = value;
	}
	public java.lang.Integer getTraineeAge() {
		return this.traineeAge;
	}
	public void setTraineeName(java.lang.String value) {
		this.traineeName = value;
	}
	public java.lang.String getTraineeName() {
		return this.traineeName;
	}
	public void setTraineeHeadimg(java.lang.String value) {
		this.traineeHeadimg = value;
	}
	public java.lang.String getTraineeHeadimg() {
		return this.traineeHeadimg;
	}
	public void setClassId(java.lang.String value) {
		this.classId = value;
	}
	public java.lang.String getClassId() {
		return this.classId;
	}
	public void setClassName(java.lang.String value) {
		this.className = value;
	}
	public java.lang.String getClassName() {
		return this.className;
	}
	public void setBlogName(java.lang.String value) {
		this.blogName = value;
	}
	public java.lang.String getBlogName() {
		return this.blogName;
	}
	public void setBlogSummar(java.lang.String value) {
		this.blogSummar = value;
	}
	public java.lang.String getBlogSummar() {
		return this.blogSummar;
	}
	public void setBlogLogo(java.lang.String value) {
		this.blogLogo = value;
	}
	public java.lang.String getBlogLogo() {
		return this.blogLogo;
	}
	public void setBlogAttentionNum(java.lang.Integer value) {
		this.blogAttentionNum = value;
	}
	public java.lang.Integer getBlogAttentionNum() {
		return this.blogAttentionNum;
	}
	public void setBlogFansNum(java.lang.Integer value) {
		this.blogFansNum = value;
	}
	public java.lang.Integer getBlogFansNum() {
		return this.blogFansNum;
	}
	public void setBlogTopicNum(java.lang.Integer value) {
		this.blogTopicNum = value;
	}
	public java.lang.Integer getBlogTopicNum() {
		return this.blogTopicNum;
	}
	public void setBlogReplyNum(java.lang.Integer value) {
		this.blogReplyNum = value;
	}
	public java.lang.Integer getBlogReplyNum() {
		return this.blogReplyNum;
	}
	public void setBlogAccessNum(java.lang.Integer value) {
		this.blogAccessNum = value;
	}
	public java.lang.Integer getBlogAccessNum() {
		return this.blogAccessNum;
	}
	public void setRegisterTime(java.lang.String value) {
		this.registerTime = value;
	}
	public java.lang.String getRegisterTime() {
		return this.registerTime;
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

