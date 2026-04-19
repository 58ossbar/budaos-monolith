package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskGroupMember;
import com.budaos.modules.evgl.activity.query.ActTaskGroupQuery;
import com.budaos.modules.evgl.activity.vo.ActTraineeAnswerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface TevglActivityTaskGroupMemberMapper extends BaseSqlMapper<TevglActivityTaskGroupMember> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(@Param("list") List<TevglActivityTaskGroupMember> list);

	/**
	 * 根据小组id查询该组人员
	 * @param groupId
	 * @return
	 */
	List<String> findMemberIdsByGroupId(@Param("groupId") String groupId);
	
	/**
	 * 根据学员id查询
	 * @param ctId
	 * @param activityId
	 * @param traineeId
	 * @return
	 */
	TevglActivityTaskGroupMember selectObjectBy(@Param("ctId") String ctId, @Param("activityId") String activityId, @Param("traineeId") String traineeId);

	/**
	 * 更新学生回答的内容
	 * @param t
	 * @return
	 */
	int updateContent(TevglActivityTaskGroupMember t);

	/**
	 * 根据条件查询数据
	 * @param query
	 * @return
	 */
	List<ActTraineeAnswerVo> findTraineeAnswerList(ActTaskGroupQuery query);
}