package com.budaos.modules.evgl.eao.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.*;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookRegularMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.eao.api.TeaoTraineeExamroomService;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamine;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExammember;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamroom;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamineMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExammemberMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamroomMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.im.core.entity.MsgType;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.math.BigDecimal;
import java.util.*;
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
public class TeaoTraineeExamroomServiceImpl implements TeaoTraineeExamroomService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TeaoTraineeExamroomServiceImpl.class);
	
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	@Autowired
	private DictUtil dictUtil;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	
	@Autowired
	private TeaoTraineeExamroomMapper teaoTraineeExamroomMapper;
	@Autowired
	private TeaoTraineeExammemberMapper teaoTraineeExammemberMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookRegularMapper tevglBookRegularMapper;
	@Autowired
	private TeaoTraineeExamineMapper teaoTraineeExamineMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;


	 /**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TeaoTraineeExamroom> teaoTraineeExamroomList = teaoTraineeExamroomMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(teaoTraineeExamroomList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(teaoTraineeExamroomList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(teaoTraineeExamroomList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)")
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> teaoTraineeExamroomList = teaoTraineeExamroomMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(teaoTraineeExamroomList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(teaoTraineeExamroomList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param teaoTraineeExamroom
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TeaoTraineeExamroom teaoTraineeExamroom) throws BudaosException {
		teaoTraineeExamroom.setErId(Identities.uuid());
		teaoTraineeExamroom.setCreateUserId(serviceLoginUtil.getLoginUserId());
		teaoTraineeExamroom.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(teaoTraineeExamroom);
		teaoTraineeExamroomMapper.insert(teaoTraineeExamroom);
		return R.ok();
	}
	/**
	 * 修改
	 * @param teaoTraineeExamroom
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TeaoTraineeExamroom teaoTraineeExamroom) throws BudaosException {
	    teaoTraineeExamroom.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    teaoTraineeExamroom.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(teaoTraineeExamroom);
		teaoTraineeExamroomMapper.update(teaoTraineeExamroom);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		teaoTraineeExamroomMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		teaoTraineeExamroomMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, teaoTraineeExamroomMapper.selectObjectById(id));
	}

	/**
	 * 进入考核填写页面
	 * @param ctId
	 * @param erId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R list(String ctId, String erId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(erId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("数据获取未成功");
		}
		if ("1".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂未开始");
		}
		if ("3".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂已结束");
		}
		// 查询本次考核信息
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", tevglTchClassroom.getPkgId());
		params.put("activityId", erId);
		TeaoTraineeExamroom room = teaoTraineeExamroomMapper.selectObjectByIdAndPkgId(params);
		//TeaoTraineeExamroom room = teaoTraineeExamroomMapper.selectObjectById(erId);
		if (room == null) {
			return R.error("无效的记录");
		}
		if ("0".equals(room.getActivityStateActual())) {
			return R.error("考核未开始");
		}
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验当前提交人是否有考核资格
		map.put("erId", erId);
		map.put("traineeId", loginUserId);
		List<TeaoTraineeExammember> list = teaoTraineeExammemberMapper.selectListByMap(map);
		if (list == null || list.size() == 0) {
			return R.error("没有资格");
		}
		// 查询本次考核的所有规则
		map.clear();
		map.put("subjectId", room.getSubjectId());
		map.put("sidx", "regular_sort");
		map.put("order", "asc");
		List<Map<String, Object>> allRegularList = tevglBookRegularMapper.selectListByMapForSp(map);
		// 本次考核的一级规则
		List<Map<String, Object>> regularList = allRegularList.parallelStream().filter(a -> "-1".equals(a.get("parent_id"))).collect(Collectors.toList());
		// 遍历填充一级规则所包括的二级规则
		regularList.stream().forEach(regP -> {
			List<Map<String, Object>> children = allRegularList.stream().filter(regular -> regP.get("regular_id").equals(regular.get("parent_id"))).collect(Collectors.toList());
			regP.put("children", children);
		});
		// 查询参与本次考核的所有学员信息
		map.clear();
		map.put("erId", room.getErId());
		List<Map<String, Object>> members = teaoTraineeExammemberMapper.selectListByMapForSp(map);
		String classNames = "";
		for (Map<String, Object> m : members) {
			if(!StrUtils.isNull(m.get("class_name")) && !Arrays.asList(classNames.split(", ")).contains(m.get("class_name").toString())) {
				classNames += ", " + m.get("class_name").toString();
			}
		}
		// 临时保存的考核成绩
		map.clear();
		map.put("createUserId", loginUserId);
		map.put("subjectId", room.getSubjectId());
		map.put("erId", erId);
		List<TeaoTraineeExamine> examdatas = teaoTraineeExamineMapper.selectListByMap(map);
		log.debug("查询当前登录用户临时保存的考核成绩：" + examdatas.size());
		// 最终返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		// 本次考核信息
		data.put("room", getSimpleRoomInfo(room));
		// 考核规则
		data.put("regularList", regularList);
		// 考核成员
		data.put("memberList", members.parallelStream().filter(a -> "1".equals(a.get("trainee_type"))).collect(Collectors.toList()));
		// 本次已经保存的考核数据
		data.put("examdatas", examdatas);
		// 自己
		List<Map<String, Object>> mymembers = members.parallelStream()
				.filter(a -> loginUserId.equals(a.get("trainee_id")))
				.collect(Collectors.toList());
		if(mymembers != null && mymembers.size() > 0) {
			data.put("mymember", mymembers.get(0));
			data.put("classNames", classNames.length() > 0 ? classNames.substring(1) : classNames);
			data.put("subjectName", members.get(0).get("subject_name"));
		}
		return R.ok().put(Constant.R_DATA, data);
	}
	
	private Map<String, Object> getSimpleRoomInfo(TeaoTraineeExamroom teaoTraineeExamroom){
		Map<String, Object> info = new HashMap<>();
		if (teaoTraineeExamroom != null) {
			info.put("erId", teaoTraineeExamroom.getErId());
			info.put("subjectId", teaoTraineeExamroom.getSubjectId());
			info.put("classIds", teaoTraineeExamroom.getClassIds());
			info.put("erAddr", teaoTraineeExamroom.getErAddr());
			info.put("erStime", teaoTraineeExamroom.getErStime());
			info.put("state", teaoTraineeExamroom.getState());
			info.put("createUserId", teaoTraineeExamroom.getCreateUserId());
			info.put("ctId", teaoTraineeExamroom.getErAddr());
		}
		return info;
	}
	
	
	/**
	 * 新增实践考核
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	public R saveActivityExamroomInfo(JSONObject jsonObject, String loginUserId) {
		// 主键id
		String erId = jsonObject.getString("erId");
		// 当前课堂
		String ctId = jsonObject.getString("ctId");
		String pkgId = jsonObject.getString("pkgId");
		String content = jsonObject.getString("content");
		String activityTitle = jsonObject.getString("activityTitle");
		String purpose = jsonObject.getString("purpose");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂，无法添加活动");
		}
		if ("3".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂已结束，无法添加活动");
		}
		// 权限判断
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 参与考核的学员
		JSONArray traineeIds = jsonObject.getJSONArray("traineeIds");
		if (traineeIds == null || traineeIds.size() == 0) {
			return R.error("参与考核学员不能为空");
		}
		// 参与考核的教师
		JSONArray teacherIds = jsonObject.getJSONArray("teacherIds");
		if (teacherIds != null) {
			// 课堂没有被移交
			if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
				// 且当前用户是创建者
				if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					if (!teacherIds.contains(loginUserId)) {
						teacherIds.add(loginUserId);
					}
				} else { // 助教
					if (!teacherIds.contains(tevglTchClassroom.getCreateUserId())) {
						teacherIds.add(tevglTchClassroom.getCreateUserId());
					}
				}
			}
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				// 且当前用户是接收者，即表示是接收者在创建活动
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					if (!teacherIds.contains(loginUserId)) {
						teacherIds.add(loginUserId);
					}
				} else { // 否则是助教在创建
					if (!teacherIds.contains(tevglTchClassroom.getReceiverUserId())) {
						teacherIds.add(tevglTchClassroom.getCreateUserId());
					}
				}
			}
		}
		if (teacherIds == null || teacherIds.size() == 0) {
			return R.error("参与考核教师不能为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("数据异常");
		}
		// 当前课堂教学包对应的教材
		String subjectId = tevglPkgInfo.getSubjectId();
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (tevglBookSubject == null || StrUtils.isEmpty(tevglBookSubject.getSubjectRef())) {
			return R.error("无效的记录，数据异常");
		}
		// 获取教材对应的课程
		TevglBookSubject subject1 = tevglBookSubjectMapper.selectObjectById(tevglBookSubject.getSubjectRef());
		if (subject1 == null) {
			return R.error("无效的记录，数据异常"); 
		}
		String platformSubjectId = "";
		if (StrUtils.isEmpty(subject1.getSubjectRef())) {
			platformSubjectId = subject1.getSubjectId();
		} else {
			TevglBookSubject subject2 = tevglBookSubjectMapper.selectObjectById(subject1.getSubjectRef());
			if (subject2 != null && StrUtils.isEmpty(subject2.getSubjectRef())) {
				platformSubjectId = subject2.getSubjectRef();
			}
		}
		log.debug("根据当前课堂["+ctId+"]"+"，对应教学包["+pkgId+"]的教材["+subjectId+tevglBookSubject.getSubjectName()+"]，查到到平台课程["+platformSubjectId+"]");
		if (StrUtils.isEmpty(platformSubjectId)) {
			return R.error("数据获取失败，无法创建实践考核");
		}
		// 当前课堂对应上课的班级
		String classId = tevglTchClassroom.getClassId();
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		// 填充基本信息
		TeaoTraineeExamroom teaoTraineeExamroom = new TeaoTraineeExamroom();
		teaoTraineeExamroom.setErId(erId);
		// 考核教室(这里直接理解为课堂，看做ct_id)
		teaoTraineeExamroom.setErAddr(ctId);
		teaoTraineeExamroom.setSubjectId(platformSubjectId);
		teaoTraineeExamroom.setClassIds(tevglTchClassroom.getClassId());
		teaoTraineeExamroom.setActivityTitle(activityTitle);
		teaoTraineeExamroom.setRemark(content);
		teaoTraineeExamroom.setPurpose(purpose);
		if (StrUtils.isEmpty(teaoTraineeExamroom.getErId())) {
			erId = Identities.uuid();
			teaoTraineeExamroom.setErId(erId);
			// 原版，考核结果(0正在考核1考核结束)
			teaoTraineeExamroom.setState("0"); // 理解为活动状态，0未开始1进行中2已结束
			teaoTraineeExamroom.setCreateTime(DateUtils.getNowTimeStamp());
			teaoTraineeExamroom.setCreateUserId(loginUserId);
			teaoTraineeExamroomMapper.insert(teaoTraineeExamroom);
			// 保存教学包与活动之间的关系
			pkgUtils.buildRelation(pkgId, teaoTraineeExamroom.getErId(), GlobalActivity.ACTIVITY_9_TRAINEE_EXAM);
			// 更新教学包活动数量
			pkgUtils.plusPkgActivityNum(pkgId);
		} else {
			teaoTraineeExamroom.setUpdateTime(DateUtils.getNowTimeStamp());
			teaoTraineeExamroom.setUpdateUserId(loginUserId);
			teaoTraineeExamroomMapper.update(teaoTraineeExamroom);
			// 删除原来添加的所有成员
			map.put("erId", teaoTraineeExamroom.getErId());
			teaoTraineeExammemberMapper.deleteOther(map);
		}
		// 将参与考核的学员入库
		map.clear();
		for (int i = 0; i < traineeIds.size(); i++) {
			String traineeId = traineeIds.get(i).toString();
			map.put("erId", erId);
			map.put("traineeId", traineeId);
			map.put("subjectId", platformSubjectId);
			// 校验该成员是否已经添加到该课程的考核，防止重复添加
			List<TeaoTraineeExammember> list = teaoTraineeExammemberMapper.selectListByMap(map);
			if (list.size() > 0) {
				return R.error("学员【"+list.get(0).getTraineeName()+"】已完成考核，不能重复添加");
			}
			// 查询学员信息
			TevglTraineeInfo trainee = tevglTraineeInfoMapper.selectObjectById(traineeId);
			// 将参与考核的学员入库
			TeaoTraineeExammember member = new TeaoTraineeExammember();
			member.setEtId(Identities.uuid());
			member.setErId(erId);
			member.setClassId(classId);
			member.setTraineeId(traineeId);
			member.setSubjectId(platformSubjectId);
			member.setTraineeHeadimg(StrUtils.isEmpty(trainee.getTraineePic()) ? trainee.getTraineeHead() : trainee.getTraineePic());
			member.setTraineeName(StrUtils.isEmpty(trainee.getTraineeName()) ? trainee.getNickName() : trainee.getTraineeName());
			member.setErId(teaoTraineeExamroom.getErId());
			member.setTraineeType("1"); // 成员类型(0只评分的学员，1学员，2考核负责人，3参与考核教师，4外部专家)
			member.setState("N"); // 是否提交了考核(Y/N)
			teaoTraineeExammemberMapper.insert(member);
		}
		// 将参加本次考核的所有学员打乱随机排序
		map.clear();
		map.put("erId", teaoTraineeExamroom.getErId());
		map.put("traineeType", "1");//1学员2考核负责人3参与考核教师4外部专家
		// 考核安排时，将学员考核顺序随机打乱
		randomSort(teaoTraineeExammemberMapper.selectListByMap(map));
		//将参与考核的教师入库
		for (int i=0; i<teacherIds.size(); i++) {
			String teacherId = teacherIds.getString(i);
			// 查询教师信息
			TevglTchTeacher trainee = tevglTchTeacherMapper.selectObjectById(teacherId);
			if (trainee == null) {
				trainee = tevglTchTeacherMapper.selectObjectByTraineeId(teacherId);
			}
			map.put("traineeId", trainee.getTraineeId());
			map.put("subjectId", platformSubjectId);
			map.put("erId", teaoTraineeExamroom.getErId());
			// 校验该教师是否已经添加到该课程的考核，防止重复添加
			List<TeaoTraineeExammember> list = teaoTraineeExammemberMapper.selectListByMap(map);
			if(list.size() > 0) {
				continue;
			}
			// 将参与考核的教师入库
			TeaoTraineeExammember member = new TeaoTraineeExammember();
			member.setEtId(Identities.uuid());
			member.setErId(erId);
			member.setTraineeId(trainee.getTraineeId());
			member.setSubjectId(platformSubjectId);
			member.setTraineeHeadimg(trainee.getTeacherPic());
			member.setTraineeName(trainee.getTeacherName());
			member.setErId(teaoTraineeExamroom.getErId());
			member.setTraineeType("3");
			member.setState("N");
			teaoTraineeExammemberMapper.insert(member);
		}
		return R.ok("保存成功").put(Constant.R_DATA, teaoTraineeExamroom.getErId());
	}
	
	/**
	 * 考核安排时，将学员考核顺序随机打乱
	 * @param members
	 */
	private void randomSort(List<TeaoTraineeExammember> members) {
		Collections.shuffle(members);
		Collections.shuffle(members);
		Collections.shuffle(members);
		for(int i=0; i<members.size(); i++) {
			members.get(i).setSortNum(i+1);
			teaoTraineeExammemberMapper.update(members.get(i));
		}
	}

	/**
	 * 删除实践考核
	 * @param activityId t_eao_trainee_examroom表主键id的值
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R deleteActivityExamroomInfo(String activityId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TeaoTraineeExamroom teaoTraineeExamroom = teaoTraineeExamroomMapper.selectObjectByIdAndPkgId(params);
		if (teaoTraineeExamroom == null) {
			return R.error("无效的记录");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂，无法添加活动");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_DELETE);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 如果活动已经开始或结束,控制不能删除
		if (!"0".equals(teaoTraineeExamroom.getActivityStateActual())) {
			return R.error("当前活动已被使用，无法删除");
		}
		// 查找考核成员，并删除
		params.clear();
		params.put("erId", activityId);
		List<TeaoTraineeExammember> list = teaoTraineeExammemberMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			String[] array = list.stream().map(a -> a.getEtId()).toArray(String[]::new);
			teaoTraineeExammemberMapper.deleteBatch(array);
		}
		// 物理删除
		teaoTraineeExamroomMapper.delete(activityId);
		// 删除与教学包的关系
		tevglPkgActivityRelationMapper.deleteByActivityId(activityId);
		// 教学包活动数量-1
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("删除成功");
	}

	/**
	 * 开始活动
	 * @param ctId 课堂id
	 * @param activityId 活动id t_eao_trainee_examroom表主键id
	 * @param loginUserId 当前登录用户
	 * @param activityEndTime 非必传参数，活动自动结束的时间
	 * @return
	 */
	@Override
	public R startActivityExamroomInfo(String ctId, String activityId, String loginUserId, String activityEndTime) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)
				|| StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(classroom.getClassroomState())) {
			return R.error("课堂尚未开始，无法开始活动");
		}
		if ("3".equals(classroom.getClassroomState())) {
			return R.error("课堂已经结束，无法开始活动");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", classroom.getPkgId());
		params.put("activityId", activityId);
		TeaoTraineeExamroom activityInfo = teaoTraineeExamroomMapper.selectObjectByIdAndPkgId(params);
		if (activityInfo == null) {
			return R.error("无效的活动，请刷新后重试");
		}
		if ("1".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动已开始");
		}
		if ("2".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动已结束");
		}
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("pkgId", classroom.getPkgId());
		ps.put("activityId", activityId);
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(ps);
		if (list == null || list.size() == 0) {
			log.debug("t_evgl_pkg_activity_relation没有数据,直接认为没有权限");
			return R.error("没有权限");
		}
		TevglPkgActivityRelation relation = list.get(0);
		// 权限判断
		/*if (!pkgActivityUtils.hasStartActPermission(activityId, relation, loginUserId, activityInfo.getCreateUserId())) {
			return R.error("暂无权限，操作失败");
		}*/
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(classroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_START);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 调整为修改关联表的状态
		relation.setActivityState("1");
		relation.setActivityBeginTime(DateUtils.getNowTimeStamp());
		relation.setActivityEndTime(StrUtils.isEmpty(activityEndTime) ? null : activityEndTime);
		tevglPkgActivityRelationMapper.update(relation);
		// 返回数据
		Map<String, Object> data = new HashMap<>();
		data.put("empiricalValue", activityInfo.getEmpiricalValue());
		data.put("activityType", GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		data.put("content", classroom.getName() + "，实践考核：" + activityInfo.getActivityTitle());
		// 实践考核是否有资格进入规则评分页面
        ps.clear();
        ps.put("traineeId", loginUserId);
        ps.put("erId", activityId);
        List<TeaoTraineeExammember> exammemberList = teaoTraineeExammemberMapper.selectListByMap(ps);
    	boolean hasEligible = exammemberList.stream().anyMatch(a -> a.getErId().equals(activityId) && a.getTraineeId().equals(loginUserId));
		// ================================================== 即时通讯相关处理 begin ==================================================
		// 找到本课堂所有有效成员
		params.clear();
		params.put("ctId", ctId);
		params.put("state", "Y");
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		// 组装数据
		String tips = "发起了活动【" + activityInfo.getActivityTitle() + "】";
		JSONObject msg = new JSONObject();
		msg.put("busitype", MsgType.ACTIVITY); // 为活动类型消息时弹出弹窗
		msg.put("activity_type", GlobalActivity.ACTIVITY_9_TRAINEE_EXAM); 
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("send_id", loginUserId);
		busiMsg.put("send_name", null);
		busiMsg.put("tips", tips);
		busiMsg.put("activity_id", activityInfo.getErId());
		busiMsg.put("activity_type", GlobalActivity.ACTIVITY_9_TRAINEE_EXAM);
		busiMsg.put("activity_title", activityInfo.getActivityTitle());
		busiMsg.put("activity_pic", null);
		busiMsg.put("activity_state", "1"); // 活动状态0未开始1进行中2已结束
		busiMsg.put("activityState", "1");
		busiMsg.put("content", classroom.getName() + "，实践考核：" + activityInfo.getActivityTitle());
		busiMsg.put("ct_id", ctId);
		// 返回满足PC端的数据
		busiMsg.put("ctId", ctId);
		busiMsg.put("name", classroom.getName());
		busiMsg.put("classId", classroom.getClassId());
		busiMsg.put("pkgId", classroom.getPkgId());
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(classroom.getPkgId());
		if (pkgInfo != null) {
			busiMsg.put("subjectId", pkgInfo.getSubjectId());	
		}
		busiMsg.put("hasEligible", hasEligible);
		msg.put("msg", busiMsg);
		// 获取tioconfig
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		for (TevglTchClassroomTrainee tevglTchClassroomTrainee : classroomTraineeList) {
			// 绑定用户与群
			Tio.bindGroup(tioConfig, tevglTchClassroomTrainee.getTraineeId(), activityId);
			// 非活动创建者时
			if (!loginUserId.equals(tevglTchClassroomTrainee.getTraineeId())) {
				CBImUtils.sendToUser(tioConfig, tevglTchClassroomTrainee.getTraineeId(), msg);	
			}
		}
		// ================================================== 即时通讯相关处理 end ==================================================
		return R.ok("活动开始").put(Constant.R_DATA, data);
	}

	/**
	 * 结束活动
	 * @param ctId 课堂id
	 * @param activityId 活动id t_eao_trainee_examroom表主键id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R endActivityExamroomInfo(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)
				|| StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(classroom.getClassroomState())) {
			return R.error("课堂尚未开始，无法结束活动");
		}
		if ("3".equals(classroom.getClassroomState())) {
			return R.error("课堂已经结束，无法结束活动");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", classroom.getPkgId());
		params.put("activityId", activityId);
		TeaoTraineeExamroom activityInfo = teaoTraineeExamroomMapper.selectObjectByIdAndPkgId(params);
		if (activityInfo == null) {
			return R.error("无效的活动，请刷新后重试");
		}
		if ("0".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动未开始");
		}
		if ("2".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动已结束");
		}
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("pkgId", classroom.getPkgId());
		ps.put("activityId", activityId);
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(ps);
		if (list == null || list.size() == 0) {
			log.debug("t_evgl_pkg_activity_relation没有数据,直接认为没有权限");
			return R.error("没有权限");
		}
		TevglPkgActivityRelation relation = list.get(0);
		// 权限判断
		/*if (!pkgActivityUtils.hasStartActPermission(activityId, relation, loginUserId, activityInfo.getCreateUserId())) {
			return R.error("暂无权限，操作失败");
		}*/
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(classroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_END);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 调整为修改关联表的状态
		relation.setActivityState("2"); // 0未开始1进行中2已结束
		relation.setActivityEndTime(DateUtils.getNowTimeStamp());
		tevglPkgActivityRelationMapper.update(relation);
		// 返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		// 记录学员经验值
		
		return R.ok("成功结束活动").put(Constant.R_DATA, data);
	}

	/**
	 * 补考
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R saveMakeUp(JSONObject jsonObject, String loginUserId) {
		// 主键id
		String erId = jsonObject.getString("erId");
		// 当前课堂
		String ctId = jsonObject.getString("ctId");
		String pkgId = jsonObject.getString("pkgId");
		String content = jsonObject.getString("content");
		String activityTitle = jsonObject.getString("activityTitle");
		String purpose = jsonObject.getString("purpose");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(erId)) {
			return R.error("必传参数为空");
		}
		TeaoTraineeExamroom oldRoom = teaoTraineeExamroomMapper.selectObjectById(erId);
		if (oldRoom == null) {
			return R.error("参数错误");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("参数错误");
		}
		if ("1".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂未开始，无法补考");
		}
		if ("2".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂已结束，无法补考");
		}
		if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
			return R.error("非法操作");
		}
		// 参与补考的学员
		JSONArray makeupTraineeIds = jsonObject.getJSONArray("makeupTraineeIds");
		if (makeupTraineeIds == null || makeupTraineeIds.size() == 0) {
			return R.error("参与补考的学员不能为空");
		}
		// 参与考核的学员
		JSONArray traineeIds = jsonObject.getJSONArray("traineeIds");
		// 参与考核的教师
		JSONArray teacherIds = jsonObject.getJSONArray("teacherIds");
		if (teacherIds != null) {
			// 将自己加入
			if (!teacherIds.contains(loginUserId)) {
				teacherIds.add(loginUserId);
			}
		}
		if (teacherIds == null || teacherIds.size() == 0) {
			return R.error("参与考核教师不能为空");
		}
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		// 填充信息
		TeaoTraineeExamroom teaoTraineeExamroom = new TeaoTraineeExamroom();
		teaoTraineeExamroom.setErId(erId);
		teaoTraineeExamroom.setErAddr(ctId);
		teaoTraineeExamroom.setSubjectId(oldRoom.getSubjectId());
		teaoTraineeExamroom.setClassIds(oldRoom.getClassIds());
		teaoTraineeExamroom.setState("0"); // 考核结果(0未开始1正在考核2考核结束)
		teaoTraineeExamroom.setCreateTime(DateUtils.getNowTimeStamp());
		teaoTraineeExamroom.setCreateUserId(loginUserId);
		teaoTraineeExamroom.setRemark(content);
		teaoTraineeExamroom.setActivityTitle(activityTitle);
		teaoTraineeExamroom.setEmpiricalValue(10);
		teaoTraineeExamroom.setPurpose(purpose);
		teaoTraineeExamroomMapper.insert(teaoTraineeExamroom);
		// 将参与考核的学员入库
		map.clear();
		for (int i = 0; i < makeupTraineeIds.size(); i++) {
			String classId = oldRoom.getClassIds();
			String traineeId = makeupTraineeIds.getString(i);
			if (traineeId.length() <= 32) {
				continue;
			}
			map.put("traineeId", traineeId);
			map.put("subjectId", oldRoom.getSubjectId());
			teaoTraineeExamineMapper.deleteOther(map);
			teaoTraineeExammemberMapper.deleteOther(map);
			// 查询学员信息
			TevglTraineeInfo trainee = tevglTraineeInfoMapper.selectObjectById(traineeId);
			// 将参与考核的学员入库
			TeaoTraineeExammember member = new TeaoTraineeExammember();
			member.setEtId(Identities.uuid());
			member.setClassId(classId);
			member.setTraineeId(traineeId);
			member.setSubjectId(oldRoom.getSubjectId());
			member.setTraineeHeadimg(StrUtils.isEmpty(trainee.getTraineePic()) ? trainee.getTraineeHead() : trainee.getTraineePic());
			member.setTraineeName(StrUtils.isEmpty(trainee.getTraineeName()) ? trainee.getNickName() : trainee.getTraineeName());
			member.setErId(teaoTraineeExamroom.getErId());
			// 成员类型(0只评分的学员，1学员，2考核负责人，3参与考核教师，4外部专家)
			member.setTraineeType("1");
			// 是否提交了考核(Y/N)
			member.setState("N");
			teaoTraineeExammemberMapper.insert(member);
		}
		// 将参与考评的学员入库
		if (traineeIds != null && traineeIds.size() > 0) {
			for (int j = 0; j < traineeIds.size(); j++) {
				String classId = oldRoom.getClassIds();
				String traineeId = traineeIds.getString(j);
				if(traineeId.length() <= 32) {
					continue;
				}
				if(makeupTraineeIds.contains(traineeId)) {
					continue;
				}
				// 查询学员信息
				TevglTraineeInfo trainee = tevglTraineeInfoMapper.selectObjectById(traineeId);
				//将参与考核的学员入库
				TeaoTraineeExammember member = new TeaoTraineeExammember();
				member.setEtId(Identities.uuid());
				member.setClassId(classId);
				member.setTraineeId(traineeId);
				member.setSubjectId(oldRoom.getSubjectId());
				member.setTraineeHeadimg(StrUtils.isEmpty(trainee.getTraineePic()) ? trainee.getTraineeHead() : trainee.getTraineePic());
				member.setTraineeName(StrUtils.isEmpty(trainee.getTraineeName()) ? trainee.getNickName() : trainee.getTraineeName());
				member.setErId(teaoTraineeExamroom.getErId());
				// 成员类型(0只评分的学员，1学员，2考核负责人，3参与考核教师，4外部专家)
				member.setTraineeType("0");
				// 是否提交了考核(Y/N)
				member.setState("N");
				teaoTraineeExammemberMapper.insert(member);
			}
		}
		// 将参加本次考核的所有学员打乱随机排序
		map.clear();
		map.put("erId", teaoTraineeExamroom.getErId());
		map.put("traineeType", "1");//1学员2考核负责人3参与考核教师4外部专家
		randomSort(teaoTraineeExammemberMapper.selectListByMap(map));
		// 将参与考核的教师入库
		for (int i = 0; i < teacherIds.size(); i++) {
			String teacherId = teacherIds.getString(i);
			if(teacherId.length() != 32) {
				continue;
			}
			// 查询教师信息
			TevglTchTeacher trainee = tevglTchTeacherMapper.selectObjectById(teacherId);
			map.put("traineeId", trainee.getTraineeId());
			map.put("subjectId", oldRoom.getSubjectId());
			map.put("erId", teaoTraineeExamroom.getErId());
			// 校验该教师是否已经添加到该课程的考核，防止重复添加
			List<TeaoTraineeExammember> list = teaoTraineeExammemberMapper.selectListByMap(map);
			if(list.size() > 0) {
				continue;
			}
			// 将参与考核的教师入库
			TeaoTraineeExammember member = new TeaoTraineeExammember();
			member.setEtId(Identities.uuid());
			member.setTraineeId(trainee.getTraineeId());
			member.setSubjectId(oldRoom.getSubjectId());
			member.setTraineeHeadimg(trainee.getTeacherPic());
			member.setTraineeName(trainee.getTeacherName());
			member.setErId(teaoTraineeExamroom.getErId());
			member.setTraineeType("3");
			member.setState("N");
			teaoTraineeExammemberMapper.insert(member);
		}
		return R.ok().put(Constant.R_DATA, teaoTraineeExamroom.getErId());
	}

	/**
	 * 查看基本信息
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewExamroomInfo(String activityId, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TeaoTraineeExamroom teaoTraineeExamroom = teaoTraineeExamroomMapper.selectObjectById(activityId);
		if (teaoTraineeExamroom == null) {
			return R.error("无效的记录");
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		data.put("activityId", activityId);
		data.put("erId", activityId);
		data.put("activityTitle", teaoTraineeExamroom.getActivityTitle());
		data.put("content", teaoTraineeExamroom.getRemark());
		data.put("purpose", teaoTraineeExamroom.getPurpose());
		data.put("traineeIds", null);
		data.put("teacherIds", null);
		// 返回参数考核的学员,成员类型(0只评分的学员，1学员，2考核负责人，3参与考核教师，4外部专家)
		params.clear();
		params.put("erId", activityId);
		params.put("traineeType", "1");
		List<TeaoTraineeExammember> traineeExamMemberList = teaoTraineeExammemberMapper.selectListByMap(params);
		if (traineeExamMemberList != null && traineeExamMemberList.size() > 0)  {
			List<String> traineeIds = traineeExamMemberList.stream().map(a -> a.getTraineeId()).distinct().collect(Collectors.toList());
			data.put("traineeIds", traineeIds);	
		}
		// 返回参与考核的教师
		params.clear();
		params.put("erId", activityId);
		params.put("traineeType", "3");
		List<TeaoTraineeExammember> teacherExamMemberList = teaoTraineeExammemberMapper.selectListByMap(params);
		if (teacherExamMemberList != null && teacherExamMemberList.size() > 0)  {
			List<String> traineeIds = teacherExamMemberList.stream().map(a -> a.getTraineeId()).distinct().collect(Collectors.toList());
			data.put("teacherIds", traineeIds);	
		}
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 查看评分排名结果
	 * @param activityId
	 * @param traineeName
	 * @param mobile
	 * @param jobNumber
	 * @param loginUserId
	 * @return
	 * @apiNote
	 * 1.计算【自评成绩】
	 * 2.计算【互评成绩】
	 * 3.计算【师评成绩】
	 * 4.从字典表去各自的分数权重,计算出最终得分
	 */
	@Override
	public R viewExamResultInfo(String activityId, String traineeName, String mobile, String jobNumber, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TeaoTraineeExamroom teaoTraineeExamroom = teaoTraineeExamroomMapper.selectObjectById(activityId);
		if (teaoTraineeExamroom == null) {
			return R.error("无效的记录，已无法查看");
		}
		// 考核结果(0未开始1正在考核2考核结束)
		/*if ("0".equals(teaoTraineeExamroom.getState())) {
			return R.error("考核未开始，无法查看");
		}
		if ("1".equals(teaoTraineeExamroom.getState())) {
			return R.error("正在考核，无法查看");
		}*/
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("traineeType", "1");
		params.put("traineeName", traineeName);
		params.put("mobile", mobile);
		params.put("jobNumber", jobNumber);
		params.put("erId", activityId);
		params.put("subjectId", teaoTraineeExamroom.getSubjectId());
		String classId = teaoTraineeExamroom.getClassIds();
		String[] classIdArr = classId.split(",");
		if (classIdArr.length > 0 && classIdArr != null) {
			List<String> classIdList = Stream.of(classIdArr).collect(Collectors.toList());
			params.put("classIds", classIdList);
		}
		List<TeaoTraineeExammember> teaoTraineeExammemberList = teaoTraineeExammemberMapper.selectListByMap(params);
		log.debug("根据查询查询考核成员:" + params);
		log.debug("查询结果:" + teaoTraineeExammemberList.size());
		if (teaoTraineeExammemberList != null && teaoTraineeExammemberList.size() > 0) {
			List<String> traineeIds = teaoTraineeExammemberList.stream().map(a -> a.getTraineeId()).distinct().collect(Collectors.toList());
			teaoTraineeExamroom = countFinalScore(teaoTraineeExamroom, traineeIds);
		}
		// 返回数据
		data.put("erId", activityId);
		data.put("activityId", activityId);
		data.put("teaoTraineeExamroom", teaoTraineeExamroom);
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 计算最终成绩
	 * @param teaoTraineeExamroom
	 * @param traineeIds
	 * @return
	 */
	private TeaoTraineeExamroom countFinalScore(TeaoTraineeExamroom teaoTraineeExamroom, List<String> traineeIds) {
		if (teaoTraineeExamroom == null) {
			return new TeaoTraineeExamroom();
		}
		if (traineeIds == null || traineeIds.size() == 0) {
			return teaoTraineeExamroom;
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> studentList = new ArrayList<>();
		// 学员信息
		params.put("traineeIds", traineeIds);
		List<Map<String, Object>> traineeList = tevglTraineeInfoMapper.selectSimpleListByMap(params);
		// 本次考核
		String erId = teaoTraineeExamroom.getErId();
		// 本次考核的平台课程
		String subjectId = teaoTraineeExamroom.getSubjectId();
		for (Map<String, Object> traineeInfo : traineeList) {
			// 处理头像
			traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic"), "16"));
			// 调用方法,设置学员的自评成绩
			getMySelfGiveScore(traineeInfo, erId, subjectId, params);
			// 调用方法,设置学员的互评成绩
			getMyStudentGiveScore(traineeInfo, erId, subjectId, params);
			// 调用方法，设置学员的师评成绩
			getMyTeacherGiveScore(traineeInfo, erId, subjectId, params);
			// 调用方法，设置学员的最终成绩
			getFinalScore(traineeInfo);
			// 加入集合
			studentList.add(traineeInfo);
		}
		// 按成绩降序
		studentList = sort(studentList);
		// 处理同分同名的情况
		ranking(studentList);
		// 填充数据
		teaoTraineeExamroom.setStudentList(studentList);
		return teaoTraineeExamroom;
	}

	/**
	 * 排名
	 * @param studentList
	 * @return
	 */
	private List<Map<String,Object>> sort(List<Map<String,Object>> studentList) {
		// 最终成绩
		if (studentList == null || studentList.size() == 0) {
			return new ArrayList<>();
		}
		return studentList.stream().sorted((h1, h2) -> ((BigDecimal)h2.get("finalScore")).compareTo(((BigDecimal)h1.get("finalScore")))).collect(Collectors.toList());
	}

	/**
	 * 处理最高得分有同分的情况
	 * @param resultList
	 */
	private void ranking(List<Map<String, Object>> resultList) {
		if (resultList == null || resultList.size() == 0) {
			return;
		}
		// 排名
		int index = 0;
		// 最近一次的分
		BigDecimal lastScore = new BigDecimal("-1");
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = resultList.get(i);
			// 如果成绩和上一名的成绩不相同,那么排名+1
			if (!(lastScore.compareTo((BigDecimal) map.get("finalScore")) == 0)) {
				lastScore = (BigDecimal)map.get("finalScore");
				index++;
			}
			// 排名
			map.put("ranking", index);
		}
	}
	
	/**
	 * 实际 计算并设置学员最终成绩（平均成绩）
	 * @param traineeInfo 学员信息
	 * @apiNote
	 * mySelfWeight 自评的权重
	 * myStudentWeight 互评的权重
	 * myTeacherWeight 师评的权重
	 * @return 传入的学员 teaoTraineeInfo
	 */
	private Map<String, Object> getFinalScore(Map<String, Object> traineeInfo) {
		// 调用方法，获取相应得分权重
		Map<String,BigDecimal> md = getExamScoreScale(); 
		BigDecimal mySelfWeight = md.get("自评");
		BigDecimal myStudentWeight = md.get("互评");
		BigDecimal myTeacherWeight = md.get("师评");
		// 自评、互评、师评按权重计算
		BigDecimal my = ((BigDecimal) traineeInfo.get("mySelfGiveScore")).multiply(mySelfWeight);
		BigDecimal his = ((BigDecimal) traineeInfo.get("myStudentGiveScore")).multiply(myStudentWeight);
		BigDecimal teacher = ((BigDecimal) traineeInfo.get("myTeacherGiveScore")).multiply(myTeacherWeight);
		// 计算最终成绩
		BigDecimal temp = my.add(his);
		BigDecimal finalScore = temp.add(teacher);
		BigDecimal finalScoreOk = finalScore.setScale(2, BigDecimal.ROUND_HALF_UP); // 四舍五入保留两位小数
		// 设置最终成绩
		traineeInfo.put("finalScore", finalScoreOk);
		return traineeInfo;
	}
	
	/**
	 * 获取师评分
	 * 【师评成绩】    1.统计多少老师参与师评，得到人数    2.统计师评总分    3. 总分 / 人数 = 师评成绩
	 * @param traineeInfo 学员信息
	 * @param erId 本次考核t_eao_trainee_examroom主键
	 * @param subjectId 本次考核对应的平台课程
	 * @param params 查询条件
	 * @return
	 */
	private Map<String, Object> getMyTeacherGiveScore(Map<String, Object> traineeInfo, String erId, String subjectId, Map<String, Object> params) {
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "3"); // 评分类型(1自评2互评3教师4临时)
		// 参与师评的成绩
		String myTeacherGiveScoreTotal = teaoTraineeExamineMapper.countMyTeacherGiveScore(params);
		// 参与师评的人数
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "1");
		params.put("examineType2", "2");
		List<TeaoTraineeExamine> myTeacherNum = teaoTraineeExamineMapper.selectHowManyTeacherJoinExamine(params);
		BigDecimal myTeacherGiveScore = new BigDecimal("0");
		if (myTeacherNum != null && myTeacherNum.size() > 0) {
			// 计算
			BigDecimal teacherNum = new BigDecimal(myTeacherNum.size());
			myTeacherGiveScore = new BigDecimal(myTeacherGiveScoreTotal).divide(teacherNum, 2, BigDecimal.ROUND_HALF_UP);	
		}
		traineeInfo.put("myTeacherGiveScore", myTeacherGiveScore);
		return traineeInfo;
	}
	
	/**
	 * 获取互评分
	 * 【互评成绩】    1.先查出有多少学员参与互评    2.查出互评成绩的总和    3. 总和 / 人数 = 互评成绩
	 * @param traineeInfo 学员信息
	 * @param erId 本次考核t_eao_trainee_examroom主键
	 * @param subjectId 本次考核对应的平台课程
	 * @param params 查询条件
	 * @return
	 */
	private Map<String, Object> getMyStudentGiveScore(Map<String, Object> traineeInfo, String erId, String subjectId, Map<String, Object> params) {
		// 获得对学员互评的人数
		params.clear();
		params.put("erId", erId);
		params.put("examineType", "2"); // 评分类型(1自评2互评3教师4临时)
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		List<TeaoTraineeExamine> list = teaoTraineeExamineMapper.selectHowManyTraineesJoinExamine(params);
		// 如果没有人给他评分
		if (list == null || list.size() == 0) {
			traineeInfo.put("myStudentGiveScore", new BigDecimal("0"));
			return traineeInfo;
		}
		// 根据条件获得学员互评成绩的总分
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "2"); // 评分类型(1自评2互评3教师4临时)
		// 互评总分
		String score = teaoTraineeExamineMapper.countMyStudentGiveScore(params);
		// 互评人数
		BigDecimal number = new BigDecimal(list.size());
		// 计算
		BigDecimal myStudentGiveScore = new BigDecimal(score).divide(number, 2, BigDecimal.ROUND_HALF_UP);
		traineeInfo.put("myStudentGiveScore", myStudentGiveScore);
		return traineeInfo;
	}
	
	/**
	 * 获取自评分
	 * @param traineeInfo 学员信息
	 * @param erId 本次考核t_eao_trainee_examroom主键
	 * @param subjectId 本次考核对应的平台课程
	 * @param params 查询条件
	 * @return 学员信息
	 */
	private Map<String, Object> getMySelfGiveScore(Map<String, Object> traineeInfo, String erId, String subjectId, Map<String, Object> params) {
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "1"); // 评分类型(1自评2互评3教师4临时)
		String mySelfGiveScore = teaoTraineeExamineMapper.countMySelfGiveScore(params);
		traineeInfo.put("mySelfGiveScore", new BigDecimal(StrUtils.isEmpty(mySelfGiveScore) ? "0" : mySelfGiveScore));
		return traineeInfo;
	}
	
	
	/**
	 * 获取自评、互评、师评的权重比例
	 * @return
	 */
	private Map<String, BigDecimal> getExamScoreScale() {
		List<TsysDict> dicts = dictUtil.getByType("examScoreScale");
		Map<String, BigDecimal> dictMaps = new HashMap<String, BigDecimal>();
		if(dicts != null && dicts.size() > 0) {
			for(TsysDict dict : dicts) {
				dictMaps.put(dict.getDictCode(), new BigDecimal(dict.getDictValue()));
			}
		}
		return dictMaps;
	}

	/**
	 * 查看本次考核某人的给其它所有人评的分
	 * @param traineeId
	 * @param activityId
	 * @param loginUserId
	 * @param type 1查看某人给别人评的规则分2查看某人的规则得分
	 * @return
	 */
	@Override
	public R viewExamDetailInfo(String traineeId, String activityId, String loginUserId, String type) {
		if (StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TeaoTraineeExamroom room = teaoTraineeExamroomMapper.selectObjectById(activityId);
		if (room == null) {
			return R.error("无效的考核记录，无法查看");
		}
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询本次考核的所有规则
		map.clear();
		map.put("subjectId", room.getSubjectId());
		map.put("sidx", "regular_sort");
		map.put("order", "asc");
		List<Map<String, Object>> allRegularList = tevglBookRegularMapper.selectListByMapForSp(map);
		// 本次考核的一级规则
		List<Map<String, Object>> regularList = allRegularList.parallelStream().filter(a -> "-1".equals(a.get("parent_id"))).collect(Collectors.toList());
		// 遍历填充一级规则所包括的二级规则
		regularList.stream().forEach(regP -> {
			List<Map<String, Object>> children = allRegularList.stream().filter(regular -> regP.get("regular_id").equals(regular.get("parent_id"))).collect(Collectors.toList());
			regP.put("children", children);
		});
		// 查询参与本次考核的所有学员信息
		map.clear();
		map.put("erId", room.getErId());
		List<Map<String, Object>> members = teaoTraineeExammemberMapper.selectListByMapForSp(map);
		String classNames = "";
		for (Map<String, Object> m : members) {
			if(!StrUtils.isNull(m.get("class_name")) && !Arrays.asList(classNames.split(", ")).contains(m.get("class_name").toString())) {
				classNames += ", " + m.get("class_name").toString();
			}
		}
		// 保存的考核成绩
		map.clear();
		map.put("erId", activityId);
		map.put("subjectId", room.getSubjectId());
		// 查看某对别人给的规则得分
		type = StrUtils.isEmpty(type) ? "1" : type;
		if ("1".equals(type)) {
			map.put("createUserId", traineeId);	
		}
		// 查看某人的得分
		if ("2".equals(type)) {
			map.put("traineeId", traineeId);	
		}
		List<TeaoTraineeExamine> examdatas = teaoTraineeExamineMapper.selectListByMap(map);
		// 最终返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		// 本次考核信息
		data.put("room", getSimpleRoomInfo(room));
		// 考核规则
		data.put("regularList", regularList);
		// 考核成员
		data.put("memberList", members.parallelStream().filter(a -> "1".equals(a.get("trainee_type"))).collect(Collectors.toList()));
		// 本次已经保存的考核数据
		data.put("examdatas", examdatas);
		return R.ok().put(Constant.R_DATA, data);
	}
}
