package com.budaos.modules.evgl.web.controller.threefeetplatform.testingcenter;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.book.api.TevglBookChapterService;
import com.budaos.modules.evgl.book.api.TevglBookMajorService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.medu.me.api.TmeduMeFavorityService;
import com.budaos.modules.evgl.question.api.TepExaminePaperQuestionsDetailService;
import com.budaos.modules.evgl.question.api.TevglQuestionChoseService;
import com.budaos.modules.evgl.question.api.TevglQuestionsInfoService;
import com.budaos.modules.evgl.question.api.TevglQuestionsRecoveryErrorService;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;
import com.budaos.modules.evgl.question.domain.TevglQuestionsRecoveryError;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: 题库</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/questions-api")
public class QuestionsInfoController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(QuestionsInfoController.class);
	@Autowired
	private TevglQuestionsInfoService tevglQuestionsInfoService;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private TevglBookChapterService tevglBookChapterService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglQuestionChoseService tevglQuestionChoseService;
	@Autowired
	private TmeduMeFavorityService tmeduMeFavorityService;
	@Autowired
	private TevglBookMajorService tevglBookMajorService;
	@Autowired
	private TevglQuestionsRecoveryErrorService tevglQuestionsRecoveryErrorService;
	@Autowired
	private TepExaminePaperQuestionsDetailService tepExaminePaperQuestionsDetailService;
	/**
	 * <p>题目列表</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param params
	 * @return
	 * @apiNote 
	 * // 注意，这里不要使用日志注解注解，否则会引起Gjson问题，
	 * com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.write(ReflectiveTypeAdapterFactory.java:245)         
	 * at com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper.write(TypeAdapterRuntimeTypeWrapper.java:69)
	 */
	@GetMapping("/queryQuestions")
	//@SysLog("查询列表(返回List<Bean>)") 
	@CheckSession
	public R queryQuestions(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglQuestionsInfoService.queryQuestions(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * <p>获取教材，只展示自己的</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param params
	 * @return
	 */
	@GetMapping("/getSubjects")
	public R getSubjects(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息");
		}
		params.put("createUserId", traineeInfo.getTraineeId());
		List<TevglBookSubject> list = tevglBookSubjectService.getLiveSubjectList(params);
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * <p>获取当前上课的指定教材</p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param subjectId 教材ID
	 * @param request
	 * @return
	 */
	@GetMapping("/getCurrentSubject")
	public R getCurrentSubject(String subjectId, HttpServletRequest request) {
		if (StrUtils.isEmpty(subjectId) || "null".equals(subjectId)) {
			return R.error("参数subjectId为空");
		}
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息");
		}
		TevglBookSubject subject = tevglBookSubjectService.selectObjectById(subjectId);
		if (!subject.getCreateUserId().equals(traineeInfo.getTraineeId())) {
			return R.error("非法操作，无法对他人的教材录入题目");
		}
		return R.ok().put(Constant.R_DATA, subject);
	}
	
	/**
	 * <p>获取章节</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param params 当key为type,值为withQuestions:会查询每个章节下对应的题目数量,值为1时：查询该教材的正常题目总数量
	 * @return
	 */
	@GetMapping("/getChapters")
	@CheckSession
	public R getChapters(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		String subjectId = (String) params.get("subjectId");
		String type = (String) params.get("type");
		if (StrUtils.isEmpty(subjectId) || "null".equals(subjectId)) {
			return R.error("参数subjectId为空");
		}
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		TevglBookSubject subject = tevglBookSubjectService.selectObjectById(subjectId);
		if (subject == null) {
			return R.error("无效的教材，请重试");
		}
		if (!subject.getCreateUserId().equals(traineeInfo.getTraineeId())) {
			return R.error("非法操作，当前不能查看别人的章节数据");
		}
		// 该教材的总题目数量
		int subjectQuestionTotal = 0;
		if ("1".equals(type)) {
			subjectQuestionTotal = tevglQuestionsInfoService.countSubjectQuestionTotal(subjectId);
		}
		// 该教材的
		List<TevglBookChapter> list = tevglBookChapterService.getDirectLineChapters(subjectId);
		if (list != null && list.size() > 0) {
			if ("2".equals(type)) {
				Map<String, Object> map = new HashMap<>();
				list.forEach(chapter -> {
					map.put("chaptersId", chapter.getChapterId());
					map.put("questionsState", "Y");
					List<TevglQuestionsInfo> questionList = tevglQuestionsInfoService.selectListByMap(map);
					chapter.setQuestionList(questionList);
				});
			}
		}
		subject.setChildren(list);
		return R.ok().put(Constant.R_DATA, list)
				.put("subject", subject)
				.put("subjectQuestionTotal", subjectQuestionTotal);
	}
	
	/**
	 * <p>获取题型、题目类型(字典:1选择2判断等)</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @return
	 */
	@GetMapping("/getQuestionsType")
	public R getQuestionsType() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("questions_type"));
	}
	
	/**
	 * <p>获取题目难易程度(1容易、2普通、3困难)</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @return
	 */
	@GetMapping("/getQuestionsComplexity")
	public R getQuestionsComplexity() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("questions_complexity"));
	}
	
	/**
	 * <p>获取排序方式(91最近新增、92组卷率高、93正确率高、94收藏率高)</p>  
	 * @author zyl改
	 * @data 2020年11月16日	
	 * @return
	 */
	@GetMapping("/getQuestionsStyle")
	public R getQuestionsStyle() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("questions_style"));
	}
	
	/**
	 * <p>获取题目种类(1实训、2开源)</p>  
	 * @author zyl改
	 * @data 2020年11月13日	
	 * @return
	 */
	@GetMapping("/getQuestionsTypeSort")
	public R getQuestionsTypeSort() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("question_type_sort"));
	}
	
	/**
	 * <p>点击添加题目按钮，准备进入录题页面</p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param request
	 * @param map {subjectId:'', chapterId:'', questionsComplexity:'', questionsType:''} 条件原样返回，便于录题页面默认选择这些值
	 * @return
	 */
	@PostMapping("toAddQuestions")
	public R toAddQuestions(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		return R.ok().put(Constant.R_DATA, map);
	}
	
	/**
	 * <p>保存题目</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param tevglQuestionsInfo
	 * @param request
	 * @return
	 */
	@PostMapping("/saveQuestion")
	@CheckSession
	public R saveQuestion(@RequestBody(required = false) TevglQuestionsInfo tevglQuestionsInfo, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息");
		}
		if (StrUtils.isEmpty(tevglQuestionsInfo.getQuestionsId())) {
			log.debug("新增题目操作");
			tevglQuestionsInfo.setCreateUserId(traineeInfo.getTraineeId()); // 创建人
			tevglQuestionsInfo.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
			return tevglQuestionsInfoService.saveQuestion(tevglQuestionsInfo, traineeInfo);
		} else {
			log.debug("修改题目操作");
			tevglQuestionsInfo.setUpdateUserId(traineeInfo.getTraineeId());
			tevglQuestionsInfo.setUpdateTime(DateUtils.getNowTimeStamp());
			return tevglQuestionsInfoService.editQuestion(tevglQuestionsInfo, traineeInfo);
		}
	}
	
	/**
	 * <p>题目明细</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param id
	 * @return
	 */
	@GetMapping("/viewQuestion/{id}")
	@CheckSession
	public R viewQuestion(@PathVariable("id")String id, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		TevglQuestionsInfo questionsInfo = tevglQuestionsInfoService.selectObjectById(id);
		Map<String, Object> licence = new HashMap<>();
		licence.put("allowChangeMajor", false);
        licence.put("allowChangeSubject", false);
        licence.put("allowChangeChapter", false);
        // 查询这个题目是否有被使用（参与了组卷），如果没有被使用，允许修改
        if (questionsInfo != null) {
            List<String> paperIdList = tepExaminePaperQuestionsDetailService.findPaperIdListByQuestionId(questionsInfo.getQuestionsId());
            boolean flag1 = paperIdList == null || paperIdList.size() == 0;
            if (flag1) {
                licence.put("allowChangeMajor", true);
                licence.put("allowChangeSubject", true);
                licence.put("allowChangeChapter", true);
            }
        }
        return R.ok().put(Constant.R_DATA, questionsInfo).put("licence", licence);
	}
	
	/**
	 * <p>批量删除题目</p>  
	 * @author huj
	 * @data 2019年12月10日	
	 * @param ids
	 * @param request
	 * @return
	 */
	@GetMapping("/deleteQuestion/{id}")
	@SysLog("批量删除")
	@CheckSession
	public R deleteQuestion(@PathVariable("id")String id, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		return tevglQuestionsInfoService.delete(id);
	}
	
	/**
	 * <p>禁用题目</p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param questionsId
	 * @param request
	 * @return
	 */
	@PostMapping("/forbiddenQuestions")
	@CheckSession
	public R forbiddenQuestions(String questionsId, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglQuestionsInfoService.forbiddenQuestions(questionsId, traineeInfo.getTraineeId());
	}
	
	/**
	 * <p>纠错题目</p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param questionsId
	 * @param request
	 * @return
	 */
	@GetMapping("/errorCorrectionQuestions")
	@CheckSession
	public R errorCorrectionQuestions(String questionsId, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		
		return tevglQuestionsRecoveryErrorService.selectByCollectionMap(questionsId);
	}
	
	/**
	 * 保存纠错信息
	 * @author zyl
	 * @data 2020年11月20日
	 * @param tevglQuestionsRecoveryError
	 * @return
	 */
	@PostMapping("/confirmErrorCorrection")
	@CheckSession
	public R confirmErrorCorrection(String questionsId, String content, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		TevglQuestionsRecoveryError tevglQuestionsRecoveryError = new TevglQuestionsRecoveryError();
		tevglQuestionsRecoveryError.setQuestionId(questionsId);
		tevglQuestionsRecoveryError.setContent(content);
		tevglQuestionsRecoveryError.setCreateUserId(traineeInfo.getTraineeId());  //操作人
		return tevglQuestionsRecoveryErrorService.save(tevglQuestionsRecoveryError);
	}
	
	/**
	 * <p>收藏或取消收藏题目</p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param request
	 * @param questionsId
	 * @param state
	 * @return
	 */
	@GetMapping("/collectQuestions")
	@CheckSession
	public R collectQuestions(HttpServletRequest request, String questionsId, String state) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if (StrUtils.isEmpty(state) || "null".equals(state)) {
			return R.error("参数state为空");
		}
		// 收藏题目
		if ("Y".equals(state)) {
			return tevglQuestionsInfoService.collectQuestions(questionsId, traineeInfo.getTraineeId());
		}
		// 取消收藏
		if ("N".equals(state)) {
			return tevglQuestionsInfoService.cancelCollectQuestions(questionsId, traineeInfo.getTraineeId());
		}
		return R.ok();
	}
	
	/**
	 * 新增或修改复合题
	 * @param jsonObject
	 * @param request
	 * @return
	 */
	@PostMapping("/saveCompositeQuestionInfo")
    @CheckSession
    public R saveCompositeQuestionInfo(@RequestBody(required = true)JSONObject jsonObject, HttpServletRequest request) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error("无法获取登录信息");
        }
        String questionsId = jsonObject.getString("questionsId");
        if (StrUtils.isEmpty(questionsId)) {
        	return tevglQuestionsInfoService.saveCompositeQuestionInfo(jsonObject, traineeInfo.getTraineeId());
        } else {
        	return tevglQuestionsInfoService.updateCompositeQuestionInfo(jsonObject, traineeInfo.getTraineeId());
        }
    }
	
	/**
	 * 删除复合题
	 * @param request
	 * @param questionsId 父题目id
	 * @return
	 */
	@PostMapping("deleteCompositeQuestionInfo")
	@CheckSession
	public R deleteCompositeQuestionInfo(HttpServletRequest request, String questionsId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error("无法获取登录信息");
        }
        return tevglQuestionsInfoService.deleteCompositeQuestionInfo(questionsId, traineeInfo.getTraineeId());
	}
}
