package com.budaos.modules.evgl.question.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.question.domain.TepExamineHistoryPaper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 学员答卷历史表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TepExamineHistoryPaperService extends IBaseService<TepExamineHistoryPaper> {

	List<TepExamineHistoryPaper> selectListByMap(Map<String, Object> map);
	
}
