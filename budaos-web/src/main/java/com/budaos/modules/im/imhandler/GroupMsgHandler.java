package com.budaos.modules.im.imhandler;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.im.core.annotation.CBImHandler;
import com.budaos.modules.im.core.annotation.CBImService;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.entity.BaseMessage;
import com.budaos.modules.im.core.entity.FileMessage;
import com.budaos.modules.im.core.entity.MsgType;
import com.budaos.modules.im.core.entity.TextMessage;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.im.domain.*;
import com.budaos.modules.im.service.TimAttachService;
import com.budaos.modules.im.service.TimChatListService;
import com.budaos.modules.im.service.TimGroupMsgService;
import com.budaos.modules.im.service.TimGroupUserService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.redis.RedisUtils;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群聊消息
 * 
 * @author huj
 *
 */
@CBImService("groupchat")
@Service
public class GroupMsgHandler {

	private static Logger log = LoggerFactory.getLogger(GroupMsgHandler.class);

	@Autowired
	private TimGroupMsgService timGroupMsgService;
	@Autowired
	private TimGroupUserService timGroupUserService;
	@Autowired
	private TimAttachService timAttachService;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private TimChatListService timChatListService;
	
	/**
	 * 获取群聊所有成员
	 * 
	 * @param groupId
	 * @return
	 */
	private List<TimGroupUser> getAllUser(String groupId) {
		return timGroupUserService.selectListByGroupId(groupId);
	}
	
	/**
	 * 发送文本消息，注意，参数位置顺序要保证为WsRequest、JSONObject、ChannelContext
	 * 
	 * @param wsRequest
	 * @param msg            一个示例：{"msg":{"msgtime":"2020-08-12
	 *                       14:36:25","fromuser":"2","text":{"content":"Hi"},"msgtype":"text","toparty":"788cfafad8314d50b0b23affec883cdc"},"busitype":"/groupchat/send","msgtype":"text"}
	 * @param channelContext
	 */
	@CBImHandler(value = "send", msgType = MsgType.TEXT)
	public void sendText(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		String userId = channelContext.userid; // 发送人
		TextMessage convertMsg = CBImUtils.convertMsg(busiMsg, TextMessage.class);
		String groupId = convertMsg.getToparty(); // 接收的群组
		log.debug("发送人：" + userId);
		log.debug("接收的群组：" + groupId);
		TimUserinfo userInfo = (TimUserinfo) channelContext.get(Const.CHANNEL_INFO_USERINFO);
		busiMsg.clear();
		busiMsg.put("send_id", channelContext.userid);
		busiMsg.put("send_name", userInfo.getUserRealname());
		busiMsg.put("send_img", handle(userInfo.getUserimg()));
		busiMsg.put("msg_type", MsgType.TEXT);
		busiMsg.put("msg_content", convertMsg.getText().getContent());
		busiMsg.put("send_time", convertMsg.getMsgtime());
		busiMsg.put("toparty", groupId);
		// 找到所有将本群设置了无效的聊天列表
		Map<String, Object> map = new HashMap<>();
		map.put("friendId", groupId);
		map.put("state", "N");
		List<Map<String, Object>> groupChatList = timChatListService.getWithGroupChat(map);
		groupChatList.stream().forEach(item -> {
			Integer val = StrUtils.isNull(item.get("unread_num")) ? 1 : Integer.valueOf(String.valueOf(item.get("unread_num"))) + 1;
			item.put("unread_num", val);
			item.put("content", convertMsg.getText().getContent());
		});
		busiMsg.put("receiveChatMap", groupChatList);
		// 群发
		CBImUtils.sendToGroup(channelContext, groupId, new R().put(Constant.R_DATA, msg));
		// 获取本群所有成员
		boolean sended = false;
		Integer unreadNum = 1;
		List<TimGroupUser> timGroupUserList = timGroupUserService.selectListByGroupId(groupId);
		for (TimGroupUser timGroupUser : timGroupUserList) {
			if (userId.equals(timGroupUser.getUserId())) {
				sended = true; // 自己时，标识为已发送
				unreadNum = 0; // 自己时，直接认为已读
			} else {
				sended = false;
				unreadNum = 1;
			}
			// 更新自己和群里其它人的聊天列表数据
			timChatListService.save(null, timGroupUser.getUserId(), groupId, "2", unreadNum, 
					convertMsg.getText().getContent(), sended, null, null);
		}
		// 消息入库 往群组表插入一条消息
		doSaveTextMsg(convertMsg, channelContext.userid, timGroupUserList);
	}

