package com.budaos.modules.im.imhandler;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.im.core.annotation.CBImHandler;
import com.budaos.modules.im.core.annotation.CBImService;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.im.domain.TimChatList;
import com.budaos.modules.im.domain.TimChatListTop;
import com.budaos.modules.im.service.TimChatListService;
import com.budaos.modules.im.service.TimChatListTopService;
import com.budaos.modules.im.service.TimGroupMsgService;
import com.budaos.modules.im.service.TimPrivateMsgService;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 公共处理类
 * @author zhuq
 *
 */
@CBImService("public")
@Service
public class PublicMsgHandler {

	private static Logger log = LoggerFactory.getLogger(PublicMsgHandler.class);
	
	@Autowired
	private TimPrivateMsgService privateMsgService;
	@Autowired
	private TimChatListService chatListService;
	@Autowired
	private TimGroupMsgService timGroupMsgService;
	@Autowired
	private TimChatListTopService timChatListTopService;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	/**
	 * 消息列表
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="chatlist")
	public void chatlist(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		Map<String, Object> map = new HashMap<>();
		map.put("userId", channelContext.userid);
		map.put("lastid", busiMsg.get("lastid"));
		if ("Y".equals(busiMsg.get("nopage"))) {
			map.put("nopage", "Y");
			map.remove("lastid");
		}
		map.put("name", busiMsg.get("name"));
		List<Map<String, Object>> datas = chatListService.queryChatList(map);
		log.debug("消息列表：" + datas.size());
		msg.put(Const.BUSI_MSG, datas);
		CBImUtils.sendToSelf(channelContext, msg);
	}
	
	/**
	 * 标识消息已读
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="tagReaded")
	public void tagReaded(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		CBImUtils.sendToSelf(channelContext, R.ok("tagReaded"));
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		String listId = busiMsg.getString("listId");
		String friendType = busiMsg.getString("friendType");
		String friendId = busiMsg.getString("friendId");
		String fromuser = busiMsg.getString("fromuser");
		String msgtime = busiMsg.getString("msgtime");
		//私聊
		if("1".equals(friendType)) {
			privateMsgService.updateIsRead(null, fromuser, friendId, DateUtils.parse(msgtime, "yyyy-MM-dd HH:mm:ss"));
		//群聊
		}else if("2".equals(friendType)) {
			// friendType为2时，friendId就是群的主键
			fromuser = channelContext.userid;
			timGroupMsgService.updateIsRead(friendId, fromuser, DateUtils.parse(msgtime, "yyyy-MM-dd HH:mm:ss"));
		}
		
		TimChatList chat = new TimChatList();
		chat.setListId(listId);
		chat.setUnreadNum(0);
		chat.setUserId(fromuser);
		chat.setFriendId(friendId);
		chat.setFriendType(friendType);
		chatListService.updateNum(chat);
	}
	
	@CBImHandler(value="setTop")
	public void setTop(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		String listId = busiMsg.getString("listId");
		String userId = channelContext.userid;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("listId", listId);
		List<TimChatListTop> list = timChatListTopService.selectListByMap(map);
		if (list == null || list.size() == 0) {
			TimChatListTop top = new TimChatListTop();
			top.setTopId(Identities.uuid());
			top.setListId(listId);
			top.setUserId(userId);
			top.setTopTime(DateUtils.getNowTimeStamp());
			timChatListTopService.save(top);
		}
		CBImUtils.sendToSelf(channelContext, msg);
	}
	
	@CBImHandler(value="cancelTop")
	public void cancelTop(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		String listId = busiMsg.getString("listId");
		String userId = channelContext.userid;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("listId", listId);
		List<TimChatListTop> list = timChatListTopService.selectListByMap(map);
		if (list.size() > 0) {
			List<String> topIdList = list.stream().map(a -> a.getTopId()).collect(Collectors.toList());
			timChatListTopService.deleteBatch(topIdList.stream().toArray(String[]::new));
		}
		CBImUtils.sendToSelf(channelContext, msg);
	}
	
	/**
	 * 移除会话列表
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="delChatList")
	public void delChatList(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		String listId = busiMsg.getString("listId");
		TimChatList t = new TimChatList();
		t.setListId(listId);
		t.setState("N");
		chatListService.update(t);
		CBImUtils.sendToSelf(channelContext, msg);
		// 删除置顶记录
		Map<String, Object> map = new HashMap<>();
		map.put("userId", channelContext.userid);
		map.put("listId", listId);
		List<TimChatListTop> topList = timChatListTopService.selectListByMap(map);
		if (topList.size() > 0) {
			topList.stream().forEach(a -> {
				timChatListTopService.delete(a.getTopId());
			});
		}
	}
	
}
