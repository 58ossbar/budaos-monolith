package com.budaos.modules.evgl.book.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.vo.BookTreeVo;
import com.budaos.modules.evgl.book.vo.SaveChapterVisibleVo;
import com.budaos.modules.evgl.book.vo.SaveChapterVo;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 章节</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglBookChapterService extends IBaseService<TevglBookChapter>{
	
	/**
	 * <p>重命名章节名称</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param pkgId 教学包id
	 * @param id 章节主键
	 * @param name 章节名称
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R rename(String pkgId, String id, String name, String loginUserId);
	
	/**
	 * <p>删除章节</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param pkgId 教学包id
	 * @param id 章节主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Deprecated
	R remove(String pkgId, String id, String loginUserId);
	
	/**
	 * 删除章节（同时删除其节点下所有子节点）
	 * @param pkgId
	 * @param id
	 * @param loginUserId
	 * @return
	 */
	R removeV2(String pkgId, String id, String loginUserId);
	

	/**
	 * 获取指定节点下的所有子孙节点
	 * @param subjectId 章节所在的课程
	 * @param sourceChapterId 必传参数
	 * @return 返回空集合，或者ok的数据
	 */
	List<String> findChapterIdList(String subjectId, String sourceChapterId);

	/**
	 * 获取指定节点下的所有子孙节点
	 * @param sourceChapterId
	 * @param allChapterList
	 * @return
	 */
	List<String> findChapterIdList(String sourceChapterId, List<BookTreeVo> allChapterList);

	
	/**
	 * <p>移动</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param currId 当前ID
	 * @param targetId 目标ID
	 * @return
	 */
	R move(String currId, String targetId, String loginUserId, String pkgId);
	
	/**
	 * 新增章节
	 * @author huj
	 * @data 2019年8月8日
	 * @param tevglBookChapter
	 * @return
	 * @apiNote 新增章节时必传参数: chapterName，subjectId，parentId
	 */
	R saveChapterInfo(TevglBookChapter tevglBookChapter, String loginUserId);
	
	/**
	 * 修改章节信息
	 * @param tevglBookChapter
	 * @param loginUserId
	 * @return
	 */
	R updateChapterInfo(TevglBookChapter tevglBookChapter, String loginUserId);
	
	/**
	 * <p>获取指定教材或课程且状态为Y有效的的直系章节</p>  
	 * @author huj
	 * @data 2019年12月23日	
	 * @param subjectId
	 * @return
	 */
	List<TevglBookChapter> getDirectLineChapters(String subjectId);
	
	/**
	 * <p>明细</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param id
	 * @return
	 */
	TevglBookChapter selectObjectById(String id);
	
	/**
	 * 查看章节
	 * @param chapterId 章节主键
	 * @param loginUserId 登录用户
	 * @param pkgId 教学包主键
	 * @return
	 */
	R viewChapterInfo(String chapterId, String loginUserId, String pkgId, String type);
	
	/**
	 * 批量设置章节对学员是否可见（小程序专用）
	 * @param jsonObject
	 * @return
	 */
	R batchSetChapterIsTraineesVisible(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 设置章节对学员是否可见（web端专用）
	 * @param pkgId 课堂表对应的那个pkgId
	 * @param chapterId
	 * @param isTraineesVisible Y可见N不可见
	 * @param loginUserId
	 * @return
	 */
	R setChapterIsTraineesVisible(String ctId, String pkgId, String chapterId, String isTraineesVisible, String loginUserId);
	
	/**
	 * 设置课程视频标签对学员是否可见
	 * @param ctId 课堂id
	 * @param pkgId 课堂教学包
	 * @param resgroupId
	 * @param isTraineesVisible Y可见N不可见
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R setTraineesVisibleResgroup(String ctId, String pkgId, String resgroupId, String isTraineesVisible, String loginUserId);
	
	/**
	 * 批量设置 课程视频等标签对学员是否可见
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R setTraineesVisibleResgroupBatch(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params);
	
	/**
	 * 左右滑动进入上下章节
	 * @param pkgId
	 * @param chapterId
	 * @return
	 */
	R slide(String pkgId, String chapterId, String loginUserId);
	
	/**
	 * 一键生成（任务描述、任务知识点、任务准备、任务实时）
	 * @param pkgId
	 * @param chapterId
	 * @param loginUserId
	 * @return
	 */
	R create(String pkgId, String chapterId, String loginUserId);
	
	/**
	 * 章节下拉列表
	 * @param params
	 * @return
	 */
	R listSelectChapter(Map<String, Object> params);
	
	/**
	 * 查询当前课堂上课教材的源课程
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R queryTopChapterList(String pkgId, String loginUserId);

	/**
	 * 获取层次机构的树形数据（且章节名称后面拼接此章节授权给了谁）
	 * @param subjectId
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R getChapterTreeWithTeacherName(String subjectId, String pkgId, String loginUserId);
	
	/**
	 * 章节树（可见性）
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R getChapterTreeVisibleForWeb(String pkgId, String loginUserId);
	
	/**
	 * 批量设置 课程视频等标签对学员是否可见
	 * @param vo
	 * @param loginUserId
	 * @return
	 */
	R setTraineesVisibleBatchForWeb(SaveChapterVisibleVo vo, String loginUserId);
	
	/**
	 * 追加同级节点
	 * @param tevglBookChapter
	 * @return
	 */
	R appendPeerNodes(TevglBookChapter tevglBookChapter);
	
	/**
	 * 拖拽节点
	 * @param pkgId
	 * @param subjectId 教学包对应的课程教材id
	 * @param soureNodeId 当前被选中托拽的节点
	 * @param targetId 必传参数，情况：①被拖拽至该节点下，成为其下子节点。②被拖拽至某节点后。详见ztree api <a>http://www.treejs.cn/v3/demo.php#_302</a>
	 * @param moveType 指定移动到目标节点的相对位置 "inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
	 * @param traineeId 当前登录用户
	 * @return
	 */
	R dragNode(String pkgId, String subjectId, String soureNodeId, String targetId, String moveType, String traineeId, String list);
	
	/**
	 * 升级节点（例如升级2.1.1三级，则变成2.1二级）
	 * @param chapterId
	 * @param traineeId 当前登录用户
	 * @return
	 */
	R upgradeNodes(String chapterId, String traineeId);
	
	/**
	 * 降级
	 * @param chapterId
	 * @param traineeId
	 * @return
	 */
	R demotionNodes(String chapterId, String traineeId);
	
	/**
	 * 老师批量新增教材章节目录
	 * @param vo
	 * @param traineeId
	 * @return
	 */
	R batchSaveChapter(SaveChapterVo vo, String traineeId);
	
}