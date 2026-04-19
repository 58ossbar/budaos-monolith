package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClasstrainee;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 班级学员</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglTchClasstraineeMapper extends BaseSqlMapper<TevglTchClasstrainee> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>根据条件查询记录</p>  
	 * @author huj
	 * @data 2019年12月16日	
	 * @param map
	 * @return
	 * @apiNote 查询并返回了如下字段：ctId班级学员表主键，classId班级id，className班级名称，
	 * traineeId学员id，traineeName学员名称（如没有值则查的微信昵称），traineeSex学员性别，traineePic学员头像（没有证件照则查微信头像）
	 */
	List<Map<String, Object>> selectListMapForWeb(Map<String, Object> map);
	
	/**
	 * 注意，只会查询未加入课堂的成员
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListMapForWebExclude(Map<String, Object> map); 
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglTchClasstrainee> list);
	
	/**
	 * 查询某人所在的班级
	 * @param traineeId
	 * @return
	 */
	List<String> selectClassIdListByTraineeId(String traineeId);
	
	/**
	 * 查询某班的学员
	 * @param classId
	 * @return
	 */
	List<String> selectTraineeIdListByClassId(String classId);
	
	/**
	 * 查找最新班级
	 * @param traineeId
	 * @return
	 */
	String findTheLatestClassId(String traineeId);
	
	/**
	 * 根据条件查询班级成员
	 * @param map
	 * @return
	 */
	List<TevglTraineeInfoVo> selectClassTraineeListByMap(Map<String, Object> map);
	
}