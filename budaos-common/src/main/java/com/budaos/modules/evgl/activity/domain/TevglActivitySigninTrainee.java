package com.budaos.modules.evgl.activity.domain;

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

public class TevglActivitySigninTrainee extends BaseDomain<TevglActivitySigninTrainee>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglActivitySigninTrainee";
	public static final String ALIAS_ST_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_ACTIVITY_ID = "活动id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_SIGN_TIME = "签到时间";
	public static final String ALIAS_SIGN_TYPE = "签到类型(1普通签到2手势签到3手工登记)";
	public static final String ALIAS_FILE = "附件";
	public static final String ALIAS_NUMBER = "手势签到的数字顺序";
	public static final String ALIAS_SIGN_STATE = "签到状态1正常2迟到3早退4旷课5请假";
	public static final String ALIAS_AREA = "签到位置";
	public static final String ALIAS_AREA_TITLE = "areaTitle";
	

    /**
     * 主键id       db_column: st_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String stId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 活动id       db_column: activity_id 
     */	
 	@NotNull(msg="活动id不能为空")
 	@MaxLength(value=32, msg="字段[活动id]超出最大长度[32]")
	private java.lang.String activityId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 签到时间       db_column: sign_time 
     */	
 	@NotNull(msg="签到时间不能为空")
 	@MaxLength(value=19, msg="字段[签到时间]超出最大长度[19]")
	private java.lang.String signTime;
    /**
     * 签到类型(1普通签到2手势签到3手工登记)       db_column: sign_type 
     */	
 	@NotNull(msg="签到类型(1普通签到2手势签到3手工登记)不能为空")
 	@MaxLength(value=2, msg="字段[签到类型(1普通签到2手势签到3手工登记)]超出最大长度[2]")
	private java.lang.String signType;
    /**
     * 附件       db_column: file 
     */	
 	@NotNull(msg="附件不能为空")
 	@MaxLength(value=500, msg="字段[附件]超出最大长度[500]")
	private java.lang.String file;
    /**
     * 手势签到的数字顺序       db_column: number 
     */	
 	@NotNull(msg="手势签到的数字顺序不能为空")
 	@MaxLength(value=100, msg="字段[手势签到的数字顺序]超出最大长度[100]")
	private java.lang.String number;
    /**
     * 签到状态1正常2迟到3早退4旷课5请假       db_column: sign_state 
     */	
 	@NotNull(msg="签到状态1正常2迟到3早退4旷课5请假不能为空")
 	@MaxLength(value=2, msg="字段[签到状态1正常2迟到3早退4旷课5请假]超出最大长度[2]")
	private java.lang.String signState;
 	/**
     * 签到具体位置       db_column: area 
     */	
 	//@NotNull(msg="签到位置不能为空")
 	@MaxLength(value=500, msg="字段[签到位置]超出最大长度[500]")
	private java.lang.String area;
 	/**
     * areaTitle       db_column: area_title 
     */	
 	@NotNull(msg="areaTitle不能为空")
 	@MaxLength(value=255, msg="字段[areaTitle]超出最大长度[255]")
	private java.lang.String areaTitle;
	//columns END
 	
 	private java.lang.String attachId;

	public TevglActivitySigninTrainee(){
	}

	public TevglActivitySigninTrainee(
		java.lang.String stId
	){
		this.stId = stId;
	}

	public void setStId(java.lang.String value) {
		this.stId = value;
	}
	public java.lang.String getStId() {
		return this.stId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setActivityId(java.lang.String value) {
		this.activityId = value;
	}
	public java.lang.String getActivityId() {
		return this.activityId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	
	public java.lang.String getSignTime() {
		return signTime;
	}

	public void setSignTime(java.lang.String signTime) {
		this.signTime = signTime;
	}

	public void setSignType(java.lang.String value) {
		this.signType = value;
	}
	public java.lang.String getSignType() {
		return this.signType;
	}
	public void setFile(java.lang.String value) {
		this.file = value;
	}
	public java.lang.String getFile() {
		return this.file;
	}
	public void setNumber(java.lang.String value) {
		this.number = value;
	}
	public java.lang.String getNumber() {
		return this.number;
	}
	public void setSignState(java.lang.String value) {
		this.signState = value;
	}
	public java.lang.String getSignState() {
		return this.signState;
	}
	
	private TevglActivitySigninInfo tevglActivitySigninInfo;
	
	public void setTevglActivitySigninInfo(TevglActivitySigninInfo tevglActivitySigninInfo){
		this.tevglActivitySigninInfo = tevglActivitySigninInfo;
	}
	
	public TevglActivitySigninInfo getTevglActivitySigninInfo() {
		return tevglActivitySigninInfo;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.String getAttachId() {
		return attachId;
	}

	public void setAttachId(java.lang.String attachId) {
		this.attachId = attachId;
	}

	public java.lang.String getArea() {
		return area;
	}

	public void setArea(java.lang.String area) {
		this.area = area;
	}

	public java.lang.String getAreaTitle() {
		return areaTitle;
	}

	public void setAreaTitle(java.lang.String areaTitle) {
		this.areaTitle = areaTitle;
	}
}

