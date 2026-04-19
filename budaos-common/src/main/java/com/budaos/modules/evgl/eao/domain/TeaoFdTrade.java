package com.budaos.modules.evgl.eao.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

@SuppressWarnings("deprecation")
public class TeaoFdTrade extends BaseDomain<TeaoFdTrade>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TeaoFdTrade";
	public static final String ALIAS_T_ID = "交易ID";
	public static final String ALIAS_ORDER_ID = "订单标识ID";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_T_NO = "交易单号：格式yyyyMMdd0000000";
	public static final String ALIAS_T_WXNO = "微信流水号";
	public static final String ALIAS_T_TIME = "交易时间";
	public static final String ALIAS_T_TYPE = "交易类型：来源于字典：活动、服务、交账";
	public static final String ALIAS_T_MONEY = "收入金额";
	public static final String ALIAS_T_PAYMENT = "支付方式：来源于字典微信、线下";
	public static final String ALIAS_T_CHANNEL = "退款申请来源：微信、线下";
	public static final String ALIAS_T_STATE = "状态：收入、退款中、退款成功、已关闭";
	public static final String ALIAS_T_REMAIN_MONEY = "余额，退款后的收入";
	public static final String ALIAS_CI_ID = "学员ID";
	public static final String ALIAS_OF_SERIANO = "订单编号";
	

    /**
     * 交易ID       db_column: t_id 
     */	
	 private String tid;
    /**
     * 订单标识ID       db_column: order_id 
     */	
	 private String orderId;
    /**
     * 机构ID       db_column: ORG_ID 
     */	
	 private String orgId;
    /**
     * 交易单号：格式yyyyMMdd0000000       db_column: t_no 
     */	
	 private String tno;
    /**
     * 微信流水号       db_column: t_wxno 
     */	
	 private String twxno;
    /**
     * 交易时间       db_column: t_Time 
     */	
	 private String ttime;
    /**
     * 交易类型：来源于字典：活动、服务、交账       db_column: t_type 
     */	
	 private String ttype;
    /**
     * 收入金额       db_column: t_money 
     */	
	 private java.math.BigDecimal tmoney;
    /**
     * 支付方式：来源于字典微信、线下       db_column: t_payment 
     */	
	 private String tpayment;
    /**
     * 退款申请来源：微信、线下       db_column: t_channel 
     */	
	 private String tchannel;
    /**
     * 状态：收入、退款中、退款成功、已关闭       db_column: t_state 
     */	
	 private String tstate;
    /**
     * 余额，退款后的收入       db_column: t_remain_money 
     */	
	 private java.math.BigDecimal tremainMoney;
    /**
     * 学员ID       db_column: ci_id 
     */	
	 private String ciId;
    /**
     * 订单编号       db_column: of_seriano 
     */	
	 private String ofSeriano;
	//columns END

	public TeaoFdTrade(){
	}

	public TeaoFdTrade(
		String tid
	){
		this.tid = tid;
	}

	public void setTid(String value) {
		this.tid = value;
	}
	public String getTid() {
		return this.tid;
	}
	public void setOrderId(String value) {
		this.orderId = value;
	}
	public String getOrderId() {
		return this.orderId;
	}
	public void setOrgId(String value) {
		this.orgId = value;
	}
	public String getOrgId() {
		return this.orgId;
	}
	public void setTno(String value) {
		this.tno = value;
	}
	public String getTno() {
		return this.tno;
	}
	public void setTwxno(String value) {
		this.twxno = value;
	}
	public String getTwxno() {
		return this.twxno;
	}
	public void setTtime(String value) {
		this.ttime = value;
	}
	public String getTtime() {
		return this.ttime;
	}
	public void setTtype(String value) {
		this.ttype = value;
	}
	public String getTtype() {
		return this.ttype;
	}
	public void setTmoney(java.math.BigDecimal value) {
		this.tmoney = value;
	}
	public java.math.BigDecimal getTmoney() {
		return this.tmoney;
	}
	public void setTpayment(String value) {
		this.tpayment = value;
	}
	public String getTpayment() {
		return this.tpayment;
	}
	public void setTchannel(String value) {
		this.tchannel = value;
	}
	public String getTchannel() {
		return this.tchannel;
	}
	public void setTstate(String value) {
		this.tstate = value;
	}
	public String getTstate() {
		return this.tstate;
	}
	public void setTremainMoney(java.math.BigDecimal value) {
		this.tremainMoney = value;
	}
	public java.math.BigDecimal getTremainMoney() {
		return this.tremainMoney;
	}
	public void setCiId(String value) {
		this.ciId = value;
	}
	public String getCiId() {
		return this.ciId;
	}
	public void setOfSeriano(String value) {
		this.ofSeriano = value;
	}
	public String getOfSeriano() {
		return this.ofSeriano;
	}
	
	private TeaoFdOrder tmeOrder;

	public TeaoFdOrder getTmeOrder() {
		return tmeOrder;
	}

	public void setTmeOrder(TeaoFdOrder tmeOrder) {
		this.tmeOrder = tmeOrder;
	}
	
}

