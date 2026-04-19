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

public class TevglSiteResourceext extends BaseDomain<TevglSiteResourceext>{
	private static final long serialVersionUID = 1L;
	 
	//alias
	public static final String TABLE_ALIAS = "TevglSiteResourceext";
	public static final String ALIAS_SITE_ID = "站点ID";
	public static final String ALIAS_MENU_ID = "资源ID";
	public static final String ALIAS_SITE_PRIMARY = "是否主站";
	public static final String ALIAS_SITE_HTTP = "站点http";
	public static final String ALIAS_TRADE_TYPE = "行业分类，取值于字典，例如IT互联网类、电商类、餐饮类、运动类等";
	public static final String ALIAS_SITE_LOGO = "站点logo";
	public static final String ALIAS_SITE_URLICON = "地址栏icon";
	public static final String ALIAS_STATUS = "状态：正常、禁用";
	public static final String ALIAS_ISAUDIT = "审核发布:1、直接发布；2、审核发布；3、定时发布";
	public static final String ALIAS_MOBILE_PHONE = "移动电话";
	public static final String ALIAS_EMAIL = "邮箱";
	public static final String ALIAS_ADDRESS = "公司地址";
	public static final String ALIAS_CORPORATE_NAME = "公司名称";
	public static final String ALIAS_WECHAT_URL = "公众号二维码URL";
	public static final String ALIAS_PHONE = "固定电话，格式为：区号-电话-分机号";
	public static final String ALIAS_MANAGER = "负责人";
	

    /**
     * 站点ID       db_column: site_id 
     */	
 	@NotNull(msg="站点ID不能为空")
 	@MaxLength(value=32, msg="字段[站点ID]超出最大长度[32]")
	private java.lang.String siteId;
    /**
     * 资源ID       db_column: MENU_ID 
     */	
 	//@NotNull(msg="资源ID不能为空")
 	@MaxLength(value=32, msg="字段[资源ID]超出最大长度[32]")
	private java.lang.String menuId;
    /**
     * 是否主站       db_column: site_primary 
     */	
 	//@NotNull(msg="是否主站不能为空")
 	@MaxLength(value=2, msg="字段[是否主站]超出最大长度[2]")
	private java.lang.String sitePrimary;
    /**
     * 站点http       db_column: site_http 
     */	
 	//@NotNull(msg="站点http不能为空")
 	@MaxLength(value=500, msg="字段[站点http]超出最大长度[500]")
	private java.lang.String siteHttp;
    /**
     * 行业分类，取值于字典，例如IT互联网类、电商类、餐饮类、运动类等       db_column: trade_type 
     */	
 	//@NotNull(msg="行业分类，取值于字典，例如IT互联网类、电商类、餐饮类、运动类等不能为空")
 	@MaxLength(value=32, msg="字段[行业分类，取值于字典，例如IT互联网类、电商类、餐饮类、运动类等]超出最大长度[32]")
	private java.lang.String tradeType;
    /**
     * 站点logo       db_column: site_logo 
     */	
 	//@NotNull(msg="站点logo不能为空")
 	@MaxLength(value=300, msg="字段[站点logo]超出最大长度[300]")
	private java.lang.String siteLogo;
    /**
     * 地址栏icon       db_column: site_urlicon 
     */	
 	//@NotNull(msg="地址栏icon不能为空")
 	@MaxLength(value=300, msg="字段[地址栏icon]超出最大长度[300]")
	private java.lang.String siteUrlicon;
    /**
     * 状态：正常、禁用       db_column: status 
     */	
 	//@NotNull(msg="状态：正常、禁用不能为空")
 	@MaxLength(value=32, msg="字段[状态：正常、禁用]超出最大长度[32]")
	private java.lang.String status;
    /**
     * 审核发布:1、直接发布；2、审核发布；3、定时发布       db_column: isaudit 
     */	
 	//@NotNull(msg="审核发布:1、直接发布；2、审核发布；3、定时发布不能为空")
 	@MaxLength(value=4, msg="字段[审核发布:1、直接发布；2、审核发布；3、定时发布]超出最大长度[4]")
	private java.lang.String isaudit;
    /**
     * 移动电话       db_column: mobile_phone 
     */	
 	//@NotNull(msg="移动电话不能为空")
 	@MaxLength(value=20, msg="字段[移动电话]超出最大长度[20]")
	private java.lang.String mobilePhone;
    /**
     * 邮箱       db_column: email 
     */	
 	//@NotNull(msg="邮箱不能为空")
 	@MaxLength(value=200, msg="字段[邮箱]超出最大长度[200]")
	private java.lang.String email;
    /**
     * 公司地址       db_column: address 
     */	
 	//@NotNull(msg="公司地址不能为空")
 	@MaxLength(value=100, msg="字段[公司地址]超出最大长度[100]")
	private java.lang.String address;
    /**
     * 公司名称       db_column: corporate_name 
     */	
 	//@NotNull(msg="公司名称不能为空")
 	@MaxLength(value=100, msg="字段[公司名称]超出最大长度[100]")
	private java.lang.String corporateName;
    /**
     * 公众号二维码URL       db_column: wechat_url 
     */	
 	//@NotNull(msg="公众号二维码URL不能为空")
 	@MaxLength(value=200, msg="字段[公众号二维码URL]超出最大长度[200]")
	private java.lang.String wechatUrl;
    /**
     * 固定电话，格式为：区号-电话-分机号       db_column: phone 
     */	
 	//@NotNull(msg="固定电话，格式为：区号-电话-分机号不能为空")
 	@MaxLength(value=30, msg="字段[固定电话，格式为：区号-电话-分机号]超出最大长度[30]")
	private java.lang.String phone;
    /**
     * 负责人       db_column: manager 
     */	
 	//@NotNull(msg="负责人不能为空")
 	@MaxLength(value=50, msg="字段[负责人]超出最大长度[50]")
	private java.lang.String manager;
	//columns END

