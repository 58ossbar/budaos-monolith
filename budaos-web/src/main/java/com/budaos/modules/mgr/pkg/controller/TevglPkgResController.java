package com.budaos.modules.mgr.pkg.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.pkg.api.TevglPkgResService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgRes;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p> Title: 教学包资源</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/api/pkg/tevglpkgres")
public class TevglPkgResController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgResController.class);
	@Autowired
	private TevglPkgResService tevglPkgResService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('pkg:tevglpkgres:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglPkgResService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('pkg:tevglpkgres:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglPkgResService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkgres:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglPkgResService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('pkg:tevglpkgres:add') or hasAuthority('pkg:tevglpkgres:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglPkgRes tevglPkgRes) {
		if(StrUtils.isEmpty(tevglPkgRes.getResId())) { //新增
			return tevglPkgResService.save(tevglPkgRes);
		} else {
			return tevglPkgResService.update(tevglPkgRes);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglpkgres:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglPkgResService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('pkg:tevglpkgres:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		return tevglPkgResService.deleteBatch(ids);
	}
}
