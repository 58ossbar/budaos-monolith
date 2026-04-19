package com.budaos.modules.evglsp.rest.controller.activity;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;
import com.budaos.modules.evgl.tch.api.TevglTchRoomPereAnswerService;
import com.budaos.modules.evgl.tch.api.TevglTchRoomPereTraineeAnswerService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/activity-api/performance")
public class ActivityRoomPerformanceController {

	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TmeduApiTokenService tmeduApiTokenService;
	@Autowired
	private TevglTchRoomPereAnswerService tevglTchRoomPereAnswerService;
	@Autowired
	private TevglTchRoomPereTraineeAnswerService tevglTchRoomPereTraineeAnswerService;
	
	/**
	 * 【教师端】新增课堂表现
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("launchAnswer")
	public R launchAnswer(@RequestBody JSONObject jsonObject) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(jsonObject.getString("token"));
		if(te == null){
			return R.error(401, "Invalid token");
		}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchRoomPereAnswerService.launchAnswer(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 修改信息
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	/*@PostMapping("updateAnswer")
	public R updateAnswer(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(jsonObject.getString("token"));
		if(te == null){
			return R.error(401, "Invalid token");
		}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchRoomPereAnswerService.updateAnswer(jsonObject, traineeInfo.getTraineeId());
	}*/
	
	/**
	 * 学生抢答
	 * @param token
	 * @return
	 */
	@PostMapping("traineeAnswer")
	public R traineeAnswer(String token, String ctId, String activityId) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchRoomPereTraineeAnswerService.traineeAnswer(ctId, activityId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询抢答成功的学员
	 * @param token
	 * @param ctId
	 * @param activityId
	 * @return
	 */
	@PostMapping("querySuccessfulTraineeList")
	public R querySuccessfulTraineeList(String token, String ctId, String activityId) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchRoomPereTraineeAnswerService.querySuccessfulTraineeList(ctId, activityId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询该课堂下的所有成员不分页
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * @param ctId 课堂id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	@GetMapping("selectClassroomTraineeList")
	public R selectClassroomTraineeList(String token, String ctId) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchRoomPereAnswerService.selectClassroomTraineeInfo(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 手动选人以及随机选人 (评分，给经验值)
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/giveScore")
	public R giveScore(@RequestBody JSONObject jsonObject) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(jsonObject.getString("token"));
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchRoomPereAnswerService.selectPeopleToAnswer(jsonObject, traineeInfo.getTraineeId());
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
	@PostMapping("answerSummaryResults")
	public R answerSummaryResults(String token, String ctId, String activityId) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchRoomPereAnswerService.answerSummaryResults(ctId, activityId, traineeInfo.getTraineeId());
	}
}
