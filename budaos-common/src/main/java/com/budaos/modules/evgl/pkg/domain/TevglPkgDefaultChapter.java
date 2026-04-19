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

public class TevglPkgDefaultChapter extends BaseDomain<TevglPkgDefaultChapter>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglPkgDefaultChapter";
	public static final String ALIAS_SE_ID = "主键id";
	public static final String ALIAS_PKG_ID = "教学包id";
	public static final String ALIAS_NAME = "名称";
	public static final String ALIAS_SORT_NUM = "排序号";
	

    /**
     * 主键id       db_column: se_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String seId;
    /**
     * 教学包id       db_column: pkg_id 
     */	
 	@NotNull(msg="教学包id不能为空")
 	@MaxLength(value=32, msg="字段[教学包id]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * 名称       db_column: name 
     */	
 	@NotNull(msg="名称不能为空")
 	@MaxLength(value=20, msg="字段[名称]超出最大长度[20]")
	private java.lang.String name;
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
	//columns END

	public TevglPkgDefaultChapter(){
	}

	public TevglPkgDefaultChapter(
		java.lang.String seId
	){
		this.seId = seId;
	}

	public void setSeId(java.lang.String value) {
		this.seId = value;
	}
	public java.lang.String getSeId() {
		return this.seId;
	}
	public void setPkgId(java.lang.String value) {
		this.pkgId = value;
	}
	public java.lang.String getPkgId() {
		return this.pkgId;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

