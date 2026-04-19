package com.budaos.modules.evgl.common;

import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.SpringContextUtils;
import com.budaos.utils.tool.StrUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: 会话过期，重新登录 Copyright: Copyright (c) 2016 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
	
	static Log log = LogFactory.getLog(SessionInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            CheckSession annotation = method.getAnnotation(CheckSession.class);
            if(annotation != null) {
            	HttpSession session = request.getSession();
            	// 设置页面不缓存
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Cache-Control", "no-store");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				if ("Y".equals(EvglGlobal.MULTI_USER_LOGIN) && LoginUtils.checkIsLogin(request)) {
					return super.preHandle(request, response, handler);
				}
				// 校验该会话是否被剔除开始
				if("true".equals(session.getAttribute("KICKOUT"))){
					session.invalidate();
					initKickOut(request, response);
					response.setStatus(502);
					return false;
				}
				String token = request.getHeader(EvglGlobal.TOKEN_KEY_NAME);
				if(StrUtils.isEmpty(token)) {
					String servletPath = request.getServletPath();
					if ("/resourceCenter-api/listChapters".equals(servletPath)) {
						String evglToken = request.getParameter(EvglGlobal.TOKEN_KEY_NAME);
						if (StrUtils.isNotEmpty(evglToken)) {
							token = evglToken;
						}
					}
				}
				if(StrUtils.isEmpty(token)) {
					Cookie[] cookies = request.getCookies();
					if(cookies != null)
					for (Cookie cookie : cookies) {
					    switch(cookie.getName()){
					        case EvglGlobal.TOKEN_KEY_NAME:
					            token = cookie.getValue();
					            break;
					        default:
					            break;
					    }
					}
					if(StrUtils.isEmpty(token)) {
						response.setStatus(502);
						return false;
					}
				}
				// 校验会话合法性
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("token", token);
				TevglTraineeInfoService service = SpringContextUtils.getBean(TevglTraineeInfoService.class);
				List<TevglTraineeInfo> list = service.queryByMap(map);
				if(list.size() == 0) {
					Cookie imgCodeCookie = new Cookie(EvglGlobal.TOKEN_KEY_NAME,"");
					imgCodeCookie.setMaxAge(0);
				    imgCodeCookie.setPath("/");
					response.addCookie(imgCodeCookie);
					response.setStatus(502);
					return false;
				}
				// 会话没登陆，但是有合法的token，则做免登陆
				if(!LoginUtils.checkIsLogin(request)) {
					TevglTraineeInfo tevglTraineeInfo = list.get(0);
					session.setAttribute(EvglGlobal.LOGIN_SESSION_TOKEN, token);
					session.setAttribute(EvglGlobal.LOGIN_SESSION_KEY, tevglTraineeInfo);
					session.setAttribute(EvglGlobal.LOGIN_SESSION_ISLOGIN, "Y");
					if(StrUtils.isNotEmpty(tevglTraineeInfo.getMobile())) {
						session.setAttribute(EvglGlobal.LOGIN_SESSION_ISBIND, "Y");
					}else {
						String type = "";
						if(StrUtils.isNotEmpty(tevglTraineeInfo.getWxOpenid())) {
							type = "3";
						}else if(StrUtils.isNotEmpty(tevglTraineeInfo.getWeiboOpenid())) {
							type = "2";
						}else if(StrUtils.isNotEmpty(tevglTraineeInfo.getQqOpenid())) {
							type = "1";
						}
						session.setAttribute("ep_usertype", type);
					}
				}
            }
		}
		return super.preHandle(request, response, handler);
	}

	private void initKickOut(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String loginurl = ("localhost".equals(request.getServerName())?"http":"https")+"://" + request.getServerName()+
		(80 == request.getServerPort()?"":(":"+request.getServerPort()))+ request.getContextPath();
		PrintWriter out = response.getWriter();
		StringBuilder builder = new StringBuilder();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("sessionstatus", "kickout");
		response.setContentType("text/html;charset=UTF-8");
		Cookie imgCodeCookie = new Cookie("token","");
		imgCodeCookie.setMaxAge(0);
	    imgCodeCookie.setPath("/");
		response.addCookie(imgCodeCookie);
		builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
		builder.append("alert(\"您的账号已在其他地方登陆，请重新登陆\");");
		builder.append("window.top.location.href='" + loginurl + "?showlogin=1'");
		builder.append("</script>");
		out.print(builder.toString());
		out.close();
	}
	
	private void showMsg(HttpServletResponse response, String message){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.println(message);
        } catch (IOException e) {
            log.error("response error",e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
