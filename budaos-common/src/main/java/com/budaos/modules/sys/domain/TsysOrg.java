/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.ArrayList;
import java.util.List;

public class TsysOrg extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;

	// alias
	public static final String TABLE_ALIAS = "TsysOrg";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_ORG_SN = "机构排序ID";
	public static final String ALIAS_ORG_NAME = "机构名称";
	public static final String ALIAS_ORG_CODE = "机构编号";
	public static final String ALIAS_ORG_XZQM = "行政区码";
	public static final String ALIAS_ORG_SHOWNAME = "机构显示名称";
	public static final String ALIAS_PARENT_ID = "父机构ID";
	public static final String ALIAS_LAYER = "层（阶次）";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_CREATOR = "创建人";
	public static final String ALIAS_REMARK = "单位简介";
	public static final String ALIAS_ORG_TYPE = "机构类型:1、部门 2、公司";
	public static final String ALIAS_ADDR = "通讯地址";
	public static final String ALIAS_ZIP = "邮政编码";
	public static final String ALIAS_EMAIL = "电子邮箱";
	public static final String ALIAS_LEADER = "机构负责人";
	public static final String ALIAS_PHONE = "办公电话";
	public static final String ALIAS_FAX = "传真号码";
	public static final String ALIAS_STATE = "状态:1有效 2、停用";
	public static final String ALIAS_MOBILE = "负责人手机号码";
	public static final String ALIAS_JP = "简拼";
	public static final String ALIAS_QP = "全拼";
	public static final String ALIAS_ANCESTRY = "排序";
	public static final String ALIAS_COVER_PIC = "封面图";
	public static final String ALIAS_DESCRIPTION = "机构描述";
	public static final String ALIAS_COLLEGE_CODE = "学院代码";
	public static final String ALIAS_MAJOR_CODE = "专业代码";
	/**
	 * 机构ID db_column: ORG_ID
	 */
	private java.lang.String orgId;
	/**
	 * 机构排序ID db_column: ORG_SN
	 */
	private java.lang.Integer orgSn;
	/**
	 * 机构名称 db_column: ORG_NAME
	 */
	private java.lang.String orgName;
	/**
	 * 机构编号 db_column: ORG_CODE
	 */
	private java.lang.String orgCode;
	/**
	 * 行政区码 db_column: ORG_XZQM
	 */
	private java.lang.String orgXzqm;
	/**
	 * 机构显示名称 db_column: ORG_SHOWNAME
	 */
	private java.lang.String orgShowname;
	/**
	 * 父机构ID db_column: PARENT_ID
	 */
	private java.lang.String parentId;

	/**
	 * 父机构name
	 */
	private java.lang.String parentName;

	/**
	 * 层（阶次） db_column: LAYER
	 */
	private java.lang.Integer layer;

	/**
	 * 创建人 db_column: CREATOR
	 */
	private java.lang.String creator;
	/**
	 * 单位简介 db_column: REMARK
	 */
	private java.lang.String remark;
	/**
	 * 机构类型:1、部门 2、公司 db_column: ORG_TYPE
	 */
	private java.lang.String orgType;
	/**
	 * 通讯地址 db_column: ADDR
	 */
	private java.lang.String addr;
	/**
	 * 邮政编码 db_column: ZIP
	 */
	private java.lang.String zip;
	/**
	 * 电子邮箱 db_column: EMAIL
	 */
	private java.lang.String email;
	/**
	 * 机构负责人 db_column: LEADER
	 */
	private java.lang.String leader;
	/**
	 * 办公电话 db_column: PHONE
	 */
	private java.lang.String phone;
	/**
	 * 传真号码 db_column: FAX
	 */
	private java.lang.String fax;
	/**
	 * 状态:1有效 2、停用 db_column: STATE
	 */
	private java.lang.String state;
	/**
	 * 负责人手机号码 db_column: MOBILE
	 */
	private java.lang.String mobile;
	/**
	 * 简拼 db_column: JP
	 */
	private java.lang.String jp;
	/**
	 * 全拼 db_column: QP
	 */
	private java.lang.String qp;
	/**
	 * 排序 db_column: ANCESTRY
	 */
	private java.lang.String ancestry;
	/**
	 * 封面图 db_column: cover_pic
	 */

	private java.lang.String coverPic;
	/**
	 * 机构描述 db_column: description
	 */
	private java.lang.String description;
	/**
	 * 学院代码 db_column: college_code
	 */
	private java.lang.String collegeCode;
	/**
	 * 专业代码 db_column: major_code
	 */
	private java.lang.String majorCode;
	/**
	 * 是否有子结点 true有无子结点 false有子节点
	 */
	private boolean isLeaf;

	// columns END

	private List<TsysOrg> children = new ArrayList<TsysOrg>();

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public java.lang.String getCollegeCode() {
		return collegeCode;
	}

	public void setCollegeCode(java.lang.String collegeCode) {
		this.collegeCode = collegeCode;
	}

	public java.lang.String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(java.lang.String majorCode) {
		this.majorCode = majorCode;
	}

	public TsysOrg() {
	}

	public TsysOrg(java.lang.String orgId) {
		this.orgId = orgId;
	}

	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}

	public java.lang.String getOrgId() {
		return this.orgId;
	}

	public void setOrgSn(java.lang.Integer value) {
		this.orgSn = value;
	}

	public java.lang.Integer getOrgSn() {
		return this.orgSn;
	}

	public void setOrgName(java.lang.String value) {
		this.orgName = value;
	}

	public java.lang.String getOrgName() {
		return this.orgName;
	}

	public void setOrgCode(java.lang.String value) {
		this.orgCode = value;
	}

	public java.lang.String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgXzqm(java.lang.String value) {
		this.orgXzqm = value;
	}

	public java.lang.String getOrgXzqm() {
		return this.orgXzqm;
	}

	public void setOrgShowname(java.lang.String value) {
		this.orgShowname = value;
	}

	public java.lang.String getOrgShowname() {
		return this.orgShowname;
	}

	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}

	public java.lang.String getParentId() {
		return this.parentId;
	}

	public java.lang.String getParentName() {
		return parentName;
	}

	public void setParentName(java.lang.String parentName) {
		this.parentName = parentName;
	}

	public void setLayer(java.lang.Integer value) {
		this.layer = value;
	}

	public java.lang.Integer getLayer() {
		return this.layer;
	}

	public void setCreator(java.lang.String value) {
		this.creator = value;
	}

	public java.lang.String getCreator() {
		return this.creator;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.lang.String getRemark() {
		return this.remark;
	}

	public void setOrgType(java.lang.String value) {
		this.orgType = value;
	}

	public java.lang.String getOrgType() {
		return this.orgType;
	}

	public void setAddr(java.lang.String value) {
		this.addr = value;
	}

	public java.lang.String getAddr() {
		return this.addr;
	}

	public void setZip(java.lang.String value) {
		this.zip = value;
	}

	public java.lang.String getZip() {
		return this.zip;
	}

	public void setEmail(java.lang.String value) {
		this.email = value;
	}

	public java.lang.String getEmail() {
		return this.email;
	}

	public void setLeader(java.lang.String value) {
		this.leader = value;
	}

	public java.lang.String getLeader() {
		return this.leader;
	}

	public void setPhone(java.lang.String value) {
		this.phone = value;
	}

	public java.lang.String getPhone() {
		return this.phone;
	}

	public void setFax(java.lang.String value) {
		this.fax = value;
	}

	public java.lang.String getFax() {
		return this.fax;
	}

	public void setState(java.lang.String value) {
		this.state = value;
	}

	public java.lang.String getState() {
		return this.state;
	}

	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}

	public java.lang.String getMobile() {
		return this.mobile;
	}

	public void setJp(java.lang.String value) {
		this.jp = value;
	}

	public java.lang.String getJp() {
		return this.jp;
	}

	public void setQp(java.lang.String value) {
		this.qp = value;
	}

	public java.lang.String getQp() {
		return this.qp;
	}

	public void setAncestry(java.lang.String value) {
		this.ancestry = value;
	}

	public java.lang.String getAncestry() {
		return this.ancestry;
	}

	public void setCoverPic(java.lang.String value) {
		this.coverPic = value;
	}

	public java.lang.String getCoverPic() {
		return this.coverPic;
	}

	public void setDescription(java.lang.String value) {
		this.description = value;
	}

	public java.lang.String getDescription() {
		return this.description;
	}

	public List<TsysOrg> getChildren() {
		return children;
	}

	public void setChildren(List<TsysOrg> children) {
		this.children = children;
	}

}
