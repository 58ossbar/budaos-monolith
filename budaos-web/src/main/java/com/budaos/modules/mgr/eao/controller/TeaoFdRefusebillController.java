package com.budaos.modules.mgr.eao.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.eao.api.TeaoFdRefusebillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/eao/teaofdrefusebill")
public class TeaoFdRefusebillController {

	@Autowired
	private TeaoFdRefusebillService teaoFdRefusebillService;
	
	/**
	 * 根据条件查询数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	@PreAuthorize("hasAuthority('eao:teaofdrefusebill:query')")
	public R query(@RequestParam Map<String, Object> params) {
		return teaoFdRefusebillService.query(params);
	}
	
}
