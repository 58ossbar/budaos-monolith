package com.budaos.modules.evgl.book.vo;

import java.io.Serializable;

/**
 * @author huj
 * @create 2022-04-27 17:01
 * @email hujun@budaos.com
 */
public class MyBookVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 课堂id */
    private String ctId;

    /** 教学包id */
    private String pkgId;

    /**
     * 来源教学包id
     */
    private String refPkgId;

    /** 课堂名称 */
    private String roomName;

    /**
     * 类型：room、collec
     */
    private String type;

    /**
     * 课程id
     */
    private String subjectId;

    /**
     * 来源课程id
     */
    private String subjectRef;

    /**
     * 课程名称
     */
    private String subjectName;

    /**
     * 作者
     */
    private String subjectAuthor;

    /**
     * 发布时间
     */
    private String deployTime;

    /**
     * 教学包冯妙
     */
    private String pkgLogo;

    /**
     * 教学包限制
     */
    private String pkgLimit;

    /**
     * 创建者
     */
    private String createUserId;

    /**
     * 浏览次数
     */
    private Integer viewNum;

    /**
     * 课堂状态
     */
    private String classroomState;

    /**
     * 
     */
    private String accessState;

    /**
     * 标识为是否为免费的教材
     */
    private Boolean ifFree;

    private Integer totalViewNum;

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

    public String getRefPkgId() {
        return refPkgId;
    }

    public void setRefPkgId(String refPkgId) {
        this.refPkgId = refPkgId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectRef() {
        return subjectRef;
    }

    public void setSubjectRef(String subjectRef) {
        this.subjectRef = subjectRef;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectAuthor() {
        return subjectAuthor;
    }

    public void setSubjectAuthor(String subjectAuthor) {
        this.subjectAuthor = subjectAuthor;
    }

    public String getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(String deployTime) {
        this.deployTime = deployTime;
    }

    public String getPkgLogo() {
        return pkgLogo;
    }

    public void setPkgLogo(String pkgLogo) {
        this.pkgLogo = pkgLogo;
    }

    public String getPkgLimit() {
        return pkgLimit;
    }

    public void setPkgLimit(String pkgLimit) {
        this.pkgLimit = pkgLimit;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public String getClassroomState() {
        return classroomState;
    }

    public void setClassroomState(String classroomState) {
        this.classroomState = classroomState;
    }

    public String getAccessState() {
        return accessState;
    }

    public void setAccessState(String accessState) {
        this.accessState = accessState;
    }

    public Boolean getIfFree() {
        return ifFree;
    }

    public void setIfFree(Boolean ifFree) {
        this.ifFree = ifFree;
    }

    public Integer getTotalViewNum() {
        return totalViewNum;
    }

    public void setTotalViewNum(Integer totalViewNum) {
        this.totalViewNum = totalViewNum;
    }
}
