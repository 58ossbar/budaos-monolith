package com.budaos.modules.evgl.activity.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;

import java.util.List;
import java.util.Map;

/**
 * 活动相关接口（公共）
 * @author huj
 *
 */
public interface TevglActivityService {

	/**
	 * 从字典获取活动状态
	 * @param pkgId 教学包
	 * @param ctId 课堂
	 * @param loginUserId
	 * @return
	 */
	R listActivityState(String pkgId, String ctId, String chapterId, String loginUserId);
	
	/**
	 * 获取活动分组(包含字典默认、自己针对教学包的活动创建的分组（注：没有细分到某个分组）)
	 * @param pkgId 教学包主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R listActivityGroup(String pkgId, String loginUserId);
	
	/**
	 * 活动列表（union all 查询各种活动表）
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R listActivityForRoom(Map<String, Object> params, String loginUserId);
	
	/**
	 * 活动列表（教学包专用）
	 * @param params
	 * @return
	 */
	R listActivityForPackage(Map<String, Object> params, String loginUserId);
	
	/**
	 * 保存自定义活动分组
	 * @param jsonObject {pkgId:'教学包主键', resgroupName:'分组名称'}
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R saveResgroup(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 开始活动
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param pkgId 教学包id
	 * @param activityType 活动类型1投票/问卷2头脑风暴3答疑/讨论4测试活动等等
	 * @param loginUserId 当前登录用户
	 * @param activityEndTime 活动自动结束时间
	 * @return
	 */
	R startActivity(String ctId, String activityId, String pkgId, String activityType, String loginUserId, String activityEndTime);

	/**
	 * 结束活动
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param activityType 活动类型
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R endActivity(String ctId, String activityId, String activityType, String loginUserId);
	
	/**
	 * 删除活动
	 * @param activityId 活动id
	 * @param activityType 活动类型
	 * @param loginUserId 当前登录用户
	 * @param pkgId 所属教学包
	 * @return
	 */
	R deleteActivity(String activityId, String activityType, String loginUserId, String pkgId);
	
	/**
	 * 查看活动信息
	 * @param activityId
	 * @param activityType
	 * @return
	 */
	R viewActivityInfo(String activityId, String activityType);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMapForRelease(Map<String, Object> params);
}
