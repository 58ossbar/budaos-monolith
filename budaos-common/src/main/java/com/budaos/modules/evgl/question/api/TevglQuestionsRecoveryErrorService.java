package com.budaos.modules.evgl.question.api;

import com.budaos.common.exception.BudaosException;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.domain.TevglQuestionsRecoveryError;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglQuestionsRecoveryErrorService extends IBaseService<TevglQuestionsRecoveryError>{
	
	/**
	 * 新增
	 */
	public R save(TevglQuestionsRecoveryError tevglQuestionsRecoveryError) throws BudaosException;
	
	/**
	 * 根据题目id查询题目信息
	 * @param questionsId
	 * @return
	 */
	public R selectByCollectionMap(String questionsId);
}