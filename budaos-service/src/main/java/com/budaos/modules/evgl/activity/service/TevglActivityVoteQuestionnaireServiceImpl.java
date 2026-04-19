package com.budaos.modules.evgl.activity.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.evgl.activity.api.TevglActivityVoteQuestionnaireService;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaire;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireQuestion;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireQuestionOption;
import com.budaos.modules.evgl.activity.persistence.*;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p> Title: 活动-投票/问卷</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivityVoteQuestionnaireServiceImpl implements TevglActivityVoteQuestionnaireService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityVoteQuestionnaireServiceImpl.class);
	@Autowired
	private TevglActivityVoteQuestionnaireMapper tevglActivityVoteQuestionnaireMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionMapper tevglActivityVoteQuestionnaireQuestionMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionOptionMapper tevglActivityVoteQuestionnaireQuestionOptionMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private PkgActivityUtils pkgActivityUtils;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerMapper tevglActivityVoteQuestionnaireTraineeAnswerMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerFileMapper tevglActivityVoteQuestionnaireTraineeAnswerFileMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	// 文件格式
	private final List<String> imageSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP");
	private final List<String> videoSuffixList = Arrays.asList(".MP4", ".FLV", ".RMVB", ".AVI", ".WMV", ".MOV");
	private final List<String> audioSuffixList = Arrays.asList(".MP3", ".WAV", ".WMA", ".OGG", ".FLAC");
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
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
		List<TevglActivityVoteQuestionnaire> tevglActivityVoteQuestionnaireList = tevglActivityVoteQuestionnaireMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityVoteQuestionnaireList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityVoteQuestionnaireList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表
	 * @param params
	 * @return
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityVoteQuestionnaireList = tevglActivityVoteQuestionnaireMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityVoteQuestionnaireList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityVoteQuestionnaire
	 * @throws BudaosException
	 */
	public R save(TevglActivityVoteQuestionnaire tevglActivityVoteQuestionnaire) throws BudaosException {
		tevglActivityVoteQuestionnaire.setActivityId(Identities.uuid());
		tevglActivityVoteQuestionnaire.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityVoteQuestionnaire.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityVoteQuestionnaire);
		tevglActivityVoteQuestionnaireMapper.insert(tevglActivityVoteQuestionnaire);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityVoteQuestionnaire
	 * @throws BudaosException
	 */
	public R update(TevglActivityVoteQuestionnaire tevglActivityVoteQuestionnaire) throws BudaosException {
	    tevglActivityVoteQuestionnaire.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityVoteQuestionnaire.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityVoteQuestionnaire);
		tevglActivityVoteQuestionnaireMapper.update(tevglActivityVoteQuestionnaire);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityVoteQuestionnaireMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityVoteQuestionnaireMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityVoteQuestionnaireMapper.selectObjectById(id));
	}

	/**
	 * 保存投票/问卷
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 * @apiNote
	 * [
	 *     {
	 *         'questionId':'题目id', 'questionName':'题目名称', 'questionType':'题目类型', 'sortNum':'排序号'
	 *         'children':[
	 *             {'optionId':'选项id', 'optionCode':'选项编码', 'optionName':'选项名称', 'isRight':'Y/N'}
	 *         ]
	 *     }
	 * ]
	 */
	@Override
	@Transactional
	public R saveVoteQuestionnaireInfo(JSONObject jsonObject, String loginUserId) throws BudaosException {
		// 基本信息
		String pkgId = jsonObject.getString("pkgId");
		String chapterId = jsonObject.getString("chapterId");
		String activityTitle = jsonObject.getString("activityTitle");
		// 创建投票问卷活动时固定分组
		String resgroupId = GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE;//jsonObject.getString("resgroupId");
		String isShow = jsonObject.getString("isShow");
		String purpose = jsonObject.getString("purpose");
		// 题目和选项信息
		String json = jsonObject.getString("json");
		R r = checkIsPass(pkgId, activityTitle, resgroupId, isShow, json, loginUserId);
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
		JSONArray jsonArray = JSON.parseArray(json);
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("json格式为空");
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
		// 填充基本信息
		TevglActivityVoteQuestionnaire vo = fillVoteQuestionnaireInfo(activityTitle, loginUserId, pkgId, resgroupId, isShow, purpose, chapterId);
		// 保存基本信息
		vo.setQuestionNumber(jsonArray.size());
		tevglActivityVoteQuestionnaireMapper.insert(vo);
		// 保存活动与教学包的关系
		pkgUtils.buildRelation(pkgId, vo.getActivityId(), GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		// 保存题目信息
		doSaveQuestionInfo(jsonArray, vo.getActivityId(), loginUserId);
		// 更新数量
		pkgUtils.plusPkgActivityNum(pkgId);
		return R.ok("[投票/问卷] 创建成功");
	}
	
	/**
	 * 修改投票问卷
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R updateVoteQuestionnaireInfo(JSONObject jsonObject, String loginUserId) {
		// 基本信息
		String activityId = jsonObject.getString("activityId");
		if (StrUtils.isEmpty(activityId)) {
			return R.error("参数activityId为空");
		}
		TevglActivityVoteQuestionnaire activityInfo = tevglActivityVoteQuestionnaireMapper.selectObjectById(activityId);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(activityInfo.getActivityState())) {
			return R.error("活动已开始，无法修改");
		}
		if ("2".equals(activityInfo.getActivityState())) {
			return R.error("活动已结束，无法修改");
		}
		String pkgId = jsonObject.getString("pkgId");
		String chapterId = jsonObject.getString("chapterId");
		String activityTitle = jsonObject.getString("activityTitle");
		// 规定活动分组
		String resgroupId = GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE; // jsonObject.getString("resgroupId");
		String isShow = jsonObject.getString("isShow");
		String use = jsonObject.getString("use");
		// 题目和选项信息
		String json = jsonObject.getString("json");
		R r = checkIsPass(pkgId, activityTitle, resgroupId, isShow, json, loginUserId);
		if ((Integer) r.get("code") != 0) {
			return r;
		}
		JSONArray jsonArray = JSON.parseArray(json);
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("json格式为空");
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
		// 填充基本信息
		TevglActivityVoteQuestionnaire vo = fillVoteQuestionnaireInfo(activityTitle, loginUserId, pkgId, resgroupId, isShow, use, chapterId);
		vo.setActivityId(activityId);
		vo.setQuestionNumber(jsonArray.size());
		// 保存基本数据
		tevglActivityVoteQuestionnaireMapper.update(vo);
		Map<String, Object> params = new HashMap<String, Object>();
		// 先查询该活动原先存于数据库中的题目
		params.put("activityId", activityId);
		List<TevglActivityVoteQuestionnaireQuestion> voteQuestionnaireQuestionList = tevglActivityVoteQuestionnaireQuestionMapper.selectListByMap(params);
		List<String> questionIds = voteQuestionnaireQuestionList.stream().map(a -> a.getQuestionId()).collect(Collectors.toList());
		List<String> oldQuestionIds = new ArrayList<String>();
		// 处理题目
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject questionObject = (JSONObject) jsonArray.get(i);
			if (questionObject != null) {
				// 填充题目信息
				String questionId = questionObject.getString("questionId");
				String questionName = questionObject.getString("questionName");
				String questionType = questionObject.getString("questionType");
				Integer questionSortNum = questionObject.getInteger("sortNum");
				String tips = questionObject.getString("tips");
				TevglActivityVoteQuestionnaireQuestion question = new TevglActivityVoteQuestionnaireQuestion();
				question.setQuestionId(questionId);
				question.setActivityId(activityId);
				question.setQuestionType(questionType);
				question.setQuestionName(questionName);
				question.setSortNum(questionSortNum);
				question.setSortNum(i);
				question.setUpdateUserId(loginUserId);
				question.setTips(tips);
				if (StrUtils.isEmpty(questionId)) { // 说明这个是新添加题目
					questionId = Identities.uuid();
					question.setQuestionId(questionId);
					question.setState("Y");
					question.setCreateTime(DateUtils.getNowTimeStamp());
					tevglActivityVoteQuestionnaireQuestionMapper.insert(question);
				} else {
					oldQuestionIds.add(questionId);
					question.setCreateUserId(loginUserId);
					question.setUpdateTime(DateUtils.getNowTimeStamp());
					tevglActivityVoteQuestionnaireQuestionMapper.update(question);	
				}
				// 选项信息
				JSONArray children = questionObject.getJSONArray("children");
				// 查出该题目原有选项
				params.put("questionId", questionId);
				//List<Map<String, Object>> oldOptionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectListMapByMap(params);
				List<TevglActivityVoteQuestionnaireQuestionOption> oldOptionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectListByMap(params);
				doOptionChange(children, oldOptionList, questionId);
			}
		}
		// 额外处理,删除某种情况下产出的冗余数据
		questionIds.removeAll(oldQuestionIds);
		for (String string : questionIds) {
			// 删除冗余选项
			tevglActivityVoteQuestionnaireQuestionOptionMapper.deleteByQuestionId(string);
			// 删除冗余题目
			tevglActivityVoteQuestionnaireQuestionMapper.delete(string);
		}
		return R.ok("修改成功");
	}
	
	/**
     * 处理题目选项变化的情况（如不变，变多，变少），且更新数据库记录
     * @param children 用户前端录入的选项
     * @param oldOptionList 此题目已存在于数据库中的选项
     * @param questionId 此题目
     * @return
     */
    private String doOptionChange(JSONArray children, List<TevglActivityVoteQuestionnaireQuestionOption> oldOptionList, String questionId) {
        String ids = "";
        // 用于存放已存在于数据库中的选项id
        List<String> oldOptionIds = new ArrayList<String>();
        for (int j = 0; j < children.size(); j++) {
            JSONObject optionObject = children.getJSONObject(j);
            String optionId = optionObject.getString("optionId");
			String optionCode = optionObject.getString("optionCode");
			String optionName = optionObject.getString("optionName");
			String isRight = optionObject.getString("isRight");
			String canFill = optionObject.getString("canFill");
            // 填充选项
            TevglActivityVoteQuestionnaireQuestionOption option = new TevglActivityVoteQuestionnaireQuestionOption();
            option.setOptionId(optionId);
            option.setQuestionId(questionId);
			option.setOptionCode(optionCode);
			option.setOptionName(optionName);
			option.setSortNum(j);
			option.setIsRight(isRight);
			option.setCanFill(canFill);
            // 注意，注意，注意
            // 如果删除已经存在的选项,再重新添加一个或多个新的选项,则需要额外处理
            // 说明这个是新添加的选项
            if (StrUtils.isEmpty(optionId) || optionId.length() < 32) {
                // 入库
                option.setOptionId(Identities.uuid());
                tevglActivityVoteQuestionnaireQuestionOptionMapper.insert(option);
            } else {
                // 更新入库
                option.setOptionId(optionId);
                tevglActivityVoteQuestionnaireQuestionOptionMapper.update(option);
                // 加入集合
                oldOptionIds.add(optionId);
            }
            // 拼接题目标准答案选项optionId
            if ("Y".equals(isRight) || "YES".equals(isRight)) {
                ids += option.getOptionId()+",";
            }
            // 最后一次循环时
            if (j == children.size() - 1) {
                // 单独取出选项id
                List<String> optionIds = oldOptionList.stream().map(a -> a.getOptionId()).collect(Collectors.toList());
                // 取差集
                optionIds.removeAll(oldOptionIds);
                // 执行删除
                if (optionIds != null && optionIds.size() > 0) {
                    log.debug("选修改前后，取差集，需要被删除的选项是：" + optionIds);
                    for (String string : optionIds) {
                    	tevglActivityVoteQuestionnaireQuestionOptionMapper.delete(string);
                    }
                }
            }
        }
        if (StrUtils.isNotEmpty(ids)) {
            ids = ids.substring(0, ids.length() - 1);
        }
        return ids;
    }
	
	/**
	 * 查看投票问卷
	 * @param activityId
	 * @return
	 */
	@Override
	public R viewVoteQuestionnaireInfo(String activityId) {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> voInfo = tevglActivityVoteQuestionnaireMapper.selectObjectMapById(activityId);
		if (voInfo == null) {
			return R.error("无效的记录");
		}
		/*TevglPkgActivityRelation activityRelation = tevglPkgActivityRelationMapper.selectObjectByActivityId(activityId);
		if (activityRelation != null) {
			voInfo.put("pkgId", activityRelation.getPkgId());
		}*/
		// 题目信息
		voInfo.put("questionList", null);
		params.put("activityId", activityId);
		params.put("state", "Y");
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> questionList = tevglActivityVoteQuestionnaireQuestionMapper.selectSimpleListMapByMap(params);
		// 题目选项信息
		List<String> questionIds = questionList.stream().map(a -> (String)a.get("questionId")).collect(Collectors.toList());
		params.clear();
		params.put("questionIds", questionIds);
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> optionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectListMapByMap(params);
		questionList.stream().forEach(questionInfo -> {
			// 取出题目对应的选项
			List<Map<String, Object>> children = optionList.stream().filter(optionInfo -> questionInfo.get("questionId").equals(optionInfo.get("questionId"))).collect(Collectors.toList());
			questionInfo.put("children", children);
		});
		voInfo.put("questionList", questionList);
		return R.ok().put(Constant.R_DATA, voInfo);
	}

	/**
	 * 删除投票问卷
	 * @param activityId 活动id
	 * @param pkgId 所属教学包
	 * @param loginUserId 当前登录用户
	 * @return
	 * @throws BudaosException
	 */
	@Override
	@Transactional
	public R deleteVoteQuestionnaireInfo(String activityId, String pkgId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("activityId", activityId);
		TevglActivityVoteQuestionnaire voteQuestionnaireInfo = tevglActivityVoteQuestionnaireMapper.selectObjectByIdAndPkgId(params);
		if (voteQuestionnaireInfo == null) {
			return R.error("无效的记录");
		}
		// 如果活动已经开始或结束,控制不能删除
		if (!"0".equals(voteQuestionnaireInfo.getActivityStateActual())) {
			return R.error("当前活动已被使用，无法删除");
		}
		// 权限校验
		if (!pkgPermissionUtils.hasPermissionDeleteActivity(pkgId, voteQuestionnaireInfo.getCreateUserId(), loginUserId, voteQuestionnaireInfo.getChapterId())) {
			return R.error("暂无权限，操作失败");
		}
		// 删除活动与教学包的关系
		tevglPkgActivityRelationMapper.deleteByActivityId(activityId);
		// 找到对应活动的题目和选项
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("activityId", activityId);
		List<Map<String, Object>> questionList = tevglActivityVoteQuestionnaireQuestionMapper.selectSimpleListMapByMap(ps);
		if (questionList != null && questionList.size() > 0) {
			String[] questionIds = questionList.stream().map(a -> (String)a.get("questionId")).toArray(String[]::new);
			if (questionIds != null && questionIds.length > 0) {
				// 先删除选项
				tevglActivityVoteQuestionnaireQuestionOptionMapper.deleteBatchByQuestionId(questionIds);
				// 再删除题目
				tevglActivityVoteQuestionnaireQuestionMapper.deleteBatch(questionIds);	
			}	
		}
		// 删除活动
		tevglActivityVoteQuestionnaireMapper.delete(activityId);
		// 教学包活动数量-1
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("删除成功");
	}

	/**
	 * 开始活动
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @return 参与活动获取的经验值
	 * @throws BudaosException
	 */
	@Override
	public R startActivityVoteQuestionnaire(String ctId, String activityId, String loginUserId, String activityEndTime) throws BudaosException {
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
		TevglActivityVoteQuestionnaire activityInfo = tevglActivityVoteQuestionnaireMapper.selectObjectByIdAndPkgId(params);
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
		// 调整为修改关联表的状态与时间
		relation.setActivityState("1");
		relation.setActivityBeginTime(DateUtils.getNowTimeStamp());
		relation.setActivityEndTime(StrUtils.isEmpty(activityEndTime) ? null : activityEndTime);
		tevglPkgActivityRelationMapper.update(relation);
		// 返回数据
		Map<String, Object> data = new HashMap<>();
		data.put("empiricalValue", activityInfo.getEmpiricalValue());
		data.put("activityType", GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getActivityTitle());
		data.put("content", "");
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
		msg.put("activity_type", GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE); 
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("send_id", loginUserId);
		busiMsg.put("send_name", null);
		busiMsg.put("tips", tips);
		busiMsg.put("activity_id", activityInfo.getActivityId());
		busiMsg.put("activity_type", GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		busiMsg.put("activity_title", activityInfo.getActivityTitle());
		busiMsg.put("activity_pic", activityInfo.getActivityPic());
		busiMsg.put("activity_state", "1"); // 活动状态0未开始1进行中2已结束
		busiMsg.put("activityState", "1");
		busiMsg.put("content", "");
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
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * 结束活动
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R endActivityVoteQuestionnaire(String ctId, String activityId, String loginUserId) throws BudaosException {
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
		TevglActivityVoteQuestionnaire activityInfo = tevglActivityVoteQuestionnaireMapper.selectObjectById(activityId);
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
	 * 校验参数
	 * @param pkgId
	 * @param activityTitle
	 * @param resgroupId
	 * @param isShow
	 * @param json
	 * @param loginUserId
	 * @return
	 */
	private R checkIsPass(String pkgId, String activityTitle, String resgroupId
			, String isShow, String json, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("参数loginUserId为空");
		}
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("参数pkgId为空");
		}
		if (StrUtils.isEmpty(activityTitle)) {
			return R.error("参数activityTitle为空");
		}
		activityTitle = activityTitle.trim();
		if (activityTitle.length() > 100) {
			return R.error("标题不能超过100个字符");
		}
		if (StrUtils.isEmpty(resgroupId)) {
			return R.error("参数resgroupId为空");
		}
		if (StrUtils.isEmpty(isShow)) {
			return R.error("参数isShow为空");
		}
		if (StrUtils.isEmpty(json)) {
			return R.error("参数json为空");
		}
		return R.ok();
	}

	/**
	 * 填充投票问卷信息
	 * @param activityTitle
	 * @param loginUserId
	 * @param pkgId
	 * @param resgroupId
	 * @param isShow
	 * @param purpose
	 * @param chapterId
	 * @return
	 */
	private TevglActivityVoteQuestionnaire fillVoteQuestionnaireInfo(String activityTitle, String loginUserId
			, String pkgId, String resgroupId, String isShow, String purpose
			, String chapterId) {
		TevglActivityVoteQuestionnaire vo = new TevglActivityVoteQuestionnaire();
		vo.setActivityId(Identities.uuid());
		vo.setActivityTitle(activityTitle);
		vo.setCreateTime(DateUtils.getNowTimeStamp());
		vo.setCreateUserId(loginUserId);
		vo.setResgroupId(resgroupId);
		vo.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		vo.setActivityState("0"); // 0未开始1进行中2已结束
		vo.setState("Y"); // Y有效N无效
		vo.setActivityType(GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		vo.setAnswerNumber(0);
		vo.setIsShow(isShow);
		vo.setPurpose(purpose);
		vo.setChapterId(chapterId);
		// 排序号处理
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("pkgId", pkgId);
		ps.put("resgroupId", resgroupId);
		Integer sortNum = tevglActivityVoteQuestionnaireMapper.getMaxSortNum(ps);
		vo.setSortNum(sortNum);
		return vo;
	}
	
	/**
	 * 保存题目信息
	 * @param jsonArray
	 * @param activityId 活动id
	 * @apiNote
	 * [
	 *     {
	 *         'questionId':'题目id', 'questionName':'题目名称', 'questionType':'题目类型', 'sortNum':'排序号'
	 *         'children':[
	 *             {'optionId':'选项id', 'optionCode':'选项编码', 'optionName':'选项名称', 'isRight':'Y/N'}
	 *         ]
	 *     }
	 * ]
	 */
	private R doSaveQuestionInfo(JSONArray jsonArray, String activityId, String loginUserId) {
		if (jsonArray == null) {
			return R.error("请录入题目");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("activityId", activityId);
		Integer maxSortNum = tevglActivityVoteQuestionnaireQuestionMapper.getMaxSortNum(params);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject questionObject = (JSONObject) jsonArray.get(i);
			if (questionObject != null) {
				// 填充题目信息
				String questionName = questionObject.getString("questionName");
				String questionType = questionObject.getString("questionType");
				Integer questionSortNum = questionObject.getInteger("sortNum");
				JSONArray children = questionObject.getJSONArray("children");
				String tips = questionObject.getString("tips");
				TevglActivityVoteQuestionnaireQuestion question = new TevglActivityVoteQuestionnaireQuestion();
				question.setQuestionId(Identities.uuid());
				question.setActivityId(activityId);
				question.setQuestionType(questionType);
				question.setQuestionName(questionName);
				question.setSortNum(questionSortNum);
				question.setState("Y");
				question.setCreateTime(DateUtils.getNowTimeStamp());
				question.setSortNum(i + maxSortNum);
				question.setCreateUserId(loginUserId);
				if (StrUtils.isNotEmpty(tips)) {
					if (tips.length() > 500) {
						return R.error("提示语不能超过500个字符");
					}
					question.setTips(tips);
				}
				tevglActivityVoteQuestionnaireQuestionMapper.insert(question);
				// 单选或多选题的情况下才执行
				if ("1".equals(questionType) || "2".equals(questionType)) {
					if (children != null && children.size() > 0) {
						String ids = ""; // 存放正确选项id
						for (int j = 0; j < children.size(); j++) {
							JSONObject optionObject = (JSONObject) children.get(j);
							if (optionObject != null) {
								String optionCode = optionObject.getString("optionCode");
								String optionName = optionObject.getString("optionName");
								String isRight = optionObject.getString("isRight");
								String canFill = optionObject.getString("canFill");
								TevglActivityVoteQuestionnaireQuestionOption option = new TevglActivityVoteQuestionnaireQuestionOption();
								option.setOptionId(Identities.uuid());
								option.setQuestionId(question.getQuestionId()); // 所属题目
								option.setOptionCode(optionCode);
								option.setOptionName(optionName);
								option.setSortNum(j);
								option.setIsRight(isRight);
								option.setCanFill(canFill);
								tevglActivityVoteQuestionnaireQuestionOptionMapper.insert(option);
								// 处理正确答案
								if ("Y".equals(isRight)) {
									ids += option.getOptionId()+",";
								}
							}
						}
						// 更新题目的正确答案
						if (StrUtils.isNotEmpty(ids)) {
							ids = ids.substring(0, ids.length() - 1);
							question.setQuestionAnswer(ids);
							tevglActivityVoteQuestionnaireQuestionMapper.update(question);
						}
					}	
				}
			}
		}
		return R.ok();
	}

	/**
	 * 复制活动
	 * @param targetActivityId
	 * @param newPkgId
	 * @param loginUserId
	 * @param chapterId
	 * @param sortNum
	 */
	@Override
	public void copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum) {
		if (StrUtils.isEmpty(targetActivityId) || StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(loginUserId)) {
			log.debug("必传参数为空");
			return;
		}
		// 找到目标活动
		TevglActivityVoteQuestionnaire voteQuestionnaireInfo = tevglActivityVoteQuestionnaireMapper.selectObjectById(targetActivityId);
		if (voteQuestionnaireInfo == null) {
			return ;
		}
		// 找到目标活动的活动分组(自定义活动分组时记得控制分组名称唯一)(没有自定义活动分组)
		String resgroupId = "0";
		resgroupId = voteQuestionnaireInfo.getResgroupId();
		/*TevglPkgResgroup resgroup = tevglPkgResgroupMapper.selectObjectById(voteQuestionnaireInfo.getResgroupId());
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
		// 找到所有题目
		List<TevglActivityVoteQuestionnaireQuestion> questionList = tevglActivityVoteQuestionnaireQuestionMapper.selectActivityQuestionList(targetActivityId);
		if (questionList != null && questionList.size() > 0) {
			List<String> questionIds = questionList.stream().map(a -> a.getQuestionId()).collect(Collectors.toList());
			// 找到所有选项
			List<TevglActivityVoteQuestionnaireQuestionOption> optionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectOptionListByQuestionIds(questionIds);
			questionList.stream().forEach(a -> {
				List<TevglActivityVoteQuestionnaireQuestionOption> children = optionList.stream().filter(optionInfo -> optionInfo.getQuestionId().equals(a.getQuestionId())).collect(Collectors.toList());
				a.setChildren(children);
			});
		}
		// 填充新的活动基本信息
		TevglActivityVoteQuestionnaire vo = new TevglActivityVoteQuestionnaire();
		vo = voteQuestionnaireInfo;
		vo.setActivityId(Identities.uuid());
		vo.setChapterId(StrUtils.isEmpty(chapterId) ? voteQuestionnaireInfo.getChapterId() : chapterId);
		vo.setActivityTitle(voteQuestionnaireInfo.getActivityTitle());
		vo.setCreateUserId(loginUserId);
		vo.setSortNum(sortNum);
		vo.setActivityBeginTime(null);
		vo.setActivityEndTime(null);
		vo.setState("Y");
		vo.setCreateTime(DateUtils.getNowTimeStamp());
		vo.setResgroupId(resgroupId);
		vo.setActivityState("0"); // 状态统一改为未开始
		// 保存活动至数据库
		tevglActivityVoteQuestionnaireMapper.insert(vo);
		String newActivityId = vo.getActivityId();
		// 保存活动与教学包的关系
		pkgActivityUtils.buildRelation(newPkgId, newActivityId, GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(newPkgId);
		// 保存新活动的题目
		if (questionList != null && questionList.size() > 0) {
			// 以题目主键作为key， 选项作为value
			Map<String, Object> optionData = new HashMap<String, Object>();
			// 遍历题目
			questionList.stream().forEach(questionInfo -> {
				TevglActivityVoteQuestionnaireQuestion qq = new TevglActivityVoteQuestionnaireQuestion();
				qq = questionInfo;
				qq.setQuestionId(Identities.uuid());
				qq.setActivityId(newActivityId);
				qq.setCreateTime(DateUtils.getNowTimeStamp());
				tevglActivityVoteQuestionnaireQuestionMapper.insert(qq);
				String newQuestionId = qq.getQuestionId();
				// 保存题目选项
				List<TevglActivityVoteQuestionnaireQuestionOption> children = questionInfo.getChildren();
				if (children != null && children.size() > 0) {
					optionData.put(newQuestionId, children);
				}
			});
			// 前面生成题目之后,随之生成题目的选项
			for (Entry<String, Object> op : optionData.entrySet()) {
				String newQuestionId = op.getKey();
				@SuppressWarnings("unchecked")
				List<TevglActivityVoteQuestionnaireQuestionOption> children = (List<TevglActivityVoteQuestionnaireQuestionOption>)op.getValue();
				children.forEach(optionInfo -> {
					TevglActivityVoteQuestionnaireQuestionOption option = new TevglActivityVoteQuestionnaireQuestionOption();
					option = optionInfo;
					option.setOptionId(Identities.uuid());
					option.setQuestionId(newQuestionId);
					tevglActivityVoteQuestionnaireQuestionOptionMapper.insert(option);
				});
			}
		}
		
	}

	/**
	 * 查看学员针对投票/问卷的作答内容（总体结果）
	 * @return
	 */
	@Override
	public R viewTraineeAnswerListData(Map<String, Object> params, String loginUserId) {
		String ctId = (String) params.get("ctId");
		String activityId = (String) params.get("activityId");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂记录");
		}
		Map<String, Object> activityInfo = tevglActivityVoteQuestionnaireMapper.selectObjectMapById(activityId);
		if (activityInfo == null) {
			return R.error("无效的活动记录");
		}
		boolean isCreator = StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getReceiverUserId());
		boolean isTeachingAssistant = loginUserId.equals(tevglTchClassroom.getTraineeId());
		boolean desensitizationPhoneNumber = !cbRoomUtils.hasOperatingAuthorization(tevglTchClassroom, loginUserId);
		// 返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 此课堂总提交人数
		int submitTheNumber = tevglActivityVoteQuestionnaireTraineeAnswerMapper.countAnswerNum(params);
		// 获取投票问卷所有有效题目
		//List<Map<String,Object>> questionList = getQuestionListData(activityId, params);
		params.put("activityId", activityId);
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> questionList = tevglActivityVoteQuestionnaireQuestionMapper.selectSimpleListMapByMap(query);
		// 获取此课堂此活动的所有学员作答内容
		List<Map<String, Object>> traineeAnswerList = getTraineeAnswerListData(ctId, activityId, params);
		// 处理手机号码和性别
		traineeAnswerList.stream().forEach(traineeInfo -> {
			//traineeInfo.put("mobile", desensitizationPhoneNumber ? getYourPhone(traineeInfo) : traineeInfo.get("mobile"));
			traineeInfo.put("mobile", desensitizationPhoneNumber ? "" : traineeInfo.get("mobile"));
			traineeInfo.put("traineeSex", getYourSex(traineeInfo));
		});
		// 当前用户是否提交
		boolean hasBeenDone = traineeAnswerList.stream().anyMatch(a -> a.get("traineeId").equals(loginUserId));
		if (questionList != null && questionList.size() > 0) {
			// 查询题目所有选项
			List<String> questionIds = questionList.stream().map(a -> (String)a.get("questionId")).collect(Collectors.toList());
			// 是否展示具体谁投了票
			if (isCreator || isReceiver || isTeachingAssistant) {
				params.put("showPerson", "Y");
			}
			List<Map<String, Object>> optionList = getOptionListData(activityId, questionIds, traineeAnswerList, params);
			// 处理题目对应的选项
			handleQuestionOptionData(questionList, optionList);
			// 获取此课堂此活动的所有学员作答内容提交的相关附件
			//List<String> traineeIds = traineeAnswerList.stream().map(a -> a.get("traineeId").toString()).collect(Collectors.toList());
			List<String> ids = traineeAnswerList.stream().map(a -> a.get("id").toString()).collect(Collectors.toList());
			List<Map<String,Object>> fileList = getFileListData(ids, params);
			// 遍历题目
			questionList.forEach(question -> {
				// 取出此题目的选项
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> children = (List<Map<String, Object>>) question.get("children");
				// 取出此题目的作答数据（可认为有多少人回答了此题）
				List<Map<String, Object>> traineeAnswerListFilter = traineeAnswerList.stream().filter(a -> a.get("questionId").equals(question.get("questionId"))).collect(Collectors.toList());
				// 单选或多选题时，调用方法计算
				if ("1".equals(question.get("questionType")) || "2".equals(question.get("questionType"))) {
					countNum(question, children, traineeAnswerListFilter);
				} else if ("3".equals(question.get("questionType"))) {
					traineeAnswerListFilter.forEach(traineeAnswerInfo -> {
						// 头像处理
						traineeAnswerInfo.put("traineePic", uploadPathUtils.stitchingPath((String)traineeAnswerInfo.get("traineePic"), "16"));
						// TODO 获取学员针对简答题提交的相关附件
						List<Map<String, Object>> targetFileList = new ArrayList<>();
						fileList.forEach(file -> {
							if (file.get("id") != null) {
								if (file.get("id").equals(traineeAnswerInfo.get("id"))) {
									String fileUrl = file.get("url").toString();
									file.put("url", uploadPathUtils.stitchingPath(fileUrl, "20"));
									file.put("type", getFileType(fileUrl));
									file.put("fileType", file.get("fileType"));
									targetFileList.add(file);
								}
							}
						});
						traineeAnswerInfo.put("fileList", targetFileList);
					});
					// 返回学员针对此题简答题的回答
					question.put("traineeAnswerList", traineeAnswerListFilter);
				}
			});
		}
		// 添加并返回数据
		result.put("activityId", activityInfo.get("activityId"));
		result.put("activityTitle", activityInfo.get("activityTitle"));
		result.put("submitTheNumber", submitTheNumber); // 总提交人数
		result.put("hasBeenDone", hasBeenDone);
		//result.put("questionList", questionList);
		//return R.ok().put(Constant.R_DATA, result);
		//return R.ok().put(Constant.R_DATA, result);
		PageUtils pageUtil = new PageUtils(questionList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil)
				.put("activityInfo", result);
	}

	/**
	 * 获取文件列表
	 * @param ids
	 * @param params
	 * @return
	 */
	private List<Map<String, Object>> getFileListData(List<String> ids, Map<String, Object> params){
		params.clear();
		params.put("ids", ids);
		params.put("state", "Y");
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> fileList = tevglActivityVoteQuestionnaireTraineeAnswerFileMapper.selectSimpleListMapByMap(params);
		return fileList;
	}

	/**
	 * 根据文件url返回文件类型
	 * @param url
	 * @return
	 */
	private String getFileType(String url) {
		if (StrUtils.isEmpty(url)) {
			return null;
		}
		String suffix = url.substring(url.lastIndexOf("."));
		if (imageSuffixList.contains(suffix.toUpperCase())) {
			return "image";
		} else if (videoSuffixList.contains(suffix.toUpperCase())) {
			return "video";
		} else if (audioSuffixList.contains(suffix.toUpperCase())) {
			return "audio";
		} else {
			return "other";
		}
	}
	
	/**
	 * 获取此课堂此活动的所有学员作答内容
	 * @param ctId 课堂主键
	 * @param activityId 活动主键
	 * @param params 查询条件
	 * @return
	 */
	private List<Map<String, Object>> getTraineeAnswerListData(String ctId, String activityId, Map<String, Object> params){
		params.clear();
		params.put("ctId", ctId);
		params.put("activityId", activityId);
		List<Map<String, Object>> traineeAnswerList = tevglActivityVoteQuestionnaireTraineeAnswerMapper.selectListMapByMap(params);
		return traineeAnswerList;
	}
	
	/**
	 * 获取题目选项
	 * @param activityId 活动id
	 * @param questionIds 题目ID集合
	 * @param traineeAnswerList 学员作答记录
	 * @param params 查询条件
	 * @return
	 */
	private List<Map<String, Object>> getOptionListData(String activityId, List<String> questionIds, List<Map<String, Object>> traineeAnswerList, Map<String, Object> params){
		params.put("questionIds", questionIds);
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> optionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectListMapForWeb(params);
		
		optionList.stream().forEach(option -> {
			List<Map<String, Object>> answerInfo = traineeAnswerList.stream().filter(a -> a.get("optionId").equals(option.get("optionId"))).collect(Collectors.toList());
			if (answerInfo != null && answerInfo.size() > 0) {
				// 存放选填的内容
				List<Map<String, Object>> answerInfos = new ArrayList<>();
				if ("Y".equals(params.get("showPerson"))) {
					answerInfo.stream().forEach(answer -> {
						Map<String, Object> data = new HashMap<>();
						data.put("traineeId", answer.get("traineeId"));
						data.put("mobile", answer.get("mobile"));
						data.put("traineeSex", answer.get("traineeSex"));
						data.put("traineeName", answer.get("traineeName") == null ? "" : answer.get("traineeName"));
						data.put("traineeHead", answer.get("traineeHead"));
						data.put("traineePic", answer.get("traineePic") == null ? "/uploads/defaulthead.png" :  uploadPathUtils.stitchingPath(answer.get("traineePic"), "16"));
						List<Map<String,Object>> collect = answerInfo.stream().filter(a -> a.get("traineeId").equals(answer.get("traineeId"))).distinct().collect(Collectors.toList());
						if (collect != null && !collect.isEmpty()) {
							data.put("answerInfo", collect.get(0).get("content"));
						}
						answerInfos.add(data);
						option.put("answerInfos", answerInfos);
					});
				} else {
					option.put("answerInfos", answerInfos);
				}
			}
		});
		return optionList;
	}
	
	private String getYourPhone(Map<String, Object> answer) {
		String mobile = "";
		if (answer == null || StrUtils.isNull(answer.get("mobile"))) {
			return mobile;
		}
		mobile = answer.get("mobile").toString();
		return mobile.substring(0, 3) + "****" + mobile.substring(7);
	}
	
	private String getYourSex(Map<String, Object> answer) {
		String name = "保密";
		if (answer != null && !StrUtils.isNull(answer.get("traineeSex"))) {
			switch (answer.get("traineeSex").toString()) {
			case "1":
				name = "男";
				break;
			case "2":
				name = "女";
				break;
			default:
				break;
			}
		}
		return name;
	}
	
	/**
	 * 处理题目选项数据
	 * @param questionList
	 * @param optionList
	 */
	private void handleQuestionOptionData(List<Map<String, Object>> questionList, List<Map<String, Object>> optionList) {
		questionList.stream().forEach(question -> {
			question.put("questionTypeName", question.get("questionType"));
			if ("1".equals(question.get("questionType")) || "2".equals(question.get("questionType"))) {
				List<Map<String, Object>> children = new ArrayList<>();
				optionList.forEach(option -> {
					if (question.get("questionId").equals(option.get("questionId"))) {
						if (!children.contains(option)) {
							children.add(option);							
						}
					}
				});
				question.put("children", children);	
			}
		});
		// 字典转换
		convertUtil.convertDict(questionList, "questionTypeName", "activityQuestionType");
	}
	
	/**
	 * 计算多少人选择了某选项，占比百分多少
	 * @param question 题目信息
	 * @param children 题目下的选项信息
	 * @param traineeAnswerListFilter 此题目的回答
	 */
	private void countNum(Map<String, Object> question, List<Map<String, Object>> children, List<Map<String, Object>> traineeAnswerListFilter) {
		// 如果是单选或多选题
		if ("1".equals(question.get("questionType")) || "2".equals(question.get("questionType"))) {
			// 遍历选项
			for (int i = 0; i < children.size(); i++) {
				Map<String, Object> option = children.get(i);
				// 有多少人选择了此题目此选项
				int traineeAnswerNum = 0;
				for (Map<String, Object> traineeAnswer : traineeAnswerListFilter) {
					String optionId = (String) traineeAnswer.get("optionId");
					if (StrUtils.isNotEmpty(optionId)) {
						// 学员选择的题目选项
						String[] split = optionId.split(",");
						List<String> optionIdList = Stream.of(split).collect(Collectors.toList());
						if (optionIdList.contains(option.get("optionId"))) {
							traineeAnswerNum ++;
						}
					}
				}
				option.put("traineeAnswerNum", traineeAnswerNum);
				// 计算百分比,表示有多少人再此题目中选择了此选项
				BigDecimal percent = new BigDecimal("0");
				if (traineeAnswerListFilter.size() != 0) {
					percent = new BigDecimal(traineeAnswerNum).divide(new BigDecimal(traineeAnswerListFilter.size()), 2, BigDecimal.ROUND_HALF_UP);	
				}
				option.put("percent", percent.multiply(new BigDecimal("100")) + "%");
			}
		}
	}

	/**
	 * 查看学员针对投票/问卷的作答内容（单个结果）
	 * @param traineeId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewTraineeAnswerData(String ctId, String activityId, String traineeId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId)
			|| StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂，已无法查看活动");
		}
		boolean isCreator = StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getReceiverUserId());
		boolean isTeachingAssistant = loginUserId.equals(tevglTchClassroom.getTraineeId());
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 获取学员提交的内容
		params.put("ctId", ctId);
		params.put("activityId", activityId);
		if (StrUtils.isNotEmpty(traineeId)) {
			params.put("traineeId", traineeId);
		}
		List<Map<String, Object>> traineeAnswerList = tevglActivityVoteQuestionnaireTraineeAnswerMapper.selectListMapByMap(params);
		if (traineeAnswerList == null || traineeAnswerList.size() == 0) {
			return R.ok("此学员没有作答").put(Constant.R_DATA, null);
		}
		// 获取学员提交的附件
		List<Object> ids = traineeAnswerList.stream().map(a -> a.get("id")).collect(Collectors.toList());
		params.clear();
		params.put("ids", ids);
		params.put("traineeId", traineeId);
		List<Map<String, Object>> fileList = tevglActivityVoteQuestionnaireTraineeAnswerFileMapper.selectSimpleListMapByMap(params);
		// 获取投票问卷的题目
		params.clear();
		params.put("activityId", activityId);
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> questionList = tevglActivityVoteQuestionnaireQuestionMapper.selectSimpleListMapByMap(params);
		if (questionList == null || questionList.size() == 0) {
			return R.ok("已经找不到题目").put(Constant.R_DATA, null);
		}
		// 获取题目的选项
		List<String> questionIds = questionList.stream().map(a -> (String)a.get("questionId")).collect(Collectors.toList());
		// 是否展示具体谁投了票
		if (isCreator || isReceiver || isTeachingAssistant) {
			params.put("showPerson", "Y");
		}
		List<Map<String, Object>> optionList = getOptionListData(activityId, questionIds, traineeAnswerList, params);
		questionList.stream().forEach(question -> {
			// 选择题
			if ("1".equals(question.get("questionType")) || "2".equals(question.get("questionType"))) {
				// 学员此题目选择的选项
				List<String> optionIds = new ArrayList<>();
				// 取出题目的选项
				List<Map<String, Object>> children = optionList.stream().filter(a -> a.get("questionId").equals(question.get("questionId"))).collect(Collectors.toList());
				// 遍历取出学员该选项的作答
				children.stream().forEach(ch -> {
					traineeAnswerList.stream().forEach(traineeAnswerInfo -> {
						if (traineeAnswerInfo.get("optionId") != null) {
							if ("1".equals(question.get("questionType"))) {
								if (ch.get("optionId").equals(traineeAnswerInfo.get("optionId"))) {
									optionIds.add(ch.get("optionId").toString());
									ch.put("content", traineeAnswerInfo.get("content"));
								}
							} else { // 多选题，可能存的多个英文逗号隔开
								String string = traineeAnswerInfo.get("optionId").toString();
								String[] split = string.split(",");
								for (int i = 0; i < split.length; i++) {
									if (ch.get("optionId").equals(split[i])) {
										optionIds.add(split[i]);
										ch.put("content", traineeAnswerInfo.get("content"));
									}
								}
							}
						}
					});					
				});
				question.put("children", children);
				question.put("traineeSelectList", optionIds.stream().distinct().collect(Collectors.toList()));
			} else if ("3".equals(question.get("questionType"))) {
				// 取出学员这道简答题的的回答
				Map<String, Object> traineeAnswerData = new HashMap<>();
				List<Map<String,Object>> collect = traineeAnswerList.stream().filter(a -> a.get("questionId").equals(question.get("questionId"))).collect(Collectors.toList());
				if (collect != null && collect.size() > 0) {
					List<Map<String, Object>> traineeAnswerFileList = new ArrayList<>();
					for (Map<String, Object> file : fileList) {
						if (file.get("id") != null) {
							if (file.get("id").equals(collect.get(0).get("id"))) {
								String fileUrl = file.get("url").toString();
								file.put("url", uploadPathUtils.stitchingPath(file.get("url"), "20"));
								file.put("type", getFileType(fileUrl));	
								file.put("fileType", file.get("fileType"));
								traineeAnswerFileList.add(file);
							}	
						}
					}
					traineeAnswerData.put("fileList", traineeAnswerFileList); // 简答题提交的附件
					traineeAnswerData.put("content", collect.get(0).get("content")); // 简答题提交的内容
				}
				question.put("traineeAnswerData", traineeAnswerData);
			}
		});
		return R.ok().put(Constant.R_DATA, questionList);
	}

	/**
	 * 修改投票问卷（优化版）
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	public R updateVoteQuestionnaireInfoNew(JSONObject jsonObject, String loginUserId) {
		// 主键
		String activityId = jsonObject.getString("activityId");
		// 第一步，合法性校验
		R r = checkIsPass(jsonObject, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		String pkgId = jsonObject.getString("pkgId");
		String chapterId = jsonObject.getString("chapterId");
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			if ("3".equals(tevglTchClassroom.getClassroomState())) {
				return R.error("课堂已结束，无法修改活动");
			}
			// 权限判断，用于课堂创建者与课堂助教
			boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_ACT_EDIT);
			if (!hasOperationBtnPermission) {
				return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
			}
		} else {
			// 权限校验，用于教学包创建者与章节共建者
			if (!pkgPermissionUtils.hasPermissionAv2(pkgId, loginUserId, chapterId)) {
				return R.error("暂无权限，操作失败");
			}
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的教学包记录");
		}
		// 调用方法，是否需要生成默认【活动】分组标签
		toCreateDefaultActivityTab(tevglPkgInfo, jsonObject.getString("chapterId"), loginUserId);
		// 题目和选项信息
		String json = jsonObject.getString("json");
		JSONArray jsonArray = JSON.parseArray(json);
		// 第二步，填充投票问卷实体对象，并入库
		TevglActivityVoteQuestionnaire vo = fillVotoInfo(jsonObject, loginUserId);
		vo.setActivityId(activityId);
		vo.setQuestionNumber(jsonArray.size());
		tevglActivityVoteQuestionnaireMapper.update(vo);
		// 第三步，处理题目与选项信息
		handlQuestionAndOption(activityId, loginUserId, jsonArray);
		return R.ok("保存成功");
	}

	/**
	 * 第三步，处理题目与选项信息
	 * @param activityId 活动
	 * @param loginUserId 当前登录用户
	 * @param jsonArray 用户录入的数据
	 */
	private void handlQuestionAndOption(String activityId, String loginUserId, JSONArray jsonArray){
		List<TevglActivityVoteQuestionnaireQuestionOption> oldOptionList = new ArrayList<>();
		// 先查询该活动原先存于数据库中的题目
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", activityId);
		List<TevglActivityVoteQuestionnaireQuestion> existedVoteQuestionnaireQuestionList = tevglActivityVoteQuestionnaireQuestionMapper.selectListByMap(params);
		List<String> questionIds = existedVoteQuestionnaireQuestionList.stream().map(a -> a.getQuestionId()).collect(Collectors.toList());
		if (questionIds != null && questionIds.size() > 0) {
			// 再查出题目对应的选项
			params.clear();
			params.put("questionIds", questionIds);
			oldOptionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectListByMap(params);
		}
		// 存旧的
		List<String> oldQuestionIds = new ArrayList<String>();
		// 遍历题目
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject questionObject = jsonArray.getJSONObject(i);
			String tempQuestionId = questionObject.getString("questionId");
			String questionType = questionObject.getString("questionType");
			// 如果更换了题型，则需要把冗余的选项给干掉
			hasChangeQuestionType(tempQuestionId, questionType, existedVoteQuestionnaireQuestionList, oldOptionList);
			// 题目基本信息入库（注意点：处理更换题型的情况）
			String questionId = toSaveQuestionInfo(questionObject, activityId, i, loginUserId, oldQuestionIds);
			// 题目选项信息
			JSONArray children = questionObject.getJSONArray("children");
			// 取出此题目对应的选项
			List<TevglActivityVoteQuestionnaireQuestionOption> thisQuestionOptionList = oldOptionList.stream().filter(a -> a.getQuestionId().equals(questionId)).collect(Collectors.toList());
			// 题目选项更新入库
			doOptionChange(children, thisQuestionOptionList, questionId);
		}
		// 额外处理,删除某种情况下产出的冗余数据
		questionIds.removeAll(oldQuestionIds);
		if (questionIds != null && questionIds.size() > 0) {
			for (String string : questionIds) {
				// 删除冗余选项
				tevglActivityVoteQuestionnaireQuestionOptionMapper.deleteByQuestionId(string);
			}
			// 删除冗余题目
			tevglActivityVoteQuestionnaireQuestionMapper.deleteBatch(questionIds.stream().toArray(String[]::new));
		}

	}

	/**
	 * 如果更换了题型，则需要把冗余的选项给干掉
	 * @param inputQuestionId 前端，传入的某个题目
	 * @param questionType 对应的题目类型
	 * @param existedVoteQuestionnaireQuestionList 已存在于数据库中的旧题目
	 * @param oldOptionList 旧选项们
	 */
	private void hasChangeQuestionType(String inputQuestionId, String questionType, List<TevglActivityVoteQuestionnaireQuestion> existedVoteQuestionnaireQuestionList, List<TevglActivityVoteQuestionnaireQuestionOption> oldOptionList ){
		if (StrUtils.isNotEmpty(inputQuestionId)) {
			List<TevglActivityVoteQuestionnaireQuestion> collect = existedVoteQuestionnaireQuestionList.stream().filter(a -> a.getQuestionId().equals(inputQuestionId)).collect(Collectors.toList());
			// 如果更换了题型，且由选择题变为了简答题
			if (!questionType.equals(collect.get(0).getQuestionType()) 
					&& Arrays.asList("1", "2").contains(collect.get(0).getQuestionType())
					&& "3".equals(questionType)) {
				// 那么先删除原有选项
				List<TevglActivityVoteQuestionnaireQuestionOption> needDelOptionList = oldOptionList.stream().filter(a -> a.getQuestionId().equals(inputQuestionId)).collect(Collectors.toList());
				log.debug("等待被删除的冗余题目选项有：" + needDelOptionList.size());
				if (needDelOptionList != null && needDelOptionList.size() > 0) {
					List<String> needDelOptionIdList = needDelOptionList.stream().map(a -> a.getOptionId()).collect(Collectors.toList());
					tevglActivityVoteQuestionnaireQuestionOptionMapper.deleteBatch(needDelOptionIdList.stream().toArray(String[]::new));
				}
			}
		}
	}

	/**
	 * 题目基本信息入库
	 * @param questionObject
	 * @param activityId
	 * @param i
	 * @param loginUserId
	 * @param oldQuestionIds
	 * @return
	 */
	private String toSaveQuestionInfo(JSONObject questionObject, String activityId, int i, String loginUserId, List<String> oldQuestionIds){
		String questionId = questionObject.getString("questionId");
		String questionName = questionObject.getString("questionName");
		String questionType = questionObject.getString("questionType");
		Integer questionSortNum = questionObject.getInteger("sortNum");
		String tips = questionObject.getString("tips");
		TevglActivityVoteQuestionnaireQuestion question = new TevglActivityVoteQuestionnaireQuestion();
		question.setActivityId(activityId);
		question.setQuestionType(questionType);
		question.setQuestionName(questionName);
		question.setSortNum(questionSortNum);
		question.setSortNum(i);
		question.setTips(tips);
		question.setState("Y");
		// 说明这个是新添加题目
		if (StrUtils.isEmpty(questionId)) {
			question.setQuestionId(Identities.uuid());
			question.setCreateTime(DateUtils.getNowTimeStamp());
			question.setCreateUserId(loginUserId);
			tevglActivityVoteQuestionnaireQuestionMapper.insert(question);
		} else {
			// 加入集合
			oldQuestionIds.add(questionId);
			// 更新入库
			question.setQuestionId(questionId);
			question.setCreateUserId(loginUserId);
			question.setUpdateTime(DateUtils.getNowTimeStamp());
			tevglActivityVoteQuestionnaireQuestionMapper.update(question);
		}
		return question.getQuestionId();
	}

	/**
	 * 第二步，填充对象信息，并返回对象
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	private TevglActivityVoteQuestionnaire fillVotoInfo(JSONObject jsonObject, String loginUserId){
		String pkgId = jsonObject.getString("pkgId");
		String chapterId = jsonObject.getString("chapterId");
		String activityTitle = jsonObject.getString("activityTitle");
		String resgroupId = GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE;
		String isShow = jsonObject.getString("isShow");
		String purpose = jsonObject.getString("use");
		TevglActivityVoteQuestionnaire vo = new TevglActivityVoteQuestionnaire();
		vo.setActivityId(Identities.uuid());
		vo.setActivityTitle(activityTitle);
		vo.setCreateTime(DateUtils.getNowTimeStamp());
		vo.setCreateUserId(loginUserId);
		vo.setResgroupId(resgroupId);
		vo.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		vo.setActivityState("0"); // 0未开始1进行中2已结束
		vo.setState("Y"); // Y有效N无效
		vo.setActivityType(GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		vo.setAnswerNumber(0);
		vo.setIsShow(isShow);
		vo.setPurpose(purpose);
		vo.setChapterId(chapterId);
		// 排序号处理
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("pkgId", pkgId);
		ps.put("resgroupId", resgroupId);
		Integer sortNum = tevglActivityVoteQuestionnaireMapper.getMaxSortNum(ps);
		vo.setSortNum(sortNum);
		return vo;
	}

	/**
	 * 去生成默认的分组标签
	 * @param tevglPkgInfo
	 * @param chapterId
	 * @param loginUserId
	 */
	private void toCreateDefaultActivityTab(TevglPkgInfo tevglPkgInfo, String chapterId, String loginUserId){
		if (tevglPkgInfo == null) {
			log.debug("无法生成，默认【活动】分组标签");
			return;
		}
		// 判断此章节是否已经生成了[活动]分组,如果没有,则生成
		String refPkgId = tevglPkgInfo.getPkgId();
		if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
			refPkgId = tevglPkgInfo.getRefPkgId();
		}
		pkgUtils.createDefaultActivityTab(refPkgId, chapterId, loginUserId);
	}

	/**
	 * 第一步，检验数据格式是否合法
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	private R checkIsPass(JSONObject jsonObject, String loginUserId){
		String activityId = jsonObject.getString("activityId");
		String pkgId = jsonObject.getString("pkgId");
		String activityTitle = jsonObject.getString("activityTitle");
		// 题目和选项信息
		String json = jsonObject.getString("json");
		if (StrUtils.isEmpty(activityId) ||StrUtils.isEmpty(pkgId)
				|| StrUtils.isEmpty(activityTitle)
				|| StrUtils.isEmpty(loginUserId)) {
			return R.error("参数为空");
		}
		activityTitle = activityTitle.trim();
		if (StrUtils.isEmpty(activityTitle)) {
			return R.error("标题不能为空");
		}
		if (activityTitle.length() > 100) {
			return R.error("标题过长，不能超过100个字符");
		}
		if (StrUtils.isEmpty(json)) {
			return R.error("题目和选项数据获取失败");
		}
		TevglActivityVoteQuestionnaire activityInfo = tevglActivityVoteQuestionnaireMapper.selectObjectById(activityId);
		if (activityInfo == null) {
			return R.error("无效的记录");
		}
		if ("1".equals(activityInfo.getActivityState())) {
			return R.error("活动已开始，无法修改");
		}
		if ("2".equals(activityInfo.getActivityState())) {
			return R.error("活动已结束，无法修改");
		}
		return R.ok();
	}


}
