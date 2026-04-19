package com.budaos.modules.evglsp.rest.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;
import com.budaos.modules.evgl.site.api.TevglNumsService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息（如未读的点赞数、系统通知数等）
 * @author admin
 *
 */
@RestController
@RequestMapping("/wx/msgnum-api")
public class MsgNumController {

	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TmeduApiTokenService tmeduApiTokenService;
	@Autowired
	private TevglNumsService tevglNumsService;
	
	/**
	 * 统计这个人的一些未读数
	 * @param token
	 * @return
	 */
	@GetMapping("/queryNums")
	public R queryNums(String token) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
			return R.error(401, "Invalid token");
		}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglNumsService.queryNums(traineeInfo.getTraineeId());
	}
	
}
