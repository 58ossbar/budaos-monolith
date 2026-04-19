package com.budaos.modules.evgl.question.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicPaper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 动态试卷表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

@Mapper
public interface TepExamineDynamicPaperMapper extends BaseSqlMapper<TepExamineDynamicPaper> {
	
	//根据条件查询记录，返回List<Map>
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询试卷卷作答人id
	 * @param map
	 * @return
	 */
	List<String> selectTraineeIdList(Map<String, Object> map);
	
	/**
	 * 根据条件查询试卷卷作答人信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectTraineeInfoList(Map<String, Object> map);
	
	/**
	 * 根据条件查询动态试卷以及题目/题目选项乱序信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectDynamicAndRandom(Map<String, Object> map);
}