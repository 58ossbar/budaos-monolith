package com.budaos.modules.im.utils;

import com.budaos.modules.im.domain.TimChatList;
import com.budaos.modules.im.domain.TimGroupMsg;
import com.budaos.modules.im.domain.TimGroupState;
import com.budaos.modules.im.domain.TimGroupUser;
import com.budaos.modules.im.persistence.TimChatListMapper;
import com.budaos.modules.im.persistence.TimGroupMsgMapper;
import com.budaos.modules.im.persistence.TimGroupStateMapper;
import com.budaos.modules.im.persistence.TimGroupUserMapper;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 群聊的一些方法，如删除群组、群成员
 * @author admin
 *
 */
public class CBImGroupUtils {
	
	@Autowired
	private TimGroupMsgMapper timGroupMsgMapper;
	@Autowired
	private TimGroupUserMapper timGroupUserMapper;
	@Autowired
	private TimGroupStateMapper timGroupStateMapper;
	@Autowired
	private TimChatListMapper timChatListMapper;
	
	/**
	 * 删除群组，群成员、群聊天记录通通删除
	 * @param groupId
	 */
	public void deleteGroup(String groupId) {
		if (StrUtils.isEmpty(groupId)) {
			return;
		}
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		// 先找到群组成员
		params.put("groupId", groupId);
		List<TimGroupUser> timGroupUserList = timGroupUserMapper.selectListByMap(params);
		if (timGroupUserList != null && timGroupUserList.size() > 0) {
			List<String> groupuserIds = timGroupUserList.stream().map(a -> a.getGroupuserId()).collect(Collectors.toList());
			// 再去找到这个群聊的成员的聊天记录
			params.clear();
			params.put("groupuserIds", groupuserIds);
			List<TimGroupMsg> timGroupMsgList = timGroupMsgMapper.selectListByMap(params);
			// 删除主表消息
			for (TimGroupMsg timGroupMsg : timGroupMsgList) {
				timGroupMsgMapper.delete(timGroupMsg.getMsgId());
			}
			// 删除状态表消息
			params.put("groupuserIds", groupuserIds);
			List<TimGroupState> timGroupStateList = timGroupStateMapper.selectListByMap(params);
			for (TimGroupState timGroupState : timGroupStateList) {
				timGroupStateMapper.delete(timGroupState.getStateId());
			}
			// 删除群成员
			for (TimGroupUser timGroupUser : timGroupUserList) {
				timGroupUserMapper.delete(timGroupUser);
			}
		}
		// 删除列表记录
		params.put("friendId", groupId);
		params.put("friendType", "2");
		List<TimChatList> timChatList = timChatListMapper.selectListByMap(params);
		for (TimChatList timChat : timChatList) {
			timChatListMapper.delete(timChat.getListId());
		}
		// 删除群
		timGroupMsgMapper.delete(groupId);
	}
	
	
}
