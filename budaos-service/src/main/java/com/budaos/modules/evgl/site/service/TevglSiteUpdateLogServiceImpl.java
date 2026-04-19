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
import com.budaos.modules.evgl.site.api.TevglSiteUpdateLogService;
import com.budaos.modules.evgl.site.domain.TevglSiteUpdateLog;
import com.budaos.modules.evgl.site.persistence.TevglSiteUpdateLogMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
public class TevglSiteUpdateLogServiceImpl implements TevglSiteUpdateLogService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteUpdateLogServiceImpl.class);
	@Autowired
	private TevglSiteUpdateLogMapper tevglSiteUpdateLogMapper;
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
		List<TevglSiteUpdateLog> tevglSiteUpdateLogList = tevglSiteUpdateLogMapper.selectListByMap(query);
		convertUtil.convertDict(tevglSiteUpdateLogList, "type", "feedbackBigType");
		convertUtil.convertUserId2RealName(tevglSiteUpdateLogList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglSiteUpdateLogList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglSiteUpdateLogList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteUpdateLogList = tevglSiteUpdateLogMapper.selectListMapByMap(query);
		convertUtil.convertDict(tevglSiteUpdateLogList, "type", "feedbackBigType");
		convertUtil.convertUserId2RealName(tevglSiteUpdateLogList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteUpdateLogList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteUpdateLog
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglSiteUpdateLog tevglSiteUpdateLog) throws BudaosException {
		tevglSiteUpdateLog.setLogId(Identities.uuid());
		tevglSiteUpdateLog.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteUpdateLog.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteUpdateLog);
		tevglSiteUpdateLogMapper.insert(tevglSiteUpdateLog);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteUpdateLog
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglSiteUpdateLog tevglSiteUpdateLog) throws BudaosException {
	    tevglSiteUpdateLog.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSiteUpdateLog.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSiteUpdateLog);
		tevglSiteUpdateLogMapper.update(tevglSiteUpdateLog);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteUpdateLogMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglSiteUpdateLogMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteUpdateLogMapper.selectObjectById(id));
	}

	/**
	 * 查询更新日志
	 * @param params
	 * @return
	 */
	@Override
	public R queryUpdateLog( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglSiteUpdateLog> tevglSiteUpdateLogList = tevglSiteUpdateLogMapper.selectListByMap(query);
		List<Map<String, Object>> list = tevglSiteUpdateLogList.stream().map(a -> {
			Map<String, Object> info = new HashMap<>();
			info.put("logId", a.getLogId());
			info.put("version", a.getVersion());
			info.put("content", a.getContent());
			info.put("createTime", a.getCreateTime().substring(0, 10));
			return info;
		}).collect(Collectors.toList());
		PageUtils pageUtil = new PageUtils(list,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
}
