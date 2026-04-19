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

public class TevglForumBlogPost extends BaseDomain<TevglForumBlogPost>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglForumBlogPost";
	public static final String ALIAS_POST_ID = "主键ID";
	public static final String ALIAS_TRAINEE_ID = "博主";
	public static final String ALIAS_SUBJECT_ID = "所属课程";
	public static final String ALIAS_POST_TITLE = "主题";
	public static final String ALIAS_POST_CONTENT = "内容";
	public static final String ALIAS_POST_LABLE = "文章标签";
	public static final String ALIAS_POST_TIME = "发表时间";
	public static final String ALIAS_POST_IS_REPLY = "是否可评论";
	public static final String ALIAS_POST_IS_RECOMMEND = "是否推荐";
	public static final String ALIAS_POST_IS_CHOICE = "是否精选";
	public static final String ALIAS_POST_IS_STICK = "是否置顶";
	public static final String ALIAS_POST_STICK_SORT = "置顶排序号";
	public static final String ALIAS_POST_IS_RESOLVE = "是否已解决";
	public static final String ALIAS_VIEW_NUM = "查阅数";
	public static final String ALIAS_STORE_NUM = "收藏数";
	public static final String ALIAS_REPLY_NUM = "回复数";
	public static final String ALIAS_LIKE_NUM = "点赞数";
	public static final String ALIAS_REPORT_NUM = "举报数";
	public static final String ALIAS_LAST_REPLY_TRAINEE = "最近回复人";
	public static final String ALIAS_LAST_REPLY_TIME = "最近回复时间";
	public static final String ALIAS_STATE = "状态(Y正常N删除)";
	public static final String ALIAS_BLOG_ID = "博客ID";
	public static final String ALIAS_BLOG_TYPE = "版块ID";
	public static final String ALIAS_LABEL = "标签";
	

    /**
     * 主键ID       db_column: post_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String postId;
    /**
     * 博主       db_column: trainee_id 
     */	
 	@NotNull(msg="博主不能为空")
 	@MaxLength(value=32, msg="字段[博主]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 所属课程       db_column: subject_id 
     */	
 	//@NotNull(msg="所属课程不能为空")
 	@MaxLength(value=32, msg="字段[所属课程]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 主题       db_column: post_title 
     */	
 	@NotNull(msg="主题不能为空")
 	@MaxLength(value=200, msg="字段[主题]超出最大长度[200]")
	private java.lang.String postTitle;
    /**
     * 内容       db_column: post_content 
     */	
 	@NotNull(msg="内容不能为空")
 	@MaxLength(value=2147483647, msg="字段[内容]超出最大长度[2147483647]")
	private java.lang.String postContent;
    /**
     * 文章标签       db_column: post_lable 
     */	
 	@NotNull(msg="文章标签不能为空")
 	@MaxLength(value=200, msg="字段[文章标签]超出最大长度[200]")
	private java.lang.String postLable;
    /**
     * 发表时间       db_column: post_time 
     */	
 	@NotNull(msg="发表时间不能为空")
 	@MaxLength(value=20, msg="字段[发表时间]超出最大长度[20]")
	private java.lang.String postTime;
    /**
     * 是否可评论       db_column: post_is_reply 
     */	
 	//@NotNull(msg="是否可评论不能为空")
 	@MaxLength(value=1, msg="字段[是否可评论]超出最大长度[1]")
	private java.lang.String postIsReply;
    /**
     * 是否推荐       db_column: post_is_recommend 
     */	
 	//@NotNull(msg="是否推荐不能为空")
 	@MaxLength(value=1, msg="字段[是否推荐]超出最大长度[1]")
	private java.lang.String postIsRecommend;
    /**
     * 是否精选       db_column: post_is_choice 
     */	
 	//@NotNull(msg="是否精选不能为空")
 	@MaxLength(value=1, msg="字段[是否精选]超出最大长度[1]")
	private java.lang.String postIsChoice;
    /**
     * 是否置顶       db_column: post_is_stick 
     */	
 	//@NotNull(msg="是否置顶不能为空")
 	@MaxLength(value=1, msg="字段[是否置顶]超出最大长度[1]")
	private java.lang.String postIsStick;
    /**
     * 置顶排序号       db_column: post_stick_sort 
     */	
 	//@NotNull(msg="置顶排序号不能为空")
 	@MaxLength(value=10, msg="字段[置顶排序号]超出最大长度[10]")
	private java.lang.Integer postStickSort;
    /**
     * 是否已解决       db_column: post_is_resolve 
     */	
 	//@NotNull(msg="是否已解决不能为空")
 	@MaxLength(value=1, msg="字段[是否已解决]超出最大长度[1]")
	private java.lang.String postIsResolve;
    /**
     * 查阅数       db_column: view_num 
     */	
 	//@NotNull(msg="查阅数不能为空")
 	@MaxLength(value=10, msg="字段[查阅数]超出最大长度[10]")
	private java.lang.Integer viewNum;
    /**
     * 收藏数       db_column: store_num 
     */	
 	//@NotNull(msg="收藏数不能为空")
 	@MaxLength(value=10, msg="字段[收藏数]超出最大长度[10]")
	private java.lang.Integer storeNum;
    /**
     * 回复数       db_column: reply_num 
     */	
 	//@NotNull(msg="回复数不能为空")
 	@MaxLength(value=10, msg="字段[回复数]超出最大长度[10]")
	private java.lang.Integer replyNum;
    /**
     * 点赞数       db_column: like_num 
     */	
 	//@NotNull(msg="点赞数不能为空")
 	@MaxLength(value=10, msg="字段[点赞数]超出最大长度[10]")
	private java.lang.Integer likeNum;
    /**
     * 举报数       db_column: report_num 
     */	
 	//@NotNull(msg="举报数不能为空")
 	@MaxLength(value=10, msg="字段[举报数]超出最大长度[10]")
	private java.lang.Integer reportNum;
    /**
     * 最近回复人       db_column: last_reply_trainee 
     */	
 	//@NotNull(msg="最近回复人不能为空")
 	@MaxLength(value=32, msg="字段[最近回复人]超出最大长度[32]")
	private java.lang.String lastReplyTrainee;
    /**
     * 最近回复时间       db_column: last_reply_time 
     */	
 	//@NotNull(msg="最近回复时间不能为空")
 	@MaxLength(value=20, msg="字段[最近回复时间]超出最大长度[20]")
	private java.lang.String lastReplyTime;
    /**
     * 状态(Y正常N删除)       db_column: state 
     */	
 	@NotNull(msg="状态(Y正常N删除)不能为空")
 	@MaxLength(value=5, msg="字段[状态(Y正常N删除)]超出最大长度[5]")
	private java.lang.String state;
    /**
     * 博客ID       db_column: blog_id 
     */	
 	//@NotNull(msg="博客ID不能为空")
 	@MaxLength(value=32, msg="字段[博客ID]超出最大长度[32]")
	private java.lang.String blogId;
    /**
     * 版块ID       db_column: blog_type 
     */	
 	//@NotNull(msg="版块ID不能为空")
 	@MaxLength(value=255, msg="字段[版块ID]超出最大长度[255]")
	private java.lang.String blogType;
    /**
     * 标签       db_column: dict_code 
     */	
 	@NotNull(msg="技术标签不能为空")
 	@MaxLength(value=500, msg="字段[技术标签]超出最大长度[10]")
	private java.lang.String dictCode;
	//columns END

	public TevglForumBlogPost(){
	}

	public TevglForumBlogPost(
		java.lang.String postId
	){
		this.postId = postId;
	}

	public void setPostId(java.lang.String value) {
		this.postId = value;
	}
	public java.lang.String getPostId() {
		return this.postId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setPostTitle(java.lang.String value) {
		this.postTitle = value;
	}
	public java.lang.String getPostTitle() {
		return this.postTitle;
	}
	public void setPostContent(java.lang.String value) {
		this.postContent = value;
	}
	public java.lang.String getPostContent() {
		return this.postContent;
	}
	public void setPostLable(java.lang.String value) {
		this.postLable = value;
	}
	public java.lang.String getPostLable() {
		return this.postLable;
	}
	public void setPostTime(java.lang.String value) {
		this.postTime = value;
	}
	public java.lang.String getPostTime() {
		return this.postTime;
	}
	public void setPostIsReply(java.lang.String value) {
		this.postIsReply = value;
	}
	public java.lang.String getPostIsReply() {
		return this.postIsReply;
	}
	public void setPostIsRecommend(java.lang.String value) {
		this.postIsRecommend = value;
	}
	public java.lang.String getPostIsRecommend() {
		return this.postIsRecommend;
	}
	public void setPostIsChoice(java.lang.String value) {
		this.postIsChoice = value;
	}
	public java.lang.String getPostIsChoice() {
		return this.postIsChoice;
	}
	public void setPostIsStick(java.lang.String value) {
		this.postIsStick = value;
	}
	public java.lang.String getPostIsStick() {
		return this.postIsStick;
	}
	public void setPostStickSort(java.lang.Integer value) {
		this.postStickSort = value;
	}
	public java.lang.Integer getPostStickSort() {
		return this.postStickSort;
	}
	public void setPostIsResolve(java.lang.String value) {
		this.postIsResolve = value;
	}
	public java.lang.String getPostIsResolve() {
		return this.postIsResolve;
	}
	public void setViewNum(java.lang.Integer value) {
		this.viewNum = value;
	}
	public java.lang.Integer getViewNum() {
		return this.viewNum;
	}
	public void setStoreNum(java.lang.Integer value) {
		this.storeNum = value;
	}
	public java.lang.Integer getStoreNum() {
		return this.storeNum;
	}
	public void setReplyNum(java.lang.Integer value) {
		this.replyNum = value;
	}
	public java.lang.Integer getReplyNum() {
		return this.replyNum;
	}
	public void setLikeNum(java.lang.Integer value) {
		this.likeNum = value;
	}
	public java.lang.Integer getLikeNum() {
		return this.likeNum;
	}
	public void setReportNum(java.lang.Integer value) {
		this.reportNum = value;
	}
	public java.lang.Integer getReportNum() {
		return this.reportNum;
	}
	public void setLastReplyTrainee(java.lang.String value) {
		this.lastReplyTrainee = value;
	}
	public java.lang.String getLastReplyTrainee() {
		return this.lastReplyTrainee;
	}
	public void setLastReplyTime(java.lang.String value) {
		this.lastReplyTime = value;
	}
	public java.lang.String getLastReplyTime() {
		return this.lastReplyTime;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setBlogId(java.lang.String value) {
		this.blogId = value;
	}
	public java.lang.String getBlogId() {
		return this.blogId;
	}
	public void setBlogType(java.lang.String value) {
		this.blogType = value;
	}
	public java.lang.String getBlogType() {
		return this.blogType;
	}
	public java.lang.String getDictCode() {
		return dictCode;
	}
	public void setDictCode(java.lang.String dictCode) {
		this.dictCode = dictCode;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

