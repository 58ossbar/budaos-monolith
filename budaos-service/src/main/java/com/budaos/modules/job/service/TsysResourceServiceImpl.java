package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysOrgMapper;
import com.budaos.modules.job.persistence.TsysResourceMapper;
import com.budaos.modules.job.persistence.TsysUserinfoMapper;
import com.budaos.modules.sys.api.TsysResourceService;
import com.budaos.modules.sys.domain.TsysResource;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.constants.Constant.MenuType;
import com.budaos.utils.tool.BeanUtils;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源管理接口实现类
 * 
 * @author huangwb
 * @date 2019-05-06 15:18
 */
@Service
public class TsysResourceServiceImpl implements TsysResourceService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TsysResourceMapper tsysResourceMapper;
	@Autowired
	private TsysUserinfoMapper tsysUserinfoMapper;
	@Autowired
	private TsysOrgMapper tsysOrgMapper;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	
	/**
	 * 角色授权菜单
	 * @return
	 */
	@Override
	public List<TsysResource> perms() {
		String userId = serviceLoginUtil.getLoginUserId();
		// 查询列表数据
		List<TsysResource> menuList = new ArrayList<>();
		// 只有超级管理员，才能查看所有管理员列表
		if (Constant.SUPER_ADMIN.equalsIgnoreCase(userId)) {
			menuList = tsysResourceMapper.selectAllListByMap(new HashMap<String, Object>());
		} else {
			// 方法有问题
			menuList = tsysResourceMapper.selectUserList(userId);
		}
		return menuList;
	}
	
	/**
	 * 保存操作
	 * 
	 * @param tsysResource
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Transactional
	@Override
	@CacheEvict(value = "menu_list_cache", allEntries = true)
	public R saveOrUpdate(TsysResource tsysResource) {
		try {
			if (Strings.isNullOrEmpty(tsysResource.getMenuId())) {
				// 数据校验
				verifyForm(tsysResource);
				tsysResource
						.setOrderNum(tsysResourceMapper.selectMaxOrderNumByParentId(tsysResource.getParentId()) + 1);
				insert(tsysResource);
			} else {
				update(tsysResource);
			}
			if (!Strings.isNullOrEmpty(tsysResource.getOrgId())) {
				tsysResource.setOrgName(tsysOrgMapper.selectObjectById(tsysResource.getOrgId()).getOrgName());
			}
			return R.ok().put("data", tsysResource);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}

	/**
	 * 删除操作
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Transactional
	@Override
	@CacheEvict(value = "menu_list_cache", allEntries = true)
	public R delete(String menuId) {
		// 判断是否有子菜单或按钮
		List<TsysResource> menuList = tsysResourceMapper.selectListParentId(menuId);
		if (menuList != null && !menuList.isEmpty()) {
			return R.error("请先删除子菜单或按钮");
		}
		tsysResourceMapper.deleteBatchRes(new String[] { menuId });
		return R.ok();
	}

	/**
	 * 用户菜单列表
	 * 
	 * @param userId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	@Cacheable(value = "menu_list_cache", key = "'user_'+#userId+ '_' +#systemId")
	public R user(String userId, String systemId) {
		if (Strings.isNullOrEmpty(systemId)) {
			systemId = "19c786f2bfbf46398e3b495f6c7014b1";
		}
		List<TsysResource> menuList = getUserMenuList(userId, systemId);
		Set<String> permissions = getUserPermissions(userId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("menuList", menuList);
		dataMap.put("permissions", permissions);
		return R.ok().put("data", dataMap);
	}

	/**
	 * 根据父菜单，查询子菜单
	 * 
	 * @param parentId 父菜单ID
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	@Cacheable(value = "menu_list_cache", key = "'selectListParentId_' +#parentId")
	public R selectListParentId(String parentId) {
		return R.ok().put("data", tsysResourceMapper.selectListParentId(parentId));
	}

	/**
	 * 根据资源id查询资源
	 * 
	 * @param menuId
	 * @return R
	 * 
	 */
	@Override
	public R selectObjectById(String menuId) {
		return R.ok().put("data", tsysResourceMapper.selectObjectById(menuId));
	}

	/**
	 * 通过用户名获取用户菜单列表
	 * 
	 * @param userId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	@Cacheable(value = "menu_list_cache", key = "'getUserAllPermission_'+#username+'_' +#parentId")
	public R getUserAllPermission(String username, String systemId) {
		if (Strings.isNullOrEmpty(systemId)) {
			systemId = "19c786f2bfbf46398e3b495f6c7014b1";
		}
		TsysUserinfo user = tsysUserinfoMapper.selectObjectByUserName(username);
		if (user == null) {
			return R.error("用户不存在");
		}
		List<TsysResource> menuList = getUserMenuList(user.getUserId(), systemId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("menuList", menuList);
		dataMap.put("user", user);
		return R.ok().put("data", dataMap);
	}

	/**
	 * 获取用户的资源菜单
	 * 
	 * @param userId
	 * @param systemId
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	@Cacheable(value = "menu_list_cache", key = "'getUserMenuList_'+#userId+'_' +#systemId")
	public List<TsysResource> getUserMenuList(String userId, String systemId) {
		// 系统管理员，拥有最高权限
		if (userId.equalsIgnoreCase(Constant.SUPER_ADMIN)) {
			return getAllMenuList(null, systemId);
		}
		// 用户菜单列表
		List<String> menuIdList = tsysUserinfoMapper.selectAllMenuId(userId);
		return getAllMenuList(menuIdList, systemId);
	}

	/**
	 * 获取用户权限id
	 * 
	 * @param userId
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	@Cacheable(value = "menu_list_cache", key = "'getUserPermissions_'+#userId")
	public Set<String> getUserPermissions(String userId) {
		List<String> permsList;
		// 系统管理员，拥有最高权限
		if (userId.equalsIgnoreCase(Constant.SUPER_ADMIN)) {
			List<TsysResource> menuList = tsysResourceMapper.selectListByMap(new HashMap<>());
			permsList = new ArrayList<>(menuList.size());
			for (TsysResource menu : menuList) {
				permsList.add(menu.getPerms());
			}
		} else {
			permsList = tsysUserinfoMapper.selectAllPerms(userId);
		}
		// 用户权限列表
		Set<String> permsSet = new HashSet<>();
		for (String perms : permsList) {
			if (StringUtils.isBlank(perms)) {
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}
		return permsSet;
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	public R select() {
		// 查询列表数据
		List<TsysResource> menuList = tsysResourceMapper.selectNotButtonList();
		// 添加顶级菜单
		TsysResource root = new TsysResource();
		root.setMenuId("0");
		root.setName("一级菜单");
		root.setParentId("-1");
		root.setOpen(true);
		menuList.add(root);
		return R.ok().put("data", menuList);
	}

	/**
	 * 角色授权菜单
	 * 
	 * @param userId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	@CacheEvict(value = "menu_list_cache", allEntries = true)
	public List<TsysResource> perms(String userId) {
		// 查询列表数据
		List<TsysResource> menuList = null;
		// 只有超级管理员，才能查看所有管理员列表
		if (userId.equalsIgnoreCase(Constant.SUPER_ADMIN)) {
			menuList = tsysResourceMapper.selectAllListByMap(new HashMap<String, Object>());
		} else {
			// 方法有问题
			menuList = tsysResourceMapper.selectUserList(userId);
		}
		return menuList;
	}

	/**
	 * 一键生成增删改查按钮菜单
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Transactional
	@Override
	@CacheEvict(value = "menu_list_cache", allEntries = true)
	public R init(@PathVariable("menuId") String menuId) {
		try {
			TsysResource resource = tsysResourceMapper.selectObjectById(menuId);
			String url = resource.getUrl();
			if (url == null || url.trim().length() == 0) {
				return R.error("请先设置菜单url");
			}
			url = url.substring(1, url.length());
			String[] perms = url.split("/");
			if (perms.length < 2) {
				return R.error("url格式不正确");
			}
			String permsP = perms[0] + ":" + perms[1];
			if (resource.getPerms() == null || resource.getPerms().trim().length() == 0) {
				resource.setPerms(permsP + ":list," + permsP + ":query");
				update(resource);
			}
			List<TsysResource> menuList = tsysResourceMapper.selectListParentId(menuId);
			List<String> names = new ArrayList<String>();
			for (TsysResource res : menuList) {
				names.add(res.getName());
			}
			TsysResource temp = new TsysResource();
			temp.setParentId(menuId);
			temp.setType(2);
			temp.setDisplay("1");
			List<TsysResource> menuData = new ArrayList<TsysResource>();
			if (!names.contains("新增")) {
				temp.setName("新增");
				temp.setPerms(permsP + ":add");
				temp.setOrderNum(tsysResourceMapper.selectMaxOrderNumByParentId(temp.getParentId()) + 1);
				insert(temp);
				// 由于List中存放的是对象引用 所以需要调用BeanUtils对对象进行clone操作
				menuData.add((TsysResource) BeanUtils.cloneBean(temp));
			}
			if (!names.contains("修改")) {
				temp.setName("修改");
				temp.setPerms(permsP + ":edit");
				temp.setOrderNum(tsysResourceMapper.selectMaxOrderNumByParentId(temp.getParentId()) + 1);
				insert(temp);
				// 由于List中存放的是对象引用 所以需要调用BeanUtils对对象进行clone操作
				menuData.add((TsysResource) BeanUtils.cloneBean(temp));
			}
			if (!names.contains("删除")) {
				temp.setName("删除");
				temp.setPerms(permsP + ":remove");
				temp.setOrderNum(tsysResourceMapper.selectMaxOrderNumByParentId(temp.getParentId()) + 1);
				insert(temp);
				// 由于List中存放的是对象引用 所以需要调用BeanUtils对对象进行clone操作
				menuData.add((TsysResource) BeanUtils.cloneBean(temp));
			}
			if (!names.contains("查看")) {
				temp.setName("查看");
				temp.setPerms(permsP + ":view");
				temp.setOrderNum(tsysResourceMapper.selectMaxOrderNumByParentId(temp.getParentId()) + 1);
				insert(temp);
				// 由于List中存放的是对象引用 所以需要调用BeanUtils对对象进行clone操作
				menuData.add((TsysResource) BeanUtils.cloneBean(temp));
			}
			return R.ok().put("data", menuData);
		} catch (Exception e) {
			return R.error(e.getMessage());
		}
	}

	private void insert(TsysResource tsysResource) {
		tsysResource.setMenuId(Identities.uuid());
		tsysResource.setCreateTime(DateUtils.getNowTimeStamp());
		tsysResource.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tsysResourceMapper.insert(tsysResource);
	}

	private void update(TsysResource tsysResource) {
		tsysResource.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		tsysResource.setUpdateTime(DateUtils.getNowTimeStamp());
		tsysResourceMapper.update(tsysResource);
	}

	/**
	 * 根据获取菜单列表
	 * 
	 * @param menuId 判断需要查询的是子菜单还是父级菜单
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	public R query(String menuId) {
		List<TsysResource> menuList = null;
		if (menuId == null || StringUtils.isEmpty(menuId)) {
			menuList = tsysResourceMapper.selectSubSysList();
		} else {
			menuList = tsysResourceMapper.selectListParentId(menuId);
		}
		// 查询父名称
		menuList.stream().forEach(menu -> {
			if (menu.getParentId() != null && StringUtils.isNotEmpty(menu.getParentId())) {
				menu.setParentName(tsysResourceMapper.selectObjectById(menu.getParentId()).getName());
			}
			menu.setLeaf(tsysResourceMapper.selectCountParentId(menu.getMenuId()) > 0 ? false : true);
		});
		return R.ok().put("data", menuList);
	}

	/**
	 * 根据前端map参数获取菜单列表
	 * 
	 * @param map 判断需要查询的是子菜单还是父级菜单 type判断是否是展开树还是模糊查询的操作 name模糊查询参数
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@Override
	public R queryByMap( Map<String, Object> map) {
		List<TsysResource> menuList = null;
		// 如果为1代表只需要点击 如果type为2代表树形菜单模糊查询
		if (map.get("type") != null && map.get("type").equals("1")) {
			Map<String, Object> params = new HashMap<String, Object>();
			if (map.get("parentId") == null || StringUtils.isEmpty(map.get("parentId").toString())) {
				menuList = tsysResourceMapper.selectSubSysList();
			} else {
				menuList = tsysResourceMapper.selectListParentId(map.get("parentId").toString());
			}
			// 查询父名称
			menuList.stream().forEach(menu -> {
				if (menu.getParentId() != null && StringUtils.isNotEmpty(menu.getParentId())
						&& !menu.getParentId().equals("-1")) {
					menu.setParentName(tsysResourceMapper.selectObjectById(menu.getParentId()).getName());
				}
				menu.setLeaf(tsysResourceMapper.selectCountParentId(menu.getMenuId()) > 0 ? false : true);
			});
		} else {
			menuList = tsysResourceMapper.selectAllListByMap(map);
			// 递归存储资源菜单集合
			List<TsysResource> list = new ArrayList<TsysResource>();
			// 缓存操作 保存已经查询过的menu_id 防止重复查询同一个父菜单
			Map<String, Object> params = new HashMap<String, Object>();
			// 遍历查询的数据
			menuList.stream().forEach(menu -> {
				menu.setLeaf(tsysResourceMapper.selectCountParentId(menu.getMenuId()) > 0 ? false : true);
				recursionMenuTree(list, menu, params);
			});
			menuList.addAll(list);
			// 清空map方便GC回收
			params.clear();
		}
		for (TsysResource tsysResource : menuList) {
			if (tsysResource.getOrgId() != null && StringUtils.isNotBlank(tsysResource.getOrgId())) {
				tsysResource.setOrgName(tsysOrgMapper.selectObjectById(tsysResource.getOrgId()).getOrgName());
			}
		}
		return R.ok().put("data", menuList);
	}

	/**
	 * 根据前端map参数获取display显示的菜单列表
	 * 
	 * @param map 判断需要查询的是子菜单还是父级菜单 type判断是否是展开树还是模糊查询的操作 name模糊查询参数
	 * @return R
	 * @author huangwb
	 * @date 2019-06-1 15:18
	 */
	@Override
	public R queryByMapLinkState( Map<String, Object> map) {
		List<TsysResource> menuList = null;
		if (map.get("parentId") == null || StringUtils.isEmpty(map.get("parentId").toString())) {
			menuList = tsysResourceMapper.selectSubSysListByDisplay("1");
		} else {
			menuList = tsysResourceMapper.selectListParentIdAndDisplay(map.get("parentId").toString(), "1");
		}
		// 查询父名称
		menuList.stream().forEach(menu -> {
			if (menu.getParentId() != null && StringUtils.isNotEmpty(menu.getParentId())
					&& !menu.getParentId().equals("-1")) {
				menu.setParentName(tsysResourceMapper.selectObjectById(menu.getParentId()).getName());
			}
			menu.setLeaf(tsysResourceMapper.selectCountParentIdAndDisplay(menu.getMenuId(), "1") > 0 ? false : true);
		});
		return R.ok().put("data", menuList);
	}

	/**
	 * 递归资源菜单 查询出数据的所有父菜单
	 * 
	 * @param datas    递归存储资源菜单集合
	 * @param resource 递归判断因素
	 * @param params   缓存操作 保存已经查询过的menu_id 防止重复查询同一个父菜单
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	public void recursionMenuTree(List<TsysResource> datas, TsysResource resource, Map<String, Object> params) {
		// 判断数据是否有父节点
		if (!resource.getType().equals("-1") && resource.getParentId() != null
				&& StringUtils.isNotEmpty(resource.getParentId()) && !resource.getParentId().equals("1")) {
			// 判断缓存map中是否存在已经查询过的父菜单id
			if (params != null && !params.containsKey(resource.getParentId())) {
				TsysResource selectObjectById = tsysResourceMapper.selectObjectById(resource.getParentId());
				// 从下往上遍历 所以查询结点必然存在子结点
				selectObjectById.setLeaf(false);
				datas.add(selectObjectById);
				params.put(resource.getParentId(), selectObjectById);
				recursionMenuTree(datas, selectObjectById, params);
			}
		}
	}

	/**
	 * 查出所有子系统
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	public R selectSubSysList() {
		return R.ok().put("data", tsysResourceMapper.selectSubSysList());
	}

	/**
	 * 获取不包含按钮的菜单列表
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	@Override
	public R selectNotButtonList() {
		return R.ok().put("data", tsysResourceMapper.selectNotButtonList());
	}

	/**
	 * 移动资源菜单操作
	 * 
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @param moveType 移动的类型
	 * @return
	 * @author huangwb
	 * @date 2019-06-03 17:18
	 */
	@Override
	@CacheEvict(value = "menu_list_cache", allEntries = true)
	public R drag(String moveId, String targetId, String moveType) {
		TsysResource move = tsysResourceMapper.selectObjectById(moveId);
		TsysResource target = tsysResourceMapper.selectObjectById(targetId);
		// 如果是移动到内部
		if (moveType == "inner") {
			// 获取最大的排序号
			move.setOrderNum(tsysResourceMapper.selectMaxOrderNumByParentId(target.getMenuId()) + 1);
			move.setParentId(target.getMenuId());
		} else if (moveType.equals("before")) {
			// 获取目标元素的所有同辈元素 如果目标元素和移动元素同级则移除移动元素
			List<TsysResource> targetParentResources = tsysResourceMapper.selectListParentId(target.getParentId())
					.stream().filter(resource -> !resource.getMenuId().equals(move.getMenuId()))
					.collect(Collectors.toList());
			// 计算排序号
			int k = 0;
			// 遍历目标元素所有同辈元素
			for (int i = 0; i < targetParentResources.size(); i++) {
				TsysResource tsysResource = targetParentResources.get(i);
				// 排序号从1开始
				k++;
				// 由于是before 所以找到元素之后要在找到的元素将他的排序号再次+1 并将未+1的排序号给move对象
				// 留出一个空闲的排序号位置给move对象
				if (tsysResource.getOrgId().equals(target.getOrgId())) {
					move.setOrderNum(k);
					tsysResource.setOrderNum(k + 1);
				} else {
					// 否则k就是自然排序号
					tsysResource.setOrderNum(k);
				}
				if (tsysResource.getOrderNum() == k) {
					continue;
				}
				update(tsysResource);
			}
			// 讲move的父id设置成target目标对象的父id
			move.setParentId(target.getParentId());
		} else {
			// 获取目标元素的所有同辈元素 如果目标元素和移动元素同级则移除移动元素
			List<TsysResource> targetParentResources = tsysResourceMapper.selectListParentId(target.getParentId())
					.stream().filter(resource -> !resource.getMenuId().equals(move.getMenuId()))
					.collect(Collectors.toList());
			// 计算排序号
			int k = 0;
			// 遍历目标元素所有同辈元素
			for (int i = 0; i < targetParentResources.size(); i++) {
				TsysResource tsysResource = targetParentResources.get(i);
				// 排序号从1开始
				k++;
				// 由于我们是after操作 所以先自然排序
				tsysResource.setOrderNum(k);
				// 当找到目标元素的时候 此时k再+1 留出一个空闲的排序号位置给move对象
				if (tsysResource.getOrgId().equals(target.getOrgId())) {
					k++;
					move.setOrderNum(k);
				}
				if (tsysResource.getOrderNum() == k) {
					continue;
				}
				update(tsysResource);
			}
			move.setParentId(target.getParentId());
		}
		update(move);
		return R.ok().put("data", move);
	}

	/**
	 * 点击上下按钮移动资源管理节点操作
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @return
	 * @author huangwb
	 * @date 2019-06-17 14:18
	 */
	@Override
	@CacheEvict(value = "menu_list_cache", allEntries = true)
	public R clickDrag(String moveId, String targetId) {
		// 思路：交换两节点的OrgSN和OrgCode便可行
		TsysResource move = tsysResourceMapper.selectObjectById(moveId);
		TsysResource target = tsysResourceMapper.selectObjectById(targetId);
		Integer targetOrderNum = target.getOrderNum();
		target.setOrderNum(move.getOrderNum());
		target.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		target.setUpdateTime(DateUtils.getNow());
		move.setOrderNum(targetOrderNum);
		move.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		move.setUpdateTime(DateUtils.getNow());
		tsysResourceMapper.update(target);
		tsysResourceMapper.update(move);
		return R.ok();
	}

	/**
	 * 根据map查询资源菜单
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<TsysResource> selectListByMap(Map<String, Object> map) {
		return tsysResourceMapper.selectListByMap(map);
	}

	private List<TsysResource> selectListParentId(String parentId, List<String> menuIdList) {
		List<TsysResource> menuList = tsysResourceMapper.selectListParentId(parentId);
		if (menuIdList == null) {
			return menuList;
		}
		List<TsysResource> userMenuList = new ArrayList<>();
		for (TsysResource menu : menuList) {
			if (menuIdList.contains(menu.getMenuId())) {
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	/**
	 * 获取所有菜单列表
	 */
	public List<TsysResource> getAllMenuList(List<String> menuIdList, String systemId) {
		// 查询根菜单列表
		List<TsysResource> menuList = selectListParentId(systemId, menuIdList);
		// 递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		return menuList;
	}

	public List<TsysResource> selectSubSysListWithMenu(String userId, String systemId, String nsystemId) {
		List<String> menuIdList = null;
		if (!userId.equalsIgnoreCase(Constant.SUPER_ADMIN)) {
			menuIdList = tsysUserinfoMapper.selectAllMenuId(userId);
		}
		// 查询根菜单列表
		List<TsysResource> menuList = selectListParentIdForDocs(nsystemId, systemId, menuIdList);
		// 递归获取子菜单
		getMenuTreeListForDocs(menuList, menuIdList);
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<TsysResource> getMenuTreeList(List<TsysResource> menuList, List<String> menuIdList) {
		List<TsysResource> subMenuList = new ArrayList<TsysResource>();
		for (TsysResource entity : menuList) {
			if (entity.getType() == MenuType.CATALOG.getValue()) {// 目录
				entity.setList(getMenuTreeList(selectListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		return subMenuList;
	}

	/**
	 * 递归
	 */
	private List<TsysResource> getMenuTreeListForDocs(List<TsysResource> menuList, List<String> menuIdList) {
		List<TsysResource> subMenuList = new ArrayList<TsysResource>();

		for (int i = 0, j = menuList.size(); i < j; i++) {
			if (menuList.get(i).getType() == MenuType.CATALOG.getValue()) {// 目录
				menuList.addAll(getMenuTreeListForDocs(selectListParentIdForDocs(menuList.get(i).getParentName(),
						menuList.get(i).getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(menuList.get(i));
		}

		return subMenuList;
	}

	private List<TsysResource> selectListParentIdForDocs(String nparentId, String parentId, List<String> menuIdList) {
		List<TsysResource> menuList = tsysResourceMapper.selectListParentId(parentId);
		menuList.forEach(a -> {
			a.setRemark(a.getParentId());
			a.setParentId(nparentId);
			a.setParentName(Identities.uuid());
		});
		if (menuIdList == null) {
			return menuList;
		}

		List<TsysResource> userMenuList = new ArrayList<>();
		for (TsysResource menu : menuList) {
			if (menuIdList.contains(menu.getMenuId())) {
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(TsysResource menu) {
		if (StringUtils.isBlank(menu.getName())) {
			throw new BudaosException("菜单名称不能为空");
		}

		if (menu.getParentId() == null) {
			throw new BudaosException("上级菜单不能为空");
		}

		// 菜单
		if (menu.getType() == MenuType.MENU.getValue()) {
			if (StringUtils.isBlank(menu.getUrl())) {
				throw new BudaosException("菜单URL不能为空");
			}
		}

		// 上级菜单类型
		int parentType = MenuType.SYS.getValue();
		if (menu.getParentId().length() > 0 && !menu.getParentId().equals("-1")) {
			TsysResource parentMenu = tsysResourceMapper.selectObjectById(menu.getParentId());
			parentType = parentMenu.getType();
		}
		// 子系统
		if (menu.getType() == MenuType.SYS.getValue()) {
			if (menu.getParentId().length() > 0 && !menu.getParentId().equals("-1")) {
				throw new BudaosException("子系统不能选择上级菜单");
			}
			return;
		}
		// 目录
		if (menu.getType() == MenuType.CATALOG.getValue()) {
			if (parentType != MenuType.CATALOG.getValue() && parentType != MenuType.SYS.getValue()) {
				throw new BudaosException("上级菜单只能为目录类型或者子系统类型");
			}
			return;
		}
		// 菜单
		if (menu.getType() == MenuType.MENU.getValue()) {
			if (parentType != MenuType.CATALOG.getValue()) {
				throw new BudaosException("上级菜单只能为目录类型");
			}
			return;
		}
		// 按钮
		if (menu.getType() == MenuType.BUTTON.getValue()) {
			if (parentType != MenuType.MENU.getValue()) {
				throw new BudaosException("上级菜单只能为菜单类型");
			}
			return;
		}
	}

}
