package com.budaos.modules.evgl.activity.service;

import com.alibaba.fastjson.JSONObject;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.common.PkgUtils;
import com.budaos.modules.evgl.activity.api.TevglActivityLiveService;
import com.budaos.modules.evgl.activity.domain.TevglActivityLive;
import com.budaos.modules.evgl.activity.persistence.TevglActivityLiveMapper;
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
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class TevglActivityLiveServiceImpl implements TevglActivityLiveService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityLiveServiceImpl.class);
	@Autowired
	private TevglActivityLiveMapper tevglActivityLiveMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired	
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityLive> tevglActivityLiveList = tevglActivityLiveMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityLiveList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityLiveList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityLiveList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>>)
	 * @param Map
	 * @return R
	 */
	public R queryForMap(Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityLiveList = tevglActivityLiveMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityLiveList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityLiveList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityLive
	 * @throws BudaosException
	 */
	public R save(TevglActivityLive tevglActivityLive) throws BudaosException {
		tevglActivityLive.setActivityId(Identities.uuid());
		tevglActivityLive.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityLive.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityLive);
		tevglActivityLiveMapper.insert(tevglActivityLive);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityLive
	 * @throws BudaosException
	 */
	public R update(TevglActivityLive tevglActivityLive) throws BudaosException {
	    tevglActivityLive.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityLive.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityLive);
		tevglActivityLiveMapper.update(tevglActivityLive);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityLiveMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityLiveMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityLiveMapper.selectObjectById(id));
	}

	@Override
	public R saveLive(TevglActivityLive tevglActivityLive, String loginUserId) {
		R r = checkIsPass(tevglActivityLive, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		String pkgId = tevglActivityLive.getPkgId();
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("参数异常，添加失败");
		}
		if ("3".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂已结束，无法添加活动");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		tevglActivityLive.setActivityId(Identities.uuid());
		tevglActivityLive.setCreateTime(DateUtils.getNowTimeStamp());
		tevglActivityLive.setCreateUserId(loginUserId);
		tevglActivityLive.setResgroupId(GlobalActivity.ACTIVITY_7_LIGHT_LIVE);
		tevglActivityLive.setActivityState("0");
		Map<String, Object> ps = new HashMap<>();
		ps.put("pkgId", pkgId);
		Integer sortNum = tevglActivityLiveMapper.getMaxSortNum(ps);
		tevglActivityLive.setSortNum(sortNum);
		tevglActivityLiveMapper.insert(tevglActivityLive);
		pkgUtils.buildRelation(pkgId, tevglActivityLive.getActivityId(), GlobalActivity.ACTIVITY_7_LIGHT_LIVE);
		pkgUtils.plusPkgActivityNum(pkgId);
		return R.ok("新增成功");
	}

	@Override
	public R updateLive(TevglActivityLive tevglActivityLive, String loginUserId) {
		R r = checkIsPass(tevglActivityLive, loginUserId);
		if ((Integer)r.get("code") != 0) {
			return r;
		}
		String pkgId = tevglActivityLive.getPkgId();
		String activityId = tevglActivityLive.getActivityId();
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("参数异常，无法修改");
		}
		if ("3".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂已结束，无法修改活动");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		TevglActivityLive activityInfo = tevglActivityLiveMapper.selectObjectById(activityId);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(activityInfo.getActivityState())) {
			return R.error("活动已开始，无法修改");
		}
		if ("2".equals(activityInfo.getActivityState())) {
			return R.error("活动已结束，无法修改");
		}
		tevglActivityLive.setUpdateUserId(loginUserId);
		tevglActivityLive.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglActivityLiveMapper.update(tevglActivityLive);
		return R.ok("修改成功");
	}
	
	private R checkIsPass(TevglActivityLive tevglActivityLive, String loginUserId){
		if (StrUtils.isEmpty(tevglActivityLive.getPkgId()) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(tevglActivityLive.getActivityTitle())) {
			return R.error("标题不能为空");
		}
		String title = tevglActivityLive.getActivityTitle().trim();
		if (StrUtils.isEmpty(title)) {
			return R.error("标题不能为空");
		}
		if (title.length() > 50) {
			return R.error("标题不能超过50个字符");
		}
		return R.ok();
	}

	@Override
	public R viewLive(String activityId) {
		TevglActivityLive tevglActivityLive = tevglActivityLiveMapper.selectObjectById(activityId);
		return R.ok().put(Constant.R_DATA, tevglActivityLive);
	}

	@Override
	public R deleteLive(String activityId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TevglActivityLive activityInfo = tevglActivityLiveMapper.selectObjectByIdAndPkgId(params);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("参数异常，无法删除");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		tevglPkgActivityRelationMapper.deleteByActivityId(activityId);
		tevglTchClassroomMapper.delete(activityId);
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("删除成功");
	}

	@Override
	public R startActivityLive(String ctId, String activityId, String loginUserId, String activityEndTime) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)
				|| StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("参数异常，无效的课堂");
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
		TevglActivityLive activityInfo = tevglActivityLiveMapper.selectObjectByIdAndPkgId(params);
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
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(classroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_START);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		relation.setActivityState("1");
		relation.setActivityBeginTime(DateUtils.getNowTimeStamp());
		relation.setActivityEndTime(null);
		tevglPkgActivityRelationMapper.update(relation);
		Map<String, Object> data = new HashMap<>();
		data.put("empiricalValue", activityInfo.getEmpiricalValue());
		data.put("activityType", GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		data.put("content", activityInfo.getContent());
		params.clear();
		params.put("ctId", ctId);
		params.put("state", "Y");
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		String tips = "发起了活动【" + activityInfo.getActivityTitle() + "】";
		JSONObject msg = new JSONObject();
		msg.put("busitype", MsgType.ACTIVITY);
		msg.put("activity_type", GlobalActivity.ACTIVITY_2_BRAINSTORMING);
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("send_id", loginUserId);
		busiMsg.put("send_name", null);
		busiMsg.put("tips", tips);
		busiMsg.put("activity_id", activityInfo.getActivityId());
		busiMsg.put("activity_type", GlobalActivity.ACTIVITY_7_LIGHT_LIVE);
		busiMsg.put("activity_title", activityInfo.getActivityTitle());
		busiMsg.put("activity_pic", "");
		busiMsg.put("activity_state", "1");
		busiMsg.put("activityState", "1");
		busiMsg.put("content", activityInfo.getContent());
		busiMsg.put("ct_id", ctId);
		busiMsg.put("ctId", ctId);
		busiMsg.put("name", classroom.getName());
		busiMsg.put("classId", classroom.getClassId());
		busiMsg.put("pkgId", classroom.getPkgId());
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(classroom.getPkgId());
		if (pkgInfo != null) {
			busiMsg.put("subjectId", pkgInfo.getSubjectId());
		}
		msg.put("msg", busiMsg);
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		for (TevglTchClassroomTrainee tevglTchClassroomTrainee : classroomTraineeList) {
			Tio.bindGroup(tioConfig, tevglTchClassroomTrainee.getTraineeId(), activityId);
			if (!loginUserId.equals(tevglTchClassroomTrainee.getTraineeId())) {
				CBImUtils.sendToUser(tioConfig, tevglTchClassroomTrainee.getTraineeId(), msg);
			}
		}
		return R.ok("活动开始").put(Constant.R_DATA, data);
	}

	@Override
	public R endActivityLive(String ctId, String activityId, String loginUserId) {
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
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", classroom.getPkgId());
		params.put("activityId", activityId);
		TevglActivityLive activityInfo = tevglActivityLiveMapper.selectObjectByIdAndPkgId(params);
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
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(classroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_END);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		relation.setActivityState("2");
		relation.setActivityEndTime(DateUtils.getNowTimeStamp());
		tevglPkgActivityRelationMapper.update(relation);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		return R.ok("成功结束活动").put(Constant.R_DATA, data);
	}
}
