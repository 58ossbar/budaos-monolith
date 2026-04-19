package com.budaos.modules.evgl.tch.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglTchClassroomSb extends BaseDomain<TevglTchClassroomSb>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchClassroomSb";
	public static final String ALIAS_SB_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_CT_PKG_ID = "课堂教学包id";
	public static final String ALIAS_SUBJECT_ID = "教材id";
	

    /**
     * 主键id       db_column: sb_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String sbId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 课堂教学包id       db_column: ct_pkg_id 
     */	
 	@NotNull(msg="课堂教学包id不能为空")
 	@MaxLength(value=32, msg="字段[课堂教学包id]超出最大长度[32]")
	private java.lang.String ctPkgId;
 	
 	private java.lang.String pkgId;
 	
    /**
     * 教材id       db_column: subject_id 
     */	
 	@NotNull(msg="教材id不能为空")
 	@MaxLength(value=32, msg="字段[教材id]超出最大长度[32]")
	private java.lang.String subjectId;
	//columns END

	private List<Map<String, Object>> subjectList;
	
	public TevglTchClassroomSb(){
	}

	public TevglTchClassroomSb(
		java.lang.String sbId
	){
		this.sbId = sbId;
	}

	public void setSbId(java.lang.String value) {
		this.sbId = value;
	}
	public java.lang.String getSbId() {
		return this.sbId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setCtPkgId(java.lang.String value) {
		this.ctPkgId = value;
	}
	public java.lang.String getCtPkgId() {
		return this.ctPkgId;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public java.lang.String getPkgId() {
		return pkgId;
	}

	public void setPkgId(java.lang.String pkgId) {
		this.pkgId = pkgId;
	}

	public List<Map<String, Object>> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Map<String, Object>> subjectList) {
		this.subjectList = subjectList;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

