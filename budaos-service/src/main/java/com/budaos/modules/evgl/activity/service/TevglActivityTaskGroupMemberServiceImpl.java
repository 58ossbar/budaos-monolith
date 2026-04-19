package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.common.enums.CommonEnum;
import com.budaos.modules.evgl.activity.api.TevglActivityTaskGroupMemberService;
import com.budaos.modules.evgl.activity.domain.TevglActivityTask;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskFile;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskGroupMember;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskTraineeAnswerFile;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskFileMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskGroupMemberMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskTraineeAnswerFileMapper;
import com.budaos.modules.evgl.activity.query.ActTaskGroupQuery;
import com.budaos.modules.evgl.activity.vo.ActTaskTraineeAnswerFileVo;
import com.budaos.modules.evgl.activity.vo.ActTraineeAnswerVo;
import com.budaos.modules.evgl.activity.vo.CommitTaskAnswerVo;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.tch.vo.TevglTchClassroomTraineeVo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
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
public class TevglActivityTaskGroupMemberServiceImpl implements TevglActivityTaskGroupMemberService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityTaskGroupMemberServiceImpl.class);
	@Autowired
	private TevglActivityTaskGroupMemberMapper tevglActivityTaskGroupMemberMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglActivityTaskFileMapper tevglActivityTaskFileMapper;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglActivityTaskMapper tevglActivityTaskMapper;
	@Autowired
	private TevglActivityTaskTraineeAnswerFileMapper tevglActivityTaskTraineeAnswerFileMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;

	/**
	 * 查询列表
	 * @param params
	 * @return
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityTaskGroupMember> tevglActivityTaskGroupMemberList = tevglActivityTaskGroupMemberMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskGroupMemberList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskGroupMemberList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityTaskGroupMemberList = tevglActivityTaskGroupMemberMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskGroupMemberList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskGroupMemberList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityTaskGroupMember
	 * @throws BudaosException
	 */
	public R save(TevglActivityTaskGroupMember tevglActivityTaskGroupMember) throws BudaosException {
		tevglActivityTaskGroupMember.setMemberId(Identities.uuid());
		tevglActivityTaskGroupMember.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityTaskGroupMember.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityTaskGroupMember);
		tevglActivityTaskGroupMemberMapper.insert(tevglActivityTaskGroupMember);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityTaskGroupMember
	 * @throws BudaosException
	 */
	public R update(TevglActivityTaskGroupMember tevglActivityTaskGroupMember) throws BudaosException {
	    ValidatorUtils.check(tevglActivityTaskGroupMember);
		tevglActivityTaskGroupMemberMapper.update(tevglActivityTaskGroupMember);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityTaskGroupMemberMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityTaskGroupMemberMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityTaskGroupMemberMapper.selectObjectById(id));
	}

	/**
	 * 查询某学员的作答情况
	 * @param ctId
	 * @param activityId
	 * @param traineeId
	 * @return
	 */
	@Override
	public R viewTraineeAnswerContent(String ctId, String activityId, String traineeId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(traineeId)) {
			return R.error(BizCodeEnume.PARAM_MISSING.getCode(), BizCodeEnume.PARAM_MISSING.getMsg());
		}
		TevglActivityTaskGroupMember tevglActivityTaskGroupMember = tevglActivityTaskGroupMemberMapper.selectObjectBy(ctId, activityId, traineeId);
		if (tevglActivityTaskGroupMember == null) {
			return R.error("没有找到该成员");
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 活动基本信息
		Map<String, Object> activityInfo = tevglActivityTaskMapper.selectObjectMapById(activityId);
		activityInfo.remove("referenceAnswer"); // 不展示答案
		// 查询任务的附件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", activityId);
		params.put("fromType", "1");
		List<TevglActivityTaskFile> taskFiles = tevglActivityTaskFileMapper.selectListByMap(params);
		// 处理附件显示地址
		taskFiles.stream().forEach(item -> {
			item.setFileAccessUrl(uploadPathUtils.stitchingPath(item.getFileAccessUrl(), "22"));
		});
		activityInfo.put("fileList", taskFiles);
		// 存储答案
		params.clear();
		params.put("activityId", activityId);
		params.put("groupId", tevglActivityTaskGroupMember.getGroupId());
		params.put("memberId", tevglActivityTaskGroupMember.getMemberId());
		List<TevglActivityTaskTraineeAnswerFile> answerFileList = tevglActivityTaskTraineeAnswerFileMapper.selectListByMap(params);
		answerFileList.stream().forEach(item -> {
			item.setFileAccessUrl(uploadPathUtils.stitchingPath(item.getFileAccessUrl(), "22"));
		});
		data.put("activityInfo", activityInfo);
		data.put("answerContent", tevglActivityTaskGroupMember.getContent());
		data.put("answerFileList", answerFileList);
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 学生提交作业/小组任务
	 *
	 * @param vo
	 * @return
	 */
	@Override
	public R commitTast(CommitTaskAnswerVo vo) {
		ValidatorUtils.check(vo);
		TevglTchClassroom classroomInfo = tevglTchClassroomMapper.selectObjectById(vo.getCtId());
		if (classroomInfo == null) {
			return R.error("无效的课堂，提交失败");
		}
		// 如果不是该课堂成员，不允许提交
		List<TevglTchClassroomTraineeVo> classroomTrainees = tevglTchClassroomTraineeMapper.findClassroomTraineeBy(vo.getTraineeId(), Arrays.asList(vo.getCtId()));
		if (classroomTrainees == null || classroomTrainees.isEmpty()) {
			return R.error("不是该课堂成员，无法进行当前操作！");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", classroomInfo.getPkgId());
		params.put("activityId", vo.getActivityId());
		TevglActivityTask activityInfo = tevglActivityTaskMapper.selectObjectByIdAndPkgId(params);
		if (activityInfo == null) {
			return R.error("无效的活动，提交失败");
		}
		if ("0".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动未开始，暂不能提交");
		}
		if ("2".equals(activityInfo.getActivityStateActual())) {
			return R.error("活动已结束，不能提交了");
		}
		// 找到
		params.clear();
		params.put("ctId", vo.getCtId());
		params.put("activityId", vo.getActivityId());
		params.put("traineeId", vo.getTraineeId());
		params.put("state", CommonEnum.STATE_YES.getCode());
		List<TevglActivityTaskGroupMember> memberList = tevglActivityTaskGroupMemberMapper.selectListByMap(params);
		// 新增
		if (memberList == null || memberList.isEmpty()) {
			return R.error("无法提交内容");
		}
		String memberId = memberList.get(0).getMemberId();
		TevglActivityTaskGroupMember t = new TevglActivityTaskGroupMember();
		t.setMemberId(memberId);
		t.setContent(vo.getContent());
		tevglActivityTaskGroupMemberMapper.updateContent(t);
		// 绑定附件关系
		bindRelationship(vo, memberList.get(0));
		// 删除该用户上传的冗余文件
		deleteFileList(vo.getTraineeId());
		return R.ok("保存成功");
	}

	/**
	 * 删除该用户提交的冗余附件
	 * @param traineeId
	 */
	private void deleteFileList(String traineeId) {
		Map<String, Object> map = new HashMap<>();
		map.put("createUserId", traineeId);
		List<TevglActivityTaskTraineeAnswerFile> tevglActivityTaskTraineeAnswerFiles = tevglActivityTaskTraineeAnswerFileMapper.selectListByMap(map);
		List<TevglActivityTaskTraineeAnswerFile> traineeAnswerFiles = tevglActivityTaskTraineeAnswerFiles.stream().filter(a -> StrUtils.isEmpty(a.getActivityId())).collect(Collectors.toList());
		if (traineeAnswerFiles != null && !traineeAnswerFiles.isEmpty()) {
			List<String> deleteFileIdList = new ArrayList<>();
			traineeAnswerFiles.stream().forEach(item -> {
				// 从磁盘上删除文件
				if (StrUtils.isNotEmpty(item.getFileSavePath())) {
					File f = new File(item.getFileSavePath());
					if (f.exists() && !f.isDirectory()) {
						f.delete();
					}
				}
				deleteFileIdList.add(item.getFileId());
			});
			tevglActivityTaskTraineeAnswerFileMapper.deleteBatch(deleteFileIdList.stream().toArray(String[]::new));
		}
	}

	/**
	 * 绑定关系
	 * @param vo
	 * @param member
	 */
	private void bindRelationship(CommitTaskAnswerVo vo, TevglActivityTaskGroupMember member) {
		if (vo.getFileIdList() != null && !vo.getFileIdList().isEmpty()) {
			List<TevglActivityTaskTraineeAnswerFile> updateFileList = new ArrayList<>();
			vo.getFileIdList().stream().forEach(item -> {
				TevglActivityTaskTraineeAnswerFile tf = new TevglActivityTaskTraineeAnswerFile();
				tf.setFileId(item);
				tf.setCtId(vo.getCtId());
				tf.setActivityId(vo.getActivityId());
				tf.setGroupId(member.getGroupId());
				tf.setMemberId(member.getMemberId());
				tf.setUpdateUserId(vo.getTraineeId());
				tf.setUpdateTime(DateUtils.getNowTimeStamp());
				updateFileList.add(tf);
			});
			tevglActivityTaskTraineeAnswerFileMapper.updateBatchByDuplicateKey(updateFileList);
		}
	}
	
	/**
	 * 教师查看所有学生的作业任务
	 *
	 * @param query
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryTraineeAnswerList(ActTaskGroupQuery query, String loginUserId) {
		ValidatorUtils.check(query);
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(query.getCtId());
		boolean hasOperatingAuthorization = cbRoomUtils.hasOperatingAuthorization(tevglTchClassroom, loginUserId);
		if (!hasOperatingAuthorization) {
			return R.error("没有权限查看相关数据！");
		}
		TevglActivityTask tevglActivityTask = tevglActivityTaskMapper.selectObjectById(query.getActivityId());
		if (tevglActivityTask == null) {
			return R.error(BizCodeEnume.PARAM_INVALID.getCode(), BizCodeEnume.PARAM_INVALID.getMsg());
		}
		// 如果是 不划分小组
		if ("1".equals(tevglActivityTask.getDivideGroupType())) {
			List<ActTraineeAnswerVo> traineeAnswerList = tevglActivityTaskGroupMemberMapper.findTraineeAnswerList(query);
			if (traineeAnswerList != null && !traineeAnswerList.isEmpty()) {
				Map<String, Object> map = new HashMap<>();
				map.put("memberIdList", traineeAnswerList.stream().map(a -> a.getMemberId()).collect(Collectors.toList()));
				List<ActTaskTraineeAnswerFileVo> allFileList = tevglActivityTaskTraineeAnswerFileMapper.findTraineeAnswerList(map);
				traineeAnswerList.stream().forEach(item -> {
					// 处理头像
					item.setTraineeHead(uploadPathUtils.stitchingPath(item.getTraineeHead(), "16"));
					// 处理附件
					List<ActTaskTraineeAnswerFileVo> fileList = allFileList.stream().filter(a -> a.getMemberId().equals(item.getMemberId())).collect(Collectors.toList());
					fileList.stream().forEach(fileInfo -> {
						fileInfo.setFileAccessUrl(uploadPathUtils.stitchingPath(fileInfo.getFileAccessUrl(), "22"));
					});
					item.setFileList(fileList);
				});
			}
			return R.ok().put(Constant.R_DATA, traineeAnswerList);
		}
		// 如果是随机划分
		if ("2".equals(tevglActivityTask.getDivideGroupType())) {

		}
		if ("3".equals(tevglActivityTask.getDivideGroupType())) {

		}
		//
		if ("4".equals(tevglActivityTask.getDivideGroupType())) {

		}
		return R.ok();
	}
}
