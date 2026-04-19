package com.budaos.modules.evgl.forum.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.forum.domain.TevglForumFriendType;

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

public interface TevglForumFriendTypeService extends IBaseService<TevglForumFriendType>{
	
	/**
	 * 分类列表
	 * @param params
	 * @return
	 */
	R queryTypeList(Map<String, Object> params);
	R queryTypeListForMgr(Map<String, Object> params);
	
	/**
	 * 获取最大排序号
	 * @param params
	 * @return
	 */
	R getMaxSortNum(Map<String, Object> params);
	
	R getTree(Map<String, Object> map);
	
}