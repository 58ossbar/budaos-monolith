package com.budaos.modules.evgl.activity.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 测试活动
 * @author huj
 *
 */
public interface TevglActivityTestPaperService {

	/**
	 * 保存测试活动（其实就是保存一张问卷）
	 * @param request
	 * @param jsonObject
	 * @param loginUserId
	 * @param traineeType 当前登录用户对应的学员类型
	 * @return
	 */
	R saveTestPaperInfo(HttpServletRequest request, JSONObject jsonObject, String loginUserId, String traineeType);
	
	/**
	 * 修改测试活动
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R updateTestPaperInfo(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查看测试活动基本信息与题目（教师所用）（题目返回正确答案）
	 * @param activityId 活动主键（试卷主键）
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R viewTestPaperInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 学生做试卷的查询接口（题目不返回正确答案）
	 * @param activityId
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R viewTestPaperInfoTrainee(String activityId, String pkgId, String loginUserId);

	/**
	 * 删除测试活动
	 * @param activityId 活动主键（试卷主键）
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R deleteTestPaperInfo(String activityId, String pkgId, String loginUserId);
	
	/**
	 * 开始测试活动
	 * @param ctId 当前上课课堂主键id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户id
	 * @return
	 */
	R startTestPaperInfo(String ctId, String activityId, String loginUserId, String activityEndTime);
	
	/**
	 * 结束测试活动
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	R endTestPaperInfo(String ctId, String activityId, String loginUserId);
	
	/**
	 * 复制（引用）一个新的活动
	 * @param targetActivityId 目标活动
	 * @param newPkgId 新教学包id
	 * @param loginUserId 当前登录用户
	 * @param chapterId 所属章节
	 * @param sortNum 排序号
	 * @return
	 */
	R copy(String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum);
	
	/**
	 * 点击左侧章节获取章节详情和题目类型的个数
	 * @param subjectId
	 * @param chapterId
	 * @param type
	 * @param loginUserId
	 * @return
	 */
	R loadChapters(String subjectId, String chapterId, String type, String loginUserId);
	
	/**
	 * <p>抽取公共的随机题目方法 用来给教师组卷和学员组卷提供题目的随机</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param choiceChapters 用户前台界面选择的章节
	 * @return map 返回一个Map对象的选择题和判断题 页面可以通过k,v形式拿
	 * @apiNote choiceChapters:对象格式为 {chapterId: "", choseNum: "0", judgeNum: "0", type: "02"} chapterId:章节ID, choseNum:选择题的数量, judgeNum:判断题的数量, type:
	 */
	Map<String, Object> combinationPaperQuestionsRandom(List<Map<String, Object>> choiceChapters);
	
	/**
	 * <p>重选该题目，根据旧题目的id能够重新随机该题目 可以根据题目章节的id重新随机题目</p>  
	 * @author huj
	 * @data 2019年12月26日	
	 * @param oldQuestionsId 原有的题目 避免随机的题目重复，该试卷已经存在的所有题目ID,多个以逗号隔开
	 * @param questionsId 题目id， 被重选的这个题目ID
	 * @return
	 */
	R randomQuestions(String oldQuestionsId, String questionsId);
	
	/**
	 * 删除试卷编辑页中的题目，用户点击删除按钮来删除题目时的请求 匹配session的数据并过滤掉需要删除的那个元素
	 * @author huj
	 * @data 2019年12月26日	
	 * @param request 从session中拿数据 List<Map<String, Object>> maps = (List<Map<String, Object>>) request.getSession().getAttribute("temporarySaveSession");
	 * @param questionsId 被删除的题目ID
	 * @param questionsType 题目类型1选择2判断
	 * @return
	 */
	R deleteSessionQuestions(HttpServletRequest request, String questionsId, String questionsType);
	
	/**
	 * 新增测试活动时，拍照录入题目
	 * @param request
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R takeAPhoto(HttpServletRequest request, String pkgId, String loginUserId);
	
	/**
	 * 试卷重做
	 * @param activityId 活动id
	 * @param ctId 课堂id
	 * @param loginUserId 登录用户id
	 * @param traineeId 学员id
	 * @return
	 */
	//R resetTestPaper(String ctId, String activityId, String loginUserId, String traineeId);
	R resetTestPaper(JSONObject jsonObject, String loginUserId);
	
	/**
	 * ① 查询当前登录人所学课堂中，所属的职业路径下的试卷库
	 * ② 试卷库中的试卷
	 * @author zhouyl加
	 * @date 2021年3月25日
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R queryPaperListForWx(Map<String, Object> params, String loginUserId);
	
}
