package com.budaos.modules.evgl.activity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 作答时提交的数据
 * @author huj
 * @create 2022-05-30 11:10
 * @email hujun@budaos.com
 */
public class CommitTaskAnswerVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 课堂id
	 */
	private String ctId;
	
	/**
	 * 活动id
	 */
	private String activityId;
	
	/**
	 * 回答的人（当前登录用户）
	 */
	private String traineeId;
	
	/**
	 * 学生回答的内容
	 */
	private String content;
	
	/**
	 * 提交的附件
	 */
	private List<String> fileIdList;

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

	public String getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(String traineeId) {
		this.traineeId = traineeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getFileIdList() {
		return fileIdList;
	}

	public void setFileIdList(List<String> fileIdList) {
		this.fileIdList = fileIdList;
	}

	@Override
	public String toString() {
		return "CommitTaskAnswerVo [ctId=" + ctId + ", activityId=" + activityId + ", traineeId=" + traineeId
				+ ", content=" + content + ", fileIdList=" + fileIdList + "]";
	}

}