	/**
	 * 往群组表插入一条文本消息
	 * @param textMsg
	 * @param userId 用户id 表（t_im_group_user）的user_id字段、表t_im_userinfo的user_id、relate_id字段
	 * @param timGroupUserList 所有群成员
	 * @return
	 */
	private String doSaveTextMsg(TextMessage textMsg, String userId, List<TimGroupUser> timGroupUserList) {
		String groupuserId = timGroupUserService.selectGroupuserIdByUserId(userId, textMsg.getToparty());
		// 实例化对象
		TimGroupMsg timGroupMsg = new TimGroupMsg();
		timGroupMsg.setGroupuserId(groupuserId); // 发送人，已表t_im_group_user的主键作为发送人
		timGroupMsg.setMsgContent(textMsg.getText().getContent()); // 消息内容
		timGroupMsg.setMsgType(textMsg.getMsgtype()); // 消息类型
		timGroupMsg.setUnreadNum(timGroupUserList.size() - 1); // 应阅读人数
		timGroupMsg.setReadNum(1); // 自己是已读，则默认已读数为1
		Date date = DateUtils.parse(textMsg.getMsgtime(), "yyyy-MM-dd HH:mm:ss");
		timGroupMsg.setMsgSendTime(date); // 发送时间
		Long seq = redisUtils.incr(Const.REDIS_KEY_PRIVATEMSGNO + ":" + textMsg.getMsgtime(), 60 * 60 * 24);
		timGroupMsg.setMsgSeq(DateUtils.format(date, "yyyyMMddHHmmss") + StrUtils.apadLeft(seq, 0, 4));
		// 插入数据库
		String msgId = timGroupMsgService.saveGroupMsg(timGroupMsg);
		// 接着往【群组消息接收状态表t_im_group_state】中为插入消息 （状态表）
		for (TimGroupUser timGroupUser : timGroupUserList) {
			TimGroupState t = new TimGroupState();
			t.setStateId(Identities.uuid());
			t.setMsgId(msgId); // 消息ID
			t.setGroupuserId(timGroupUser.getGroupuserId()); // 成员ID
			t.setUserId(timGroupUser.getUserId()); // 用户ID
			t.setReadTime(null); // 阅读时间
			t.setReadState("1"); // 阅读状态(1.未读;2.已读)
			// 如果是自己，则标识为已读
			if (userId.equals(timGroupUser.getUserId())) {
				t.setReadTime(date);
				t.setReadState("2");
			}
			timGroupMsgService.saveGroupState(t);
		}
		return msgId;
	}
	
