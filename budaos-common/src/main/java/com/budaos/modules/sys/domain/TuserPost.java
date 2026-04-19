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


public class TuserPost extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TuserPost";
	public static final String ALIAS_USER_JOBID = "userJobid";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_POST_ID = "岗位ID";
	public static final String ALIAS_IS_MAIN = "是否主岗位（0：否，1：是）";
	

    /**
     * userJobid       db_column: USER_JOBID 
     */	
	private java.lang.String userJobid;
    /**
     * 用户ID       db_column: USER_ID 
     */	
	private java.lang.String userId;
    /**
     * 岗位ID       db_column: POST_ID 
     */	
	private java.lang.String postId;
	private java.lang.String postName;
    /**
     * 是否主岗位（0：否，1：是）       db_column: IS_MAIN 
     */	
	private java.lang.String isMain;
	//columns END

	public TuserPost(){
	}

	public TuserPost(
		java.lang.String userJobid
	){
		this.userJobid = userJobid;
	}

	public void setUserJobid(java.lang.String value) {
		this.userJobid = value;
	}
	
	public java.lang.String getUserJobid() {
		return this.userJobid;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setPostId(java.lang.String value) {
		this.postId = value;
	}
	
	public java.lang.String getPostId() {
		return this.postId;
	}
	public void setIsMain(java.lang.String value) {
		this.isMain = value;
	}
	
	public java.lang.String getIsMain() {
		return this.isMain;
	}
	
	private TsysUserinfo tsysUserinfo;
	
	public void setTsysUserinfo(TsysUserinfo tsysUserinfo){
		this.tsysUserinfo = tsysUserinfo;
	}
	
	public TsysUserinfo getTsysUserinfo() {
		return tsysUserinfo;
	}
	
	private TsysPost tsysPost;
	
	public void setTsysPost(TsysPost tsysPost){
		this.tsysPost = tsysPost;
	}
	
	public TsysPost getTsysPost() {
		return tsysPost;
	}

	public java.lang.String getPostName() {
		return postName;
	}

	public void setPostName(java.lang.String postName) {
		this.postName = postName;
	}

}

