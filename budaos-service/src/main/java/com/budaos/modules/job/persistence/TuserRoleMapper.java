package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TuserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Title: 用户与角色对应关系 Description: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TuserRoleMapper extends BaseSqlMapper<TuserRole> {

	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<String> selectRoleListByUserId(String userId);
	/**
	 * 根据角色ID，获取用户ID列表
	 */
	List<String> selectUserListByRoleId(String roleId);
	/**
	 * 根据角色ID删除角色与用户关系数据
	 */
	int deleteByRole(String roleId);
	
	/**
	 * <p>根据用户ID查询角色的交集</p>
	 * @author huj
	 * @data 2019年6月17日
	 * @param userIds
	 * @return
	 */
	List<TuserRole> selectRoleIntersectionByUserId(String[] userIds);
}
