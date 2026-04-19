package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityVoteQuestionnaireQuestionOptionService;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireQuestionOption;
import com.budaos.modules.evgl.activity.persistence.TevglActivityVoteQuestionnaireQuestionOptionMapper;
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
 * <p> Title: 活动-投票/问卷 -> 题目选项</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivityVoteQuestionnaireQuestionOptionServiceImpl implements TevglActivityVoteQuestionnaireQuestionOptionService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityVoteQuestionnaireQuestionOptionServiceImpl.class);
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionOptionMapper tevglActivityVoteQuestionnaireQuestionOptionMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityVoteQuestionnaireQuestionOption> tevglActivityVoteQuestionnaireQuestionOptionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireQuestionOptionList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>>)
	 * @param Map
	 * @return R
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityVoteQuestionnaireQuestionOptionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireQuestionOptionList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityVoteQuestionnaireQuestionOption
	 * @throws BudaosException
	 */
	public R save(TevglActivityVoteQuestionnaireQuestionOption tevglActivityVoteQuestionnaireQuestionOption) throws BudaosException {
		tevglActivityVoteQuestionnaireQuestionOption.setOptionId(Identities.uuid());
		ValidatorUtils.check(tevglActivityVoteQuestionnaireQuestionOption);
		tevglActivityVoteQuestionnaireQuestionOptionMapper.insert(tevglActivityVoteQuestionnaireQuestionOption);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityVoteQuestionnaireQuestionOption
	 * @throws BudaosException
	 */
	public R update(TevglActivityVoteQuestionnaireQuestionOption tevglActivityVoteQuestionnaireQuestionOption) throws BudaosException {
	    ValidatorUtils.check(tevglActivityVoteQuestionnaireQuestionOption);
		tevglActivityVoteQuestionnaireQuestionOptionMapper.update(tevglActivityVoteQuestionnaireQuestionOption);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityVoteQuestionnaireQuestionOptionMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityVoteQuestionnaireQuestionOptionMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityVoteQuestionnaireQuestionOptionMapper.selectObjectById(id));
	}
}
