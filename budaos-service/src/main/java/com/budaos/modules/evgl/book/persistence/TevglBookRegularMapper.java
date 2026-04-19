package com.budaos.modules.evgl.book.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.book.domain.TevglBookRegular;
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
public interface TevglBookRegularMapper extends BaseSqlMapper<TevglBookRegular> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	List<Map<String,Object>> selectListByMapForSp(Map<String,Object> map);
	
	/**
	 * 根据条件获取最大排序号
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String,Object> map);
}