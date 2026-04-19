package com.budaos.modules.mgr.eao.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.utils.poi.ExcelUtil;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.eao.api.TeaoFdOrderService;
import com.budaos.modules.evgl.eao.domain.TeaoFdOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eao/teaofdorder")
public class TeaoFdOrderController {

	@Autowired
	private TeaoFdOrderService teaoFdOrderService;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('eao:teaofdorder:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return teaoFdOrderService.query(params);
	}
	
	/**
	 * 导出excel
	 * @param map 前端传递的查询条件
	 * @return
	 */
	@RequestMapping(value = "/exportExcel")
	@PreAuthorize("hasAuthority('eao:teaofdorder:export')")
	@SysLog("导出excel")
	public R exportExcel(@RequestParam Map<String, Object> map) {
		List<TeaoFdOrder> list = teaoFdOrderService.selectListByMap(map);
		ExcelUtil<TeaoFdOrder> objectExcelUtil = new ExcelUtil<>(TeaoFdOrder.class);
		return objectExcelUtil.exportExcel(list, "报名数据");
	}
}
