package com.budaos.modules.evgl.tch.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchSchedule;
import com.budaos.modules.evgl.tch.params.TevglTchScheduleParams;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;

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

public interface TevglTchScheduleService extends IBaseService<TevglTchSchedule>{
	
	/**
     * 根据条件查询数据
     * @param map
     * @return
     */
    R queryScheduleData(Map<String, Object> map);
    
    /**
     * 根据条件查询数据
     * @param map
     * @return
     */
    R queryScheduleDataV2(Map<String, Object> map);
    
    /**
     * 新增
     * @param tevglTchSchedule
     * @return
     */
    R saveBatch(TevglTchSchedule tevglTchSchedule);
    
    /**
     * 网站端查询课表
     * @param params 前端传递的查询条件
     * @param traineeInfo 登陆用户
     * @return
     */
    R queryScheduleDataForWeb(TevglTchScheduleParams params, TevglTraineeInfo traineeInfo);
    
    /**
     * 小程序端查询课表
     * @param params 前端传递的查询条件
     * @param traineeInfo 登陆用户
     * @return
     */
    R queryScheduleDataForWx(TevglTchScheduleParams params, TevglTraineeInfo traineeInfo);
    
    /**
     * 查看某一天的详细授课记录
     * @param params
     * @param traineeInfo
     * @return
     */
    R viewSomeDaySchedule(TevglTchScheduleParams params, TevglTraineeInfo traineeInfo);
    
    /**
     * 查询实训室
     * @param params
     * @return
     */
    R queryTrainingRoomList(Map<String, Object> params);
    
    /**
     * 调课
     * @param tevglTchSchedule
     * @return
     */
    R exchangeSchedule(TevglTchSchedule tevglTchSchedule);
    
    /**
     * 审核 调课记录
     * @param scheduleIdLatest
     * @param checkState 审核状态(0待审核1通过2未通过)
     * @param reason 未通过时的原因
     * @return
     */
    R check(String scheduleIdLatest, String checkState, String reason);
    
}