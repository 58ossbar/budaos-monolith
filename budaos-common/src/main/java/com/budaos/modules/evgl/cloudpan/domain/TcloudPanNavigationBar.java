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

public class TcloudPanNavigationBar extends BaseDomain<TcloudPanNavigationBar>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TcloudPanNavigationBar";
	public static final String ALIAS_ID = "主键id";
	public static final String ALIAS_DIR_ID = "dirId";
	public static final String ALIAS_SORT_NUM = "sortNum";
	public static final String ALIAS_CT_ID = "ctId";
	public static final String ALIAS_PKG_ID = "pkgId";
	

    /**
     * 主键id       db_column: id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String id;
    /**
     * dirId       db_column: dir_id 
     */	
 	@NotNull(msg="dirId不能为空")
 	@MaxLength(value=32, msg="字段[dirId]超出最大长度[32]")
	private java.lang.String dirId;
    /**
     * sortNum       db_column: sort_num 
     */	
 	@NotNull(msg="sortNum不能为空")
 	@MaxLength(value=10, msg="字段[sortNum]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * ctId       db_column: ct_id 
     */	
 	@NotNull(msg="ctId不能为空")
 	@MaxLength(value=32, msg="字段[ctId]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * pkgId       db_column: pkg_id 
     */	
 	@NotNull(msg="pkgId不能为空")
 	@MaxLength(value=32, msg="字段[pkgId]超出最大长度[32]")
	private java.lang.String pkgId;
	//columns END

	public TcloudPanNavigationBar(){
	}

	public TcloudPanNavigationBar(
		java.lang.String id
	){
		this.id = id;
	}

	public void setId(java.lang.String value) {
		this.id = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setDirId(java.lang.String value) {
		this.dirId = value;
	}
	public java.lang.String getDirId() {
		return this.dirId;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setPkgId(java.lang.String value) {
		this.pkgId = value;
	}
	public java.lang.String getPkgId() {
		return this.pkgId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

