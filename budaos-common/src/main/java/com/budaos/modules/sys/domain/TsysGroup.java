/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

package com.budaos.modules.sys.domain;
import com.budaos.core.baseclass.domain.BaseDomain;


public class TsysGroup extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TsysGroup";
	public static final String ALIAS_GROUP_ID = "组ID";
	public static final String ALIAS_GROUP_NAME = "组名称";
	public static final String ALIAS_REMARK = "组描述";
	public static final String ALIAS_GROUP_TYPE = "组类别";
	public static final String ALIAS_PARENT_GROUPID = "父组ID ";
	

    /**
     * 组ID       db_column: GROUP_ID 
     */	
	private java.lang.String groupId;
    /**
     * 组名称       db_column: GROUP_NAME 
     */	
	private java.lang.String groupName;
    /**
     * 组描述       db_column: REMARK 
     */	
	private java.lang.String remark;
    /**
     * 组类别
                   db_column: GROUP_TYPE 
     */	
	private java.lang.String groupType;
    /**
     * 父组ID        db_column: PARENT_GROUPID 
     */	
	private java.lang.String parentGroupid;
	//columns END

	public TsysGroup(){
	}

	public TsysGroup(
		java.lang.String groupId
	){
		this.groupId = groupId;
	}

	public void setGroupId(java.lang.String value) {
		this.groupId = value;
	}
	
	public java.lang.String getGroupId() {
		return this.groupId;
	}
	public void setGroupName(java.lang.String value) {
		this.groupName = value;
	}
	
	public java.lang.String getGroupName() {
		return this.groupName;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setGroupType(java.lang.String value) {
		this.groupType = value;
	}
	
	public java.lang.String getGroupType() {
		return this.groupType;
	}
	public void setParentGroupid(java.lang.String value) {
		this.parentGroupid = value;
	}
	
	public java.lang.String getParentGroupid() {
		return this.parentGroupid;
	}
}

