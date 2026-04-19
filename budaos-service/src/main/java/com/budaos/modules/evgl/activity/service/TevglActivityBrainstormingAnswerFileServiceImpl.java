package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityBrainstormingAnswerFileService;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstormingAnswerFile;
import com.budaos.modules.evgl.activity.persistence.TevglActivityBrainstormingAnswerFileMapper;
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
public class TevglActivityBrainstormingAnswerFileServiceImpl implements TevglActivityBrainstormingAnswerFileService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityBrainstormingAnswerFileServiceImpl.class);
	@Autowired
	private TevglActivityBrainstormingAnswerFileMapper tevglActivityBrainstormingAnswerFileMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;

	/**
	 * 查询列表
	 * @param params
	 * @return
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityBrainstormingAnswerFile> tevglActivityBrainstormingAnswerFileList = tevglActivityBrainstormingAnswerFileMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityBrainstormingAnswerFileList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglActivityBrainstormingAnswerFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglActivityBrainstormingAnswerFileList = tevglActivityBrainstormingAnswerFileMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglActivityBrainstormingAnswerFileList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglActivityBrainstormingAnswerFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 	新增
	 * @param tevglActivityBrainstormingAnswerFile
	 * @return
	 * @throws BudaosException
	 */
	public R save(TevglActivityBrainstormingAnswerFile tevglActivityBrainstormingAnswerFile) throws BudaosException {
		tevglActivityBrainstormingAnswerFile.setFiId(Identities.uuid());
		if (StrUtils.isEmpty(tevglActivityBrainstormingAnswerFile.getCreateUserId())) {
			tevglActivityBrainstormingAnswerFile.setCreateUserId(serviceLoginUtil.getLoginUserId());
		}
		tevglActivityBrainstormingAnswerFile.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglActivityBrainstormingAnswerFile);
		tevglActivityBrainstormingAnswerFileMapper.insert(tevglActivityBrainstormingAnswerFile);
		return R.ok();
	}

	/**
	 * 	修改
	 * @param tevglActivityBrainstormingAnswerFile
	 * @return
	 * @throws BudaosException
	 */
	public R update(TevglActivityBrainstormingAnswerFile tevglActivityBrainstormingAnswerFile) throws BudaosException {
	    ValidatorUtils.check(tevglActivityBrainstormingAnswerFile);
		tevglActivityBrainstormingAnswerFileMapper.update(tevglActivityBrainstormingAnswerFile);
		return R.ok();
	}

	/**
	 * 	删除
	 * @param id
	 * @return
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityBrainstormingAnswerFileMapper.delete(id);
		return R.ok();
	}

	/**
	 * 	批量删除
	 * @param ids
	 * @return
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityBrainstormingAnswerFileMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 	查看
	 * @param id
	 * @return
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityBrainstormingAnswerFileMapper.selectObjectById(id));
	}
}
