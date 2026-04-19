package com.budaos.modules.evgl.question.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: 动态试卷表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TepExamineDynamicPaper extends BaseDomain<TepExamineDynamicPaper>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TepExamineDynamicPaper";
	public static final String ALIAS_DY_ID = "dy_id(主键ID)";
	public static final String ALIAS_PAPER_ID = "试卷ID";
	public static final String ALIAS_TRAINEE_ID = "学员ID";
	public static final String ALIAS_DYNAMIC_TYPE = "动态类型(Y动态N非动态)";
	public static final String ALIAS_DYNAMIC_STATE = "试卷类型(1试卷2自测3考核)";
	public static final String ALIAS_PAPER_IS_FINISHED = "试卷是否完成（Y/N）";
	public static final String ALIAS_CT_ID = "课堂id";
	public static final String ALIAS_PKG_ID = "课堂对应的教学包id";
	

    /**
     * dy_id(主键ID)       db_column: dy_id 
     */	
 	//@NotNull(msg="dy_id(主键ID)不能为空")
 	@MaxLength(value=32, msg="字段[dy_id(主键ID)]超出最大长度[32]")
	private java.lang.String dyId;
    /**
     * 试卷ID       db_column: paper_id 
     */	
 	//@NotNull(msg="试卷ID不能为空")
 	@MaxLength(value=32, msg="字段[试卷ID]超出最大长度[32]")
	private java.lang.String paperId;
    /**
     * 学员ID       db_column: trainee_id 
     */	
 	//@NotNull(msg="学员ID不能为空")
 	@MaxLength(value=32, msg="字段[学员ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 动态类型(Y动态N非动态)       db_column: dynamic_type 
     */	
 	//@NotNull(msg="动态类型(Y动态N非动态)不能为空")
 	@MaxLength(value=2, msg="字段[动态类型(Y动态N非动态)]超出最大长度[2]")
	private java.lang.String dynamicType;
 	/**
     * 试卷类型(1试卷2自测3考核)       db_column: dynamic_state 
     */	
 	//@NotNull(msg="试卷类型(1试卷2自测3考核)不能为空")
 	@MaxLength(value=5, msg="字段[试卷类型(1试卷2自测3考核)]超出最大长度[5]")
	private java.lang.String dynamicState;
    /**
     * 试卷是否完成（Y/N）       db_column: paper_is_finished 
     */	
 	//@NotNull(msg="试卷是否完成（Y/N）不能为空")
 	@MaxLength(value=5, msg="字段[试卷是否完成（Y/N）]超出最大长度[5]")
	private java.lang.String paperIsFinished;
    /**
     * 课堂id       db_column: ct_id 
     */	
 	//@NotNull(msg="课堂id不能为空")
 	@MaxLength(value=0, msg="字段[课堂id]超出最大长度[0]")
	private java.lang.String ctId;
    /**
     * 课堂对应的教学包id       db_column: pkg_id 
     */	
 	//@NotNull(msg="课堂对应的教学包id不能为空")
 	@MaxLength(value=0, msg="字段[课堂对应的教学包id]超出最大长度[0]")
	private java.lang.String pkgId;
 	
	//columns END


	public String getDynamicState() {
		return dynamicState;
	}

	public String getPaperIsFinished() {
		return paperIsFinished;
	}

	public void setPaperIsFinished(String paperIsFinished) {
		this.paperIsFinished = paperIsFinished;
	}

	public void setDynamicState(String dynamicState) {
		this.dynamicState = dynamicState;
	}

	public TepExamineDynamicPaper(){
	}

	public TepExamineDynamicPaper(
		java.lang.String dyId
	){
		this.dyId = dyId;
	}

	public void setDyId(java.lang.String value) {
		this.dyId = value;
	}
	public java.lang.String getDyId() {
		return this.dyId;
	}
	public void setPaperId(java.lang.String value) {
		this.paperId = value;
	}
	public java.lang.String getPaperId() {
		return this.paperId;
	}
	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setDynamicType(java.lang.String value) {
		this.dynamicType = value;
	}
	public java.lang.String getDynamicType() {
		return this.dynamicType;
	}
	
	public java.lang.String getCtId() {
		return ctId;
	}

	public void setCtId(java.lang.String ctId) {
		this.ctId = ctId;
	}

	public java.lang.String getPkgId() {
		return pkgId;
	}

	public void setPkgId(java.lang.String pkgId) {
		this.pkgId = pkgId;
	}

	private TepExaminePaperInfo tepExaminePaperInfo;
	
	public void setTepExaminePaperInfo(TepExaminePaperInfo tepExaminePaperInfo){
		this.tepExaminePaperInfo = tepExaminePaperInfo;
	}
	
	public TepExaminePaperInfo getTepExaminePaperInfo() {
		return tepExaminePaperInfo;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

