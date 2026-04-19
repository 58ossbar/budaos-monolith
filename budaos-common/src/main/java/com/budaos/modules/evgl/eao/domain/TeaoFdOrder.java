package com.budaos.modules.evgl.eao.domain;

import com.budaos.core.baseclass.domain.BaseDomain;
import com.budaos.modules.common.annotation.Excel;

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
public class TeaoFdOrder extends BaseDomain<TeaoFdOrder>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TeaoFdOrder";
	public static final String ALIAS_ORDER_ID = "订单标识ID";
	public static final String ALIAS_TRAINEE_ID = "学员标识ID";
	public static final String ALIAS_CLASS_ID = "班级标识ID";
	public static final String ALIAS_OF_SERIANO = "订单编号:订单标识码(6)+类型(1活动；2服务)+下单时间的UNIX时间戳后6位+用户ID后四位";
	public static final String ALIAS_OF_NAME = "订单名称";
	public static final String ALIAS_OF_TIME = "生成订单时间";
	public static final String ALIAS_OF_STATE = "订单状态:待支付已成交退款中已取消已关闭";
	public static final String ALIAS_OF_LINKMAN = "订单联系人";
	public static final String ALIAS_OF_TELPHONE = "订单联系电话";
	public static final String ALIAS_SOS_LINKMAN = "紧急联系人";
	public static final String ALIAS_SOS_TELPHONE = "紧急联系电话";
	public static final String ALIAS_ORDER_RECEIVABLE = "应收款，客户下单时订单应支付金额";
	public static final String ALIAS_ORDER_PROCEEDS = "实收款，当前订单实际收款金额";
	public static final String ALIAS_ORDER_REFUND = "退款金额";
	public static final String ALIAS_S_PAYMENT = "支付方式：预付全部费用预付定金";
	public static final String ALIAS_S_PAYMENTPER = "支付比例：预付定金时有效";
	public static final String ALIAS_S_REFUND = "是否可退款";
	

    /**
     * 订单标识ID       db_column: order_id 
     */	
	 private String orderId;
 	/**
     * 机构ID       db_column: org_id 
     */	
	 private String orgId;
    /**
     * 学员标识ID       db_column: trainee_id 
     */	
	 private String traineeId;
    /**
     * 班级标识ID       db_column: class_id 
     */	
	 private String classId;
    /**
     * 订单编号:订单标识码(6)+类型(1活动；2服务)+下单时间的UNIX时间戳后6位+用户ID后四位       db_column: of_seriano 
     */	
	 @Excel(name = "订单编号", sort = 1)
	 private String ofSeriano;
    /**
     * 订单名称       db_column: of_name 
     */	
	 @Excel(name = "班级名称", sort = 2)
	 private String ofName;
    /**
     * 生成订单时间       db_column: of_time 
     */	
	 private String ofTime;
    /**
     * 订单状态:待支付已成交退款中已取消已关闭       db_column: of_state 
     */	
	 private String ofState;
    /**
     * 订单联系人       db_column: of_linkman 
     */	
	 @Excel(name = "订单联系人", sort = 3)
	 private String ofLinkman;
    /**
     * 订单联系电话       db_column: of_telphone 
     */	
	 @Excel(name = "订单联系电话", sort = 4)
	 private String ofTelphone;
    /**
     * 紧急联系人       db_column: sos_linkman 
     */	
	 private String sosLinkman;
    /**
     * 紧急联系电话       db_column: sos_telphone 
     */	
	 private String sosTelphone;
    /**
     * 应收款，客户下单时订单应支付金额       db_column: order_receivable 
     */	
	 private java.math.BigDecimal orderReceivable;
    /**
     * 实收款，当前订单实际收款金额       db_column: order_proceeds 
     */	
	 private java.math.BigDecimal orderProceeds;
    /**
     * 退款金额       db_column: order_refund 
     */	
	 private java.math.BigDecimal orderRefund;
    /**
     * 支付方式：预付全部费用预付定金       db_column: s_payment 
     */	
	 private String spayment;
    /**
     * 支付比例：预付定金时有效       db_column: s_paymentper 
     */	
	 private Long spaymentper;
    /**
     * 是否可退款       db_column: s_refund 
     */	
	 private String srefund;
	 private String ofRefundState;//退款状态
	 private String ofApplyrefundDealtime;//退款处理时间
	 private String ofApplyrefundDealman;//退款处理人
	 private String ofApplyrefundReason;//申请退款原因
	 private String ofApplyrefundExplain;//退款处理说明
	 private String rbSeriano;//退款编号
	 private String ofApplyrefundTime;//退款申请时间
	 private String ispx;//是否参加培训
	 private String signupType;// 报名类型
	//columns END

	public String getSignupType() {
		return signupType;
	}

	public void setSignupType(String signupType) {
		this.signupType = signupType;
	}

	public TeaoFdOrder(){
	}

	public TeaoFdOrder(
		String orderId
	){
		this.orderId = orderId;
	}

	public void setOrderId(String value) {
		this.orderId = value;
	}
	public String getOrderId() {
		return this.orderId;
	}
	public void setTraineeId(String value) {
		this.traineeId = value;
	}
	public String getTraineeId() {
		return this.traineeId;
	}
	public void setClassId(String value) {
		this.classId = value;
	}
	public String getClassId() {
		return this.classId;
	}
	public void setOfSeriano(String value) {
		this.ofSeriano = value;
	}
	public String getOfSeriano() {
		return this.ofSeriano;
	}
	public void setOfName(String value) {
		this.ofName = value;
	}
	public String getOfName() {
		return this.ofName;
	}
	public void setOfTime(String value) {
		this.ofTime = value;
	}
	public String getOfTime() {
		return this.ofTime;
	}
	public void setOfState(String value) {
		this.ofState = value;
	}
	public String getOfState() {
		return this.ofState;
	}
	public void setOfLinkman(String value) {
		this.ofLinkman = value;
	}
	public String getOfLinkman() {
		return this.ofLinkman;
	}
	public void setOfTelphone(String value) {
		this.ofTelphone = value;
	}
	public String getOfTelphone() {
		return this.ofTelphone;
	}
	public void setSosLinkman(String value) {
		this.sosLinkman = value;
	}
	public String getSosLinkman() {
		return this.sosLinkman;
	}
	public void setSosTelphone(String value) {
		this.sosTelphone = value;
	}
	public String getSosTelphone() {
		return this.sosTelphone;
	}
	public void setOrderReceivable(java.math.BigDecimal value) {
		this.orderReceivable = value;
	}
	public java.math.BigDecimal getOrderReceivable() {
		return this.orderReceivable;
	}
	public void setOrderProceeds(java.math.BigDecimal value) {
		this.orderProceeds = value;
	}
	public java.math.BigDecimal getOrderProceeds() {
		return this.orderProceeds;
	}
	public void setOrderRefund(java.math.BigDecimal value) {
		this.orderRefund = value;
	}
	public java.math.BigDecimal getOrderRefund() {
		return this.orderRefund;
	}
	public void setSpayment(String value) {
		this.spayment = value;
	}
	public String getSpayment() {
		return this.spayment;
	}
	public void setSpaymentper(Long value) {
		this.spaymentper = value;
	}
	public Long getSpaymentper() {
		return this.spaymentper;
	}
	public void setSrefund(String value) {
		this.srefund = value;
	}
	public String getSrefund() {
		return this.srefund;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOfRefundState() {
		return ofRefundState;
	}

	public String getOfApplyrefundDealtime() {
		return ofApplyrefundDealtime;
	}

	public String getOfApplyrefundDealman() {
		return ofApplyrefundDealman;
	}

	public String getOfApplyrefundReason() {
		return ofApplyrefundReason;
	}

	public String getOfApplyrefundExplain() {
		return ofApplyrefundExplain;
	}

	public void setOfRefundState(String ofRefundState) {
		this.ofRefundState = ofRefundState;
	}

	public void setOfApplyrefundDealtime(String ofApplyrefundDealtime) {
		this.ofApplyrefundDealtime = ofApplyrefundDealtime;
	}

	public void setOfApplyrefundDealman(String ofApplyrefundDealman) {
		this.ofApplyrefundDealman = ofApplyrefundDealman;
	}

	public void setOfApplyrefundReason(String ofApplyrefundReason) {
		this.ofApplyrefundReason = ofApplyrefundReason;
	}

	public void setOfApplyrefundExplain(String ofApplyrefundExplain) {
		this.ofApplyrefundExplain = ofApplyrefundExplain;
	}

	public String getRbSeriano() {
		return rbSeriano;
	}

	public void setRbSeriano(String rbSeriano) {
		this.rbSeriano = rbSeriano;
	}

	public String getOfApplyrefundTime() {
		return ofApplyrefundTime;
	}

	public void setOfApplyrefundTime(String ofApplyrefundTime) {
		this.ofApplyrefundTime = ofApplyrefundTime;
	}

	public String getIspx() {
		return ispx;
	}

	public void setIspx(String ispx) {
		this.ispx = ispx;
	}

}

