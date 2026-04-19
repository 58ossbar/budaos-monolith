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


public class TorgUser extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TorgUser";
	public static final String ALIAS_ORGUSER_ID = "orguserId";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_IS_MAIN = "是否主机构（0：否，1：是）";
	

    /**
     * orguserId       db_column: orguser_id 
     */	
	private java.lang.String orguserId;
    /**
     * 机构ID       db_column: ORG_ID 
     */	
	private java.lang.String orgId;
    /**
     * 用户ID       db_column: USER_ID 
     */	
	private java.lang.String userId;
    /**
     * 是否主机构（0：否，1：是）       db_column: IS_MAIN 
     */	
	private java.lang.Integer isMain;
	//columns END

	public TorgUser(){
	}

	public TorgUser(
		java.lang.String orguserId
	){
		this.orguserId = orguserId;
	}

	public void setOrguserId(java.lang.String value) {
		this.orguserId = value;
	}
	
	public java.lang.String getOrguserId() {
		return this.orguserId;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setIsMain(java.lang.Integer value) {
		this.isMain = value;
	}
	
	public java.lang.Integer getIsMain() {
		return this.isMain;
	}
	
	private TsysOrg tsysOrg;
	
	public void setTsysOrg(TsysOrg tsysOrg){
		this.tsysOrg = tsysOrg;
	}
	
	public TsysOrg getTsysOrg() {
		return tsysOrg;
	}
	
	private TsysUserinfo tsysUserinfo;
	
	public void setTsysUserinfo(TsysUserinfo tsysUserinfo){
		this.tsysUserinfo = tsysUserinfo;
	}
	
	public TsysUserinfo getTsysUserinfo() {
		return tsysUserinfo;
	}

}

