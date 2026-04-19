package com.budaos.modules.evgl.activity.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaire;

import java.util.Map;

/**
 * <p> Title: 活动-投票/问卷</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityVoteQuestionnaireService extends IBaseService<TevglActivityVoteQuestionnaire>{
	
	/**
	 * 保存投票/问卷
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveVoteQuestionnaireInfo(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 修改投票问卷
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R updateVoteQuestionnaireInfo(JSONObject jsonObject, String loginUserId);
	R updateVoteQuestionnaireInfoNew(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查看投票问卷
	 * @param activityId
	 * @return
	 */
	R viewVoteQuestionnaireInfo(String activityId);
	
	/**
	 * 删除投票问卷
	 * @param activityId 活动id
	 * @param pkgId 所属教学包
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R deleteVoteQuestionnaireInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 开启活动
	 * @param ctId 课堂主键
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R startActivityVoteQuestionnaire(String ctId, String activityId, String loginUserId, String activityEndTime);
	
	/**
	 * 结束活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R endActivityVoteQuestionnaire(String ctId, String activityId, String loginUserId);
	
	/**
	 * 复制一个新的活动
	 * @param 必传参数，targetActivityId 目标活动
	 * @param 必传参数，newPkgId 新教学包id
	 * @param 必传参数，loginUserId
	 * @param 非必传参数，chapterId 所属章节
	 * @param 非必传参数，sortNum 排序号
	 */
	void copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum);
	
	/**
	 * 查看学员针对投票/问卷的作答内容（总体结果）
	 * @return
	 */
	R viewTraineeAnswerListData(Map<String, Object> params, String loginUserId);
	
	/**
	 * 查看学员针对投票/问卷的作答内容（单个结果）
	 * @param ctId
	 * @param activityId
	 * @param traineeId
	 * @param loginUserId
	 * @return
	 */
	R viewTraineeAnswerData(String ctId, String activityId, String traineeId, String loginUserId);
	
}