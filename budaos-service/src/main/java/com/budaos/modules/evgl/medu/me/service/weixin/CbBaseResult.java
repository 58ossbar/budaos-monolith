package com.budaos.modules.evgl.medu.me.service.weixin;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信直播API返回结果（从旧版 weixin-popular 源码迁移，weixin-popular 2.8+ 已移除该类）
 */
public class CbBaseResult extends JSONObject {
    private static final long serialVersionUID = -3563449037179472726L;

    public boolean isSuccess() {
        Object object = get("errcode");
        return (object != null && Integer.parseInt(object.toString()) == 0);
    }
}
