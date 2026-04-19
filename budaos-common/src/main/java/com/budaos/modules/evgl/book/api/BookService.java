package com.budaos.modules.evgl.book.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;

import java.util.List;
import java.util.Map;

public interface BookService {

	/**
	 * 重名命
	 * @param chapterId
	 * @param name
	 * @return
	 */
	R rename(String chapterId, String name);
	
	/**
	 * 删除
	 * @param chapterId
	 * @return
	 */
	R remove(String chapterId);
	
	/**
	 * 保存
	 * @param tevglBookChapter
	 * @return
	 */
	R saveChapterInfo(TevglBookChapter tevglBookChapter);
	
	/**
	 * 仅更新章节内容
	 * @param tevglBookChapter
	 * @return
	 */
	R saveChapterContent(TevglBookChapter tevglBookChapter);
	
	/**
	 * 查看章节
	 * @param chapterId
	 * @return
	 */
	R viewChapter(String chapterId);
	
	/**
	 * 课堂页面，刷新ztree树专用方法
	 * @param id 主键id，可能是课程id、可能是章节id
	 * @param pkgId
	 * @param subjectId
	 * @return
	 */
	List<Map<String, Object>> listChaptersForRoomPage(String loginUserId, String id, String pkgId, String subjectId);
	
	/**
	 * 教学包页面，刷新ztree树专用方法
	 * @param loginUserId
	 * @param id 主键id，可能是课程id、可能是章节id
	 * @param serial 序号，前端传递过来的值，形如1.1.1
	 * @param type 值可能为subject可能为chapter
	 * @param pkgId
	 * @param subjectId
	 * @return
	 */
	List<Map<String, Object>> listChaptersForPkgPage(String loginUserId, String id, String serial, String type, String pkgId, String subjectId);
	
	/**
	 * 删除缓存
	 * @param ctId
	 * @apiNote
	 * room_book::4654d7d821f14c778d5a842e686b4f37_teacher
	 * room_book::c1e0397b5737483c8915b958f81b46c9_trainee
	 */
	void deleteRoomBookCacheable(String ctId);
	
}
