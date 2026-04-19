package com.budaos.modules.evgl.empirical.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglEmpiricalLogChapter extends BaseDomain<TevglEmpiricalLogChapter>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglEmpiricalLogChapter";
	public static final String ALIAS_CB_ID = "主键id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_PKG_ID = "教学包id";
	public static final String ALIAS_SUBJECT_ID = "教材id";
	public static final String ALIAS_CHAPTER_ID = "章节id";
	public static final String ALIAS_RES_ID = "资源id";
	public static final String ALIAS_EMPIRICAL_VALUE = "获得经验值";
	

    /**
     * 主键id       db_column: cb_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String cbId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 教学包id       db_column: pkg_id 
     */	
 	@NotNull(msg="教学包id不能为空")
 	@MaxLength(value=32, msg="字段[教学包id]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * 教材id       db_column: subject_id 
     */	
 	@NotNull(msg="教材id不能为空")
 	@MaxLength(value=32, msg="字段[教材id]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 章节id       db_column: chapter_id 
     */	
 	@NotNull(msg="章节id不能为空")
 	@MaxLength(value=32, msg="字段[章节id]超出最大长度[32]")
	private java.lang.String chapterId;
    /**
     * 资源id       db_column: res_id 
     */	
 	@NotNull(msg="资源id不能为空")
 	@MaxLength(value=32, msg="字段[资源id]超出最大长度[32]")
	private java.lang.String resId;
    /**
     * 获得经验值       db_column: empirical_value 
     */	
 	@NotNull(msg="获得经验值不能为空")
 	@MaxLength(value=10, msg="字段[获得经验值]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
	//columns END

	public TevglEmpiricalLogChapter(){
	}

	public TevglEmpiricalLogChapter(
		java.lang.String cbId
	){
		this.cbId = cbId;
	}

	public void setCbId(java.lang.String value) {
		this.cbId = value;
	}
	public java.lang.String getCbId() {
		return this.cbId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
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
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setChapterId(java.lang.String value) {
		this.chapterId = value;
	}
	public java.lang.String getChapterId() {
		return this.chapterId;
	}
	public void setResId(java.lang.String value) {
		this.resId = value;
	}
	public java.lang.String getResId() {
		return this.resId;
	}
	public void setEmpiricalValue(java.lang.Integer value) {
		this.empiricalValue = value;
	}
	public java.lang.Integer getEmpiricalValue() {
		return this.empiricalValue;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

