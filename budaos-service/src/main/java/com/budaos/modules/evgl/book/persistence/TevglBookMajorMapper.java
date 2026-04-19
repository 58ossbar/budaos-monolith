package com.budaos.modules.evgl.book.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.book.domain.TevglBookMajor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 职业课程</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglBookMajorMapper extends BaseSqlMapper<TevglBookMajor> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>前端查询该专业下的课程</p>
	 * @author huj
	 * @data 2019年7月11日
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListMapByMapForWeb(Map<String, Object> map);
	
	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param map
	 * @return
	 */
	List<TevglBookMajor> selectListByMap2(Map<String, Object> map);
}