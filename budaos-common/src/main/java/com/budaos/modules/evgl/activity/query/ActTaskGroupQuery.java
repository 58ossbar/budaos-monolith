package com.budaos.modules.evgl.activity.query;

import com.budaos.core.baseclass.annotation.validator.NotNull;

import java.io.Serializable;

/**
 * 作业/小组任务 查询参数 类
 * @author huj
 * @create 2022-05-31 9:57
 * @email hujun@budaos.com
 */
public class ActTaskGroupQuery implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 课堂id
     */
    @NotNull(msg = "参数课堂id不能为空")
    private String ctId;

    /**
     * 活动id
     */
    @NotNull(msg = "参数活动id不能为空")
    private String activityId;

    /**
     * 姓名
     */
    private String traineeName;

    /**
     * 手机号码
     */
    private String mobile;

    private String groupId;

    public String getCtId() {
        return ctId;
    }

    public void setCtId(String ctId) {
        this.ctId = ctId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "ActTaskGroupQuery{" +
                "ctId='" + ctId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", traineeName='" + traineeName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
