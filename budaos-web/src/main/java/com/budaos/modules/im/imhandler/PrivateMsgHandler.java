package com.budaos.modules.im.imhandler;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.im.core.annotation.CBImHandler;
import com.budaos.modules.im.core.annotation.CBImService;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.entity.BaseMessage;
import com.budaos.modules.im.core.entity.FileMessage;
import com.budaos.modules.im.core.entity.MsgType;
import com.budaos.modules.im.core.entity.TextMessage;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.im.domain.TimAttach;
import com.budaos.modules.im.domain.TimPrivateMsg;
import com.budaos.modules.im.domain.TimUserinfo;
import com.budaos.modules.im.service.TimAttachService;
import com.budaos.modules.im.service.TimChatListService;
import com.budaos.modules.im.service.TimPrivateMsgService;
import com.budaos.utils.redis.RedisUtils;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

import java.util.*;

/**
 * 私聊消息处理类
 * @author zhuq
 *
 */
@CBImService("privatechat")
@Service
public class PrivateMsgHandler {

	@Autowired
	private TimPrivateMsgService privateMsgService;
	@Autowired
	private TimChatListService chatListService;
	@Autowired
	private TimAttachService timAttachService;
	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 处理前端发起的busitype值为chat的websocket文本消息(与spring mvc逻辑一致)
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="send", msgType=MsgType.TEXT)
	public void sendTest(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		TextMessage convertMsg = CBImUtils.convertMsg(busiMsg, TextMessage.class);
		String touser = convertMsg.getTouser();
		TimUserinfo userInfo = (TimUserinfo)channelContext.get(Const.CHANNEL_INFO_USERINFO);
		busiMsg.clear();
		busiMsg.put("send_id", channelContext.userid);
		busiMsg.put("send_name", userInfo.getUserRealname());
		busiMsg.put("send_img", userInfo.getUserimg());
		busiMsg.put("msg_type", MsgType.TEXT);
		busiMsg.put("msg_content", convertMsg.getText().getContent());
		busiMsg.put("send_time", convertMsg.getMsgtime());
		busiMsg.put("touser", touser);
		// TODO 找到被聊聊天的这个人与当前通道用户的消息列表
		Map<String, Object> map = new HashMap<>();
		map.put("userId", touser);
		map.put("friendId", channelContext.userid);
		map.put("state", "N");
		Map<String, Object> seftWithUserChat = chatListService.getSeftWithUserChat(map);
		if (seftWithUserChat != null) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			seftWithUserChat.put("msg_content", convertMsg.getText().getContent());
			seftWithUserChat.put("content", convertMsg.getText().getContent());
			Integer val = StrUtils.isNull(seftWithUserChat.get("unread_num")) ? 1 : Integer.valueOf(String.valueOf(seftWithUserChat.get("unread_num"))) + 1;
			seftWithUserChat.put("unread_num", val);
			list.add(seftWithUserChat);
			busiMsg.put("receiveChatMap", list);
		}
		boolean sended = CBImUtils.sendToUser(channelContext, touser, msg);
		if (!touser.equals(channelContext.userid)) {
			// 满足小程序需求
			CBImUtils.sendToUser(channelContext, channelContext.userid, msg);
		}
		//更新聊天列表数据
		chatListService.save(null, touser, channelContext.userid, "1", 1, convertMsg.getText().getContent(), sended);
		//将聊天数据入库
		TimPrivateMsg privateMsg = new TimPrivateMsg();
		privateMsg.setMsgContent(convertMsg.getText().getContent()); // 消息内容
		privateMsg.setReceiveId(convertMsg.getTouser()); // 接收者ID
		privateMsg.setSendId(convertMsg.getFromuser()); // 发送者ID
		privateMsg.setMsgType(convertMsg.getMsgtype());
		Date date = DateUtils.parse(convertMsg.getMsgtime(), "yyyy-MM-dd HH:mm:ss");
		privateMsg.setSendTime(date); // 发送时间
		Long seq = redisUtils.incr(Const.REDIS_KEY_PRIVATEMSGNO + ":" + convertMsg.getMsgtime(), 60*60*24);
		privateMsg.setMsgSeq(DateUtils.format(date, "yyyyMMddHHmmss") + StrUtils.apadLeft(seq, 0, 4));
		if(sended) {
			privateMsg.setReadState("Y"); // 阅读状态
			privateMsg.setReadTime(DateUtils.getNowDate());
		}else {
			privateMsg.setReadState("N"); // 阅读状态
		}
		privateMsgService.save(privateMsg);
	}
	
	/**
	 * 发送文件
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="send", msgType=MsgType.FILE)
	public void sendFile(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		FileMessage convertMsg = CBImUtils.convertMsg(busiMsg, FileMessage.class);
		String mediaIds = convertMsg.getFile().getMedia_id();
		for(String mediaId : mediaIds.split(",")) {
			doSave(msg, channelContext, convertMsg, mediaId);
		}
	}
	@Transactional
	private void doSave(JSONObject msg, ChannelContext channelContext, BaseMessage convertMsg, String mediaId){
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		String touser = convertMsg.getTouser();
		TimUserinfo userInfo = (TimUserinfo)channelContext.get(Const.CHANNEL_INFO_USERINFO);
		TimAttach attach = timAttachService.selectObjectById(mediaId);
		if(attach == null) {
			return;
		}
		busiMsg.clear();
		busiMsg.put("send_id", channelContext.userid);
		busiMsg.put("send_name", userInfo.getUserRealname());
		busiMsg.put("send_img", userInfo.getUserimg());
		busiMsg.put("msg_type", attach.getAttachType());
		busiMsg.put("msg_content", attach.getAccessUrl());
		busiMsg.put("file_name", attach.getFileName());
		busiMsg.put("file_size", attach.getFileSize());
		busiMsg.put("duration_time", attach.getDurationTime());
		busiMsg.put("first_capture_access_url", attach.getFirstCaptureAccessUrl());
		busiMsg.put("send_time", convertMsg.getMsgtime());
		busiMsg.put("touser", touser);
		String title = "";
		if(MsgType.VIDEO.equals(attach.getAttachType())) {
			title = "视频";
		}else if(MsgType.IMAGE.equals(attach.getAttachType())) {
			title = "图片";
		}else if(MsgType.VOICE.equals(attach.getAttachType())) {
			title = "语音";
		}else if(MsgType.NEWS.equals(attach.getAttachType())) {
			title = "图文";
		}else {
			title = "文件";
		}
		// TODO 找到被聊聊天的这个人与当前通道用户的消息列表
		Map<String, Object> map = new HashMap<>();
		map.put("userId", touser);
		map.put("friendId", channelContext.userid);
		map.put("state", "N");
		Map<String, Object> seftWithUserChat = chatListService.getSeftWithUserChat(map);
		if (seftWithUserChat != null) {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			seftWithUserChat.put("msg_content", title);
			seftWithUserChat.put("content", title);
			Integer val = StrUtils.isNull(seftWithUserChat.get("unread_num")) ? 1 : Integer.valueOf(String.valueOf(seftWithUserChat.get("unread_num"))) + 1;
			seftWithUserChat.put("unread_num", val);
			list.add(seftWithUserChat);
			busiMsg.put("receiveChatMap", list);	
		}
		boolean sended = CBImUtils.sendToUser(channelContext, touser, msg);
		if (!touser.equals(channelContext.userid)) {
			// 满足小程序需求
			CBImUtils.sendToUser(channelContext, channelContext.userid, msg);	
		}
		//更新聊天列表数据
		chatListService.save(null, touser, channelContext.userid, "1", 1, "[" + title + "]", sended);
		//将聊天数据入库
		TimPrivateMsg privateMsg = new TimPrivateMsg();
		privateMsg.setMsgContent(attach.getAccessUrl()); // 消息内容
		privateMsg.setAttachId(mediaId);
		privateMsg.setReceiveId(convertMsg.getTouser()); // 接收者ID
		privateMsg.setSendId(convertMsg.getFromuser()); // 发送者ID
		privateMsg.setMsgType(attach.getAttachType());
		Date date = DateUtils.parse(convertMsg.getMsgtime(), "yyyy-MM-dd HH:mm:ss");
		privateMsg.setSendTime(date); // 发送时间
		Long seq = redisUtils.incr(Const.REDIS_KEY_PRIVATEMSGNO + ":" + convertMsg.getMsgtime(), 60*60*24);
		privateMsg.setMsgSeq(DateUtils.format(date, "yyyyMMddHHmmss") + StrUtils.apadLeft(seq, 0, 4));
		if(sended) {
			privateMsg.setReadState("Y"); // 阅读状态
			privateMsg.setReadTime(DateUtils.getNowDate());
		}else {
			privateMsg.setReadState("N"); // 阅读状态
		}
		privateMsgService.save(privateMsg);
		//将附件和消息绑定
		TimAttach timAttach = new TimAttach();
		timAttach.setAttachId(mediaId);
		timAttach.setMsgId(privateMsg.getMsgId());
		timAttach.setAttachSource("1");//1私聊2群组
		timAttach.setAttachType(attach.getAttachType());
		timAttachService.update(timAttach);
	}
	
	/**
	 * 进入私聊界面时拉取历史消息
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="list")
	public void list(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		Map<String, Object> map = new HashMap<>();
		map.put("receiveId", channelContext.userid);
		map.put("sendId", busiMsg.get("touser"));
		map.put("lastid", busiMsg.get("lastid"));
		List<Map<String, Object>> datas = privateMsgService.queryChatList(map);
		msg.put(Const.BUSI_MSG, datas);
		CBImUtils.sendToSelf(channelContext, msg);
	}
}
