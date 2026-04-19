package com.budaos.modules.evgl.eao.api;

import com.budaos.core.baseclass.domain.R;

import java.util.Map;

public interface TeaoFdRefusebillService {

	/**
	 * 根据条件查询退款记录
	 * @param params
	 * @return
	 */
	R query(Map<String, Object> params);
	
	/**
	 * 审核
	 * @param orderId
	 * @param type
	 * @param orderProceeds
	 * @param rbReason
	 * @return
	 */
	R saveorupdate(String orderId, String type, String orderProceeds, String rbReason);
}
