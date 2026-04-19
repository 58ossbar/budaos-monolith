package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

//import org.hibernate.validator.constraints.NotBlank;
//import com.budaos.common.cbsecurity.validator.group.AddGroup;
//import com.budaos.common.cbsecurity.validator.group.UpdateGroup;
//import com.budaos.core.baseclass.domain.BaseDomain;

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

public class TsysDict extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;

	// alias
	public static final String TABLE_ALIAS = "TsysDict";
	public static final String ALIAS_DICT_ID = "dictId";
	public static final String ALIAS_DICT_TYPE = "字典分类";
	public static final String ALIAS_DICT_NAME = "字典分类名称";
	public static final String ALIAS_DICT_VALUE = "字典值";
	public static final String ALIAS_DICT_CODE = "字典编码";
	public static final String ALIAS_REMARK = "字典描述";
	public static final String ALIAS_DICT_SORT = "1、平台内2、平台外";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_PARENT_TYPE = "父分类";
	public static final String ALIAS_DISPLAY_SORT = "字典展现分类:下拉类型(select)树形(tree) 复选型(checkbox)单选radio";
	public static final String ALIAS_DICT_CLASS = "dictClass";
	public static final String ALIAS_MULTI_TYPE = "单选或多选：主要针对树形控件";
	public static final String ALIAS_ORG_ID = "所属机构";
	public static final String ALIAS_DISPLAYING = "是否显示";

	/**
	 * dictId db_column: DICT_ID
	 */
	private java.lang.String dictId;
	// @NotBlank(message = "字典分类不能为空", groups = { AddGroup.class, UpdateGroup.class
	// })
	private java.lang.String dictType;
	private String dictUrl;

	public String getDictUrl() {
		return dictUrl;
	}

	public void setDictUrl(String dictUrl) {
		this.dictUrl = dictUrl;
	}

	/**
	 * 字典分类名称 db_column: DICT_NAME
	 */
	// @NotBlank(message = "字典分类名称不能为空", groups = { AddGroup.class,
	// UpdateGroup.class })
	private java.lang.String dictName;
	/**
	 * 字典值 db_column: DICT_VALUE
	 */
	// @NotBlank(message = "字典值不能为空", groups = { AddGroup.class, UpdateGroup.class
	// })
	private java.lang.String dictValue;
	/**
	 * 字典编码 db_column: DICT_CODE
	 */
	// @NotBlank(message = "字典编码不能为空", groups = { AddGroup.class, UpdateGroup.class
	// })
	private java.lang.String dictCode;
	/**
	 * 字典描述 db_column: REMARK
	 */
	private java.lang.String remark;
	/**
	 * 1、平台内2、平台外 db_column: DICT_SORT
	 */
	private java.lang.String dictSort;
	/**
	 * 排序号 db_column: SORT_NUM
	 */
	private java.lang.Integer sortNum;
	/**
	 * 父分类 db_column: PARENT_TYPE
	 */
	private java.lang.String parentType;
	/**
	 * 字典展现分类:下拉类型(select)树形(tree) 复选型(checkbox)单选radio db_column: DISPLAY_SORT
	 */
	private java.lang.String displaySort;
	/**
	 * dictClass db_column: DICT_CLASS
	 */
	private java.lang.Integer dictClass;
	/**
	 * 单选或多选：主要针对树形控件 db_column: MULTI_TYPE
	 */
	private java.lang.String multiType;
	/**
	 * 所属机构 db_column: ORG_ID
	 */
	private java.lang.String orgId;
	/**
	 * 是否显示 db_column: displaying
	 */
	private java.lang.String displaying;

	/**
	 * 是否默认值 db_column: isdefault
	 */
	private java.lang.String isdefault;
	// columns END
	private String orgName;
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public java.lang.String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(java.lang.String isdefault) {
		this.isdefault = isdefault;
	}

	public TsysDict() {
	}

	public TsysDict(java.lang.String dictId) {
		this.dictId = dictId;
	}

	public void setDictId(java.lang.String value) {
		this.dictId = value;
	}

	public java.lang.String getDictId() {
		return this.dictId;
	}

	public void setDictType(java.lang.String value) {
		this.dictType = value;
	}

	public java.lang.String getDictType() {
		return this.dictType;
	}

	public void setDictName(java.lang.String value) {
		this.dictName = value;
	}

	public java.lang.String getDictName() {
		return this.dictName;
	}

	public void setDictValue(java.lang.String value) {
		this.dictValue = value;
	}

	public java.lang.String getDictValue() {
		return this.dictValue;
	}

	public void setDictCode(java.lang.String value) {
		this.dictCode = value;
	}

	public java.lang.String getDictCode() {
		return this.dictCode;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.lang.String getRemark() {
		return this.remark;
	}

	public void setDictSort(java.lang.String value) {
		this.dictSort = value;
	}

	public java.lang.String getDictSort() {
		return this.dictSort;
	}

	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}

	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}

	public void setParentType(java.lang.String value) {
		this.parentType = value;
	}

	public java.lang.String getParentType() {
		return this.parentType;
	}

	public void setDisplaySort(java.lang.String value) {
		this.displaySort = value;
	}

	public java.lang.String getDisplaySort() {
		return this.displaySort;
	}

	public void setDictClass(java.lang.Integer value) {
		this.dictClass = value;
	}

	public java.lang.Integer getDictClass() {
		return this.dictClass;
	}

	public void setMultiType(java.lang.String value) {
		this.multiType = value;
	}

	public java.lang.String getMultiType() {
		return this.multiType;
	}

	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}

	public java.lang.String getOrgId() {
		return this.orgId;
	}

	public void setDisplaying(java.lang.String value) {
		this.displaying = value;
	}

	public java.lang.String getDisplaying() {
		return this.displaying;
	}

	public TsysDict clone() {
		TsysDict dict = new TsysDict();

		dict.setCreateTime(this.getCreateTime());
		dict.setCreateUserId(this.getCreateUserId());
		dict.setDelFlag(this.getDelFlag());
		dict.setDictClass(this.getDictClass());
		dict.setDictCode(this.getDictCode());
		dict.setDictId(this.getDictId());
		dict.setDictName(this.getDictName());
		dict.setDictSort(this.getDictSort());
		dict.setDictType(this.getDictType());
		dict.setDictValue(this.getDictValue());
		dict.setDisplaying(this.getDisplaying());
		dict.setDisplaySort(this.getDisplaySort());
		dict.setIsdefault(this.getIsdefault());
		dict.setMultiType(this.getMultiType());
		dict.setOrgId(this.getOrgId());
		dict.setParentType(this.getParentType());
		dict.setRemark(this.getRemark());
		dict.setSortNum(this.getSortNum());
		dict.setSqlMap(this.getSqlMap());
		dict.setUpdateTime(this.getUpdateTime());
		dict.setUpdateUserId(this.getUpdateUserId());
		dict.setDictUrl(this.getDictUrl());
		return dict;
	}

}
