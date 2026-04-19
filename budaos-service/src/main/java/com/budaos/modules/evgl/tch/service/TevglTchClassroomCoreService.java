package com.budaos.modules.evgl.tch.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;

/**
 * <p>课堂核心 CRUD 操作 Service</p>
 * <p>职责：负责课堂的增删改查基础操作</p>
 * 
 * @author budaos
 * @version 1.0
 */
public interface TevglTchClassroomCoreService {
	
	/**
	 * 新增课堂
	 * @param tevglTchClassroom 课堂对象
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R saveClassroomInfoNew(TevglTchClassroom tevglTchClassroom, String loginUserId);
	
	/**
	 * 修改课堂信息
	 * @param tevglTchClassroom 课堂对象
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R updateClassroomInfo(TevglTchClassroom tevglTchClassroom, String loginUserId);
	
	/**
	 * 删除课堂
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户 ID
	 * @return R 结果
	 */
	R deleteClassroom(String ctId, String loginUserId);
	
	/**
	 * 根据课堂主键查询
	 * @param id 课堂主键
	 * @return TevglTchClassroom 课堂对象
	 */
	TevglTchClassroom selectObjectById(Object id);
	
	/**
	 * 根据教学包 id 查询课堂
	 * @param pkgId 教学包 ID
	 * @return TevglTchClassroom 课堂对象
	 */
	TevglTchClassroom selectObjectByPkgId(Object pkgId);
	
	/**
	 * 保存（通用）
	 * @param tevglTchClassroom 课堂对象
	 * @return R 结果
	 */
	R save(TevglTchClassroom tevglTchClassroom);
	
	/**
	 * 更新（通用）
	 * @param tevglTchClassroom 课堂对象
	 * @return R 结果
	 */
	R update(TevglTchClassroom tevglTchClassroom);
	
	/**
	 * 删除（通用）
	 * @param id 课堂主键
	 * @return R 结果
	 */
	R delete(String id);
	
	/**
	 * 批量删除
	 * @param ids 课堂主键数组
	 * @return R 结果
	 */
	R deleteBatch(String[] ids);
}
