package com.budaos.modules.im.service;

import com.budaos.common.utils.Query;
import com.budaos.modules.im.domain.TimChatList;
import com.budaos.modules.im.persistence.TimChatListMapper;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息列表服务类
 * @author zhuq
 *
 */
@Service
public class TimChatListService {
	@Autowired
	private TimChatListMapper timChatListMapper;
	
	/**
	 * 获取消息列表数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryChatList(Map<String, Object> params) {
		if ("Y".equals(params.get("nopage"))) {
			List<Map<String, Object>> datas = timChatListMapper.selectListMapByMap(params);
			datas.stream().forEach(item -> {
				item.put("isTop", StrUtils.isNull(item.get("top_time")) ? "N" : "Y");
			});
			return datas;
		}
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> datas = timChatListMapper.selectListMapByMap(query);
		datas.stream().forEach(item -> {
			item.put("isTop", StrUtils.isNull(item.get("top_time")) ? "N" : "Y");
		});
		return datas;
	}
	
	/**
	 * 累加未读消息数量
	 * @param chat
	 * @return
	 */
	public void plusNum(TimChatList chat) {
		timChatListMapper.plusNum(chat);
	}

	/**
	 * 更新为已读
	 * @param chat
	 * @return
	 */
	public void updateNum(TimChatList chat) {
		timChatListMapper.update(chat);
	}
	/**
	 * 新增/修改
	 * @param chat
	 * @return
	 */
	public void save(TimChatList chat, boolean sended) {
		chat.setState("Y");
		Map<String, Object> map = new HashMap<>();
		map.put("listId", chat.getListId());
		map.put("userId", chat.getUserId());
		map.put("friendId", chat.getFriendId());
		map.put("friendType", chat.getFriendType());
		List<TimChatList> datas = timChatListMapper.selectListByMap(map);
		chat.setUpdateTime(DateUtils.getNowTimeStamp());
		if(datas.size() == 0) {
			chat.setListId(Identities.uuid());
			timChatListMapper.insert(chat);
		}else {
			if(!sended) {
				plusNum(chat);
			}else {
				chat.setUnreadNum(0);
				updateNum(chat);
			}
		}
	}
	
	/**
	 * 新增/修改
	 * @param listId 表t_im_chat_list主键
	 * @param userId 用户
	 * @param friendId 私聊id或群聊id
	 * @param friendType 类型，1私聊2群聊
	 * @param unreadNum 未读数
	 * @param content 内容
	 * @param sended 是否发送了
	 * @param targetType 非必传参数，类型，1答疑讨论
	 * @param targetId 非必传参数
	 */
	public void save(String listId, String userId, String friendId, String friendType, Integer unreadNum, String content, boolean sended) {
		//给对方聊天列表更新数据
		TimChatList chat = new TimChatList();
		chat.setListId(listId);
		chat.setUserId(userId);
		chat.setFriendId(friendId);
		chat.setFriendType(friendType);
		chat.setUnreadNum(unreadNum);
		chat.setContent(content);
		chat.setState("Y");
		save(chat, sended);
		//给自己列表更新数据
		if(StrUtils.isEmpty(listId)) {
			TimChatList chatSelf = new TimChatList();
			chatSelf.setListId(listId);
			chatSelf.setUserId(friendId);
			chatSelf.setFriendId(userId);
			chatSelf.setFriendType(friendType);
			chatSelf.setUnreadNum(unreadNum);
			chatSelf.setContent(content);
			chatSelf.setState("Y");
			save(chatSelf, true);
		}
	}
	
	/**
	 * 新增/修改
	 * @param listId 表t_im_chat_list主键
	 * @param userId 用户
	 * @param friendId 私聊id或群聊id
	 * @param friendType 类型，1私聊2群聊
	 * @param unreadNum 未读数
	 * @param content 内容
	 * @param sended 是否发送了
	 * @param targetType 非必传参数，类型，1答疑讨论
	 * @param targetId 非必传参数
	 */
	public void save(String listId, String userId, String friendId, String friendType, Integer unreadNum, 
			String content, boolean sended, String targetType, String targetId) {
		//给对方聊天列表更新数据
		TimChatList chat = new TimChatList();
		chat.setListId(listId);
		chat.setUserId(userId);
		chat.setFriendId(friendId);
		chat.setFriendType(friendType);
		chat.setUnreadNum(unreadNum);
		chat.setContent(content);
		chat.setTargetType(targetType);
		chat.setTargetId(targetId);
		chat.setState("Y");
		save(chat, sended);
		//给自己列表更新数据
		if(StrUtils.isEmpty(listId)) {
			TimChatList chatSelf = new TimChatList();
			chatSelf.setListId(listId);
			chatSelf.setUserId(friendId);
			chatSelf.setFriendId(userId);
			chatSelf.setFriendType(friendType);
			chatSelf.setUnreadNum(unreadNum);
			chatSelf.setContent(content);
			chatSelf.setTargetType(targetType);
			chatSelf.setTargetId(targetId);
			chatSelf.setState("Y");
			save(chatSelf, true);
		}
	}
	
	public List<TimChatList> selectListByMap(Map<String, Object> map){
		return timChatListMapper.selectListByMap(map);
	}
	
	/**
	 * 更新
	 * @param chat
	 * @return
	 */
	public void update(TimChatList chat) {
		timChatListMapper.update(chat);
	}
	
	/**
	 * 获取自己和某人的聊天列表
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSeftWithUserChat( Map<String, Object> map){
		return timChatListMapper.getSeftWithUserChat(map);
	}
	
	/**
	 * 获取自己与某群的聊天列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getWithGroupChat( Map<String, Object> map){
		return timChatListMapper.getWithGroupChat(map);
	}
	
	/**
	 * 根据条件查询记录，仅返回list_id、user_id、friend_id、unread_num
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> selectSimpleList(Map<String, Object> map){
		return timChatListMapper.selectSimpleList(map);
	}
	
	public void delete(Object id) {
		timChatListMapper.delete(id);
	}
	
	public void deleteBatch(Object[] ids) {
		timChatListMapper.deleteBatch(ids);
	}
}
