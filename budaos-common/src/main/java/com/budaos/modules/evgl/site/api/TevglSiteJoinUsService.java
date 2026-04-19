package com.budaos.modules.evgl.site.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.domain.TevglSiteJoinUs;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglSiteJoinUsService extends IBaseService<TevglSiteJoinUs>{
	
	/**
	 * 加入我们
	 * @param tevglSiteJoinUs
	 * @param loginUserId
	 * @return
	 */
	R joinUs(TevglSiteJoinUs tevglSiteJoinUs, String loginUserId);
	
}