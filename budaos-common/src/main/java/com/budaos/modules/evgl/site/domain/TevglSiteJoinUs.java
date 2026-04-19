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

public class TevglSiteJoinUs extends BaseDomain<TevglSiteJoinUs>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglSiteJoinUs";
	public static final String ALIAS_ID = "主键id";
	public static final String ALIAS_USER_NAME = "姓名";
	public static final String ALIAS_COMPANY_NAME = "公司名称";
	public static final String ALIAS_POST_NAME = "职位名称";
	public static final String ALIAS_EMAIL = "邮箱";
	public static final String ALIAS_MOBILE = "电话";
	public static final String ALIAS_TYPE = "模式(1产品代理合作2解决方案合作3实施服务合作4战略生态合作)";
	public static final String ALIAS_HAS_TOUCH = "是否联系过(Y/N)";
	

    /**
     * 主键id       db_column: id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String id;
    /**
     * 姓名       db_column: user_name 
     */	
 	@NotNull(msg="姓名不能为空")
 	@MaxLength(value=50, msg="字段[姓名]超出最大长度[50]")
	private java.lang.String userName;
    /**
     * 公司名称       db_column: company_name 
     */	
 	@NotNull(msg="公司名称不能为空")
 	@MaxLength(value=100, msg="字段[公司名称]超出最大长度[100]")
	private java.lang.String companyName;
    /**
     * 职位名称       db_column: post_name 
     */	
 	@NotNull(msg="职位名称不能为空")
 	@MaxLength(value=50, msg="字段[职位名称]超出最大长度[50]")
	private java.lang.String postName;
    /**
     * 邮箱       db_column: email 
     */	
 	@NotNull(msg="邮箱不能为空")
 	@MaxLength(value=20, msg="字段[邮箱]超出最大长度[20]")
	private java.lang.String email;
    /**
     * 电话       db_column: mobile 
     */	
 	@NotNull(msg="电话不能为空")
 	@MaxLength(value=20, msg="字段[电话]超出最大长度[20]")
	private java.lang.String mobile;
    /**
     * 模式(1产品代理合作2解决方案合作3实施服务合作4战略生态合作)       db_column: type 
     */	
 	@NotNull(msg="模式(1产品代理合作2解决方案合作3实施服务合作4战略生态合作)不能为空")
 	@MaxLength(value=2, msg="字段[模式(1产品代理合作2解决方案合作3实施服务合作4战略生态合作)]超出最大长度[2]")
	private java.lang.String type;
    /**
     * 是否联系过(Y/N)       db_column: has_touch 
     */	
 	//@NotNull(msg="是否联系过(Y/N)不能为空")
 	@MaxLength(value=2, msg="字段[是否联系过(Y/N)]超出最大长度[2]")
	private java.lang.String hasTouch;
	//columns END

	public TevglSiteJoinUs(){
	}

	public TevglSiteJoinUs(
		java.lang.String id
	){
		this.id = id;
	}

	public void setId(java.lang.String value) {
		this.id = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setUserName(java.lang.String value) {
		this.userName = value;
	}
	public java.lang.String getUserName() {
		return this.userName;
	}
	public void setCompanyName(java.lang.String value) {
		this.companyName = value;
	}
	public java.lang.String getCompanyName() {
		return this.companyName;
	}
	public void setPostName(java.lang.String value) {
		this.postName = value;
	}
	public java.lang.String getPostName() {
		return this.postName;
	}
	public void setEmail(java.lang.String value) {
		this.email = value;
	}
	public java.lang.String getEmail() {
		return this.email;
	}
	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}
	public java.lang.String getMobile() {
		return this.mobile;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setHasTouch(java.lang.String value) {
		this.hasTouch = value;
	}
	public java.lang.String getHasTouch() {
		return this.hasTouch;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

