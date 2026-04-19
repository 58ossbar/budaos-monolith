package com.budaos.modules.evgl.book.domain;

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

public class TevglBookChapterVisible extends BaseDomain<TevglBookChapterVisible>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglBookChapterVisible";
	public static final String ALIAS_ID = "主键id";
	public static final String ALIAS_PKG_ID = "教学包id";
	public static final String ALIAS_CHAPTER_ID = "chapterId";
	public static final String ALIAS_IS_TRAINEES_VISIBLE = "是否可见(Y/N)";
	

    /**
     * 主键id       db_column: id 
     */	
 	@NotNull(msg="主键id不能为空")
 	@MaxLength(value=32, msg="字段[主键id]超出最大长度[32]")
	private java.lang.String id;
    /**
     * 教学包id       db_column: pkg_id 
     */	
 	@NotNull(msg="教学包id不能为空")
 	@MaxLength(value=32, msg="字段[教学包id]超出最大长度[32]")
	private java.lang.String pkgId;
    /**
     * chapterId       db_column: chapter_id 
     */	
 	@NotNull(msg="chapterId不能为空")
 	@MaxLength(value=32, msg="字段[chapterId]超出最大长度[32]")
	private java.lang.String chapterId;
    /**
     * 是否可见(Y/N)       db_column: is_trainees_visible 
     */	
 	@NotNull(msg="是否可见(Y/N)不能为空")
 	@MaxLength(value=2, msg="字段[是否可见(Y/N)]超出最大长度[2]")
	private java.lang.String isTraineesVisible;
	//columns END

	public TevglBookChapterVisible(){
	}

	public TevglBookChapterVisible(
		java.lang.String id
	){
		this.id = id;
	}

	public void setId(java.lang.String value) {
		this.id = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setPkgId(java.lang.String value) {
		this.pkgId = value;
	}
	public java.lang.String getPkgId() {
		return this.pkgId;
	}
	public void setChapterId(java.lang.String value) {
		this.chapterId = value;
	}
	public java.lang.String getChapterId() {
		return this.chapterId;
	}
	public void setIsTraineesVisible(java.lang.String value) {
		this.isTraineesVisible = value;
	}
	public java.lang.String getIsTraineesVisible() {
		return this.isTraineesVisible;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}

