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

public class TevglSiteVideoMgr extends BaseDomain<TevglSiteVideoMgr>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteVideoMgr";
	public static final String ALIAS_VM_ID = "主键id";
	public static final String ALIAS_VIDEO_ID = "视频id";
	public static final String ALIAS_MAJOR_ID = "专业id(职业路径)";
	

    /**
     * 主键id       db_column: vm_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String vmId;
    /**
     * 视频id       db_column: video_id 
     */	
 	@NotNull(msg="视频id不能为空")
 	@MaxLength(value=32, msg="字段[视频id]超出最大长度[32]")
	private java.lang.String videoId;
    /**
     * 专业id(职业路径)       db_column: major_id 
     */	
 	@NotNull(msg="专业id(职业路径)不能为空")
 	@MaxLength(value=32, msg="字段[专业id(职业路径)]超出最大长度[32]")
	private java.lang.String majorId;
	//columns END

	public TevglSiteVideoMgr(){
	}

	public TevglSiteVideoMgr(
		java.lang.String vmId
	){
		this.vmId = vmId;
	}

	public void setVmId(java.lang.String value) {
		this.vmId = value;
	}
	public java.lang.String getVmId() {
		return this.vmId;
	}
	public void setVideoId(java.lang.String value) {
		this.videoId = value;
	}
	public java.lang.String getVideoId() {
		return this.videoId;
	}
	public void setMajorId(java.lang.String value) {
		this.majorId = value;
	}
	public java.lang.String getMajorId() {
		return this.majorId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

