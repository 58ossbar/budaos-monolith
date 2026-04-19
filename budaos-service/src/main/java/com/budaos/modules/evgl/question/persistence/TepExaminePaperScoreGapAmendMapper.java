package com.budaos.modules.evgl.question.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.question.domain.TepExaminePaperInfo;
import com.budaos.modules.evgl.question.domain.TepExaminePaperScoreGapAmend;
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
public interface TepExaminePaperScoreGapAmendMapper extends BaseSqlMapper<TepExaminePaperScoreGapAmend> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	String findHistoryIdByScoreId(String scoreId);

	String findLatestScoreByScoreId(String scoreId);

	List<TepExaminePaperScoreGapAmend> findLogListByScoreId(String scoreId);
	
	TepExaminePaperInfo findPaperByScoreId(String scoreId);
}