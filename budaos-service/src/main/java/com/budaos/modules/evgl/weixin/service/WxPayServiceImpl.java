package com.budaos.modules.evgl.weixin.service;

import com.alibaba.fastjson.JSON;
import com.budaos.common.exception.BudaosException;
import com.budaos.modules.common.RandomStringUtil;
import com.budaos.modules.common.sdk.weixinpay.PaymentApi;
import com.budaos.modules.common.sdk.weixinpay.PaymentKit;
import com.budaos.modules.common.sdk.weixinpay.WXPayUtil;
import com.budaos.modules.evgl.weixin.api.WxPayService;
import com.budaos.modules.evgl.weixin.domain.ParamEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {

	Logger log = LoggerFactory.getLogger(WxPayServiceImpl.class);
	
    /**
     * 支付小程序appid
     */
    @Value("${weixin.appid}")
    private String appid;

    /**
     * 支付APP端的appid
     */
    @Value("${weixin.app_appid}")
    private String app_appid;

    /**
     * 支付小程序秘钥
     */
    @Value("${weixin.secret}")
    private String secret;

    /**
     * 商户号
     */
    @Value("${weixin.mchid}")
    private String mch_id;

    /**
     * 证书路径
     */
    @Value("${weixin.certurl}")
    private String cert_url;

    /**
     * pc回调地址
     */
    @Value("${weixin.pc_notifyurl}")
    private String pc_notify_url;

    /**
     * pc退款回调地址
     */
    @Value("${weixin.pc_refund_notifyurl}")
    private String pc_refund_notify_url;

    /**
     * pc退款回调地址
     */
    @Value("${weixin.order_refund_notifyurl}")
    private String order_refund_notifyurl;

    /**
     * pc保证金退款回调地址
     */
    @Value("${weixin.pc_bond_refund_notifyurl}")
    private String pc_bond_refund_notify_url;

    /**
     * app回调地址
     */
    @Value("${weixin.app_notifyurl}")
    private String app_notify_url;

    /**
     * 商户秘钥
     */
    @Value("${weixin.key}")
    private String key;

    /**
     * h5支付跳转地址
     */
    @Value("${weixin.redirect_url}")
    private String redirectUrl;


    @Override
    public Map<String, String> gotoPay(String orderFormId, BigDecimal money, String openid, String ip,Integer type) throws BudaosException, Exception {
        Map<String, String> reqParams = new HashMap<>();
        //生成一个新的订单支付编号
        String outTradeNo="";
        if(ParamEnum.PAY_XCX.getCode().equals(type)){
            outTradeNo=orderFormId+"-"+ RandomStringUtil.getRandomCode(3,0)+"XCX";
            //微信分配的小程序ID
            reqParams.put("appid", appid);
            //用户标识
            reqParams.put("openid", openid);
            //交易类型
            reqParams.put("trade_type", "JSAPI");
        } else if (ParamEnum.PAY_H5.getCode().equals(type)) {
            outTradeNo=orderFormId+"-"+ RandomStringUtil.getRandomCode(3,0)+"XCX";
            //微信分配的小程序ID
            reqParams.put("appid", appid);
            //用户标识
            reqParams.put("openid", openid);
            //交易类型
            reqParams.put("trade_type", "MWEB");
        } else {
            outTradeNo=orderFormId+"-"+ RandomStringUtil.getRandomCode(3,0)+"APP";
            //微信分配的APPID
            reqParams.put("appid", app_appid);
            //交易类型
            reqParams.put("trade_type", "APP");
        }
        //微信支付分配的商户号
        reqParams.put("mch_id", mch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "支付订单号"+orderFormId+"");

        //商户订单编号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
        reqParams.put("spbill_create_ip", ip);
        //通知地址
        reqParams.put("notify_url", app_notify_url);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, key);
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        log.info(JSON.toJSONString(reqParams));
        String xmlResult = PaymentApi.pushOrder(reqParams);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        //预付单信息
        String prepay_id = result.get("prepay_id");

        /*
            小程序调起支付数据签名
         */
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("appId", reqParams.get("appid"));
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", System.currentTimeMillis() + "");
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = WXPayUtil.generateSignature(packageParams, key);
        packageParams.put("paySign", packageSign);
        packageParams.put("codeUrl", result.get("code_url"));
        if (ParamEnum.PAY_H5.getCode().equals(type)) {
            packageParams.put("mwebUrl", result.get("mweb_url") + "&redirect_url="+ URLEncoder.encode(redirectUrl, "GBK"));
        }
        if (ParamEnum.PAY_APP.getCode().equals(type)) {
            packageParams.put("partnerId", mch_id);
        }
        return packageParams;
    }

    @Override
    public Map<String, String> refund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws BudaosException, Exception {
        //退款资金来源-可用余额退款
        String refundAccount="REFUND_SOURCE_RECHARGE_FUNDS";
        Map<String, String> params = new HashMap<>();
        if(outRefundNo.contains("XCX")){
            //小程序微信退款
            params.put("appid", appid);
        }else if(outRefundNo.contains("APP")){
            //APP微信退款
            params.put("appid", app_appid);
        }else {
            params.put("appid", appid);
        }
        params.put("mch_id",mch_id);
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //商户订单号和微信订单号二选一
//        params.put("out_trade_no", wxPayLog.getOutTradeNo());
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", outRefundNo);
        params.put("total_fee", total.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_fee", refund.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_account", refundAccount);
        // 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
        params.put("refund_desc","退款");
        //退款回调
        params.put("notify_url", pc_refund_notify_url);
        //签名算法
        String sign = WXPayUtil.generateSignature(params,key);
        params.put("sign", sign);
        String xml = PaymentKit.toXml(params);
        log.info(xml);
        String xmlStr = WXPayUtil.doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund", xml,cert_url,mch_id);
        log.info(xmlStr);
        //加入微信支付日志
        Map map = PaymentKit.xmlToMap(xmlStr);
        return map;
    }

    @Override
    public Map<String, String> orderRefund(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws BudaosException, Exception {
        //退款资金来源-可用余额退款
        String refundAccount="REFUND_SOURCE_RECHARGE_FUNDS";
        Map<String, String> params = new HashMap<>();
        if(outRefundNo.contains("XCX")){
            //小程序微信退款
            params.put("appid", appid);
        }else if(outRefundNo.contains("APP")){
            //APP微信退款
            params.put("appid", app_appid);
        }else {
            params.put("appid", appid);
        }
        params.put("mch_id",mch_id);
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //商户订单号和微信订单号二选一
//        params.put("out_trade_no", wxPayLog.getOutTradeNo());
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", outRefundNo);
        params.put("total_fee", total.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_fee", refund.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_account", refundAccount);
        // 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
        params.put("refund_desc","退款");
        //退款回调
        params.put("notify_url", order_refund_notifyurl);
        //签名算法
        String sign = WXPayUtil.generateSignature(params,key);
        params.put("sign", sign);
        String xml = PaymentKit.toXml(params);
        log.info(xml);
        String xmlStr = WXPayUtil.doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund", xml,cert_url,mch_id);
        log.info(xmlStr);
        //加入微信支付日志
        Map map = PaymentKit.xmlToMap(xmlStr);
        return map;
    }

    @Override
    public Map<String, String> refundBond(String transactionId, String outRefundNo, BigDecimal total, BigDecimal refund) throws BudaosException, Exception {
        //退款资金来源-可用余额退款
        String refundAccount="REFUND_SOURCE_RECHARGE_FUNDS";
        Map<String, String> params = new HashMap<>();
        if(outRefundNo.contains("XCX")){
            //小程序微信退款
            params.put("appid", appid);
        }else if(outRefundNo.contains("APP")){
            //APP微信退款
            params.put("appid", app_appid);
        }else {
            params.put("appid", appid);
        }
        params.put("mch_id",mch_id);
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //商户订单号和微信订单号二选一
//        params.put("out_trade_no", wxPayLog.getOutTradeNo());
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", outRefundNo);
        params.put("total_fee", total.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_fee", refund.multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("refund_account", refundAccount);
        // 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
        params.put("refund_desc","退款");
        //退款回调
        params.put("notify_url", pc_bond_refund_notify_url);
        //签名算法
        String sign = WXPayUtil.generateSignature(params,key);
        params.put("sign", sign);
        String xml = PaymentKit.toXml(params);
        log.info(xml);
        String xmlStr = WXPayUtil.doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund", xml,cert_url,mch_id);
        log.info(xmlStr);
        //加入微信支付日志
        Map map = PaymentKit.xmlToMap(xmlStr);
        return map;
    }

    @Override
    public String getCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws BudaosException,Exception {
        Map<String, String> reqParams = new HashMap<>();
        //生成一个新的订单支付编号
        String outTradeNo=orderFormid+"-"+ RandomStringUtil.getRandomCode(3,0);
        //微信分配的小程序ID
        reqParams.put("appid",appid);
        //微信支付分配的商户号
        reqParams.put("mch_id",mch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "支付订单号"+orderFormid);

        //商户订单编号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
//        reqParams.put("spbill_create_ip", "127.0.0.1");
        reqParams.put("spbill_create_ip", ip);
        //支付回调地址
        reqParams.put("notify_url", pc_notify_url);
        //交易类型
        reqParams.put("trade_type", tradeType);
        //用户标识
//        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams,key);
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String code_url = result.get("code_url");
        return code_url;
    }

    @Override
    public String getOrderCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws BudaosException,Exception {
        Map<String, String> reqParams = new HashMap<>();
        //生成一个新的订单支付编号
        String outTradeNo=orderFormid+"-"+ RandomStringUtil.getRandomCode(3,0);
        //微信分配的小程序ID
        reqParams.put("appid",appid);
        //微信支付分配的商户号
        reqParams.put("mch_id",mch_id);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "支付订单号"+orderFormid);

        //商户订单编号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
//        reqParams.put("spbill_create_ip", "127.0.0.1");
        reqParams.put("spbill_create_ip", ip);
        //支付回调地址
        reqParams.put("notify_url", app_notify_url);
        //交易类型
        reqParams.put("trade_type", tradeType);
        //用户标识
//        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams,key);
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        String code_url = result.get("code_url");
        return code_url;
    }
}
