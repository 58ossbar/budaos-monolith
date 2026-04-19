package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
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
public interface TevglTchTeacherMapper extends BaseSqlMapper<TevglTchTeacher> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>多表关联查询记录</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> map);
	
	/**
	 * <p>根据学员ID查询教师信息</p>  
	 * @author huj
	 * @data 2019年11月20日	
	 * @param traineeId
	 * @return
	 */
	TevglTchTeacher selectObjectByTraineeId(Object traineeId);
	
	/**
	 * 获取所有手机号码，从username字段查
	 * @return
	 */
	List<Map<String, Object>> getMobileList();
	
	/**
	 * 根据条件查询记录，返回List<T>，关联学员表
	 * @param map
	 * @return 
	 */
	List<TevglTchTeacher> selectListByMapInnerJoinTraineeTable(Map<String, Object> map);
}