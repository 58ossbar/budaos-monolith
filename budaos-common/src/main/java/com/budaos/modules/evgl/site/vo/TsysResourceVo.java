package com.budaos.modules.evgl.site.vo;

import com.budaos.modules.sys.domain.TsysResource;

public class TsysResourceVo extends TsysResource {

	private static final long serialVersionUID = 1L;

	/**
	 * 该栏目下有多少条新闻咨询
	 */
	private Integer num;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
}
