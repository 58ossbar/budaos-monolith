package com.budaos.modules.evgl.question.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.domain.TepExaminePaperScoreGapAmend;
import com.budaos.modules.evgl.question.dto.SaveScoreGapAmendDTO;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TepExaminePaperScoreGapAmendService extends IBaseService<TepExaminePaperScoreGapAmend>{

    /**
     *
     * @param scoreId
     * @param traineeId
     * @return
     */
    R findScoreGapAmend(String scoreId, String traineeId);

    /**
     *
     * @param dto
     * @param traineeId
     * @return
     */
    R saveScoreGapAmend(SaveScoreGapAmendDTO dto, String traineeId);
}