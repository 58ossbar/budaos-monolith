package com.budaos.modules.evgl.web.controller.tchclass;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivitySigninTraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/teachingCenter-api/sign-api")
public class SigninController {

	@Autowired
	private TevglActivitySigninTraineeService tevglActivitySigninTraineeService;
	
	/**
	 * 学员签到详情列表
	 * @param ctId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/queryTraineeSigninDetail")
	public R queryTraineeSigninDetail(@RequestParam Map<String, Object> params) {
		return tevglActivitySigninTraineeService.queryTraineeSigninDetail(params);
	}
	
}
