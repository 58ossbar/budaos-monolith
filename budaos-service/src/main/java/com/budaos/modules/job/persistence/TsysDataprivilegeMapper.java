package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysDataprivilege;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

@Mapper
public interface TsysDataprivilegeMapper extends BaseSqlMapper<TsysDataprivilege> {

	/**
	 * 根据角色ID，获取机构ID列表
	 */
	List<String> selectOrgListByRoleId(String roleId);
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<String> selectOrgListByRoleIds(String[] roleIds);

	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TsysDataprivilege> list);
}