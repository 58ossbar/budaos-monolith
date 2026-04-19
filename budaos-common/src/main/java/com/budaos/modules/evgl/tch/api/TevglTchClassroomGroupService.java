package com.budaos.modules.evgl.tch.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomGroup;

import java.util.Map;

/**
 * <p> Title: 课堂小组</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchClassroomGroupService extends IBaseService<TevglTchClassroomGroup>{
	
	/**
	 * 保存课堂小组信息
	 * @param tevglTchClassroomGroup
	 * @param loginUserId
	 * @return
	 */
	R saveClassroomGroupInfo(TevglTchClassroomGroup tevglTchClassroomGroup, String loginUserId);
	
	/**
	 * 批量保存小组信息
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveGroupInfo(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查询课堂的小组信息
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	R listClassroomGroupSimpleInfo(String ctId, String loginUserId);
	
	/**
	 * 查看课堂分组信息
	 * @param gpId 课堂小组主键
	 * @param type 0默认查询基本信息，1查询基本信息和小组下的成员信息
	 * @param params
	 * @return
	 */
	R viewClassroomGroupInfo(String gpId, int type, Map<String, Object> params, String loginUserId);
	
	/**
	 * 将学员加入某小组
	 * @param jsonObject {'ctId':'课堂主键', 'gpId':'小组主键必传','traineeIds':['1', '2'], 'token':''}
	 * @param loginUserId
	 * @return
	 */
	R setTraineeToGroup(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 删除小组
	 * @param gpId
	 * @param loginUserId
	 * @return
	 */
	R deleteClassroomGroup(String gpId, String loginUserId);
	
	/**
	 * 将成员移出小组
	 * @param gmId
	 * @param loginUserId
	 * @return
	 */
	R deleteClassroomGroupMember(String gmId, String loginUserId);
	
	/**
	 * 将成员设为组长
	 * @param gpId
	 * @param traineeId
	 * @param loginUserId
	 * @return
	 */
	R setTraineeToLeader(String gpId, String traineeId, String loginUserId);
	
	/**
	 * 重命名小组
	 * @param gpId
	 * @param name
	 * @param loginUserId
	 * @return
	 */
	R rename(String gpId, String name, String loginUserId);
}