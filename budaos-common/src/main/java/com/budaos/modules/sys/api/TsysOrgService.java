package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysOrg;

import java.util.List;
import java.util.Map;

/**
 * 
 * 机构管理
 * 
 * @author huangwb
 * @date 2019-05-05 15:18
 */
public interface TsysOrgService {
	
    /**
     * 获取机构树形数据
     * @author huj
     * @data 2019年5月16日
     * @return
     */
    R getOrgTree(Map<String, Object> map);
	
	/**
	 * 保存机构
	 * 
	 * @param tsysOrg
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R save(TsysOrg tsysOrg);

	/**
	 * 修改机构
	 * 
	 * @param tsysOrg
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R update(TsysOrg tsysOrg);

	/**
	 * 删除
	 * 
	 * @param id
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R delete(String id);

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R deleteBatch(String[] ids);

	/**
	 * 获取所有机构id
	 * 
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R getAllOrgIds();

	/**
	 * 获取子部门ID，用于数据过滤
	 * 
	 * @param orgId
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R getSubOrgIdList(String orgId);

	/**
	 * <p>
	 * 访问列表
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月16日
	 * @return
	 */
	R query2(Map<String, Object> map);

	/**
	 * 获取机构信息
	 * 
	 * @param orgId
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R selectListById(String orgId);

	/**
	 * 访问列表
	 * 
	 * @param parentId
	 * @param userId
	 * @return R
	 * @author huangwb
	 */
	R query(String parentId, String userId);

	/**
	 * 通过前端map参数访问机构列表
	 * 
	 * @param parentId
	 * @param userId
	 * @return R
	 * @author huangwb
	 */
	R queryByMap(Map<String, Object> map, String userId);
	R queryByMapWithAll(Map<String, Object> map, String userId);
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

	R queryByMap2(Map<String, Object> map, String userId);

	/**
	 * 点击上下按钮移动机构管理节点操作
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @return
	 * @author huangwb
	 * @date 2019-06-17 14:18
	 */
	R clickDrag(String moveId, String targetId);

	/**
	 * 根据orgId递归查询所有父级id
	 * @param orgId
	 * @return
	 */
	List<String> queryOrgsBySubid(String orgId);
}
