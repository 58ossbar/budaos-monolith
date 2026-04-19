package com.budaos.modules.evgl.web.controller.book;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.api.TevglBookrackService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/subject-api/book")
public class BookrackController {

	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TevglBookrackService tevglBookrackService;
	
	/**
	 * 书架列表
	 * @author zhouyl加
	 * @date 2021年1月12日
	 * @param request
	 * @param params
	 * @param region Y 查询我的书架 N 查询云教程
	 * @return
	 */
	@GetMapping("/queryBookList")
	@CheckSession
	public R queryBookList(HttpServletRequest request, @RequestParam Map<String, Object> params, String region) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookrackService.queryBookList(params, traineeInfo.getTraineeId(), region);
	}
	
	/**
	 * 活教材列表
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("/queryLiveBookList")
	public R queryLiveBookList(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		return tevglBookrackService.queryLiveBookList(params, traineeInfo == null ? null : traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看课程详情
	 * @author zhouyl加
	 * @date 2021年1月13日
	 * @param request
	 * @param ctId 课堂id
	 * @param subjectId 课程id
	 * @param pkgId 教学包id
	 * @return
	 */
	@GetMapping("/viewSubjectInfo")
	@CheckSession
	public R viewSubjectInfo(HttpServletRequest request, String ctId, String subjectId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookrackService.viewSubjectInfo(ctId, subjectId, pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看章节详情
	 * @author zhouyl加
	 * @date 2021年1月13日
	 * @param request
	 * @param chapterId 章节id
	 * @return
	 */
	@GetMapping("viewChapterInfo")
	@CheckSession
	public R viewChapterInfo(HttpServletRequest request, String chapterId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookrackService.viewChapterInfo(chapterId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 收藏课程
	 * @author zhouyl加
	 * @date 2021年1月29日
	 * @param request
	 * @param subjectId
	 * @param pkgId
	 * @return
	 */
	@GetMapping("collectionThisSubject")
	@CheckSession
	public R collectionThisSubject(HttpServletRequest request, String subjectId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookrackService.collectionThisSubject(subjectId, pkgId, traineeInfo.getTraineeId());
	}
}
