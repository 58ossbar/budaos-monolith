package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTraineeCheck;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 课堂成员审核表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglTchClassroomTraineeCheckMapper extends BaseSqlMapper<TevglTchClassroomTraineeCheck> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 * @apiNote 查询返回了如下字段：主键tcId，ctId课堂主键，traineeId学员主键，isPass是否通过，createTime创建时间
	 * traineeName学员名称（昵称），traineePic学员证件照（头像），traineeSex学员性别
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 批量更新为不通过
	 * @param list
	 */
	int updateNotPassBatch(List<String> list);
}