package com.budaos.modules.evgl.web.controller.activity;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityTestPaperService;
import com.budaos.modules.evgl.book.api.TevglBookChapterService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.question.api.*;
import com.budaos.modules.evgl.question.domain.TepExaminePaperInfo;
import com.budaos.modules.evgl.question.domain.TepExaminePaperQuestionsDetail;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;
import com.budaos.modules.evgl.question.dto.SaveScoreGapAmendDTO;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 4测试活动
 * @author huj
 *
 */
@RestController
@RequestMapping("/activity/testPaper")
public class ActivityTestPaperController {
	
	@Autowired
	private TevglActivityTestPaperService tevglActivityTestPaperService;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private TevglBookChapterService tevglBookChapterService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TevglQuestionsInfoService tevglQuestionsInfoService;
	@Autowired
	private TevglQuestionChoseService tevglQuestionChoseService;
	@Autowired
	private TepExaminePaperInfoService tepExaminePaperInfoService;
	@Autowired
	private TepExaminePaperQuestionsDetailService tepExaminePaperQuestionsDetailService;
	@Autowired
	private TepExaminePaperScoreGapAmendService tepExaminePaperScoreGapAmendService;
	
	
	/**
	 * 查询当前课堂的使用的教材的源课程章节
	 * @param request
	 * @param ctId
	 * @return
	 */
	@PostMapping("/queryTopChapter")
	@CheckSession
	public R queryTopChapterList(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.queryTopChapterList(pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 点击左侧章节获取章节详情和题目类型的个数
	 * @param subjectId
	 * @param chapterId
	 * @param type
	 * @param request
	 * @return
	 */
	@GetMapping("/loadChapters")
	@CheckSession
	public R loadChapters(String subjectId, String chapterId, String type, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息");
		}
		return tevglActivityTestPaperService.loadChapters(subjectId, chapterId, type, traineeInfo.getTraineeId());
	}
	
	/**
	 * 定时发送请求，临时将选中的章节保存进session中。
	 * @author huj
	 * @data 2019年12月25日	
	 * @param choiceChapters
	 * @param session
	 * @return
	 * @apiNote choiceChapters:对象格式为 {chapterId: "", choseNum: "0", judgeNum: "0", type: "02"} chapterId:章节ID, choseNum:选择题的数量, judgeNum:判断题的数量, type:
	 */
	/*@PostMapping("temporarySaveSession")
	@CheckSession
	public R temporarySaveSession(HttpSession session, HttpServletRequest request, @RequestBody List<Map<String, Object>> choiceChapters) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		session.setAttribute("temporarySaveSession", choiceChapters);
		return R.ok("数据存入成功");
	}*/
	
	/**
	 * 进入试卷编辑页面后，发送请求调该接口，获取到试卷的所有题目
	 * @param request
	 * @return
	 */
	@PostMapping("/loadCombinationPaper")	
	@CheckSession
	public R loadCombinationPaper(HttpServletRequest request, @RequestBody List<Map<String, Object>> choiceChapters) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		//@SuppressWarnings("unchecked")
		//List<Map<String, Object>> choiceChapters = (List<Map<String, Object>>) request.getSession().getAttribute("temporarySaveSession");
		if (choiceChapters == null || choiceChapters.isEmpty()) {
			return R.error("无法获取到您选择的章节，请重新选择");
		}
		request.getSession().setAttribute("temporarySaveSession", choiceChapters);
		return R.ok().put("questionsMap", tevglActivityTestPaperService.combinationPaperQuestionsRandom(choiceChapters));
	}
	
	/**
	 * <p>重选该题目，根据旧题目的id能够重新随机该题目 可以根据题目章节的id重新随机题目</p>  
	 * @author huj
	 * @data 2019年12月26日	
	 * @param oldQuestionsId 原有的题目 避免随机的题目重复，该试卷已经存在的所有题目ID,多个以逗号隔开
	 * @param questionsId 题目id， 被重选的这个题目ID
	 * @return
	 */
	@PostMapping("/randomQuestions")
	@CheckSession
	public R randomQuestions(HttpServletRequest request, String oldQuestionsId, String questionsId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTestPaperService.randomQuestions(oldQuestionsId, questionsId);
	}
	
