package com.budaos.modules.evgl.common;

import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.SpringContextUtils;
import com.budaos.utils.tool.StrUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 富文本上传拦截
 * @author huj
 * @create 2022-01-18 14:12
 * @email hujun@budaos.com
 */
public class UploadInterceptor extends HandlerInterceptorAdapter {

    private static Log log = LogFactory.getLog(SessionInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String token = getEvglToken(request);
        log.info("拦截富文本中的上传，判断token是否为空 => " + token);
        if(StrUtils.isEmpty(token)) {
            showMsg(response, "token为空，无法上传");
            response.setStatus(502);
            return false;
        }
        // 校验会话合法性
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", token);
		TevglTraineeInfoService service = SpringContextUtils.getBean(TevglTraineeInfoService.class);
		List<TevglTraineeInfo> list = service.queryByMap(map);
		if(list.size() == 0) {
			Cookie imgCodeCookie = new Cookie(EvglGlobal.TOKEN_KEY_NAME, "");
			imgCodeCookie.setMaxAge(0);
		    imgCodeCookie.setPath("/");
			response.addCookie(imgCodeCookie);
			response.setStatus(502);
			showMsg(response, "不合法的token，无法上传");
			return false;
		}
        return super.preHandle(request, response, handler);
    }
    
    private String getEvglToken(HttpServletRequest request) {
    	String token = request.getHeader(EvglGlobal.TOKEN_KEY_NAME);
        if(StrUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null)
                for (Cookie cookie : cookies) {
                    switch(cookie.getName()){
                        case EvglGlobal.TOKEN_KEY_NAME:
                        case "token":
                            token = cookie.getValue();
                            break;
                        default:
                            break;
                    }
                }
        }
        return token;
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
