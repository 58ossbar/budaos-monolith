package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysOrg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhujw
 * @version 1.0
 */

@Mapper
public interface TsysOrgMapper extends BaseSqlMapper<TsysOrg> {

	/**
	 * 查询所有机构
	 */
	List<TsysOrg> getAllOrgs();

	// 查询所有机构ID
	List<String> getAllOrgIds();

	String selectMaxCodeByParenId(String parentId);

	/**
	 * 查询子部门ID列表
	 * 
	 * @param parentId 上级部门ID
	 */
	List<String> selectOrgIdList(String parentId);

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
	int selectMaxOrderSnByParentId(String parentId);

	/**
	 * 根据parentId 查询子元素
	 * 
	 * @param parentId
	 * @return
	 */
	List<TsysOrg> selectListByParentId(String parentId);
}