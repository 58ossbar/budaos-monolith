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

public class TevglSitePartner extends BaseDomain<TevglSitePartner>{
	private static final long serialVersionUID = 1L;
	 
	//alias
	public static final String TABLE_ALIAS = "TevglSitePartner";
	public static final String ALIAS_COMPANY_ID = "合作企业主键ID";
	public static final String ALIAS_COMPANY_NAME = "公司名称";
	public static final String ALIAS_COMPANY_WEBSITE = "公司网站";
	public static final String ALIAS_COMPANY_LOGO = "公司logo";
	public static final String ALIAS_COMPANY_ADDR = "公司具体地址";
	public static final String ALIAS_COOPERATION_TIME = "合作时间";
	public static final String ALIAS_COOPERATION_END_TIME = "合作结束时间";
	public static final String ALIAS_STATE = "状态(Y有效N无效)";
	public static final String ALIAS_COOPERATION_AGREEMENT = "合作框架协议";
	public static final String ALIAS_COMPANY_INTRO = "公司简介";
	public static final String ALIAS_COMPANY_DETAIL_INTRO = "企业详细介绍（编辑器）";
	public static final String ALIAS_COMPANY_EVALUATION = "企业评价";
	public static final String ALIAS_COMPANY_EVALUATION_PERSON = "企业评价人";
	public static final String ALIAS_COMPANY_LINKMAN = "联系人";
	public static final String ALIAS_LINKMAN_PHONE = "联系电话";
	public static final String ALIAS_COMPANY_SIZE = "企业规模";
	public static final String ALIAS_IS_PUBLIC_COMPANY = "是否上市公司(Y是N否)";
	public static final String ALIAS_IS_KEY_POINT = "是否重点企业Y是N否";
	

    /**
     * 合作企业主键ID       db_column: company_id 
     */	
 	@NotNull(msg="合作企业主键ID不能为空")
 	@MaxLength(value=32, msg="字段[合作企业主键ID]超出最大长度[32]")
	private java.lang.String companyId;
    /**
     * 公司名称       db_column: company_name 
     */	
 	@NotNull(msg="公司名称不能为空")
 	@MaxLength(value=30, msg="字段[公司名称]超出最大长度[30]")
	private java.lang.String companyName;
    /**
     * 公司网站       db_column: company_website 
     */	
 	@NotNull(msg="公司网站不能为空")
 	@MaxLength(value=30, msg="字段[公司网站]超出最大长度[30]")
	private java.lang.String companyWebsite;
    /**
     * 公司logo       db_column: company_logo 
     */	
 	//@NotNull(msg="公司logo不能为空")
 	@MaxLength(value=300, msg="字段[公司logo]超出最大长度[300]")
	private java.lang.String companyLogo;
    /**
     * 公司具体地址       db_column: company_addr 
     */	
 	//@NotNull(msg="公司具体地址不能为空")
 	@MaxLength(value=50, msg="字段[公司具体地址]超出最大长度[50]")
	private java.lang.String companyAddr;
    /**
     * 合作时间       db_column: cooperation_time 
     */	
 	//@NotNull(msg="合作时间不能为空")
 	@MaxLength(value=20, msg="字段[合作时间]超出最大长度[20]")
	private java.lang.String cooperationTime;
    /**
     * 合作结束时间       db_column: cooperation_end_time 
     */	
 	//@NotNull(msg="合作结束时间不能为空")
 	@MaxLength(value=20, msg="字段[合作结束时间]超出最大长度[20]")
	private java.lang.String cooperationEndTime;
    /**
     * 状态(Y有效N无效)       db_column: state 
     */	
 	//@NotNull(msg="状态(Y有效N无效)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y有效N无效)]超出最大长度[2]")
	private java.lang.String state;
    /**
     * 合作框架协议       db_column: cooperation_agreement 
     */	
 	//@NotNull(msg="合作框架协议不能为空")
 	@MaxLength(value=2147483647, msg="字段[合作框架协议]超出最大长度[2147483647]")
	private java.lang.String cooperationAgreement;
    /**
     * 公司简介       db_column: company_intro 
     */	
 	//@NotNull(msg="公司简介不能为空")
 	@MaxLength(value=200, msg="字段[公司简介]超出最大长度[200]")
	private java.lang.String companyIntro;
    /**
     * 企业详细介绍（编辑器）       db_column: company_detail_intro 
     */	
 	//@NotNull(msg="企业详细介绍（编辑器）不能为空")
 	@MaxLength(value=2147483647, msg="字段[企业详细介绍（编辑器）]超出最大长度[2147483647]")
	private java.lang.String companyDetailIntro;
    /**
     * 企业评价       db_column: company_evaluation 
     */	
 	//@NotNull(msg="企业评价不能为空")
 	@MaxLength(value=500, msg="字段[企业评价]超出最大长度[500]")
	private java.lang.String companyEvaluation;
    /**
     * 企业评价人       db_column: company_evaluation_person 
     */	
 	//@NotNull(msg="企业评价人不能为空")
 	@MaxLength(value=8, msg="字段[企业评价人]超出最大长度[8]")
	private java.lang.String companyEvaluationPerson;
    /**
     * 联系人       db_column: company_linkman 
     */	
 	//@NotNull(msg="联系人不能为空")
 	@MaxLength(value=8, msg="字段[联系人]超出最大长度[8]")
	private java.lang.String companyLinkman;
    /**
     * 联系电话       db_column: linkman_phone 
     */	
 	//@NotNull(msg="联系电话不能为空")
 	@MaxLength(value=20, msg="字段[联系电话]超出最大长度[20]")
	private java.lang.String linkmanPhone;
    /**
     * 企业规模       db_column: company_size 
     */	
 	//@NotNull(msg="企业规模不能为空")
 	@MaxLength(value=10, msg="字段[企业规模]超出最大长度[10]")
	private java.lang.Integer companySize;
    /**
     * 是否上市公司(Y是N否)       db_column: is_public_company 
     */	
 	//@NotNull(msg="是否上市公司(Y是N否)不能为空")
 	@MaxLength(value=2, msg="字段[是否上市公司(Y是N否)]超出最大长度[2]")
	private java.lang.String isPublicCompany;
    /**
     * 是否重点企业Y是N否       db_column: is_key_point 
     */	
 	//@NotNull(msg="是否重点企业Y是N否不能为空")
 	@MaxLength(value=2, msg="字段[是否重点企业Y是N否]超出最大长度[2]")
	private java.lang.String isKeyPoint;
	//columns END

