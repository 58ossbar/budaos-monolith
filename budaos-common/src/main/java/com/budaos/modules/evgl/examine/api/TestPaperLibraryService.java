package com.budaos.modules.evgl.examine.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.examine.domain.TevglExaminePaperScore;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface TestPaperLibraryService {

	/**
	 * 根据条件查询试卷列表
	 * @author zhouyl加
	 * @date 2021年1月3日
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R queryTestPapers(Map<String, Object> params, String loginUserId);
	
	/**
	 * 根据时间进行搜索
	 * @param params
	 * @return
	 */
	R queryTime(Map<String, Object> params, String loginUserId);
	
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
	R startTheExam(String isDynamic, String paperId, String paperType, String loginUserId);
	
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
	R saveReplyInformation(String dyId, List<TevglExaminePaperScore> list, String loginUserId, String traineeType);
	
	/**
	 * 每隔30秒提交一次题目答案 需要传递题目号
	 * @param dyId
	 * @param list 提交的数据
	 * @param loginUserId
	 * @param traineeType
	 * @return
	 */
	R paperCommit(String dyId, List<TevglExaminePaperScore> list, String loginUserId, String traineeType);
	
	/**
	 * 【试卷库】教师组卷
	 * @param subjectId 课程id
	 * @param chapterId 章节id
	 * @param type 要组卷的题目来源是课程下的还是章节下的  类型(1 课程 2 章节)
	 * @param loginUserId
	 * @return
	 */
	R teacherGenerateTestPaper(String subjectId, String chapterId, String type, String loginUserId);
	
	/**
	 * 查看该章节组卷的题目信息
	 * @author zhouyl加
	 * @date 2021年1月16日
	 * @param choseChapters
	 * @return
	 */
	Map<String, Object> generateTestPaperQuestionsRandom(List<Map<String, Object>> choseChapters);

	/**
	 * 手动组卷（根据用户勾选的题目，进行组卷）
	 * @param questionIdList
	 * @return
	 */
	R generateTestPaperQuestionsManual(List<String> questionIdList);
	
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
	R randomQuestions(String oldQuestionsId, String reSelectionQuestionsId, String subjectNum, String chapterNum);
	
	/**
	 * @author zhouyl加
	 * @date 2021年1月21日
	 * 点击【保存试卷】按钮生成试卷信息
	 * @param jsonObject
	 * @param loginUserId 当前登录用户id
	 * @param traineeType 学员类型 
	 * @return
	 */
	R generateTestPaper(JSONObject jsonObject, String loginUserId, String traineeType);
	
	/**
	 * 加载试卷时间
	 * @author zhouyl加
	 * @date 2021年1月27日
	 * @param dyId 试卷id
	 * @param loginUserId
	 * @return
	 * @throws ParseException 
	 */
	R loadPaperTime(String dyId, String loginUserId) throws ParseException;
}
