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

public class TevglPkgCheck extends BaseDomain<TevglPkgCheck>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglPkgCheck";
	public static final String ALIAS_PC_ID = "主键id";
	public static final String ALIAS_PKG_ID = "教学包id";
	public static final String ALIAS_CHECK_STATE = "审核状态（Y/N）";
	public static final String ALIAS_REASON = "不通过理由";
	

    /**
     * 主键id       db_column: pc_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String pcId;
    /**
     * 教学包id       db_column: pkg_id 
     */	
 	@NotNull(msg="教学包id不能为空")
 	@MaxLength(value=32, msg="字段[教学包id]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * 审核状态（Y/N）       db_column: check_state 
     */	
 	@NotNull(msg="审核状态（Y/N）不能为空")
 	@MaxLength(value=2, msg="字段[审核状态（Y/N）]超出最大长度[2]")
	private java.lang.String checkState;
    /**
     * 不通过理由       db_column: reason 
     */	
 	@NotNull(msg="不通过理由不能为空")
 	@MaxLength(value=65535, msg="字段[不通过理由]超出最大长度[65535]")
	private java.lang.String reason;
	//columns END

	public TevglPkgCheck(){
	}

	public TevglPkgCheck(
		java.lang.String pcId
	){
		this.pcId = pcId;
	}

	public void setPcId(java.lang.String value) {
		this.pcId = value;
	}
	public java.lang.String getPcId() {
		return this.pcId;
	}
	public void setPkgId(java.lang.String value) {
		this.pkgId = value;
	}
	public java.lang.String getPkgId() {
		return this.pkgId;
	}
	public void setCheckState(java.lang.String value) {
		this.checkState = value;
	}
	public java.lang.String getCheckState() {
		return this.checkState;
	}
	public void setReason(java.lang.String value) {
		this.reason = value;
	}
	public java.lang.String getReason() {
		return this.reason;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

