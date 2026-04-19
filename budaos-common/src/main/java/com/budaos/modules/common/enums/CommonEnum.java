package com.budaos.modules.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用枚举类
 * @author huj
 * @create 2022-04-21 14:12
 * @email hujun@budaos.com
 */
public enum CommonEnum {

    STATE_YES("Y", "含义：成立、真、是、等等"),
    STATE_NO("N", ""),

    CHAPTER_LEVEL_LIMIT("3", "限制章节只能有多少级"),

    ;

    String code;
    String name;

    private static Map<String, CommonEnum> valueMap = new HashMap<>();

    static {
        for(CommonEnum ob : CommonEnum.values()) {
            valueMap.put(ob.code, ob);
        }
    }

    CommonEnum(String code, String name) {
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
