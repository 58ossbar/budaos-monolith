package com.budaos.modules.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum WxPayEnum {

    PAY_TYPE_JSPAPI("微信支付","JSAPI"),
    PAY_TYPE_NATIVE("微信扫码支付","NATIVE"),
    REFUND_SUCCESS("退款成功","OK"),
    REFUND_OK("退款接口请求成功","SUCCESS"),
    BUSINESS_BALANCE_NOTENOUGH("商户可用余额不足","NOTENOUGH"),
    REFUND_ERROR("微信扫码支付","NATIVE");

    String name;
    String code;

    private static Map<String, WxPayEnum> valueMap = new HashMap<>();

    static {
        for(WxPayEnum gender : WxPayEnum.values()) {
            valueMap.put(gender.name, gender);
        }
    }

    WxPayEnum(String name, String code) {
        this.code = code;
        this.name=name;
    }

    public static String getByName(String name) {
        WxPayEnum result = valueMap.get(name);
        if(result == null) {
            throw new IllegalArgumentException("No element matches " + name);
        }
        return result.code;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
    
}
