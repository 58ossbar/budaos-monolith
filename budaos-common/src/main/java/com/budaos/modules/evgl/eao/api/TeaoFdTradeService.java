package com.budaos.modules.evgl.eao.api;

import com.budaos.common.exception.BudaosException;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.eao.domain.TeaoFdTrade;

import java.util.Map;

public interface TeaoFdTradeService {

	R query(Map<String, Object> params);
	
	void save(TeaoFdTrade teaoFdTrade) throws BudaosException;
	
}
