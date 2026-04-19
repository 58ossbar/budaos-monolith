package com.budaos.modules.evgl.eao.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamroom;
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
public interface TeaoTraineeExamroomMapper extends BaseSqlMapper<TeaoTraineeExamroom> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListByMapForSp(Map<String, Object> map);
	
	/**
	 * 查询对象
	 * @param params
	 * @return
	 */
	TeaoTraineeExamroom selectObjectByIdAndPkgId(Map<String, Object> params);
}