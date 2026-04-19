package com.budaos.modules.evgl.examine.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.examine.api.TevglExaminePaperScoreGapfillingService;
import com.budaos.modules.evgl.examine.domain.TevglExaminePaperScoreGapfilling;
import com.budaos.modules.evgl.examine.persistence.TevglExaminePaperScoreGapfillingMapper;
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
public class TevglExaminePaperScoreGapfillingServiceImpl implements TevglExaminePaperScoreGapfillingService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglExaminePaperScoreGapfillingServiceImpl.class);
	@Autowired
	private TevglExaminePaperScoreGapfillingMapper tevglExaminePaperScoreGapfillingMapper;
	
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
		List<TevglExaminePaperScoreGapfilling> tevglExaminePaperScoreGapfillingList = tevglExaminePaperScoreGapfillingMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglExaminePaperScoreGapfillingList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglExaminePaperScoreGapfillingList = tevglExaminePaperScoreGapfillingMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglExaminePaperScoreGapfillingList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglExaminePaperScoreGapfilling
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglExaminePaperScoreGapfilling tevglExaminePaperScoreGapfilling) throws BudaosException {
		tevglExaminePaperScoreGapfilling.setId(Identities.uuid());
		ValidatorUtils.check(tevglExaminePaperScoreGapfilling);
		tevglExaminePaperScoreGapfillingMapper.insert(tevglExaminePaperScoreGapfilling);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglExaminePaperScoreGapfilling
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglExaminePaperScoreGapfilling tevglExaminePaperScoreGapfilling) throws BudaosException {
	    ValidatorUtils.check(tevglExaminePaperScoreGapfilling);
		tevglExaminePaperScoreGapfillingMapper.update(tevglExaminePaperScoreGapfilling);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglExaminePaperScoreGapfillingMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglExaminePaperScoreGapfillingMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglExaminePaperScoreGapfillingMapper.selectObjectById(id));
	}
}
