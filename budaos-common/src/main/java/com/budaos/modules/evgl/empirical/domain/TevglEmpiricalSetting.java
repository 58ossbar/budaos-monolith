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

public class TevglEmpiricalSetting extends BaseDomain<TevglEmpiricalSetting>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglEmpiricalSetting";
	public static final String ALIAS_ST_ID = "主键id";
	public static final String ALIAS_TYPE_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_DICT_CODE = "类型(来源字典1章节阅读加2查看视频加3查看音频加4正常签到一次加5迟到一次减旷课一次减6请假一次减7参与投票问卷加8参与头脑风暴加9参与答疑讨论加)";
	public static final String ALIAS_NAME = "名称";
	public static final String ALIAS_VALUE = "可得经验值";
	

    /**
     * 主键id       db_column: st_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String stId;
    /**
     * 主键id       db_column: type_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String typeId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 类型(来源字典1章节阅读加2查看视频加3查看音频加4正常签到一次加5迟到一次减旷课一次减6请假一次减7参与投票问卷加8参与头脑风暴加9参与答疑讨论加)       db_column: dict_code 
     */	
 	@NotNull(msg="类型(来源字典1章节阅读加2查看视频加3查看音频加4正常签到一次加5迟到一次减旷课一次减6请假一次减7参与投票问卷加8参与头脑风暴加9参与答疑讨论加)不能为空")
 	@MaxLength(value=5, msg="字段[类型(来源字典1章节阅读加2查看视频加3查看音频加4正常签到一次加5迟到一次减旷课一次减6请假一次减7参与投票问卷加8参与头脑风暴加9参与答疑讨论加)]超出最大长度[5]")
	private java.lang.String dictCode;
    /**
     * 名称       db_column: name 
     */	
 	@NotNull(msg="名称不能为空")
 	@MaxLength(value=100, msg="字段[名称]超出最大长度[100]")
	private java.lang.String name;
    /**
     * 可得经验值       db_column: value 
     */	
 	@NotNull(msg="可得经验值不能为空")
 	@MaxLength(value=5, msg="字段[可得经验值]超出最大长度[5]")
	private java.math.BigDecimal value;
	//columns END

	public TevglEmpiricalSetting(){
	}

	public TevglEmpiricalSetting(
		java.lang.String stId
	){
		this.stId = stId;
	}

	public void setStId(java.lang.String value) {
		this.stId = value;
	}
	public java.lang.String getStId() {
		return this.stId;
	}
	public void setTypeId(java.lang.String value) {
		this.typeId = value;
	}
	public java.lang.String getTypeId() {
		return this.typeId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setDictCode(java.lang.String value) {
		this.dictCode = value;
	}
	public java.lang.String getDictCode() {
		return this.dictCode;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setValue(java.math.BigDecimal value) {
		this.value = value;
	}
	public java.math.BigDecimal getValue() {
		return this.value;
	}
	
	private TevglEmpiricalType tevglEmpiricalType;
	
	public void setTevglEmpiricalType(TevglEmpiricalType tevglEmpiricalType){
		this.tevglEmpiricalType = tevglEmpiricalType;
	}
	
	public TevglEmpiricalType getTevglEmpiricalType() {
		return tevglEmpiricalType;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

