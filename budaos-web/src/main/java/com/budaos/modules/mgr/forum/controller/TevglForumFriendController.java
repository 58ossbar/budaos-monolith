package com.budaos.modules.mgr.forum.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.forum.api.TevglForumFriendService;
import com.budaos.modules.evgl.forum.api.TevglForumFriendTypeService;
import com.budaos.modules.evgl.forum.domain.TevglForumFriend;
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
@RequestMapping("/api/forum/tevglforumfriend")
public class TevglForumFriendController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglForumFriendController.class);
	@Autowired
	private TevglForumFriendService tevglForumFriendService;
	@Autowired
	private TevglForumFriendTypeService tevglForumFriendTypeService;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('forum:tevglforumfriend:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglForumFriendService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('forum:tevglforumfriend:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglForumFriendService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('forum:tevglforumfriend:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglForumFriendService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('forum:tevglforumfriend:add') or hasAuthority('forum:tevglforumfriend:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglForumFriend tevglForumFriend) {
		if(StrUtils.isEmpty(tevglForumFriend.getFriendId())) { //新增
			return tevglForumFriendService.save(tevglForumFriend);
		} else {
			return tevglForumFriendService.update(tevglForumFriend);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('forum:tevglforumfriend:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglForumFriendService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('forum:tevglforumfriend:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglForumFriendService.deleteBatch(ids);
	}
	
	/**
	 * 更新状态
	 */
	@PostMapping("/updateState")
	@SysLog("更新状态")
	public R updateState(@RequestBody TevglForumFriend tevglForumFriend) {
		return tevglForumFriendService.updateState(tevglForumFriend);
	}
	
}
