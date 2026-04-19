package com.budaos.modules.evgl.site.domain;

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

public class TevglSiteFeedback extends BaseDomain<TevglSiteFeedback>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteFeedback";
	public static final String ALIAS_FEEDBACK_ID = "意见反馈id";
	public static final String ALIAS_TYPE = "类型(1网站2小程序3app)";
	public static final String ALIAS_FEEDBACK_TYPE = "意见反馈类型(1功能异常2体验问题3新功能建议4其它)";
	public static final String ALIAS_FEEDBACK_CONTENT = "内容";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_HAS_REPLIED = "是否已回复(Y已回复N未回复)";
	

    /**
     * 意见反馈id       db_column: feedback_id 
     */	
 	@NotNull(msg="意见反馈id不能为空")
 	@MaxLength(value=32, msg="字段[意见反馈id]超出最大长度[32]")
	private java.lang.String feedbackId;
    /**
     * 类型(1网站2小程序3app)       db_column: type 
     */	
 	@NotNull(msg="类型(1网站2小程序3app)不能为空")
 	@MaxLength(value=2, msg="字段[类型(1网站2小程序3app)]超出最大长度[2]")
	private java.lang.String type;
    /**
     * 意见反馈类型(1功能异常2体验问题3新功能建议4其它)       db_column: feedback_type 
     */	
 	@NotNull(msg="意见反馈类型(1功能异常2体验问题3新功能建议4其它)不能为空")
 	@MaxLength(value=2, msg="字段[意见反馈类型(1功能异常2体验问题3新功能建议4其它)]超出最大长度[2]")
	private java.lang.String feedbackType;
    /**
     * 内容       db_column: feedback_content 
     */	
 	@NotNull(msg="内容不能为空")
 	@MaxLength(value=2000, msg="字段[内容]超出最大长度[2000]")
	private java.lang.String feedbackContent;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 是否已回复(Y已回复N未回复)       db_column: has_replied 
     */	
 	@NotNull(msg="是否已回复(Y已回复N未回复)不能为空")
 	@MaxLength(value=2, msg="字段[是否已回复(Y已回复N未回复)]超出最大长度[2]")
	private java.lang.String hasReplied;
	//columns END

	public TevglSiteFeedback(){
	}

	public TevglSiteFeedback(
		java.lang.String feedbackId
	){
		this.feedbackId = feedbackId;
	}

	public void setFeedbackId(java.lang.String value) {
		this.feedbackId = value;
	}
	public java.lang.String getFeedbackId() {
		return this.feedbackId;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setFeedbackType(java.lang.String value) {
		this.feedbackType = value;
	}
	public java.lang.String getFeedbackType() {
		return this.feedbackType;
	}
	public void setFeedbackContent(java.lang.String value) {
		this.feedbackContent = value;
	}
	public java.lang.String getFeedbackContent() {
		return this.feedbackContent;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setHasReplied(java.lang.String value) {
		this.hasReplied = value;
	}
	public java.lang.String getHasReplied() {
		return this.hasReplied;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

