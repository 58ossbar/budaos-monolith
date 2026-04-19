package com.budaos.modules.evgl.book.vo;

import java.io.Serializable;

/**
 * 用于回显下拉控件等，显示简易数据
 * @author huj
 *
 */
public class TevglBookSubjectSelectVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subjectId;
	
	private String subjectName;
	
	private String subjectLogo;

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectLogo() {
		return subjectLogo;
	}

	public void setSubjectLogo(String subjectLogo) {
		this.subjectLogo = subjectLogo;
	}
	
	
}
