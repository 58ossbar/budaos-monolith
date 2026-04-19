package com.budaos.common.cbsecurity.logs.aspect;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.modules.sys.domain.TsysLog;
import com.budaos.utils.tool.DateUtils;
import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Title:系统日志，切面处理类 Copyright: Copyright (c) 2017
 * Company:creatorblue.co.,ltd
 * 修改系统日志切面，调到服务层统一处理
 * @author creatorblue.co.,ltd
 * @version 1.0
 */
//@Aspect
//@Component
public class ServiceSysLogAspect {
    //本地异常日志记录对象
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    private ThreadLocal<TsysLog> localLog = new ThreadLocal<TsysLog>();
    //@Autowired
    //private TsysLogService sysLogService;
    private TsysLog sysLog;

    //定义日志切入点
    @Pointcut("@annotation(com.budaos.common.cbsecurity.logs.annotation.SysLog)")
    public void servicelogPointCut() {

    }

    @Around("servicelogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        sysLog = new TsysLog();
        localLog.set(sysLog);
        startTime.set(beginTime);
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, time);

        return result;
    }
    /**
     * 后置通知 用于拦截service层记录用户的操作
     *
     * @param joinPoint 切点
     */

    private void saveSysLog(ProceedingJoinPoint joinPoint,long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if(syslog != null){
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        if(args != null && args.length > 0) {
            String params = new Gson().toJson(args[0]);
            sysLog.setParams(params);
        }

        //获取request
        //HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        //sysLog.setIp(IPUtils.getIpAddr(request));

        //用户名
        String username = "";//SecurityUtils.getSubject().getPrincipal() == null ? "匿名用户" : ((TsysUserinfo) SecurityUtils.getSubject().getPrincipal()).getUserRealname();
        sysLog.setUsername(username);
        sysLog.setCreateDate(DateUtils.getNowTimeStamp());
        sysLog.setAppid("");
        //耗时
        sysLog.setTimeConsuming(time);
        //sysLog.setRequestUri(request.getRequestURI());
        sysLog.setLogType(0);
        //sysLog.setUserAgent(request.getHeader("User-Agent"));
        sysLog.setExceptionCode(null);
        sysLog.setExceptionDetail(null);
        //保存系统日志
        //sysLogService.save(sysLog);
        localLog.remove();
        startTime.remove();
    }


    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "servicelogPointCut()", throwing = "e")
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
            log.setExceptionDetail(e.getMessage());

            //保存数据库
            //sysLogService.save(log);
        }  catch (Exception ex) {
            ex.printStackTrace();
            //记录本地异常日志
            // log.error("异常方法全路径:{},异常信息:{},请求参数:{}", getFullMethodName(joinPoint), e.getMessage(), JSONUtil.toJsonStr(args));
        } finally {
            localLog.remove();
            startTime.remove();
        }
    }

}