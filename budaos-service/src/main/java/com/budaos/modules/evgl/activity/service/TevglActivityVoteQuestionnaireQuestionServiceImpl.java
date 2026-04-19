package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityVoteQuestionnaireQuestionService;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireQuestion;
import com.budaos.modules.evgl.activity.persistence.TevglActivityVoteQuestionnaireQuestionMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 活动-投票/问卷 -> 题目 </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivityVoteQuestionnaireQuestionServiceImpl implements TevglActivityVoteQuestionnaireQuestionService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityVoteQuestionnaireQuestionServiceImpl.class);
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionMapper tevglActivityVoteQuestionnaireQuestionMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityVoteQuestionnaireQuestion> tevglActivityVoteQuestionnaireQuestionList = tevglActivityVoteQuestionnaireQuestionMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityVoteQuestionnaireQuestionList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityVoteQuestionnaireQuestionList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireQuestionList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglActivityVoteQuestionnaireQuestionList = tevglActivityVoteQuestionnaireQuestionMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityVoteQuestionnaireQuestionList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireQuestionList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityVoteQuestionnaireQuestion
	 * @throws BudaosException
	 */
	public R save(TevglActivityVoteQuestionnaireQuestion tevglActivityVoteQuestionnaireQuestion) throws BudaosException {
		tevglActivityVoteQuestionnaireQuestion.setQuestionId(Identities.uuid());
		tevglActivityVoteQuestionnaireQuestion.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityVoteQuestionnaireQuestion.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityVoteQuestionnaireQuestion);
		tevglActivityVoteQuestionnaireQuestionMapper.insert(tevglActivityVoteQuestionnaireQuestion);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityVoteQuestionnaireQuestion
	 * @throws BudaosException
	 */
	public R update(TevglActivityVoteQuestionnaireQuestion tevglActivityVoteQuestionnaireQuestion) throws BudaosException {
	    tevglActivityVoteQuestionnaireQuestion.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityVoteQuestionnaireQuestion.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityVoteQuestionnaireQuestion);
		tevglActivityVoteQuestionnaireQuestionMapper.update(tevglActivityVoteQuestionnaireQuestion);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityVoteQuestionnaireQuestionMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityVoteQuestionnaireQuestionMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityVoteQuestionnaireQuestionMapper.selectObjectById(id));
	}
}
