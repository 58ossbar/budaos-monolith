package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysResource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单管理
 * 
 * @author huangwb
 * @date 2019-05-05 16:03
 */
public interface TsysResourceService {

	/**
	 * 角色授权菜单
	 * @return
	 */
	List<TsysResource> perms();
	
	/**
	 * 保存或者修改操作
	 * 
	 * @param tsysResource
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R saveOrUpdate(TsysResource tsysResource);

	/**
	 * 删除操作
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R delete(String id);

	/**
	 * 一键生成增删改查按钮菜单
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R init(String menuId);

	/**
	 * 选择菜单(添加、修改菜单)
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R select();

	/**
	 * 角色授权菜单
	 * 
	 * @param userId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	List<TsysResource> perms(String userId);

	/**
	 * 用户菜单列表
	 * 
	 * @param userId
	 * @param systemId 默认业务基础平台的menuid
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R user(String userId, String systemId);

	/**
	 * 获取菜单列表
	 * 
	 * @param map 判断需要查询的是子菜单还是父级菜单
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R query(String menuId);

	/**
	 * 用户菜单列表
	 * 
	 * @param userId
	 * @param systemId 默认业务基础平台的menuid
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R getUserAllPermission(String username, String systemId);

	/**
	 * 查出用户资源权限
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	List<TsysResource> getUserMenuList(String userId, String systemId);

	/**
	 * 查出所有子系统
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R selectSubSysList();

	/**
	 * 获取不包含按钮的菜单列表
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R selectNotButtonList();

	/**
	 * 根据父菜单，查询子菜单
	 * 
	 * @param parentId 父菜单ID
	 * @param userId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R selectListParentId(String parentId);

	/**
	 * 根据资源id查询资源
	 * 
	 * @param menuId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R selectObjectById(String menuId);

	/**
	 * 根据map查询资源菜单
	 * 
	 * @param map
	 * @return
	 */
	List<TsysResource> selectListByMap(Map<String, Object> map);

	/**
	 * 获取用户权限id
	 * 
	 * @param userId
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	Set<String> getUserPermissions(String userId);

	/**
	 * 通过前端map参数获取菜单列表
	 * 
	 * @param map 判断需要查询的是子菜单还是父级菜单
	 * @return R
	 * @author huangwb
	 * @date 2019-05-06 15:18
	 */
	R queryByMap(Map<String, Object> map);

	/**
	 * 移动资源菜单操作
	 * 
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @param moveType 移动的类型
	 * @return
	 * @author huangwb
	 * @date 2019-06-03 17:18
	 */
	R drag(String moveId, String targetId, String moveType);

	/**
	 * 根据前端map参数获取display显示的菜单列表
	 * 
	 * @param parentId 判断需要查询的是子菜单还是父级菜单 type判断是否是展开树还是模糊查询的操作 name模糊查询参数
	 * @return R
	 * @author huangwb
	 * @date 2019-06-17 15:18
	 */
	R queryByMapLinkState(Map<String, Object> map);

	/**
	 * 点击上下按钮移动资源管理节点操作
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @return
	 * @author huangwb
	 * @date 2019-06-17 14:18
	 */
	R clickDrag(String moveId, String targetId);

}
