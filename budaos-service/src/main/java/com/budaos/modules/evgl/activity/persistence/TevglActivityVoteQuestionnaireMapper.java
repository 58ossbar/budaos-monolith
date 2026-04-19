package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaire;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 活动-投票/问卷</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglActivityVoteQuestionnaireMapper extends BaseSqlMapper<TevglActivityVoteQuestionnaire> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件获取排序号（已+1）
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	Map<String, Object> selectObjectMapById(Object id);
	
	/**
	 * 更新数量
	 * @param TevglActivityVoteQuestionnaire
	 */
	void plusNum(TevglActivityVoteQuestionnaire TevglActivityVoteQuestionnaire);
	
	TevglActivityVoteQuestionnaire selectObjectByIdAndPkgId(Map<String, Object> params);
	
	/**
	 * 统计累计有多少人参与了该问卷
	 * @param id
	 * @return
	 */
	Integer countTotalUserNum(Object id);
}