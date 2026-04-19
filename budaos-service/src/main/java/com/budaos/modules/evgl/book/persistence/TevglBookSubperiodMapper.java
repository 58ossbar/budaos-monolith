package com.budaos.modules.evgl.book.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.book.domain.TevglBookSubperiod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 专业课程（职业课程）关系</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglBookSubperiodMapper extends BaseSqlMapper<TevglBookSubperiod> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p></p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListForEvglWeb(Map<String, Object> map);
	
	/**
	 * 根据课程id查询其所属专业id
	 * @param subjectId
	 * @return
	 */
	List<String> findMajorIdListBySubjectId(@Param("subjectId") String subjectId);
	
	/**
	 * 根据专业id查询其下课程id
	 * @param majorId
	 * @return
	 */
	List<String> findSubjectIdListByMajorId(@Param("majorId") String majorId);
}