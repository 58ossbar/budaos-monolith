package com.budaos.modules.evgl.site.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.domain.TevglSiteUpdateLog;

import java.util.Map;

/**
 * <p> Title: 布道师各版本更新日志</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglSiteUpdateLogService extends IBaseService<TevglSiteUpdateLog>{
	
	/**
	 * 查询更新日志
	 * @param params
	 * @return
	 */
	R queryUpdateLog(Map<String, Object> params);
	
}