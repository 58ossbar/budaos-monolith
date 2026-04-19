package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireTraineeAnswer;
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
public interface TevglActivityVoteQuestionnaireTraineeAnswerMapper extends BaseSqlMapper<TevglActivityVoteQuestionnaireTraineeAnswer> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 * @apiNote id主键，ctId课堂主键，activityId活动主键，questionId题目主键，optionId选项主键
	 * content简单题时作答内容，questionType题目类型，createTime提交时间
	 * traineeId学员主键，traineeName学员名称（昵称），traineePic证件照（头像）
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 统计此课堂总提交人数
	 * @param params 必传参数：ctId课堂主键，activityId活动主键
	 * @return
	 */
	int countAnswerNum(Map<String, Object> params);
	
	/**
	 * 仅查询主键id
	 * @param params
	 * @return
	 */
	List<String> selectIdListByMap(Map<String, Object> params);
}