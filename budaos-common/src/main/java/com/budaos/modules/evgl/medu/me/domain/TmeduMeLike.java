package com.budaos.modules.evgl.medu.me.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 点赞表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TmeduMeLike extends BaseDomain<TmeduMeLike>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TmeduMeLike";
	public static final String ALIAS_LIKE_ID = "主键id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_LIKE_TYPE = "点赞类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂、12投票/问卷、13头脑风暴、14学员作答头脑风暴、15答疑讨论的聊天内容))";
	public static final String ALIAS_TARGET_ID = "点赞目标id";
	public static final String ALIAS_LIKE_TIME = "点赞时间";
	public static final String ALIAS_TARGET_TRAINEE_ID = "被点赞记录的所属人";
	public static final String ALIAS_READ_STATE = "是否已读(Y已读N未读)";
	

    /**
     * 主键id       db_column: like_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String likeId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 点赞类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂、12投票/问卷、13头脑风暴、14学员作答头脑风暴、15答疑讨论的聊天内容))       db_column: like_type 
     */	
 	@NotNull(msg="点赞类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂、12投票/问卷、13头脑风暴、14学员作答头脑风暴、15答疑讨论的聊天内容))不能为空")
 	@MaxLength(value=2, msg="字段[点赞类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂、12投票/问卷、13头脑风暴、14学员作答头脑风暴、15答疑讨论的聊天内容))]超出最大长度[2]")
	private java.lang.String likeType;
    /**
     * 点赞目标id       db_column: target_id 
     */	
 	@NotNull(msg="点赞目标id不能为空")
 	@MaxLength(value=32, msg="字段[点赞目标id]超出最大长度[32]")
	private java.lang.String targetId;
    /**
     * 点赞时间       db_column: like_time 
     */	
 	@NotNull(msg="点赞时间不能为空")
 	@MaxLength(value=20, msg="字段[点赞时间]超出最大长度[20]")
	private java.lang.String likeTime;
    /**
     * 被点赞记录的所属人       db_column: target_trainee_id 
     */	
 	@NotNull(msg="被点赞记录的所属人不能为空")
 	@MaxLength(value=32, msg="字段[被点赞记录的所属人]超出最大长度[32]")
	private java.lang.String targetTraineeId;
 	 /**
     * 是否已读(Y已读N未读)       db_column: read_state 
     */	
 	@NotNull(msg="是否已读(Y已读N未读)不能为空")
 	@MaxLength(value=2, msg="字段[是否已读(Y已读N未读)]超出最大长度[2]")
	private java.lang.String readState;
	//columns END

	public TmeduMeLike(){
	}

	public TmeduMeLike(
		java.lang.String likeId
	){
		this.likeId = likeId;
	}

	public void setLikeId(java.lang.String value) {
		this.likeId = value;
	}
	public java.lang.String getLikeId() {
		return this.likeId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setLikeType(java.lang.String value) {
		this.likeType = value;
	}
	public java.lang.String getLikeType() {
		return this.likeType;
	}
	public void setTargetId(java.lang.String value) {
		this.targetId = value;
	}
	public java.lang.String getTargetId() {
		return this.targetId;
	}
	public void setLikeTime(java.lang.String value) {
		this.likeTime = value;
	}
	public java.lang.String getLikeTime() {
		return this.likeTime;
	}
	public void setTargetTraineeId(java.lang.String value) {
		this.targetTraineeId = value;
	}
	public java.lang.String getTargetTraineeId() {
		return this.targetTraineeId;
	}
	public java.lang.String getReadState() {
		return readState;
	}
	public void setReadState(java.lang.String readState) {
		this.readState = readState;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

