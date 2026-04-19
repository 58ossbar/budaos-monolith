package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityTaskTraineeAnswerFileService;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskTraineeAnswerFile;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTaskTraineeAnswerFileMapper;
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
public class TevglActivityTaskTraineeAnswerFileServiceImpl implements TevglActivityTaskTraineeAnswerFileService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityTaskTraineeAnswerFileServiceImpl.class);
	@Autowired
	private TevglActivityTaskTraineeAnswerFileMapper tevglActivityTaskTraineeAnswerFileMapper;
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
		List<TevglActivityTaskTraineeAnswerFile> tevglActivityTaskTraineeAnswerFileList = tevglActivityTaskTraineeAnswerFileMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskTraineeAnswerFileList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglActivityTaskTraineeAnswerFileList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskTraineeAnswerFileList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglActivityTaskTraineeAnswerFileList = tevglActivityTaskTraineeAnswerFileMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityTaskTraineeAnswerFileList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityTaskTraineeAnswerFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityTaskTraineeAnswerFile
	 * @throws BudaosException
	 */
	public R save(TevglActivityTaskTraineeAnswerFile tevglActivityTaskTraineeAnswerFile) throws BudaosException {
		tevglActivityTaskTraineeAnswerFile.setFileId(Identities.uuid());
		tevglActivityTaskTraineeAnswerFile.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglActivityTaskTraineeAnswerFile.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityTaskTraineeAnswerFile);
		tevglActivityTaskTraineeAnswerFileMapper.insert(tevglActivityTaskTraineeAnswerFile);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityTaskTraineeAnswerFile
	 * @throws BudaosException
	 */
	public R update(TevglActivityTaskTraineeAnswerFile tevglActivityTaskTraineeAnswerFile) throws BudaosException {
	    tevglActivityTaskTraineeAnswerFile.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglActivityTaskTraineeAnswerFile.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglActivityTaskTraineeAnswerFile);
		tevglActivityTaskTraineeAnswerFileMapper.update(tevglActivityTaskTraineeAnswerFile);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityTaskTraineeAnswerFileMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityTaskTraineeAnswerFileMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityTaskTraineeAnswerFileMapper.selectObjectById(id));
	}

	/**
	 * 批量新增
	 *
	 * @param list
	 */
	@Override
	public void insertBatch(List<TevglActivityTaskTraineeAnswerFile> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		tevglActivityTaskTraineeAnswerFileMapper.insertBatch(list);
	}
}
