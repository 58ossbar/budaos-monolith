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

public class TevglTchScheduleClass extends BaseDomain<TevglTchScheduleClass>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTchScheduleClass";
	public static final String ALIAS_SCHEDULE_ID = "scheduleId";
	public static final String ALIAS_CLASS_ID = "classId";
	

    /**
     * scheduleId       db_column: schedule_id 
     */	
 	@NotNull(msg="scheduleId不能为空")
 	@MaxLength(value=32, msg="字段[scheduleId]超出最大长度[32]")
	private java.lang.String scheduleId;
    /**
     * classId       db_column: class_id 
     */	
 	@NotNull(msg="classId不能为空")
 	@MaxLength(value=32, msg="字段[classId]超出最大长度[32]")
	private java.lang.String classId;
	//columns END

	public void setScheduleId(java.lang.String value) {
		this.scheduleId = value;
	}
	public java.lang.String getScheduleId() {
		return this.scheduleId;
	}
	public void setClassId(java.lang.String value) {
		this.classId = value;
	}
	public java.lang.String getClassId() {
		return this.classId;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

