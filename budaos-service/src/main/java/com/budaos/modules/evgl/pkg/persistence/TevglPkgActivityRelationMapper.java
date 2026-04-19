package com.budaos.modules.evgl.pkg.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 教学包与活动关系表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglPkgActivityRelationMapper extends BaseSqlMapper<TevglPkgActivityRelation> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据活动id删除记录
	 * @param id
	 */
	void deleteByActivityId(Object id);
	
	/**
	 * 发布教学包中专用（只会查询未绑定章节的活动）
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMapForRelease(Map<String, Object> map);
	
	/**
	 * 根据条件查询教学包的活动
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询活动
	 * @param map
	 * @return
	 */
	List<String> selectActivityIdList(Map<String, Object> map);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	Integer updateActivityStateBatch(List<Object> list);
	
	/**
	 * 批量插入
	 * @param list
	 */
	void insertBatch(List<TevglPkgActivityRelation> list);
	
	Integer countTraineJoinActivityNum(Map<String, Object> map);
}