package com.budaos.modules.job;

import com.budaos.modules.sys.api.TsysSettingsService;
import com.budaos.modules.sys.domain.TsysSettings;
import com.budaos.utils.tool.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.token.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序更新access_token的定时任务
 * @author huj
 *
 */
@Component("accessTokenJob")
public class AccessTokenJob {

	private Logger logger = LoggerFactory.getLogger(getClass());
	/**类型**/
	private static String SETTING_TYPE = "wx";
	/**编码**/
	private static String SETTING_CODE = "wxAccessToken";
	/**名称**/
	private static String SETTING_NAME = "布道师小程序AccessToken";
	// 小程序
	@Value("${com.budaos.appid:}")
	private String appid;
	@Value("${com.budaos.secret:}")
	private String secret;
	// 微信公众号
	@Value("${com.budaos.appid-official:}")
	private String appidOfficial;
	@Value("${com.budaos.secret-official:}")
	private String secretOfficial;
	@Autowired
	private TsysSettingsService tsysSettingsService;
	
	
	
	public void doJob() {
		
		logger.debug("正在执行：AccessTokenJob");
		Token token = TokenAPI.token(appid, secret);
		String access_token = token.getAccess_token();
		logger.debug("access_token:" + access_token);
		
		Map<String, Object> map = new HashMap<>();
		map.put("settingType", SETTING_TYPE);
		List<TsysSettings> tsysSettingsList = tsysSettingsService.selectListByMap(map);
		// 若没有记录则插入
		if (tsysSettingsList == null || tsysSettingsList.size() == 0) {
			TsysSettings t = new TsysSettings();
			t.setSettingType(SETTING_TYPE);
			t.setSettingCode(SETTING_CODE);
			t.setSettingName(SETTING_NAME);
			t.setSettingValue(access_token);
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setState("1");
			t.setCreateUserId("1");
			tsysSettingsService.save(t);
		} else { // 否则更新
			if (tsysSettingsList.size() > 0) {
				TsysSettings tsysSettings = tsysSettingsList.get(0);
				tsysSettings.setSettingValue(access_token);
				tsysSettings.setUpdateTime(DateUtils.getNowTimeStamp());
				tsysSettings.setUpdateUserId("1");
				tsysSettingsService.update(tsysSettings);
			}
		}
	}
	
	/**
	 * 每隔两小时更新公众号的access_token
	 */
	public void updateOfficialAccessToken() {
		Token token = TokenAPI.token(appidOfficial, secretOfficial);
		String access_token = token.getAccess_token();
		Map<String, Object> map = new HashMap<>();
		map.put("settingType", "official");
		List<TsysSettings> tsysSettingsList = tsysSettingsService.selectListByMap(map);
		// 若没有记录则插入
		if (tsysSettingsList == null || tsysSettingsList.size() == 0) {
			TsysSettings t = new TsysSettings();
			t.setSettingType("official");
			t.setSettingCode("weChatOfficialAccountAccessToken");
			t.setSettingName("创蓝微信公众号AccessToken");
			t.setSettingValue(access_token);
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setState("1");
			t.setCreateUserId("1");
			tsysSettingsService.save(t);
		} else { // 否则更新
			if (tsysSettingsList.size() > 0) {
				TsysSettings tsysSettings = tsysSettingsList.get(0);
				tsysSettings.setSettingValue(access_token);
				tsysSettings.setUpdateTime(DateUtils.getNowTimeStamp());
				tsysSettings.setUpdateUserId("1");
				tsysSettingsService.update(tsysSettings);
			}
		}
	}
}
