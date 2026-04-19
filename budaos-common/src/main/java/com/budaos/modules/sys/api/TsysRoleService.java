package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysRole;
import com.budaos.modules.sys.domain.TsysUserinfo;

import java.util.List;
import java.util.Map;

/**
 * <p>系统角色api</p>
 * <p>Title: TsysRoleServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @param <R>
 * @date 2019年5月5日
 */
public interface TsysRoleService {

	/**
	 * <p>【角色列表】根据条件查询记录(不带分页效果)</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param map
	 * @return
	 */
	List<TsysRole> queryAll(Map<String, Object> map);
	
	/**
	 * <p>【角色列表】根据条件查询记录(带分页效果)</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param map
	 * @return
	 */
	R query(Map<String, Object> map);
	
    /**
     * 查看明细
     * @param roleId
     * @return
     */
    R view(String roleId);
	
	/**
	 * <p>注:原ModelAndView的进入修改角色页面</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param request
	 * @param response
	 * @return
	 */
	R add();
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月24日
	 * @param role 
	 * @return
	 */
	R save(TsysRole role);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月24日
	 * @param role
	 * @return
	 */
	R update(TsysRole role);
	
	/**
	 * <p>注:原ModelAndView的进入修改角色页面</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	R edit(String roleId);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param id
	 * @return
	 */
	R delete(String id);
	
	/**
	 * <p>批量删除角色信息</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param roleIds
	 * @return
	 */
	R deleteBatch(String[] roleIds);
	
	/**
	 * <p>分配角色</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param roleId
	 * @return
	 */
	R setUser(String[] roleIds);
	
	/**
	 * <p>角色列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	R select(TsysUserinfo userInfo);
	
	/**
	 * <p>保存</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param role
	 * @return
	 */
	R saveOrUpdate(TsysRole role);
	
	/**
	 * <p>保存角色</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param role
	 * @return
	 */
	R saveRoleUser(TsysRole role);
	
	List<String> selectRoleListByCreatorUserId(String createUserId);
	
	TsysRole selectObjectByRoleId(String roleId);
	
	/**
	 * <p>更新角色状态</p>
	 * @author huj
	 * @data 2019年6月12日
	 * @param role
	 * @return
	 */
	R updateStatus(TsysRole role);
}
