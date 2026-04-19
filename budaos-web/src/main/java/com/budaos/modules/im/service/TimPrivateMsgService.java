package com.budaos.modules.im.service;

import com.budaos.common.utils.Query;
import com.budaos.modules.im.domain.TimPrivateMsg;
import com.budaos.modules.im.persistence.TimPrivateMsgMapper;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 私聊消息表服务类
 * @author zhuq
 *
 */
@Service
public class TimPrivateMsgService {

	@Autowired
	private TimPrivateMsgMapper timPrivateMsgMapper;
	
	public void save(TimPrivateMsg msg) {
		msg.setMsgId(Identities.uuid()); // 消息ID
		timPrivateMsgMapper.insert(msg);
	}
	
	/**
	 * 更新消息状态为已读
	 * @param msgId
	 */
	public Integer updateIsRead(String msgId, String receiveId, String sendId, Date readTime) {
		TimPrivateMsg msg = new TimPrivateMsg();
		msg.setMsgId(msgId);
		msg.setReceiveId(receiveId);
		msg.setSendId(sendId);
		msg.setReadState("Y");
		msg.setReadTime(readTime);
		return timPrivateMsgMapper.update(msg);
	}
	
	public List<Map<String, Object>> queryChatList(Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> datas = timPrivateMsgMapper.selectListMapByMap(query);
		datas.forEach(a -> {
			SimpleDateFormat sdfS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdfT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			try {
				a.put("send_time", sdfS.format(sdfT.parse(a.get("send_time").toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return datas;
	}
}
