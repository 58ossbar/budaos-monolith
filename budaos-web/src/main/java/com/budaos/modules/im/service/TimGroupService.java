package com.budaos.modules.im.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.im.domain.TimGroup;
import com.budaos.modules.im.persistence.TimGroupMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 群组服务类
 * @author huj
 *
 */
@Service
public class TimGroupService {

	@Autowired
	private TimGroupMapper timGroupMapper;
	
	/**
	 * 创建一个群聊（讨论组）
	 * @param t
	 * @return R.ok().put(Constant.R_DATA, t.getGroupId()) 返回群聊或讨论组主键
	 */
	public R save(TimGroup t) {
		if (StrUtils.isEmpty(t.getCreateUser())) {
			return R.error("参数createUser为空");
		}
		// 主键组ID
		if (StrUtils.isEmpty(t.getGroupId())) {
			String uuid = Identities.uuid();
			t.setGroupId(uuid);	
		}
		// 成员数量
		t.setGroupNum(t.getGroupNum() == null ? 0 : t.getGroupNum()); 
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setState("Y");
		timGroupMapper.insert(t);
		return R.ok().put(Constant.R_DATA, t.getGroupId());
	}
	
	/**
	 * 更新
	 * @param timGroup
	 */
	public void update(TimGroup timGroup) {
		timGroupMapper.update(timGroup);
	}
	
	/**
	 * 更新数量
	 * @param timGroup
	 */
	public void plusNum(TimGroup timGroup) {
		timGroupMapper.plusNum(timGroup);
	}
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	public TimGroup selectObjectById(Object id) {
		return timGroupMapper.selectObjectById(id);
	}
	
	/**
	 * 批量新增
	 * @param list
	 */
	public void insertBatch(List<TimGroup> list) {
		timGroupMapper.insertBatch(list);
	}
}
