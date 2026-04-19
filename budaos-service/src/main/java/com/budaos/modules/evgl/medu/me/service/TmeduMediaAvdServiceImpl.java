package com.budaos.modules.evgl.medu.me.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.medu.me.api.TmeduMediaAvdService;
import com.budaos.modules.evgl.medu.me.domain.TmeduMediaAvd;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMediaAvdMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
public class TmeduMediaAvdServiceImpl implements TmeduMediaAvdService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TmeduMediaAvdServiceImpl.class);
	@Autowired
	private TmeduMediaAvdMapper tmeduMediaAvdMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	
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
		List<TmeduMediaAvd> tmeduMediaAvdList = tmeduMediaAvdMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tmeduMediaAvdList, "createUserId", "updateUserId");
		convertUtil.convertDict(tmeduMediaAvdList, "avdState", "avdState");
		// 图片处理
		tmeduMediaAvdList.forEach(a -> {
			a.setAvdPicurl(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("15") + "/" + a.getAvdPicurl());
		});
		PageUtils pageUtil = new PageUtils(tmeduMediaAvdList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tmeduMediaAvdList = tmeduMediaAvdMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tmeduMediaAvdList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tmeduMediaAvdList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tmeduMediaAvd
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TmeduMediaAvd tmeduMediaAvd) throws BudaosException {
		tmeduMediaAvd.setAvdId(Identities.uuid());
		tmeduMediaAvd.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tmeduMediaAvd.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tmeduMediaAvd);
		tmeduMediaAvdMapper.insert(tmeduMediaAvd);
		return R.ok();
	}
	/***
	 * 新增
	 * @param tmeduMediaAvd
	 * @param attachId
	 */
	@SysLog(value="新增")
	public R saveHasAttach( TmeduMediaAvd tmeduMediaAvd, String attachId) throws BudaosException {
		String id = Identities.uuid();
		tmeduMediaAvd.setAvdId(id);
		tmeduMediaAvd.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tmeduMediaAvd.setCreateTime(DateUtils.getNowTimeStamp());
		tmeduMediaAvd.setScene(StrUtils.isEmpty(tmeduMediaAvd.getScene()) ? "1" : tmeduMediaAvd.getScene());
		ValidatorUtils.check(tmeduMediaAvd);
		tmeduMediaAvdMapper.insert(tmeduMediaAvd);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, id, "1", "15");
		}
		return R.ok();
	}
	/**
	 * 修改
	 * @param tmeduMediaAvd
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TmeduMediaAvd tmeduMediaAvd) throws BudaosException {
	    tmeduMediaAvd.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tmeduMediaAvd.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tmeduMediaAvd);
		tmeduMediaAvdMapper.update(tmeduMediaAvd);
		return R.ok();
	}
	
	/***
	 * 修改
	 * @param tmeduMediaAvd
	 * @param attachId
	 */
	@SysLog(value="修改")
	public R updateHasAttach( TmeduMediaAvd tmeduMediaAvd, String attachId) throws BudaosException {
	    tmeduMediaAvd.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tmeduMediaAvd.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tmeduMediaAvd);
		tmeduMediaAvdMapper.update(tmeduMediaAvd);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, tmeduMediaAvd.getAvdId(), "0", "15");
		}
		return R.ok();
	}
	
	@SysLog(value="更新状态")
	public R updateState(TmeduMediaAvd tmeduMediaAvd){
	    if((tmeduMediaAvd == null) || ((tmeduMediaAvd != null) && ((tmeduMediaAvd.getAvdId() == null) || ("".equals(tmeduMediaAvd.getAvdId())) ) ) ){
	    	return R.error("操作失败");
	    }
		tmeduMediaAvdMapper.update(tmeduMediaAvd);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tmeduMediaAvdMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tmeduMediaAvdMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		TmeduMediaAvd tmeduMediaAvd = tmeduMediaAvdMapper.selectObjectById(id);
		String avdPicurl = "";
		if(tmeduMediaAvd != null){
			avdPicurl = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("15") + "/" + tmeduMediaAvd.getAvdPicurl();
		}
		return R.ok().put(Constant.R_DATA,tmeduMediaAvd).put("avdPicurl",avdPicurl);
	}
	/**
	 * 查询首页轮播图列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R queryShowIndex( Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>(); // 查询条件
		map.clear();
		map.put("sidx", "avd_num");
		map.put("order", "asc");
		map.put("avdState", "Y"); // 状态(Y已上线N已下线)
		map.put("avdBegintime", DateUtils.getNowTimeStamp());
		map.put("avdEndtime", DateUtils.getNowTimeStamp());
		map.put("scene", params.get("scene"));
		List<TmeduMediaAvd> tmeduMediaAvdList = tmeduMediaAvdMapper.selectListByMap(map);
		String nowDateTime = DateUtils.getNowTimeStamp();
		// 过滤，只取有效时间内的广告图
		tmeduMediaAvdList = tmeduMediaAvdList.parallelStream().filter(a -> (nowDateTime.compareTo(((TmeduMediaAvd) a).getAvdBegintime()) >= 0
				&& nowDateTime.compareTo(((TmeduMediaAvd) a).getAvdEndtime()) <= 0)).collect(Collectors.toList());
		
		convertUtil.convertUserId2RealName(tmeduMediaAvdList, "createUserId", "updateUserId");
//		convertUtil.convertDict(tmeduMediaAvdList, "avdState", "avdState");
		// 图片处理
		tmeduMediaAvdList.forEach(a -> {
			a.setAvdPicurl(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("15") + "/" + a.getAvdPicurl());
		});
		return R.ok().put(Constant.R_DATA, tmeduMediaAvdList);
	}
}
