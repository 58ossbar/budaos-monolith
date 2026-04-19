package com.budaos.modules.evgl.site.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.domain.TevglSiteFeedback;

import java.util.Map;

/**
 * <p> Title: 意见反馈表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglSiteFeedbackService extends IBaseService<TevglSiteFeedback>{
	
	R queryFeedbackInfo(Map<String, Object> params);
	
}