package com.budaos.modules.evgl.empirical.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalLogChapter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
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

@Mapper
public interface TevglEmpiricalLogChapterMapper extends BaseSqlMapper<TevglEmpiricalLogChapter> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 统计某学员，在某课堂中，某教材，查阅了多少章节
	 * @param map {"traineeId": "", "subjectId": "", "ctId": ""}
	 * @return
	 */
	Integer countRoomSubjectChapterNum(Map<String, Object> map);
}