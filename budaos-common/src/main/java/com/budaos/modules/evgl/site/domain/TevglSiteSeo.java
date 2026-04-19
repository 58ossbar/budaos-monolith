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

public class TevglSiteSeo extends BaseDomain<TevglSiteSeo>{
	private static final long serialVersionUID = 1L;
	 
	//alias
	public static final String TABLE_ALIAS = "TevglSiteSeo";
	public static final String ALIAS_SEO_ID = "seoId";
	public static final String ALIAS_SEO_TITLE = "SEO标题";
	public static final String ALIAS_SEO_WORD = "SEO关键字：注意：多个SEO关键字以逗号隔开";
	public static final String ALIAS_SEO_DESC = "SEO描述";
	public static final String ALIAS_SEO_TYPE = "SEO分类:1、站点 2、栏目 3、内容";
	public static final String ALIAS_SEO_RELATION = "关联ID";
	

    /**
     * seoId       db_column: seo_id 
     */	
 	@NotNull(msg="seoId不能为空")
 	@MaxLength(value=32, msg="字段[seoId]超出最大长度[32]")
	private java.lang.String seoId;
    /**
     * SEO标题       db_column: seo_title 
     */	
 	//@NotNull(msg="SEO标题不能为空")
 	@MaxLength(value=300, msg="字段[SEO标题]超出最大长度[300]")
	private java.lang.String seoTitle;
    /**
     * SEO关键字：注意：多个SEO关键字以逗号隔开       db_column: seo_word 
     */	
 	//@NotNull(msg="SEO关键字：注意：多个SEO关键字以逗号隔开不能为空")
 	@MaxLength(value=1000, msg="字段[SEO关键字：注意：多个SEO关键字以逗号隔开]超出最大长度[1000]")
	private java.lang.String seoWord;
    /**
     * SEO描述       db_column: seo_desc 
     */	
 	//@NotNull(msg="SEO描述不能为空")
 	@MaxLength(value=500, msg="字段[SEO描述]超出最大长度[500]")
	private java.lang.String seoDesc;
    /**
     * SEO分类:1、站点 2、栏目 3、内容       db_column: seo_type 
     */	
 	//@NotNull(msg="SEO分类:1、站点 2、栏目 3、内容不能为空")
 	@MaxLength(value=2, msg="字段[SEO分类:1、站点 2、栏目 3、内容]超出最大长度[2]")
	private java.lang.String seoType;
    /**
     * 关联ID       db_column: seo_relation 
     */	
 	//@NotNull(msg="关联ID不能为空")
 	@MaxLength(value=32, msg="字段[关联ID]超出最大长度[32]")
	private java.lang.String seoRelation;
	//columns END

	public TevglSiteSeo(){
	}

	public TevglSiteSeo(
		java.lang.String seoId
	){
		this.seoId = seoId;
	}

	public void setSeoId(java.lang.String value) {
		this.seoId = value;
	}
	public java.lang.String getSeoId() {
		return this.seoId;
	}
	public void setSeoTitle(java.lang.String value) {
		this.seoTitle = value;
	}
	public java.lang.String getSeoTitle() {
		return this.seoTitle;
	}
	public void setSeoWord(java.lang.String value) {
		this.seoWord = value;
	}
	public java.lang.String getSeoWord() {
		return this.seoWord;
	}
	public void setSeoDesc(java.lang.String value) {
		this.seoDesc = value;
	}
	public java.lang.String getSeoDesc() {
		return this.seoDesc;
	}
	public void setSeoType(java.lang.String value) {
		this.seoType = value;
	}
	public java.lang.String getSeoType() {
		return this.seoType;
	}
	public void setSeoRelation(java.lang.String value) {
		this.seoRelation = value;
	}
	public java.lang.String getSeoRelation() {
		return this.seoRelation;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

