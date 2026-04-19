package com.budaos.modules.mgr.pkg.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.pkg.api.TevglPkgResgroupService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/api/pkg/tevglpkgresgroup")
public class TevglPkgResgroupController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgResgroupController.class);
	@Autowired
	private TevglPkgResgroupService tevglPkgResgroupService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('pkg:tevglpkgresgroup:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglPkgResgroupService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('pkg:tevglpkgresgroup:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglPkgResgroupService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkgresgroup:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglPkgResgroupService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('pkg:tevglpkgresgroup:add') or hasAuthority('pkg:tevglpkgresgroup:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglPkgResgroup tevglPkgResgroup) {
		if(StrUtils.isEmpty(tevglPkgResgroup.getResgroupId())) { //新增
			return tevglPkgResgroupService.save(tevglPkgResgroup);
		} else {
			return tevglPkgResgroupService.update(tevglPkgResgroup);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkgresgroup:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglPkgResgroupService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('pkg:tevglpkgresgroup:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		return tevglPkgResgroupService.deleteBatch(ids);
	}
}
