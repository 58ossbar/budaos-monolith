package com.budaos.modules.evgl.tch.domain;

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

public class TevglTchClassroomSetting extends BaseDomain<TevglTchClassroomSetting>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchClassroomSetting";
	public static final String ALIAS_SETTING_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_MODULE = "影响的模块：classroom_trainee课堂成员列表（1根据经验值2根据学员名称3根据手机号码4根据加入课堂时间），2课堂小组列表，3课堂小组成员，等等";
	public static final String ALIAS_SIDX = "排序字段";
	public static final String ALIAS_ORDER = "固定值，asc升序，desc降序";
	

    /**
     * 主键id       db_column: setting_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String settingId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 影响的模块：classroom_trainee课堂成员列表（1根据经验值2根据学员名称3根据手机号码4根据加入课堂时间），2课堂小组列表，3课堂小组成员，等等       db_column: module 
     */	
 	@NotNull(msg="影响的模块：classroom_trainee课堂成员列表（1根据经验值2根据学员名称3根据手机号码4根据加入课堂时间），2课堂小组列表，3课堂小组成员，等等不能为空")
 	@MaxLength(value=20, msg="字段[影响的模块：classroom_trainee课堂成员列表（1根据经验值2根据学员名称3根据手机号码4根据加入课堂时间），2课堂小组列表，3课堂小组成员，等等]超出最大长度[20]")
	private java.lang.String module;
    /**
     * 排序字段       db_column: sidx 
     */	
 	@NotNull(msg="排序字段不能为空")
 	@MaxLength(value=20, msg="字段[排序字段]超出最大长度[20]")
	private java.lang.String sidx;
    /**
     * 固定值，asc升序，desc降序       db_column: order 
     */	
 	@NotNull(msg="固定值，asc升序，desc降序不能为空")
 	@MaxLength(value=20, msg="字段[固定值，asc升序，desc降序]超出最大长度[20]")
	private java.lang.String order;
	//columns END

	public TevglTchClassroomSetting(){
	}

	public TevglTchClassroomSetting(
		java.lang.String settingId
	){
		this.settingId = settingId;
	}

	public void setSettingId(java.lang.String value) {
		this.settingId = value;
	}
	public java.lang.String getSettingId() {
		return this.settingId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setModule(java.lang.String value) {
		this.module = value;
	}
	public java.lang.String getModule() {
		return this.module;
	}
	public void setSidx(java.lang.String value) {
		this.sidx = value;
	}
	public java.lang.String getSidx() {
		return this.sidx;
	}
	public void setOrder(java.lang.String value) {
		this.order = value;
	}
	public java.lang.String getOrder() {
		return this.order;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

