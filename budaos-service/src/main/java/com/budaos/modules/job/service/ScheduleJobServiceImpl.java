package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.ScheduleJobMapper;
import com.budaos.modules.job.utils.ScheduleUtils;
import com.budaos.modules.sys.api.ScheduleJobService;
import com.budaos.modules.sys.domain.ScheduleJobEntity;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.constants.Constant.ScheduleStatus;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Order(value=Integer.MAX_VALUE)
public class ScheduleJobServiceImpl implements ScheduleJobService, CommandLineRunner{
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private ScheduleJobMapper scheduleJobMapper;

	/**
	 * 项目启动时，初始化定时器
	 * 
	 * @param arg0
	 * @return R
	 */
	@Override
	public void run(String... arg0) {
		List<ScheduleJobEntity> scheduleJobList = scheduleJobMapper.selectListByMap(new HashMap<>());
		System.out.println("加载定时任务列表，共 " + scheduleJobList.size() + " 条记录");
		
		for (ScheduleJobEntity scheduleJob : scheduleJobList) {
			// 先检查 jobId 是否为 null
			if (StringUtils.isEmpty(scheduleJob.getJobId())) {
				System.err.println("定时任务的 jobId 为空，beanName=" + scheduleJob.getBeanName() 
					+ ", methodName=" + scheduleJob.getMethodName() + ", cronExpression=" + scheduleJob.getCronExpression() + "，跳过创建");
				continue;
			}
			
			// 检查 cron 表达式是否为空
			if (StringUtils.isEmpty(scheduleJob.getCronExpression())) {
				System.err.println("定时任务 [" + scheduleJob.getJobId() + "] 的 cron 表达式为空，跳过创建");
				continue;
			}
			
			// 验证 cron 表达式是否有效
			if (!CronExpression.isValidExpression(scheduleJob.getCronExpression())) {
				System.err.println("定时任务 [" + scheduleJob.getJobId() + "] 的 cron 表达式无效: " + scheduleJob.getCronExpression() + "，跳过创建");
				continue;
			}
			
			try {
				CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
				// 如果不存在，则创建
				if (cronTrigger == null) {
					ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
				} else {
					ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
				}
			} catch (Exception e) {
				System.err.println("处理定时任务 [" + scheduleJob.getJobId() + "] 时发生异常: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 定时任务信息
	 * 
	 * @param jobId
	 * @return R
	 */
	@Override
	public R selectObjectByJobId( String jobId) {
		return R.ok().put(Constant.R_DATA, scheduleJobMapper.selectObjectById(jobId));
	}

	/**
	 * 保存或者修改
	 * 
	 * @param scheduleJob
	 * @return R
	 */
	@Transactional
	@Override
	public R saveOrUpdate( ScheduleJobEntity scheduleJob) {
		boolean validExpression = CronExpression.isValidExpression(scheduleJob.getCronExpression());
        if (!validExpression) {
            return R.error("请输入正确的cron表达式！");
        }
		try {
			if (scheduleJob.getJobId() == null || StringUtils.isEmpty(scheduleJob.getJobId())) {
				scheduleJob.setCreateTime(DateUtils.getNowTimeStamp());
				scheduleJob.setUpdateTime(scheduleJob.getCreateTime());
				scheduleJob.setJobId(Identities.uuid());
				scheduleJob.setStatus(ScheduleStatus.NORMAL.getValue());
				scheduleJobMapper.insert(scheduleJob);
				ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
			} else {
				ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
				scheduleJob.setUpdateTime(DateUtils.getNowTimeStamp());
				scheduleJobMapper.update(scheduleJob);
			}
			return R.ok("操作成功");
		} catch (Exception e) {
			return R.error(e.getMessage());
		}
	}

	/**
	 * 删除定时任务
	 * 
	 * @param jobIds
	 * @return R
	 */
	@Transactional
	@Override
	public R deleteBatch( String[] jobIds) {
		for (String jobId : jobIds) {
			ScheduleUtils.deleteScheduleJob(scheduler, jobId);
		}
		// 删除数据
		scheduleJobMapper.deleteBatch(jobIds);
		return R.ok();
	}

	/**
	 * 立即执行任务
	 * 
	 * @param jobIds
	 * @return R
	 */
	@Override
	public R runTask( String[] jobIds) {
		for (String jobId : jobIds) {
			ScheduleUtils.run(scheduler, (ScheduleJobEntity) selectObjectByJobId(jobId).get("data"));
		}
		return R.ok();
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param jobIds
	 * @return R
	 */
	@Transactional
	@Override
	public R pause( String[] jobIds) {
		for (String jobId : jobIds) {
			ScheduleUtils.pauseJob(scheduler, jobId);
		}
		updateBatch(jobIds, ScheduleStatus.PAUSE.getValue());
		return R.ok();
	}

	/**
	 * 恢复定时任务
	 * 
	 * @param jobIds
	 * @return R
	 */
	@Transactional
	@Override
	public R resume( String[] jobIds) {
		for (String jobId : jobIds) {
			ScheduleUtils.resumeJob(scheduler, jobId);
		}
		updateBatch(jobIds, ScheduleStatus.NORMAL.getValue());
		return R.ok();
	}

	/**
	 * 定时任务列表
	 * 
	 * @param params
	 * @return R
	 */
	@Override
	public R query( Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<ScheduleJobEntity> list = scheduleJobMapper.selectListByMap(params);
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	private int updateBatch(String[] jobIds, int status) {
		Map<String, Object> map = new HashMap<>();
		map.put("list", jobIds);
		map.put("status", status);
		return scheduleJobMapper.updateBatch(map);
	}
}
