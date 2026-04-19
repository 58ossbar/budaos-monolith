package com.budaos.modules.evgl.activity.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstormingTraineeAnswer;

import java.util.Map;

/**
 * <p> Title: 活动之头脑风暴---学员作答信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityBrainstormingTraineeAnswerService extends IBaseService<TevglActivityBrainstormingTraineeAnswer>{
	
	/**
	 * 保存学员针对头脑风暴提交的作答内容
	 * @param jsonObject {'ctId':'', 'activityId':'', 'content', 'json':'[{'attachId':'', 'fileName':''}]'}
	 * @param loginUserId
	 * @return
	 */
	R saveTraineeCommitAnswerContent(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查看自己提交的头脑风暴作答内容
	 * @param ctId 课堂主键
	 * @param activityId 活动主键
	 * @param anId 表t_evgl_activity_brainstorming_trainee_answer主键
	 * @param loginUserId
	 * @return
	 * @apiNote 当anId不为空时，可直接拿到作答内容
	 */
	R viewTraineeAnswerInfo(String ctId, String activityId, String anId, String loginUserId);
	
	/**
	 * 查看所有学员的作答内容
	 * @param params 必传参数：ctId课堂id，activityId活动id，pageNum，pageSize
	 * @param loginUserId 登录用户id
	 * @return
	 */
	R viewTraineeAnswerListData(Map<String, Object> params, String loingUserId);
	
	/**
	 * 点赞
	 * @param anId 头脑风暴学员作答表主键id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R clickLike(String anId, String loginUserId);
	
	/**
	 * 取消点赞
	 * @param anId 头脑风暴学员作答表主键id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R cancelLike(String anId, String loginUserId);
}