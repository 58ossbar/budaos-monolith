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
import com.budaos.modules.common.annotation.NoRepeatSubmit;
import com.budaos.modules.evgl.site.api.TevglSiteJoinUsService;
import com.budaos.modules.evgl.site.domain.TevglSiteJoinUs;
import com.budaos.modules.evgl.site.persistence.TevglSiteJoinUsMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class TevglSiteJoinUsServiceImpl implements TevglSiteJoinUsService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteJoinUsServiceImpl.class);
	@Autowired
	private TevglSiteJoinUsMapper tevglSiteJoinUsMapper;
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
		List<TevglSiteJoinUs> tevglSiteJoinUsList = tevglSiteJoinUsMapper.selectListByMap(query);
		convertUtil.convertDict(tevglSiteJoinUsList, "type", "cooperation_model");
		PageUtils pageUtil = new PageUtils(tevglSiteJoinUsList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteJoinUsList = tevglSiteJoinUsMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteJoinUsList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteJoinUsList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteJoinUs
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglSiteJoinUs tevglSiteJoinUs) throws BudaosException {
		tevglSiteJoinUs.setId(Identities.uuid());
		tevglSiteJoinUs.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteJoinUs.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteJoinUs);
		tevglSiteJoinUsMapper.insert(tevglSiteJoinUs);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteJoinUs
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSiteJoinUs tevglSiteJoinUs) throws BudaosException {
	    ValidatorUtils.check(tevglSiteJoinUs);
		tevglSiteJoinUsMapper.update(tevglSiteJoinUs);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteJoinUsMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglSiteJoinUsMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteJoinUsMapper.selectObjectById(id));
	}

	/**
	 * 加入我们
	 * @param tevglSiteJoinUs
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R joinUs(TevglSiteJoinUs tevglSiteJoinUs, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("createUserId", loginUserId);
		List<TevglSiteJoinUs> list = tevglSiteJoinUsMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			boolean flag = list.stream().anyMatch(a -> a.getMobile().equals(tevglSiteJoinUs.getMobile()));
			if (flag) {
				return R.ok("你已经提交过该联系方式等信息了，无需多次提交");
			}
		}
		tevglSiteJoinUs.setId(Identities.uuid());
		tevglSiteJoinUs.setCreateUserId(loginUserId);
		tevglSiteJoinUs.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteJoinUs);
		tevglSiteJoinUsMapper.insert(tevglSiteJoinUs);
		return R.ok("您好，已经收到了您填写的信息，我们尽快与您联系，请保持手机号码联系畅通");
	}
	
	/**
	 * 手机格式验证
	 * 
	 * @param mobile
	 * @return
	 */
	private boolean isMobile(String mobile) {
		Pattern p = Pattern.compile("^1[3456789]\\d{9}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 邮箱格式验证
	 * @param email
	 * @return
	 */
	private boolean isEmail(String email) {
		return email.matches("^\\w+@(\\w+\\.)+\\w+$");
	}
}
