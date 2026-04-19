package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;

//import org.hibernate.validator.constraints.Email;
//import org.hibernate.validator.constraints.NotBlank;
//import com.budaos.common.cbsecurity.validator.group.AddGroup;
//import com.budaos.common.cbsecurity.validator.group.UpdateGroup;

/**
 * 用户信息
 * @author
 *
 */
public class TsysUserinfo extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;
	private String id;

	/**
	 * 用户名
	 */
	//@NotBlank(message = "用户名不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String username;

	/**
	 * 密码
	 */
	//@NotBlank(message = "密码不能为空", groups = AddGroup.class)
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	// @NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	//@Email(message = "邮箱格式不正确", groups = { AddGroup.class, UpdateGroup.class })
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态 0：禁用 1：正常
	 */
	private String status;

	/**
	 * 角色ID列表
	 */
	private List<String> roleIdList;
	private List<String> roleNameList;

	private List<String> orgIdList;

	/**
	 * 所属机构
	 */
	private String orgId;
	
	/**
	 * 用户真实姓名
	 */
	private String userRealname;
	
	/**
	 * 所属机构
	 */
	private String orgnames;

	/**

	 * 用户头像
	 */
	private String userimg;
	
    /**
     * 附件id
     */
    private String userimgAttachId;
	
	/**
	 * 邮政编码
	 */
	private String zip;
	/**
	 * 排序号
	 */
	private Integer sortNum;
	/**
	 * 用户类型
	 */
	private String userType;
	/**
	 * 所属岗位
	 */
	private List<String> postIdList;
	
	private String postId;
	
	/**
	 * 性别
	 */
	private String sex;
	private java.lang.String userTheme;
    /**
     * 身份证号码       db_column: user_card 
     */	
	private java.lang.String userCard;
    /**
     * 出生年月       db_column: birthday 
     */	
	private java.lang.String birthday;
    /**
     * 家庭住址       db_column: native_place 
     */	
	private java.lang.String nativePlace;
    /**
     * 民族       db_column: nation 
     */	
	private java.lang.String nation;
	
	public java.lang.String getUserTheme() {
		return userTheme;
	}

	public void setUserTheme(java.lang.String userTheme) {
		this.userTheme = userTheme;
	}

	public java.lang.String getUserCard() {
		return userCard;
	}

	public void setUserCard(java.lang.String userCard) {
		this.userCard = userCard;
	}

	public java.lang.String getBirthday() {
		return birthday;
	}

	public void setBirthday(java.lang.String birthday) {
		this.birthday = birthday;
	}

	public java.lang.String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(java.lang.String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public java.lang.String getNation() {
		return nation;
	}

	public void setNation(java.lang.String nation) {
		this.nation = nation;
	}

	/**
	 * 设置：
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置：用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取：用户名
	 * 
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置：密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取：密码
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置：邮箱
	 * 
	 * @param email
	 *            邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取：邮箱
	 * 
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置：手机号
	 * 
	 * @param mobile
	 *            手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取：手机号
	 * 
	 * @return String
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置：状态 0：禁用 1：正常
	 * 
	 * @param status
	 *            状态 0：禁用 1：正常
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取：状态 0：禁用 1：正常
	 * 
	 * @return Integer
	 */
	public String getStatus() {
		return status;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}


	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public List<String> getOrgIdList() {
		return orgIdList;
	}

	/**
	 * 设置：机构ID列表
	 * 
	 * @param orgIdList
	 *            机构ID列表
	 */
	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}

	public String getOrgnames() {
		return orgnames;
	}

	/**
	 * 设置：机构名称
	 * 
	 * @param orgnames
	 *            机构名称
	 */
	public void setOrgnames(String orgnames) {
		this.orgnames = orgnames;
	}


	public String getUserimg() {
		return userimg;
	}
	/**
	 * 设置：用户头像
	 * @param userimg 用户头像
	 */
	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public String getZip() {
		return zip;
	}
	/**
	 * 设置：邮政编码
	 * @param zip 邮政编码
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	public Integer getSortNum() {
		return sortNum;
	}
	/**
	 * 设置：排序号
	 * @param sort_num 排序号
	 */
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getUserType() {
		return userType;
	}
	/**
	 * 设置：用户类型
	 * @param user_type:内部用户、第三方用户、外部用户
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<String> getPostIdList() {
		return postIdList;
	}
	/**
	 * 设置：所属岗位
	 * @param post_id 所属岗位
	 */
	public void setPostIdList(List<String> postIdList) {
		this.postIdList = postIdList;
	}

	public String getSex() {
		return sex;
	}
	/**
	 * 设置：性别
	 * @param sex 性别
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserRealname() {
		return userRealname;
	}
	/**
	 * 设置：用户真实姓名
	 * @param user_realname 用户真实姓名
	 */
	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}

	public String getOrgId() {
		return orgId;
	}
	/**
	 * 设置：所属机构
	 * @param orgId 所属机构
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<String> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public String getUserimgAttachId() {
		return userimgAttachId;
	}

	public void setUserimgAttachId(String userimgAttachId) {
		this.userimgAttachId = userimgAttachId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	
	
}
