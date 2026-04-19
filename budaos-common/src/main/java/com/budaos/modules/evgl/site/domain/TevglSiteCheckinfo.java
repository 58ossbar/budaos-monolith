package com.budaos.modules.evgl.site.domain;

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

public class TevglSiteCheckinfo extends BaseDomain<TevglSiteCheckinfo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteCheckinfo";
	public static final String ALIAS_ID = "审核记录标识主键";
	public static final String ALIAS_REF_ID = "关联id";
	public static final String ALIAS_REASON = "理由";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_TYPE = "审核类型(1新闻2广告3等等等)";
	

    /**
     * 审核记录标识主键       db_column: id 
     */	
 	@NotNull(msg="审核记录标识主键不能为空")
 	@MaxLength(value=32, msg="字段[审核记录标识主键]超出最大长度[32]")
	private java.lang.String id;
    /**
     * 关联id       db_column: ref_id 
     */	
 	@NotNull(msg="关联id不能为空")
 	@MaxLength(value=32, msg="字段[关联id]超出最大长度[32]")
	private java.lang.String refId;
    /**
     * 理由       db_column: reason 
     */	
 	//@NotNull(msg="理由不能为空")
 	@MaxLength(value=500, msg="字段[理由]超出最大长度[500]")
	private java.lang.String reason;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	//@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 审核类型(1新闻2广告3等等等)       db_column: type 
     */	
 	//@NotNull(msg="审核类型(1新闻2广告3等等等)不能为空")
 	@MaxLength(value=3, msg="字段[审核类型(1新闻2广告3等等等)]超出最大长度[3]")
	private java.lang.String type;
 	/**
     * 是否通过(Y通过N未通过)       db_column: pass 
     */	
 	//@NotNull(msg="是否通过(Y通过N未通过)不能为空")
 	@MaxLength(value=5, msg="字段[是否通过(Y通过N未通过)]超出最大长度[5]")
	private java.lang.String pass;
	//columns END

	public TevglSiteCheckinfo(){
	}

	public TevglSiteCheckinfo(
		java.lang.String id
	){
		this.id = id;
	}

	public void setId(java.lang.String value) {
		this.id = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setRefId(java.lang.String value) {
		this.refId = value;
	}
	public java.lang.String getRefId() {
		return this.refId;
	}
	public void setReason(java.lang.String value) {
		this.reason = value;
	}
	public java.lang.String getReason() {
		return this.reason;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public java.lang.String getPass() {
		return pass;
	}

	public void setPass(java.lang.String pass) {
		this.pass = pass;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

