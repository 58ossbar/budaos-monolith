package com.budaos.modules.evgl.web.controller.tchclass;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomSettingService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 课堂相关设置
 * @author huj
 *
 */
@RestController
@RequestMapping("/classroom-api/setting")
public class ClassroomSettingController {
	
	@Autowired
	private TevglTchClassroomSettingService tevglTchClassroomSettingService;
	
	/**
	 * 保存设置
	 * @param request
	 * @param radio1
	 * @param radio2
	 * @return
	 */
	@RequestMapping("saveSetting")
	public R saveSetting(HttpServletRequest request, String ctId, String radio1, String radio2) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomSettingService.saveSetting(ctId, traineeInfo.getTraineeId(), radio1, radio2);
	}
	
	@RequestMapping("viewSetting")
	public R viewSetting(String ctId) {
		return tevglTchClassroomSettingService.viewSetting(ctId);
	}

}
