package com.budaos.modules.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 活动状态枚举类
 * @author huj
 * @create 2021-12-28 11:26
 * @email 1552281464@qq.com
 */
public enum ActivityStateEnum {

    ACTIVITY_STATE_0_NOT_START("0", "未开始"),
    ACTIVITY_STATE_1_UNDER_WAY("1", "进行中"),
    ACTIVITY_STATE_2_FINISHED("2", "已结束");

    String code;
    String name;

    private static Map<String, ActivityStateEnum> valueMap = new HashMap<>();

    static {
        for(ActivityStateEnum ob : ActivityStateEnum.values()) {
            valueMap.put(ob.code, ob);
        }
    }

    ActivityStateEnum(String code, String name) {
        this.name=name;
        this.code = code;
    }
    
    public String getCode() {
		return code;
	}
    
    public String getName() {
		return name;
	}
}
