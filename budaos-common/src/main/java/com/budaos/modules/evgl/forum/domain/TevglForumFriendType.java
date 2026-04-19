package com.budaos.modules.evgl.forum.domain;

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

public class TevglForumFriendType extends BaseDomain<TevglForumFriendType>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglForumFriendType";
	public static final String ALIAS_TYPE_ID = "主键";
	public static final String ALIAS_PARENT_ID = "parentId";
	public static final String ALIAS_NAME = "名称";
	public static final String ALIAS_ICON = "图标";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_REMARK = "备注";
	

    /**
     * 主键       db_column: type_id 
     */	
 	@NotNull(msg="主键不能为空")
 	@MaxLength(value=32, msg="字段[主键]超出最大长度[32]")
	private java.lang.String typeId;
    /**
     * parentId       db_column: parent_id 
     */	
 	@NotNull(msg="parentId不能为空")
 	@MaxLength(value=32, msg="字段[parentId]超出最大长度[32]")
	private java.lang.String parentId;
    /**
     * 名称       db_column: name 
     */	
 	@NotNull(msg="名称不能为空")
 	@MaxLength(value=50, msg="字段[名称]超出最大长度[50]")
	private java.lang.String name;
    /**
     * 图标       db_column: icon 
     */	
 	//@NotNull(msg="图标不能为空")
 	@MaxLength(value=100, msg="字段[图标]超出最大长度[100]")
	private java.lang.String icon;
    /**
     * 排序号       db_column: sort_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 备注       db_column: remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=500, msg="字段[备注]超出最大长度[500]")
	private java.lang.String remark;
	//columns END

	public TevglForumFriendType(){
	}

	public TevglForumFriendType(
		java.lang.String typeId
	){
		this.typeId = typeId;
	}

	public void setTypeId(java.lang.String value) {
		this.typeId = value;
	}
	public java.lang.String getTypeId() {
		return this.typeId;
	}
	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}
	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setIcon(java.lang.String value) {
		this.icon = value;
	}
	public java.lang.String getIcon() {
		return this.icon;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