	public TevglSiteResourceext(){
	}

	public TevglSiteResourceext(
		java.lang.String siteId
	){
		this.siteId = siteId;
	}

	public void setSiteId(java.lang.String value) {
		this.siteId = value;
	}
	public java.lang.String getSiteId() {
		return this.siteId;
	}
	public void setMenuId(java.lang.String value) {
		this.menuId = value;
	}
	public java.lang.String getMenuId() {
		return this.menuId;
	}
	public void setSitePrimary(java.lang.String value) {
		this.sitePrimary = value;
	}
	public java.lang.String getSitePrimary() {
		return this.sitePrimary;
	}
	public void setSiteHttp(java.lang.String value) {
		this.siteHttp = value;
	}
	public java.lang.String getSiteHttp() {
		return this.siteHttp;
	}
	public void setTradeType(java.lang.String value) {
		this.tradeType = value;
	}
	public java.lang.String getTradeType() {
		return this.tradeType;
	}
	public void setSiteLogo(java.lang.String value) {
		this.siteLogo = value;
	}
	public java.lang.String getSiteLogo() {
		return this.siteLogo;
	}
	public void setSiteUrlicon(java.lang.String value) {
		this.siteUrlicon = value;
	}
	public java.lang.String getSiteUrlicon() {
		return this.siteUrlicon;
	}
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	public java.lang.String getStatus() {
		return this.status;
	}
	public void setIsaudit(java.lang.String value) {
		this.isaudit = value;
	}
	public java.lang.String getIsaudit() {
		return this.isaudit;
	}
	public void setMobilePhone(java.lang.String value) {
		this.mobilePhone = value;
	}
	public java.lang.String getMobilePhone() {
		return this.mobilePhone;
	}
	public void setEmail(java.lang.String value) {
		this.email = value;
	}
	public java.lang.String getEmail() {
		return this.email;
	}
	public void setAddress(java.lang.String value) {
		this.address = value;
	}
	public java.lang.String getAddress() {
		return this.address;
	}
	public void setCorporateName(java.lang.String value) {
		this.corporateName = value;
	}
	public java.lang.String getCorporateName() {
		return this.corporateName;
	}
	public void setWechatUrl(java.lang.String value) {
		this.wechatUrl = value;
	}
	public java.lang.String getWechatUrl() {
		return this.wechatUrl;
	}
	public void setPhone(java.lang.String value) {
		this.phone = value;
	}
	public java.lang.String getPhone() {
		return this.phone;
	}
	public void setManager(java.lang.String value) {
		this.manager = value;
	}
	public java.lang.String getManager() {
		return this.manager;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

