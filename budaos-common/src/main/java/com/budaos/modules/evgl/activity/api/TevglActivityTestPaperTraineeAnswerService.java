package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.domain.TepExaminePaperScore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 测试活动 学员作答
 * @author huj
 *
 */
public interface TevglActivityTestPaperTraineeAnswerService {

	/**
	 * 进入试卷考试考核界面触发
	 * @param isDynamic
	 * @param paperId
	 * @param paperType
	 * @param loginUserId
	 * @return
	 */
	R list(String isDynamic, String paperId, String paperType, String loginUserId, String pkgId, String traineeType, String ctId);
	
	/**
	 * 每隔30秒提交一次题目答案 需要传递题目号
	 * @param request
	 * @param list [{"questionsId": "", "questionsNum": "", "replyId": "", "questionsScore": ""}]
	 * @param dyId
	 * @return
	 * @apiNote questionsId题目、questionsNum题目序号、replyId这道题选择的选项主键id,多个英文逗号分隔，questionsScore本题答对可得得分
	 */
	R examinePreCommit(HttpServletRequest request, List<TepExaminePaperScore> list, String dyId, String loginUserId, String traineeType);
	
	/**
	 * 保存学员提交的试卷题目作答信息（学员手动交卷）
	 * @param ctId
	 * @param dyId
	 * @param list
	 * @param loginUserId
	 * @param traineeType
	 * @return
	 */
	R saveTraineeCommitAnswerContent(String ctId, String dyId, List<TepExaminePaperScore> list, String loginUserId, String traineeType);
	
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
	R queryTraineeAnswerInfo(String ctId, String pkgId, String activityId, String loginUserId, String traineeName, String mobile, String jobNumber);
	
	/**
	 * 查看某一次试卷考核结果（试卷得分、题目选择等）
	 * @param ctId 当前课堂id
	 * @param dyId 
	 * @param loginUserId
	 * @param traineeId 被查看的人
	 * @return
	 */
	R viewExamineResult(String ctId, String dyId, String loginUserId, String traineeId);
	
	/**
	 * 单独给简答题评分
	 * @param ctId
	 * @param pkgId
	 * @param activityId
	 * @param scoreId
	 * @param questionsScore
	 * @param loginUserId
	 * @return
	 */
	R giveScore(String ctId, String pkgId, String activityId, String scoreId, String questionsScore, String loginUserId);
}
