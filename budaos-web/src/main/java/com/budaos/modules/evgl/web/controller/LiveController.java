package com.budaos.modules.evgl.web.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.medu.me.api.TmeduLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序直播接口
 * @author admin
 *
 */
@RestController
@RequestMapping("/live-api")
public class LiveController {

	@Autowired
	private TmeduLiveService tmeduLiveService;
	
	/**
	 * <p>获取直播间列表</p>
	 * @author zhuq
	 * @data 2021年10月15日
	 * @param start 从第几条开始
	 * @param limit 拉取多少条
	 * @return
	 */
	@GetMapping("/liveinfo")
	public R listPkgLevel(Integer start, Integer limit) {
		return tmeduLiveService.getliveinfo(start, limit);
	}
}
