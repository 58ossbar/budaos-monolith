package com.budaos.modules.evgl.site.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.api.TevglSiteVideoRelationService;
import com.budaos.modules.evgl.site.domain.TevglSiteVideoRelation;
import com.budaos.modules.evgl.site.persistence.TevglSiteVideoRelationMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
public class TevglSiteVideoRelationServiceImpl implements TevglSiteVideoRelationService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteVideoRelationServiceImpl.class);
	@Autowired
	private TevglSiteVideoRelationMapper tevglSiteVideoRelationMapper;
	
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
		List<TevglSiteVideoRelation> tevglSiteVideoRelationList = tevglSiteVideoRelationMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteVideoRelationList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteVideoRelationList = tevglSiteVideoRelationMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteVideoRelationList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteVideoRelation
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglSiteVideoRelation tevglSiteVideoRelation) throws BudaosException {
		tevglSiteVideoRelation.setVrId(Identities.uuid());
		ValidatorUtils.check(tevglSiteVideoRelation);
		tevglSiteVideoRelationMapper.insert(tevglSiteVideoRelation);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteVideoRelation
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSiteVideoRelation tevglSiteVideoRelation) throws BudaosException {
	    ValidatorUtils.check(tevglSiteVideoRelation);
		tevglSiteVideoRelationMapper.update(tevglSiteVideoRelation);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteVideoRelationMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglSiteVideoRelationMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteVideoRelationMapper.selectObjectById(id));
	}

	/**
	 * 批量新增
	 * @param list
	 * @throws BudaosException
	 */
	@Override
	public void insertBatch(List<TevglSiteVideoRelation> list) throws BudaosException {
		tevglSiteVideoRelationMapper.insertBatch(list);
	}

	@Override
	public List<TevglSiteVideoRelation> selectListByMap(Map<String, Object> map) {
		return tevglSiteVideoRelationMapper.selectListByMap(map);
	}
	
}
