package com.budaos.modules.evgl.site.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.api.TevglSiteCheckinfoService;
import com.budaos.modules.evgl.site.domain.TevglSiteCheckinfo;
import com.budaos.modules.evgl.site.persistence.TevglSiteCheckinfoMapper;
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
public class TevglSiteCheckinfoServiceImpl implements TevglSiteCheckinfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteCheckinfoServiceImpl.class);
	@Autowired
	private TevglSiteCheckinfoMapper tevglSiteCheckinfoMapper;
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
		List<TevglSiteCheckinfo> tevglSiteCheckinfoList = tevglSiteCheckinfoMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteCheckinfoList, "createUserId", "updateUserId");
		convertUtil.convertDict(tevglSiteCheckinfoList, "state", "state4"); // 状态(Y有效N无效)
		convertUtil.convertDict(tevglSiteCheckinfoList, "pass", "isPass");// 是否通过(Y通过N未通过)
		PageUtils pageUtil = new PageUtils(tevglSiteCheckinfoList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteCheckinfoList = tevglSiteCheckinfoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteCheckinfoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteCheckinfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteCheckinfo
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglSiteCheckinfo tevglSiteCheckinfo) throws BudaosException {
		tevglSiteCheckinfo.setId(Identities.uuid());
		tevglSiteCheckinfo.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteCheckinfo.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteCheckinfo);
		tevglSiteCheckinfoMapper.insert(tevglSiteCheckinfo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteCheckinfo
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSiteCheckinfo tevglSiteCheckinfo) throws BudaosException {
	    ValidatorUtils.check(tevglSiteCheckinfo);
		tevglSiteCheckinfoMapper.update(tevglSiteCheckinfo);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglSiteCheckinfoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglSiteCheckinfoMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteCheckinfoMapper.selectObjectById(id));
	}
}
