package com.budaos.modules.evgl.trainee.persistence;

import com.budaos.modules.evgl.trainee.vo.TevglTraineeScoreVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TevglTraineeScoreMapper {

	/**
	 * 根据条件查询学生成绩
	 * @param map
	 * @return
	 */
	List<TevglTraineeScoreVo> findTraineeScoreList(Map<String, Object> map);
	
	/**
	 * 查询部分学生信息
	 * @param traineeId
	 * @return
	 */
	Map<String, Object> findStudentInfo(String traineeId);
	
}
