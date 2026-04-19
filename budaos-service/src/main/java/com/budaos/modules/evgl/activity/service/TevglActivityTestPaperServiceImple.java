package com.budaos.modules.evgl.activity.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.evgl.activity.api.TevglActivityTestPaperService;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.domain.TevglBookSubperiod;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubperiodMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.question.domain.*;
import com.budaos.modules.evgl.question.persistence.*;
import com.budaos.modules.evgl.question.service.TevglQuestionsInfoServiceImpl;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.im.core.entity.MsgType;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试活动
 * @author huj
 *
 */
@Service
public class TevglActivityTestPaperServiceImple implements TevglActivityTestPaperService {
	
	private Logger log = LoggerFactory.getLogger(TevglActivityBrainstormingServiceImpl.class);
	
	@Autowired
	private TepExaminePaperInfoMapper tepExaminePaperInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglQuestionsInfoMapper tevglQuestionsInfoMapper;
	@Autowired
	private TevglQuestionChoseMapper tevglQuestionChoseMapper;
	@Autowired
	private TepExaminePaperQuestionsDetailMapper tepExaminePaperQuestionsDetailMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TepExamineDynamicPaperMapper tepExamineDynamicPaperMapper;
	@Autowired
	private TepExamineHistoryPaperMapper tepExamineHistoryPaperMapper;
	@Autowired
	private TepExaminePaperQuestionsOptionRandomMapper tepExaminePaperQuestionsOptionRandomMapper;
	@Autowired
	private TepExaminePaperQuestionsRandomMapper tepExaminePaperQuestionsRandomMapper;
	@Autowired
	private TepExaminePaperScoreMapper tepExaminePaperScoreMapper;
	@Autowired
	private TepExaminePaperScoreGapfillingMapper tepExaminePaperScoreGapfillingMapper;
	@Autowired
	private TevglQuestionsInfoServiceImpl tevglQuestionsInfoServiceImpl;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	
	// 文件上传路径
	@Value("${com.budaos.file-upload-path}")
    private String uploadPath;
	// 文件显示路径
	@Value("${com.budaos.file-access-path}")
	private String accessPath;

	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private PkgActivityUtils pkgActivityUtils;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	
	/**
	 * 保存
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	public R saveTestPaperInfo(HttpServletRequest request, JSONObject jsonObject, String loginUserId, String traineeType) {
		traineeType = StrUtils.isEmpty(traineeType) ? "4" : traineeType;
		// 教学包
		String pkgId = jsonObject.getString("pkgId");
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(traineeType)) {
			return R.error("必传参数为空");
		}
		// 用户手动录入的题目，数据格式示例 
		JSONArray questionsData = jsonObject.getJSONArray("questionsData");
		if (questionsData == null || questionsData.size() == 0) {
			return R.error("没有获取到题目数据，请先从题库选择题目、或者自行录入题目");
		}
		// 章节
		String chapterId = jsonObject.getString("chapterId"); 
		// 当前选择的所有题目id,多个以英文逗号分隔
		String questions = jsonObject.getString("questions");
		// 判断题和选择题的总分
		String totalScore = jsonObject.getString("totalScore");
		// 单选题题分值（即每答对一题可得多少分）
		String singleChoiceScore = jsonObject.getString("singleChoiceScore");
		// 多选题
		String multipleChoiceScore = jsonObject.getString("multipleChoiceScore");
		// 判断题分值（即每答对一题可得多少分）
		String judgeScore = jsonObject.getString("judgeScore");
		// 问答题分值
		String shortAnswerScore = jsonObject.getString("shortAnswerScore");
		// 填空题分值
		String gapFillingScore = jsonObject.getString("gapFilling");
		// 填空题得分规则
		String gapFillingScoreStandard = jsonObject.getString("gapFillingScoreStandard");
		// 复合题每小题分值
		String compositeScore = jsonObject.getString("compositeScore");
		// 试卷名称（活动名称）
		String paperName = jsonObject.getString("paperName");
		// 答题时长（约束时间）单位分钟 0不限时
		String answerTime = jsonObject.getString("answerTime");
		// 是否随机(Y是N否),如果开启,则每个学员答题时的试卷中，题目顺序是不一致的
		String paperIsRandom = jsonObject.getString("paperIsRandom");
		// 重做次数(-1不限制0不允许重做1对应允许重做一次，依此类推)
		Integer redoNum = jsonObject.getInteger("redoNum");
		// 查看答案时机（1测试活动结束之后查看2交卷之后查看答案）
		String viewResultTime = jsonObject.getString("viewResultTime");
		// 所属分组
		// String resgroupId = jsonObject.getString("resgroupId");
		// 用途
		String purpose = jsonObject.getString("purpose");
		// 合法性校验
		R r = checkIsPass(pkgId, questions, paperName, answerTime);
		if (!r.get("code").equals(0)) {
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
		TepExaminePaperInfo t = new TepExaminePaperInfo();
		String activityId = Identities.uuid();
		t.setPaperId(activityId);
		t.setPaperName(paperName);
		t.setPaperTotalScore(totalScore);
		t.setSingleChoiceScore(singleChoiceScore);
		t.setMultipleChoiceScore(multipleChoiceScore);
		t.setJudgeScore(judgeScore);
		t.setShortAnswerScore(shortAnswerScore);
		t.setPurpose(purpose);
		t.setCompositeScore(compositeScore);
		// 试卷类型
		if (traineeType.equals("4") || traineeType.equals("2")) {
			t.setPaperType("1");
		} else {
			t.setPaperType("2");
		}
		t.setPaperConfinementTime(answerTime);
		t.setPaperPracticeTime(0);
		t.setPaperIsRandom(paperIsRandom);
		t.setRedoNum(redoNum);
		t.setViewResultTime(viewResultTime);
		t.setChapterId(chapterId);
		t.setResgroupId(GlobalActivity.ACTIVITY_4_TEST_ACT);
		t.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_4_TEST_ACT);
		t.setCreateUserId(loginUserId);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setQuestionNumber(questionsData == null ? 0 : questionsData.size());
		t.setPaperState("Y");
		t.setGapFilling(gapFillingScore);
		t.setGapFillingScoreStandard(gapFillingScoreStandard);
		t = countNums(questionsData, t);
		// 设置职业路径和课程体系
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(pkgInfo.getSubjectId());
		if (tevglBookSubject != null) {
			String subjectRef = tevglBookSubject.getSubjectRef();
			if (StrUtils.isNotEmpty(subjectRef)) {
				Map<String, Object> map = new HashMap<>();
				map.put("subjectId", subjectRef);
				List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
				String majorIds = list.stream().map(a -> a.getMajorId()).distinct().collect(Collectors.joining(","));
				t.setMajorId(majorIds);
				t.setSubjectId(subjectRef);
			}
		}
		// 保存试卷基本数据
		tepExaminePaperInfoMapper.insert(t);
		// 保存用户手动录入的题目并建立试卷与题目的关系
		doSaveUserInputQuestionList(questionsData, pkgInfo.getSubjectId(), activityId, singleChoiceScore, multipleChoiceScore, judgeScore, shortAnswerScore, gapFillingScore, compositeScore, loginUserId);
		// 从session移除数据
		request.getSession().removeAttribute("temporarySaveSession");
		// 保存教学包与活动之间的关系
		pkgUtils.buildRelation(pkgId, activityId, GlobalActivity.ACTIVITY_4_TEST_ACT);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(pkgId);
		return R.ok("活动[测试活动] 创建成功").put(Constant.R_DATA, activityId);
	}
	
	/**
	 * 填充不同题型数量
	 * @param questionsData
	 * @param t
	 * @return
	 */
	private TepExaminePaperInfo countNums(JSONArray questionsData, TepExaminePaperInfo t) {
		// 各种题型的数量
		Integer singleChoiceNum = 0;
		Integer multipleChoiceNum = 0;
		Integer judgeNum = 0;
		Integer shortAnswerNum = 0;
		Integer gapFillingNum = 0;
		Integer compositeNum = 0;
		if (questionsData == null || questionsData.size() == 0) {
			t.setSingleChoiceNum(singleChoiceNum);
			t.setMultipleChoiceNum(multipleChoiceNum);
			t.setJudgeNum(judgeNum);
			t.setShortAnswerNum(shortAnswerNum);
			t.setGapFillingNum(gapFillingNum);
			t.setCompositeNum(compositeNum);
			return t;
		}
		// 计算不同题型题目数量
		for (int i = 0; i < questionsData.size(); i++) {
			JSONObject questionInfo = questionsData.getJSONObject(i);
			String questionsType = questionInfo.getString("questionsType");
			if ("1".equals(questionsType)) {
				singleChoiceNum ++;
			}
			if ("2".equals(questionsType)) {
				multipleChoiceNum ++;
			}
			if ("3".equals(questionsType)) {
				judgeNum ++;
			}
			if ("4".equals(questionsType)) {
				shortAnswerNum ++;
			}
			if ("5".equals(questionsType)) {
				gapFillingNum ++;
			}
			if ("6".equals(questionsType)) {
				compositeNum ++;
			}
		}
		t.setSingleChoiceNum(singleChoiceNum);
		t.setMultipleChoiceNum(multipleChoiceNum);
		t.setJudgeNum(judgeNum);
		t.setShortAnswerNum(shortAnswerNum);
		t.setGapFillingNum(gapFillingNum);
		t.setCompositeNum(compositeNum);
		return t;
	}
	
