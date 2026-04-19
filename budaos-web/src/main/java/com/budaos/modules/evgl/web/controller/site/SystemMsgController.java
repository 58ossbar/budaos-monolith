package com.budaos.modules.evgl.web.controller.site;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.site.api.TevglSiteSysMsgService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/site/sys-msg")
public class SystemMsgController {

	@Autowired
	private TevglSiteSysMsgService tevglSiteSysMsgService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	
	@GetMapping("/query")
	@CheckSession
	public R query(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglSiteSysMsgService.queryMyMsg(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 发送通知
	 * @param token
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/sendText")
	@CheckSession
	public R sendText(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglSiteSysMsgService.sendText(jsonObject, traineeInfo.getTraineeId());
	}
}
