package com.budaos.modules.evgl.medu.me.api;

import com.budaos.core.baseclass.domain.R;

public interface TmeduLiveService {

	/**
	 * 获取直播间列表数据
	 * @param start 从第几条开始
	 * @param limit 拉取多少条
	 */
	public R getliveinfo(Integer start, Integer limit);
}
