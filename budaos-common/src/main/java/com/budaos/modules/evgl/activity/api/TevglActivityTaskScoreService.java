package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskScore;
import com.budaos.modules.evgl.activity.vo.ActivityTaskScoreVo;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityTaskScoreService extends IBaseService<TevglActivityTaskScore>{
	
	/**
	 * 老师评分
	 * @param vo
	 * @param loginUserId
	 * @return
	 */
	R teachGiveScore(ActivityTaskScoreVo vo, String loginUserId);
	
}