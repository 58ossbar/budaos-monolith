package com.budaos.modules.evgl.pkg.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglPkgCheck;

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

public interface TevglPkgCheckService extends IBaseService<TevglPkgCheck>{
	
	/**
	 * 管理端，查询教学包列表
	 * @param params
	 * @return
	 */
	R queryPkgListForMgr(Map<String, Object> params);
	
	/**
	 * 查询我的待审核的教学包
	 * @param params
	 * @return
	 */
	R querMyWaitCheckPkgList(Map<String, Object> params, String loginUserId);
	
	/**
	 * 管理端，审核教学包
	 * @param tevglPkgCheck
	 * @return
	 */
	R updatePkgReleaseStatus(TevglPkgCheck tevglPkgCheck);
	
	/**
	 * 查看教学包对应教材的树形章节数据，（管理端专用）
	 * @param pkgId
	 * @return
	 */
	R getBookTreeDataForMgrCheck(String pkgId);
	
}