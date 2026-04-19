package com.budaos.modules.core.common.exception;

import com.budaos.common.constants.ExecStatus;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.exception.RepeatedSubmitFormException;
import com.budaos.core.baseclass.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Title:异常处理器 Copyright: Copyright (c) 2017 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@RestControllerAdvice
public class BudaosExceptionAdvice {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private Object returnValue(R r, HttpServletRequest request, HttpServletResponse response) {
		/*
		if (WebUtil.isAjaxRequest(request)) {
			String uuid = UUID.randomUUID().toString();
			// 往session重新set个值
			request.getSession(false).setAttribute(Constant.TOKEN_FORM, uuid);
			// refresh_token放在header中
			response.setHeader(Constant.HEAD_REFRESH_TOKEN_FORM, uuid);
			return r;
		} else {
			ModelAndView m = new ModelAndView("error").addObject("r", r);
			return m;
		}
		*/
		return r;
	}

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(BudaosException.class)
	public Object handleBudaosException(BudaosException e, HttpServletRequest request,
			HttpServletResponse response) {
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
		return returnValue(r, request, response);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Object handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request,
			HttpServletResponse response) {
		logger.error(e.getMessage(), e);
		return returnValue(R.error("数据库中已存在该记录"), request, response);
	}

	/**
	 * 处理表单重复提交异常
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler(RepeatedSubmitFormException.class)
	public R handleBindExceptionException(RepeatedSubmitFormException ex, HttpServletRequest request,
			HttpServletResponse response) {
		logger.error("请不要重复提交数据！");
		return R.error("请不要重复提交数据！");
	}

	private static String getExceptionToString(Throwable e) {
		if (e == null){
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
	
	@ExceptionHandler(Exception.class)
	public Object handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		//开发阶段直接把异常抛到前台去，方便调试
		logger.error(e.getMessage(), e);
		return returnValue(R.error("系统异常，请联系管理员！"), request, response);
		//return returnValue(R.error(getExceptionToString(e)), request, response);
	}

	/**
	 * spring validator实体对象参数校验结果处理
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public Object handleBindExceptionException(BindException ex, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断提交表单的请求是否为Ajax请求,若是则生成refresh_token,以替换表单页面的formToken,解决Ajax提交后,验证不通过无法再次提交的问题
		logger.error(ex.getMessage(), ex);
		return returnValue(R.error("表单参数校验不通过！"), request, response);
	}

	/**
	 * spring validator方法参数校验结果处理
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleMethodArgumentException(MethodArgumentNotValidException ex, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断提交表单的请求是否为Ajax请求,若是则生成refresh_token,以替换表单页面的formToken,解决Ajax提交后,验证不通过无法再次提交的问题
		logger.error(ex.getMessage(), ex);
		return returnValue(R.error("表单参数校验不通过！"), request, response);

	}

	/**
	 * 数据库外键约束错误
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public Object handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request,
			HttpServletResponse response) {
		logger.error(ex.getMessage(), ex);
		return returnValue(R.error("外键约束，操作失败！"), request, response);

	}
	
	/**
	 * oauth2资源权限认证异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public Object handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request,
			HttpServletResponse response) {
		logger.error(ex.getMessage(), ex);
		return returnValue(R.error(ExecStatus.DENIED_TOKEN.getCode(), ExecStatus.DENIED_TOKEN.getMsg()), request, response);

	}
}
