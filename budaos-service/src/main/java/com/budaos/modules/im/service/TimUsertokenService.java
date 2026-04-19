package com.budaos.modules.im.service;

import com.budaos.modules.im.domain.TimUsertoken;
import com.budaos.modules.im.persistence.TimUsertokenMapper;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimUsertokenService {

	@Autowired
	private TimUsertokenMapper timUsertokenMapper;

	/**
	 * 根据授权码和渠道获取用户记录
	 * @param token
	 * @return
	 */
	public TimUsertoken selectByToken(String token) {
		TimUsertoken info = timUsertokenMapper.selectByToken(token);
		return info;
	}
	
	/**
	 * token入库
	 * @param info
	 */
	public void initToken(String userId, String channel, String token) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("channel", channel);
		List<TimUsertoken> datas = timUsertokenMapper.selectListByMap(map);
		TimUsertoken info = new TimUsertoken();
		info.setUpdateTime(DateUtils.getNowTimeStamp());
		info.setChannel(channel);
		info.setUserId(userId);
		info.setToken(token);
		//新增
		if(datas.size() == 0) {
			info.setUtId(Identities.uuid());
			timUsertokenMapper.insert(info);
		//修改
		}else {
			info.setUtId(datas.get(0).getUtId());
			timUsertokenMapper.update(info);
		}
	}
}
