package com.budaos.modules.evgl.pkg.domain;

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

public class TevglPkgResgroupAllowCopy extends BaseDomain<TevglPkgResgroupAllowCopy>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglPkgResgroupAllowCopy";
	public static final String ALIAS_CP_ID = "主键id";
	public static final String ALIAS_PKG_ID = "课堂对应的教学包id";
	public static final String ALIAS_SUBJECT_ID = "subjectId";
	public static final String ALIAS_RESGROUP_ID = "资源分组表主键id";
	public static final String ALIAS_IS_CAN_COPY = "（Y/N）是否允许复制本分组下的资源内容";
	

    /**
     * 主键id       db_column: cp_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String cpId;
    /**
     * 课堂对应的教学包id       db_column: pkg_id 
     */	
 	@NotNull(msg="课堂对应的教学包id不能为空")
 	@MaxLength(value=32, msg="字段[课堂对应的教学包id]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * subjectId       db_column: subject_id 
     */	
 	@NotNull(msg="subjectId不能为空")
 	@MaxLength(value=32, msg="字段[subjectId]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 资源分组表主键id       db_column: resgroup_id 
     */	
 	@NotNull(msg="资源分组表主键id不能为空")
 	@MaxLength(value=32, msg="字段[资源分组表主键id]超出最大长度[32]")
	private java.lang.String resgroupId;
    /**
     * （Y/N）是否允许复制本分组下的资源内容       db_column: is_can_copy 
     */	
 	@NotNull(msg="（Y/N）是否允许复制本分组下的资源内容不能为空")
 	@MaxLength(value=2, msg="字段[（Y/N）是否允许复制本分组下的资源内容]超出最大长度[2]")
	private java.lang.String isCanCopy;
	//columns END

	public TevglPkgResgroupAllowCopy(){
	}

	public TevglPkgResgroupAllowCopy(
		java.lang.String cpId
	){
		this.cpId = cpId;
	}

	public void setCpId(java.lang.String value) {
		this.cpId = value;
	}
	public java.lang.String getCpId() {
		return this.cpId;
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
	public void setResgroupId(java.lang.String value) {
		this.resgroupId = value;
	}
	public java.lang.String getResgroupId() {
		return this.resgroupId;
	}
	public void setIsCanCopy(java.lang.String value) {
		this.isCanCopy = value;
	}
	public java.lang.String getIsCanCopy() {
		return this.isCanCopy;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

