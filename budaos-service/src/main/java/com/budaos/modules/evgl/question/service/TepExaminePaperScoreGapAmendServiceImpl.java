package com.budaos.modules.evgl.question.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.api.TepExaminePaperScoreGapAmendService;
import com.budaos.modules.evgl.question.domain.*;
import com.budaos.modules.evgl.question.dto.SaveScoreGapAmendDTO;
import com.budaos.modules.evgl.question.persistence.*;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.BeanUtils;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
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
public class TepExaminePaperScoreGapAmendServiceImpl implements TepExaminePaperScoreGapAmendService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TepExaminePaperScoreGapAmendServiceImpl.class);
	@Autowired
	private TepExaminePaperScoreGapAmendMapper tepExaminePaperScoreGapAmendMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TepExaminePaperScoreMapper tepExaminePaperScoreMapper;
	@Autowired
	private TepExamineHistoryPaperMapper tepExamineHistoryPaperMapper;
	@Autowired
	private TevglQuestionsInfoMapper tevglQuestionsInfoMapper;
	@Autowired
	private TevglQuestionChoseMapper tevglQuestionChoseMapper;
	@Autowired
	private TepExaminePaperScoreGapfillingMapper tepExaminePaperScoreGapfillingMapper;
	
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
		List<TepExaminePaperScoreGapAmend> tepExaminePaperScoreGapAmendList = tepExaminePaperScoreGapAmendMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tepExaminePaperScoreGapAmendList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tepExaminePaperScoreGapAmendList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tepExaminePaperScoreGapAmendList = tepExaminePaperScoreGapAmendMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tepExaminePaperScoreGapAmendList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tepExaminePaperScoreGapAmendList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tepExaminePaperScoreGapAmend
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TepExaminePaperScoreGapAmend tepExaminePaperScoreGapAmend) throws BudaosException {
		tepExaminePaperScoreGapAmend.setAmId(Identities.uuid());
		tepExaminePaperScoreGapAmend.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tepExaminePaperScoreGapAmend.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tepExaminePaperScoreGapAmend);
		tepExaminePaperScoreGapAmendMapper.insert(tepExaminePaperScoreGapAmend);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tepExaminePaperScoreGapAmend
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TepExaminePaperScoreGapAmend tepExaminePaperScoreGapAmend) throws BudaosException {
	    ValidatorUtils.check(tepExaminePaperScoreGapAmend);
		tepExaminePaperScoreGapAmendMapper.update(tepExaminePaperScoreGapAmend);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tepExaminePaperScoreGapAmendMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tepExaminePaperScoreGapAmendMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tepExaminePaperScoreGapAmendMapper.selectObjectById(id));
	}

	@Override
	public R findScoreGapAmend(String scoreId, String traineeId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R saveScoreGapAmend(SaveScoreGapAmendDTO dto, String traineeId) {
		// 找到已评分的分值
		TepExaminePaperScore tepExaminePaperScore = tepExaminePaperScoreMapper.selectObjectById(dto.getScoreId());
		if (tepExaminePaperScore == null) {
			return R.error("参数异常");
		}
		if ("Y".equals(tepExaminePaperScore.getIsCorrect())) {
			return R.error("当前填空题回答已回答正确，不允许操作！");
		}
		TevglQuestionsInfo tevglQuestionsInfo = tevglQuestionsInfoMapper.selectObjectById(tepExaminePaperScore.getQuestionsId());
		if (tevglQuestionsInfo == null) {
			return R.error("题目已丢失！");
		}
		TepExaminePaperInfo tepExaminePaperInfo = tepExaminePaperScoreGapAmendMapper.findPaperByScoreId(dto.getScoreId());
		if (tepExaminePaperInfo == null) {
			return R.error("参数异常，试卷未找到！");
		}
		// 重新更改试卷总分
		TepExamineHistoryPaper tepExamineHistoryPaper = null;
		String historyId = tepExaminePaperScoreGapAmendMapper.findHistoryIdByScoreId(dto.getScoreId());
		if (StrUtils.isNotEmpty(historyId)) {
			tepExamineHistoryPaper = tepExamineHistoryPaperMapper.selectObjectById(historyId);
			if (tepExamineHistoryPaper == null) {
				return R.error("参数异常！");
			}
		} else {
			return R.error("参数异常！");
		}
		// 填充信息
		TepExaminePaperScoreGapAmend tepExaminePaperScoreGapAmend = new TepExaminePaperScoreGapAmend();
		BeanUtils.copyProperties(tepExaminePaperScoreGapAmend, dto);
		tepExaminePaperScoreGapAmend.setAmId(Identities.uuid());
		tepExaminePaperScoreGapAmend.setCreateUserId(traineeId);
		tepExaminePaperScoreGapAmend.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tepExaminePaperScoreGapAmend);
		// TODO 计算最新总分
		BigDecimal val = new BigDecimal("0");
		// 该题是否为复合题的子题目
		boolean flag = StrUtils.isNotEmpty(tevglQuestionsInfo.getParentId());
		if (flag) {
			if (new BigDecimal(dto.getScore()).compareTo(new BigDecimal(tepExaminePaperInfo.getCompositeScore())) > 1) {
				return R.error("给出的分数不能超过复合题限定的小题最高分！");
			}
			String latestScore = tepExaminePaperScoreGapAmendMapper.findLatestScoreByScoreId(dto.getScoreId());
			if (StrUtils.isEmpty(latestScore)) {
				val = new BigDecimal(tepExamineHistoryPaper.getPaperFinalScore()).add(new BigDecimal(tepExaminePaperInfo.getCompositeScore()));
			} else {
				val = new BigDecimal(tepExamineHistoryPaper.getPaperFinalScore()).subtract(new BigDecimal(latestScore)).add(new BigDecimal(tepExaminePaperInfo.getCompositeScore()));
			}
		} else {
			Map<String, Object> params = new HashMap<>();
			params.put("questionsIds", Arrays.asList(tepExaminePaperScore.getQuestionsId()));
			params.put("sidx", "sort_num");
			params.put("order", "asc");
			List<Map<String, Object>> optionList = tevglQuestionChoseMapper.selectSimpleListByMap(params);
			BigDecimal multiplyVal = new BigDecimal(tepExaminePaperInfo.getGapFilling()).multiply(new BigDecimal(optionList.size()));
			if (new BigDecimal(dto.getScore()).compareTo(multiplyVal) > 1) {
				return R.error("给出的分数不能超过最高" + multiplyVal + "分！");
			}
			// 取出本题填空的标准答案
			List<Object> modelAnswerList = optionList.stream().filter(a -> a.get("questionsId").equals(tepExaminePaperScore.getQuestionsId())).map(a -> a.get("content")).collect(Collectors.toList());
			// 1.回答错误 2.每空得分规则  同时满足两种情况才去计算
			if ("N".equals(tepExaminePaperScore.getIsCorrect()) && "1".equals(tepExaminePaperInfo.getGapFillingScoreStandard())) {
				int inCorrectNum = 0;
				params.clear();
				params.put("historyId", tepExamineHistoryPaper.getHistoryId());
				params.put("scoreId", dto.getScoreId());
				params.put("traineeId", tepExaminePaperScore.getTraineeId());
				params.put("sidx", "sort_num");
				params.put("order", "asc");
				List<Map<String, Object>> userAnswerList = tepExaminePaperScoreGapfillingMapper.selectSimpleListMapByMap(params);
				// TODO 算出该题本来得了多少分
				for (int i = 0; i < modelAnswerList.size(); i++) {
					if (modelAnswerList.get(i).equals(userAnswerList.get(i))) {
						inCorrectNum++;
					}
				}
				BigDecimal multiply = new BigDecimal(inCorrectNum).multiply(new BigDecimal(tepExaminePaperInfo.getGapFilling()));
				val = new BigDecimal(tepExamineHistoryPaper.getPaperFinalScore()).subtract(multiply).add(new BigDecimal(tepExaminePaperInfo.getCompositeScore()));
			}
		}
		tepExaminePaperScoreGapAmendMapper.insert(tepExaminePaperScoreGapAmend);
		TepExamineHistoryPaper t = new TepExamineHistoryPaper();
		t.setHistoryId(historyId);
		t.setPaperFinalScore(String.valueOf(val));
		tepExamineHistoryPaperMapper.update(t);
		return R.ok("操作成功 接口实现中！");
	}

}
