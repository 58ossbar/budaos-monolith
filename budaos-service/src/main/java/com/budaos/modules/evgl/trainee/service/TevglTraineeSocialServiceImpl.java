package com.budaos.modules.evgl.trainee.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.trainee.api.TevglTraineeSocialService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeSocial;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeSocialMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
public class TevglTraineeSocialServiceImpl implements TevglTraineeSocialService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTraineeSocialServiceImpl.class);
	@Autowired
	private TevglTraineeSocialMapper tevglTraineeSocialMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	
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
		List<TevglTraineeSocial> tevglTraineeSocialList = tevglTraineeSocialMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglTraineeSocialList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglTraineeSocialList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglTraineeSocialList = tevglTraineeSocialMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglTraineeSocialList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglTraineeSocialList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTraineeSocial
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglTraineeSocial tevglTraineeSocial) throws BudaosException {
		tevglTraineeSocial.setSocialId(Identities.uuid());
		tevglTraineeSocial.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglTraineeSocial.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglTraineeSocial);
		tevglTraineeSocialMapper.insert(tevglTraineeSocial);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTraineeSocial
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTraineeSocial tevglTraineeSocial) throws BudaosException {
	    tevglTraineeSocial.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglTraineeSocial.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglTraineeSocial);
		tevglTraineeSocialMapper.update(tevglTraineeSocial);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglTraineeSocialMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTraineeSocialMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglTraineeSocialMapper.selectObjectById(id));
	}
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return List<TevglTraineeSocial>
	 */
	public List<TevglTraineeSocial> queryByMap( Map<String, Object> params) {
		return tevglTraineeSocialMapper.selectListByMap(params);
	}
}
