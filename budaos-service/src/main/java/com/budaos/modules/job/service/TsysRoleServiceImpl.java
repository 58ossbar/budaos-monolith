package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.dataprivilege.annotation.DataFilter;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.constants.ExecStatus;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysDataprivilegeMapper;
import com.budaos.modules.job.persistence.TsysRoleMapper;
import com.budaos.modules.job.persistence.TsysRoleprivilegeMapper;
import com.budaos.modules.job.persistence.TuserRoleMapper;
import com.budaos.modules.sys.api.*;
import com.budaos.modules.sys.domain.TsysRole;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理api的实现类
 * <p>Title: TsysRoleServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖布道师学习通</p> 
 * @author huj
 * @param <R>
 * @date 2019年5月5日
 */
@Service
public class TsysRoleServiceImpl implements TsysRoleService {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TsysRoleMapper tsysRoleMapper;
	@Lazy
	@Autowired
	private TsysUserinfoService tsysUserinfoService;
	@Autowired
	private TsysRoleprivilegeService tsysRoleprivilegeService;
	@Autowired
	private TuserRoleService tuserRoleService;
	@Autowired
	private TsysDataprivilegeService tsysDataprivilegeService;
	@Autowired
	private TuserRoleMapper tuserRoleMapper;
	@Autowired
	private TsysRoleprivilegeMapper tsysRoleprivilegeMapper;
	@Autowired
	private TsysDataprivilegeMapper tsysDataprivilegeMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;

	/**
	 * <p>【角色列表】根据条件查询记录(不带分页效果)</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param map
	 * @return
	 */
	@Override
	@DataFilter(tableAlias = "o", customDataAuth = "", selfUser = false)
	public List<TsysRole> queryAll( Map<String, Object> map) {
		List<TsysRole> tsysRoleList = tsysRoleMapper.selectListByMap(map);
		return tsysRoleList;
	}
	
	/**
	 * <p>【角色列表】根据条件查询记录(带分页效果)</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param map
	 * @return
	 */
	@Override
	@DataFilter(tableAlias = "o", customDataAuth = "", selfUser = false)
	public R query( Map<String, Object> map) {
		TsysUserinfo userInfo = serviceLoginUtil.getLoginUser(); // 当前登陆用户
		if (userInfo == null) {
			return R.error("无法获取当前登陆信息，请尝试重新登陆");
		}
		// 查询列表数据
		Query query = new Query(map);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TsysRole> list = tsysRoleMapper.selectListByMap(map);
		convertUtil.convertOrgId(list, "orgId");
		if (list != null && list.size() > 0) {
			for (TsysRole role : list) {
				role.setId(role.getRoleId());
			}
		}
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put("data", pageUtil);
	}

	/**
     * 查看明细
     *
     * @param roleId
     * @return
     */
	@Override
	public R view(String roleId) {
		TsysRole tsysRole = tsysRoleMapper.selectObjectById(roleId);
        if (tsysRole == null) {
            return R.error(ExecStatus.INVALID_PARAM.getMsg());
        }
        // 根据角色ID,获取菜单ID列表
        tsysRole.setMenuIdList(tsysRoleprivilegeMapper.selectMenuListByRoleId(roleId));
        // 根据角色ID,获取机构ID列表
        tsysRole.setOrgIdList(tsysDataprivilegeMapper.selectOrgListByRoleId(roleId));
        return R.ok().put(Constant.R_DATA, tsysRole);
	}
	
