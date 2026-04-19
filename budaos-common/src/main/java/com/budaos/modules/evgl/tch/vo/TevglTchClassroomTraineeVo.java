package com.budaos.modules.evgl.tch.vo;

import java.io.Serializable;

/**
 * 课堂成员信息
 * @author huj
 * @create 2022-04-22 9:10
 * @email hujun@budaos.com
 */
public class TevglTchClassroomTraineeVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	/**
     * 课堂id
     */
    private String ctId;

    /**
     * 学员id
     */
    private String traineeId;

    /**
     * 所在班级id
     */
    private String classId;

    /**
     * 姓名
     */
    private String traineeName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String traineePic;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 加入课堂日期 yyyy-MM-dd
     */
    private String joinDate;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 状态(Y有效N无效)
     */
    private String state;
    private String stateName;

    /**
     * 身份，如助教
     */
    private String identity;

    /**
     * 学号
     */
    private String jobNumber;

    /**
     * 性别
     */
    private String traineeSex;
    private String traineeSexName;

    /**
     * 当值为N时,且如果课堂处于已结束状态，那么表示不再允许进入课堂，需要联系管理员
     */
    private String accessState;

    public String getCtId() {
        return ctId;
    }

    public void setCtId(String ctId) {
        this.ctId = ctId;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTraineePic() {
        return traineePic;
    }

    public void setTraineePic(String traineePic) {
        this.traineePic = traineePic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getTraineeSex() {
        return traineeSex;
    }

    public void setTraineeSex(String traineeSex) {
        this.traineeSex = traineeSex;
    }

    public String getTraineeSexName() {
        return traineeSexName;
    }

    public void setTraineeSexName(String traineeSexName) {
        this.traineeSexName = traineeSexName;
    }

    public String getAccessState() {
        return accessState;
    }

    public void setAccessState(String accessState) {
        this.accessState = accessState;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
