package com.budaos.modules.evgl.site.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
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

public class TevglSiteAvd extends BaseDomain<TevglSiteAvd>{
	private static final long serialVersionUID = 1L;
	 
	//alias
	public static final String TABLE_ALIAS = "TevglSiteAvd";
	public static final String ALIAS_AVD_ID = "广告主键ID";
	public static final String ALIAS_MENU_ID = "所属栏目ID";
	public static final String ALIAS_AVD_TITLE = "广告主题";
	public static final String ALIAS_AVD_NUM = "排序号";
	public static final String ALIAS_AVD_STATE = "状态(Y已上线N已下线)";
	public static final String ALIAS_AVD_BEGINTIME = "投放开始时间";
	public static final String ALIAS_AVD_ENDTIME = "投放结束时间";
	public static final String ALIAS_AVD_LINKHREF = "跳转链接";
	public static final String ALIAS_AVD_PICURL_PC = "电脑端图片访问地址";
	public static final String ALIAS_AVD_PICURL_MOBILE = "手机端图片访问地址";
	public static final String ALIAS_AVD_POSITION = "投放位置(东南西北组合)";
	public static final String ALIAS_CONTENT = "广告内容";
	public static final String ALIAS_DEPLOY_USER_ID = "发布人";
	public static final String ALIAS_DEPLOY_TIME = "发布时间";
	public static final String ALIAS_REMARK = "备注";
	

    /**
     * 广告主键ID       db_column: avd_id 
     */	
 	//@NotNull(msg="广告主键ID不能为空")
 	@MaxLength(value=32, msg="字段[广告主键ID]超出最大长度[32]")
	private java.lang.String avdId;
    /**
     * 所属栏目ID       db_column: menu_id 
     */	
 	//@NotNull(msg="所属栏目ID不能为空")
 	@MaxLength(value=32, msg="字段[所属栏目ID]超出最大长度[32]")
	private java.lang.String menuId;
    /**
     * 广告主题       db_column: avd_title 
     */	
 	//@NotNull(msg="广告主题不能为空")
 	@MaxLength(value=100, msg="字段[广告主题]超出最大长度[100]")
	private java.lang.String avdTitle;
    /**
     * 排序号       db_column: avd_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer avdNum;
    /**
     * 状态(Y已上线N已下线)       db_column: avd_state 
     */	
 	//@NotNull(msg="状态(Y已上线N已下线)不能为空")
 	@MaxLength(value=2, msg="字段[状态(Y已上线N已下线)]超出最大长度[2]")
	private java.lang.String avdState;
    /**
     * 投放开始时间       db_column: avd_begintime 
     */	
 	//@NotNull(msg="投放开始时间不能为空")
 	@MaxLength(value=20, msg="字段[投放开始时间]超出最大长度[20]")
	private java.lang.String avdBegintime;
    /**
     * 投放结束时间       db_column: avd_endtime 
     */	
 	//@NotNull(msg="投放结束时间不能为空")
 	@MaxLength(value=20, msg="字段[投放结束时间]超出最大长度[20]")
	private java.lang.String avdEndtime;
    /**
     * 跳转链接       db_column: avd_linkhref 
     */	
 	//@NotNull(msg="跳转链接不能为空")
 	@MaxLength(value=100, msg="字段[跳转链接]超出最大长度[100]")
	private java.lang.String avdLinkhref;
    /**
     * 电脑端图片访问地址       db_column: avd_picurl_pc 
     */	
 	//@NotNull(msg="电脑端图片访问地址不能为空")
 	@MaxLength(value=300, msg="字段[电脑端图片访问地址]超出最大长度[300]")
	private java.lang.String avdPicurlPc;
    /**
     * 手机端图片访问地址       db_column: avd_picurl_mobile 
     */	
 	//@NotNull(msg="手机端图片访问地址不能为空")
 	@MaxLength(value=300, msg="字段[手机端图片访问地址]超出最大长度[300]")
	private java.lang.String avdPicurlMobile;
    /**
     * 投放位置(东南西北组合)       db_column: avd_position 
     */	
 	//@NotNull(msg="投放位置(东南西北组合)不能为空")
 	@MaxLength(value=32, msg="字段[投放位置(东南西北组合)]超出最大长度[32]")
	private java.lang.String avdPosition;
    /**
     * 广告内容       db_column: content 
     */	
 	//@NotNull(msg="广告内容不能为空")
 	@MaxLength(value=2147483647, msg="字段[广告内容]超出最大长度[2147483647]")
	private java.lang.String content;
    /**
     * 发布人       db_column: deploy_user_id 
     */	
 	//@NotNull(msg="发布人不能为空")
 	@MaxLength(value=32, msg="字段[发布人]超出最大长度[32]")
	private java.lang.String deployUserId;
    /**
     * 发布时间       db_column: deploy_time 
     */	
 	//@NotNull(msg="发布时间不能为空")
 	@MaxLength(value=20, msg="字段[发布时间]超出最大长度[20]")
	private java.lang.String deployTime;
    /**
     * 备注       db_column: remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=500, msg="字段[备注]超出最大长度[500]")
	private java.lang.String remark;

 	private String menuName;
 	private String orgId;
 	
	//columns END

	public TevglSiteAvd(){
	}

	public TevglSiteAvd(
		java.lang.String avdId
	){
		this.avdId = avdId;
	}

	public void setAvdId(java.lang.String value) {
		this.avdId = value;
	}
	public java.lang.String getAvdId() {
		return this.avdId;
	}
	public void setMenuId(java.lang.String value) {
		this.menuId = value;
	}
	public java.lang.String getMenuId() {
		return this.menuId;
	}
	public void setAvdTitle(java.lang.String value) {
		this.avdTitle = value;
	}
	public java.lang.String getAvdTitle() {
		return this.avdTitle;
	}
	public void setAvdNum(java.lang.Integer value) {
		this.avdNum = value;
	}
	public java.lang.Integer getAvdNum() {
		return this.avdNum;
	}
	public void setAvdState(java.lang.String value) {
		this.avdState = value;
	}
	public java.lang.String getAvdState() {
		return this.avdState;
	}
	public void setAvdBegintime(java.lang.String value) {
		this.avdBegintime = value;
	}
	public java.lang.String getAvdBegintime() {
		return this.avdBegintime;
	}
	public void setAvdEndtime(java.lang.String value) {
		this.avdEndtime = value;
	}
	public java.lang.String getAvdEndtime() {
		return this.avdEndtime;
	}
	public void setAvdLinkhref(java.lang.String value) {
		this.avdLinkhref = value;
	}
	public java.lang.String getAvdLinkhref() {
		return this.avdLinkhref;
	}
	public void setAvdPicurlPc(java.lang.String value) {
		this.avdPicurlPc = value;
	}
	public java.lang.String getAvdPicurlPc() {
		return this.avdPicurlPc;
	}
	public void setAvdPicurlMobile(java.lang.String value) {
		this.avdPicurlMobile = value;
	}
	public java.lang.String getAvdPicurlMobile() {
		return this.avdPicurlMobile;
	}
	public void setAvdPosition(java.lang.String value) {
		this.avdPosition = value;
	}
	public java.lang.String getAvdPosition() {
		return this.avdPosition;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setDeployUserId(java.lang.String value) {
		this.deployUserId = value;
	}
	public java.lang.String getDeployUserId() {
		return this.deployUserId;
	}
	public void setDeployTime(java.lang.String value) {
		this.deployTime = value;
	}
	public java.lang.String getDeployTime() {
		return this.deployTime;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}

