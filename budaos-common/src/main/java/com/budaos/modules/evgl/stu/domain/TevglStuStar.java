package com.budaos.modules.evgl.stu.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */
 
public class TevglStuStar extends BaseDomain<TevglStuStar>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglStuStar";
	public static final String ALIAS_STAR_ID = "主键ID";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_STAR_PIC = "明星头像";
	public static final String ALIAS_STAR_NAME = "姓名";
	public static final String ALIAS_STAR_SALARY = "工资:K为单位";
	public static final String ALIAS_WORK_AGE = "工作年限";
	public static final String ALIAS_WORK_PROVICE = "工作省份";
	public static final String ALIAS_WORK_CORP = "工作单位";
	public static final String ALIAS_TRAINEE_STORY = "学员故事";
	public static final String ALIAS_TRAINEE_STORY_INFO = "学员故事详情";
	public static final String ALIAS_TRAINEE_STORY_TITLE = "学员故事标题";
	public static final String ALIAS_STATE = "首页显示标识(Y显示N不显示)";
	

    /**
     * 主键ID       db_column: star_id 
     */	
 	@NotNull(msg="主键ID不能为空")
 	@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String starId;
    /**
     * 机构ID       db_column: ORG_ID 
     */	
 	//@NotNull(msg="机构ID不能为空")
 	@MaxLength(value=32, msg="字段[机构ID]超出最大长度[32]")
	private java.lang.String orgId;
    /**
     * 明星头像       db_column: star_pic 
     */	
 	//@NotNull(msg="明星头像不能为空")
 	@MaxLength(value=300, msg="字段[明星头像]超出最大长度[300]")
	private java.lang.String starPic;
    /**
     * 姓名       db_column: star_name 
     */	
 	@NotNull(msg="姓名不能为空")
 	@MaxLength(value=50, msg="字段[姓名]超出最大长度[50]")
	private java.lang.String starName;
    /**
     * 工资:K为单位       db_column: star_salary 
     */	
 	//@NotNull(msg="工资:K为单位不能为空")
 	@MaxLength(value=10, msg="字段[工资:K为单位]超出最大长度[10]")
	private java.lang.String starSalary;
    /**
     * 工作年限       db_column: work_age 
     */	
 	//@NotNull(msg="工作年限不能为空")
 	@MaxLength(value=10, msg="字段[工作年限]超出最大长度[10]")
	private java.lang.Integer workAge;
    /**
     * 工作省份       db_column: work_provice 
     */	
 	//@NotNull(msg="工作省份不能为空")
 	@MaxLength(value=50, msg="字段[工作省份]超出最大长度[50]")
	private java.lang.String workProvice;
    /**
     * 工作单位       db_column: work_corp 
     */	
 	//@NotNull(msg="工作单位不能为空")
 	@MaxLength(value=100, msg="字段[工作单位]超出最大长度[100]")
	private java.lang.String workCorp;
    /**
     * 学员故事       db_column: trainee_story 
     */	
 	//@NotNull(msg="学员故事不能为空")
 	@MaxLength(value=2147483647, msg="字段[学员故事]超出最大长度[2147483647]")
	private java.lang.String traineeStory;
    /**
     * 学员故事详情       db_column: trainee_story_info 
     */	
 	//@NotNull(msg="学员故事详情不能为空")
 	@MaxLength(value=2147483647, msg="字段[学员故事详情]超出最大长度[2147483647]")
	private java.lang.String traineeStoryInfo;
    /**
     * 学员故事标题       db_column: trainee_story_title 
     */	
 	//@NotNull(msg="学员故事标题不能为空")
 	@MaxLength(value=200, msg="字段[学员故事标题]超出最大长度[200]")
	private java.lang.String traineeStoryTitle;
    /**
     * 首页显示标识(Y显示N不显示)       db_column: state 
     */	
 	//@NotNull(msg="首页显示标识(Y显示N不显示)不能为空")
 	@MaxLength(value=2, msg="字段[首页显示标识(Y显示N不显示)]超出最大长度[2]")
	private java.lang.String state;
	//columns END

	public TevglStuStar(){
	}

	public TevglStuStar(
		java.lang.String starId
	){
		this.starId = starId;
	}

	public void setStarId(java.lang.String value) {
		this.starId = value;
	}
	public java.lang.String getStarId() {
		return this.starId;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setStarPic(java.lang.String value) {
		this.starPic = value;
	}
	public java.lang.String getStarPic() {
		return this.starPic;
	}
	public void setStarName(java.lang.String value) {
		this.starName = value;
	}
	public java.lang.String getStarName() {
		return this.starName;
	}
	public void setStarSalary(java.lang.String value) {
		this.starSalary = value;
	}
	public java.lang.String getStarSalary() {
		return this.starSalary;
	}
	public void setWorkAge(java.lang.Integer value) {
		this.workAge = value;
	}
	public java.lang.Integer getWorkAge() {
		return this.workAge;
	}
	public void setWorkProvice(java.lang.String value) {
		this.workProvice = value;
	}
	public java.lang.String getWorkProvice() {
		return this.workProvice;
	}
	public void setWorkCorp(java.lang.String value) {
		this.workCorp = value;
	}
	public java.lang.String getWorkCorp() {
		return this.workCorp;
	}
	public void setTraineeStory(java.lang.String value) {
		this.traineeStory = value;
	}
	public java.lang.String getTraineeStory() {
		return this.traineeStory;
	}
	public void setTraineeStoryInfo(java.lang.String value) {
		this.traineeStoryInfo = value;
	}
	public java.lang.String getTraineeStoryInfo() {
		return this.traineeStoryInfo;
	}
	public void setTraineeStoryTitle(java.lang.String value) {
		this.traineeStoryTitle = value;
	}
	public java.lang.String getTraineeStoryTitle() {
		return this.traineeStoryTitle;
	}
	public void setState(java.lang.String value) {
		this.state = value;
	}
	public java.lang.String getState() {
		return this.state;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

