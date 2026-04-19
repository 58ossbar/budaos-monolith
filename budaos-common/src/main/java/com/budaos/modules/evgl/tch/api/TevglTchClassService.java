package com.budaos.modules.evgl.tch.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClass;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchClassService extends IBaseService<TevglTchClass>{

	/**
	 * 查询班级列表
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R queryClassListData(Map<String, Object> params, String loginUserId);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params);
	
	/**
	 * 获取机构年份班级树
	 * @param params
	 * @return
	 */
	R getClassTree(Map<String, Object> params);
	
	TevglTchClass selectObjectById(Object id);
	
	/**
	 * 根据条件查询班级列表，无分页
	 * @param params
	 * @return
	 */
	List<TevglTchClass> findClassList(Map<String, Object> params);
	
	/**
	 * 根据条件分页查询班级列表
	 * @param params
	 * @return
	 */
	R queryClassListForWeb(Map<String, Object> params);
	
	/**
	 * 从字典获取班级类型（1单科强化，2订单就业）
	 * @param params
	 * @return
	 */
	R getClassDictTypeList(Map<String, Object> params);
}