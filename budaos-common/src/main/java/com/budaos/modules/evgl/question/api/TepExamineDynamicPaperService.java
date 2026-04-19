package com.budaos.modules.evgl.question.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicPaper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 动态试卷表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TepExamineDynamicPaperService extends IBaseService<TepExamineDynamicPaper> {

	TepExamineDynamicPaper selectObjectById(Object id);
	
	List<TepExamineDynamicPaper> selectListByMap(Map<String, Object> map);
	
}
