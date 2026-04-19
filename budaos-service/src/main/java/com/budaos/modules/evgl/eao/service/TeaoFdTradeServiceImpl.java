package com.budaos.modules.evgl.eao.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.eao.api.TeaoFdTradeService;
import com.budaos.modules.evgl.eao.domain.TeaoFdTrade;
import com.budaos.modules.evgl.eao.persistence.TeaoFdTradeMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TeaoFdTradeServiceImpl implements TeaoFdTradeService {

	@Autowired
	private TeaoFdTradeMapper teaoFdTradeMapper;
	@Autowired
    private ConvertUtil convertUtil;
	
	@Override
	public void save(TeaoFdTrade teaoFdTrade) throws BudaosException {
		teaoFdTrade.setTid(Identities.uuid());
		teaoFdTradeMapper.insert(teaoFdTrade);
	}

	@Override
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		if(params.get("sttime") != null && params.get("sttime").toString().length() > 0){
			params.put("sttime",params.get("sttime")+" 00:00:00");
		}
		if(params.get("ettime") != null && params.get("ettime").toString().length() > 0){
			params.put("ettime",params.get("ettime")+" 23:59:59");
		}
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TeaoFdTrade> teaoFdTradeList = teaoFdTradeMapper.selectListByMap(query);
		convertUtil.convertOrgId(teaoFdTradeList, "orgId");
		convertUtil.convertDict(teaoFdTradeList, "tpayment", "tpayment");
		PageUtils pageUtil = new PageUtils(teaoFdTradeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

}
