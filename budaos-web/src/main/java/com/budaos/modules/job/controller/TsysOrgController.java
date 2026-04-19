package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.sys.api.TsysOrgService;
import com.budaos.modules.sys.domain.TsysOrg;
import com.budaos.modules.sys.domain.TsysUserinfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 机构管理
 * 
 * @author huangwb
 * @date 2019-05-013 15:18
 */
@RestController
@RequestMapping("/api/sys/org")
public class TsysOrgController {
	
	@Autowired
	private TsysOrgService tsysOrgService;
	@Autowired
	private LoginUtils loginUtils;

    /**
     * 获取机构树形数据
     * @data 2019年5月14日
     * @return
     */
    @RequestMapping({"/getOrgTree"})
    @SysLog("获取机构树")
    public R getOrgTree() {
        return tsysOrgService.getOrgTree(new HashMap<String, Object>());
    }

	
	/**
	 * 保存或者修改操作
	 * 
	 * @param tsysOrg
	 * @return R
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@PostMapping("saveOrUpdate")
	@PreAuthorize("hasAuthority('sys:tsysorg:add') and hasAuthority('sys:tsysorg:edit')")
	@SysLog("机构保存或修改")
	public R saveOrUpdate(@RequestBody(required = false) TsysOrg tsysOrg) {
		if (tsysOrg.getOrgId() == null || StringUtils.isEmpty(tsysOrg.getOrgId())) {
			tsysOrg.setCreateUserId(loginUtils.getLoginUserId());
			return tsysOrgService.save(tsysOrg);
		} else {
			tsysOrg.setUpdateUserId(loginUtils.getLoginUserId());
			return tsysOrgService.update(tsysOrg);
		}
	}

	/**
	 * 删除操作
	 * 
	 * @param ids
	 * @return
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@DeleteMapping("delete")
	@PreAuthorize("hasAuthority('sys:tsysorg:remove')")
	@SysLog("删除机构")
	public R deleteBatch(@RequestBody(required = false) String[] ids) {
		if (ids == null) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysOrgService.deleteBatch(ids);
	}

	/**
	 * 获取指定机构信息
	 * 
	 * @param orgId 指定机构id
	 * @return
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@GetMapping("/get/{orgId}")
	@PreAuthorize("hasAuthority('sys:tsysorg:view')")
	@SysLog("获取指定机构信息")
	public R get(@PathVariable("orgId") String orgId) {
		if (orgId == null || StringUtils.isBlank(orgId)) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysOrgService.selectListById(orgId);
	}

	/**
	 * 查询机构列表
	 * 
	 * @param parentId 父机构节点ID orgName机构名称模糊查询条件 type=1代表树形菜单展开否则模糊查询情况
	 * @return
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@GetMapping(value = "/query")
	@PreAuthorize("hasAuthority('sys:tsysorg:query')")
	@SysLog("查询机构列表")
	public R query(@RequestParam(required = false) Map<String, Object> map) {
		TsysUserinfo loginUser = loginUtils.getLoginUser();
		return tsysOrgService.queryByMap(map, loginUser.getUserId());
	}

	/**
	 * 查询机构列表
	 * 
	 * @return
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@GetMapping(value = "/queryLinkState")
	@PreAuthorize("hasAuthority('sys:tsysorg:query')")
	@SysLog("查询机构列表")
	public R queryLinkState() {
		TsysUserinfo loginUser = loginUtils.getLoginUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "1");
		return tsysOrgService.queryByMap(map, loginUser.getUserId());
	}

	/**
	 * 拖拽移动机构管理操作
	 * 
	 * 
	 * @param map moveId移动对象的id targetId目标对象的id moveType移动的类型
	 * @author huangwb
	 * @date 2019-06-03 17:18
	 * @return
	 */
	@PostMapping("/drag")
	@SysLog("拖拽移动机构管理")
	@PreAuthorize("hasAuthority('sys:tsysorg:move')")
	public R drag(@RequestBody(required = true) Map<String, Object> map) {
		if (map.get("moveId") == null || StringUtils.isEmpty(map.get("moveId").toString())) {
			return R.error("移动节点不能为空");
		} else if (map.get("targetId") == null || StringUtils.isEmpty(map.get("targetId").toString())) {
			return R.error("目标节点不能为空");
		} else if (map.get("moveType") == null || StringUtils.isEmpty(map.get("moveType").toString())) {
			return R.error("移动类型不能为空");
		}
		return tsysOrgService.drag(map.get("moveId").toString(), map.get("targetId").toString(),
				map.get("moveType").toString());
	}

	/**
	 * 点击上下按钮移动机构管理节点操作
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @return
	 * @author huangwb
	 * @date 2019-06-17 14:18
	 */
	@PostMapping("/clickDrag")
	@SysLog("点击按钮移动机构管理")
	@PreAuthorize("hasAuthority('sys:tsysorg:move')")
	public R clickDrag(@RequestBody(required = true) Map<String, Object> map) {
		if (map.get("moveId") == null || StringUtils.isEmpty(map.get("moveId").toString())) {
			return R.error("移动节点不能为空");
		} else if (map.get("targetId") == null || StringUtils.isEmpty(map.get("targetId").toString())) {
			return R.error("目标节点不能为空");
		}
		return tsysOrgService.clickDrag(map.get("moveId").toString(), map.get("targetId").toString());
	}

}
