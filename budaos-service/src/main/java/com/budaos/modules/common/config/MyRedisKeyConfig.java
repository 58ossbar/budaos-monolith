package com.budaos.modules.common.config;

public class MyRedisKeyConfig {
	//生成订单，退款单，支付单的编号的KEY前缀
	public static String SERAI_KEY = "api:seriano:";
	//微信accessToken
	public static String ACCESS_TOKEN = "api:accesstoken";
	//订单编号
	public static String ORDER_SERAINO = "orderinfo";
	//交易编号
	public static String PAY_SERAINO = "tradeinfo";
	//退款编号
	public static String REFUND_SERAINO = "refundinfo";
	//登陆错误次数控制
	public static String LOGIN_KEY = "login:errcount:";
	//同账号登陆次数控制
	public static String USER_LOGIN_KEY = "login:samecount:";
	//活动排期报名人数控制
	public static String ACTIVITY_SCHEDULE_KEY = "activity:schedule:";
}
