package com.budaos.modules.evgl.question.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.question.domain.TepExaminePaperQuestionsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:试卷题目详情表
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhujw
 * @version 1.0
 */

@Mapper
public interface TepExaminePaperQuestionsDetailMapper extends BaseSqlMapper<TepExaminePaperQuestionsDetail> {

	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 *
	 * @desc //TODO 关联题目表查询试卷题目详情
	 * @author huangwb
	 * 
	 * @data 2019年2月22日 下午5:23:20
	 */
	List<Map<String, Object>> selectListMapLinkQuestionByMap(Map<String, Object> map);

	/**
	 * 关联题目表查询试卷题目详情，返回List<Map>
	 * @param map
	 * @return 查询返回了如下字段：detailId、paperId、activityId、questionsId、questionsScore、questionsNumber、questionName、questionsType
	 */
	List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TepExaminePaperQuestionsDetail> list);
	
	/**
	 * 仅根据条件查询试卷id
	 * @param map
	 * @return
	 */
	List<String> getPaperIdList(Map<String, Object> map);
	
	/**
	 * 查找这个题目被哪些试卷所使用了
	 * @param questionId
	 * @return
	 */
	List<String> findPaperIdListByQuestionId(@Param("quuestionId") String questionId);
	
	/**
	 * 查询该学生正在考试中的所有试卷的所有题目
	 * @param traineeId
	 * @return
	 */
	List<String> findQuestionIdListByTraineeId(@Param("traineeId") String traineeId);
}