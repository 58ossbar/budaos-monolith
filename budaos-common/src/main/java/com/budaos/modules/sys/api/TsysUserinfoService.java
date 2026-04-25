package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysUserinfo;

import java.util.List;
import java.util.Map;

/**
 * <p>系统用户api</p>
 * <p>Title: TsysUserinfoServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月5日
 */
public interface TsysUserinfoService {

	/**
	 * <p>根据条件查询记录(所有用户列表)</p>
	 * @author huj
	 * @data 2019年5月7日
	 * @param params 查询条件
	 * @return
	 */
	R query(Map<String, Object> params);
	
	/**
	 * <p>根据机构ID,查询用户列表</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param orgIds 机构ID 多个以逗号隔开
	 * @return 
	 */
	List<TsysUserinfo> queryList(String[] orgIds);
	
	
	/**
	 * <p>注:原ModelAndView下的用户信息编辑页</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param map
	 * @return HashMap集合,通过key: data获取数据
	 */
	R add(Map<String, Object> map);
	
	/**
	 * <p>注:原ModelAndView下的用户信息编辑页</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId 被修改的用户ID
	 * @return
	 */
	R edit(String userId);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月7日
	 * @param user
	 * @return
	 */
	R save(TsysUserinfo user, String attachId);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月7日
	 * @return
	 */
	R update(TsysUserinfo user, String attachId);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param id 被删除用户的ID
	 * @param loginUserId 当前登陆用户ID
	 * @return 
	 */
	R delete(String id, String loginUserId);
	
	/**
	 * <p>批量删除用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param ids 被删除用户的ID
	 * @return
	 */
	R deleteBatch(String[] ids);
	
	/**
	 * <p>详细信息</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param id
	 * @return
	 */
	R view(String id);
	
	//R saveOrUpdate(TsysUserinfo user, String orgList, String roleList);
	
	/**
	 * <p>分配角色，当前登录用户不能给自己分配角色</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIds 用户ID 多个以逗号分隔
	 * @param roleIds 角色ID 多个以逗号分隔
	 * @param loginUserId 当前登陆用户ID
	 * @return
	 */
	R grantRole(String[] userIds,String[] roleIds, String loginUserId);
	
	/**
	 * <p>进入分配角色界面</p>
	 * @author huj
	 * @data 2019年6月14日
	 * @param ids
	 * @return
	 */
	R toGrantRole(String[] ids);
	
	/**
	 * <p>修改登录用户密码</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId 当前登陆用户ID
	 * @param password 旧密码
	 * @param newPassword 新密码
	 * @return
	 */
	R updatePassword(String userId, String password, String newPassword);
	
	/**
	 * <p>重置用户密码,自动忽略当前登录用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIds 用户ID 多个以逗号隔开
	 * @param loginUserId 当前登陆用户ID
	 * @return
	 */
	R resetPassword(String[] userIds, String loginUserId);

	/**
	 * 清除用户的所有权限，忽略当前登录用户
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIds
	 * @return
	 */
	R clearPermissions(List<String> userIds);
	
	/**
	 * <p>清除用户的所有权限，忽略当前登录用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIds 用户ID 多个以逗号隔开
	 * @param loginUserId 当前登陆用户ID
	 * @return
	 */
	R clearPermissions(String[] userIds, String loginUserId);
	
	/**
	 * <p>用户分配权限，当前登录用户不能分配权限</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIds 用户ID 多个以逗号隔开
	 * @param menuIds 菜单ID 多个以逗号隔开
	 * @param loginUserId 当前登陆用户ID
	 * @return
	 */
	R grantPerms(String[] userIds,String[] menuIds, String loginUserId);
	
	
	
	/**
	 * <p>查询用户的所有权限</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	List<String> selectAllPerms(String userId);
	
	/**
	 * <p>查询用户的所有菜单ID</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param userId
	 * @return
	 */
	List<String> selectAllMenuId(String userId);
	
	/**
	 * <p>根据用户名，查询系统用户</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param username
	 * @return
	 */
	TsysUserinfo selectObjectByUserName(String username);
	
	/**
	 * <p>根据用户ID,查询信息</p>
	 * @author huj
	 * @data 2019年5月11日
	 * @param userId
	 * @return
	 */
	TsysUserinfo selectObjectByUserId(String userId);
	
	/**
	 * <p>查询所有用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	List<TsysUserinfo> getAllUserinfo();
	
	/**
	 * <p>更改状态</p>
	 * @author huj
	 * @data 2019年6月12日
	 * @param user
	 * @return
	 */
	R updataStatus(TsysUserinfo user);
	
	/**
	 * <p>更新排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @param params
	 * @return
	 */
	R updateSort(Map<String, Object> params);
	
	/**
	 * <p>获取最大排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @return
	 */
	int getMaxSort();
}
