package com.budaos.modules.evgl.tch.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomRoleprivilege;

import java.util.List;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchClassroomRoleprivilegeService extends IBaseService<TevglTchClassroomRoleprivilege>{
	
	/**
	 * 保存设置
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R savePermissionSet(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查询权限
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	R queryPermissionList(String ctId, String loginUserId);    
	/**
     * 查看某课堂，助教的权限
     * @param ctId
     * @return
     */
    List<String> queryPermissionListByCtId(String ctId);
	
}