	/**
	 * <p>注:原ModelAndView的进入修改角色页面</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @return
	 */
	@Override
	public R add() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DATA_SCOPE", Constant.DATA_SCOPE.values());
		TsysRole tsysRole = new TsysRole();
		tsysRole.setRoleType("2");
		tsysRole.setStatus("2");		
		map.put("tsysRole", tsysRole);
		return R.ok().put("data", map);
	}
	
	/**
	 * <p>修改,注:原ModelAndView的进入修改角色页面</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	@Override
	public R edit(String roleId) {
		if (roleId == null || "".equals(roleId) || "null".equals(roleId)) {
			return R.error("无法获取roleId");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("DATA_SCOPE", Constant.DATA_SCOPE.values());
		//String roleId = request.getParameter("roleId");
		TsysRole tsysRole = (TsysRole) tsysRoleMapper.selectObjectById(roleId); // 获取角色
		if (tsysRole == null) {
			return R.error("无法获取被修改的角色信息");
		}
		tsysRole.setMenuIdList(tsysRoleprivilegeMapper.selectMenuListByRoleId(roleId)); // 根据角色ID,获取菜单ID列表
		tsysRole.setOrgIdList(tsysDataprivilegeMapper.selectOrgListByRoleId(roleId)); // 根据角色ID,获取机构ID列表
		map.put("tsysRole", tsysRole);
		// 转换机构ID为机构名称
		TsysRole t = new TsysRole();
		t.setRoleId(tsysRole.getRoleId());
		t.setOrgId(tsysRole.getOrgId());
		convertUtil.convertOrgId(t, "orgId");
		map.put("orgName", t.getOrgId());
		return R.ok().put("data", map);
	}

	/**
	 * <p>分配角色</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @return
	 */
	@CacheEvict(value = {"menu_list_cache", "authorization_cache"}, allEntries = true)
	public R setUser(@RequestBody String[] roleIds) {
		if (roleIds == null || roleIds.length <= 0) {
			return R.error("无法获取roleIds");
		}
		List<TsysRole> list = new ArrayList<TsysRole>();
		for(String id : roleIds) {
			TsysRole tsysRole = tsysRoleMapper.selectObjectById(id);
			if (tsysRole == null) {
				return R.error("无法获取被选中角色的信息");
			}
			tsysRole.setUserIdList(tuserRoleMapper.selectUserListByRoleId(id));
			list.add(tsysRole);
		}
		if (roleIds.length >= 2) {
			for(int i=0; i<list.size(); i++) {
				if (i>0) {
					list.get(i).getUserIdList().retainAll(list.get(i-1).getUserIdList());
				}
			}
		}
		return R.ok().put("data", list).put("intersection", list.get(list.size()-1).getUserIdList());
	}
	
	/**
	 * <p>角色列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	@Override
	public R select(TsysUserinfo userInfo) {
		Map<String, Object> map = new HashMap<>();
		//如果不是超级管理员，则只查询自己所拥有的角色列表
		if(userInfo != null && !userInfo.getUserId().equalsIgnoreCase(Constant.SUPER_ADMIN)){
			map.put("createUserId", userInfo.getUserId());
		}
		List<TsysRole> list = tsysRoleMapper.selectListByMap(map);
		return R.ok().put("data", list);
	}
	

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月24日
	 * @return
	 */
	@Override

	@CacheEvict(value = "authorization_cache", allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public R save(@RequestBody(required = false) TsysRole tsysRole) {
		// 角色名称唯一性校验
        TsysRole existedRole = tsysRoleMapper.checkRoleNameUnique(tsysRole.getRoleName());
        if (existedRole != null) {
            return R.error(tsysRole.getRoleName() + " 名称已存在，请更改");
        }
        // 检查权限是否越权
        tsysRole.setCreateUserId(serviceLoginUtil.getLoginUserId());
        R r = checkPrems(tsysRole, serviceLoginUtil.getLoginUserId());
        if (!r.get("code").equals(0)) {
            return r;
        }
        // 填充信息
        tsysRole.setRoleId(Identities.uuid());
        tsysRole.setCreateTime(DateUtils.getNowTimeStamp());
        tsysRole.setUpdateTime(tsysRole.getCreateTime());
        tsysRole.setStatus(StrUtils.isEmpty(tsysRole.getStatus()) ? "1" : tsysRole.getStatus());
        // 角色状态：1启用、0禁用
        tsysRole.setStatus("1");
        // 入库
        tsysRoleMapper.insert(tsysRole);
        // 保存角色与菜单关系
        tsysRoleprivilegeService.saveOrUpdate(tsysRole.getRoleId(), tsysRole.getMenuIdList());
        // 保存角色与数据权限关系
        tsysDataprivilegeService.saveOrUpdate(tsysRole.getRoleId(), tsysRole.getOrgIdList());
        return R.ok("角色新增成功");
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月24日
	 * @param role
	 * @return
	 */
	@Override
	@CacheEvict(value = {"menu_list_cache", "authorization_cache"}, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public R update(@RequestBody TsysRole role) {
        // 角色名称唯一性校验
        TsysRole existedRole = tsysRoleMapper.checkRoleNameUnique(role.getRoleName());
        if (existedRole != null && !existedRole.getRoleId().equals(role.getRoleId())) {
            return R.error(role.getRoleName() + " 名称已存在，请更改");
        }
        // 检查权限是否越权
        String userId = serviceLoginUtil.getLoginUserId();
        R r = checkPrems(role, userId);
        if (!r.get("code").equals(0)) {
            return r;
        }
        role.setStatus(StrUtils.isEmpty(role.getStatus()) ? "1" : role.getStatus());
        role.setUpdateUserId(userId);
        role.setUpdateTime(DateUtils.getNowTimeStamp());
        tsysRoleMapper.update(role);
        // 保存角色与菜单关系
        tsysRoleprivilegeService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
        // 保存角色与数据权限关系
        tsysDataprivilegeService.saveOrUpdate(role.getRoleId(), role.getOrgIdList());
        return R.ok("角色修改成功");
	}

	
	/**
	 * <p>保存</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param role
	 * @return
	 */
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R saveOrUpdate(TsysRole role) {
		try {
			//ValidatorUtils.validateEntity(role); // 验证
			role.setStatus("1"); // 设置状态
			if(!"9".equals(role.getDataScope())){
				//role.setOrgIdList(null);
			}
			if(role.getRoleId() == null || "".equals(role.getRoleId())){ // 新增
				R r = checkPrems(role, role.getCreateUserId()); // 检查权限是否越权
				if (!r.get("code").equals(0)) {
					return r;
				}
				role.setRoleId(Identities.uuid()); // 设置角色ID(主键ID)
				role.setCreateTime(DateUtils.getNowTimeStamp()); // 设置创建时间
				tsysRoleMapper.insert(role); // 调用方法保存
				// 保存角色与菜单关系
				R r1 = tsysRoleprivilegeService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
				if (!r1.get("code").equals(0)) {
					return r1;
				}
				// 保存角色与数据权限关系
				R r2 = tsysDataprivilegeService.saveOrUpdate(role.getRoleId(), role.getOrgIdList());
				if (!r2.get("code").equals(0)) {
					return r2;
				}
			} else {
				R r = checkPrems(role, role.getUpdateUserId()); // 检查权限是否越权
				if (!r.get("code").equals(0)) {
					return r;
				}
				role.setUpdateTime(DateUtils.getNowTimeStamp()); // 修改时间
				tsysRoleMapper.update(role); // 调用方法保存
				// 更新角色与菜单关系
				R r1 = tsysRoleprivilegeService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
				if (!r1.get("code").equals(0)) {
					return r1;
				}
				//  保存角色与数据权限关系
				R r2 = tsysDataprivilegeService.saveOrUpdate(role.getRoleId(), role.getOrgIdList());
				if (!r2.get("code").equals(0)) {
					return r2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("保存失败");
		}
		return R.ok("保存成功");
	}

	/**
	 * <p>分配用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param role
	 * @return
	 */
	@Override
	@CacheEvict(value = {"menu_list_cache", "authorization_cache"}, allEntries = true)
	public R saveRoleUser(@RequestBody(required = false) TsysRole role) {
		try {
			/* 原先版
			R r = tuserRoleService.saveOrUpdateForRole(role.getRoleId(), role.getUserIdList());
			if (!r.get("code").equals(0)) {
				return r;
			}
			*/
			if (role.getRoleIdList() != null && role.getRoleIdList().size() > 0) {
				for (String s : role.getRoleIdList()) {
					tuserRoleService.saveOrUpdateForRole(s, role.getUserIdList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("分配用户失败");
		}
		return R.ok("分配用户成功");
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param id
	 * @return
	 */
	@Override
	@SysLog("删除角色信息")
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R delete(String id) {
		return R.ok().put("data", "此方法为具体实现");
	}
	
	/**
	 * <p>批量删除角色信息</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param roleIds
	 * @return
	 */
	@Override
	@Transactional
	@SysLog("删除角色信息")
	@CacheEvict(value = {"menu_list_cache", "authorization_cache"}, allEntries = true)
	public R deleteBatch(String[] roleIds) {
		if (roleIds.length == 0 || roleIds == null) {
			return R.error("无法获取被删除的roleIds");
		}
		tsysRoleMapper.deleteBatchUserRole(roleIds);
		tsysRoleMapper.deleteBatchRolePrivilege(roleIds);
		tsysRoleMapper.deleteBatchRoleDataPrivilege(roleIds);
		tsysRoleMapper.deleteBatchRole(roleIds);
		return R.ok("角色删除成功");
	}
	
	@Override
	public List<String> selectRoleListByCreatorUserId(String createUserId) {
		return tsysRoleMapper.selectRoleListByCreatorUserId(createUserId);
	}
	
	@Override
	public TsysRole selectObjectByRoleId(String roleId) {
		return tsysRoleMapper.selectObjectById(roleId);
	}
	
	/**
	 * <p>检查权限是否越权</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param role 角色
	 * @param userId 用户ID
	 */
	private R checkPrems(TsysRole role,String userId){
		// 如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if(userId.equalsIgnoreCase(Constant.SUPER_ADMIN)){
			return R.ok();
		}
		// 查询用户所拥有的菜单列表
		List<String> menuIdList = tsysUserinfoService.selectAllMenuId(role.getCreateUserId());
		// 判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			//throw new BudaosException("新增角色的权限，已超出你的权限范围");
			return R.error("新增角色的权限，已超出你的权限范围");
		}
		return R.ok();
	}

	/**
	 * <p>更新角色状态</p>
	 * @author huj
	 * @data 2019年6月12日
	 * @param role
	 * @return
	 */
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R updateStatus(@RequestBody TsysRole role) {
		try {
			if (role.getRoleId() == null || "".equals(role.getRoleId())) {
				return R.error("roleId为空");
			}
			tsysRoleMapper.updateStatus(role);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改失败");
		}
		return R.ok("修改成功");
	}


}
