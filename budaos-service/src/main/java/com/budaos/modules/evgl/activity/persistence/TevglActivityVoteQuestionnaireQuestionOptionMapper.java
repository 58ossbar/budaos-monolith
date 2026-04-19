package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireQuestionOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 活动-投票/问卷 -> 题目选项</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglActivityVoteQuestionnaireQuestionOptionMapper extends BaseSqlMapper<TevglActivityVoteQuestionnaireQuestionOption> {
	
	/**
	 * 根据条件查询记录，返回List<Map>,且对象为驼峰命名
	 * @param map
	 * @return 
	 * @apiNote 查询了并返回如下字段 optionId，questionId，optionCode，optionName，sortNum，isRight
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询记录，返回List<Map>,且对象为驼峰命名
	 * @param map
	 * @return
	 * @apiNote 查询了并返回如下字段 optionId，questionId，optionCode，optionName，sortNum
	 */
	List<Map<String, Object>> selectListMapForWeb(Map<String, Object> map);
	
	/**
	 * 根据题目id删除选项
	 * @param questionId
	 */
	void deleteByQuestionId(Object questionId);
	
	/**
	 * 根据题目id批量删除选项
	 * @param questionId
	 */
	void deleteBatchByQuestionId(Object[] questionId);
	
	/**
	 * 根据题目id集合查询选项
	 * @param list
	 * @return
	 */
	List<TevglActivityVoteQuestionnaireQuestionOption> selectOptionListByQuestionIds(List<String> list);
	
	/**
	 * 根据条件仅查询主键id
	 * @param params
	 * @return
	 */
	List<String> selectOptionIdListByMap(Map<String, Object> params);
}