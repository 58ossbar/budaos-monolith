package com.budaos.modules.sys.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TsysSettings extends BaseDomain<TsysSettings> {
	private static final long serialVersionUID = 1L;

	// alias
	public static final String TABLE_ALIAS = "TsysSettings";
	public static final String ALIAS_SETTING_ID = "主键";
	public static final String ALIAS_SETTING_TYPE = "属性类型(sys系统级user用户级)";
	public static final String ALIAS_SETTING_CODE = "属性编码";
	public static final String ALIAS_SETTING_NAME = "属性名称";
	public static final String ALIAS_SETTING_VALUE = "属性值";
	public static final String ALIAS_SETTING_GROUP = "属性分组";
	public static final String ALIAS_SETTING_USER_ID = "属性类型为user用户级时的用户id";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_REMARK = "备注";

	/**
	 * 主键 db_column: setting_id
	 */
	@NotNull(msg = "主键不能为空")
	@MaxLength(value = 32, msg = "字段[主键]超出最大长度[32]")
	private java.lang.String settingId;
	/**
	 * 属性类型(sys系统级user用户级) db_column: setting_type
	 */
	// @NotNull(msg="属性类型(sys系统级user用户级)不能为空")
	@MaxLength(value = 10, msg = "字段[属性类型(sys系统级user用户级)]超出最大长度[10]")
	private java.lang.String settingType;
	/**
	 * 属性编码 db_column: setting_code
	 */
	// @NotNull(msg="属性编码不能为空")
	@MaxLength(value = 50, msg = "字段[属性编码]超出最大长度[50]")
	private java.lang.String settingCode;
	/**
	 * 属性名称 db_column: setting_name
	 */
	// @NotNull(msg="属性名称不能为空")
	@MaxLength(value = 100, msg = "字段[属性名称]超出最大长度[100]")
	private java.lang.String settingName;
	/**
	 * 属性值 db_column: setting_value
	 */
	//@NotNull(msg = "属性值不能为空")
	private java.lang.String settingValue;
	/**
	 * 属性分组 db_column: setting_group
	 */
	// @NotNull(msg="属性分组不能为空")
	@MaxLength(value = 50, msg = "字段[属性分组]超出最大长度[50]")
	private java.lang.String settingGroup;
	/**
	 * 属性类型为user用户级时的用户id db_column: setting_user_id
	 */
	@MaxLength(value = 32, msg = "字段[属性类型为user用户级时的用户id]超出最大长度[32]")
	private java.lang.String settingUserId;
	/**
	 * 状态(Y有效N无效) db_column: state
	 */
	// @NotNull(msg="状态(Y有效N无效)不能为空")
	@MaxLength(value = 2, msg = "字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
	/**
	 * 备注 db_column: remark
	 */
	@MaxLength(value = 255, msg = "字段[备注]超出最大长度[255]")
	private java.lang.String remark;
	// columns END

	public TsysSettings() {
	}

	public TsysSettings(java.lang.String settingId) {
		this.settingId = settingId;
	}

	public void setSettingId(java.lang.String value) {
		this.settingId = value;
	}

	public java.lang.String getSettingId() {
		return this.settingId;
	}

	public void setSettingType(java.lang.String value) {
		this.settingType = value;
	}

	public java.lang.String getSettingType() {
		return this.settingType;
	}

	public void setSettingCode(java.lang.String value) {
		this.settingCode = value;
	}

	public java.lang.String getSettingCode() {
		return this.settingCode;
	}

	public void setSettingName(java.lang.String value) {
		this.settingName = value;
	}

	public java.lang.String getSettingName() {
		return this.settingName;
	}

	public void setSettingValue(java.lang.String value) {
		this.settingValue = value;
	}

	public java.lang.String getSettingValue() {
		return this.settingValue;
	}

	public void setSettingGroup(java.lang.String value) {
		this.settingGroup = value;
	}

	public java.lang.String getSettingGroup() {
		return this.settingGroup;
	}

	public void setSettingUserId(java.lang.String value) {
		this.settingUserId = value;
	}

	public java.lang.String getSettingUserId() {
		return this.settingUserId;
	}

	public void setState(java.lang.String value) {
		this.state = value;
	}

	public java.lang.String getState() {
		return this.state;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.lang.String getRemark() {
		return this.remark;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
