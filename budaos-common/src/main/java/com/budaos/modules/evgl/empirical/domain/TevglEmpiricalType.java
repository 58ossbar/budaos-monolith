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

public class TevglEmpiricalType extends BaseDomain<TevglEmpiricalType>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglEmpiricalType";
	public static final String ALIAS_TYPE_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_DICT_CODE = "类别(来源1活教材占比权重2考勤占比权重3活动占比权重)";
	public static final String ALIAS_NAME = "名称";
	public static final String ALIAS_WEIGHT = "占比权重";
	

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
     * 类别(来源1活教材占比权重2考勤占比权重3活动占比权重)       db_column: dict_code 
     */	
 	@NotNull(msg="类别(来源1活教材占比权重2考勤占比权重3活动占比权重)不能为空")
 	@MaxLength(value=2, msg="字段[类别(来源1活教材占比权重2考勤占比权重3活动占比权重)]超出最大长度[2]")
	private java.lang.String dictCode;
    /**
     * 名称       db_column: name 
     */	
 	@NotNull(msg="名称不能为空")
 	@MaxLength(value=50, msg="字段[名称]超出最大长度[50]")
	private java.lang.String name;
    /**
     * 占比权重       db_column: weight 
     */	
 	@NotNull(msg="占比权重不能为空")
 	@MaxLength(value=5, msg="字段[占比权重]超出最大长度[5]")
	private java.math.BigDecimal weight;
	//columns END

	public TevglEmpiricalType(){
	}

	public TevglEmpiricalType(
		java.lang.String typeId
	){
		this.typeId = typeId;
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
	public void setWeight(java.math.BigDecimal value) {
		this.weight = value;
	}
	public java.math.BigDecimal getWeight() {
		return this.weight;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

