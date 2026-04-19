package com.budaos.modules.evgl.question.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalFavority;
import com.budaos.modules.common.RoleUtils;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeFavority;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeFavorityMapper;
import com.budaos.modules.evgl.question.api.TevglQuestionsInfoService;
import com.budaos.modules.evgl.question.domain.TepExaminePaperQuestionsDetail;
import com.budaos.modules.evgl.question.domain.TevglQuestionChose;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;
import com.budaos.modules.evgl.question.persistence.TepExaminePaperQuestionsDetailMapper;
import com.budaos.modules.evgl.question.persistence.TevglQuestionChoseMapper;
import com.budaos.modules.evgl.question.persistence.TevglQuestionsInfoMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Title: 题目基本信息
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglQuestionsInfoServiceImpl implements TevglQuestionsInfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglQuestionsInfoServiceImpl.class);
	@Autowired
	private TevglQuestionsInfoMapper tevglQuestionsInfoMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglQuestionChoseMapper tevglQuestionChoseMapper;
	@Autowired
	private TepExaminePaperQuestionsDetailMapper tepExaminePaperQuestionsDetailMapper;
	@Autowired
	private TmeduMeFavorityMapper tmeduMeFavorityMapper;
	@Autowired
	private RoleUtils roleUtils;

	/**
	 * 查询列表(返回List<Bean>)
	 * 
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TevglQuestionsInfo> tevglQuestionsInfoList = tevglQuestionsInfoMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglQuestionsInfoList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglQuestionsInfoList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglQuestionsInfoList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * 
	 * @param Map
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tevglQuestionsInfoList = tevglQuestionsInfoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglQuestionsInfoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglQuestionsInfoList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 新增
	 * 
	 * @param tevglQuestionsInfo
	 * @throws BudaosException
	 */
	@SysLog(value = "新增")
	public R save(TevglQuestionsInfo tevglQuestionsInfo) throws BudaosException {
		tevglQuestionsInfo.setQuestionsId(Identities.uuid());
		tevglQuestionsInfo.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglQuestionsInfo.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglQuestionsInfo);
		tevglQuestionsInfoMapper.insert(tevglQuestionsInfo);
		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param tevglQuestionsInfo
	 * @throws BudaosException
	 */
	@SysLog(value = "修改")
	public R update( TevglQuestionsInfo tevglQuestionsInfo) throws BudaosException {
		tevglQuestionsInfo.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		tevglQuestionsInfo.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglQuestionsInfo);
		tevglQuestionsInfoMapper.update(tevglQuestionsInfo);
		return R.ok();
	}

	/**
	 * 单条删除
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "单条删除")
	public R delete( String id) throws BudaosException {
		if (StrUtils.isEmpty(id) || "null".equals(id)) {
			return R.error("参数id为空");
		}
		TevglQuestionsInfo questionsInfo = tevglQuestionsInfoMapper.selectObjectById(id);
		if (questionsInfo == null) {
			return R.ok("删除成功");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("questionsId", id);
		List<TepExaminePaperQuestionsDetail> list = tepExaminePaperQuestionsDetailMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			return R.error("题目已被使用，无法删除该题目");
		}
		// 如果是删除复合题
		if ("6".equals(questionsInfo.getQuestionsType())) {
			return deleteCompositeQuestionInfo(id, null);
		}
		// 删除题目
		tevglQuestionsInfoMapper.delete(id);
		// 删除题目选项
		List<TevglQuestionChose> optionList = tevglQuestionChoseMapper.selectListByMap(map);
		List<String> optionIds = optionList.stream().map(a -> a.getOptionId()).collect(Collectors.toList());
		if (optionIds != null && optionIds.size() > 0) {
			String[] ids = optionIds.stream().toArray(String[]::new);
			tevglQuestionChoseMapper.deleteBatch(ids);
		}
		return R.ok("题目删除成功");
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value = "批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglQuestionsInfoMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglQuestionsInfoMapper.selectObjectById(id));
	}

	/**
	 * <p>
	 * 根据条件查询题目列表
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月9日
	 * @param params
	 * @apiNote 可传参majorId、subjectType、subjectTechnology、subjectId、chaptersId、questionsType、questionsComplexity、sortBy、pageNum、pageSize
	 */
	@Override
	@SysLog(value = "新增题目")
	public R queryQuestions(Map<String, Object> params, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 是否有添加题目的权限
		boolean hasPermissionQuestion = roleUtils.checkIsTeacher(loginUserId);
		// 1最近新增2组卷率高3正确率高4收藏率高
		String sortBy = StrUtils.isNull(params.get("sortBy")) ? "1" : params.get("sortBy").toString();
		String sidx = "create_time";
		switch (sortBy) {
		case "1":
			sidx = "create_time";
			break;
		case "2":
			sidx = "questions_constructing_num";
			break;
		case "3":
			sidx = "questions_accuracy";
			break;
		case "4":
			sidx = "questions_store_num";
			break;
		default:
			break;
		}
		// 只查询有效的题目
		params.put("questionsState", "Y");
		// 排序条件
		params.put("sidx", sidx);
		params.put("order", "desc");
		// 注意点，不查询子题目（需要查询子题目的话，注释即可）
		params.put("parentIdIsNull", "Y");
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> list = tevglQuestionsInfoMapper.selectSimpleListMap(query);
		if (list == null || list.size() == 0) {
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
			return R.ok().put(Constant.R_DATA, pageInfo).put("hasPermissionQuestion", hasPermissionQuestion);
		}
		// 字典转换
		convertUtil.convertDict(list, "questionsTypeName", "questions_type"); // 转换题目类型（选择、判断等）
		convertUtil.convertDict(list, "questionsComplexity", "questions_complexity"); // 转换 难易程度 （容易、普通、困难等）
		// 查询条件
		Map<String, Object> map = new HashMap<>();
		// 查询题目的选项
		List<Object> questionIds = list.stream().map(a -> a.get("questionsId")).collect(Collectors.toList());
		map.clear();
		map.put("state", "Y");
		map.put("questionIds", questionIds);
		List<Map<String, Object>> choseList = tevglQuestionChoseMapper.selectSimpleListByMap(map);
		// 查询题目的收藏数据
		map.clear();
		map.put("traineeId", loginUserId);
		map.put("favorityType", GlobalFavority.FAVORITY_6_QUESTION);
		List<TmeduMeFavority> favorityList = tmeduMeFavorityMapper.selectListByMap(map);
		// 当前用户正在考试的试卷中的题目，控制不允许查看正确答案和题目解析（测试活动+试卷库）
        List<String> usingQuestionIdList = tepExaminePaperQuestionsDetailMapper.findQuestionIdListByTraineeId(loginUserId);
		// 处理题目选项与题目是否收藏了
		list.stream().forEach(questionInfo -> {
			List<Map<String, Object>> optionList = choseList.stream()
					.filter(optionInfo -> optionInfo.get("questionsId").equals(questionInfo.get("questionsId")))
					.collect(Collectors.toList());
			questionInfo.put("optionList", optionList);
			boolean isCollected = favorityList.stream()
					.anyMatch(a -> a.getTargetId().equals(questionInfo.get("questionsId")));
			questionInfo.put("isCollected", isCollected);
			// 【TODO】【临时修复题目正确率的超过1问题，此问题可能是由其它地方，如测试活动，计算错误导致正确率超过了100%】
            fixQuestionsAccuracy(questionInfo);
            // 如果该题目参与了该学生的考试试卷
            if (usingQuestionIdList.contains(questionInfo.get("questionsId"))) {
                questionInfo.put("questionsParse", "由于你正在考试的试卷，使用了该题目，暂无法查看正确答案和题目解析");
                questionInfo.put("replyIds", "-1");
            }
		});
		// 处理复合题的子题目
		handleCompositeQuestionList6(list, map, usingQuestionIdList);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return R.ok().put(Constant.R_DATA, pageInfo).put("hasPermissionQuestion", hasPermissionQuestion);
	}
	
	/**
     * 临时修复题目正确率的超过100%问题，此问题可能是由其它地方，如测试活动，计算错误导致正确率超过了100%
     */
    private void fixQuestionsAccuracy(Map<String, Object> questionInfo){
        if (StrUtils.notNull(questionInfo.get("questionsAccuracy"))) {
            BigDecimal questionsAccuracy = new BigDecimal(questionInfo.get("questionsAccuracy").toString());
            if (questionsAccuracy.compareTo(new BigDecimal("1")) == 1) {
                Integer questionsCorrectNum = (Integer)questionInfo.get("questionsCorrectNum");
                Integer questionsAnswerNum = (Integer)questionInfo.get("questionsAnswerNum");
                BigDecimal val = new BigDecimal(questionsCorrectNum).divide(new BigDecimal(questionsAnswerNum), 4, BigDecimal.ROUND_HALF_UP);
                questionInfo.put("questionsAccuracy", val.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
            }
        }
    }

	/**
	 * 处理复合题的子题目
	 * 
	 * @param list
	 * @param map
	 */
	public void handleCompositeQuestionList6(List<Map<String, Object>> list, Map<String, Object> map, List<String> usingQuestionIdList) {
		// 取出复合题
		List<Map<String, Object>> compositeQuestionList = list.stream().filter(a -> "6".equals(a.get("questionsType")))
				.collect(Collectors.toList());
		if (compositeQuestionList != null && compositeQuestionList.size() > 0) {
			// 查子题目
			List<Object> parentIds = compositeQuestionList.stream().map(a -> a.get("questionsId"))
					.collect(Collectors.toList());
			map.clear();
			map.put("parentIds", parentIds);
			map.put("sidx", "sort_num");
			map.put("order", "asc");
			List<Map<String, Object>> childrenQuestionList = tevglQuestionsInfoMapper.selectSimpleListMap(map);
			// 字典转换
			convertUtil.convertDict(childrenQuestionList, "questionsTypeName", "questions_type"); // 转换题目类型（选择、判断等）
			convertUtil.convertDict(childrenQuestionList, "questionsComplexity", "questions_complexity"); // 转换 难易程度 （容易、普通、困难等）
			// 查子题目选项
			if (childrenQuestionList != null && childrenQuestionList.size() > 0) {
				List<Object> ids = childrenQuestionList.stream().map(a -> a.get("questionsId"))
						.collect(Collectors.toList());
				map.clear();
				map.put("state", "Y");
				map.put("questionIds", ids);
				List<Map<String, Object>> childrenQuestionOptionList = tevglQuestionChoseMapper
						.selectSimpleListByMap(map);
				// 选项归属题目
				childrenQuestionList.stream().forEach(questionInfo -> {
					List<Map<String, Object>> optionList = childrenQuestionOptionList.stream()
							.filter(optionInfo -> optionInfo.get("questionsId").equals(questionInfo.get("questionsId")))
							.collect(Collectors.toList());
					questionInfo.put("optionList", optionList);
				});
				// 子题目归属主题目
				list.stream().forEach(questionInfo -> {
					List<Map<String, Object>> children = childrenQuestionList.stream()
							.filter(childrenInfo -> !StrUtils.isNull(childrenInfo.get("parentId"))
									&& childrenInfo.get("parentId").equals(questionInfo.get("questionsId")))
							.collect(Collectors.toList());
					children.stream().forEach(item -> {
                        // 如果该题目参与了该学生的考试试卷
                        if (usingQuestionIdList.contains(item.get("questionsId"))) {
                            questionInfo.put("questionsParse", "由于你正在考试的试卷，使用了该题目，暂无法查看正确答案和题目解析");
                            questionInfo.put("replyIds", "-1");
                        }
                    });
					questionInfo.put("children", children);
				});
			}
		}
	}

	/**
	 * <p>
	 * 新增题目
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月9日
	 * @param tevglQuestionsInfo
	 * @param "json":"[{'code':'A', 'content':'封装', 'isRight':'YES'}, {'code':'B',
	 *                              'content':'多态', 'isRight':'YES'}, {'code':'C',
	 *                              'content':'多态', 'isRight':'YES'}, {'code':'D',
	 *                              'content':'接口', 'isRight':'NO'}]"
	 */
	@Override
	@SysLog(value = "新增题目")
	@Transactional
	public R saveQuestion(TevglQuestionsInfo tevglQuestionsInfo,
			TevglTraineeInfo traineeInfo) throws BudaosException {
		// 权限校验
		if (!roleUtils.checkIsTeacher(traineeInfo.getTraineeId())) {
			return R.error("暂无权限添加题目，请联系系统管理员");
		}
		if (!StrUtils.isEmpty(tevglQuestionsInfo.getQuestionsId())) {
			return R.error("非新增操作");
		}
		try {
			String json = tevglQuestionsInfo.getJson();
			if (StrUtils.isEmpty(json) && Arrays.asList("1", "2").contains(tevglQuestionsInfo.getQuestionsType())) {
				return R.error("选择题、或判断题的情况不能没有选项");
			}
			// 填充数据项
			String newQuestionsId = Identities.uuid();
			tevglQuestionsInfo.setQuestionsId(newQuestionsId); // 主键ID
			tevglQuestionsInfo.setQuestionsState("Y"); // 题目状态(Y启用N停用)
			tevglQuestionsInfo.setQuestionsStar("0"); // 题目星级
			tevglQuestionsInfo.setQuestionsConstructingNum(0); // 组卷次数
			tevglQuestionsInfo.setQuestionsAccuracy(new BigDecimal(0)); // 正确率
			tevglQuestionsInfo.setQuestionsStoreNum(0); // 收藏数
			tevglQuestionsInfo.setQuestionsAnswerNum(0); // 作答数
			tevglQuestionsInfo.setQuestionsCorrectNum(0); // 正确数
			tevglQuestionsInfo.setQuestionsErrorNum(0); // 错误数
			tevglQuestionsInfo.setReplyIds(null);
			// 合法性校验
			ValidatorUtils.check(tevglQuestionsInfo);
			JSONArray jsonArray = JSON.parseArray(json);
			// 保存题目基本信息和题目选项数据
			return doSaveQuestionAndOptionList(newQuestionsId, jsonArray, tevglQuestionsInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("新增题目失败", e);
			return R.error("系统错误，题目保存失败");
		}
	}

	/**
	 * 新增时保存此题目信息和选项
	 * @param questionId
	 * @param jsonArray
	 * @param tevglQuestionsInfo
	 * @return
	 */
	private R doSaveQuestionAndOptionList(String questionId, JSONArray jsonArray,
			TevglQuestionsInfo tevglQuestionsInfo) {
		if (StrUtils.isEmpty(questionId)) {
			return R.error("必传参数为空");
		}
		if (Arrays.asList("1", "2", "3").contains(tevglQuestionsInfo.getQuestionsType())) {
			if (jsonArray == null || jsonArray.size() == 0) {
				return R.error("必传参数为空");
			}
			if (!hasModelAnswer(jsonArray)) {
				return R.error("录入单选题、选择题、判断题时，请设置正确答案");
			}
		}
		if ("5".equals(tevglQuestionsInfo.getQuestionsType())) {
			if (jsonArray == null || jsonArray.size() == 0) {
				return R.error("填空题请设置正确答案");
			}
		}
		String majorId = tevglQuestionsInfo.getMajorId();
		String subjectId = tevglQuestionsInfo.getSubjectId();
		Map<String, Object> params = new HashMap<>();
		params.put("majorId", majorId);
		params.put("subjectId", subjectId);
		params.put("questionsType", tevglQuestionsInfo.getQuestionsType());
		params.put("noChildrenQuestion", "Y");
		List<Map<String,Object>> list = tevglQuestionsInfoMapper.selectSimpleListMap(params);
		boolean anyMatch = list.stream().anyMatch(a -> a.get("questionsName").equals(tevglQuestionsInfo.getQuestionsName()));
		if (anyMatch) {
			return R.error("同职业路径，同课程体系下已存在相同题目名称的题目，无需重复录入");
		}
		// 待保存的题目选项
		List<TevglQuestionChose> optionList = new ArrayList<TevglQuestionChose>();
		// 存放正确答案,即选项ID
		String ids = "";
		int choiceNum = 0;
		if (jsonArray != null && jsonArray.size() > 0) {
			// 选择题判断题
			if (Arrays.asList("1", "2", "3").contains(tevglQuestionsInfo.getQuestionsType())) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String code = jsonObject.getString("code"); // 选项编码
					String content = jsonObject.getString("content"); // 选项内容
					String isRight = jsonObject.getString("isRight"); // 是否正确
					// 填充选项
					TevglQuestionChose tevglQuestionChose = new TevglQuestionChose();
					tevglQuestionChose.setOptionId(Identities.uuid()); // 选项ID
					tevglQuestionChose.setQuestionsId(questionId); // 题目ID
					tevglQuestionChose.setCode(code); // 选项编码
					tevglQuestionChose.setContent(content); // 选项内容
					tevglQuestionChose.setState("Y"); // 状态Y有效N无效
					tevglQuestionChose.setSortNum(i);
					if ("YES".equals(isRight) || "Y".equals(isRight)) {
						ids += tevglQuestionChose.getOptionId() + ",";
						choiceNum++;
					}
					optionList.add(tevglQuestionChose);
				}
			}
			// 填空题
			if ("5".equals(tevglQuestionsInfo.getQuestionsType())) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String content = jsonObject.getString("content");
					// 填充选项
					TevglQuestionChose tevglQuestionChose = new TevglQuestionChose();
					tevglQuestionChose.setOptionId(Identities.uuid()); // 选项ID
					tevglQuestionChose.setQuestionsId(questionId); // 题目ID
					tevglQuestionChose.setContent(content);
					tevglQuestionChose.setState("Y"); // 状态Y有效N无效
					tevglQuestionChose.setSortNum(i);
					optionList.add(tevglQuestionChose);
				}
			}
		}
		if (StrUtils.isNotEmpty(ids)) {
			// 单选题情况
			if (tevglQuestionsInfo.getQuestionsType().equals("1") && choiceNum > 1) {
				return R.error("这道题只有" + choiceNum + "个正确答案，单选题最多只能有一个正确答案");
			}
			// 多选题情况
			if (tevglQuestionsInfo.getQuestionsType().equals("2") && choiceNum < 2) {
				return R.error("这道题只有" + choiceNum + "个正确答案，多选题最少两个正确答案");
			}
			// 更新题目的正确答案
			String optionIds = ids.substring(0, ids.length() - 1);
			tevglQuestionsInfo.setQuestionsId(questionId);
			tevglQuestionsInfo.setReplyIds(optionIds);
		}
		// 题目入库
		tevglQuestionsInfoMapper.insert(tevglQuestionsInfo);
		if (optionList != null && optionList.size() > 0) {
			// 题目选项入库
			tevglQuestionChoseMapper.insertBatch(optionList);
		}
		return R.ok("题目保存成功").put(Constant.R_DATA, questionId);
	}

	/**
	 * <p>
	 * 修改题目
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月9日
	 * @param tevglQuestionsInfo
	 * @return
	 */
	@Override
	@SysLog(value = "修改题目")
	public R editQuestion( TevglQuestionsInfo tevglQuestionsInfo,
			TevglTraineeInfo traineeInfo) throws BudaosException {
		// 权限校验
		if (!roleUtils.checkIsTeacher(traineeInfo.getTraineeId())) {
			return R.error("你还不是教师，无权限修改题目");
		}
		String questionsId = tevglQuestionsInfo.getQuestionsId();
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsId为空，非修改操作");
		}
		TevglQuestionsInfo info = tevglQuestionsInfoMapper.selectObjectById(questionsId);
		if (info == null) {
			return R.error("该题目已经不存在，请重试");
		}
		if (!info.getQuestionsType().equals(tevglQuestionsInfo.getQuestionsType())) {
			return R.error("修改题目时，不能更改题型");
		}
		try {
			String json = tevglQuestionsInfo.getJson();
			JSONArray children = JSON.parseArray(json);
			// 单选多选判断填空题时
			if (Arrays.asList("1", "2", "3").contains(tevglQuestionsInfo.getQuestionsType())) {
				if (StrUtils.isEmpty(json)) {
					return R.error("参数json为空");
				}
				if (!hasModelAnswer(children)) {
					return R.error("录入单选题、选择题、判断题时，请设置正确答案");
				}
			}
			// 修改题目基本信息
			tevglQuestionsInfoMapper.update(tevglQuestionsInfo);
			// 存放正确答案
			String ids = "";
			// 查出该题目原有选项
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "Y");
			map.put("questionsId", questionsId);
			map.put("sidx", "sort_num");
			map.put("order", "asc");
			List<TevglQuestionChose> oldOptionList = tevglQuestionChoseMapper.selectListByMap(map);
			if (children != null && children.size() > 0) {
				ids = doOptionChange(children, oldOptionList, questionsId);
				// 重新更新正确答案
				tevglQuestionsInfo.setReplyIds(ids);
				tevglQuestionsInfoMapper.update(tevglQuestionsInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("系统错误，题目修改失败");
		}
		// 返回题目信息
		TevglQuestionsInfo questionsInfo = selectObjectById(questionsId);
		List<String> allQuestionIds = tevglQuestionsInfoMapper.findQuestionIdsByUnionAll();
		if (allQuestionIds.stream().anyMatch(id -> id.equals(questionsInfo.getQuestionsId()))) {
			questionsInfo.setHasEditPermission(false);
		} else {
			questionsInfo.setHasEditPermission(true);
		}
		return R.ok("题目信息修改成功").put(Constant.R_DATA, questionsInfo);		
	}

	/**
	 * 校验单选、多选、判断题时是否设置了标准答案
	 * @param userOptionList
	 * @return 返回布尔值true表示设置了标准答案
	 */
	private boolean hasModelAnswer(JSONArray userOptionList) {
		if (userOptionList == null || userOptionList.size() == 0) {
			return false;
		}
		// 存储标准答案
		String ids = "";
		for (int j = 0; j < userOptionList.size(); j++) {
			JSONObject optionObject = userOptionList.getJSONObject(j);
			// 选项id
            String optionId = optionObject.getString("optionId");
			// 是否正确
			String isRight = optionObject.getString("isRight");
			// 填充选项
            TevglQuestionChose option = new TevglQuestionChose();
			// 说明这个是新添加的选项
            if (StrUtils.isEmpty(optionId) || optionId.length() < 32) {
                option.setOptionId(Identities.uuid());
            } else {
                option.setOptionId(optionId);
            }
            // 拼接题目标准答案选项optionId
            if ("Y".equals(isRight) || "YES".equals(isRight)) {
                ids += option.getOptionId() + ",";
            }
		}
		if (StrUtils.isNotEmpty(ids)) {
            ids = ids.substring(0, ids.length() - 1);
            return true;
        } else {
        	return false;
        }
	}
	
	/**
     * 处理题目选项变化的情况（如不变，变多，变少），且更新数据库记录
     * @param children 用户前端录入的选项
     * @param oldOptionList 此题目已存在于数据库中的选项
     * @param questionId 此题目
     * @return
     */
    private String doOptionChange(JSONArray children, List<TevglQuestionChose> oldOptionList, String questionId) {
        String ids = "";
        // 用于存放已存在于数据库中的选项id
        List<String> oldOptionIds = new ArrayList<String>();
        for (int j = 0; j < children.size(); j++) {
            JSONObject optionObject = children.getJSONObject(j);
            String optionId = optionObject.getString("optionId"); // 选项id
            String code = optionObject.getString("code"); // 选项编码
            String content = optionObject.getString("content"); // 选项内容
            String isRight = optionObject.getString("isRight"); // 是否正确
            // 填充选项
            TevglQuestionChose option = new TevglQuestionChose();
            option.setOptionId(optionId);
            option.setQuestionsId(questionId);
            option.setCode(code); // 选项编码
            option.setContent(content); // 选项内容
            option.setState("Y"); // 状态Y有效N无效
            option.setSortNum(j);
            // 注意，注意，注意
            // 如果删除已经存在的选项,再重新添加一个或多个新的选项,则需要额外处理
            // 说明这个是新添加的选项
            if (StrUtils.isEmpty(optionId) || optionId.length() < 32) {
                // 入库
                option.setOptionId(Identities.uuid());
                tevglQuestionChoseMapper.insert(option);
            } else {
                // 更新入库
                option.setOptionId(optionId);
                tevglQuestionChoseMapper.update(option);
                // 加入集合
                oldOptionIds.add(optionId);
            }
            // 拼接题目标准答案选项optionId
            if ("Y".equals(isRight) || "YES".equals(isRight)) {
                ids += option.getOptionId()+",";
            }
            // 最后一次循环时
            if (j == children.size() - 1) {
                // 单独取出选项id
                List<String> optionIds = oldOptionList.stream().map(a -> a.getOptionId()).collect(Collectors.toList());
                // 取差集
                optionIds.removeAll(oldOptionIds);
                // 执行删除
                if (optionIds != null && optionIds.size() > 0) {
                    log.debug("选修改前后，取差集，需要被删除的选项是：" + optionIds);
                    for (String string : optionIds) {
                        tevglQuestionChoseMapper.delete(string);
                    }
                }
            }
        }
        if (StrUtils.isNotEmpty(ids)) {
            ids = ids.substring(0, ids.length() - 1);
        }
        return ids;
    }
	
	@Override
	public List<TevglQuestionsInfo> selectListByMap(Map<String, Object> map) {
		return tevglQuestionsInfoMapper.selectListByMap(map);
	}

	/**
	 * 查询题目信息（包含选项信息）
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TevglQuestionsInfo selectObjectById(String id) {
		TevglQuestionsInfo questionsInfo = tevglQuestionsInfoMapper.selectObjectById(id);
		if (questionsInfo == null) {
			return null;
		}
		// 如果是复合题
		if ("6".equals(questionsInfo.getQuestionsType())) {
			// 查子题目
			Map<String, Object> map = new HashMap<>();
			map.put("parentId", questionsInfo.getQuestionsId());
			map.put("questionsState", "Y");
			map.put("sidx", "sort_num");
			map.put("order", "asc");
			List<TevglQuestionsInfo> childrenQuestionList = tevglQuestionsInfoMapper.selectListByMap(map);
			if (childrenQuestionList != null && childrenQuestionList.size() > 0) {
				List<String> questionIds = childrenQuestionList.stream().map(a -> a.getQuestionsId())
						.collect(Collectors.toList());
				map.clear();
				map.put("state", "Y");
				map.put("questionIds", questionIds);
				map.put("sidx", "sort_num");
				map.put("order", "asc, code asc");
				List<TevglQuestionChose> choseList = tevglQuestionChoseMapper.selectListByMap(map);
				childrenQuestionList.stream().forEach(question -> {
					List<TevglQuestionChose> optionList = choseList.stream()
							.filter(optionInfo -> optionInfo.getQuestionsId().equals(question.getQuestionsId()))
							.collect(Collectors.toList());
					question.setOptionList(optionList);
				});
				questionsInfo.setChildren(childrenQuestionList);
			}
			return questionsInfo;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("questionsId", questionsInfo.getQuestionsId());
		map.put("sidx", "code");
		map.put("order", "asc, sort_num asc");
		List<TevglQuestionChose> optionList = tevglQuestionChoseMapper.selectListByMap(map);
		// 处理标准答案
		questionsInfo.setOptionList(isModelAnswer(questionsInfo, optionList));
		return questionsInfo;
	}

	/**
	 * 处理单选题、多选题、判断题的标准答案
	 * 
	 * @param questionsInfo
	 * @param optionList
	 */
	private List<TevglQuestionChose> isModelAnswer(TevglQuestionsInfo questionsInfo,
			List<TevglQuestionChose> optionList) {
		if (!Arrays.asList("1", "2", "3").contains(questionsInfo.getQuestionsType())) {
			return optionList;
		}
		if (StrUtils.isEmpty(questionsInfo.getReplyIds())) {
			return optionList;
		}
		if (optionList == null || optionList.size() == 0) {
			return optionList;
		}
		String[] split = questionsInfo.getReplyIds().split(",");
		for (TevglQuestionChose optionInfo : optionList) {
			// 可用于回显题目的正确答案
			optionInfo.setModelAnswer(false);
			for (int i = 0; i < split.length; i++) {
				if (optionInfo.getOptionId().equals(split[i])) {
					optionInfo.setModelAnswer(true);
				}
			}
		}
		return optionList;
	}

	/**
	 * <p>
	 * 通过题目类型、知识点id随机生成limit个问题
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月23日
	 * @param map
	 * @return
	 */
	@Override
	public List<TevglQuestionsInfo> selectRandomListByKnowangeIdAndType(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * 根据问题id批量查询问题
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月23日
	 * @param questionsIds
	 * @return
	 */
	@Override
	public List<TevglQuestionsInfo> selectBatchQuestionsByArrays(String[] questionsIds) {
		return tevglQuestionsInfoMapper.selectBatchQuestionsByArrays(questionsIds);
	}

	/**
	 * <p>
	 * 用于更新问题的组卷数、正确数、错误数、正确率等问题信息
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月23日
	 * @param tevglQuestionsInfo
	 */
	@Override
	@SysLog(value = "更新问题的组卷数、正确数、错误数、正确率等问题信息")
	public void plusNum(TevglQuestionsInfo tevglQuestionsInfo) {
		tevglQuestionsInfoMapper.plusNum(tevglQuestionsInfo);
	}

	/**
	 * <p>
	 * 只根据条件统计选择题数量和判断题题数量,以及题目总数量(问答题不在统计范围内)
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月25日
	 * @param map
	 * @return
	 */
	@Override
	@SysLog(value = "只根据条件统计选择题数量和判断题题数量,以及题目总数量(问答题不在统计范围内)")
	public Map<String, Object> countQuestionNumByMap(Map<String, Object> map) {
		return tevglQuestionsInfoMapper.countQuestionNumByMap(map);
	}

	/**
	 * 根据条件统计单选题、多选题、判断题、问答题的题数，以及总题目数量
	 * 
	 * @param map
	 * @return
	 * @apiNote totalSingleChose
	 *          单选题数量、totalMultipleChose多选题数量、totalJudge判断题数量、totalShortAnswer问答题数量、totalSize总题目量
	 */
	@Override
	@SysLog(value = "根据条件统计单选题、多选题、判断题、问答题的题数，以及总题目数量")
	public Map<String, Object> countQuestionNumByMap2(Map<String, Object> map) {
		return tevglQuestionsInfoMapper.countQuestionNumByMap2(map);
	}

	/**
	 * <p>
	 * 根据条件随机筛选题目，如章节ID、题目类型
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月25日
	 * @param map
	 * @return
	 */
	@Override
	@SysLog(value = "根据条件随机筛选题目，如章节ID、题目类型")
	public List<TevglQuestionsInfo> selectRandomQuestionListByMap(Map<String, Object> map) {
		return tevglQuestionsInfoMapper.selectRandomQuestionListByMap(map);
	}

	/**
	 * <p>
	 * 统计教材的总题目数量
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月30日
	 * @param subjectId
	 * @return
	 */
	@Override
	public int countSubjectQuestionTotal(String subjectId) {
		if (StrUtils.isNotEmpty(subjectId)) {
			return tevglQuestionsInfoMapper.countSubjectQuestionTotal(subjectId);
		}
		return 0;
	}

	/**
	 * 禁用题目
	 * 
	 * @param questionsId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R forbiddenQuestions(String questionsId, String loginUserId) {
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsId为空");
		}
		try {
			TevglQuestionsInfo t = new TevglQuestionsInfo();
			t.setQuestionsId(questionsId);
			t.setQuestionsState("N"); // 题目状态(Y启用N停用)
			/*//根据题目id查询题目信息，然后判断要禁用的题目是否是当前登录用户创建的，如果不是则提示
			TevglQuestionsInfo questionsInfo = tevglQuestionsInfoMapper.selectObjectById(questionsId);
			if (!questionsInfo.getCreateUserId().equals(loginUserId)) {
				return R.error("非创建者不能操作");
			}*/
			tevglQuestionsInfoMapper.update(t);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("题目禁用失败", e);
			return R.error("系统错误，题目禁用失败");
		}
		return R.ok("题目禁用成功");
	}

	/**
	 * 收藏题目
	 * 
	 * @param questionsId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R collectQuestions(String questionsId, String loginUserId) {
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsId为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("favorityType", GlobalFavority.FAVORITY_6_QUESTION); // 6题目
		map.put("targetId", questionsId);
		map.put("traineeId", loginUserId);
		List<TmeduMeFavority> list = tmeduMeFavorityMapper.selectListByMap(map);
		if (list.size() > 0) {
			return R.error(501, "你已收藏该题目");
		}
		try {
			TmeduMeFavority tmeduFavority = new TmeduMeFavority();
			tmeduFavority.setFavorityId(Identities.uuid());
			tmeduFavority.setTargetId(questionsId);
			tmeduFavority.setFavorityType(GlobalFavority.FAVORITY_6_QUESTION);
			tmeduFavority.setTraineeId(loginUserId);
			tmeduFavority.setFavorityTime(DateUtils.getNowTimeStamp());
			// 保存
			tmeduMeFavorityMapper.insert(tmeduFavority);
			// 更新题目收藏数
			TevglQuestionsInfo tevglQuestionsInfo = new TevglQuestionsInfo();
			tevglQuestionsInfo.setQuestionsId(questionsId);
			tevglQuestionsInfo.setQuestionsStoreNum(1);
			tevglQuestionsInfoMapper.plusNum(tevglQuestionsInfo);
		} catch (BudaosException e) {
			e.printStackTrace();
		}
		return R.ok("收藏题目成功");
	}

	/**
	 * 取消收藏题目
	 * 
	 * @param questionsId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R cancelCollectQuestions(String questionsId, String loginUserId) {
		if (StrUtils.isEmpty(questionsId) || "null".equals(questionsId)) {
			return R.error("参数questionsId为空");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("favorityType", GlobalFavority.FAVORITY_6_QUESTION); // 6题目
		map.put("targetId", questionsId);
		map.put("traineeId", loginUserId);
		List<TmeduMeFavority> list = tmeduMeFavorityMapper.selectListByMap(map);
		if (list.size() == 0) {
			return R.error(502, "你尚未收藏该题目");
		}
		try {
			for (TmeduMeFavority tmeduFavority : list) {
				tmeduMeFavorityMapper.delete(tmeduFavority.getFavorityId());
			}
			// 更新题目收藏数
			TevglQuestionsInfo tevglQuestionsInfo = new TevglQuestionsInfo();
			tevglQuestionsInfo.setQuestionsId(questionsId);
			tevglQuestionsInfo.setQuestionsStoreNum(-1);
			tevglQuestionsInfoMapper.plusNum(tevglQuestionsInfo);
		} catch (BudaosException e) {
			e.printStackTrace();
		}
		return R.ok("取消收藏题目成功");
	}

	/**
	 * 新增题目（复合题专属接口）
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 * @apiNote 数据格式要求如下
	 */
	@Override
	@Transactional
	public R saveCompositeQuestionInfo( JSONObject jsonObject, String loginUserId) {
		// 合法性校验
		R r = checkIsPass(jsonObject, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 0不显示1显示 从测试活动录入的题目，也就是from_type为1时，根据此值是否在题库显示
		String showType = jsonObject.getString("showType");
		// 填充主题目信息
		TevglQuestionsInfo mainQuestionInfo = fillMainQuestionsInfo(jsonObject, loginUserId);
		mainQuestionInfo.setShowType(showType);
		log.debug("此题是否在题目显示：" + mainQuestionInfo.getShowType());
		// 入库
		tevglQuestionsInfoMapper.insert(mainQuestionInfo);
		// 子题目
		JSONArray questionList = jsonObject.getJSONArray("questionList");
		// 返回数据
		List<String> questionIdList = new ArrayList<String>();
		for (int i = 0; i < questionList.size(); i++) {
			JSONObject questionData = questionList.getJSONObject(i);
			// 如果子题目，还选择了复合题，直接返回
			if ("6".equals(questionData.get("questionsType"))) {
				return R.error("注意，复合题里面的子题目，不可以再继续添加复合题");
			}
			questionData.put("majorId", mainQuestionInfo.getMajorId());
			questionData.put("subjectId", mainQuestionInfo.getSubjectId());
			questionData.put("chaptersId", mainQuestionInfo.getChaptersId());
			TevglQuestionsInfo childrenQuestionsInfo = fillChildrenQuestionsInfo(questionData, loginUserId,
					mainQuestionInfo.getQuestionsId());
			JSONArray optionList = questionData.getJSONArray("optionList");
			// 入库
			childrenQuestionsInfo.setSortNum(i+1);
			R res = doSaveQuestionAndOptionList(childrenQuestionsInfo.getQuestionsId(), optionList, childrenQuestionsInfo);
			log.debug("结果：" + res);
			if (res.get("code").equals(0)) {
				questionIdList.add(res.get(Constant.R_DATA).toString());
			}
		}
		return R.ok("题目保存成功").put(Constant.R_DATA, mainQuestionInfo.getQuestionsId()).put("questionIdList", questionIdList);
	}

	/**
	 * 填充主体题目信息
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	private TevglQuestionsInfo fillMainQuestionsInfo(JSONObject jsonObject, String loginUserId) {
		return doFillQuestionsInfo(jsonObject, loginUserId, null);
	}

	/**
	 * 填充子题目信息
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	private TevglQuestionsInfo fillChildrenQuestionsInfo(JSONObject jsonObject, String loginUserId, String parentId) {
		return doFillQuestionsInfo(jsonObject, loginUserId, parentId);
	}
	
	/**
	 * 填充题目信息
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @param parentId
	 * @return
	 */
	private TevglQuestionsInfo doFillQuestionsInfo(JSONObject jsonObject, String loginUserId, String parentId) {
		// 1单选题2多选题3判断题4简答题5填空题6复合题
		String questionsType = StrUtils.isEmpty(jsonObject.getString("questionsType")) ? "6"
				: jsonObject.getString("questionsType");
		String questionsId = jsonObject.getString("questionsId");
		String majorId = jsonObject.getString("majorId");
		String subjectId = jsonObject.getString("subjectId");
		String chaptersId = jsonObject.getString("chaptersId");
		String questionsName = jsonObject.getString("questionsName");
		String questionsParse = jsonObject.getString("questionsParse");
		String questionsComplexity = jsonObject.getString("questionsComplexity");
		String fromType = StrUtils.isNull(jsonObject.getString("fromType")) ? null : jsonObject.getString("fromType");
		// 填充数据项
		TevglQuestionsInfo tevglQuestionsInfo = new TevglQuestionsInfo();
		// 主键ID
		tevglQuestionsInfo.setQuestionsId(StrUtils.isEmpty(questionsId) ? Identities.uuid() : questionsId);
		tevglQuestionsInfo.setMajorId(majorId);
		tevglQuestionsInfo.setSubjectId(subjectId);
		tevglQuestionsInfo.setChaptersId(chaptersId);
		tevglQuestionsInfo.setQuestionsName(questionsName);
		tevglQuestionsInfo.setParentId(StrUtils.isEmpty(parentId) ? null : parentId);
		tevglQuestionsInfo.setQuestionsType(questionsType);
		tevglQuestionsInfo.setQuestionsParse(questionsParse);
		tevglQuestionsInfo.setQuestionsComplexity(questionsComplexity);
		tevglQuestionsInfo.setQuestionsState("Y"); // 题目状态(Y启用N停用)（是否作废）
		tevglQuestionsInfo.setQuestionsStar("0"); // 题目星级
		tevglQuestionsInfo.setQuestionsConstructingNum(0); // 组卷次数
		tevglQuestionsInfo.setQuestionsAccuracy(new BigDecimal(0)); // 正确率
		tevglQuestionsInfo.setQuestionsStoreNum(0); // 收藏数
		tevglQuestionsInfo.setQuestionsAnswerNum(0); // 作答数
		tevglQuestionsInfo.setQuestionsCorrectNum(0); // 正确数
		tevglQuestionsInfo.setQuestionsErrorNum(0); // 错误数
		tevglQuestionsInfo.setReplyIds(null);
		tevglQuestionsInfo.setQuestionsState("Y");
		tevglQuestionsInfo.setCreateTime(DateUtils.getNowTimeStamp());
		tevglQuestionsInfo.setCreateUserId(loginUserId);
		tevglQuestionsInfo.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglQuestionsInfo.setUpdateUserId(loginUserId);
		tevglQuestionsInfo.setFromType(fromType);
		return tevglQuestionsInfo;
	}

	/**
	 * 新增复合题时的合法性校验
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	private R checkIsPass(JSONObject jsonObject, String loginUserId) {
		String majorId = jsonObject.getString("majorId");
		String subjectId = jsonObject.getString("subjectId");
		if (StrUtils.isEmpty(majorId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (!roleUtils.checkIsTeacher(loginUserId)) {
			return R.error("暂无权限操作题目，请联系系统管理员");
		}
		String questionsName = jsonObject.getString("questionsName");
		if (StrUtils.isEmpty(questionsName)) {
			return R.error("题目名称不能为空");
		}
		questionsName = questionsName.trim();
		if (StrUtils.isEmpty(questionsName)) {
			return R.error("题目名称不能为空");
		}
		// 题目类型1单选题2多选题3判断题4简答题5填空题6复合题
		String questionsType = jsonObject.getString("questionsType");
		if (!"6".equals(questionsType)) {
			return R.error("非法操作");
		}
		JSONArray questionList = jsonObject.getJSONArray("questionList");
		if (questionList == null || questionList.size() == 0) {
			return R.error("请录入题目");
		}
		return R.ok();
	}

	/**
	 * 删除复合题
	 * 
	 * @param questionsId
	 * @param loginUserId
	 * @return
	 */
	@Override
    @Transactional
    public R deleteCompositeQuestionInfo(String questionsId, String loginUserId) {
        if (StrUtils.isEmpty(questionsId)) {
            return R.error("必传参数为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", questionsId);
        // 查找子题目
        List<TevglQuestionsInfo> questionsInfoList = tevglQuestionsInfoMapper.selectListByMap(params);
        if (questionsInfoList != null && questionsInfoList.size() > 0) {
            // 查找子题目的选项
            List<String> questionIds = questionsInfoList.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList());
            params.clear();
            params.put("questionIds", questionIds);
            List<TevglQuestionChose> choseList = tevglQuestionChoseMapper.selectListByMap(params);
            if (choseList != null && choseList.size() > 0) {
                List<String> optionsIds = choseList.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList());
                tevglQuestionChoseMapper.deleteBatch(optionsIds.stream().toArray(String[]::new));
            }
            // 删除题目表
            tevglQuestionsInfoMapper.deleteBatch(questionIds.stream().toArray(String[]::new));
        }
        // 删除主题
        tevglQuestionsInfoMapper.delete(questionsId);
        return R.ok("删除成功");
    }

	/**
	 * 修改复合题
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 * @apiNote
	 * 考虑三种情况
	 * ①子题目个数修改前后没有变化，注意考虑，删除原有的子题目，再录入一个新的题目，这个时候也是相等的
	 * ②修改前后，子题目数量变多
	 * ③修改前后，子题目数量变少
	 */
	@Override
	public R updateCompositeQuestionInfo( JSONObject jsonObject, String loginUserId) {
		// 合法性校验
		R r = checkIsPass(jsonObject, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		String mainQuestionsId = jsonObject.getString("questionsId");
		TevglQuestionsInfo tevglQuestionsInfo = tevglQuestionsInfoMapper.selectObjectById(mainQuestionsId);
		if (tevglQuestionsInfo == null) {
			return R.error("题目已不存在，无法修改");
		}
		// 不允许修改部分信息
		jsonObject.put("majorId", tevglQuestionsInfo.getMajorId());
		jsonObject.put("subjectId", tevglQuestionsInfo.getSubjectId());
		jsonObject.put("chapterId", tevglQuestionsInfo.getChaptersId());
		// 填充主题目信息，更新入库
		TevglQuestionsInfo mainQuestionInfo = fillMainQuestionsInfo(jsonObject, loginUserId);
		tevglQuestionsInfoMapper.update(mainQuestionInfo);
		// 用户录入的子题目数据
		JSONArray questionList = jsonObject.getJSONArray("questionList");
		// 已存在的子题目数据
		Map<String, Object> params = new HashMap<>();
		params.put("parentId", mainQuestionsId);
		List<TevglQuestionsInfo> existedQuestionList = tevglQuestionsInfoMapper.selectListByMap(params);
		if (existedQuestionList == null || existedQuestionList.size() == 0) {
			return R.ok("未能获取到子题目");
		}
		for (int i = 0; i < questionList.size(); i++) {
			questionList.getJSONObject(i).put("majorId", tevglQuestionsInfo.getMajorId());
			questionList.getJSONObject(i).put("subjectId", tevglQuestionsInfo.getSubjectId());
		}
		// 查找题目选项
		List<String> existedQuestionIdList = existedQuestionList.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList());
		params.clear();
		params.put("questionsIds", existedQuestionIdList);
		List<TevglQuestionChose> existedOptionList = tevglQuestionChoseMapper.selectListByMap(params);
		// 处理题目与选项数据
		doQuestionChange(mainQuestionsId, questionList, existedQuestionList, existedOptionList, loginUserId);
		// 返回题目信息
		TevglQuestionsInfo questionsInfo = selectObjectById(mainQuestionsId);
		List<String> allQuestionIds = tevglQuestionsInfoMapper.findQuestionIdsByUnionAll();
		if (allQuestionIds.stream().anyMatch(id -> id.equals(questionsInfo.getQuestionsId()))) {
			questionsInfo.setHasEditPermission(false);
		} else {
			questionsInfo.setHasEditPermission(true);
		}
		return R.ok("题目修改成功").put(Constant.R_DATA, questionsInfo);
	}
	
	/**
	 * 修改复合题时，子题目数据处理
	 * @param mainQuestionsId 当前被修改的主题目
	 * @param userQuestionList 用户前端录入的题目
	 * @param existedQuestionList 当前主题目，已存在于数据库中子题目
	 * @param existedOptionList 当前主题目，所有子题目对应的选项
	 * @param loginUserId 当前登录用户
	 */
	private void doQuestionChange(String mainQuestionsId, JSONArray userQuestionList, 
			List<TevglQuestionsInfo> existedQuestionList, 
			List<TevglQuestionChose> existedOptionList, String loginUserId) {
		if (StrUtils.isEmpty(mainQuestionsId) 
				|| userQuestionList == null || userQuestionList.size() == 0
				|| existedQuestionList == null || existedQuestionList.size() == 0) {
			return;
		}
		// 用于存放已存在于数据库中的题目id
		List<String> oldQuestionIds = new ArrayList<String>();
		// 遍历
		for (int i = 0; i < userQuestionList.size(); i++) {
			// 用户录入的题目信息
			JSONObject jsonObject = userQuestionList.getJSONObject(i);
			String questionsId = jsonObject.getString("questionsId");
			// 用户录入的题目选项
			JSONArray userOptionList = jsonObject.getJSONArray("optionList");
			// 填充信息
			TevglQuestionsInfo t = fillChildrenQuestionsInfo(jsonObject, loginUserId, mainQuestionsId);
			t.setSortNum(i+1);
			// 说明这个是新添加的题目
			if (StrUtils.isEmpty(questionsId) || questionsId.length() < 32) {
				// 调用方法，保存这个新题目和对应选项
				doSaveQuestionAndOptionList(t.getQuestionsId(), userOptionList, t);
			} else {
				// 加入集合
				oldQuestionIds.add(questionsId);
				// 更新
				tevglQuestionsInfoMapper.update(t);
				// 取出原有题目的选项
				List<TevglQuestionChose> thisQuestionOldOptionList = existedOptionList.stream().filter(a -> a.getQuestionsId().equals(questionsId)).collect(Collectors.toList());
				if (userOptionList != null && userOptionList.size() > 0) {
					// 处理选项数据
					String ids = doOptionChange(userOptionList, thisQuestionOldOptionList, questionsId);
					// 重新更新正确答案
					t.setReplyIds(ids);
	                tevglQuestionsInfoMapper.update(t);
				}
			}
			// 最后一次循环时
            if (i == userQuestionList.size() - 1) {
                List<String> questionsIds = existedQuestionList.stream().map(a -> a.getQuestionsId()).collect(Collectors.toList());
                // 取差集
                questionsIds.removeAll(oldQuestionIds);
                // 执行删除
                if (questionsIds != null && questionsIds.size() > 0) {
                    for (String id : questionsIds) {
                    	deleteQuesiton(id, existedOptionList);
                    }
                }
            }
		}
	}
	
	/**
	 * 删除这个题目的题目选项
	 * @param questionsId
	 * @param existedOptionList
	 */
	private void deleteQuesiton(String questionsId, List<TevglQuestionChose> existedOptionList) {
		if (StrUtils.isEmpty(questionsId) || existedOptionList == null || existedOptionList.size() == 0) {
			return;
		}
		// 删除题目选项
		List<TevglQuestionChose> optionList = existedOptionList.stream().filter(a -> a.getQuestionsId().equals(questionsId)).collect(Collectors.toList());
		if (optionList != null && optionList.size() > 0) {
			List<String> optionIds = optionList.stream().map(a -> a.getOptionId()).collect(Collectors.toList());
			String[] ids = optionIds.stream().toArray(String[]::new);
			tevglQuestionChoseMapper.deleteBatch(ids);
		}
		// 删除题目
		tevglQuestionsInfoMapper.delete(questionsId);
	}
}
