package com.budaos.modules.core.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@Component
public class LoginUtils {

	@Autowired
	private HttpServletRequest request;
	
	/**
	 * <p>获取当前登陆用户</p>
	 * @author huj
	 * @data 2019年5月16日
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TsysUserinfo getLoginUser(HttpServletRequest request) {
		// 优先从 Session 获取（mgr 应用使用 Session 认证）
		if (request != null) {
			Object sessionUser = request.getSession().getAttribute("LOGIN_USER");
			if (sessionUser instanceof TsysUserinfo) {
				return (TsysUserinfo) sessionUser;
			}
		}
		
		// 如果 Session 中没有，尝试从 Spring Security 获取（OAuth2 模式）
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || authentication.getPrincipal() == null) {
				return null;
			}
			LinkedHashMap<String, ?> lhm = (LinkedHashMap)((LinkedHashMap)((LinkedHashMap)authentication.getPrincipal()).get("principal")).get("tsysUserinfo");
			TsysUserinfo tsysUserinfo = new TsysUserinfo();
			if(lhm == null || lhm.isEmpty()) {
				return null;
			}
			BeanUtils.populate(tsysUserinfo, lhm);
			return tsysUserinfo;
		}catch (Exception e) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null || authentication.getPrincipal() == null) {
					return null;
				}
				String string = mapper.writeValueAsString(authentication.getPrincipal());
				JSONObject obj = JSONObject.parseObject(string);
				return mapper.readValue(obj.getString("tsysUserinfo"), TsysUserinfo.class);
			} catch (Exception e1) {
				return null;
			}
		}
	}
	
	public TsysUserinfo getLoginUser() {
		return getLoginUser(request);
	}
	
	/**
	 * <p>获取当前登陆用户ID</p>
	 * @author huj
	 * @data 2019年5月15日
	 * @return 返回当前登陆用户ID
	 */
	public String getLoginUserId() {
		TsysUserinfo user = getLoginUser(request);
		if (user != null) {
			return user.getUserId();
		}
		return null;
	}
}
