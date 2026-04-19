package com.budaos.modules.evgl.tch.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 课堂成员审核表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglTchClassroomTraineeCheck extends BaseDomain<TevglTchClassroomTraineeCheck>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchClassroomTraineeCheck";
	public static final String ALIAS_TC_ID = "主键id";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_TRAINEE_ID = "学员id";
	public static final String ALIAS_IS_PASS = "审核是否通过(Y/N)，通过才之后正式加入课堂成员表";
	

    /**
     * 主键id       db_column: tc_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String tcId;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=32, msg="字段[课堂id]超出最大长度[32]")
	private java.lang.String ctId;
    /**
     * 学员id       db_column: trainee_id 
     */	
 	@NotNull(msg="学员id不能为空")
 	@MaxLength(value=32, msg="字段[学员id]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 审核是否通过(Y/N)，通过才之后正式加入课堂成员表       db_column: is_pass 
     */	
 	@NotNull(msg="审核是否通过(Y/N)，通过才之后正式加入课堂成员表不能为空")
 	@MaxLength(value=2, msg="字段[审核是否通过(Y/N)，通过才之后正式加入课堂成员表]超出最大长度[2]")
	private java.lang.String isPass;
	//columns END

	public TevglTchClassroomTraineeCheck(){
	}

	public TevglTchClassroomTraineeCheck(
		java.lang.String tcId
	){
		this.tcId = tcId;
	}

	public void setTcId(java.lang.String value) {
		this.tcId = value;
	}
	public java.lang.String getTcId() {
		return this.tcId;
	}
	public void setCtId(java.lang.String value) {
		this.ctId = value;
	}
	public java.lang.String getCtId() {
		return this.ctId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setIsPass(java.lang.String value) {
		this.isPass = value;
	}
	public java.lang.String getIsPass() {
		return this.isPass;
	}
	
	private TevglTchClassroom tevglTchClassroom;
	
	public void setTevglTchClassroom(TevglTchClassroom tevglTchClassroom){
		this.tevglTchClassroom = tevglTchClassroom;
	}
	
	public TevglTchClassroom getTevglTchClassroom() {
		return tevglTchClassroom;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

