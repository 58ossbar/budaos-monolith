package com.budaos.modules.evgl.web.controller.tchclass;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.tch.api.TevglTchTeacherService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("teachingCenter-api/teacher-api")
public class TeacherController {

	@Autowired
	private TevglTchTeacherService tevglTchTeacherService;
	
	/**
	 * 教师列表
	 * @param params
	 * @return
	 */
	@GetMapping("/queryTeacherList")
	@CheckSession
	public R queryTeacherList(HttpServletRequest request, @RequestParam Map<String, Object> params, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 只查询在职的教师
		params.put("state", "Y");
		params.put("loginUserId", traineeInfo.getTraineeId());
		return tevglTchTeacherService.querySimpleListMapByMap(params, type);
	}
	
}
