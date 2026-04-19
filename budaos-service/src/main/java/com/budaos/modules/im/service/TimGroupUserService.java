package com.budaos.modules.im.service;

import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.im.domain.TimGroupUser;
import com.budaos.modules.im.persistence.TimGroupUserMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 群聊成员 服务
 * @author huj
 *
 */
@Service
public class TimGroupUserService {

	@Autowired
	private TimGroupUserMapper timGroupUserMapper;

	/**
	 * 根据群组id获取群组所有成员（从群组成员表t_im_group_user查询数据）
	 * @param groupId
	 * @return
	 */
	public List<TimGroupUser> selectListByGroupId(Object groupId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", groupId);
		List<TimGroupUser> list = timGroupUserMapper.selectListByMap(params);
		return list;
	}
	
	/**
	 * 获取某用户所在的所有群聊
	 * @param userId
	 * @return
	 */
	public List<String> listGroupIds(String userId){
		List<Map<String,Object>> list = timGroupUserMapper.selectGroupIds(userId);
		return list.stream().map(a -> a.get("groupId").toString()).collect(Collectors.toList());
	}
	
	/**
	 * 新增
	 * @param timGroupUser
	 */
	public void save(TimGroupUser timGroupUser) {
		if (StrUtils.isEmpty(timGroupUser.getGroupuserId())) {
			timGroupUser.setGroupuserId(Identities.uuid());
		}
		timGroupUserMapper.insert(timGroupUser);
	}
	
	/**
	 * 批量新增群组用户
	 * @param list
	 */
	public void insertBatch(List<TimGroupUser> list) {
		try {
			timGroupUserMapper.insertBatch(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改
	 * @param timGroupUser
	 */
	public void update(TimGroupUser timGroupUser) {
		timGroupUserMapper.update(timGroupUser);
	}
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	public List<TimGroupUser> selectListByMap(Map<String, Object> map) {
		return timGroupUserMapper.selectListByMap(map);
	}
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	public R query(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> list = timGroupUserMapper.selectListMapByMap(query);
		list.stream().forEach(a -> {
			if (!StrUtils.isNull(a.get("userimg"))) {
				String userimg = a.get("userimg").toString();
				if (userimg.indexOf("uploads") != -1) {
					String first = userimg.substring(0, 1);
					if (!"/".equals(first)) {
						a.put("userimg", "/" + userimg);
					}
				}
			}
		});
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 根据user_id查询groupuser_id
	 * @param userId
	 * @return
	 */
	public String selectGroupuserIdByUserId(String userId, String groupId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("groupId", groupId);
		return timGroupUserMapper.selectGroupuserIdByUserId(map);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void delete(Object id) {
		timGroupUserMapper.delete(id);
	}
	
	public void deleteBatch(Object[] ids) {
		timGroupUserMapper.deleteBatch(ids);
	}
}
