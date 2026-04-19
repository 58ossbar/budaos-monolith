package com.budaos.modules.evgl.tch.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;

import java.util.List;
import java.util.Map;

/**
 * <p>课堂查询 Service</p>
 * <p>职责：负责课堂列表查询、详情查看、统计报表等查询操作</p>
 * 
 * @author budaos
 * @version 1.0
 */
public interface TevglTchClassroomQueryService {
	
	/**
	 * 课堂列表（分页）
	 * @param params 查询参数
	 * @param loginUserId 当前登录用户 ID
	 * @param type 类型：1=我听的课，2=我教的课，null=全部
	 * @return R 结果
	 */
	R listClassroom(Map<String, Object> params, String loginUserId, String type);
	
	/**
	 * 查看课堂信息
	 * @param id 课堂主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R viewClassroomInfo(String id, String loginUserId);
	
	/**
	 * 查看课堂基本信息
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R viewClassroomBaseInfo(String ctId, String loginUserId);
	
	/**
	 * 查询 - 无分页
	 * @param map 查询参数
	 * @return List<TevglTchClassroom> 课堂列表
	 */
	List<TevglTchClassroom> queryNoPage(Map<String, Object> map);
	
	/**
	 * 根据邀请码搜索课堂
	 * @param invitationCode 邀请码
	 * @return R 结果
	 */
	R selectClassroomListData(String invitationCode);
	
	/**
	 * 根据条件获取年份
	 * @param loginUserId 当前登录用户 ID
	 * @param type 类型：1=我听的课的年份，2=我教的课的年份，null=全部
	 * @return List<Map<String, Object>> 年份列表
	 */
	List<Map<String, Object>> getDates(String loginUserId, String type);
	
	/**
	 * 查询当前登录用户创建的所有课堂信息
	 * @param ctId 课堂 ID（可选）
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R queryClassroomList(String ctId, String loginUserId);
	
	/**
	 * 校验是否能进入课堂页面
	 * @param ctId 课堂主键
	 * @param traineeId 学员 ID
	 * @return R 结果
	 */
	R verifyAccessToClass(String ctId, String traineeId);
	
	/**
	 * 查询列表（通用）
	 * @param params 查询参数
	 * @return R 结果
	 */
	R query(Map<String, Object> params);
	
	/**
	 * 查询列表返回 Map（通用）
	 * @param params 查询参数
	 * @return R 结果
	 */
	R queryForMap(Map<String, Object> params);
}
