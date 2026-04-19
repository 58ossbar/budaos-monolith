package com.budaos.modules.evgl.book.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 批量设置章节对学生不可见的时的实体类
 * @author huj
 * @create 2022-05-10 14:12
 * @email hujun@budaos.com
 */
public class SaveChapterVisibleVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ctId;

    private String pkgId;

    private List<String> chapterIds;

    public String getCtId() {
        return ctId;
    }

    public void setCtId(String ctId) {
        this.ctId = ctId;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public List<String> getChapterIds() {
        return chapterIds;
    }

    public void setChapterIds(List<String> chapterIds) {
        this.chapterIds = chapterIds;
    }
}
