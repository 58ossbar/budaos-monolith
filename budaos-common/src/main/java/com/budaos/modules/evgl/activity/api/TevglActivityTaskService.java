package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityTask;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityTaskService extends IBaseService<TevglActivityTask>{
	
	/**
	 * 新增活动【作业/小组任务】
	 * @param tevglActivityTask
	 * @param loginUserId
	 * @return
	 */
	R saveTaskInfo(TevglActivityTask tevglActivityTask, String loginUserId);
	
	/**
	 * 修改活动【作业/小组任务】
	 * @param tevglActivityTask
	 * @param loginUserId
	 * @return
	 */
	R updateTaskInfo(TevglActivityTask tevglActivityTask, String loginUserId);
	
	/**
	 * 删除活动[头脑风暴]
	 * @param activityId
	 * @param loginUserId
	 * @param pkgId
	 * @return
	 */
	R deleteTaskInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 开启活动
	 * @param ctId 课堂主键
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R startActivityTask(String ctId, String activityId, String loginUserId, String activityEndTime);
	
	/**
	 * 结束活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R endActivityTask(String ctId, String activityId, String loginUserId);
	
	/**
	 * 查看活动
	 * @param activityId
	 * @return
	 */
	R viewActivityTaskInfo(String activityId);
	
	/**
	 * 复制一个新的活动
	 * @param targetActivityId 必传参数，目标活动
	 * @param newPkgId 必传参数，新教学包id
	 * @param loginUserId 必传参数，当前登录用户
	 * @param chapterId 非必传参数，所属章节
	 * @param sortNum 非必传参数排序号
	 */
	R copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum);
	
	/**
	 * 查看活动基本信息（注意，此接口不返回参考答案数据）
	 * @param ctId 当前上课课堂
	 * @param pkgId 当前上课课堂对应的教学包id
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R viewActivityTaskInfo(String ctId, String pkgId, String activityId, String loginUserId);
}