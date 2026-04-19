package com.budaos.modules.evgl.question.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicQuestionsOptions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

@Mapper
public interface TepExamineDynamicQuestionsOptionsMapper extends BaseSqlMapper<TepExamineDynamicQuestionsOptions> {
	
	//根据条件查询记录，返回List<Map>
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据动态试卷的id 查询出动态试卷题目详情并关联问题查询
	 * @desc //TODO 描述
	 * @author huangwb
	 * 
	 *@data 2019年2月21日 下午4:46:08
	 */
	List<Map<String, Object>> selectListMapWithQuestionInfoByDyId(String dyId);
}