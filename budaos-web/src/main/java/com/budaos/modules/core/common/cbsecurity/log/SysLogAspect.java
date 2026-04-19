package com.budaos.modules.core.common.cbsecurity.log;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.sys.api.TsysLogService;
import com.budaos.modules.sys.domain.TsysLog;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.rabbitmq.RabbitMqProducer;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.IPUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * Title:系统日志，切面处理类 Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 修改系统日志切面，调到服务层统一处理
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Aspect
@Component
public class SysLogAspect {
    private static final Logger log = LoggerFactory.getLogger(SysLogAspect.class);
    //本地异常日志记录对象
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    
    @Value("${com.budaos.log-type:mysql}")
    private String logType;
    @Value("${com.budaos.log-topic:cb-core-log}")
    private String logTopic;
	private ThreadLocal<TsysLog> localLog = new ThreadLocal<TsysLog>();
	@Autowired(required = false)
	private TsysLogService sysLogService;
	@Autowired(required = false)
	private RabbitMqProducer rabbitMqProducer;
    @Autowired
    private LoginUtils loginUtils;
	//定义日志切入点
	@Pointcut("@annotation(com.budaos.modules.core.common.cbsecurity.log.SysLog)")
	public void logPointCut() { 
		
	}
	
	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object[] args = point.getArgs();
		if(args != null && args.length > 0) {
			String params = new Gson().toJson(args[0]);
			//System.err.println(params);
		}
		long beginTime = System.currentTimeMillis();
		TsysLog sysLog = new TsysLog();
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
	    sysLog.setId(Identities.uuid());
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){
			//注解上的描述 
			sysLog.setOperation(syslog.value());
		}
		
		//请求的方法名
		String className = point.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		
		//请求的参数
		if(args != null && args.length > 0) {
			String params = new Gson().toJson(args[0]);
			sysLog.setParams(params);
		}
		//获取request
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));
		
		//用户名
		TsysUserinfo loginUser = loginUtils.getLoginUser();
		String username = loginUser==null ? "匿名用户" : loginUser.getUserRealname();
		sysLog.setUsername(username);	
		sysLog.setCreateDate(DateUtils.getNowTimeStamp());
		sysLog.setAppid("");
		sysLog.setRequestUri(request.getRequestURI());
		sysLog.setUserAgent(request.getHeader("User-Agent"));
		localLog.set(sysLog);
		startTime.set(beginTime);
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		//耗时
		sysLog.setTimeConsuming(time);
		sysLog.setLogType(0);
		sysLog.setReturns(StrUtils.toString(result));
		sysLog.setExceptionCode(null);
		sysLog.setExceptionDetail(null);
		//保存日志
		doSave(sysLog);
		return result;
	}
	
	private void doSave(TsysLog tsysLog) {
    	cleanLocal();
		if("mysql".equals(logType)) {
			if(sysLogService != null) {
				sysLogService.save(tsysLog);
			} else {
				log.warn("TsysLogService未注入，无法保存日志到数据库");
			}
		}else if("rabbitmq".equals(logType)) {
			if(rabbitMqProducer != null) {
				rabbitMqProducer.send(logTopic, JSONObject.toJSONString(tsysLog));
			} else {
				log.warn("RabbitMQ未启用，无法发送日志消息到队列[" + logTopic + "]");
			}
		}
	}
	 /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        //Map<String,String[]> requestParams = new HashMap<>();
        //请求的参数
       // Object[] args = joinPoint.getArgs();
        try {
            /*==========数据库日志=========*/
            TsysLog log = localLog.get();
            log.setTimeConsuming(System.currentTimeMillis() - startTime.get());
            log.setLogType(1);
            log.setExceptionCode(e.getClass().getName());
            log.setExceptionDetail(getExceptionToString(e));

            //保存数据库
            doSave(log);
        }  catch (Exception ex) {
        	ex.printStackTrace();
        	cleanLocal();
            //记录本地异常日志
        	//log.error("异常方法全路径:{},异常信息:{},请求参数:{}", getFullMethodName(joinPoint), e.getMessage(), JSONUtil.toJsonStr(args));
        }
    }
    private static String getExceptionToString(Throwable e) {
		if (e == null){
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
    private void cleanLocal() {
    	localLog.remove();
    	startTime.remove();
    }
}
