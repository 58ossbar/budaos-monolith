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
public class TeaoFdRefusebill extends BaseDomain<TeaoFdRefusebill>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TeaoFdRefusebill";
	public static final String ALIAS_RB_ID = "退款信息ID";
	public static final String ALIAS_ORDER_ID = "订单标识ID";
	public static final String ALIAS_T_ID = "交易ID";
	public static final String ALIAS_RB_TIME = "退款申请时间";
	public static final String ALIAS_RB_DEALTIME = "退款处理时间";
	public static final String ALIAS_RB_DEALMAN = "退款处理人";
	public static final String ALIAS_RB_REASON = "退款原因";
	public static final String ALIAS_RB_EXPLAIN = "处理说明";
	public static final String ALIAS_RB_MONEY = "退款金额";
	public static final String ALIAS_RB_RESULT = "处理结果 ,成功，打回等";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_T_NO = "交易单号：格式yyyyMMdd0000000";
	public static final String ALIAS_T_WXNO = "微信流水号";
	public static final String ALIAS_T_TIME = "交易时间";
	public static final String ALIAS_T_TYPE = "交易类型：来源于字典：活动、服务、交账";
	public static final String ALIAS_T_CHANNEL = "退款申请来源：微信、线下";
	public static final String ALIAS_T_STATE = "状态：收入、退款中、退款成功、已关闭";
	public static final String ALIAS_CI_ID = "学员ID";
	public static final String ALIAS_OF_SERIANO = "订单编号";
	public static final String ALIAS_RB_SERIANO = "退款编号";
	

    /**
     * 退款信息ID       db_column: rb_id 
     */
	 private String rbId;
    /**
     * 订单标识ID       db_column: order_id 
     */	
	 private String orderId;
    /**
     * 交易ID       db_column: t_id 
     */	
	 private String tid;
    /**
     * 退款申请时间       db_column: rb_time 
     */	
	 private String rbTime;
    /**
     * 退款处理时间       db_column: rb_dealtime 
     */	
	 private String rbDealtime;
    /**
     * 退款处理人       db_column: rb_dealman 
     */	
	 private String rbDealman;
    /**
     * 退款原因       db_column: rb_reason 
     */	
	 private String rbReason;
    /**
     * 处理说明       db_column: rb_explain 
     */	
	 private String rbExplain;
    /**
     * 退款金额       db_column: rb_money 
     */	
	 private java.math.BigDecimal rbMoney;
    /**
     * 处理结果 ,成功，打回等       db_column: rb_result 
     */	
	 private String rbResult;
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
     * 退款申请来源：微信、线下       db_column: t_channel 
     */	
	 private String tchannel;
    /**
     * 状态：收入、退款中、退款成功、已关闭       db_column: t_state 
     */	
	 private String tstate;
    /**
     * 学员ID       db_column: ci_id 
     */	
	 private String ciId;
    /**
     * 订单编号       db_column: of_seriano 
     */	
	 private String ofSeriano;
    /**
     * 退款编号       db_column: rb_seriano 
     */	
	 private String rbSeriano;
	//columns END

	public TeaoFdRefusebill(){
	}

	public TeaoFdRefusebill(
		String rbId
	){
		this.rbId = rbId;
	}

	public void setRbId(String value) {
		this.rbId = value;
	}
	public String getRbId() {
		return this.rbId;
	}
	public void setOrderId(String value) {
		this.orderId = value;
	}
	public String getOrderId() {
		return this.orderId;
	}
	public void setTid(String value) {
		this.tid = value;
	}
	public String getTid() {
		return this.tid;
	}
	public void setRbTime(String value) {
		this.rbTime = value;
	}
	public String getRbTime() {
		return this.rbTime;
	}
	public void setRbDealtime(String value) {
		this.rbDealtime = value;
	}
	public String getRbDealtime() {
		return this.rbDealtime;
	}
	public void setRbDealman(String value) {
		this.rbDealman = value;
	}
	public String getRbDealman() {
		return this.rbDealman;
	}
	public void setRbReason(String value) {
		this.rbReason = value;
	}
	public String getRbReason() {
		return this.rbReason;
	}
	public void setRbExplain(String value) {
		this.rbExplain = value;
	}
	public String getRbExplain() {
		return this.rbExplain;
	}
	public void setRbMoney(java.math.BigDecimal value) {
		this.rbMoney = value;
	}
	public java.math.BigDecimal getRbMoney() {
		return this.rbMoney;
	}
	public void setRbResult(String value) {
		this.rbResult = value;
	}
	public String getRbResult() {
		return this.rbResult;
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
	public void setRbSeriano(String value) {
		this.rbSeriano = value;
	}
	public String getRbSeriano() {
		return this.rbSeriano;
	}
	
	private TeaoFdOrder teaoFdOrder;
	
	private TeaoFdTrade teaoFdTrade;

	public TeaoFdOrder getTeaoFdOrder() {
		return teaoFdOrder;
	}

	public TeaoFdTrade getTeaoFdTrade() {
		return teaoFdTrade;
	}

	public void setTeaoFdOrder(TeaoFdOrder teaoFdOrder) {
		this.teaoFdOrder = teaoFdOrder;
	}

	public void setTeaoFdTrade(TeaoFdTrade teaoFdTrade) {
		this.teaoFdTrade = teaoFdTrade;
	}
	
}

