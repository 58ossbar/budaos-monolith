package com.budaos.modules.evgl.medu.me.domain;

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

public class TmeduMediaAvd extends BaseDomain<TmeduMediaAvd>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TmeduMediaAvd";
	public static final String ALIAS_AVD_ID = "广告标识ID";
	public static final String ALIAS_AVD_TITLE = "广告标题";
	public static final String ALIAS_AVD_NUM = "排序号";
	public static final String ALIAS_AVD_STATE = "状态:已上线、已下线";
	public static final String ALIAS_AVD_BEGINTIME = "投放开始时间";
	public static final String ALIAS_AVD_ENDTIME = "投放结束时间";
	public static final String ALIAS_AVD_LINKHREF = "跳转链接";
	public static final String ALIAS_AVD_PICURL = "图片访问地址";
	public static final String ALIAS_REMARK = "备注";
	

    /**
     * 广告标识ID       db_column: avd_id 
     */	
 	@NotNull(msg="广告标识ID不能为空")
 	@MaxLength(value=32, msg="字段[广告标识ID]超出最大长度[32]")
	private java.lang.String avdId;
    /**
     * 广告标题       db_column: avd_title 
     */	
 	@NotNull(msg="广告标题不能为空")
 	@MaxLength(value=100, msg="字段[广告标题]超出最大长度[100]")
	private java.lang.String avdTitle;
    /**
     * 排序号       db_column: avd_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer avdNum;
    /**
     * 状态:已上线、已下线       db_column: avd_state 
     */	
 	@NotNull(msg="状态:已上线、已下线不能为空")
 	@MaxLength(value=32, msg="字段[状态:已上线、已下线]超出最大长度[32]")
	private java.lang.String avdState;
    /**
     * 投放开始时间       db_column: avd_begintime 
     */	
 	@NotNull(msg="投放开始时间不能为空")
 	@MaxLength(value=20, msg="字段[投放开始时间]超出最大长度[20]")
	private java.lang.String avdBegintime;
    /**
     * 投放结束时间       db_column: avd_endtime 
     */	
 	@NotNull(msg="投放结束时间不能为空")
 	@MaxLength(value=20, msg="字段[投放结束时间]超出最大长度[20]")
	private java.lang.String avdEndtime;
    /**
     * 跳转链接       db_column: avd_linkhref 
     */	
 	@NotNull(msg="跳转链接不能为空")
 	@MaxLength(value=100, msg="字段[跳转链接]超出最大长度[100]")
	private java.lang.String avdLinkhref;
    /**
     * 图片访问地址       db_column: avd_picurl 
     */	
 	@NotNull(msg="图片访问地址不能为空")
 	@MaxLength(value=300, msg="字段[图片访问地址]超出最大长度[300]")
	private java.lang.String avdPicurl;
    /**
     * 备注       db_column: remark 
     */	
// 	@NotNull(msg="备注不能为空")
 	@MaxLength(value=300, msg="字段[备注]超出最大长度[300]")
	private java.lang.String remark;
	/**
	 * 场景       db_column: scene
	 */
// 	@NotNull(msg="场景不能为空")
	@MaxLength(value=2, msg="字段[场景]超出最大长度[2]")
	private java.lang.String scene;
	//columns END

	public TmeduMediaAvd(){
	}

	public TmeduMediaAvd(
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
	public void setAvdPicurl(java.lang.String value) {
		this.avdPicurl = value;
	}
	public java.lang.String getAvdPicurl() {
		return this.avdPicurl;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	public java.lang.String getScene() {
		return scene;
	}
	public void setScene(java.lang.String scene) {
		this.scene = scene;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

