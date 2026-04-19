package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.ScheduleJobLogEntity;

import java.util.Map;

/**
 * Title:定时任務日誌服務類 Copyright: Copyright (c) 2017 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public interface ScheduleJobLogService {
	/**
	 * 定时任务日志信息
	 * 
	 * @param logid
	 * @return R
	 * 
	 */
	public R selectObjectByLogId(String logId);

	/**
	 * 定时任务日志列表
	 * 
	 * @param params
	 * @return R
	 */
	R query(Map<String, Object> params);

	/**
	 * 保存操作
	 * 
	 * @param ScheduleJobLogEntity
	 * @return R
	 */
	R save(ScheduleJobLogEntity ScheduleJobLogEntity);
}
