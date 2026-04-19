package com.budaos.modules.mgr.pkg.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.pkg.api.TevglBookpkgTeamService;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p> Title: 资源共建权限</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/api/pkg/tevglbookpkgteam")
public class TevglBookpkgTeamController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookpkgTeamController.class);
	@Autowired
	private TevglBookpkgTeamService tevglBookpkgTeamService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('pkg:tevglbookpkgteam:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglBookpkgTeamService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('pkg:tevglbookpkgteam:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglBookpkgTeamService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglbookpkgteam:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglBookpkgTeamService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('pkg:tevglbookpkgteam:add') or hasAuthority('pkg:tevglbookpkgteam:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglBookpkgTeam tevglBookpkgTeam) {
		if(StrUtils.isEmpty(tevglBookpkgTeam.getTeamId())) { //新增
			return tevglBookpkgTeamService.save(tevglBookpkgTeam);
		} else {
			return tevglBookpkgTeamService.update(tevglBookpkgTeam);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('pkg:tevglbookpkgteam:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglBookpkgTeamService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('pkg:tevglbookpkgteam:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		return tevglBookpkgTeamService.deleteBatch(ids);
	}
}
