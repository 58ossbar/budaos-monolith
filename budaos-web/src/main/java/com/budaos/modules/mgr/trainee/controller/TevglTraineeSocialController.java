package com.budaos.modules.mgr.trainee.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.trainee.api.TevglTraineeSocialService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeSocial;
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
@RequestMapping("/api/trainee/tevgltraineesocial")
public class TevglTraineeSocialController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTraineeSocialController.class);
	@Autowired
	private TevglTraineeSocialService tevglTraineeSocialService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('trainee:tevgltraineesocial:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglTraineeSocialService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('trainee:tevgltraineesocial:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglTraineeSocialService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('trainee:tevgltraineesocial:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglTraineeSocialService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('trainee:tevgltraineesocial:add') or hasAuthority('trainee:tevgltraineesocial:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglTraineeSocial tevglTraineeSocial) {
		if(StrUtils.isEmpty(tevglTraineeSocial.getSocialId())) { //新增
			return tevglTraineeSocialService.save(tevglTraineeSocial);
		} else {
			return tevglTraineeSocialService.update(tevglTraineeSocial);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('trainee:tevgltraineesocial:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglTraineeSocialService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('trainee:tevgltraineesocial:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		return tevglTraineeSocialService.deleteBatch(ids);
	}
}
