package com.budaos.modules.evgl.web.controller.im;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityAnswerDiscussService;
import com.budaos.modules.evgl.activity.domain.TevglActivityAnswerDiscuss;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.pkg.api.TevglPkgActivityRelationService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.im.service.TimGroupService;
import com.budaos.utils.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cbim/group")
public class TimGroupController {

	@Autowired
	private TimGroupService timGroupService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TevglActivityAnswerDiscussService tevglActivityAnswerDiscussService;
	@Autowired
	private TevglPkgActivityRelationService tevglPkgActivityRelationService;
	@Autowired
	private TevglTchClassroomService tevglTchClassroomService;
	
	
	/**
	 * 查看群基本信息
	 * @param groupId
	 * @return
	 */
	@GetMapping("/viewInfo")
	@CheckSession
	public R viewInfo(HttpServletRequest request, String groupId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return R.ok().put(Constant.R_DATA, timGroupService.selectObjectById(groupId));
	}
	
	/**
	 * 查看群成员
	 * @param groupId
	 * @param pageNum
	 * @param pageSize
	 * @param token
	 * @return
	 */
	@GetMapping("/queryGroupUserList")
	@CheckSession
	public R queryGroupUserList(HttpServletRequest request, String username, String groupId, Integer pageNum, Integer pageSize, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss = tevglActivityAnswerDiscussService.selectObjectByGroupId(groupId);
		if (tevglActivityAnswerDiscuss == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("activityId", groupId);
			map.put("pkgId", pkgId);
			List<TevglPkgActivityRelation> list = tevglPkgActivityRelationService.selectListByMap(map);
			if (list.size() > 0) {
				groupId = list.get(0).getGroupId();
			}
		}
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("groupId", groupId);
		params.put("pageNum", pageNum = pageNum == null ? 1 : pageNum);
		params.put("pageSize", pageSize = pageSize == null ? 10 : pageSize);
		//return timGroupUserService.query(params);
		return tevglTchClassroomService.queryImGroupUserList(params);
	}
	
}
