package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title:活动-投票/问卷 -> 题目 </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglActivityVoteQuestionnaireQuestionMapper extends BaseSqlMapper<TevglActivityVoteQuestionnaireQuestion> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询记录,返回List<Map>,且对象为驼峰命名
	 * @param map
	 * @return
	 * @apiNote 只查询了返回了如下字段
	 * questionId, activityId, questionName, questionType, sortNum, state
	 */
	List<Map<String, Object>> selectSimpleListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件获取排序号(已经+1)
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 查询这个活动的所有题目
	 * @param activityId
	 * @return
	 */
	List<TevglActivityVoteQuestionnaireQuestion> selectActivityQuestionList(Object activityId);
	
	/**
	 * 根据条件仅查询主键id
	 * @param params
	 * @return
	 */
	List<String> selectQuestionIdListByMap(Map<String, Object> params);
}