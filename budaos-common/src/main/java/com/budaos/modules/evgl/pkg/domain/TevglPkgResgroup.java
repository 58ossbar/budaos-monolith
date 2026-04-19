package com.budaos.modules.evgl.pkg.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglPkgResgroup extends BaseDomain<TevglPkgResgroup>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglPkgResgroup";
	public static final String ALIAS_RESGROUP_ID = "资源分组主键ID";
	public static final String ALIAS_CT_ID = "课堂(教室)主键";
	public static final String ALIAS_PKG_ID = "所属教学包ID";
	public static final String ALIAS_SUBJECT_ID = "课程id(满足多个教学包对应一个课程的情况)";
	public static final String ALIAS_CHAPTER_ID = "所属章节ID";
	public static final String ALIAS_RESGROUP_NAME = "分组名称";
	public static final String ALIAS_PARENT_ID = "父分组ID";
	public static final String ALIAS_RES_TYPE = "资源类型(教学资源)";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_RESGROUP_TOTAL = "资源总数";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_GROUP_TYPE = "分组类型(1活教材2活动)";
	public static final String ALIAS_DICT_CODE = "默认的资源分组的dict_code，值为1时表示[课程内容]，值为2时表示[活动]";
	

    /**
     * 资源分组主键ID       db_column: resgroup_id 
     */	
 	@NotNull(msg="资源分组主键ID不能为空")
 	@MaxLength(value=32, msg="字段[资源分组主键ID]超出最大长度[32]")
	private java.lang.String resgroupId;
    /**
     * 课堂(教室)主键       db_column: ct_id 
     */	
 	@NotNull(msg="课堂(教室)主键不能为空")
 	@MaxLength(value=32, msg="字段[课堂(教室)主键]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 所属教学包ID       db_column: pkg_id 
     */	
 	@NotNull(msg="所属教学包ID不能为空")
 	@MaxLength(value=32, msg="字段[所属教学包ID]超出最大长度[32]")
	private java.lang.String pkgId;
 	/**
     * 课程id(满足多个教学包对应一个课程的情况)       db_column: subject_id 
     */	
 	@NotNull(msg="课程id(满足多个教学包对应一个课程的情况)不能为空")
 	@MaxLength(value=32, msg="字段[课程id(满足多个教学包对应一个课程的情况)]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 所属章节ID       db_column: chapter_id 
     */	
 	@NotNull(msg="所属章节ID不能为空")
 	@MaxLength(value=32, msg="字段[所属章节ID]超出最大长度[32]")
	private java.lang.String chapterId;
    /**
     * 分组名称       db_column: resgroup_name 
     */	
 	@NotNull(msg="分组名称不能为空")
 	@MaxLength(value=50, msg="字段[分组名称]超出最大长度[50]")
	private java.lang.String resgroupName;
    /**
     * 父分组ID       db_column: parent_id 
     */	
 	@NotNull(msg="父分组ID不能为空")
 	@MaxLength(value=32, msg="字段[父分组ID]超出最大长度[32]")
	private java.lang.String parentId;
    /**
     * 资源类型(教学资源)       db_column: res_type 
     */	
 	@NotNull(msg="资源类型(教学资源)不能为空")
 	@MaxLength(value=32, msg="字段[资源类型(教学资源)]超出最大长度[32]")
	private java.lang.String resType;
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 资源总数       db_column: resgroup_total 
     */	
 	@NotNull(msg="资源总数不能为空")
 	@MaxLength(value=10, msg="字段[资源总数]超出最大长度[10]")
	private java.lang.Integer resgroupTotal;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 分组类型(1活教材2活动)       db_column: group_type 
     */	
 	@NotNull(msg="分组类型(1活教材2活动)不能为空")
 	@MaxLength(value=2, msg="字段[分组类型(1活教材2活动)]超出最大长度[2]")
	private java.lang.String groupType;
    /**
     * 默认的资源分组的dict_code，值为1时表示[课程内容]，值为2时表示[活动]       db_column: dict_code 
     */	
 	@NotNull(msg="默认的资源分组的dict_code，值为1时表示[课程内容]，值为2时表示[活动]不能为空")
 	@MaxLength(value=2, msg="字段[默认的资源分组的dict_code，值为1时表示[课程内容]，值为2时表示[活动]]超出最大长度[2]")
	private java.lang.String dictCode;
	//columns END
 	
 	private List<TevglPkgResgroup> children; // 教学包资源分组目录子数据
 	private List<TevglPkgRes> tevglPkgResList; // 教学包资源

	public TevglPkgResgroup(){
	}

	public TevglPkgResgroup(
		java.lang.String resgroupId
	){
		this.resgroupId = resgroupId;
	}

	public void setResgroupId(java.lang.String value) {
		this.resgroupId = value;
	}
	public java.lang.String getResgroupId() {
		return this.resgroupId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setPkgId(java.lang.String value) {
		this.pkgId = value;
	}
	public java.lang.String getPkgId() {
		return this.pkgId;
	}
	public java.lang.String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(java.lang.String subjectId) {
		this.subjectId = subjectId;
	}
	public void setChapterId(java.lang.String value) {
		this.chapterId = value;
	}
	public java.lang.String getChapterId() {
		return this.chapterId;
	}
	public void setResgroupName(java.lang.String value) {
		this.resgroupName = value;
	}
	public java.lang.String getResgroupName() {
		return this.resgroupName;
	}
	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}
	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setResType(java.lang.String value) {
		this.resType = value;
	}
	public java.lang.String getResType() {
		return this.resType;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setResgroupTotal(java.lang.Integer value) {
		this.resgroupTotal = value;
	}
	public java.lang.Integer getResgroupTotal() {
		return this.resgroupTotal;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setGroupType(java.lang.String value) {
		this.groupType = value;
	}
	public java.lang.String getGroupType() {
		return this.groupType;
	}
	public void setDictCode(java.lang.String value) {
		this.dictCode = value;
	}
	public java.lang.String getDictCode() {
		return this.dictCode;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public List<TevglPkgResgroup> getChildren() {
		return children;
	}

	public void setChildren(List<TevglPkgResgroup> children) {
		this.children = children;
	}

	public List<TevglPkgRes> getTevglPkgResList() {
		return tevglPkgResList;
	}

	public void setTevglPkgResList(List<TevglPkgRes> tevglPkgResList) {
		this.tevglPkgResList = tevglPkgResList;
	}
}

