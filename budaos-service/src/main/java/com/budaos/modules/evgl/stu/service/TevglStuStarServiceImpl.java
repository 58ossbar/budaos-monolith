package com.budaos.modules.evgl.stu.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.stu.api.TevglStuStarService;
import com.budaos.modules.evgl.stu.domain.TevglStuStar;
import com.budaos.modules.evgl.stu.persistence.TevglStuStarMapper;
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

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 【实训故事】接口实现类</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglStuStarServiceImpl implements TevglStuStarService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglStuStarServiceImpl.class);
	@Autowired
	private TevglStuStarMapper tevglStuStarMapper;
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
		List<TevglStuStar> tevglStuStarList = tevglStuStarMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglStuStarList, "createUserId", "updateUserId");
		convertUtil.convertOrgId(tevglStuStarList, "orgId"); // 所属院校
		convertUtil.convertDict(tevglStuStarList, "state", "state1"); // 是否首页显示
		tevglStuStarList.forEach(a -> {
			a.setStarPic(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("8") + "/" + a.getStarPic());
		});
		PageUtils pageUtil = new PageUtils(tevglStuStarList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglStuStarList = tevglStuStarMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglStuStarList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglStuStarList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglStuStar
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglStuStar tevglStuStar) throws BudaosException {
		tevglStuStar.setStarId(Identities.uuid());
		tevglStuStar.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglStuStar.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglStuStar);
		tevglStuStarMapper.insert(tevglStuStar);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglStuStar
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglStuStar tevglStuStar) throws BudaosException {
	    tevglStuStar.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglStuStar.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglStuStar);
		tevglStuStarMapper.update(tevglStuStar);
		return R.ok();
	}
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年7月26日
	 * @param tevglStuStar
	 * @param attachId
	 * @return
	 */
	@Override
	public R save2(TevglStuStar tevglStuStar, String attachId) {
		String id = Identities.uuid();
		tevglStuStar.setStarId(id);
		tevglStuStar.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglStuStar.setCreateTime(DateUtils.getNowTimeStamp());
		tevglStuStar.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglStuStar);
		tevglStuStarMapper.insert(tevglStuStar);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, id, "1", "8");
		}
		return R.ok();
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年7月26日
	 * @param tevglStuStar
	 * @param attachId
	 * @return
	 */
	@Override
	@SysLog(value="修改")
	public R update2(TevglStuStar tevglStuStar, String attachId) {
		tevglStuStar.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglStuStar.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglStuStar);
		tevglStuStarMapper.update(tevglStuStar);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglStuStar.getStarId(), "0", "8");
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
		tevglStuStarMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglStuStarMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		TevglStuStar a = tevglStuStarMapper.selectObjectById(id);
		if (a == null) {
			return R.ok().put(Constant.R_DATA, new TevglStuStar());
		}
		a.setStarPic(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("8") + "/" + a.getStarPic());
		return R.ok().put(Constant.R_DATA, a);
	}

	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglStuStar
	 * @return
	 */
	@Override
	public R updateState(TevglStuStar tevglStuStar) {
		if (tevglStuStar == null) {
			return R.error("操作失败");
		}
		if (tevglStuStar.getStarId() == null || "".equals(tevglStuStar.getStarId())) {
			return R.error("操作失败");
		}
		tevglStuStarMapper.update(tevglStuStar);
		return R.ok();
	}

}
