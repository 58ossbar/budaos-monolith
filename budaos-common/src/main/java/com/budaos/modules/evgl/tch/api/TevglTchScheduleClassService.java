package com.budaos.modules.evgl.tch.api;

import com.budaos.core.baseclass.domain.R;

public interface TevglTchScheduleClassService {

	/**
	 * 查询该用户所教，上课的班级
	 * @param traineeId
	 * @return
	 */
	R findMyClassList(String traineeId);
	
	/**
	 * 查询我已经加入的班级
	 * @param traineeId
	 * @return
	 */
	R findMyJoinedClassList(String traineeId);
	
	/**
	 * 查询该用户上课的老师
	 * @param traineeId
	 * @return
	 */
	R findMyTeacherList(String traineeId);
	
}
