package com.budaos.modules.evgl.tch.service;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;

import java.util.Map;

/**
 * <p>课堂成员管理 Service</p>
 * <p>职责：负责学员加入/退出、助教设置、课堂移交等成员相关操作</p>
 * 
 * @author budaos
 * @version 1.0
 */
public interface TevglTchClassroomMemberService {
	
	/**
	 * 将学员设为助教
	 * @param map 参数包含 ctId(课堂 ID), traineeId(学员 ID)
	 * @return R 结果
	 */
	R setTraineeToTeachingAssistant(Map<String, Object> map);
	
	/**
	 * 取消课堂的助教
	 * @param ctId 课堂主键
	 * @param traineeId 学员主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R cancelTeachingAssistant(String ctId, String traineeId, String loginUserId);
	
	/**
	 * 一键加入课堂
	 * @param jsonObject 请求数据
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R oneClickToJoinClassroom(JSONObject jsonObject, String ctId, String loginUserId);
	
	/**
	 * 移交课堂（管理端将课堂移交给某老师）
	 * @param ctId 课堂主键
	 * @param traineeId 用户主键
	 * @return R 结果
	 */
	R turnOver(String ctId, String traineeId);
	
	/**
	 * 根据条件查询群成员
	 * @param params 查询参数
	 * @return R 结果
	 */
	R queryImGroupUserList(Map<String, Object> params);
}
