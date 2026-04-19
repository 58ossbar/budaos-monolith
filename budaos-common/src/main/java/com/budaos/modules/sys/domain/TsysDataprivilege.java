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


public class TsysDataprivilege extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TsysDataprivilege";
	public static final String ALIAS_ROLE_ORGID = "roleOrgid";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_ROLE_ID = "角色ID";
	

    /**
     * roleOrgid       db_column: ROLE_ORGID 
     */	
	private java.lang.String roleOrgid;
    /**
     * 机构ID       db_column: ORG_ID 
     */	
	private java.lang.String orgId;
    /**
     * 角色ID       db_column: ROLE_ID 
     */	
	private java.lang.String roleId;
	//columns END

	public TsysDataprivilege(){
	}

	public TsysDataprivilege(
		java.lang.String roleOrgid
	){
		this.roleOrgid = roleOrgid;
	}

	public void setRoleOrgid(java.lang.String value) {
		this.roleOrgid = value;
	}
	
	public java.lang.String getRoleOrgid() {
		return this.roleOrgid;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setRoleId(java.lang.String value) {
		this.roleId = value;
	}
	
	public java.lang.String getRoleId() {
		return this.roleId;
	}
	
	private TsysRole tsysRole;
	
	public void setTsysRole(TsysRole tsysRole){
		this.tsysRole = tsysRole;
	}
	
	public TsysRole getTsysRole() {
		return tsysRole;
	}
	
	private TsysOrg tsysOrg;
	
	public void setTsysOrg(TsysOrg tsysOrg){
		this.tsysOrg = tsysOrg;
	}
	
	public TsysOrg getTsysOrg() {
		return tsysOrg;
	}

}

