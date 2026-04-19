package com.budaos.modules.im.service;

import com.budaos.modules.im.domain.TimAttach;
import com.budaos.modules.im.persistence.TimAttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 附件服务类
 * @author huj
 *
 */
@Service
public class TimAttachService {

	@Autowired
	private TimAttachMapper timAttachMapper;
	
	/**
	 * 新增
	 * @param timAttach
	 */
	public void save(TimAttach timAttach) {
		timAttachMapper.insert(timAttach);
	}
	
	/**
	 * 修改
	 * @param timAttach
	 */
	public void update(TimAttach timAttach) {
		timAttachMapper.update(timAttach);
	}
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	public TimAttach selectObjectById(Object id) {
		return timAttachMapper.selectObjectById(id);
	}
	
	/**
	 * 根据条件查询对象
	 * @param map
	 * @return
	 */
	public List<TimAttach> selectListByMap(Map<String, Object> map){
		return timAttachMapper.selectListByMap(map);
	}
	
	/**
	 * 根据条件查询对象
	 * @param map
	 * @return [{"attachId":"", "accessUrl":"", "attachType":"", "msgId":"", "durationTime":"", "first_capture_acess_url":""}]
	 */
	public List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> map){
		List<TimAttach> timAttachList = timAttachMapper.selectListByMap(map);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		timAttachList.stream().map(a -> {
			Map<String, Object> info = new HashMap<>();
			info.put("attachId", a.getAttachId()); // 附件ID
			info.put("accessUrl", a.getAccessUrl()); // 访问路径
			info.put("attachType", a.getAttachType()); // 类型(与后台MsgType对应)
			info.put("msgId", a.getMsgId()); // 
			info.put("durationTime", a.getDurationTime()); //
			info.put("first_capture_acess_url", null); // 视频第1帧图片访问路径
			return info;
		});
		return list;
	}
	
}
