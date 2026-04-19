package com.budaos.modules.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 面向前端的消息类型 枚举类
 * @author huj
 * @create 2022-06-20 16:08
 * @email hujun@budaos.com
 */
public enum SiteSysMsgTypeEnum {

    MST_TYPE_0("0", "通用纯文本消息，不牵扯任何模块"),
    MST_TYPE_1("1", "投票问卷"),
    MST_TYPE_2("2", "头脑风暴"),
    MST_TYPE_3("3", "答疑讨论"),
    MST_TYPE_4("4", "测试活动"),
    MST_TYPE_5("5", "作业/小组任务"),
    MST_TYPE_6("6", "课堂表现"),
    MST_TYPE_7("7", "轻直播"),
    MST_TYPE_8("8", "签到"),
    MST_TYPE_9("9", "实践考核"),
    /** 教学包移交时的提示信息 */
    MST_TYPE_110_PKG_TRANSFER("9", "教学包移交"),

    ;

    /**
     * 业务处理时，使用code字段来处理
     */
    String code;

    /**
     * 无其他含义，类似于注释作用
     */
    String name;

    private static Map<String, SiteSysMsgTypeEnum> valueMap = new HashMap<>();

    static {
        for(SiteSysMsgTypeEnum ob : SiteSysMsgTypeEnum.values()) {
            valueMap.put(ob.code, ob);
        }
    }

    SiteSysMsgTypeEnum(String code, String name) {
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
