package com.budaos.modules.evgl.activity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author huj
 * @create 2022-05-31 10:10
 * @email hujun@budaos.com
 */
public class ActTraineeAnswerVo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 学员id
     */
    private String traineeId;

    /**
     * 姓名/昵称
     */
    private String traineeName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 头像
     */
    private String traineeHead;

    /**
     * 学号
     */
    private String jobNumber;

    /**
     * 小组成员id
     */
    private String memberId;

    /**
     * 课堂id
     */
    private String ctId;

    /**
     * 课堂名称
     */
    private String ctName;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 活动名称
     */
    private String activityTitle;

    /**
     * 小组id
     */
    private String groupId;

    /**
     * 小组名称
     */
    private String groupName;

    /**
     * 学员简答内容
     */
    private String content;

    /**
     * 学员最终得分
     */
    private String finalScore;

    /**
     * 学员提交的附件
     */
    private List<ActTaskTraineeAnswerFileVo> fileList;

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTraineeHead() {
        return traineeHead;
    }

    public void setTraineeHead(String traineeHead) {
        this.traineeHead = traineeHead;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCtId() {
        return ctId;
    }

    public void setCtId(String ctId) {
        this.ctId = ctId;
    }

    public String getCtName() {
        return ctName;
    }

    public void setCtName(String ctName) {
        this.ctName = ctName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public List<ActTaskTraineeAnswerFileVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<ActTaskTraineeAnswerFileVo> fileList) {
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return "TraineeAnswerList{" +
                "traineeId='" + traineeId + '\'' +
                ", traineeName='" + traineeName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", traineeHead='" + traineeHead + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", memberId='" + memberId + '\'' +
                ", ctId='" + ctId + '\'' +
                ", ctName='" + ctName + '\'' +
                ", activityId='" + activityId + '\'' +
                ", activityTitle='" + activityTitle + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", content='" + content + '\'' +
                ", finalScore='" + finalScore + '\'' +
                ", fileList=" + fileList +
                '}';
    }
}
