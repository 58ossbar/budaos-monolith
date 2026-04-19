package com.budaos.modules.evgl.site.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 意见反馈回复表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglSiteFeedbackReply extends BaseDomain<TevglSiteFeedbackReply>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteFeedbackReply";
	public static final String ALIAS_REPLY_ID = "主键id";
	public static final String ALIAS_FEEDBACK_ID = "意见反馈id";
	public static final String ALIAS_CONTENT = "回复内容";
	

    /**
     * 主键id       db_column: reply_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String replyId;
    /**
     * 意见反馈id       db_column: feedback_id 
     */	
 	@NotNull(msg="意见反馈id不能为空")
 	@MaxLength(value=32, msg="字段[意见反馈id]超出最大长度[32]")
	private java.lang.String feedbackId;
    /**
     * 回复内容       db_column: content 
     */	
 	@NotNull(msg="回复内容不能为空")
 	@MaxLength(value=65535, msg="字段[回复内容]超出最大长度[65535]")
	private java.lang.String content;
	//columns END

	public TevglSiteFeedbackReply(){
	}

	public TevglSiteFeedbackReply(
		java.lang.String replyId
	){
		this.replyId = replyId;
	}

	public void setReplyId(java.lang.String value) {
		this.replyId = value;
	}
	public java.lang.String getReplyId() {
		return this.replyId;
	}
	public void setFeedbackId(java.lang.String value) {
		this.feedbackId = value;
	}
	public java.lang.String getFeedbackId() {
		return this.feedbackId;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	
	private TevglSiteFeedback tevglSiteFeedback;
	
	public void setTevglSiteFeedback(TevglSiteFeedback tevglSiteFeedback){
		this.tevglSiteFeedback = tevglSiteFeedback;
	}
	
	public TevglSiteFeedback getTevglSiteFeedback() {
		return tevglSiteFeedback;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

