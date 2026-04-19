package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityTraineeJoinInfoService;
import com.budaos.modules.evgl.activity.domain.TevglActivityTraineeJoinInfo;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTraineeJoinInfoMapper;
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
 * <p> Title: 学员活动参与情况信息</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglActivityTraineeJoinInfoServiceImpl implements TevglActivityTraineeJoinInfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityTraineeJoinInfoServiceImpl.class);
	@Autowired
	private TevglActivityTraineeJoinInfoMapper tevglActivityTraineeJoinInfoMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityTraineeJoinInfo> tevglActivityTraineeJoinInfoList = tevglActivityTraineeJoinInfoMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivityTraineeJoinInfoList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglActivityTraineeJoinInfoList = tevglActivityTraineeJoinInfoMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivityTraineeJoinInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityTraineeJoinInfo
	 * @throws BudaosException
	 */
	public R save(TevglActivityTraineeJoinInfo tevglActivityTraineeJoinInfo) throws BudaosException {
		tevglActivityTraineeJoinInfo.setAjId(Identities.uuid());
		ValidatorUtils.check(tevglActivityTraineeJoinInfo);
		tevglActivityTraineeJoinInfoMapper.insert(tevglActivityTraineeJoinInfo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityTraineeJoinInfo
	 * @throws BudaosException
	 */
	public R update(TevglActivityTraineeJoinInfo tevglActivityTraineeJoinInfo) throws BudaosException {
	    ValidatorUtils.check(tevglActivityTraineeJoinInfo);
		tevglActivityTraineeJoinInfoMapper.update(tevglActivityTraineeJoinInfo);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityTraineeJoinInfoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityTraineeJoinInfoMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityTraineeJoinInfoMapper.selectObjectById(id));
	}
}
