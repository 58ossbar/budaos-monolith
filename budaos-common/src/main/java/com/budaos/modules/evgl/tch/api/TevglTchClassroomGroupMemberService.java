package com.budaos.modules.evgl.tch.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomGroupMember;

/**
 * <p> Title: 课堂小组成员</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchClassroomGroupMemberService extends IBaseService<TevglTchClassroomGroupMember>{
	
	/**
	 * 根据课堂小组id查看课堂小组成员
	 * @param gpId
	 * @return
	 */
	R listClassroomGroupMemberById(String gpId);
	
	/**
	 * 删除小组成员
	 * @param gmId
	 * @param loginUserId
	 * @return
	 */
	R deleteClassroomGroupMember(String gmId, String loginUserId);
	
	/**
	 * 删除小组成员
	 * @param jsonObject
	 * @return
	 */
	R removeGroupMembers(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 设置小组成员身份
	 * @param ctId
	 * @param gmId
	 * @param dictCode
	 * @param loginUserId
	 * @return
	 */
	R setIdentity(String ctId, String gmId, String dictCode, String loginUserId);
	
	/**
	 * 批量设置小组成员身份
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R setIdentityBatch(JSONObject jsonObject, String loginUserId);
	
}