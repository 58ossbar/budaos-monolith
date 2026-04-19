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


public class TrolePost extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TrolePost";
	public static final String ALIAS_ROLE_POSTID = "rolePostid";
	public static final String ALIAS_POST_ID = "岗位ID";
	public static final String ALIAS_ROLE_ID = "角色ID";
	

    /**
     * rolePostid       db_column: ROLE_POSTID 
     */	
	private java.lang.String rolePostid;
    /**
     * 岗位ID       db_column: POST_ID 
     */	
	private java.lang.String postId;
    /**
     * 角色ID       db_column: ROLE_ID 
     */	
	private java.lang.String roleId;
	//columns END

	public TrolePost(){
	}

	public TrolePost(
		java.lang.String rolePostid
	){
		this.rolePostid = rolePostid;
	}

	public void setRolePostid(java.lang.String value) {
		this.rolePostid = value;
	}
	
	public java.lang.String getRolePostid() {
		return this.rolePostid;
	}
	public void setPostId(java.lang.String value) {
		this.postId = value;
	}
	
	public java.lang.String getPostId() {
		return this.postId;
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
	
	private TsysPost tsysPost;
	
	public void setTsysPost(TsysPost tsysPost){
		this.tsysPost = tsysPost;
	}
	
	public TsysPost getTsysPost() {
		return tsysPost;
	}

}

