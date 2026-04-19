package com.budaos.modules.evgl.activity.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.TevglActivityVoteQuestionnaireTraineeAnswerFileService;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireTraineeAnswerFile;
import com.budaos.modules.evgl.activity.persistence.TevglActivityVoteQuestionnaireTraineeAnswerFileMapper;
import com.budaos.utils.constants.Constant;
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
public class TevglActivityVoteQuestionnaireTraineeAnswerFileServiceImpl implements TevglActivityVoteQuestionnaireTraineeAnswerFileService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglActivityVoteQuestionnaireTraineeAnswerFileServiceImpl.class);
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerFileMapper tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglActivityVoteQuestionnaireTraineeAnswerFile> tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileList = tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileList = tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile
	 * @throws BudaosException
	 */
	public R save(TevglActivityVoteQuestionnaireTraineeAnswerFile tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile) throws BudaosException {
		tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile.setFileId(Identities.uuid());
		ValidatorUtils.check(tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile);
		tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper.insert(tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile
	 * @throws BudaosException
	 */
	public R update(TevglActivityVoteQuestionnaireTraineeAnswerFile tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile) throws BudaosException {
	    ValidatorUtils.check(tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile);
		tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper.update(tevglActivityVoteQuestionnaireTraineeAnswerQuestionFile);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	public R delete(String id) throws BudaosException {
		tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglActivityVoteQuestionnaireTraineeAnswerQuestionFileMapper.selectObjectById(id));
	}
}
