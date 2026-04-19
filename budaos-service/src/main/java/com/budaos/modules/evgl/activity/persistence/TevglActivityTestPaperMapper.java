package com.budaos.modules.evgl.activity.persistence;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TevglActivityTestPaperMapper {

	List<Map<String, Object>> selectListMapByMap(Map<String, Object> params);
	
	/**
	 * 根据条件搜索课堂信息
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectClassroomList(Map<String, Object> params);
	
	/**
	 * 根据活动状态和活动类型查询与教学包id对应的活动
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectRelationList(Map<String, Object> params);
	
	/**
	 * 根据试卷id查询试卷信息
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectPaperList(Map<String, Object> params);
	
	/**
	 * 根据教学包id查询所有的衍生教学包
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryTotalViewNum(Map<String, Object> params);
	
}
