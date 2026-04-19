package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.ScheduleJobLogMapper;
import com.budaos.modules.sys.api.ScheduleJobLogService;
import com.budaos.modules.sys.domain.ScheduleJobLogEntity;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Service
@RestController
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {
	@Autowired
	private ScheduleJobLogMapper scheduleJobLogMapper;

	/**
	 * 定时任务日志信息
	 * 
	 * @param logId
	 * @return R
	 * 
	 */
	@Override
	public R selectObjectByLogId(String logId) {
		return R.ok().put("data", scheduleJobLogMapper.selectListById(logId));
	}

	/**
	 * 定时任务日志列表
	 * 
	 * @param params
	 * @return R
	 */
	@Override
	public R query(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<ScheduleJobLogEntity> list = scheduleJobLogMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 保存操作
	 * 
	 * @param ScheduleJobLogEntity
	 * @return R
	 */
	@Override
	public R save(ScheduleJobLogEntity ScheduleJobLogEntity) {
		ScheduleJobLogEntity.setLogId(Identities.uuid());
		scheduleJobLogMapper.insert(ScheduleJobLogEntity);
		return R.ok();
	}
}
