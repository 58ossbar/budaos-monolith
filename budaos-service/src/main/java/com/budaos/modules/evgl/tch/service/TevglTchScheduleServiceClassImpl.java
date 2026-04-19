package com.budaos.modules.evgl.tch.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.api.TevglTchScheduleClassService;
import com.budaos.modules.evgl.tch.persistence.TevglTchClasstraineeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchScheduleClassMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TevglTchScheduleServiceClassImpl implements TevglTchScheduleClassService {

	@Autowired
	private TevglTchScheduleClassMapper tevglTchScheduleClassMapper;
	@Autowired
	private TevglTchClasstraineeMapper tevglTchClasstraineeMapper;
	
	/**
	 * 查询某人教的班级
	 * @param traineeId
	 * @return
	 */
	@Override
	public R findMyClassList(String traineeId) {
		if (StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		List<Map<String,Object>> list = tevglTchScheduleClassMapper.findMyClassList(traineeId);
		return R.ok().put(Constant.R_DATA, list);
	}

	/**
	 * 查询我已经加入的班级
	 *
	 * @param traineeId
	 * @return
	 */
	@Override
	public R findMyJoinedClassList(String traineeId) {
		if (StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		List<Map<String, Object>> myJoinedClassList = tevglTchScheduleClassMapper.findMyJoinedClassList(traineeId);
		return R.ok().put(Constant.R_DATA, myJoinedClassList);
	}
	
	/**
	 * 查询教某人的老师们
	 * @param traineeId
	 * @return
	 */
	@Override
	public R findMyTeacherList(String traineeId) {
		if (StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		// 先找到学员所在的班级（注意：由于历史原因，生产环境中，可能出现一个学员在多个班级的情况！！！，这里至于学员最新加入的班级）
		String classId = tevglTchClasstraineeMapper.findTheLatestClassId(traineeId);
		List<Map<String,Object>> teacherList = tevglTchScheduleClassMapper.findMyTeacherListByClassId(classId);
		return R.ok().put(Constant.R_DATA, teacherList);
	}

}
