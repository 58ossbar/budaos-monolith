package com.budaos.modules.evgl.question.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.api.TepExamineDynamicQuestionsOptionsService;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicQuestionsOptions;
import com.budaos.modules.evgl.question.persistence.TepExamineDynamicQuestionsOptionsMapper;
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
public class TepExamineDynamicQuestionsOptionsServiceImpl implements TepExamineDynamicQuestionsOptionsService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TepExamineDynamicQuestionsOptionsServiceImpl.class);
	@Autowired
	private TepExamineDynamicQuestionsOptionsMapper tepExamineDynamicQuestionsOptionsMapper;
	
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
		List<TepExamineDynamicQuestionsOptions> tepExamineDynamicQuestionsOptionsList = tepExamineDynamicQuestionsOptionsMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tepExamineDynamicQuestionsOptionsList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tepExamineDynamicQuestionsOptionsList = tepExamineDynamicQuestionsOptionsMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tepExamineDynamicQuestionsOptionsList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tepExamineDynamicQuestionsOptions
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TepExamineDynamicQuestionsOptions tepExamineDynamicQuestionsOptions) throws BudaosException {
		tepExamineDynamicQuestionsOptions.setDqoId(Identities.uuid());
		ValidatorUtils.check(tepExamineDynamicQuestionsOptions);
		tepExamineDynamicQuestionsOptionsMapper.insert(tepExamineDynamicQuestionsOptions);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tepExamineDynamicQuestionsOptions
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TepExamineDynamicQuestionsOptions tepExamineDynamicQuestionsOptions) throws BudaosException {
	    ValidatorUtils.check(tepExamineDynamicQuestionsOptions);
		tepExamineDynamicQuestionsOptionsMapper.update(tepExamineDynamicQuestionsOptions);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tepExamineDynamicQuestionsOptionsMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tepExamineDynamicQuestionsOptionsMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tepExamineDynamicQuestionsOptionsMapper.selectObjectById(id));
	}

	@Override
	public List<Map<String, Object>> selectListMapWithQuestionInfoByDyId(String dyId) {
		return tepExamineDynamicQuestionsOptionsMapper.selectListMapWithQuestionInfoByDyId(dyId);
	}
}
