package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysDataprivilege;

import java.util.List;
import java.util.Map;

/**
 * 数据权限api (角色与数据权限关系)
 * <p>Title: TsysDataprivilegeService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖布道师学习通</p> 
 * @author huj
 * @date 2019年5月6日
 */
public interface TsysDataprivilegeService {

	/**
	 * <p>根据条件查询记录(访问列表)</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	R query(Map<String, Object> map);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysDataprivilege
	 * @return
	 */
	R save(TsysDataprivilege tsysDataprivilege);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysDataprivilege
	 * @return
	 */
	R update(TsysDataprivilege tsysDataprivilege);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param id
	 * @return
	 */
	R delete(String id);
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param ids
	 * @return
	 */
	R deleteBatch(String[] ids);
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param id
	 * @return
	 */
	R view(String id);
	
	/**
	 * <p>保存角色与数据权限关系</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param roleId
	 * @param orgIdList
	 * @return
	 */
	R saveOrUpdate(String roleId, List<String> orgIdList);
	
	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	TsysDataprivilege selectObjectById(String id) ;

	/**
	 * 根据条件查询记录
	 * 
	 * @param map
	 * @throws BudaosException
	 */
	List<TsysDataprivilege> selectListByMap(Map<String, Object> map);
	
	/**
	 * 根据角色ID，获取机构ID列表
	 */
	List<String> selectOrgListByRoleId(String roleId);
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<String> selectOrgListByRoleId(String[] roleIds);
	/**
	 * 获取数据过滤的SQL
	 */
	public String getSQLFilter(String userId, String tableAlias, Object point);
}
