package com.budaos.modules.mgr.pkg.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.pkg.api.TevglPkgCheckService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgCheck;
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
@RequestMapping("/api/pkg/tevglpkgcheck")
public class TevglPkgCheckController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgCheckController.class);
	@Autowired
	private TevglPkgCheckService tevglPkgCheckService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('pkg:tevglpkgcheck:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglPkgCheckService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('pkg:tevglpkgcheck:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		//return tevglPkgCheckService.queryForMap(params);
		return tevglPkgCheckService.queryPkgListForMgr(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkgcheck:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglPkgCheckService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('pkg:tevglpkgcheck:add') or hasAuthority('pkg:tevglpkgcheck:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglPkgCheck tevglPkgCheck) {
		if(StrUtils.isEmpty(tevglPkgCheck.getPcId())) { //新增
			return tevglPkgCheckService.save(tevglPkgCheck);
		} else {
			return tevglPkgCheckService.update(tevglPkgCheck);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkgcheck:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglPkgCheckService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('pkg:tevglpkgcheck:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglPkgCheckService.deleteBatch(ids);
	}
	
	/**
	 * 审核是否通过
	 * @param tevglPkgCheck
	 * @return
	 */
	@PostMapping("/updatePkgReleaseStatus")
	@PreAuthorize("hasAuthority('pkg:tevglpkgcheck:updatePkgReleaseStatus')")
	@SysLog("批量删除")
	public R updatePkgReleaseStatus(@RequestBody(required = true) TevglPkgCheck tevglPkgCheck) {
		return tevglPkgCheckService.updatePkgReleaseStatus(tevglPkgCheck);
	}
	
	
}
