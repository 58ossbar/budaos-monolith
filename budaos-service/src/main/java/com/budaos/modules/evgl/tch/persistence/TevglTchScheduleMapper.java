package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchSchedule;
import com.budaos.modules.evgl.tch.params.TevglTchScheduleParams;
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
public interface TevglTchScheduleMapper extends BaseSqlMapper<TevglTchSchedule> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 *
	 * @param map
	 * @return
	 */
	List<TevglTchSchedule> queryScheduleData(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglTchSchedule> list);
	
	/**
	 * 批量新增安排和班级的关系
	 * @param list
	 */
	void insertBatchScheduleClass(List<Map<String, Object>> list);
	
	/**
	 * 网站端根据条件查询课表
	 * @param map
	 * @return
	 */
	List<TevglTchSchedule> queryScheduleDataForWeb(TevglTchScheduleParams params);
	
	/**
	 * 删除授课班级关系
	 * @param scheduleId
	 * @return
	 */
	int deleteScheduleClass(Object scheduleId);
	
	/**
	 * 查询实训室
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryTrainingRoomList(Map<String, Object> map);
}