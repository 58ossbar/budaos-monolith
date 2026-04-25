package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.sys.api.TsysPostService;
import com.budaos.modules.sys.domain.TsysPost;
import com.budaos.utils.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>岗位管理</p>
 * <p>Title: TsysPostController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖布道师学习通</p> 
 * @author huj
 * @date 2019年5月29日
 */
@RestController
@RequestMapping("/api/sys/post")
public class TsysPostController {

	@Autowired
	private TsysPostService tsysPostService;
	
	/**
	 * <p>查询列表(返回List<Bean>)</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('sys:tsyspost:query')")
	@SysLog("根据条件分页查询岗位")
	public R query(@RequestParam Map<String, Object> params) {
		return tsysPostService.query(params);
	}
	
	/**
	 * <p>无分页查询</p>
	 * @author huj
	 * @data 2019年5月30日
	 * @return
	 */
	@GetMapping("/queryAll")
	@PreAuthorize("hasAuthority('sys:tsyspost:query')")
	public R queryNoPage(@RequestParam Map<String, Object> params) {
		List<TsysPost> tsysPostList = tsysPostService.selectListByMap(params);
		return R.ok().put(Constant.R_DATA, tsysPostList);
	}
	
	/**
	 * <p>无分页查询（有父子关系）</p>
	 * @author huj
	 * @data 2019年6月13日
	 * @param params
	 * @return
	 */
	@GetMapping("/queryNoPage")
	@PreAuthorize("hasAuthority('sys:tsyspost:query')")
	public R queryNoPage2(@RequestParam Map<String, Object> params) {
		return tsysPostService.queryNoPage(params);
	}
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('sys:tsyspost:view')")
	public R selectObjectById(@PathVariable("id") String id) {
		return tsysPostService.selectObjectById(id);
	}
	
	/**
	 * <p>执行数据新增和修改</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param tstudent
	 * @return
	 */
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('sys:tsyspost:add') and hasAuthority('sys:tsyspost:edit')")
	@SysLog("执行数据新增或修改")
	public R save(@RequestBody(required = false) TsysPost tsysPost) {
		if (tsysPost.getPostId() == null || "".equals(tsysPost.getPostId())) {
			return tsysPostService.save(tsysPost);
		} else {
			return tsysPostService.update(tsysPost);
		}
	}
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param ids
	 * @return
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('sys:tsyspost:remove')")
	@SysLog("批量删除岗位")
	public R deleteBatch(@RequestBody String[] ids) {
		return tsysPostService.deleteBatch(ids);
	}

	/**
	 * <p>修改排序号</p>
	 * @author huj
	 * @data 2019年6月18日
	 * @param params
	 * @return
	 */
	@GetMapping("/updateSort")
	public R updateSort(@RequestParam Map<String, Object> params) {
		return tsysPostService.updateSort(params);
	}
	
	/**
	 * <p>获取岗位的最大排序号</p>
	 * @author huj
	 * @data 2019年6月24日
	 * @return
	 */
	@GetMapping("/getMaxSort")
	public R getMaxSort() {
		return R.ok().put(Constant.R_DATA, tsysPostService.getMaxSort());
	}
}
