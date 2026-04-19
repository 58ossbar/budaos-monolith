package com.budaos.modules.evgl.tch.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomRoleprivilegeService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomRoleprivilege;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglTchClassroomRoleprivilegeServiceImpl implements TevglTchClassroomRoleprivilegeService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomRoleprivilegeServiceImpl.class);
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private CbRoomUtils roomUtils;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchClassroomRoleprivilege> tevglTchClassroomRoleprivilegeList = tevglTchClassroomRoleprivilegeMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomRoleprivilegeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchClassroomRoleprivilegeList = tevglTchClassroomRoleprivilegeMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomRoleprivilegeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchClassroomRoleprivilege
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglTchClassroomRoleprivilege tevglTchClassroomRoleprivilege) throws BudaosException {
		tevglTchClassroomRoleprivilege.setId(Identities.uuid());
		ValidatorUtils.check(tevglTchClassroomRoleprivilege);
		tevglTchClassroomRoleprivilegeMapper.insert(tevglTchClassroomRoleprivilege);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClassroomRoleprivilege
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglTchClassroomRoleprivilege tevglTchClassroomRoleprivilege) throws BudaosException {
	    ValidatorUtils.check(tevglTchClassroomRoleprivilege);
		tevglTchClassroomRoleprivilegeMapper.update(tevglTchClassroomRoleprivilege);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglTchClassroomRoleprivilegeMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTchClassroomRoleprivilegeMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomRoleprivilegeMapper.selectObjectById(id));
	}

	/**
	 * 保存
	 * @param jsonObject
	 * @return
	 */
	@Override
	public R savePermissionSet(JSONObject jsonObject, String loginUserId) {
		String ctId = jsonObject.getString("ctId");
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("参数异常，保存失败");
		}
		boolean hasAuth = roomUtils.hasOperatingAuthorization(tevglTchClassroom, loginUserId);
		if (!hasAuth) {
			return R.error("非法操作，没有权限");
		}
		JSONArray jsonArray = jsonObject.getJSONArray("menuIdList");
		// 先直接清除
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ctId", ctId);
		List<TevglTchClassroomRoleprivilege> list = tevglTchClassroomRoleprivilegeMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			List<String> collect = list.stream().map(a -> a.getId()).collect(Collectors.toList());
			tevglTchClassroomRoleprivilegeMapper.deleteBatch(collect.stream().toArray(String[]::new));
		}
		// 再重新设置
		if (jsonArray != null && jsonArray.size() > 0) {
			List<String> menuIdList = new ArrayList<String>();
			for (int i = 0; i < jsonArray.size(); i++) {
				if (!menuIdList.contains(jsonArray.getString(i))) {
					menuIdList.add(jsonArray.getString(i));
				}
			}
			List<TevglTchClassroomRoleprivilege> insertList = new ArrayList<TevglTchClassroomRoleprivilege>();
			for (int i = 0; i < menuIdList.size(); i++) {
				TevglTchClassroomRoleprivilege t = new TevglTchClassroomRoleprivilege();
				t.setId(Identities.uuid());
				t.setCtId(ctId);
				t.setRoleId("1"); // 直接写死为助教角色
				t.setMenuId(menuIdList.get(i));
				insertList.add(t);
			}
			tevglTchClassroomRoleprivilegeMapper.insertBatch(insertList);
		}
		return R.ok("设置成功");
	}

	/**
	 * 查询权限
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryPermissionList(String ctId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>());
		}
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && loginUserId.equals(tevglTchClassroom.getTraineeId());
		// 既不是助教、也不是课堂创建者的话
		if (!(isRoomCreator || isTeachingAssistant)) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("roleId", "1");
		map.put("ctId", ctId);
		return R.ok().put(Constant.R_DATA, tevglTchClassroomRoleprivilegeMapper.queryPermissionList(map));
	}
	
	/**
	 * 查看某课堂，助教的权限
	 *
	 * @param ctId
	 * @return
	 */
	@Override
	public List<String> queryPermissionListByCtId(String ctId) {
		return tevglTchClassroomRoleprivilegeMapper.queryPermissionListByCtId(ctId);
	}
}
