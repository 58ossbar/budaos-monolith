package com.budaos.modules.evgl.question.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;
import com.budaos.modules.evgl.question.vo.GenerateOptionVO;
import com.budaos.modules.evgl.question.vo.GenerateQuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 题目基本信息</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglQuestionsInfoMapper extends BaseSqlMapper<TevglQuestionsInfo> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 查询部分数据，List<Map>
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> map);
	
	/**
	 * <p>只根据条件统计选择题数量和判断题题数量,以及题目总数量(问答题不在统计范围内)</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param map
	 */
	Map<String, Object> countQuestionNumByMap(Map<String, Object> map);
	
	/**
	 * 根据条件统计单选题、多选题、判断题、问答题的题数，以及总题目数量
	 * @param map
	 * @return totalSingleChose 单选题数量、totalMultipleChose多选题数量、totalJudge判断题数量、totalShortAnswer问答题数量、totalSize总题目量
	 * @apiNote 
	 */
	Map<String, Object> countQuestionNumByMap2(Map<String, Object> map);
	
	/**
	 * <p>根据条件随机筛选题目，如章节ID、题目类型</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param map
	 * @return
	 */
	List<TevglQuestionsInfo> selectRandomQuestionListByMap(Map<String, Object> map);
	
	/**
	 * <p>根据条件随机筛选题目，如章节ID、题目类型</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param map
	 * @return 只查询返回了如下字段questionsId、questionsName
	 */
	List<Map<String, Object>> selectRandomQuestionSimpleListByMap(Map<String, Object> map);

	/**
	 * 查询题目
	 * @param questionIdList
	 * @return
	 */
	List<GenerateQuestionVO> findQuestionByQuestionIdList(List<String> questionIdList);

	/**
	 * 查询子题目
	 * @param questionIdList
	 * @return
	 */
	List<GenerateQuestionVO> findQuestionByParentIdList(List<String> questionIdList);
	
	/**
	 * 查询题目选项
	 * @param questionIdList
	 * @return
	 */
	List<GenerateOptionVO> findQuestionOptionByQuestionIdList(List<String> questionIdList);
	
	/**
	 * <p></p>  
	 * @author huj
	 * @data 2019年12月26日	
	 * @param questionsIds
	 * @return
	 */
	List<TevglQuestionsInfo> selectBatchQuestionsByArrays(String[] questionsIds);
	
	/**
	 * <p>统计教材的总题目数量</p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param subjectId
	 * @return
	 */
	int countSubjectQuestionTotal(String subjectId);
	
	/**
	 * 用于更新问题的组卷数、正确数、错误数、正确率等问题信息
	 * @param t
	 */
	void plusNum(TevglQuestionsInfo t);
	
	List<String> selectQuestionIdListByMap(Map<String, Object> map);
	
	/**
	 * 批量更新题目部分数值，如错误数、正确数、正确率、作答数等
	 * @param list
	 */
	void plusNumBatchByCaseWhen(@Param("list") List<TevglQuestionsInfo> list);
	
	/**
	 * 查询所有已被作答的题目
	 * @return
	 */
	List<String> findQuestionIdsByUnionAll();
}