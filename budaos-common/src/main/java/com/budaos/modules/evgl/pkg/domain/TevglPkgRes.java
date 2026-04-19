package com.budaos.modules.evgl.pkg.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 教学包资源</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglPkgRes extends BaseDomain<TevglPkgRes>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglPkgRes";
	public static final String ALIAS_RES_ID = "资源主键ID";
	public static final String ALIAS_PKG_ID = "所属教学包";
	public static final String ALIAS_RESGROUP_ID = "所属资源分组";
	public static final String ALIAS_RES_TYPE = "资源类型(离线or在线)";
	public static final String ALIAS_RES_CONTENT = "资源内容";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_RES_SAVE_URL = "资源保存路径";
	public static final String ALIAS_RES_DOWN_URL = "资源下载路径";
	public static final String ALIAS_RES_NAME = "资源名称";
	public static final String ALIAS_RES_SIZE = "资源大小";
	public static final String ALIAS_RES_SUFFIX = "资源文件后缀";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	

    /**
     * 资源主键ID       db_column: res_id 
     */	
 	@NotNull(msg="资源主键ID不能为空")
 	@MaxLength(value=32, msg="字段[资源主键ID]超出最大长度[32]")
	private java.lang.String resId;
    /**
     * 所属教学包       db_column: pkg_id 
     */	
 	//@NotNull(msg="所属教学包不能为空")
 	@MaxLength(value=32, msg="字段[所属教学包]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * 所属资源分组       db_column: resgroup_id 
     */	
 	@NotNull(msg="所属资源分组不能为空")
 	@MaxLength(value=32, msg="字段[所属资源分组]超出最大长度[32]")
	private java.lang.String resgroupId;
    /**
     * 资源类型(离线or在线)       db_column: res_type 
     */	
 	//@NotNull(msg="资源类型(离线or在线)不能为空")
 	@MaxLength(value=32, msg="字段[资源类型(离线or在线)]超出最大长度[32]")
	private java.lang.String resType;
    /**
     * 资源内容       db_column: res_content 
     */	
 	//@NotNull(msg="资源内容不能为空")
 	@MaxLength(value=2147483647, msg="字段[资源内容]超出最大长度[2147483647]")
	private java.lang.String resContent;
    /**
     * 排序号       db_column: sort_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 资源保存路径       db_column: res_save_url 
     */	
 	//@NotNull(msg="资源保存路径不能为空")
 	@MaxLength(value=300, msg="字段[资源保存路径]超出最大长度[300]")
	private java.lang.String resSaveUrl;
    /**
     * 资源下载路径       db_column: res_down_url 
     */	
 	//@NotNull(msg="资源下载路径不能为空")
 	@MaxLength(value=300, msg="字段[资源下载路径]超出最大长度[300]")
	private java.lang.String resDownUrl;
    /**
     * 资源名称       db_column: res_name 
     */	
 	@NotNull(msg="资源名称不能为空")
 	@MaxLength(value=50, msg="字段[资源名称]超出最大长度[50]")
	private java.lang.String resName;
    /**
     * 资源大小       db_column: res_size 
     */	
 	//@NotNull(msg="资源大小不能为空")
 	@MaxLength(value=10, msg="字段[资源大小]超出最大长度[10]")
	private java.lang.Integer resSize;
    /**
     * 资源文件后缀       db_column: res_suffix 
     */	
 	//@NotNull(msg="资源文件后缀不能为空")
 	@MaxLength(value=10, msg="字段[资源文件后缀]超出最大长度[10]")
	private java.lang.String resSuffix;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
 	/**
     * 阅读量       db_column: view_num 
     */	
 	//@NotNull(msg="阅读量不能为空")
 	@MaxLength(value=11, msg="字段[资源大小]超出最大长度[11]")
	private java.lang.Integer viewNum;
	//columns END

	public TevglPkgRes(){
	}

	public TevglPkgRes(
		java.lang.String resId
	){
		this.resId = resId;
	}

	public void setResId(java.lang.String value) {
		this.resId = value;
	}
	public java.lang.String getResId() {
		return this.resId;
	}
	public void setPkgId(java.lang.String value) {
		this.pkgId = value;
	}
	public java.lang.String getPkgId() {
		return this.pkgId;
	}
	public void setResgroupId(java.lang.String value) {
		this.resgroupId = value;
	}
	public java.lang.String getResgroupId() {
		return this.resgroupId;
	}
	public void setResType(java.lang.String value) {
		this.resType = value;
	}
	public java.lang.String getResType() {
		return this.resType;
	}
	public void setResContent(java.lang.String value) {
		this.resContent = value;
	}
	public java.lang.String getResContent() {
		return this.resContent;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setResSaveUrl(java.lang.String value) {
		this.resSaveUrl = value;
	}
	public java.lang.String getResSaveUrl() {
		return this.resSaveUrl;
	}
	public void setResDownUrl(java.lang.String value) {
		this.resDownUrl = value;
	}
	public java.lang.String getResDownUrl() {
		return this.resDownUrl;
	}
	public void setResName(java.lang.String value) {
		this.resName = value;
	}
	public java.lang.String getResName() {
		return this.resName;
	}
	public void setResSize(java.lang.Integer value) {
		this.resSize = value;
	}
	public java.lang.Integer getResSize() {
		return this.resSize;
	}
	public void setResSuffix(java.lang.String value) {
		this.resSuffix = value;
	}
	public java.lang.String getResSuffix() {
		return this.resSuffix;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	
	private TevglPkgResgroup tevglPkgResgroup;
	
	public void setTevglPkgResgroup(TevglPkgResgroup tevglPkgResgroup){
		this.tevglPkgResgroup = tevglPkgResgroup;
	}
	
	public TevglPkgResgroup getTevglPkgResgroup() {
		return tevglPkgResgroup;
	}
	
	private TevglPkgInfo tevglPkgInfo;
	
	public void setTevglPkgInfo(TevglPkgInfo tevglPkgInfo){
		this.tevglPkgInfo = tevglPkgInfo;
	}
	
	public TevglPkgInfo getTevglPkgInfo() {
		return tevglPkgInfo;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(java.lang.Integer viewNum) {
		this.viewNum = viewNum;
	}
}

