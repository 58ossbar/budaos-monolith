package com.budaos.modules.evgl.eao.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.EmptyUtils;
import com.budaos.modules.common.enums.WxPayEnum;
import com.budaos.modules.evgl.eao.api.TeaoFdRefusebillService;
import com.budaos.modules.evgl.eao.domain.TeaoFdOrder;
import com.budaos.modules.evgl.eao.domain.TeaoFdRefusebill;
import com.budaos.modules.evgl.eao.domain.TeaoFdTrade;
import com.budaos.modules.evgl.eao.persistence.TeaoFdOrderMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoFdRefusebillMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoFdTradeMapper;
import com.budaos.modules.evgl.weixin.api.WxPayService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeaoFdRefusebillServiceImpl implements TeaoFdRefusebillService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TeaoFdRefusebillMapper teaoFdRefusebillMapper;
	@Autowired
	private TeaoFdOrderMapper teaoFdOrderMapper;
	@Autowired
	private TeaoFdTradeMapper teaoFdTradeMapper;
	/*@Autowired
	private WeiXPayService weiXPayService;*/
	@Autowired
	private WxPayService wxPayService;
	
	@Override
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		if(params.get("srbTime") != null && params.get("srbTime").toString().length() > 0){
			params.put("srbTime",params.get("srbTime")+" 00:00:00");
		}
		if(params.get("erbTime") != null && params.get("erbTime").toString().length() > 0){
			params.put("erbTime",params.get("erbTime")+" 59:59:59");
		}
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> teaoFdRefusebillList = teaoFdRefusebillMapper.selectListByMapForRefund(query);
		PageUtils pageUtil = new PageUtils(teaoFdRefusebillList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	@Override
	@Transactional
	public R saveorupdate(String orderId, String type, String orderProceeds, String rbReason) {
		try {
			TeaoFdOrder teaoFdOrder = teaoFdOrderMapper.selectObjectById(orderId);
			if(!"01".equals(teaoFdOrder.getOfRefundState())){
				return R.error("订单状态异常");
			}
			teaoFdOrder.setOfApplyrefundDealtime(DateUtils.getNowTimeStamp());
			teaoFdOrder.setOfApplyrefundExplain(rbReason);
			//teaoFdOrder.setOfApplyrefundDealman(getUser().getUserRealname());
			if("N".equals(type)){
				teaoFdOrder.setOfRefundState("02");
				teaoFdOrder.setOfState("05");
			}else if("Y".equals(type)){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("orderId", orderId);
				TeaoFdTrade trade = teaoFdTradeMapper.selectListByMap(map).get(0);
				teaoFdOrder.setOfRefundState("03");
				teaoFdOrder.setOfState("04");
				TeaoFdRefusebill teaoFdRefusebill = new TeaoFdRefusebill();
				teaoFdRefusebill.setRbId(Identities.uuid());
				teaoFdRefusebill.setCiId(teaoFdOrder.getCreateUserId());
				teaoFdRefusebill.setTid(trade.getTid());
				teaoFdRefusebill.setOrderId(teaoFdOrder.getOrderId());
				teaoFdRefusebill.setOfSeriano(teaoFdOrder.getOfSeriano());
				teaoFdRefusebill.setRbDealman(teaoFdOrder.getOfApplyrefundDealman());
				teaoFdRefusebill.setRbDealtime(teaoFdOrder.getOfApplyrefundDealtime());
				teaoFdRefusebill.setRbExplain(teaoFdOrder.getOfApplyrefundExplain());
				teaoFdRefusebill.setRbMoney(teaoFdOrder.getOrderProceeds());
				teaoFdRefusebill.setRbReason(teaoFdOrder.getOfApplyrefundReason());
				teaoFdRefusebill.setRbTime(teaoFdOrder.getOfApplyrefundTime());
				teaoFdRefusebill.setOrgId(teaoFdOrder.getOrgId());
				teaoFdRefusebill.setRbSeriano(teaoFdOrder.getRbSeriano());
				teaoFdRefusebill.setTtype(trade.getTtype());
				teaoFdRefusebill.setTchannel(trade.getTchannel());
				teaoFdRefusebill.setTstate(trade.getTstate());
				teaoFdRefusebill.setTno(teaoFdOrder.getRbSeriano());
				teaoFdRefusebill.setTwxno(trade.getTwxno());
				teaoFdRefusebill.setTtime(DateUtils.getNowTimeStamp());
				teaoFdRefusebillMapper.insert(teaoFdRefusebill);
				//boolean flag = weiXPayService.refundOrder(teaoFdRefusebill, trade.getTmoney());
				Map<String, String> resultMap = wxPayService.orderRefund(teaoFdRefusebill.getOfSeriano(), teaoFdRefusebill.getRbSeriano(), trade.getTmoney(), teaoFdRefusebill.getRbMoney());
				log.info("退款结果 =》 {}", resultMap);
				if (!EmptyUtils.isEmpty(resultMap)) {
                    if (!EmptyUtils.isEmpty(resultMap.get("err_code"))) {
                        throw new BudaosException("退款失败，请稍后再试");
                    }
                    if (resultMap.get("return_msg").equals(WxPayEnum.REFUND_SUCCESS.getCode())
                            && resultMap.get("return_code").equals(WxPayEnum.REFUND_OK.getCode())) {
                        // 退款成功
                    } else if (resultMap.get("return_code").equals(WxPayEnum.BUSINESS_BALANCE_NOTENOUGH.getCode())) {
                        // 退款失败,商户余额不足
                        throw new BudaosException("退款失败,商户余额不足");
                    }
                }
			}
			teaoFdOrderMapper.update(teaoFdOrder);
		} catch (Exception e) {
			e.printStackTrace();
			R.error();
		}
		return R.ok();
	}

}
