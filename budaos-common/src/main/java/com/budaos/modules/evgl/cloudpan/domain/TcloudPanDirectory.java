package com.budaos.modules.evgl.cloudpan.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TcloudPanDirectory extends BaseDomain<TcloudPanDirectory>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TcloudPanDirectory";
	public static final String ALIAS_DIR_ID = "主键目录id";
	public static final String ALIAS_REF_DIR_ID = "来源目录id";
	public static final String ALIAS_NAME = "目录名称";
	public static final String ALIAS_PARENT_ID = "父级目录id";
	public static final String ALIAS_INTR = "简介";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_DICT_CODE = "字典（标识为默认目录）";
	

    /**
     * 主键目录id       db_column: dir_id 
     */	
 	@NotNull(msg="主键目录id不能为空")
 	@MaxLength(value=32, msg="字段[主键目录id]超出最大长度[32]")
	private java.lang.String dirId;
	 /**
	 * 来源目录id       db_column: ref_dir_id 
	 */	
 	@NotNull(msg="来源目录id不能为空")
 	@MaxLength(value=2, msg="字段[来源目录id]超出最大长度[2]")
	private java.lang.String refDirId;
 	/**
     * 教学包       db_column: pkg_id 
     */	
 	@NotNull(msg="教学包不能为空")
 	@MaxLength(value=32, msg="字段[教学包id]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * 目录名称       db_column: name 
     */	
 	@NotNull(msg="目录名称不能为空")
 	@MaxLength(value=20, msg="字段[目录名称]超出最大长度[20]")
	private java.lang.String name;
    /**
     * 父级目录id       db_column: parent_id 
     */	
 	@NotNull(msg="父级目录id不能为空")
 	@MaxLength(value=32, msg="字段[父级目录id]超出最大长度[32]")
	private java.lang.String parentId;
    /**
     * 简介       db_column: intr 
     */	
 	@NotNull(msg="简介不能为空")
 	@MaxLength(value=200, msg="字段[简介]超出最大长度[200]")
	private java.lang.String intr;
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
 	/**
     * 字典（标识为默认目录）       db_column: dict_code 
     */	
 	//@NotNull(msg="字典（标识为默认目录）不能为空")
 	@MaxLength(value=2, msg="字段[字典（标识为默认目录）]超出最大长度[2]")
	private java.lang.String dictCode;
 	
	private java.lang.String icon;
	
	@MaxLength(value=2, msg="字段[学员是否可见(Y可见N不可见)]超出最大长度[2]")
	private java.lang.String isTraineeVisible;
	//columns END

 	private List<Map<String, Object>> children;
 	
	public TcloudPanDirectory(){
	}

	public TcloudPanDirectory(
		java.lang.String dirId
	){
		this.dirId = dirId;
	}

	public void setDirId(java.lang.String value) {
		this.dirId = value;
	}
	public java.lang.String getDirId() {
		return this.dirId;
	}
	public java.lang.String getRefDirId() {
		return refDirId;
	}
	public void setRefDirId(java.lang.String refDirId) {
		this.refDirId = refDirId;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public java.lang.String getPkgId() {
		return pkgId;
	}
	public void setPkgId(java.lang.String pkgId) {
		this.pkgId = pkgId;
	}
	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}
	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setIntr(java.lang.String value) {
		this.intr = value;
	}
	public java.lang.String getIntr() {
		return this.intr;
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
	public void setDictCode(java.lang.String value) {
		this.dictCode = value;
	}
	public java.lang.String getDictCode() {
		return this.dictCode;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public List<Map<String, Object>> getChildren() {
		return children;
	}

	public void setChildren(List<Map<String, Object>> children) {
		this.children = children;
	}

	public java.lang.String getIcon() {
		return icon;
	}

	public void setIcon(java.lang.String icon) {
		this.icon = icon;
	}

	public java.lang.String getIsTraineeVisible() {
		return isTraineeVisible;
	}

	public void setIsTraineeVisible(java.lang.String isTraineeVisible) {
		this.isTraineeVisible = isTraineeVisible;
	}
	
}

