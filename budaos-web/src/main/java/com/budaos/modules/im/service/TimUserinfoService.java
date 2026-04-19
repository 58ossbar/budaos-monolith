package com.budaos.modules.im.service;

import com.budaos.modules.im.domain.TimUserinfo;
import com.budaos.modules.im.persistence.TimUserinfoMapper;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表服务类
 * @author zhuq
 *
 */
@Service
public class TimUserinfoService {

	@Autowired
	private TimUserinfoMapper timUserinfoMapper;
	
	/**
	 * 新增/修改用户信息
	 * @param info
	 */
	public void save(TimUserinfo info) {
		if(StrUtils.isNotEmpty(info.getUserId())) {
			info.setUpdateTime(DateUtils.getNowTimeStamp());
			timUserinfoMapper.update(info);
			return;
		}
		info.setUserId(info.getRelateId());
		info.setCreateTime(DateUtils.getNowTimeStamp());
		timUserinfoMapper.insert(info);
	}
	
	/**
	 * 列表查询(无分页)
	 * @param map
	 * @return
	 */
	public List<TimUserinfo> selectListByMapNoPage(Map<String, Object> map){
		return timUserinfoMapper.selectListByMap(map);
	}
	
	public TimUserinfo selectObjectById(String userId) {
		TimUserinfo data = timUserinfoMapper.selectObjectById(userId);
		return data;
	}
}
