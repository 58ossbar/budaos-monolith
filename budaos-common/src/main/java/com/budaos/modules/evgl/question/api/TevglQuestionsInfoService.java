package com.budaos.modules.evgl.question.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;

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

public interface TevglQuestionsInfoService extends IBaseService<TevglQuestionsInfo>{
	
	/**
	 * <p>题目列表</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param params
	 * @return
	 */
	R queryQuestions(Map<String, Object> params, String loginUserId);
	
	/**
	 * <p>新增题目</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param tevglQuestionsInfo
	 * @param traineeInfo 登录用户
	 */
	R saveQuestion(TevglQuestionsInfo tevglQuestionsInfo, TevglTraineeInfo traineeInfo);
	
	/**
	 * 新增题目（复合题专属接口）
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveCompositeQuestionInfo(JSONObject jsonObject, String loginUserId);
	
	/**
	 * <p>修改题目</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param tevglQuestionsInfo
	 * @param traineeInfo 登录用户
	 * @return
	 */
	R editQuestion(TevglQuestionsInfo tevglQuestionsInfo, TevglTraineeInfo traineeInfo);
	
	/**
	 * 修改复合题
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R updateCompositeQuestionInfo(JSONObject jsonObject, String loginUserId);
	
	/**
	 * <p>题目明细</p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param id
	 * @return
	 */
	TevglQuestionsInfo selectObjectById(String id);
	
	/**
	 * <p></p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param map
	 * @return
	 */
	List<TevglQuestionsInfo> selectListByMap(Map<String, Object> map);
	
	/**
	 * <p>通过题目类型、知识点id随机生成limit个问题</p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param map
	 * @return
	 */
	List<TevglQuestionsInfo> selectRandomListByKnowangeIdAndType(Map<String, Object> map);
	
	/**
	 * <p>根据问题id批量查询问题</p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param questionsIds
	 * @return
	 */
	List<TevglQuestionsInfo> selectBatchQuestionsByArrays(String[] questionsIds);
	
	/**
	 * <p>用于更新问题的组卷数、正确数、错误数、正确率等问题信息</p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param tevglQuestionsInfo
	 */
	void plusNum(TevglQuestionsInfo tevglQuestionsInfo);
	
	/**
	 * <p>只根据条件统计选择题数量和判断题题数量,以及题目总数量(问答题不在统计范围内)</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param map
	 * @return
	 */
	Map<String, Object> countQuestionNumByMap(Map<String, Object> map);
	
	/**
	 * 
	 * @param map
	 * @return
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
	 * <p>统计教材的总题目数量</p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param subjectId
	 * @return
	 */
	int countSubjectQuestionTotal(String subjectId);
	
	/**
	 * 禁用题目
	 * @param questionsId 被禁用的题目
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R forbiddenQuestions(String questionsId, String loginUserId);
	
	/**
	 * 收藏题目
	 * @param questionsId
	 * @param loginUserId
	 * @return
	 */
	R collectQuestions(String questionsId, String loginUserId);
	
	/**
	 * 取消收藏题目
	 * @param questionsId
	 * @param loginUserId
	 * @return
	 */
	R cancelCollectQuestions(String questionsId, String loginUserId);
	
	/**
	 * 删除复合题
	 * @param questionsId
	 * @param loginUserId
	 * @return
	 */
	R deleteCompositeQuestionInfo(String questionsId, String loginUserId);
}