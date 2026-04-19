package com.budaos.modules.evgl.site.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.site.domain.TevglSiteSeo;
import org.apache.ibatis.annotations.Mapper;

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

@Mapper
public interface TevglSiteSeoMapper extends BaseSqlMapper<TevglSiteSeo> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p></p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param relationId
	 * @return
	 */
	TevglSiteSeo selectObjectByRelationId(String relationId);
	
	/**
	 * <p></p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param relationId
	 */
	void deleteByRelationId(String relationId);
}