package com.budaos.modules.evglsp.rest.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.examine.api.TestPaperLibraryService;
import com.budaos.modules.evgl.examine.domain.TevglExaminePaperScore;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/exam-api")
public class ExamController {

	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TmeduApiTokenService tmeduApiTokenService;
	@Autowired
	private TestPaperLibraryService testPaperLibraryService;
	
	/**
	 * 截取试卷的创建时间并去重
	 * @param params
	 * @return
	 */
	@GetMapping("queryTime")
	public R queryTime(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(params.get("token").toString());
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
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
	public R startTheExam(HttpServletRequest request, String isDynamic, String paperId, String paperType, String token) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		// 查询非动态且试卷类型为基本试卷的试卷
		isDynamic = "N";
		paperType = "1";
		return testPaperLibraryService.startTheExam(isDynamic, paperId, paperType, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看该章节组卷的题目信息
	 * @author zhouyl加
	 * @date 2021年4月2日
	 * @param request
	 * @param choseChapters 选择的章节
	 * @param token
	 * @return
	 */
	@PostMapping("getQuestions")
	public R generateTestPaperQuestionsRandom(@RequestBody List<Map<String, Object>> choseChapters, String token) {
		TmeduApiToken tmeduApiToken = tmeduApiTokenService.selectTokenByToken(token);
		if(tmeduApiToken == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(tmeduApiToken.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return R.ok().put(Constant.R_DATA, testPaperLibraryService.generateTestPaperQuestionsRandom(choseChapters));
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
	public R loadPaperTime(HttpServletRequest request, @PathVariable("dyId") String dyId, String token) throws ParseException {
		TmeduApiToken tmeduApiToken = tmeduApiTokenService.selectTokenByToken(token);
		if(tmeduApiToken == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(tmeduApiToken.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return testPaperLibraryService.loadPaperTime(dyId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 每隔30秒提交一次题目答案 需要传递题目号
	 * @author zhouyl加
	 * @date 2021年4月2日
	 * @param dyId
	 * @param list 作答的记录
	 * @param traineeType
	 * @param token
	 * @return
	 */
	@PostMapping("paperCommit")
	public R paperCommit(String dyId, @RequestBody List<TevglExaminePaperScore> list, String traineeType, String token) {
		TmeduApiToken tmeduApiToken = tmeduApiTokenService.selectTokenByToken(token);
		if(tmeduApiToken == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(tmeduApiToken.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return testPaperLibraryService.paperCommit(dyId, list, traineeInfo.getTraineeId(), traineeInfo.getTraineeType());
	}
	
	/**
	 * 保存用户提交的作答信息
	 * @author zhouyl加
	 * @date 2021年4月2日
	 * @param request
	 * @param dyId
	 * @param list 提交的数据 
	 * @param traineeType
	 * @return
	 */
	@PostMapping("saveReplyInformation/{dyId}")
	public R saveReplyInformation(@PathVariable String dyId, @RequestBody List<TevglExaminePaperScore> list, String traineeType, String token) {
		TmeduApiToken tmeduApiToken = tmeduApiTokenService.selectTokenByToken(token);
		if(tmeduApiToken == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo traineeInfo = tevglTraineeInfoService.selectObjectById(tmeduApiToken.getUserId());
		if (traineeInfo == null) {
			return R.error("无效的用户信息");
		}
		return testPaperLibraryService.saveReplyInformation(dyId, list, traineeInfo.getTraineeId(), traineeInfo.getTraineeType());
	}
}
