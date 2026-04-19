package com.budaos.modules.evgl.tch.service;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTraineeCheck;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeCheckMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>课堂成员管理 Service 实现</p>
 * <p>职责：负责学员加入/退出、助教设置、课堂移交等成员相关操作</p>
 * 
 * @author budaos
 * @version 1.0
 */
@Service
public class TevglTchClassroomMemberServiceImpl implements TevglTchClassroomMemberService {
	
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	
	@Autowired
	private TevglTchClassroomTraineeCheckMapper tevglTchClassroomTraineeCheckMapper;
	
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	
	@Override
	@Transactional
	public R setTraineeToTeachingAssistant(Map<String, Object> map) {
		String traineeId = (String) map.get("traineeId");
		String ctId = (String) map.get("ctId"); // 课堂 (教室) 主键
		String loginUserId = (String) map.get("loginUserId");
		
		// 合法性校验
		R r = checkIsPass(traineeId, ctId, loginUserId);
		Integer code = (Integer) r.get("code");
		if (code != 0) {
			return r;
		}
		
		TevglTchClassroom classroom = (TevglTchClassroom) r.get("classroom");
		if (classroom != null) {
			classroom.setTraineeId(traineeId);
			tevglTchClassroomMapper.update(classroom);
			
			// 如果此人有待审核记录，直接更新为通过
			Map<String, Object> params = new HashMap<>();
			params.put("ctId", ctId);
			params.put("traineeId", traineeId);
			params.put("isPass", "N");
			List<TevglTchClassroomTraineeCheck> list = tevglTchClassroomTraineeCheckMapper.selectListByMap(params);
			if (list != null && list.size() > 0) {
				TevglTchClassroomTraineeCheck traineeCheck = list.get(0);
				traineeCheck.setIsPass("Y");
				tevglTchClassroomTraineeCheckMapper.update(traineeCheck);
			}
			
			params.clear();
			params.put("ctId", ctId);
			params.put("traineeId", traineeId);
			params.put("state", "N");
			List<TevglTchClassroomTrainee> traineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
			if (traineeList != null && traineeList.size() > 0) {
				TevglTchClassroomTrainee classroomTrainee = traineeList.get(0);
				classroomTrainee.setState("Y");
				tevglTchClassroomTraineeMapper.update(classroomTrainee);
				
				// 更新课堂人数
				TevglTchClassroom room = new TevglTchClassroom();
				room.setCtId(ctId);
				room.setStudyNum(1);
				tevglTchClassroomMapper.plusNum(room);
			}
		}
		
		return R.ok("助教设置成功");
	}
	
	/**
	 * 合法性校验（设为助教所用）
	 * 
	 * @param traineeId 学员 ID
	 * @param ctId 课堂 ID
	 * @param loginUserId 登录用户 ID
	 * @return 校验结果
	 */
	private R checkIsPass(String traineeId, String ctId, String loginUserId) {
		if (StrUtils.isEmpty(traineeId)) {
			return R.error("参数 traineeId 为空");
		}
		if (StrUtils.isEmpty(ctId)) {
			return R.error("参数 ctId 为空");
		}
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("参数 loginUserId 为空");
		}
		
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("课堂已不存在");
		}
		if (!"Y".equals(classroom.getState())) {
			return R.error("课堂已无效");
		}
		
		// TODO: 需要注入 roomUtils 或使用其他权限校验方式
		// boolean hasAuth = roomUtils.hasOperatingAuthorization(classroom, loginUserId);
		// if (!hasAuth) {
		//     return R.error("非法操作，没有权限！");
		// }
		
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", classroom.getCtId());
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		List<String> traineeIds = classroomTraineeList.stream().map(classroomTrainee -> classroomTrainee.getTraineeId())
				.collect(Collectors.toList());
		if (!traineeIds.contains(traineeId)) {
			return R.error("该学员不属于此课堂，无法设为助教");
		}
		
		return R.ok().put("classroom", classroom);
	}
	
	@Override
	public R cancelTeachingAssistant(String ctId, String traineeId, String loginUserId) {
		// TODO: 后续从原 3440 行 Service 中迁移完整业务逻辑
		return R.ok("待实现");
	}
	
	@Override
	public R oneClickToJoinClassroom(JSONObject jsonObject, String ctId, String loginUserId) {
		// TODO: 后续从原 3440 行 Service 中迁移完整业务逻辑
		return R.ok("待实现");
	}
	
	@Override
	public R turnOver(String ctId, String traineeId) {
		// TODO: 后续从原 3440 行 Service 中迁移完整业务逻辑
		return R.ok("待实现");
	}
	
	@Override
	public R queryImGroupUserList(Map<String, Object> params) {
		// TODO: 后续从原 3440 行 Service 中迁移完整业务逻辑
		return R.ok("待实现");
	}
}
