package com.budaos.modules.evgl.pkg.vo;

import java.io.Serializable;

/**
 * @author huj
 * @create 2022-10-12 10:53
 * @email hujun@budaos.com
 */
public class ResVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String resId;

    private String rvId;

    private String pkg_id;

    private String pkgId;

    private String resContent;

    private String resgroupId;

    private String chapterId;

    private String cpId;

    private String resgroupName;

    private Integer sortNum;

    private String dictCode;

    private String isTraineesVisible;

    private String isCanCopy;

    public ResVO() {
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getRvId() {
        return rvId;
    }

    public void setRvId(String rvId) {
        this.rvId = rvId;
    }

    public String getPkg_id() {
        return pkg_id;
    }

    public void setPkg_id(String pkg_id) {
        this.pkg_id = pkg_id;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public String getResContent() {
        return resContent;
    }

    public void setResContent(String resContent) {
        this.resContent = resContent;
    }

    public String getResgroupId() {
        return resgroupId;
    }

    public void setResgroupId(String resgroupId) {
        this.resgroupId = resgroupId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getResgroupName() {
        return resgroupName;
    }

    public void setResgroupName(String resgroupName) {
        this.resgroupName = resgroupName;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getIsTraineesVisible() {
        return isTraineesVisible;
    }

    public void setIsTraineesVisible(String isTraineesVisible) {
        this.isTraineesVisible = isTraineesVisible;
    }

    public String getIsCanCopy() {
        return isCanCopy;
    }

    public void setIsCanCopy(String isCanCopy) {
        this.isCanCopy = isCanCopy;
    }

    @Override
    public String toString() {
        return "ResVO{" +
                "resId='" + resId + '\'' +
                ", rvId='" + rvId + '\'' +
                ", pkg_id='" + pkg_id + '\'' +
                ", pkgId='" + pkgId + '\'' +
                ", resContent='" + resContent + '\'' +
                ", resgroupId='" + resgroupId + '\'' +
                ", chapterId='" + chapterId + '\'' +
                ", cpId='" + cpId + '\'' +
                ", resgroupName='" + resgroupName + '\'' +
                ", sortNum=" + sortNum +
                ", dictCode='" + dictCode + '\'' +
                ", isTraineesVisible='" + isTraineesVisible + '\'' +
                ", isCanCopy='" + isCanCopy + '\'' +
                '}';
    }
}
