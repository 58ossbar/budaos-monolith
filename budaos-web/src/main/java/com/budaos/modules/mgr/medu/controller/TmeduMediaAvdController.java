package com.budaos.modules.mgr.medu.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.medu.me.api.TmeduMediaAvdService;
import com.budaos.modules.evgl.medu.me.domain.TmeduMediaAvd;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/medu/tmedumediaavd")
public class TmeduMediaAvdController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TmeduMediaAvdController.class);
	@Autowired
	private TmeduMediaAvdService tmeduMediaAvdService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('medu:tmedumediaavd:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tmeduMediaAvdService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('medu:tmedumediaavd:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tmeduMediaAvdService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('medu:tmedumediaavd:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tmeduMediaAvdService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('medu:tmedumediaavd:add') or hasAuthority('medu:tmedumediaavd:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TmeduMediaAvd tmeduMediaAvd, HttpServletRequest request) {
		String attachId = request.getParameter("attachId"); //图片附件关联id
		if(StrUtils.isEmpty(tmeduMediaAvd.getAvdId())) { //新增
//			return tmeduMediaAvdService.save(tmeduMediaAvd);
			return tmeduMediaAvdService.saveHasAttach(tmeduMediaAvd, attachId);
		} else {
//			return tmeduMediaAvdService.update(tmeduMediaAvd);
			return tmeduMediaAvdService.updateHasAttach(tmeduMediaAvd, attachId);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('medu:tmedumediaavd:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tmeduMediaAvdService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('medu:tmedumediaavd:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tmeduMediaAvdService.deleteBatch(ids);
	}
	/**
	 * 更新状态
	 */
	@PostMapping("/updateStatus")
	@PreAuthorize("hasAuthority('medu:tmedumediaavd:updateStatus')")
	@SysLog("更新状态")
	public R updateStatus(@RequestBody(required = false) TmeduMediaAvd tmeduMediaAvd) {
		return tmeduMediaAvdService.updateState(tmeduMediaAvd);
	}
}
