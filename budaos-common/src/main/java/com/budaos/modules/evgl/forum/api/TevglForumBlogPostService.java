package com.budaos.modules.evgl.forum.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;

import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglForumBlogPostService extends IBaseService<TevglForumBlogPost>{
	
	/**
	 * 博客列表
	 * @param params 诸多查询条件
	 * @param loginUserId 当前登录用户
	 * @param filterType 为空查全部，为1查自己的，为2查收藏
	 * @return
	 */
	R queryBlogList(Map<String, Object> params, String loginUserId, String filterType);
	
	/**
	 * 删除博客
	 * @param postId
	 * @param loginUserId
	 * @return
	 */
	R deleteBlog(String postId, String loginUserId);
	
	/**
	 * 收藏博客
	 * @param postId
	 * @param loginUserId
	 * @return
	 */
	R collect(String postId, String loginUserId);
	
	/**
	 * 点赞
	 * @param postId
	 * @param loginUserId
	 * @return
	 */
	R like(String postId, String loginUserId);
	
	/**
	 * 查找推荐标签
	 * @return
	 */
	R recommendedLabel();
	
	/**
	 * 查找热门博主
	 * @return
	 */
	R popularBloggers();
	
	/**
	 * 查找友情社区
	 * @return
	 */
	R friendshipCommunity();
	
	/**
	 * 根据博客id查找博客详情
	 * @param postId 
	 * @param loginUserId
	 * @return
	 */
	R viewBlogDetails(String postId, String loginUserId);
}