	/**
	 * 删除试卷编辑页中的题目，用户点击删除按钮来删除题目时的请求 匹配session的数据并过滤掉需要删除的那个元素
	 * @param request
	 * @param questionsId
	 * @param questionsType
	 * @return
	 */
	@PostMapping("/deleteQuestions")
	@CheckSession
	public R deleteSessionQuestions(HttpServletRequest request, String questionsId, String questionsType) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTestPaperService.deleteSessionQuestions(request, questionsId, questionsType);
	}
	
	/**
	 * 试卷编辑页面数据填写完毕之后，保存试卷
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("saveTestPaperInfo")
	@CheckSession
	public R saveTestPaperInfo(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTestPaperService.saveTestPaperInfo(request, jsonObject, traineeInfo.getTraineeId(), null);
	}
	
	/**
	 * 查看试卷与试卷题目信息
	 * @param request
	 * @param activityId
	 * @param pkgId
	 * @return
	 */
	@PostMapping("/viewTestPaperInfo")
	@CheckSession
	public R viewTestPaperInfo(HttpServletRequest request, String activityId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTestPaperService.viewTestPaperInfoTrainee(activityId, pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 修改测试活动
	 * @author zyl
	 * @data 2020年11月23日
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/updateTestPaperInfo")
	@CheckSession
	public R updateTestPaperInfo(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTestPaperService.updateTestPaperInfo(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 点击测试活动的信息进行查看
	 * @author zyl加
	 * @data 2020年11月21日
	 * @param request
	 * @param activityId
	 * @param pkgId
	 * @return
	 */
	@PostMapping("/queryTestPaper")
	@CheckSession
	public R queryTestPaper(HttpServletRequest request, String activityId, String pkgId) {
		TevglTraineeInfo tevglTraineeInfo = LoginUtils.getLoginUser(request);
		if (tevglTraineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTestPaperService.viewTestPaperInfo(activityId, pkgId, tevglTraineeInfo.getTraineeId());
	}
	
	/**
	 * 试卷重做
	 * @param request
	 * @param ctId 课堂id
	 * @param activityId 活动id(试卷id)
	 * @param traineeId 学员id
	 * @return
	 */
	@PostMapping("resetTestPaper")
	@CheckSession
	public R resetTestPaper(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo trainee = LoginUtils.getLoginUser(request);
		if (trainee == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTestPaperService.resetTestPaper(jsonObject, trainee.getTraineeId());
	}
	
	/**
	 * <p>学员自测卷</p>  
	 * @author huj
	 * @data 2020年1月3日	
	 * @param choiceChapters:对象格式为 {chapterId: "", choseNum: "0", judgeNum: "0", type: "02"} chapterId:章节ID, choseNum:选择题的数量, judgeNum:判断题的数量, type:
	 * @param request
	 * @return
	 */
	@PostMapping("traineeCreatePaper")
	@CheckSession
	public R traineePaper(@RequestBody List<Map<String, Object>> choiceChapters, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		if (choiceChapters == null || choiceChapters.size() < 0) {
			return R.error("无法获取选中的章节信息，请选中章节");
		}
		String questions = simpleTraineeCombinationPaperQuestionsRandom(choiceChapters);
		// 保存试卷
		TepExaminePaperInfo tepExaminePaperInfo = new TepExaminePaperInfo();
		tepExaminePaperInfo.setPaperName(creatorTraineeCombinationPaperName(traineeInfo.getTraineeName()));
		tepExaminePaperInfo.setPaperPracticeTime(0);
		tepExaminePaperInfo.setPaperIsRandom("N");
		tepExaminePaperInfo.setPaperType("2");
		tepExaminePaperInfo.setPaperState("Y");
		tepExaminePaperInfo.setCreateUserId(traineeInfo.getTraineeId());
		tepExaminePaperInfoService.save(tepExaminePaperInfo);
		// 批量查询
		TepExaminePaperQuestionsDetail questionDetail = null;
		List<TevglQuestionsInfo> batchQuestions = tevglQuestionsInfoService.selectBatchQuestionsByArrays(questions.split(","));
		for (int i = 0; i < batchQuestions.size(); i++) {
			// 保存试卷题目详情
			TevglQuestionsInfo quesitonInfo = batchQuestions.get(i);
			questionDetail = new TepExaminePaperQuestionsDetail();
			questionDetail.setPaperId(tepExaminePaperInfo.getPaperId());
			questionDetail.setQuestionsId(quesitonInfo.getQuestionsId());
			questionDetail.setQuestionsNumber(i + 1);
			tepExaminePaperQuestionsDetailService.save(questionDetail);
			// 组卷数++
			TevglQuestionsInfo questionsInfo = new TevglQuestionsInfo();
			questionsInfo.setQuestionsConstructingNum(1);
			questionsInfo.setQuestionsId(quesitonInfo.getQuestionsId());
			tevglQuestionsInfoService.plusNum(questionsInfo);
		}
		request.getSession().removeAttribute("temporarySaveSession");
		return R.ok().put("paperId", tepExaminePaperInfo.getPaperId());
	}
	
	
	public String simpleTraineeCombinationPaperQuestionsRandom(List<Map<String, Object>> choiceChapters) {
		// 返回选择题数据集
		List<TevglQuestionsInfo> choiceQuestions = new ArrayList<>();
		// 返回判断题数据集
		List<TevglQuestionsInfo> judgeQuestions = new ArrayList<>();
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder stringBuilder = new StringBuilder();
		// 随机题目
		choiceChapters.stream().forEach(choseChapters -> {
			params.put("chaptersId", choseChapters.get("chapterId")); // 章节ID
			params.put("questionsState", "Y"); // 题目状态
			String choseNum = (String) choseChapters.get("choseNum"); // 选择题题目数量
			String judgeNum = (String) choseChapters.get("judgeNum"); // 判断题题目数量
			if (!StrUtils.isEmpty(choseNum) && !"0".equals(choseNum)) {
				params.put("questionsType", "1"); // 1选择2判断3问答
				params.put("limit", (String)choseChapters.get("choseNum")); // 随机多少个题目
				List<TevglQuestionsInfo> choseQuestionsList = tevglQuestionsInfoService.selectRandomQuestionListByMap(params);
				choiceQuestions.addAll(choseQuestionsList);
			}
			if (!StrUtils.isEmpty(judgeNum) && !"0".equals(judgeNum)) {
				params.put("questionsType", "2"); // 1选择2判断3问答
				params.put("limit", (String)choseChapters.get("judgeNum")); // 随机多少个题目
				List<TevglQuestionsInfo> judgeQuestionsList = tevglQuestionsInfoService.selectRandomQuestionListByMap(params);
				judgeQuestions.addAll(judgeQuestionsList);
			}
			params.clear();
		});
		choiceQuestions.stream().forEach(a -> {
			stringBuilder.append(a.getQuestionsId());
			stringBuilder.append(",");
		});
		judgeQuestions.stream().forEach(a -> {
			stringBuilder.append(a.getQuestionsId());
			stringBuilder.append(",");
		});
		String questionsId = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
		return questionsId;
	}
	
	/**
	 * 学员组卷 默认设置名称 默认名称 日期 [学员名称]自测卷
	 * @param traineeName
	 * @return
	 */
	private String creatorTraineeCombinationPaperName(String traineeName) {
		return DateUtils.getNow("yyyy-MM-dd HH:mm:ss") + " [" + traineeName + "]自测卷";
	}

	@RequestMapping("/findHistory")
	@CheckSession
	public R findHistory(HttpServletRequest request, String scoreId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tepExaminePaperScoreGapAmendService.findScoreGapAmend(scoreId, traineeInfo.getTraineeId());
	}
	
	@RequestMapping("/saveScoreGapAmend")
	@CheckSession
	public R saveScoreGapAmend(HttpServletRequest request, @RequestBody SaveScoreGapAmendDTO dto) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tepExaminePaperScoreGapAmendService.saveScoreGapAmend(dto, traineeInfo.getTraineeId());
	}
}
