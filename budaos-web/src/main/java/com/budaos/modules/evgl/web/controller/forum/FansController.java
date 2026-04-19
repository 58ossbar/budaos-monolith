package com.budaos.modules.evgl.web.controller.forum;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.forum.api.TevglForumAttentionService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 粉丝控制类，关注，取关
 * @author admin
 *
 */
@RestController
@RequestMapping("/site/forum/fans")
public class FansController {
	
	@Autowired
	private TevglForumAttentionService tevglForumAttentionService;

	/**
	 * 关注/取消关注博主
	 * @author zhouyl加
	 * @date 2021年3月3日
	 * @param request
	 * @param traineeId
	 * @return
	 */
	@PostMapping("follow")
	@CheckSession
	public R follow(HttpServletRequest request, String traineeId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumAttentionService.follow(traineeId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 展示我的关注列表
	 * @author zhouyl加
	 * @date 2021年3月3日
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("queryMyFollowList")
	@CheckSession
	public R queryMyFollowList(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumAttentionService.queryMyFollowList(params, traineeInfo.getTraineeId());
	}
}
