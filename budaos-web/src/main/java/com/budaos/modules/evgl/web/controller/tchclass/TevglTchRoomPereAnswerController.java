package com.budaos.modules.evgl.web.controller.tchclass;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.tch.api.TevglTchRoomPereAnswerService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/activity/pere")
public class TevglTchRoomPereAnswerController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchRoomPereAnswerController.class);
	@Autowired
	private TevglTchRoomPereAnswerService tevglTchRoomPereAnswerService;
	
	/**
	 * 【教师端】新增课堂表现
	 * @author zhouyl加
	 * @date 2020年12月22日
	 * @param request
	 * @param pkgId 教学包id
	 * @param activityType 消息类型
	 * @param ctId 课堂id
	 * @param answerNum 抢答人数
	 * @return
	 */
	@PostMapping("launchAnswer")
	@CheckSession
	public R launchAnswer(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.launchAnswer(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 修改抢答
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param request
	 * @param pkgId 教学包id
	 * @param answerId 抢答id
	 * @param answerNum 抢答人数
	 * @return
	 */
	/*@PostMapping("updateAnswer")
	@CheckSession
	public R updateAnswer(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.updateAnswer(jsonObject, traineeInfo.getTraineeId());
	}*/
	
	/**
	 * 开始活动
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param ctId 课堂id
	 * @param activityId 活动id(抢答id/随机选人id/手动选人id)
	 * @param loginUserId 登录用户id
	 * @param activityEndTime 结束时间
	 * @return
	 */
	/*@PostMapping("startAnswerActivity")
	@CheckSession
	public R startAnswerActivity(HttpServletRequest request, String ctId, String activityId, String activityEndTime) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.startAnswerActivity(ctId, activityId, traineeInfo.getTraineeId(), activityEndTime);
	}*/
	
	/**
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param ctId 课堂id
	 * @param activityId 活动id(随机选人id)
	 * @param loginUserId 登录用户id
	 * @return
	 */
	/*@PostMapping("endAnswerActivity")
	@CheckSession
	public R endAnswerActivity(HttpServletRequest request, String ctId, String activityId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.endAnswerActivity(ctId, activityId, traineeInfo.getTraineeId());
	}*/
	
	/**
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * 手动选人以及随机选人
	 * @param HttpServletRequest request
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("selectPeopleToAnswer")
	@CheckSession
	public R selectPeopleToAnswer(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.selectPeopleToAnswer(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询该课堂下的所有成员不分页
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param ctId 课堂id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	@GetMapping("selectClassroomTraineeInfo")
	@CheckSession
	public R selectClassroomTraineeInfo(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.selectClassroomTraineeInfo(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 删除抢答活动
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param activityId 活动id
	 * @param pkgId 教学包id
	 * @param loginUserId 登录用户id
	 * @return
	 * @throws BudaosException
	 */
	/*@PostMapping("deleteAnswerActivity")
	@CheckSession
	public R deleteAnswerActivity(HttpServletRequest request, String activityId, String pkgId) throws BudaosException {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.deleteAnswerActivity(activityId, pkgId, traineeInfo.getTraineeId());
	}*/
	
	/**
	 * 【教师端】查询课堂下抢答活动的抢答人员
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	@GetMapping("answerActivityTraineeInfo")
	@CheckSession
	public R answerActivityTraineeInfo(HttpServletRequest request, String ctId, String activityId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.answerActivityTraineeInfo(ctId, activityId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询抢答活动的总体结果
	 * @author zhouyl加
	 * @date 2020年12月26日
	 * @param request
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @return
	 */
	@GetMapping("answerSummaryResults")
	@CheckSession
	public R answerSummaryResults(HttpServletRequest request, String ctId, String activityId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchRoomPereAnswerService.answerSummaryResults(ctId, activityId, traineeInfo.getTraineeId());
	}
}
