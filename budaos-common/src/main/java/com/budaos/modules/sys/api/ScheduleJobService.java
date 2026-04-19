package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.ScheduleJobEntity;

import java.util.Map;

/**
 * Title:定时任务执行服务类 Copyright: Copyright (c) 2017 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public interface ScheduleJobService {

	/**
	 * 定时任务信息
	 * 
	 * @param jobId
	 * @return R
	 */
	public R selectObjectByJobId(String jobId);

	/**
	 * 保存或者修改
	 * 
	 * @param scheduleJob
	 * @return R
	 */
	public R saveOrUpdate(ScheduleJobEntity scheduleJob);

	/**
	 * 删除定时任务
	 * 
	 * @param jobIds
	 * @return R
	 */
	public R deleteBatch(String[] jobIds);

	/**
	 * 立即执行任务
	 * 
	 * @param jobIds
	 * @return R
	 */
	public R runTask(String[] jobIds);

	/**
	 * 暂停定时任务
	 * 
	 * @param arg0
	 * @return R
	 */
	public R pause(String[] jobIds);

	/**
	 * 恢复定时任务
	 * 
	 * @param jobIds
	 * @return R
	 */
	public R resume(String[] jobIds);

	/**
	 * 定时任务列表
	 * 
	 * @param params
	 * @return R
	 */
	public R query(Map<String, Object> params);

}
