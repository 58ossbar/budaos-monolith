package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchRoomPereTraineeAnswer;
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
public interface TevglTchRoomPereTraineeAnswerMapper extends BaseSqlMapper<TevglTchRoomPereTraineeAnswer> {
	
	/**
	 * @author zhouyl加
	 * @date 2020年12月22日
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * @author zhouyl加
	 * @date 2020年12月23日
	 * 手动选人时批量添加学员入库
	 * @param list
	 * @return 
	 * @return
	 */
	void batchInsert(Map<String, Object> params);
	
	/**
	 * 根据课堂id和活动id查询活动的抢答人数
	 * @param params
	 * @return
	 */
	List<TevglTchRoomPereTraineeAnswer> selectAnswerNum(Map<String, Object> params);

	/**
	 * 根据条件查询学员id
	 * @param params
	 * @return
	 */
	List<String> answerSummaryResults(Map<String, Object> params);
	
	/**
	 * 根据课堂id和活动id查询活动的抢答人数
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAnswerNum(Map<String, Object> params);
	

	/**
	 * 查询该课堂中的参与该课堂表现的学生
	 * @param ctId
	 * @param activityId
	 * @return
	 */
	List<String> selectTraineeIdList(@Param("ctId") String ctId, @Param("activityId") String activityId);

	/**
	 * 统计该课堂该活动已经成功抢答的人数
	 * @param ctId
	 * @param activityId
	 * @return
	 */
	Integer countSuccessPersonNum(@Param("ctId") String ctId, @Param("activityId") String activityId);

	/**
	 * 计算学生某课堂某课堂表现是否抢答成功，返回数值，大于0视为成功
	 * @param ctId
	 * @param activityId
	 * @param traineeId
	 * @return
	 */
	Integer countTraineeAnswer(@Param("ctId") String ctId, @Param("activityId") String activityId, @Param("traineeId") String traineeId);
	
	/**
	 * 批量更新指定数据
	 * @param list
	 */
	void updateBatchByCaseWhen(@Param("list") List<TevglTchRoomPereTraineeAnswer> list);
}