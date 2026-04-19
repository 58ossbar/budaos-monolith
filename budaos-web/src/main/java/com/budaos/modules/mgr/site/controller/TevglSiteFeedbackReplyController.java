package com.budaos.modules.mgr.site.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.site.api.TevglSiteFeedbackReplyService;
import com.budaos.modules.evgl.site.domain.TevglSiteFeedbackReply;
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
@RequestMapping("/api/site/tevglsitefeedbackreply")
public class TevglSiteFeedbackReplyController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteFeedbackReplyController.class);
	@Autowired(required = false)
	private TevglSiteFeedbackReplyService tevglSiteFeedbackReplyService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('site:tevglsitefeedbackreply:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		if (tevglSiteFeedbackReplyService == null) {
			return R.error("服务未启用，请联系管理员配置");
		}
		return tevglSiteFeedbackReplyService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('site:tevglsitefeedbackreply:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		if (tevglSiteFeedbackReplyService == null) {
			return R.error("服务未启用，请联系管理员配置");
		}
		return tevglSiteFeedbackReplyService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitefeedbackreply:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		if (tevglSiteFeedbackReplyService == null) {
			return R.error("服务未启用，请联系管理员配置");
		}
		return tevglSiteFeedbackReplyService.view(id);
	}

	/**
	 * 执行数据新增
	 *
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('site:tevglsitefeedbackreply:add') or hasAuthority('site:tevglsitefeedbackreply:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglSiteFeedbackReply tevglSiteFeedbackReply) {
		if (tevglSiteFeedbackReplyService == null) {
			return R.error("服务未启用，请联系管理员配置");
		}
		if(StrUtils.isEmpty(tevglSiteFeedbackReply.getReplyId())) { //新增
			return tevglSiteFeedbackReplyService.save(tevglSiteFeedbackReply);
		} else {
			return tevglSiteFeedbackReplyService.update(tevglSiteFeedbackReply);
		}
	}

	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitefeedbackreply:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		if (tevglSiteFeedbackReplyService == null) {
			return R.error("服务未启用，请联系管理员配置");
		}
		return tevglSiteFeedbackReplyService.delete(id);
	}

	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('site:tevglsitefeedbackreply:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		if (tevglSiteFeedbackReplyService == null) {
			return R.error("服务未启用，请联系管理员配置");
		}
		return tevglSiteFeedbackReplyService.deleteBatch(ids);
	}

	/**
	 * 查看意见反馈
	 * @param feedbackId
	 * @return
	 */
	@GetMapping("/viewFeedbackReplyInfo")
	public R viewFeedbackReplyInfo(String feedbackId) {
		if (tevglSiteFeedbackReplyService == null) {
			return R.error("服务未启用，请联系管理员配置");
		}
		return tevglSiteFeedbackReplyService.viewFeedbackReplyInfo(feedbackId);
	}
}
