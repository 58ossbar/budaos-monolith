package com.budaos.modules.evgl.book.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.vo.MyBookVo;
import com.budaos.modules.evgl.book.vo.TevglBookSubjectSelectVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 课程教材</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglBookSubjectMapper extends BaseSqlMapper<TevglBookSubject> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>根据条件查询活教材</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param map 参数中若isSubjectRefNull为"Y"则查询的是课程，若isSubjectRefNotNull为"Y"则查询的是活教材
	 * @return
	 */
	List<TevglBookSubject> selectListByMapForWeb(Map<String, Object> map);
	
	/**
	 * 公共方法，注意此方法只会查询subject_ref为空的记录
	 * @param map
	 * @return
	 */
	List<TevglBookSubject> selectListByMapForCommon(Map<String, Object> map); 
	
	/**
	 * <p>更新阅读量、星级、收藏数、点赞数、举报数等</p>
	 * @author huj
	 * @data 2019年7月22日
	 * @param map
	 * @return
	 */
	void updateNum(TevglBookSubject tevglBookSubject);
	
	/**
	 * <p>根据条件查询课程，关联了t_evgl_book_subperiod</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListByMapForMgr(Map<String,Object> map);
	
	List<TevglBookSubject> selectSubjectList(Map<String, Object> map); 
	
	/**
	 * 活教材列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryLiveBookList(Map<String,Object> map);
	
	/**
	 * 我的书架列表
	 * @param map
	 * @return
	 */
	List<MyBookVo> queryMyBookListVo(Map<String, Object> map);
	
	/**
	 * 根据条件查询课程
	 * @param map
	 * @return
	 */
	List<TevglBookSubjectSelectVo> querySubjectList(Map<String,Object> map);
	
	
}