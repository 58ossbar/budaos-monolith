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

public class TevglPkgResgroupVisible extends BaseDomain<TevglPkgResgroupVisible>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglPkgResgroupVisible";
	public static final String ALIAS_RV_ID = "rvId";
	public static final String ALIAS_CT_ID = "ctId";
	public static final String ALIAS_PKG_ID = "pkgId";
	public static final String ALIAS_RESGROUP_ID = "resgroupId";
	public static final String ALIAS_IS_TRAINEES_VISIBLE = "isTraineesVisible";
	public static final String ALIAS_REF_PKG_ID = "refPkgId";
	
	/**
     * rvId       db_column: rv_id 
     */	
 	@NotNull(msg="rvId不能为空")
 	@MaxLength(value=32, msg="字段[rvId]超出最大长度[32]")
	private java.lang.String rvId;

    /**
     * ctId       db_column: ct_id 
     */	
 	@NotNull(msg="ctId不能为空")
 	@MaxLength(value=32, msg="字段[ctId]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * pkgId       db_column: pkg_id 
     */	
 	@NotNull(msg="pkgId不能为空")
 	@MaxLength(value=32, msg="字段[pkgId]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * resgroupId       db_column: resgroup_id 
     */	
 	@NotNull(msg="resgroupId不能为空")
 	@MaxLength(value=32, msg="字段[resgroupId]超出最大长度[32]")
	private java.lang.String resgroupId;
    /**
     * isTraineesVisible       db_column: is_trainees_visible 
     */	
 	@NotNull(msg="isTraineesVisible不能为空")
 	@MaxLength(value=2, msg="字段[isTraineesVisible]超出最大长度[2]")
	private java.lang.String isTraineesVisible;
    /**
     * refPkgId       db_column: ref_pkg_id 
     */	
 	@NotNull(msg="refPkgId不能为空")
 	@MaxLength(value=32, msg="字段[refPkgId]超出最大长度[32]")
	private java.lang.String refPkgId;
	//columns END

	public TevglPkgResgroupVisible(){
	}

	public TevglPkgResgroupVisible(
		java.lang.String rvId
	){
		this.rvId = rvId;
	}

	public void setRvId(java.lang.String value) {
		this.rvId = value;
	}
	public java.lang.String getRvId() {
		return this.rvId;
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
	public void setResgroupId(java.lang.String value) {
		this.resgroupId = value;
	}
	public java.lang.String getResgroupId() {
		return this.resgroupId;
	}
	public void setIsTraineesVisible(java.lang.String value) {
		this.isTraineesVisible = value;
	}
	public java.lang.String getIsTraineesVisible() {
		return this.isTraineesVisible;
	}
	public void setRefPkgId(java.lang.String value) {
		this.refPkgId = value;
	}
	public java.lang.String getRefPkgId() {
		return this.refPkgId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

