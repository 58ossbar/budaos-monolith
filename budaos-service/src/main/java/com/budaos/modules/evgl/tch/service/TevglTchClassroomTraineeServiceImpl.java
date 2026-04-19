package com.budaos.modules.evgl.tch.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.common.enums.CommonEnum;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninTrainee;
import com.budaos.modules.evgl.activity.persistence.TevglActivitySigninTraineeMapper;
import com.budaos.modules.evgl.empirical.persistence.TevglEmpiricalLogChapterMapper;
import com.budaos.modules.evgl.forum.persistence.TevglForumBlogPostMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResgroupMapper;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomTraineeService;
import com.budaos.modules.evgl.tch.domain.*;
import com.budaos.modules.evgl.tch.persistence.*;
import com.budaos.modules.evgl.tch.vo.TevglTchClassroomTraineeVo;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeEmpiricalValueLog;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeEmpiricalValueLogMapper;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.im.domain.TimChatList;
import com.budaos.modules.im.domain.TimGroup;
import com.budaos.modules.im.domain.TimGroupUser;
import com.budaos.modules.im.service.TimChatListService;
import com.budaos.modules.im.service.TimGroupService;
import com.budaos.modules.im.service.TimGroupUserService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * <p>
 * Title: 课堂成员
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglTchClassroomTraineeServiceImpl implements TevglTchClassroomTraineeService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomTraineeServiceImpl.class);
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	// 访问地址
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	
	// 文件上传路径
	@Value("${com.budaos.file-upload-path}")
    private String uploadPath;
	
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClasstraineeMapper tevglTchClasstraineeMapper;
	@Autowired
	private TevglTchClassroomTraineeCheckMapper tevglTchClassroomTraineeCheckMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TimGroupUserService timGroupUserService;
	@Autowired
	private TimChatListService timChatListService;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private TimGroupService timGroupService;
	@Autowired
	private TevglTchClassroomGroupMapper tevglTchClassroomGroupMapper;
	@Autowired
	private TevglTchClassroomGroupMemberMapper tevglTchClassroomGroupMemberMapper;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private TevglTraineeEmpiricalValueLogMapper tevglTraineeEmpiricalValueLogMapper;
	@Autowired
	private TevglTchRoomPereTraineeAnswerMapper tevglTchRoomPereTraineeAnswerMapper;
	@Autowired
	private TevglForumBlogPostMapper tevglForumBlogPostMapper;
	@Autowired
	private TevglActivitySigninTraineeMapper tevglActivitySigninTraineeMapper;
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglEmpiricalLogChapterMapper tevglEmpiricalLogChapterMapper;
	@Autowired
	private TevglTchClassroomSettingMapper tevglTchClassroomSettingMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	@Autowired
	private ConvertUtil convertUtil;
 
	/**
	 * 查询列表(返回List<Bean>)
	 * 
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TevglTchClassroomTrainee> tevglTchClassroomTraineeList = tevglTchClassroomTraineeMapper
				.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomTraineeList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * 
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tevglTchClassroomTraineeList = tevglTchClassroomTraineeMapper
				.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomTraineeList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 新增
	 * 
	 * @param tevglTchClassroomTrainee
	 * @throws BudaosException
	 */
	@SysLog(value = "新增")
	public R save(TevglTchClassroomTrainee tevglTchClassroomTrainee)
			throws BudaosException {
		tevglTchClassroomTrainee.setId(Identities.uuid());
		ValidatorUtils.check(tevglTchClassroomTrainee);
		tevglTchClassroomTraineeMapper.insert(tevglTchClassroomTrainee);
		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param tevglTchClassroomTrainee
	 * @throws BudaosException
	 */
	@SysLog(value = "修改")
	public R update( TevglTchClassroomTrainee tevglTchClassroomTrainee)
			throws BudaosException {
		ValidatorUtils.check(tevglTchClassroomTrainee);
		tevglTchClassroomTraineeMapper.update(tevglTchClassroomTrainee);
		return R.ok();
	}

	/**
	 * 单条删除
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "单条删除")
	public R delete(String id) throws BudaosException {
		tevglTchClassroomTraineeMapper.delete(id);
		return R.ok();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value = "批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTchClassroomTraineeMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomTraineeMapper.selectObjectById(id));
	}
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	@Override
	public List<TevglTchClassroomTrainee> selectListByMap(Map<String, Object> map) {
		return tevglTchClassroomTraineeMapper.selectListByMap(map);
	}

	/**
	 * 课堂成员列表公共方法， 根据条件查询记录，返回List<Map>, 且对象为驼峰命名
	 * @param params
	 * @param loginUserId
	 * @return
	 * @apiNote { id主键，ctId课堂主键，classId班级主键，joinDate加入日期，createTime创建时间，
	 *          traineeId学员头像，traineePic学员头像，traineeName学员名称，state状态Y有效N无效 }
	 */
	@Override
	public R selectListMapForWeb( Map<String, Object> params, String loginUserId) {
		String ctId = (String) params.get("ctId");
		if (StrUtils.isEmpty(ctId)) {
			return R.error("参数课堂主键ctId为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		// 处理排序条件
		handleSidxOrder(ctId, params);
		// 只展示状态为有效的成员
		//params.put("state", "Y");
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		log.debug("排序条件 {}", query);
		List<Map<String, Object>> list = tevglTchClassroomTraineeMapper.selectListMapForWeb(query);
		convertUtil.convertDict(list, "traineeSexName", "sex");
		// 当前登录用户是否为
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isRoomReveiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getReceiverUserId().equals(loginUserId);
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		list.stream().forEach(a -> {
			// 头像处理
			String traineePic = (String) a.get("traineePic");
			if (StrUtils.isNotEmpty(traineePic) && !"null".equals(traineePic)) {
				a.put("traineePic", uploadPathUtils.stitchingPath(traineePic, "16"));
			}
			// 显示下助教
			if (a.get("identity") != null) {
				a.put("stateName", a.get("identity"));
				a.remove("identity");
				a.put("ifTa", true);
			}
			// 数据脱敏
			if (!StrUtils.isNull(a.get("mobile")) && (isRoomCreator || isTeachingAssistant || isRoomReveiver)) {
				String mobile = a.get("mobile").toString();
				//mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
				a.put("mobile", mobile);
			} else {
				a.remove("mobile");
			}
		});
		if (StrUtils.notNull(params.get("queryPermission")) && "Y".equals(params.get("queryPermission"))) {
			PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
			Map<String, Object> licenseMap = getLicenseMap(tevglTchClassroom, loginUserId);
			return R.ok().put(Constant.R_DATA, pageUtil).put("license", licenseMap);
		}
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 处理排序条件
	 * @param ctId
	 * @param params
	 */
	private void handleSidxOrder(String ctId, Map<String, Object> params) {
		// 默认排序条件,优先展示未审核,助教
		params.put("sidx", "t1.state asc, identity desc");
		params.put("order", ", empirical_value desc, traineeName desc, t1.id desc");
		// 如果设置了排序规则
		/*Map<String, Object> map = new HashMap<>();
		map.put("ctId", ctId);
		map.put("modules", GlobalRoomSetting.MDULES_CLASSROOM_TRAINEE);
		List<TevglTchClassroomSetting> list = tevglTchClassroomSettingMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			String sidx = "t1.state asc, identity desc, " + getField(list.get(0).getSidx()) + " " + list.get(0).getOrder();
			params.put("sidx", sidx);
			params.remove("order");
		}*/
	}
	
	private String getField(String sidx){
		Map<String, Object> map = new HashMap<>();
		map.put("1", "empirical_value");
		map.put("2", "t2.trainee_name");
		map.put("3", "t2.mobile");
		map.put("4", "t1.create_time");
		return map.get(sidx).toString();
	}
	
	/**
	 * 处理权限标识
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @return
	 */
	private Map<String, Object> getLicenseMap(TevglTchClassroom tevglTchClassroom, String loginUserId){
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("ctId", tevglTchClassroom.getCtId());
		List<String> permissionList = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(info);
		info.put(GlobalRoomPermission.ADD_TRAINEE_INFO_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ADD_TRAINEE_INFO, permissionList));
		info.put(GlobalRoomPermission.CHECK_TRAINEE_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.CHECK_TRAINEEA, permissionList));
		info.put(GlobalRoomPermission.DELETE_TRAINEE_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.DELETE_TRAINEE, permissionList));
		info.put(GlobalRoomPermission.EDIT_TRAINEE_INFO_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.EDIT_TRAINEE_INFO, permissionList));
		return info;
	}

	/**
	 * 加入课堂
	 * 
	 * @param map
	 * @return
	 * @apiNote map的key需要如下值 { ctId:课堂主键，invitationCode邀请码 }
	 */
	@Override
	public R joinTheClassroom( Map<String, Object> map, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 满足ctId加入课堂和根据邀请码和创建时间加入课堂
		String ctId = "";
		TevglTchClassroom classroomInfo = null;
		if (map.get("ctId") == null || "null".equals(map.get("ctId"))) {
			if (map.get("invitationCode") == null || map.get("createTime") == null) {
				return R.error("必传参数invitationCode或createTime为空");
			}
			Map<String, Object> ps = new HashMap<String, Object>();
			// 邀请码
			ps.put("invitationCode", map.get("invitationCode"));
			// 创建时间，sql中切记不要使用模糊查，格式严格要求为yyyyMMddHHmmss
			if (map.get("createTime").toString().length() != 14) {
				return R.error("日期格式不符合");
			}
			ps.put("createTime", handleTime(map.get("createTime").toString()));
			List<TevglTchClassroom> list = tevglTchClassroomMapper.selectListByMap(ps);
			if (list != null && list.size() > 0) {
				ctId = list.get(0).getCtId();
			}
			log.debug("ctId:" + ctId);
		} else {
			if (map.get("ctId") == null) {
				return R.error("必传参数ctId为空");
			}
			ctId = (String) map.get("ctId");
			log.debug("根据课堂主键"+ctId+"加入课堂");
			classroomInfo = tevglTchClassroomMapper.selectObjectById(ctId);
		}
		classroomInfo = tevglTchClassroomMapper.selectObjectById(ctId);
		// 合法性校验
		R r = checkIsPass(classroomInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("traineeId", loginUserId);
		List<TevglTchClassroomTrainee> list = tevglTchClassroomTraineeMapper.selectListByMap(params);
		log.debug("用户"+loginUserId+",是否加入了课堂：" + ctId+" => " + list.size());
		// 如果已经加入课堂了
		if (list != null && list.size() > 0) {
			if ("Y".equals(list.get(0).getState())) {
				return R.ok("已加入课堂").put(Constant.R_DATA, getNeedReturnData(classroomInfo));
			}
		}
		// 如果加入课堂需要审核
		if ("Y".equals(classroomInfo.getIsCheck())) {
			return doSetTraineeToClassroom(ctId, classroomInfo.getName(), loginUserId, classroomInfo.getClassId(), classroomInfo.getCreateUserId());
		}
		TevglTchClassroomTrainee t = createClassroomTrainee(ctId, loginUserId, classroomInfo.getClassId());
		String msg = "";
		// 如果未加入该课堂
		if (list == null || list.size() == 0) {
			// 课堂成员信息入库
			tevglTchClassroomTraineeMapper.insert(t);
			msg = "成功加入课堂";
			// 将此课堂学习人数加一
			cbRoomUtils.plusStudyNum(ctId);
			// 即时通讯
			sendIm(ctId, loginUserId, classroomInfo, params);
			// 同步维护到班级成员表
			cbRoomUtils.doHandleClassTraineeTable(loginUserId, classroomInfo.getClassId(), params);
		} else {
			TevglTchClassroomTrainee classroomTrainee = list.get(0);
			if (classroomTrainee != null) {
				classroomTrainee.setJoinDate(DateUtils.getNow("yyyy-MM-dd"));
				classroomTrainee.setCreateTime(DateUtils.getNowTimeStamp());
				classroomTrainee.setState("Y");
				classroomTrainee.setClassId(classroomInfo.getClassId());
				tevglTchClassroomTraineeMapper.update(classroomTrainee);
			}
			msg = "已加入课堂";
		}
		// 返回数据，满足小程序端需要
		Map<String, Object> data = getNeedReturnData(classroomInfo);
		return R.ok(msg).put(Constant.R_DATA, data);
	}
	
	/**
	 * 加入课堂时的合法性校验
	 * @param classroomInfo
	 * @param loginUserId
	 * @return
	 */
	private R checkIsPass(TevglTchClassroom classroomInfo, String loginUserId) {
		if (classroomInfo == null) {
			return R.error("无效的记录，无法加入");
		}
		if (!"Y".equals(classroomInfo.getState())) {
			return R.error("无效的课堂，无法加入");
		}
		/*if ("1".equals(classroomInfo.getClassroomState())) {
			return R.error("课堂未开始，不允许加入");
		}*/
		if ("3".equals(classroomInfo.getClassroomState())) {
			return R.error("课堂已结束，已不允许加入");
		}
		// 如果课堂没有被移交
		if (StrUtils.isEmpty(classroomInfo.getReceiverUserId())) {
			// 创建者自己不让加入
			if (loginUserId.equals(classroomInfo.getCreateUserId())) {
				return R.error("您是此课堂创建者，无需加入");
			}
		}
		// 如果课堂被移交了
		if (StrUtils.isNotEmpty(classroomInfo.getReceiverUserId())) {
			// 接收者自己不让加入
			if (loginUserId.equals(classroomInfo.getReceiverUserId())) {
				return R.error("您是此课堂接收者，无需加入");
			}
		}
		return R.ok();
	}
	
	/**
	 * 加入课堂后的即时通讯处理
	 * @param ctId 当前申请加入的课堂
	 * @param loginUserId 当前登录用户
	 * @param classroomInfo 当前课堂的信息
	 * @param params 查询条件
	 */
	private void sendIm(String ctId, String loginUserId, TevglTchClassroom classroomInfo, Map<String, Object> params) {
		// 获取Tioconfig
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		// 加入课堂群
		joinTimGroup(ctId, loginUserId);
		// 绑定课堂群
		Tio.bindGroup(tioConfig, loginUserId, ctId);
		// 更新聊天列表记录
		timChatListService.save(null, loginUserId, ctId, "2", 0, "", true);
		// 获取课堂的活动群
		Map<String, String> mm = getActivityMap(classroomInfo, params);
		if (mm != null) {
			for (Entry<String, String> o : mm.entrySet()) {
				// 加入各种活动群,成为群成员
				String activityId = o.getKey();
				String activityType = o.getValue();
				TimGroupUser user = new TimGroupUser();
				user.setGroupuserId(Identities.uuid());
				user.setGroupId(activityId);
				user.setUserId(loginUserId);
				user.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
				timGroupUserService.save(user);
				// 更新活动群人数
				TimGroup timGroup = new TimGroup();
				timGroup.setGroupId(activityId);
				timGroup.setGroupNum(1);
				timGroupService.plusNum(timGroup);
				// 将用户绑定到活动群组
				Tio.bindGroup(tioConfig, loginUserId, activityId);
				// 继续更新聊天列表数据
				if ("3".equals(activityType)) {
					timChatListService.save(null, loginUserId, activityId, "2", 0, "", true, "1", activityId);
				} else {
					timChatListService.save(null, loginUserId, activityId, "2", 0, "", true);	
				}
			}
		}
	}
	
	/**
	 * 返回数据，满足小程序端需要
	 * @param classroomInfo
	 * @return
	 */
	private Map<String, Object> getNeedReturnData(TevglTchClassroom classroomInfo){
		Map<String, Object> data = new HashMap<>();
		data.put("ctId", classroomInfo.getCtId());
		data.put("pkgId", classroomInfo.getPkgId());
		data.put("subjectId", null);
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(classroomInfo.getPkgId());
		if (tevglPkgInfo != null) {
			data.put("subjectId", tevglPkgInfo.getSubjectId());	
		}
		data.put("classId", classroomInfo.getClassId());
		return data;
	}
	
	/**
	 * 获取该课堂的活动
	 * @param tchClassroom
	 * @param params
	 * @return
	 */
	private Map<String, String> getActivityMap(TevglTchClassroom tchClassroom, Map<String, Object> params){
		if (tchClassroom == null) {
			return null;
		}
		params.clear();
		params.put("pkgId", tchClassroom.getPkgId());
		params.put("activityType", GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		params.put("activityState", "1"); // 实际活动状态(0未开始1进行中2已结束)
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			//return list.stream().collect(Collectors.toMap(TevglPkgActivityRelation::getActivityId, TevglPkgActivityRelation::getActivityType));
			return list.stream().collect(Collectors.toMap(TevglPkgActivityRelation::getGroupId, TevglPkgActivityRelation::getActivityType));
		}
		return null;
	}
	
	/**
	 * 构建一个课堂成员的实体类信息
	 * @param ctId
	 * @param loginUserId
	 * @param classId
	 * @return
	 */
	private TevglTchClassroomTrainee createClassroomTrainee(String ctId, String loginUserId, String classId) {
		// 填充信息
		TevglTchClassroomTrainee t = new TevglTchClassroomTrainee();
		t.setId(Identities.uuid());
		t.setCtId(ctId);
		t.setTraineeId(loginUserId);
		t.setClassId(classId);
		t.setJoinDate(new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getNowDate()));
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setState("Y");
		return t;
	}
	
	/**
	 * 加入
	 * @param ctId 以课堂主键作为群聊主键
	 * @param loginUserId
	 */
	private void joinTimGroup(String ctId, String loginUserId) {
		List<TimGroupUser> list = timGroupUserService.selectListByGroupId(ctId);
		// 如果已经是群成员了
		if (list.stream().anyMatch(a -> a.getUserId().equals(loginUserId))) {
			return;
		}
		TimGroupUser user = new TimGroupUser();
		user.setGroupId(ctId);
		user.setGroupuserId(Identities.uuid());
		user.setUserId(loginUserId);
		user.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
		timGroupUserService.save(user);
		// 更新课堂群人数
		TimGroup timGroup = new TimGroup();
		timGroup.setGroupId(ctId);
		timGroup.setGroupNum(1);
		timGroupService.plusNum(timGroup);
	}
	
	private void joinTimGroupV2(TevglTchClassroom tevglTchClassroom, String loginUserId) {
		if (tevglTchClassroom == null || StrUtils.isEmpty(loginUserId)) {
			return;
		}
		String ctId = tevglTchClassroom.getCtId();
		// 如果课堂没有被移交，不允许课堂创建者加入群里
		if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
			// 如果登录用户是创建者，直接返回，不再执行后续代码
			if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
				return;
			}
		}
		// 如果课堂被移交了
		if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
			
		}
		List<TimGroupUser> list = timGroupUserService.selectListByGroupId(ctId);
		// 如果已经是群成员了
		if (list.stream().anyMatch(a -> a.getUserId().equals(loginUserId))) {
			return;
		}
		TimGroupUser user = new TimGroupUser();
		user.setGroupId(ctId);
		user.setGroupuserId(Identities.uuid());
		user.setUserId(loginUserId);
		user.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
		timGroupUserService.save(user);
		// 更新课堂群人数
		TimGroup timGroup = new TimGroup();
		timGroup.setGroupId(ctId);
		timGroup.setGroupNum(1);
		timGroupService.plusNum(timGroup);
	}
	
	/**
	 * 处理一下字符串
	 * @param time 生成小程序课堂二维码时的创建时间参数，格式严格要求为：yyyyMMddHHmmss
	 * @return 处理后返回格式：yyyy-MM-dd HH:mm:ss
	 */
	private String handleTime(String time) {
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		String hour = time.substring(8, 10);
		String minute = time.substring(10, 12);
		String second = time.substring(12, 14);
		return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
	}
	
	/**
	 * 如果加入班级需要审核
	 * @param ctId 必传参数，课堂主键
	 * @param name 课堂名称
	 * @param loginUserId 必传参数，课堂主键
	 * @param classId
	 * @return
	 */
	private R doSetTraineeToClassroom(String ctId, String name, String loginUserId, String classId, String createUserId) {
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("ctId", ctId);
		ps.put("traineeId", loginUserId);
		ps.put("isPass", "N"); // 未通过，待审核的
		List<TevglTchClassroomTraineeCheck> list = tevglTchClassroomTraineeCheckMapper.selectListByMap(ps);
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", 520);
		if (list != null && list.size() > 0) {
			res.put("msg", "你已经申请过加入课堂【"+name+"】，请耐心等待此课堂老师审核通过...");
			return R.ok().put(Constant.R_DATA, res);
		} else {
			// 课堂成员插入一条状态无效的记录,用于后续更新为有效
			TevglTchClassroomTrainee classroomTrainee = createClassroomTrainee(ctId, loginUserId, classId);
			classroomTrainee.setState("N");
			tevglTchClassroomTraineeMapper.insert(classroomTrainee);
			// 审核表插入一条记录
			TevglTchClassroomTraineeCheck tc = new TevglTchClassroomTraineeCheck();
			tc.setTcId(Identities.uuid());
			tc.setCtId(ctId);
			tc.setTraineeId(loginUserId);
			tc.setCreateTime(DateUtils.getNowTimeStamp());
			tc.setIsPass("N");
			tevglTchClassroomTraineeCheckMapper.insert(tc);
			res.put("msg", "由于加入此课堂需要审核，请等待课堂创建者批准...");
			// 推送消息给课堂创建者
			ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
			JSONObject msg = new JSONObject();
			msg.put("busitype", "joinclassroom");
			JSONObject busiMsg = new JSONObject();
			busiMsg.put("ctId", ctId);
			busiMsg.put("title", "当前有人申请了加入您的课堂【"+name+"】，记得去审核通过吧~");
			msg.put("msg", busiMsg);
			CBImUtils.sendToUser(tioConfig, createUserId, msg);
			return R.ok().put(Constant.R_DATA, res);
		}
	}

	/**
	 * 删除课堂成员
	 * 
	 * @param map
	 * @return
	 * @apiNote 必传参数：id课堂成员表主键，ctId课堂表主键，traineeId被删除的学员，loginUserId当前登录用户
	 */
	@Override
	public R deleteClassroomTrainee(Map<String, Object> map) throws BudaosException {
		String id = (String) map.get("id"); // 课堂成员表主键
		String ctId = (String) map.get("ctId"); // 课堂表主键
		String traineeId = (String) map.get("traineeId"); // 被删除的人
		String loginUserId = (String) map.get("loginUserId"); // 当前登录用户
		// 合法性校验
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 如果当前该课堂不属于当前登录，不允许删除课堂成员
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.DELETE_TRAINEE);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		Map<String, Object> params = new HashMap<>();
		TevglTchClassroomTrainee classroomTrainee = tevglTchClassroomTraineeMapper.selectObjectById(id);
		if (classroomTrainee != null) {
			// 状态为有效的才去更新课堂人数
			if ("Y".equals(classroomTrainee.getState())) {
				// 更新课堂学习人数
				cbRoomUtils.plusStudyNum(ctId, -1);
			}
			// 找到此课堂的所有小组
			params.clear();
			params.put("ctId", ctId);
			List<TevglTchClassroomGroup> classroomGroupList = tevglTchClassroomGroupMapper.selectListByMap(params);
			if (classroomGroupList != null && classroomGroupList.size() > 0) {
				List<String> gpIds = classroomGroupList.stream().map(a -> a.getGpId()).collect(Collectors.toList());
				params.clear();
				params.put("gpIds", gpIds);
				List<TevglTchClassroomGroupMember> list = tevglTchClassroomGroupMemberMapper.selectListByMap(params);
				for (TevglTchClassroomGroupMember member : list) {
					// 将此人从小组移出
					if (traineeId.equals(member.getTraineeId())) {
						// 移除小组
						tevglTchClassroomGroupMemberMapper.delete(member.getGmId());
						// 小组人数-1
						TevglTchClassroomGroup group = new TevglTchClassroomGroup();
						group.setGpId(member.getGpId());
						group.setNumber(-1);
						tevglTchClassroomGroupMapper.plusNum(group);
					}
				}
			}
		}
		// 删除课堂成员
		tevglTchClassroomTraineeMapper.delete(id);
		// 删除审核记录表
		params.clear();
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		List<TevglTchClassroomTraineeCheck> classroomTraineeCheckList = tevglTchClassroomTraineeCheckMapper.selectListByMap(params);
		if (classroomTraineeCheckList != null && classroomTraineeCheckList.size() > 0) {
			classroomTraineeCheckList.stream().forEach(a -> {
				tevglTchClassroomTraineeCheckMapper.delete(a.getTcId());
			});
		}
		// 如果此人是助教
		if (StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId())) {
			if (tevglTchClassroom.getTraineeId().equals(traineeId)) {
				tevglTchClassroom.setTraineeId(null);
				tevglTchClassroomMapper.update(tevglTchClassroom);
			}	
		}
		// TODO 将此人从课堂群、答疑讨论群等中移除
		removeFromImGroup(tevglTchClassroom, traineeId);
		// 删除班级成员(不删除，需求需要的话放开即可)
		/*
		params.clear();
		params.put("classId", tevglTchClassroom.getClassId());
		params.put("traineeId", traineeId);
		List<TevglTchClasstrainee> list = tevglTchClasstraineeMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			tevglTchClasstraineeMapper.delete(list.get(0).getCtId());
		}
		*/
		// 推送消息告知此人被移出课堂
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		JSONObject msg = new JSONObject();
		msg.put("busitype", "removeoutroom");
		msg.put("msgtype", "alert");
		JSONObject busitype = new JSONObject();
		busitype.put("ctId", ctId);
		busitype.put("ct_id", ctId);
		busitype.put("title", "你已被移出课堂【"+tevglTchClassroom.getName()+"】");
		msg.put("msg", busitype);
		CBImUtils.sendToUser(tioConfig, traineeId, msg);
		return R.ok("删除成功");
	}
	
	/**
	 * 将此人从群里移除
	 * @param traineeId
	 */
	private void removeFromImGroup(TevglTchClassroom tevglTchClassroom, String traineeId) {
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 将["+traineeId+"]，从即时通讯群里面移除 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		String ctId = tevglTchClassroom.getCtId();
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId)) {
			return;
		}
		// 存放群id
		List<String> groupIdList = new ArrayList<String>();
		groupIdList.add(ctId);
		// 等待被删的群成员主键
		List<String> needDelGroupuserId = new ArrayList<String>();
		// 等待被删除的聊天列表
		List<String> neewDelChatListId = new ArrayList<String>();
		// 找用户
		Map<String, Object> params = new HashMap<>();
		params.put("userId", traineeId);
		List<TimGroupUser> timGroupUserList = timGroupUserService.selectListByMap(params);
		// 找到这个人在的课堂群聊
		if (timGroupUserList.size() > 0) {
			List<TimGroupUser> collect = timGroupUserList.stream().filter(a -> a.getGroupId().equals(ctId)).collect(Collectors.toList());
			if (collect.size() > 0) {
				needDelGroupuserId.add(collect.get(0).getGroupuserId());
			}
		}
		// 从答疑讨论群里删除
		Map<String, String> mm = getActivityMap(tevglTchClassroom, params);
		if (mm != null) {
			for (Entry<String, String> o : mm.entrySet()) {
				String groupId = o.getKey();
				String activityType = o.getValue();
				groupIdList.add(groupId);
				// 取出当前群用户
				if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) {
					List<TimGroupUser> list = timGroupUserList.stream().filter(a -> a.getGroupId().equals(groupId)).collect(Collectors.toList());
					if (list.size() > 0) {
						List<String> idList = list.stream().map(a -> a.getGroupuserId()).distinct().collect(Collectors.toList());
						needDelGroupuserId.addAll(idList);
					}
				}
			}
		}
		// 找到这个人与群的聊天列表
		params.clear();
		params.put("userId", traineeId);
		params.put("friendType", "2");
		List<TimChatList> chatList1 = timChatListService.selectListByMap(params);
		if (chatList1.size() > 0) {
			for (String groupId : groupIdList) {
				List<String> idList = chatList1.stream().filter(a -> a.getFriendId().equals(groupId)).map(a -> a.getListId()).collect(Collectors.toList());
				if (idList.size() > 0) {
					neewDelChatListId.addAll(idList);
				}
			}
		}
		// 找到这个群的与人的聊天列表
		params.clear();
		params.put("userIds", groupIdList);
		params.put("friendType", "2");
		List<TimChatList> chatList2 = timChatListService.selectListByMap(params);
		if (chatList2.size() > 0) {
			List<String> idList = chatList2.stream().filter(a -> a.getFriendId().equals(traineeId)).map(a -> a.getListId()).collect(Collectors.toList());
			if (idList.size() > 0) {
				neewDelChatListId.addAll(idList);
			}
		}
		if (needDelGroupuserId.size() > 0) {
			timGroupUserService.deleteBatch(needDelGroupuserId.stream().toArray(String[]::new));
		}
		if (neewDelChatListId.size() > 0) {
			timChatListService.deleteBatch(neewDelChatListId.stream().toArray(String[]::new));
		}
		if (groupIdList.size() > 0) {
			groupIdList.stream().forEach(groupId -> {
				TimGroup timGroup = new TimGroup();
				timGroup.setGroupId(groupId);
				timGroup.setGroupNum(-1);
				timGroupService.plusNum(timGroup);
			});
		}
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 将["+traineeId+"]，从即时通讯群里面移除 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}

	/**
	 * 批量删除
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteClassroomTraineeBatch(JSONObject jsonObject, String loginUserId) {
		String ctId = jsonObject.getString("ctId");
		JSONArray ids = jsonObject.getJSONArray("ids"); // t_evgl_tch_classroom_trainee表的主键
		// 合法性校验
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 如果当前该课堂不属于当前登录，不允许删除课堂成员
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的记录，删除失败");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.DELETE_TRAINEE);
		if (!hasOperationBtnPermission) {
			return R.error("没有权限，删除失败");
		}
		// 推送消息告知此人被移出课堂
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		JSONObject msg = new JSONObject();
		msg.put("busitype", "removeoutroom");
		msg.put("msgtype", "alert");
		JSONObject busitype = new JSONObject();
		busitype.put("ctId", ctId);
		busitype.put("ct_id", ctId);
		busitype.put("title", "你已被移出课堂【"+tevglTchClassroom.getName()+"】");
		msg.put("msg", busitype);
		int studyNum = 0; // 等待被减的课堂学习人数
		// 查出此课堂所有成员
		Map<String, Object> map = new HashMap<>();
		map.put("ctId", ctId);
		List<TevglTchClassroomTrainee> allClassroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(map);
		// 找到此课堂的所有小组
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		List<TevglTchClassroomGroup> classroomGroupList = tevglTchClassroomGroupMapper.selectListByMap(params);
		// 小组成员
		List<TevglTchClassroomGroupMember> groupMemberList = new ArrayList<>();
		if (classroomGroupList != null && classroomGroupList.size() > 0) {
			List<String> gpIds = classroomGroupList.stream().map(a -> a.getGpId()).collect(Collectors.toList());
			params.clear();
			params.put("gpIds", gpIds);
			groupMemberList = tevglTchClassroomGroupMemberMapper.selectListByMap(params);
		}
		// 该课堂成员的审核记录
		params.clear();
		params.put("ctId", ctId);
		List<TevglTchClassroomTraineeCheck> allClassroomTraineeCheckList = tevglTchClassroomTraineeCheckMapper.selectListByMap(params);
		// 查询课堂的班级成员
		params.clear();
		params.put("classId", tevglTchClassroom.getClassId());
		List<TevglTchClasstrainee> allClassTraineeList = tevglTchClasstraineeMapper.selectListByMap(params);
		// 等待批量删除的课堂成员表主键id
		List<String> deleteRoomTraineeIdList = new ArrayList<>();
		// 等待操作的集合
		List<String> deleteCheckIdList = new ArrayList<>();
		// 等待删除的班级成员表主键id
		List<String> deleteClassTraineeIdList = new ArrayList<>();
		// 等待删除的课堂小组成员表主键id
		List<String> deleteGroupMemberIdList = new ArrayList<>();
		// 等待更新的课堂小组成员人数
		List<TevglTchClassroomGroup> updateGroupList = new ArrayList<>();
		// 遍历课堂成员表主键
		for (int i = 0; i < ids.size(); i++) {
			String id = ids.getString(i);
			List<TevglTchClassroomTrainee> classroomTraineeList = allClassroomTraineeList.stream().filter(a -> a.getId().equals(id)).collect(Collectors.toList());
			if (allClassroomTraineeList == null || allClassroomTraineeList.size() == 0) {
				continue;
			}
			// 批量删除课堂成员
			deleteRoomTraineeIdList.add(id);
			// 课堂成员信息
			TevglTchClassroomTrainee classroomTrainee = classroomTraineeList.get(0);
			String traineeId = classroomTrainee.getTraineeId();
			// 状态为有效的才去更新课堂人数
			if ("Y".equals(classroomTrainee.getState())) {
				studyNum ++;
			}
			if (classroomGroupList != null && classroomGroupList.size() > 0) {
				for (TevglTchClassroomGroupMember member : groupMemberList) {
					// 将此人从小组移出
					if (traineeId.equals(member.getTraineeId())) {
						// 移除小组
						deleteGroupMemberIdList.add(member.getGmId());
						// 小组人数-1
						TevglTchClassroomGroup group = new TevglTchClassroomGroup();
						group.setGpId(member.getGpId());
						group.setNumber(-1);
						updateGroupList.add(group);
					}
				}
			}
			// 删除审核记录表
			List<TevglTchClassroomTraineeCheck> classroomTraineeCheckList = allClassroomTraineeCheckList.stream().filter(a -> a.getTraineeId().equals(traineeId)).collect(Collectors.toList());
			if (classroomTraineeCheckList != null && classroomTraineeCheckList.size() > 0) {
				List<String> collect = classroomTraineeCheckList.stream().map(a -> a.getTcId()).collect(Collectors.toList());
				deleteCheckIdList.addAll(collect);
			}
			// 如果此人是助教
			//log.debug("当前学员:" + id + "，是否是课堂["+tevglTchClassroom.getCtId()+"]的助教" + tevglTchClassroom.getTraineeId());
			if (StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId())) {
				if (tevglTchClassroom.getTraineeId().equals(classroomTrainee.getTraineeId())) {
					tevglTchClassroom.setTraineeId(null);
					tevglTchClassroomMapper.update(tevglTchClassroom);
				}
			}
			// 删除班级成员
			List<TevglTchClasstrainee> classTraineeList = allClassTraineeList.stream().filter(a -> a.getTraineeId().equals(traineeId)).collect(Collectors.toList());
			if (classTraineeList != null && classTraineeList.size() > 0) {
				deleteClassTraineeIdList.add(classTraineeList.get(0).getCtId());
			}
			// 推送消息告知此人被移出课堂
			CBImUtils.sendToUser(tioConfig, traineeId, msg);
			// TODO 
			removeFromImGroup(tevglTchClassroom, traineeId);
		}
		// 删除审核记录表数据
		if (deleteCheckIdList.size() > 0) {
			tevglTchClassroomTraineeCheckMapper.deleteBatch(deleteCheckIdList.stream().toArray(String[]::new));
		}
		// 批量删除课堂成员表数据
		if (deleteRoomTraineeIdList.size() > 0) {
			tevglTchClassroomTraineeMapper.deleteBatch(deleteRoomTraineeIdList.stream().toArray(String[]::new));
		}
		// 批量删除班级成员表数据
		if (deleteClassTraineeIdList.size() > 0) {
			// 记得跟单个删除的方法，保持一致即可
			//tevglTchClasstraineeMapper.deleteBatch(deleteClassTraineeIdList.stream().toArray(String[]::new));
		}
		// 批量删除课堂小组成员表数据
		if (deleteGroupMemberIdList.size() > 0) {
			tevglTchClassroomGroupMemberMapper.deleteBatch(deleteGroupMemberIdList.stream().toArray(String[]::new));
		}
		// 批量更新小组人数
		if (updateGroupList.size() > 0) {
			tevglTchClassroomGroupMapper.plusNumBatchByCaseWhen(updateGroupList);
		}
		// 更新课堂学习人数
		TevglTchClassroom t = new TevglTchClassroom();
		t.setCtId(ctId);
		t.setStudyNum(-studyNum);
		tevglTchClassroomMapper.plusNum(t);
		return R.ok("删除成功");
	}
	
	/**
	 * 将班级学员导入成课堂成员
	 * 
	 * @param traineeIds
	 * @return
	 */
	@Override
	public R importTraineeBatch(String ctId, String classId, List<String> traineeIds) throws BudaosException {
		if (StrUtils.isEmpty(classId)) {
			return R.error("参数classId为空");
		}
		if (traineeIds == null || traineeIds.size() == 0) {
			return R.ok("添加成功");
		}
		TevglTchClassroom classroomInfo = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroomInfo == null) {
			return R.error("无效的课堂");
		}
		if (!"Y".equals(classroomInfo.getState())) {
			return R.error("课堂已被删除");
		}
		/*if ("1".equals(classroomInfo.getClassroomState())) {
			return R.error("请先开始课堂，再来添加");
		}*/
		List<String> ids = new ArrayList<String>();
		// 先查询这个班级所有学员
		Map<String, Object> map = new HashMap<>();
		map.put("classId", classId);
		List<Map<String, Object>> classTraineeList = tevglTchClasstraineeMapper.selectListMapForWeb(map);
		for (Map<String, Object> m : classTraineeList) {
			String traineeId = (String) m.get("traineeId");
			for (String targetTraineeId : traineeIds) {
				// 如果传入的学员属于这个班级里的学员
				if (traineeId.equals(targetTraineeId)) {
					ids.add(targetTraineeId);
				}
			}
		}
		// 获取已经加入此课堂的成员
		map.clear();
		map.put("ctId", ctId);
		List<Map<String, Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(map);
		if (classroomTraineeList != null && classroomTraineeList.size() > 0) {
			List<String> classroomTraineeIds = classroomTraineeList.stream().map(a -> (String) a.get("traineeId"))
					.collect(Collectors.toList());
			// 取差集
			ids.removeAll(classroomTraineeIds);
		}
		// 获取课堂的活动群
		Map<String, String> mm = getActivityMap(classroomInfo, map);
		// 批量插入
		if (ids != null && ids.size() > 0) {
			// 获取Tioconfig
			ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
			List<TevglTchClassroomTrainee> list = new ArrayList<TevglTchClassroomTrainee>();
			List<TimGroupUser> insertList = new ArrayList<TimGroupUser>();
			for (String id : ids) {
				TevglTchClassroomTrainee ct = new TevglTchClassroomTrainee();
				ct.setId(Identities.uuid());
				ct.setCtId(ctId);
				ct.setTraineeId(id);
				ct.setClassId(classId);
				ct.setJoinDate(DateUtils.getNowTimeStamp().substring(0, 10));
				ct.setCreateTime(DateUtils.getNowTimeStamp());
				ct.setState("Y");
				// 加入集合
				list.add(ct);
				// 成为课堂群成员
				TimGroupUser timGroupUser = new TimGroupUser();
				timGroupUser.setGroupuserId(Identities.uuid());
				timGroupUser.setGroupId(ctId);
				timGroupUser.setUserId(id);
				timGroupUser.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
				//timGroupUserService.save(timGroupUser);
				insertList.add(timGroupUser);
				// 将用户绑定到课堂群
				Tio.bindGroup(tioConfig, id, ctId);
				// 更新成员的聊天列表
				timChatListService.save(null, id, ctId, "2", 0, "", true);
				if (mm != null) {
					for (Entry<String, String> o : mm.entrySet()) {
						// 继续更新聊天列表数据
						String activityType = o.getValue();
						String activityId = o.getKey();
						if ("3".equals(activityType)) {
							timChatListService.save(null, id, activityId, "2", 0, "", true);
						} else {
							timChatListService.save(null, id, activityId, "2", 0, "", true);
						}
						// 成为活动群成员
						TimGroupUser timGroupUserActivity = new TimGroupUser();
						timGroupUserActivity.setGroupuserId(Identities.uuid());
						timGroupUserActivity.setGroupId(activityId);
						timGroupUserActivity.setUserId(id);
						timGroupUserActivity.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
						//timGroupUserService.save(timGroupUserActivity);
						insertList.add(timGroupUser);
						// 更新活动群人数
						TimGroup timGroupActivity = new TimGroup();
						timGroupActivity.setGroupId(activityId);
						timGroupActivity.setGroupNum(1);
						timGroupService.plusNum(timGroupActivity);
						// 将用户绑定到活动群组
						Tio.bindGroup(tioConfig, id, activityId);
					}
				}
			}
			if (list.size() > 0) {
				tevglTchClassroomTraineeMapper.insertBatch(list);
				// 更新课堂学习人数
				TevglTchClassroom tevglTchClassroom = new TevglTchClassroom();
				tevglTchClassroom.setCtId(ctId);
				tevglTchClassroom.setStudyNum(ids.size());
				tevglTchClassroomMapper.plusNum(tevglTchClassroom);
			}
			if (insertList.size() > 0) {
				timGroupUserService.insertBatch(insertList);
				// 更新课堂群人数
				TimGroup timGroup = new TimGroup();
				timGroup.setGroupId(ctId);
				timGroup.setGroupNum(insertList.size());
				timGroupService.plusNum(timGroup);
			}
		}
		return R.ok("添加成功");
	}

	/**
	 * 根据条件查询课堂成员
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String,Object>> listClassroomTrainee(Map<String, Object> params) {
		boolean flag = true;
		if (!StrUtils.isNull(params.get("ctId")) && StrUtils.notNull(params.get("loginUserId"))) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(params.get("ctId"));
			if (tevglTchClassroom != null) {
				// 当前登录用户是否为
				Object loginUserId = params.get("loginUserId");
				boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
				boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
				if (isRoomCreator || isTeachingAssistant) {
					flag = false;
				}
			}
		}
		List<Map<String,Object>> list = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> a : list) {
				a.put("traineePic", uploadPathUtils.stitchingPath(a.get("traineePic") , "16"));
				if (flag) {
					a.remove("mobile");
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据条件查询课堂成员，注意：默认只会查询未加入课堂小组的成员，传参参数withOutPage时，查询审核状态通过的所有课程成员
	 * 
	 * @param params 参数ctId必传
	 * @return
	 */
	@Override
	public R listClassroomTraineeExcludeJoinedGroup(@RequestParam Map<String, Object> params) {
		String ctId = (String) params.get("ctId");
		if (StrUtils.isEmpty(ctId)) {
			return R.error("参数ctId为空");
		}
		// 兼容不需要分页的情况
		if (!StrUtils.isNull(params.get("withOutPage"))) {
			params.put("state", "Y");
			List<Map<String, Object>> list = tevglTchClassroomTraineeMapper.selectListMapForWebExclude(params);
			if (list != null && list.size() > 0) {
				// 头像处理
				list.stream().forEach(a -> {
					a.put("traineePic", uploadPathUtils.stitchingPath(a.get("traineePic") , "16"));
				});
			}
			return R.ok().put(Constant.R_DATA, list);
		}
		params.put("state", "Y"); // 只查询正式的课堂成员（不展示未审核的）
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> list = tevglTchClassroomTraineeMapper.selectListMapForWebExclude(query);
		if (list != null && list.size() > 0) {
			list.stream().forEach(a -> {
				a.put("traineePic", uploadPathUtils.stitchingPath(a.get("traineePic") , "16"));
			});
		}
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查看课堂成员的基本信息，如，姓名、手机号码、课堂表现
	 * 
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	@Override
	public R viewTraineeBaseInfo(String ctId, String traineeId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		// 返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		List<Map<String, Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		if (classroomTraineeList == null || classroomTraineeList.size() == 0) {
			return R.error("未查询到相关数据");
		}
		// 当前登录用户是否为
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		Map<String, Object> baseInfo = classroomTraineeList.get(0);
		baseInfo.put("traineePic", uploadPathUtils.stitchingPath(baseInfo.get("traineePic") , "16"));
		baseInfo.put("remark", "");
		if (!isRoomCreator && !isTeachingAssistant) {
			baseInfo.remove("mobile");
		}
		baseInfo.put("blogsNum", tevglTraineeInfoMapper.countBlogNum(traineeId)); // 博客数
		baseInfo.put("blogLikeNum", tevglTraineeInfoMapper.countBlogLikeNum(traineeId)); // 博客获赞数
		baseInfo.put("classroomPerformanceNum", tevglTraineeInfoMapper.countClassroomPerformanceNum(traineeId, ctId)); // 课堂表现数
		baseInfo.put("videoStudy", 0); // 视频学习时长
		// 课堂表现相关数据
		Map<String, Object> performanceInfo = new HashMap<>();
		performanceInfo.put("taNum", 0); // ta的
		performanceInfo.put("averageNum", 0); // 班课平均
		performanceInfo.put("excellentNum", 0); // 优秀平均
		// 添加并返回数据
		result.put("baseInfo", baseInfo);
		result.put("performanceInfo", performanceInfo);
		return R.ok().put(Constant.R_DATA, result);
	}

	/**
	 * 查看课堂成员的综合评价信息 （小程序所用）
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	@Override
	@SysLog(value = "新增")
	public R viewTraineeDetailInfoV2(String ctId, String traineeId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("数据获取未成功");
		}
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		// 查询此课堂所有正在进行、或已结束的活动
		params.put("pkgId", tevglTchClassroom.getPkgId());
		params.put("activityStateList", Arrays.asList("1", "2")); // 实际活动状态(0未开始1进行中2已结束)
		List<TevglPkgActivityRelation> activityList = tevglPkgActivityRelationMapper.selectListByMap(params);
		// 最终返回数据
		List<Map<String, Object>> chartArr = new ArrayList<>();
		// 经验值图形
		Map<String, Object> empiricalMap = new HashMap<>();
		empiricalMap.put("title", "经验值");
		empiricalMap.put("id", "ring1"); // 唯一性
		empiricalMap.put("type", "ring");
		empiricalMap.put("name", "经验值明细");
		Map<String, Object> ringLingInfo = getRingLingInfo(ctId, traineeId, activityList);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> array = (List<Map<String, Object>>) ringLingInfo.get("array");
		empiricalMap.put("data", array);
		// 学习概况图形
		Map<String, Object> studyMap = new HashMap<>();
		studyMap.put("title", "学习概况");
		studyMap.put("id", "ring2"); // 确保该值唯一性
		studyMap.put("type", "gauge");
		studyMap.put("series", getDataList(tevglTchClassroom, ctId, traineeId, activityList));
		// 返回数据
		chartArr.add(empiricalMap);
		chartArr.add(studyMap);
		return R.ok().put(Constant.R_DATA, chartArr);
	}
	
	private List<Map<String, Object>> getDataList(TevglTchClassroom tevglTchClassroom, String ctId, String traineeId, List<TevglPkgActivityRelation> activityList) {
		List<Map<String, Object>> list = new ArrayList<>();

		Map<String, Object> m1 = new HashMap<>();
		m1.put("name", "云教材学习");
		List<Map<String, Object>> dataList1 = new ArrayList<>();
		Map<String, Object> m1Data = new HashMap<>();
		m1Data.put("name", "云教材学习0%\n\n共计学习0小时");
		m1Data.put("value", 20); // 百分比
		dataList1.add(m1Data);
		m1.put("data", dataList1);

		// 活动参与详情
		Map<String, Object> activityInfo = getActivityInfo(ctId, traineeId, activityList);
		Integer currentNum2 = (Integer)activityInfo.get("currentNum");
		Integer totalNum2 = (Integer)activityInfo.get("totalNum");
		Map<String, Object> m2 = new HashMap<>();
		m2.put("name", "活动参与详情");
		List<Map<String, Object>> dataList2 = new ArrayList<>();
		Map<String, Object> m2Data = new HashMap<>();
		m2Data.put("name", "活动已参与"+currentNum2+"项\n\n当前有效"+totalNum2+"项");
		BigDecimal value2 = new BigDecimal("0"); 
		if (totalNum2 != null && totalNum2 > 0) {
			new BigDecimal(currentNum2).divide(new BigDecimal(totalNum2), 2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
		}
		m2Data.put("value", value2);
		dataList2.add(m2Data);
		m2.put("data", dataList2);

		// 资源查看详情
		Map<String, Object> resourceInfo = getResourceInfo(tevglTchClassroom, traineeId);
		Integer currentNum3 = (Integer)resourceInfo.get("currentNum");
		Integer totalNum3 = (Integer)resourceInfo.get("totalNum");
		Map<String, Object> m3 = new HashMap<>();
		m3.put("name", "资源查看详情");
		List<Map<String, Object>> dataList3 = new ArrayList<>();
		Map<String, Object> m3Data = new HashMap<>();
		m3Data.put("name", "资源已查看"+currentNum3+"个\n\n当前有效"+totalNum3+"个");
		BigDecimal value3 = new BigDecimal("0"); 
		if (totalNum3 != null && totalNum3 > 0) {
			new BigDecimal(currentNum3).divide(new BigDecimal(totalNum3), 2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
		}
		m3Data.put("value", value3);
		dataList3.add(m3Data);
		m3.put("data", dataList3);

		// 课堂出勤详情
		Map<String, Object> attendanceRateInfo = getAttendanceRateInfo(ctId, traineeId, activityList);
		Integer currentNum4 = (Integer)attendanceRateInfo.get("currentNum");
		Integer totalNum4 = (Integer)attendanceRateInfo.get("totalNum");
		Map<String, Object> m4 = new HashMap<>();
		m4.put("name", "课堂出勤详情");
		List<Map<String, Object>> dataList4 = new ArrayList<>();
		Map<String, Object> m4Data = new HashMap<>();
		m4Data.put("name", "课堂已出勤"+currentNum4+"次\n\n当前有效"+totalNum4+"次");
		BigDecimal value4 = new BigDecimal("0"); 
		if (totalNum4 != null && totalNum4 > 0) {
			value4 = new BigDecimal(currentNum4).divide(new BigDecimal(totalNum4), 2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
		}
		m4Data.put("value", value4);
		dataList4.add(m4Data);
		m4.put("data", dataList4);

		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.add(m4);
		return list;
	}
	
	/**
	 * 查看课堂成员的综合评价信息（小程序所用）
	 * 
	 * @param ctId      课堂主键
	 * @param traineeId 被查看的学员
	 * @return
	 */
	@Deprecated
	@Override
	public R viewTraineeDetailInfo(String ctId, String traineeId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 这个id是唯一的，用于标识图表
		resultMap.put("id", "");

		// 这个是图例
		List<String> legendData = Arrays.asList("Ta的", "班课平均", "优秀平均");
		resultMap.put("legendData", legendData);

		List<Map<String, Object>> radarIndicator = getRadarIndicatorListData();
		resultMap.put("radarIndicator", radarIndicator);

		List<Map<String, Object>> series = getSeriesListData();
		resultMap.put("series", series);

		return R.ok().put(Constant.R_DATA, resultMap);
	}

	private List<Map<String, Object>> getSeriesListData() {
		List<Map<String, Object>> seriesList = new ArrayList<Map<String, Object>>();
		Map<String, Object> s1 = new HashMap<String, Object>();
		// 当前该雷达图的名称
		s1.put("name", "个人综合评价");
		s1.put("data", null);
		// data的顺序对应legendData的顺序，value的值对应radarIndicator的顺序，
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 第一个name等于'TA的'，value的第一个值100对应
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("name", "Ta的");
		m1.put("value", Arrays.asList(100, 200, 300, 400, 500, 600, 700, 800));

		Map<String, Object> m2 = new HashMap<String, Object>();
		m2.put("name", "班课平均");
		m2.put("value", Arrays.asList(200, 300, 400, 500, 600, 700, 800, 900));

		Map<String, Object> m3 = new HashMap<String, Object>();
		m3.put("name", "优秀平均");
		m3.put("value", Arrays.asList(300, 400, 500, 600, 700, 800, 900, 1500));

		// 添加数据
		dataList.add(m1);
		dataList.add(m2);
		dataList.add(m3);
		s1.put("data", dataList);
		seriesList.add(s1);
		return seriesList;
	}

	/**
	 * 
	 * @return
	 * @apiNote
	 */
	private List<Map<String, Object>> getRadarIndicatorListData() {
		List<Map<String, Object>> radarIndicator = new ArrayList<Map<String, Object>>();
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("name", "视频\n资源学习"); // max: 6500 // name 是围成园的名称，max是当前这个name类型的最大值
		Map<String, Object> m2 = new HashMap<String, Object>();
		m2.put("name", "签到"); // max: 16000
		Map<String, Object> m3 = new HashMap<String, Object>();
		m3.put("name", "测试"); // max: 30000
		Map<String, Object> m4 = new HashMap<String, Object>();
		m4.put("name", "轻直播\n讨论"); // max: 38000
		Map<String, Object> m5 = new HashMap<String, Object>();
		m5.put("name", "头脑\n风暴"); // max: 52000
		Map<String, Object> m6 = new HashMap<String, Object>();
		m6.put("name", "投票\n问卷"); // max: 25000
		Map<String, Object> m7 = new HashMap<String, Object>();
		m7.put("name", "作业小\n小组任务");// max: 52000
		Map<String, Object> m8 = new HashMap<String, Object>();
		m8.put("name", "非视频\n学习资源"); // max: 25000
		// 添加并返回数据
		radarIndicator.add(m1);
		radarIndicator.add(m2);
		radarIndicator.add(m3);
		radarIndicator.add(m4);
		radarIndicator.add(m5);
		radarIndicator.add(m6);
		radarIndicator.add(m7);
		radarIndicator.add(m8);
		return radarIndicator;
	}

	/**
	 * PC端查看课堂成员相关信息 （PC端所用）当前课堂的经验值、热心解答次数、获取点赞数、课堂表现次数、视频学习个数
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	@Override
	public R viewTraineeBaseInfoForWeb(String ctId, String traineeId) {
		TevglTraineeInfo tevglTraineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
		if (tevglTraineeInfo == null) {
			return R.error("无效的用户");
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		// 当前学员课堂获取到的总经验值
		Integer currRoomEmpiricalValue = tevglTraineeEmpiricalValueLogMapper.sumEmpiricalValueByMap(params);
		// 课堂成员信息
		List<Map<String, Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		if (classroomTraineeList == null || classroomTraineeList.size() == 0) {
			return R.error("未查询到相关数据");
		}
		Map<String, Object> baseInfo = classroomTraineeList.get(0);
		baseInfo.put("traineePic", uploadPathUtils.stitchingPath(baseInfo.get("traineePic") , "16"));
		baseInfo.put("remark", "");
		// 热心解答次数（暂定取评论条数） => 改为累计博客数
		Integer blogNums = StrUtils.isNull(tevglTraineeInfo.getBlogsNum()) ? 0 : tevglTraineeInfo.getBlogsNum();
		// 博客点赞数
		Integer blogLikeNums = tevglForumBlogPostMapper.getTotalBlogLikes(traineeId);
		// 课堂表现
		Map<String, Object> performanceData = getPerformanceData(ctId, traineeId);
		// 视频学习
		Map<String, Object> videoStudyData = new HashMap<String, Object>();
		videoStudyData.put("num", 0);
		videoStudyData.put("minute", "0");
		// 返回
		data.put("baseInfo", baseInfo);
		data.put("currRoomEmpiricalValue", currRoomEmpiricalValue);
		data.put("blogNums", blogNums);
		data.put("blogLikeNums", blogLikeNums == null ? 0 : blogLikeNums);
		data.put("performanceData", performanceData);
		data.put("videoStudyData", videoStudyData);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * 获取某学员某课堂中的课堂表现次数，和得到的对应的总分（经验值）
	 * @param ctId
	 * @param traineeId
	 * @return {"num":"", "score":""}
	 */
	private Map<String, Object> getPerformanceData(String ctId, String traineeId) {
		Map<String, Object> performanceData = new HashMap<String, Object>();
		performanceData.put("ctId", ctId);
		performanceData.put("traineeId", traineeId);
		List<TevglTchRoomPereTraineeAnswer> list = tevglTchRoomPereTraineeAnswerMapper.selectListByMap(performanceData);
		int score = 0;
		if (list != null && list.size() > 0) {
			for (TevglTchRoomPereTraineeAnswer tevglTchRoomPereTraineeAnswer : list) {
				score += tevglTchRoomPereTraineeAnswer.getTraineeEmpiricalValue();
			}
		}
		performanceData.clear();
		performanceData.put("num", list == null ? 0 : list.size());
		performanceData.put("score", score);
		return performanceData;
	}

	
	/**
	 * PC端查看课堂成员相关信息（PC端所用）
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	@Override
	public R viewTraineeDetailInfoForWeb(String ctId, String traineeId) {
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("数据获取未成功");
		}
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		// 查询此课堂所有正在进行、或已结束的活动
		params.put("pkgId", tevglTchClassroom.getPkgId());
		params.put("activityStateList", Arrays.asList("1", "2")); // 实际活动状态(0未开始1进行中2已结束)
		List<TevglPkgActivityRelation> activityList = tevglPkgActivityRelationMapper.selectListByMap(params);
		// 定制最终返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		// 返回ring-ling图
		Map<String, Object> ringLingInfo = getRingLingInfo(ctId, traineeId, activityList);
		// 活动参与详情
		Map<String, Object> activityInfo = getActivityInfo(ctId, traineeId, activityList);
		// 资源查看详情
		Map<String, Object> resourceInfo = getResourceInfo(tevglTchClassroom, traineeId);
		// 课堂出勤详情
		Map<String, Object> attendanceRateInfo = getAttendanceRateInfo(ctId, traineeId, activityList);
		// 仪表图
		Map<String, Object> meterDiagramInfo = getMeterDiagramInfo(resourceInfo);
		// 返回数据
		result.put("ringLingInfo", ringLingInfo);
		result.put("meterDiagramInfo", meterDiagramInfo);
		result.put("activityInfo", activityInfo);
		result.put("resourceInfo", resourceInfo);
		result.put("attendanceRateInfo", attendanceRateInfo);
		return R.ok().put(Constant.R_DATA, result);
	}
	
	/**
	 * PC端获取仪表图信息
	 * @return
	 */
	private Map<String, Object> getMeterDiagramInfo(Map<String, Object> resourceInfo){
		Map<String, Object> meterDiagramInfo = new HashMap<String, Object>();
		Object rate = null;
		if (StrUtils.notNull(resourceInfo.get("list"))) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) resourceInfo.get("list");
			if (list.size() > 0) {
				rate = list.get(0).get("value");
			}
		}
		if (rate != null) {
			rate = new BigDecimal(rate.toString()).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString(); 
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("name", "进度");
		m1.put("value", rate);
		list.add(m1);
		meterDiagramInfo.put("list", list); // 进度
		meterDiagramInfo.put("rate", rate + "%"); // 进度
		meterDiagramInfo.put("learningTime", 0); // 视频学习时长
		return meterDiagramInfo;
	}
	
	/**
	 * PC端 资源查看 图形数据格式
	 * @param tevglTchClassroom
	 * @param traineeId
	 * @return
	 */
	private Map<String, Object> getResourceInfo(TevglTchClassroom tevglTchClassroom, String traineeId) {
		// 最终返回数据
		Map<String, Object> resourceInfo = new HashMap<String, Object>();
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(tevglTchClassroom.getPkgId());
		if (tevglPkgInfo == null) {
			return null;
		}
		// 得到当前课堂正在使用的教材
		String ctId = tevglTchClassroom.getCtId();
		String subjectId = tevglPkgInfo.getSubjectId();
		// 首先找出课程内容的总数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);	
		Integer resourceNum = tevglPkgResgroupMapper.countResourceNum(subjectId);
		log.debug("【课程内容】" + resourceNum);
		params.clear();
		// 然后找出这个学员、课堂下这门课程的资源总数
		params.put("ctId", ctId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeId);
		//List<TevglEmpiricalLogChapter> emChapters = tevglEmpiricalLogChapterMapper.selectListByMap(params);
		Integer emChapters = tevglEmpiricalLogChapterMapper.countRoomSubjectChapterNum(params);
		log.debug("已查阅" + emChapters);
		BigDecimal viewValue = new BigDecimal("0");
		BigDecimal unViewValue = new BigDecimal("0");
		// 查看资源
		if (resourceNum == null || resourceNum == 0) {
			viewValue = new BigDecimal("1");
			unViewValue = new BigDecimal("0");
		} else {
			viewValue = new BigDecimal(emChapters).divide(new BigDecimal(resourceNum), 4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
			unViewValue = new BigDecimal("1").subtract(viewValue);
		}
		
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> m1 = new HashMap<>();
		m1.put("name", "查看资源");
		m1.put("value", viewValue);
		Map<String, Object> m2 = new HashMap<>();
		m2.put("name", "未查看资源 ");
		m2.put("value", unViewValue);
		list.add(m1);
		list.add(m2);
		
		// 返回数据
		resourceInfo.put("list", list);
		resourceInfo.put("totalNum", emChapters); // 已参与多少项
		resourceInfo.put("currentNum", resourceNum); // 当前有效项
		resourceInfo.put("msg", "资源查看详情"); // 当前有效项
		return resourceInfo;
	}

	private Map<String, Object> getRingLingInfo(String ctId, String traineeId, List<TevglPkgActivityRelation> activityList){
		// 经验值获取方式(来源字典1投票问卷2头脑风暴3答疑讨论4测试活动5作业6作业/任务7课堂表现8签到9查看章节课程内容10查看视频11查看音频)
		// 查询出用户当前课堂获取的经验值日志
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		params.put("state", "Y");
		List<TevglTraineeEmpiricalValueLog> traineeEmpiricalValueList = tevglTraineeEmpiricalValueLogMapper.selectListByMap(params);
		
		// 最终返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> dataList = new ArrayList<>();
		
		// 总经验值
		int totalValue = 0;
		// 通过参与活动获得的经验值
		int valueByActivity = 0;
		// 通过查询资源获得的经验值
		int valueByResource = 0;
		// 通过出勤获得的经验值
		int valueBySign = 0;
		if (traineeEmpiricalValueList.size() > 0) {
			List<String> asListForActivity = Arrays.asList("1", "2", "3", "4", "5", "6", "7"/*, "8"*/);
			valueByActivity = traineeEmpiricalValueList.stream()
					.filter(a -> asListForActivity.contains(a.getType())).mapToInt(TevglTraineeEmpiricalValueLog::getEmpiricalValue).sum();
			List<String> asListForResource = Arrays.asList("9", "10", "11");
			valueByResource = traineeEmpiricalValueList.stream()
					.filter(a -> asListForResource.contains(a.getType())).mapToInt(TevglTraineeEmpiricalValueLog::getEmpiricalValue).sum();
			valueBySign = traineeEmpiricalValueList.stream()
					.filter(a -> "8".equals(a.getType())).mapToInt(TevglTraineeEmpiricalValueLog::getEmpiricalValue).sum();
			totalValue = traineeEmpiricalValueList.stream().mapToInt(TevglTraineeEmpiricalValueLog::getEmpiricalValue).sum();
		}
		Map<String, Object> m1 = new HashMap<>();
		m1.put("name", "参与活动");
		m1.put("value", valueByActivity);
		Map<String, Object> m2 = new HashMap<>();
		m2.put("name", "查看资源");
		m2.put("value", valueByResource);
		Map<String, Object> m3 = new HashMap<>();
		m3.put("name", "课堂出勤");
		m3.put("value", valueBySign);
		// 返回数据
		dataList.add(m1);
		dataList.add(m2);
		dataList.add(m3);
		result.put("array", dataList);
		result.put("msg", "圆形图");
		List<String> radiusList = new ArrayList<String>();
		
		BigDecimal p1 = new BigDecimal("0");
		BigDecimal p2 = new BigDecimal("0");
		BigDecimal p3 = new BigDecimal("0");
		if (totalValue > 0) {
			if (valueByActivity == totalValue) {
				p1 = new BigDecimal("100");
			} else {
				p1 = new BigDecimal(valueByActivity).divide(new BigDecimal(totalValue), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).stripTrailingZeros();
			}
			if (valueByResource == totalValue) {
				p2 = new BigDecimal("100");
			} else {
				p2 = new BigDecimal(valueByResource).divide(new BigDecimal(totalValue), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).stripTrailingZeros();
			}
			if (valueBySign == totalValue) {
				p3 = new BigDecimal("100");
			} else {
				p3 = new BigDecimal(valueBySign).divide(new BigDecimal(totalValue), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).stripTrailingZeros();
			}
		}
		radiusList.add(p1.toPlainString() + "%"); // 参与活动
		radiusList.add(p2.toPlainString() + "%"); // 查看资源
		radiusList.add(p3.toPlainString() + "%"); // 课堂出勤
		result.put("radius", radiusList);
		result.put("totalValue", totalValue);
		return result;
	}
	
	/**
	 * PC端 课堂出勤 图形数据格式
	 * @param ctId
	 * @param traineeId
	 * @param activityList
	 * @return
	 */
	private Map<String, Object> getAttendanceRateInfo(String ctId, String traineeId, List<TevglPkgActivityRelation> activityList) {
		Map<String, Object> attendanceRateInfo = new HashMap<String, Object>();
		// 取出当前课堂的签到活动
		List<String> activityIdList = activityList.stream().filter(a -> a.getActivityType().equals(GlobalActivity.ACTIVITY_8_SIGININ_INFO))
				.map(a -> a.getActivityId()).distinct().collect(Collectors.toList());
		log.debug("课堂["+ctId+"]的签到活动：" + activityIdList.size());
		// 当前学员的签到记录
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		params.put("signStates", Arrays.asList("1", "2", "3", "4", "5")); // 签到状态0未签到1正常2迟到3早退4旷课5请假
		List<TevglActivitySigninTrainee> signinTraineeList = tevglActivitySigninTraineeMapper.selectListByMap(params);
		log.debug("学员的签到记录次数：" + signinTraineeList);
		// 计算
		BigDecimal attendanceRate = new BigDecimal("0"); // 出勤率
		BigDecimal unAttendanceRate = new BigDecimal("0"); // 未出勤率
		if (activityIdList != null && activityIdList.size() > 0) {
			if (signinTraineeList != null && signinTraineeList.size() > 0) {
				attendanceRate = new BigDecimal(signinTraineeList.size()).divide(new BigDecimal(activityIdList.size()), 2, BigDecimal.ROUND_HALF_UP);
				unAttendanceRate = new BigDecimal("1").subtract(attendanceRate);
			} else {
				unAttendanceRate = new BigDecimal("100");
			}
		} else {
			unAttendanceRate = new BigDecimal("100");
		}
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> m1 = new HashMap<>();
		m1.put("name", "出勤率");
		m1.put("value", attendanceRate);
		Map<String, Object> m2 = new HashMap<>();
		m2.put("name", "未出勤率 ");
		m2.put("value", unAttendanceRate);
		list.add(m1);
		list.add(m2);
		attendanceRateInfo.put("list", list);
		attendanceRateInfo.put("totalNum", signinTraineeList.size()); // 已参与多少项
		attendanceRateInfo.put("currentNum", activityIdList.size()); // 当前有效项
		attendanceRateInfo.put("msg", "课堂出勤详情");
		return attendanceRateInfo;
	}

	/**
	 * PC端 活动参与 图形数据格式
	 * @param ctId
	 * @param traineeId
	 * @param activityList
	 * @return
	 */
	private Map<String, Object> getActivityInfo(String ctId, String traineeId, List<TevglPkgActivityRelation> activityList) {
		// 计算当前用户参与的课堂的活动
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		Integer val = tevglPkgActivityRelationMapper.countTraineJoinActivityNum(params);
		log.debug("结果：" + val);
		// 参与率
		BigDecimal participationRate = new BigDecimal("0");
		// 未参与率
		BigDecimal unParticipationRate = new BigDecimal("0");
		// 如果没有活动
		if (activityList == null || activityList.size() == 0) {
			unParticipationRate = new BigDecimal("100");
		} else {
			// 如果有活动，但没参与
			if (val == null || val.equals(0)) {
				unParticipationRate = new BigDecimal("100");	
			} else {
				participationRate = new BigDecimal(val).divide(new BigDecimal(activityList.size()), 2, BigDecimal.ROUND_HALF_UP);
				unParticipationRate = new BigDecimal("1").subtract(participationRate);
			}
		}
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> m1 = new HashMap<>();
		m1.put("name", "未参与活动率");
		m1.put("value", unParticipationRate);
		Map<String, Object> m2 = new HashMap<>();
		m2.put("name", "参与活动率");
		m2.put("value", participationRate);
		list.add(m1);
		list.add(m2);
		// 活动图表
		activityInfo.put("list", list);
		activityInfo.put("totalNum", val); // 已参与多少项
		activityInfo.put("currentNum", activityList.size()); // 当前有效项
		activityInfo.put("msg", "活动参与详情");
		return activityInfo;
	}

	/**
	 * 教师修改学员部分信息
	 * @author zyl加
	 * @data 2020年12月2日
	 * @param request
	 * @param ctId 当前上课课堂
	 * @param traineeId 被修改的学员
	 * @param traineePic 头像
	 * @param traineeName 姓名
	 * @param traineeSex 性别
	 * @param loginUserId 当前登录用户
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Override
	public R updateTraineeInfo(HttpServletRequest request, String ctId, String traineeId, String traineePic, String traineeName,
			String nickName, String traineeSex, String loginUserId) throws Exception {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(traineeSex) 
				|| StrUtils.isEmpty(traineeName) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (traineeName.trim().length() > 20 || nickName.trim().length() > 20) {
			return R.error("姓名或昵称不能超过20长度");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		/*if (!loginUserId.equals(classroom.getCreateUserId())) {
			return R.error("只有课堂创建者才能修改学员信息");
		}*/
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(classroom, loginUserId, GlobalRoomPermission.EDIT_TRAINEE_INFO);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		//根据学员id查询学员信息，然后获取图片地址，在把图片删掉
		TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
		if (traineeInfo == null) {
			return R.error("学员不存在");
		}
		
		traineePic = traineeInfo.getTraineePic() == null? null : traineeInfo.getTraineePic();
		//第一种情况:默认图片 (/uploads/defaulthead.png)
		int slashsubscript = 0;
		if (traineePic != null) {
			slashsubscript = traineePic.lastIndexOf("/"); //斜杠下标
		}

		//第二种情况:只有文件名
		String picPath = uploadPath + uploadPathUtils.getPathByParaNo("16") + "/" + traineePic;
		File delfile = new File(picPath);
		
		TevglTraineeInfo trainee = new TevglTraineeInfo();
		//上传文件部分
		MultipartHttpServletRequest multipartHttpServletRequest = null;
	    //强制转换为MultipartHttpServletRequest接口对象 (它包含所有HttpServletRequest的方法)
		if(request instanceof MultipartHttpServletRequest){
	        multipartHttpServletRequest = (MultipartHttpServletRequest) request;
	    }else{
	        return R.error("上传失败");
	    }
		MultipartFile file = null;
		String newName = null;
	    //获取MultipartFile文件信息(注意参数为前端对应的参数名称)
		if (request != null) {
			file = multipartHttpServletRequest.getFile("file");
		    //如果没有上传文件则保持原有的头像，如果上传了文件则把上传的头像替换成原有的头像
		    if (file != null) {
		    	if (delfile.exists()) {
		    		delfile.delete();
				}
				
				// 源文件名
				String originalFilename = file.getOriginalFilename();
				int pos = originalFilename.lastIndexOf(".");
				// 后缀名
				String extension = originalFilename.substring(pos);
			    // 磁盘上生成文件
				File path = new File(uploadPath + uploadPathUtils.getPathByParaNo("16"));
				if(!path.exists()) {
					path.mkdirs();
				}
				
				String uuid = UUID.randomUUID().toString();
				//新文件名
				newName = uuid + extension;
				
				String _traineePic = uploadPath + uploadPathUtils.getPathByParaNo("16") + "/" + newName;
				
				File saveFile = new File(_traineePic);
				file.transferTo(saveFile);
				
			}
		}
	    
	    //填充信息
		trainee.setTraineeId(traineeId);
	    if (slashsubscript > 0 && !file.isEmpty()) {//有默认图片且上传了文件修改
			trainee.setTraineePic(newName);
		}else if(slashsubscript > 0){//有默认图片但没上传文件不修改
			trainee.setTraineePic(traineePic);
		}else if (file != null) {//第三种情况:本就没文件或者有文件上传了文件也会修改
			trainee.setTraineePic(newName);
		}else {//没有上传文件不修改
			trainee.setTraineePic(traineePic);
		}
		trainee.setTraineeName(traineeName); 
		trainee.setNickName(nickName);
		trainee.setTraineeSex(traineeSex);
		tevglTraineeInfoMapper.update(trainee);
			
		return R.ok("成功修改学员信息");
	}

	/**
	 * 将选择的人，加入成该课堂成员（注意：同时需要将它们加入到该课堂群里面去，以及对应的进行中的答疑讨论群）
	 * @param ctId 必传参数，当前课堂
	 * @param loginUserId 必传参数，当前登录用户
	 * @param traineeIds 选传参数，被选的学员们
	 * @return
	 */
	@Override
	public R importTraineeBatchNew(String ctId, String loginUserId, List<String> traineeIds) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		boolean isRoomCreator = StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		boolean isReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getReceiverUserId().equals(loginUserId);
		if (!isRoomCreator && !isTeachingAssistant && !isReceiver) {
			return R.error("没有权限");
		}
		// 入库课堂成员表
		doSaveClassroomTrainee(ctId, traineeIds);
		// 处理即时通讯相关表数据
		doHandleRoomImDatas(tevglTchClassroom, ctId, traineeIds);
		return R.ok("操作成功");
	}
	
	@Override
	public R saveClassroomTraineeBatch(String ctId, List<String> traineeIdList) {
		this.doSaveClassroomTrainee(ctId, traineeIdList);
		return R.ok("批量新增成功");
	}

	
	private void doHandleRoomImDatas(TevglTchClassroom tevglTchClassroom, String ctId, List<String> traineeIds) {
		// 找到课堂群的群成员
		List<TimGroupUser> timGroupUserList = timGroupUserService.selectListByGroupId(ctId);
		List<String> existedTraineeIdList = timGroupUserList.stream().map(a -> a.getUserId()).distinct().collect(Collectors.toList());
		List<String> tempTraineeIdList = new ArrayList<String>();
		if (traineeIds != null && traineeIds.size() > 0) {
			tempTraineeIdList.addAll(traineeIds);
		}
		log.debug("已经在课堂群的学员：" + existedTraineeIdList);
		log.debug("去重前：" + tempTraineeIdList);
		tempTraineeIdList.removeAll(existedTraineeIdList);
		log.debug("去重后，等待加入【课堂群】的学员有：" + tempTraineeIdList);
		if (tempTraineeIdList == null || tempTraineeIdList.size() == 0) {
			return;
		}
		// 获取课堂的活动群（如：答疑讨论群）
		Map<String, String> mm = getActivityMap(tevglTchClassroom, new HashMap<String, Object>());
		// tio
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		// 等待入库课堂群的成员
		List<TimGroupUser> insertList = new ArrayList<TimGroupUser>();
		for (String traineeId : tempTraineeIdList) {
			TimGroupUser timGroupUser = new TimGroupUser();
			timGroupUser.setGroupuserId(Identities.uuid());
			timGroupUser.setGroupId(ctId);
			timGroupUser.setUserId(traineeId);
			timGroupUser.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
			//timGroupUserService.save(timGroupUser);
			insertList.add(timGroupUser);
			// 将用户绑定到课堂群
			Tio.bindGroup(tioConfig, traineeId, ctId);
			// 更新成员的聊天列表
			timChatListService.save(null, traineeId, ctId, "2", 0, "", true);
			// 
			if (mm != null) {
				for (Entry<String, String> o : mm.entrySet()) {
					// 继续更新聊天列表数据
					String activityType = o.getValue();
					String activityId = o.getKey();
					if ("3".equals(activityType)) {
						timChatListService.save(null, traineeId, activityId, "2", 0, "", true);
					} else {
						timChatListService.save(null, traineeId, activityId, "2", 0, "", true);
					}
					// 成为活动群成员
					TimGroupUser timGroupUserActivity = new TimGroupUser();
					timGroupUserActivity.setGroupuserId(Identities.uuid());
					timGroupUserActivity.setGroupId(activityId);
					timGroupUserActivity.setUserId(traineeId);
					timGroupUserActivity.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
					//timGroupUserService.save(timGroupUserActivity);
					insertList.add(timGroupUser);
					// 更新活动群人数
					TimGroup timGroupActivity = new TimGroup();
					timGroupActivity.setGroupId(activityId);
					timGroupActivity.setGroupNum(1);
					timGroupService.plusNum(timGroupActivity);
					// 将用户绑定到活动群组
					Tio.bindGroup(tioConfig, traineeId, activityId);
				}
			}
		}
		if (insertList.size() > 0) {
			timGroupUserService.insertBatch(insertList);
			// 更新课堂群人数
			TimGroup timGroup = new TimGroup();
			timGroup.setGroupId(ctId);
			timGroup.setGroupNum(insertList.size());
			timGroupService.plusNum(timGroup);
		}
	}
	
	/**
	 * 入库课堂成员表
	 * @param ctId
	 * @param traineeIds
	 */
	private void doSaveClassroomTrainee(String ctId, List<String> traineeIds) {
		if (StrUtils.isEmpty(ctId) || traineeIds == null || traineeIds.size() == 0) {
			return;
		}
		// 等待入库的课堂成员
		List<TevglTchClassroomTrainee> insertList = new ArrayList<TevglTchClassroomTrainee>();
		// 先找到当前已经在课堂里面的学员，控制不重复，加入课堂
		Map<String, Object> map = new HashMap<>();
		map.put("ctId", ctId);
		List<TevglTchClassroomTrainee> existedClassroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(map);
		List<String> existedTraineeIdList = existedClassroomTraineeList.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		List<String> tempTraineeIdList = new ArrayList<String>();
		if (traineeIds != null && traineeIds.size() > 0) {
			tempTraineeIdList.addAll(traineeIds);
		}
		log.debug("已经在课堂里的学员：" + existedTraineeIdList);
		log.debug("去重前：" + tempTraineeIdList);
		// 去重
		tempTraineeIdList.removeAll(existedTraineeIdList);
		log.debug("去重后，等待加入课堂的学员有：" + tempTraineeIdList);
		if (tempTraineeIdList == null || tempTraineeIdList.size() == 0) {
			return;
		}
		// 遍历等待入库的学生
		for (String traineeId : tempTraineeIdList) {
			TevglTchClassroomTrainee ct = new TevglTchClassroomTrainee();
			ct.setId(Identities.uuid());
			ct.setCtId(ctId);
			ct.setTraineeId(traineeId);
			//ct.setClassId(classId);
			ct.setJoinDate(DateUtils.getNowTimeStamp().substring(0, 10));
			ct.setCreateTime(DateUtils.getNowTimeStamp());
			ct.setState("Y");
			insertList.add(ct);
		}
		if (insertList.size() > 0) {
			// 入库
			tevglTchClassroomTraineeMapper.insertBatch(insertList);
			// 更新课堂学习人数
			TevglTchClassroom tevglTchClassroom = new TevglTchClassroom();
			tevglTchClassroom.setCtId(ctId);
			tevglTchClassroom.setStudyNum(insertList.size());
			tevglTchClassroomMapper.plusNum(tevglTchClassroom);
		}
	}

	/**
	 * 根据条件查询课堂成员
	 * @param map
	 * @return
	 */
	@Override
	@SysLog(value = "根据条件查询课堂成员")
	public List<TevglTchClassroomTraineeVo> findClassroomTraineeList(Map<String, Object> map) {
		Query query = new Query(map);
		List<TevglTchClassroomTraineeVo> list = tevglTchClassroomTraineeMapper.findClassroomTraineeList(query);
		convertUtil.convertDict(list, "traineeSexName", "sex");
		return list;
	}
	

	/**
	 * 批量更新选中的数据，在课堂结束后，允许再进入课堂
	 *
	 * @param ctId   课堂id
	 * @param idList 用户选择的，课堂成员id，对应t_evgl_tch_classroom_trainee表主键id值
	 * @return
	 */
	@Override
	@SysLog(value = "批量更新选中的数据，在课堂结束后，允许再进入课堂")
	public R batchUpdateAccessState(String ctId, List<String> idList) {
		if (StrUtils.isEmpty(ctId)) {
			return R.error(BizCodeEnume.PARAM_MISSING.getCode(), BizCodeEnume.PARAM_MISSING.getMsg());
		}
		List<String> ids = tevglTchClassroomTraineeMapper.findIdByCtId(ctId);
		// 验证
		//verifyData(ids, idList);
		// 如果为空，则表示全部清空为不允许再进入课堂
		if (idList == null || idList.size() == 0) {
			tevglTchClassroomTraineeMapper.batchUpdateAccessState(CommonEnum.STATE_NO.getCode(), ids);
		} else {
			tevglTchClassroomTraineeMapper.batchUpdateAccessState(CommonEnum.STATE_YES.getCode(), idList);
		}
		return R.ok(BizCodeEnume.SUCCESS.getMsg());
	}

	/**
	 * 数据校验，判断是否为非法操作
	 * @param ids 当前课堂成员id
	 * @param idList 用户选择的课堂成员id
	 */
	private void verifyData(List<String> ids, List<String> idList) {
		if (idList != null && idList.size() > 0) {
			for (int i = 0; i < idList.size(); i++) {
				if (!ids.contains(idList.get(i))) {
					throw new BudaosException(BizCodeEnume.ILLEGAL_OPERATION.getMsg());
				}
			}
		}
	}


}
