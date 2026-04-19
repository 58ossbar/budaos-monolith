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

public class TcloudPanPermissions extends BaseDomain<TcloudPanPermissions>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TcloudPanPermissions";
	public static final String ALIAS_PER_ID = "perId";
	public static final String ALIAS_OWNER = "owner";
	public static final String ALIAS_PKG_ID = "pkgId";
	public static final String ALIAS_DIR_ID = "dirId";
	public static final String ALIAS_FILE_ID = "fileId";
	public static final String ALIAS_IS_PERMISSION = "isPermission";
	

    /**
     * perId       db_column: per_id 
     */	
 	@NotNull(msg="perId不能为空")
 	@MaxLength(value=32, msg="字段[perId]超出最大长度[32]")
	private java.lang.String perId;
    /**
     * owner       db_column: owner 
     */	
 	@NotNull(msg="owner不能为空")
 	@MaxLength(value=32, msg="字段[owner]超出最大长度[32]")
	private java.lang.String owner;
    /**
     * pkgId       db_column: pkg_id 
     */	
 	@NotNull(msg="pkgId不能为空")
 	@MaxLength(value=32, msg="字段[pkgId]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * dirId       db_column: dir_id 
     */	
 	@NotNull(msg="dirId不能为空")
 	@MaxLength(value=32, msg="字段[dirId]超出最大长度[32]")
	private java.lang.String dirId;
    /**
     * fileId       db_column: file_id 
     */	
 	@NotNull(msg="fileId不能为空")
 	@MaxLength(value=32, msg="字段[fileId]超出最大长度[32]")
	private java.lang.String fileId;
    /**
     * isPermission       db_column: is_permission 
     */	
 	@NotNull(msg="isPermission不能为空")
 	@MaxLength(value=32, msg="字段[isPermission]超出最大长度[32]")
	private java.lang.String isPermission;
 	/**
     * refPkgId       db_column: ref_pkg_id 
     */	
 	@NotNull(msg="refPkgId不能为空")
 	@MaxLength(value=32, msg="字段[refPkgId]超出最大长度[32]")
 	private java.lang.String currentPkgId;
	//columns END

	public TcloudPanPermissions(){
	}

	public TcloudPanPermissions(
		java.lang.String perId
	){
		this.perId = perId;
	}

	public void setPerId(java.lang.String value) {
		this.perId = value;
	}
	public java.lang.String getPerId() {
		return this.perId;
	}
	public void setOwner(java.lang.String value) {
		this.owner = value;
	}
	public java.lang.String getOwner() {
		return this.owner;
	}
	public void setPkgId(java.lang.String value) {
		this.pkgId = value;
	}
	public java.lang.String getPkgId() {
		return this.pkgId;
	}
	public void setDirId(java.lang.String value) {
		this.dirId = value;
	}
	public java.lang.String getDirId() {
		return this.dirId;
	}
	public void setFileId(java.lang.String value) {
		this.fileId = value;
	}
	public java.lang.String getFileId() {
		return this.fileId;
	}
	public void setIsPermission(java.lang.String value) {
		this.isPermission = value;
	}
	public java.lang.String getIsPermission() {
		return this.isPermission;
	}
	public java.lang.String getCurrentPkgId() {
		return currentPkgId;
	}
	public void setCurrentPkgId(java.lang.String currentPkgId) {
		this.currentPkgId = currentPkgId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

