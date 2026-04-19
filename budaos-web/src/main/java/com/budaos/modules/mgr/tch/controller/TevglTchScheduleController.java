package com.budaos.modules.mgr.tch.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.tch.api.TevglTchScheduleService;
import com.budaos.modules.evgl.tch.domain.TevglTchSchedule;
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
@RequestMapping("/api/tch/tevgltchschedule")
public class TevglTchScheduleController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchScheduleController.class);
	@Autowired
	private TevglTchScheduleService tevglTchScheduleService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglTchScheduleService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglTchScheduleService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglTchScheduleService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:add') or hasAuthority('tch:tevgltchschedule:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglTchSchedule tevglTchSchedule) {
		if(StrUtils.isEmpty(tevglTchSchedule.getScheduleId())) { //新增
			return tevglTchScheduleService.save(tevglTchSchedule);
		} else {
			return tevglTchScheduleService.update(tevglTchSchedule);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglTchScheduleService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglTchScheduleService.deleteBatch(ids);
	}
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@GetMapping("/queryScheduleData")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R queryScheduleData(@RequestParam Map<String, Object> params) {
		return tevglTchScheduleService.queryScheduleData(params);
	}
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@GetMapping("/queryScheduleDataV2")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R queryScheduleDataV2(@RequestParam Map<String, Object> params) {
		return tevglTchScheduleService.queryScheduleDataV2(params);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveBatch")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:add') or hasAuthority('tch:tevgltchschedule:edit')")
	@SysLog("执行数据新增")
	public R saveBatch(@RequestBody(required = false) TevglTchSchedule tevglTchSchedule) {
		return tevglTchScheduleService.saveBatch(tevglTchSchedule);
	}
	
	/**
	 * 查询教室列表
	 * @param params
	 * @return R
	 */
	@GetMapping("/queryTrainingRoomList")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:query')")
	@SysLog("查询教室列表")
	public R queryTrainingRoomList(@RequestParam Map<String, Object> params) {
		return tevglTchScheduleService.queryTrainingRoomList(params);
	}
	
	/**
	 * 调课
	 * 
	 */
	@PostMapping("/exchangeSchedule")
	@PreAuthorize("hasAuthority('tch:tevgltchschedule:exchange')")
	@SysLog("调课")
	public R exchangeSchedule(@RequestBody TevglTchSchedule tevglTchSchedule) {
		return tevglTchScheduleService.exchangeSchedule(tevglTchSchedule);
	}
}
