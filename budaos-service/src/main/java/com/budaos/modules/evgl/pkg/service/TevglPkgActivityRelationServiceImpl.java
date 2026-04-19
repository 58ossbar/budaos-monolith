package com.budaos.modules.evgl.pkg.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.api.TevglPkgActivityRelationService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
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
 * <p> Title: 教学包与活动关系表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglPkgActivityRelationServiceImpl implements TevglPkgActivityRelationService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgActivityRelationServiceImpl.class);
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglPkgActivityRelation> tevglPkgActivityRelationList = tevglPkgActivityRelationMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglPkgActivityRelationList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglPkgActivityRelationList = tevglPkgActivityRelationMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglPkgActivityRelationList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglPkgActivityRelation
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglPkgActivityRelation tevglPkgActivityRelation) throws BudaosException {
		tevglPkgActivityRelation.setPaId(Identities.uuid());
		ValidatorUtils.check(tevglPkgActivityRelation);
		tevglPkgActivityRelationMapper.insert(tevglPkgActivityRelation);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglPkgActivityRelation
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglPkgActivityRelation tevglPkgActivityRelation) throws BudaosException {
	    ValidatorUtils.check(tevglPkgActivityRelation);
		tevglPkgActivityRelationMapper.update(tevglPkgActivityRelation);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglPkgActivityRelationMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglPkgActivityRelationMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglPkgActivityRelationMapper.selectObjectById(id));
	}
	
	/**
	 * 根据条件查询活动
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params) {
		return tevglPkgActivityRelationMapper.selectSimpleListMap(params);
	}

	@Override
	public List<TevglPkgActivityRelation> selectListByMap(Map<String, Object> params) {
		return tevglPkgActivityRelationMapper.selectListByMap(params);
	}
}
