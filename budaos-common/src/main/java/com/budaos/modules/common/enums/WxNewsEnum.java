package com.budaos.modules.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 从微信公众号点击菜单，携带的参数。
 * 比如在公众号那里点击了【招生简章】，会携带参数?officialLinkType=1
 * 服务根据该参数查询对应数据
 * @author huj
 * @create 2022-01-25 9:04
 * @email hujun@budaos.com
 */
public enum WxNewsEnum {

    OFFICIAL_LINK_TYPE_1("1", "招生简章"),
    OFFICIAL_LINK_TYPE_2("2", "视频介绍"),
    OFFICIAL_LINK_TYPE_3("3", "教师团队"),
    OFFICIAL_LINK_TYPE_4("4", "优秀学员"),
    OFFICIAL_LINK_TYPE_5("5", "就业观园");

    String code;
    String name;

    private static Map<String, WxNewsEnum> valueMap = new HashMap<>();

    static {
        for(WxNewsEnum ob : WxNewsEnum.values()) {
            valueMap.put(ob.code, ob);
        }
    }

    WxNewsEnum(String code, String name) {
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
