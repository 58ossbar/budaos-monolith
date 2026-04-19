package com.budaos.modules.evgl.eao.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamine;
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
public interface TeaoTraineeExamineMapper extends BaseSqlMapper<TeaoTraineeExamine> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件删除
	 * @param map
	 * @return
	 */
	int deleteOther(Map<String, Object> map);
	
	/**
	 * 计算自评分
	 * @param map
	 * @return
	 */
	String countMySelfGiveScore(Map<String, Object> map);
	
	/**
	 * 计算互评总分
	 * @param map
	 * @return
	 */
	String countMyStudentGiveScore(Map<String, Object> map);
	
	/**
	 * 计算师评总分
	 * @param map
	 * @return
	 */
	String countMyTeacherGiveScore(Map<String, Object> map);
	
	/**
	 * 统计参与师评的人数
	 */
	List<TeaoTraineeExamine> selectHowManyTeacherJoinExamine(Map<String,Object> map);
	
	/**
	 * 统计 参与互评的人数
	 */
	List<TeaoTraineeExamine> selectHowManyTraineesJoinExamine(Map<String,Object> map);
	
	/**
	 * 根据条件统计实际参与考核的学员
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> countTraineeListByMap(Map<String,Object> map);
}