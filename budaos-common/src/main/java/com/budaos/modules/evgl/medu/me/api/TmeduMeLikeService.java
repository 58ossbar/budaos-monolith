package com.budaos.modules.evgl.medu.me.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeLike;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 点赞表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TmeduMeLikeService extends IBaseService<TmeduMeLike>{
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<TmeduMeLike> selectListByMap(Map<String, Object> map);
	
	/**
	 * 查询我的被点赞记录（别人给我点的赞）
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R queryPeopleGiveMeLikes(Map<String, Object> params, String loginUserId);
	
}