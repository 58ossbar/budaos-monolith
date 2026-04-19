package com.budaos.modules.evgl.medu.me.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.medu.me.api.TmeduLiveService;
import com.budaos.modules.evgl.medu.me.service.weixin.CbBaseResult;
import com.budaos.modules.evgl.medu.me.service.weixin.WxaLiveAPI;
import com.budaos.modules.job.persistence.TsysSettingsMapper;
import com.budaos.modules.sys.domain.TsysSettings;
import com.budaos.utils.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 小程序直播服务类
 * @author zhuq
 *
 */
@Service
public class TmeduLiveServiceImpl implements TmeduLiveService{

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TmeduLiveServiceImpl.class);
	@Autowired
	private TsysSettingsMapper tsysSettingsMapper;
	
	/**
	 * 从数据库中获取小程序accessToken
	 * @return
	 */
	public String getAccessToken() {
		TsysSettings settings = tsysSettingsMapper.selectObjectById("wxaccesstoken");
		return settings == null ? null : settings.getSettingValue(); 
	}
	
	/**
	 * 获取直播间列表数据
	 * @param start 从第几条开始
	 * @param limit 拉取多少条
	 */
	@Override
	public R getliveinfo(Integer start, Integer limit) {
		CbBaseResult liveinfo = WxaLiveAPI.getliveinfo(getAccessToken(), start, limit);
		return R.ok().put(Constant.R_DATA, liveinfo);
	}

}
