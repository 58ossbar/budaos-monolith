package com.budaos.modules.sys.domain;

//import org.hibernate.validator.constraints.NotBlank;
//import com.budaos.common.cbsecurity.validator.group.AddGroup;
//import com.budaos.common.cbsecurity.validator.group.UpdateGroup;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TsysAttach extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;

	// alias
	public static final String TABLE_ALIAS = "TsysAttach";
	public static final String ALIAS_ATTACH_ID = "attachId";
	public static final String ALIAS_UPLOAD_MAN = "上传人";
	public static final String ALIAS_UPLOAD_TIME = "上传时间";
	public static final String ALIAS_FILE_URL = "文件路径";
	public static final String ALIAS_FILE_NAME = "文件名称";
	public static final String ALIAS_REMARK = "文件备注";
	public static final String ALIAS_SECOND_NAME = "文件别名";
	public static final String ALIAS_FILE_TYPE = "附件分类";
	public static final String ALIAS_PKID = "关联ID";
	public static final String ALIAS_FILE_SIZE = "文件大小";
	public static final String ALIAS_FILE_SUFFIX = "文件后缀";
	public static final String ALIAS_LJ_URL = "链接地址";
	public static final String ALIAS_FILE_NO = "文件编号";
	public static final String ALIAS_FILE_STATE = "文件状态";
	public static final String ALIAS_FILE_ORERNO = "文件排序号";

	/**
	 * attachId db_column: ATTACH_ID
	 */
	// @NotBlank(message="attachId不能为空", groups = {AddGroup.class,
	// UpdateGroup.class})
	private java.lang.String attachId;

	/**
	 * 文件路径 db_column: FILE_URL
	 */
	// @NotBlank(message="文件路径不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String fileUrl;
	/**
	 * 文件名称 db_column: FILE_NAME
	 */
	// @NotBlank(message="文件名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String fileName;
	/**
	 * 文件备注 db_column: REMARK
	 */
	// @NotBlank(message="文件备注不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String remark;
	/**
	 * 文件别名 db_column: SECOND_NAME
	 */
	// @NotBlank(message="文件别名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String secondName;
	/**
	 * 附件分类 db_column: FILE_TYPE
	 */
	// @NotBlank(message="附件分类不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String fileType;
	/**
	 * 关联ID db_column: PKID
	 */
	// @NotBlank(message="关联ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String pkid;
	/**
	 * 文件大小 db_column: FILE_SIZE
	 */
	// @NotBlank(message="文件大小不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String fileSize;
	/**
	 * 文件后缀 db_column: FILE_SUFFIX
	 */
	// @NotBlank(message="文件后缀不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String fileSuffix;
	/**
	 * 链接地址 db_column: LJ_URL
	 */
	// @NotBlank(message="链接地址不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String ljUrl;
	/**
	 * 文件编号 db_column: FILE_NO
	 */
	// @NotBlank(message="文件编号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String fileNo;
	/**
	 * 文件状态 db_column: FILE_STATE
	 */
	// @NotBlank(message="文件状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.String fileState;
	/**
	 * 文件排序号 db_column: FILE_ORERNO
	 */
	// @NotBlank(message="文件排序号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private java.lang.Integer fileOrerno;
	// columns END

	public TsysAttach() {
	}

	public TsysAttach(java.lang.String attachId) {
		this.attachId = attachId;
	}

	public void setAttachId(java.lang.String value) {
		this.attachId = value;
	}

	public java.lang.String getAttachId() {
		return this.attachId;
	}

	public void setFileUrl(java.lang.String value) {
		this.fileUrl = value;
	}

	public java.lang.String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileName(java.lang.String value) {
		this.fileName = value;
	}

	public java.lang.String getFileName() {
		return this.fileName;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.lang.String getRemark() {
		return this.remark;
	}

	public void setSecondName(java.lang.String value) {
		this.secondName = value;
	}

	public java.lang.String getSecondName() {
		return this.secondName;
	}

	public void setFileType(java.lang.String value) {
		this.fileType = value;
	}

	public java.lang.String getFileType() {
		return this.fileType;
	}

	public void setPkid(java.lang.String value) {
		this.pkid = value;
	}

	public java.lang.String getPkid() {
		return this.pkid;
	}

	public void setFileSize(java.lang.String value) {
		this.fileSize = value;
	}

	public java.lang.String getFileSize() {
		return this.fileSize;
	}

	public void setFileSuffix(java.lang.String value) {
		this.fileSuffix = value;
	}

	public java.lang.String getFileSuffix() {
		return this.fileSuffix;
	}

	public void setLjUrl(java.lang.String value) {
		this.ljUrl = value;
	}

	public java.lang.String getLjUrl() {
		return this.ljUrl;
	}

	public void setFileNo(java.lang.String value) {
		this.fileNo = value;
	}

	public java.lang.String getFileNo() {
		return this.fileNo;
	}

	public void setFileState(java.lang.String value) {
		this.fileState = value;
	}

	public java.lang.String getFileState() {
		return this.fileState;
	}

	public void setFileOrerno(java.lang.Integer value) {
		this.fileOrerno = value;
	}

	public java.lang.Integer getFileOrerno() {
		return this.fileOrerno;
	}

}
