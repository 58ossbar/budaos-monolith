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
import com.budaos.modules.common.enums.CommonEnum;
import com.budaos.modules.evgl.activity.api.TevglActivityTaskService;
import com.budaos.modules.evgl.activity.domain.TevglActivityTask;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskFile;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskGroup;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskGroupMember;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskFileMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskGroupMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskGroupMemberMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskMapper;
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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * Title:
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
public class TevglActivityTaskServiceImpl implements TevglActivityTaskService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityTaskServiceImpl.class);
	
		
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private PkgActivityUtils pkgActivityUtils;
	@Autowired
	private CbRoomUtils cbRoomUtils;

	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;

	@Autowired
	private TevglActivityTaskMapper tevglActivityTaskMapper;
	@Autowired
	private TevglActivityTaskFileMapper tevglActivityTaskFileMapper;
	@Autowired
	private TevglActivityTaskGroupMapper tevglActivityTaskGroupMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglActivityTaskGroupMemberMapper tevglActivityTaskGroupMemberMapper;

	/**
	 * 查询列表(返回List<Bean>)
	 * 
	 * @param Map
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TevglActivityTask> tevglActivityTaskList = tevglActivityTaskMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityTaskList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * 
	 * @param Map
	 * @return R
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tevglActivityTaskList = tevglActivityTaskMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 新增
	 * 
	 * @param tevglActivityTask
	 * @throws BudaosException
	 */
	public R save(TevglActivityTask tevglActivityTask) throws BudaosException {
		tevglActivityTask.setActivityId(Identities.uuid());
		tevglActivityTask.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityTask.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityTask);
		tevglActivityTaskMapper.insert(tevglActivityTask);
		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param tevglActivityTask
	 * @throws BudaosException
	 */
	public R update(TevglActivityTask tevglActivityTask) throws BudaosException {
		tevglActivityTask.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityTask.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityTask);
		tevglActivityTaskMapper.update(tevglActivityTask);
		return R.ok();
	}

	/**
	 * 单条删除
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityTaskMapper.delete(id);
		return R.ok();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityTaskMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityTaskMapper.selectObjectById(id));
	}

	/**
	 * 新增活动【作业/小组任务】
	 * 
	 * @param tevglActivityTask
	 * @param loginUserId
	 * @return
	 * @throws IOException 
	 */
	@Override
	public R saveTaskInfo(TevglActivityTask tevglActivityTask, String loginUserId) {
		String pkgId = tevglActivityTask.getPkgId();
		String chapterId = tevglActivityTask.getChapterId();
		String media_id = tevglActivityTask.getMedia_id();
		String anMedia_id = tevglActivityTask.getAnMedia_id();
		// String resgroupId = tevglActivityTask.getResgroupId(); // 所属资源分组
		String resgroupId = GlobalActivity.ACTIVITY_5_TASK_GROUP;
		String activityTitle = tevglActivityTask.getActivityTitle(); // 活动标题
		String content = tevglActivityTask.getContent(); // 任务详情(内容)
		String lastestSubmissionTime = tevglActivityTask.getLastestSubmissionTime(); // 最晚提交时间
		// 合法性校验
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		R r = checkIsPass(resgroupId, activityTitle, content, lastestSubmissionTime);
		if ((Integer) r.get("code") != 0) {
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
		tevglActivityTask.setActivityId(Identities.uuid());
		tevglActivityTask.setActivityType(GlobalActivity.ACTIVITY_5_TASK_GROUP);
		// 任务小组划分方式(1不划分2随机划分3线下划分4使用成员小组方案)
		String divideGroupType = StrUtils.isEmpty(tevglActivityTask.getDivideGroupType()) ? "1" : tevglActivityTask.getDivideGroupType();
		tevglActivityTask.setDivideGroupType(divideGroupType);
		// 评分方式
		String scoreType = StrUtils.isEmpty(tevglActivityTask.getScoreType()) ? "1" : tevglActivityTask.getScoreType();
		tevglActivityTask.setScoreType(scoreType);
		tevglActivityTask.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_5_TASK_GROUP);
		tevglActivityTask.setCreateTime(DateUtils.getNowTimeStamp());
		tevglActivityTask.setCreateUserId(loginUserId);
		tevglActivityTask.setActivityState("0"); // 0未开始1进行中2已结束
		tevglActivityTask.setState("Y"); // Y有效N无效
		// 排序号处理
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("pkgId", pkgId);
		ps.put("resgroupId", tevglActivityTask.getResgroupId());
		Integer sortNum = tevglActivityTaskMapper.getMaxSortNum(ps);
		tevglActivityTask.setSortNum(sortNum);
		// 入库
		tevglActivityTaskMapper.insert(tevglActivityTask);
		// 绑定附件关系
		handleTaskFile(tevglActivityTask.getActivityId(), media_id, loginUserId);
		// 绑定答案附件关系
		handleTaskFile(tevglActivityTask.getActivityId(), anMedia_id, loginUserId);
		// 保存教学包与活动之间的关系
		pkgUtils.buildRelation(pkgId, tevglActivityTask.getActivityId(), GlobalActivity.ACTIVITY_5_TASK_GROUP);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(pkgId);
		return R.ok("活动[作业/小组任务] 创建成功");
	}
	
	/**
	 * 绑定与附件的关系
	 * @param activityId
	 * @param media_id
	 * @param loginUserId
	 */
	private void handleTaskFile(String activityId, String media_id, String loginUserId) {
		if (StrUtils.isNotEmpty(media_id)) {
			List<TevglActivityTaskFile> updateFileList = new ArrayList<>();
			String[] split = media_id.split(",");
			for (String fileId : split) {
				TevglActivityTaskFile fileInfo = new TevglActivityTaskFile();
				fileInfo.setFileId(fileId);
				fileInfo.setActivityId(activityId);
				fileInfo.setCreateUserId(loginUserId); //创建人
				fileInfo.setCreateTime(DateUtils.getNowTimeStamp()); //创建时间
				updateFileList.add(fileInfo);
			}
			if (updateFileList.size() > 0) {
				tevglActivityTaskFileMapper.updateBatchByDuplicateKey(updateFileList);
			}
		}
	}

	/**
	 * 修改活动【作业/小组任务】
	 * 
	 * @author zyl加
	 * @data 2020年11月26日
	 * @param tevglActivityTask
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R updateTaskInfo(TevglActivityTask tevglActivityTask, String loginUserId) {
		// 必传参数
		String pkgId = tevglActivityTask.getPkgId(); // 教学包id
		String activityId = tevglActivityTask.getActivityId(); // 活动id

		// 合法性校验
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(activityId)) {
			return R.error("必传参数为空");
		}
		TevglActivityTask task = tevglActivityTaskMapper.selectObjectById(activityId);
		if (task == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if ("1".equals(task.getActivityState())) {
			return R.error("活动尚未开始，不能修改");
		}
		if ("2".equals(task.getActivityState())) {
			return R.error("活动已结束，不能修改");
		}
		// 修改内容
		String chapterId = tevglActivityTask.getChapterId(); // 章节id
		String media_id = tevglActivityTask.getMedia_id();
		String anMedia_id = tevglActivityTask.getAnMedia_id();
		String activityTitle = tevglActivityTask.getActivityTitle(); // 活动标题
		String resgroupId = GlobalActivity.ACTIVITY_5_TASK_GROUP; //tevglActivityTask.getResgroupId(); // 所属资源分组
		String content = tevglActivityTask.getContent(); // 任务详情(内容)
		String activityPic = tevglActivityTask.getActivityPic(); // 活动封面
		String divideGroupType = tevglActivityTask.getDivideGroupType(); // 任务小组划分方式(1不划分2随机划分3线下划分4使用成员小组方案)
		String scoreType = tevglActivityTask.getScoreType(); // 评分方式、作业分值及评分点(1老师评分2置顶助教/学生评分3学生互评4老师评分，组间评分，组内互评)
		String lastestSubmissionTime = tevglActivityTask.getLastestSubmissionTime(); // 最晚提交时间
		String isAllowExceedTime = tevglActivityTask.getIsAllowExceedTime(); // 是否允许超时提交作业(Y/N)
		String referenceAnswer = tevglActivityTask.getReferenceAnswer(); // 参考答案
		String activityBeginTime = tevglActivityTask.getActivityBeginTime(); // 活动开始时间
		String activityEndTime = tevglActivityTask.getActivityEndTime(); // 活动结束时间
		String state = tevglActivityTask.getState(); // 状态(Y有效N无效)
		String activityState = tevglActivityTask.getActivityState(); // 活动状态(0未开始1进行中2已结束)
		String purpose = tevglActivityTask.getPurpose(); // 用途(来源字典)

		// 校验
		R r = checkIsPass(resgroupId, activityTitle, content, lastestSubmissionTime);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 权限校验
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
		tevglActivityTask.setActivityId(activityId);
		tevglActivityTask.setResgroupId(resgroupId);
		tevglActivityTask.setActivityTitle(activityTitle);
		tevglActivityTask.setContent(content);
		tevglActivityTask.setActivityPic(activityPic);
		tevglActivityTask.setDivideGroupType(divideGroupType);
		tevglActivityTask.setScoreType(scoreType);
		tevglActivityTask.setIsSetLastestSubmissionTime(isAllowExceedTime);
		tevglActivityTask.setLastestSubmissionTime(lastestSubmissionTime);
		tevglActivityTask.setIsAllowExceedTime(isAllowExceedTime);
		tevglActivityTask.setReferenceAnswer(referenceAnswer);
		tevglActivityTask.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_5_TASK_GROUP);
		tevglActivityTask.setActivityBeginTime(activityBeginTime);
		tevglActivityTask.setActivityEndTime(activityEndTime);
		tevglActivityTask.setState(state);
		tevglActivityTask.setActivityState(activityState);
		tevglActivityTask.setUpdateUserId(loginUserId);
		tevglActivityTask.setUpdateUserId(DateUtils.getNowTimeStamp());
		tevglActivityTask.setActivityType(GlobalActivity.ACTIVITY_5_TASK_GROUP);
		tevglActivityTask.setChapterId(chapterId);
		tevglActivityTask.setPurpose(purpose);
		// 排序号处理
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		params.put("resgroupId", tevglActivityTask.getResgroupId());
		Integer sortNum = tevglActivityTaskMapper.getMaxSortNum(params);
		tevglActivityTask.setSortNum(sortNum);
		tevglActivityTaskMapper.update(tevglActivityTask);
		params.clear();
		params.put("activityId", activityId);
		// 截取上传文件附件的数组
		String[] split = null;
		// 截取上传答案附件的数组
		String[] anSplit = null;
		if (StrUtils.isNotEmpty(tevglActivityTask.getMedia_id())) {
			split = tevglActivityTask.getMedia_id().split(",");
		}
		if (StrUtils.isNotEmpty(tevglActivityTask.getAnMedia_id())) {
			anSplit = tevglActivityTask.getAnMedia_id().split(",");
		}
		// 通过活动id查找文件附件信息
		List<TevglActivityTaskFile> activityTaskFiles = tevglActivityTaskFileMapper.selectListByMap(params);
		if (activityTaskFiles != null && activityTaskFiles.size() > 0) {
			for (TevglActivityTaskFile tevglActivityTaskFile : activityTaskFiles) {
				// 定义一个布尔值类型的变量来判断这个值是否数据库中存在
				boolean temp = false;
				if ("1".equals(tevglActivityTaskFile.getFromType())) { //当文件的来源类型为1时执行下面的操作
					// 如果不存在则删除
					temp = handleBoolean(split, tevglActivityTaskFile.getFileId());
					if (!temp) {
						//获取本地上的文件附件的保存地址--本地测
						String fileSavePath = tevglActivityTaskFile.getFileSavePath();
						File file = new File(fileSavePath);
						//获取磁盘上的文件附件的显示地址--测试环境测
						//String fileAccessUrl = tevglActivityTaskFile.getFileAccessUrl();
						//File file = new File(fileAccessUrl);
						//删除磁盘位置的文件附件
						file.delete();
						//删除数据库中的记录
						tevglActivityTaskFileMapper.delete(tevglActivityTaskFile);
					}
				} else {
					// 如果不存在则删除
					temp = handleBoolean(anSplit, tevglActivityTaskFile.getFileId());
					if (!temp) {
						// 获取本地上的答案附件的保存地址--本地测
						String fileSavePath = tevglActivityTaskFile.getFileSavePath();
						File file = new File(fileSavePath);
						if (file.exists() && !file.isDirectory()) {
							// 获取磁盘上的答案附件的显示地址--测试环境测
							//String fileAccessUrl = tevglActivityTaskFile.getFileAccessUrl();
							//File file = new File(fileAccessUrl);
							// 删除磁盘位置的答案附件
							file.delete();
							// 删除数据库中的记录
							tevglActivityTaskFileMapper.delete(tevglActivityTaskFile);
						}
					}
				}
			}
		}
		// 绑定附件相关
		handleTaskFile(activityId, media_id, loginUserId);
		// 答案附件
		handleTaskFile(activityId, anMedia_id, loginUserId);
		return R.ok("活动[作业/小组任务] 修改成功");
	}
	
	private boolean handleBoolean(String[] split, String fileId) {
		boolean temp = false;
		if (split != null) {
			for (int i = 0; i < split.length; i++) {
				//如果要上传的文件已经存于数据库中则不删除
				if (split[i].equals(fileId)) {
					//从一个数组中截取一定长度的元素放到新数组中
					System.arraycopy(split, i, split, 0, split.length - i);
					split = Arrays.copyOfRange(split, 0, split.length);
					//true表示存在，false表示不存在
					temp = true;
					break;
				}
			}
		}
		return temp;
	}

	/**
	 *
	 * 合理的校验
	 * @param resgroupId
	 * @param activityTitle
	 * @param content
	 * @param lastestSubmissionTime
	 * @return
	 */
	private R checkIsPass(String resgroupId, String activityTitle, String content, String lastestSubmissionTime) {
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
			return R.error("活动介绍不能为空");
		}
		content = content.trim();
		if (content.length() > 3000) {
			return R.error("活动介绍不能超过3000个字符");
		}
		// 如果设置了最晚提交时间yyyy-MM-dd
		if ("Y".equals(lastestSubmissionTime)) {
			if (StrUtils.isEmpty(lastestSubmissionTime)) {
				return R.error("请设置最晚提交时间");
			}
		}
		return R.ok();
	}

	/**
	 * 删除活动[作业/小组任务]
	 * 
	 * @author zyl加
	 * @data 2020年11月26日
	 * @param activityId  活动id
	 * @param loginUserId 登录用户id
	 * @param pkgId       教学包id
	 * @return
	 */
	@Override
	public R deleteTaskInfo(String activityId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", activityId);
		params.put("pkgId", pkgId);
		TevglActivityTask activityTask = tevglActivityTaskMapper.selectObjectByIdAndPkgId(params);
		if (activityTask == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		// 如果活动已经开始或结束,控制不能删除
		if (!"0".equals(activityTask.getActivityStateActual())) {
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
			if (!pkgPermissionUtils.hasPermissionDeleteActivity(pkgId, activityTask.getCreateUserId(), loginUserId, activityTask.getChapterId())) {
				return R.error("暂无权限，操作失败");
			}
		}
		// 删除与教学包的关系
		tevglPkgActivityRelationMapper.delete(activityId);
		// 删除试卷题目信息
		params.clear();
		params.put("activityId", activityId);
		// 通过活动id找到新增[作业/小组任务]时上传的文件集合
		List<TevglActivityTaskFile> taskFiles = tevglActivityTaskFileMapper.selectListByMap(params);
		if (taskFiles != null && taskFiles.size() > 0) {
			for (TevglActivityTaskFile tevglActivityTaskFile : taskFiles) {
				// 有外键约束，必须先删除外键所在的表
				tevglActivityTaskFileMapper.delete(tevglActivityTaskFile.getFileId());
				//获取本地上的文件附件的保存地址--本地测
				String fileSavePath = tevglActivityTaskFile.getFileSavePath();
				File file = new File(fileSavePath);
				//获取磁盘上的文件附件的显示地址--测试环境测
				/*String fileAccessUrl = tevglActivityTaskFile.getFileAccessUrl();
				File file = new File(fileAccessUrl);*/
				//删除存在磁盘上的文件及答案附件
				if (file.exists() && !file.isDirectory()) {
					file.delete();
				}
			}
		}
		// 批量删除数据库记录

		// 删除基本信息
		tevglActivityTaskMapper.delete(activityId);
		// 教学包活动数量-1
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("[作业/小组任务]活动删除成功");
	}

	/**
	 * 开启活动
	 * 
	 * @param ctId        课堂主键
	 * @param activityId  活动id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R startActivityTask(String ctId, String activityId, String loginUserId, String activityEndTime) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(ctId)) {
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
		TevglActivityTask activityInfo = tevglActivityTaskMapper.selectObjectByIdAndPkgId(params);
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
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(classroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_START);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 调整为修改关联表的状态
		relation.setActivityState("1");
		relation.setActivityBeginTime(DateUtils.getNowTimeStamp());
		relation.setActivityEndTime(StrUtils.isEmpty(activityEndTime) ? null : activityEndTime);
		tevglPkgActivityRelationMapper.update(relation);
		// TODO 其它业务处理
		// 小组划分方式1不划分小组、2随机划分小组、3线下划分小组、4使用成员小组方案
		String divideGroupType = activityInfo.getDivideGroupType();
		switch (divideGroupType) {
		case "1":
			doCaseAivideGroupTypeOne(ctId, activityId, params);
			break;
		case "2":
			doCaseAivideGroupTypeTwo(ctId, activityInfo);
			break;
		case "3":
			doCaseAivideGroupTypeThree(ctId, activityId, params);
			break;
		case "4":
			doCaseAivideGroupTypeFour(ctId, activityId, params);
			break;
		default:
			break;
		}
		// 返回数据
		Map<String, Object> data = new HashMap<>();
		data.put("empiricalValue", activityInfo.getEmpiricalValue());
		data.put("activityType", GlobalActivity.ACTIVITY_5_TASK_GROUP);
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		data.put("content", activityInfo.getContent());
		// ================================================== 即时通讯相关处理 begin
		// ==================================================
		// 找到本课堂所有有效成员
		params.clear();
		params.put("ctId", ctId);
		params.put("state", "Y");
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		// 组装数据
		String tips = "发起了活动【" + activityInfo.getActivityTitle() + "】";
		JSONObject msg = new JSONObject();
		msg.put("busitype", MsgType.ACTIVITY); // 为活动类型消息时弹出弹窗
		msg.put("activity_type", GlobalActivity.ACTIVITY_5_TASK_GROUP);
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("send_id", loginUserId);
		busiMsg.put("send_name", null);
		busiMsg.put("tips", tips);
		busiMsg.put("activity_id", activityInfo.getActivityId());
		busiMsg.put("activity_type", GlobalActivity.ACTIVITY_5_TASK_GROUP);
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
		// ================================================== 即时通讯相关处理 end
		// ==================================================
		return R.ok("活动时间设置成功").put(Constant.R_DATA, data);
	}

	/**
	 * 第一种情况不划分小组（直接创建一个课堂作业小组，将课堂成员，添加至此小组）
	 * 
	 * @param ctId       当前课堂id
	 * @param activityId 活动id
	 * @param params     查询条件
	 */
	private void doCaseAivideGroupTypeOne(String ctId, String activityId, Map<String, Object> params) {
		// 第一步找到此课堂成员
		params.clear();
		params.put("ctId", ctId);
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		if (classroomTraineeList == null || classroomTraineeList.size() == 0) {
			return;
		}
		// 第二步生成此活动的成员
		classroomTraineeList.stream().forEach(trainee -> {
			TevglActivityTaskGroupMember member = new TevglActivityTaskGroupMember();
			String traineeId = trainee.getTraineeId();
			member.setMemberId(traineeId);
			member.setActivityId(activityId);
			member.setGroupId(null);
			member.setTraineeId(traineeId);
			member.setContent(null);
			member.setFinalScore(new BigDecimal("0"));
			member.setCreateTime(DateUtils.getNowTimeStamp());
			member.setCreateUserId(traineeId);
			member.setState("Y");
			tevglActivityTaskGroupMemberMapper.insert(member);
		});
	}

	/**
	 * 第二种情况
	 * @param ctId
	 * @param activityInfo
	 */
	private void doCaseAivideGroupTypeTwo (String ctId, TevglActivityTask activityInfo) {
		String activityId = activityInfo.getActivityId();
		String time = DateUtils.getNowTimeStamp();
		// 分几个组
		int groups = 3;
		// 每组几个人
		//int groupMemberNum = 10;
		// 找到当前课堂的成员（已审核通过的）
		List<String> traineeIds = tevglTchClassroomTraineeMapper.findTraineeIdByCtIdAndState(ctId, CommonEnum.STATE_YES.getCode());
		// 数据源的list
		List<String> list = new ArrayList<String>();
		// 往数据源里面添加数据
		list.addAll(traineeIds);
		// 作为结果返回的list
		List<List<String>> groupsList = new ArrayList<List<String>>();
		// 随机打乱一下顺序
		Collections.shuffle(list);
		// 计算一下每组多少人
		int peoples = traineeIds.size() / groups;
		// 分组开始
		for (int i = 0; i < groups; i++) {
			List<String> group = new ArrayList<String>();
			for (int j = 0; j < peoples; j++) {
				int random = getRandom(list.size());
				group.add(list.get(random));
				list.remove(random);
			}
			groupsList.add(group);
		}
		// 最后剩下的人再重新分配一遍
		for (int i = 0; i < list.size(); i++) {
			groupsList.get(i).add(list.get(i));
		}
		if (groupsList.size() > 0) {
			// 等待新增的小组数据
			List<TevglActivityTaskGroup> insertGroupList = new ArrayList<>();
			// 等待新增的成员数据
			List<TevglActivityTaskGroupMember> insertMemberList = new ArrayList<>();
			for (int i = 0; i < groupsList.size(); i++) {
				List<String> traineeIdList = groupsList.get(i);
				String groupId = Identities.uuid();
				TevglActivityTaskGroup t = new TevglActivityTaskGroup();
				t.setGroupId(groupId);
				t.setCtId(ctId);
				t.setGroupName("第"+(i+1)+"组");
				t.setGroupNum(traineeIdList.size());
				t.setCreateTime(time);
				t.setState("Y");
				insertGroupList.add(t);
				if (traineeIdList.size() > 0) {
					traineeIdList.stream().forEach(traineeId -> {
						TevglActivityTaskGroupMember member = new TevglActivityTaskGroupMember();
						member.setMemberId(Identities.uuid());
						member.setCtId(ctId);
						member.setActivityId(activityId);
						member.setGroupId(t.getGroupId());
						member.setTraineeId(traineeId);
						member.setContent("");
						member.setFinalScore(new BigDecimal("0"));
						member.setCreateUserId(traineeId);
						member.setCreateTime(time);
						member.setState("Y");
						insertMemberList.add(member);
					});
				}
			}
			if (insertGroupList.size() > 0) {
				tevglActivityTaskGroupMapper.insertBatch(insertGroupList);
			}
			if (insertMemberList.size() > 0) {
				tevglActivityTaskGroupMemberMapper.insertBatch(insertMemberList);
			}
		}
	}

	/**
	 * 获取随机数
	 */
	public static int getRandom(int i) {
		Random r = new Random();
		return r.nextInt(i);
	}

	/**
	 * TODO 第三种情况
	 * 
	 * @param ctId
	 * @param activityId
	 * @param params
	 */
	private void doCaseAivideGroupTypeThree(String ctId, String activityId, Map<String, Object> params) {

	}

	/**
	 * TODO 第四种情况
	 * 
	 * @param ctId
	 * @param activityId
	 * @param params
	 */
	private void doCaseAivideGroupTypeFour(String ctId, String activityId, Map<String, Object> params) {

	}

	/**
	 * 结束活动
	 * 
	 * @author zyl加
	 * @data 2020年11月26日
	 * @param ctId        课堂id
	 * @param activityId  活动id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	@Override
	public R endActivityTask(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if ("1".equals(classroom.getClassroomState())) {
			return R.error("课堂尚未开始，结束失败");
		}
		if ("3".equals(classroom.getClassroomState())) {
			return R.error("课堂已结束，结束失败");
		}
		// 通过活动id查找任务信息
		TevglActivityTask activityInfo = tevglActivityTaskMapper.selectObjectById(activityId);
		if (activityInfo == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if ("0".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动未开始");
		}
		if ("2".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动已结束");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", classroom.getPkgId());
		params.put("activityId", activityId);
		List<TevglPkgActivityRelation> relations = tevglPkgActivityRelationMapper.selectListByMap(params);
		if (relations == null || relations.size() == 0) {
			return R.error("没有权限");
		}
		TevglPkgActivityRelation relation = relations.get(0);
		/*if (!pkgActivityUtils.hasStartActPermission(activityId, relation, loginUserId, activityInfo.getCreateUserId())) {
			return R.error("暂无权限，操作失败");
		}*/
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(classroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_END);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 调整为修改关联表的状态
		relation.setActivityState("2"); // 0未开始1进行中2已结束
		relation.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglPkgActivityRelationMapper.update(relation);
		// 返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		return R.ok("成功结束活动").put(Constant.R_DATA, data);
	}

	/**
	 * 查看活动
	 * 
	 * @param activityId
	 * @return
	 */
	@Override
	public R viewActivityTaskInfo(String activityId) {
		if (StrUtils.isEmpty(activityId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", activityId);
		List<TevglActivityTaskFile> taskFiles = tevglActivityTaskFileMapper.selectListByMap(params);

		List<TevglActivityTaskFile> fileList = new ArrayList<TevglActivityTaskFile>(); // 存储文件的集合
		List<TevglActivityTaskFile> answerList = new ArrayList<TevglActivityTaskFile>(); // 存储答案的集合

		for (TevglActivityTaskFile tevglActivityTaskFile : taskFiles) {
			if (tevglActivityTaskFile.getFromType().equals("1")) {
				fileList.add(tevglActivityTaskFile);
			} else {
				answerList.add(tevglActivityTaskFile);
			}
		}
		Map<String, Object> activityInfo = tevglActivityTaskMapper.selectObjectMapById(activityId);
		return R.ok().put(Constant.R_DATA, activityInfo).put("fileList", fileList).put("answerList", answerList);
	}

	/**
	 * 复制一个新的活动
	 * 
	 * @author zyl加
	 * @data 2020年11月27日
	 * @param targetActivityId 必传参数，目标活动
	 * @param newPkgId         必传参数，新教学包id
	 * @param loginUserId      必传参数，当前登录用户
	 * @param chapterId        非必传参数，所属章节
	 * @param sortNum          非必传参数排序号
	 */
	@Override
	public R copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum) {
		if (StrUtils.isEmpty(targetActivityId) || StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 找到目标活动
		TevglActivityTask activityTask = tevglActivityTaskMapper.selectObjectById(targetActivityId);
		if (activityTask == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		// 找到目标活动的活动分组(自定义活动分组时记得控制分组名称唯一)
		String resgroupId = "0";
		resgroupId = activityTask.getResgroupId();
		// 创建并填充一个新的活动
		TevglActivityTask task = new TevglActivityTask();
		task = activityTask;
		task.setActivityId(Identities.uuid()); // 活动id
		task.setActivityTitle(activityTask.getActivityTitle()); // 任务标题
		task.setContent(activityTask.getContent()); // 任务详情
		task.setScoreType(activityTask.getScoreType()); // 评分方式
		task.setChapterId(StrUtils.isEmpty(chapterId) ? activityTask.getChapterId() : chapterId); // 所属章节
		task.setDivideGroupType(activityTask.getDivideGroupType()); // 任务小组划分方式
		task.setResgroupId(resgroupId); // 分组
		task.setLastestSubmissionTime(activityTask.getLastestSubmissionTime()); // 最晚提交日期
		// 保存活动到数据库中
		tevglActivityTaskMapper.insert(task);
		// 获取新活动id
		String newActivityId = task.getActivityId();
		// 复制上传文件数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", targetActivityId);
		// 根据活动id查询上传的附件信息
		List<TevglActivityTaskFile> taskFiles = tevglActivityTaskFileMapper.selectListByMap(params);
		// 存储【任务详情处提交的附件】的集合
		List<TevglActivityTaskFile> fileList = new ArrayList<TevglActivityTaskFile>();
		// 存储【参考答案处提交的附件】的集合
		List<TevglActivityTaskFile> answerList = new ArrayList<TevglActivityTaskFile>();
		if (taskFiles != null && taskFiles.size() > 0) {
			TevglActivityTaskFile taskFile = new TevglActivityTaskFile();
			for (TevglActivityTaskFile tevglActivityTaskFile : taskFiles) {
				taskFile = tevglActivityTaskFile;
				taskFile.setActivityId(newActivityId);
				taskFile.setFileId(Identities.uuid());
				// 附件处理
				if (taskFile.getFromType().equals("1")) {
					fileList.add(taskFile);
				} else {
					answerList.add(taskFile);
				}
				// 保存附件到数据库中
				tevglActivityTaskFileMapper.insert(taskFile);
			}
		}
		// 保存活动与教学包的关系
		pkgActivityUtils.buildRelation(newPkgId, newActivityId, GlobalActivity.ACTIVITY_5_TASK_GROUP);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(newPkgId);
		return R.ok("[作业/小组任务]活动复制成功").put("fileList", fileList).put("answerList", answerList);
	}

	/**
	 * 查看活动基本信息（注意，此接口不返回参考答案数据）
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewActivityTaskInfo(String ctId, String pkgId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TevglActivityTask tevglActivityTask = tevglActivityTaskMapper.selectObjectByIdAndPkgId(params);
		if (tevglActivityTask == null) {
			return R.error("无效的记录");
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 活动基本信息
		Map<String, Object> activityInfo = getSimpleActivityInfo(tevglActivityTask);
		// 返回数据
		data.put("activityInfo", activityInfo);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	private Map<String, Object> getSimpleActivityInfo(TevglActivityTask tevglActivityTask){
		if (tevglActivityTask == null) {
			return null;
		}
		Map<String, Object> info = new HashMap<>();
		info.put("activityId", tevglActivityTask.getActivityId());
		info.put("activityTitle", tevglActivityTask.getActivityTitle());
		info.put("content", tevglActivityTask.getContent());
		// 任务小组划分方式(1不划分2随机划分3线下划分4使用成员小组方案)
		info.put("divideGroupType", tevglActivityTask.getDivideGroupType());
		// 评分方式、作业分值及评分点(1老师评分2置顶助教/学生评分3学生互评4老师评分，组间评分，组内互评)
		info.put("scoreType", tevglActivityTask.getScoreType());
		// 是否设置最晚提交时间(Y/N)
		info.put("isSetLastestSubmissionTime", tevglActivityTask.getIsSetLastestSubmissionTime());
		// 最晚提交时间
		info.put("lastestSubmissionTime", tevglActivityTask.getLastestSubmissionTime());
		// 是否允许超时提交作业(Y/N)
		info.put("isAllowExceedTime", tevglActivityTask.getIsAllowExceedTime());
		// 可获得经验值
		info.put("empiricalValue", tevglActivityTask.getEmpiricalValue());
		// 活动开始时间
		info.put("empiricalValue", tevglActivityTask.getActivityBeginTime());
		return info;
	}
	
}
