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

public class TevglTchScheduleBetween extends BaseDomain<TevglTchScheduleBetween>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchScheduleBetween";
	public static final String ALIAS_TIME_QUANTUM_ID = "主键id";
	public static final String ALIAS_NAME = "名称，示例值：第一大节课";
	public static final String ALIAS_BEGIN_TIME = "开始时间，示例值：08:30";
	public static final String ALIAS_END_TIME = "结束时间，示例值：10:00";
	public static final String ALIAS_TYPE = "类别(夏季summer、冬季winter)";
	public static final String ALIAS_SORT_NUM = "排序号";
	

    /**
     * 主键id       db_column: time_quantum_id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String timeQuantumId;
    /**
     * 名称，示例值：第一大节课       db_column: name 
     */	
 	@NotNull(msg="名称，示例值：第一大节课不能为空")
 	@MaxLength(value=10, msg="字段[名称，示例值：第一大节课]超出最大长度[10]")
	private java.lang.String name;
    /**
     * 开始时间，示例值：08:30       db_column: begin_time 
     */	
 	@NotNull(msg="开始时间，示例值：08:30不能为空")
 	@MaxLength(value=10, msg="字段[开始时间，示例值：08:30]超出最大长度[10]")
	private java.lang.String beginTime;
    /**
     * 结束时间，示例值：10:00       db_column: end_time 
     */	
 	@NotNull(msg="结束时间，示例值：10:00不能为空")
 	@MaxLength(value=10, msg="字段[结束时间，示例值：10:00]超出最大长度[10]")
	private java.lang.String endTime;
    /**
     * 类别(夏季summer、冬季winter)       db_column: type 
     */	
 	@NotNull(msg="类别(夏季summer、冬季winter)不能为空")
 	@MaxLength(value=10, msg="字段[类别(夏季summer、冬季winter)]超出最大长度[10]")
	private java.lang.String type;
    /**
     * 排序号       db_column: sort_num 
     */	
 	@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
	//columns END
 	
 	private TevglTchSchedule tevglTchSchedule;
 	
 	private String id;

	public TevglTchScheduleBetween(){
	}

	public TevglTchScheduleBetween(
		java.lang.String timeQuantumId
	){
		this.timeQuantumId = timeQuantumId;
	}

	public void setTimeQuantumId(java.lang.String value) {
		this.timeQuantumId = value;
	}
	public java.lang.String getTimeQuantumId() {
		return this.timeQuantumId;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setBeginTime(java.lang.String value) {
		this.beginTime = value;
	}
	public java.lang.String getBeginTime() {
		return this.beginTime;
	}
	public void setEndTime(java.lang.String value) {
		this.endTime = value;
	}
	public java.lang.String getEndTime() {
		return this.endTime;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TevglTchSchedule getTevglTchSchedule() {
		return tevglTchSchedule;
	}

	public void setTevglTchSchedule(TevglTchSchedule tevglTchSchedule) {
		this.tevglTchSchedule = tevglTchSchedule;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

