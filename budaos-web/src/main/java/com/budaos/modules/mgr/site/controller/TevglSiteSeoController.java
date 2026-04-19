package com.budaos.modules.mgr.site.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.site.api.TevglSiteSeoService;
import com.budaos.modules.evgl.site.domain.TevglSiteSeo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/api/site/tevglsiteseo")
public class TevglSiteSeoController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteSeoController.class);
	@Autowired
	private TevglSiteSeoService tevglSiteSeoService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('site:tevglsiteseo:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglSiteSeoService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('site:tevglsiteseo:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglSiteSeoService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('site:tevglsiteseo:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglSiteSeoService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('site:tevglsiteseo:add') or hasAuthority('site:tevglsiteseo:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglSiteSeo tevglSiteSeo) {
		if(StrUtils.isEmpty(tevglSiteSeo.getSeoId())) { //新增
			return tevglSiteSeoService.save(tevglSiteSeo);
		} else {
			return tevglSiteSeoService.update(tevglSiteSeo);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('site:tevglsiteseo:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglSiteSeoService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('site:tevglsiteseo:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		return tevglSiteSeoService.deleteBatch(ids);
	}
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param id
	 * @return
	 */
	@GetMapping("/viewSeo/{id}")
	public R viewSeo(@PathVariable("id") String id) {
		TevglSiteSeo tevglSiteSeo = tevglSiteSeoService.selectObjectByRelationId(id);
		return R.ok().put(Constant.R_DATA, tevglSiteSeo);
	}
	
	/**
	 * <p>进入编辑界面时，需要的数据</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/editsiteseo/{id}")
	public R editSiteSeo(HttpServletRequest request, HttpServletResponse response) {
		return R.ok().put(Constant.R_DATA, tevglSiteSeoService.editSiteSeo(request, response));

	}
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/removeseo/{id}")
	public R deleteSeo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		return tevglSiteSeoService.deleteBatch(new String[]{id});
	}
	
	
}
