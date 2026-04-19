package com.budaos.modules.evgl.web.controller.tchclass;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomGroupMemberService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomGroupService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomTraineeService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomGroup;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("teachingCenter-api/classroom-group-api")
public class ClassroomGroupController {

	@Autowired
	private TmeduApiTokenService tmeduApiTokenService;
	@Autowired
	private TevglTchClassroomGroupService tevglTchClassroomGroupService;
	@Autowired
	private TevglTchClassroomGroupMemberService tevglTchClassroomGroupMemberService;
	@Autowired
	private TevglTchClassroomTraineeService tevglTchClassroomTraineeService;
	@Autowired
	private DictService dictService;
	
	/**
	 * 课堂小组列表
	 * @param request
	 * @param ctId
	 * @return
	 */
	@GetMapping("/listClassroomGroupSimpleInfo")
	@CheckSession
	public R listClassroomGroupSimpleInfo(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.listClassroomGroupSimpleInfo(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 保存课堂小组信息
	 * @param request
	 * @param tevglTchClassroomGroup
	 * @return
	 */
	@PostMapping("/saveClassroomGroupInfo")
	@CheckSession
	public R saveClassroomGroupInfo(HttpServletRequest request, 
			@RequestBody TevglTchClassroomGroup tevglTchClassroomGroup) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.saveClassroomGroupInfo(tevglTchClassroomGroup, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看课堂小组信息
	 * @param gpId 小组主键
	 * @param traineeName 学员名称
	 * @param pageNum 当前页码
	 * @param pageSize 每页显示数 
	 * @return
	 */
	@GetMapping("/viewClassroomGroupInfo")
	@CheckSession
	public R viewClassroomGroupInfo(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.viewClassroomGroupInfo(params.get("gpId").toString(), 1, params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 只会查询未加入课堂小组的成员
	 * @param ctId
	 * @param traineeName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/listClassroomTraineeExcludeJoinedGroup")
	@CheckSession
	public R listClassroomTraineeExcludeJoinedGroup(String ctId, String traineeName, Integer pageNum, Integer pageSize, String withOutPage) {
		pageNum = pageNum == null ? 1 : pageNum;
		pageSize = pageSize == null ? 10 : pageSize;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ctId", ctId);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		if (StrUtils.isNotEmpty(traineeName) && !"null".equals(traineeName)) {
			params.put("traineeName", traineeName);
		}
		if (StrUtils.isNotEmpty(withOutPage)) {
			params.put("withOutPage", withOutPage);
		}
		return tevglTchClassroomTraineeService.listClassroomTraineeExcludeJoinedGroup(params);
	}
	
	/**
	 * 将学员加入小组
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/setTraineeToGroup")
	@CheckSession
	public R setTraineeToGroup(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.setTraineeToGroup(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 将成员移出小组
	 * @param request
	 * @param gmId
	 * @return
	 */
	@GetMapping("/deleteClassroomGroupMember")
	@CheckSession
	public R deleteClassroomGroupMember(HttpServletRequest request, String gmId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupMemberService.deleteClassroomGroupMember(gmId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 批量将成员移出小组
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/removeGroupMembers")
	@CheckSession
	public R removeGroupMembers(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupMemberService.removeGroupMembers(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 将学员设为组长
	 * @param request
	 * @param gpId
	 * @param traineeId
	 * @return
	 */
	@PostMapping("/setTraineeToLeader")
	@CheckSession
	public R setTraineeToLeader(HttpServletRequest request, String gpId, String traineeId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.setTraineeToLeader(gpId, traineeId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 批量保存小组信息
	 * @param request
	 * @param ctId
	 * @return
	 */
	@PostMapping("/saveGroupInfo")
	@CheckSession
	public R saveGroupInfo(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.saveGroupInfo(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 删除课堂小组
	 * @param request
	 * @param gpId
	 * @return
	 */
	@PostMapping("/deleteClassroomGroup")
	@CheckSession
	public R deleteClassroomGroup(HttpServletRequest request, String gpId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.deleteClassroomGroup(gpId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 重命名小组
	 * @param request
	 * @param gpId
	 * @return
	 */
	@PostMapping("/rename")
	@CheckSession
	public R rename(HttpServletRequest request, String gpId, String name) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupService.rename(gpId, name, traineeInfo.getTraineeId());
	}
	
	/**
	 * 获取课堂小组成员身份列表
	 * @return
	 */
	@GetMapping("/getIdentityList")
	public R getIdentityList() {
		List<Map<String,Object>> dictList = dictService.getDictList("group_member_identity");
		dictList = dictList.stream().sorted((h1, h2) -> h1.get("dictCode").toString().compareTo(h2.get("dictCode").toString())).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 批量设置小组成员身份
	 * @param request
	 * @param ctId
	 * @param gmId
	 * @param dictCode
	 * @return
	 */
	@PostMapping("/setIdentity")
	@CheckSession
	public R setIdentity(HttpServletRequest request, String ctId, String gmId, String dictCode) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupMemberService.setIdentity(ctId, gmId, dictCode, traineeInfo.getTraineeId());
	}
	
	/**
	 * 设置小组成员身份
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/setIdentityBatch")
	@CheckSession
	public R setIdentityBatch(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomGroupMemberService.setIdentityBatch(jsonObject, traineeInfo.getTraineeId());
	}
}
