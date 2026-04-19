package com.budaos.modules.evgl.weixin.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 微信素材库的图片的vo
 * @author huj
 * @create 2021-12-30 8:49
 * @email 1552281464@qq.com
 */
public class MaterialVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 示例：NLqoRU2OWvmZ1GD6Ds-eXgulfyjlnc0-rAk_ShTYDzE
     */
    private String media_id;

    /**
     * 名称，示例：01cfefec285b4ce4a7477a53a31fecff.jpg
     */
    private String name;

    /**
     * 时间，示例：1640766539
     */
    private Long updateTime;
    private String updateTimeStr;

    /**
     * 图片地址，示例：https://mmbiz.qpic.cn/mmbiz_jpg/pkf7iaN3tHMFbibdxv0qKaMKCK2rmj8qfF7vOia8KFlEzjT1QSNRkHRH0PSTGiap8aJfrhzodJAbFJIvRmiczCY1TlA/0?wx_fmt=jpeg
     */
    private String url;

    /**
     * 视频封面图
     */
    private String cover_url;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * newcat
     */
    private String newcat;
    
    /**
     * newsubcat
     */
    private String newsubcat;
    
    private List<String> tags;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public String getCover_url() {
		return cover_url;
	}

	public void setCover_url(String cover_url) {
		this.cover_url = cover_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNewcat() {
		return newcat;
	}

	public void setNewcat(String newcat) {
		this.newcat = newcat;
	}

	public String getNewsubcat() {
		return newsubcat;
	}

	public void setNewsubcat(String newsubcat) {
		this.newsubcat = newsubcat;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

}
