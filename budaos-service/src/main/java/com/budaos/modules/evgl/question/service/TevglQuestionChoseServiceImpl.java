package com.budaos.modules.evgl.question.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.api.TevglQuestionChoseService;
import com.budaos.modules.evgl.question.domain.TevglQuestionChose;
import com.budaos.modules.evgl.question.persistence.TevglQuestionChoseMapper;
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
 * <p> Title: 题目选项</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglQuestionChoseServiceImpl implements TevglQuestionChoseService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglQuestionChoseServiceImpl.class);
	@Autowired
	private TevglQuestionChoseMapper tevglQuestionChoseMapper;
	
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
		List<TevglQuestionChose> tevglQuestionChoseList = tevglQuestionChoseMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglQuestionChoseList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglQuestionChoseList = tevglQuestionChoseMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglQuestionChoseList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglQuestionChose
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglQuestionChose tevglQuestionChose) throws BudaosException {
		tevglQuestionChose.setOptionId(Identities.uuid());
		ValidatorUtils.check(tevglQuestionChose);
		tevglQuestionChoseMapper.insert(tevglQuestionChose);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglQuestionChose
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglQuestionChose tevglQuestionChose) throws BudaosException {
	    ValidatorUtils.check(tevglQuestionChose);
		tevglQuestionChoseMapper.update(tevglQuestionChose);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglQuestionChoseMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglQuestionChoseMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglQuestionChoseMapper.selectObjectById(id));
	}

	/**
	 * <p>根据条件查询记录</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param map
	 * @return
	 */
	@Override
	public List<TevglQuestionChose> selectListByMap(Map<String, Object> map) {
		return tevglQuestionChoseMapper.selectListByMap(map);
	}

	/**
	 * <p>根据题目正确选项ID批量查询 </p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param arrays
	 * @return
	 */
	@Override
	public List<TevglQuestionChose> selectBatchQuestionsChoseByReplyIds(String[] arrays) {
		return tevglQuestionChoseMapper.selectBatchQuestionsChoseByReplyIds(arrays);
	}

	@Override
	public TevglQuestionChose selectObjectById(Object id) {
		return tevglQuestionChoseMapper.selectObjectById(id);
	}
}
