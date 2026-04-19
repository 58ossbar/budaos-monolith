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
import com.budaos.modules.evgl.activity.api.TevglActivityAnswerDiscussService;
import com.budaos.modules.evgl.activity.domain.TevglActivityAnswerDiscuss;
import com.budaos.modules.evgl.activity.persistence.TevglActivityAnswerDiscussMapper;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeLike;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeLikeMapper;
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
import com.budaos.modules.im.domain.*;
import com.budaos.modules.im.persistence.TimChatListMapper;
import com.budaos.modules.im.persistence.TimGroupStateMapper;
import com.budaos.modules.im.persistence.TimGroupUserMapper;
import com.budaos.modules.im.service.TimChatListService;
import com.budaos.modules.im.service.TimGroupMsgService;
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
import org.springframework.stereotype.Service;
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class TevglActivityAnswerDiscussServiceImpl implements TevglActivityAnswerDiscussService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityAnswerDiscussServiceImpl.class);
	@Autowired
	private TevglActivityAnswerDiscussMapper tevglActivityAnswerDiscussMapper;
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
	private TimGroupService timGroupService;
	@Autowired
	private TimGroupUserService timGroupUserService;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TimChatListService timChatListService;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private TmeduMeLikeMapper tmeduMeLikeMapper;
	@Autowired
	private TimGroupMsgService timGroupMsgService;
	@Autowired
	private TimGroupUserMapper timGroupUserMapper;
	@Autowired
	private TimChatListMapper timChatListMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private CbNumsUtils cbNumsUtils;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TimGroupStateMapper timGroupStateMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	
	
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityAnswerDiscuss> tevglActivityAnswerDiscussList = tevglActivityAnswerDiscussMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityAnswerDiscussList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityAnswerDiscussList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityAnswerDiscussList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params

	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityAnswerDiscussList = tevglActivityAnswerDiscussMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityAnswerDiscussList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityAnswerDiscussList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityAnswerDiscuss
	 * @throws BudaosException
	 */
	public R save(TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss) throws BudaosException {
		tevglActivityAnswerDiscuss.setActivityId(Identities.uuid());
		tevglActivityAnswerDiscuss.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityAnswerDiscuss.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityAnswerDiscuss);
		tevglActivityAnswerDiscussMapper.insert(tevglActivityAnswerDiscuss);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityAnswerDiscuss
	 * @throws BudaosException
	 */
	public R update(TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss) throws BudaosException {
	    tevglActivityAnswerDiscuss.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityAnswerDiscuss.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityAnswerDiscuss);
		tevglActivityAnswerDiscussMapper.update(tevglActivityAnswerDiscuss);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityAnswerDiscussMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityAnswerDiscussMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityAnswerDiscussMapper.selectObjectById(id));
	}
	
	/**
	 * 保存答疑讨论信息
	 * @param tevglActivityAnswerDiscuss
	 * @param loginUserId
	 * @return
	 * @throws BudaosException
	 */
	@Override
	public R saveAnswerDiscussInfo(TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss, String loginUserId) throws BudaosException {
		String pkgId = tevglActivityAnswerDiscuss.getPkgId();
		String chapterId = tevglActivityAnswerDiscuss.getChapterId();
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
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
		pkgUtils.createDefaultActivityTab(refPkgId, chapterId, loginUserId);
		// 填充答疑讨论部分信息
		tevglActivityAnswerDiscuss.setActivityId(Identities.uuid());
		tevglActivityAnswerDiscuss.setActivityState("0"); // 0未开始1进行中2已结束
		tevglActivityAnswerDiscuss.setState("Y"); // Y有效N无效
		tevglActivityAnswerDiscuss.setActivityType(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		tevglActivityAnswerDiscuss.setCreateTime(DateUtils.getNowTimeStamp());
		tevglActivityAnswerDiscuss.setCreateUserId(loginUserId);
		tevglActivityAnswerDiscuss.setAnswerNumber(0);
		tevglActivityAnswerDiscuss.setActivityPic("iconfont_svg icondayitaolun");
		tevglActivityAnswerDiscuss.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_3_ANSWER_DISCUSS);
		tevglActivityAnswerDiscuss.setIsAllowPic(StrUtils.isEmpty(tevglActivityAnswerDiscuss.getIsAllowPic()) ? "N" : tevglActivityAnswerDiscuss.getIsAllowPic());
		tevglActivityAnswerDiscuss.setIsAllowVoice(StrUtils.isEmpty(tevglActivityAnswerDiscuss.getIsAllowVoice()) ? "N" : tevglActivityAnswerDiscuss.getIsAllowVoice());
		tevglActivityAnswerDiscuss.setIsAllowVideo(StrUtils.isEmpty(tevglActivityAnswerDiscuss.getIsAllowVideo()) ? "N" : tevglActivityAnswerDiscuss.getIsAllowVideo());
		// 创建答疑讨论活动时固定分组
		tevglActivityAnswerDiscuss.setResgroupId(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		Map<String, Object> ps = new HashMap<>();
		ps.put("pkgId", pkgId);
		ps.put("resgroupId", tevglActivityAnswerDiscuss.getResgroupId());
		Integer sortNum = tevglActivityAnswerDiscussMapper.getMaxSortNum(ps);
		tevglActivityAnswerDiscuss.setSortNum(sortNum);
		// 验证
		ValidatorUtils.check(tevglActivityAnswerDiscuss);
		// 保存至数据库
		tevglActivityAnswerDiscussMapper.insert(tevglActivityAnswerDiscuss);
		// 保存答疑讨论与教学包的关系
		pkgUtils.buildRelation(pkgId, tevglActivityAnswerDiscuss.getActivityId(), GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		// 更新数量
		pkgUtils.plusPkgActivityNum(pkgId);
		return R.ok("[答疑/讨论] 创建成功");
	}

	/**
	 * 修改答疑讨论
	 * @param tevglActivityAnswerDiscuss
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R updateAnswerDiscussInfo(TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss
			, String loginUserId) throws BudaosException {
		String activityId = tevglActivityAnswerDiscuss.getActivityId();
		String pkgId = tevglActivityAnswerDiscuss.getPkgId();
		String chapterId = tevglActivityAnswerDiscuss.getChapterId();
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglActivityAnswerDiscuss activityInfo = tevglActivityAnswerDiscussMapper.selectObjectById(activityId);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(activityInfo.getActivityState())) {
			return R.error("活动已开始，无法修改");
		}
		if ("2".equals(activityInfo.getActivityState())) {
			return R.error("活动已结束，无法修改");
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
		pkgUtils.createDefaultActivityTab(refPkgId, chapterId, loginUserId);
		// 填充信息
		tevglActivityAnswerDiscuss.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglActivityAnswerDiscuss.setUpdateUserId(loginUserId);
		tevglActivityAnswerDiscuss.setState("Y");
		tevglActivityAnswerDiscuss.setActivityType(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		tevglActivityAnswerDiscuss.setResgroupId(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		// 验证
		ValidatorUtils.check(tevglActivityAnswerDiscuss);
		tevglActivityAnswerDiscussMapper.update(tevglActivityAnswerDiscuss);
		return R.ok("[答疑/讨论] 修改成功");
	}

	/**
	 * 查看答疑讨论
	 * @param activityId（注意，这个里值，具体可能会是t_evgl_pkg_activity_relation表的activity_id或group_id的值）
	 * @return
	 */
	@Override
	public R viewAnswerDiscussInfo(String activityId) {
		if (StrUtils.isEmpty(activityId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> answerDiscussInfo = tevglActivityAnswerDiscussMapper.selectObjectMapById(activityId);
		if (answerDiscussInfo == null) {
			TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss = tevglActivityAnswerDiscussMapper.selectObjectByGroupId(activityId);
			if (tevglActivityAnswerDiscuss != null) {
				answerDiscussInfo = new HashMap<>();
				answerDiscussInfo.put("activityState", tevglActivityAnswerDiscuss.getActivityStateActual());
				answerDiscussInfo.put("activityId", tevglActivityAnswerDiscuss.getActivityId());
				answerDiscussInfo.put("groupId", tevglActivityAnswerDiscuss.getGroupId());
				answerDiscussInfo.put("activityTitle", tevglActivityAnswerDiscuss.getActivityTitle());
				answerDiscussInfo.put("content", tevglActivityAnswerDiscuss.getContent());
				answerDiscussInfo.put("isAllowPic", tevglActivityAnswerDiscuss.getIsAllowPic());
				answerDiscussInfo.put("isAllowVoice", tevglActivityAnswerDiscuss.getIsAllowVoice());
				answerDiscussInfo.put("isAllowVideo", tevglActivityAnswerDiscuss.getIsAllowVideo());
				answerDiscussInfo.put("purpose", tevglActivityAnswerDiscuss.getPurpose());
				answerDiscussInfo.put("chapterId", tevglActivityAnswerDiscuss.getChapterId());
			}
		}
		return R.ok().put(Constant.R_DATA, answerDiscussInfo);
	}
	
	/**
	 * 聊天内容
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewActInfo(Map<String, Object> params, String loginUserId) {
		Object pkgId = params.get("pkgId");
		Object groupId = params.get("activityId");
		if (StrUtils.isNull(groupId)) {
			return R.error("必传参数为空");
		}
		// 如果没有加入群组,则先加入群组
		List<TimGroupUser> timGroupUserList = timGroupUserService.selectListByGroupId(groupId);
		if (!timGroupUserList.stream().anyMatch(a -> a.getUserId().equals(loginUserId))) {
			TimGroupUser timGroupUser = new TimGroupUser();
			timGroupUser.setGroupuserId(Identities.uuid());
			timGroupUser.setUserId(loginUserId);
			timGroupUser.setGroupId(groupId.toString());
			timGroupUser.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
			timGroupUserService.save(timGroupUser);
			// 并更新群组人数
			TimGroup timGroup = new TimGroup();
			timGroup.setGroupId(groupId.toString());
			timGroup.setGroupNum(1);
			timGroupService.plusNum(timGroup);
			// 更新聊天列表记录
			timChatListService.save(null, loginUserId, groupId.toString(), "2", 0, "", true);
		}
		
		// 答疑/讨论
		//TevglActivityAnswerDiscuss answerDiscuss = tevglActivityAnswerDiscussMapper.selectObjectById2(groupId);
		TevglActivityAnswerDiscuss answerDiscuss = null;
		if (StrUtils.isNull(pkgId)) {
			answerDiscuss = tevglActivityAnswerDiscussMapper.selectObjectById(groupId);
		} else {
			answerDiscuss = tevglActivityAnswerDiscussMapper.selectObjectByIdAndPkgId(params);
		}
		Map<String, Object> info = new HashMap<String, Object>();
		if (answerDiscuss != null) {
			info.put("isAllowPic", answerDiscuss.getIsAllowPic()); // 是否允许图片信息(Y/N)
			info.put("isAllowVoice", answerDiscuss.getIsAllowVoice()); // 是否允许语音信息(Y/N)(仅支持移动设备)
			info.put("isAllowVideo", answerDiscuss.getIsAllowVideo()); // 是否允许视频消息
			info.put("activityState", answerDiscuss.getActivityStateActual()); // 实际的活动状态
			info.put("activityTitle", answerDiscuss.getActivityTitle());
			info.put("activityPic", answerDiscuss.getActivityPic());
			info.put("content", answerDiscuss.getContent());
			info.put("empiricalValue", answerDiscuss.getEmpiricalValue());
		}
		return R.ok().put(Constant.R_DATA, info);
	}

	/**
	 * 删除答疑讨论
	 * @param activityId 活动id
	 * @param pkgId 所属教学包
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R deleteAnswerDiscussInfo(String activityId, String pkgId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TevglActivityAnswerDiscuss answerDiscussInfo = tevglActivityAnswerDiscussMapper.selectObjectByIdAndPkgId(params);
		if (answerDiscussInfo == null) {
			return R.error("无效的记录");
		}
		// 权限校验
		if (!pkgPermissionUtils.hasPermissionDeleteActivity(pkgId, answerDiscussInfo.getCreateUserId(), loginUserId, answerDiscussInfo.getChapterId())) {
			return R.error("暂无权限，操作失败");
		}
		// 如果活动已经开始或结束,控制不能删除
		if (!"0".equals(answerDiscussInfo.getActivityStateActual())) {
			return R.error("当前活动已被使用,无法删除");
		}
		// 删除活动与教学包的关系
		tevglPkgActivityRelationMapper.deleteByActivityId(activityId);
		// 删除活动
		tevglActivityAnswerDiscussMapper.delete(activityId);
		// 教学包活动数量-1
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("删除成功");
	}

	/**
	 * 开始答疑讨论
	 * @param ctId 必传参数，课堂主键
	 * @param activityId 必传参数，活动主键
	 * @param pkgId 活动对应的教学包id
	 * @param loginUserId 必传参数，当前登录用户
	 * @return
	 */
	@Override
	public R startAnswerDiscussInfo(String ctId, String activityId, String pkgId, String loginUserId, String activityEndTime) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) 
				|| StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(ctId)) {
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
		//params.put("pkgId", classroom.getPkgId()); // 不要用这个值
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TevglActivityAnswerDiscuss activityInfo = tevglActivityAnswerDiscussMapper.selectObjectByIdAndPkgId(params);
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
		ps.put("pkgId", pkgId);
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
		data.put("activityType", GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		data.put("content", activityInfo.getContent());
		// ================================================== 即时通讯相关处理 begin ==================================================
		 
		// 第一步、把答疑讨论当成一个群组即可,所以需要创建一个群组
		//String groupId = createTimGroup(activityId, loginUserId, activityInfo);
		String groupId = createTimGroup(activityInfo.getGroupId(), loginUserId, activityInfo, pkgId);
		// 第二步、活动创建者（也是课堂创建者）成为[群主]
		createTimGroupUserAdmin(groupId, loginUserId);
		// 第三步、更新群主自己的聊天列表记录
		timChatListService.save(null, loginUserId, groupId, "2", 0, "", true, "1", groupId);
		// 第四步、先找到本课堂所有有效成员
		List<TevglTchClassroomTrainee> classroomTraineeList = getClassroomTraineeList(ctId);
		log.debug("找到本课堂所有有效成员:" + classroomTraineeList.size());
		// 第五步、所有有效课堂成员成为此活动群的群成员(注：记得更新群聊列表记录)
		createTimGroupUser(groupId, classroomTraineeList);
		// 组装返回前端的数据
		String tips = "发起了活动【" + activityInfo.getActivityTitle() + "】";
		JSONObject msg = new JSONObject();
		// 为活动类型消息时弹出弹窗
		msg.put("busitype", MsgType.ACTIVITY);
		// 3标识答疑讨论活动
		msg.put("activity_type", GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS); 
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("send_id", loginUserId);
		busiMsg.put("send_name", null);
		busiMsg.put("tips", tips);
		busiMsg.put("activity_id", activityInfo.getActivityId());
		busiMsg.put("activity_type", GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
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
		// 找到本群所有人
		List<TimGroupUser> timGroupUserList = timGroupUserService.selectListByGroupId(groupId);
		for (TimGroupUser timGroupUser : timGroupUserList) {
			// 绑定用户与群
			Tio.bindGroup(tioConfig, timGroupUser.getUserId(), groupId);
			// 非活动创建者时
			if (!loginUserId.equals(timGroupUser.getUserId())) {
				CBImUtils.sendToUser(tioConfig, timGroupUser.getUserId(), msg);	
			}
		}
		// ================================================== 即时通讯相关处理 end ==================================================
		return R.ok("活动时间设置成功").put(Constant.R_DATA, data);
	}
	
	/**
	 * 第一步、创建一个群组
	 * @param activityId
	 * @return 返回群组主键
	 */
	private String createTimGroup(String activityId, String loginUserId, TevglActivityAnswerDiscuss activityInfo, String ctPkgId) {
		String groupId = null;
		TimGroup timGroup = new TimGroup();
		// 已活动主键作为群聊主键
		timGroup.setGroupId(activityId); // 组ID
		timGroup.setCreateUser(loginUserId); // 创建者
		timGroup.setState("Y");
		timGroup.setPkgId(ctPkgId);
		if (activityInfo != null) {
			timGroup.setGroupTitle(activityInfo.getActivityTitle()); // 群组名称
			timGroup.setGroupNickname(activityInfo.getActivityTitle()); // 群组昵称
			timGroup.setGroupImg(activityInfo.getActivityPic());	
		}
		R r = timGroupService.save(timGroup);
		if (r.get(Constant.R_DATA) != null) {
			groupId = r.get(Constant.R_DATA).toString();
		}
		return groupId;
	}

	/**
	 * 第二步、将自己加入群主并成为群主
	 * @param groupId
	 * @param loginUserId
	 */
	private void createTimGroupUserAdmin(String groupId, String loginUserId) {
		List<TimGroupUser> list = timGroupUserService.selectListByGroupId(groupId);
		// 如果有记录
		if (list.stream().anyMatch(a -> a.getUserId().equals(loginUserId))) {
			List<TimGroupUser> userList = list.stream().filter(user -> user.getUserId().equals(loginUserId)).collect(Collectors.toList());
			// 则更新为群主
			if (userList != null && userList.size() > 0) {
				log.debug("更新为群主");
				// 不是群主时才更新为群主
				TimGroupUser timGroupUser = userList.get(0);
				if (!"2".equals(timGroupUser.getGroupuserAdmin())) {
					timGroupUser.setGroupuserAdmin("2"); // 是否群主(1.非群主;2.群主)
					timGroupUserService.update(timGroupUser);
				}
			}
		} else { // 否在需要生成一条记录
			log.debug("创建为群主");
			TimGroupUser timGroupUser = new TimGroupUser();
			timGroupUser.setGroupuserId(Identities.uuid());
			timGroupUser.setUserId(loginUserId);
			timGroupUser.setGroupId(groupId);
			timGroupUser.setGroupuserAdmin("2"); // // 是否群主(1.非群主;2.群主)
			timGroupUserService.save(timGroupUser);
		}
	}
	
	/**
	 * 第四步、获取课堂所有有效成员
	 * @param ctId
	 * @return
	 */
	private List<TevglTchClassroomTrainee> getClassroomTraineeList(String ctId) {
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("state", "Y");
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		return classroomTraineeList;
	}

	/**
	 * 第五步、将课堂成员加进群组,成为群组用户
	 * @param groupId
	 * @param classroomTraineeList
	 * @return
	 */
	private List<TimGroupUser> createTimGroupUser(String groupId, List<TevglTchClassroomTrainee> classroomTraineeList) {
		if (StrUtils.isEmpty(groupId) || classroomTraineeList == null || classroomTraineeList.size() == 0) {
			return null;
		}
		List<TimGroupUser> list = new ArrayList<TimGroupUser>();
		for (TevglTchClassroomTrainee classroomTrainee : classroomTraineeList) {
			TimGroupUser timGroupUser = new TimGroupUser();
			String traineeId = classroomTrainee.getTraineeId();
			timGroupUser.setGroupuserId(Identities.uuid());
			timGroupUser.setUserId(traineeId);
			timGroupUser.setGroupId(groupId);
			timGroupUser.setGroupuserAdmin("1"); // 是否群主(1.非群主;2.群主)
			list.add(timGroupUser);
			//timGroupUserService.save(timGroupUser);
			// 更新成员的聊天列表
			log.debug("更新成员["+timGroupUser.getUserId()+"]的聊天列表");
			timChatListService.save(null, timGroupUser.getUserId(), groupId, "2", 0, "", true, "1", groupId);
		}
		timGroupUserService.insertBatch(list);
		// 更新群组人数
		TimGroup timGroup = new TimGroup();
		timGroup.setGroupId(groupId);
		timGroup.setGroupNum(classroomTraineeList.size());
		timGroupService.plusNum(timGroup);
		return list;
	}
	

	/**
	 * 结束活动
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R endAnswerDiscussInfo(String ctId, String activityId, String loginUserId) {
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
		TevglActivityAnswerDiscuss activityInfo = tevglActivityAnswerDiscussMapper.selectObjectById(activityId);
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
		// ============================== 即时通讯相关处理 begin ==============================
		// 结束答疑讨论之后，将答疑讨论活动群状态设为无效
		activityId = list.get(0).getGroupId();
		TimGroup timGroup = new TimGroup();
		timGroup.setGroupId(activityId);
		timGroup.setState("N");
		timGroupService.update(timGroup);
		// 找到本群的聊天列表数据
		Map<String, Object> params = new HashMap<>();
		params.put("friendType", "2");
		params.put("friendId", activityId);
		List<TimChatList> timChatList = timChatListMapper.selectListByMap(params);
		// 将聊天列表数据状态设为无效
		for (TimChatList timChat : timChatList) {
			timChat.setState("N");
			timChatListMapper.update(timChat);
		}
		// 结束活动更新未读消息为已读
		params.clear();
		params.put("groupId", activityId);
		List<TimGroupUser> timGroupUserList = timGroupUserMapper.selectListByMap(params);
		if (timGroupUserList != null && timGroupUserList.size() > 0) {
			List<String> groupuserIds = timGroupUserList.stream().map(a -> a.getGroupuserId()).collect(Collectors.toList());
			params.put("groupuserIds", groupuserIds);
			params.put("readState", "1");
			List<TimGroupState> groupStateList = timGroupStateMapper.selectListByMap(params);
			if (groupStateList.size() > 0) {
				List<String> stateIds = groupStateList.stream().map(a -> a.getStateId()).collect(Collectors.toList());
				Map<String, Object> act = new HashMap<>();
				act.put("readState", "2");
				act.put("readTime", DateUtils.getNowTimeStamp());
				act.put("stateIds", stateIds);
				timGroupStateMapper.updateBatch(act);
			}
		}
		// ============================== 即时通讯相关处理 end ==============================
		// 返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		return R.ok("成功结束活动").put(Constant.R_DATA, data);
	}

	/**
	 * 复制一个新的活动
	 * @param targetActivityId 目标活动
	 * @param newPkgId 新教学包id
	 * @param loginUserId
	 * @param chapterId
	 */
	@Override
	public void copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum) {
		if (StrUtils.isEmpty(targetActivityId) || StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(loginUserId)) {
			log.debug("必传参数为空");
			return;
		}
		// 找到目标活动
		TevglActivityAnswerDiscuss activityInfo = tevglActivityAnswerDiscussMapper.selectObjectById(targetActivityId);
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
		TevglActivityAnswerDiscuss t = new TevglActivityAnswerDiscuss();
		t = activityInfo;
		t.setActivityId(Identities.uuid());
		t.setChapterId(StrUtils.isEmpty(chapterId) ? null : chapterId);
		t.setActivityTitle(activityInfo.getActivityTitle());
		t.setCreateUserId(loginUserId);
		t.setSortNum(sortNum);
		t.setActivityBeginTime(null);
		t.setActivityEndTime(null);
		t.setState("Y");
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setResgroupId(resgroupId);
		t.setActivityState("0"); // 状态统一改为未开始
		// 保存活动至数据库
		tevglActivityAnswerDiscussMapper.insert(t);
		String newActivityId = t.getActivityId();
		// 保存活动与教学包的关系
		pkgActivityUtils.buildRelation(newPkgId, newActivityId, GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(newPkgId);
	}


	/**
	 * 点赞
	 * @param msgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R clickLike(String msgId, String loginUserId) {
		if (StrUtils.isEmpty(msgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TimGroupMsg timGroupMsg = timGroupMsgService.selectObjectById(msgId);
		if (timGroupMsg == null) {
			return R.ok();
		}
		List<TmeduMeLike> list = getMeLikeListData(msgId, loginUserId);
		if (list.size() > 0) {
			return R.error(GlobalLike.ALREADY_LIKE_MSG);
		}
		TmeduMeLike t = new TmeduMeLike();
		t.setLikeId(Identities.uuid());
		t.setLikeType(GlobalLike.LIKE_15_ACTIVITY_ANSWER_DISCUSS_TRAINEE_ANSWER);
		t.setLikeTime(DateUtils.getNowTimeStamp());
		t.setTraineeId(loginUserId);
		t.setTargetId(msgId);
		t.setReadState("N");
		TimGroupUser timGroupUser = timGroupUserMapper.selectObjectById(timGroupMsg.getGroupuserId());
		if (timGroupUser != null) {
			t.setTargetTraineeId(timGroupUser.getUserId());	
		}
		tmeduMeLikeMapper.insert(t);
		// 即时通讯,通知被点赞的人
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		TimGroupUser groupUser = timGroupUserMapper.selectObjectById(timGroupMsg.getGroupuserId());
		if (groupUser != null) {
			JSONObject msg = cbNumsUtils.getNums(groupUser.getUserId());
			CBImUtils.sendToUser(tioConfig, groupUser.getUserId(), msg);	
		}
		return R.ok("点赞成功");
	}

	/**
	 * 取消点赞
	 * @param msgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R cancelLike(String msgId, String loginUserId) {
		if (StrUtils.isEmpty(msgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TimGroupMsg timGroupMsg = timGroupMsgService.selectObjectById(msgId);
		if (timGroupMsg == null) {
			return R.ok();
		}
		List<TmeduMeLike> list = getMeLikeListData(msgId, loginUserId);
		if (list.size() == 0) {
			return R.error(GlobalLike.NO_LIKE_MSG);
		}
		for (TmeduMeLike tmeduMeLike : list) {
			tmeduMeLikeMapper.delete(tmeduMeLike.getLikeId());
		}
		return R.ok("取消点赞");
	}
	

	/**
	 * 获取当前登录用户是否对此作答点赞了
	 * @param anId
	 * @param loginUserId
	 * @return
	 */
	private List<TmeduMeLike> getMeLikeListData(String anId, String loginUserId){
		Map<String, Object> params = new HashMap<>();
		params.put("targetId", anId);
		params.put("likeType", GlobalLike.LIKE_15_ACTIVITY_ANSWER_DISCUSS_TRAINEE_ANSWER);
		params.put("traineeId", loginUserId);
		return tmeduMeLikeMapper.selectListByMap(params);
	}

	@Override
	public TevglActivityAnswerDiscuss selectObjectByGroupId(Object groupId) {
		return tevglActivityAnswerDiscussMapper.selectObjectByGroupId(groupId);
	}

}
