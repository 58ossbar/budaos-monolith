package com.budaos.modules.evgl.site.api;

import com.budaos.common.exception.BudaosException;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.site.domain.TevglSiteVideoRelation;

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

public interface TevglSiteVideoRelationService extends IBaseService<TevglSiteVideoRelation>{
	
	/**
	 * 批量新增
	 * @param list
	 * @throws BudaosException
	 */
	void insertBatch(List<TevglSiteVideoRelation> list) throws BudaosException;
	
	List<TevglSiteVideoRelation> selectListByMap(Map<String, Object> map);
	
}