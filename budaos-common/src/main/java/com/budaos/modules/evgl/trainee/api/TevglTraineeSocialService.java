package com.budaos.modules.evgl.trainee.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeSocial;

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

public interface TevglTraineeSocialService extends IBaseService<TevglTraineeSocial>{
	
	public List<TevglTraineeSocial> queryByMap(Map<String, Object> params);
}