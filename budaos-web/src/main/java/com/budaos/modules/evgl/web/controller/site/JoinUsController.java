package com.budaos.modules.evgl.web.controller.site;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.site.api.TevglSiteJoinUsService;
import com.budaos.modules.evgl.site.domain.TevglSiteJoinUs;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/site/join-us")
public class JoinUsController {

	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TevglSiteJoinUsService tevglSiteJoinUsService;
	
	@Autowired
	private DictService dictService;
	
	/**
	 * 从字典获取合作模式
	 * @return
	 */
	@RequestMapping("/queryCooperationModelList")
	public R queryCooperationModelList() {
		List<Map<String,Object>> dictList = dictService.getDictList("cooperation_model");
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 加入我们
	 * @param request
	 * @param tevglSiteJoinUs
	 * @return
	 */
	@PostMapping("/commit")
	@CheckSession
	public R commit(HttpServletRequest request, @RequestBody TevglSiteJoinUs tevglSiteJoinUs) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglSiteJoinUsService.joinUs(tevglSiteJoinUs, traineeInfo.getTraineeId());
	}
	
}
