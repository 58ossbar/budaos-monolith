package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivitySigninTrainee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 学员活动签到信息表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglActivitySigninTraineeMapper extends BaseSqlMapper<TevglActivitySigninTrainee> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据课堂id和活动id等其它条件查询签到人员
	 * @param map
	 * @return
	 * @apiNote 查询返回了如下字段：stId签到表主键，activityId签到活动表主键，traineeId学员主键，signTime签到时间，signType签到类型，
	 * ctId课堂主键，classroomName课堂名称，classroomPic课堂封面，
	 * traineeName学员名称（没有学员名称则查询微信昵称），traineePic证件照（没有证件照则查微信头像），traineeSex学员性别
	 */
	List<Map<String, Object>> selectListMapForCommon(Map<String, Object> map);
	
	/**
	 * 批量设置学员签到状态
	 * @param map 必传key traineeIds，signState
	 */
	void updateBatchSignState(Map<String, Object> map);
	
	/**
	 * 根据签到活动删除签到记录
	 * @param activityId
	 */
	void deleteByActivityId(Object activityId);

	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(@Param("list") List<TevglActivitySigninTrainee> list);
}