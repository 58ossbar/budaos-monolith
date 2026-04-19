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

public class TevglSiteVideoRelation extends BaseDomain<TevglSiteVideoRelation>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteVideoRelation";
	public static final String ALIAS_VR_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_VIDEO_ID = "视频id";
	

    /**
     * 主键id       db_column: vr_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String vrId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 视频id       db_column: video_id 
     */	
 	@NotNull(msg="视频id不能为空")
 	@MaxLength(value=32, msg="字段[视频id]超出最大长度[32]")
	private java.lang.String videoId;
	//columns END

	public TevglSiteVideoRelation(){
	}

	public TevglSiteVideoRelation(
		java.lang.String vrId
	){
		this.vrId = vrId;
	}

	public void setVrId(java.lang.String value) {
		this.vrId = value;
	}
	public java.lang.String getVrId() {
		return this.vrId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setVideoId(java.lang.String value) {
		this.videoId = value;
	}
	public java.lang.String getVideoId() {
		return this.videoId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

