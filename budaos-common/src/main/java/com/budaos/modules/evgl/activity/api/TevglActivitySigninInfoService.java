package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninInfo;

import java.util.Map;

/**
 * <p> Title: 活动-签到活动信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivitySigninInfoService extends IBaseService<TevglActivitySigninInfo>{
	
	/**
	 * 新增一个签到活动
	 * @param tevglActivitySigninInfo
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R saveSigninInfo(TevglActivitySigninInfo tevglActivitySigninInfo, String pkgId, String loginUserId);
	
	/**
	 * 修改签到活动
	 * @param tevglActivitySigninInfo
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R updateSigninInfo(TevglActivitySigninInfo tevglActivitySigninInfo, String pkgId, String loginUserId);
	
	/**
	 * 查看签到活动基本信息
	 * @param activityId
	 * @return
	 */
	R viewSigninInfo(String activityId);
	
	/**
	 * 删除签到活动
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R deleteSigninInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 查看签到历史记录
	 * @param ctId 课堂主键
	 * @return
	 */
	R queryHistorySigninInfo(Map<String, Object> params);
	R deleteHistorySigninInfo(String activityId, String loginUserId);
	
	/**
	 * 开始签到活动
	 * @param ctId 课堂主键
	 * @param activityId 活动主键
	 * @param loginUserId
	 * @return
	 */
	R startSigninInfo(String ctId, String activityId, String loginUserId, String activityEndTime);
	
	/**
	 * 结束签到活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R endSingninInfo(String ctId, String activityId, String loginUserId);
	
	/**
	 * PC网站端查看出勤统计图
	 * @param ctId
	 * @return
	 */
	R viewAttendanceStatisticsChart(String ctId);
}