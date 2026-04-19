package com.budaos.modules.evgl.site.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglSiteVideo extends BaseDomain<TevglSiteVideo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteVideo";
	public static final String ALIAS_VIDEO_ID = "主键id";
	public static final String ALIAS_TITLE = "标题";
	public static final String ALIAS_NAME = "名称（uuid组成）";
	public static final String ALIAS_ORIGINAL_FILENAME = "源名称";
	public static final String ALIAS_TYPE = "所属分类(1辅教视频2公共视频)";
	public static final String ALIAS_FILE_SUFFIX = "文件后缀";
	public static final String ALIAS_FILE_SIZE = "文件大小(单位KB)";
	public static final String ALIAS_CHECK_STATE = "审核状态（0待审核1通过2未通过）";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_FIRST_CAPTURE_ACCESS_URL = "视频第1帧图片访问路径";
	public static final String ALIAS_FIRST_CAPTURE_SAVE_PATH = "视频第1帧图片实际路径";
	public static final String ALIAS_REMARK = "备注";
	public static final String ALIAS_BIG_TYPE = "预留字段";
	public static final String ALIAS_SYS_CREATE_USER_ID = "系统用户";
	

    /**
     * 主键id       db_column: video_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String videoId;
    /**
     * 标题       db_column: title 
     */	
 	//@NotNull(msg="标题不能为空")
 	@MaxLength(value=200, msg="字段[标题]超出最大长度[200]")
	private java.lang.String title;
    /**
     * 名称（uuid组成）       db_column: name 
     */	
 	@NotNull(msg="名称（uuid组成）不能为空")
 	@MaxLength(value=200, msg="字段[名称（uuid组成）]超出最大长度[200]")
	private java.lang.String name;
    /**
     * 源名称       db_column: original_filename 
     */	
 	//@NotNull(msg="源名称不能为空")
 	@MaxLength(value=200, msg="字段[源名称]超出最大长度[200]")
	private java.lang.String originalFilename;
    /**
     * 所属分类(1辅教视频2公共视频)       db_column: type 
     */	
 	@NotNull(msg="所属分类(1辅教视频2公共视频)不能为空")
 	@MaxLength(value=2, msg="字段[所属分类(1辅教视频2公共视频)]超出最大长度[2]")
	private java.lang.String type;
    /**
     * 文件后缀       db_column: file_suffix 
     */	
 	//@NotNull(msg="文件后缀不能为空")
 	@MaxLength(value=20, msg="字段[文件后缀]超出最大长度[20]")
	private java.lang.String fileSuffix;
    /**
     * 文件大小(单位KB)       db_column: file_size 
     */	
 	//@NotNull(msg="文件大小(单位KB)不能为空")
 	@MaxLength(value=19, msg="字段[文件大小(单位KB)]超出最大长度[19]")
	private java.lang.Long fileSize;
    /**
     * 审核状态（0待审核1通过2未通过）       db_column: check_state 
     */	
 	@NotNull(msg="审核状态（0待审核1通过2未通过）不能为空")
 	@MaxLength(value=2, msg="字段[审核状态（0待审核1通过2未通过）]超出最大长度[2]")
	private java.lang.String checkState;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 视频第1帧图片访问路径       db_column: first_capture_access_url 
     */	
 	//@NotNull(msg="视频第1帧图片访问路径不能为空")
 	@MaxLength(value=255, msg="字段[视频第1帧图片访问路径]超出最大长度[255]")
	private java.lang.String firstCaptureAccessUrl;
    /**
     * 视频第1帧图片实际路径       db_column: first_capture_save_path 
     */	
 	//@NotNull(msg="视频第1帧图片实际路径不能为空")
 	@MaxLength(value=255, msg="字段[视频第1帧图片实际路径]超出最大长度[255]")
	private java.lang.String firstCaptureSavePath;
    /**
     * 备注       db_column: remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=65535, msg="字段[备注]超出最大长度[65535]")
	private java.lang.String remark;
    /**
     * 预留字段       db_column: big_type 
     */	
 	//@NotNull(msg="预留字段不能为空")
 	@MaxLength(value=2, msg="字段[预留字段]超出最大长度[2]")
	private java.lang.String bigType;
 	/**
     * 系统用户       db_column: sys_create_user_id 
     */	
 	//@NotNull(msg="系统用户不能为空")
 	@MaxLength(value=32, msg="字段[系统用户]超出最大长度[32]")
	private java.lang.String sysCreateUserId;
	//columns END

 	private List<String> ctIdList;
 	private List<String> majorIdList;
 	private String majorId;
 	
	public TevglSiteVideo(){
	}

	public TevglSiteVideo(
		java.lang.String videoId
	){
		this.videoId = videoId;
	}

	public void setVideoId(java.lang.String value) {
		this.videoId = value;
	}
	public java.lang.String getVideoId() {
		return this.videoId;
	}
	public void setTitle(java.lang.String value) {
		this.title = value;
	}
	public java.lang.String getTitle() {
		return this.title;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setOriginalFilename(java.lang.String value) {
		this.originalFilename = value;
	}
	public java.lang.String getOriginalFilename() {
		return this.originalFilename;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setFileSuffix(java.lang.String value) {
		this.fileSuffix = value;
	}
	public java.lang.String getFileSuffix() {
		return this.fileSuffix;
	}
	public void setFileSize(java.lang.Long value) {
		this.fileSize = value;
	}
	public java.lang.Long getFileSize() {
		return this.fileSize;
	}
	public void setCheckState(java.lang.String value) {
		this.checkState = value;
	}
	public java.lang.String getCheckState() {
		return this.checkState;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setFirstCaptureAccessUrl(java.lang.String value) {
		this.firstCaptureAccessUrl = value;
	}
	public java.lang.String getFirstCaptureAccessUrl() {
		return this.firstCaptureAccessUrl;
	}
	public void setFirstCaptureSavePath(java.lang.String value) {
		this.firstCaptureSavePath = value;
	}
	public java.lang.String getFirstCaptureSavePath() {
		return this.firstCaptureSavePath;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setBigType(java.lang.String value) {
		this.bigType = value;
	}
	public java.lang.String getBigType() {
		return this.bigType;
	}
	public void setSysCreateUserId(java.lang.String value) {
		this.sysCreateUserId = value;
	}
	public java.lang.String getSysCreateUserId() {
		return this.sysCreateUserId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public List<String> getCtIdList() {
		return ctIdList;
	}

	public void setCtIdList(List<String> ctIdList) {
		this.ctIdList = ctIdList;
	}

	public List<String> getMajorIdList() {
		return majorIdList;
	}

	public void setMajorIdList(List<String> majorIdList) {
		this.majorIdList = majorIdList;
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}
	
}

