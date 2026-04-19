package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * Title:定时器 Copyright: Copyright (c) 2017 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public class ScheduleJobEntity extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务调度参数key
	 */
	public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

	/**
	 * 任务id
	 */
	private String jobId;

	/**
	 * spring bean名称
	 */
	// @NotBlank(message = "bean名称不能为空")
	private String beanName;

	/**
	 * 方法名
	 */
	// @NotBlank(message = "方法名称不能为空")
	private String methodName;

	/**
	 * 参数
	 */
	private String params;

	/**
	 * cron表达式
	 */
	// @NotBlank(message = "cron表达式不能为空")
	private String cronExpression;

	/**
	 * 任务状态
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 设置：任务id
	 * 
	 * @param jobId 任务id
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * 获取：任务id
	 * 
	 * @return Long
	 */
	public String getJobId() {
		return jobId;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 设置：任务状态
	 * 
	 * @param status 任务状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：任务状态
	 * 
	 * @return String
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置：cron表达式
	 * 
	 * @param cronExpression cron表达式
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * 获取：cron表达式
	 * 
	 * @return String
	 */
	public String getCronExpression() {
		return cronExpression;
	}

}
