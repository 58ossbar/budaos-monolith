package com.budaos.modules.evgl.site.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.api.TevglSiteSeoService;
import com.budaos.modules.evgl.site.domain.TevglSiteSeo;
import com.budaos.modules.evgl.site.persistence.TevglSiteSeoMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class TevglSiteSeoServiceImpl implements TevglSiteSeoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteSeoServiceImpl.class);
	@Autowired
	private TevglSiteSeoMapper tevglSiteSeoMapper;
	
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
		List<TevglSiteSeo> tevglSiteSeoList = tevglSiteSeoMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteSeoList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteSeoList = tevglSiteSeoMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteSeoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteSeo
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglSiteSeo tevglSiteSeo) throws BudaosException {
		tevglSiteSeo.setSeoId(Identities.uuid());
		ValidatorUtils.check(tevglSiteSeo);
		tevglSiteSeoMapper.insert(tevglSiteSeo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteSeo
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSiteSeo tevglSiteSeo) throws BudaosException {
	    ValidatorUtils.check(tevglSiteSeo);
		tevglSiteSeoMapper.update(tevglSiteSeo);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteSeoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglSiteSeoMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteSeoMapper.selectObjectById(id));
	}

	
	/**
	 * <p>执行数据保存和修改</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param tevglSiteSeo
	 * @param request
	 * @param response
	 * @return
	 */
	public R saveorupdate(TevglSiteSeo tevglSiteSeo ,HttpServletRequest request, HttpServletResponse response) {
		try {
			// 数据校验
			ValidatorUtils.check(tevglSiteSeo);
			// 新增
		    if ((tevglSiteSeo.getSeoId() == null) || ("").equals(tevglSiteSeo.getSeoId())) {
		    	tevglSiteSeoMapper.insert(tevglSiteSeo);
			} else {
				tevglSiteSeoMapper.update(tevglSiteSeo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		//如果已发布，则重新发布
		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());	
		try {
			String id = tepsiteSeo.getSeoRelation();
		} catch (Exception e) {
			logger.error("文档发布", e);
		}
		*/
		return R.ok().put("seoId", tevglSiteSeo.getSeoId());
	}
	
	/**
	 * <p>进入编辑界面时，需要的数据</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public TevglSiteSeo editSiteSeo(HttpServletRequest request, HttpServletResponse response) {
		TevglSiteSeo tevglSiteSeo = tevglSiteSeoMapper.selectObjectById(request.getParameter("seoId"));
		if(tevglSiteSeo == null){
			tevglSiteSeo = new TevglSiteSeo();
			tevglSiteSeo.setSeoRelation(request.getParameter("siteId"));
			tevglSiteSeo.setSeoType(request.getParameter("seoType"));
		}
		return tevglSiteSeo;
	}
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param id
	 * @return
	 */
	@Override
	public TevglSiteSeo selectObjectByRelationId(String id) {
		return tevglSiteSeoMapper.selectObjectByRelationId(id);
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param ids
	 * @param request
	 * @param response
	 */
	@Override
	public void deleteSeo(String[] ids, HttpServletRequest request, HttpServletResponse response) {
		
	}

}
