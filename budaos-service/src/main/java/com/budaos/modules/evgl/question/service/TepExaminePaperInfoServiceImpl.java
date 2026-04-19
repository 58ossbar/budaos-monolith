package com.budaos.modules.evgl.question.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.api.TepExaminePaperInfoService;
import com.budaos.modules.evgl.question.domain.TepExaminePaperInfo;
import com.budaos.modules.evgl.question.persistence.TepExaminePaperInfoMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TepExaminePaperInfoServiceImpl implements TepExaminePaperInfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TepExaminePaperInfoServiceImpl.class);
	@Autowired
	private TepExaminePaperInfoMapper tepExaminePaperInfoMapper;
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
		List<TepExaminePaperInfo> tepExaminePaperInfoList = tepExaminePaperInfoMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tepExaminePaperInfoList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tepExaminePaperInfoList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tepExaminePaperInfoList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tepExaminePaperInfoList = tepExaminePaperInfoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tepExaminePaperInfoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tepExaminePaperInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tepExaminePaperInfo
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TepExaminePaperInfo tepExaminePaperInfo) throws BudaosException {
		tepExaminePaperInfo.setPaperId(Identities.uuid());
		if (StrUtils.isEmpty(tepExaminePaperInfo.getCreateUserId())) {
			tepExaminePaperInfo.setCreateUserId(serviceLoginUtil.getLoginUserId());
		}
		tepExaminePaperInfo.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tepExaminePaperInfo);
		tepExaminePaperInfoMapper.insert(tepExaminePaperInfo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tepExaminePaperInfo
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TepExaminePaperInfo tepExaminePaperInfo) throws BudaosException {
	    tepExaminePaperInfo.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tepExaminePaperInfo.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tepExaminePaperInfo);
		tepExaminePaperInfoMapper.update(tepExaminePaperInfo);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tepExaminePaperInfoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tepExaminePaperInfoMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tepExaminePaperInfoMapper.selectObjectById(id));
	}

	/**
	 * <p>试卷列表</p>  
	 * @author huj
	 * @data 2019年12月12日	
	 * @param params
	 * @return
	 */
	@Override
	@SysLog(value="根据条件查询试卷列表")
	public R queryPapers( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tepExaminePaperInfoList = tepExaminePaperInfoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tepExaminePaperInfoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tepExaminePaperInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * <p>明细</p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param paperId 试卷ID
	 * @return
	 */
	@Override
	public TepExaminePaperInfo selectObjectById(String paperId) {
		return tepExaminePaperInfoMapper.selectObjectById(paperId);
	}

	@Override
	public void plusNum(TepExaminePaperInfo tepExaminePaperInfo) {
		tepExaminePaperInfoMapper.plusNum(tepExaminePaperInfo);
	}

	@Override
	public List<TepExaminePaperInfo> selectListByMap(Map<String, Object> map) {
		return tepExaminePaperInfoMapper.selectListByMap(map);
	}

	/**
	 * <p>根据条件获取日期</p>  
	 * @author huj
	 * @data 2020年1月11日	
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDates(Map<String, Object> map) {
		return tepExaminePaperInfoMapper.getDates(map);
	}
}
