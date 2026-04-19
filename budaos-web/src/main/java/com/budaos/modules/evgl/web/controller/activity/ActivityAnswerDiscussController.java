package com.budaos.modules.evgl.web.controller.activity;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityAnswerDiscussService;
import com.budaos.modules.evgl.activity.domain.TevglActivityAnswerDiscuss;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 3答疑/讨论
 * @author huj
 *
 */
@RestController
@RequestMapping("/activity/answerDiscuss")
public class ActivityAnswerDiscussController {

	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TevglActivityAnswerDiscussService tevglActivityAnswerDiscussService;
	
	/**
	 * 保存答疑/讨论
	 * @param request
	 * @param groupType system标识是系统字典默认分组，custom表示自定义分组
	 * @param tevglActivityAnswerDiscuss
	 * @return
	 */
	@PostMapping("/saveActivityAnswerDiscuss")
	@CheckSession
	public R saveActivityAnswerDiscuss(HttpServletRequest request, String groupType,
			@RequestBody TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		String loginUserId = traineeInfo.getTraineeId();
		String activityId = tevglActivityAnswerDiscuss.getActivityId();
		if (StrUtils.isEmpty(activityId)) {
			return tevglActivityAnswerDiscussService.saveAnswerDiscussInfo(tevglActivityAnswerDiscuss, loginUserId);
		} else {
			return tevglActivityAnswerDiscussService.updateAnswerDiscussInfo(tevglActivityAnswerDiscuss, loginUserId);
		}
	}
	
	/**
	 * 查看答疑/讨论信息
	 * @param activityId
	 * @return
	 */
	@GetMapping("/viewAnswerDiscussInfo")
	@CheckSession
	public R viewAnswerDiscussInfo(String activityId) {
		return tevglActivityAnswerDiscussService.viewAnswerDiscussInfo(activityId);
	}
	
	/**
	 * 点赞与取消点赞
	 * @param msgId
	 * @param value Y点赞N取消点赞
	 * @param token
	 * @return
	 */
	@PostMapping("/clickLike")
	@CheckSession
	public R clickLike(HttpServletRequest request, String msgId, String value) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if ("Y".equals(value)) {
			return tevglActivityAnswerDiscussService.clickLike(msgId, traineeInfo.getTraineeId());
		} else {
			return tevglActivityAnswerDiscussService.cancelLike(msgId, traineeInfo.getTraineeId());
		}
	}
}
