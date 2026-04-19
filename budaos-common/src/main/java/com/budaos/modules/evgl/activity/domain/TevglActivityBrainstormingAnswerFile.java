package com.budaos.modules.evgl.activity.domain;

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

public class TevglActivityBrainstormingAnswerFile extends BaseDomain<TevglActivityBrainstormingAnswerFile>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivityBrainstormingAnswerFile";
	public static final String ALIAS_FI_ID = "主键id";
	public static final String ALIAS_AN_ID = "作答id（对应t_evgl_activity_brainstorming_trainee_answer主键）";
	public static final String ALIAS_URL = "文件路径";
	public static final String ALIAS_FILE_SUFFIX = "文件后缀";
	public static final String ALIAS_FILE_TYPE = "文件类型";
	public static final String ALIAS_FILE_SIZE = "文件大小(单位字节)";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_ORIGINAL_FILENAME = "源文件名";
	public static final String ALIAS_DURATION_TIME = "视频/音频的时长(单位秒)";
	public static final String ALIAS_FIRST_CAPTURE_ACCESS_URL = "视频第1帧图片访问路径";
	public static final String ALIAS_FIRST_CAPTURE_SAVE_PATH = "视频第1帧图片实际路径";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 主键id       db_column: fi_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String fiId;
    /**
     * 作答id（对应t_evgl_activity_brainstorming_trainee_answer主键）       db_column: an_id 
     */	
 	//@NotNull(msg="作答id（对应t_evgl_activity_brainstorming_trainee_answer主键）不能为空")
 	@MaxLength(value=32, msg="字段[作答id（对应t_evgl_activity_brainstorming_trainee_answer主键）]超出最大长度[32]")
	private java.lang.String anId;
    /**
     * 文件路径       db_column: url 
     */	
 	//@NotNull(msg="文件路径不能为空")
 	@MaxLength(value=100, msg="字段[文件路径]超出最大长度[100]")
	private java.lang.String url;
    /**
     * 文件后缀       db_column: file_suffix 
     */	
 	//@NotNull(msg="文件后缀不能为空")
 	@MaxLength(value=20, msg="字段[文件后缀]超出最大长度[20]")
	private java.lang.String fileSuffix;
    /**
     * 文件类型       db_column: file_type 
     */	
 	//@NotNull(msg="文件类型不能为空")
 	@MaxLength(value=20, msg="字段[文件类型]超出最大长度[20]")
	private java.lang.String fileType;
    /**
     * 文件大小(单位字节)       db_column: file_size 
     */	
 	//@NotNull(msg="文件大小(单位字节)不能为空")
 	@MaxLength(value=19, msg="字段[文件大小(单位字节)]超出最大长度[19]")
	private java.lang.Long fileSize;
    /**
     * 排序号       db_column: sort_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 源文件名       db_column: original_filename 
     */	
 	//@NotNull(msg="源文件名不能为空")
 	@MaxLength(value=255, msg="字段[源文件名]超出最大长度[255]")
	private java.lang.String originalFilename;
    /**
     * 视频/音频的时长(单位秒)       db_column: duration_time 
     */	
 	//@NotNull(msg="视频/音频的时长(单位秒)不能为空")
 	@MaxLength(value=19, msg="字段[视频/音频的时长(单位秒)]超出最大长度[19]")
	private java.lang.Long durationTime;
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
     * 状态(Y有效N无效)       db_column: state 
     */	
 	//@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglActivityBrainstormingAnswerFile(){
	}

	public TevglActivityBrainstormingAnswerFile(
		java.lang.String fiId
	){
		this.fiId = fiId;
	}

	public void setFiId(java.lang.String value) {
		this.fiId = value;
	}
	public java.lang.String getFiId() {
		return this.fiId;
	}
	public void setAnId(java.lang.String value) {
		this.anId = value;
	}
	public java.lang.String getAnId() {
		return this.anId;
	}
	public void setUrl(java.lang.String value) {
		this.url = value;
	}
	public java.lang.String getUrl() {
		return this.url;
	}
	public void setFileSuffix(java.lang.String value) {
		this.fileSuffix = value;
	}
	public java.lang.String getFileSuffix() {
		return this.fileSuffix;
	}
	public void setFileType(java.lang.String value) {
		this.fileType = value;
	}
	public java.lang.String getFileType() {
		return this.fileType;
	}
	public void setFileSize(java.lang.Long value) {
		this.fileSize = value;
	}
	public java.lang.Long getFileSize() {
		return this.fileSize;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setOriginalFilename(java.lang.String value) {
		this.originalFilename = value;
	}
	public java.lang.String getOriginalFilename() {
		return this.originalFilename;
	}
	public void setDurationTime(java.lang.Long value) {
		this.durationTime = value;
	}
	public java.lang.Long getDurationTime() {
		return this.durationTime;
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
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	
	private TevglActivityBrainstormingTraineeAnswer tevglActivityBrainstormingTraineeAnswer;
	
	public void setTevglActivityBrainstormingTraineeAnswer(TevglActivityBrainstormingTraineeAnswer tevglActivityBrainstormingTraineeAnswer){
		this.tevglActivityBrainstormingTraineeAnswer = tevglActivityBrainstormingTraineeAnswer;
	}
	
	public TevglActivityBrainstormingTraineeAnswer getTevglActivityBrainstormingTraineeAnswer() {
		return tevglActivityBrainstormingTraineeAnswer;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

