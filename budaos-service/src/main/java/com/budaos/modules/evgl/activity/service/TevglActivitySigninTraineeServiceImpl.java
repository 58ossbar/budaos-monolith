package com.budaos.modules.evgl.activity.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.evgl.activity.api.TevglActivitySigninTraineeService;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninInfo;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninTrainee;
import com.budaos.modules.evgl.activity.persistence.TevglActivitySigninInfoMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivitySigninTraineeMapper;
import com.budaos.modules.evgl.empirical.persistence.TevglEmpiricalSettingMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeEmpiricalValueLog;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeEmpiricalValueLogMapper;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p> Title: 学员活动签到信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivitySigninTraineeServiceImpl implements TevglActivitySigninTraineeService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivitySigninTraineeServiceImpl.class);
	@Autowired
	private TevglActivitySigninTraineeMapper tevglActivitySigninTraineeMapper;
	@Autowired
	private TevglActivitySigninInfoMapper tevglActivitySigninInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private TevglTraineeEmpiricalValueLogMapper tevglTraineeEmpiricalValueLogMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private TevglEmpiricalSettingMapper tevglEmpiricalSettingMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivitySigninTrainee> tevglActivitySigninTraineeList = tevglActivitySigninTraineeMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivitySigninTraineeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>>)
	 * @param Map
	 * @return R
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivitySigninTraineeList = tevglActivitySigninTraineeMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivitySigninTraineeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivitySigninTrainee
	 * @throws BudaosException
	 */
	public R save(TevglActivitySigninTrainee tevglActivitySigninTrainee) throws BudaosException {
		tevglActivitySigninTrainee.setStId(Identities.uuid());
		ValidatorUtils.check(tevglActivitySigninTrainee);
		tevglActivitySigninTraineeMapper.insert(tevglActivitySigninTrainee);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivitySigninTrainee
	 * @throws BudaosException
	 */
	public R update(TevglActivitySigninTrainee tevglActivitySigninTrainee) throws BudaosException {
	    ValidatorUtils.check(tevglActivitySigninTrainee);
		tevglActivitySigninTraineeMapper.update(tevglActivitySigninTrainee);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivitySigninTraineeMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivitySigninTraineeMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivitySigninTraineeMapper.selectObjectById(id));
	}

	/**
	 * 根据课堂id和活动id等查询已签到人员
	 * @param params
	 * @return
	 */
	@Override
	public R listActivitySigninTrainee(Map<String, Object> params) {
		String ctId = (String) params.get("ctId");
		String activityId = (String) params.get("activityId");
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		return R.ok();
	}

	/**
	 * 学员签到
	 * @param tevglActivitySigninTrainee
	 * @param loginUserId
	 * @return
	 * @throws BudaosException
	 */
	@Override
	public R signIn(TevglActivitySigninTrainee tevglActivitySigninTrainee, String loginUserId) throws BudaosException {
		String ctId = tevglActivitySigninTrainee.getCtId();
		String activityId = tevglActivitySigninTrainee.getActivityId();
		String signType = tevglActivitySigninTrainee.getSignType();
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(activityId)
				|| StrUtils.isEmpty(ctId) || StrUtils.isEmpty(signType)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tchClassroomInfo = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tchClassroomInfo == null) {
			return R.error("无效的课堂，请刷新后重试");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", tchClassroomInfo.getPkgId());
		params.put("activityId", activityId);
		TevglActivitySigninInfo activityInfo = tevglActivitySigninInfoMapper.selectObjectByIdAndPkgId(params);
		if (activityInfo == null) {
			return R.error("无效的签到活动，请刷新后重试");
		}
		if (!"1".equals(activityInfo.getActivityStateActual())) {
			return R.error("无法签到，签到未开始或者已经结束了");
		}
		if ("2".equals(tevglActivitySigninTrainee.getSignType())) {
			if (StrUtils.isEmpty(tevglActivitySigninTrainee.getNumber())) {
				return R.error("请完成手势签到");
			}
			if (!tevglActivitySigninTrainee.getNumber().equals(activityInfo.getNumber())) {
				return R.error("手势动作不正确，请重新尝试");
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("ctId", ctId);
		List<Map<String,Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(map);
		if (classroomTraineeList != null && classroomTraineeList.size() > 0) {
			boolean flag = classroomTraineeList.stream().anyMatch(a -> a.get("traineeId").equals(loginUserId));
			if (!flag) {
				return R.error("签到未成功，非法操作");
			}
		}
		map.clear();
		map.put("ctId", ctId);
		map.put("activityId", activityId);
		map.put("traineeId", loginUserId);
		List<TevglActivitySigninTrainee> signinTraineeList = tevglActivitySigninTraineeMapper.selectListByMap(map);
		if (signinTraineeList != null && signinTraineeList.size() > 0) {
			return R.ok("你已经签到成功了");
		}
		if ("1".equals(signType)) {
			if (!isInTimeRange(activityInfo.getLimitTime(), activityInfo.getActivityBeginTime())) {
				return R.error("签到未成功，已经过了签到时间，下次记得早点~");
			}	
		}
		String uuid = Identities.uuid();
		tevglActivitySigninTrainee.setStId(uuid);
		tevglActivitySigninTrainee.setSignTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
		tevglActivitySigninTrainee.setSignState("1");
		tevglActivitySigninTrainee.setTraineeId(loginUserId);
		tevglActivitySigninTraineeMapper.insert(tevglActivitySigninTrainee);
		String attachId = tevglActivitySigninTrainee.getAttachId();
		if (StrUtils.isEmpty(attachId)) {
			tsysAttachService.updateAttach(attachId, uuid, "1", "18");
		}
		TevglActivitySigninInfo tevglActivitySigninInfo = new TevglActivitySigninInfo();
		tevglActivitySigninInfo.setActivityId(activityId);
		tevglActivitySigninInfo.setTraineeNum(1);
		tevglActivitySigninInfoMapper.plusNum(tevglActivitySigninInfo);
		doRecordTraineeEmpiricalValueLog(ctId, activityId, loginUserId, activityInfo.getEmpiricalValue(), tevglActivitySigninTrainee.getSignState());
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		JSONObject msg = new JSONObject();
		msg.put("busitype", "reloadtchsignlist");
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("ctId", ctId);
		busiMsg.put("activityId", activityId);
		busiMsg.put("title", "当前有人在课堂 " + tchClassroomInfo.getName() + " 签到");
		msg.put("msg", busiMsg);
		String teacherId = StrUtils.isEmpty(tchClassroomInfo.getReceiverUserId()) ? tchClassroomInfo.getCreateUserId() : tchClassroomInfo.getReceiverUserId();
		CBImUtils.sendToUser(tioConfig, teacherId, msg);
		return R.ok("签到成功").put(Constant.R_DATA, tevglActivitySigninTrainee.getStId());
	}
	
	private void doRecordTraineeEmpiricalValueLog(String ctId, String activityId, String loginUserId, Integer empiricalValue, String signState) {
		TevglTraineeEmpiricalValueLog ev = new TevglTraineeEmpiricalValueLog();
		ev.setEvId(Identities.uuid());
		ev.setActivityId(activityId);
		ev.setType(GlobalActivity.ACTIVITY_8_SIGININ_INFO);
		ev.setTraineeId(loginUserId);
		ev.setCtId(ctId);
		ev.setEmpiricalValue(toGetEmpiricalValue(ctId, signState));
		ev.setCreateTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
		ev.setState("Y");
		ev.setParams2(signState); 
		tevglTraineeEmpiricalValueLogMapper.insert(ev);
	}
	
	private Integer toGetEmpiricalValue(String ctId, String signState) {
		Map<String, Object> map = new HashMap<>();
		map.put("ctId", ctId);
		String dictCode = "";
		switch (signState) {
		case "1":
			dictCode = "4";
			break;
		case "2":
			dictCode = "5";
			break;
		case "3":
			dictCode = "5";
		case "4":
			dictCode = "5";
		case "5":
			dictCode = "7";	
		break;
		default:
			break;
		}
		map.put("dictCode", dictCode);
		Integer empiricalValue = tevglEmpiricalSettingMapper.getEmpiricalValueByMap(map);
		if (Arrays.asList("2", "3", "4", "5").contains(signState)) {
			empiricalValue = empiricalValue == null ? 1 : empiricalValue;
			empiricalValue = -empiricalValue;
		}
		return empiricalValue;
	}
	
	private boolean isInTimeRange(Integer limitTime, String createTime) {
		if (limitTime != null && StrUtils.isNotEmpty(createTime)) {
			Date date = DateUtils.convertStringToDate(createTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, limitTime);
	        date = cal.getTime();
	        if (date.before(new Date())) {
	        	return false;
	        }
		}		
		return true;
	}

	@Override
	public R queryTraineeList(Map<String, Object> params, String loginUserId) {
		if (params.get("ctId") == null || params.get("activityId") == null) {
			return R.error("必传参数为空");
		}
		String ctId = params.get("ctId").toString();
		String activityId = params.get("activityId").toString();
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		params.put("sidx", "t1.create_time");
		params.put("order", "desc");
		List<Map<String, Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		if (classroomTraineeList == null || classroomTraineeList.size() == 0) {
			return R.ok().put(Constant.R_DATA, classroomTraineeList);
		}
		classroomTraineeList.stream().forEach(classroomTrainee -> {
			classroomTrainee.put("signState", "0");
			classroomTrainee.put("signStateName", "未签到成员");
			classroomTrainee.put("traineePic", uploadPathUtils.stitchingPath(classroomTrainee.get("traineePic"), "16"));
		});
		params.clear();
		params.put("ctId", ctId);
		params.put("activityId", activityId);
		params.put("sidx", "dict.dict_code");
		params.put("order", "asc, sign_time desc");
		List<Map<String, Object>> traineeList = tevglActivitySigninTraineeMapper.selectListMapForCommon(params);
		if (traineeList != null && traineeList.size() > 0) {
			classroomTraineeList.stream().forEach(traineeInfo -> {
				traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic"), "16"));
				traineeList.stream().forEach(a -> {
					if (traineeInfo.get("traineeId").equals(a.get("traineeId"))
							&& a.get("ctId").equals(ctId)
							&& a.get("activityId").equals(activityId)) {
						traineeInfo.put("signState", a.get("signState"));
						traineeInfo.put("signStateName", a.get("signStateName"));
						traineeInfo.put("createTime", a.get("signTime"));
						traineeInfo.put("signTime", a.get("signTime"));
					}
				});
				if (!isRoomCreator && !isTeachingAssistant) {
					traineeInfo.remove("mobile");
				}
			});
		}
		Function<Map<String,Object>, String> s = new Function<Map<String,Object>, String>() {
			@Override
			public String apply(Map<String, Object> t) {
				Object object = t.get("signStateName");
				String string = object.toString();
				return string;
			}
		};
		Map<String, List<Map<String, Object>>> collect = classroomTraineeList.stream().collect(Collectors.groupingBy(s));
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for (Entry<String, List<Map<String, Object>>> m : collect.entrySet()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("signStateName", m.getKey());
			map.put("children", m.getValue());
			result.add(map);
		}
		int index = 0;
		for (int i = 0; i < result.size(); i++) {
			if ("未签到成员".equals(result.get(i).get("signStateName"))) {
				index = i;
			}
		}
		if (index != 0) {
			result.add(0, result.get(index));
			result.remove(index + 1);
		}
		return R.ok().put(Constant.R_DATA, result);
	}

	@Override
	public R setTraineeSignState(JSONObject jsonObject, String loginUserId) throws BudaosException {
		String ctId = jsonObject.getString("ctId");
		String activityId = jsonObject.getString("activityId");
		String signState = jsonObject.getString("signState");
		JSONArray jsonArray = jsonObject.getJSONArray("traineeIds");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId)
				|| StrUtils.isEmpty(signState)  || StrUtils.isEmpty(loginUserId)) {
			return R.error(BizCodeEnume.PARAM_MISSING.getCode(), BizCodeEnume.PARAM_MISSING.getMsg());
		}
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("请选择成员");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error(BizCodeEnume.INEFFECTIVE_CLASSROOM.getCode(), BizCodeEnume.INEFFECTIVE_CLASSROOM.getMsg());
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_START);
		if (!hasOperationBtnPermission) {
			return R.error(BizCodeEnume.WITHOUT_PERMISSION.getCode(), BizCodeEnume.WITHOUT_PERMISSION.getMsg());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", tevglTchClassroom.getPkgId());
		params.put("activityId", activityId);
		TevglActivitySigninInfo activityInfo = tevglActivitySigninInfoMapper.selectObjectByIdAndPkgId(params);
		if (activityInfo == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if (!"3".equals(activityInfo.getSignType())) {
			if ("0".equals(activityInfo.getActivityStateActual())) {
				return R.error("活动未开始，无法设置签到状态");
			}
			if ("3".equals(activityInfo.getActivityStateActual())) {
				return R.error("活动已结束，无法设置签到状态");
			}
		}
		Map<String, Object> map = new HashMap<>();
		List<String> traineeIds = jsonArray.stream().map(a -> (String)a).collect(Collectors.toList());
		if (traineeIds != null) {
			Integer empiricalValue = toGetEmpiricalValue(ctId, signState);
			map.put("ctId", ctId);
			map.put("activityId", activityId);
			List<TevglActivitySigninTrainee> signinTraineeList = tevglActivitySigninTraineeMapper.selectListByMap(map);
			if (signinTraineeList == null || signinTraineeList.size() == 0) {
				handleBatchInsert(tevglTchClassroom, activityInfo, traineeIds, signState, empiricalValue);
				TevglActivitySigninInfo sign = new TevglActivitySigninInfo();
				sign.setActivityId(activityId);
				sign.setTraineeNum(traineeIds.size());
				tevglActivitySigninInfoMapper.plusNum(sign);
			} else {
				map.clear();
				map.put("type", "8");
				map.put("ctId", ctId);
				map.put("activityId", activityId);
				List<TevglTraineeEmpiricalValueLog> traineeEmpiricalValueLogList = tevglTraineeEmpiricalValueLogMapper.selectListByMap(map);
				map.clear();
				map.put("ctId", ctId);
				map.put("activityId", activityId);
				List<TevglActivitySigninTrainee> allList = tevglActivitySigninTraineeMapper.selectListByMap(map);
				List<String> stIds = new ArrayList<>();
				Map<String, Object> dataMap = new HashMap<>();
				List<TevglActivitySigninTrainee> insertList = new ArrayList<>();
				List<TevglTraineeEmpiricalValueLog> insertLogList = new ArrayList<>();
				List<TevglTraineeEmpiricalValueLog> updateLogList = new ArrayList<>();
				traineeIds.stream().forEach(traineeId -> {
					List<TevglActivitySigninTrainee> list = allList.stream().filter(a -> a.getTraineeId().equals(traineeId)).collect(Collectors.toList());
					if (list != null && list.size() > 0) {
						stIds.add(list.get(0).getStId());
					} else {
						TevglActivitySigninTrainee t = new TevglActivitySigninTrainee();
						t.setStId(Identities.uuid());
						t.setCtId(ctId);
						t.setActivityId(activityId);
						t.setTraineeId(traineeId);
						t.setSignTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
						t.setSignType(activityInfo.getSignType());
						t.setSignState(signState);
						insertList.add(t);
					}
					List<TevglTraineeEmpiricalValueLog> collect = traineeEmpiricalValueLogList.stream().filter(a -> a.getTraineeId().equals(traineeId)).collect(Collectors.toList());
					if (collect != null && collect.size() > 0) {
						TevglTraineeEmpiricalValueLog valueLog = collect.get(0);
						valueLog.setEmpiricalValue(empiricalValue);
						valueLog.setParams2(signState);
						valueLog.setMsg("在课堂【" + tevglTchClassroom.getName() + "】签到活动中获得" + empiricalValue + "经验值");
						updateLogList.add(valueLog);
					} else {
						TevglTraineeEmpiricalValueLog ev = new TevglTraineeEmpiricalValueLog();
						ev.setEvId(Identities.uuid());
						ev.setActivityId(activityId);
						ev.setType(GlobalActivity.ACTIVITY_8_SIGININ_INFO);
						ev.setTraineeId(traineeId);
						ev.setCtId(ctId);
						ev.setEmpiricalValue(empiricalValue);
						ev.setCreateTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
						ev.setState("Y");
						ev.setMsg("在课堂【" + tevglTchClassroom.getName() + "】签到活动中获得" + empiricalValue + "经验值");
						insertLogList.add(ev);
					}
				});
				if (stIds.size() > 0) {
					dataMap.put("stIds", stIds);
					dataMap.put("signState", signState);
					dataMap.put("signTime", com.budaos.utils.tool.DateUtils.getNowTimeStamp());
					tevglActivitySigninTraineeMapper.updateBatchSignState(dataMap);
				}
				if (updateLogList.size() > 0) {
					tevglTraineeEmpiricalValueLogMapper.updateBatchByCaseWhen(updateLogList);
				}
				if (insertList.size() > 0) {
					tevglActivitySigninTraineeMapper.insertBatch(insertList);
					TevglActivitySigninInfo sign = new TevglActivitySigninInfo();
					sign.setActivityId(activityId);
					sign.setTraineeNum(insertList.size());
					tevglActivitySigninInfoMapper.plusNum(sign);
				}
				if (insertLogList.size() > 0) {
					tevglTraineeEmpiricalValueLogMapper.insertBatch(insertLogList);
				}
			}
		}
		return R.ok("设置成功");
	}
	
	private void handleBatchInsert(TevglTchClassroom tevglTchClassroom, TevglActivitySigninInfo activityInfo, List<String> traineeIds, String signState, Integer empiricalValue) {
		String ctId = tevglTchClassroom.getCtId();
		Map<String, Object> map = new HashMap<>();
		map.put("type", "8");
		map.put("ctId", ctId);
		map.put("activityId", activityInfo.getActivityId());
		List<TevglTraineeEmpiricalValueLog> traineeEmpiricalValueLogList = tevglTraineeEmpiricalValueLogMapper.selectListByMap(map);
		List<TevglActivitySigninTrainee> insertList = new ArrayList<>();
		List<TevglTraineeEmpiricalValueLog> insertLogList = new ArrayList<>();
		traineeIds.stream().forEach(traineeId -> {
			TevglActivitySigninTrainee t = new TevglActivitySigninTrainee();
			t.setStId(Identities.uuid());
			t.setCtId(ctId);
			t.setActivityId(activityInfo.getActivityId());
			t.setTraineeId(traineeId);
			t.setSignTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
			t.setSignType(activityInfo.getSignType());
			t.setSignState(signState);
			insertList.add(t);
			List<TevglTraineeEmpiricalValueLog> collect = traineeEmpiricalValueLogList.stream().filter(a -> a.getTraineeId().equals(traineeId)).collect(Collectors.toList());
			if (collect == null || collect.size() == 0) {
				TevglTraineeEmpiricalValueLog ev = new TevglTraineeEmpiricalValueLog();
				ev.setEvId(Identities.uuid());
				ev.setActivityId(activityInfo.getActivityId());
				ev.setType(GlobalActivity.ACTIVITY_8_SIGININ_INFO);
				ev.setTraineeId(traineeId);
				ev.setCtId(ctId);
				ev.setEmpiricalValue(empiricalValue);
				ev.setCreateTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
				ev.setState("Y");
				ev.setMsg("在课堂【" + tevglTchClassroom.getName() + "】的签到活动中，签到获得" + empiricalValue + "经验值");
				insertLogList.add(ev);
			}
		});
		if (insertList.size() > 0) {
			tevglActivitySigninTraineeMapper.insertBatch(insertList);
		}
		if (insertLogList.size() > 0) {
			tevglTraineeEmpiricalValueLogMapper.insertBatch(insertLogList);
		}
	}

	@Override
	public R viewTraineeSignInfo(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglActivitySigninTrainee signinTrainee = null;
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("activityId", activityId);
		params.put("traineeId", loginUserId);
		List<TevglActivitySigninTrainee> list = tevglActivitySigninTraineeMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			signinTrainee = list.get(0);
			if (signinTrainee != null) {
				signinTrainee.setFile(uploadPathUtils.stitchingPath(signinTrainee.getFile(), "18"));
				if (StrUtils.isNotEmpty(signinTrainee.getSignTime())) {
					signinTrainee.setSignTime(signinTrainee.getSignTime().substring(11, 16));
				}
			}
		}
		return R.ok().put(Constant.R_DATA, signinTrainee);
	}
	
	@Override
	public R queryTraineeSigninDetail(Map<String, Object> params) {
		if (params.get("ctId") == null) {
			return R.error("参数ctId为空");
		}
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String,Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(query);
		Integer totalNum = tevglTchClassroomMapper.countSignupActivityNum(params.get("ctId").toString());
		List<Map<String, Object>> signinTraineeListData = getSigninTraineeListData(params.get("ctId").toString(), params);
		classroomTraineeList.stream().forEach(traineeInfo -> {
			long normalNum = signinTraineeListData.stream().filter(a -> "1".equals(a.get("signState")) && a.get("traineeId").equals(traineeInfo.get("traineeId"))).count();
			long lateNum = signinTraineeListData.stream().filter(a -> "2".equals(a.get("signState")) && a.get("traineeId").equals(traineeInfo.get("traineeId"))).count();
			long leaveEarlyNum = signinTraineeListData.stream().filter(a -> "3".equals(a.get("signState")) && a.get("traineeId").equals(traineeInfo.get("traineeId"))).count();
			long truantNum = signinTraineeListData.stream().filter(a -> "4".equals(a.get("signState")) && a.get("traineeId").equals(traineeInfo.get("traineeId"))).count();
			long leaveNum = signinTraineeListData.stream().filter(a -> "5".equals(a.get("signState")) && a.get("traineeId").equals(traineeInfo.get("traineeId"))).count();
			traineeInfo.put("normalNum", normalNum);
			traineeInfo.put("lateNum", lateNum);
			traineeInfo.put("leaveEarlyNum", leaveEarlyNum);
			traineeInfo.put("truantNum", truantNum);
			traineeInfo.put("leaveNum", leaveNum);
			traineeInfo.put("totalNum", totalNum);
			if (totalNum == null || totalNum == 0) {
				traineeInfo.put("attendanceRate", new BigDecimal("0"));
			} else {
				traineeInfo.put("attendanceRate", new BigDecimal(normalNum).divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString() + "%");
			}
			if (traineeInfo.get("traineePic") != null) {
				traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic").toString(), "16"));
			}
		});
		PageUtils pageUtil = new PageUtils(classroomTraineeList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	private List<Map<String, Object>> getSigninTraineeListData(String ctId, Map<String, Object> params) {
		params.clear();
		params.put("ctId", ctId);
		List<Map<String, Object>> list = tevglActivitySigninTraineeMapper.selectListMapForCommon(params);
		return list;
	}
}
