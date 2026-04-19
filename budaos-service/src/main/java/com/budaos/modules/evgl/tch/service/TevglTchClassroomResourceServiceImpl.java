package com.budaos.modules.evgl.tch.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.common.RecursionUtils;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomResourceService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomResource;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomRoleprivilege;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomResourceMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TevglTchClassroomResourceServiceImpl implements TevglTchClassroomResourceService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomResourceServiceImpl.class);
	@Autowired
	private TevglTchClassroomResourceMapper tevglTchClassroomResourceMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private RecursionUtils recursionUtils;
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
		List<TevglTchClassroomResource> tevglTchClassroomResourceList = tevglTchClassroomResourceMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomResourceList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglTchClassroomResourceList = tevglTchClassroomResourceMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomResourceList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchClassroomResource
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglTchClassroomResource tevglTchClassroomResource) throws BudaosException {
		tevglTchClassroomResource.setMenuId(Identities.uuid());
		ValidatorUtils.check(tevglTchClassroomResource);
		tevglTchClassroomResourceMapper.insert(tevglTchClassroomResource);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClassroomResource
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTchClassroomResource tevglTchClassroomResource) throws BudaosException {
	    ValidatorUtils.check(tevglTchClassroomResource);
		tevglTchClassroomResourceMapper.update(tevglTchClassroomResource);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglTchClassroomResourceMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglTchClassroomResourceMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomResourceMapper.selectObjectById(id));
	}

	/**
	 * 获取树形数据
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R getTreeData(String ctId, String loginUserId) {
		if (StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.ok().put(Constant.R_DATA, new HashMap<>());
		}
		boolean hasAuth = roomUtils.hasOperatingAuthorization(tevglTchClassroom, loginUserId);
		if (!hasAuth) {
			return R.ok().put(Constant.R_DATA, new HashMap<>());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> allList = tevglTchClassroomResourceMapper.selectSimpleListByMap(map);
		//List<String> list = Arrays.asList("2a3", "2a4", "2a5");
		allList.stream().forEach(a -> {
			/*if (list.contains(a.get("menuId"))) {
				a.put("disabled", true);
			}*/
		});
		List<Map<String, Object>> treeData = recursionUtils.getTreeData("classroom", allList, "menuId", "parentId");
		map.put("ctId", ctId);
		map.put("roleId", "1");
		List<TevglTchClassroomRoleprivilege> roleprivilegeList = tevglTchClassroomRoleprivilegeMapper.selectListByMap(map);
		// 返回数据
		map.put("treeData", treeData);
		map.put("selectedList", roleprivilegeList.stream().map(a -> a.getMenuId()).distinct().collect(Collectors.toList()));
		List<Object> defaultExpandedKeys = allList.stream().filter(a -> a.get("parentId").equals(GlobalRoomPermission.MENU_ID)).map(a -> a.get("menuId")).collect(Collectors.toList());
		map.put("defaultExpandedKeys", defaultExpandedKeys);
		return R.ok().put(Constant.R_DATA, map);
	}
}
