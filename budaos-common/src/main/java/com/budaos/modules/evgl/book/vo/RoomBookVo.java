package com.budaos.modules.evgl.book.vo;

import java.io.Serializable;

public class RoomBookVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ctId;

	private String pkgId;

	private String pkgLogo;

	private String pkgVersion;

	private String subjectId;

	private String subjectName;

	private String subjectAuthor;

	private String type;

	private String refPkgId;

	private String majorId;

	private String pkgLimit;

	private String createUserId;

	private String receiverUserId;

	private Integer ranking;

	public RoomBookVo() {
	}

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

	public String getPkgLogo() {
		return pkgLogo;
	}

	public void setPkgLogo(String pkgLogo) {
		this.pkgLogo = pkgLogo;
	}

	public String getPkgVersion() {
		return pkgVersion;
	}

	public void setPkgVersion(String pkgVersion) {
		this.pkgVersion = pkgVersion;
	}

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

	public String getSubjectAuthor() {
		return subjectAuthor;
	}

	public void setSubjectAuthor(String subjectAuthor) {
		this.subjectAuthor = subjectAuthor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefPkgId() {
		return refPkgId;
	}

	public void setRefPkgId(String refPkgId) {
		this.refPkgId = refPkgId;
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

	public String getPkgLimit() {
		return pkgLimit;
	}

	public void setPkgLimit(String pkgLimit) {
		this.pkgLimit = pkgLimit;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	@Override
	public String toString() {
		return "RoomBookVo{" +
				"ctId='" + ctId + '\'' +
				", pkgId='" + pkgId + '\'' +
				", pkgLogo='" + pkgLogo + '\'' +
				", pkgVersion='" + pkgVersion + '\'' +
				", subjectId='" + subjectId + '\'' +
				", subjectName='" + subjectName + '\'' +
				", subjectAuthor='" + subjectAuthor + '\'' +
				'}';
	}
}
