package com.budaos.modules.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.sys.api.TsysOrgService;
import com.budaos.modules.sys.api.TsysResourceService;
import com.budaos.modules.sys.api.TsysRoleService;
import com.budaos.modules.sys.domain.TsysResource;
import com.budaos.modules.sys.domain.TsysRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>角色管理（系统角色）控制类</p>
 * <p>Title: TsysRoleController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月13日
 */
@RestController
@RequestMapping("/api/sys/role")
public class TsysRoleController {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TsysRoleService tsysRoleService;
	@Autowired
	private TsysOrgService tsysOrgService;
	@Autowired
	private TsysResourceService tsysResourceService;
	@Autowired
	private LoginUtils loginUtils;
	
	
	/**
	 * <p>根据条件查询记录(有分页)</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param data
	 * @param request
	 * @return
	 */
	@GetMapping("/findPage")
	@PreAuthorize("hasAuthority('sys:role:query')")
	@SysLog("根据条件查询角色信息")
	public R query(@RequestParam Map<String, Object> params) {
		return tsysRoleService.query(params);
	}
	
	/**
	 * <p>获取全部角色列表(没有分页)</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryall")
	@PreAuthorize("hasAuthority('sys:role:query')")
	@SysLog("查询所有角色信息")
	public R queryAll(@RequestParam Map<String, Object> params){
		params.put("status", "1"); // 0 禁用 1启用
		List<TsysRole> list = tsysRoleService.queryAll(params);
		return R.ok().put("data", list);
	}
	
	/**
	 * <p>进入新增角色页面</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param data
	 * @param request
	 * @return
	 */
	@RequestMapping("/add")
	@PreAuthorize("hasAuthority('sys:role:add')")
	@SysLog("进入新增角色页面或弹窗")
	public R add(@RequestBody(required = false) JSONObject data, HttpServletRequest request) {
		return tsysRoleService.add();
	}
	
	/**
	 * <p>进入修改角色页面</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param data
	 * @param request
	 * @return
	 */
	@RequestMapping("/edit")
	@PreAuthorize("hasAuthority('sys:role:edit')")
	@SysLog("进入修改角色页面或弹窗")
	public R edit(@RequestBody(required = false) JSONObject data, HttpServletRequest request) {
		String roleId = data.getString("roleId");
		return tsysRoleService.edit(roleId);
	}

	/**
	 * <p>执行新增或修改</p>
	 * <p>角色表t_sys_role, 角色与菜单关系t_sys_roleprivilege, 角色与数据权限关系t_sys_dataprivilege</p>
	 * @author huj
	 * @data 2019年5月24日
	 * @param role
	 * @return
	 */
	@PostMapping("/save")
	@SysLog("执行角色相关数据新增和修改保存")
	@PreAuthorize("hasAuthority('sys:role:add') or hasAuthority('sys:role:edit')")
	public R save(@RequestBody(required = false) TsysRole role) {
		if (role.getRoleId() == null || "".equals(role.getRoleId())) { // 新增
			return tsysRoleService.save(role);
		} else {
			return tsysRoleService.update(role);
		}
	}

	/**
	 * <p>删除</p>
	 * <p>角色表t_sys_role, 用户与角色关系t_user_role, 角色与菜单关系t_sys_roleprivilege, 角色与数据权限关系t_sys_dataprivilege</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@PreAuthorize("hasAuthority('sys:role:remove')")
	@SysLog("删除角色")
	public R delete(@RequestBody(required = false) String[] ids) {
		return tsysRoleService.deleteBatch(ids);
	}
	
	/**
	 * <p>分配用户(可参考https://edu.budaos.com/eao/index中用户管理中分配用户按钮)</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param data
	 * @return
	 */
	@PostMapping("/setUser")
	@PreAuthorize("hasAuthority('sys:role:setUser')")
	@SysLog("分配用户")
	public R setUser(@RequestBody(required = false) String[] roleIds) {
		return tsysRoleService.setUser(roleIds);
	}
	
	/**
	 * <p>保存用户角色t_user_role(参考https://edu.budaos.com/eao/index中用户管理中点分配用户按钮-弹窗中的保存按钮)</p>
	 * @author huj
	 * @data 2019年5月16日
	 * @param data
	 * @param role
	 * @return
	 */
	@RequestMapping("/saveRoleUser")
	@PreAuthorize("hasAuthority('sys:role:save')")
	@SysLog("分配用户-保存用户与角色关系")
	public R saveRoleUser(@RequestBody(required = false) TsysRole role) {
		return tsysRoleService.saveRoleUser(role);
	}
	
	/**
	 * <p>机构树</p>
	 * @author huj
	 * @data 2019年5月14日
	 * @param data
	 * @param request
	 * @return
	 */
	@RequestMapping({"/orgTree", "/orgtree"})
	@SysLog("获取机构树")
	public R orgTree(@RequestBody(required = false) JSONObject data, HttpServletRequest request) {
		return tsysOrgService.query2(new HashMap<String, Object>());
	}
	
	/**
	 * <p>角色授权菜单</p>
	 * @author huj
	 * @data 2019年5月14日
	 * @param data
	 * @return
	 */
	@RequestMapping("/perms")
	@SysLog("查询角色授权菜单")
	public R perms(@RequestBody(required = false) JSONObject data) {
		// tsysResourceService.perms(null); 此处有问题。
		List<TsysResource> list = tsysResourceService.perms(loginUtils.getLoginUserId());
		return R.ok().put("data", list);
	}
	
	/**
	 * <p>更新角色状态</p>
	 * @author huj
	 * @data 2019年6月12日
	 * @param role
	 * @return
	 */
	@PostMapping("/updateStatus")
	@SysLog("更新角色状态")
	public R updateStatus(@RequestBody TsysRole role) {
		return tsysRoleService.updateStatus(role);
	}
	
    /**
     * 查看
     * @author huj
     * @data 2019年5月29日
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    @PreAuthorize("hasAuthority('sys:role:view')")
    public R view(@PathVariable("id") String id) {
        return tsysRoleService.view(id);
    }

}
