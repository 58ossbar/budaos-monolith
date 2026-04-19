package com.budaos.modules.evgl.question.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicQuestionsOptions;

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

public interface TepExamineDynamicQuestionsOptionsService extends IBaseService<TepExamineDynamicQuestionsOptions> {

	List<Map<String, Object>> selectListMapWithQuestionInfoByDyId(String dyId);
	
}
