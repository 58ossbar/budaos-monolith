package com.budaos.modules.core.common.cbsecurity.repeatsubmit;

import com.budaos.common.exception.RepeatedSubmitFormException;
import com.budaos.modules.core.common.constants.Global;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Title: 防止重复提交 Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
	private static Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatToken annotation = method.getAnnotation(RepeatToken.class);
            if (annotation != null) {

                boolean needSaveSession = annotation.save();
                if (needSaveSession) {
                    //跳转到页面前将token设置到session中
                    request.getSession(true).setAttribute(Global.APPLICATION_NAME + ":" + Constant.TOKEN_FORM, UUID.randomUUID().toString());
                }

                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                	synchronized (this) {
                		 if (isRepeatSubmit(request)) {
                             //不允许重复提交
                             throw new RepeatedSubmitFormException();
                         }
                         //移除session中保存的旧token
                         request.getSession(false).removeAttribute(Global.APPLICATION_NAME + ":" + Constant.TOKEN_FORM);
					}
                   
                }
            }
        }

        return true;
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute(Global.APPLICATION_NAME + ":" + Constant.TOKEN_FORM);
        
        if (serverToken == null) {
            return true;
        }
        
        String clientToken = request.getParameter(Constant.TOKEN_FORM);

        if(StrUtils.isEmpty(clientToken)){
            return true;
        }
        if (!serverToken.equals(clientToken)) {
            return true;
        }
        log.info("校验是否重复提交：表单页面Token值为："+clientToken + ",Session中的Token值为:"+serverToken);
        return false;
    }
}
