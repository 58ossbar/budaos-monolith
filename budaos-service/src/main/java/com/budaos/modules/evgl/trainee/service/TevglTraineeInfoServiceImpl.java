package com.budaos.modules.evgl.trainee.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.book.domain.TevglBookMajor;
import com.budaos.modules.evgl.book.persistence.TevglBookMajorMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClasstraineeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoUpdateVo;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoVo;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.budaos.utils.tool.TicketDesUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglTraineeInfoServiceImpl implements TevglTraineeInfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTraineeInfoServiceImpl.class);
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper;
	@Autowired
	private TevglBookMajorMapper tevglBookMajorMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglTchClasstraineeMapper tevglTchClasstraineeMapper;
	// 访问地址
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTraineeInfo> tevglTraineeInfoList = tevglTraineeInfoMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglTraineeInfoList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglTraineeInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTraineeInfoList = tevglTraineeInfoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglTraineeInfoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglTraineeInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTraineeInfo
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglTraineeInfo tevglTraineeInfo) throws BudaosException {
		tevglTraineeInfo.setTraineeId(Identities.uuid());
		tevglTraineeInfo.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglTraineeInfo.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglTraineeInfo);
		tevglTraineeInfoMapper.insert(tevglTraineeInfo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTraineeInfo
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTraineeInfo tevglTraineeInfo) throws BudaosException {
		tevglTraineeInfo.setUpdateUserId(tevglTraineeInfo.getTraineeId());
	    tevglTraineeInfo.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglTraineeInfoMapper.update(tevglTraineeInfo);
		String attachId = tevglTraineeInfo.getAttachId();
		// 如果上传了附件
		if (StrUtils.isNotEmpty(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglTraineeInfo.getTraineeId(), "0", "16");
		}
		// 更改对应的头像
		/*TimUserinfo timUserinfo = new TimUserinfo();
		timUserinfo.setUserId(tevglTraineeInfo.getTraineeId());
		timUserinfo.setUserRealname(tevglTraineeInfo.getTraineeName());
		timUserinfo.setUsername(tevglTraineeInfo.getTraineeName());
		if (StrUtils.isNotEmpty(tevglTraineeInfo.getTraineePic())) {
			timUserinfo.setUserimg(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("16") + "/" + tevglTraineeInfo.getTraineePic());	
		}
		timUserinfoMapper.update(timUserinfo);*/
		return R.ok("修改成功");
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglTraineeInfoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTraineeInfoMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		//TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(id);
		TevglTraineeInfoVo traineeInfo = this.selectTraineeVoById(id);
		return R.ok().put(Constant.R_DATA, traineeInfo);
	}

	/**
	 * 根据手机号码查询粉丝信息
	 * @param mobile 手机号码
	 * @return TevglTraineeInfo
	 */
	public TevglTraineeInfo selectByMobile (String mobile){
		return tevglTraineeInfoMapper.selectByMobile(mobile);
	}
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return List<TevglTraineeInfo>
	 */
	public List<TevglTraineeInfo> queryByMap( Map<String, Object> params) {
		return tevglTraineeInfoMapper.selectListByMap(params);
	}
	
	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	public TevglTraineeInfo selectObjectById(String id) {
		return tevglTraineeInfoMapper.selectObjectById(id);
	}
	

	@Override
	public TevglTraineeInfoVo selectTraineeVoById(Object id) {
		TevglTraineeInfoVo traineeInfo = tevglTraineeInfoMapper.selectTraineeVoById(id);
		if (traineeInfo != null) {
			// 处理头像
			traineeInfo.setTraineePic(uploadPathUtils.stitchingPath(traineeInfo.getTraineePic(), "16"));
			// 处理所属班级
			List<String> classIdList = tevglTchClasstraineeMapper.selectClassIdListByTraineeId(traineeInfo.getTraineeId());
			if (classIdList != null && classIdList.size() > 0) {
				traineeInfo.setClassId(classIdList.stream().collect(Collectors.joining(",")));	
			}
		}
		return traineeInfo;
	}
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> selectObjectMapById(String id) {
		Map<String, Object> traineeInfo = tevglTraineeInfoMapper.selectObjectMapById(id);
		// 头像处理
		traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic").toString(), "16"));
		// 字典转换
		convertUtil.convertDict(traineeInfo, "orgId", "orgId"); // 用户类型
		convertUtil.convertDict(traineeInfo, "traineeTypeName", "trainee_type"); // 用户类型1客户2系统用户3学员4教师
		return traineeInfo;
	}

	
	/**
	 * 绑定手机号后该做的业务逻辑
	 * @param traineeInfo
	 */
	public void doAfterBindMobile(TevglTraineeInfo traineeInfo) {
		// 1同步用户信息到博客表start
		
		// 1同步用户信息到博客表end
	}

	/**
	 * 根据微信openid查询用户
	 * @param openid
	 * @return
	 */
	@Override
	public TevglTraineeInfo selectObjectByOpenId(String openid) {
		return tevglTraineeInfoMapper.selectObjectByOpenId(openid);
	}

	/**
	 * 个人信息
	 * @param traineeId
	 * @return
	 */
	@Override
	public R viewTraineeInfoForManagementPanel(String traineeId) {
		if (StrUtils.isEmpty(traineeId)) {
			return R.error("参数traineeId为空");
		}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
		if (traineeInfo == null) {
			return R.error("用户信息已不存在");
		}
		// 最终返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		// 基本信息
		Map<String, Object> basicInfo = toGetBasicInfo(traineeInfo);
		// 魅力信息
		Map<String, Object> charmInfo = toGetCharmInfo(traineeInfo);
		// 返回数据
		result.put("basicInfo", basicInfo);
		result.put("charmInfo", charmInfo);
		return R.ok().put(Constant.R_DATA, result);
	}
	
	/***
	 * 基本信息
	 * @param traineeInfo
	 * @apiNote 
	 * { "teacherName":"教师名称", "teacherIdNo" : "教师证件号", "teacherPostName":"职称",
	 *   "mainSubjects":"主攻科目", "jobNum":"工号/学号", "school":"所在学校", "college":"所在院系",
	 *   "teacherPic":"教师头像"
	 * }
	 * @return
	 */
	private Map<String, Object> toGetBasicInfo(TevglTraineeInfo traineeInfo){
		if (traineeInfo == null) {
			return null;
		}
		Map<String, Object> info = new HashMap<>();
		String traineeId = traineeInfo.getTraineeId();
		// 教师信息
		TevglTchTeacher teacherInfo = tevglTchTeacherMapper.selectObjectByTraineeId(traineeId);
		if (teacherInfo == null) {
			teacherInfo = new TevglTchTeacher();
			log.debug("用户 traineeId [" + traineeId +"], 不是教师了");
		}
		// 转换学员类型
		String traineeType = traineeInfo.getTraineeType();
		String traineeSex = traineeInfo.getTraineeSex();
		convertUtil.convertDict(traineeInfo, "traineeSex", "sex");
		convertUtil.convertDict(traineeInfo, "traineeType", "trainee_type");
		// 转换一级机构
		convertUtil.convertOrgId(teacherInfo, "orgId");
		// 转换二级机构
		convertUtil.convertOrgId(teacherInfo, "orgIdDepartment");
		// 主攻科目(所教专业)
		String mainSubjects = "";
		if (StrUtils.isNotEmpty(teacherInfo.getMajorId())) {
			String[] split = teacherInfo.getMajorId().split(",");
			List<String> majorIds = Stream.of(split).collect(Collectors.toList());
			if (majorIds != null && majorIds.size() > 0) {
				Map<String, Object> map = new HashMap<>();
				map.put("majorIds", majorIds);
				List<TevglBookMajor> majorList = tevglBookMajorMapper.selectListByMap(map);
				mainSubjects = majorList.stream().map(a -> a.getMajorName()).collect(Collectors.joining(","));
			}
		}
		// 头像处理,没有教师照则用微信头像
		String teacherPic = teacherInfo.getTeacherPic();
		if (StrUtils.isNotEmpty(teacherPic)) {
			teacherPic = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("7") + "/" + teacherPic;
		} else {
			// 网络头像
			teacherPic = traineeInfo.getTraineeHead();
			// 如果学员表证件不为空, (教师表头像不开放给用户修改)
			if (StrUtils.isNotEmpty(traineeInfo.getTraineePic())) {
				teacherPic = uploadPathUtils.stitchingPath(traineeInfo.getTraineePic(), "16");
			} else { // 如果学生证也为空就传个默认头像
				info.put("teacherPic", "/uploads/defaulthead.png");
			}
		}
		// 填充信息
		info.put("traineeId", traineeId); // 主键(学员和教师表主键值需一致)
		info.put("traineePic", teacherPic); // 头像使用学员头像
		// 名称,修改新的时候会同步维护学员名称和教师名称
		//info.put("traineeName", StrUtils.isEmpty(traineeInfo.getTraineeName()) ? traineeInfo.getNickName() : traineeInfo.getTraineeName());
		info.put("traineeName", traineeInfo.getTraineeName());
		info.put("nickName", traineeInfo.getNickName());
		info.put("teacherErtificateNumber", teacherInfo.getTeacherErtificateNumber()); // 教师证
		info.put("teacherPostName", teacherInfo.getPostName()); // 职位
		info.put("mainSubjects", mainSubjects); // 主攻科目
		info.put("jobNumber", teacherInfo.getJobNumber()); // 工号/学号
		info.put("school", teacherInfo.getOrgId()); // 所在学校
		info.put("college", teacherInfo.getOrgIdDepartment()); // 所在院系
		info.put("mobile", traineeInfo.getMobile()); // 手机
		info.put("email", traineeInfo.getEmail()); // 邮箱
		info.put("empiricalValue", handleEmpiricalValue(traineeInfo)); // 经验值
		info.put("traineeType", traineeType); // 学员类型
		info.put("traineeTypeName", traineeInfo.getTraineeType()); // 学员类型名称
		info.put("traineeSex", traineeSex); // 性别
		info.put("traineeSexName", traineeInfo.getTraineeSex());
		info.put("traineeQq", traineeInfo.getTraineeQq());
		info.put("traineeEducation", traineeInfo.getTraineeEducation());
		return info;
	}
	
	/**
	 * 经验值
	 * @param traineeInfo
	 * @return
	 */
	private Integer handleEmpiricalValue(TevglTraineeInfo traineeInfo) {
		Integer empiricalValue = tevglTraineeInfoMapper.countEmpiricalValue(traineeInfo.getTraineeId());
		// 如果两边经验值不一致了，修正一下
		if (empiricalValue != null && traineeInfo.getEmpiricalValue() != null && !empiricalValue.equals(traineeInfo.getEmpiricalValue())) {
			TevglTraineeInfo t = new TevglTraineeInfo();
			t.setTraineeId(traineeInfo.getTraineeId());
			t.setEmpiricalValue(empiricalValue);
			tevglTraineeInfoMapper.update(t);
		}
		return empiricalValue;
	}
	
	/**
	 * 魅力信息
	 * @param traineeInfo
	 * @apiNote
	 * { "cloudClassroomNum":"创建云课堂", "studentNum":"累计学生", "resourceNum":"发布资源",
	 *   "activityNum":"开展活动", "blogsNum":"累计博客", "charmValue":"魅力值"
	 * }
	 * @return
	 */
	private Map<String, Object> toGetCharmInfo(TevglTraineeInfo traineeInfo) {
		if (traineeInfo == null) {
			return null;
		}
		String traineeId = traineeInfo.getTraineeId();
		Map<String, Object> info = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		// 
		params.put("createUserId", traineeId);
		List<TevglTchClassroom> classroomList = tevglTchClassroomMapper.selectListByMap(params);
		// 累计学生数
		Integer studentNum = tevglTchClassroomTraineeMapper.countStudentNumByTraineeId(traineeId);
		// 累计发布资源
		Integer resourceNum = tevglPkgInfoMapper.countPkgResCount(traineeId);
		// 累计开展活动
		Integer activityNum = tevglPkgInfoMapper.countPkgActCount(traineeId);
		// 返回给前端数据
		info.put("empiricalValue", handleEmpiricalValue(traineeInfo)); // 经验值
		info.put("cloudClassroomNum", classroomList.size()); // 累计开设的课堂数
		info.put("studentNum", studentNum); // 累计学生数
		info.put("resourceNum", resourceNum == null ? 0 : resourceNum); // 累计发布资源数
		info.put("activityNum", activityNum == null ? 0 : activityNum); // 累计开展活动数
		info.put("blogsNum", StrUtils.isNull(traineeInfo.getBlogsNum()) ? 0 : traineeInfo.getBlogsNum()); // 个人总博客数
		// TODO 魅力值（等待公式计算）
		info.put("charmValue", 0);
		return info;
	}

	/**
	 * 查看学员信息
	 * @param traineeId 学员主键
	 * @return
	 */
	@Override
	public R viewTraineeInfo(String traineeId) {
		if (StrUtils.isEmpty(traineeId)) {
			return R.error("参数traineeId为空");
		}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
		if (traineeInfo == null) {
			return R.error("用户信息已不存在");
		}
		// 最终返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		// 基本信息
		Map<String, Object> basicInfo = toGetBasicTraineeInfo(traineeInfo);
		// 其它信息
		Map<String, Object> charmInfo = toGetCharmTraineeInfo(traineeInfo);
		// 返回数据
		result.put("basicInfo", basicInfo);
		result.put("charmInfo", charmInfo);
		return R.ok().put(Constant.R_DATA, result);
	}

	/**
	 * 基本信息（学生）
	 * @param traineeInfo
	 * @return
	 * @apiNote
	 * {
	 * traineeId学员主键，traineeName学员名称，traineeSex性别，mobile手机号码，className班级名称
	 * traineeSchool就读院校，traineePic头像，studentNumber学号
	 * }
	 */
	private Map<String, Object> toGetBasicTraineeInfo(TevglTraineeInfo traineeInfo) {
		if (traineeInfo == null) {
			traineeInfo = new TevglTraineeInfo();
		}
		String traineeId = traineeInfo.getTraineeId();
		String traineeSex = traineeInfo.getTraineeSex();
		convertUtil.convertDict(traineeInfo, "traineeSex", "sex");
		Map<String, Object> info = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		// 班级名称
		params.put("traineeId", traineeId);
		List<Map<String, Object>> list = tevglTchClasstraineeMapper.selectListMapForWeb(params);
		if (list != null && list.size() > 0) {
			info.put("classId", list.get(0).get("classId"));
			info.put("className", list.get(0).get("className"));
		}
		// 头像处理
		String traineePic = traineeInfo.getTraineePic();
		if (StrUtils.isNotEmpty(traineePic)) {
			//traineePic = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("16") + "/" + traineePic;
			traineePic = uploadPathUtils.stitchingPath(traineePic, "16");
		} else {
			traineePic = traineeInfo.getTraineeHead();
		}
		// 填充并返回信息
		info.put("traineeId", traineeInfo.getTraineeId());
		//info.put("traineeName", StrUtils.isEmpty(traineeInfo.getTraineeName()) ? traineeInfo.getNickName() : traineeInfo.getTraineeName());
		info.put("traineeName", traineeInfo.getTraineeName());
		info.put("nickName", traineeInfo.getNickName());
		info.put("traineeSex", traineeSex);
		info.put("traineeSexName", traineeInfo.getTraineeSex());
		info.put("mobile", traineeInfo.getMobile());
		info.put("traineePic", traineePic);
		info.put("traineeSchool", traineeInfo.getTraineeSchool());
		info.put("traineePic", traineePic);
		info.put("jobNumber", traineeInfo.getJobNumber());
		// TODO 该字段没有随之更新，替换为实时统计
		info.put("empiricalValue", handleEmpiricalValue(traineeInfo));
		info.put("email", traineeInfo.getEmail());
		info.put("traineeQq", traineeInfo.getTraineeQq());
		info.put("traineeEducation", traineeInfo.getTraineeEducation());
		return info;
	}

	/**
	 * 其它信息（学生）
	 * @param traineeInfo
	 * @return
	 */
	private Map<String, Object> toGetCharmTraineeInfo(TevglTraineeInfo traineeInfo) {
		Map<String, Object> info = new HashMap<>();
		// 参加云课堂数
		//String cloudClassroomNum = "0";
		info.clear();
		info.put("traineeId", traineeInfo.getTraineeId());
		info.put("state", "Y");
		List<TevglTchClassroomTrainee> list = tevglTchClassroomTraineeMapper.selectListByMap(info);
		long count = list.stream().map(a -> a.getCtId()).distinct().count();
		// 添加返回数据
		info.put("cloudClassroomNum", count);
		info.put("studentNum", 0);
		info.put("resourceNum", 0);
		info.put("activityNum", 0);
		info.put("blogsNum", StrUtils.isNull(traineeInfo.getBlogsNum()) ? 0 : traineeInfo.getBlogsNum());
		info.put("charmValue", 0);
		info.put("empiricalValue", handleEmpiricalValue(traineeInfo));
		return info;
	}

	/**
	 * 学员列表
	 * @param params
	 * @return
	 */
	@Override
	public R listTrainee(Map<String, Object> params) {
		Query query = new Query(params);
		List<Map<String,Object>> list = tevglTraineeInfoMapper.selectSimpleListByMap(query);
		list.stream().forEach(trainee -> {
			trainee.put("traineePic", uploadPathUtils.stitchingPath(trainee.get("traineePic"), "16"));
		});
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 修改密码
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param confimPwd 确保新密码与旧密码一致
	 * @param logUserId 当前要修改密码的人
	 * @return
	 */
	@Override
	public R updatePassword(String oldPwd, String newPwd, String confimPwd, String logUserId) {
		// 验证
		oldPwd = oldPwd.trim();
		newPwd = newPwd.trim();
		confimPwd = newPwd.trim();
		if (StrUtils.isEmpty(oldPwd.trim())) {
			return R.error("原始密码不能为空");
		}
		if (oldPwd.length() > 16) {
			return R.error("原密码长度不能超过16位");
		}
		if (StrUtils.isEmpty(newPwd.trim()) || StrUtils.isEmpty(confimPwd.trim())) {
			return R.error("新密码以及确认密码也不能为空");
		}
		if (newPwd.length() > 16 || confimPwd.length() > 16) {
			return R.error("新密码或确认密码，长度不能超过16位");
		}
		if (!newPwd.equals(confimPwd)) {
			return R.error("新密码与确认密码不一致");
		}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(logUserId);
		// 如果没有登录提示，如果登录了且原始密码和数据库中存在的密码不一致也要提示
		if (traineeInfo == null) {
			return R.error("未登录，无法修改密码");
		}else {
			// 将数据库的密码解密进行校验
			String originalPassword = TicketDesUtil.encryptWithMd5(oldPwd, traineeInfo.getUserYan());
			if (!originalPassword.equals(traineeInfo.getUserPasswd())) {
				return R.error("原始密码错误，请重新输入");
			}
		}
		// 如果密码都不为空则修改
		if (traineeInfo != null) {
			TevglTraineeInfo userInfo = new TevglTraineeInfo();
			if (StrUtils.isNotEmpty(newPwd)) {
				String salt = RandomStringUtils.randomAlphanumeric(24);
				userInfo.setUserPasswd(TicketDesUtil.encryptWithMd5(newPwd, salt));  // 修改后的密码
				userInfo.setUserYan(salt);  										 // 加密盐
				userInfo.setTraineeId(logUserId);
				tevglTraineeInfoMapper.update(userInfo);
			}
		}
		return R.ok("成功修改密码");
	}
	
	/**
	 * 修改个人信息
	 *
	 * @param vo
	 * @return
	 */
	@SysLog(value="修改个人信息")
	@Override
	public R updatePersonInfo(TevglTraineeInfoUpdateVo vo) {
		if (StrUtils.isEmpty(vo.getTraineeName())) {
			vo.setTraineeName(vo.getTeacherName());
		}
		ValidatorUtils.check(vo);
		TevglTraineeInfo t = new TevglTraineeInfo();
		t.setTraineeId(vo.getTraineeId());
		t.setTraineeName(vo.getTraineeName());
		t.setNickName(vo.getNickName());
		t.setTraineeSex(vo.getTraineeSex());
		t.setJobNumber(vo.getJobNumber());
		t.setEmail(vo.getEmail());
		tevglTraineeInfoMapper.update(t);
		// 如果该用户是教师
		TevglTchTeacher existedTevglTchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(vo.getTraineeId());
		if (existedTevglTchTeacher != null) {
			TevglTchTeacher o = new TevglTchTeacher();
			o.setTeacherId(existedTevglTchTeacher.getTeacherId());
			o.setTeacherName(vo.getTeacherName());
			o.setSex(vo.getTraineeSex());
			o.setJobNumber(vo.getJobNumber());
			o.setTeacherErtificateNumber(vo.getTeacherErtificateNumber());
			tevglTchTeacherMapper.update(o);
		}
		return R.ok("保存成功");
	}

	/**
	 * 根据条件查询，不在任何班级里面的学员（学员：账号有效，且绑定了手机号码）
	 * @return
	 */
	@Override
	public List<TevglTraineeInfoVo> findTraineeListNotInClass(Map<String, Object> map) {
		List<TevglTraineeInfoVo> traineeList = tevglTraineeInfoMapper.findTraineeListNotInClass(map);
		if (traineeList != null && traineeList.size() > 0) {
			traineeList.stream().forEach(trainee -> {
				trainee.setTraineePic(uploadPathUtils.stitchingPath(trainee.getTraineePic(), "16"));
			});
		}
		return traineeList;
	}

	/**
	 * 根据条件，分页查询，不在任何班级里面的学员
	 * @param map
	 * @return
	 */
	@Override
	public R findTraineeListNotInClassWithPage(Map<String, Object> map) {
		Query query = new Query(map);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TevglTraineeInfoVo> list = this.findTraineeListNotInClass(query);
		PageUtils pageUtil = new PageUtils(list, query.getLimit(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

}
