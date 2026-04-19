package com.budaos.modules.im.service;

import com.budaos.common.utils.Query;
import com.budaos.modules.im.domain.TimGroupMsg;
import com.budaos.modules.im.domain.TimGroupState;
import com.budaos.modules.im.domain.TimGroupUser;
import com.budaos.modules.im.persistence.TimGroupMsgMapper;
import com.budaos.modules.im.persistence.TimGroupStateMapper;
import com.budaos.modules.im.persistence.TimGroupUserMapper;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimGroupMsgService {

	private static Logger log = LoggerFactory.getLogger(TimGroupMsgService.class);
	
	@Autowired
	private TimGroupMsgMapper timGroupMsgMapper;
	@Autowired
	private TimGroupStateMapper timGroupStateMapper;
	@Autowired
	private TimGroupUserMapper timGroupUserMapper;
	
	/**
	 * 保存群聊消息（往表t_im_group_msg插入数据）
	 * @param msg
	 * @return 返回消息ID
	 */
	public String saveGroupMsg(TimGroupMsg msg) {
		// 消息ID
		msg.setMsgId(StrUtils.isEmpty(msg.getMsgId()) ? Identities.uuid() : msg.getMsgId());
		// 应阅读人数
		msg.setUnreadNum(msg.getUnreadNum() == null ? 0 : msg.getUnreadNum());
		// 实际已阅读人数
		msg.setReadNum(msg.getReadNum() == null ? 0 : msg.getReadNum());
		// 插入数据库
		timGroupMsgMapper.insert(msg);
		// 返回主键
		return msg.getMsgId();
	}
	
	/**
	 * 保存群组消息接收状态（往表t_im_group_state插入数据）
	 * @param state
	 * @return
	 */
	public String saveGroupState(TimGroupState state) {
		state.setStateId(Identities.uuid()); // 消息状态ID
		if (StrUtils.isEmpty(state.getReadState())) { // 阅读状态(1.未读;2.已读)
			state.setReadState("1");
		}
		timGroupStateMapper.insert(state);
		return state.getStateId();
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryChatList(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String,Object>> list = timGroupMsgMapper.selectListMapByMap(query);
		log.debug("消息 => " + list.size());
		list.forEach(a -> {
			SimpleDateFormat sdfS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdfT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			try {
				a.put("send_time", sdfS.format(sdfT.parse(a.get("send_time").toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return list;
	}
	
	/**
	 * 标识消息已读
	 * @param friendId 保证其为群聊ID
	 * @param fromuser 用户， t_im_userinfo的主键或relate_id，同时也是表t_im_group_user的user_id
	 * @param readTime 阅读时间
	 * @return
	 */
	public Integer updateIsRead(String friendId, String fromuser, Date readTime) {
		Integer val = 0;
		Map<String, Object> map = new HashMap<>();
		// 先找这个群的所有成员
		map.put("groupId", friendId);
		List<TimGroupUser> timGroupUserList = timGroupUserMapper.selectListByMap(map);
		List<String> groupuserIds = timGroupUserList.stream().map(a -> a.getGroupuserId()).collect(Collectors.toList());
		// 再去找到消息主表的记录
		map.clear();
		map.put("groupuserIds", groupuserIds);
		List<TimGroupMsg> timGroupMsgList = timGroupMsgMapper.selectListByMap(map);
		if (timGroupMsgList == null || timGroupMsgList.size() == 0) {
			return null;
		}
		List<String> msgIds = timGroupMsgList.stream().map(a -> a.getMsgId()).collect(Collectors.toList());
		// 再从状态表找到相应记录
		map.clear();
		map.put("msgIds", msgIds);
		map.put("userId", fromuser); // 筛选出发送给当前用户的
		map.put("readState", "1"); // 阅读状态(1.未读;2.已读)
		List<TimGroupState> timGroupStateList = timGroupStateMapper.selectListByMap(map);
		// 主表消息已读数+1未读数-1
		List<String> msgIdList = timGroupStateList.stream().map(a -> a.getMsgId()).collect(Collectors.toList());
		if (msgIdList != null && msgIdList.size() > 0) {
			map.clear();
			map.put("msgIds", msgIdList);
			map.put("readNum", 1);
			map.put("unreadNum", -1);
			timGroupMsgMapper.plusNumBatch(map);	
		}
		// 最后状态表记录标识为已读
		List<String> stateIds = timGroupStateList.stream().map(a -> a.getStateId()).collect(Collectors.toList());
		if (stateIds != null && stateIds.size() > 0) {
			map.clear();
			map.put("stateIds", stateIds);
			map.put("readState", "2"); // 1未读2已读
			map.put("readTime", readTime);
			val = timGroupStateMapper.updateBatch(map);
		}	
		return val; 
	}
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	public TimGroupMsg selectObjectById(Object id) {
		return timGroupMsgMapper.selectObjectById(id);
	}
	
}
