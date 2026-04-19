package com.budaos.modules.evgl.trainee.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeEmpiricalValueLog;

/**
 * <p> Title: 学员经验获取记录日志表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTraineeEmpiricalValueLogService extends IBaseService<TevglTraineeEmpiricalValueLog>{
	
	/**
	 * 查看经验值明细
	 * @param ctId 当前课堂
	 * @param traineeId 学员
	 * @param loginUserId 当前登录用户
	 */
	R viewEmpiricalValueLogs(String ctId, String traineeId, String loginUserId, Integer pageNum, Integer pageSize);
	
	/**
	 * 查看某学员的当前课堂的经验值、热心解答次数、获取点赞数、课堂表现次数、视频学习个数
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	R viewNums(String ctId, String traineeId);
}