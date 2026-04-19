package com.budaos.modules.evgl.web.controller.testcenter;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.examine.api.TestPaperLibraryService;
import com.budaos.modules.evgl.examine.domain.TevglExaminePaperScore;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 试卷库
 * @author zhouyl
 * @date 2021年1月3日
 */

@RestController
@RequestMapping("/teachingCenter-api/testPaperLibrary")
public class TestPaperLibraryController {

	@Autowired
	private TestPaperLibraryService testPaperLibraryService;
	
	/**
	 * 根据条件查询试卷列表
	 * @author zhouyl加
	 * @date 2021年1月3日
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("queryTestPapers")
	@CheckSession
	public R queryTestPapers(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return testPaperLibraryService.queryTestPapers(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 截取试卷的创建时间并去重
	 * @param params
	 * @return
	 */
	@GetMapping("queryTime")
	@CheckSession
	public R queryTime(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return testPaperLibraryService.queryTime(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 点击开始考试进入考核页面触发此请求
	 * @author zhouyl加
	 * @date 2021年1月4日
	 * @param request
	 * @param isDynamic
	 * @param paperId
	 * @param paperType
	 * @return
	 */
	@PostMapping("startTheExam")
	@CheckSession
	public R startTheExam(HttpServletRequest request, String isDynamic, String paperId, String paperType) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 查询非动态且试卷类型为基本试卷的试卷
		isDynamic = "N";
		paperType = "1";
		return testPaperLibraryService.startTheExam(isDynamic, paperId, paperType, traineeInfo.getTraineeId());
	}
	
	/**
	 * 每隔30秒提交一次题目答案 需要传递题目号
	 * @param request
	 * @param dyId
	 * @param list
	 * @return
	 */
	@PostMapping("paperCommit/{dyId}")
	@CheckSession
	public R paperCommit(HttpServletRequest request, @PathVariable("dyId") String dyId, @RequestBody List<TevglExaminePaperScore> list) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return testPaperLibraryService.paperCommit(dyId, list, traineeInfo.getTraineeId(), traineeInfo.getTraineeType());
	}
	
	/**
	 * 保存用户提交的作答信息
	 * @param request
	 * @param dyId
	 * @param list 提交的数据 
	 * @param traineeType
	 * @return
	 */
	@PostMapping("saveReplyInformation/{dyId}")
	@CheckSession
	public R saveReplyInformation(HttpServletRequest request, @PathVariable String dyId, @RequestBody List<TevglExaminePaperScore> list, String traineeType) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return testPaperLibraryService.saveReplyInformation(dyId, list, traineeInfo.getTraineeId(), traineeInfo.getTraineeType());
	}
	
	/**
	 * @author zhouyl加
	 * @date 2021年1月22日
	 * 【试卷库】教师组卷选题页面
	 *  @param request
	 * @param subjectId 课程id
	 * @param chapterId 章节id
	 * @param type 要组卷的题目来源是课程下的还是章节下的  类型(1 课程 2 章节)
	 * @return
	 */
	@GetMapping("teacherGenerateTestPaper")
	@CheckSession
	public R teacherGenerateTestPaper(HttpServletRequest request, String subjectId, String chapterId, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		type = "1";
		return testPaperLibraryService.teacherGenerateTestPaper(subjectId, chapterId, type, traineeInfo.getTraineeId());
	}
	
	/**
	 * 点击开始答题触发此接口，获取试卷的所有题目信息
	 * @param request
	 * @param choseChapters
	 * @return
	 */
	@PostMapping("generateTestPaperQuestionsRandom")
	@CheckSession
	public R generateTestPaperQuestionsRandom(HttpServletRequest request, @RequestBody List<Map<String, Object>> choseChapters) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if (choseChapters == null || choseChapters.size() == 0) {
			return R.error("无法获取到您选择的章节，请重新选择");
		}
		return R.ok().put("questionsMap", testPaperLibraryService.generateTestPaperQuestionsRandom(choseChapters));
	}
	
	/**
	 * 点击开始答题触发此接口，获取试卷的所有题目信息
	 * @param request
	 * @param questionIdList
	 * @return
	 */
	@PostMapping("generateTestPaperQuestionsManual")
	@CheckSession
	public R generateTestPaperQuestionsManual(HttpServletRequest request, @RequestBody List<String> questionIdList) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return testPaperLibraryService.generateTestPaperQuestionsManual(questionIdList);
	}
	
	/**
	 * 重选题目
	 * @author zhouyl加
	 * @date 2021年1月22日
	 * @param request
	 * @param oldQuestionsId
	 * @param newQuestionsId
	 * @param subjectNum 课程数
	 * @param chapterNum 章节数
	 * @return
	 */
	@GetMapping("randomQuestions")
	@CheckSession
	public R randomQuestions(HttpServletRequest request, String oldQuestionsId, String newQuestionsId, String subjectNum, String chapterNum) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return testPaperLibraryService.randomQuestions(oldQuestionsId, newQuestionsId, subjectNum, chapterNum);
	}
	
	/**
	 * @author zhouyl加
	 * @date 2021年1月23日
	 * 点击【保存试卷】按钮生成试卷信息
	 * @param request
	 * @param jsonObject
	 * @param traineeType
	 * @return
	 */
	@PostMapping("generateTestPaper")
	@CheckSession
	public R generateTestPaper(HttpServletRequest request, @RequestBody(required = true) JSONObject jsonObject, String traineeType) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 当前为教师组卷，默认人员类型为教师（4）
		traineeType = "4";
		return testPaperLibraryService.generateTestPaper(jsonObject, traineeInfo.getTraineeId(), traineeType);
	}
	
	/**
	 * 加载试卷时间
	 * @author zhouyl加
	 * @date 2021年1月27日
	 * @param request
	 * @param dyId
	 * @return
	 * @throws ParseException
	 */
	@GetMapping("loadPaperTime/{dyId}")
	@CheckSession
	public R loadPaperTime(HttpServletRequest request, @PathVariable("dyId") String dyId) throws ParseException {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return testPaperLibraryService.loadPaperTime(dyId, traineeInfo.getTraineeId());
	}
}
