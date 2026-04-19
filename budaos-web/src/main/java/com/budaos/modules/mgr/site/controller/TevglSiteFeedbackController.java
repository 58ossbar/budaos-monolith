package com.budaos.modules.mgr.site.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.site.api.TevglSiteFeedbackService;
import com.budaos.modules.evgl.site.domain.TevglSiteFeedback;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p> Title: 意见反馈</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/api/site/tevglsitefeedback")
public class TevglSiteFeedbackController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteFeedbackController.class);
	@Autowired
	private TevglSiteFeedbackService tevglSiteFeedbackService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('site:tevglsitefeedback:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		//return tevglSiteFeedbackService.query(params);
		// 优先展示未回复
		params.put("sidx", "t1.has_replied");
		params.put("order", "asc, t1.create_time desc");
		return tevglSiteFeedbackService.queryFeedbackInfo(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('site:tevglsitefeedback:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglSiteFeedbackService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitefeedback:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglSiteFeedbackService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('site:tevglsitefeedback:add') or hasAuthority('site:tevglsitefeedback:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglSiteFeedback tevglSiteFeedback) {
		if(StrUtils.isEmpty(tevglSiteFeedback.getFeedbackId())) { //新增
			return tevglSiteFeedbackService.save(tevglSiteFeedback);
		} else {
			return tevglSiteFeedbackService.update(tevglSiteFeedback);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitefeedback:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglSiteFeedbackService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('site:tevglsitefeedback:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody String[] ids) {
		return tevglSiteFeedbackService.deleteBatch(ids);
	}
}
