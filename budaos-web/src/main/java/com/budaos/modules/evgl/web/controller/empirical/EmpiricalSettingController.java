package com.budaos.modules.evgl.web.controller.empirical;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.empirical.api.TevglEmpiricalSettingService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 课堂经验值设置
 * @author huj
 *
 */
@RestController
@RequestMapping("/classroom-api/empirical")
public class EmpiricalSettingController {
 
	@Autowired
	private TevglEmpiricalSettingService tevglEmpiricalSettingService;
	
	/**
	 * 经验值设置页面
	 * @param request
	 * @param ctId
	 * @return
	 */
	@PostMapping("/viewEmpiricalSetting")
	@CheckSession
	public R viewEmpiricalSetting(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglEmpiricalSettingService.viewEmpiricalSetting(ctId, traineeInfo.getTraineeId());
	}
	
	@PostMapping("saveSetting")
	@CheckSession
	public R saveSetting(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglEmpiricalSettingService.saveSessting(jsonObject, traineeInfo.getTraineeId());
	}
}
