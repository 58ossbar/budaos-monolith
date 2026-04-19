package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Title: 角色管理 Description: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TsysRoleMapper extends BaseSqlMapper<TsysRole> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<String> selectRoleListByCreatorUserId(String createUserId);
	
	/**
	 * 删除角色数据
	 */
	int deleteBatchRole(Object[] id);
	
	/**
	 * 删除用户角色数据
	 */
	int deleteBatchUserRole(Object[] id);
	
	/**
	 * 删除角色对应的资源数据
	 */
	int deleteBatchRolePrivilege(Object[] id);
	
	/**
	 * 删除角色对应的数据权限
	 */
	int deleteBatchRoleDataPrivilege(Object[] id);
	
	/**
	 * <p>更新角色状态</p>
	 * @author huj
	 * @data 2019年6月12日
	 * @param role
	 */
	int updateStatus(TsysRole role);
	
	/**
	 *
	 * @param roleName
	 * @return
	 */
	TsysRole checkRoleNameUnique(String roleName);
	
}
