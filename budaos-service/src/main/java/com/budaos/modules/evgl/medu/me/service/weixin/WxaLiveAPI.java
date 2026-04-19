package com.budaos.modules.evgl.medu.me.service.weixin;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import weixin.popular.api.API;
import weixin.popular.api.BaseAPI;
import weixin.popular.client.LocalHttpClient;

import java.nio.charset.Charset;

/**
 * 微信小程序直播API（从旧版 weixin-popular 源码迁移，weixin-popular 2.8+ 已移除该类）
 */
public class WxaLiveAPI extends BaseAPI {

    /**
     * 获取直播间列表
     * @param access_token 小程序 AccessToken
     * @param start 从第几条开始
     * @param limit 拉取多少条
     */
    public static CbBaseResult getliveinfo(String access_token, Integer start, Integer limit) {
        JSONObject json = new JSONObject();
        json.put("start", start != null ? start.intValue() : 0);
        json.put("limit", limit != null ? limit.intValue() : 10);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setUri("https://api.weixin.qq.com/wxa/business/getliveinfo")
                .addParameter("access_token", API.accessToken(access_token))
                .setEntity(new StringEntity(json.toJSONString(), Charset.forName("utf-8")))
                .build();
        return (CbBaseResult) LocalHttpClient.executeJsonResult(httpUriRequest, CbBaseResult.class);
    }
}
