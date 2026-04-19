package com.budaos.modules.evgl.cloudpan.domain;

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

public class TcloudPanFile extends BaseDomain<TcloudPanFile>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TcloudPanFile";
	public static final String ALIAS_FILE_ID = "主键id";
	public static final String ALIAS_DIR_ID = "所属目录id";
	public static final String ALIAS_NAME = "文件名称";
	public static final String ALIAS_PATH = "实际路径";
	public static final String ALIAS_FILE_ACCESS_URL = "文件显示地址";
	public static final String ALIAS_FILE_SAVE_PATH = "文件保存地址";
	public static final String ALIAS_FILE_SUFFIX = "文件后缀";
	public static final String ALIAS_FILE_SIZE = "文件大小(单位KB)";
	public static final String ALIAS_FILE_TYPE = "文件类型(image图片video视频audio音频docs文档other其它)";
	public static final String ALIAS_ORIGINAL_FILENAME = "文件源名称";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 主键id       db_column: file_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String fileId;
    /**
     * 所属目录id       db_column: dir_id 
     */	
 	@NotNull(msg="所属目录id不能为空")
 	@MaxLength(value=32, msg="字段[所属目录id]超出最大长度[32]")
	private java.lang.String dirId;
    /**
     * 文件名称       db_column: name 
     */	
 	@NotNull(msg="文件名称不能为空")
 	@MaxLength(value=200, msg="字段[文件名称]超出最大长度[200]")
	private java.lang.String name;
    /**
     * 实际路径       db_column: path 
     */	
 	@NotNull(msg="实际路径不能为空")
 	@MaxLength(value=100, msg="字段[实际路径]超出最大长度[100]")
	private java.lang.String path;
    /**
     * 文件显示地址       db_column: file_access_url 
     */	
 	@NotNull(msg="文件显示地址不能为空")
 	@MaxLength(value=100, msg="字段[文件显示地址]超出最大长度[100]")
	private java.lang.String fileAccessUrl;
    /**
     * 文件保存地址       db_column: file_save_path 
     */	
 	@NotNull(msg="文件保存地址不能为空")
 	@MaxLength(value=100, msg="字段[文件保存地址]超出最大长度[100]")
	private java.lang.String fileSavePath;
    /**
     * 文件后缀       db_column: file_suffix 
     */	
 	@NotNull(msg="文件后缀不能为空")
 	@MaxLength(value=20, msg="字段[文件后缀]超出最大长度[20]")
	private java.lang.String fileSuffix;
    /**
     * 文件大小(单位KB)       db_column: file_size 
     */	
 	@NotNull(msg="文件大小(单位KB)不能为空")
 	@MaxLength(value=19, msg="字段[文件大小(单位KB)]超出最大长度[19]")
	private java.lang.Long fileSize;
    /**
     * 文件类型(image图片video视频audio音频docs文档other其它)       db_column: file_type 
     */	
 	@NotNull(msg="文件类型(image图片video视频audio音频docs文档other其它)不能为空")
 	@MaxLength(value=8, msg="字段[文件类型(image图片video视频audio音频docs文档other其它)]超出最大长度[8]")
	private java.lang.String fileType;
    /**
     * 文件源名称       db_column: original_filename 
     */	
 	@NotNull(msg="文件源名称不能为空")
 	@MaxLength(value=100, msg="字段[文件源名称]超出最大长度[100]")
	private java.lang.String originalFilename;
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TcloudPanFile(){
	}

	public TcloudPanFile(
		java.lang.String fileId
	){
		this.fileId = fileId;
	}

	public void setFileId(java.lang.String value) {
		this.fileId = value;
	}
	public java.lang.String getFileId() {
		return this.fileId;
	}
	public void setDirId(java.lang.String value) {
		this.dirId = value;
	}
	public java.lang.String getDirId() {
		return this.dirId;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setPath(java.lang.String value) {
		this.path = value;
	}
	public java.lang.String getPath() {
		return this.path;
	}
	public void setFileAccessUrl(java.lang.String value) {
		this.fileAccessUrl = value;
	}
	public java.lang.String getFileAccessUrl() {
		return this.fileAccessUrl;
	}
	public void setFileSavePath(java.lang.String value) {
		this.fileSavePath = value;
	}
	public java.lang.String getFileSavePath() {
		return this.fileSavePath;
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
	public void setFileType(java.lang.String value) {
		this.fileType = value;
	}
	public java.lang.String getFileType() {
		return this.fileType;
	}
	public void setOriginalFilename(java.lang.String value) {
		this.originalFilename = value;
	}
	public java.lang.String getOriginalFilename() {
		return this.originalFilename;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	
	private TcloudPanDirectory tcloudPanDirectory;
	
	public void setTcloudPanDirectory(TcloudPanDirectory tcloudPanDirectory){
		this.tcloudPanDirectory = tcloudPanDirectory;
	}
	
	public TcloudPanDirectory getTcloudPanDirectory() {
		return tcloudPanDirectory;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

