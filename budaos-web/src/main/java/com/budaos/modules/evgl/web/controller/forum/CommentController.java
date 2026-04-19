package com.budaos.modules.evgl.web.controller.forum;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.forum.api.TevglForumCommentInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论
 * @author huj
 *
 */
@RestController
@RequestMapping("/site/forum/comment")
public class CommentController {

	@Autowired
	private TevglForumCommentInfoService tevglForumCommentInfoService;
	
	/**
	 * 发表评论或回复
	 * @param request
	 * @param postId
	 * @param parentId
	 * @param content
	 * @return
	 */
	@PostMapping("saveCommentInfo")
	@CheckSession
	public R saveCommentInfo(HttpServletRequest request, String postId, String parentId, String content) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumCommentInfoService.saveCommentInfo(postId, parentId, content, traineeInfo.getTraineeId());
	}
	
	/**
	 * 博客评论列表
	 * @param request
	 * @param postId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@PostMapping("queryBlogCommentList")
	public R queryBlogCommentList(HttpServletRequest request, String postId, Integer pageNum, Integer pageSize) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		String traineeId = "";
		if (traineeInfo != null) {
			traineeId = traineeInfo.getTraineeId();
		}
		/*if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}*/
		return tevglForumCommentInfoService.queryBlogCommentList(postId, pageNum, pageSize, traineeId);
	}
	
	/**
	 * 删除回复记录
	 * @param request
	 * @param ciId
	 * @return
	 */
	@PostMapping("deleteReply")
	@CheckSession
	public R deleteReply(HttpServletRequest request, String postId, String ciId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumCommentInfoService.deleteReply(postId, ciId, traineeInfo.getTraineeId());
	}
	
	@PostMapping("clickLike/{ciId}")
	@CheckSession
	public R clickLike(HttpServletRequest request, @PathVariable("ciId") String ciId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumCommentInfoService.clickLike(ciId, traineeInfo.getTraineeId());
	}
}
