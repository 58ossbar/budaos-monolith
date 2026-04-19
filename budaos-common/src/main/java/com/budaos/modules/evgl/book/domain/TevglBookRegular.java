package com.budaos.modules.evgl.book.domain;

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

public class TevglBookRegular extends BaseDomain<TevglBookRegular>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglBookRegular";
	public static final String ALIAS_REGULAR_ID = "主键id";
	public static final String ALIAS_SUBJECT_ID = "课程id";
	public static final String ALIAS_PARENT_ID = "父级id";
	public static final String ALIAS_REGULAR_NAME = "考核规则名称";
	public static final String ALIAS_REGULAR_SUM = "考核规则分";
	public static final String ALIAS_REGULAR_SORT = "排序号";
	public static final String ALIAS_REGULAR_TYPE = "考核规则分类";
	public static final String ALIAS_REGULAR_DESC = "规则说明";
	

    /**
     * 主键id       db_column: regular_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String regularId;
    /**
     * 课程id       db_column: subject_id 
     */	
 	@NotNull(msg="课程id不能为空")
 	@MaxLength(value=32, msg="字段[课程id]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 父级id       db_column: parent_id 
     */	
 	@NotNull(msg="父级id不能为空")
 	@MaxLength(value=32, msg="字段[父级id]超出最大长度[32]")
	private java.lang.String parentId;
    /**
     * 考核规则名称       db_column: regular_name 
     */	
 	@NotNull(msg="考核规则名称不能为空")
 	@MaxLength(value=32, msg="字段[考核规则名称]超出最大长度[32]")
	private java.lang.String regularName;
    /**
     * 考核规则分       db_column: regular_sum 
     */	
 	//@NotNull(msg="考核规则分不能为空")
 	@MaxLength(value=5, msg="字段[考核规则分]超出最大长度[5]")
	private java.math.BigDecimal regularSum;
    /**
     * 排序号       db_column: regular_sort 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer regularSort;
    /**
     * 考核规则分类       db_column: regular_type 
     */	
 	//@NotNull(msg="考核规则分类不能为空")
 	@MaxLength(value=2, msg="字段[考核规则分类]超出最大长度[2]")
	private java.lang.String regularType;
    /**
     * 规则说明       db_column: regular_desc 
     */	
 	//@NotNull(msg="规则说明不能为空")
 	@MaxLength(value=65535, msg="字段[规则说明]超出最大长度[65535]")
	private java.lang.String regularDesc;
	//columns END

	public TevglBookRegular(){
	}

	public TevglBookRegular(
		java.lang.String regularId
	){
		this.regularId = regularId;
	}

	public void setRegularId(java.lang.String value) {
		this.regularId = value;
	}
	public java.lang.String getRegularId() {
		return this.regularId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}
	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setRegularName(java.lang.String value) {
		this.regularName = value;
	}
	public java.lang.String getRegularName() {
		return this.regularName;
	}
	public void setRegularSum(java.math.BigDecimal value) {
		this.regularSum = value;
	}
	public java.math.BigDecimal getRegularSum() {
		return this.regularSum;
	}
	public void setRegularSort(java.lang.Integer value) {
		this.regularSort = value;
	}
	public java.lang.Integer getRegularSort() {
		return this.regularSort;
	}
	public void setRegularType(java.lang.String value) {
		this.regularType = value;
	}
	public java.lang.String getRegularType() {
		return this.regularType;
	}
	public void setRegularDesc(java.lang.String value) {
		this.regularDesc = value;
	}
	public java.lang.String getRegularDesc() {
		return this.regularDesc;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

