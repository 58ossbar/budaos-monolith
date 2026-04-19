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


public class TuserGroup extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TuserGroup";
	public static final String ALIAS_USERGROUP_ID = "usergroupId";
	public static final String ALIAS_GROUP_ID = "组ID";
	public static final String ALIAS_USER_ID = "用户ID";
	

    /**
     * usergroupId       db_column: USERGROUP_ID 
     */	
	private java.lang.String usergroupId;
    /**
     * 组ID       db_column: GROUP_ID 
     */	
	private java.lang.String groupId;
    /**
     * 用户ID       db_column: USER_ID 
     */	
	private java.lang.String userId;
	//columns END

	public TuserGroup(){
	}

	public TuserGroup(
		java.lang.String usergroupId
	){
		this.usergroupId = usergroupId;
	}

	public void setUsergroupId(java.lang.String value) {
		this.usergroupId = value;
	}
	
	public java.lang.String getUsergroupId() {
		return this.usergroupId;
	}
	public void setGroupId(java.lang.String value) {
		this.groupId = value;
	}
	
	public java.lang.String getGroupId() {
		return this.groupId;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
	public java.lang.String getUserId() {
		return this.userId;
	}
	
	private TsysUserinfo tsysUserinfo;
	
	public void setTsysUserinfo(TsysUserinfo tsysUserinfo){
		this.tsysUserinfo = tsysUserinfo;
	}
	
	public TsysUserinfo getTsysUserinfo() {
		return tsysUserinfo;
	}
	
	private TsysGroup tsysGroup;
	
	public void setTsysGroup(TsysGroup tsysGroup){
		this.tsysGroup = tsysGroup;
	}
	
	public TsysGroup getTsysGroup() {
		return tsysGroup;
	}

}

