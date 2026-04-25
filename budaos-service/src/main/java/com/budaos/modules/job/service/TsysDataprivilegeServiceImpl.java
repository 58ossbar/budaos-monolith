package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.dataprivilege.annotation.DataFilter;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysDataprivilegeMapper;
import com.budaos.modules.job.persistence.TsysOrgMapper;
import com.budaos.modules.sys.api.*;
import com.budaos.modules.sys.domain.TsysDataprivilege;
import com.budaos.modules.sys.domain.TsysRole;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>角色与数据权限关系 api的实现类</p>
 * <p>Title: TsysDataprivilegeServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月6日
 */
@Service
public class TsysDataprivilegeServiceImpl implements TsysDataprivilegeService {
	
	private static Logger log = LoggerFactory.getLogger(TsysDataprivilegeServiceImpl.class);

	@Autowired
	private TsysOrgService tsysOrgService;
	@Autowired
	private TsysOrgMapper tsysOrgMapper;
	@Autowired
	private TorgUserService torgUserService;
	@Lazy
	@Autowired
	private TsysRoleService tsysRoleService;
	@Autowired
	private TuserRoleService tuserRoleService;
	@Autowired
	private TsysDataprivilegeMapper tsysDataprivilegeMapper;
	
	/**
	 * <p>根据条件查询记录(访问列表)</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	@Override
	public R query(Map<String, Object> map) {
		List<TsysDataprivilege> list = tsysDataprivilegeMapper.selectListByMap(new HashMap<String, Object>());
		return R.ok().put("list", list);
	}

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysDataprivilege
	 * @return
	 */
	@Override
	public R save( TsysDataprivilege tsysDataprivilege) {
		try {
			verifyForm(tsysDataprivilege);	// 数据校验
			tsysDataprivilege.setRoleOrgid(Identities.uuid()); // 设置主键ID
			tsysDataprivilegeMapper.insert(tsysDataprivilege); // 调用方法保存
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok();
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysDataprivilege
	 * @return
	 */
	@Override
	public R update(TsysDataprivilege tsysDataprivilege) {
		try {
			verifyForm(tsysDataprivilege);	// 数据校验
			tsysDataprivilegeMapper.update(tsysDataprivilege);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok();
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	@Override
	@SysLog("删除")
	public R delete(String id) {
		tsysDataprivilegeMapper.delete(id);
		return R.ok();
	}

	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param ids
	 * @return
	 */
	@Override
	@SysLog("批量删除")
	public R deleteBatch( String[] ids) {
		try {
			tsysDataprivilegeMapper.deleteBatch(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok();
	}
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param id
	 * @return
	 */
	@Override
	public R view( String id) {
		TsysDataprivilege tsysDataprivilege = tsysDataprivilegeMapper.selectObjectById(id);
		return R.ok().put("tsysDataprivilege", tsysDataprivilege);
	}
	
	/**
	 * <p>保存角色与数据权限关系</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param roleId
	 * @param orgIdList
	 * @return
	 */
	@Override
	@Transactional
	public R saveOrUpdate(String roleId, List<String> orgIdList) {
		// 先删除角色与数据权限关系
        tsysDataprivilegeMapper.delete(roleId);
        if (orgIdList == null || orgIdList.size() == 0) {
            return R.ok();
        }
        List<TsysDataprivilege> insertList = new ArrayList<>();
        orgIdList.stream().forEach(orgId -> {
            TsysDataprivilege t = new TsysDataprivilege();
            t.setRoleOrgid(Identities.uuid());
            t.setRoleId(roleId);
            t.setOrgId(orgId);
            insertList.add(t);
        });
        if (insertList != null && insertList.size() > 0) {
            tsysDataprivilegeMapper.insertBatch(insertList);
        }
        return R.ok();
	}
	

	/**
	 * <p>验证参数</p>
	 * @author huj
	 * @data 2019年5月6日
	 */
	private void verifyForm(TsysDataprivilege  tsysDataprivilege) {
		/*if (StringUtils.isBlank("")) { 
			throw new BudaosException("****不能为空"); 
		}*/
	}

	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	public TsysDataprivilege selectObjectById(String id) {
		return tsysDataprivilegeMapper.selectObjectById(id);
	}

	/**
	 * 根据条件查询记录
	 * 
	 * @param map
	 * @throws BudaosException
	 */
	public List<TsysDataprivilege> selectListByMap(Map<String, Object> map) {
		return tsysDataprivilegeMapper.selectListByMap(map);
	}
	/**
	 * 根据角色ID，获取机构ID列表
	 */
	public List<String> selectOrgListByRoleId(String roleId) {
		return tsysDataprivilegeMapper.selectOrgListByRoleId(roleId);
	}
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	public List<String> selectOrgListByRoleId(String[] roleIds) {
	 return tsysDataprivilegeMapper.selectOrgListByRoleIds(roleIds);
	}
	
	/**
	 * 获取数据过滤的SQL
	 */
	@Cacheable(value = "authorization_cache", key = "'getSQLFilter_'+#userId+'_'+#tableAlias")
	public String getSQLFilter(String userId, String tableAlias, Object point) {
		MethodSignature signature = (MethodSignature) ((JoinPoint)point).getSignature();
		DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
		String datascope = dataFilter.customDataAuth();
		/******************************
		 * 基于注解型或配置型数据权限支持
		 *************************************************************/
		// 1、通过用户id查询用户归属部门：同一用户会涉及多个部门的情况，同时用户可能多角色的情况(配置数据权限时合并)
		Set<String> orgIdList = new HashSet<>();
		StringBuilder sqlFilter = new StringBuilder();
		// 数据范围（1：所有数据；2：所在机构及以下数据；3：所在机构数据；4：仅本人数据；5：按明细设置）
		// 获取用户分配的角色数据权限，根据角色数据权限范围来匹配相应的数据权限
		// 用户自定义角色:5：按明细设置
		List<String> roleIdList = tuserRoleService.selectRoleListByUserId(userId);
		if (roleIdList.size() > 0) {
			for (String roleId : roleIdList) {
				TsysRole tsysRole = tsysRoleService.selectObjectByRoleId(roleId);
				datascope = tsysRole.getDataScope();
				// 5自定义明细的情况
				if (dataFilter.customOrg() || datascope.equalsIgnoreCase("5")) {
					List<String> useOrgIdList = selectOrgListByRoleId(roleIdList.toArray(new String[roleIdList.size()]));
					orgIdList.addAll(useOrgIdList);
				}

				// 1查询所有数据
				if (dataFilter.allOrg() || datascope.equalsIgnoreCase("1")) {
					List<String> allOrgIdList = tsysOrgMapper.getAllOrgIds();
					orgIdList.addAll(allOrgIdList);
				}
				// 2：所在机构及以下数据
				List<String> org = torgUserService.selectListByUserId(userId);
				for (String orgid : org) {
					// 用户查询本部门及子部门数据
					if (dataFilter.selfAndSubOrg() || datascope.equalsIgnoreCase("2")) {
						@SuppressWarnings("unchecked")
						List<String> subOrgIdList = (List<String>) tsysOrgService.getSubOrgIdList(orgid)
								.get(Constant.R_DATA);
						orgIdList.add(orgid);
						orgIdList.addAll(subOrgIdList);
					}
					// 3：所在机构:本机构数据(此问题暂时还没解决，构造树时根结点的问题)
					if (dataFilter.selfOrg() || datascope.equalsIgnoreCase("3")) {
						// List<String> subOrgIdList = tsysOrgService.getSubOrgIdList(orgid);
						// orgIdList.add("-1");
						// orgIdList.addAll(subOrgIdList);
						orgIdList.add(orgid);
					}
				}
				// orgIdList.addAll(org);
				// 4用户只能查询本人数据
				if (dataFilter.selfUser() || datascope.equalsIgnoreCase("4")) {
					sqlFilter.append(tableAlias).append("create_user_id='").append(userId + "' ");
				}

			}
		} 
		if (orgIdList.size() > 0) {
			if (sqlFilter.toString().length() > 0) {
				sqlFilter.append(" or");
			}
			sqlFilter.append(" (");
			sqlFilter.append(tableAlias).append("org_id in(").append("'").append(StringUtils.join(orgIdList, "','"))
					.append("'").append(")");
			sqlFilter.append(")");
		}else {
			if (sqlFilter.toString().length() > 0) {
				sqlFilter.append(" or");
			}
			sqlFilter.append(" 1!=1");
		}
		log.debug("数据权限sql：" + sqlFilter);
		return sqlFilter.toString();
	}
}
