package com.budaos.modules.evgl.tch.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.common.exception.BudaosException;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchRoomPereAnswer;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchRoomPereAnswerService extends IBaseService<TevglTchRoomPereAnswer>{
	/**
	 * 新增课堂表现
	 * @author zhouyl加
	 * @date 2020年12月21日
	 * @param ctId 课堂id
	 * @param pkgId 教学包id
	 * @param loginUserId 登录用户id
	 * @param answerNum 抢答人数
	 * @param type 类型：1 抢答、2 随机选人、3 手动选人]
	 * @return
	 */
	R launchAnswer(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 修改抢答
	 * @author zyl加
	 * @date 2020年12月22日
	 * @param pkgId 教学包id
	 * @param answerId 抢答id(活动id)
	 * @param answerNum 抢答人数
	 * @param loginUserId 登录用户id
	 * @return
	 */
	//R updateAnswer(String pkgId, String answerId, Integer answerNum, Integer type, String loginUserId);
	R updateAnswer(JSONObject jsonObject, String loginUserId);
	/**
	 * 开始活动
	 * @author zhouyl加
	 * @date 2020年12月22日
	 * @param ctId 课堂id
	 * @param activityId 活动id(抢答id)
	 * @param loginUserId 登录用户id
	 * @param activityEndTime 结束时间
	 * @return
	 */
	R startAnswerActivity(String ctId, String activityId, String loginUserId, String activityEndTime);
	
	/**
	 * @author zhouyl加
	 * @date 2020年12月22日
	 * @param ctId 课堂id
	 * @param activityId 活动id(随机选人id)
	 * @param loginUserId 登录用户id
	 * @return
	 */
	R endAnswerActivity(String ctId, String activityId, String loginUserId);
	
	/**
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * 手动选人以及随机选人
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 登录用户id
	 * @param traineeId 学员id
	 * @return
	 */
	R selectPeopleToAnswer(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查询该课堂下的所有成员
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param ctId 课堂id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	R selectClassroomTraineeInfo(String ctId, String loginUserId);
	
	/**
	 * 删除抢答活动
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param activityId 活动id
	 * @param pkgId 教学包id
	 * @param loginUserId 登录用户id
	 * @return
	 * @throws BudaosException
	 */
	R deleteAnswerActivity(String activityId, String pkgId, String loginUserId) throws BudaosException;
	
	/**
	 * 查询课堂下抢答活动的抢答人员
	 * @author zhouyl加
	 * @date 2020年12月25日
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	R answerActivityTraineeInfo(String ctId, String activityId, String loginUserId);
	
	/**
	 * 查询抢答活动的总体结果
	 * @author zhouyl加
	 * @date 2020年12月26日
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	R answerSummaryResults(String ctId, String activityId, String loginUserId);
	
	/**
	 * 抢答活动中的key
	 * @param ctId
	 * @param activityId
	 * @return 示例结果：activity:6_classroom_performance:c1e0397b5737483c8915b958f81b46c9_263148468eda4619ba4763be58ccf2e5
	 */
	String getMyRedisKey(String ctId, String activityId);
}