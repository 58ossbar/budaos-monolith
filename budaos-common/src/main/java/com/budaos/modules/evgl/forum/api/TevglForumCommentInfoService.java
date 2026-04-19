package com.budaos.modules.evgl.forum.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.forum.domain.TevglForumCommentInfo;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglForumCommentInfoService extends IBaseService<TevglForumCommentInfo>{
	
	/**
	 * 保存评论信息
	 * @param postId 博客
	 * @param parentId 父ID
	 * @param content 评论内容
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R saveCommentInfo(String postId, String parentId, String content, String loginUserId);
	
	/**
	 * 查询博客评论
	 * @param postId
	 * @param pageNum
	 * @param pageSize
	 * @param loginUserId
	 * @return
	 */
	R queryBlogCommentList(String postId, Integer pageNum, Integer pageSize, String loginUserId);
	
	/**
	 * 点赞
	 * @param ciId
	 * @param loginUserId
	 * @return
	 */
	R clickLike(String ciId, String loginUserId);
	
	/**
	 * 删除回复记录
	 * @param postId 博客id
	 * @param ciId 评论id
	 * @param loginUserId
	 * @return
	 */
	R deleteReply(String postId, String ciId, String loginUserId);
	
	
}