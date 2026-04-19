package com.budaos.modules.evgl.tch.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomTopService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTop;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTopMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class TevglTchClassroomTopServiceImpl implements TevglTchClassroomTopService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomTopServiceImpl.class);
	@Autowired
	private TevglTchClassroomTopMapper tevglTchClassroomTopMapper;
	
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
		List<TevglTchClassroomTop> tevglTchClassroomTopList = tevglTchClassroomTopMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomTopList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglTchClassroomTopList = tevglTchClassroomTopMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomTopList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchClassroomTop
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglTchClassroomTop tevglTchClassroomTop) throws BudaosException {
		tevglTchClassroomTop.setTopId(Identities.uuid());
		ValidatorUtils.check(tevglTchClassroomTop);
		tevglTchClassroomTopMapper.insert(tevglTchClassroomTop);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClassroomTop
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglTchClassroomTop tevglTchClassroomTop) throws BudaosException {
	    ValidatorUtils.check(tevglTchClassroomTop);
		tevglTchClassroomTopMapper.update(tevglTchClassroomTop);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglTchClassroomTopMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTchClassroomTopMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomTopMapper.selectObjectById(id));
	}

	@Override
	public R setTop(String ctId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("ctId", ctId);
		ps.put("traineeId", loginUserId);
		List<TevglTchClassroomTop> list = tevglTchClassroomTopMapper.selectListByMap(ps);
		if (list == null || list.size() == 0) {
			TevglTchClassroomTop top = new TevglTchClassroomTop();
			top.setTopId(Identities.uuid());
			top.setTraineeId(loginUserId);
			top.setCtId(ctId);
			top.setCreateTime(DateUtils.getNowTimeStamp());
			top.setUpdateTime(DateUtils.getNowTimeStamp());
			tevglTchClassroomTopMapper.insert(top);
		} else {
			TevglTchClassroomTop tevglTchClassroomTop = list.get(0);
			TevglTchClassroomTop top = new TevglTchClassroomTop();
			top.setTopId(tevglTchClassroomTop.getTopId());
			top.setUpdateTime(DateUtils.getNowTimeStamp());
			tevglTchClassroomTopMapper.update(top);
		}
		return R.ok("置顶成功");
	}

	/**
	 * 取消置顶
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R cancelTop(String ctId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("ctId", ctId);
		ps.put("traineeId", loginUserId);
		List<TevglTchClassroomTop> list = tevglTchClassroomTopMapper.selectListByMap(ps);
		if (list != null && list.size() > 0) {
			for (TevglTchClassroomTop tevglTchClassroomTop : list) {
				tevglTchClassroomTopMapper.delete(tevglTchClassroomTop.getTopId());
			}
		}
		return R.ok("取消置顶");
	}
}
