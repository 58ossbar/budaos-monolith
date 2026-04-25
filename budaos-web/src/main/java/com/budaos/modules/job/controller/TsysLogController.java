package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.api.TsysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>操作日志</p>
 * <p>Title: TsysLogController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月23日
 */
@RestController
@RequestMapping("/api/sys/log")
public class TsysLogController {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TsysLogController.class);
	@Autowired
	private TsysLogService tsysLogService;
	
	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年5月20日
	 * @param params
	 * @return
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('sys:tsyslog:query')")
	public R query(@RequestParam Map<String, Object> params) {
		params.put("page", params.get("pageNum"));
		params.put("limit", params.get("pageSize"));
		return tsysLogService.query(params);
	}
	
	/**
	 * <p>单条删除</p>
	 * @author huj
	 * @data 2019年5月20日
	 * @param ids
	 * @return
	 */
	@PostMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('sys:tsyslog:remove')")
	public R delete(@PathVariable("id") String id) {
		return tsysLogService.delete(id);
	}
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月20日
	 * @param ids
	 * @return
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('sys:tsyslog:remove')")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tsysLogService.deleteBatch(ids);
	}
}
