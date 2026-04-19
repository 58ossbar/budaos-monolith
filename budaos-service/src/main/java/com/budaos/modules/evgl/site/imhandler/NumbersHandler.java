package com.budaos.modules.evgl.site.imhandler;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.common.CbNumsUtils;
import com.budaos.modules.im.core.annotation.CBImHandler;
import com.budaos.modules.im.core.annotation.CBImService;
import com.budaos.modules.im.core.utils.CBImUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

@CBImService("numbers")
@Service
public class NumbersHandler {

	@Autowired
	private CbNumsUtils cbNumsUtils;
	
	/**
	 * 获取各种数量
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="get")
	public void get(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		String loginUserId = channelContext.userid;
		msg = cbNumsUtils.getNums(loginUserId);
		CBImUtils.sendToUser(channelContext, channelContext.userid, msg);
	}
	
}
