package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityTaskFileService;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskFile;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskFileMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
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
public class TevglActivityTaskFileServiceImpl implements TevglActivityTaskFileService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityTaskFileServiceImpl.class);
	@Autowired
	private TevglActivityTaskFileMapper tevglActivityTaskFileMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityTaskFile> tevglActivityTaskFileList = tevglActivityTaskFileMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskFileList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityTaskFileList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>>)
	 * @param Map
	 * @return R
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityTaskFileList = tevglActivityTaskFileMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskFileList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityTaskFile
	 * @throws BudaosException
	 */
	public R save(TevglActivityTaskFile tevglActivityTaskFile) throws BudaosException {
		tevglActivityTaskFile.setFileId(Identities.uuid());
		tevglActivityTaskFile.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityTaskFile.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityTaskFile);
		tevglActivityTaskFileMapper.insert(tevglActivityTaskFile);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityTaskFile
	 * @throws BudaosException
	 */
	public R update(TevglActivityTaskFile tevglActivityTaskFile) throws BudaosException {
	    tevglActivityTaskFile.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityTaskFile.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityTaskFile);
		tevglActivityTaskFileMapper.update(tevglActivityTaskFile);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityTaskFileMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityTaskFileMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityTaskFileMapper.selectObjectById(id));
	}
}
