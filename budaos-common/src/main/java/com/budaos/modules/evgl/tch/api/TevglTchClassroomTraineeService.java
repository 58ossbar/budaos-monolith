package com.budaos.modules.evgl.tch.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.vo.TevglTchClassroomTraineeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: 课堂成员</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglTchClassroomTraineeService extends IBaseService<TevglTchClassroomTrainee>{
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<TevglTchClassroomTrainee> selectListByMap(Map<String, Object> map);
	
	/**
	 * 课堂成员列表公共方法,根据条件查询记录，返回List<Map>, 对象为驼峰命名
	 * @param params
	 * @return
	 */
	R selectListMapForWeb(Map<String, Object> params, String loginUserId);
	
	/**
	 * 加入课堂
	 * @param map
	 * @return
	 * @apiNote map的key需要如下值
	 * {
	 * ctId:课堂主键，invitationCode邀请码
	 * }
	 */
	R joinTheClassroom(Map<String, Object> map, String loginUserId);
	
	/**
	 * 删除课堂成员
	 * @param map
	 * @return
	 * @apiNote 必传参数：id课堂成员表主键，ctId课堂表主键，traineeId被删除的学员，loginUserId当前登录用户
	 */
	R deleteClassroomTrainee(Map<String, Object> map);
	
	/**
	 * 批量删除
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R deleteClassroomTraineeBatch(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 将班级学员导入成课堂成员
	 * @param ctId 课堂id
	 * @param classId 班级id
	 * @param traineeIds
	 * @return
	 */
	R importTraineeBatch(String ctId, String classId, List<String> traineeIds);
	
	/**
	 * 根据条件查询课堂成员
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> listClassroomTrainee(Map<String, Object> params);
	
	/**
	 * 根据条件查询课堂成员，注意：默认只会查询未加入课堂小组的成员，传参参数withOutPage时，查询审核状态通过的所有课程成员
	 * @param params  参数ctId课堂主键必传
	 * @return
	 */
	R listClassroomTraineeExcludeJoinedGroup(Map<String, Object> params);
	
	/**
	 * 查看课堂成员的基本信息，如，姓名、手机号码、课堂表现
	 * @param ctId 课堂主键
	 * @param traineeId 被查看的学员
	 * @return
	 */
	R viewTraineeBaseInfo(String ctId, String traineeId, String loginUserId);
	
	/**
	 * 查看课堂成员的综合评价信息 （小程序所用）
	 * @param ctId 课堂主键
	 * @param traineeId 被查看的学员
	 * @return
	 */
	@Deprecated
	R viewTraineeDetailInfo(String ctId, String traineeId);
	
	/**
	 * 查看课堂成员的综合评价信息 （小程序所用）
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	R viewTraineeDetailInfoV2(String ctId, String traineeId);
	
	/**
	 * PC端查看课堂成员相关信息 （PC端所用）当前课堂的经验值、热心解答次数、获取点赞数、课堂表现次数、视频学习个数
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	R viewTraineeBaseInfoForWeb(String ctId, String traineeId);
	
	/**
	 * PC端查看课堂成员相关信息（PC端所用）
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	R viewTraineeDetailInfoForWeb(String ctId, String traineeId);
	
	/**
	 * 教师修改学员部分信息
	 * @param request
	 * @param ctId
	 * @param traineeId
	 * @param traineeName
	 * @param traineeSex
	 * @param loginUserId
	 * @return
	 */
	R updateTraineeInfo(HttpServletRequest request, String ctId, String traineeId, String traineePic, String traineeName, String nickName, String traineeSex, String loginUserId) throws Exception;
	
	/**
	 * 将选择的人，加入成该课堂成员（注意：同时需要将它们加入到该课堂群里面去，以及对应的进行中的答疑讨论群）
	 * @param ctId 必传参数，当前课堂
	 * @param loginUserId 必传参数，当前登录用户
	 * @param traineeIds 选传参数，被选的学员们
	 * @return
	 */
	R importTraineeBatchNew(String ctId, String loginUserId, List<String> traineeIds);
	
	/**
	 * 批量新增课堂成员
	 * @param ctId 必传参数
	 * @param traineeIdList 必传参数
	 * @return
	 * @apiNote
	 * ① 往t_evgl_tch_classroom_trainee表中插入记录
	 * ② 更新ctId课堂的学习人数
	 */
	R saveClassroomTraineeBatch(String ctId, List<String> traineeIdList);
	
	/**
	 * 根据条件查询课堂成员
	 * @param map
	 * @return
	 */
	List<TevglTchClassroomTraineeVo> findClassroomTraineeList(Map<String, Object> map);
	
	/**
	 * 批量更新选中的数据，在课堂结束后，允许再进入课堂
	 * @param ctId 课堂id
	 * @param idList 课堂成员id，对应t_evgl_tch_classroom_trainee表主键id值
	 * @return
	 */
	R batchUpdateAccessState(String ctId, List<String> idList);
	
}