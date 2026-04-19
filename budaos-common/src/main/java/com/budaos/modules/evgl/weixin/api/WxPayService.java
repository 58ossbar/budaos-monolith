package com.budaos.modules.evgl.weixin.api;

import com.budaos.common.exception.BudaosException;

import java.math.BigDecimal;
import java.util.Map;

public interface WxPayService {

    /**
     * 微信支付
     * @param orderFormid 订单编号
     * @param money 订单金额
     * @param openid 客户openid
     * @param ip 服务ip地址
     * @param type 小程序或app
     * @return
     * @throws CoBusinessException
     * @throws Exception
     */
    public Map<String,String> gotoPay(String orderFormid, BigDecimal money, String openid, String ip, Integer type) throws BudaosException, Exception;

    /**
     * 售后微信退款
     * @param transactionId 支付单号
     * @param outRefundNo 支付生成的退款单号
     * @param total 订单总金额
     * @param refund 退款金额
     * @throws CoBusinessException
     * @throws Exception
     */
    public Map<String,String> refund(String transactionId,String outRefundNo,BigDecimal total,BigDecimal refund) throws BudaosException, Exception;

    /**
     * 订单取消微信退款
     * @param transactionId 支付单号
     * @param outRefundNo 支付生成的退款单号
     * @param total 订单总金额
     * @param refund 退款金额
     * @throws CoBusinessException
     * @throws Exception
     */
    public Map<String,String> orderRefund(String transactionId,String outRefundNo,BigDecimal total,BigDecimal refund) throws BudaosException, Exception;

    /**
     * 保证金退款
     * @param transactionId 支付单号
     * @param outRefundNo 支付生成的退款单号
     * @param total 订单总金额
     * @param refund 退款金额
     * @throws CoBusinessException
     * @throws Exception
     */
    public Map<String,String> refundBond(String transactionId,String outRefundNo,BigDecimal total,BigDecimal refund) throws BudaosException, Exception;


    /**
     * 生成保证金收款码返回
     * @param orderFormid 订单编号
     * @param money 金额
     * @param ip IP地址
     * @param tradeType 支付类型
     * @throws Exception
     */
    public String getCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws BudaosException, Exception;

    /**
     * 生成订单收款码返回
     * @param orderFormid 订单编号
     * @param money 金额
     * @param ip IP地址
     * @param tradeType 支付类型
     * @throws Exception
     */
    public String getOrderCollectionCode(String orderFormid, BigDecimal money, String ip, String tradeType) throws BudaosException, Exception;
}
