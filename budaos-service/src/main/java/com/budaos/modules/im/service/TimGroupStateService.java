package com.budaos.modules.im.service;

import com.budaos.modules.im.domain.TimGroupState;
import com.budaos.modules.im.persistence.TimGroupStateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TimGroupStateService {

	@Autowired
	private TimGroupStateMapper timGroupStateMapper;
	
	public List<TimGroupState> selectListByMap(Map<String, Object> map){
		return timGroupStateMapper.selectListByMap(map);
	}
	
	public TimGroupState selectObjectById(Object id) {
		return timGroupStateMapper.selectObjectById(id);
	}
	
}
