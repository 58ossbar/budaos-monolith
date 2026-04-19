package com.budaos.modules.evgl.web.controller.tchclass;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomTraineeCheckService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 课堂-审核成员
 * @author huj
 *
 */
@RestController
@RequestMapping("teachingCenter-api/classroom-trainee-check-api")
public class ClassroomTraineeCheckController {

	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TmeduApiTokenService tmeduApiTokenService;
	@Autowired
	private TevglTchClassroomTraineeCheckService tevglTchClassroomTraineeCheckService;
	
	/**
	 * 待审核的成员列表
	 * @param params {'ctId':'', 'traineeName':'', 'pageNum':1, 'pageSize':10}
	 * @param token
	 * @return
	 */
	@GetMapping("/queryTraineeList")
	@CheckSession
	public R queryTraineeList(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		params.put("isPass", "N");
		return tevglTchClassroomTraineeCheckService.queryTraineeList(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 将待审核的学员加入成课程成员
	 * @param jsonObject {'token':'', 'ctId':'', 'traineeIds':[]}
	 * @return
	 */
	@PostMapping("/setTraineeToClassroom")
	@CheckSession
	public R setTraineeToClassroom(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomTraineeCheckService.setTraineeToClassroom(jsonObject, traineeInfo.getTraineeId());
	}
}