	/**
	 * 发送文件消息
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value="send", msgType=MsgType.FILE)
	public void sendFile(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		FileMessage convertMsg = CBImUtils.convertMsg(busiMsg, FileMessage.class);
		String mediaIds = convertMsg.getFile().getMedia_id();
		// 找到所有将本群设置了无效的聊天列表
		Map<String, Object> map = new HashMap<>();
		map.put("friendId", convertMsg.getToparty());
		map.put("state", "N");
		List<Map<String, Object>> groupChatList = timChatListService.getWithGroupChat(map);
		groupChatList.stream().forEach(item -> {
			Integer val = StrUtils.isNull(item.get("unread_num")) ? 1 : Integer.valueOf(String.valueOf(item.get("unread_num"))) + 1;
			item.put("unread_num", val);
		});
		for(String mediaId : mediaIds.split(",")) {
			doSave(msg, channelContext, convertMsg, mediaId, groupChatList);
		}
	}
	
	/**
	 * 实际保存
	 * @param msg
	 * @param channelContext
	 * @param convertMsg 消息基类 {"fromuser": "", "touser": "", "toparty": "", "totag": "", "msgtype": ""} 等
	 * @param mediaId 附件主键
	 */
	@Transactional
	private void doSave(JSONObject msg, ChannelContext channelContext, BaseMessage convertMsg, String mediaId, List<Map<String, Object>> groupChatList){
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		String groupId = convertMsg.getToparty(); // 目标群组
		String userId = channelContext.userid; // 用户
		TimUserinfo userInfo = (TimUserinfo)channelContext.get(Const.CHANNEL_INFO_USERINFO);
		TimAttach attach = timAttachService.selectObjectById(mediaId);
		String title = getTitle(attach);
		busiMsg.clear();
		busiMsg.put("send_id", channelContext.userid); // 发送人
		busiMsg.put("send_name", userInfo.getUserRealname()); // 发送人姓名
		busiMsg.put("send_img", handle(userInfo.getUserimg())); // 发送人头像
		busiMsg.put("msg_type", attach.getAttachType()); // 消息类型
		busiMsg.put("msg_content", attach.getAccessUrl()); // 消息内容
		busiMsg.put("file_name", attach.getFileName());
		busiMsg.put("file_size", attach.getFileSize());
		busiMsg.put("duration_time", attach.getDurationTime());
		busiMsg.put("first_capture_access_url", attach.getFirstCaptureAccessUrl());
		busiMsg.put("send_time", convertMsg.getMsgtime());
		busiMsg.put("toparty", groupId);
		groupChatList.stream().forEach(a -> {
			a.put("content", title);
		});
		busiMsg.put("receiveChatMap", groupChatList);
		// 群发
		CBImUtils.sendToGroup(channelContext, groupId, new R().put(Constant.R_DATA, msg));
		// 找到本群所有成员
		List<TimGroupUser> allUser = getAllUser(groupId);
		// 更新自己和群里其它人的聊天列表数据
		boolean sended = false;
		Integer unreadNum = 1;
		for (TimGroupUser timGroupUser : allUser) {
			/*// TODO 更新聊天列表数量
			List<Map<String, Object>> collect = groupChatList.stream()
					.filter(a -> "N".equals(a.get("state")) && a.get("user_id").equals(timGroupUser.getUserId()))
					.collect(Collectors.toList());
			if (collect.size() > 0) {
				JSONObject ob = new JSONObject();
				ob.put("titile", "测试");
				ob.put("busitype", "receiveChatList");
				ob.put("msgtype", "other");
				collect.get(0).put("content", title);
				ob.put("msg", collect.size() > 0 ? collect.get(0) : null);
				CBImUtils.sendToUser(channelContext, timGroupUser.getUserId(), ob);
			}*/
			if (userId.equals(timGroupUser.getUserId())) {
				sended = true; // 自己时，标识为已发送
				unreadNum = 0; // 自己时，直接认为已读
			} else {
				sended = false;
				unreadNum = 1;
			}
			timChatListService.save(null, timGroupUser.getUserId(), groupId, "2", unreadNum, "[" + title + "]", sended);	
		}
		String groupuserId = timGroupUserService.selectGroupuserIdByUserId(userId, groupId);
		// 群聊消息入库、实例化群组对象并填充相关数据
		TimGroupMsg timGroupMsg = new TimGroupMsg();
		timGroupMsg.setMsgContent(attach.getAccessUrl());
		timGroupMsg.setGroupuserId(groupuserId); // 发送人
		timGroupMsg.setMsgType(attach.getAttachType()); // 消息类型
		timGroupMsg.setAttachId(mediaId); // 附件ID
		timGroupMsg.setUnreadNum(allUser.size() - 1); // 应阅读人数
		timGroupMsg.setReadNum(1); // 自己是已读，则默认已读数为1
		Date date = DateUtils.parse(convertMsg.getMsgtime(), "yyyy-MM-dd HH:mm:ss");
		Long seq = redisUtils.incr(Const.REDIS_KEY_PRIVATEMSGNO + ":" + convertMsg.getMsgtime(), 60 * 60 * 24);
		timGroupMsg.setMsgSendTime(date); // 发送时间
		timGroupMsg.setMsgSeq(DateUtils.format(date, "yyyyMMddHHmmss") + StrUtils.apadLeft(seq, 0, 4));
		// 主表消息入库
		String msgId = timGroupMsgService.saveGroupMsg(timGroupMsg);
		// 状态表消息入库
		for (TimGroupUser timGroupUser : allUser) {
			TimGroupState t = new TimGroupState();
			t.setMsgId(msgId); // 消息ID
			t.setGroupuserId(timGroupUser.getGroupuserId()); // 成员ID
			t.setUserId(timGroupUser.getUserId()); // userid
			t.setReadTime(null); // 阅读时间
			t.setReadState("1"); // 阅读状态(1.未读;2.已读)
			// 如果是自己，则标识为已读
			if (userId.equals(timGroupUser.getUserId())) {
				t.setReadTime(date);
				t.setReadState("2");
			}
			timGroupMsgService.saveGroupState(t);
		}
		// 绑定附件与消息之间的关系
		TimAttach attachInfo = new TimAttach();
		attachInfo.setAttachId(mediaId);
		attachInfo.setMsgId(msgId);
		attachInfo.setAttachSource("2"); // 1私聊2群组
		attachInfo.setAttachType(attach.getAttachType()); // 消息类型
		timAttachService.update(attachInfo);
	}
	
	private String getTitle(TimAttach attach) {
		// 标题
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
		return title;
	}
	
	/**
	 * 进入群聊界面时拉取历史消息
	 * 
	 * @param wsRequest
	 * @param msg
	 * @param channelContext
	 */
	@CBImHandler(value = "list")
	public void list(WsRequest wsRequest, JSONObject msg, ChannelContext channelContext) {
		JSONObject busiMsg = msg.getJSONObject(Const.BUSI_MSG);
		Map<String, Object> map = new HashMap<>();
		map.put("groupId", busiMsg.get("toparty"));
		map.put("lastid", busiMsg.get("lastid"));
		map.put("loginUserId", channelContext.userid);
		List<Map<String, Object>> list = timGroupMsgService.queryChatList(map);
		list.stream().forEach(a -> {
			a.put("send_img", handle(a.get("send_img")));
		});
		msg.put(Const.BUSI_MSG, list);
		CBImUtils.sendToSelf(channelContext, new R().put(Constant.R_DATA, msg));
	}
	
	private String handle(Object userImg) {
		if (StrUtils.isNull(userImg)) {
			return null;
		}
		String userimg = userImg.toString();
		if (userimg.indexOf("uploads") != -1) {
			String first = userimg.substring(0, 1);
			if (!"/".equals(first)) {
				userimg = "/" + userimg;
			}
		}
		return userimg;
	}
}
