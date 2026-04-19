package com.budaos.modules.evgl.trainee.domain;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.annotation.validator.MaxLength;
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

public class TevglTraineeInfo extends BaseDomain<TevglTraineeInfo>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TevglTraineeInfo";
	public static final String ALIAS_TRAINEE_ID = "粉丝主键ID";
	public static final String ALIAS_TRAINEE_NAME = "姓名";
	public static final String ALIAS_NICK_NAME = "昵称";
	public static final String ALIAS_TRAINEE_SEX = "性别";
	public static final String ALIAS_TRAINEE_CARD = "身份证号码";
	public static final String ALIAS_TRAINEE_BIRTHDAY = "出生年月";
	public static final String ALIAS_MOBILE = "手机号码";
	public static final String ALIAS_TRAINEE_STATE = "状态";
	public static final String ALIAS_SORT_NUM = "排序号";
	public static final String ALIAS_REMARK = "备注";
	public static final String ALIAS_MAJOR = "在校专业";
	public static final String ALIAS_COMPANY = "就职公司";
	public static final String ALIAS_TRAINEE_SCHOOL = "就读院校";
	public static final String ALIAS_CONTACT_MAN = "紧急联系人";
	public static final String ALIAS_CONTACT_TEL = "紧急联系电话";
	public static final String ALIAS_TRAINEE_PIC = "证件照";
	public static final String ALIAS_TRAINEE_HEAD = "头像";
	public static final String ALIAS_TRAINEE_QQ = "qq号码";
	public static final String ALIAS_TRAINEE_EDUCATION = "学历";
	public static final String ALIAS_EMAIL = "用户邮箱";
	public static final String ALIAS_USER_PASSWD = "密码";
	public static final String ALIAS_USER_YAN = "加密盐";
	public static final String ALIAS_TOKEN = "登陆标识";
	public static final String ALIAS_TRAINEE_WXID = "小程序微信ID";
	public static final String ALIAS_WX_OPENID = "网站微信ID";
	public static final String ALIAS_QQ_OPENID = "qq接入openid";
	public static final String ALIAS_WEIBO_OPENID = "新浪微薄接入openid";
	public static final String ALIAS_TRAINEE_TYPE = "用户类型";
	public static final String ALIAS_EMPIRICAL_VALUE = "总经验值";
	public static final String ALIAS_BLOGS_NUM = "博客总数量";
	public static final String ALIAS_CLASSROOM_NUM = "开设课堂总数量";
	public static final String ALIAS_JOB_NUMBER = "工号/学号";
	public static final String ALIAS_FANS_NUM = "粉丝数";
	public static final String ALIAS_FOLLOW_NUM = "关注数";
	public static final String ALIAS_BLOG_BG_PIC = "博客背景图";
	public static final String ALIAS_BLOG_REMARK = "博客个人描述";

    /**
     * 粉丝主键ID       db_column: trainee_id 
     */	
 	//(msg="粉丝主键ID不能为空")
 	@MaxLength(value=32, msg="字段[粉丝主键ID]超出最大长度[32]")
	private java.lang.String traineeId;
    /**
     * 姓名       db_column: trainee_name 
     */	
 	//@NotNull(msg="姓名不能为空")
 	@MaxLength(value=50, msg="字段[姓名]超出最大长度[50]")
	private java.lang.String traineeName;
    /**
     * 昵称       db_column: nick_name 
     */	
 	//@NotNull(msg="昵称不能为空")
 	@MaxLength(value=100, msg="字段[昵称]超出最大长度[100]")
	private java.lang.String nickName;
    /**
     * 性别       db_column: trainee_sex 
     */	
 	//@NotNull(msg="性别不能为空")
 	@MaxLength(value=32, msg="字段[性别]超出最大长度[32]")
	private java.lang.String traineeSex;
    /**
     * 身份证号码       db_column: trainee_card 
     */	
 	//@NotNull(msg="身份证号码不能为空")
 	@MaxLength(value=30, msg="字段[身份证号码]超出最大长度[30]")
	private java.lang.String traineeCard;
    /**
     * 出生年月       db_column: trainee_birthday 
     */	
 	//@NotNull(msg="出生年月不能为空")
 	@MaxLength(value=20, msg="字段[出生年月]超出最大长度[20]")
	private java.lang.String traineeBirthday;
    /**
     * 手机号码       db_column: mobile 
     */	
 	//@NotNull(msg="手机号码不能为空")
 	@MaxLength(value=20, msg="字段[手机号码]超出最大长度[20]")
	private java.lang.String mobile;
    /**
     * 状态       db_column: trainee_state 
     */	
 	//@NotNull(msg="状态不能为空")
 	@MaxLength(value=32, msg="字段[状态]超出最大长度[32]")
	private java.lang.String traineeState;
    /**
     * 排序号       db_column: sort_num 
     */	
 	//@NotNull(msg="排序号不能为空")
 	@MaxLength(value=10, msg="字段[排序号]超出最大长度[10]")
	private java.lang.Integer sortNum;
    /**
     * 备注       db_column: remark 
     */	
 	//@NotNull(msg="备注不能为空")
 	@MaxLength(value=1000, msg="字段[备注]超出最大长度[1000]")
	private java.lang.String remark;
    /**
     * 在校专业       db_column: major 
     */	
 	//@NotNull(msg="在校专业不能为空")
 	@MaxLength(value=100, msg="字段[在校专业]超出最大长度[100]")
	private java.lang.String major;
    /**
     * 就职公司       db_column: company 
     */	
 	//@NotNull(msg="就职公司不能为空")
 	@MaxLength(value=100, msg="字段[就职公司]超出最大长度[100]")
	private java.lang.String company;
    /**
     * 就读院校       db_column: trainee_school 
     */	
 	//@NotNull(msg="就读院校不能为空")
 	@MaxLength(value=100, msg="字段[就读院校]超出最大长度[100]")
	private java.lang.String traineeSchool;
    /**
     * 紧急联系人       db_column: contact_man 
     */	
 	//@NotNull(msg="紧急联系人不能为空")
 	@MaxLength(value=50, msg="字段[紧急联系人]超出最大长度[50]")
	private java.lang.String contactMan;
    /**
     * 紧急联系电话       db_column: contact_tel 
     */	
 	//@NotNull(msg="紧急联系电话不能为空")
 	@MaxLength(value=20, msg="字段[紧急联系电话]超出最大长度[20]")
	private java.lang.String contactTel;
    /**
     * 证件照       db_column: trainee_pic 
     */	
 	//@NotNull(msg="证件照不能为空")
 	@MaxLength(value=300, msg="字段[证件照]超出最大长度[300]")
	private java.lang.String traineePic;
    /**
     * 头像       db_column: trainee_head 
     */	
 	//@NotNull(msg="头像不能为空")
 	@MaxLength(value=300, msg="字段[头像]超出最大长度[300]")
	private java.lang.String traineeHead;
    /**
     * qq号码       db_column: trainee_qq 
     */	
 	//@NotNull(msg="qq号码不能为空")
 	@MaxLength(value=20, msg="字段[qq号码]超出最大长度[20]")
	private java.lang.String traineeQq;
    /**
     * 学历       db_column: trainee_education 
     */	
 	//@NotNull(msg="学历不能为空")
 	@MaxLength(value=20, msg="字段[学历]超出最大长度[20]")
	private java.lang.String traineeEducation;
    /**
     * 用户邮箱       db_column: email 
     */	
 	//@NotNull(msg="用户邮箱不能为空")
 	@MaxLength(value=100, msg="字段[用户邮箱]超出最大长度[100]")
	private java.lang.String email;
    /**
     * 密码       db_column: user_passwd 
     */	
 	//@NotNull(msg="密码不能为空")
 	@MaxLength(value=100, msg="字段[密码]超出最大长度[100]")
	private java.lang.String userPasswd;
    /**
     * 加密盐       db_column: user_yan 
     */	
 	//@NotNull(msg="加密盐不能为空")
 	@MaxLength(value=100, msg="字段[加密盐]超出最大长度[100]")
	private java.lang.String userYan;
    /**
     * 登陆标识       db_column: token 
     */	
 	//@NotNull(msg="登陆标识不能为空")
 	@MaxLength(value=100, msg="字段[登陆标识]超出最大长度[100]")
	private java.lang.String token;
    /**
     * 小程序微信ID       db_column: trainee_wxid 
     */	
 	//@NotNull(msg="小程序微信ID不能为空")
 	@MaxLength(value=100, msg="字段[小程序微信ID]超出最大长度[100]")
	private java.lang.String traineeWxid;
    /**
     * 网站微信ID       db_column: wx_openid 
     */	
 	//@NotNull(msg="网站微信ID不能为空")
 	@MaxLength(value=100, msg="字段[网站微信ID]超出最大长度[100]")
	private java.lang.String wxOpenid;
    /**
     * qq接入openid       db_column: qq_openid 
     */	
 	//@NotNull(msg="qq接入openid不能为空")
 	@MaxLength(value=100, msg="字段[qq接入openid]超出最大长度[100]")
	private java.lang.String qqOpenid;
    /**
     * 新浪微薄接入openid       db_column: weibo_openid 
     */	
 	//@NotNull(msg="新浪微薄接入openid不能为空")
 	@MaxLength(value=100, msg="字段[新浪微薄接入openid]超出最大长度[100]")
	private java.lang.String weiboOpenid;
    /**
     * 用户类型       db_column: trainee_type 
     */	
 	//@NotNull(msg="用户类型不能为空")
 	@MaxLength(value=32, msg="字段[用户类型]超出最大长度[32]")
	private java.lang.String traineeType;
 	/**
     * 总经验值       db_column: empirical_value 
     */	
 	//@NotNull(msg="总经验值不能为空")
 	@MaxLength(value=10, msg="字段[总经验值]超出最大长度[10]")
	private java.lang.Integer empiricalValue;
    /**
     * 博客总数量       db_column: blogs_num 
     */	
 	//@NotNull(msg="博客总数量不能为空")
 	@MaxLength(value=10, msg="字段[博客总数量]超出最大长度[10]")
	private java.lang.Integer blogsNum;
    /**
     * 开设课堂总数量       db_column: classroom_num 
     */	
 	//@NotNull(msg="开设课堂总数量不能为空")
 	@MaxLength(value=10, msg="字段[开设课堂总数量]超出最大长度[10]")
	private java.lang.Integer classroomNum;
 	/**
     * 工号/学号       db_column: job_number 
     */	
 	//@NotNull(msg="工号/学号不能为空")
 	@MaxLength(value=50, msg="字段[工号/学号]超出最大长度[50]")
	private java.lang.String jobNumber;
 	
 	/**
     * 粉丝数       db_column: fans_num 
     */	
 	@MaxLength(value=10, msg="字段[粉丝数]超出最大长度[10]")
	private java.lang.Integer fansNum;
 	
 	/**
     * 关注数       db_column: follow_num 
     */	
 	@MaxLength(value=10, msg="字段[关注数]超出最大长度[10]")
	private java.lang.Integer followNum;
 	
 	 /**
     * 博客背景图       db_column: blog_bg_pic 
     */	
 	//@NotNull(msg="博客背景图不能为空")
 	@MaxLength(value=500, msg="字段[博客背景图]超出最大长度[500]")
	private java.lang.String blogBgPic;
 	/**
     * 博客个人描述       db_column: blog_remark 
     */	
 	//@NotNull(msg="博客个人描述不能为空")
 	@MaxLength(value=255, msg="字段[博客个人描述]超出最大长度[255]")
	private java.lang.String blogRemark;
 	
 	private java.lang.String attachId;
 	
 	/**
 	 * 班级id
 	 */
 	private String classId;
 	
	//columns END

	public TevglTraineeInfo(){
	}

	public TevglTraineeInfo(
		java.lang.String traineeId
	){
		this.traineeId = traineeId;
	}

	public void setTraineeId(java.lang.String value) {
		this.traineeId = value;
	}
	public java.lang.String getTraineeId() {
		return this.traineeId;
	}
	public void setTraineeName(java.lang.String value) {
		this.traineeName = value;
	}
	public java.lang.String getTraineeName() {
		return this.traineeName;
	}
	public void setNickName(java.lang.String value) {
		this.nickName = value;
	}
	public java.lang.String getNickName() {
		return this.nickName;
	}
	public void setTraineeSex(java.lang.String value) {
		this.traineeSex = value;
	}
	public java.lang.String getTraineeSex() {
		return this.traineeSex;
	}
	public void setTraineeCard(java.lang.String value) {
		this.traineeCard = value;
	}
	public java.lang.String getTraineeCard() {
		return this.traineeCard;
	}
	public void setTraineeBirthday(java.lang.String value) {
		this.traineeBirthday = value;
	}
	public java.lang.String getTraineeBirthday() {
		return this.traineeBirthday;
	}
	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}
	public java.lang.String getMobile() {
		return this.mobile;
	}
	public void setTraineeState(java.lang.String value) {
		this.traineeState = value;
	}
	public java.lang.String getTraineeState() {
		return this.traineeState;
	}
	public void setSortNum(java.lang.Integer value) {
		this.sortNum = value;
	}
	public java.lang.Integer getSortNum() {
		return this.sortNum;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setMajor(java.lang.String value) {
		this.major = value;
	}
	public java.lang.String getMajor() {
		return this.major;
	}
	public void setCompany(java.lang.String value) {
		this.company = value;
	}
	public java.lang.String getCompany() {
		return this.company;
	}
	public void setTraineeSchool(java.lang.String value) {
		this.traineeSchool = value;
	}
	public java.lang.String getTraineeSchool() {
		return this.traineeSchool;
	}
	public void setContactMan(java.lang.String value) {
		this.contactMan = value;
	}
	public java.lang.String getContactMan() {
		return this.contactMan;
	}
	public void setContactTel(java.lang.String value) {
		this.contactTel = value;
	}
	public java.lang.String getContactTel() {
		return this.contactTel;
	}
	public void setTraineePic(java.lang.String value) {
		this.traineePic = value;
	}
	public java.lang.String getTraineePic() {
		return this.traineePic;
	}
	public void setTraineeHead(java.lang.String value) {
		this.traineeHead = value;
	}
	public java.lang.String getTraineeHead() {
		return this.traineeHead;
	}
	public void setTraineeQq(java.lang.String value) {
		this.traineeQq = value;
	}
	public java.lang.String getTraineeQq() {
		return this.traineeQq;
	}
	public void setTraineeEducation(java.lang.String value) {
		this.traineeEducation = value;
	}
	public java.lang.String getTraineeEducation() {
		return this.traineeEducation;
	}
	public void setEmail(java.lang.String value) {
		this.email = value;
	}
	public java.lang.String getEmail() {
		return this.email;
	}
	public void setUserPasswd(java.lang.String value) {
		this.userPasswd = value;
	}
	public java.lang.String getUserPasswd() {
		return this.userPasswd;
	}
	public void setUserYan(java.lang.String value) {
		this.userYan = value;
	}
	public java.lang.String getUserYan() {
		return this.userYan;
	}
	public void setToken(java.lang.String value) {
		this.token = value;
	}
	public java.lang.String getToken() {
		return this.token;
	}
	public void setTraineeWxid(java.lang.String value) {
		this.traineeWxid = value;
	}
	public java.lang.String getTraineeWxid() {
		return this.traineeWxid;
	}
	public void setWxOpenid(java.lang.String value) {
		this.wxOpenid = value;
	}
	public java.lang.String getWxOpenid() {
		return this.wxOpenid;
	}
	public void setQqOpenid(java.lang.String value) {
		this.qqOpenid = value;
	}
	public java.lang.String getQqOpenid() {
		return this.qqOpenid;
	}
	public void setWeiboOpenid(java.lang.String value) {
		this.weiboOpenid = value;
	}
	public java.lang.String getWeiboOpenid() {
		return this.weiboOpenid;
	}
	public void setTraineeType(java.lang.String value) {
		this.traineeType = value;
	}
	public java.lang.String getTraineeType() {
		return this.traineeType;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public java.lang.Integer getEmpiricalValue() {
		return empiricalValue;
	}

	public void setEmpiricalValue(java.lang.Integer empiricalValue) {
		this.empiricalValue = empiricalValue;
	}

	public java.lang.Integer getBlogsNum() {
		return blogsNum;
	}

	public void setBlogsNum(java.lang.Integer blogsNum) {
		this.blogsNum = blogsNum;
	}

	public java.lang.Integer getClassroomNum() {
		return classroomNum;
	}

	public void setClassroomNum(java.lang.Integer classroomNum) {
		this.classroomNum = classroomNum;
	}

	public java.lang.String getAttachId() {
		return attachId;
	}

	public void setAttachId(java.lang.String attachId) {
		this.attachId = attachId;
	}

	public java.lang.String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(java.lang.String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public java.lang.Integer getFansNum() {
		return fansNum;
	}

	public void setFansNum(java.lang.Integer fansNum) {
		this.fansNum = fansNum;
	}

	public java.lang.Integer getFollowNum() {
		return followNum;
	}

	public void setFollowNum(java.lang.Integer followNum) {
		this.followNum = followNum;
	}

	public java.lang.String getBlogBgPic() {
		return blogBgPic;
	}

	public void setBlogBgPic(java.lang.String blogBgPic) {
		this.blogBgPic = blogBgPic;
	}

	public java.lang.String getBlogRemark() {
		return blogRemark;
	}

	public void setBlogRemark(java.lang.String blogRemark) {
		this.blogRemark = blogRemark;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
}

