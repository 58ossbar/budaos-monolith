package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchScheduleClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface TevglTchScheduleClassMapper extends BaseSqlMapper<TevglTchScheduleClass> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 查询该用户所教，上课的班级
	 * @param traineeId
	 * @return
	 */
	List<Map<String, Object>> findMyClassList(@Param("traineeId") String traineeId);
	
	/**
	 * 查询该用户所加入的班级
	 * @param traineeId
	 * @return
	 */
	List<Map<String, Object>> findMyJoinedClassList(@Param("traineeId") String traineeId);

	/**
	 * 查询该班级上课的老师
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> findMyTeacherListByClassId(@Param("classId") String classId);
}