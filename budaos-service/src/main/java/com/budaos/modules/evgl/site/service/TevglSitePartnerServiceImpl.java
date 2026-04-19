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
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.site.api.TevglSitePartnerService;
import com.budaos.modules.evgl.site.domain.TevglSitePartner;
import com.budaos.modules.evgl.site.persistence.TevglSitePartnerMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 【合作伙伴】接口实现类</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglSitePartnerServiceImpl implements TevglSitePartnerService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSitePartnerServiceImpl.class);
	@Autowired
	private TevglSitePartnerMapper tevglSitePartnerMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TsysAttachService tsysAttachService;	
	
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	
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
		List<TevglSitePartner> tevglSitePartnerList = tevglSitePartnerMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglSitePartnerList, "createUserId", "updateUserId");
		convertUtil.convertDict(tevglSitePartnerList, "isKeyPoint", "state1"); // 是否首页显示(重点企业)
		convertUtil.convertDict(tevglSitePartnerList, "state", "state4"); // 状态(Y有效N无效)
		tevglSitePartnerList.forEach(a -> {
			a.setCompanyLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("9") + "/" + a.getCompanyLogo());
		});
		PageUtils pageUtil = new PageUtils(tevglSitePartnerList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSitePartnerList = tevglSitePartnerMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSitePartnerList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSitePartnerList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSitePartner
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglSitePartner tevglSitePartner) throws BudaosException {
		tevglSitePartner.setCompanyId(Identities.uuid());
		tevglSitePartner.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSitePartner.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSitePartner);
		tevglSitePartnerMapper.insert(tevglSitePartner);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSitePartner
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSitePartner tevglSitePartner) throws BudaosException {
	    tevglSitePartner.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSitePartner.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSitePartner);
		tevglSitePartnerMapper.update(tevglSitePartner);
		return R.ok();
	}
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年7月26日
	 * @param tevglSitePartner
	 * @param attachId
	 * @return
	 */
	@SysLog(value="新增")
	@Override
	public R save2(TevglSitePartner tevglSitePartner, String attachId) {
		String id = Identities.uuid();
		tevglSitePartner.setCompanyId(id);
		tevglSitePartner.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSitePartner.setCreateTime(DateUtils.getNowTimeStamp());
		tevglSitePartner.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSitePartner);
		tevglSitePartnerMapper.insert(tevglSitePartner);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, id, "1", "9");
		}
		return R.ok();
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年7月26日
	 * @param tevglSitePartner
	 * @param attachId
	 * @return
	 */
	@Override
	public R update2(TevglSitePartner tevglSitePartner, String attachId) {
		tevglSitePartner.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSitePartner.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSitePartner);
		tevglSitePartnerMapper.update(tevglSitePartner);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglSitePartner.getCompanyId(), "0", "9");
		}
		return R.ok();
	}
	
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSitePartnerMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglSitePartnerMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		TevglSitePartner tevglSitePartner = tevglSitePartnerMapper.selectObjectById(id);
		if (tevglSitePartner == null) {
			return R.ok().put(Constant.R_DATA, new TevglSitePartner());
		}
		tevglSitePartner.setCompanyLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("9") + "/" + tevglSitePartner.getCompanyLogo());
		return R.ok().put(Constant.R_DATA, tevglSitePartner);
	}

	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglSitePartner
	 * @return
	 */
	@Override
	public R updateState(TevglSitePartner tevglSitePartner) {
		if (tevglSitePartner == null) {
			return R.error("操作失败");
		}
		if (tevglSitePartner.getCompanyId() == null || "".equals(tevglSitePartner.getCompanyId())) {
			return R.error("操作失败");
		}
		tevglSitePartnerMapper.update(tevglSitePartner);
		return R.ok();
	}

}
