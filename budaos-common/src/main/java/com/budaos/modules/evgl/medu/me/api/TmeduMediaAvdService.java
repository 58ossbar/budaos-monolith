package com.budaos.modules.evgl.medu.me.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.medu.me.domain.TmeduMediaAvd;

import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TmeduMediaAvdService extends IBaseService<TmeduMediaAvd>{
	/**
	 * <p>新增</p>
	 * @author znn
	 * @data 2020年5月31日
	 * @param tevglSiteAvd
	 * @param attachId
	 * @return
	 */
	R saveHasAttach(TmeduMediaAvd tmeduMediaAvd, String attachId);
	
	/**
	 * <p>修改</p>
	 * @author znn
	 * @data 2020年5月31日
	 * @param tevglSiteAvd
	 * @param attachId
	 * @return
	 */
	R updateHasAttach(TmeduMediaAvd tmeduMediaAvd, String attachId);
	
	/**
	 * <p>更新状态</p>
	 * @author znn
	 * @data 2020年5月31日
	 * @param tevglSiteAvd
	 * @return
	 */
	R updateState(TmeduMediaAvd tmeduMediaAvd);
	/**
	 * <p>首页查询广告轮播图</p>
	 * @author znn
	 * @data 2020年6月8日
	 * @param tevglSiteAvd
	 * @return
	 */
	R queryShowIndex(Map<String, Object> map);
	
}