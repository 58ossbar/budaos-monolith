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
import com.budaos.modules.evgl.site.api.TevglSiteAvdService;
import com.budaos.modules.evgl.site.domain.TevglSiteAvd;
import com.budaos.modules.evgl.site.persistence.TevglSiteAvdMapper;
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
 * <p> Title: 【广告轮播图】接口实现类</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglSiteAvdServiceImpl implements TevglSiteAvdService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteAvdServiceImpl.class);
	@Autowired
	private TevglSiteAvdMapper tevglSiteAvdMapper;
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
		List<TevglSiteAvd> tevglSiteAvdList = tevglSiteAvdMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteAvdList, "createUserId", "updateUserId");
		convertUtil.convertDict(tevglSiteAvdList, "avdState", "avdState");
		// 图片处理
		tevglSiteAvdList.forEach(a -> {
			a.setAvdPicurlPc(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("5") + "/" + a.getAvdPicurlPc());
			a.setAvdPicurlMobile(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("5") + "/" + a.getAvdPicurlMobile());
		});
		PageUtils pageUtil = new PageUtils(tevglSiteAvdList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteAvdList = tevglSiteAvdMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteAvdList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteAvdList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteAvd
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglSiteAvd tevglSiteAvd) throws BudaosException {
		tevglSiteAvd.setAvdId(Identities.uuid());
		tevglSiteAvd.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteAvd.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteAvd);
		tevglSiteAvdMapper.insert(tevglSiteAvd);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteAvd
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSiteAvd tevglSiteAvd) throws BudaosException {
	    tevglSiteAvd.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSiteAvd.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSiteAvd);
		tevglSiteAvdMapper.update(tevglSiteAvd);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglSiteAvdMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglSiteAvdMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		TevglSiteAvd tevglSiteAvd = tevglSiteAvdMapper.selectObjectById(id);
		String avdPicurlPc = "", avdPicurlMobile = "";
		if (tevglSiteAvd != null) {
			avdPicurlPc = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("5") + "/" + tevglSiteAvd.getAvdPicurlPc();
			avdPicurlMobile = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("5") + "/" + tevglSiteAvd.getAvdPicurlMobile();
		}
		return R.ok().put(Constant.R_DATA, tevglSiteAvd)
				.put("avdPicurlPc", avdPicurlPc)
				.put("avdPicurlMobile", avdPicurlMobile);
	}

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param tevglSiteAvd
	 * @param attachId
	 * @return
	 */
	@Override
	@SysLog(value="新增")
	public R save2(TevglSiteAvd tevglSiteAvd, String attachId, String attachIdForMobile) {
		String id = Identities.uuid();
		tevglSiteAvd.setAvdId(id);
		tevglSiteAvd.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteAvd.setCreateTime(DateUtils.getNowTimeStamp());
		tevglSiteAvd.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteAvd);
		tevglSiteAvdMapper.insert(tevglSiteAvd);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, id, "1", "5");
		}
		if (attachIdForMobile != null && !"".equals(attachIdForMobile)) {
			tsysAttachService.updateAttach(attachIdForMobile, id, "1", "5");
		}
		return R.ok();
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param tevglSiteAvd
	 * @param attachId
	 * @return
	 */
	@Override
	@SysLog(value="修改")
	public R update2(TevglSiteAvd tevglSiteAvd, String attachId, String attachIdForMobile) {
		tevglSiteAvd.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSiteAvd.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSiteAvd);
		tevglSiteAvdMapper.update(tevglSiteAvd);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglSiteAvd.getAvdId(), "0", "5");
		}
		if (attachIdForMobile != null && !"".equals(attachIdForMobile)) {
			tsysAttachService.updateAttach(attachIdForMobile, tevglSiteAvd.getAvdId(), "0", "5");
		}
		return R.ok();
	}

	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月29日
	 * @param tevglSiteAvd
	 * @return
	 */
	@Override
	@SysLog(value="更新状态")
	public R updateState(TevglSiteAvd tevglSiteAvd) {
		if (tevglSiteAvd == null) {
			return R.error("操作失败");
		}
		if (tevglSiteAvd.getAvdId() == null || "".equals(tevglSiteAvd.getAvdId())) {
			return R.error("操作失败");
		}
		tevglSiteAvdMapper.update(tevglSiteAvd);
		return R.ok();
	}
}
