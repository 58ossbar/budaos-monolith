package com.budaos.modules.mgr.book.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.book.api.TevglBookRegularService;
import com.budaos.modules.evgl.book.domain.TevglBookRegular;
import com.budaos.utils.constants.Constant;
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
@RequestMapping("/api/book/tevglbookregular")
public class TevglBookRegularController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookRegularController.class);
	@Autowired
	private TevglBookRegularService tevglBookRegularService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('book:tevglbookregular:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglBookRegularService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('book:tevglbookregular:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglBookRegularService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('book:tevglbookregular:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglBookRegularService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('book:tevglbookregular:add') or hasAuthority('book:tevglbookregular:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglBookRegular tevglBookRegular) {
		if(StrUtils.isEmpty(tevglBookRegular.getRegularId())) { //新增
			return tevglBookRegularService.save(tevglBookRegular);
		} else {
			return tevglBookRegularService.update(tevglBookRegular);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('book:tevglbookregular:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglBookRegularService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('book:tevglbookregular:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglBookRegularService.deleteBatch(ids);
	}
	
	/**
	 * 左侧课程考核规则树
	 * @param params
	 * @return
	 */
	@GetMapping("/getSubjectRegularTree")
	public R getSubjectRegularTree(@RequestParam Map<String, Object> params) {
		return R.ok().put(Constant.R_DATA, tevglBookRegularService.getSubjectRegularTree(params));
	}

	/**
	 * 右键新增规则节点
	 * @param regular
	 * @return
	 */
	@PostMapping("/saveRegular")
	public R saveRegular(@RequestBody TevglBookRegular regular) {
		return tevglBookRegularService.saveRegular(regular);
	}
	
	/**
	 * 点击课程将显示出该课程对应的预览规则页面
	 * @param subjectId
	 * @return
	 */
	@GetMapping("/viewRegular")
	//@PreAuthorize("hasAuthority('book:tevglbookregular:viewRegular')")
	@SysLog("查看规则")
	public R viewRegular(String subjectId) {
		return tevglBookRegularService.viewRegular(subjectId);
	}
	
	/**
	 * 新增修改基本信息
	 * @param tevglBookRegular
	 * @param type 为1时标识是父节点
	 * @return
	 */
	@PostMapping("/saveOrUpdate")
	@PreAuthorize("hasAuthority('book:tevglbookregular:add') or hasAuthority('book:tevglbookregular:edit')")
	public R saveOrUpdate(@RequestBody TevglBookRegular tevglBookRegular, String type) {
		return tevglBookRegularService.saveOrUpdate(tevglBookRegular, type);
	}
	
	/**
	 * 新增修改基本信息
	 * @param tevglBookRegular
	 * @param type 为1时标识是父节点
	 * @return
	 */
	@PostMapping("/removeRegular")
	public R removeRegular(String regularId, String type) {
		return tevglBookRegularService.removeRegular(regularId, type);
	}
	
	/**
	 * 复制粘贴
	 * @param copySubjectId
	 * @param pasteSubjectId
	 * @return
	 */
	@PostMapping("/paste")
	public R paste(String copySubjectId, String pasteSubjectId) {
		return tevglBookRegularService.paste(copySubjectId, pasteSubjectId);
	}
}
