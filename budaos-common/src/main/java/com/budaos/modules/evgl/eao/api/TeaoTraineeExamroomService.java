package com.budaos.modules.evgl.eao.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamroom;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TeaoTraineeExamroomService extends IBaseService<TeaoTraineeExamroom>{
	
	/**
	 * 新增实践考核
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveActivityExamroomInfo(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 补考
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveMakeUp(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 删除实践考核
	 * @param activityId t_eao_trainee_examroom表主键id的值
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R deleteActivityExamroomInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 开始活动
	 * @param ctId 课堂id
	 * @param activityId 活动id t_eao_trainee_examroom表主键id
	 * @param loginUserId 当前登录用户
	 * @param activityEndTime 非必传参数，活动自动结束的时间
	 * @return
	 */
	R startActivityExamroomInfo(String ctId, String activityId, String loginUserId, String activityEndTime);
	
	/**
	 * 结束活动
	 * @param ctId 课堂id
	 * @param activityId 活动id t_eao_trainee_examroom表主键id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R endActivityExamroomInfo(String ctId, String activityId, String loginUserId);
	
	/**
	 * 进入考核填表界面
	 * @param ctId
	 * @param erId
	 * @param loginUserId
	 * @return
	 */
	R list(String ctId, String erId, String loginUserId);

	/**
	 * 查看基本信息
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R viewExamroomInfo(String activityId, String loginUserId);

	/**
	 * 查看评分排名结果
	 * @param activityId
	 * @param traineeName
	 * @param mobile
	 * @param jobNumber
	 * @param loginUserId
	 * @return
	 * @apiNote
	 * 1.计算【自评成绩】
	 * 2.计算【互评成绩】
	 * 3.计算【师评成绩】
	 * 4.从字典表去各自的分数权重,计算出最终得分
	 */
	R viewExamResultInfo(String activityId, String traineeName, String mobile, String jobNumber, String loginUserId);
	
	/**
	 * 查看本次考核某人的给其它所有人评的分
	 * @param traineeId
	 * @param activityId
	 * @param loginUserId
	 * @param type 1查看某人给别人评的规则分2查看某人的规则得分
	 * @return
	 */
	R viewExamDetailInfo(String traineeId, String activityId, String loginUserId, String type);
	
	
}