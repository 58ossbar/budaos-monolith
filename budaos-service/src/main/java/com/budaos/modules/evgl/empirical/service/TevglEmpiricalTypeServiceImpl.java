package com.budaos.modules.evgl.empirical.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.empirical.api.TevglEmpiricalTypeService;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalType;
import com.budaos.modules.evgl.empirical.persistence.TevglEmpiricalTypeMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglEmpiricalTypeServiceImpl implements TevglEmpiricalTypeService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglEmpiricalTypeServiceImpl.class);
	@Autowired
	private TevglEmpiricalTypeMapper tevglEmpiricalTypeMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglEmpiricalType> tevglEmpiricalTypeList = tevglEmpiricalTypeMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglEmpiricalTypeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglEmpiricalTypeList = tevglEmpiricalTypeMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglEmpiricalTypeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglEmpiricalType
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglEmpiricalType tevglEmpiricalType) throws BudaosException {
		tevglEmpiricalType.setTypeId(Identities.uuid());
		ValidatorUtils.check(tevglEmpiricalType);
		tevglEmpiricalTypeMapper.insert(tevglEmpiricalType);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglEmpiricalType
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglEmpiricalType tevglEmpiricalType) throws BudaosException {
	    ValidatorUtils.check(tevglEmpiricalType);
		tevglEmpiricalTypeMapper.update(tevglEmpiricalType);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglEmpiricalTypeMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglEmpiricalTypeMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglEmpiricalTypeMapper.selectObjectById(id));
	}
}
