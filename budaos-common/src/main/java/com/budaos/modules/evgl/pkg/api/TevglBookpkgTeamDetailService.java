package com.budaos.modules.evgl.pkg.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeamDetail;

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

public interface TevglBookpkgTeamDetailService extends IBaseService<TevglBookpkgTeamDetail>{
	
	/**
	 * 单独对某个人授权
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R authorizationAlone(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 根据条件查询记录
	 * @param parmas
	 * @return
	 */
	List<TevglBookpkgTeamDetail> selectListByMap(Map<String, Object> params);
}