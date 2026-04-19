package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Title: 系统资源配置 Description: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TsysResourceMapper extends BaseSqlMapper<TsysResource> {

	/**
	 * 根据父菜单，查询子菜单
	 * 
	 * @param parentId 父菜单ID
	 */
	List<TsysResource> selectListParentId(String parentId);
	
	/**
	 * <p>没有type <= 2 这个查询条件</p>  
	 * @author huj
	 * @data 2019年8月15日	
	 * @param parentId
	 * @return
	 */
	List<TsysResource> selectListParentId2(String parentId);

	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<TsysResource> selectNotButtonList();

	/**
	 * 查询用户的权限列表
	 */
	List<TsysResource> selectUserList(String userId);

	/**
	 * 删除资源数据
	 */
	int deleteBatchRes(Object[] id);

	/**
	 * 角色对应的资源数据
	 */
	int deleteBatchRolPri(Object[] id);

	/**
	 * 查出所有资源，包括其他系统的个性化资源，统一做授权
	 * 
	 */
	List<TsysResource> selectAllListByMap(Map<String, Object> map);

	/**
	 * 查出所有子系统
	 * 
	 */
	List<TsysResource> selectSubSysList();

	/**
	 * 查出所有显示的子系统
	 * 
	 */
	List<TsysResource> selectSubSysListByDisplay(String display);

	/**
	 * 查询指定节点下的显示的节点总数
	 * 
	 * @param parentId
	 * @return
	 */
	int selectCountParentIdAndDisplay(@Param("parentId") String parentId, @Param("display") String display);

	/**
	 * 查询指定节点下的节点总数
	 * 
	 * @param parentId
	 * @return
	 */
	int selectCountParentId(String parentId);

	/**
	 * 查询指定节点下的最大排序号
	 * 
	 * @param parentId
	 * @return
	 */
	int selectMaxOrderNumByParentId(String parentId);

	/**
	 * 查出出所有显示的所有子系统
	 * 
	 */
	List<TsysResource> selectListParentIdAndDisplay(@Param("parentId") String parentId,
			@Param("display") String display);
}
