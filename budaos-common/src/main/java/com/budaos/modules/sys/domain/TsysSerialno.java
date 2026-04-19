/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

package com.budaos.modules.sys.domain;

//import javax.validation.constraints.Min;
//import org.hibernate.validator.constraints.NotBlank;
//import com.budaos.common.cbsecurity.validator.group.AddGroup;
//import com.budaos.common.cbsecurity.validator.group.UpdateGroup;
import com.budaos.core.baseclass.domain.BaseDomain;


public class TsysSerialno extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TsysSerialno";
	public static final String ALIAS_SERIALNO_ID = "serialnoId";
	public static final String ALIAS_SERIAL_NAME = "名称";
	public static final String ALIAS_SECOUND_NAME = "字段名";
	public static final String ALIAS_FORMULAR_REGX = "规则 {YYYY}{MM}{DD}-{NO}  NO:000001开始";
	public static final String ALIAS_CREATE_TYPE = "生成方式";
	public static final String ALIAS_SERIAL_LENGTH = "流水号长度";
	public static final String ALIAS_STEP = "步长";
	public static final String ALIAS_INIT_VALUE = "初始值";
	public static final String ALIAS_CURRENT_VALUE = "当前值";
	public static final String ALIAS_REMARK = "备注说明";
	public static final String ALIAS_TAB_NAME = "表名";
	public static final String ALIAS_SFBL = "是否补零";
	

    /**
     * serialnoId       db_column: SERIALNO_ID 
     */	
	private java.lang.String serialnoId;
	private java.lang.String id;
	
    /**
     * 名称       db_column: SERIAL_NAME 
     */	
	//@NotBlank(message = ALIAS_SERIAL_NAME +"不能为空",groups={AddGroup.class,UpdateGroup.class})
	private java.lang.String serialName;
    /**
     * 字段名       db_column: SECOUND_NAME 
     */	
	//@NotBlank(message = ALIAS_SECOUND_NAME +"不能为空",groups={AddGroup.class,UpdateGroup.class})
	private java.lang.String secoundName;
    /**
     * 规则 {yyyy}{MM}{DD}-{NO}  NO:000001开始       db_column: FORMULAR_REGX 
     */	
	//@NotBlank(message = "规则不能为空",groups={AddGroup.class,UpdateGroup.class})
	private java.lang.String formularRegx;
    /**
     * 生成方式       db_column: CREATE_TYPE 
     */	
	private java.lang.String createType;
    /**
     * 流水号长度       db_column: SERIAL_LENGTH 
     */	
	//@NotBlank(message = ALIAS_SERIAL_LENGTH +"不能为空",groups={AddGroup.class,UpdateGroup.class})
	//@Min(value=1,message=ALIAS_SERIAL_LENGTH + "必须大于{value}")
	private java.lang.Integer serialLength;
    /**
     * 步长       db_column: STEP 
     */	
	//@NotBlank(message = ALIAS_STEP +"不能为空",groups={AddGroup.class,UpdateGroup.class})
	private java.lang.Integer step;
    /**
     * 初始值       db_column: INIT_VALUE 
     */	
	//@NotBlank(message = ALIAS_INIT_VALUE +"不能为空",groups={AddGroup.class,UpdateGroup.class})
	private java.lang.String initValue;
    /**
     * 当前值       db_column: CURRENT_VALUE 
     */	
	//@NotBlank(message = ALIAS_CURRENT_VALUE +"不能为空",groups={AddGroup.class,UpdateGroup.class})
	private java.lang.String currentValue;
    /**
     * 备注说明       db_column: REMARK 
     */	
	private java.lang.String remark;
    /**
     * 表名       db_column: TAB_NAME 
     */	
	//@NotBlank(message = ALIAS_TAB_NAME +"不能为空",groups={AddGroup.class,UpdateGroup.class})
	private java.lang.String tabName;
    /**
     * 是否补零       db_column: SFBL 
     */	
	private java.lang.String sfbl;
	//columns END

	public TsysSerialno(){
	}

	public TsysSerialno(
		java.lang.String serialnoId
	){
		this.serialnoId = serialnoId;
	}

	public void setSerialnoId(java.lang.String value) {
		this.serialnoId = value;
	}
	
	public java.lang.String getSerialnoId() {
		return this.serialnoId;
	}
	public void setSerialName(java.lang.String value) {
		this.serialName = value;
	}
	
	public java.lang.String getSerialName() {
		return this.serialName;
	}
	public void setSecoundName(java.lang.String value) {
		this.secoundName = value;
	}
	
	public java.lang.String getSecoundName() {
		return this.secoundName;
	}
	public void setFormularRegx(java.lang.String value) {
		this.formularRegx = value;
	}
	
	public java.lang.String getFormularRegx() {
		return this.formularRegx;
	}
	public void setCreateType(java.lang.String value) {
		this.createType = value;
	}
	
	public java.lang.String getCreateType() {
		return this.createType;
	}
	public void setSerialLength(java.lang.Integer value) {
		this.serialLength = value;
	}
	
	public java.lang.Integer getSerialLength() {
		return this.serialLength;
	}
	public void setStep(java.lang.Integer value) {
		this.step = value;
	}
	
	public java.lang.Integer getStep() {
		return this.step;
	}
	public void setInitValue(java.lang.String value) {
		this.initValue = value;
	}
	
	public java.lang.String getInitValue() {
		return this.initValue;
	}
	public void setCurrentValue(java.lang.String value) {
		this.currentValue = value;
	}
	
	public java.lang.String getCurrentValue() {
		return this.currentValue;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setTabName(java.lang.String value) {
		this.tabName = value;
	}
	
	public java.lang.String getTabName() {
		return this.tabName;
	}
	public void setSfbl(java.lang.String value) {
		this.sfbl = value;
	}
	
	public java.lang.String getSfbl() {
		return this.sfbl;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

}

