package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskTraineeAnswerFile;

import java.util.List;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityTaskTraineeAnswerFileService extends IBaseService<TevglActivityTaskTraineeAnswerFile>{
	
    /**
     * 批量新增
     * @param list
     */
    void insertBatch(List<TevglActivityTaskTraineeAnswerFile> list);
}