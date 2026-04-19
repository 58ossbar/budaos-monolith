package com.budaos.modules.evgl.question.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.question.domain.TepExaminePaperScore;

import java.util.List;
import java.util.Map;

/**
 * <p> Title:试卷成绩表 </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TepExaminePaperScoreService extends IBaseService<TepExaminePaperScore> {

	List<TepExaminePaperScore> selectListByMap(Map<String, Object> map);
	
	List<Map<String, Object>> selectListMapLinkQuestionByMap(Map<String, Object> map);
	
}
