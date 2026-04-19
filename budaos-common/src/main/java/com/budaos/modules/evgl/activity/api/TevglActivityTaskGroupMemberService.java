package com.budaos.modules.evgl.activity.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskGroupMember;
import com.budaos.modules.evgl.activity.query.ActTaskGroupQuery;
import com.budaos.modules.evgl.activity.vo.CommitTaskAnswerVo;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglActivityTaskGroupMemberService extends IBaseService<TevglActivityTaskGroupMember>{

    /**
     * 查询某学员的作答情况
     * @param ctId
     * @param activityId
     * @param traineeId
     * @return
     */
    R viewTraineeAnswerContent(String ctId, String activityId, String traineeId);

    /**
     * 学生提交作业/小组任务
     * @param vo
     * @return
     */
    R commitTast(CommitTaskAnswerVo vo);

    /**
     * 教师查看所有学生的作业任务
     * @param loginUserId
     * @return
     */
    R queryTraineeAnswerList(ActTaskGroupQuery query, String loginUserId);
}