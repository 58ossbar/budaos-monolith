package com.budaos.modules.im.service;

import com.budaos.modules.im.domain.TimChatListTop;
import com.budaos.modules.im.persistence.TimChatListTopMapper;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TimChatListTopService {

	@Autowired
	private TimChatListTopMapper timChatListTopMapper;
	
	/**
	 * 保存
	 * @param timChatListTop
	 */
	public void save(TimChatListTop timChatListTop) {
		timChatListTop.setTopId(StrUtils.isEmpty(timChatListTop.getTopId()) ? Identities.uuid() : timChatListTop.getTopId());
		timChatListTop.setTopTime(StrUtils.isEmpty(timChatListTop.getTopTime()) ? DateUtils.getNowTimeStamp() : timChatListTop.getTopTime());
		timChatListTopMapper.insert(timChatListTop);
	}
	
	/**
	 * 根据条件查询数据
	 * @param map
	 * @return
	 */
	public List<TimChatListTop> selectListByMap(Map<String, Object> map){
		List<TimChatListTop> list = timChatListTopMapper.selectListByMap(map);
		return list;
	}
	
	public void deleteBatch(Object[] ids) {
		timChatListTopMapper.deleteBatch(ids);
	}
	
	public void delete(Object id) {
		timChatListTopMapper.delete(id);
	}
}
