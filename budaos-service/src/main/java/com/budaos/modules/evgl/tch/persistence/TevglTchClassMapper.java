package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClass;
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
public interface TevglTchClassMapper extends BaseSqlMapper<TevglTchClass> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件记录
	 * @param map
	 * @return
	 * @apiNote 查询返回了如下字段：班级主键classId，专业方向majorId，所属机构orgId，班级名称className，
	 * 班级封面classPic，remark备注
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询单科强化、订单就业班级
	 * @param map
	 * @return
	 */
	List<TevglTchClass> findClassListByMap(Map<String, Object> map);
	
	/**
	 * 统计单科强化班级的总数
	 * @return
	 */
	Integer countClassNumByType(Object type);
	
	/**
	 * 更新自定义图片
	 * @param tevglTchClass
	 * @return
	 */
	int updateClassImg(TevglTchClass tevglTchClass);
	
}