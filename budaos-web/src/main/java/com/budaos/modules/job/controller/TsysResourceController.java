package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.sys.api.TsysResourceService;
import com.budaos.modules.sys.domain.TsysResource;
import com.budaos.utils.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 
 * 资源管理
 * 
 * @author huangwb
 * @date 2019-05-013 15:18
 */
@RestController
@RequestMapping("/api/sys/resource")
public class TsysResourceController {
	@Autowired
	private TsysResourceService tsysResourceService;
	@Autowired
	private LoginUtils loginUtils;

    /**
     * 角色授权菜单
     * @author huj
     * @data 2019年5月14日
     * @return
     */
    @RequestMapping("/perms")
    @SysLog("查询角色授权菜单")
    public R perms() {
        List<TsysResource> list = tsysResourceService.perms();
        return R.ok().put(Constant.R_DATA, list);
    }
	
	/**
	 * 保存修改操作
	 * 
	 * @param tsysResource
	 * @return R
	 * @author huangwb
	 * @date 2019-05-13 15:18
	 */
	@PostMapping("/saveOrUpdate")
	@PreAuthorize("hasAuthority('sys:menu:add') and hasAuthority('sys:menu:edit')")
	@SysLog("资源菜单保存或修改")
	public R saveOrupdate(@RequestBody(required = false) TsysResource tsysResource) {
		if (tsysResource.getMenuId() == null || StringUtils.isBlank(tsysResource.getMenuId())) {
			tsysResource.setCreateUserId(loginUtils.getLoginUserId());
		} else {
			tsysResource.setUpdateUserId(loginUtils.getLoginUserId());
		}
		return tsysResourceService.saveOrUpdate(tsysResource);
	}

	/**
	 * 删除操作
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-13 15:18
	 */
	@DeleteMapping("/delete/{menuId}")
	@PreAuthorize("hasAuthority('sys:menu:remove')")
	@SysLog("删除指定资源菜单")
	public R delete(@PathVariable("menuId") String menuId) {
		if (menuId == null || StringUtils.isBlank(menuId)) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysResourceService.delete(menuId);
	}

	/**
	 * 一键生成增删改查按钮菜单
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-13 15:18
	 */
	@PostMapping("/init/{menuId}")
	@PreAuthorize("hasAuthority('sys:menu:init')")
	@SysLog("资源菜单一键生成增删改查按钮")
	public R init(@PathVariable("menuId") String menuId) {
		if (menuId == null || StringUtils.isBlank(menuId)) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysResourceService.init(menuId);
	}

	/**
	 * 获取菜单列表
	 * 
	 * @param map parentId父资源菜单id type=1则是点击树形结构操作否则是模糊查询 name为模糊查询的条件
	 * @return R
	 * @author huangwb
	 * @date 2019-05-13 15:18
	 */
	@GetMapping(value = "/query")
	@PreAuthorize("hasAuthority('sys:menu:list')")
	@SysLog("获取资源菜单列表")
	public R query(@RequestParam(required = false) Map<String, Object> map) {
		return tsysResourceService.queryByMap(map);
	}

	/**
	 * 获取显示的菜单列表
	 * 
	 * @param map parentId父资源菜单id
	 * @return R
	 * @author huangwb
	 * @date 2019-05-13 15:18
	 */
	@GetMapping(value = "/queryLinkDisplay")
	@PreAuthorize("hasAuthority('sys:menu:list')")
	@SysLog("获取资源菜单列表")
	public R queryLinkDisplay(@RequestParam(required = false) Map<String, Object> map) {
		return tsysResourceService.queryByMapLinkState(map);
	}

	/**
	 * 根据menuid查询资源数据 可用于复制菜单查询操作
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-13 15:18
	 */
	@GetMapping("/get/{menuId}")
	@PreAuthorize("hasAuthority('sys:menu:info')")
	@SysLog("获取指定资源菜单信息")
	public R get(@PathVariable("menuId") String menuId) {
		if (menuId == null || StringUtils.isBlank(menuId)) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysResourceService.selectObjectById(menuId);
	}

	/**
	 * 移动资源菜单操作
	 * 
	 * 
	 * @param map moveId移动对象的id targetId目标对象的id moveType移动的类型
	 * @author huangwb
	 * @date 2019-06-03 17:18
	 * @return
	 */
	@PostMapping("/drag")
	@PreAuthorize("hasAuthority('sys:menu:move')")
	@SysLog("移动资源菜单")
	public R drag(@RequestBody(required = true) Map<String, Object> map) {
		if (map.get("moveId") == null || StringUtils.isEmpty(map.get("moveId").toString())) {
			return R.error("移动节点不能为空");
		} else if (map.get("targetId") == null || StringUtils.isEmpty(map.get("targetId").toString())) {
			return R.error("目标节点不能为空");
		} else if (map.get("moveType") == null || StringUtils.isEmpty(map.get("moveType").toString())) {
			return R.error("移动类型不能为空");
		}
		return tsysResourceService.drag(map.get("moveId").toString(), map.get("targetId").toString(),
				map.get("moveType").toString());
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
	@PostMapping("/clickDrag")
	@PreAuthorize("hasAuthority('sys:menu:move')")
	@SysLog("点击按钮移动资源管理")
	public R clickDrag(@RequestBody(required = true) Map<String, Object> map) {
		if (map.get("moveId") == null || StringUtils.isEmpty(map.get("moveId").toString())) {
			return R.error("移动节点不能为空");
		} else if (map.get("targetId") == null || StringUtils.isEmpty(map.get("targetId").toString())) {
			return R.error("目标节点不能为空");
		}
		return tsysResourceService.clickDrag(map.get("moveId").toString(), map.get("targetId").toString());
	}

	@PostMapping("/subSystem")
	@PreAuthorize("hasAuthority('sys:menu:subsystem')")
	@SysLog("获取子系统菜单")
	public R subSystem() {
		return tsysResourceService.selectSubSysList();
	}
}
