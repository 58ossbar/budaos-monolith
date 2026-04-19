package com.budaos.modules.evgl.site.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.domain.TevglSiteFeedbackReply;

/**
 * <p> Title: 意见反馈回复表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglSiteFeedbackReplyService extends IBaseService<TevglSiteFeedbackReply>{
	
	/**
	 * 查看意见反馈的内容
	 * @param feedbackId
	 * @return
	 */
	R viewFeedbackReplyInfo(String feedbackId);
	
}