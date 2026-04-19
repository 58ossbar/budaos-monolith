package com.budaos.modules.evgl.web.controller.tchclass;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.tch.api.TevglTchRoomPereTraineeAnswerService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/activity/traineeAnswer")
public class TevglTchRoomPereTraineeAnswerController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchRoomPereTraineeAnswerController.class);
	@Autowired
	private TevglTchRoomPereTraineeAnswerService tevglTchRoomPereTraineeAnswerService;
	
	/**
	 *【学生端】学员抢答
	 * @author zhouyl加
	 * @date 2020年12月25日
	 * @param request
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @return
	 */
	@PostMapping("traineeAnswer")
	@CheckSession
	public R traineeAnswer(HttpServletRequest request, String ctId, String activityId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereTraineeAnswerService.traineeAnswer(ctId, activityId, traineeInfo.getTraineeId());
	}
}
