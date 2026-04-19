package com.budaos.modules.evgl.tch.domain;

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

public class TevglTchScheduleTrainingRoom extends BaseDomain<TevglTchScheduleTrainingRoom>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchScheduleTrainingRoom";
	public static final String ALIAS_ROOM_ID = "主键id";
	public static final String ALIAS_ORG_ID = "关联所属机构";
	public static final String ALIAS_ROOM_NAME = "实训室名称";
	public static final String ALIAS_ROOM_NUMBER = "实训室编号";
	public static final String ALIAS_STATE = "状态(Y/N)";
	public static final String ALIAS_REMARK = "备注";
	public static final String ALIAS_IMAGE = "image";
	

    /**
     * 主键id       db_column: room_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String roomId;
    /**
     * 关联所属机构       db_column: org_id 
     */	
 	@NotNull(msg="关联所属机构不能为空")
 	@MaxLength(value=32, msg="字段[关联所属机构]超出最大长度[32]")
	private java.lang.String orgId;
    /**
     * 实训室名称       db_column: room_name 
     */	
 	@NotNull(msg="实训室名称不能为空")
 	@MaxLength(value=20, msg="字段[实训室名称]超出最大长度[20]")
	private java.lang.String roomName;
    /**
     * 实训室编号       db_column: room_number 
     */	
 	@NotNull(msg="实训室编号不能为空")
 	@MaxLength(value=20, msg="字段[实训室编号]超出最大长度[20]")
	private java.lang.String roomNumber;
    /**
     * 状态(Y/N)       db_column: state 
     */	
 	@NotNull(msg="状态(Y/N)不能为空")
 	@MaxLength(value=1, msg="字段[状态(Y/N)]超出最大长度[1]")
	private java.lang.String state;
    /**
     * 备注       db_column: remark 
     */	
 	@NotNull(msg="备注不能为空")
 	@MaxLength(value=500, msg="字段[备注]超出最大长度[500]")
	private java.lang.String remark;
    /**
     * image       db_column: image 
     */	
 	@NotNull(msg="image不能为空")
 	@MaxLength(value=500, msg="字段[image]超出最大长度[500]")
	private java.lang.String image;
	//columns END

	public TevglTchScheduleTrainingRoom(){
	}

	public TevglTchScheduleTrainingRoom(
		java.lang.String roomId
	){
		this.roomId = roomId;
	}

	public void setRoomId(java.lang.String value) {
		this.roomId = value;
	}
	public java.lang.String getRoomId() {
		return this.roomId;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setRoomName(java.lang.String value) {
		this.roomName = value;
	}
	public java.lang.String getRoomName() {
		return this.roomName;
	}
	public void setRoomNumber(java.lang.String value) {
		this.roomNumber = value;
	}
	public java.lang.String getRoomNumber() {
		return this.roomNumber;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setImage(java.lang.String value) {
		this.image = value;
	}
	public java.lang.String getImage() {
		return this.image;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

