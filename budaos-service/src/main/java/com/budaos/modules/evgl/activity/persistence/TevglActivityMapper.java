package com.budaos.modules.evgl.activity.persistence;

import org.apache.ibatis.annotations.Mapper;

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
public interface TevglActivityMapper {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 * @apiNote 查询返回了如下字段：activityId活动主键，resgroupId分组主键，
	 * activityTitle标题，purpose用途，activityTypeName活动类型名称，activityType活动类型，
	 * activityPic活动封面，empiricalValue经验值，activityBeginTime活动开始时间，
	 * activityEndTime活动结束时间，activityState活动状态，questionNumber题目数量，answerNumber作答人数，
	 * traineeNum学员人数，signType签到类型
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 查找当前学员参与过的活动
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryJoinActivity(Map<String, Object> map);
}