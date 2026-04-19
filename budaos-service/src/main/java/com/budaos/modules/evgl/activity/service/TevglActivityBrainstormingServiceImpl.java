package com.budaos.modules.evgl.activity.service;

import com.alibaba.fastjson.JSONObject;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.evgl.activity.api.TevglActivityBrainstormingService;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstorming;
import com.budaos.modules.evgl.activity.persistence.TevglActivityBrainstormingMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.im.core.entity.MsgType;
import com.budaos.modules.im.core.utils.CBImUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: 活动-头脑风暴</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivityBrainstormingServiceImpl implements TevglActivityBrainstormingService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityBrainstormingServiceImpl.class);
	@Autowired
	private TevglActivityBrainstormingMapper tevglActivityBrainstormingMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private PkgActivityUtils pkgActivityUtils;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;


	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityBrainstorming> tevglActivityBrainstormingList = tevglActivityBrainstormingMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityBrainstormingList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityBrainstormingList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityBrainstormingList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityBrainstormingList = tevglActivityBrainstormingMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityBrainstormingList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityBrainstormingList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityBrainstorming
	 * @throws BudaosException
	 */
	public R save(TevglActivityBrainstorming tevglActivityBrainstorming) throws BudaosException {
		tevglActivityBrainstorming.setActivityId(Identities.uuid());
		tevglActivityBrainstorming.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityBrainstorming.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityBrainstorming);
		tevglActivityBrainstormingMapper.insert(tevglActivityBrainstorming);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityBrainstorming
	 * @throws BudaosException
	 */
	public R update(TevglActivityBrainstorming tevglActivityBrainstorming) throws BudaosException {
	    tevglActivityBrainstorming.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityBrainstorming.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityBrainstorming);
		tevglActivityBrainstormingMapper.update(tevglActivityBrainstorming);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityBrainstormingMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityBrainstormingMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		TevglActivityBrainstorming brainstormingInfo = tevglActivityBrainstormingMapper.selectObjectById(id);
		return R.ok().put(Constant.R_DATA, brainstormingInfo);
	}

	/**
	 * 新增活动[头脑风暴]
	 * @param tevglActivityBrainstorming
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 * @throws BudaosException
	 */
	@Override
	@Transactional
	public R saveBrainstormingInfo(TevglActivityBrainstorming tevglActivityBrainstorming, String loginUserId) throws BudaosException {
		String pkgId = tevglActivityBrainstorming.getPkgId();
		String chapterId = tevglActivityBrainstorming.getChapterId();
		// 合法性校验
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		tevglActivityBrainstorming.setResgroupId(GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		R r = checkIsPass(tevglActivityBrainstorming);
		if ((Integer)r.get("code") != 0) {
			return r;
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			if ("3".equals(tevglTchClassroom.getClassroomState())) {
				return R.error("课堂已结束，无法添加活动");
			}
			// 权限判断，用于课堂创建者与课堂助教
			boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
			if (!hasOperationBtnPermission) {
				return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
			}
		} else {
			// 权限校验，用于教学包创建者与章节共建者
			if (!pkgPermissionUtils.hasPermissionAv2(pkgId, loginUserId, chapterId)) {
				return R.error("暂无权限，操作失败");
			}
		}
		// 判断此章节是否已经生成了[活动]分组,如果没有,则生成
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包记录");
		}
		String refPkgId = pkgInfo.getPkgId();
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			refPkgId = pkgInfo.getRefPkgId();
		}
		// 若该章节下没有[活动分组]，则默认创建一个分组（使用情形：新增修改活动时）
		pkgUtils.createDefaultActivityTab(refPkgId, chapterId, loginUserId);
		// 填充信息
		TevglActivityBrainstorming t = new TevglActivityBrainstorming();
		t.setActivityId(Identities.uuid());
		t.setActivityTitle(tevglActivityBrainstorming.getActivityTitle());
		// 创建头脑风暴活动时固定分组
		t.setResgroupId(GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		t.setChapterId(chapterId);
		t.setActivityPic(tevglActivityBrainstorming.getActivityPic());
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setCreateUserId(loginUserId);
		t.setContent(tevglActivityBrainstorming.getContent());
		t.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_2_BRAINSTORMING);
		t.setActivityState("0"); // 0未开始1进行中2已结束
		t.setState("Y"); // Y有效N无效
		t.setActivityType(GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		t.setPurpose(tevglActivityBrainstorming.getPurpose());
		t.setAnswerNumber(0);
		// 排序号处理
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("pkgId", pkgId);
		ps.put("resgroupId", t.getResgroupId());
		Integer sortNum = tevglActivityBrainstormingMapper.getMaxSortNum(ps);
		t.setSortNum(sortNum);
		// 保存数据
		tevglActivityBrainstormingMapper.insert(t);
		// 保存教学包与活动之间的关系
		pkgUtils.buildRelation(pkgId, t.getActivityId(), GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(pkgId);
		return R.ok("活动[头脑风暴] 创建成功");
	}
	
	/**
	 * 修改活动[头脑风暴]
	 * @param tevglActivityBrainstorming
	 * @return
	 */
	@Override
	@Transactional
	public R updateBrainstormingInfo(TevglActivityBrainstorming tevglActivityBrainstorming, String loginUserId) throws BudaosException {
		// 合法性校验
		String pkgId = tevglActivityBrainstorming.getPkgId();
		String activityId = tevglActivityBrainstorming.getActivityId();
		String chapterId = tevglActivityBrainstorming.getChapterId();
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		tevglActivityBrainstorming.setResgroupId(GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		R r = checkIsPass(tevglActivityBrainstorming);
		if ((Integer)r.get("code") != 0) {
			return r;
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			if ("3".equals(tevglTchClassroom.getClassroomState())) {
				return R.error("课堂已结束，无法修改活动");
			}
			// 权限判断，用于课堂创建者与课堂助教
			boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
			if (!hasOperationBtnPermission) {
				return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
			}
		} else {
			// 权限校验，用于教学包创建者与章节共建者
			if (!pkgPermissionUtils.hasPermissionAv2(pkgId, loginUserId, chapterId)) {
				return R.error("暂无权限，操作失败");
			}
		}
		TevglActivityBrainstorming activityInfo = tevglActivityBrainstormingMapper.selectObjectById(activityId);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(activityInfo.getActivityState())) {
			return R.error("活动已开始，无法修改");
		}
		if ("2".equals(activityInfo.getActivityState())) {
			return R.error("活动已结束，无法修改");
		}
		// 判断此章节是否已经生成了[活动]分组,如果没有,则生成
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包记录");
		}
		String refPkgId = pkgInfo.getPkgId();
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			refPkgId = pkgInfo.getRefPkgId();
		}
		pkgUtils.createDefaultActivityTab(refPkgId, chapterId, loginUserId);
		// 填充信息
		tevglActivityBrainstorming.setResgroupId(GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		tevglActivityBrainstorming.setUpdateUserId(loginUserId);
		tevglActivityBrainstorming.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglActivityBrainstormingMapper.update(tevglActivityBrainstorming);
		return R.ok("修改成功");
	}
	
	/**
	 * 删除活动
	 * @param activityId
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 * @throws BudaosException
	 */
	@Override
	public R deleteBrainstormingInfo(String activityId, String pkgId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TevglActivityBrainstorming brainstormingInfo = tevglActivityBrainstormingMapper.selectObjectByIdAndPkgId(params);
		if (brainstormingInfo == null) {
			return R.error("无效的记录");
		}
		// 如果活动已经开始或结束,控制不能删除
		if (!"0".equals(brainstormingInfo.getActivityStateActual())) {
			return R.error("当前活动已被使用，无法删除");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			if ("3".equals(tevglTchClassroom.getClassroomState())) {
				return R.error("课堂已结束，无法删除活动");
			}
			// 权限判断，用于课堂创建者与课堂助教
			boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
			if (!hasOperationBtnPermission) {
				return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
			}
		} else {
			// 权限校验，用于教学包创建者与章节共建者
			if (!pkgPermissionUtils.hasPermissionDeleteActivity(pkgId, brainstormingInfo.getCreateUserId(), loginUserId, brainstormingInfo.getChapterId())) {
				return R.error("暂无权限，操作失败");
			}
		}
		// 删除与教学包的关系
		tevglPkgActivityRelationMapper.deleteByActivityId(activityId);
		// 删除
		tevglActivityBrainstormingMapper.delete(activityId);
		// 教学包活动数量-1
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("删除成功");
	}

	/**
	 * 开启活动
	 * @param ctId 课堂主键
	 * @param activityId 活动主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R startActivityBrainstorming(String ctId, String activityId, String loginUserId, String activityEndTime) throws BudaosException {
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
		TevglActivityBrainstorming activityInfo = tevglActivityBrainstormingMapper.selectObjectByIdAndPkgId(params);
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
		data.put("content", activityInfo.getContent());
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
		msg.put("activity_type", GlobalActivity.ACTIVITY_2_BRAINSTORMING); 
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("send_id", loginUserId);
		busiMsg.put("send_name", null);
		busiMsg.put("tips", tips);
		busiMsg.put("activity_id", activityInfo.getActivityId());
		busiMsg.put("activity_type", GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		busiMsg.put("activity_title", activityInfo.getActivityTitle());
		busiMsg.put("activity_pic", activityInfo.getActivityPic());
		busiMsg.put("activity_state", "1"); // 活动状态0未开始1进行中2已结束
		busiMsg.put("activityState", "1");
		busiMsg.put("content", activityInfo.getContent());
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
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R endActivityBrainstorming(String ctId, String activityId, String loginUserId) throws BudaosException {
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
		//TevglActivityBrainstorming activityInfo = tevglActivityBrainstormingMapper.selectObjectById(activityId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", classroom.getPkgId());
		params.put("activityId", activityId);
		TevglActivityBrainstorming activityInfo = tevglActivityBrainstormingMapper.selectObjectByIdAndPkgId(params);
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
		return R.ok("成功结束活动").put(Constant.R_DATA, data);
	}

	
	/**
	 * 查看活动
	 * @param activityId
	 * @return
	 */
	@Override
	public R viewActivityBrainstormingInfo(String activityId) {
		if (StrUtils.isEmpty(activityId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> brainstormingInfo = tevglActivityBrainstormingMapper.selectObjectMapById(activityId);
		if (brainstormingInfo != null) {
			String activityPic = (String) brainstormingInfo.get("activityPic");
			brainstormingInfo.put("activityPic", uploadPathUtils.stitchingPath(activityPic, "21"));
		}
		return R.ok().put(Constant.R_DATA, brainstormingInfo);
	}

	/**
	 * 合法性校验
	 * @param tevglActivityBrainstorming
	 * @return
	 */
	private R checkIsPass(TevglActivityBrainstorming tevglActivityBrainstorming) {
		String activityTitle = tevglActivityBrainstorming.getActivityTitle(); 
		String resgroupId = tevglActivityBrainstorming.getResgroupId();
		String content = tevglActivityBrainstorming.getContent();
		if (StrUtils.isEmpty(activityTitle)) {
			return R.error("活动标题不能为空");
		}
		activityTitle = activityTitle.trim();
		if (activityTitle.length() > 100) {
			return R.error("活动标题不能超过100个字符");
		}
		if (StrUtils.isEmpty(resgroupId)) {
			return R.error("请选择分组");
		}
		if (StrUtils.isEmpty(content)) {
			return R.error("活动主题不能为空");
		}
		content = content.trim();
		if (content.length() > 3000) {
			return R.error("活动主题不能超过3000个字符");
		}
		return R.ok();
	}
	
	/**
	 * 复制一个新的活动
	 * @param targetActivityId 目标活动
	 * @param newPkgId 新教学包id
	 * @param loginUserId
	 */
	@Override
	public void copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum) {
		if (StrUtils.isEmpty(targetActivityId) || StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(loginUserId)) {
			log.debug("必传参数为空");
			return;
		}
		// 找到目标活动
		TevglActivityBrainstorming activityInfo = tevglActivityBrainstormingMapper.selectObjectById(targetActivityId);
		if (activityInfo == null) {
			return ;
		}
		// 找到目标活动的活动分组(自定义活动分组时记得控制分组名称唯一)
		String resgroupId = "0";
		resgroupId = activityInfo.getResgroupId();
		/*TevglPkgResgroup resgroup = tevglPkgResgroupMapper.selectObjectById(activityInfo.getResgroupId());
		// 不为空则表示是自己定义的活动分组
		if (resgroup != null) {
			Map<String, Object> ps = new HashMap<>();
			ps.put("pkgId", newPkgId);
			ps.put("groupType", GlobalActivity.ACTIVITY_GROUP_TYPE_2);
			List<TevglPkgResgroup> tevglPkgResgroupList = tevglPkgResgroupMapper.selectListByMap(ps);
			List<String> resgroupNameList = tevglPkgResgroupList.stream().map(a -> a.getResgroupName()).collect(Collectors.toList());
			// 若不存在这个分组才添加至数据库
			if (!resgroupNameList.contains(resgroup.getResgroupName())) {
				TevglPkgResgroup rg = new TevglPkgResgroup();
				rg = resgroup;
				rg.setPkgId(newPkgId);
				rg.setResgroupId(Identities.uuid());
				rg.setCreateTime(DateUtils.getNowTimeStamp());
				tevglPkgResgroupMapper.insert(rg);
				resgroupId = rg.getResgroupId();
				log.debug("新的活动分组:" + resgroupId);
			}
		}*/
		// 创建并填充一个新的活动
		TevglActivityBrainstorming t = new TevglActivityBrainstorming();
		t = activityInfo;
		t.setActivityId(Identities.uuid());
		t.setChapterId(StrUtils.isEmpty(chapterId) ? activityInfo.getChapterId() : chapterId);
		t.setActivityTitle(activityInfo.getActivityTitle());
		t.setSortNum(sortNum);
		t.setCreateUserId(loginUserId);
		t.setActivityBeginTime(null);
		t.setActivityEndTime(null);
		t.setState("Y");
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setResgroupId(resgroupId);
		t.setActivityState("0"); // 状态统一改为未开始
		// 保存活动至数据库
		tevglActivityBrainstormingMapper.insert(t);
		String newActivityId = t.getActivityId();
		// 保存活动与教学包的关系
		pkgActivityUtils.buildRelation(newPkgId, newActivityId, GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(newPkgId);
		
	}

	
	
	
}
