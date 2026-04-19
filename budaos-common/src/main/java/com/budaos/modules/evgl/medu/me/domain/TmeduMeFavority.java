package com.budaos.modules.evgl.medu.me.domain;

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

public class TmeduMeFavority extends BaseDomain<TmeduMeFavority>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TmeduMeFavority";
	public static final String ALIAS_FAVORITY_ID = "我的收藏";
	public static final String ALIAS_TRAINEE_ID = "学员标识ID";
	public static final String ALIAS_FAVORITY_TYPE = "收藏类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂";
	public static final String ALIAS_TARGET_ID = "收藏目标ID";
	public static final String ALIAS_FAVORITY_TIME = "收藏时间";
	public static final String ALIAS_CLASS_ID = "班级标识ID";
	public static final String ALIAS_MAJOR_ID = "专业标识ID";
	

    /**
     * 我的收藏       db_column: favority_id 
     */	
 	@NotNull(msg="我的收藏不能为空")
 	@MaxLength(value=32, msg="字段[我的收藏]超出最大长度[32]")
	private java.lang.String favorityId;
    /**
     * 学员标识ID       db_column: trainee_id 
     */	
 	@NotNull(msg="学员标识ID不能为空")
 	@MaxLength(value=32, msg="字段[学员标识ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 收藏类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂       db_column: favority_type 
     */	
 	@NotNull(msg="收藏类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂不能为空")
 	@MaxLength(value=20, msg="字段[收藏类型，来源于字典，1:课程、2:视频、3:资讯、4:内部教材、5:开源文库、6:题目、7:博客、8:贴子、10活教材、11课堂]超出最大长度[20]")
	private java.lang.String favorityType;
    /**
     * 收藏目标ID       db_column: target_id 
     */	
 	@NotNull(msg="收藏目标ID不能为空")
 	@MaxLength(value=32, msg="字段[收藏目标ID]超出最大长度[32]")
	private java.lang.String targetId;
    /**
     * 收藏时间       db_column: favority_time 
     */	
 	@NotNull(msg="收藏时间不能为空")
 	@MaxLength(value=32, msg="字段[收藏时间]超出最大长度[32]")
	private java.lang.String favorityTime;
    /**
     * 班级标识ID       db_column: class_id 
     */	
 	//@NotNull(msg="班级标识ID不能为空")
 	//@MaxLength(value=32, msg="字段[班级标识ID]超出最大长度[32]")
	private java.lang.String classId;
    /**
     * 专业标识ID       db_column: major_id 
     */	
 	//@NotNull(msg="专业标识ID不能为空")
 	//@MaxLength(value=32, msg="字段[专业标识ID]超出最大长度[32]")
	private java.lang.String majorId;
	/**
     * 教学包id       db_column: pkg_id 
     */	
	private java.lang.String pkgId;
	//columns END

	public TmeduMeFavority(){
	}

	public TmeduMeFavority(
		java.lang.String favorityId
	){
		this.favorityId = favorityId;
	}

	public void setFavorityId(java.lang.String value) {
		this.favorityId = value;
	}
	public java.lang.String getFavorityId() {
		return this.favorityId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setFavorityType(java.lang.String value) {
		this.favorityType = value;
	}
	public java.lang.String getFavorityType() {
		return this.favorityType;
	}
	public void setTargetId(java.lang.String value) {
		this.targetId = value;
	}
	public java.lang.String getTargetId() {
		return this.targetId;
	}
	public void setFavorityTime(java.lang.String value) {
		this.favorityTime = value;
	}
	public java.lang.String getFavorityTime() {
		return this.favorityTime;
	}
	public void setClassId(java.lang.String value) {
		this.classId = value;
	}
	public java.lang.String getClassId() {
		return this.classId;
	}
	public void setMajorId(java.lang.String value) {
		this.majorId = value;
	}
	public java.lang.String getMajorId() {
		return this.majorId;
	}
	public java.lang.String getPkgId() {
		return pkgId;
	}
	public void setPkgId(java.lang.String pkgId) {
		this.pkgId = pkgId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

