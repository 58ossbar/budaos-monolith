package com.budaos.modules.evgl.weixin.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author huj
 * @create 2022-01-14 10:27
 * @email hujun@budaos.com
 */
public class MaterialResponseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int total_count;

    private int item_count;

    private List<MaterialVo> item;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public List<MaterialVo> getItem() {
        return item;
    }

    public void setItem(List<MaterialVo> item) {
        this.item = item;
    }
}
