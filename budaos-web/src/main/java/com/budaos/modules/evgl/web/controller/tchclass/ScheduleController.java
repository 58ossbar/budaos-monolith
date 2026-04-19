package com.budaos.modules.evgl.web.controller.tchclass;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.tch.api.TevglTchScheduleClassService;
import com.budaos.modules.evgl.tch.api.TevglTchScheduleService;
import com.budaos.modules.evgl.tch.params.TevglTchScheduleParams;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授课安排
 * @author huj
 *
 */
@RestController
@RequestMapping("/site/schedule")
public class ScheduleController {

	@Autowired
	private TevglTchScheduleService tevglTchScheduleService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglTchScheduleClassService tevglTchScheduleClassService;
	
	/**
	 * 根据条件查询课表
	 * @param request
	 * @param params
	 * @return
	 */
	@PostMapping("/queryScheduleDataForWeb")
	@CheckSession
	public R queryScheduleDataForWeb(HttpServletRequest request, @RequestBody TevglTchScheduleParams params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchScheduleService.queryScheduleDataForWeb(params, traineeInfo);
	}
	
	/**
	 * 获取字典
	 * @return
	 */
	@RequestMapping("/getDictScheduleState")
	public R getDictScheduleState() {
		List<Map<String,Object>> dictList = dictService.getDictList("scheduleState");
		Map<String, Object> defaultMap = new HashMap<String, Object>();
		defaultMap.put("dictId", Identities.uuid());
		defaultMap.put("dictCode", "");
		defaultMap.put("dictValue", "全部类型");
		dictList.add(0, defaultMap);
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 查询该用户所教，上课的班级
	 * @return
	 */
	@PostMapping("/findMyClassList")
	@CheckSession
	public R findMyClassList(HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchScheduleClassService.findMyClassList(traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询我已经加入的班级
	 * @return
	 */
	@PostMapping("/findMyJoinedClassList")
	@CheckSession
	public R findMyJoinedClassList(HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchScheduleClassService.findMyJoinedClassList(traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询该用户上课的老师
	 * @return
	 */
	@PostMapping("/findMyTeacherList")
	@CheckSession
	public R findMyTeacherList(HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchScheduleClassService.findMyTeacherList(traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询教室列表
	 * @param params
	 * @return R
	 */
	@GetMapping("/queryTrainingRoomList")
	public R queryTrainingRoomList(@RequestParam Map<String, Object> params) {
		return tevglTchScheduleService.queryTrainingRoomList(params);
	}
}
