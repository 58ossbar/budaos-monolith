package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

public class TsysLog extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;

	private String id;
	// 用户名
	private String username;
	// 用户操作
	private String operation;
	// 请求方法
	private String method;
	// 请求参数
	private String params;
	// 响应参数
	private String returns;
	// IP地址
	private String ip;
	// 创建时间
	private String createDate;

	// 应用系统ID
	private String appid;
	/**
	 * 日志类型 0操作日志，1异常日志
	 */
	private Integer logType;
	/**
	 * 请求uri
	 */
	private String requestUri;
	/**
	 * 异常编码
	 */
	private String exceptionCode;
	/**
	 * 异常详情
	 */
	private String exceptionDetail;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionDetail() {
		return exceptionDetail;
	}

	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}

	public Long getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(Long timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * 耗时(单位：毫秒)
	 */
	private Long timeConsuming;
	/**
	 * 客户端信息
	 */
	private String userAgent;

	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置：用户操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 获取：用户操作
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * 设置：请求方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 获取：请求方法
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置：请求参数
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * 获取：请求参数
	 */
	public String getParams() {
		return params;
	}

	/**
	 * 设置：IP地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取：IP地址
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取：创建时间
	 */
	public String getCreateDate() {
		return createDate;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}
	
}
