package com.budaos.modules.evgl.weixin.vo;

import java.io.Serializable;

/**
 * @author huj
 * @create 2022-01-18 16:57
 * @email hujun@budaos.com
 * @apiNote 详见官方文档 https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Adding_Permanent_Assets.html
 */
public class AddMaterialResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增的永久素材的media_id
     */
    private String media_id;

    /**
     * 新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
     */
    private String url;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AddMaterialResponseVo{" +
                "media_id='" + media_id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
