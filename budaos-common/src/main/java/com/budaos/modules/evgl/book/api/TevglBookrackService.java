package com.budaos.modules.evgl.book.api;

import com.budaos.core.baseclass.domain.R;

import java.util.Map;

/**
 * 我的书架
 * @author huj
 *
 */
public interface TevglBookrackService {

	/**
	 * 我的书架列表
	 * @author zhouyl加
	 * @date 2021年1月12日
	 * @param params
	 * @param loginUserId
	 * @param region Y 查询我的书架 N 查询云教程
	 * @return
	 */
	R queryBookList(Map<String, Object> params, String loginUserId, String region);
	
	/**
	 * 活教材列表
	 * @param params
	 * @param loginUserId 选传参数，当前登陆用户
	 * @return
	 */
	R queryLiveBookList(Map<String, Object> params, String loginUserId);
	
	/**
	 * 查看课程详情
	 * @author zhouyl加
	 * @date 2021年1月13日
	 * @param ctId 		     课堂id
	 * @param subjectId   课程id
	 * @param pkgId 	     教学包id
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R viewSubjectInfo(String ctId, String subjectId, String pkgId, String loginUserId);
	
	/**
	 * 查看章节详情
	 * @author zhouyl加
	 * @date 2021年1月13日
	 * @param chapterId 章节id
	 * @param loginUserId
	 * @return
	 */
	R viewChapterInfo(String chapterId, String loginUserId);
	
	/**
	 * 收藏课程
	 * @author zhouyl加
	 * @date 2021年1月29日
	 * @param subjectId 课程id
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录人
	 * @return
	 */
	R collectionThisSubject(String subjectId, String pkgId, String loginUserId);
}
