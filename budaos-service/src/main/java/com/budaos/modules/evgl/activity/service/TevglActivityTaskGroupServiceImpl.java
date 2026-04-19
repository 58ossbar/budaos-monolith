package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityTaskGroupService;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskGroup;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskGroupMapper;
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
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivityTaskGroupServiceImpl implements TevglActivityTaskGroupService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityTaskGroupServiceImpl.class);
	@Autowired
	private TevglActivityTaskGroupMapper tevglActivityTaskGroupMapper;
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
		List<TevglActivityTaskGroup> tevglActivityTaskGroupList = tevglActivityTaskGroupMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskGroupList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityTaskGroupList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskGroupList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglActivityTaskGroupList = tevglActivityTaskGroupMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskGroupList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskGroupList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityTaskGroup
	 * @throws BudaosException
	 */
	public R save(TevglActivityTaskGroup tevglActivityTaskGroup) throws BudaosException {
		tevglActivityTaskGroup.setGroupId(Identities.uuid());
		tevglActivityTaskGroup.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityTaskGroup.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityTaskGroup);
		tevglActivityTaskGroupMapper.insert(tevglActivityTaskGroup);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityTaskGroup
	 * @throws BudaosException
	 */
	public R update(TevglActivityTaskGroup tevglActivityTaskGroup) throws BudaosException {
	    tevglActivityTaskGroup.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityTaskGroup.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityTaskGroup);
		tevglActivityTaskGroupMapper.update(tevglActivityTaskGroup);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityTaskGroupMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityTaskGroupMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityTaskGroupMapper.selectObjectById(id));
	}
}
