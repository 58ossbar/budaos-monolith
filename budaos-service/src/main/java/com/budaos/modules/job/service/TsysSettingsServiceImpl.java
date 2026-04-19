package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysSettingsMapper;
import com.budaos.modules.sys.api.TsysSettingsService;
import com.budaos.modules.sys.domain.TsysSettings;
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

//import com.budaos.modules.sys.domain.TsysUserinfo;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TsysSettingsServiceImpl implements TsysSettingsService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TsysSettingsServiceImpl.class);
	@Autowired
	private TsysSettingsMapper tsysSettingsMapper;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private ConvertUtil convertUtil;

	/**
	 * 查询列表(返回List<Bean>)
	 * 
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TsysSettings> tsysSettingsList = tsysSettingsMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tsysSettingsList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tsysSettingsList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * 
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tsysSettingsList = tsysSettingsMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tsysSettingsList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tsysSettingsList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 新增
	 * 
	 * @param tsysSettings
	 * @throws BudaosException
	 */
	@SysLog(value = "新增")
	public R save( TsysSettings tsysSettings) throws BudaosException {
		tsysSettings.setSettingId(Identities.uuid());
		tsysSettings.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tsysSettings.setCreateTime(DateUtils.getNowTimeStamp());
		//ValidatorUtils.check(tsysSettings);
		tsysSettingsMapper.insert(tsysSettings);
		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param tsysSettings
	 * @throws BudaosException
	 */
	@SysLog(value = "修改")
	public R update( TsysSettings tsysSettings) throws BudaosException {
		tsysSettings.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		tsysSettings.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tsysSettings);
		tsysSettingsMapper.update(tsysSettings);
		return R.ok();
	}

	/**
	 * 单条删除
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "单条删除")
	public R delete( String id) throws BudaosException {
		tsysSettingsMapper.delete(id);
		return R.ok();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value = "批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tsysSettingsMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tsysSettingsMapper.selectObjectById(id));
	}

	/**
	 * 批量修改
	 */
	@SysLog(value = "批量修改")
	public R updateBatchSettings(List<TsysSettings> settings) {
		settings.stream().forEach(setting -> {
			update(setting);
		});
		return R.ok();
	}

	/**
	 * 查询设置 settingType 系统设置或用户设置（必填） settingUserId 用户设置则必填
	 * 
	 * @param map
	 * @return
	 */
	@SysLog(value = "查询")
	@Override
	public R querySetting(Map<String, Object> map) {
		if (map.get("settingType") == null || map.get("settingType").equals("")) {
			map.put("settingType", "sys");
		}
		List<TsysSettings> settings = tsysSettingsMapper.selectListByMap(map);
		return R.ok().put("data", settings);
	}

	public enum SettingCodeEnum {
		// 登录页面logo标题
		LOGOTITLE("logoTitle", "logoTitle", "创蓝教育实训云平台"),
		// 公司信息
		COMPANYINFO("companyInfo", "companyInfo", "湖南创蓝信息科技有限公司 "),
		// 联系信息
		CONTACTINFO("contactInfo", "contactInfo", "0731-89913439"),
		// 创蓝图标
		CBLOGO("cbLogo", "cbLogo", "./static/img/logo.a39a449.png"),
		// 登录背景图
		LOGINBGIMG("loginBgImg", "loginBgImg", "../../static/img/bjt.565c312.jpg");
		public String getDeafultValue() {
			return deafultValue;
		}

		public void setDeafultValue(String deafultValue) {
			this.deafultValue = deafultValue;
		}

		private SettingCodeEnum(String settingCodeKey, String settingCodeValue, String deafultValue) {
			this.settingCodeKey = settingCodeKey;
			this.settingCodeValue = settingCodeValue;
			this.deafultValue = deafultValue;
		}

		private String settingCodeKey;
		private String settingCodeValue;
		private String deafultValue;

		public String getSettingCodeKey() {
			return settingCodeKey;
		}

		public void setSettingCodeKey(String settingCodeKey) {
			this.settingCodeKey = settingCodeKey;
		}

		public String getSettingCodeValue() {
			return settingCodeValue;
		}

		public void setSettingCodeValue(String settingCodeValue) {
			this.settingCodeValue = settingCodeValue;
		}

	}

	@SysLog(value = "根据id查询数据")
	@Override
	public R selectObjectById(String id) {
		return R.ok().put("data", tsysSettingsMapper.selectObjectById(id));
	}

	@Override
	public List<TsysSettings> selectListByMap(Map<String, Object> map) {
		return tsysSettingsMapper.selectListByMap(map);
	}
}
