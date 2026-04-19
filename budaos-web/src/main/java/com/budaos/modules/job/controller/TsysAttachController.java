package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.sys.api.TsysAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sys/attach")
public class TsysAttachController {
	@Autowired
	private TsysAttachService tsysAttachService;

	/**
	 * 根据条件查询记录
	 * 
	 * @author huangwb
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('sys:tsysattach:query')")
	@SysLog(value = "根据条件查询记录")
	public R query(@RequestParam(required = false) Map<String, Object> params) {
		return tsysAttachService.query(params);
	}

	/**
	 * 删除
	 * 
	 * @author huangwb
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deletes")
	@PreAuthorize("hasAuthority('sys:tsysattach:remove')")
	@SysLog(value = "删除")
	public R delete(@RequestBody String[] ids) {
		return tsysAttachService.deleteBatch(ids);
	}

	/**
	 * 查看明细
	 * 
	 * @author huangwb
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	@GetMapping("/view/{id}")
	@SysLog(value = "查看明细")
	public R view(@PathVariable("id") String id) {
		return tsysAttachService.view(id);
	}

}
