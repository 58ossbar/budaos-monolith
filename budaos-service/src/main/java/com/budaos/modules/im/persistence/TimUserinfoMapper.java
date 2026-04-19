package com.budaos.modules.im.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.im.domain.TimUserinfo;
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
public interface TimUserinfoMapper extends BaseSqlMapper<TimUserinfo> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据关联id查询对象
	 * @param id
	 * @return
	 */
	TimUserinfo	selectObjectByRelateId(Object id);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TimUserinfo> list);
}