package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysLoginLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登录日志接口
 * 
 * @author huangwb
 * @date 2019-05-08 17:30
 */
public interface TsysLoginLogService {

	/**
	 * 删除操作
	 * 
	 * @date 2019-05-08 17:30
	 * @param ids
	 * @return
	 */
	R delete(String[] ids);

	/**
	 * 查询操作
	 * 
	 * @date 2019-05-08 17:30
	 * @param map
	 * @return
	 */
	R selectAllByMap(Map<String, Object> map);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月31日
	 * @param tsysLoginLog
	 */
	R save(TsysLoginLog tsysLoginLog);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月31日
	 * @param tsysLoginLog
	 * @return
	 */
	R update(TsysLoginLog tsysLoginLog);
	
    /**
     * 登录失败时，记录下日志
     * @param request
     * @param message
     * @return
     */
    R saveFailMessage(HttpServletRequest request, String message);

    /**
     * 登录成功时，记录下日志
     * @param request
     * @param message
     * @param userRealname
     * @return
     */
    R saveSuccessMessage(HttpServletRequest request, String message, String userRealname);
}
