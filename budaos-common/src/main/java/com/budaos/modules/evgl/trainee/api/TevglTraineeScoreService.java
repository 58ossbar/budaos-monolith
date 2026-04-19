package com.budaos.modules.evgl.trainee.api;

import com.budaos.core.baseclass.domain.R;

import java.util.Map;

/**
 * 学员成绩（数据来源课堂的测试活动、实践考核）
 * @author huj
 *
 */
public interface TevglTraineeScoreService {

	
	/**
	 * 根据条件查询学生成绩
	 * @param params
	 * @return
	 */
	R findTraineeScoreList(Map<String, Object> params);
	
	/**
	 * 学生成绩中，查询部分个人信息
	 * @param traineeId
	 * @return
	 */
	R findStudentInfo(String traineeId);
	
}
