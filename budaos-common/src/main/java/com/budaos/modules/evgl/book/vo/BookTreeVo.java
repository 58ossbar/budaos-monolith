package com.budaos.modules.evgl.book.vo;

import java.io.Serializable;

/**
 * @author huj
 * @create 2021-12-01 16:47
 * @email 1552281464@qq.com
 */
public class BookTreeVo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subjectId;

    private String id;

    private String parentId;

    private String name;

    private String parenetName;

    private Integer level;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParenetName() {
        return parenetName;
    }

    public void setParenetName(String parenetName) {
        this.parenetName = parenetName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "BookTreeVo{" +
                "subjectId='" + subjectId + '\'' +
                ", id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", parenetName='" + parenetName + '\'' +
                ", level=" + level +
                '}';
    }
}
