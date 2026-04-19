package com.budaos.modules.evgl.empirical.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalSetting;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglEmpiricalSettingService extends IBaseService<TevglEmpiricalSetting>{
	
	/**
	 * 经验值设置页面
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	R viewEmpiricalSetting(String ctId, String loginUserId);
	
	/**
	 * 保存规则设置
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveSessting(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 统一格式处理文本
	 * @param classroomName
	 * @param activityName
	 * @param empiricalValue
	 * @return
	 */
	String handleMessage(String classroomName, String activityName, Integer empiricalValue);
	
	/**
	 * 根据类型获取，课堂的各种经验值
	 * @param ctId
	 * @param dictCode 该值来源字典，亦可见EmpiricalValueEnum枚举类
	 * @return
	 */
	Integer getEmpiricalValueByMap(String ctId, String dictCode);
}