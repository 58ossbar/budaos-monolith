package com.budaos.modules.im.domain;

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

public class TimChatListTop extends BaseDomain<TimChatListTop>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimChatListTop";
	public static final String ALIAS_TOP_ID = "主键id";
	public static final String ALIAS_LIST_ID = "列表id";
	public static final String ALIAS_USER_ID = "用户id";
	public static final String ALIAS_TOP_TIME = "置顶时间";
	

    /**
     * 主键id       db_column: top_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String topId;
    /**
     * 列表id       db_column: list_id 
     */	
 	@NotNull(msg="列表id不能为空")
 	@MaxLength(value=32, msg="字段[列表id]超出最大长度[32]")
	private java.lang.String listId;
    /**
     * 用户id       db_column: user_id 
     */	
 	@NotNull(msg="用户id不能为空")
 	@MaxLength(value=32, msg="字段[用户id]超出最大长度[32]")
	private java.lang.String userId;
    /**
     * 置顶时间       db_column: top_time 
     */	
 	@NotNull(msg="置顶时间不能为空")
 	@MaxLength(value=20, msg="字段[置顶时间]超出最大长度[20]")
	private java.lang.String topTime;
	//columns END

	public TimChatListTop(){
	}

	public TimChatListTop(
		java.lang.String topId
	){
		this.topId = topId;
	}

	public void setTopId(java.lang.String value) {
		this.topId = value;
	}
	public java.lang.String getTopId() {
		return this.topId;
	}
	public void setListId(java.lang.String value) {
		this.listId = value;
	}
	public java.lang.String getListId() {
		return this.listId;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setTopTime(java.lang.String value) {
		this.topTime = value;
	}
	public java.lang.String getTopTime() {
		return this.topTime;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

