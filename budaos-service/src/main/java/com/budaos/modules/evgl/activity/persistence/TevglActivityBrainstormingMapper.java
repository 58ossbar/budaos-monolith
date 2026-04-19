package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstorming;
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
public interface TevglActivityBrainstormingMapper extends BaseSqlMapper<TevglActivityBrainstorming> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件获取排序号（已+1）
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 * @apiNote 查询并返回了如下字段：
	 * activityId，resgroupId，activityTitle，resgroupName，
	 * content，activityPic，empiricalValue，sortNum,purpose
	 */
	Map<String, Object> selectObjectMapById(Object id);
	
	/**
	 * 更新数量
	 * @param tevglActivityBrainstorming
	 */
	void plusNum(TevglActivityBrainstorming tevglActivityBrainstorming);
	
	/**
	 * 查询对象
	 * @param params
	 * @return
	 */
	TevglActivityBrainstorming selectObjectByIdAndPkgId(Map<String, Object> params);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglActivityBrainstorming> list);
}