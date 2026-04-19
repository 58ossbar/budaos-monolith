package com.budaos.modules.evgl.tch.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 课堂小组</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglTchClassroomGroup extends BaseDomain<TevglTchClassroomGroup>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchClassroomGroup";
	public static final String ALIAS_GP_ID = "主键小组ID";
	public static final String ALIAS_CT_ID = "课堂ID";
	public static final String ALIAS_GROUP_NAME = "小组名称";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 主键小组ID       db_column: gp_id 
     */	
 	@NotNull(msg="主键小组ID不能为空")
 	@MaxLength(value=32, msg="字段[主键小组ID]超出最大长度[32]")
	private java.lang.String gpId;
    /**
     * 课堂ID       db_column: ct_id 
     */	
 	@NotNull(msg="课堂ID不能为空")
 	@MaxLength(value=32, msg="字段[课堂ID]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 小组名称       db_column: group_name 
     */	
 	@NotNull(msg="小组名称不能为空")
 	@MaxLength(value=20, msg="字段[小组名称]超出最大长度[20]")
	private java.lang.String groupName;
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
     * 当前课堂小组的人数       db_column: number 
     */	
 	@NotNull(msg="当前课堂小组的人数不能为空")
 	@MaxLength(value=10, msg="字段[当前课堂小组的人数]超出最大长度[10]")
	private java.lang.Integer number;
	//columns END

	public TevglTchClassroomGroup(){
	}

	public TevglTchClassroomGroup(
		java.lang.String gpId
	){
		this.gpId = gpId;
	}

	public void setGpId(java.lang.String value) {
		this.gpId = value;
	}
	public java.lang.String getGpId() {
		return this.gpId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setGroupName(java.lang.String value) {
		this.groupName = value;
	}
	public java.lang.String getGroupName() {
		return this.groupName;
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
	public java.lang.Integer getNumber() {
		return number;
	}
	public void setNumber(java.lang.Integer number) {
		this.number = number;
	}

	private TevglTchClassroom tevglTchClassroom;
	
	public void setTevglTchClassroom(TevglTchClassroom tevglTchClassroom){
		this.tevglTchClassroom = tevglTchClassroom;
	}
	
	public TevglTchClassroom getTevglTchClassroom() {
		return tevglTchClassroom;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

