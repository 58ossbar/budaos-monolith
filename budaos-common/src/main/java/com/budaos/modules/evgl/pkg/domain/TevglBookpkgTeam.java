package com.budaos.modules.evgl.pkg.domain;

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

public class TevglBookpkgTeam extends BaseDomain<TevglBookpkgTeam>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglBookpkgTeam";
	public static final String ALIAS_TEAM_ID = "资源共建主键ID";
	public static final String ALIAS_RES_TYPE = "资源类型(课程教材or教学包)";
	public static final String ALIAS_SUBJECT_ID = "课程教材ID";
	public static final String ALIAS_PGK_ID = "教学包ID";
	public static final String ALIAS_USER_ID = "共建人";
	public static final String ALIAS_IS_SUBEDITOR = "是否为副主编(Y/N)";
	

    /**
     * 资源共建主键ID       db_column: team_id 
     */	
 	@NotNull(msg="资源共建主键ID不能为空")
 	@MaxLength(value=32, msg="字段[资源共建主键ID]超出最大长度[32]")
	private java.lang.String teamId;
    /**
     * 资源类型(课程教材or教学包)       db_column: res_type 
     */	
 	@NotNull(msg="资源类型(课程教材or教学包)不能为空")
 	@MaxLength(value=32, msg="字段[资源类型(课程教材or教学包)]超出最大长度[32]")
	private java.lang.String resType;
    /**
     * 课程教材ID       db_column: subject_id 
     */	
 	@NotNull(msg="课程教材ID不能为空")
 	@MaxLength(value=32, msg="字段[课程教材ID]超出最大长度[32]")
	private java.lang.String subjectId;
    /**
     * 教学包ID       db_column: pgk_id 
     */	
 	@NotNull(msg="教学包ID不能为空")
 	@MaxLength(value=32, msg="字段[教学包ID]超出最大长度[32]")
	private java.lang.String pgkId;
    /**
     * 共建人       db_column: user_id 
     */	
 	@NotNull(msg="共建人不能为空")
 	@MaxLength(value=32, msg="字段[共建人]超出最大长度[32]")
	private java.lang.String userId;
    /**
     * 是否为副主编(Y/N)       db_column: is_subeditor 
     */	
 	@NotNull(msg="是否为副主编(Y/N)不能为空")
 	@MaxLength(value=2, msg="字段[是否为副主编(Y/N)]超出最大长度[2]")
	private java.lang.String isSubeditor;
	//columns END

	public TevglBookpkgTeam(){
	}

	public TevglBookpkgTeam(
		java.lang.String teamId
	){
		this.teamId = teamId;
	}

	public void setTeamId(java.lang.String value) {
		this.teamId = value;
	}
	public java.lang.String getTeamId() {
		return this.teamId;
	}
	public void setResType(java.lang.String value) {
		this.resType = value;
	}
	public java.lang.String getResType() {
		return this.resType;
	}
	public void setSubjectId(java.lang.String value) {
		this.subjectId = value;
	}
	public java.lang.String getSubjectId() {
		return this.subjectId;
	}
	public void setPgkId(java.lang.String value) {
		this.pgkId = value;
	}
	public java.lang.String getPgkId() {
		return this.pgkId;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setIsSubeditor(java.lang.String value) {
		this.isSubeditor = value;
	}
	public java.lang.String getIsSubeditor() {
		return this.isSubeditor;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