	public TevglSitePartner(){
	}

	public TevglSitePartner(
		java.lang.String companyId
	){
		this.companyId = companyId;
	}

	public void setCompanyId(java.lang.String value) {
		this.companyId = value;
	}
	public java.lang.String getCompanyId() {
		return this.companyId;
	}
	public void setCompanyName(java.lang.String value) {
		this.companyName = value;
	}
	public java.lang.String getCompanyName() {
		return this.companyName;
	}
	public void setCompanyWebsite(java.lang.String value) {
		this.companyWebsite = value;
	}
	public java.lang.String getCompanyWebsite() {
		return this.companyWebsite;
	}
	public void setCompanyLogo(java.lang.String value) {
		this.companyLogo = value;
	}
	public java.lang.String getCompanyLogo() {
		return this.companyLogo;
	}
	public void setCompanyAddr(java.lang.String value) {
		this.companyAddr = value;
	}
	public java.lang.String getCompanyAddr() {
		return this.companyAddr;
	}
	public void setCooperationTime(java.lang.String value) {
		this.cooperationTime = value;
	}
	public java.lang.String getCooperationTime() {
		return this.cooperationTime;
	}
	public void setCooperationEndTime(java.lang.String value) {
		this.cooperationEndTime = value;
	}
	public java.lang.String getCooperationEndTime() {
		return this.cooperationEndTime;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setCooperationAgreement(java.lang.String value) {
		this.cooperationAgreement = value;
	}
	public java.lang.String getCooperationAgreement() {
		return this.cooperationAgreement;
	}
	public void setCompanyIntro(java.lang.String value) {
		this.companyIntro = value;
	}
	public java.lang.String getCompanyIntro() {
		return this.companyIntro;
	}
	public void setCompanyDetailIntro(java.lang.String value) {
		this.companyDetailIntro = value;
	}
	public java.lang.String getCompanyDetailIntro() {
		return this.companyDetailIntro;
	}
	public void setCompanyEvaluation(java.lang.String value) {
		this.companyEvaluation = value;
	}
	public java.lang.String getCompanyEvaluation() {
		return this.companyEvaluation;
	}
	public void setCompanyEvaluationPerson(java.lang.String value) {
		this.companyEvaluationPerson = value;
	}
	public java.lang.String getCompanyEvaluationPerson() {
		return this.companyEvaluationPerson;
	}
	public void setCompanyLinkman(java.lang.String value) {
		this.companyLinkman = value;
	}
	public java.lang.String getCompanyLinkman() {
		return this.companyLinkman;
	}
	public void setLinkmanPhone(java.lang.String value) {
		this.linkmanPhone = value;
	}
	public java.lang.String getLinkmanPhone() {
		return this.linkmanPhone;
	}
	public void setCompanySize(java.lang.Integer value) {
		this.companySize = value;
	}
	public java.lang.Integer getCompanySize() {
		return this.companySize;
	}
	public void setIsPublicCompany(java.lang.String value) {
		this.isPublicCompany = value;
	}
	public java.lang.String getIsPublicCompany() {
		return this.isPublicCompany;
	}
	public void setIsKeyPoint(java.lang.String value) {
		this.isKeyPoint = value;
	}
	public java.lang.String getIsKeyPoint() {
		return this.isKeyPoint;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

