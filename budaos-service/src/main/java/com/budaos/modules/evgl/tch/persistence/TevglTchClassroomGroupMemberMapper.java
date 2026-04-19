package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 课堂小组成员</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglTchClassroomGroupMemberMapper extends BaseSqlMapper<TevglTchClassroomGroupMember> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询课堂小组成员
	 * @param map
	 * @return
	 * @apiNote 查询返回了如下字段：gmId课堂小组成员表主键，gpId课堂小组表主键，traineeId学员主键，sortNum排序号，
	 * createTime加入小组时间，traineeName学员名称（昵称），traineePic证件照（头像），traineeSex性别
	 */
	List<Map<String, Object>> selectListMapForCommon(Map<String, Object> map);
	
	/**
	 * 根据条件获取排序号
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglTchClassroomGroupMember> list);
	
	/**
	 * 批量更新身份
	 * @param dictCode
	 * @param list
	 */
	void updateBatchDictCode(@Param("dictCode") String dictCode, @Param("list") List<String> list);
}