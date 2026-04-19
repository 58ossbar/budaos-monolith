package com.budaos.modules.evgl.web.controller.activity;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityLiveService;
import com.budaos.modules.evgl.activity.domain.TevglActivityLive;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 7轻直播
 * @author huj
 *
 */
@RestController
@RequestMapping("/activity/live")
public class ActivityLiveController {

	@Autowired
	private TevglActivityLiveService tevglActivityLiveService;
	
	@PostMapping("save")
	@CheckSession
	public R saveLive(HttpServletRequest request, @RequestBody TevglActivityLive tevglActivityLive) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (StrUtils.isEmpty(tevglActivityLive.getActivityId())) {
			return tevglActivityLiveService.saveLive(tevglActivityLive, traineeInfo.getTraineeId());
		} else {
			return tevglActivityLiveService.updateLive(tevglActivityLive, traineeInfo.getTraineeId());
		}
	}
	
}
