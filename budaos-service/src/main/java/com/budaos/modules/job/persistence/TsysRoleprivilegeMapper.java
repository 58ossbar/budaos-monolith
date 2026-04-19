package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysRoleprivilege;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Title: 角色和菜单对应 Description: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TsysRoleprivilegeMapper extends BaseSqlMapper<TsysRoleprivilege> {
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<String> selectMenuListByRoleId(String roleId);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TsysRoleprivilege> list);
}
