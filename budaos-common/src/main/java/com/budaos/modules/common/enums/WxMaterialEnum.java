package com.budaos.modules.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 素材的类型
 * @author huj
 * @create 2021-12-30 10:13
 * @email 1552281464@qq.com
 */
public enum WxMaterialEnum {

    TYPE_IMAGE("image", "图片"),
    TYPE_VIDEO("video", "视频"),
    TYPE_VOICE("voice", "语音"),
    TYPE_NEWS("news", "图文"),
    TYPE_THUMB("thumb", "缩略图");

    String code;
    String name;

    private static Map<String, WxMaterialEnum> valueMap = new HashMap<>();

    static {
        for(WxMaterialEnum ob : WxMaterialEnum.values()) {
            valueMap.put(ob.code, ob);
        }
    }

    WxMaterialEnum(String code, String name) {
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
