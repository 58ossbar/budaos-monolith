package com.budaos.modules.evgl.book.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.vo.BookTreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

@Mapper
public interface TevglBookChapterMapper extends BaseSqlMapper<TevglBookChapter> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>查询某课程下直系章节</p>
	 * @author huj
	 * @data 2019年7月5日
	 * @param map
	 * @return
	 */
	List<TevglBookChapter> selectListByParentId(Map<String, Object> map);
	
	/**
	 * <p>根据条件查询记录（非全部字段都查）</p>
	 * @author huj
	 * @data 2019年7月6日
	 * @param map
	 * @return
	 */
	List<TevglBookChapter> selectListByMapForSimple(Map<String, Object> map);
	
	/**
	 * 根据条件查询记录（非全部字段都查）
	 * @param map
	 * @return 查询返回了如下字段
	 * chapterId，subjectId，chapterNo，parentId，level，
	 * chapterName，chapterIcon，orderNum
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> map);
	
	/**
	 * <p>最大排序号</p>
	 * @author huj
	 * @data 2019年8月8日
	 * @param map
	 * @return
	 */
	int selectMaxSortByMap(Map<String, Object> map);
	
	/**
	 * 统计这个课程的章节数量
	 * @param map
	 * @return
	 */
	Integer countChapterNum(Map<String, Object> map);
	
	/**
	 * 查询章节id
	 * @param subjectId
	 * @return
	 */
	List<String> selectChapterIdList(Object subjectId);

	/**
	 * 查询章节id
	 * @param parentId
	 * @return
	 */
	List<String> findChapterIdListByParentId(@Param("parentId") String parentId);
	
	/**
	 * 根据条件获取最大排序号
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 根据课程ID拿章节ID
	 * @param map
	 * @return
	 */
	List<TevglBookChapter> selectListByMapForChapter(Map<String, Object> map);
	
	/**
	 * 批量插入
	 * @param list
	 */
	void insertBatch(List<TevglBookChapter> list);
	
	/**
	 * 不要关联任何，且只查询部分数据
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListByMapForRelease(Map<String, Object> params);
	

	/**
	 * 根据条件查询简易章节数据
	 * @param subjectId
	 * @return
	 */
	List<BookTreeVo> findAllChapterList(@Param("subjectId") String subjectId);
	
	/**
	 * 利用case when批量更新序号
	 * @param list
	 */
	void updateBatchByCaseWhen(List<TevglBookChapter> list);
}