	/**
	 * 保存用户手动录入的题目，并建立与试卷的关系
	 * @param questionsData
	 * @param subjectId 
	 * @param activityId 活动（试卷）id
	 * @param singleChoiceScore 单选题分值
	 * @param multipleChoiceScore 多选题分值
	 * @param judgeScore 判断题分值
	 * @param shortAnswerScore 简答题分值
	 * @param gapFillingScore 填空题每空分值
	 * @param compositeScore 复合题每小题得分
	 * @param loginUserId
	 */
	private void doSaveUserInputQuestionList(JSONArray questionsData, String subjectId, String activityId, String singleChoiceScore, String multipleChoiceScore, 
			String judgeScore, String shortAnswerScore, String gapFillingScore, String compositeScore, String loginUserId) {
		if (questionsData == null) {
			return;
		}
		String sbId = null;
		String majorId = null;
		// 找到当前课堂对应的源课程，从而拿到职业路径，为题目填充对应值
		TevglBookSubject bookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (bookSubject != null) {
			sbId = bookSubject.getSubjectRef();
			Map<String, Object> map = new HashMap<>();
			map.put("subjectId", bookSubject.getSubjectRef());
			List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
			if (list != null && list.size() > 0) {
				majorId = list.get(0).getMajorId();
			}
		}
		Map<String, Object> params = new HashMap<>();
		// 记录各种题型的个数，计算总分
		int singleNum = 0;
		int multipleNum = 0;
		int judgeNum = 0;
		int shortNum = 0;
		// 填空题目的填空总个数
		int totalGapFillingSpaceNum = 0;
		BigDecimal value5 = new BigDecimal("0");
		// 所有复合题的总分
		BigDecimal value6 = new BigDecimal("0");
		// 等待入库的数据
		List<TepExaminePaperQuestionsDetail> insertPaperQuesDetailList = new ArrayList<>();
		// 等待更新的数据
		List<TevglQuestionsInfo> updateQuestionList = new ArrayList<>();
		// 题目
		for (int i = 0; i < questionsData.size(); i++) {
			// [{"questionsName": "", "questionsParse": "", "questionsComplexity": "", "questionsType": "", "optionList": [{"code": "", "content": "", "isRight": "YES/NO"}]}]
			JSONObject questionInfo = questionsData.getJSONObject(i);
			// 题目id 分为两种情况，有值表示是从题库选择过来，没有值则表示是用户自己添加的
			String questionsId = questionInfo.getString("questionsId");
			String questionsType = questionInfo.getString("questionsType");
			JSONArray optionList = questionInfo.getJSONArray("optionList");
			String showType = questionInfo.getString("showType");
			if ("5".equals(questionsType) && optionList != null && optionList.size() > 0) {
				totalGapFillingSpaceNum = optionList.size();
			}
			// 子题目
			List<String> questionIdList = new ArrayList<String>();
			// 1首先判断下这个是不是复合题
			if (StrUtils.isNotEmpty(questionsId) && "6".equals(questionsType)) {
				params.put("parentId", questionsId);
				List<TevglQuestionsInfo> list = tevglQuestionsInfoMapper.selectListByMap(params);
				if (list != null && list.size() > 0) {
					questionIdList = list.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList());
				}
			}
			// 2没有值则表示是用户自己添加的
			if (StrUtils.isEmpty(questionsId)) {
				log.debug("============================用户手动录入题目 begin ===================================");
				if ("6".equals(questionsType)) {
					questionInfo.put("majorId", majorId);
					questionInfo.put("subjectId", sbId);
					questionInfo.put("fromType", "1"); // 为空表示是在题库录入的题目，值为1表示是测试活动那边录入的
					R r = tevglQuestionsInfoServiceImpl.saveCompositeQuestionInfo(questionInfo, loginUserId);
					log.debug("复合题新增结果：" + r);
					if (r.get("code").equals(0)) {
						questionsId = r.get(Constant.R_DATA).toString();
						questionIdList = (List<String>) r.get("questionIdList");
					}
				} else {
					// 保存新题目
					questionsId = doSaveNewQuestionInfo(questionInfo, activityId, sbId, majorId, loginUserId);					
				}
				log.debug("用户手动录入了一个题目：" + questionsId);
				log.debug("============================用户手动录入题目 end ===================================");
			}
			// 3有值表示是从题库选择过来
			if (StrUtils.isNotEmpty(questionsId)) {
				// 试卷与题目的关系
				TepExaminePaperQuestionsDetail questionDetail = new TepExaminePaperQuestionsDetail();
				questionDetail.setDetailId(Identities.uuid());
				questionDetail.setPaperId(activityId);
				questionDetail.setQuestionsId(questionsId);
				questionDetail.setQuestionsNumber(i + 1);
				if ("1".equals(questionsType)) {
					questionDetail.setQuestionsScore(singleChoiceScore);
					singleNum ++;
				}
				// 选择题设置分值
				if ("2".equals(questionsType)) {
					questionDetail.setQuestionsScore(multipleChoiceScore);
					multipleNum ++;
				}
				// 判断题分值
				if ("3".equals(questionsType)) {
					questionDetail.setQuestionsScore(judgeScore);
					judgeNum ++;
				}
				// 问答分值
				if ("4".equals(questionsType)) {
					questionDetail.setQuestionsScore(shortAnswerScore);
					shortNum ++;
				}
				// 填空题分值
				if ("5".equals(questionsType)) {
					questionDetail.setQuestionsScore(gapFillingScore);
					// 填空的总分
					value5 = value5.add(new BigDecimal(gapFillingScore).multiply(new BigDecimal(totalGapFillingSpaceNum)));
				}
				// 复合题分值
				if ("6".equals(questionsType)) {
					// 子题目数量
					JSONArray array = questionInfo.getJSONArray("questionList");
					String compositeTotalScore = array == null ? "0" : new BigDecimal(array.size()).multiply(new BigDecimal(compositeScore)).toString();
					// 设置本主题目总分
					questionDetail.setQuestionsScore(compositeTotalScore);
					// 设置子题目分值
					for (int j = 0; j < questionIdList.size(); j++) {
						// 试卷与题目的关系
						TepExaminePaperQuestionsDetail t = new TepExaminePaperQuestionsDetail();
						t.setDetailId(Identities.uuid());
						t.setPaperId(activityId);
						t.setQuestionsId(questionIdList.get(j));
						t.setQuestionsNumber(i + 1);
						t.setQuestionsScore(compositeScore);
						t.setParentId(questionsId);
						insertPaperQuesDetailList.add(t);
					}
					value6 = value6.add(new BigDecimal(compositeTotalScore));
				}
				// 入库
				tepExaminePaperQuestionsDetailMapper.insert(questionDetail);
				// 组卷数++
				TevglQuestionsInfo questionsInfo = new TevglQuestionsInfo();
				questionsInfo.setQuestionsId(questionsId);
				questionsInfo.setQuestionsConstructingNum(1);
				questionsInfo.setShowType(showType);
				updateQuestionList.add(questionsInfo);
			}
		}
		// 批量新增
		if (insertPaperQuesDetailList.size() > 0) {
			tepExaminePaperQuestionsDetailMapper.insertBatch(insertPaperQuesDetailList);
		}
		// 批量更新
		if (updateQuestionList.size() > 0) {
			tevglQuestionsInfoMapper.plusNumBatchByCaseWhen(updateQuestionList);
		}
		//
		// 记录各种题型的个数，计算总分
		BigDecimal value1 = new BigDecimal(singleNum).multiply(new BigDecimal(singleChoiceScore));
		BigDecimal value2 = new BigDecimal(multipleNum).multiply(new BigDecimal(multipleChoiceScore));
		BigDecimal value3 = new BigDecimal(judgeNum).multiply(new BigDecimal(judgeScore));
		BigDecimal value4 = new BigDecimal(shortNum).multiply(new BigDecimal(shortAnswerScore));
		//BigDecimal value5 = new BigDecimal(gapFillingNum).multiply(new BigDecimal(gapFillingScore)).multiply(new BigDecimal(totalGapFillingSpaceNum));
		BigDecimal totalValue = value1.add(value2).add(value3).add(value4).add(value5).add(value6);
		// 重置填空题的填空个数
		totalGapFillingSpaceNum = 0;
		// 更新试卷总分
		TepExaminePaperInfo pp = new TepExaminePaperInfo();
		pp.setPaperId(activityId);
		pp.setPaperTotalScore(totalValue.toString());
		tepExaminePaperInfoMapper.update(pp);
	}
	
	/**
	 * 保存手动录入的题目信息
	 * @param questionInfo [{"questionsName": "", "questionsParse": "", "questionsComplexity": "", "questionsType": "", "optionList": [{"code": "", "content": "", "isRight": "YES/NO"}]}]
	 * @param activityId
	 * @param sbId
	 * @param majorId
	 * @param loginUserId
	 * @return
	 */
	private String doSaveNewQuestionInfo(JSONObject questionInfo, String activityId, String sbId, String majorId, String loginUserId) {
		String questionsName = questionInfo.getString("questionsName");
		String questionsParse = questionInfo.getString("questionsParse");
		String questionsComplexity = questionInfo.getString("questionsComplexity");
		String questionsType = questionInfo.getString("questionsType");
		// 0不显示1显示 从测试活动录入的题目，也就是from_type为1时，根据此值是否在题库显示
		String showType = questionInfo.getString("showType");
		showType = StrUtils.isEmpty(showType) ? "1" : showType;
		// 题目选项数据格式
		// [{"code": "", "content": "", "isRight": "YES/NO"}]
		JSONArray optionList = questionInfo.getJSONArray("optionList");
		// 填充基本信息
		String questionsId = Identities.uuid();
		TevglQuestionsInfo t = new TevglQuestionsInfo();
		t.setQuestionsId(questionsId);
		t.setMajorId(majorId);
		t.setSubjectId(sbId);
		t.setQuestionsName(questionsName);
		t.setQuestionsParse(questionsParse);
		t.setQuestionsComplexity(questionsComplexity);
		t.setQuestionsType(questionsType);
		t.setQuestionsStar("0");
		t.setQuestionsConstructingNum(0);
		t.setQuestionsAccuracy(new BigDecimal("0"));
		t.setQuestionsStoreNum(0);
		t.setQuestionsAnswerNum(0);
		t.setQuestionsCorrectNum(0);
		t.setQuestionsErrorNum(0);
		t.setCreateUserId(loginUserId);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setQuestionsState("Y");
		t.setFromType("1");
		t.setShowType(showType);
		tevglQuestionsInfoMapper.insert(t);
		// 保存题目选项
		doSaveOptionInfo(questionsId, optionList);
		return t.getQuestionsId();
	}
	
	/**
	 * 保存题目选项
	 * @param questionsId
	 * @param optionList [{"code": "", "content": "", "isRight": "YES/NO"}]
	 */
	private void doSaveOptionInfo(String questionsId, JSONArray optionList) {
		if (StrUtils.isEmpty(questionsId) || optionList == null || optionList.size() == 0) {
			return;
		}
		try {
			// 待保存的题目选项
			List<TevglQuestionChose> list = new ArrayList<TevglQuestionChose>();
			// 存放正确答案,即选项ID
			String ids = "";
			for (int i = 0; i < optionList.size(); i++) {
				JSONObject jsonObject = optionList.getJSONObject(i);
				String code = jsonObject.getString("code"); // 选项编码
				String content = jsonObject.getString("content"); // 选项内容
				String isRight = jsonObject.getString("isRight"); // 是否正确
				// 填充选项
				TevglQuestionChose tevglQuestionChose = new TevglQuestionChose();
				tevglQuestionChose.setOptionId(Identities.uuid()); // 选项ID
				tevglQuestionChose.setQuestionsId(questionsId); // 题目ID
				tevglQuestionChose.setCode(code); // 选项编码
				tevglQuestionChose.setContent(content); // 选项内容
				tevglQuestionChose.setState("Y"); // 状态Y有效N无效
				tevglQuestionChose.setSortNum(i);
				if ("YES".equals(isRight) || "true".equals(isRight) || "Y".equals(isRight)) {
					ids += tevglQuestionChose.getOptionId()+",";
				}
				list.add(tevglQuestionChose);
			}
			if (StrUtils.isNotEmpty(ids)) {
				// 更新题目的正确答案
				String optionIds =  ids.substring(0, ids.length()-1);
				TevglQuestionsInfo tevglQuestionsInfo = new TevglQuestionsInfo();
				tevglQuestionsInfo.setQuestionsId(questionsId);
				tevglQuestionsInfo.setReplyIds(optionIds);
				tevglQuestionsInfoMapper.update(tevglQuestionsInfo);
			}
			// 题目选项入库
			if (list != null && list.size() > 0) {
				tevglQuestionChoseMapper.insertBatch(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("题目选项数据保存失败", e);
		}
	}
	
	/**
	 * 合法性校验
	 * @param pkgId
	 * @param questions
	 * @param paperName
	 * @param answerTime
	 * @return
	 */
	private R checkIsPass(String pkgId, String questions, String paperName, String answerTime) {
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(paperName)) {
			return R.error("标题不能为空");
		}
		paperName = paperName.trim();
		if (StrUtils.isEmpty(paperName)) {
			return R.error("标题不能为空");
		}
		if (paperName.length() > 32) {
			return R.error("标题名称过长，不能超过32个字符");
		}
		if (StrUtils.isEmpty(answerTime)) {
			return R.error("请设置答题时长");
		}
		return R.ok();
	}

	/**
	 * 修改测试活动
	 * @author zyl加
	 * @data 2020年11月23日
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R updateTestPaperInfo(JSONObject jsonObject, String loginUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 试卷ID(活动ID)
		String paperId = jsonObject.getString("paperId");
		// 教学包
		String pkgId = jsonObject.getString("pkgId");
		if (StrUtils.isEmpty(pkgId) && StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		params.put("activityId", paperId);
		params.put("pkgId", pkgId);
		// 根据活动id和教学包id查询测试活动信息
		TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectObjectByIdAndPkgId(params);
		if (paperInfo == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if (paperInfo.getPaperState().equals("N")) {
			return R.error("此活动无效，无法修改");
		}
		
		if ("1".equals(paperInfo.getActivityStateActual())) {
			return R.error("活动已开始，不能修改");
		}
		if ("2".equals(paperInfo.getActivityStateActual())) {
			return R.error("活动已结束，不能修改");
		}
		
		// 用户手动录入的题目，数据格式示例 
		JSONArray questionsData = jsonObject.getJSONArray("questionsData");
		if (questionsData == null || questionsData.size() == 0) {
			return R.error("没有获取到题目数据，请先从题库选择题目、或者自行录入题目");
		}
		//章节
		String chapterId = jsonObject.getString("chapterId");
		//当前要修改测试活动的所有题目，多个以英文逗号隔开
		String questions = jsonObject.getString("questions");
		// 判断题和选择题的总分
		String totalScore = jsonObject.getString("totalScore");
		// 单选题分值
		String singleChoiceScore = jsonObject.getString("singleChoiceScore");
		// 多选题分值
		String multipleChoiceScore = jsonObject.getString("multipleChoiceScore");
		// 判断题分值（即每答对一题可得多少分）
		String judgeScore = jsonObject.getString("judgeScore");
		// 问答题分值
		String shortAnswerScore = jsonObject.getString("shortAnswerScore");
		// 填空题分值
		String gapFillingScore = jsonObject.getString("gapFilling");
		// 填空题得分规则
		String gapFillingScoreStandard = jsonObject.getString("gapFillingScoreStandard");
		// 复合题每小题分值
		String compositeScore = jsonObject.getString("compositeScore");	
		// 试卷名称（活动名称）
		String paperName = jsonObject.getString("paperName");
		// 答题时长（约束时间）单位分钟
		String answerTime = jsonObject.getString("answerTime");
		// 是否随机(Y是N否),如果开启,则每个学员答题时的试卷中，题目顺序是不一致的
		String paperIsRandom = jsonObject.getString("paperIsRandom");
		// 重做次数(-1不限制0不允许重做1对应允许重做一次，依此类推)
		Integer redoNum = jsonObject.getInteger("redoNum");
		// 查看答案时机（1测试活动结束之后查看2交卷之后查看答案）
		String viewResultTime = jsonObject.getString("viewResultTime");
		// 所属分组(来源字典，严格按照0未分组活动1投票问卷2头脑风暴3答疑讨论4课堂考核（包含测试活动、实践考核）5作业/小组任务6课堂表现7轻直播/讨论8课堂签到)
		String resgroupId = GlobalActivity.ACTIVITY_4_TEST_ACT; //jsonObject.getString("resgroupId");
		// 用途
		String purpose = jsonObject.getString("purpose");
		// 各种题型的数量
		/*Integer singleChoiceNum = jsonObject.getInteger("singleChoiceNum");
		Integer multipleChoiceNum = jsonObject.getInteger("multipleChoiceNum");
		Integer judgeNum = jsonObject.getInteger("judgeNum");
		Integer shortAnswerNum = jsonObject.getInteger("shortAnswerNum");
		Integer gapFillingNum = jsonObject.getInteger("gapFillingNum");
		Integer compositeNum = jsonObject.getInteger("compositeNum");*/
		// 校验
		R r = checkIsPass(pkgId, questions, paperName, answerTime);
		if (!r.get("code").equals(0)) {
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
		//判断此章节下是否生成了[活动]分组,如果没有则生成
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
		TepExaminePaperInfo examinePaperInfo = new TepExaminePaperInfo();
		examinePaperInfo.setPaperId(paperId);
		examinePaperInfo.setChapterId(chapterId);
		examinePaperInfo.setPaperName(paperName);
		examinePaperInfo.setPaperTotalScore(totalScore);
		examinePaperInfo.setPaperIsRandom(paperIsRandom);
		examinePaperInfo.setRedoNum(redoNum);
		examinePaperInfo.setUpdateTime(DateUtils.getNowTimeStamp());
		examinePaperInfo.setUpdateUserId(loginUserId);
		examinePaperInfo.setPaperConfinementTime(answerTime); //限制时间
		examinePaperInfo.setEmpiricalValue(GlobalActivityEmpiricalValue.ACTIVITY_4_TEST_ACT);
		examinePaperInfo.setViewResultTime(viewResultTime); //查看答案时机
		examinePaperInfo.setResgroupId(resgroupId);
		examinePaperInfo.setPaperTotalScore(totalScore);
		examinePaperInfo.setSingleChoiceScore(singleChoiceScore);
		examinePaperInfo.setMultipleChoiceScore(multipleChoiceScore);
		examinePaperInfo.setJudgeScore(judgeScore);
		examinePaperInfo.setShortAnswerScore(shortAnswerScore);
		examinePaperInfo.setPurpose(purpose);
		examinePaperInfo.setQuestionNumber(questionsData == null ? 0 : questionsData.size());
		examinePaperInfo.setGapFilling(gapFillingScore);
		examinePaperInfo.setGapFillingScoreStandard(gapFillingScoreStandard);
		examinePaperInfo.setCompositeScore(compositeScore);
		examinePaperInfo = countNums(questionsData, examinePaperInfo);
		//修改试卷的基本信息
		tepExaminePaperInfoMapper.update(examinePaperInfo);
		// 先查询该活动原先存于数据库中的题目
		params.clear();
		params.put("paperId", paperId);
		List<TepExaminePaperQuestionsDetail> details = tepExaminePaperQuestionsDetailMapper.selectListByMap(params);
		// 删除原先存于数据库中的试卷题目关系
		if (details != null && details.size() > 0) {
			String[] array = details.stream().map(a -> a.getDetailId()).toArray(String[]::new);
			tepExaminePaperQuestionsDetailMapper.deleteBatch(array);
		}
		// 保存用户手动录入的题目并建立试卷与题目的关系
		doSaveUserInputQuestionList(questionsData, pkgInfo.getSubjectId(), paperId, singleChoiceScore, multipleChoiceScore, judgeScore, shortAnswerScore, gapFillingScore, compositeScore, loginUserId);
		return R.ok("[测试活动] 修改成功").put("questionsData", questionsData);
	}

	/**
	 * 查看测试活动查看测试活动基本信息与题目（教师所用）
	 * @param activityId 活动主键（试卷主键）
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录用户
	 * @author zyl加
	 * @data 2020年11月21日
	 * @return
	 */
	@Override
	public R viewTestPaperInfo(String activityId, String pkgId, String loginUserId) {
		return viewTestPaperInfoTrainee(activityId, pkgId, loginUserId);
	}

	/**
	 * 学生做试卷的查询接口
	 * @param activityId
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewTestPaperInfoTrainee(String activityId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("查看失败");
		}
		TepExaminePaperInfo tepExaminePaperInfo = tepExaminePaperInfoMapper.selectObjectById(activityId);
		if (tepExaminePaperInfo == null) {
			return R.error("无效的记录");
		}
		if (!"Y".equals(tepExaminePaperInfo.getPaperState())) {
			return R.error("无效的记录，无法查看");
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		// 试卷基本信息
		Map<String, Object> paperInfo = getSimplePaperInfo(tepExaminePaperInfo);
		// 题目信息
		List<Map<String, Object>> questionList = getQuestionListData(activityId, tepExaminePaperInfo.getPaperIsRandom());
		if (questionList != null && !questionList.isEmpty()) {
			questionList.stream().forEach(a -> {
				List<String> allQuestionIds = tevglQuestionsInfoMapper.findQuestionIdsByUnionAll();
				a.put("isCreator", loginUserId.equals(a.get("createUserId")));
				// 处理标识，是否允许修改题目
				if (allQuestionIds.stream().anyMatch(id -> id.equals(a.get("questionsId")))) {
					a.put("hasEditPermission", false);
				} else {
					a.put("hasEditPermission", true);
				}
			});	
		}
		// 返回数据
		data.put("paperInfo", paperInfo);
		data.put("questionList", questionList);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * 活动题目与选项数据
	 * @param activityId 活动id（试卷id）
	 * @param 学员查看试卷题目是，题目是否乱序 （Y/N）
	 * @return
	 */
	private List<Map<String, Object>> getQuestionListData(String activityId, String isRandom){
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (StrUtils.isEmpty(activityId)) {
			return resultList;
		}
		// 查询这套试卷的题目
		Map<String, Object> params = new HashMap<>();
		params.put("paperId", activityId);
		params.put("noChildrenQuestion", "Y"); // 不查复合题的子题目
		List<Map<String, Object>> questionList = tepExaminePaperQuestionsDetailMapper.selectSimpleListByMap(params);
		if (questionList != null && questionList.size() > 0) {
			// 题目类型转换
			convertUtil.convertDict(questionList, "questionsTypeName", "questions_type");
			// 转换 难易程度 （容易、普通、困难等）
			convertUtil.convertDict(questionList, "questionsComplexityName", "questions_complexity");
			// 处理有选项的题目
			List<String> questionTypeList = Arrays.asList("1", "2", "3", "5");
			List<Map<String, Object>> oneList = questionList.stream().filter(a -> questionTypeList.contains(a.get("questionsType"))).collect(Collectors.toList());
			if (oneList != null && oneList.size() > 0) {
				// 单独取出题目主键
				List<Object> qeustionsIds = oneList.stream().map(questionInfo -> questionInfo.get("questionsId")).collect(Collectors.toList());
				// 查询出题目的选项
				params.clear();
				params.put("questionsIds", qeustionsIds);
				List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
				// 遍历设置题目的选项
				questionList.stream().forEach(questionInfo -> {
					List<Map<String, Object>> list = optionList.stream().filter(optionInfo -> optionInfo.get("questionsId").equals(questionInfo.get("questionsId"))).collect(Collectors.toList());
					if (!StrUtils.isNull(questionInfo.get("replyIds"))) {
						String string = questionInfo.get("replyIds").toString();
						String[] split = string.split(",");
						List<String> optionIdList = Stream.of(split).collect(Collectors.toList());
						list.stream().forEach(optionInfo -> {
							optionInfo.put("isModelAnswer", false);
							if (optionIdList.contains(optionInfo.get("optionId"))) {
								optionInfo.put("isModelAnswer", true);
							}
						});
					}
					questionInfo.put("optionList", list);
					if (!"6".equals(questionInfo.get("questionsType")) && !"4".equals(questionInfo.get("questionsType"))) {
						resultList.add(questionInfo);
					}
				});
			}
			// 处理简答题
			List<Map<String, Object>> shortList = questionList.stream().filter(a -> "4".equals(a.get("questionsType"))).collect(Collectors.toList());
			if (shortList != null && shortList.size() > 0) {
				resultList.addAll(shortList);
			}
			// 处理复合题
			List<Map<String, Object>> compositeList = questionList.stream().filter(a -> "6".equals(a.get("questionsType"))).collect(Collectors.toList());
			if (compositeList != null && compositeList.size() > 0) {
				handleCompositeQuestionList(compositeList);
				resultList.addAll(compositeList);
			}
		}
		return resultList;
	}
	
	/**
	 * 取部分数据
	 * @param tepExaminePaperInfo
	 * @return
	 */
	private Map<String, Object> getSimplePaperInfo(TepExaminePaperInfo tepExaminePaperInfo){
		if (tepExaminePaperInfo == null) {
			return null;
		}
		Map<String, Object> paperInfo = new HashMap<>();
		paperInfo.put("paperId", tepExaminePaperInfo.getPaperId());
		paperInfo.put("activityId", tepExaminePaperInfo.getPaperId());
		paperInfo.put("paperName", tepExaminePaperInfo.getPaperName());
		paperInfo.put("paperType", tepExaminePaperInfo.getPaperType()); // 试卷类型
		paperInfo.put("paperTotalScore", tepExaminePaperInfo.getPaperTotalScore()); // 总分
		paperInfo.put("chapterId", tepExaminePaperInfo.getChapterId());  //章节
		paperInfo.put("resgroupId", tepExaminePaperInfo.getResgroupId()); //所属分组
		paperInfo.put("paperIsRandom", tepExaminePaperInfo.getPaperIsRandom()); //是否随机
		paperInfo.put("redoNum", tepExaminePaperInfo.getRedoNum()); //重做次数
		paperInfo.put("viewResultTime", tepExaminePaperInfo.getViewResultTime()); //查看结束时机
		paperInfo.put("singleChoiceScore", tepExaminePaperInfo.getSingleChoiceScore()); // 单选题分值
		paperInfo.put("multipleChoiceScore", tepExaminePaperInfo.getMultipleChoiceScore());
		paperInfo.put("judgeScore", tepExaminePaperInfo.getJudgeScore()); // 判断题分值
		paperInfo.put("shortAnswerScore", tepExaminePaperInfo.getShortAnswerScore()); // 简答题分值
		paperInfo.put("compositeScore", tepExaminePaperInfo.getCompositeScore()); // 复合题里面的子题目每题分值
		paperInfo.put("purpose", tepExaminePaperInfo.getPurpose());
		// 答题时长(单位分钟)
		paperInfo.put("paperConfinementTime", tepExaminePaperInfo.getPaperConfinementTime());
		// 填空题分值
		paperInfo.put("gapFilling", tepExaminePaperInfo.getGapFilling());
		paperInfo.put("gapFillingScoreStandard", tepExaminePaperInfo.getGapFillingScoreStandard());
		// 返回题目数量
		paperInfo.put("questionNumber", tepExaminePaperInfo.getQuestionNumber());
		paperInfo.put("singleChoiceNum", tepExaminePaperInfo.getSingleChoiceNum());
		paperInfo.put("multipleChoiceNum", tepExaminePaperInfo.getMultipleChoiceNum());
		paperInfo.put("judgeNum", tepExaminePaperInfo.getJudgeNum());
		paperInfo.put("shortAnswerNum", tepExaminePaperInfo.getShortAnswerNum());
		paperInfo.put("gapFillingNum", tepExaminePaperInfo.getGapFillingNum());
		paperInfo.put("compositeNum", tepExaminePaperInfo.getCompositeNum());
		return paperInfo;
	}
	
	/**
	 * 删除测试活动
	 * @param activityId 活动主键（试卷主键）
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录用户
	 * @author zyl加
	 * @data 2020年11月21日
	 * @return
	 */
	@Override
	public R deleteTestPaperInfo(String activityId, String pkgId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", activityId);
		params.put("pkgId", pkgId);
		TepExaminePaperInfo examinePaperInfo = tepExaminePaperInfoMapper.selectObjectByIdAndPkgId(params);
		if (examinePaperInfo == null) {
			return R.error("无效的记录");
		}
		// 如果活动已经开始或结束,控制不能删除
		if (!"0".equals(examinePaperInfo.getActivityStateActual())) {
			return R.error("当前活动已被使用，无法删除");
		}
		// 权限校验
		if (!pkgPermissionUtils.hasPermissionDeleteActivity(pkgId, examinePaperInfo.getCreateUserId(), loginUserId, examinePaperInfo.getChapterId())) {
			return R.error("暂无权限，操作失败");
		}
		// 删除与教学包的关系
		tevglPkgActivityRelationMapper.deleteByActivityId(activityId);
		// 删除试卷题目信息
		params.clear();
		params.put("paperId", activityId);
		List<TepExaminePaperQuestionsDetail> list = tepExaminePaperQuestionsDetailMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			List<String> collect = list.stream().map(a -> a.getDetailId()).collect(Collectors.toList());
			tepExaminePaperQuestionsDetailMapper.deleteBatch(collect.stream().toArray(String[]::new));
			/*for (TepExaminePaperQuestionsDetail tepExaminePaperQuestionsDetail : list) {
				tepExaminePaperQuestionsDetailMapper.delete(tepExaminePaperQuestionsDetail.getDetailId());
			}*/
		}
		// 删除试卷基本信息
		tepExaminePaperInfoMapper.delete(activityId);
		// 教学包活动数量-1
		pkgUtils.plusPkgActivityReduceNum(pkgId);
		return R.ok("删除成功");
	}

	/**
	 * 开始测试活动
	 * @param ctId 当前上课课堂主键id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户id
	 * @return
	 */
	@Override
	public R startTestPaperInfo(String ctId, String activityId, String loginUserId, String activityEndTime) {
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
		TepExaminePaperInfo activityInfo = tepExaminePaperInfoMapper.selectObjectByIdAndPkgId(params);
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
		data.put("activityType", GlobalActivity.ACTIVITY_4_TEST_ACT);
		data.put("activityId", activityId);
		data.put("activityTitle", activityInfo.getPaperName());
		data.put("content", null);
		// 即时通讯
		doSendIm(ctId, classroom.getName(), classroom.getClassId(), classroom.getPkgId(), activityId, activityInfo.getPaperName(), params, loginUserId);
		return R.ok("活动时间设置成功").put(Constant.R_DATA, data);
	}

	/**
	 * 
	 * @param ctId 当前课堂
	 * @param classroomName 当前课堂名称 
	 * @param classId 当前课堂上课班级
	 * @param pkgId 当前课堂对应的教学包
	 * @param activityId 活动id
	 * @param activityTitle 活动名称
	 * @param params 查询条件
	 * @param loginUserId 当前登录用户
	 */
	private void doSendIm(String ctId, String classroomName, String classId, String pkgId, String activityId, String activityTitle, Map<String, Object> params, String loginUserId) {
		// ================================================== 即时通讯相关处理 begin ==================================================
		// 找到本课堂所有有效成员
		params.clear();
		params.put("ctId", ctId);
		params.put("state", "Y");
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		// 组装数据
		String tips = "发起了测试活动【" + activityTitle + "】";
		JSONObject msg = new JSONObject();
		msg.put("busitype", MsgType.ACTIVITY); // 为活动类型消息时弹出弹窗
		msg.put("activity_type", GlobalActivity.ACTIVITY_4_TEST_ACT); 
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("send_id", loginUserId);
		busiMsg.put("send_name", null);
		busiMsg.put("tips", tips);
		busiMsg.put("activity_id", activityId);
		busiMsg.put("activity_type", GlobalActivity.ACTIVITY_4_TEST_ACT);
		busiMsg.put("activity_title", activityTitle);
		busiMsg.put("activity_state", "1"); // 活动状态0未开始1进行中2已结束
		busiMsg.put("activityState", "1");
		busiMsg.put("activity_pic", null);
		busiMsg.put("content", null);
		busiMsg.put("ct_id", ctId);
		// 返回满足PC端的数据
		busiMsg.put("ctId", ctId);
		busiMsg.put("name", classroomName);
		busiMsg.put("classId", classId);
		busiMsg.put("pkgId", pkgId);
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
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
	}
	
	/**
	 * 结束测试活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R endTestPaperInfo(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("无效的记录");
		}
		//判断课堂是否开始或已结束
		if ("1".equals(classroom.getClassroomState())) {
			return R.error("课堂尚未开始，无法开始活动");
		}
		if ("3".equals(classroom.getClassroomState())) {
			return R.error("课堂已经结束，无法结束活动");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		//通过活动ID(试卷ID)找试卷信息
		//TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectByPaperId(activityId);
		params.put("pkgId", classroom.getPkgId());
		params.put("activityId", activityId);
		TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectObjectByIdAndPkgId(params);
		if (paperInfo == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if ("0".equals(paperInfo.getActivityStateActual())) {
			return R.error("活动未开始");
		}
		if ("2".equals(paperInfo.getActivityStateActual())) {
			return R.error("活动已结束");
		}
		params.clear();
		params.put("pkgId", classroom.getPkgId());
		params.put("activityId", activityId);
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			return R.error("没有权限");
		}
		TevglPkgActivityRelation relation = list.get(0);
		/*if (!pkgActivityUtils.hasStartActPermission(activityId, relation, loginUserId, paperInfo.getCreateUserId())) {
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
		data.put("activityTitle", paperInfo.getPaperName());
		return R.ok("成功结束活动").put(Constant.R_DATA, data);
	}

	/**
	 * 复制（引用）一个新的活动
	 * @param targetActivityId 目标活动
	 * @param newPkgId 新教学包id
	 * @param loginUserId 当前登录用户
	 * @param chapterId 所属章节
	 * @param sortNum 排序号
	 * @return
	 */
	@Override
	public R copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum) {
		if (StrUtils.isEmpty(targetActivityId) || StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 找到目标活动
		TepExaminePaperInfo activityInfo = tepExaminePaperInfoMapper.selectObjectById(targetActivityId);
		if (activityInfo == null) {
			return R.error("未能找到目标活动");
		}
		// 找到目标活动的活动分组(自定义活动分组时记得控制分组名称唯一)
		String resgroupId = "0";
		resgroupId = activityInfo.getResgroupId();
		// 创建并填充一个新的活动
		TepExaminePaperInfo t = new TepExaminePaperInfo();
		t = activityInfo;
		t.setPaperId(Identities.uuid());
		t.setChapterId(StrUtils.isEmpty(chapterId) ? activityInfo.getChapterId() : chapterId);
		t.setPaperName(activityInfo.getPaperName());
		t.setCreateUserId(loginUserId);
		t.setPaperBeginTime(null);
		t.setPaperState("Y");
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setResgroupId(resgroupId);
		// 保存活动至数据库
		tepExaminePaperInfoMapper.insert(t);
		String newActivityId = t.getPaperId();
		// TODO 复制试卷题目数据
		Map<String, Object> params = new HashMap<>();
		params.put("paperId", targetActivityId);
		List<TepExaminePaperQuestionsDetail> paperQuestionsDetailList = tepExaminePaperQuestionsDetailMapper.selectListByMap(params);
		if (paperQuestionsDetailList != null && paperQuestionsDetailList.size() > 0) {
			TepExaminePaperQuestionsDetail tepExaminePaperQuestionsDetail = new TepExaminePaperQuestionsDetail();
			for (TepExaminePaperQuestionsDetail detail : paperQuestionsDetailList) {
				tepExaminePaperQuestionsDetail = detail;
				tepExaminePaperQuestionsDetail.setDetailId(Identities.uuid());
				tepExaminePaperQuestionsDetail.setPaperId(newActivityId);
				tepExaminePaperQuestionsDetailMapper.insert(tepExaminePaperQuestionsDetail);
			}
		}
		// 保存活动与教学包的关系
		pkgActivityUtils.buildRelation(newPkgId, newActivityId, GlobalActivity.ACTIVITY_4_TEST_ACT);
		// 更新教学包活动数量
		pkgUtils.plusPkgActivityNum(newPkgId);
		return R.ok("复制测试活动成功");
	}
	
	/**
	 * 点击左侧章节获取章节详情和题目类型的个数
	 * @param subjectId
	 * @param chapterId
	 * @param type
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R loadChapters(String subjectId, String chapterId, String type, String loginUserId) {
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(type)) {
			return R.error("必传参数为空");
		}
		// 最终返回数据, 章节的信心,选择题数量、判断题数量、两者之和
		Map<String, Object> result = new HashMap<String, Object>();
		switch (type) {
		case "01":
			TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
			if (tevglBookSubject != null) {
				Map<String, Object> chapterInfo = new HashMap<>();
				chapterInfo.put("id", tevglBookSubject.getSubjectId());
				chapterInfo.put("name", tevglBookSubject.getSubjectName());
				result.put("chapter", chapterInfo);
				// 统计题目数量
				Map<String, Object> map = new HashMap<>();
				map.put("subjectId", chapterId);
				map.put("questionsState", "Y");
				map.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				Map<String, Object> m = tevglQuestionsInfoMapper.countQuestionNumByMap2(map);
				result.put("choiceNumMap", m);
			}
			break;
		case "02":
			// 章节信息
			TevglBookChapter chapter = tevglBookChapterMapper.selectObjectById(chapterId);
			if (chapter != null) {
				Map<String, Object> chapterInfo = new HashMap<>();
				chapterInfo.put("id", chapter.getChapterId());
				chapterInfo.put("name", chapter.getChapterName());
				result.put("chapter", chapterInfo);
				// 统计题目数量
				Map<String, Object> map = new HashMap<>();
				map.put("chaptersId", chapter.getChapterId());
				map.put("questionsState", "Y");
				map.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				Map<String, Object> m = tevglQuestionsInfoMapper.countQuestionNumByMap2(map);
				result.put("choiceNumMap", m);
			}
			break;
		default:
			break;
		}
		return R.ok().put(Constant.R_DATA, result);
	}

	/**
	 * <p>抽取公共的随机题目方法 用来给教师组卷和学员组卷提供题目的随机</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param choiceChapters 用户前台界面选择的章节
	 * @return map 返回一个Map对象的选择题和判断题 页面可以通过k,v形式拿
	 * @apiNote choiceChapters:对象格式为 {chapterId: "", choseNum: "0", judgeNum: "0", type: "02"} chapterId:章节ID, choseNum:选择题的数量, judgeNum:判断题的数量, type:
	 * @apiNote choiceChapters: [{"chapterId": "", "singleChoseNum": "0", "multipleChoseNum": "2", "judgeNum": "1", "shortAnswerNum": "0", "gapFilling": "0", "compositeNum": "1", "type": "01/02"}]
	 */
	@Override
	public Map<String, Object> combinationPaperQuestionsRandom(List<Map<String, Object>> choiceChapters) {
		// 返回最终数据
		Map<String, Object> returnMap = new HashMap<>();
		// 返回选择题数据集
		List<Map<String, Object>> choiceQuestions = new ArrayList<>();
		// 返回判断题数据集
		List<Map<String, Object>> judgeQuestions = new ArrayList<>();
		// 返回问答题数据集
		List<Map<String, Object>> shortAnswerQuestions = new ArrayList<>();
		// 返回填空题数据集
		List<Map<String, Object>> gapFillingQuestions = new ArrayList<>();
		// 返回复合题数据集
		List<Map<String, Object>> compositeQuestions = new ArrayList<>();
		// 所有归属章节的题目
		List<Map<String, Object>> allList = new ArrayList<>();
		// 课程节点
		Map<String, Object> subjectNode = new HashMap<String, Object>();
		// 纯章节节点
		List<Map<String, Object>> nodeList = new ArrayList<>();
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		// 随机题目
		for (Map<String, Object> node : choiceChapters) {
			// 01表示课程节点02表示章节节点
			Object type = node.get("type");
			// 处理条件
			if ("01".equals(type)) {
				subjectNode = node;
			} else {
				nodeList.add(node);
			}
		}
		nodeList.stream().forEach(choseChapters -> {
			// 已经选择了的题目，避免重复选中
			Object oldQuestionsId = choseChapters.get("oldQuestionsId");
			if (!StrUtils.isNull(oldQuestionsId)) {
				String string = oldQuestionsId.toString();
				List<String> existedQuestionsIds = Stream.of(string).collect(Collectors.toList());
				if (existedQuestionsIds != null && existedQuestionsIds.size() > 0) {
					params.put("existedQuestionsIds", existedQuestionsIds);
				}
			}
			// 01表示课程节点02表示章节节点
			//Object type = choseChapters.get("type");
			// 单选题题目的数量
			Object singleChoseNum = choseChapters.get("singleChoseNum");
			// 多选题题目的数量
			Object multipleChoseNum = choseChapters.get("multipleChoseNum");
			// 判断题题目的数量
			Object judgeNum = choseChapters.get("judgeNum");
			// 简单题题目的数量
			Object shortAnswerNum = choseChapters.get("shortAnswerNum");
			// 填空题题目的数量
			Object gapFillingNum = choseChapters.get("gapFillingNum");
			// 复合题题目的数量
			Object compositeNum = choseChapters.get("compositeNum");
			// 处理条件
			params.put("chaptersId", choseChapters.get("chapterId")); // 章节ID
			// 随机单选题
			if (!StrUtils.isNull(singleChoseNum) && !"0".equals(singleChoseNum)) {
				params.put("questionsState", "Y"); // 题目状态
				params.put("questionsType", "1"); // 1单选2多选3判断4问答5填空6复合
				params.put("limit", singleChoseNum); // 随机多少个题目
				params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
				params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				List<Map<String, Object>> choseQuestionsList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
				if (choseQuestionsList != null && choseQuestionsList.size() > 0) {
					choiceQuestions.addAll(choseQuestionsList);
					allList.addAll(choseQuestionsList);
				}
			}
			// 随机多选题
			if (!StrUtils.isNull(multipleChoseNum) && !"0".equals(multipleChoseNum)) {
				params.put("questionsState", "Y"); // 题目状态
				params.put("questionsType", "2"); // 1单选2多选3判断4问答5填空6复合
				params.put("limit", multipleChoseNum); // 随机多少个题目
				params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
				params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				List<Map<String, Object>> choseQuestionsList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
				if (choseQuestionsList != null && choseQuestionsList.size() > 0) {
					choiceQuestions.addAll(choseQuestionsList);
					allList.addAll(choseQuestionsList);
				}
			}
			// 随机判断题
			if (!StrUtils.isNull(judgeNum) && !"0".equals(judgeNum)) {
				params.put("questionsState", "Y"); // 题目状态
				params.put("questionsType", "3"); // 1单选2多选3判断4问答5填空6复合
				params.put("limit", judgeNum); // 随机多少个题目
				params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
				params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				List<Map<String, Object>> judgeQuestionsList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
				if (judgeQuestionsList != null && judgeQuestionsList.size() > 0) {
					judgeQuestions.addAll(judgeQuestionsList);
					allList.addAll(judgeQuestionsList);
				}
			}
			// 随机问答题
			if (!StrUtils.isNull(shortAnswerNum) && !"0".equals(shortAnswerNum)) {
				params.put("questionsState", "Y"); // 题目状态
				params.put("questionsType", "4"); // 1单选2多选3判断4问答5填空6复合
				params.put("limit", shortAnswerNum); // 随机多少个题目
				params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
				params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				List<Map<String, Object>> shortAnswerList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
				if (shortAnswerList != null && shortAnswerList.size() > 0) {
					shortAnswerQuestions.addAll(shortAnswerList);
					allList.addAll(shortAnswerList);
				}
			}
			// 随机填空题
			if (!StrUtils.isNull(gapFillingNum) && !"0".equals(gapFillingNum)) {
				params.put("questionsState", "Y"); // 题目状态
				params.put("questionsType", "5"); // 1单选2多选3判断4问答5填空6复合
				params.put("limit", gapFillingNum); // 随机多少个题目
				params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
				params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				List<Map<String, Object>> gapFillingList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
				if (gapFillingList != null && gapFillingList.size() > 0) {
					gapFillingQuestions.addAll(gapFillingList);
					allList.addAll(gapFillingQuestions);
				}
			}
			// 随机复合题
			if (!StrUtils.isNull(compositeNum) && !"0".equals(compositeNum)) {
				params.put("questionsState", "Y"); // 题目状态
				params.put("questionsType", "6"); // 1单选2多选3判断4问答5填空6复合
				params.put("limit", compositeNum); // 随机多少个题目
				params.put("noChildrenQuestion", null);
				params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
				List<Map<String, Object>> compositeList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
				if (compositeList != null && compositeList.size() > 0) {
					compositeQuestions.addAll(compositeList);
					allList.addAll(compositeQuestions);
				}
			}
			params.clear();
		});
		List<Object> existedQuestionsIds = allList.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
		// 再随机题目课程节点的相关题目
		randSubjectQuestionList(subjectNode, params, choiceQuestions, judgeQuestions, shortAnswerQuestions, gapFillingQuestions, compositeQuestions, existedQuestionsIds);
		if (choiceQuestions.size() + judgeQuestions.size() + shortAnswerQuestions.size() + gapFillingQuestions.size() + compositeQuestions.size() > 200) {
			return R.error("组成试卷，目前总题目不能超过200道");
		}
		//handleCompositeQuestionList(compositeQuestions);
		// 人为判断下题目类型与难易程度
		converShowName(choiceQuestions);
		converShowName(judgeQuestions);
		converShowName(shortAnswerQuestions);
		converShowName(gapFillingQuestions);
		converShowName(compositeQuestions);
		// 处理下题目选项
		handleQuestionList(choiceQuestions, null);
		handleQuestionList(judgeQuestions, null);
		handleQuestionList(shortAnswerQuestions, "4");
		handleQuestionList(gapFillingQuestions, null);
		handleCompositeQuestionList(compositeQuestions);
		// 存入并返回最终数据
		returnMap.put("choiceQuestions", choiceQuestions);
		returnMap.put("judgeQuestions", judgeQuestions);
		returnMap.put("shortAnswerQuestions", shortAnswerQuestions);
		returnMap.put("gapFillingQuestions", gapFillingQuestions);
		returnMap.put("compositeQuestions", compositeQuestions);
		return returnMap;
	}
	
	/**
	 * 先随机章节的题目之后，再随机课程的，避免出现重复题目
	 * @param subjectNode
	 * @param params
	 * @param choiceQuestions
	 * @param judgeQuestions
	 * @param shortAnswerQuestions
	 * @param gapFillingQuestions
	 * @param compositeQuestions
	 * @param existedQuestionsIds
	 */
	private void randSubjectQuestionList(Map<String, Object> subjectNode, Map<String, Object> params, List<Map<String, Object>> choiceQuestions, List<Map<String, Object>> judgeQuestions, List<Map<String, Object>> shortAnswerQuestions, List<Map<String, Object>> gapFillingQuestions, List<Map<String, Object>> compositeQuestions, List<Object> existedQuestionsIds){
		Object subjectId = subjectNode.get("chapterId");
		// 随机题目课程节点的（单选题）
		if (!StrUtils.isNull(subjectNode.get("singleChoseNum")) && !"0".equals(subjectNode.get("singleChoseNum"))) {
			params.clear();
			params.put("existedQuestionsIds", existedQuestionsIds);
			params.put("subjectId", subjectId);
			params.put("questionsState", "Y"); // 题目状态
			params.put("questionsType", "1"); // 1单选2多选3判断4问答
			params.put("limit", subjectNode.get("singleChoseNum")); // 随机多少个题目
			params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
			params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
			List<Map<String, Object>> list1 = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
			choiceQuestions.addAll(list1);
		}
		// 随机题目课程节点的（多选题）
		if (!StrUtils.isNull(subjectNode.get("multipleChoseNum")) && !"0".equals(subjectNode.get("multipleChoseNum"))) {
			params.clear();
			params.put("existedQuestionsIds", existedQuestionsIds);
			params.put("subjectId", subjectId);
			params.put("questionsState", "Y"); // 题目状态
			params.put("questionsType", "2"); // 1单选2多选3判断4问答
			params.put("limit", subjectNode.get("multipleChoseNum")); // 随机多少个题目
			params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
			params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
			List<Map<String, Object>> list2 = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
			choiceQuestions.addAll(list2);
		}
		// 随机题目课程节点的（判断题）
		if (!StrUtils.isNull(subjectNode.get("judgeNum")) && !"0".equals(subjectNode.get("judgeNum"))) {
			params.clear();
			params.put("existedQuestionsIds", existedQuestionsIds);
			params.put("subjectId", subjectId);
			params.put("questionsState", "Y"); // 题目状态
			params.put("questionsType", "3"); // 1单选2多选3判断4问答
			params.put("limit", subjectNode.get("judgeNum")); // 随机多少个题目
			params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
			params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
			List<Map<String, Object>> list3 = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
			judgeQuestions.addAll(list3);
		}
		// 随机题目课程节点的（简答题）
		if (!StrUtils.isNull(subjectNode.get("shortAnswerNum")) && !"0".equals(subjectNode.get("shortAnswerNum"))) {
			params.clear();
			params.put("existedQuestionsIds", existedQuestionsIds);
			params.put("subjectId", subjectId);
			params.put("questionsState", "Y"); // 题目状态
			params.put("questionsType", "4"); // 1单选2多选3判断4问答
			params.put("limit", subjectNode.get("shortAnswerNum")); // 随机多少个题目
			params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
			params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
			List<Map<String, Object>> list4 = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
			shortAnswerQuestions.addAll(list4);
		}
		// 随机题目课程节点的（填空题）
		if (!StrUtils.isNull(subjectNode.get("gapFillingNum")) && !"0".equals(subjectNode.get("gapFillingNum"))) {
			params.clear();
			params.put("existedQuestionsIds", existedQuestionsIds);
			params.put("subjectId", subjectId);
			params.put("questionsState", "Y"); // 题目状态
			params.put("questionsType", "5"); // 1单选2多选3判断4问答
			params.put("limit", subjectNode.get("gapFillingNum")); // 随机多少个题目
			params.put("noChildrenQuestion", "Y"); // 不随机复合题的子题目
			params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
			List<Map<String, Object>> list5 = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
			gapFillingQuestions.addAll(list5);
		}
		// 随机复合题
		if (!StrUtils.isNull(subjectNode.get("compositeNum")) && !"0".equals(subjectNode.get("compositeNum"))) {
			params.clear();
			params.put("existedQuestionsIds", existedQuestionsIds);
			params.put("subjectId", subjectId);
			params.put("questionsState", "Y"); // 题目状态
			params.put("questionsType", "6"); // 1单选2多选3判断4问答5填空6复合
			params.put("limit", subjectNode.get("compositeNum")); // 随机多少个题目
			params.put("filterQuestion", "Y"); // 不查询未在题库展示的题目
			List<Map<String, Object>> list6 = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
			compositeQuestions.addAll(list6);
		}
	}
	
	/**
	 * 处理相关数据
	 * @param questionList 必传参数
	 * @param questionsType
	 */
	private void handleQuestionList(List<Map<String, Object>> questionList, String questionsType) {
		if (questionList == null || questionList.size() == 0) {
			return;
		}
		/*convertUtil.convertDict(questionList, "questionsTypeName", "questions_type");
		convertUtil.convertDict(questionList, "questionsComplexityName", "questions_complexity");*/
		// 如果是简答题的情形
		if ("4".equals(questionsType)) {
			questionList.stream().forEach(questionInfo -> {
				// 移除这个不必要的key
				if (!StrUtils.isNull(questionInfo.get("r"))) {
					questionInfo.remove("r");
				}
			});
			return;
		}
		Map<String, Object> map = new HashMap<>();
		List<Object> questionIds = questionList.stream().map(questionInfo -> questionInfo.get("questionsId")).collect(Collectors.toList());
		map.put("state", "Y");
		map.put("questionIds", questionIds);
		List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(map);
		questionList.stream().forEach(questionInfo -> {
			List<Map<String, Object>> children = optionList.stream().filter(optionInfo -> optionInfo.get("questionsId").equals(questionInfo.get("questionsId"))).collect(Collectors.toList());
			questionInfo.put("optionList", children);
			// 移除这个不必要的key
			if (!StrUtils.isNull(questionInfo.get("r"))) {
				questionInfo.remove("r");
			}
		});
	}
	
	/**
	 * 处理复合题相关数据
	 * @param questionList
	 */
	private void handleCompositeQuestionList(List<Map<String, Object>> questionList) {
		if (questionList == null || questionList.size() == 0) {
			return;
		}
		List<Object> questionIds = new ArrayList<Object>();
		questionList.stream().forEach(questionInfo -> {
			// 移除这个不必要的key
			if (!StrUtils.isNull(questionInfo.get("r"))) {
				questionInfo.remove("r");
			}
			questionIds.add(questionInfo.get("questionsId"));
		});
		// 查询复合题的子题目
		Map<String, Object> params = new HashMap<>();
		params.put("parentIds", questionIds);
		params.put("sidx", "t1.sort_num");
		params.put("order", "asc");
		List<Map<String,Object>> list = tevglQuestionsInfoMapper.selectSimpleListMap(params);
		// 如果没有子题目，直接返回
		if (list == null || list.size() == 0) {
			return;
		}
		// 查询所有子题目的选项
		List<Object> questionIdList = list.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
		params.clear();
		params.put("questionsIds", questionIdList);
		List<Map<String, Object>> allOptionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
		questionList.stream().forEach(questionInfo -> {
			// 取出子题目
			List<Map<String, Object>> childrenQuestionList = list.stream().filter(a -> !StrUtils.isNull(a.get("parentId")) && a.get("parentId").equals(questionInfo.get("questionsId"))).collect(Collectors.toList());
			converShowName(childrenQuestionList);
			// 取出子题目的选项
			childrenQuestionList.stream().forEach(info -> {
				info.put("optionList", allOptionList.stream().filter(option -> option.get("questionsId").equals(info.get("questionsId"))).collect(Collectors.toList()));
			});
			questionInfo.put("children", childrenQuestionList);
		});
	}
	
	/**
	 * 手动处理下（如果选择的题目过多，字典转换会导致加载速度变慢）
	 * @param questionList
	 */
	public void converShowName(List<Map<String, Object>> questionList){
		if (questionList == null || questionList.size() == 0) {
			return;
		}
		questionList.stream().forEach(questionInfo -> {
			Object questionsType = questionInfo.get("questionsType");
			Object questionsComplexity = questionInfo.get("questionsComplexity");
			switch (questionsType.toString()) {
				case "1":
					questionInfo.put("questionsTypeName", "单选题");
					break;
				case "2":
					questionInfo.put("questionsTypeName", "多选题");
					break;
				case "3":
					questionInfo.put("questionsTypeName", "判断题");
					break;
				case "4":
					questionInfo.put("questionsTypeName", "简答题");
					break;
				case "5":
					questionInfo.put("questionsTypeName", "填空题");
					break;
				case "6":
					questionInfo.put("questionsTypeName", "复合题");
					break;		
			}
			switch (questionsComplexity.toString()) {
				case "1":
					questionInfo.put("questionsComplexityName", "简单");
					break;
				case "2":
					questionInfo.put("questionsComplexityName", "普通");
					break;
				case "3":
					questionInfo.put("questionsComplexityName", "困难");
					break;
			}
		});
	}
	
	/**
	 * <p>重选该题目，根据旧题目的id能够重新随机该题目 可以根据题目章节的id重新随机题目</p>  
	 * @author huj
	 * @data 2019年12月26日	
	 * @param oldQuestionsId 原有的题目 避免随机的题目重复，该试卷已经存在的所有题目ID,多个以逗号隔开
	 * @param questionsId 题目id， 被重选的这个题目ID
	 * @return
	 */
	@Override
	public R randomQuestions(String oldQuestionsId, String questionsId) {
		if (StrUtils.isEmpty(oldQuestionsId) || "null".equals(oldQuestionsId)) {
			return R.error("参数oldQuestionsId为空");
		}
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsId为空");
		}
		List<String> asList = Arrays.asList(oldQuestionsId.split(","));
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取被重选的题目信息
		TevglQuestionsInfo questionsInfo = tevglQuestionsInfoMapper.selectObjectById(questionsId);
		if (questionsInfo == null) {
			return R.error("重选题目未成功");
		}
		// 查找要重选的旧题目的信息
		map.put("questionsId", asList);
		List<TevglQuestionsInfo> questionsInfos = tevglQuestionsInfoMapper.selectListByMap(map);
		// 只能重选同一课程下的题目
		for (TevglQuestionsInfo tevglQuestionsInfo : questionsInfos) {
			if (!tevglQuestionsInfo.getSubjectId().equals(questionsInfo.getSubjectId())) {
				return R.error("只能重选同一课程下的题目");
			}
		}
		Integer length = asList.size();
		// 循环判断 如果原有的题目中没有这道题则直接返回
		while (length > 0) {
			// 根据条件筛选题目
			map.clear();
			map.put("subjectId", questionsInfo.getSubjectId());
			map.put("chaptersId", questionsInfo.getChaptersId());
			map.put("questionsType", questionsInfo.getQuestionsType());
			map.put("limit", 1);
			// 只重选除了页面上题目之外的
			map.put("existedQuestionsIds", asList);
			// 不随机复合题的子题目
			map.put("noChildrenQuestion", "Y"); 
			List<Map<String, Object>> randomQuestionsList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(map);
			if (randomQuestionsList == null || randomQuestionsList.isEmpty()) {
				return R.error("未获取到题目信息，暂无题目可重选");
			}
			if (randomQuestionsList.get(0) != null) {
				Map<String, Object> questionInfo = randomQuestionsList.get(0);
				// 如果新的题目不在试卷里面
				if (!asList.contains(questionInfo.get("questionsId"))) {
					// 移除不必要的key
					if (!StrUtils.isNull(questionInfo.get("r"))) {
						questionInfo.remove("r");	
					}
					// 题目类型转换
					convertUtil.convertDict(questionInfo, "questionsTypeName", "questions_type");
					convertUtil.convertDict(questionInfo, "questionsComplexityName", "questions_complexity");
					// 如果为复合题
					if ("6".equals(questionsInfo.getQuestionsType())) {
						map.clear();
						map.put("parentId", questionInfo.get("questionsId"));
						List<Map<String, Object>> childrenQuestionList = tevglQuestionsInfoMapper.selectSimpleListMap(map);
						if (childrenQuestionList != null && childrenQuestionList.size() > 0) {
							// 题目类型转换
							convertUtil.convertDict(childrenQuestionList, "questionsTypeName", "questions_type");
							convertUtil.convertDict(childrenQuestionList, "questionsComplexityName", "questions_complexity");
							List<Object> questionsIdList = childrenQuestionList.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
							map.clear();
							map.put("questionsIds", questionsIdList);
							List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(map);
							childrenQuestionList.stream().forEach(qs -> {
								qs.put("optionList", optionList.stream().filter(a -> a.get("questionsId").equals(qs.get("questionsId"))));
							});
							questionInfo.put("children", childrenQuestionList);
						}
						return R.ok().put(Constant.R_DATA, questionInfo);
					}
					map.clear();
					map.put("questionsId", questionInfo.get("questionsId"));
					List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(map);
					questionInfo.put("optionList", optionList);
					return R.ok().put(Constant.R_DATA, questionInfo);
				}
			}
			length--;
		}
		return R.error("该章节下暂未有其他相同的题目可以随机");
	}

	/**
	 * 删除试卷编辑页中的题目，用户点击删除按钮来删除题目时的请求 匹配session的数据并过滤掉需要删除的那个元素
	 * @author huj
	 * @data 2019年12月26日	
	 * @param request
	 * @param questionsId 被删除的题目ID
	 * @param questionsType 题目类型1选择2判断
	 * @return
	 */
	@Override
	public R deleteSessionQuestions(HttpServletRequest request, String questionsId, String questionsType) {
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsId为空");
		}
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsType为空");
		}
		// 被删除题目
		TevglQuestionsInfo questionsInfo = tevglQuestionsInfoMapper.selectObjectById(questionsId);
		// 从session拿数据
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> maps = (List<Map<String, Object>>) request.getSession().getAttribute("temporarySaveSession");
		Integer index = null;
		if (maps != null && maps.size() > 0) {
			for (int i = 0; i < maps.size(); i++) {
				// 对象的数据格式会为 {chapterId: "", choseNum: "0", judgeNum: "0"}
				Map<String, Object> a = maps.get(i);
				if (a.get("chapterId").equals(questionsInfo.getChaptersId())) {
					if (questionsType.equals("1")) {
						Integer valueInteger = Integer.valueOf(a.get("choseNum").toString()) - 1; // 选择题的个数
						if (valueInteger == 0 && Integer.valueOf(a.get("judgeNum").toString()) == 0) {
							index = i; // 找到该被删题目的在数组中的下标
							break;
						}
						a.put("choseNum", valueInteger);
					} else {
						Integer valueInteger = Integer.valueOf(a.get("judgeNum").toString()) - 1;
						if (valueInteger == 0 && Integer.valueOf(a.get("choseNum").toString()) == 0) {
							index = i;
							break;
						}
						a.put("judgeNum", valueInteger);
					}
				}
			}
		}
		if (index != null) {
			maps.remove(index);
		}
		request.getSession().setAttribute("temporarySaveSession", maps);
		return R.ok("从试卷中删除题目成功");
	}

	/**
	 * 新增测试活动时，拍照录入题目
	 * @param request
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R takeAPhoto(HttpServletRequest request, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			if ("3".equals(tevglTchClassroom.getClassroomState())) {
				return R.error("课堂已结束，无法添加活动");
			}
		}
		// 判断此章节是否已经生成了[活动]分组,如果没有,则生成
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包记录");
		}
		if (!loginUserId.equals(pkgInfo.getCreateUserId())) {
			return R.error("无权操作");
		}
		/*// 固定值
		String tag = "img";
		// 上传至此磁盘目录下
		String path = uploadPath + uploadPathUtils.getPathByParaNo("23");
		// 职业路径
		String majorId = null;
		// 课程体系
		String subjectId = null;
		// 找到当前课堂对应的源课程，从而拿到职业路径，为题目填充对应值
		TevglBookSubject bookSubject = tevglBookSubjectMapper.selectObjectById(pkgInfo.getSubjectId());
		if (bookSubject != null) {
			subjectId = bookSubject.getSubjectRef();
			Map<String, Object> map = new HashMap<>();
			map.put("subjectId", bookSubject.getSubjectRef());
			List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
			if (list != null && list.size() > 0) {
				majorId = list.get(0).getMajorId();
			}
		}*/
		// TODO 
		
		return R.ok("此功能等待实现中");
	}

	/**
	 * 学员批量重做
	 * @author zhouyunlong加
	 * @data 2020年12月15日
	 * @param activityId 活动id(试卷id)
	 * @param ctId 课堂id
	 * @param loginUserId 登录用户id
	 * @param traineeId 学员id
	 * @return
	 */
	@Override
    public R resetTestPaper(JSONObject jsonObject, String loginUserId) {
        String ctId = jsonObject.getString("ctId");
        String activityId = jsonObject.getString("activityId");
        JSONArray traineeIds = jsonObject.getJSONArray("traineeIds");
        if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(ctId)) {
            return R.error("必传参数为空");
        }
        if (traineeIds == null || traineeIds.size() == 0) {
            return R.error("请选择需要重做的学员");
        }
        TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
        if (!loginUserId.equals(classroom.getCreateUserId())) {
            return R.error("你不是课堂创建人，不能重置次数");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("paperId", activityId);
        params.put("traineeIds", traineeIds);
        params.put("ctId", ctId);
        List<Map<String, Object>> dynamicPapers = tepExamineDynamicPaperMapper.selectListMapByMap(params);
        List<Object> dyIds = dynamicPapers.stream().map(a -> a.get("dy_id")).collect(Collectors.toList());
        if (dyIds == null || dyIds.size() == 0) {
            return R.ok();
        }
        // 从历史表查找数据
        params.clear();
        params.put("dyIds", dyIds);
        params.put("traineeIds", traineeIds);
        List<TepExamineHistoryPaper> tepExamineHistoryPaperList = tepExamineHistoryPaperMapper.selectListByMap(params);
        if (tepExamineHistoryPaperList != null && tepExamineHistoryPaperList.size() > 0) {
            // 从成绩表查找数据
            List<String> historyIds = tepExamineHistoryPaperList.stream().map(a -> a.getHistoryId()).collect(Collectors.toList());
            params.clear();
            params.put("historyIds", historyIds);
            params.put("traineeIs", traineeIds);
            List<TepExaminePaperScore> tepExaminePaperScoreList = tepExaminePaperScoreMapper.selectListByMap(params);
            if (tepExaminePaperScoreList != null && tepExaminePaperScoreList.size() > 0) {
                List<String> scoreIdList = tepExaminePaperScoreList.stream().map(x -> x.getScoreId()).collect(Collectors.toList());
                String[] scoreIds = scoreIdList.stream().toArray(String[]::new);
                // 删除成绩表数据
                tepExaminePaperScoreMapper.deleteBatch(scoreIds);
                params.clear();
                params.put("scoreIds", scoreIdList);
                List<TepExaminePaperScoreGapfilling> gapFillingList = tepExaminePaperScoreGapfillingMapper.selectListByMap(params);
                if (gapFillingList != null && gapFillingList.size() > 0) {
                	List<String> collect = gapFillingList.stream().map(a -> a.getId()).collect(Collectors.toList());
                	tepExaminePaperScoreGapfillingMapper.deleteBatch(collect.stream().toArray(String[]::new));
                }
            }
            // 查询题目选项乱序信息
            params.clear();
            params.put("dyIds", dyIds);
            params.put("traineeIds", traineeIds);
            // 根据dyId查找题目选项乱序信息，删除与dyId对应的信息
            List<TepExaminePaperQuestionsOptionRandom> optionRandomList = tepExaminePaperQuestionsOptionRandomMapper.selectListByMap(params);
            // 根据dyId查找题目乱序信息，删除与dyId对应的信息
            List<TepExaminePaperQuestionsRandom> questionsRandomList = tepExaminePaperQuestionsRandomMapper.selectListByMap(params);
            // 删除题目选项乱序信息
            if (optionRandomList != null && optionRandomList.size() > 0)  {
                List<String> optionRdIdList = optionRandomList.stream().map(a -> a.getRdId()).collect(Collectors.toList());
                String[] optionrdIds = optionRdIdList.stream().toArray(String[]::new);
                tepExaminePaperQuestionsOptionRandomMapper.deleteBatch(optionrdIds);
            }
            // 删除题目乱序信息
            if (questionsRandomList != null && questionsRandomList.size() > 0) {
                List<String> questionsRdIdList = questionsRandomList.stream().map(a -> a.getRdId()).collect(Collectors.toList());
                String[] questionrdIds = questionsRdIdList.stream().toArray(String[]::new);
                tepExaminePaperQuestionsRandomMapper.deleteBatch(questionrdIds);
            }
            // 删除历史表数据
            if (historyIds != null && historyIds.size() > 0) {
                String[] historyArrayIds = historyIds.stream().toArray(String[]::new);
                tepExamineHistoryPaperMapper.deleteBatch(historyArrayIds);
            }
            // 删除动态试卷信息
            if (dyIds != null && dyIds.size() > 0) {
                // 将集合类型转换成数组
                String[] dyArrayIds = dyIds.stream().toArray(String[]::new);
                tepExamineDynamicPaperMapper.deleteBatch(dyArrayIds);
            }
        }
        return R.ok("重置成功");
    }
	
	/**
	 * 查询当前登录人所属的职业路径下的试卷库
	 * @author zhouyl加
	 * @date 2021年3月25日
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryPaperListForWx(Map<String, Object> params, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 返回数据
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
		List<String> majorIdList = tevglTraineeInfoMapper.getTraineeMajorIdList(loginUserId);
		log.debug("majorIdList" + majorIdList);
		if (majorIdList == null || majorIdList.size() == 0) {
			pageInfo = new PageInfo<>(new ArrayList<>());
			return R.ok().put(Constant.R_DATA, pageInfo);
		}
		// 根据当前登录人查询加入过的课堂
		params.put("traineeId", loginUserId);
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		List<Object> pkgIds = new ArrayList<>();
		if (classroomTraineeList.size() > 0) {
			// 去除课堂id
			List<String> ctIds = classroomTraineeList.stream().map(a -> a.getCtId()).collect(Collectors.toList());
			// 根据课堂id查询课堂信息且是已开始或已结束的课堂
			params.clear();
			params.put("ctIds", ctIds);
			params.put("isStart", "true");
			List<Map<String, Object>> classroomList = tevglTchClassroomMapper.selectCtIdPkgIdList(params);
			if (classroomList.size() > 0) {
				// 取出教学包id
				pkgIds = classroomList.stream().map(a -> a.get("pkgId")).collect(Collectors.toList());
			}
		}
		
		// 查询当前登录人所属的职业路径下的试卷库
		params.clear();
		params.put("pkgIds", pkgIds);
		params.put("majorIds", majorIdList);
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> paperList = tepExaminePaperInfoMapper.queryPaperListByUnionAll(query);
		pageInfo = new PageInfo<>(paperList);
		return R.ok().put(Constant.R_DATA, pageInfo);
	}
	
	/**
	 * 为了满足，小程序，只能查看自己所在的职业路径的试卷（迂回处理）
	 * @param classroomList 当前登录用户，所在的课堂
	 * @return
	 */
	private List<String> handleMajorPaperList(List<Map<String, Object>> classroomList, Object paperName) {
		if (classroomList == null || classroomList.size() == 0) {
			return null;
		}
		// 找到学生所在职业路径
		List<Object> majorIds = classroomList.stream()
		.filter(a -> !StrUtils.isNull(a.get("majorId")))
		.map(a -> a.get("majorId"))
		.collect(Collectors.toList());
		Map<String, Object> map = new HashMap<>();
		map.put("majorIds", majorIds);
		// 再找试卷
		List<String> questionIdList = tevglQuestionsInfoMapper.selectQuestionIdListByMap(map);
		map.clear();
		map.put("questionIdList", questionIdList);
		List<String> paperIdList = tepExaminePaperQuestionsDetailMapper.getPaperIdList(map);
		return paperIdList;
	}
	
}
