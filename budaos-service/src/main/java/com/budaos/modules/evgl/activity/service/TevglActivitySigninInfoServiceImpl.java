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
import com.budaos.modules.evgl.activity.api.TevglActivitySigninInfoService;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninInfo;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninTrainee;
import com.budaos.modules.evgl.activity.persistence.TevglActivitySigninInfoMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivitySigninTraineeMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> Title: 活动-签到活动信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivitySigninInfoServiceImpl implements TevglActivitySigninInfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivitySigninInfoServiceImpl.class);
	@Autowired
	private TevglActivitySigninInfoMapper tevglActivitySigninInfoMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglActivitySigninTraineeMapper tevglActivitySigninTraineeMapper;
	@Autowired
	private PkgUtils pkgUtils;
	
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	
	// 文件上传路径
	@Value("${com.budaos.file-upload-path}")
    private String uploadPath;
	// 文件显示路径
	@Value("${com.budaos.file-access-path}")
	private String accessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
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
		List<TevglActivitySigninInfo> tevglActivitySigninInfoList = tevglActivitySigninInfoMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivitySigninInfoList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivitySigninInfoList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivitySigninInfoList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglActivitySigninInfoList = tevglActivitySigninInfoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivitySigninInfoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivitySigninInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivitySigninInfo
	 * @throws BudaosException
	 */
	public R save(TevglActivitySigninInfo tevglActivitySigninInfo) throws BudaosException {
		tevglActivitySigninInfo.setActivityId(Identities.uuid());
		tevglActivitySigninInfo.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivitySigninInfo.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivitySigninInfo);
		tevglActivitySigninInfoMapper.insert(tevglActivitySigninInfo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivitySigninInfo
	 * @throws BudaosException
	 */
	public R update(TevglActivitySigninInfo tevglActivitySigninInfo) throws BudaosException {
	    tevglActivitySigninInfo.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivitySigninInfo.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivitySigninInfo);
		tevglActivitySigninInfoMapper.update(tevglActivitySigninInfo);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivitySigninInfoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivitySigninInfoMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivitySigninInfoMapper.selectObjectById(id));
	}

	/**
	 * 新增一个签到活动
	 * @param tevglActivitySigninInfo
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R saveSigninInfo(TevglActivitySigninInfo tevglActivitySigninInfo, String pkgId, String loginUserId) throws BudaosException {
		String chapterId = tevglActivitySigninInfo.getChapterId();
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("非法数据");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		if ("3".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂已结束，无法添加活动");
		}
		// 权限校验
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_ADD);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		loginUserId = tevglTchClassroom.getCreateUserId();
		tevglActivitySigninInfo.setActivityId(Identities.uuid());
		tevglActivitySigninInfo.setActivityType(GlobalActivity.ACTIVITY_8_SIGININ_INFO);
		tevglActivitySigninInfo.setState("Y");
		tevglActivitySigninInfo.setActivityState("0"); // 0未开始1进行中2已结束
		tevglActivitySigninInfo.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_8_SIGININ_INFO);
		tevglActivitySigninInfo.setCreateTime(DateUtils.getNowTimeStamp());
		tevglActivitySigninInfo.setCreateUserId(loginUserId);
		tevglActivitySigninInfo.setTraineeNum(0);
		tevglActivitySigninInfo.setIsTraineesVisible("Y"); // 学员是否可见
		if ("1".equals(tevglActivitySigninInfo.getSignType())) {
			tevglActivitySigninInfo.setActivityTitle("普通签到");
		}
		// 如果为手势签到
		if ("2".equals(tevglActivitySigninInfo.getSignType())) {
			if (StrUtils.isEmpty(tevglActivitySigninInfo.getNumber())) {
				return R.error("请设置签到的手势动作！");
			}
			tevglActivitySigninInfo.setNumber(tevglActivitySigninInfo.getNumber());
			tevglActivitySigninInfo.setActivityTitle("手势签到");
		}
		// 如果为手工签到
		if ("3".equals(tevglActivitySigninInfo.getSignType())) {
			tevglActivitySigninInfo.setIsTraineesVisible("N");
			tevglActivitySigninInfo.setActivityTitle("手工签到");
		}
		tevglActivitySigninInfoMapper.insert(tevglActivitySigninInfo);
		// 保存活动与教学包的关系
		pkgUtils.buildRelation(pkgId, tevglActivitySigninInfo.getActivityId(), GlobalActivity.ACTIVITY_8_SIGININ_INFO);
		// 更新教学包的活动数量
		pkgUtils.plusPkgActivityNum(pkgId);
		return R.ok("[签到活动] 创建成功").put(Constant.R_DATA, tevglActivitySigninInfo.getActivityId());
	}
	
	/**
	 * 修改签到活动
	 * @param tevglActivitySigninInfo
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R updateSigninInfo(TevglActivitySigninInfo tevglActivitySigninInfo, String pkgId, String loginUserId) {
		String chapterId = tevglActivitySigninInfo.getChapterId();
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("非法数据");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		if ("3".equals(tevglTchClassroom.getClassroomState())) {
			return R.error("课堂已结束，无法修改活动");
		}
		TevglActivitySigninInfo activityInfo = tevglActivitySigninInfoMapper.selectObjectById(tevglActivitySigninInfo.getActivityId());
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(activityInfo.getActivityState())) {
			return R.error("活动已开始，无法修改");
		}
		if ("2".equals(activityInfo.getActivityState())) {
			return R.error("活动已结束，无法修改");
		}
		// 权限判断
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_EDIT);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 如果为手势签到
		if ("2".equals(tevglActivitySigninInfo.getSignType())) {
			if (StrUtils.isEmpty(tevglActivitySigninInfo.getNumber())) {
				return R.error("请设置签到的手势动作！");
			}
			tevglActivitySigninInfo.setNumber(tevglActivitySigninInfo.getNumber());
			tevglActivitySigninInfo.setActivityTitle(activityInfo.getActivityTitle());
			tevglActivitySigninInfo.setEmpiricalValue(activityInfo.getEmpiricalValue());
		}
		tevglActivitySigninInfoMapper.update(tevglActivitySigninInfo);
		return R.ok("保存成功");
	}

	/**
	 * 查看签到活动基本信息
	 * @param activityId
	 * @return
	 */
	@Override
	public R viewSigninInfo(String activityId) {
		if (StrUtils.isEmpty(activityId)) {
			return R.error("必传参数为空");
		}
		TevglActivitySigninInfo signinInfo = tevglActivitySigninInfoMapper.selectObjectById(activityId);
		if (signinInfo == null) {
			return R.error("无效的记录");
		}
		return R.ok().put(Constant.R_DATA, signinInfo);
	}

	/**
	 * 删除签到活动
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteSigninInfo(String activityId, String pkgId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TevglActivitySigninInfo activityInfo = tevglActivitySigninInfoMapper.selectObjectByIdAndPkgId(params);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		// 权限判断
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_DELETE);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		/*if ("1".equals(activityInfo.getActivityStateActual())) {
			return R.error("签到活动进行中，无法删除活动");
		}
		if ("2".equals(activityInfo.getActivityStateActual())) {
			return R.error("签到活动已结束，无法删除活动");
		}*/
		// 权限校验
		/*if (!pkgPermissionUtils.hasPermissionDeleteActivity(pkgId, activityInfo.getCreateUserId(), loginUserId, activityInfo.getChapterId())) {
			return R.error("暂无权限，操作失败");
		}*/
		// 删除签到文件
		params.clear();
		params.put("activityId", activityId);
		List<TevglActivitySigninTrainee> signinTrainees = tevglActivitySigninTraineeMapper.selectListByMap(params);
		for (TevglActivitySigninTrainee tevglActivitySigninTrainee : signinTrainees) {
			String path = uploadPath + uploadPathUtils.getPathByParaNo("18") + "/" + tevglActivitySigninTrainee.getFile();
			File file = new File(path);
			//如果文件存在就删除
			if (file.exists()) {
				file.delete();
			}
		}
		// 删除签到记录
		tevglActivitySigninTraineeMapper.deleteByActivityId(activityId);
		// 删除活动与教学包的关系
		tevglPkgActivityRelationMapper.deleteByActivityId(activityId);
		// 删除活动
		tevglActivitySigninInfoMapper.delete(activityId);
		// 教学包活动数量-1
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("删除成功");
	}
	
	/**
	 * 查看签到历史记录
	 * @param ctId 课堂主键
	 * @param activityId 活动主键
	 * @return
	 */
	@Override
	public R queryHistorySigninInfo(Map<String, Object> params) {
		String ctId = params.get("ctId").toString();
		if (StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("未能找到相关数据");
		}
		params.put("state", "Y");
		// 查询该课堂所有有效的签到活动
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String,Object>> list = tevglActivitySigninInfoMapper.selectListMapByMap(query);
		convertUtil.convertDict(list, "signTypeName", "sign_type");
		list.stream().forEach(info -> {
			if (info.get("createTime") != null) {
				String createTime = (String) info.get("createTime");
				info.put("date", createTime.substring(0, 10));
				info.put("time", createTime.substring(11, 16));
				// 计算创建日期处于星期几
				String val = com.budaos.modules.common.DateUtils.getDayOfWeek(createTime.substring(0, 10));
				info.put("dayOfWeek", val);
			}
		});
		// 查询该课堂所有成员（作为应到总人数）
		List<Map<String, Object>> classroomTraineeList = getClassroomTraineeListData(ctId, params);
		// 查询此课堂所有的签到记录
		List<Map<String, Object>> signinTraineeListData = getSigninTraineeListData(ctId, params);
		// 遍历筛选
		list.stream().forEach(signInfo -> {
			// 总人数
			signInfo.put("totalNum", classroomTraineeList.size());
			// 实际签到人数
			long count = signinTraineeListData.stream().filter(a -> a.get("activityId").equals(signInfo.get("activityId"))).count();
			signInfo.put("currentNum", count);
		});
		PageUtils pageUtil = new PageUtils(list,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询此课堂所有的签到记录
	 * @param ctId
	 * @param params
	 * @return
	 */
	private List<Map<String, Object>> getSigninTraineeListData(String ctId, Map<String, Object> params) {
		params.clear();
		params.put("ctId", ctId);
		List<Map<String, Object>> list = tevglActivitySigninTraineeMapper.selectListMapForCommon(params);
		return list;
	}

	/**
	 * 查询该课堂所有成员
	 * @param ctId
	 * @return
	 */
	private List<Map<String, Object>> getClassroomTraineeListData(String ctId, Map<String, Object> params) {
		params.clear();
		params.put("ctId", ctId);
		List<Map<String, Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		return classroomTraineeList;
	}

	/**
	 * 取部分属性
	 * @param a
	 * @return
	 */
	private Map<String, Object> converToSimpleInfo(TevglActivitySigninInfo a){
		Map<String, Object> info = new HashMap<>();		
		info.put("activityId", a.getActivityId());
		info.put("activityTitle", a.getActivityTitle());
		info.put("signType", a.getSignType()); // 签到类型
		info.put("signTypeName", a.getSignType());
		info.put("date", a.getCreateTime().substring(0, 10));
		info.put("time", a.getCreateTime().substring(12, 16));
		// 计算创建日期处于星期几
		String val = com.budaos.modules.common.DateUtils.getDayOfWeek(a.getCreateTime().substring(0, 10));
		info.put("dayOfWeek", val);
		return info;
	}

	/**
	 * 删除
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteHistorySigninInfo(String activityId, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglActivitySigninInfo activityInfo = tevglActivitySigninInfoMapper.selectObjectById(activityId);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		TevglActivitySigninInfo t = new TevglActivitySigninInfo();
		t.setActivityId(activityId);
		t.setState("N");
		tevglActivitySigninInfoMapper.update(t);
		return R.ok();
	}

	
	
	/**
	 * 开始签到活动
	 * @param ctId 课堂主键
	 * @param activityId 活动主键
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R startSigninInfo(String ctId, String activityId, String loginUserId, String activityEndTime) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
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
		TevglActivitySigninInfo activityInfo = tevglActivitySigninInfoMapper.selectObjectByIdAndPkgId(params);
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
		// 手工签到时
		if ("3".equals(activityInfo.getSignType())) {
			// 获取此课堂所有成员
			ps.clear();
			ps.put("ctId", ctId);
			List<Map<String, Object>> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(ps);
			// 如果有成员
			if (classroomTraineeList != null && classroomTraineeList.size() > 0) {
				classroomTraineeList.stream().forEach(a -> {
					// 生成零状态的学员签到记录
					TevglActivitySigninTrainee st = new TevglActivitySigninTrainee();
					st.setStId(Identities.uuid());
					st.setCtId(ctId);
					st.setActivityId(activityId);
					st.setTraineeId(a.get("traineeId").toString());
					st.setSignTime(DateUtils.getNowTimeStamp());
					st.setSignState("0"); // 0没签到，签到状态1正常2迟到3早退4旷课5请假
					st.setSignType(activityInfo.getSignType());
					tevglActivitySigninTraineeMapper.insert(st);
				});
			}	
		} else { // 普通签到（拍照打卡）、手势签到时
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
			msg.put("activity_type", GlobalActivity.ACTIVITY_8_SIGININ_INFO); 
			JSONObject busiMsg = new JSONObject();
			busiMsg.put("send_id", loginUserId);
			busiMsg.put("send_name", null);
			busiMsg.put("tips", tips);
			busiMsg.put("activity_id", activityInfo.getActivityId());
			busiMsg.put("activity_type", GlobalActivity.ACTIVITY_8_SIGININ_INFO);
			busiMsg.put("activity_title", activityInfo.getActivityTitle());
			busiMsg.put("activity_pic", "");
			busiMsg.put("activity_state", "1"); // 活动状态0未开始1进行中2已结束
			busiMsg.put("activityState", "1");
			busiMsg.put("content", "");
			busiMsg.put("ct_id", ctId);
			busiMsg.put("sign_type", activityInfo.getSignType());
			busiMsg.put("isSigned", false);
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
			/* 公告测试
			// 获取Tioconfig
			ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
			// 组装返回前端的数据
			String title = "公告提醒";
			String content = "测试小程序：\n1.即时通讯继续测试优化中\n2.创蓝欢迎您";
			JSONObject msg = CBImTipsUtils.confirm(title, content);
			// 获取课堂群成员
			Map<String, Object> params = new HashMap<String, Object>();
			List<TimUserinfo> timUserinfoList = timUserinfoMapper.selectListByMap(params);
			for (TimUserinfo timUserinfo : timUserinfoList) {
				CBImUtils.sendToUser(tioConfig, timUserinfo.getUserId(), msg);
			}*/
			// ================================================== 即时通讯相关处理 end ==================================================
		}
		// 返回数据
		Map<String, Object> data = new HashMap<>();
		data.put("empiricalValue", activityInfo.getEmpiricalValue());
		data.put("activityType", GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		data.put("content", "");
		data.put("signType", activityInfo.getSignType());
		return R.ok("开始签到").put(Constant.R_DATA, data);
	}
	
	/**
	 * 结束签到活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R endSingninInfo(String ctId, String activityId, String loginUserId) {
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
		TevglActivitySigninInfo activityInfo = tevglActivitySigninInfoMapper.selectObjectById(activityId);
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
	 * PC网站端查看出勤统计图
	 * @param ctId
	 * @return
	 */
	@Override
	public R viewAttendanceStatisticsChart(String ctId) {
		if (StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}

		Map<String, Object> brokenLineInfo = new HashMap<>();
		List<String> xDayList = getBrokenLineInfoxAxisArr();
		List<Map<String, Object>> seriesArr = countSeriesArr(ctId, xDayList);
		brokenLineInfo.put("xAxisArr", xDayList);
		brokenLineInfo.put("seriesArr", seriesArr);
		brokenLineInfo.put("title", "当天最新出勤统计图（%）");

		Map<String, Object> otherInfo = new HashMap<>();
		// 考勤次数（进行中、已结束的签到活动）（这里取一天中最新的签到活动）
		otherInfo.put("totalNum", tevglTchClassroomMapper.countSignupActivityNum(ctId));
		// 平均出勤率（seriesArr总和 / xDayList天数）
		Map<String, Object> map = seriesArr.get(0);
		BigDecimal totalValue = (BigDecimal) map.get("totalValue");
		otherInfo.put("averageAttendance", totalValue.divide(new BigDecimal(xDayList.size()), 2, BigDecimal.ROUND_HALF_UP));

		// 返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("brokenLineInfo", brokenLineInfo);
		data.put("otherInfo", otherInfo);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	public List<Map<String, Object>> countSeriesArr(String ctId, List<String> dayList){
		if (StrUtils.isEmpty(ctId) || dayList == null || dayList.size() == 0) {
			return new ArrayList<>();
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return new ArrayList<>();
		}
		// 最终返回数据
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> m = new HashMap<>();
		m.put("name", "出勤率");
		m.put("type", "line");
		m.put("stack", "总量");
		List<BigDecimal> data = new ArrayList<>();
		// 先查出这个课堂这些天内的签到活动
		Map<String, Object> map = new HashMap<>();
		map.put("pkgId", tevglTchClassroom.getPkgId());
		map.put("activityType", GlobalActivity.ACTIVITY_8_SIGININ_INFO);
		map.put("activityStateList", Arrays.asList("1", "2"));
		map.put("sidx", "activity_begin_time");
		map.put("order", "desc");
		List<TevglPkgActivityRelation> tevglPkgActivityRelations = tevglPkgActivityRelationMapper.selectListByMap(map);
		// 找到该课堂的学员
		map.clear();
		map.put("ctId", ctId);
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(map);
		int totalUserNum = (classroomTraineeList == null || classroomTraineeList.size() == 0) ? 0 : classroomTraineeList.size();
		// 查出签到活动
		map.clear();
		map.put("ctId", ctId);
		List<TevglActivitySigninTrainee> tevglActivitySigninTrainees = tevglActivitySigninTraineeMapper.selectListByMap(map);
		dayList.stream().forEach(day -> {
			/*int num = (int) (Math.random() * 100);
			data.add(new BigDecimal(num));*/
			// 取出当天最新的一条
			List<TevglPkgActivityRelation> collect = tevglPkgActivityRelations.stream().filter(a -> a.getActivityBeginTime().indexOf(day) != -1).collect(Collectors.toList());
			if (collect != null && collect.size() > 0) {
				TevglPkgActivityRelation tevglPkgActivityRelation = collect.get(0);
				// 筛选出，在此之前就已经加入进来的学员
				//long totalUserNum = classroomTraineeList.stream().filter(a -> new Date(a.getJoinDate()).before(new Date(day))).count();
				// 找到某次活动正常签到的人数
				long count = tevglActivitySigninTrainees.stream()
						.filter(a -> a.getActivityId().equals(tevglPkgActivityRelation.getActivityId()) && "1".equals(a.getSignState()))
						.map(a -> a.getTraineeId())
						.distinct()
						.count();
				// 计算
				String val = new BigDecimal(count).divide(new BigDecimal(totalUserNum), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
				data.add(new BigDecimal(val));
			} else {
				data.add(new BigDecimal("0"));
			}
		});
		m.put("data", data);
		m.put("totalValue", data.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		dataList.add(m);
		return dataList;
	}
	
	/**
	 * 获取日期
	 * @return ["2021-12-17", "2021-12-18", "2021-12-20"]
	 */
	private List<String> getBrokenLineInfoxAxisArr(){
		List<String> dayList = com.budaos.modules.common.DateUtils.getBeforeDays(14);
		return dayList;
	}
	
}
