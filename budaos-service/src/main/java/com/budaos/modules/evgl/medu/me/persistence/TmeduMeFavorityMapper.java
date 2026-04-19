package com.budaos.modules.evgl.medu.me.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeFavority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 收藏表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TmeduMeFavorityMapper extends BaseSqlMapper<TmeduMeFavority> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 查询收藏的课堂数据
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectClassroomList(Map<String, Object> map);
	
	/**
	 * 查询收藏的视频
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectVideoList(Map<String, Object> map);
	
	/**
	 * 查询收藏的题目
	 * @param loginUserId
	 * @data 2020年11月20日
	 * @author zyl改
	 * @return
	 */
	List<Map<String, Object>> selectQuestionList(Map<String, Object> map);
}