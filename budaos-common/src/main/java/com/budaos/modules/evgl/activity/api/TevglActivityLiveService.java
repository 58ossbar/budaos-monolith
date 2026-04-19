package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityLive;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityLiveService extends IBaseService<TevglActivityLive>{
	
	/**
	 * 新增
	 * @return
	 */
	R saveLive(TevglActivityLive tevglActivityLive, String loginUserId);
	
	/**
	 * 修改
	 * @param tevglActivityLive
	 * @param loginUserId
	 * @return
	 */
	R updateLive(TevglActivityLive tevglActivityLive, String loginUserId);
	
	/**
	 * 查看
	 * @param activityId
	 * @return
	 */
	R viewLive(String activityId);
	
	/**
     * 删除
     * @param activityId
     * @param pkgId
     * @param loginUserId
     * @return
     */
    R deleteLive(String activityId, String pkgId, String loginUserId);
    
    /**
     * 开始活动
     * @param ctId
     * @param activityId
     * @param loginUserId
     * @return
     */
    R startActivityLive(String ctId, String activityId, String loginUserId, String activityEndTime);

    /**
     * 结束活动
     * @param ctId
     * @param activityId
     * @param loginUserId
     * @return
     */
    R endActivityLive(String ctId, String activityId, String loginUserId);
	
}