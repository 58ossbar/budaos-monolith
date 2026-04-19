package com.budaos.modules.evgl.eao.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.eao.api.TeaoTraineeExammemberService;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExammember;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExammemberMapper;
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
 * 	考试成员
 */

@Service
public class TeaoTraineeExammemberServiceImpl implements TeaoTraineeExammemberService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TeaoTraineeExammemberServiceImpl.class);
	@Autowired
	private TeaoTraineeExammemberMapper teaoTraineeExammemberMapper;

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TeaoTraineeExammember> teaoTraineeExammemberList = teaoTraineeExammemberMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(teaoTraineeExammemberList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> teaoTraineeExammemberList = teaoTraineeExammemberMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(teaoTraineeExammemberList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param teaoTraineeExammember
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TeaoTraineeExammember teaoTraineeExammember) throws BudaosException {
		teaoTraineeExammember.setEtId(Identities.uuid());
		ValidatorUtils.check(teaoTraineeExammember);
		teaoTraineeExammemberMapper.insert(teaoTraineeExammember);
		return R.ok();
	}
	/**
	 * 修改
	 * @param teaoTraineeExammember
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TeaoTraineeExammember teaoTraineeExammember) throws BudaosException {
	    ValidatorUtils.check(teaoTraineeExammember);
		teaoTraineeExammemberMapper.update(teaoTraineeExammember);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		teaoTraineeExammemberMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		teaoTraineeExammemberMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, teaoTraineeExammemberMapper.selectObjectById(id));
	}
}
