package com.budaos.modules.mgr.pkg.controller;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.pkg.api.TevglPkgInfoService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
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
@RequestMapping("/api/pkg/tevglpkginfo")
public class TevglPkgInfoController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgInfoController.class);
	@Autowired
	private TevglPkgInfoService tevglPkgInfoService;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@GetMapping("/queryPackageForChange")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R queryPackageForChange(@RequestParam Map<String, Object> params) {
		return tevglPkgInfoService.queryPackageForChange(params);
	}
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param jsonObject
	 * @return R
	 */
	@PostMapping("/doChangePackage")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:changePackage')")
	@SysLog("查询列表(返回List<Bean>)")
	public R doChangePackage(@RequestBody JSONObject jsonObject) {
		return tevglPkgInfoService.doChangePackage(jsonObject);
	}
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglPkgInfoService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglPkgInfoService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglPkgInfoService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:add') or hasAuthority('pkg:tevglpkginfo:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglPkgInfo tevglPkgInfo) {
		if(StrUtils.isEmpty(tevglPkgInfo.getPkgId())) { //新增
			return tevglPkgInfoService.save(tevglPkgInfo);
		} else {
			return tevglPkgInfoService.update(tevglPkgInfo);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglPkgInfoService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('pkg:tevglpkginfo:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		return tevglPkgInfoService.deleteBatch(ids);
	}
}
