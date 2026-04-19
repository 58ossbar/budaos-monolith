package com.budaos.utils.tool;

import javax.servlet.http.HttpServletRequest;

/**
 */
public class WebUtil {

    private WebUtil(){}

    /**
     * 判断是否为ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request){
        boolean flag = false;
        String requestType = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equalsIgnoreCase(requestType)){
            flag = true;
        }
        return flag;
    }
}
