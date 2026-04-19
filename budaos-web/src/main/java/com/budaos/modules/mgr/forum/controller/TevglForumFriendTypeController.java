package com.budaos.modules.mgr.forum.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.forum.api.TevglForumFriendTypeService;
import com.budaos.modules.evgl.forum.domain.TevglForumFriendType;
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
@RequestMapping("/api/forum/tevglforumfriendtype")
public class TevglForumFriendTypeController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglForumFriendTypeController.class);
	@Autowired
	private TevglForumFriendTypeService tevglForumFriendTypeService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('forum:tevglforumfriendtype:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglForumFriendTypeService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('forum:tevglforumfriendtype:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglForumFriendTypeService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('forum:tevglforumfriendtype:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglForumFriendTypeService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('forum:tevglforumfriendtype:add') or hasAuthority('forum:tevglforumfriendtype:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglForumFriendType tevglForumFriendType) {
		if(StrUtils.isEmpty(tevglForumFriendType.getTypeId())) { //新增
			return tevglForumFriendTypeService.save(tevglForumFriendType);
		} else {
			return tevglForumFriendTypeService.update(tevglForumFriendType);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('forum:tevglforumfriendtype:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglForumFriendTypeService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('forum:tevglforumfriendtype:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody String[] ids) {
		return tevglForumFriendTypeService.deleteBatch(ids);
	}
	
	/**
	 * 分类列表
	 * @param params
	 * @return
	 */
	@PostMapping("/queryTypeList")
	@PreAuthorize("hasAuthority('forum:tevglforumfriendtype:query')")
	public R queryTypeList(@RequestParam Map<String, Object> params) {
		return tevglForumFriendTypeService.queryTypeListForMgr(params);
	}
	
	/**
	 * 排序号
	 * @param params
	 * @return
	 */
	@RequestMapping("/getMaxSortNum")
	public R getMaxSortNum(@RequestParam Map<String, Object> params) {
		return tevglForumFriendTypeService.getMaxSortNum(params);
	}
	
	/**
	 * 树
	 * @param params
	 * @return
	 */
	@RequestMapping("/getTree")
	public R getTree(@RequestParam Map<String, Object> params) {
		return tevglForumFriendTypeService.getTree(params);
	}
}
