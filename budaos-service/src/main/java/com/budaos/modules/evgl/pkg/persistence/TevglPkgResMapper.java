package com.budaos.modules.evgl.pkg.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 教学包资源</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglPkgResMapper extends BaseSqlMapper<TevglPkgRes> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>获取排序号</p>  
	 * @author huj
	 * @data 2019年11月22日	
	 * @param map
	 * @return
	 */
	int getMaxSortNum(Map<String, Object> map);
	
	/**
	 * <p>统计资源数</p>  
	 * @author huj
	 * @data 2019年11月22日	
	 * @param map
	 * @return
	 */
	int countResNum(Map<String, Object> map);
	
	/**
	 * 根据分组id查询资源
	 * @param resgroupId
	 * @return
	 */
	TevglPkgRes selectObjectByResgroupId(Object resgroupId);
	
	/**
	 * 根据资源分组主键删除（一条分组记录只会对应一个资源记录）
	 * @param id
	 */
	void deleteResgroupId(Object id);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglPkgRes> list);
	
	/**
	 * 根据条件仅查询主键id
	 * @param params
	 * @return
	 */
	List<String> selectResIdListByMap(Map<String, Object> params);
	
	
}