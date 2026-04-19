package com.budaos.modules.evgl.tch.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchRoomPereTraineeAnswer;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchRoomPereTraineeAnswerService extends IBaseService<TevglTchRoomPereTraineeAnswer>{
	
	/**
	 * 学员抢答
	 * @author zhouyl加
	 * @date 2020年12月24日
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	R traineeAnswer(String ctId, String activityId, String loginUserId);
	
	/**
	 * 查询抢答成功的学员们
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R querySuccessfulTraineeList(String ctId, String activityId, String loginUserId);
}