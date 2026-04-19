package com.budaos.modules.evgl.activity.vo;

import com.budaos.modules.evgl.activity.domain.TevglActivityTaskScore;

import java.io.Serializable;
import java.util.List;

public class ActivityTaskScoreVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 课堂id
	 */
	private String ctId;
	
	/**
	 * 课堂对应的教学包id
	 */
	private String pkgId;
	
	/**
	 * 活动id
	 */
	private String activityId;
	
	/**
	 * 学员具体得分情况
	 */
	private List<TevglActivityTaskScore> scoreList;

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

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public List<TevglActivityTaskScore> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<TevglActivityTaskScore> scoreList) {
		this.scoreList = scoreList;
	}

	
	
}
