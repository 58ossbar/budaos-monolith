package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstorming;

/**
 * <p> Title: 活动-头脑风暴</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityBrainstormingService extends IBaseService<TevglActivityBrainstorming>{
	
	/**
	 * 新增活动[头脑风暴]
	 * @param tevglActivityBrainstorming
	 * @param loginUserId
	 * @return
	 */
	R saveBrainstormingInfo(TevglActivityBrainstorming tevglActivityBrainstorming, String loginUserId);
	
	/**
	 * 修改活动[头脑风暴]
	 * @param tevglActivityBrainstorming
	 * @param loginUserId
	 * @return
	 */
	R updateBrainstormingInfo(TevglActivityBrainstorming tevglActivityBrainstorming, String loginUserId);
	
	/**
	 * 删除活动[头脑风暴]
	 * @param activityId
	 * @param loginUserId
	 * @param pkgId
	 * @return
	 */
	R deleteBrainstormingInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 开启活动
	 * @param ctId 课堂主键
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R startActivityBrainstorming(String ctId, String activityId, String loginUserId, String activityEndTime);
	
	/**
	 * 结束活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R endActivityBrainstorming(String ctId, String activityId, String loginUserId);
	
	/**
	 * 查看活动
	 * @param activityId
	 * @return
	 */
	R viewActivityBrainstormingInfo(String activityId);

	/**
	 * 复制一个新的活动
	 * @param targetActivityId 必传参数，目标活动
	 * @param newPkgId 必传参数，新教学包id
	 * @param loginUserId 必传参数，当前登录用户
	 * @param chapterId 非必传参数，所属章节
	 * @param sortNum 非必传参数排序号
	 */
	void copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum);
}