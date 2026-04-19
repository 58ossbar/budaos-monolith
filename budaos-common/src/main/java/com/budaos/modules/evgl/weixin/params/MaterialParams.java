package com.budaos.modules.evgl.weixin.params;

import com.budaos.core.baseclass.annotation.validator.NotNull;

import java.io.Serializable;

/**
 * 获取素材列表的查询条件
 * @author huj
 * @create 2021-12-30 10:00
 * @email 1552281464@qq.com
 * @apiNote 详见官方文档 https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Get_materials_list.html
 */
public class MaterialParams implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     */
	@NotNull
    private String type;

    /**
     * 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     */
    private Integer offset;

    /**
     * 返回素材的数量，取值在1到20之间
     */
    private Integer count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

	@Override
	public String toString() {
		return "MaterialParams [type=" + type + ", offset=" + offset + ", count=" + count + "]";
	}
}
