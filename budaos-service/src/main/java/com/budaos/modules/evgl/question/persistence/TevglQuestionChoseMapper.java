package com.budaos.modules.evgl.question.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.question.domain.TevglQuestionChose;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 题目选项</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglQuestionChoseMapper extends BaseSqlMapper<TevglQuestionChose> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 默认按code升序查询，返回了如下字段，optionId、questionsId、content、state、 code 
	 */
	List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> map);
	List<Map<String, Object>> selectSimpleListByMapForRandom(Map<String, Object> map);
	
	
	List<TevglQuestionChose> selectBatchQuestionsChoseByReplyIds(String[] arrays);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglQuestionChose> list);
	
}