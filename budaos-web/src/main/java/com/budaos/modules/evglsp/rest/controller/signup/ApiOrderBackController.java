package com.budaos.modules.evglsp.rest.controller.signup;

import com.budaos.modules.evgl.eao.api.TeaoFdOrderService;
import com.budaos.modules.evgl.eao.api.TeaoFdTradeService;
import com.budaos.modules.evgl.eao.domain.TeaoFdOrder;
import com.budaos.modules.evgl.eao.domain.TeaoFdTrade;
import com.budaos.utils.tool.DateUtils;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/wx/api/of")
@Api(value = "退回订单服务接口", tags = { "退回订单服务接口" })
public class ApiOrderBackController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TeaoFdTradeService teaoFdTradeService;
	@Autowired
	private TeaoFdOrderService teaoFdOrderService;
	@Value("${weixin.key}")
	private String key;
	
	/**
	 * 异步回调接口
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderback", produces = "text/html;charset=utf-8")
	@ResponseBody
	@Transactional
	public String WeixinOrderNotifyPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ServletInputStream instream = request.getInputStream();
		StringBuffer sb = new StringBuffer();
		int len = -1;
		byte[] buffer = new byte[1024];

		while ((len = instream.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, len));
		}
		instream.close();
		Map<String, String> return_data = new HashMap<String, String>();
		if (sb.toString().length() == 0) {
			return_data.put("return_code", "FAIL");
			return_data.put("return_msg", "return_code不正确");
			return WXPayUtil.mapToXml(return_data);
		}
		Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());// 接受微信的通知参数
		logger.debug("支付回调参数:" + map);
		if (!WXPayConstants.SUCCESS.equals(map.get("return_code"))
				|| !WXPayConstants.SUCCESS.equals(map.get("result_code"))) {
			return_data.put("return_code", "FAIL");
			return_data.put("return_msg", "return_code不正确");
		} else {
			String sign = map.get("sign").toString();
			map.put("sign", "");
			if (!sign.equals(WXPayUtil.generateSignature(map, key, SignType.MD5))) {
				logger.debug("签名错误");
				return_data.put("return_code", "FAIL");
				return_data.put("return_msg", "签名错误");
				return WXPayUtil.mapToXml(return_data);
			}
			String outTradeNo = map.get("out_trade_no").toString();
			BigDecimal totalFee = new BigDecimal(map.get("total_fee").toString()).divide(new BigDecimal("100"));
			// 付款完成后，系统发送该交易状态通知
			//TeaoFdOrder orderInfo = teaoFdOrderService.selectObjectBySeriano(outTradeNo);
			TeaoFdOrder orderInfo = teaoFdOrderService.selectObjectBySeriano(outTradeNo.split("-")[0]);
			if (orderInfo == null) {
				logger.debug("订单不存在");
				return_data.put("return_code", "FAIL");
				return_data.put("return_msg", "订单不存在");
				return WXPayUtil.mapToXml(return_data);
			}
			// 如果订单已经支付返回错误
			if ("02".equals(orderInfo.getOfState())) {
				logger.debug("订单已经支付");
				return_data.put("return_code", "SUCCESS");
				return_data.put("return_msg", "OK");
				return WXPayUtil.mapToXml(return_data);
			}
			// 如果支付金额不等于订单金额返回错误
			if (orderInfo.getOrderReceivable().compareTo(totalFee) != 0) {
				logger.debug("资金异常");
				return_data.put("return_code", "FAIL");
				return_data.put("return_msg", "金额异常");
				return WXPayUtil.mapToXml(return_data);
			}
			try {
				// 更新订单实收金额=订单实收金额+当前支付金额
				orderInfo.setOrderProceeds(
						(orderInfo.getOrderProceeds() == null ? new BigDecimal("0") : orderInfo.getOrderProceeds())
								.add(totalFee));
				orderInfo.setOfState("02");
				orderInfo.setOrderReceivable(new BigDecimal("0"));
				teaoFdOrderService.update(orderInfo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				TeaoFdTrade trade = new TeaoFdTrade();
				trade.setOrgId(orderInfo.getOrgId());
				trade.setTno(outTradeNo);
				trade.setTwxno(map.get("transaction_id"));
				trade.setTtime(
						DateUtils.convertDatePattern(map.get("time_end"), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
				trade.setTtype("01");
				trade.setOrderId(orderInfo.getOrderId());
				trade.setTmoney(new BigDecimal(map.get("total_fee")).divide(new BigDecimal("100")));
				trade.setTremainMoney(new BigDecimal(map.get("total_fee")).divide(new BigDecimal("100")));
				trade.setTpayment("01");
				trade.setTchannel("U");
				trade.setTstate("01");
				trade.setCiId(orderInfo.getCreateUserId());
				trade.setOfSeriano(orderInfo.getOfSeriano());
				teaoFdTradeService.save(trade);
			}
			return_data.put("return_code", "SUCCESS");
			return_data.put("return_msg", "OK");
			return WXPayUtil.mapToXml(return_data);
		}
		String xml = WXPayUtil.mapToXml(return_data);
		return xml;
	}
}
