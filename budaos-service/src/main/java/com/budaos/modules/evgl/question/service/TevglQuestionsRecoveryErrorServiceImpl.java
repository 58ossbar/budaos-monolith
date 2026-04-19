package com.budaos.modules.evgl.question.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.api.TevglQuestionsRecoveryErrorService;
import com.budaos.modules.evgl.question.domain.TevglQuestionChose;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;
import com.budaos.modules.evgl.question.domain.TevglQuestionsRecoveryError;
import com.budaos.modules.evgl.question.persistence.TevglQuestionChoseMapper;
import com.budaos.modules.evgl.question.persistence.TevglQuestionsInfoMapper;
import com.budaos.modules.evgl.question.persistence.TevglQuestionsRecoveryErrorMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class TevglQuestionsRecoveryErrorServiceImpl implements TevglQuestionsRecoveryErrorService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglQuestionsRecoveryErrorServiceImpl.class);
	@Autowired
	private TevglQuestionsRecoveryErrorMapper tevglQuestionsRecoveryErrorMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglQuestionsInfoMapper tevglQuestionsInfoMapper;
	@Autowired
	private TevglQuestionChoseMapper tevglQuestionChoseMapper;
	
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
		List<TevglQuestionsRecoveryError> tevglQuestionsRecoveryErrorList = tevglQuestionsRecoveryErrorMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglQuestionsRecoveryErrorList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglQuestionsRecoveryErrorList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglQuestionsRecoveryErrorList = tevglQuestionsRecoveryErrorMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglQuestionsRecoveryErrorList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglQuestionsRecoveryErrorList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglQuestionsRecoveryError
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	@Override
	public R save(TevglQuestionsRecoveryError tevglQuestionsRecoveryError) throws BudaosException {
		if (tevglQuestionsRecoveryError.getContent() == null) {
			return R.error("纠错的理由不能为空");
		}
		tevglQuestionsRecoveryError.setId(Identities.uuid());
		tevglQuestionsRecoveryError.setCreateTime(DateUtils.getNowTimeStamp());
		tevglQuestionsRecoveryError.setQuestionId(tevglQuestionsRecoveryError.getQuestionId());  //纠错的题目id
		tevglQuestionsRecoveryError.setContent(tevglQuestionsRecoveryError.getContent());  //纠错的理由
		tevglQuestionsRecoveryError.setState("N"); //默认未处理
		ValidatorUtils.check(tevglQuestionsRecoveryError);
		tevglQuestionsRecoveryErrorMapper.insert(tevglQuestionsRecoveryError);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglQuestionsRecoveryError
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglQuestionsRecoveryError tevglQuestionsRecoveryError) throws BudaosException {
	    ValidatorUtils.check(tevglQuestionsRecoveryError);
		tevglQuestionsRecoveryErrorMapper.update(tevglQuestionsRecoveryError);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglQuestionsRecoveryErrorMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglQuestionsRecoveryErrorMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglQuestionsRecoveryErrorMapper.selectObjectById(id));
	}
	
	/**
	 * 纠错题目
	 * @data 2020年11月20日
	 * @author zyl改
	 */
	@SysLog(value="纠错题目")
	@Override
	public R selectByCollectionMap(String questionsId) {
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsId为空");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("questionsId", questionsId);
		System.out.println("questionsId:" + questionsId);
		//根据前端传过来的题目id查询纠错信息
		TevglQuestionsInfo questionsInfo = tevglQuestionsInfoMapper.selectObjectById(questionsId);
		System.out.println("题目信息:" + questionsInfo);
		if (questionsInfo == null) {
			return R.error("该题目已失效，请刷新重试");
		}
		params.put("sidx", "code");
		params.put("order", "desc");
		//根据题目id查询选项集合
		List<TevglQuestionChose> optionList = tevglQuestionChoseMapper.selectListByMap(params);
		//保存到题目列表
		questionsInfo.setOptionList(optionList);
		return R.ok().put(Constant.R_DATA, questionsInfo);
	}
}
