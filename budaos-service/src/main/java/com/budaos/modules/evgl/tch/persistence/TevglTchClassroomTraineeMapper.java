package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.vo.TevglTchClassroomTraineeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 课堂成员</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglTchClassroomTraineeMapper extends BaseSqlMapper<TevglTchClassroomTrainee> {
	
	/**
	 * 根据条件查询记录，返回List<Map>, 且对象为驼峰命名
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询创建课堂的时间
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectJoinClassroomYear(Map<String, Object> map);
	
	/**
	 * 根据条件查询课程成员
	 * @param map
	 * @return
	 * @apiNote 查询返回了如下字段：id课程成员表主键，traineeName学员名称（昵称），ctId课堂主键，traineeId学员主键
	 * classId班级主键，joinDate加入日期，createTime创建时间，state状态，traineePic学员头像，mobile手机号码
	 */
	List<Map<String, Object>> selectListMapForWeb(Map<String, Object> map);
	List<Map<String, Object>> selectListMapForWebExclude(Map<String, Object> map);
	
	
	/**
	 * 统计这个人开设课堂所加入的学员人数
	 * @param traineeId
	 */
	Integer countStudentNumByTraineeId(String traineeId);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglTchClassroomTrainee> list);
	
	/**
	 * 统计课堂总人数
	 * @param ctId
	 * @return
	 */
	Integer countTotalTraineeNumberByCtId(Object ctId);

	/**
	 * 根据条件查询课程成员的抢答情况
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAnswerResults(Map<String, Object> params);
	
	/**
	 * 查看展示的课堂信息
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> showClassroomList(Map<String, Object> params);
	
	/**
	 * 查找某人所在的课堂
	 * @param traineeId
	 * @return
	 */
	List<String> findCtIdByTraineeId(@Param("traineeId") String traineeId);
	
	/**
	 * 查找课堂里的学员
	 * @param ctId
	 * @param state
	 * @return
	 */
	List<String> findTraineeIdByCtIdAndState(@Param("ctId") String ctId, @Param("state") String state);
	
	/**
	 * 查找课堂里的学员（包含待审核的）
	 * @param ctId
	 * @return
	 */
	List<String> findTraineeIdByCtId(@Param("ctId") String ctId);
	
	/**
	 * 查询课堂成员id
	 * @param ctId
	 * @return
	 */
	List<String> findIdByCtId(@Param("ctId") String ctId);
	
	/**
	 * 批量更新为有效
	 * @param list
	 * @return
	 */
	int updateStateYBatch(List<String> list);
	
	/**
	 * 根据条件查询课堂成员
	 * @param map
	 * @return
	 */
	List<TevglTchClassroomTraineeVo> findClassroomTraineeList(Map<String, Object> map);
	
	/**
	 * 批量更新状态 [是否允许再进入课堂]
	 * @param accessState
	 * @param ids
	 * @return
	 */
	int batchUpdateAccessState(@Param("accessState") String accessState, @Param("ids") List<String> ids);
	
	List<TevglTchClassroomTraineeVo> findClassroomTraineeBy(@Param("traineeId") String traineeId, @Param("ctIdList") List<String> ctIdList);
}