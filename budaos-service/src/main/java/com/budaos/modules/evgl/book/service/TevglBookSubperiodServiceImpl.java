package com.budaos.modules.evgl.book.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.api.TevglBookSubperiodService;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.domain.TevglBookSubperiod;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubperiodMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class TevglBookSubperiodServiceImpl implements TevglBookSubperiodService {
	@Autowired
	private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;

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
		List<TevglBookSubperiod> tevglBookSubperiodList = tevglBookSubperiodMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookSubperiodList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglBookSubperiodList = tevglBookSubperiodMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookSubperiodList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglBookSubperiod
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglBookSubperiod tevglBookSubperiod) throws BudaosException {
		tevglBookSubperiod.setSubperiodId(Identities.uuid());
		ValidatorUtils.check(tevglBookSubperiod);
		tevglBookSubperiodMapper.insert(tevglBookSubperiod);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglBookSubperiod
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglBookSubperiod tevglBookSubperiod) throws BudaosException {
	    ValidatorUtils.check(tevglBookSubperiod);
		tevglBookSubperiodMapper.update(tevglBookSubperiod);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglBookSubperiodMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglBookSubperiodMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		TevglBookSubperiod subperiod = tevglBookSubperiodMapper.selectObjectById(id);
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(subperiod.getSubjectId());
		subject.setSubjectProperty(subperiod.getSubjectProperty());
		subject.setTerm(subperiod.getTerm());
		// 查询所在的职业路径，可能会有多个
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subjectId", subject.getSubjectId());
		List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			String majorIds = list.stream().map(a -> a.getMajorId())
			.distinct()
			.collect(Collectors.joining(","));
			subject.setMajorId(majorIds);
		}
		return R.ok().put(Constant.R_DATA, subject);
	}
	
	/**
	 * <p>获取技术</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	@Override
	@SysLog(value="查询技术")
	public List<Map<String, Object>> selectListForEvglWeb(Map<String, Object> map) {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> list = tevglBookSubperiodMapper.selectListForEvglWeb(map);
		if (list.size() > 0) {
			list.forEach(a -> {
				if (a != null && !"null".equals(a)) {
					result.add(a);
				}
			});
		}
		return result;
	}
}
