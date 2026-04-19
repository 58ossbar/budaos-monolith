package com.budaos.modules.evgl.cloudpan.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanNavigationBar;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TcloudPanNavigationBarService extends IBaseService<TcloudPanNavigationBar>{
	
	/**
	 * 快捷导航列表
	 * @param ctId
	 * @param name
	 * @return
	 */
	R querNavBarList(String ctId, String name, String loginUserId);
	
	/**
	 * 查询右侧导航栏
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	R queryRightList(String ctId, String loginUserId);
	
	/**
	 * 查询可设置选择的目录
	 * @param ctId
	 * @param name
	 * @return
	 */
	R querySelectList(String ctId, String name, String loginUserId);
	
	/**
	 * 保存
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveSetting(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 批量设置
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveNavBarBatch(JSONObject jsonObject, String loginUserId);
	
}