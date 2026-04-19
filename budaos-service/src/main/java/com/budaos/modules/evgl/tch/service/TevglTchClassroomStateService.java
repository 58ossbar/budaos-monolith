package com.budaos.modules.evgl.tch.service;

import com.budaos.core.baseclass.domain.R;

/**
 * <p>课堂状态管理 Service</p>
 * <p>职责：负责课堂的开始/结束、置顶/收藏等状态变更操作</p>
 * 
 * @author budaos
 * @version 1.0
 */
public interface TevglTchClassroomStateService {
	
	/**
	 * 收藏课堂
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R collect(String ctId, String loginUserId);
	
	/**
	 * 取消收藏
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R cancelCollect(String ctId, String loginUserId);
	
	/**
	 * 将课堂设置为置顶
	 * @param ctId 课堂主键
	 * @param value Y 设为置顶 N 取消置顶
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R setTop(String ctId, String value, String loginUserId);
	
	/**
	 * 开始上课
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R start(String ctId, String loginUserId);
	
	/**
	 * 结束上课
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户 ID（可能是网站端老师或管理端人员）
	 * @return R 结果
	 */
	R end(String ctId, String loginUserId);
}
