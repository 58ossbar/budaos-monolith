package com.budaos.modules.evgl.site.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.site.domain.TevglSiteVideoMgr;

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

public interface TevglSiteVideoMgrService extends IBaseService<TevglSiteVideoMgr>{
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglSiteVideoMgr> list);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void deleteBatch(Object[] ids);
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<TevglSiteVideoMgr> selectListByMap(Map<String, Object> map);
	
}