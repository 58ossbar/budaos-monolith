package com.budaos.modules.mgr.forum.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.forum.api.TevglForumBlogPostService;
import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;
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
@RequestMapping("/api/forum/tevglforumblogpost")
public class TevglForumBlogPostController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglForumBlogPostController.class);
	@Autowired
	private TevglForumBlogPostService tevglForumBlogPostService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('forum:tevglforumblogpost:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglForumBlogPostService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('forum:tevglforumblogpost:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglForumBlogPostService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('forum:tevglforumblogpost:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglForumBlogPostService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('forum:tevglforumblogpost:add') or hasAuthority('forum:tevglforumblogpost:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglForumBlogPost tevglForumBlogPost) {
		if(StrUtils.isEmpty(tevglForumBlogPost.getPostId())) { //新增
			return tevglForumBlogPostService.save(tevglForumBlogPost);
		} else {
			return tevglForumBlogPostService.update(tevglForumBlogPost);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('forum:tevglforumblogpost:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglForumBlogPostService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('forum:tevglforumblogpost:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglForumBlogPostService.deleteBatch(ids);
	}
}
