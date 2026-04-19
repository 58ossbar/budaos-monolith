package com.budaos.modules.evgl.cloudpan.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanPermissions;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TcloudPanPermissionsService extends IBaseService<TcloudPanPermissions>{
	
	/**
	 * 设置云盘权限
	 * @author zhouyl加
	 * @date 2021年1月25日
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	public R setPermissions(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 设置云盘权限（单个设置，新版测试）
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	public R setPermissionsNew(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查看云盘权限
	 * @author zhouyl加
	 * @date 2021年1月26日
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	public R queryPermissions(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查询权限（包含课堂成员，与此文件或文件夹有权限的学员）
	 * @param ctId 当前课堂
	 * @param id 当前被授权的文件或文件夹
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R queryPermissionForWx(String ctId, String id, String traineeName, String loginUserId);
	
	/**
	 * 设置学员不可见
	 * @author zhouyl加
	 * @date 2021年3月6日
	 * @param ctId 课堂id
	 * @param pkgId 教学包id
	 * @param dirId 文件夹id
	 * @param isTraineesVisible 学员是否可见   true:可见,false:不可见
	 * @param loginUserId 
	 * @param refPkgDir 来源教学包 为true表示教学包的，为false表示自己的
	 * @return
	 */
	R setStudentNotVisible(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查看有权限的目录
	 * @author zhouyl加
	 * @date 2021年3月9日
	 * @param ctId 课堂id
	 * @param loginUserId
	 * @return
	 */
	R queryDirectoryDisplay(String ctId, String loginUserId);
	
	/**
	 * 获取树形的目录结构
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R getTreeDataForAuth(String pkgId, String loginUserId);
	
	/**
	 * 保存设置
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveAuth(JSONObject jsonObject, String loginUserId);
	
	R saveAuthNew(JSONObject jsonObject, String loginUserId);
}