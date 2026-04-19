package com.budaos.modules.evgl.site.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 新闻资讯</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglSiteNews extends BaseDomain<TevglSiteNews>{
	private static final long serialVersionUID = 1L;
	 
	//alias
	public static final String TABLE_ALIAS = "TevglSiteNews";
	public static final String ALIAS_NEWSID = "资讯主键id";
	public static final String ALIAS_NEW_TITLE = "资讯标题";
	public static final String ALIAS_CONTENT = "资讯内容";
	public static final String ALIAS_VIEW_NUM = "浏览量";
	public static final String ALIAS_NEWS_LOGO = "资讯logo";
	public static final String ALIAS_STATUS = "状态1待审2已发布 3删除";
	public static final String ALIAS_RESOURCE = "来源";
	public static final String ALIAS_ADDRESS = "原文地址";
	public static final String ALIAS_NEWS_SUMMLAR = "新闻概要";
	public static final String ALIAS_NEWS_KEY = "news_key";
	public static final String ALIAS_DEPLOYED = "发布方式(1直接发布2定时发布3审核发布)";
	public static final String ALIAS_NOONE = "是否头条资讯";
	public static final String ALIAS_DEPLOY_USER_ID = "发布人";
	public static final String ALIAS_DEPLOY_TIME = "发布时间";
	public static final String ALIAS_ORG_ID = "所属社区";
	public static final String ALIAS_DEPLOY_URL = "发布地址";
	public static final String ALIAS_NEWS_NO = "资讯编号";
	public static final String ALIAS_CATEGORY_ID = "资讯分类";
	public static final String ALIAS_LANG_TYPE = "语言类型";
	public static final String ALIAS_DISPLAY = "是否展示";
	public static final String ALIAS_MENU_ID = "所属栏目";
	public static final String ALIAS_SCENE = "场景： 0布道师 1微信公众号";
	public static final String ALIAS_AUTHOR = "作者";
	public static final String ALIAS_PARENT_ID = "父级id";
	public static final String ALIAS_SORT_NUM = "排序号";
	

    /**
     * 资讯主键id       db_column: newsid 
     */	
 	@NotNull(msg="资讯主键id不能为空")
 	@MaxLength(value=32, msg="字段[资讯主键id]超出最大长度[32]")
	private String newsid;
    /**
     * 资讯标题       db_column: new_title 
     */	
 	@NotNull(msg="资讯标题不能为空")
 	@MaxLength(value=200, msg="字段[资讯标题]超出最大长度[200]")
	private String newTitle;
    /**
     * 资讯内容       db_column: content 
     */	
 	//@NotNull(msg="资讯内容不能为空")
 	@MaxLength(value=2147483647, msg="字段[资讯内容]超出最大长度[2147483647]")
	private String content;
    /**
     * 浏览量       db_column: view_num 
     */	
 	//@NotNull(msg="浏览量不能为空")
 	@MaxLength(value=10, msg="字段[浏览量]超出最大长度[10]")
	private Integer viewNum;
    /**
     * 资讯logo       db_column: news_logo 
     */	
 	//@NotNull(msg="资讯logo不能为空")
 	@MaxLength(value=300, msg="字段[资讯logo]超出最大长度[300]")
	private String newsLogo;
    /**
     * 状态1待审2已发布 3删除       db_column: status 
     */	
 	//@NotNull(msg="状态1待审2已发布 3删除不能为空")
 	@MaxLength(value=2, msg="字段[状态1待审2已发布 3删除]超出最大长度[2]")
	private String status;
    /**
     * 来源       db_column: resource 
     */	
 	//@NotNull(msg="来源不能为空")
 	@MaxLength(value=100, msg="字段[来源]超出最大长度[100]")
	private String resource;
    /**
     * 原文地址       db_column: address 
     */	
 	//@NotNull(msg="原文地址不能为空")
 	@MaxLength(value=200, msg="字段[原文地址]超出最大长度[200]")
	private String address;
    /**
     * 新闻概要       db_column: news_summlar 
     */	
 	//@NotNull(msg="新闻概要不能为空")
 	@MaxLength(value=2147483647, msg="字段[新闻概要]超出最大长度[2147483647]")
	private String newsSummlar;
    /**
     * news_key       db_column: news_key 
     */	
 	//@NotNull(msg="news_key不能为空")
 	@MaxLength(value=500, msg="字段[news_key]超出最大长度[500]")
	private String newsKey;
    /**
     * 发布方式(1直接发布2定时发布3审核发布)       db_column: deployed 
     */	
 	//@NotNull(msg="发布方式(1直接发布2定时发布3审核发布)不能为空")
 	@MaxLength(value=2, msg="字段[发布方式(1直接发布2定时发布3审核发布)]超出最大长度[2]")
	private String deployed;
    /**
     * 是否头条资讯       db_column: noone 
     */	
 	//@NotNull(msg="是否头条资讯不能为空")
 	@MaxLength(value=2, msg="字段[是否头条资讯]超出最大长度[2]")
	private String noone;
    /**
     * 发布人       db_column: deploy_user_id 
     */	
 	//@NotNull(msg="发布人不能为空")
 	@MaxLength(value=32, msg="字段[发布人]超出最大长度[32]")
	private String deployUserId;
    /**
     * 发布时间       db_column: deploy_time 
     */	
 	//@NotNull(msg="发布时间不能为空")
 	@MaxLength(value=20, msg="字段[发布时间]超出最大长度[20]")
	private String deployTime;
    /**
     * 所属社区       db_column: org_id 
     */	
 	//@NotNull(msg="所属社区不能为空")
 	@MaxLength(value=32, msg="字段[所属社区]超出最大长度[32]")
	private String orgId;
    /**
     * 发布地址       db_column: deploy_url 
     */	
 	//@NotNull(msg="发布地址不能为空")
 	@MaxLength(value=500, msg="字段[发布地址]超出最大长度[500]")
	private String deployUrl;
    /**
     * 资讯编号       db_column: news_no 
     */	
 	//@NotNull(msg="资讯编号不能为空")
 	@MaxLength(value=32, msg="字段[资讯编号]超出最大长度[32]")
	private String newsNo;
    /**
     * 资讯分类       db_column: category_id 
     */	
 	//@NotNull(msg="资讯分类不能为空")
 	@MaxLength(value=32, msg="字段[资讯分类]超出最大长度[32]")
	private String categoryId;
    /**
     * 语言类型       db_column: lang_type 
     */	
 	//@NotNull(msg="语言类型不能为空")
 	@MaxLength(value=10, msg="字段[语言类型]超出最大长度[10]")
	private String langType;
    /**
     * 是否展示       db_column: display 
     */	
 	//@NotNull(msg="是否展示不能为空")
 	@MaxLength(value=2, msg="字段[是否展示]超出最大长度[2]")
	private String display;
    /**
     * 所属栏目       db_column: menu_id 
     */	
 	//@NotNull(msg="所属栏目不能为空")
 	@MaxLength(value=32, msg="字段[所属栏目]超出最大长度[32]")
	private String menuId;
    /**
     * 场景： 0布道师 1微信公众号       db_column: scene 
     */	
 	//@NotNull(msg="场景： 0布道师 1微信公众号不能为空")
 	@MaxLength(value=10, msg="字段[场景： 0布道师 1微信公众号]超出最大长度[10]")
	private String scene;
    /**
     * 作者       db_column: author 
     */	
 	//@NotNull(msg="作者不能为空")
 	@MaxLength(value=20, msg="字段[作者]超出最大长度[20]")
	private String author;
    /**
     * 父级id       db_column: parent_id 
     */	
 	//@NotNull(msg="父级id不能为空")
 	@MaxLength(value=32, msg="字段[父级id]超出最大长度[32]")
	private String parentId;
	/**
	 * 排序号       db_column: sort_num
	 */
	//@NotNull(msg="排序号不能为空")
	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
 	
 	private String menuName;
 	private String orgName;
 	
 	@MaxLength(value=500, msg="字段[所属栏目]超出最大长度[500]")
	private String reason; // 用于新闻审核不通过时的理由
 	
 	/**
     * 类型       db_column: official_link_type
     */
    @MaxLength(value=20, msg="字段[类型]超出最大长度[20]")
    private String officialLinkType;
    
	//columns END

	public TevglSiteNews(){
	}

	public TevglSiteNews(
		String newsid
	){
		this.newsid = newsid;
	}

	public void setNewsid(String value) {
		this.newsid = value;
	}
	public String getNewsid() {
		return this.newsid;
	}
	public void setNewTitle(String value) {
		this.newTitle = value;
	}
	public String getNewTitle() {
		return this.newTitle;
	}
	public void setContent(String value) {
		this.content = value;
	}
	public String getContent() {
		return this.content;
	}
	public void setViewNum(Integer value) {
		this.viewNum = value;
	}
	public Integer getViewNum() {
		return this.viewNum;
	}
	public void setNewsLogo(String value) {
		this.newsLogo = value;
	}
	public String getNewsLogo() {
		return this.newsLogo;
	}
	public void setStatus(String value) {
		this.status = value;
	}
	public String getStatus() {
		return this.status;
	}
	public void setResource(String value) {
		this.resource = value;
	}
	public String getResource() {
		return this.resource;
	}
	public void setAddress(String value) {
		this.address = value;
	}
	public String getAddress() {
		return this.address;
	}
	public void setNewsSummlar(String value) {
		this.newsSummlar = value;
	}
	public String getNewsSummlar() {
		return this.newsSummlar;
	}
	public void setNewsKey(String value) {
		this.newsKey = value;
	}
	public String getNewsKey() {
		return this.newsKey;
	}
	public void setDeployed(String value) {
		this.deployed = value;
	}
	public String getDeployed() {
		return this.deployed;
	}
	public void setNoone(String value) {
		this.noone = value;
	}
	public String getNoone() {
		return this.noone;
	}
	public void setDeployUserId(String value) {
		this.deployUserId = value;
	}
	public String getDeployUserId() {
		return this.deployUserId;
	}
	public void setDeployTime(String value) {
		this.deployTime = value;
	}
	public String getDeployTime() {
		return this.deployTime;
	}
	public void setOrgId(String value) {
		this.orgId = value;
	}
	public String getOrgId() {
		return this.orgId;
	}
	public void setDeployUrl(String value) {
		this.deployUrl = value;
	}
	public String getDeployUrl() {
		return this.deployUrl;
	}
	public void setNewsNo(String value) {
		this.newsNo = value;
	}
	public String getNewsNo() {
		return this.newsNo;
	}
	public void setCategoryId(String value) {
		this.categoryId = value;
	}
	public String getCategoryId() {
		return this.categoryId;
	}
	public void setLangType(String value) {
		this.langType = value;
	}
	public String getLangType() {
		return this.langType;
	}
	public void setDisplay(String value) {
		this.display = value;
	}
	public String getDisplay() {
		return this.display;
	}
	public void setMenuId(String value) {
		this.menuId = value;
	}
	public String getMenuId() {
		return this.menuId;
	}
	public void setScene(String value) {
		this.scene = value;
	}
	public String getScene() {
		return this.scene;
	}
	public void setAuthor(String value) {
		this.author = value;
	}
	public String getAuthor() {
		return this.author;
	}
	public void setParentId(String value) {
		this.parentId = value;
	}
	public String getParentId() {
		return this.parentId;
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

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOfficialLinkType() {
		return officialLinkType;
	}

	public void setOfficialLinkType(String officialLinkType) {
		this.officialLinkType = officialLinkType;
	}
}

