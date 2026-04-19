package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;

import java.util.List;

/**
 * <p>用户与角色对应关系</p>
 * <p>Title: TuserRoleServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月6日
 */
public interface TuserRoleService {

	/**
	 * <p>保存用户与角色关系</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @param roleIdList
	 * @return
	 */
	R saveOrUpdate(String userId, List<String> roleIdList);
	
    /**
     * 保存用户与角色关系
     * @author huj
     * @data 2019年5月6日
     * @param roleIdList
     * @param userIdList
     * @return
     */
    R saveOrUpdate(List<String> roleIdList, List<String> userIdList);
	
	/**
	 * <p>保存用户与角色关系</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param roleId
	 * @param userIdList
	 * @return
	 */
	R saveOrUpdateForRole(String roleId, List<String> userIdList);
	
	/**
	 * <p>根据用户ID，获取角色ID列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	List<String> selectRoleListByUserId(String userId);
	
	/**
	 * <p>根据角色ID,查询用户ID列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param roleId
	 * @return
	 */
	R selectUserListByRoleId(String roleId);
	
	/**
	 * <p>根据用户ID,删除记录</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	R delete(String userId);
	
	/**
	 * <p>根据用户ID,批量删除记录</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	R deleteBatch(String[] userId);
}
