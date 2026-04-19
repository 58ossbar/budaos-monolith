package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityAnswerDiscuss;

import java.util.Map;

/**
 * <p> Title: 答疑讨论</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityAnswerDiscussService extends IBaseService<TevglActivityAnswerDiscuss>{
	
	/**
	 * 新增答疑讨论
	 * @param tevglActivityAnswerDiscuss
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R saveAnswerDiscussInfo(TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss, String loginUserId);
	
	/**
	 * 修改答疑讨论
	 * @param tevglActivityAnswerDiscuss
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R updateAnswerDiscussInfo(TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss, String loginUserId);
	
	/**
	 * 查看答疑讨论
	 * @param activityId 活动id
	 * @return
	 */
	R viewAnswerDiscussInfo(String activityId);
	
	/**
	 * 查看活动内容
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R viewActInfo(Map<String, Object> params, String loginUserId);
	
	/**
	 * 删除答疑讨论
	 * @param activityId 活动id
	 * @param pkgId 所属教学包
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R deleteAnswerDiscussInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 开始答疑讨论
	 * @param ctId 必传参数，课堂主键
	 * @param activityId 必传参数，活动主键
	 * @param pkgId 所属教学包
	 * @param loginUserId 必传参数，当前登录用户
	 * @return
	 */
	R startAnswerDiscussInfo(String ctId, String activityId, String pkgId, String loginUserId, String activityEndTime);
	
	/**
	 * 结束活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R endAnswerDiscussInfo(String ctId, String activityId, String loginUserId);
	
	/**
	 * 复制一个新的活动
	 * @param targetActivityId 目标活动
	 * @param newPkgId 新教学包id
	 * @param loginUserId
	 * @param chapterId 所属章节
	 * @param sortNum 排序号
	 */
	void copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum);
	

	/**
	 * 点赞
	 * @param taId
	 * @param loginUserId
	 * @return
	 */
	R clickLike(String taId, String loginUserId);
	
	/**
	 * 取消点赞
	 * @param taId
	 * @param loginUserId
	 * @return
	 */
	R cancelLike(String taId, String loginUserId);
	
	/**
	 * 根据
	 * @param groupId
	 * @return
	 */
	TevglActivityAnswerDiscuss selectObjectByGroupId(Object groupId);
}