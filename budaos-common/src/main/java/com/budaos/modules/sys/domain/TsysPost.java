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

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TsysPost extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TsysPost";
	public static final String ALIAS_POST_ID = "岗位ID";
	public static final String ALIAS_POST_NAME = "岗位名称";
	public static final String ALIAS_REMARK = "岗位描述";
	public static final String ALIAS_POST_TYPE = "岗位类别:字典中定义";
	public static final String ALIAS_PARENT_POSTID = "父岗位ID";
	

    /**
     * 岗位ID       db_column: POST_ID 
     */	
	private java.lang.String postId;
    /**
     * 岗位名称       db_column: POST_NAME 
     */	
	private java.lang.String postName;
    /**
     * 岗位描述       db_column: REMARK 
     */	
	private java.lang.String remark;
    /**
     * 岗位类别:字典中定义       db_column: POST_TYPE 
     */	
	private java.lang.String postType;
    /**
     * 父岗位ID       db_column: PARENT_POSTID 
     */	
	private java.lang.String parentPostid;
	
	private List<TsysPost> children;
	private java.lang.String parentName; // 父岗位名称
	private java.lang.Integer sort; // 排序号
	
	//columns END

	public TsysPost(){
	}

	public TsysPost(
		java.lang.String postId
	){
		this.postId = postId;
	}

	public void setPostId(java.lang.String value) {
		this.postId = value;
	}
	
	public java.lang.String getPostId() {
		return this.postId;
	}
	public void setPostName(java.lang.String value) {
		this.postName = value;
	}
	
	public java.lang.String getPostName() {
		return this.postName;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setPostType(java.lang.String value) {
		this.postType = value;
	}
	
	public java.lang.String getPostType() {
		return this.postType;
	}
	public void setParentPostid(java.lang.String value) {
		this.parentPostid = value;
	}
	
	public java.lang.String getParentPostid() {
		return this.parentPostid;
	}
	
	@SuppressWarnings("rawtypes")
	private Set tuserPosts = new HashSet(0);
	public void setTuserPosts(Set<TuserPost> tuserPost){
		this.tuserPosts = tuserPost;
	}
	
	@SuppressWarnings("unchecked")
	public Set<TuserPost> getTuserPosts() {
		return tuserPosts;
	}
	
	@SuppressWarnings("rawtypes")
	private Set trolePosts = new HashSet(0);
	public void setTrolePosts(Set<TrolePost> trolePost){
		this.trolePosts = trolePost;
	}
	
	@SuppressWarnings("unchecked")
	public Set<TrolePost> getTrolePosts() {
		return trolePosts;
	}

	public List<TsysPost> getChildren() {
		return children;
	}

	public void setChildren(List<TsysPost> children) {
		this.children = children;
	}

	public java.lang.String getParentName() {
		return parentName;
	}

	public void setParentName(java.lang.String parentName) {
		this.parentName = parentName;
	}

	public java.lang.Integer getSort() {
		return sort;
	}

	public void setSort(java.lang.Integer sort) {
		this.sort = sort;
	}

}

