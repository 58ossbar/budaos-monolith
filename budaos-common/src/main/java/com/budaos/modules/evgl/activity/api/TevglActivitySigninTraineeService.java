package com.budaos.modules.evgl.activity.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninTrainee;

import java.util.Map;

/**
 * <p> Title: 学员活动签到信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivitySigninTraineeService extends IBaseService<TevglActivitySigninTrainee>{

	/**
	 *  根据课堂id和活动id等查询已签到人员
	 * @param params
	 * @return
	 * @apiNote 必传值参数的：ctId课堂主键，activityId活动id；
	 * 非必传：traineeName学员名称
	 */
	R listActivitySigninTrainee(Map<String, Object> params);
	
	/**
	 * 学员签到
	 * @param tevglActivitySigninTrainee
	 * @param loginUserId
	 * @return
	 */
	R signIn(TevglActivitySigninTrainee tevglActivitySigninTrainee, String loginUserId);
	
	/**
	 * 根据条件查询成员
	 * @param params 课堂主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R queryTraineeList(Map<String, Object> params, String loginUserId);
	
	/**
	 * 手工设置学员签到状态
	 * @param jsonObject {'token':'', 'ctId':'', 'activityId':'', 'stIds':[]}
	 * @param loginUserId
	 * @return
	 */
	R setTraineeSignState(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 学员查看自己的签到信息
	 * @param ctId 课堂
	 * @param activityId 活动
	 * @param loginUserId
	 * @return
	 */
	R viewTraineeSignInfo(String ctId, String activityId, String loginUserId);
	
	/**
	 * 学员签到详情列表
	 * @param params {'ctId':'', 'pageNum':'', 'pageSize':''}
	 * @return
	 */
	R queryTraineeSigninDetail(Map<String, Object> params);
	
}