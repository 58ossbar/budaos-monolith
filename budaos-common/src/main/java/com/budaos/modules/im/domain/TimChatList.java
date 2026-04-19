package com.budaos.modules.im.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
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

public class TimChatList extends BaseDomain<TimChatList>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TimChatList";
	public static final String ALIAS_LIST_ID = "列表ID";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_FRIEND_ID = "好友ID";
	public static final String ALIAS_FRIEND_TYPE = "好友类型(1.私聊用户;2.群组)";
	public static final String ALIAS_SORT = "排序号";
	public static final String ALIAS_UNREAD_NUM = "未读消息数";
	public static final String ALIAS_TARGET_TYPE = "类型1答疑讨论";
	public static final String ALIAS_TARGET_ID = "目标id";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 列表ID       db_column: list_id 
     */	
 	//@NotNull(msg="列表ID不能为空")
 	@MaxLength(value=32, msg="字段[列表ID]超出最大长度[32]")
	private java.lang.String listId;
    /**
     * 用户ID       db_column: user_id 
     */	
 	//@NotNull(msg="用户ID不能为空")
 	@MaxLength(value=32, msg="字段[用户ID]超出最大长度[32]")
	private java.lang.String userId;
    /**
     * 好友ID       db_column: friend_id 
     */	
 	//@NotNull(msg="好友ID不能为空")
 	@MaxLength(value=32, msg="字段[好友ID]超出最大长度[32]")
	private java.lang.String friendId;
    /**
     * 好友类型(1.私聊用户;2.群组)       db_column: friend_type 
     */	
 	//@NotNull(msg="好友类型(1.私聊用户;2.群组)不能为空")
 	@MaxLength(value=10, msg="字段[好友类型(1.私聊用户;2.群组)]超出最大长度[10]")
	private java.lang.String friendType;
    /**
     * 好友类型(1.私聊用户;2.群组)       db_column: friend_type 
     */	
 	//@NotNull(msg="好友类型(1.私聊用户;2.群组)不能为空")
 	@MaxLength(value=20, msg="字段[最新消息内容]超出最大长度[20]")
	private java.lang.String content;
 	
    /**
     * 未读消息数       db_column: unread_num 
     */	
 	//@NotNull(msg="未读消息数不能为空")
 	@MaxLength(value=10, msg="字段[未读消息数]超出最大长度[10]")
	private java.lang.Integer unreadNum;
 	/**
     * 类型1答疑讨论       db_column: target_type 
     */	
 	//@NotNull(msg="类型1答疑讨论不能为空")
 	@MaxLength(value=2, msg="字段[类型1答疑讨论]超出最大长度[2]")
	private java.lang.String targetType;
    /**
     * 目标id       db_column: target_id 
     */	
 	//@NotNull(msg="目标id不能为空")
 	@MaxLength(value=32, msg="字段[目标id]超出最大长度[32]")
	private java.lang.String targetId;
 	/**
     * 状态(Y有效N无效)       db_column: state 
     */	
	//@NotNull(msg="状态(Y有效N无效)不能为空")
	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TimChatList(){
	}

	public TimChatList(
		java.lang.String listId
	){
		this.listId = listId;
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
	public void setFriendId(java.lang.String value) {
		this.friendId = value;
	}
	public java.lang.String getFriendId() {
		return this.friendId;
	}
	public void setFriendType(java.lang.String value) {
		this.friendType = value;
	}
	public java.lang.String getFriendType() {
		return this.friendType;
	}
	public void setUnreadNum(java.lang.Integer value) {
		this.unreadNum = value;
	}
	public java.lang.Integer getUnreadNum() {
		return this.unreadNum;
	}
	public java.lang.String getContent() {
		return content;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}

	public java.lang.String getTargetType() {
		return targetType;
	}

	public void setTargetType(java.lang.String targetType) {
		this.targetType = targetType;
	}

	public java.lang.String getTargetId() {
		return targetId;
	}

	public void setTargetId(java.lang.String targetId) {
		this.targetId = targetId;
	}

	public java.lang.String getState() {
		return state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

