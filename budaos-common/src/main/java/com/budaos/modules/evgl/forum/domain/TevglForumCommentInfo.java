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

public class TevglForumCommentInfo extends BaseDomain<TevglForumCommentInfo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglForumCommentInfo";
	public static final String ALIAS_CI_ID = "评论id";
	public static final String ALIAS_CUST_ID = "评论客户id";
	public static final String ALIAS_CUST_NICKNAME = "评论客户昵称";
	public static final String ALIAS_CUST_HEADIMG = "评论客户头像";
	public static final String ALIAS_CUST_STAR = "客户星级";
	public static final String ALIAS_PARENT_ID = "评论目标id";
	public static final String ALIAS_PARENT_TYPE = "评论目标类型(1评论2视频3文库)";
	public static final String ALIAS_CI_CONTENT = "评论内容";
	public static final String ALIAS_CI_TIME = "评论时间";
	public static final String ALIAS_CI_FLOOR = "评论楼层";
	public static final String ALIAS_CI_TOTAL = "评论数量";
	public static final String ALIAS_CI_REPORT = "举报次数";
	public static final String ALIAS_CI_LIKE = "点赞次数";
	public static final String ALIAS_STATE = "评论状态(Y有效N无效)";
	public static final String ALIAS_TARGET_ID = "最初起源于什么开始评论";
	

    /**
     * 评论id       db_column: ci_id 
     */	
 	@NotNull(msg="评论id不能为空")
 	@MaxLength(value=32, msg="字段[评论id]超出最大长度[32]")
	private java.lang.String ciId;
    /**
     * 评论客户id       db_column: cust_id 
     */	
 	@NotNull(msg="评论客户id不能为空")
 	@MaxLength(value=32, msg="字段[评论客户id]超出最大长度[32]")
	private java.lang.String custId;
    /**
     * 评论客户昵称       db_column: cust_nickname 
     */	
 	@NotNull(msg="评论客户昵称不能为空")
 	@MaxLength(value=50, msg="字段[评论客户昵称]超出最大长度[50]")
	private java.lang.String custNickname;
    /**
     * 评论客户头像       db_column: cust_headimg 
     */	
 	@NotNull(msg="评论客户头像不能为空")
 	@MaxLength(value=1000, msg="字段[评论客户头像]超出最大长度[1000]")
	private java.lang.String custHeadimg;
    /**
     * 客户星级       db_column: cust_star 
     */	
 	@NotNull(msg="客户星级不能为空")
 	@MaxLength(value=32, msg="字段[客户星级]超出最大长度[32]")
	private java.lang.String custStar;
    /**
     * 评论目标id       db_column: parent_id 
     */	
 	@NotNull(msg="评论目标id不能为空")
 	@MaxLength(value=32, msg="字段[评论目标id]超出最大长度[32]")
	private java.lang.String parentId;
    /**
     * 评论目标类型(1评论2视频3文库)       db_column: parent_type 
     */	
 	@NotNull(msg="评论目标类型(1评论2视频3文库)不能为空")
 	@MaxLength(value=32, msg="字段[评论目标类型(1评论2视频3文库)]超出最大长度[32]")
	private java.lang.String parentType;
    /**
     * 评论内容       db_column: ci_content 
     */	
 	@NotNull(msg="评论内容不能为空")
 	@MaxLength(value=2147483647, msg="字段[评论内容]超出最大长度[2147483647]")
	private java.lang.String ciContent;
    /**
     * 评论时间       db_column: ci_time 
     */	
 	@NotNull(msg="评论时间不能为空")
 	@MaxLength(value=20, msg="字段[评论时间]超出最大长度[20]")
	private java.lang.String ciTime;
    /**
     * 评论楼层       db_column: ci_floor 
     */	
 	@NotNull(msg="评论楼层不能为空")
 	@MaxLength(value=10, msg="字段[评论楼层]超出最大长度[10]")
	private java.lang.Integer ciFloor;
    /**
     * 评论数量       db_column: ci_total 
     */	
 	@NotNull(msg="评论数量不能为空")
 	@MaxLength(value=10, msg="字段[评论数量]超出最大长度[10]")
	private java.lang.Integer ciTotal;
    /**
     * 举报次数       db_column: ci_report 
     */	
 	@NotNull(msg="举报次数不能为空")
 	@MaxLength(value=10, msg="字段[举报次数]超出最大长度[10]")
	private java.lang.Integer ciReport;
    /**
     * 点赞次数       db_column: ci_like 
     */	
 	@NotNull(msg="点赞次数不能为空")
 	@MaxLength(value=10, msg="字段[点赞次数]超出最大长度[10]")
	private java.lang.Integer ciLike;
    /**
     * 评论状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="评论状态(Y有效N无效)不能为空")
 	@MaxLength(value=5, msg="字段[评论状态(Y有效N无效)]超出最大长度[5]")
	private java.lang.String state;
    /**
     * 最初起源于什么开始评论       db_column: target_id 
     */	
 	@NotNull(msg="最初起源于什么开始评论不能为空")
 	@MaxLength(value=32, msg="字段[最初起源于什么开始评论]超出最大长度[32]")
	private java.lang.String targetId;
	//columns END

	public TevglForumCommentInfo(){
	}

	public TevglForumCommentInfo(
		java.lang.String ciId
	){
		this.ciId = ciId;
	}

	public void setCiId(java.lang.String value) {
		this.ciId = value;
	}
	public java.lang.String getCiId() {
		return this.ciId;
	}
	public void setCustId(java.lang.String value) {
		this.custId = value;
	}
	public java.lang.String getCustId() {
		return this.custId;
	}
	public void setCustNickname(java.lang.String value) {
		this.custNickname = value;
	}
	public java.lang.String getCustNickname() {
		return this.custNickname;
	}
	public void setCustHeadimg(java.lang.String value) {
		this.custHeadimg = value;
	}
	public java.lang.String getCustHeadimg() {
		return this.custHeadimg;
	}
	public void setCustStar(java.lang.String value) {
		this.custStar = value;
	}
	public java.lang.String getCustStar() {
		return this.custStar;
	}
	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}
	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setParentType(java.lang.String value) {
		this.parentType = value;
	}
	public java.lang.String getParentType() {
		return this.parentType;
	}
	public void setCiContent(java.lang.String value) {
		this.ciContent = value;
	}
	public java.lang.String getCiContent() {
		return this.ciContent;
	}
	public void setCiTime(java.lang.String value) {
		this.ciTime = value;
	}
	public java.lang.String getCiTime() {
		return this.ciTime;
	}
	public void setCiFloor(java.lang.Integer value) {
		this.ciFloor = value;
	}
	public java.lang.Integer getCiFloor() {
		return this.ciFloor;
	}
	public void setCiTotal(java.lang.Integer value) {
		this.ciTotal = value;
	}
	public java.lang.Integer getCiTotal() {
		return this.ciTotal;
	}
	public void setCiReport(java.lang.Integer value) {
		this.ciReport = value;
	}
	public java.lang.Integer getCiReport() {
		return this.ciReport;
	}
	public void setCiLike(java.lang.Integer value) {
		this.ciLike = value;
	}
	public java.lang.Integer getCiLike() {
		return this.ciLike;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setTargetId(java.lang.String value) {
		this.targetId = value;
	}
	public java.lang.String getTargetId() {
		return this.targetId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

