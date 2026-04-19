package com.budaos.modules.evgl.examine.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.service.TevglActivityTestPaperServiceImple;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.domain.TevglBookSubperiod;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubperiodMapper;
import com.budaos.modules.evgl.examine.api.TestPaperLibraryService;
import com.budaos.modules.evgl.examine.domain.TevglExamineDynamicPaper;
import com.budaos.modules.evgl.examine.domain.TevglExamineHistoryPaper;
import com.budaos.modules.evgl.examine.domain.TevglExaminePaperScore;
import com.budaos.modules.evgl.examine.domain.TevglExaminePaperScoreGapfilling;
import com.budaos.modules.evgl.examine.persistence.TevglExamineDynamicPaperMapper;
import com.budaos.modules.evgl.examine.persistence.TevglExamineHistoryPaperMapper;
import com.budaos.modules.evgl.examine.persistence.TevglExaminePaperScoreGapfillingMapper;
import com.budaos.modules.evgl.examine.persistence.TevglExaminePaperScoreMapper;
import com.budaos.modules.evgl.question.domain.*;
import com.budaos.modules.evgl.question.persistence.*;
import com.budaos.modules.evgl.question.vo.GenerateOptionVO;
import com.budaos.modules.evgl.question.vo.GeneratePaperVO;
import com.budaos.modules.evgl.question.vo.GenerateQuestionVO;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TestPaperLibraryServiceImpl implements TestPaperLibraryService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglExamineDynamicPaperMapper tevglExamineDynamicPaperMapper;
	@Autowired
	private TevglExamineHistoryPaperMapper tevglExamineHistoryPaperMapper;
	@Autowired
	private TevglExaminePaperScoreMapper tevglExaminePaperScoreMapper;
	@Autowired
	private TevglExaminePaperScoreGapfillingMapper tevglExaminePaperScoreGapfillingMapper;
	@Autowired
	private TepExaminePaperInfoMapper tepExaminePaperInfoMapper;
	@Autowired
	private TepExaminePaperQuestionsDetailMapper tepExaminePaperQuestionsDetailMapper;
	@Autowired
	private TepExaminePaperQuestionsRandomMapper tepExaminePaperQuestionsRandomMapper;
	@Autowired
	private TepExaminePaperQuestionsOptionRandomMapper tepExaminePaperQuestionsOptionRandomMapper;
	@Autowired
	private TevglQuestionsInfoMapper tevglQuestionsInfoMapper;
	@Autowired
	private TevglQuestionChoseMapper tevglQuestionChoseMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper; 
	
	@Autowired
	private TevglActivityTestPaperServiceImple tevglActivityTestPaperServiceImple;
	@Autowired
	private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	
	@Autowired
	private ConvertUtil convertUtil;
	// 定义一个存储选项编号的集合
	private final List<String> letters = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

	/**
	 * 根据条件查询试卷列表（小程序、pc都用到了）
	 * @author zhouyl加
	 * @date 2021年1月3日
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryTestPapers(Map<String, Object> params, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 获取排序方式
		String sortField = StrUtils.isNull(params.get("sortField")) ? "1" : params.get("sortField").toString();
		// 默认按创建时间降序排序
		String sidx = "t.create_time";
		switch (sortField) {
		case "1":
			sidx = "t.create_time";  // 试卷的创建时间
			break;
		case "2":
			sidx = "t.paper_practice_time";  // 试卷的练习次数
			break;
		default:
			break;
		}
		// 只查询有效的试卷
		params.put("paper_state", "Y");
		// 排序条件
		params.put("sidx", sidx);
		params.put("order", "desc");
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		// 根据条件查询试卷信息
		List<Map<String, Object>> paperInfos = tepExaminePaperInfoMapper.selectTestPaperList(query);
		//List<Map<String, Object>> paperInfos = tepExaminePaperInfoMapper.selectTestPaperListNew(query);
		// 如果没有值则直接返回
		if (paperInfos == null || paperInfos.size() == 0) {
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(paperInfos);
			return R.ok().put(Constant.R_DATA, pageInfo);
		}
		paperInfos.stream().forEach(paperInfo -> {
			// 驼峰命名
			paperInfo.put("paperName", paperInfo.get("paper_name"));
			paperInfo.put("paperPracticeTime", paperInfo.get("paper_practice_time"));
			paperInfo.put("paperId", paperInfo.get("paper_id"));
			paperInfo.put("paperType", paperInfo.get("paper_type"));
		});
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(paperInfos);
		return R.ok().put(Constant.R_DATA, pageInfo);
	}
	
	/**
	 * 截取试卷的创建时间并去重
	 * @param params
	 * @return
	 */
	@Override
	public R queryTime(Map<String, Object> params, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		List<Map<String, Object>> queryTimeList = new ArrayList<>();
		// 根据试卷的创建时间查询
		String createTime = StrUtils.isNull(params.get("createTime")) ? "" : params.get("createTime").toString();
		params.put("createTime", createTime);
		// 排序条件
		params.put("sidx", "t1.create_time");
		params.put("order", "desc");
		List<Map<String,Object>> queryDate = tepExaminePaperInfoMapper.queryTime(params);
		for (Map<String, Object> queryTime : queryDate) {
			queryTimeList.add(queryTime);
		}
		return R.ok().put(Constant.R_DATA, queryTimeList);
	}

	/**
	 * 开始考试接口
	 * @author zhouyl加
	 * @date 2021年1月3日
	 * @param isDynamic 
	 * @param paperId 试卷id
	 * @param paperType 试卷类型
	 * @param loginUserId 当前登录用户id
	 * @return
	 */
	@Override
	public R startTheExam(String isDynamic, String paperId, String paperType, String loginUserId) {
		if (StrUtils.isEmpty(isDynamic) || StrUtils.isEmpty(paperId) || StrUtils.isEmpty(paperType)
				|| StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectObjectById(paperId);
		if (paperInfo == null) {
			return R.error("您选择的试卷不存在或已被删除！");
		}
		
		TevglExamineDynamicPaper dynamicPaper = null;
		Map<String, Object> params = new HashMap<String, Object>();
		// 查询动态试卷
		params.put("traineeId", loginUserId);
		params.put("paperId", paperId);
		params.put("dynamicState", paperType); // 试卷类型(1试卷2自测3考核)
		params.put("paperIsFinished", "N");
		List<TevglExamineDynamicPaper> dynamicPaperList = tevglExamineDynamicPaperMapper.selectListByMap(params);
		// 如果不是考核试卷
		if (!"3".equals(paperType)) {
			if (dynamicPaperList != null && dynamicPaperList.size() > 0) {
				dynamicPaper = dynamicPaperList.get(0);
			}else { // 如果没有动态试卷信息则新增
				dynamicPaper = new TevglExamineDynamicPaper();
				dynamicPaper.setDyId(Identities.uuid());
				dynamicPaper.setPaperId(paperId);
				dynamicPaper.setTraineeId(loginUserId);
				dynamicPaper.setDynamicType(isDynamic);
				dynamicPaper.setDynamicState(paperType);
				dynamicPaper.setPaperIsFinished("N"); // 未完成
				tevglExamineDynamicPaperMapper.insert(dynamicPaper);
			}
		}else { // 如果是考核试卷
			// 考核卷
			dynamicPaper = dynamicPaperList.get(0);
		}
		// 判断历史表中是否有数据
		params.clear();
		params.put("dyId", dynamicPaper.getDyId());
		params.put("traineeId", loginUserId);
		TevglExamineHistoryPaper historyPaper = null;
		List<TevglExamineHistoryPaper> historyPapers = tevglExamineHistoryPaperMapper.selectListByMap(params);
		if (historyPapers != null && historyPapers.size() > 0) {
			try {
				historyPaper = historyPapers.get(0);
				// 如果为基本试卷
				if ("1".equals(paperType)) {
					boolean isEffective = checkHistoryIsEffective(historyPaper, paperInfo);
					if (!isEffective) {
						// 试卷超出时间1分钟 自动放弃试卷
						dynamicPaper.setPaperIsFinished("D");
						tevglExamineDynamicPaperMapper.update(dynamicPaper);
						// 重新创建
						dynamicPaper = new TevglExamineDynamicPaper();
						dynamicPaper.setDyId(Identities.uuid());
						dynamicPaper.setDynamicState(paperType);
						dynamicPaper.setPaperIsFinished("N");
						dynamicPaper.setPaperId(paperId);
						dynamicPaper.setTraineeId(loginUserId);
						dynamicPaper.setDynamicType(isDynamic);
						tevglExamineDynamicPaperMapper.insert(dynamicPaper);
						historyPaper = new TevglExamineHistoryPaper();
						historyPaper.setHistoryId(Identities.uuid());
						historyPaper.setDyId(dynamicPaper.getDyId());
						historyPaper.setPaperBeginTime(DateUtils.getNowTimeStamp());
						historyPaper.setPaperFinalScore("0");
						historyPaper.setPaperAccuracy("0");
						historyPaper.setTraineeId(dynamicPaper.getTraineeId());
						tevglExamineHistoryPaperMapper.insert(historyPaper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return R.error("系统错误");
			}
			
		}else { // 如果历史表中没有数据则新增一条记录
			historyPaper = new TevglExamineHistoryPaper();
			historyPaper.setHistoryId(Identities.uuid());
			historyPaper.setDyId(dynamicPaper.getDyId());
			historyPaper.setTraineeId(loginUserId);
			historyPaper.setPaperBeginTime(DateUtils.getNowTimeStamp());
			historyPaper.setPaperFinalScore("0");
			historyPaper.setPaperAccuracy("0");
			tevglExamineHistoryPaperMapper.insert(historyPaper);
			
		}
		// 最终返回数据
		Map<String, Object> data = handleReturnPaperInfo(dynamicPaper, historyPaper, params, isDynamic, paperId, paperInfo, paperType, loginUserId);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * 查看试卷列表
	 * @author zhouyl加
	 * @date 2021年1月9日
	 * @param dynamicPaper 当前登录用户正在做的试卷
	 * @param historyPaper 历史试卷
	 * @param params
	 * @param isDynamic 是否为动态试卷
	 * @param paperId 正在做的试卷id
	 * @param paperInfo 试卷信息
	 * @param paperType 试卷类型(1基本试卷2动态试卷3考核试卷)
	 * @param loginUserId 
	 * @return
	 */
	private Map<String, Object> handleReturnPaperInfo(TevglExamineDynamicPaper dynamicPaper, TevglExamineHistoryPaper historyPaper, Map<String, Object> params, String isDynamic, String paperId,
			TepExaminePaperInfo paperInfo, String paperType, String loginUserId) {
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 如果为静态试卷
		if ("N".equals(dynamicPaper.getDynamicType())) {
			params.clear();
			params.put("historyId", historyPaper.getHistoryId());
			// 根据历史记录id查找学员作答的分数信息
			List<TevglExaminePaperScore> paperScores = tevglExaminePaperScoreMapper.selectListByMap(params);
			params.put("historyId", historyPaper.getHistoryId());
			params.put("sidx", "sort_num");
			params.put("order", "asc");
			List<TevglExaminePaperScoreGapfilling> paperScoreGapfillings = tevglExaminePaperScoreGapfillingMapper.selectListByMap(params);
			paperScores.stream().forEach(a -> {
				List<Map<String, Object>> gapfillings = paperScoreGapfillings.stream()
					.filter(gapfill -> gapfill.getScoreId().equals(a.getScoreId()))
					.map(x -> {
						Map<String, Object> info = new HashMap<>();
						info.put("content", x.getContent());
						return info;
					})
					.collect(Collectors.toList());
					// 此时返回学员填空题作答数据	
					a.setGapFillingList(gapfillings);
			});
			
			data.put("paperScores", paperScores);
			// 当用户提交过试卷时清空作答信息
			if (!StrUtils.isNull(data.get("paperScores")) && "Y".equals(dynamicPaper.getPaperIsFinished())) {
				paperScores.clear();
				data.put("paperScores", paperScores);
			}
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
		List<Map<String, Object>> compoundList = new ArrayList<>();
		// 如果是非动态试卷
		if (isDynamic.equals("N")) {
			params.clear();
			params.put("paperId", paperId);
			// 根据试卷id查询试卷关联的题目信息
			List<Map<String, Object>> paperQuestionsDetails = tepExaminePaperQuestionsDetailMapper.selectSimpleListByMap(params);
			if (paperQuestionsDetails != null && paperQuestionsDetails.size() > 0) {
				// 题目乱序
				paperQuestionsDetails = handleRandomQuestion(paperInfo, dynamicPaper.getDyId(), paperQuestionsDetails, loginUserId, params);
				// 题目类型转换
				converShowName(paperQuestionsDetails);
				//converShowName(paperQuestionsDetails);
				
				List<Object> questionsIds = paperQuestionsDetails.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
				params.clear();
				params.put("questionsIds", questionsIds);
				// 查找题目id对应的选项信息
				List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
				// 存储选项信息的集合
				List<Map<String, Object>> questionsOptionList = new ArrayList<>();
				// 题目选项乱序
				handleRandomQuestioOption(paperInfo.getPaperIsRandom(), dynamicPaper.getDyId(), paperQuestionsDetails, optionList, loginUserId, params);
				params.clear();
				params.put("questionsIds", questionsIds);
				params.put("traineeId", loginUserId);
				// 如果试卷随机了
				if ("Y".equals(paperInfo.getPaperIsRandom())) {
					params.put("dyId", dynamicPaper.getDyId());
					optionList = tevglQuestionChoseMapper.selectSimpleListByMapForRandom(params);
				}
				questionsOptionList.addAll(optionList);
				// 遍历匹配取出每个题目对应的选项
				paperQuestionsDetails.stream().forEach(questionsDetail -> {
					// 单选多选判断
					if (Arrays.asList("1", "2", "3").contains(questionsDetail.get("questionsType"))) {
						List<Map<String, Object>> chilren = questionsOptionList.stream().filter(option -> option.get("questionsId").equals(questionsDetail.get("questionsId"))).collect(Collectors.toList());
						for (int i = 0; i < chilren.size(); i++) {
							chilren.get(i).put("code", letters.get(i));
						}
						questionsDetail.put("optionList", chilren);
					}
					// 填空
					if ("5".equals(questionsDetail.get("questionsType"))) {
						List<Map<String, Object>> chilren = questionsOptionList.stream().filter(option -> option.get("questionsId").equals(questionsDetail.get("questionsId"))).collect(Collectors.toList());
						questionsDetail.put("gapFillingNumber", chilren.size());
					}
					// 移除标准答案
					if (!StrUtils.isNull(questionsDetail.get("replyIds"))) {
						questionsDetail.remove("replyIds");
					}
					// 移除题目解析
					questionsDetail.remove("questionsParse");
				});
				// 区分返回选择和判断题目数据
				// 单选及多选题数据
				choseList = paperQuestionsDetails.stream().filter(questionsInfo -> StrUtils.isNull(questionsInfo.get("parentId")) && Arrays.asList("1", "2").contains(questionsInfo.get("questionsType"))).collect(Collectors.toList());
				// 判断题数据
				judgeList = paperQuestionsDetails.stream().filter(questionsInfo -> StrUtils.isNull(questionsInfo.get("parentId")) && "3".equals(questionsInfo.get("questionsType"))).collect(Collectors.toList());
				// 问答题数据
				shortAnswerList = paperQuestionsDetails.stream().filter(questionsInfo -> StrUtils.isNull(questionsInfo.get("parentId")) && "4".equals(questionsInfo.get("questionsType"))).collect(Collectors.toList());
				// 填空题数据
				gapFillingList = paperQuestionsDetails.stream().filter(questionsInfo -> StrUtils.isNull(questionsInfo.get("parentId")) && "5".equals(questionsInfo.get("questionsType"))).collect(Collectors.toList());
				// 复合题数据
				compoundList = paperQuestionsDetails.stream().filter(questionsInfo -> "6".equals(questionsInfo.get("questionsType"))).collect(Collectors.toList());
			}
		}
		// 处理复合题数据
		handleCompoundQuestionList(compoundList);
		// 添加返回数据
		data.put("choseList", choseList);
		data.put("judgeList", judgeList);
		data.put("shortAnswerList", shortAnswerList);
		data.put("gapFillingList", gapFillingList);
		data.put("compoundList", compoundList);
		data.put("paper", getSimplePaperInfo(paperInfo));
		data.put("paperType", paperType);
		data.put("paperDetails", calculationQuestionsDetail(choseList, judgeList, paperType));
		data.put("dyId", dynamicPaper.getDyId());
		return data;
	}

	/**
	 * 处理复合题数据
	 * @param compoundList
	 */
	private void handleCompoundQuestionList(List<Map<String, Object>> compoundList) {
		// 如果复合题没有数据则直接返回
		if (compoundList == null || compoundList.size() == 0) {
			return;
		}
		// 存储复合题的父题目id
		List<Object> questionsIds = new ArrayList<>();
		compoundList.stream().forEach(compound -> {
			// 移除这个不必要的key
			if (!StrUtils.isNull(compound.get("r"))) {
				compound.remove("r");
			}
			questionsIds.add(compound.get("questionsId"));
		});
		// 取出复合题的子题目
		Map<String, Object> params = new HashMap<>();
		params.put("parentIds", questionsIds);
		List<Map<String, Object>> allChildQuestionsList = tevglQuestionsInfoMapper.selectSimpleListMap(params);
		// 如果没有子题目则直接返回
		if (allChildQuestionsList == null || allChildQuestionsList.size() == 0) {
			return;
		}
		// 取出子题目的选项
		List<Object> questionIdsList = allChildQuestionsList.stream().map(question -> question.get("questionsId")).collect(Collectors.toList());
		params.clear();
		params.put("questionIds", questionIdsList);
		List<Map<String, Object>> allOptionsList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
		compoundList.stream().forEach(question -> {
			// 取出子题目
			List<Map<String, Object>> childrenQuestionList = allChildQuestionsList.stream().filter(a -> !StrUtils.isNull(a.get("parentId")) && a.get("parentId").equals(question.get("questionsId"))).collect(Collectors.toList());
			tevglActivityTestPaperServiceImple.converShowName(childrenQuestionList);
			// 取出子题目选项
			childrenQuestionList.stream().forEach(option -> {
				List<Map<String, Object>> optionList = allOptionsList.stream().filter(a -> a.get("questionsId").equals(option.get("questionsId"))).collect(Collectors.toList());
				option.put("optionList", optionList);
				// 移除正确答案
				if (!StrUtils.isNull(option.get("replyIds"))) {
					option.remove("replyIds");
				}
				// 移除题目解析
				option.remove("questionsParse");
				// 填空题返回填空的个数
				if ("5".equals(question.get("questionsType"))) {
					option.put("gapFillingNumber", optionList.size());
				}
			});
			question.put("children", childrenQuestionList);
		});
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
		paperInfo.put("paperPracticeTime", tepExaminePaperInfo.getPaperPracticeTime()); // 练习次数
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
	
	/**
	 * 不同学员试卷的题目乱序
	 * @param paperInfo
	 * @param dyId
	 * @param paperQuestionsDetails
	 * @param loginUserId
	 * @param params
	 * @return
	 */
	private List<Map<String, Object>> handleRandomQuestion(TepExaminePaperInfo paperInfo, String dyId,
			List<Map<String, Object>> questionsDetails, String loginUserId,
			Map<String, Object> params) {
		if ("N".equals(paperInfo.getPaperIsRandom()) || loginUserId.equals(paperInfo.getCreateUserId())) {
			return questionsDetails;
		}
		// 如果试卷开启了题目随机
		String paperId = paperInfo.getPaperId();
		params.clear();
		params.put("dyId", dyId);
		params.put("traineeId", loginUserId);
		// 根据试卷id和学员id查找随机的题目信息
		List<TepExaminePaperQuestionsRandom> questionsRandoms = tepExaminePaperQuestionsRandomMapper.selectListByMap(params);
		if (questionsRandoms == null || questionsRandoms.size() == 0) {
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
		return tepExaminePaperQuestionsDetailMapper.selectSimpleListByMap(params);
	}
	
	/**
	 * 处理题目选项乱序
	 * @param isRandom
	 * @param dyId
	 * @param questionsDetailList
	 * @param optionList
	 * @param loginUserId
	 * @param params
	 * @return
	 */
	private List<Map<String, Object>> handleRandomQuestioOption(String isRandom, String dyId, List<Map<String, Object>> questionsDetailList, List<Map<String, Object>> optionList, String loginUserId, Map<String, Object> params) {
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
		questionsDetailList.stream().forEach(questionInfo -> {
			// 取出题目对应的选项
			List<TepExaminePaperQuestionsOptionRandom> questionsOptionRandomList = optionRandomList.stream().filter(optionRandom -> optionRandom.getQuestionId().equals(questionInfo.get("questionsId"))).collect(Collectors.toList());
			if (questionsOptionRandomList == null || questionsOptionRandomList.size() == 0) {
				if (Arrays.asList("1", "2", "3", "5").contains(questionInfo.get("questionsType"))) {
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
						List<TepExaminePaperQuestionsOptionRandom> saveDataList = new ArrayList<>();
						for (int i = 0; i < array.size(); i++) {
							TepExaminePaperQuestionsOptionRandom t = new TepExaminePaperQuestionsOptionRandom();
							t.setRdId(Identities.uuid());
							t.setDyId(dyId);
							t.setQuestionId(questionInfo.get("questionsId").toString());
							t.setTraineeId(loginUserId);
							t.setOptionId(list.get(i).get("optionId").toString());
							t.setOptionNum(array.get(i));
							//tepExaminePaperQuestionsOptionRandomMapper.insert(t);
							saveDataList.add(t);
						}
						if (saveDataList != null && saveDataList.size() > 0) {
							tepExaminePaperQuestionsOptionRandomMapper.insertBatch(saveDataList);
						}
					}

				}
			}
		});
		return null;
	}

	private boolean checkHistoryIsEffective(TevglExamineHistoryPaper historyPaper, TepExaminePaperInfo paperInfo) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(historyPaper.getPaperBeginTime());
		Calendar nowTime = Calendar.getInstance();
		nowTime.setTime(date);
		nowTime.add(Calendar.MINUTE, Integer.valueOf(paperInfo.getPaperConfinementTime()));
		// 考试剩余时间
		long currenTime = nowTime.getTimeInMillis() - System.currentTimeMillis();
		// 如果时间没过时且超过了1分钟则说明试卷已经失效 5分钟300000
		return (currenTime < 0 && Math.abs(currenTime) > 300000) ? false : true;
	}
	
	/**
	 * 保存用户提交的作答信息
	 * @author zhouyl加
	 * @date 2021年1月5日
	 * @param dyId 
	 * @param list 提交的数据
	 * @param loginUserId
	 * @param traineeType
	 * @return
	 */
	@Override
	@Transactional
	public R saveReplyInformation( String dyId, List<TevglExaminePaperScore> paperScores, String loginUserId, String traineeType) {
		if (StrUtils.isEmpty(dyId)) {
			return R.error("要提交的试卷不存在");
		}
		TevglExamineDynamicPaper dynamicPaper = tevglExamineDynamicPaperMapper.selectObjectById(dyId);
		if (dynamicPaper == null) {
			return R.error("试卷信息为空，请刷新页面后重试");
		}
		TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		if (paperInfo == null) {
			return R.error("试卷不存在，请重新选择");
		}
		
		Map<String, Object> params = new HashMap<>();
		// 查询历史表数据
		params.put("dyId", dyId);
		List<TevglExamineHistoryPaper> historyPapers = tevglExamineHistoryPaperMapper.selectListByMap(params);
		// 点击开始考试时就会向数据库中添加一条记录，提交的时候这里就不用添加了，所有直接判断是否为空
		if (historyPapers == null || historyPapers.size() == 0) {
			return R.error("历史表中不存在这条记录，提交失败");
		}
		TevglExamineHistoryPaper historyPaper = historyPapers.get(0);
		// 查询试卷的全部题目
		params.clear();
		params.put("paperId", dynamicPaper.getPaperId());
		// 根据试卷id查询试卷题目详情信息
		List<Map<String, Object>> paperQuestionsList = tepExaminePaperQuestionsDetailMapper.selectSimpleListByMap(params);
		// 存储题目选项的集合
		List<Map<String, Object>> optionList = new ArrayList<>();
		if (paperQuestionsList != null && paperQuestionsList.size() > 0) {
			// 查出填空题的题目id
			List<Object> questionsIds = paperQuestionsList.stream()
			.filter(a -> a.get("questionsType").equals("5"))
			.map(x -> x.get("questionsId")).collect(Collectors.toList());
			params.clear();
			params.put("questionsIds", questionsIds);
			params.put("sidx", "sort_num");
			params.put("order", "asc");
			// 取出题目的选项信息
			optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
		}
		// 查询试卷成绩
		params.clear();
		params.put("historyId", historyPaper.getHistoryId());
		params.put("traineeId", loginUserId);
		List<TevglExaminePaperScore> paperScoreList = tevglExaminePaperScoreMapper.selectListByMap(params);
		// 计算试卷得分情况
		calculationPaperScore(paperScores, paperScoreList, paperQuestionsList, dynamicPaper, loginUserId, traineeType, historyPaper, optionList, paperInfo);
		// 只有提交了试卷才会更新练习次数
		paperInfo.setPaperPracticeTime(1);
		tepExaminePaperInfoMapper.plusNum(paperInfo);
		// 再次查询试卷信息,得到练习次数
		paperInfo = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		
		// 最终返回的数据
		Map<String, Object> data = new HashMap<>();
		// 根据试卷的查看答案时机来决定是否回显数据
		String viewResultTime = StrUtils.isEmpty(paperInfo.getViewResultTime()) ? null : paperInfo.getViewResultTime();
		// 表示本套试卷是试卷库里边创建的,提交试卷就能查看答案
		if (viewResultTime == null) { 
			data = handleIsViewExamResult(historyPaper, paperInfo, paperScores, dynamicPaper);
		}else {
			// 表示本套试卷是测试活动来的，回显数据
			if (viewResultTime != null) {
				// 测试活动结束后才可以查看答案
				if ("1".equals(paperInfo.getViewResultTime())) {
					params.clear();
					params.put("paperId", paperInfo.getPaperId());
					List<Map<String,Object>> viewResult = tepExaminePaperInfoMapper.selectViewResultTime(params);
					for (Map<String, Object> map : viewResult) {
						if ("2".equals(map.get("activityState"))) {
							data = handleIsViewExamResult(historyPaper, paperInfo, paperScores, dynamicPaper);
						}else {
							return R.error("测试活动未结束，无法查看答案");
						}
					}
				}
			}
		}
		
		return R.ok("试卷提交成功").put(Constant.R_DATA, data);
	}

	/**
	 * 处理提交试卷是否回显数据
	 * @param historyPaper 历史试卷
	 * @param paperInfo 试卷基本信息
	 * @param paperScores 试卷的成绩
	 * @param dynamicPaper 
	 * @return
	 */
	private Map<String, Object> handleIsViewExamResult(TevglExamineHistoryPaper historyPaper,
			TepExaminePaperInfo paperInfo, List<TevglExaminePaperScore> paperScores, TevglExamineDynamicPaper dynamicPaper) {
		Map<String, Object> params = new HashMap<>();
		// 首先查询填空题内容
		params.put("historyId", historyPaper.getHistoryId());
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<Map<String, Object>> gapContentList = tevglExaminePaperScoreGapfillingMapper.selectSimpleListMapByMap(params);
		// 查询所有题目
		params.clear();
		params.put("historyId", historyPaper.getHistoryId());
		List<Map<String, Object>> questionsList = tevglExaminePaperScoreMapper.selectSimpleListMap(params);
		// 查询提交的作答记录
		Map<String, Object> data = viewExamResult(gapContentList, questionsList, paperInfo, params, dynamicPaper);
		data.put("history", historyPaper);
		data.put("paper", getSimplePaperInfo(paperInfo));  // 试卷信息
		data.put("paperScores", paperScores);  // 提交作答信息内容
		return data;
	}

	/**
	 * 计算试卷得分
	 * @param paperScores 用户提交的作答数据
	 * @param paperScoreList 试卷成绩信息
	 * @param paperQuestionsList 复合题数据
	 * @param dynamicPaper 正在做的这套试卷
	 * @param loginUserId 
	 * @param traineeType 
	 * @param historyPaper 历史记录
	 * @param optionList 题目选项数据
	 * @param paperInfo 试卷基本信息
	 */
	private void calculationPaperScore(List<TevglExaminePaperScore> paperScores, List<TevglExaminePaperScore> paperScoreList, List<Map<String, Object>> paperQuestionsList, TevglExamineDynamicPaper dynamicPaper, String loginUserId, String traineeType,
			TevglExamineHistoryPaper historyPaper, List<Map<String, Object>> optionList, TepExaminePaperInfo paperInfo) {
		// 查询历史填空题作答内容
		Map<String, Object> params = new HashMap<>();
		params.put("historyId", historyPaper.getHistoryId());
		List<TevglExaminePaperScoreGapfilling> gapfillingsList = tevglExaminePaperScoreGapfillingMapper.selectListByMap(params);
		// 本套试卷的复合题
		List<Map<String, Object>> compoundList = paperQuestionsList.stream().filter(a -> "6".equals(a.get("questionsType"))).collect(Collectors.toList());
		// 计算分数 
		// 总分
		Float sum = 0f;
		// 正确数
		int correctNum = 0;
		// 错误数
		int errorNum = 0;
		// 等待新增的数据
		List<TevglExaminePaperScore> insertScoreList = new ArrayList<>();
		List<TevglExaminePaperScoreGapfilling> insertScoreGapfillingList = new ArrayList<>();
		// 等待更新的数据
		List<TevglExaminePaperScore> updateScoreList = new ArrayList<>();
		List<TevglExaminePaperScoreGapfilling> updateScoreGapfillingList = new ArrayList<>();
		List<TevglQuestionsInfo> updateQuestionList = new ArrayList<>();
		for (TevglExaminePaperScore paperScore : paperScores) {
			boolean anyMatch = compoundList.stream().anyMatch(a -> StrUtils.isNotEmpty(paperScore.getQuestionsId()) && a.get("questionsId").equals(paperScore.getQuestionsId()));
			if (anyMatch) {
				continue;
			}
			// 获取试卷题目对应的分数
			String questionsScore = StrUtils.isEmpty(paperScore.getQuestionsScore()) ? "0" : paperScore.getQuestionsScore();
			List<Map<String, Object>> questionsList = paperQuestionsList.stream().filter(a -> a.get("questionsId").equals(paperScore.getQuestionsId())).collect(Collectors.toList());
			if (questionsList != null && questionsList.size() > 0) {
				questionsScore = questionsList.get(0).get("questionsScore").toString();
			}
			paperScore.setQuestionsScore(questionsScore);
			// 根据题目id查找题目信息
			TevglQuestionsInfo questionsInfo = tevglQuestionsInfoMapper.selectObjectById(paperScore.getQuestionsId());
			// 更新题目的正确数、作答数以及错误数
			TevglQuestionsInfo tevglQuestionsInfo = new TevglQuestionsInfo();
			tevglQuestionsInfo.setQuestionsId(paperScore.getQuestionsId());
			tevglQuestionsInfo.setQuestionsAnswerNum(1);
			tevglQuestionsInfoMapper.plusNum(tevglQuestionsInfo);
			List<TevglExaminePaperScore> examinePaperScores = paperScoreList.stream().filter(a -> a.getQuestionsId().equals(paperScore.getQuestionsId())).collect(Collectors.toList());
			// 如果没有成绩则新增
			if (examinePaperScores == null || examinePaperScores.size() == 0) {
				paperScore.setScoreId(Identities.uuid());
				paperScore.setHistoryId(historyPaper.getHistoryId());
				paperScore.setTraineeId(loginUserId);
				paperScore.setReplyId(paperScore.getReplyId());
				paperScore.setState("Y");
				paperScore.setTraineeType(traineeType);
				
				// 简答题需要教师手动评分
				if ("4".equals(questionsInfo.getQuestionsType())) {
					paperScore.setQuestionsScore(null);
					paperScore.setIsCorrect(null);
				}
				// 单选题多选题判断题系统评分
				if (Arrays.asList("1", "2", "3").contains(questionsInfo.getQuestionsType())) {
					boolean isRight = checkIsRight(questionsInfo.getReplyIds(), paperScore.getReplyId(), questionsInfo.getQuestionsType());
					// 答案正确
					if (isRight) {
						paperScore.setIsCorrect("Y");  // 答案正确
						// 记录正确数
						correctNum++; 
						// 计算试卷的总成绩
						if ("1".equals(dynamicPaper.getDynamicState())) {
							sum += Float.valueOf(paperScore.getQuestionsScore());
						}
						// 题目组卷数
						tevglQuestionsInfo.setQuestionsCorrectNum(1);
					} else {
						// 记录错误数
						errorNum++;
						paperScore.setIsCorrect("N");
						// 题目错误数
						tevglQuestionsInfo.setQuestionsErrorNum(1);
					}
				}
				// 存储用户选择的答案
				List<String> userAnswerList = new ArrayList<String>();
				// 插入填空题的回答
				if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
					for (int i = 0; i < paperScore.getGapFillingList().size(); i++) {
						Map<String, Object> gapfillData = paperScore.getGapFillingList().get(i);
						// 填充填空题数据
						TevglExaminePaperScoreGapfilling gapfilling = new TevglExaminePaperScoreGapfilling();
						gapfilling.setId(Identities.uuid());
						gapfilling.setHistoryId(historyPaper.getHistoryId());
						gapfilling.setScoreId(paperScore.getScoreId());
						gapfilling.setQuestionsId(paperScore.getQuestionsId());
						gapfilling.setTraineeId(dynamicPaper.getTraineeId());
						gapfilling.setContent(StrUtils.isNull(gapfillData.get("content")) ? "" : gapfillData.get("content").toString());
						gapfilling.setSortNum(i);
						tevglExaminePaperScoreGapfillingMapper.insert(gapfilling);
						// 如果填空题的内容不为空就存储到集合中
						userAnswerList.add(StrUtils.isNull(gapfillData.get("content")) ? null : gapfillData.get("content").toString());
					}
					// 如果为填空时
					if ("5".equals(questionsInfo.getQuestionsType())) {
						// 取出本题填空的标准答案
						List<Map<String, Object>> questionsIds = optionList.stream().filter(option -> option.get("questionsId").equals(paperScore.getQuestionsId())).collect(Collectors.toList());
						List<Object> gapContents = questionsIds.stream().map(a -> a.get("content")).collect(Collectors.toList());
						System.out.println("gapContents： " + gapContents);
						System.out.println("userAnswerList: " + userAnswerList);
						// 填空得分规则1只要有一空是对的也计分，2题目填空全部答对得分
						// ①只要有一空是对的也计分
						if ("1".equals(paperInfo.getGapFillingScoreStandard())) {
							for (int i = 0; i < gapContents.size(); i++) {
								// 如果数据库存放的答案和用户选择的答案一致则标识该题正确
								if (gapContents.get(i).equals(userAnswerList.get(i)) && userAnswerList.size() > 0 && gapContents.size() > 0) {
									paperScore.setIsCorrect("Y");  // 答案正确
									System.out.println("只要有一空是正确就为Y： " + paperScore.getIsCorrect());
									// 要全对才记录正确数
									//correctNum++; 
									// 计算试卷的成绩总分
									if ("1".equals(dynamicPaper.getDynamicState())) {
										sum += Float.valueOf(paperScore.getQuestionsScore());
									}
									// 题目组卷数
									tevglQuestionsInfo.setQuestionsCorrectNum(1);
								}else {
									// 记录错误数
									errorNum++;
									// 如果都不对说明该题错误
									paperScore.setIsCorrect("N");
									// 题目错误数
									tevglQuestionsInfo.setQuestionsErrorNum(1);
									System.out.println("如果都不正确为N: " + paperScore.getIsCorrect());
								}
							}
						}
						// ②题目填空全部答对得分
						if ("2".equals(paperInfo.getGapFillingScoreStandard())) {
							// 如果标准答案和用户答案完全一致才标识该题正确
							if (gapContents.equals(userAnswerList)) {
								paperScore.setIsCorrect("Y");  // 正确
								correctNum++;  // 正确数
								if ("1".equals(dynamicPaper.getDynamicState())) {
									sum += Float.valueOf(paperScore.getQuestionsScore()) * gapContents.size();  // 计算总分
								}
							}else {
								paperScore.setIsCorrect("N"); 
								errorNum++;  // 错误数
								// 题目错误数
								tevglQuestionsInfo.setQuestionsErrorNum(1);
							}
						}
					}
				}
				// 得分情况记录到数据库中
				tevglExaminePaperScoreMapper.insert(paperScore);
			} else {
				for (TevglExaminePaperScore examinePaperScore : examinePaperScores) {
					// 填充信息
					examinePaperScore.setReplyId(paperScore.getReplyId());
					examinePaperScore.setContent(paperScore.getContent());
					examinePaperScore.setTraineeType(traineeType);
					// 简答题系统不评分
					if ("4".equals(questionsInfo.getQuestionsType())) {
						examinePaperScore.setScore(null);
						examinePaperScore.setIsCorrect(null);
					}
					// 单选题多选题判断评分
					if (Arrays.asList("1", "2", "3").contains(questionsInfo.getQuestionsType())) {
						boolean isRight = checkIsRight(questionsInfo.getReplyIds(), paperScore.getReplyId(), questionsInfo.getQuestionsType());
						if (isRight) {
							examinePaperScore.setIsCorrect("Y");
							correctNum++; // 正确数
							if ("1".equals(dynamicPaper.getDynamicState())) {
								sum += Float.valueOf(paperScore.getQuestionsScore());  // 计算总分
							}
							tevglQuestionsInfo.setQuestionsCorrectNum(1);  // 题目组卷数
						}else {
							// 记录错误数
							errorNum++;
							examinePaperScore.setIsCorrect("N");
							// 题目错误数
							tevglQuestionsInfo.setQuestionsErrorNum(1);
						}
						
					}
					// 更新填空题的回答
					if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
						List<String> userAnswerList = new ArrayList<>();
						List<TevglExaminePaperScoreGapfilling> gapfillings = gapfillingsList.stream().filter(a -> a.getQuestionsId().equals(examinePaperScore.getQuestionsId()) && a.getTraineeId().equals(loginUserId)).collect(Collectors.toList());
						if (gapfillings != null && gapfillings.size() > 0) {
							for (int i = 0; i < gapfillings.size(); i++) {
								// 正确答案
								TevglExaminePaperScoreGapfilling existData = gapfillings.get(i);
								// 用户选择的答案
								Map<String, Object> userSelectedData = paperScore.getGapFillingList().get(i);
								existData.setContent(StrUtils.isNull(userSelectedData.get("content")) ? "" : userSelectedData.get("content").toString());
								userAnswerList.add(StrUtils.isNull(userSelectedData.get("content")) ? null : userSelectedData.get("content").toString());
								// 修改
								updateScoreGapfillingList.add(existData);
							}
						}
						
						// 如果为填空时
						if ("5".equals(questionsInfo.getQuestionsType())) {
							// 取出本题填空的标准答案
							List<Object> gapContents = optionList.stream().filter(option -> option.get("questionsId").equals(paperScore.getQuestionsId())).map(a -> a.get("content")).collect(Collectors.toList());
							// 填空得分规则1只要有一空是对的也计分，2题目填空全部答对得分
							// ①只要有一空是对的也计分
							if ("1".equals(paperInfo.getGapFillingScoreStandard())) {
								for (int i = 0; i < gapContents.size(); i++) {
									// 如果数据库存放的答案和用户选择的答案一致则标识该题正确
									if (gapContents.get(i).equals(userAnswerList.get(i))) {
										examinePaperScore.setIsCorrect("Y");  // 答案正确
										// 记录正确数
										//correctNum++; 
										// 计算试卷的成绩总分
										if ("1".equals(dynamicPaper.getDynamicState())) {
											sum += Float.valueOf(paperScore.getQuestionsScore()) * gapContents.size();
										}
										// 题目组卷数
										tevglQuestionsInfo.setQuestionsCorrectNum(1);
									}else {
										// 记录错误数
										errorNum++;
										// 如果都不对说明该题错误
										paperScore.setIsCorrect("N");
										// 题目错误数
										tevglQuestionsInfo.setQuestionsErrorNum(1);
									}
								}
							}
							// ②题目填空全部答对得分
							if ("2".equals(paperInfo.getGapFillingScoreStandard())) {
								// 如果标准答案和用户答案完全一致才标识该题正确
								if (gapContents.equals(userAnswerList)) {
									examinePaperScore.setIsCorrect("Y");  // 正确
									correctNum++;  // 正确数
									if ("1".equals(dynamicPaper.getDynamicState())) {
										sum += Float.valueOf(paperScore.getQuestionsScore()) * gapContents.size();  // 计算总分
									}
								}else {
									examinePaperScore.setIsCorrect("N");
									// 记录错误数
									errorNum++;
									// 题目错误数
									tevglQuestionsInfo.setQuestionsErrorNum(1);
								}
							}
						}
					}
					updateScoreList.add(examinePaperScore);
				}
			}
			// 设置问题正确率
			Integer inCorrectNum = (questionsInfo.getQuestionsConstructingNum() == null ? 0 : questionsInfo.getQuestionsConstructingNum())
					+ (tevglQuestionsInfo.getQuestionsCorrectNum() == null ? 0 : tevglQuestionsInfo.getQuestionsCorrectNum());
			Integer answerNum = (questionsInfo.getQuestionsAnswerNum() == null ? 0 : questionsInfo.getQuestionsAnswerNum())
					+ (tevglQuestionsInfo.getQuestionsAnswerNum() == null ? 0 : tevglQuestionsInfo.getQuestionsAnswerNum());
			// 当题目正确数和作答数大于0时计算正确率
			if (inCorrectNum > 0 && answerNum > 0) {
				
				Float avg = Float.valueOf(inCorrectNum) / Float.valueOf(answerNum) * 100;
				NumberFormat ddf1 = NumberFormat.getNumberInstance();
				ddf1.setMaximumFractionDigits(2);
				try {
					Number parse = ddf1.parse(ddf1.format(avg));
					questionsInfo.setQuestionsAccuracy(new BigDecimal(parse.toString()));
					updateQuestionList.add(questionsInfo);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}else {
				questionsInfo.setQuestionsAccuracy(new BigDecimal(0));
				updateQuestionList.add(questionsInfo);
			}
		}
		// 批量新增
		if (insertScoreList.size() > 0) {
			tevglExaminePaperScoreMapper.insertBatch(insertScoreList);
		}
		if (insertScoreGapfillingList.size() > 0) {
			tevglExaminePaperScoreGapfillingMapper.insertBatch(insertScoreGapfillingList);
		}
		// 批量更新
		if (updateScoreList.size() > 0) {
			tevglExaminePaperScoreMapper.updateBatchByDuplicateKey(updateScoreList);
		}
		if (updateScoreGapfillingList.size() > 0) {
			tevglExaminePaperScoreGapfillingMapper.batchUpdateContentByDuplicate(updateScoreGapfillingList);
		}
		if (updateQuestionList.size() > 0) {
			tevglQuestionsInfoMapper.plusNumBatchByCaseWhen(updateQuestionList);
		}
		// 更新动态试卷表为完成状态
		dynamicPaper.setPaperIsFinished("Y");
		tevglExamineDynamicPaperMapper.update(dynamicPaper);
		// 学员交卷时间
		historyPaper.setPaperEndTime(DateUtils.getNowTimeStamp());
		// 设置题目正确率
		if (paperScores == null || paperScores.size() == 0) {
			// 设置最后成绩
			historyPaper.setPaperAccuracy("0.0");
			historyPaper.setPaperFinalScore("0.0");
		}else {
			BigDecimal bigDecimal = (correctNum + errorNum) == 0 ? new BigDecimal("0") : new BigDecimal(correctNum).divide(new BigDecimal(correctNum + errorNum), 2, BigDecimal.ROUND_DOWN);
			historyPaper.setPaperAccuracy(bigDecimal.toString());
			if ("1".equals(dynamicPaper.getDynamicState())) {
				historyPaper.setPaperFinalScore(sum.toString());
			}
		}
		// 更新入库
		tevglExamineHistoryPaperMapper.update(historyPaper);
	}

	/**
	 * 验证（单选、多选、判断）题目标准答案，与用户选择的答案，返回true标识此题答对
	 * @param questionReplyIds 题目的正确答案（选项表主键id），多个英文逗号隔开
	 * @param userReplyId 用户选择的答案（选项表主键id），多个英文逗号隔开
	 * @param questionsType 当前题目类型
	 * @return
	 */
	private boolean checkIsRight(String questionReplyIds, String userReplyId, String questionsType) {
		if (Arrays.asList("1", "2", "3").contains(questionsType)) {
			if (StrUtils.isEmpty(questionReplyIds) || StrUtils.isEmpty(userReplyId)) {
				return false;
			}
			String[] split = questionReplyIds.split(",");
			String[] split2 = userReplyId.split(",");
			List<String> a = Stream.of(split).collect(Collectors.toList());
			List<String> b = Stream.of(split2).collect(Collectors.toList());
			boolean isRight = CollectionUtils.isEqualCollection(a, b);
			return isRight;
		}
		return false;
	}

	/**
	 * 每隔30秒提交一次题目答案 需要传递题目号
	 * @param dyId
	 * @param list 提交的数据
	 * @param loginUserId
	 * @param traineeType
	 * @return
	 */
	@Override
	public R paperCommit(String dyId,  List<TevglExaminePaperScore> list, String loginUserId, String traineeType) {
		if (StrUtils.isEmpty(dyId)) {
			return R.error("要提交的试卷不存在");
		}
		TevglExamineDynamicPaper dynamicPaper = tevglExamineDynamicPaperMapper.selectObjectById(dyId);
		if (dynamicPaper == null) {
			return R.error("试卷信息为空，请刷新页面后重试");
		}
		TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		if (paperInfo == null) {
			return R.error("试卷不存在，请重新选择");
		}
		Map<String, Object> params = new HashMap<>();
		// 根据动态试卷id查找历史表记录
		params.put("dyId", dyId);
		List<TevglExamineHistoryPaper> historyPapers = tevglExamineHistoryPaperMapper.selectListByMap(params);
		TevglExamineHistoryPaper historyPaper = new TevglExamineHistoryPaper();
		if (historyPapers == null || historyPapers.size() == 0) {
			return R.error("数据异常，无法提交");
		}
		historyPaper = historyPapers.get(0);
		params.clear();
		// 查询已存在的填空作答记录
		params.put("historyId", historyPaper.getHistoryId());
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TevglExaminePaperScoreGapfilling> gapfillingsList = tevglExaminePaperScoreGapfillingMapper.selectListByMap(params);
		// 先找到本次提交的所有信息
		params.clear();
		params.put("historyId", historyPaper.getHistoryId());
		params.put("questionsIdList", list.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList()));
		List<TevglExaminePaperScore> allExaminePaperScoresList = tevglExaminePaperScoreMapper.selectListByMap(params);
		// 等待新增的数据
		List<TevglExaminePaperScore> insertScoreList = new ArrayList<>();
		List<TevglExaminePaperScoreGapfilling> insertScoreGapfillingList = new ArrayList<>();
		// 等待更新的数据
		List<TevglExaminePaperScore> updateScoreList = new ArrayList<>();
		List<TevglExaminePaperScoreGapfilling> updateScoreGapfillingList = new ArrayList<>();
		for (TevglExaminePaperScore paperScore : list) {
			// 查询历史学员作答表
			List<TevglExaminePaperScore> examinePaperScoresList = allExaminePaperScoresList.stream().filter(a -> a.getQuestionsId().equals(paperScore.getQuestionsId())).collect(Collectors.toList());
			// 如果历史作答表没有记录则新增
			if (examinePaperScoresList == null || examinePaperScoresList.size() == 0) {
				paperScore.setScoreId(Identities.uuid());
				paperScore.setHistoryId(historyPaper.getHistoryId());
				paperScore.setTraineeId(dynamicPaper.getTraineeId());
				paperScore.setTraineeType(traineeType);
				paperScore.setState("Y");
				//tevglExaminePaperScoreMapper.insert(paperScore);
				insertScoreList.add(paperScore);
				// 插入填空题回答
				if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
					for (int i = 0; i < paperScore.getGapFillingList().size(); i++) {
						// 用户输入的
						Map<String, Object> gapData = paperScore.getGapFillingList().get(i);
						TevglExaminePaperScoreGapfilling gapfilling = new TevglExaminePaperScoreGapfilling();
						gapfilling.setId(Identities.uuid());
						gapfilling.setHistoryId(historyPaper.getHistoryId());
						gapfilling.setScoreId(paperScore.getScoreId());
						gapfilling.setQuestionsId(paperScore.getQuestionsId());
						gapfilling.setTraineeId(dynamicPaper.getTraineeId());
						gapfilling.setContent(StrUtils.isNull(gapData.get("content")) ? "" : gapData.get("content").toString());
						gapfilling.setSortNum(i);
						insertScoreGapfillingList.add(gapfilling);
					}
				}
			}else {
				for (TevglExaminePaperScore tevglExaminePaperScore : examinePaperScoresList) {
					// 填充信息
					tevglExaminePaperScore.setContent(paperScore.getContent());
					// 判断选择是否一致 如果不一致代表已经修改过了
					boolean flag = StrUtils.isNotEmpty(tevglExaminePaperScore.getReplyId()) && StrUtils.isNotEmpty(paperScore.getReplyId()) && tevglExaminePaperScore.getReplyId().equals(paperScore.getReplyId());
					if (!flag) {
						tevglExaminePaperScore.setReplyId(paperScore.getReplyId());
					}
					updateScoreList.add(tevglExaminePaperScore);
					// 更新填空题的回答
					if (paperScore.getGapFillingList() != null && paperScore.getGapFillingList().size() > 0) {
						for (int i = 0; i < paperScore.getGapFillingList().size(); i++) {
							List<TevglExaminePaperScoreGapfilling> collect = gapfillingsList.stream().filter(a -> a.getQuestionsId().equals(tevglExaminePaperScore.getQuestionsId()) && a.getTraineeId().equals(loginUserId)).collect(Collectors.toList());
							// 数据库存在的记录
							TevglExaminePaperScoreGapfilling existData = collect.get(i);
							// 用户传入的
							Map<String, Object> userSelectedData = paperScore.getGapFillingList().get(i);
							existData.setContent(StrUtils.isNull(userSelectedData.get("content")) ? "" : userSelectedData.get("content").toString());
							updateScoreGapfillingList.add(existData);
						}
					}
				}
			}
		}
		// 批量新增
		if (insertScoreList.size() > 0) {
			tevglExaminePaperScoreMapper.insertBatch(insertScoreList);
		}
		if (insertScoreGapfillingList.size() > 0) {
			tevglExaminePaperScoreGapfillingMapper.insertBatch(insertScoreGapfillingList);
		}
		// 批量更新
		if (updateScoreList.size() > 0) {
			tevglExaminePaperScoreMapper.updateBatchByDuplicateKey(updateScoreList);
		}
		if (updateScoreGapfillingList.size() > 0) {
			tevglExaminePaperScoreGapfillingMapper.batchUpdateContentByDuplicate(updateScoreGapfillingList);
		}
		return R.ok("数据定时保存成功").put(Constant.R_DATA, list);
	}

	/**
	  *查看提交了的试卷信息
	 * @author zhouyl加
	 * @date 2021年1月6日
	 * @param gapContentList 填空题答案
	 * @param questionsList 所有题目
	 * @param paperInfo 试卷信息
	 * @param params
	 * @param dynamicPaper
	 * @return
	 */
	private Map<String, Object> viewExamResult(List<Map<String, Object>> gapContentList, List<Map<String, Object>> questionsList, 
			TepExaminePaperInfo paperInfo, Map<String, Object> params, TevglExamineDynamicPaper dynamicPaper) {
		Map<String, Object> data = new HashMap<>();
		// 选择题
		List<Map<String, Object>> choseScores = new ArrayList<>();
		// 判断题
		List<Map<String, Object>> judgeScores = new ArrayList<>();
		// 问答题
		List<Map<String, Object>> shortAnswerList = new ArrayList<>();
		// 填空题
		List<Map<String, Object>> gapFillingList = new ArrayList<>();
		// 复合题
		List<Map<String, Object>> compoundList = new ArrayList<>();

		if (questionsList != null && questionsList.size() > 0) {
			// 字典转换
			converShowName(questionsList);
			//convertUtil.convertDict(questionsList, "questionsTypeName", "questions_type");
			//convertUtil.convertDict(questionsList, "questionsComplexityName", "questions_complexity");
			// 查询所有题目id
			List<Object> questionsIds = questionsList.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
			params.clear();
			// 查询题目id对应的选项
			params.put("questionsIds", questionsIds);
			List<Map<String, Object>> questionList = tevglQuestionsInfoMapper.selectSimpleListMap(params);
			List<Map<String, Object>> questionsChoseList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
			questionsList.stream().forEach(questions -> {
				questionList.stream().forEach(questionInfo -> {
					questions.put("questionsCorrectNum", questionInfo.get("questionsCorrectNum"));  // 组卷次数
					questions.put("questionsAccuracy", questionInfo.get("questionsAccuracy"));  // 题目正确率
				});
				List<Map<String, Object>> optionList = questionsChoseList.stream().filter(option -> option.get("questionsId").equals(questions.get("questionsId"))).collect(Collectors.toList());
				questions.put("optionList", optionList);
				// 处理单选多选判断题的正确答案
				handleModleAnswer(questions, optionList);
				// 返回简答题得分值与学员实际得分值
				if ("4".equals(questions.get("questionsType"))) {
					questions.put("shortAnswerScore", paperInfo.getShortAnswerScore());
					questions.put("userScore", StrUtils.isNull(questions.get("score")) ? "0" : questions.get("score").toString());
				}
				// 返回填空题的填空个数
				if ("5".equals(questions.get("questionsType"))) {
					questions.put("gapFillingNumber", optionList.size());
				}
			});
			// 区分单选多选判断问答填空,1单选2多选3判断4问答5填空
			// 如果题目没有父题目id则不查
			choseScores = questionsList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && Arrays.asList("1", "2").contains(question.get("questionsType"))).collect(Collectors.toList());
			judgeScores = questionsList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && question.get("questionsType").equals("3")).collect(Collectors.toList());
			shortAnswerList = questionsList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && question.get("questionsType").equals("4")).collect(Collectors.toList());
			gapFillingList = questionsList.stream().filter(question -> StrUtils.isNull(question.get("parentId")) && question.get("questionsType").equals("5")).collect(Collectors.toList());
			// 返回用户填空题的作答
			gapFillingList.stream().forEach(gf -> {
				gf.put("userSelectedAnswer", gapContentList.stream().filter(a -> a.get("traineeId").equals(gf.get("traineeId")) && a.get("scoreId").equals(gf.get("scoreId"))).collect(Collectors.toList()));
			});
			
			// 取出复合题的父题目
			List<Map<String, Object>> allChildrenQuestionsList = questionsList.stream().filter(question -> !StrUtils.isNull(question.get("parentId"))).collect(Collectors.toList());
			List<Object> parentIdList = allChildrenQuestionsList.stream().filter(question -> !StrUtils.isNull(question.get("parentId"))).map(question -> question.get("parentId")).collect(Collectors.toList());
			if (parentIdList != null && parentIdList.size() > 0) {
				params.clear();
				params.put("questionsIds", parentIdList);
				compoundList = tevglQuestionsInfoMapper.selectSimpleListMap(params);
				// 转换题目类型名称(1单选2多选3判断4简答5填空6复合)
				tevglActivityTestPaperServiceImple.converShowName(compoundList);
				compoundList.stream().forEach(compound -> {
					// 取出复合题的子题目
					List<Map<String, Object>> childQuestionsList = allChildrenQuestionsList.stream().filter(parentQuestion -> !StrUtils.isNull(parentQuestion.get("parentId")) && parentQuestion.get("parentId").equals(compound.get("questionsId"))).collect(Collectors.toList());
					childQuestionsList.stream().forEach(childQuestion ->{
						if ("5".equals(childQuestion.get("questionsType"))) {
							childQuestion.put("userAnswerList", gapContentList.stream().filter(a -> a.get("traineeId").equals(childQuestion.get("traineeId")) && a.get("scoreId").equals(childQuestion.get("scoreId"))).collect(Collectors.toList()));
						}
						// 返回复合题每小题分数
						childQuestion.put("questionsScore", paperInfo.getCompositeScore());
					});
					// 转换题目类型(1简单2普通3困难)
					tevglActivityTestPaperServiceImple.converShowName(childQuestionsList);
					compound.put("questionList", childQuestionsList);
					// 记录复合题每小题得分值
					compound.put("questionsScore", paperInfo.getCompositeScore());
					// 记录复合题总分
					int size = childQuestionsList == null ? 0 : childQuestionsList.size();
					// 总分 = 题目个数 * 复合题每小题得分数
					BigDecimal questionsTotalScore = new BigDecimal(size).multiply(new BigDecimal(paperInfo.getCompositeScore()));
					compound.put("questionsTotalScore", questionsTotalScore);
				});
			}
		}
		data.put("choseScores", choseScores);
		data.put("judgeScores", judgeScores);
		data.put("shortAnswerList", shortAnswerList);
		data.put("gapFillingList", gapFillingList);
		data.put("compoundList", compoundList);
		// 试卷详情
		TepExaminePaperInfo paper = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		data.put("paper", getSimplePaperInfo(paper));  // 试卷信息
		data.put("paperDetails", calculationQuestionsDetail(choseScores, judgeScores, paper.getPaperType()));  // 计算选择题、判断题分数
		return data;
	}

	/**
	 * 处理标准答案和用户选择的答案
	 * @param questions 用户选择的答案
	 * @param optionList 题目的选项
	 */
	private void handleModleAnswer(Map<String, Object> questions, List<Map<String, Object>> optionList) {
		if (!StrUtils.isNull(questions.get("replyIds")) && Arrays.asList("1", "2", "3").contains(questions.get("questionsType"))) {
			// 正确答案
			String existReplyId = questions.get("replyIds").toString();
			String[] split = existReplyId.split(",");
			// 用户选择的答案
			String[] userSelectedSplit = null;
			if (!StrUtils.isNull(questions.get("replyId"))) {
				String userSelectedReply = questions.get("replyId").toString();
				userSelectedSplit = userSelectedReply.split(",");
			}
			for (Map<String, Object> option : optionList) {
				option.put("isUserSelected", false);
				// 可用于回显题目的正确答案
				option.put("isModelAnswer", false);
				for (String reply : split) {
					if (option.get("optionId").equals(reply)) {
						option.put("isModelAnswer", true);
					}
				}
				
				// 用于回显用户选择的答案
				if (userSelectedSplit != null) {
					for (String userSelected : userSelectedSplit) {
						if (option.get("optionId").equals(userSelected)) {
							option.put("isUserSelected", true);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 【试卷库】教师组卷选题页面
	 * @param subjectId 课程id
	 * @param chapterId 章节id
	 * @param type 要组卷的题目来源是课程下的还是章节下的  类型(1 课程 2 章节)
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R teacherGenerateTestPaper(String subjectId, String chapterId, String type, String loginUserId) {
		if (StrUtils.isEmpty(type) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 最终返回的数据
		Map<String, Object> info = new HashMap<>();
		switch (type) {
		case "1":
			TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(subjectId);
			if (subject != null) {
				// 课程信息
				Map<String, Object> chapterInfo = new HashMap<>();
				chapterInfo.put("subjectId", subject.getSubjectId());
				chapterInfo.put("subjectName", subject.getSubjectName());
				info.put("chapter", chapterInfo);
				// 统计题目的数量
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("subjectId", subjectId);
				params.put("state", "Y");
				Map<String, Object> questionsNum = tevglQuestionsInfoMapper.countQuestionNumByMap2(params);
				info.put("questionsNum", questionsNum);
			}
			break;
		case "2":
			TevglBookChapter chapter = tevglBookChapterMapper.selectObjectById(chapterId);
			if (chapter != null) {
				Map<String, Object> chapterInfo = new HashMap<>();
				chapterInfo.put("chapterId", chapter.getChapterId());
				chapterInfo.put("chapterName", chapter.getChapterName());
				info.put("chapterInfo", chapterInfo);
				Map<String, Object> params = new HashMap<>();
				params.put("chapterId", chapterId);
				params.put("state", "Y");
				Map<String, Object> questionsNum = tevglQuestionsInfoMapper.countQuestionNumByMap2(params);
				info.put("questionsNum", questionsNum);
			}
			break;
		default:
			break;
		}
		return R.ok().put(Constant.R_DATA, info);
	}
	
	/**
	 * 查看该章节组卷的题目信息
	 * @author zhouyl加
	 * @date 2021年1月16日
	 * @param choseChapters
	 * @return
	 */
	@Override
	public Map<String, Object> generateTestPaperQuestionsRandom(@RequestBody List<Map<String, Object>> choseChapters) {
		if (choseChapters == null || choseChapters.size() == 0) {
			return R.error("没有检测到题目，请重新编辑");
		}
		// 最终返回的数据
		Map<String, Object> result = new HashMap<>();
		// 选择题题目集
		List<Map<String, Object>> choiceQuestions = new ArrayList<>();
		// 判断题题目集
		List<Map<String, Object>> judgeQuestions = new ArrayList<>();
		// 简答题题目集
		List<Map<String, Object>> shortAnswerQuestions = new ArrayList<>();
		// 填空题题目集
		List<Map<String, Object>> completionQuestions = new ArrayList<>();
		// 复合题题目集
		List<Map<String, Object>> compoundQuestions = new ArrayList<>();
		// 章节下的题目集
		List<Map<String, Object>> chapterQuestions = new ArrayList<>();
		// 课程节点
		List<Map<String, Object>> subjectNode = new ArrayList<>();
		// 章节节点
		List<Map<String, Object>> chapterNode = new ArrayList<>();
		// 条件查询
		Map<String, Object> params = new HashMap<>();
		
		for (Map<String, Object> node : choseChapters) {
			// 1表示课程节点2表示章节节点
			Object type = node.get("type");
			// 处理条件
			if ("1".equals(type)) {
				// 多课程题目组卷
				subjectNode.add(node);
			} else {
				// 多章节题目组卷
				chapterNode.add(node);
			}
		}
		int chapterNum = 0;
		if (chapterNode != null && chapterNode.size() > 0) {
			for (int i = 0; i < chapterNode.size(); i++) {
				// 单选题题目数
				Object singleChoiceNum = chapterNode.get(i).get("singleChoiceNum");
				// 多选题题目数
				Object multipleChoiceNum = chapterNode.get(i).get("multipleChoiceNum");
				// 判断题题目数
				Object judgeChoiceNum = chapterNode.get(i).get("judgeChoiceNum");
				// 简答题题目数
				Object shortAnswerNum = chapterNode.get(i).get("shortAnswerNum");
				// 填空题题目数
				Object completionNum = chapterNode.get(i).get("completionNum");
				// 复合题题目数
				Object compoundNum = chapterNode.get(i).get("compoundNum");
				// 处理条件
				params.put("chapterIds", chapterNode.get(i).get("chapterId"));
				// 记录章节数
				chapterNum++;
				// 先随机章节的题目
				// 随机单选题
				if (!StrUtils.isNull(singleChoiceNum) && !"0".equals(singleChoiceNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "1");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", singleChoiceNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> choiceQuestionList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					if (choiceQuestionList != null && choiceQuestionList.size() > 0) {
						choiceQuestions.addAll(choiceQuestionList);
						chapterQuestions.addAll(choiceQuestionList);
					}
				}
				// 随机多选题
				if (!StrUtils.isNull(multipleChoiceNum) && !"0".equals(multipleChoiceNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "2");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", multipleChoiceNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> multipleChoiceQuestionList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					if (multipleChoiceQuestionList != null && multipleChoiceQuestionList.size() > 0) {
						choiceQuestions.addAll(multipleChoiceQuestionList);
						chapterQuestions.addAll(multipleChoiceQuestionList);
					}
				}
				// 随机判断题
				if (!StrUtils.isNull(judgeChoiceNum) && !"0".equals(judgeChoiceNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "3");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", judgeChoiceNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> judgeQuestionList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					if (judgeQuestionList != null && judgeQuestionList.size() > 0) {
						judgeQuestions.addAll(judgeQuestionList);
						chapterQuestions.addAll(judgeQuestionList);
					}
				}
				// 随机简答题
				if (!StrUtils.isNull(shortAnswerNum) && !"0".equals(shortAnswerNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "4");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", shortAnswerNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> shortAnswerList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					if (shortAnswerList != null && shortAnswerList.size() > 0) {
						shortAnswerQuestions.addAll(shortAnswerList);
						chapterQuestions.addAll(shortAnswerList);
					}
				}
				// 随机填空题
				if (!StrUtils.isNull(completionNum) && !"0".equals(completionNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "5");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", completionNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> completionList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					if (completionList != null && completionList.size() > 0) {
						completionQuestions.addAll(completionList);
						chapterQuestions.addAll(completionList);
					}
				}
				// 随机复合题
				if (!StrUtils.isNull(compoundNum) && !"0".equals(compoundNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "6");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", compoundNum); // 随机多少道题目
					params.put("noChildrenQuestion", null);  // 不随机复合题的子题目
					List<Map<String, Object>> compoundList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					if (compoundList != null && compoundList.size() > 0) {
						compoundQuestions.addAll(compoundList);
						chapterQuestions.addAll(compoundList);
					}
				}
				params.clear();
			}
		}
		// 再随机题目课程节点的相关题目
		int subjectNum = randomSubjectQuestionList(subjectNode, params, choiceQuestions, judgeQuestions, shortAnswerQuestions, completionQuestions, compoundQuestions);
		
		// 手动转换题目类型和题目的难以程度
		converShowName(choiceQuestions);
		converShowName(judgeQuestions);
		converShowName(shortAnswerQuestions);
		converShowName(completionQuestions);
		converShowName(compoundQuestions);
		
		// 处理题目选项
		handleQuestionList(choiceQuestions, null);
		handleQuestionList(judgeQuestions, null);
		handleQuestionList(shortAnswerQuestions, "4");
		handleQuestionList(completionQuestions, null);
		handleCompoundQuestionList(compoundQuestions, null);
		
		// 最终返回数据
		result.put("choiceQuestions", choiceQuestions);
		result.put("judgeQuestions", judgeQuestions);
		result.put("shortAnswerQuestions", shortAnswerQuestions);
		result.put("completionQuestions", completionQuestions);
		result.put("compoundQuestions", compoundQuestions);
		result.put("subjectNum", subjectNum == 0 ? 0 : subjectNum);
		result.put("chapterNum", chapterNum == 0 ? 0 : chapterNum);
		return result;
	}
	
	/**
	 * 手动组卷（根据用户勾选的题目，进行组卷）
	 * @param questionIdList
	 * @return
	 */
	@Override
	public R generateTestPaperQuestionsManual(List<String> questionIdList) {
		if (questionIdList == null || questionIdList.isEmpty()) {
			return R.error("请选择题目");
		}
		if (questionIdList.size() > 200) {
			return R.error("题目过多，不允许超过200道题目");
		}
		GeneratePaperVO vo = new GeneratePaperVO();
		// 题目
		List<GenerateQuestionVO> questionList = tevglQuestionsInfoMapper.findQuestionByQuestionIdList(questionIdList);
		// 题目选项
		List<GenerateOptionVO> optionList = tevglQuestionsInfoMapper.findQuestionOptionByQuestionIdList(questionIdList);
		questionList.stream().forEach(questionsInfo -> {
			List<GenerateOptionVO> children = optionList.stream().filter(option -> option.getQuestionsId().equals(questionsInfo.getQuestionsId())).collect(Collectors.toList());
			questionsInfo.setOptionList(children);
		});
		// 字典转换
		convertUtil.convertDict(questionList, "questionsTypeName", "questions_type");
		convertUtil.convertDict(questionList, "questionsComplexityName", "questions_complexity");
		// 选择题
		vo.setChoiceQuestions(questionList.stream().filter(a -> Arrays.asList("1", "2").contains(a.getQuestionsType())).collect(Collectors.toList()));
		// 判断题
		vo.setJudgeQuestions(questionList.stream().filter(a -> a.getQuestionsType().equals("3")).collect(Collectors.toList()));
		// 填空题
		vo.setCompletionQuestions(questionList.stream().filter(a -> a.getQuestionsType().equals("4")).collect(Collectors.toList()));
		// 简答题
		vo.setShortAnswerQuestions(questionList.stream().filter(a -> a.getQuestionsType().equals("5")).collect(Collectors.toList()));
		// 复合题
		List<GenerateQuestionVO> complexQuestionList = questionList.stream().filter(a -> a.getQuestionsType().equals("6")).collect(Collectors.toList());
		// 查询复合题的子题目
		List<String> complexQuestionIdList = complexQuestionList.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList());
		if (complexQuestionIdList != null && !complexQuestionIdList.isEmpty()) {
			List<GenerateQuestionVO> childrenQuestionList = tevglQuestionsInfoMapper.findQuestionByParentIdList(complexQuestionIdList);
			convertUtil.convertDict(childrenQuestionList, "questionsTypeName", "questions_type");
			convertUtil.convertDict(childrenQuestionList, "questionsComplexityName", "questions_complexity");
			List<GenerateOptionVO> complexQuestionOptionList = tevglQuestionsInfoMapper.findQuestionOptionByQuestionIdList(childrenQuestionList.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList()));
			complexQuestionList.stream().forEach(item -> {
				// 处理子题目
				List<GenerateQuestionVO> collect = childrenQuestionList.stream().filter(a -> a.getParentId().equals(item.getQuestionsId())).collect(Collectors.toList());
				collect.stream().forEach(o -> {
					o.setOptionList(complexQuestionOptionList.stream().filter(a -> a.getQuestionsId().equals(o.getQuestionsId())).collect(Collectors.toList()));
				});
				item.setChildren(collect);
			});
		}
		vo.setCompoundQuestions(complexQuestionList);
		return R.ok().put(Constant.R_DATA, vo);
	}
	/**
	 * 处理题目相关数据
	 * @param questionsList
	 * @param questionsType
	 */
	private void handleQuestionList(List<Map<String, Object>> questionsList, String questionsType) {
		if (questionsList == null || questionsList.size() == 0) {
			return ;
		}
		// 如果是简答题
		if ("4".equals(questionsType)) {
			questionsList.stream().forEach(questionInfo -> {
				if (!StrUtils.isNull(questionInfo.get("r"))) {
					questionInfo.remove("r");
				}
			});
		}
		
		Map<String, Object> params = new HashMap<>();
		List<Object> questionsIds = questionsList.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
		params.put("questionsIds", questionsIds);
		params.put("state", "Y");
		List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
		questionsList.stream().forEach(questionsInfo -> {
			List<Map<String, Object>> children = optionList.stream().filter(option -> option.get("questionsId").equals(questionsInfo.get("questionsId"))).collect(Collectors.toList());
			questionsInfo.put("optionList", children);
			// 移除不必要的key
			if (!StrUtils.isNull(questionsInfo.get("r"))) {
				questionsInfo.remove("r");
			}
		});
	}
	
	/**
	 * 处理复合题数据
	 * @param compoundQuestions
	 * @param object
	 */
	private void handleCompoundQuestionList(List<Map<String, Object>> compoundQuestions, String questionsType) {
		if (compoundQuestions == null || compoundQuestions.size() == 0) {
			return;
		}
		compoundQuestions.stream().forEach(compound -> {
			// 删除不必要的key
			if (!StrUtils.isNull(compound.get("r"))) {
				compound.remove("r");
			}
		});
		
		// 首先拿到父题目id
		List<Object> parentIds = compoundQuestions.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
		Map<String, Object> params = new HashMap<>();
		params.put("parentIds", parentIds);
		// 然后通过复合题id查找子题目信息
		List<Map<String, Object>> questionsList = tevglQuestionsInfoMapper.selectSimpleListMap(params);
		List<Object> questionIds = questionsList.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
		params.clear();
		params.put("questionsIds", questionIds);
		List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
		if (questionsList != null && questionsList.size() > 0) {
			compoundQuestions.stream().forEach(compound -> {
				List<Map<String, Object>> childrenQuestionList = questionsList.stream().filter(questionsInfo -> !StrUtils.isNull(questionsInfo.get("parentId")) && questionsInfo.get("parentId").equals(compound.get("questionsId"))).collect(Collectors.toList());
				// 转换子题目的题目类型和题目难易程度
				converShowName(childrenQuestionList);
				childrenQuestionList.stream().forEach(questionInfo -> {
					List<Map<String, Object>> chilren = optionList.stream().filter(a -> a.get("questionsId").equals(questionInfo.get("questionsId"))).collect(Collectors.toList());
					questionInfo.put("optionList", chilren);
					// 删除不必要的key
					if (!StrUtils.isNull(questionInfo.get("r"))) {
						questionInfo.remove("r");
					}
				});
				compound.put("children", childrenQuestionList);
				
			});
		}
	}

	/**
	 * 先随机章节的题目之后，再随机课程的，避免出现重复题目
	 * @param subjectNode 课程节点
	 * @param params
	 * @param choiceQuestions 选择题题目集
	 * @param judgeQuestions 判断题题目集
	 * @param shortAnswerQuestions 简答题题目集
	 * @param completionQuestions 填空题题目集
	 * @param compoundQuestions 复合题题目集
	 */
	private int randomSubjectQuestionList(List<Map<String, Object>> subjectNode, Map<String, Object> params,
			List<Map<String, Object>> choiceQuestions, List<Map<String, Object>> judgeQuestions,
			List<Map<String, Object>> shortAnswerQuestions, List<Map<String, Object>> completionQuestions,
			List<Map<String, Object>> compoundQuestions) {
		int subjectNum = 0;
		if (subjectNode != null && subjectNode.size() > 0) {
			for (int i = 0; i < subjectNode.size(); i++) {
				Object subjectId = subjectNode.get(i).get("subjectId");
				// 单选题题目数
				Object singleChoiceNum = subjectNode.get(i).get("singleChoiceNum");
				// 多选题题目数
				Object multipleChoiceNum = subjectNode.get(i).get("multipleChoiceNum");
				// 判断题题目数
				Object judgeChoiceNum = subjectNode.get(i).get("judgeChoiceNum");
				// 简答题题目数
				Object shortAnswerNum = subjectNode.get(i).get("shortAnswerNum");
				// 填空题题目数
				Object completionNum = subjectNode.get(i).get("completionNum");
				// 复合题题目数
				Object compoundNum = subjectNode.get(i).get("compoundNum");
				params.put("subjectId", subjectId);
				// 记录课程数
				subjectNum++;
				// 随机课程节点的单选题
				if (!StrUtils.isNull(singleChoiceNum) && !"0".equals(singleChoiceNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "1");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", singleChoiceNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> singleChoices = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					choiceQuestions.addAll(singleChoices);
				}
				
				// 随机课程节点的多选题
				if (!StrUtils.isNull(multipleChoiceNum) && !"0".equals(multipleChoiceNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "2");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", multipleChoiceNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> multipleChoices = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					choiceQuestions.addAll(multipleChoices);
				}
				// 随机课程节点的判断题
				if (!StrUtils.isNull(judgeChoiceNum) && !"0".equals(judgeChoiceNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "3");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", judgeChoiceNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> judgeChoices = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					judgeQuestions.addAll(judgeChoices);
				}
				// 随机课程节点的简答题
				if (!StrUtils.isNull(shortAnswerNum) && !"0".equals(shortAnswerNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "4");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", shortAnswerNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> shortAnswers = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					shortAnswerQuestions.addAll(shortAnswers);
				}
				// 随机课程节点的填空题
				if (!StrUtils.isNull(completionNum) && !"0".equals(completionNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "5");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", completionNum); // 随机多少道题目
					params.put("noChildrenQuestion", "Y");  // 不随机复合题的子题目
					List<Map<String, Object>> completions = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					completionQuestions.addAll(completions);
				}
				// 随机课程节点的复合题
				if (!StrUtils.isNull(compoundNum) && !"0".equals(compoundNum)) {
					params.put("questionsState", "Y"); // 题目状态
					params.put("questionsType", "6");  // 1单选2多选3判断4简答5填空6复合
					params.put("limit", compoundNum); // 随机多少道题目
					List<Map<String, Object>> compounds = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
					compoundQuestions.addAll(compounds);
				}
			}
			subjectNode.stream().forEach(choseSubject -> {
				
			});
		}
		return subjectNum;
		
	}
	
	/**
	 * 手动转换题目类型和题目难易程度
	 * @param questionsList
	 */
	private void converShowName(List<Map<String, Object>> questionsList) {
		if (questionsList == null || questionsList.size() == 0) {
			return ;
		}
		questionsList.stream().forEach(questions -> {
			Object questionsType = questions.get("questionsType"); // 题目类型
			Object questionsComplexity = questions.get("questionsComplexity"); // 题目难易程度
			switch (questionsType.toString()) {
				case "1":
					questions.put("questionsTypeName", "单选题");
					break;
				case "2":
					questions.put("questionsTypeName", "多选题");
					break;
				case "3":
					questions.put("questionsTypeName", "判断题");
					break;
				case "4":
					questions.put("questionsTypeName", "简答题");
					break;
				case "5":
					questions.put("questionsTypeName", "填空题");
					break;
				case "6":
					questions.put("questionsTypeName", "复合题");
					break;
				default:
					break;
			}
			switch (questionsComplexity.toString()) {
				case "1":
					questions.put("questionsComplexityName", "简单");
					break;
				case "2":
					questions.put("questionsComplexityName", "普通");
					break;
				case "3":
					questions.put("questionsComplexityName", "困难");
					break;
				default:
					break;
			}
		});
	}
	
	/**
	 * 重选题目
	 * @author zhouyl加
	 * @date 2021年1月21日
	 * @param oldQuestionsId 所有题目id
	 * @param reSelectionQuestionsId 被重选的新题目id
	 * @param subjectNum 课程数
	 * @param chapterNum 章节数
	 * @return
	 */
	@Override
	public R randomQuestions(String oldQuestionsId, String reSelectionQuestionsId, String subjectNum, String chapterNum) {
		if (StrUtils.isEmpty(oldQuestionsId) || StrUtils.isEmpty(reSelectionQuestionsId)) {
			return R.error("必传参数为空");
		}
		// 获取重选了的题目信息
		TevglQuestionsInfo newQuestionsInfo = tevglQuestionsInfoMapper.selectObjectById(reSelectionQuestionsId);
		if (newQuestionsInfo == null) {
			return R.error("重选题目失败");
		}
		
		Map<String, Object> params = new HashMap<>();
		List<String> questionsIds = Arrays.asList(oldQuestionsId.split(","));
		// 获取被重选的题目信息
		params.put("questionsId", questionsIds);
		List<TevglQuestionsInfo> oldQuestionsInfos = tevglQuestionsInfoMapper.selectListByMap(params);
		// 如果是多课程组卷或者多章节组卷
		subjectNum = StrUtils.isEmpty(subjectNum)? null : subjectNum;
		chapterNum = StrUtils.isEmpty(chapterNum)? null : chapterNum;
		if ("1".equals(subjectNum) || "1".equals(chapterNum)) {
			// 只能重选同一课程下的题目信息
			for (TevglQuestionsInfo tevglQuestionsInfo : oldQuestionsInfos) {
				if (!newQuestionsInfo.getSubjectId().equals(tevglQuestionsInfo.getSubjectId())) {
					return R.error("只能重选同一课程下的题目信息");
				}
			}
		}
		
		int size = questionsIds.size();
		while (size > 0) {
			// 根据条件筛选题目
			params.clear();
			params.put("subjectId", newQuestionsInfo.getSubjectId());
			params.put("chaptersId", newQuestionsInfo.getChaptersId());
			params.put("limit", "1");
			params.put("questionsType", newQuestionsInfo.getQuestionsType());
			params.put("noChildrenQuestion", "Y");
			params.put("existedQuestionsIds", questionsIds);
			List<Map<String, Object>> randomQuestionsList = tevglQuestionsInfoMapper.selectRandomQuestionSimpleListByMap(params);
			if (randomQuestionsList == null || randomQuestionsList.size() == 0) {
				return R.error("暂无其它题目可选");
			}
			if (randomQuestionsList.get(0) != null) {
				Map<String, Object> questionsInfo = randomQuestionsList.get(0);
				// 如果新的题目不在试卷里面
				if (!questionsIds.contains(questionsInfo.get("questionsId"))) {
					// 删除不必要的key
					if (!StrUtils.isNull(questionsInfo.get("r"))) {
						questionsInfo.remove("r");
					}
					// 转换题目类型和题目难易程度
					convertUtil.convertDict(questionsInfo, "questionsTypeName", "questions_type");
					convertUtil.convertDict(questionsInfo, "questionsComplexityName", "questions_complexity");
					// 如果是复合题的情况
					if ("6".equals(questionsInfo.get("questionsType"))) {
						params.clear();
						params.put("parentId", questionsInfo.get("questionsId"));
						List<Map<String, Object>> childQuestionsList = tevglQuestionsInfoMapper.selectSimpleListMap(params);
						if (childQuestionsList != null && childQuestionsList.size() > 0) {
							// 转换题目类型和题目难易程度
							convertUtil.convertDict(childQuestionsList, "questionsTypeName", "questions_type");
							convertUtil.convertDict(childQuestionsList, "questionsComplexityName", "questions_complexity");
							List<Object> questionsId = childQuestionsList.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
							params.clear();
							params.put("questionsIds", questionsId);
							List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
							childQuestionsList.stream().forEach(questions -> {
								// 删除不必要的key
								if (!StrUtils.isNull(questions.get("r"))) {
									questions.remove("r");
								}
								questions.put("optionList", optionList.stream().filter(option -> option.get("questionsId").equals(questions.get("questionsId"))).collect(Collectors.toList()));
							});
							questionsInfo.put("children", childQuestionsList);
						}
						return R.ok().put(Constant.R_DATA, questionsInfo);
					}
					params.clear();
					// 非复合题的情况
					params.put("questionsId", questionsInfo.get("questionsId"));
					List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
					questionsInfo.put("optionList", optionList);
					return R.ok().put(Constant.R_DATA, questionsInfo);
				}
			}
			size--;
		}
		return R.error("该章节下没有其它题目可供重选");
	}
	
	/**
	 * @author zhouyl加
	 * @date 2021年1月21日
	 * 点击【保存试卷】按钮生成试卷信息
	 * @param jsonObject
	 * @param loginUserId 当前登录用户id
	 * @param traineeType 学员类型
	 * @return
	 */
	@Override
	@Transactional
	public R generateTestPaper(JSONObject jsonObject, String loginUserId, String traineeType) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		JSONArray questionsList = jsonObject.getJSONArray("questionsList");
		if (questionsList == null || questionsList.size() == 0) {
			return R.error("当前试卷没有题目，请添加题目");
		}
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
		
		// 填充信息
		TepExaminePaperInfo examinePaperInfo = new TepExaminePaperInfo();
		String activityId = Identities.uuid();
		examinePaperInfo.setPaperId(activityId);
		examinePaperInfo.setPaperName(paperName);
		// 试卷类型
		if (traineeType.equals("4") || traineeType.equals("1")) {
			examinePaperInfo.setPaperType("1");
		} else {
			examinePaperInfo.setPaperType("2");
		}
		examinePaperInfo.setPaperTotalScore(null);
		examinePaperInfo.setPaperPracticeTime(0);  // 练习次数
		examinePaperInfo.setRedoNum(-1); // 重做次数 -1为不做限制
		examinePaperInfo.setPaperState("Y");
		examinePaperInfo.setPaperIsRandom("N");
		examinePaperInfo.setViewResultTime(null);
		examinePaperInfo.setChapterId(null);
		examinePaperInfo.setResgroupId(null);
		examinePaperInfo.setEmpiricalValue(0);
		examinePaperInfo.setCreateUserId(loginUserId);
		examinePaperInfo.setCreateTime(DateUtils.getNowTimeStamp());
		examinePaperInfo.setQuestionNumber(questionsList == null ? 0 : questionsList.size());
		examinePaperInfo.setPaperState("Y");
		examinePaperInfo.setGapFillingScoreStandard(gapFillingScoreStandard);
		examinePaperInfo.setPaperConfinementTime(answerTime);
		examinePaperInfo.setSingleChoiceScore(singleChoiceScore);
		examinePaperInfo.setMultipleChoiceScore(multipleChoiceScore);
		examinePaperInfo.setJudgeScore(judgeScore);
		examinePaperInfo.setShortAnswerScore(shortAnswerScore);
		examinePaperInfo.setGapFilling(gapFillingScore);
		examinePaperInfo.setCompositeScore(compositeScore);
		// 注意点，来源类型，值为空或空字符，标识是测试活动那边组的卷，值为1表示是评测中心这边组的卷
		examinePaperInfo.setFromType("1");
		// TODO 处理职业路径和课程体系
		if (questionsList != null && questionsList.size() > 0) {
			List<String> subjectIdList = new ArrayList<String>();
			for (int i = 0; i < questionsList.size(); i++) {
				JSONObject object = questionsList.getJSONObject(i);
				if (!StrUtils.isNull(object.get("subjectId")) && !subjectIdList.contains(object.get("subjectId"))) {
					subjectIdList.add(object.get("subjectId").toString());
				}
			}
			String subjectIdStr = subjectIdList.stream().distinct().collect(Collectors.joining(","));
			examinePaperInfo.setSubjectId(subjectIdStr);
			Map<String, Object> map = new HashMap<>();
			map.put("subjectIds", subjectIdList);
			List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
			if (list != null && list.size() > 0) {
				String majorIdStr = list.stream().map(a -> a.getMajorId()).distinct().collect(Collectors.joining(","));
				examinePaperInfo.setMajorId(majorIdStr);
			}
		}
		log.debug("类型：" + examinePaperInfo.getFromType());
		log.debug("职业路径：" + examinePaperInfo.getMajorId());
		log.debug("课程体系：" + examinePaperInfo.getSubjectId());
		// 保存试卷基本数据
		tepExaminePaperInfoMapper.insert(examinePaperInfo);
		// 保存题目数据
		doSaveQuestionList(questionsList, activityId, singleChoiceScore, multipleChoiceScore, judgeScore, shortAnswerScore, gapFillingScore, compositeScore, loginUserId);
		return R.ok("成功组卷");
	}

	/**
	 * 保存试卷题目信息
	 * @param questionsList 待保存的题目数据
	 * @param activityId 活动（试卷）id
	 * @param singleChoiceScore 单选题分值
	 * @param multipleChoiceScore 多选题分值
	 * @param judgeScore 判断题分值
	 * @param shortAnswerScore 简答题分值
	 * @param gapFillingScore 填空题每空分值
	 * @param compositeScore 复合题每小题得分
	 * @param loginUserId
	 */
	private void doSaveQuestionList(JSONArray questionsList, String activityId, String singleChoiceScore,
			String multipleChoiceScore, String judgeScore, String shortAnswerScore, String gapFillingScore,
			String compositeScore, String loginUserId) {
		if (questionsList == null) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		// 记录各种题型的个数以及总分
		int singleChoiceNum = 0;
		int multipleChoiceNum = 0;
		int judgeNum = 0;
		int shortAnswerNum = 0;
		// 填空题的总个数
		int totalGapFillingSpaceNum = 0;
		BigDecimal value5 = new BigDecimal("0");
		// 所有复合题的总分
		BigDecimal value6 = new BigDecimal("0");
		
		for (int i = 0; i < questionsList.size(); i++) {
			JSONObject questionsInfo = questionsList.getJSONObject(i);
			String questionsId = questionsInfo.getString("questionsId");
			String questionsType = questionsInfo.getString("questionsType");
			JSONArray optionList = questionsInfo.getJSONArray("optionList");
			if ("5".equals(questionsType) && optionList != null && optionList.size() > 0) {
				totalGapFillingSpaceNum = optionList.size();
			}
			// 子题目集合
			List<String> questionsIdList = new ArrayList<>();
			// 如果当前题目的题型是复合题
			if (StrUtils.isNotEmpty(questionsId) && "6".equals(questionsType)) {
				params.put("parentId", questionsId);
				List<TevglQuestionsInfo> list = tevglQuestionsInfoMapper.selectListByMap(params);
				if (list != null && list.size() > 0) {
					questionsIdList = list.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList());
				}
			}
			if (StrUtils.isNotEmpty(questionsId)) {
				// 试卷与题目的关系
				TepExaminePaperQuestionsDetail questionsDetail = new TepExaminePaperQuestionsDetail();
				questionsDetail.setDetailId(Identities.uuid());
				questionsDetail.setPaperId(activityId);
				questionsDetail.setQuestionsId(questionsId);
				questionsDetail.setQuestionsNumber(i + 1);
				TepExaminePaperInfo paperInfo = new TepExaminePaperInfo();
				// 单选题分值
				if ("1".equals(questionsType)) {
					questionsDetail.setQuestionsScore(singleChoiceScore);
					singleChoiceNum++;
					paperInfo.setPaperId(activityId);
					paperInfo.setSingleChoiceNum(1);
				}
				// 多选题分值
				if ("2".equals(questionsType)) {
					questionsDetail.setQuestionsScore(multipleChoiceScore);
					multipleChoiceNum++;
					paperInfo.setPaperId(activityId);
					paperInfo.setMultipleChoiceNum(1);
				}
				// 判断题分值
				if ("3".equals(questionsType)) {
					questionsDetail.setQuestionsScore(judgeScore);
					judgeNum++;
					paperInfo.setPaperId(activityId);
					paperInfo.setJudgeNum(1);
				}
				// 简答题分值
				if ("4".equals(questionsType)) {
					questionsDetail.setQuestionsScore(shortAnswerScore);
					shortAnswerNum++;
					paperInfo.setPaperId(activityId);
					paperInfo.setShortAnswerNum(1);
				}
				// 填空题分值
				if ("5".equals(questionsType)) {
					questionsDetail.setQuestionsScore(gapFillingScore);
					// 计算填空题总分
					value5 = value5.add(new BigDecimal(totalGapFillingSpaceNum).multiply(new BigDecimal(gapFillingScore)));
					paperInfo.setPaperId(activityId);
					paperInfo.setGapFillingNum(1);
				}
				// 复合题分值
				if ("6".equals(questionsType)) {
					JSONArray children = questionsInfo.getJSONArray("children");
					String compositeTotalScore = children == null ? "0" : new BigDecimal(children.size()).multiply(new BigDecimal(compositeScore)).toString();
					questionsDetail.setQuestionsScore(compositeTotalScore);
					paperInfo.setPaperId(activityId);
					paperInfo.setCompositeNum(1);
					
					// 设置子题目分值
					for (int j = 0; j < questionsIdList.size(); j++) {
						// 试卷与题目的关系
						TepExaminePaperQuestionsDetail detail = new TepExaminePaperQuestionsDetail();
						detail.setDetailId(Identities.uuid());
						detail.setPaperId(activityId);
						detail.setQuestionsId(questionsIdList.get(j));  // 子题目id
						detail.setQuestionsNumber(j + 1);
						detail.setQuestionsScore(compositeScore);
						detail.setParentId(questionsId);
						tepExaminePaperQuestionsDetailMapper.insert(detail);
					}
					value6 = value6.add(new BigDecimal(compositeTotalScore));
				}
				// 入库
				tepExaminePaperQuestionsDetailMapper.insert(questionsDetail);
				// 更新题目个数
				tepExaminePaperInfoMapper.plusNum(paperInfo);
				// 题目组卷数
				TevglQuestionsInfo tevglQuestionsInfo = new TevglQuestionsInfo();
				tevglQuestionsInfo.setQuestionsId(questionsId);
				tevglQuestionsInfo.setQuestionsConstructingNum(1);
				tevglQuestionsInfoMapper.plusNum(tevglQuestionsInfo);
			}
		}
		// 计算各个题型得分以及总分
		BigDecimal value1 = new BigDecimal(singleChoiceNum).multiply(new BigDecimal(singleChoiceScore));
		BigDecimal value2 = new BigDecimal(multipleChoiceNum).multiply(new BigDecimal(multipleChoiceScore));
		BigDecimal value3 = new BigDecimal(judgeNum).multiply(new BigDecimal(judgeScore));
		BigDecimal value4 = new BigDecimal(shortAnswerNum).multiply(new BigDecimal(shortAnswerScore));
		BigDecimal totalScore = value1.add(value2).add(value3).add(value4).add(value5).add(value6);
		// 重置填空题的填空个数
		totalGapFillingSpaceNum = 0;
		// 修改试卷总分
		TepExaminePaperInfo paperInfo = new TepExaminePaperInfo();
		paperInfo.setPaperId(activityId);
		paperInfo.setPaperTotalScore(totalScore.toString());
		tepExaminePaperInfoMapper.update(paperInfo);
	}
	
	/**
	 * 加载试卷时间
	 * @author zhouyl加
	 * @date 2021年1月27日
	 * @param dyId 试卷id
	 * @param loginUserId
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public R loadPaperTime( String dyId, String loginUserId) throws ParseException {
		TevglExamineDynamicPaper dynamicPaper = tevglExamineDynamicPaperMapper.selectObjectById(dyId);
		if (dynamicPaper == null) {
			return R.error("无法查看他们的作答时间");
		}
		// 如果试卷已完成提示
		if ("Y".equals(dynamicPaper.getPaperIsFinished())) {
			return R.error("试卷已完成");
		}
		TepExaminePaperInfo paperInfo = tepExaminePaperInfoMapper.selectObjectById(dynamicPaper.getPaperId());
		Map<String, Object> params = new HashMap<>();
		params.put("traineeId", loginUserId);
		params.put("dyId", dyId);
		// 根据学员id和动态试卷id查询历史试卷信息
		List<TevglExamineHistoryPaper> historyPapers = tevglExamineHistoryPaperMapper.selectListByMap(params);
		TevglExamineHistoryPaper historyPaper = null;
		if (historyPapers != null && historyPapers.size() > 0) {
			historyPaper = historyPapers.get(0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 将试卷的开始试卷转换成date类型
		Date date = sdf.parse(historyPaper.getPaperBeginTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 将试卷的约束时间转成分钟
		calendar.add(Calendar.MINUTE, Integer.valueOf(paperInfo.getPaperConfinementTime()));
		// 获取当前试卷结束的时间(试卷的开始时间 - 当前的系统时间)
		long currentTime = calendar.getTimeInMillis() - System.currentTimeMillis();
		return R.ok().put("paperEndTime", currentTime > 0 ? false : true).put("paperEndTime", currentTime > 0 ? currentTime : 0);
	}
}
