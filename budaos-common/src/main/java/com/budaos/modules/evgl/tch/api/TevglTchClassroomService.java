package com.budaos.modules.evgl.tch.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchClassroomService extends IBaseService<TevglTchClassroom>{
	
	/**
	 * 新增课堂
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @return
	 */
	R saveClassroomInfoNew(TevglTchClassroom tevglTchClassroom, String loginUserId);
	
	/**
	 * 修改课堂信息
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @return
	 */
	R updateClassroomInfo(TevglTchClassroom tevglTchClassroom, String loginUserId);
	
	/**
	 * <p>课堂列表</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @param params
	 * @param loginUserId
	 * @param type 类型值对应：1时查询加入的课堂（我听的课）2自己创建的课堂（我教的课），为空则查全部
	 * @return
	 */
	R listClassroom(Map<String, Object> params, String loginUserId, String type);
	
	/**
	 * 查看课堂信息
	 * @param id 课堂主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R viewClassroomInfo(String id, String loginUserId);
	
	/**
	 * 查看课堂基本信息
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	R viewClassroomBaseInfo(String ctId, String loginUserId);
	
	/**
	 * 删除课堂
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R deleteClassroom(String ctId, String loginUserId);
	
	/**
	 * 根据课堂主键查询
	 * @param id
	 * @return
	 */
	TevglTchClassroom selectObjectById(Object id);
	
	/**
	 * 将学员设为助教
	 * @param map
	 * @return
	 */
	R setTraineeToTeachingAssistant(Map<String, Object> map);
	
	/**
	 * 取消课堂的助教
	 * @param ctId
	 * @param traineeId
	 * @param loginUserId
	 * @return
	 */
	R cancelTeachingAssistant(String ctId, String traineeId, String loginUserId);
	
	/**
	 * <p>查询-无分页</p>  
	 * @author znn
	 * @data 2020年6月10日	
	 * @param map
	 * @return
	 */
	List<TevglTchClassroom> queryNoPage(Map<String, Object> map);
	
	/**
	 * 根据邀请码搜索课堂
	 * @param invitationCode
	 * @return
	 */
	R selectClassroomListData(String invitationCode);
	
	/**
	 * 收藏课堂
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R collect(String ctId, String loginUserId);
	
	/**
	 * 取消收藏
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R cancelCollect(String ctId, String loginUserId);
	
	/**
	 * 将课堂设置为置顶
	 * @param ctId 课堂主键
	 * @param value Y设为置顶N取消置顶
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R setTop(String ctId, String value, String loginUserId);
	
	/**
	 * 开始上课
	 * @param ctId 课堂主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R start(String ctId, String loginUserId);
	
	/**
	 * 结束上课
	 * @param ctId
	 * @param loginUserId 登陆用户，注意：此值可能是网站段端老师，也可能是管理端，管理人员
	 * @return
	 */
	R end(String ctId, String loginUserId);
	
	/**
	 * 根据条件获取年份
	 * @param loginUserId 当前登录用户
	 * @param type 类型值对应：1时查询加入的课堂（我听的课）的年份 2自己创建的课堂（我教的课）的年份，为空则查全部
	 * @return
	 */
	List<Map<String, Object>> getDates(String loginUserId, String type);
	
	/**
	 * @author zhouyl加
	 * @date 2021年2月20日
	 * 查询当前登录用户创建的所有课堂信息
	 * @param ctId 课程id
	 * @param loginUserId
	 * @return
	 */
	R queryClassroomList(String ctId, String loginUserId);
	
	/**
	 * @author zhouyl加
	 * @date 2021年2月20日
	 * 一键加入课堂
	 * @param jsonObject
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	R oneClickToJoinClassroom(JSONObject jsonObject, String ctId, String loginUserId);
	
	/**
	 * 根据条件查询群成员
	 * @param params
	 * @return
	 */
	R queryImGroupUserList(Map<String, Object> params);
	
	/**
	 * 移交课堂（管理端将课堂移交给某老师）
	 * @param ctId 必传参数，课堂主键
	 * @param traineeId 必传参数，用户主键
	 * @return
	 */
	R turnOver(String ctId, String traineeId);
	
	/**
	 * 根据教学包id查询课堂
	 * @param pkgId
	 * @return
	 */
	TevglTchClassroom selectObjectByPkgId(Object pkgId);
	
	/**
	 * 校验是否能进入课堂页面
	 * 规则如下：
	 *   学生身份时，如果课堂已结束，且被设为了不允许再进入
	 *   老师身份时，账号被停用了的话，也不能进入课堂
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	R verifyAccessToClass(String ctId, String traineeId);
}