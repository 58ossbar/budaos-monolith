package com.budaos.modules.evgl.activity.service;

import com.alibaba.fastjson.JSONObject;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.common.annotation.NoRepeatSubmit;
import com.budaos.modules.evgl.activity.api.TevglActivityTestPaperTraineeAnswerService;
import com.budaos.modules.evgl.question.domain.*;
import com.budaos.modules.evgl.question.persistence.*;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeEmpiricalValueLog;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeEmpiricalValueLogMapper;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TevglActivityTestPaperTraineeAnswerServiceImpl implements TevglActivityTestPaperTraineeAnswerService {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TepExaminePaperInfoMapper tepExaminePaperInfoMapper;
	@Autowired
	private TepExaminePaperQuestionsDetailMapper tepExaminePaperQuestionsDetailMapper;
	@Autowired
	private TepExamineHistoryPaperMapper tepExamineHistoryPaperMapper;
	@Autowired
	private TepExamineDynamicPaperMapper tepExamineDynamicPaperMapper;
	@Autowired
	private TepExaminePaperScoreMapper tepExaminePaperScoreMapper;
	@Autowired
	private TevglQuestionChoseMapper tevglQuestionChoseMapper;
	@Autowired
	private TepExaminePaperQuestionsRandomMapper tepExaminePaperQuestionsRandomMapper;
	@Autowired
	private TepExaminePaperQuestionsOptionRandomMapper tepExaminePaperQuestionsOptionRandomMapper;
	@Autowired
	private TepExaminePaperScoreGapfillingMapper tepExaminePaperScoreGapfillingMapper;
	@Autowired
	private TevglQuestionsInfoMapper tevglQuestionsInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglActivityTestPaperServiceImple tevglActivityTestPaperServiceImple;
	@Autowired
	private TevglTraineeEmpiricalValueLogMapper tevglTraineeEmpiricalValueLogMapper;
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	
	private final List<String> letters = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
	
	@Autowired
	private TepExaminePaperScoreGapAmendMapper tepExaminePaperScoreGapAmendMapper;
	
	/**
	 * 进入试卷考试考核界面触发
	 * @param isDynamic
	 * @param paperId
	 * @param paperType
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	@NoRepeatSubmit
	public R list(String isDynamic, String paperId, String paperType, String loginUserId, String pkgId, String traineeType, String ctId) {
		if (StrUtils.isEmpty(isDynamic) || StrUtils.isEmpty(paperId) || StrUtils.isEmpty(paperType) 
				|| StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TepExaminePaperInfo paper = tepExaminePaperInfoMapper.selectObjectById(paperId);
		if (paper == null) {
			throw new BudaosException("您选择的试卷不存在或已被删除！");
		}
		if (!paper.getPaperType().equals(paperType)) {
			throw new BudaosException("您选择的试卷发生异常，请刷新页面后重试");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("activityId", paperId);
		TepExaminePaperInfo tepExaminePaperInfo = tepExaminePaperInfoMapper.selectObjectByIdAndPkgId(params);
		if (tepExaminePaperInfo == null) {
			return R.error("无法访问此测试活动，请重新选择");
		}
		if ("0".equals(tepExaminePaperInfo.getActivityStateActual())) {
			return R.error("活动尚未开始，无法作答");
		}
		if ("2".equals(tepExaminePaperInfo.getActivityStateActual())) {
			return R.error("活动已经结束，无法作答");
		}
		if (StrUtils.isEmpty(ctId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
			if (tevglTchClassroom != null) {
				ctId = tevglTchClassroom.getCtId();
			}
		}
		// 如果不是该课堂成员
		List<String> existedTraineeIdList = tevglTchClassroomTraineeMapper.findTraineeIdByCtId(ctId);
		if (!existedTraineeIdList.contains(loginUserId)) {
			return R.error("不是该课堂成员，无法参与测试活动！");
		}
		// 如果试卷限定了重做次数
		R r = handleRedoNum(tepExaminePaperInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		TepExamineDynamicPaper dynamicPaper = null;
		// 查询动态试卷
		params.clear();
		params.put("traineeId", loginUserId);
		params.put("paperId", paperId);
		params.put("dynamicState", paperType); // 其实就是对应的试卷类型(1试卷2自测3考核)
		params.put("paperIsFinished", "N");
		List<TepExamineDynamicPaper> dynamicPaperList = tepExamineDynamicPaperMapper.selectListByMap(params);
		log.debug("试卷类型：" + paperType);
		log.debug("动态试卷：" + dynamicPaperList.size());
		// 如果不是考核试卷
		if (!"3".equals(paperType)) {
			if (dynamicPaperList != null && dynamicPaperList.size() > 0) {
				dynamicPaper = dynamicPaperList.get(0);
			} else {
				dynamicPaper = new TepExamineDynamicPaper();
				dynamicPaper.setDyId(Identities.uuid());
				dynamicPaper.setDynamicState(paperType);
				dynamicPaper.setPaperIsFinished("N");
				dynamicPaper.setPaperId(paperId);
				dynamicPaper.setTraineeId(loginUserId);
				dynamicPaper.setDynamicType(isDynamic);
				dynamicPaper.setCtId(ctId);
				dynamicPaper.setPkgId(pkgId);
				tepExamineDynamicPaperMapper.insert(dynamicPaper);
				// 只有学员第一次作答时才能增加试卷的作答数
				TepExaminePaperInfo t = new TepExaminePaperInfo();
				t.setPaperId(paper.getPaperId());
				t.setPaperPracticeTime(1);
				tepExaminePaperInfoMapper.plusNum(t);
			}
		} else {
			// 考核卷
			dynamicPaper = dynamicPaperList.get(0);
		}
		// 判断历史表中是否有数据
		params.clear();
		params.put("dyId", dynamicPaper.getDyId());
		params.put("traineeId", loginUserId);
		TepExamineHistoryPaper historyPaper = null;
		List<TepExamineHistoryPaper> historys = tepExamineHistoryPaperMapper.selectListByMap(params);
		log.debug("判断历史表中是否有数据["+dynamicPaper.getDyId()+"]" + historys.size());
		// 如果没有,则新增
		if (historys == null || historys.isEmpty()) {
			historyPaper = new TepExamineHistoryPaper();
			historyPaper.setHistoryId(Identities.uuid());
			historyPaper.setDyId(dynamicPaper.getDyId());
			historyPaper.setPaperBeginTime(DateUtils.getNowTimeStamp());
			historyPaper.setPaperFinalScore("0");
			historyPaper.setPaperAccuracy("0");
			historyPaper.setTraineeId(dynamicPaper.getTraineeId());
			tepExamineHistoryPaperMapper.insert(historyPaper);
		} else {
			try {
				historyPaper = historys.get(0);
				// 如果为基本试卷
				/*if ("1".equals(paper.getPaperType())) {
					boolean isEffective = checkHistoryIsEffective(historyPaper, paper);
					log.debug("试卷超出时间1分钟 自动放弃试卷:" + isEffective);
					if (!isEffective) {
						// 试卷超出时间1分钟 自动放弃试卷
						dynamicPaper.setPaperIsFinished("D");
						tepExamineDynamicPaperMapper.update(dynamicPaper);
						// 重新创建
						dynamicPaper = new TepExamineDynamicPaper();
						dynamicPaper.setDyId(Identities.uuid());
						dynamicPaper.setDynamicState(paperType);
						dynamicPaper.setPaperIsFinished("N");
						dynamicPaper.setPaperId(paperId);
						dynamicPaper.setTraineeId(loginUserId);
						dynamicPaper.setDynamicType(isDynamic);
						dynamicPaper.setCtId(ctId);
						dynamicPaper.setPkgId(pkgId);
						tepExamineDynamicPaperMapper.insert(dynamicPaper);
						// 只有学员第一次作答时才能增加试卷的作答数
						TepExaminePaperInfo t = new TepExaminePaperInfo();
						t.setPaperId(paper.getPaperId());
						t.setPaperPracticeTime(1);
						tepExaminePaperInfoMapper.plusNum(t);
						historyPaper = new TepExamineHistoryPaper();
						historyPaper.setHistoryId(Identities.uuid());
						historyPaper.setDyId(dynamicPaper.getDyId());
						historyPaper.setPaperBeginTime(DateUtils.getNowTimeStamp());
						historyPaper.setPaperFinalScore("0");
						historyPaper.setPaperAccuracy("0");
						historyPaper.setTraineeId(dynamicPaper.getTraineeId());
						tepExamineHistoryPaperMapper.insert(historyPaper);
					}
				}*/
			} catch (Exception e) {
				log.error("系统错误", e);
				e.printStackTrace();
			}
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		// 如果为静态试卷则返回历史回复数据
		if ("N".equals(dynamicPaper.getDynamicType())) {
			params.clear();
			params.put("historyId", historyPaper.getHistoryId());
			List<TepExaminePaperScore> historyAnswers = tepExaminePaperScoreMapper.selectListByMap(params);
			// 处理填空题数据
			params.put("historyId", historyPaper.getHistoryId());
			params.put("sidx", "sort_num");
			params.put("order", "asc");
			List<TepExaminePaperScoreGapfilling> tepExaminePaperScoreGapfilling = tepExaminePaperScoreGapfillingMapper.selectListByMap(params);
			historyAnswers.stream().forEach(a -> {
				List<Map<String, Object>> gapFillingList = tepExaminePaperScoreGapfilling.stream()
				.filter(gf -> gf.getScoreId().equals(a.getScoreId()))
				.map(x -> {
					Map<String, Object> info = new HashMap<>();
					info.put("content", x.getContent());
					return info;
				})
				.collect(Collectors.toList());
				// 此时返回学员填空题作答数据
				a.setGapFillingList(gapFillingList);
			});
			data.put("historyAnswers", historyAnswers);
			log.debug("是否查到历史作答数据：" + historyAnswers);
		}
		// 选择题
		List<Map<String, Object>> choseList = new ArrayList<>();
		// 判断题
		List<Map<String, Object>> judgeList = new ArrayList<>();
		// 问答题
		List<Map<String, Object>> shortAnswerList = new ArrayList<>();
		// 填空题
		List<Map<String, Object>> gapFillingList = new ArrayList<>();
		// 复合题
		List<Map<String, Object>> compositeList = new ArrayList<>();
		log.debug("isDynamic:" + isDynamic);
		// 如果是动态试卷
		if (isDynamic.equals("Y")) {
			/*// 关联问题表查询动态试卷题目问题详情（此处有坑）selectListMapWithQuestionInfoByDyId,这个功能貌似没有实现
			List<Map<String, Object>> dynamicOptionsAndQuestionInfos = tepExamineDynamicQuestionsOptionsMapper.selectListMapWithQuestionInfoByDyId(dynamicPaper.getDyId());
			// 获取动态试卷题目详情的选项
			dynamicOptionsAndQuestionInfos.stream().forEach(options -> {
				List<TevglQuestionChose> choses = new ArrayList<>();
				String[] split = options.get("replys_id").toString().split(",");
				for (int i = 0; i < split.length; i++) {
					TevglQuestionChose chose = tevglQuestionChoseMapper.selectObjectById(split[i]);
					choses.add(chose);
				}
				options.put("questionsChose", choses);
				if (options.get("questions_type").equals("1")) {
					choseList.add(options);
				} else {
					judgeList.add(options);
				}
			});
			data.put("options", dynamicOptionsAndQuestionInfos);*/
		} else {
			// 查询试卷试题关联问题的详细信息
			params.clear();
			params.put("paperId", paperId);
			params.put("noChildrenQuestion", "Y");
			List<Map<String, Object>> questionsDetails = tepExaminePaperQuestionsDetailMapper.selectSimpleListByMap(params);
			log.debug("查询试卷试题关联问题的详细信息"+paperId+""+ questionsDetails.size());
			if (questionsDetails != null && questionsDetails.size() > 0) {
				// 题目乱序
				questionsDetails = handleRandomQuestion(paper, dynamicPaper.getDyId(), questionsDetails, loginUserId, params);
				// 题目类型转换
				convertUtil.convertDict(questionsDetails, "questionsTypeName", "questions_type");
				convertUtil.convertDict(questionsDetails, "questionsComplexityName", "questions_complexity");
				// 查询题目选项
				List<Object> questionsIds = questionsDetails.stream().map(questionInfo -> questionInfo.get("questionsId")).collect(Collectors.toList());
				params.clear();
				params.put("questionsIds", questionsIds);
				List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
				List<Map<String, Object>> optionListAgin = new ArrayList<Map<String,Object>>();
				// 题目选项乱序
				handleRandomQuestioOption(paper.getPaperIsRandom(), dynamicPaper.getDyId(), dynamicPaper.getPaperId(), questionsDetails, optionList, loginUserId, params);
				params.clear();
				params.put("questionsIds", questionsIds);
				params.put("traineeId", loginUserId);
				if ("Y".equals(paper.getPaperIsRandom())) {
					params.put("dyId", dynamicPaper.getDyId());
					params.put("sidx", "t2.option_num");
					params.put("order", "asc");
					optionList = tevglQuestionChoseMapper.selectSimpleListByMapForRandom(params);
				}
				optionListAgin.addAll(optionList);
				log.debug("查询条件：" + params);
				log.debug("查询结果：" + optionListAgin.size());
				// 遍历匹配取出每个题目对应的选项
				questionsDetails.stream().forEach(questionsDetail -> {
					// 单选多选判断
					if (Arrays.asList("1", "2", "3").contains(questionsDetail.get("questionsType"))) {
						List<Map<String, Object>> children = optionListAgin.stream().filter(optionInfo -> optionInfo.get("questionsId").equals(questionsDetail.get("questionsId"))).collect(Collectors.toList());
						for (int i = 0; i< children.size(); i ++) {
							children.get(i).put("code", letters.get(i));
						}
						questionsDetail.put("optionList", children);
					}
					// 填空题返回填空的个数
					if ("5".equals(questionsDetail.get("questionsType"))) {
						List<Map<String, Object>> children = optionListAgin.stream().filter(optionInfo -> optionInfo.get("questionsId").equals(questionsDetail.get("questionsId"))).collect(Collectors.toList());
						log.debug("填空题个数：" + children.size());
						questionsDetail.put("gapFillingNumber", children.size());
					}
					// 移除标准答案
					if (!StrUtils.isNull(questionsDetail.get("replyIds"))) {
						questionsDetail.remove("replyIds");
					}
					// 移除题目解析
					questionsDetail.remove("questionsParse");
				});
				// 区分返回选择和判断题目数据
				choseList = questionsDetails.stream().filter(quesitonInfo -> "1".equals(quesitonInfo.get("questionsType")) || "2".equals(quesitonInfo.get("questionsType"))).collect(Collectors.toList());
				judgeList = questionsDetails.stream().filter(quesitonInfo -> "3".equals(quesitonInfo.get("questionsType"))).collect(Collectors.toList());
				shortAnswerList = questionsDetails.stream().filter(quesitonInfo -> "4".equals(quesitonInfo.get("questionsType"))).collect(Collectors.toList());
				gapFillingList = questionsDetails.stream().filter(quesitonInfo -> "5".equals(quesitonInfo.get("questionsType"))).collect(Collectors.toList());
				compositeList = questionsDetails.stream().filter(quesitonInfo -> "6".equals(quesitonInfo.get("questionsType"))).collect(Collectors.toList());
			}
			// 额外处理活动开始之后,教师在30秒之前结束活动、且学员正好在试卷作答页面中，导致结果页面出现无题目的现象
			// 注释说明（已调整为教师手动结束活动，学员直接提交）
			//createDefaultDatas(historyPaper.getHistoryId(), loginUserId, traineeType, params, tepExaminePaperInfo, judgeList, choseList);
		}
		handleCompositeQuestionList(compositeList);
		// 添加返回数据
		data.put("choseList", choseList);
		data.put("judgeList", judgeList);
		data.put("shortAnswerList", shortAnswerList);
		data.put("gapFillingList", gapFillingList);
		data.put("compositeList", compositeList);
		data.put("paper", getSimplePaperInfo(paper));
		data.put("paperType", paperType);
		data.put("pkgId", pkgId);
		data.put("paperDetails", calculationQuestionsDetail(choseList, judgeList, paperType));
		data.put("dyId", dynamicPaper.getDyId());
		return R.ok().put(Constant.R_DATA, data);
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
			// 复合题每小题分值
			String questionTotalScore = questionInfo.get("questionsScore").toString();
			BigDecimal compositeScore = childrenQuestionList.size() > 0 ? new BigDecimal(questionTotalScore).divide(new BigDecimal(childrenQuestionList.size())) : new BigDecimal("0");
			// 题目类型、难易程度转换
			tevglActivityTestPaperServiceImple.converShowName(childrenQuestionList);
			// 遍历
			childrenQuestionList.stream().forEach(info -> {
				// 取出子题目的选项				
				info.put("optionList", allOptionList.stream().filter(option -> option.get("questionsId").equals(info.get("questionsId"))).collect(Collectors.toList()));
				// 移除标准答案
				if (StrUtils.isNull(info.get("replyIds"))) {
					info.remove("replyIds");
				}
				// 填空题返回填空的个数
                if ("5".equals(info.get("questionsType"))) {
                    List<Map<String, Object>> children = allOptionList.stream().filter(optionInfo -> optionInfo.get("questionsId").equals(info.get("questionsId"))).collect(Collectors.toList());
                    info.put("gapFillingNumber", children.size());
                }
                // 处理子题目的每小题分值
				info.put("questionsScore", compositeScore);
			});
			questionInfo.put("children", childrenQuestionList);
		});
	}
	
	/**
	 * 处理题目选项乱序
	 * @param dyId
	 * @param paperId
	 * @param questionsDetailList
	 * @param optionList
	 * @param loginUserId
	 * @param params
	 * @return
	 */
	private List<Map<String, Object>> handleRandomQuestioOption(String isRandom, String dyId, String paperId, List<Map<String, Object>> questionsDetailList, List<Map<String, Object>> optionList, String loginUserId, Map<String, Object> params) {
		if (!"Y".equals(isRandom)) {
			return null;
		}
		if (StrUtils.isEmpty(dyId) || StrUtils.isEmpty(loginUserId) || optionList == null || optionList.size() == 0) {
			return optionList;
		}
		params.clear();
		params.put("dyId", dyId);
		params.put("traineeId", loginUserId);
		List<TepExaminePaperQuestionsOptionRandom> optionRandomList = tepExaminePaperQuestionsOptionRandomMapper.selectListByMap(params);
		Random random = new Random();
		// 等待入库的题目选项序号
		List<TepExaminePaperQuestionsOptionRandom> saveDataList = new ArrayList<>();
		// 题目遍历 begin
		questionsDetailList.stream().forEach(questionInfo -> {
			// 取出题目对应的选项
			List<TepExaminePaperQuestionsOptionRandom> questionsOptionRandomList = optionRandomList.stream().filter(optionRandom -> optionRandom.getQuestionId().equals(questionInfo.get("questionsId"))).collect(Collectors.toList());
			if (questionsOptionRandomList == null || questionsOptionRandomList.size() == 0) {
				if (Arrays.asList("1", "2", "3", "5").contains(questionInfo.get("questionsType"))) {
					log.debug("随机生成题目选项序号");
					// 取出题目的选项
					List<Map<String, Object>> list = optionList.stream().filter(option -> option.get("questionsId").equals(questionInfo.get("questionsId"))).collect(Collectors.toList());
					if (list != null && list.size() > 0) {
						ArrayList<Integer> array = new ArrayList<Integer>();
						int count = 0;
						while (count < list.size()) {
							int number = random.nextInt(list.size()) + 1;
							if(!array.contains(number)){
								array.add(number);
								count++;
							}
						}
						for (int i = 0; i < array.size(); i++) {
							TepExaminePaperQuestionsOptionRandom t = new TepExaminePaperQuestionsOptionRandom();
							t.setRdId(Identities.uuid());
							t.setDyId(dyId);
							t.setQuestionId(questionInfo.get("questionsId").toString());
							t.setTraineeId(loginUserId);
							t.setOptionId(list.get(i).get("optionId").toString());
							t.setOptionNum(array.get(i));
							saveDataList.add(t);
						}
					}

				}
			}
		});
		// 题目遍历 end
		if (saveDataList != null && saveDataList.size() > 0) {
			tepExaminePaperQuestionsOptionRandomMapper.insertBatch(saveDataList);
		}
		return null;
	}
	
	/**
	 * 不同学员试卷的题目乱序
	 * @param paper
	 * @param dyId
	 * @param questionsDetails
	 * @param loginUserId
	 * @param params
	 * @return
	 */
	private List<Map<String, Object>> handleRandomQuestion(TepExaminePaperInfo paper, String dyId, List<Map<String, Object>> questionsDetails, String loginUserId, Map<String, Object> params) {
		if ("N".equals(paper.getPaperIsRandom()) || loginUserId.equals(paper.getCreateUserId())) {
			return questionsDetails;
		}
		// 如果试卷开启了题目随机
		String paperId = paper.getPaperId();
		params.clear();
		params.put("dyId", dyId);
		params.put("traineeId", loginUserId);
		List<TepExaminePaperQuestionsRandom> list = tepExaminePaperQuestionsRandomMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			Random random = new Random();
			ArrayList<Integer> array = new ArrayList<Integer>();
			int count = 0;
			while (count < questionsDetails.size()) {
				int number = random.nextInt(questionsDetails.size()) + 1;
				if(!array.contains(number)){
					array.add(number);
					count++;
				}
			}
			List<TepExaminePaperQuestionsRandom> saveDataList = new ArrayList<>();
			for (int i = 0; i < array.size(); i++) {
				TepExaminePaperQuestionsRandom t = new TepExaminePaperQuestionsRandom();
				t.setRdId(Identities.uuid());
				t.setDyId(dyId);
				t.setDetailId(questionsDetails.get(i).get("detailId").toString());
				t.setTraineeId(loginUserId);
				t.setQuestionsNum(array.get(i));
				//tepExaminePaperQuestionsRandomMapper.insert(t);
				saveDataList.add(t);
			}
			if (saveDataList != null && saveDataList.size() > 0) {
				tepExaminePaperQuestionsRandomMapper.insertBatch(saveDataList);
			}
		}
		params.clear();
		params.put("paperId", paperId);
		params.put("traineeId", loginUserId); // 此情形下，必传
		params.put("dyId", dyId);  // 此情形下，必传
		params.put("sidx", "t3.questions_num");
		params.put("order", "asc");
		log.debug("查询条件：" + params);
		return tepExaminePaperQuestionsDetailMapper.selectSimpleListByMap(params);
	}

	/**
	 * 额外处理活动开始之后,教师在30秒之前结束活动、且学员正好在试卷作答页面中，导致结果页面出现无题目的现象
	 * @param historyId
	 * @param loginUserId
	 * @param traineeType
	 * @param params
	 * @param tepExaminePaperInfo
	 * @param judgeList
	 * @param choseList
	 */
	private void createDefaultDatas(String historyId, String loginUserId, String traineeType, Map<String, Object> params, TepExaminePaperInfo tepExaminePaperInfo, List<Map<String, Object>> judgeList, List<Map<String, Object>> choseList) {
		if (StrUtils.isEmpty(historyId)) {
			return;
		}
		params.clear();
		params.put("historyId", historyId);
		params.put("traineeId", loginUserId);
		List<TepExaminePaperScore> list = tepExaminePaperScoreMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			if (judgeList != null && judgeList.size() > 0) {
				for (int j = 0; j < judgeList.size(); j++) {
					createScore(historyId, loginUserId, traineeType, tepExaminePaperInfo.getJudgeScore(), judgeList.get(j).get("questionsId").toString(), j+"");
				}						
			}
			if (choseList != null && choseList.size() > 0) {
				for (int j = 0; j < choseList.size(); j++) {
					String questionsNum = (judgeList == null || judgeList.size() == 0) ? j+"" : (j+judgeList.size())+"";
					String scoreNum = "1".equals(choseList.get(j).get("questionsType")) ? tepExaminePaperInfo.getSingleChoiceScore() : tepExaminePaperInfo.getMultipleChoiceScore();
					createScore(historyId, loginUserId, traineeType, scoreNum, choseList.get(j).get("questionsId").toString(), questionsNum);
				}	
			}
			//if (shortAnswerList != null && shortAnswerList.size() > 0) {}
		}
	}
	
	/**
	 * 
	 * @param historyId
	 * @param loginUserId
	 * @param traineeType
	 * @param scoreNum 题目分值
	 * @param questionsId
	 * @param questionsNum
	 */
	private void createScore(String historyId, String loginUserId, String traineeType, String scoreNum, String questionsId, String questionsNum) {
		TepExaminePaperScore score = new TepExaminePaperScore();
		score.setScoreId(Identities.uuid());
		score.setHistoryId(historyId);
		score.setQuestionsId(questionsId);
		score.setTraineeId(loginUserId);
		score.setTraineeType(traineeType);
		score.setQuestionsScore(scoreNum);
		score.setState("Y");
		score.setQuestionsNum(questionsNum);
		tepExaminePaperScoreMapper.insert(score);
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
		// 答题时长(单位分钟)
		paperInfo.put("paperConfinementTime", tepExaminePaperInfo.getPaperConfinementTime());
		return paperInfo;
	}
	
	/**
	 * 
	 * @desc //TODO 计算试卷问题的细节
	 * @author huangwb
	 * @param choseScores 选择题
	 * @param judgeScores 判断题
	 * @data 2019年2月27日 上午11:13:45
	 */
	private Map<String, Object> calculationQuestionsDetail(List<Map<String, Object>> choseScores,
			List<Map<String, Object>> judgeScores, String paperType) {
		Map<String, Object> map = new HashMap<>();
		map.put("choseSize", choseScores.size());
		map.put("judgeSize", judgeScores.size());
		/*if (!paperType.equals("2")) {
			map.put("choseScore", (choseScores == null || choseScores.size() == 0) ? 0 : choseScores.get(0).get("questionsScore"));
			map.put("judgeScore", (judgeScores == null || judgeScores.size() == 0) ? 0 : judgeScores.get(0).get("questionsScore"));
			map.put("judgeTotalScore", StrUtils.isNull(map.get("judgeScore")) ? 0 : Integer.valueOf(map.get("judgeScore").toString())
					* Integer.valueOf(map.get("judgeSize").toString()));
			map.put("choseTotalScore", StrUtils.isNull(map.get("choseScore")) ? 0 : Integer.valueOf(map.get("choseScore").toString())
					* Integer.valueOf(map.get("choseSize").toString()));
		}*/
		if (!paperType.equals("2")) {
			map.put("choseScore", (choseScores == null || choseScores.size() == 0) ? 0 : choseScores.get(0).get("questionsScore"));
			map.put("judgeScore", (judgeScores == null || judgeScores.size() == 0) ? 0 : judgeScores.get(0).get("questionsScore"));
			// 兼容小数的情况
			BigDecimal judgeScore = StrUtils.isNull(map.get("judgeScore")) ? new BigDecimal("0") : new BigDecimal(map.get("judgeScore").toString());
			BigDecimal judgeSize = StrUtils.isNull(map.get("judgeSize")) ? new BigDecimal("0") : new BigDecimal(map.get("judgeSize").toString());
			map.put("judgeTotalScore", judgeScore.multiply(judgeSize).setScale(2, BigDecimal.ROUND_HALF_UP));
			BigDecimal choseScore = StrUtils.isNull(map.get("choseScore")) ? new BigDecimal("0") : new BigDecimal(map.get("choseScore").toString());
			BigDecimal choseSize = StrUtils.isNull(map.get("choseSize")) ? new BigDecimal("0") : new BigDecimal(map.get("choseSize").toString());
			map.put("choseTotalScore", choseScore.multiply(choseSize).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		return map;
	}
	
	private boolean checkHistoryIsEffective(TepExamineHistoryPaper historyPaper, TepExaminePaperInfo paper) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(historyPaper.getPaperBeginTime());
		Calendar nowTime = Calendar.getInstance();
		nowTime.setTime(date);
		nowTime.add(Calendar.MINUTE, Integer.valueOf(paper.getPaperConfinementTime()));
		// 考试剩余时间
		long currenTime = nowTime.getTimeInMillis() - System.currentTimeMillis();
		// 如果时间没过时且超过了1分钟则说明试卷已经失效 5分钟300000
		return (currenTime < 0 && Math.abs(currenTime) > 300000) ? false : true;
	}
	
	/**
	 * 每隔30秒提交一次题目答案 需要传递题目号
	 * @param request
	 * @param list [{"questionsId": "", "questionsNum": "", "replyId": "", "questionsScore": "", "content": "", "gapFillingList": ["answer1", "answer2"]}]
	 * @param dyId
	 * @return
	 * @apiNote questionsId题目、questionsNum题目序号、replyId这道题选择的选项主键id,多个英文逗号分隔，questionsScore本题答对可得得分，content简答题回答内容，gapFillingList填空题回答内容
	 */
	@Override
	public R examinePreCommit(HttpServletRequest request, List<TepExaminePaperScore> list, String dyId, String loginUserId, String traineeType) {
		if (StrUtils.isEmpty(dyId) || list == null || list.size() == 0) {
			return R.error("参数dyId为空");
		}
		TepExamineDynamicPaper dynamicPaper = tepExamineDynamicPaperMapper.selectObjectById(dyId);
		if (dynamicPaper == null){
			return R.error("访问的数据出现异常，请重试");
		}
		if (dynamicPaper.getPaperIsFinished().equals("Y")) {
			return R.error(1024, "试卷已经完成，正在跳转试卷结算页");
		}
		TepExaminePaperInfo tepExaminePaperInfo = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		if (tepExaminePaperInfo == null) {
			return R.error("访问的数据出现异常，请重试");
		}
		// 如果试卷限定了重做次数
		R r = handleRedoNum(tepExaminePaperInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 查询参数
		Map<String, Object> params = new HashMap<>();
		params.put("dyId", dyId);
		List<TepExamineHistoryPaper> historyPaperList = tepExamineHistoryPaperMapper.selectListByMap(params);
		if (historyPaperList == null || historyPaperList.size() == 0) {
			return R.error("数据异常，无法提交！");
		}
		TepExamineHistoryPaper historyPaper = new TepExamineHistoryPaper();
		historyPaper = historyPaperList.get(0);
		// 查询已存在的填空作答记录
		params.clear();
		params.put("historyId", historyPaper.getHistoryId());
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TepExaminePaperScoreGapfilling> scoreGapfillingList = tepExaminePaperScoreGapfillingMapper.selectListByMap(params);
		// 先找到本次提交的所有信息
		params.clear();
		params.put("historyId", historyPaper.getHistoryId());
		params.put("questionsIdList", list.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList()));
		List<TepExaminePaperScore> allExaminePaperScoresList = tepExaminePaperScoreMapper.selectListByMap(params);
		// 等待新增的数据
		List<TepExaminePaperScore> insertScoreList = new ArrayList<>();
		List<TepExaminePaperScoreGapfilling> insertScoreGapfillingList = new ArrayList<>();
		// 等待更新的数据
		List<TepExaminePaperScore> updateScoreList = new ArrayList<>();
		List<TepExaminePaperScoreGapfilling> updateScoreGapfillingList = new ArrayList<>();
		for (TepExaminePaperScore paperScore : list) {
			// 查询历史学员作答表
			List<TepExaminePaperScore> examinePaperScoress = allExaminePaperScoresList.stream().filter(a -> a.getQuestionsId().equals(paperScore.getQuestionsId())).collect(Collectors.toList());
			// 如果历史学员作答表中没有数据 则添加
			if (examinePaperScoress == null || examinePaperScoress.isEmpty()) {
				paperScore.setScoreId(Identities.uuid());
				paperScore.setHistoryId(historyPaper.getHistoryId());
				paperScore.setTraineeId(dynamicPaper.getTraineeId());
				paperScore.setState("Y");
				paperScore.setTraineeType(traineeType);
				insertScoreList.add(paperScore);
				// 插入填空题的回答
				//log.debug("================================插入填空题的回答 begin ================================");
				if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
					for (int i = 0; i < paperScore.getGapFillingList().size(); i++) {
						Map<String, Object> mapData = paperScore.getGapFillingList().get(i);
						TepExaminePaperScoreGapfilling gf = new TepExaminePaperScoreGapfilling();
						gf.setId(Identities.uuid());
						gf.setHistoryId(historyPaper.getHistoryId());
						gf.setScoreId(paperScore.getScoreId());
						gf.setQuestionsId(paperScore.getQuestionsId());
						gf.setTraineeId(dynamicPaper.getTraineeId());
						gf.setContent(StrUtils.isNull(mapData.get("content")) ? null : mapData.get("content").toString());
						gf.setSortNum(i);
						insertScoreGapfillingList.add(gf);
					}
				}
				//log.debug("================================插入填空题的回答 begin ================================");
			} else {
				for (TepExaminePaperScore ex : examinePaperScoress) {
					// 判断选择是否一致 如果不一致代表已经修改过了
					//log.debug("上一次选择的答案：" + ex.getReplyId());
					//log.debug("本次选择的答案：" + paperScore.getReplyId());
					// 填充内容
					ex.setContent(paperScore.getContent());
					ex.setReplyId(paperScore.getReplyId());
					updateScoreList.add(ex);
					// 更新填空题的回答
					if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
						List<TepExaminePaperScoreGapfilling> collect = scoreGapfillingList.stream().filter(gp -> gp.getQuestionsId().equals(ex.getQuestionsId()) && gp.getTraineeId().equals(loginUserId)).collect(Collectors.toList());
						if (collect != null && collect.size() > 0) {
							for (int i = 0; i < paperScore.getGapFillingList().size(); i++) {
								// 数据库存在的记录
								TepExaminePaperScoreGapfilling existData = collect.get(i);
								// 传入的
								Map<String, Object> inputData = paperScore.getGapFillingList().get(i);
								existData.setContent(StrUtils.notNull(inputData.get("content")) ? inputData.get("content").toString() : "");
								updateScoreGapfillingList.add(existData);
							}
						}
					}
				}
			}
		}
		// 批量新增
		if (insertScoreList.size() > 0) {
			tepExaminePaperScoreMapper.insertBatch(insertScoreList);
		}
		if (insertScoreGapfillingList.size() > 0) {
			tepExaminePaperScoreGapfillingMapper.insertBatch(insertScoreGapfillingList);
		}
		// 批量更新
		if (updateScoreList.size() > 0) {
			tepExaminePaperScoreMapper.updateBatchByDuplicateKey(updateScoreList);
		}
		if (updateScoreGapfillingList.size() > 0) {
			tepExaminePaperScoreGapfillingMapper.batchUpdateContentByDuplicate(updateScoreGapfillingList);
		}
		return R.ok("数据定时保存成功");
	}
	
	/**
	 * 保存学员提交的试卷题目作答信息
	 * @param dyId
	 * @param paperArrays 学员提交的数据
	 * @param loginUserId
	 * 
	 * @param traineeType
	 * @return
	 */
	@Override
	@Transactional
	public R saveTraineeCommitAnswerContent(String ctId, String dyId, List<TepExaminePaperScore> paperArrays, String loginUserId, String traineeType) {
		if (StrUtils.isEmpty(dyId)) {
			return R.error("参数dyId为空");
		}
		TepExamineDynamicPaper dynamicPaper = tepExamineDynamicPaperMapper.selectObjectById(dyId);
		if (dynamicPaper == null || !loginUserId.equals(dynamicPaper.getTraineeId())) {
			return R.error("无法进行此次操作，请返回页面后重新选择");
		}
		if (dynamicPaper.getPaperIsFinished().equals("Y")) {
			return R.error(205, "试卷已经完成，正在跳转试卷结算页");
		}
		String paperId = dynamicPaper.getPaperId();
		TepExaminePaperInfo tepExaminePaperInfo = tepExaminePaperInfoMapper.selectObjectById(paperId);
		if (tepExaminePaperInfo == null) {
			return R.error("试卷已不存在，请重新选择");
		}
		// 如果试卷限定了重做次数
		R r = handleRedoNum(tepExaminePaperInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 查询参数
		Map<String, Object> params = new HashMap<>();
		params.put("dyId", dyId);
		List<TepExamineHistoryPaper> tepExamineHistoryPaperList = tepExamineHistoryPaperMapper.selectListByMap(params);
		if (tepExamineHistoryPaperList == null || tepExamineHistoryPaperList.size() == 0) { 
			return R.error("数据丢失，提交失败");
		}
		TepExamineHistoryPaper historyPaper = tepExamineHistoryPaperList.get(0);
		// 查询试卷的全部题目
		params.clear();
		params.put("paperId", paperId);
		List<Map<String, Object>> paperQuestionsDetailList = tepExaminePaperQuestionsDetailMapper.selectSimpleListByMap(params);
		if (paperQuestionsDetailList == null || paperQuestionsDetailList.size() == 0) {
			return R.error("无法获取试卷题目，暂无法查看");
		}
		// 本套试卷的复合题（注意复合题，不参与总分计算，只计算其子题目）
		List<Map<String, Object>> comQuesitonList = paperQuestionsDetailList.stream().filter(a -> "6".equals(a.get("questionsType"))).collect(Collectors.toList());
		// 查出填空题的正确答案
		List<Object> questionsIds = paperQuestionsDetailList.stream().filter(a -> a.get("questionsType").equals("5")).map(a -> a.get("questionsId")).collect(Collectors.toList());
		params.clear();
		params.put("questionsIds", questionsIds);
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);	
		// 查询历史作答表
		params.clear();
		params.put("historyId", historyPaper.getHistoryId());
		params.put("traineeId", loginUserId);
		List<TepExaminePaperScore> tepExaminePaperScoreList = tepExaminePaperScoreMapper.selectListByMap(params);
		// 查询历史填空题作答内容
		params.clear();
		params.put("historyId", historyPaper.getHistoryId());
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TepExaminePaperScoreGapfilling> scoreGapfillingList = tepExaminePaperScoreGapfillingMapper.selectListByMap(params);
		// 总分
		Float sum = 0f;
		// 正确数
		int inCorrectNum = 0;
		// 错误数
		int inWrongNum = 0;
		// 等待新增的数据
		List<TepExaminePaperScore> insertScoreList = new ArrayList<>();
		List<TepExaminePaperScoreGapfilling> insertScoreGapfillingList = new ArrayList<>();
		// 等待更新的数据
		List<TepExaminePaperScore> updateScoreList = new ArrayList<>();
		List<TepExaminePaperScoreGapfilling> updateScoreGapfillingList = new ArrayList<>();
		List<TevglQuestionsInfo> updateQuestionList = new ArrayList<>();
		for (TepExaminePaperScore paperScore : paperArrays) {
			boolean anyMatch = comQuesitonList.stream().anyMatch(a -> StrUtils.isNotEmpty(paperScore.getQuestionsId()) && a.get("questionsId").equals(paperScore.getQuestionsId()));
			if (anyMatch) {
				continue;
			}
			// 获取试卷题目对应的得分值
			String questionScore = StrUtils.isEmpty(paperScore.getQuestionsScore()) ? "0" : paperScore.getQuestionsScore();
			List<Map<String,Object>> qList = paperQuestionsDetailList.stream().filter(a -> a.get("questionsId").equals(paperScore.getQuestionsId())).collect(Collectors.toList());
			if (qList != null && qList.size() > 0) {
				questionScore = qList.get(0).get("questionsScore").toString();
			}
			paperScore.setQuestionsScore(questionScore);
			//log.debug("传入的得分值：" + paperScore.getQuestionsScore());
			//log.debug("题目["+paperScore.getQuestionsId()+"]的得分值：" + questionScore);
			// 查询问题
			TevglQuestionsInfo question = tevglQuestionsInfoMapper.selectObjectById(paperScore.getQuestionsId());
			// 用来更新问题正确数、错误数、作答数
			TevglQuestionsInfo questionsInfo = new TevglQuestionsInfo();
			questionsInfo.setQuestionsId(paperScore.getQuestionsId());
			questionsInfo.setQuestionsAnswerNum(1);
			// 如果历史作答表中没有数据 则添加
			List<TepExaminePaperScore> examinePaperScoress = tepExaminePaperScoreList.stream().filter(a -> a.getQuestionsId().equals(paperScore.getQuestionsId())).collect(Collectors.toList());
			if (examinePaperScoress == null || examinePaperScoress.isEmpty()) {
				// 填充得分信息
				paperScore.setScoreId(Identities.uuid());
				paperScore.setHistoryId(historyPaper.getHistoryId());
				paperScore.setTraineeId(loginUserId);
				paperScore.setTraineeType(traineeType);
				paperScore.setState("Y");
				// 简答题系统不评分
				if ("4".equals(question.getQuestionsType())) {
					paperScore.setQuestionsScore(null);
					paperScore.setIsCorrect(null);
				}
				// 单选题多选题判断题系统评分
				if (Arrays.asList("1", "2", "3").contains(question.getQuestionsType())) {
					// 单选多选判断才参与此情况答案校验
					boolean isRight = checkIsRight(question.getReplyIds(), paperScore.getReplyId(), question.getQuestionsType());
					if (isRight) {
						paperScore.setIsCorrect("Y");
						// 正确数
						inCorrectNum++;
						// 成绩总分
						if (!dynamicPaper.getDynamicState().equals("2")) {
							sum += Float.valueOf(questionScore);	
						}
						questionsInfo.setQuestionsCorrectNum(1);
					} else {
						// 错误数
						inWrongNum++;
						paperScore.setIsCorrect("N");
						questionsInfo.setQuestionsErrorNum(1);
					}
				}
				// 插入填空题的回答
				if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
					List<String> userAnswerList = new ArrayList<String>();
					for (int i = 0; i < paperScore.getGapFillingList().size(); i++) {
						Map<String, Object> mapData = paperScore.getGapFillingList().get(i);
						TepExaminePaperScoreGapfilling gf = new TepExaminePaperScoreGapfilling();
						gf.setId(Identities.uuid());
						gf.setHistoryId(historyPaper.getHistoryId());
						gf.setScoreId(paperScore.getScoreId());
						gf.setQuestionsId(paperScore.getQuestionsId());
						gf.setTraineeId(dynamicPaper.getTraineeId());
						gf.setContent(StrUtils.isNull(mapData.get("content")) ? null : mapData.get("content").toString());
						gf.setSortNum(i);
						tepExaminePaperScoreGapfillingMapper.insert(gf);
						userAnswerList.add(StrUtils.isNull(mapData.get("content")) ? "" : mapData.get("content").toString());
					}
					// 如果为填空时
					if ("5".equals(question.getQuestionsType())) {
						// 取出本题填空的标准答案
						List<Object> modelAnswerList = optionList.stream().filter(a -> a.get("questionsId").equals(paperScore.getQuestionsId())).map(a -> a.get("content")).collect(Collectors.toList());
						// 填空得分规则1每空得分，2题目填空全部答对得分
						if ("1".equals(tepExaminePaperInfo.getGapFillingScoreStandard())) {
							for (int i = 0; i < modelAnswerList.size(); i++) {
								if (modelAnswerList.get(i).equals(userAnswerList.get(i))) {
									// 正确数
									paperScore.setIsCorrect("Y");
									inCorrectNum++;
									// 成绩总分
									if (!dynamicPaper.getDynamicState().equals("2")) {
										sum += Float.valueOf(paperScore.getQuestionsScore());	
									}
									questionsInfo.setQuestionsCorrectNum(1);
								} else {
									// 错误数
									inWrongNum++;
									//paperScore.setIsCorrect("N");
									questionsInfo.setQuestionsErrorNum(1);
								}
							}
						}
						if ("2".equals(tepExaminePaperInfo.getGapFillingScoreStandard())) {
							log.debug("更新的情况下");
							log.debug("填空题全部答对才得分");
							log.debug("标准答案：" + modelAnswerList);
							log.debug("用户答案：" + userAnswerList);
							// 如果完全匹配，则回答正确，可得分
							if (modelAnswerList.equals(userAnswerList)) {
								// 正确数
								paperScore.setIsCorrect("Y");
								inCorrectNum++;
								// 成绩总分
								if (!dynamicPaper.getDynamicState().equals("2")) {
									sum += Float.valueOf(paperScore.getQuestionsScore()) * modelAnswerList.size();	
								}
								questionsInfo.setQuestionsCorrectNum(1);
							} else {
								paperScore.setIsCorrect("N");
								// 错误数
								inWrongNum++;
							}
						}
					}
				}
				// 得分情况数据入库
				tepExaminePaperScoreMapper.insert(paperScore);
			} else {
				for (TepExaminePaperScore ex : examinePaperScoress) {
					// 填充信息
					ex.setReplyId(paperScore.getReplyId());
					ex.setContent(paperScore.getContent());
					ex.setTraineeType(traineeType);
					// 简答题系统不评分
					if ("4".equals(question.getQuestionsType())) {
						ex.setQuestionsScore(null);
						ex.setIsCorrect(null);
					}
					// 单选多选判断才参与此情况答案校验
					boolean isRight = checkIsRight(question.getReplyIds(), paperScore.getReplyId(), question.getQuestionsType());
					if (isRight) {
						// 正确数
						inCorrectNum++;
						ex.setIsCorrect("Y");
						if (!dynamicPaper.getDynamicState().equals("2")) {
							// 成绩总分
							if (StrUtils.isNotEmpty(ex.getQuestionsScore())) {
								sum += Float.valueOf(ex.getQuestionsScore());
							}
						}
						questionsInfo.setQuestionsCorrectNum(1);
					} else {
						// 错误数
						inWrongNum++;
						ex.setIsCorrect("N");
						questionsInfo.setQuestionsErrorNum(1);
					}
					// 更新填空题的回答
					if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
						List<String> userAnswerList = new ArrayList<String>();
						List<TepExaminePaperScoreGapfilling> collect = scoreGapfillingList.stream().filter(gp -> gp.getQuestionsId().equals(ex.getQuestionsId()) && gp.getTraineeId().equals(loginUserId)).collect(Collectors.toList());
						if (collect != null && collect.size() > 0) {
							for (int i = 0; i < paperScore.getGapFillingList().size(); i++) {
								// 数据库存在的记录
								TepExaminePaperScoreGapfilling existData = collect.get(i);
								// 传入的
								Map<String, Object> inputData = paperScore.getGapFillingList().get(i);
								existData.setContent(StrUtils.isNull(inputData.get("content")) ? "" : inputData.get("content").toString());
								userAnswerList.add(StrUtils.isNull(inputData.get("content")) ? "" : inputData.get("content").toString());
								//tepExaminePaperScoreGapfillingMapper.update(existData);
								updateScoreGapfillingList.add(existData);
							}
						}
						// 取出本题填空的标准答案
						List<Object> modelAnswerList = optionList.stream().filter(a -> a.get("questionsId").equals(paperScore.getQuestionsId())).map(a -> a.get("content")).collect(Collectors.toList());
						// 填空得分规则1每空得分，2题目填空全部答对得分
						if ("1".equals(tepExaminePaperInfo.getGapFillingScoreStandard())) {
							for (int i = 0; i < modelAnswerList.size(); i++) {
								if (modelAnswerList.get(i).equals(userAnswerList.get(i))) {
									ex.setIsCorrect("Y");
									// 正确数
									//inCorrectNum++;
									// 成绩总分
									if (!dynamicPaper.getDynamicState().equals("2")) {
										sum += Float.valueOf(paperScore.getQuestionsScore());	
									}
									questionsInfo.setQuestionsCorrectNum(1);
								} else {
									// 错误数
									inWrongNum++;
									//paperScore.setIsCorrect("N");
									questionsInfo.setQuestionsErrorNum(1);
								}
							}
						}
						if ("2".equals(tepExaminePaperInfo.getGapFillingScoreStandard())) {
							//log.debug("填空题全部答对才得分");
							//log.debug("标准答案：" + modelAnswerList);
							//log.debug("用户答案：" + userAnswerList);
							// 如果完全匹配，则回答正确，可得分
							if (modelAnswerList.equals(userAnswerList)) {
								// 正确数
								ex.setIsCorrect("Y");
								inCorrectNum++;
								// 成绩总分
								if (!dynamicPaper.getDynamicState().equals("2")) {
									sum += Float.valueOf(paperScore.getQuestionsScore()) * modelAnswerList.size();	
								}
								questionsInfo.setQuestionsCorrectNum(1);
							} else {
								ex.setIsCorrect("N");
								inWrongNum ++;
							}
						}
					}
					//tepExaminePaperScoreMapper.update(ex);
					updateScoreList.add(ex);
				}
			}
			// 设置问题正确率
			Integer correctNum = (question.getQuestionsCorrectNum() == null ? 0 : question.getQuestionsCorrectNum())
					+ (questionsInfo.getQuestionsCorrectNum() == null ? 0 : questionsInfo.getQuestionsCorrectNum());
			Integer answerNum = (question.getQuestionsAnswerNum() == null ? 0 : question.getQuestionsAnswerNum())
					+ (questionsInfo.getQuestionsAnswerNum() == null ? 0 : questionsInfo.getQuestionsAnswerNum());
			Float avg = Float.valueOf(correctNum) / Float.valueOf(answerNum) * 100;
			NumberFormat ddf1 = NumberFormat.getNumberInstance();
			ddf1.setMaximumFractionDigits(2);
			//questionsInfo.setQuestionsAccuracy(ddf1.format(avg));
			questionsInfo.setQuestionsAccuracy(new BigDecimal(ddf1.format(avg)));
			updateQuestionList.add(questionsInfo);
		}
		// 批量新增
		if (insertScoreList.size() > 0) {
			tepExaminePaperScoreMapper.insertBatch(insertScoreList);
		}
		if (insertScoreGapfillingList.size() > 0) {
			tepExaminePaperScoreGapfillingMapper.insertBatch(insertScoreGapfillingList);
		}
		// 批量更新
		if (updateScoreList.size() > 0) {
			tepExaminePaperScoreMapper.updateBatchByDuplicateKey(updateScoreList);
		}
		if (updateScoreGapfillingList.size() > 0) {
			tepExaminePaperScoreGapfillingMapper.batchUpdateContentByDuplicate(updateScoreGapfillingList);
		}
		if (updateQuestionList.size() > 0) {
			tevglQuestionsInfoMapper.plusNumBatchByCaseWhen(updateQuestionList);
		}
		// 更新动态试卷表为完成状态
		dynamicPaper.setPaperIsFinished("Y");
		tepExamineDynamicPaperMapper.update(dynamicPaper);
		// 学员交卷时间
		historyPaper.setPaperEndTime(DateUtils.getNowTimeStamp());
		// 设置题目正确率
		if (paperArrays == null || paperArrays.isEmpty()) {
			// 设置最后成绩
			historyPaper.setPaperFinalScore("0.0");
			historyPaper.setPaperAccuracy("0.0");
		} else {
			BigDecimal decimal = (inWrongNum + inCorrectNum) == 0 ? new BigDecimal("0") : new BigDecimal(inCorrectNum).divide(new BigDecimal(inWrongNum + inCorrectNum), 2, BigDecimal.ROUND_DOWN);
			historyPaper.setPaperAccuracy(decimal.toString());
			if (!dynamicPaper.getDynamicState().equals("2")) {
				historyPaper.setPaperFinalScore(sum.toString());
			}
			// 记录经验值
			BigDecimal empiricalValue = new BigDecimal(historyPaper.getPaperFinalScore());
			doRecordTraineeEmpiricalValueLog(ctId, paperId, loginUserId, empiricalValue.intValue());
		}
		// 更新入库
		tepExamineHistoryPaperMapper.update(historyPaper);
		// 即时通讯，当学员提交时，推送消息告知课堂创建者，自动刷新重新
		if (StrUtils.isNotEmpty(ctId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
			if (tevglTchClassroom != null) {
				JSONObject msg = new JSONObject();
				msg.put("busitype", "refreshTestPaperData"); // 要求重新请求成绩接口
				JSONObject busiMsg = new JSONObject();
				busiMsg.put("activityId", paperId);
				busiMsg.put("ctId", ctId);
				msg.put("msg", busiMsg);
				// 获取tioconfig
				ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
				CBImUtils.sendToUser(tioConfig, tevglTchClassroom.getCreateUserId(), msg);
			}
		}
		return R.ok("试卷提交成功");
	}
	
	/**
	 * 记录学员经验获取日志
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @param empiricalValue 可获得的经验值
	 */
	private void doRecordTraineeEmpiricalValueLog(String ctId, String activityId, String loginUserId, Integer empiricalValue) {
		TevglTraineeEmpiricalValueLog ev = new TevglTraineeEmpiricalValueLog();
		ev.setEvId(Identities.uuid());
		ev.setActivityId(activityId);
		ev.setType(GlobalActivity.ACTIVITY_4_TEST_ACT);
		ev.setTraineeId(loginUserId);
		ev.setCtId(ctId);
		ev.setEmpiricalValue(empiricalValue);
		ev.setCreateTime(DateUtils.getNowTimeStamp());
		ev.setState("Y");
		String classroomName = "";
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom != null) {
			classroomName = "在课堂 【 " + tevglTchClassroom.getName() + " 】的";
		}
		ev.setMsg(classroomName + "测试活动中，试卷得分"+empiricalValue+"， 获得"+empiricalValue+"经验值");
		tevglTraineeEmpiricalValueLogMapper.insert(ev);
	}
	
	/**
	 * 验证（单选、多选、判断）题目标准答案，与用户选择的答案，返回true标识此题答对
	 * @param replyIds 题目的标准答案（选项表主键id），多个英文逗号隔开
	 * @param replyId 用户选择的答案（选项表主键id），多个英文逗号隔开
	 * @param questionsType 当前题目类型
	 * @return
	 */
	private boolean checkIsRight(String replyIds, String replyId, String questionsType) {
		if (Arrays.asList("1", "2", "3").contains(questionsType)) {
			if (StrUtils.isEmpty(replyIds) || StrUtils.isEmpty(replyId)) {
				return false;
			}
			String[] split = replyIds.split(",");
			String[] split2 = replyId.split(",");
			List<String> a = Stream.of(split).collect(Collectors.toList());
			List<String> b = Stream.of(split2).collect(Collectors.toList());
			boolean isRight = CollectionUtils.isEqualCollection(a, b);
			return isRight;
		}
		return false;
	}
	
	/**
	 * 如果这份试卷限制了重做时间
	 * @param tepExaminePaperInfo
	 * @param loginUserId
	 */
	private R handleRedoNum(TepExaminePaperInfo tepExaminePaperInfo, String loginUserId) {
		// // 重做次数(-1不限制0不允许重做1对应允许重做一次，依此类推最高5次)
		if (tepExaminePaperInfo.getRedoNum().equals(-1)) {
			return R.ok();
		}
		// 查询当前用户这套试卷几次做过几次
		Map<String, Object> params = new HashMap<>();
		params.put("paperId", tepExaminePaperInfo.getPaperId());
		params.put("traineeId", loginUserId);
		params.put("dynamicType", "N"); // 动态类型(Y动态N非动态)
		params.put("paperIsFinished", "Y"); // 试卷是否完成（Y/N）
		List<TepExamineDynamicPaper> list = tepExamineDynamicPaperMapper.selectListByMap(params);
		log.debug("查询条件:"+ params);
		log.debug("当前试卷允许重做几次: " + tepExaminePaperInfo.getRedoNum());
		log.debug("当前用户["+loginUserId+"]，做过这套试卷["+tepExaminePaperInfo.getPaperId()+"]几次:" + list.size());
		if (list == null || list.size() == 0) {
			return R.ok();
		}
		// 如果不能重做
		if (tepExaminePaperInfo.getRedoNum().equals(0)) {
			if (list.size() > 0) {
				return R.error("本测试活动只可参与作答一次，不允许再重做");
			}
		}
		// 如果有限制次数
		if (tepExaminePaperInfo.getRedoNum() > 0) {
			if (list.size() - 1 >= tepExaminePaperInfo.getRedoNum()) {
				return R.error("你已达到重做次数的上限，不允许再重做");
			}
		}
		return R.ok();
	}

	/**
	 * 查询本测试活动的总体结果
	 * @param ctId 课堂id
	 * @param pkgId 课堂对应的教学包id
	 * @param activityId 测试活动id（试卷id）
	 * @param loginUserId 当前登录用户
	 * @param traineeName 学员名称（昵称）
	 * @param mobile 手机号码
	 * @param jobNumber 学号工号
	 * @return
	 */
	@Override
	public R queryTraineeAnswerInfo(String ctId, String pkgId, String activityId, String loginUserId, String traineeName, String mobile, String jobNumber) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无法查看");
		}
		/*if (!pkgId.equals(tevglTchClassroom.getPkgId())) {
			return R.error("无法查看");
		}*/
		TepExaminePaperInfo tepExaminePaperInfo = tepExaminePaperInfoMapper.selectObjectById(activityId);
		if (tepExaminePaperInfo == null) {
			return R.error("查看失败");
		}
		if (!"Y".equals(tepExaminePaperInfo.getPaperState())) {
			return R.error("记录已无效，无法查看");
		}
		// 获取试卷部分基本信息
		Map<String, Object> paperInfo = getSimplePaperInfo(tepExaminePaperInfo);
		// 查询这套试卷哪些人做过，按学员分组且成绩降序查询
		Map<String, Object> params = new HashMap<>();
		params.put("paperId", activityId);
		List<String> traineeIds = tepExamineDynamicPaperMapper.selectTraineeIdList(params);
		List<String> traineeIdList = new ArrayList<>();
		// 如果不是课堂创建者，则只能查看自己的
		/*if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
			traineeIdList = traineeIds.stream().filter(id -> id.equals(loginUserId)).collect(Collectors.toList());
		} else {
			
		}*/
		// 所有人都可以看到考试成绩
		if (traineeIds != null && traineeIds.size() > 0) {
			for (String id : traineeIds) {
				if (!traineeIdList.contains(id)) {
					traineeIdList.add(id);
				}
			}
		}
		// 如果没有任何人做过，直接返回
		if (traineeIdList == null || traineeIdList.size() == 0) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>()).put("paperInfo", paperInfo);
		}
		log.debug("当前有哪些人提交了：" + traineeIdList);
		// 返回结果
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 查出这些人的信息
		params.clear();
		params.put("ctId", ctId);
		params.put("traineeIds", traineeIdList);
		params.put("traineeName", traineeName);
		params.put("mobile", mobile);
		params.put("jobNumber", jobNumber);
		List<Map<String, Object>> traineeInfoList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		// 查询
		params.clear();
		params.put("paperId", activityId);
		params.put("traineeIds", traineeIdList);
		log.debug("查询条件：" + params);
		List<TepExamineDynamicPaper> dynamicPaperList = tepExamineDynamicPaperMapper.selectListByMap(params);
		// 获取历史试卷
		if (dynamicPaperList != null && dynamicPaperList.size() > 0) {
			List<String> dyIds = dynamicPaperList.stream().map(dy -> dy.getDyId()).collect(Collectors.toList());
			params.clear();
			params.put("dyIds", dyIds);
			params.put("sidx", "t1.paper_begin_time");
			params.put("order", "desc");
			List<Map<String, Object>> historyPaperList = tepExamineHistoryPaperMapper.selectSimpleListMap(params);
			if (historyPaperList != null && historyPaperList.size() > 0) {
				historyPaperList.stream().forEach(historyPaper -> {
					converToPercent(historyPaper, "paperAccuracy");
				});
			}
			// 当前登录用户是否为
			boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
			boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
			traineeInfoList.stream().forEach(traineeInfo -> {
				if (!isRoomCreator && !isTeachingAssistant) {
					traineeInfo.remove("mobile");
				}
				/*// 数据脱敏
				if (!StrUtils.isNull(traineeInfo.get("mobile"))) {
					String phone = traineeInfo.get("mobile").toString();
					phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
					traineeInfo.put("mobile", phone);
				}*/
				// 头像处理
				traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic"), "16"));
				// 历史作答记录
				List<Map<String, Object>> traineeHistoryPaperList = historyPaperList.stream().filter(a -> a.get("traineeId").equals(traineeInfo.get("traineeId"))).collect(Collectors.toList());
				List<BigDecimal> list = traineeHistoryPaperList.stream().filter(a -> !StrUtils.isNull(a.get("paperFinalScore"))).map(a -> new BigDecimal(a.get("paperFinalScore").toString())).collect(Collectors.toList());
				list = list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
				if (list != null && list.size() > 0) {
					traineeInfo.put("maxScore", list.get(0));
				}
				traineeInfo.put("historyPaperList", traineeHistoryPaperList);
			});
		}
		traineeIdList.stream().forEach(traineeId -> {
			List<Map<String, Object>> list = traineeInfoList.stream().filter(a -> a.get("traineeId").equals(traineeId)).collect(Collectors.toList());
			if (list != null && list.size() > 0) {
				resultList.add(list.get(0));
			}
		});
		// 处理同分的情况
		handleRanking(resultList);
		return R.ok().put(Constant.R_DATA, resultList).put("paperInfo", paperInfo);
	}
	
	/**
	 * 处理最高得分有同分的情况
	 * @param resultList
	 */
	private void handleRanking(List<Map<String, Object>> resultList) {
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
			if (!(lastScore.compareTo((BigDecimal) map.get("maxScore")) == 0)) {
				lastScore = (BigDecimal)map.get("maxScore");
				index++;
			}
			// 排名
			map.put("ranking", index);
		}
	}
	
	private List<Map<String, Object>> handleTraineeAnswerList() {
		return null;
	}
	
	/**
	 * 将字符串形式的 0.12 1.60 转换成 12%
	 * @param data
	 * @param key
	 */
	private void converToPercent(Map<String, Object> data, String key){
		if (StrUtils.isNull(data)) {
			return;
		}
		if (StrUtils.isNull(data.get(key))) {
			return;
		}
		BigDecimal val = new BigDecimal(data.get(key).toString()).multiply(new BigDecimal("100"));
		String value = val.toString();
		if (value.lastIndexOf(".") > 0) {
			value = value.substring(0, value.lastIndexOf(".")) + "%";
		} else {
			value = value + "%";
		}
		data.put(key, value);
	}
	
	/**
	 * 查看某一次试卷考核结果（试卷得分、题目选择等）
	 * @param ctId 当前课堂id
	 * @param dyId 
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	@NoRepeatSubmit
	public R viewExamineResult(String ctId, String dyId, String loginUserId, String traineeId) {
		if (StrUtils.isEmpty(ctId) && StrUtils.isEmpty(dyId) && StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("没有找到指定的课堂信息");
		}
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		TepExamineDynamicPaper dynamicPaper = tepExamineDynamicPaperMapper.selectObjectById(dyId);
		if (dynamicPaper == null) {
			return R.error("无法查看试卷结果");
		}
		/*if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
			if (!loginUserId.equals(dynamicPaper.getTraineeId())) {
				return R.error("无法查看他人的试卷结果");
			}
		}*/
		TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		if (paperInfo == null) {
			return R.error("抱歉，试卷已无法查看");
		}
		// 关联题目查询选择题
		List<Map<String, Object>> choseScores = new ArrayList<>();
		// 关联题目查询判断题
		List<Map<String, Object>> judgeScores = new ArrayList<>();
		// 问答题
		List<Map<String, Object>> shortAnswerList = new ArrayList<>();
		// 填空题
		List<Map<String, Object>> gapFillingList = new ArrayList<>();
		// 复合题
		List<Map<String, Object>> compositeList = new ArrayList<>();
		// 获取历史试卷
		TepExamineHistoryPaper history = new TepExamineHistoryPaper(); 
		params.put("dyId", dyId);
		params.put("traineeId", traineeId);
		List<TepExamineHistoryPaper> historyPaperList = tepExamineHistoryPaperMapper.selectListByMap(params);
		log.debug("历史试卷：" + historyPaperList.size());
		if (historyPaperList != null && historyPaperList.size() > 0) {
			history = historyPaperList.get(0);
		}
		// 查看填空题的作答
		params.clear();
		params.put("historyId", history.getHistoryId());
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> contentList = tepExaminePaperScoreGapfillingMapper.selectSimpleListMapByMap(params);
		// 查询题目
		params.clear();
		params.put("historyId", history.getHistoryId());
		List<Map<String, Object>> questionResultList = tepExaminePaperScoreMapper.selectSimpleListMap(params);
		log.debug("题目作答信息：" + questionResultList.size());
		if (questionResultList != null && questionResultList.size() > 0) {
			// 字典转换
			convertUtil.convertDict(questionResultList, "questionsTypeName", "questions_type");
			convertUtil.convertDict(questionResultList, "questionsComplexityName", "questions_complexity");
			// 查询题目选项
			List<Object> questionsIds = questionResultList.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
			params.clear();
			params.put("questionsIds", questionsIds);
			List<Map<String, Object>> allOptionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
			// 遍历
			questionResultList.stream().forEach(questionScoreInfo -> {
				// 取出题目对应的选项
				List<Map<String, Object>> optionList = allOptionList.stream().filter(optionInfo -> optionInfo.get("questionsId").equals(questionScoreInfo.get("questionsId"))).collect(Collectors.toList());
				questionScoreInfo.put("optionList", optionList);
				// 单选、多选、判断题时，处理题目的正确答案
				handleModelAnswer(questionScoreInfo, optionList);
				// 简答题时，返回简单得分值,与学员实际得分
				if ("4".equals(questionScoreInfo.get("questionsType"))) {
					questionScoreInfo.put("maxScore", paperInfo.getShortAnswerScore());
					// 注意：如果值为null，前端是灰色标记
					questionScoreInfo.put("userScore", questionScoreInfo.get("score"));
					// 为空视为还没评分，不做判断
					if (StrUtils.isNull(questionScoreInfo.get("score"))) {
						questionScoreInfo.put("isCorrect", "");
					} else {
						// 只要大于0分，视为回答正确
						if (Integer.valueOf(questionScoreInfo.get("score").toString()) > 0) {
							questionScoreInfo.put("isCorrect", "Y");
						}
					}
				}
				// 返回填空题的填空个数
				if ("5".equals(questionScoreInfo.get("questionsType"))) {
					questionScoreInfo.put("gapFillingNumber", optionList.size());
				}
			});
			// 区分选择题和判断题，1选择2判断
			choseScores = questionResultList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && Arrays.asList("1", "2").contains(question.get("questionsType"))).collect(Collectors.toList());
			judgeScores = questionResultList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && question.get("questionsType").equals("3")).collect(Collectors.toList());
			shortAnswerList = questionResultList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && "4".equals(question.get("questionsType"))).collect(Collectors.toList());
			gapFillingList = questionResultList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && "5".equals(question.get("questionsType"))).collect(Collectors.toList());
			// 处理填空题
			gapFillingList.stream().forEach(gp -> {
				// 返回用户填空题的作答
				gp.put("userAnswerList", contentList.stream().filter(a -> a.get("traineeId").equals(gp.get("traineeId")) && a.get("scoreId").equals(gp.get("scoreId"))).collect(Collectors.toList()));
				// TODO 计算该填空题最高可得分
				gp.put("maxScore", new BigDecimal(paperInfo.getGapFilling()).multiply(new BigDecimal((Integer) gp.get("gapFillingNumber"))));
				// 老师对填空题的重新评分记录
				gp.put("scoreLogList", tepExaminePaperScoreGapAmendMapper.findLogListByScoreId(gp.get("scoreId").toString()));
			});
			// 取出复合题
			List<Map<String, Object>> allChildrenQuestionList = questionResultList.stream().filter(question -> !StrUtils.isNull(question.get("parentId"))).collect(Collectors.toList());
			List<Object> parentQuestionIdList = allChildrenQuestionList.stream().filter(question -> !StrUtils.isNull(question.get("parentId"))).map(question -> question.get("parentId")).distinct().collect(Collectors.toList());
			if (parentQuestionIdList != null && parentQuestionIdList.size() > 0) {
				params.clear();
				params.put("questionsIds", parentQuestionIdList);
				compositeList = tevglQuestionsInfoMapper.selectSimpleListMap(params);
				tevglActivityTestPaperServiceImple.converShowName(compositeList);
				compositeList.stream().forEach(composite -> {
					// 取出复合题对应的子题目
					List<Map<String, Object>> childrenQuestionList = allChildrenQuestionList.stream().filter(ch -> !StrUtils.isNull(ch.get("parentId")) && ch.get("parentId").equals(composite.get("questionsId"))).collect(Collectors.toList());
					childrenQuestionList.stream().forEach(childrenQuestion -> {
						if ("5".equals(childrenQuestion.get("questionsType"))) {
							// 填空题时，取出学员的回答
							childrenQuestion.put("userAnswerList", contentList.stream().filter(a -> a.get("traineeId").equals(childrenQuestion.get("traineeId")) && a.get("scoreId").equals(childrenQuestion.get("scoreId"))).collect(Collectors.toList()));
							// 复合题中的填空题分，跟主规则走
							childrenQuestion.put("maxScore", paperInfo.getCompositeScore());
							// 老师对填空题的重新评分记录
							childrenQuestion.put("scoreLogList", tepExaminePaperScoreGapAmendMapper.findLogListByScoreId(childrenQuestion.get("scoreId").toString()));
						}
						// 返回复合题子题目的每小题分数
						childrenQuestion.put("questionsScore", paperInfo.getCompositeScore());
					});
					// 题目类型、难易程度
					tevglActivityTestPaperServiceImple.converShowName(childrenQuestionList);
					composite.put("questionList", childrenQuestionList);
					// 复合题下的子题目每小题分值，复合题总分值
					composite.put("questionsScore", paperInfo.getCompositeScore());
					int size = childrenQuestionList == null ? 0 : childrenQuestionList.size();
					BigDecimal questionsTotalScore = new BigDecimal(size).multiply(new BigDecimal(paperInfo.getCompositeScore()));
					composite.put("questionsTotalScore", questionsTotalScore);
				});
			}
		}
		data.put("choseScores", choseScores);
		data.put("judgeScores", judgeScores);
		data.put("shortAnswerList", shortAnswerList);
		data.put("gapFillingList", gapFillingList);
		data.put("compositeList", compositeList);
		// 试卷详情
		TepExaminePaperInfo paper = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		data.put("paper", getSimplePaperInfo(paper));
		// 历史记录
		data.put("history", history);
		data.put("paperDetails", calculationQuestionsDetail(choseScores, judgeScores, paper.getPaperType()));
		return R.ok().put(Constant.R_DATA, data).put(GlobalRoomPermission.LICENSE, getLicenseMap(tevglTchClassroom, loginUserId));
	}
	
	/**
	 * 处理权限标识
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @return
	 */
	private Map<String, Object> getLicenseMap(TevglTchClassroom tevglTchClassroom, String loginUserId) {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("ctId", tevglTchClassroom.getCtId());
		List<String> permissionList = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(info);
		info.put(GlobalRoomPermission.ACTIVITY_4_SHORT_ANSWER_GRADING_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ACTIVITY_4_SHORT_ANSWER_GRADING, permissionList));
		return info;
	}
	
	/**
	 * 处理标准答案和用户选择的答案
	 * @param questionScoreInfo 用户对此题目回答的数据
	 * @param optionList 题目对应的选项
	 */
	private void handleModelAnswer(Map<String, Object> questionScoreInfo, List<Map<String, Object>> optionList) {
		// 处理题目的正确答案
		if (!StrUtils.isNull(questionScoreInfo.get("replyIds")) && Arrays.asList("1", "2", "3").contains(questionScoreInfo.get("questionsType"))) {
			// 正确答案
			String string = questionScoreInfo.get("replyIds").toString();
			String[] split = string.split(",");
			// 用户选择的答案
			String[] userSelectedArra = null;
			if (!StrUtils.isNull(questionScoreInfo.get("replyId"))) {
				String userSelectedOption = questionScoreInfo.get("replyId").toString();
				userSelectedArra = userSelectedOption.split(",");
			}
			for (Map<String, Object> optionInfo : optionList) {
				// 可用于回显题目的正确答案
				optionInfo.put("isModelAnswer", false);
				for (int i = 0; i < split.length; i++) {
					if (optionInfo.get("optionId").equals(split[i])) {
						optionInfo.put("isModelAnswer", true);
					}
				}
				// 用于前端回显用户选择的答案
				if (userSelectedArra != null) {
					for (String op : userSelectedArra) {
						if (optionInfo.get("optionId").equals(op)) {
							optionInfo.put("isSelected", true);
						}
					}
				}
			}
		}
	}

	/**
	 * 单独给简答题评分
	 * @param pkgId
	 * @param activityId
	 * @param scoreId
	 * @param questionsScore 老师给的分
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R giveScore(String ctId, String pkgId, String activityId, String scoreId, String questionsScore, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(scoreId)
				|| StrUtils.isEmpty(questionsScore) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的记录");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ACTIVITY_4_SHORT_ANSWER_GRADING);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		TepExaminePaperInfo tepExaminePaperInfo = tepExaminePaperInfoMapper.selectObjectById(activityId);
		if (tepExaminePaperInfo == null) {
			return R.error("无法获取试卷信息，评分失败");
		}
		// 如果给分超出限制
		int i = new BigDecimal(questionsScore).compareTo(new BigDecimal(tepExaminePaperInfo.getShortAnswerScore()));
		if (i == 1) {
			return R.error("当前题目最高仅可得"+tepExaminePaperInfo.getShortAnswerScore()+"分，不能超过该值");
		}
		TepExaminePaperScore tepExaminePaperScore = tepExaminePaperScoreMapper.selectObjectById(scoreId);
		if (tepExaminePaperScore == null) {
			return R.error("抱歉，已无法进行评分");
		}
		// 找到当前作答试卷，获得已得分数，进行更新
		TepExamineHistoryPaper tepExamineHistoryPaper = tepExamineHistoryPaperMapper.selectObjectById(tepExaminePaperScore.getHistoryId());
		if (tepExamineHistoryPaper == null) {
			return R.error("抱歉，访问的数据丢失，无法进行评分");
		}
		// 更新试卷总分的
		String oldScore = StrUtils.isEmpty(tepExaminePaperScore.getScore()) ? "0" : tepExaminePaperScore.getScore();
		BigDecimal val = new BigDecimal(tepExamineHistoryPaper.getPaperFinalScore()).subtract(new BigDecimal(oldScore)).add(new BigDecimal(questionsScore));
		tepExamineHistoryPaper.setPaperFinalScore(val.toString());
		tepExamineHistoryPaperMapper.update(tepExamineHistoryPaper);
		// 更新简单题得分
		tepExaminePaperScore.setScore(questionsScore);
		tepExaminePaperScoreMapper.update(tepExaminePaperScore);
		return R.ok("评分成功");
	}
	
	public static void main(String[] args) {
		TevglQuestionsInfo question = new TevglQuestionsInfo();
		question.setReplyIds("3c7deb04210d48fdaa1918f3fac1fd3a,9ace8941cf1e4b5b83233ac82da86f5f");
		TepExaminePaperScore paperScore = new TepExaminePaperScore();
		paperScore.setReplyId("ae97b713741d47b19374e8a15854602a");
		boolean flag = Arrays.equals(question.getReplyIds().split(","), paperScore.getReplyId().split(","));
		System.out.println(flag);
		String[] split = question.getReplyIds().split(",");
		String[] split2 = paperScore.getReplyId().split(",");
		List<String> a = Stream.of(split).collect(Collectors.toList());
		List<String> b = Stream.of(split2).collect(Collectors.toList());
		boolean equalCollection = CollectionUtils.isEqualCollection(a, b);
		System.out.println(equalCollection);
		System.out.println(1/2*9);
		System.out.println(new BigDecimal("-1"));
		System.out.println(19/3*3+19%3);
	}
	
}
