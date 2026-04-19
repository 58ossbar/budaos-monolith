package com.budaos.modules.evgl.trainee.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.utils.DictUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamine;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExammember;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamroom;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamineMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExammemberMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamroomMapper;
import com.budaos.modules.evgl.eao.service.TeaoTraineeExamroomServiceImpl;
import com.budaos.modules.evgl.trainee.api.TevglTraineeScoreService;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeScoreMapper;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeScoreVo;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TevglTraineeScoreServiceImpl implements TevglTraineeScoreService {

	private Logger log = LoggerFactory.getLogger(TeaoTraineeExamroomServiceImpl.class);

	@Autowired
	private TevglTraineeScoreMapper tevglTraineeScoreMapper;
	@Autowired
	private TeaoTraineeExamroomMapper teaoTraineeExamroomMapper;
	@Autowired
	private TeaoTraineeExammemberMapper teaoTraineeExammemberMapper;
	@Autowired
	private TeaoTraineeExamineMapper teaoTraineeExamineMapper;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private DictUtil dictUtil;
	
	/**
	 * 根据条件查询学生成绩
	 * @param map
	 * @return
	 */
	@Override
	@SysLog(value="根据条件查询学生成绩")
	public R findTraineeScoreList(Map<String, Object> map) {
		Query query = new Query(map);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTraineeScoreVo> traineeScoreList = tevglTraineeScoreMapper.findTraineeScoreList(query);
		// 考核
		List<String> erIdList = traineeScoreList.stream().map(TevglTraineeScoreVo::getErId).distinct().collect(Collectors.toList());
		map.put("erIdList", erIdList);
		List<TeaoTraineeExamroom> teaoTraineeExamrooms = teaoTraineeExamroomMapper.selectListByMap(map);
		// 根据条件取出考核成员
		List<TeaoTraineeExammember> allTeaoTraineeExammemberList = findAllTeaoTraineeExammemberList(traineeScoreList);
		// 查找所有学员
		List<Map<String, Object>> allTraineeList = tevglTraineeInfoMapper.selectSimpleListByMap(new HashMap<>());
		// 查出本
		traineeScoreList.stream().forEach(vo -> {
			// 如果是时间考核，计算最终成绩
			if ("9".equals(vo.getType())) {
				List<TeaoTraineeExammember> teaoTraineeExammemberList = allTeaoTraineeExammemberList.stream().filter(a -> a.getErId().equals(vo.getErId())).collect(Collectors.toList());
				if (teaoTraineeExammemberList != null && teaoTraineeExammemberList.size() > 0) {
					List<Map<String, Object>> traineeList = new ArrayList<>();
					// 取出当前考核成员
					Map<String, Object> traineeInfo = findTraineeInfo(allTraineeList, vo.getTraineeId());
					if (traineeInfo != null && !traineeList.contains(traineeInfo)) {
						traineeList.add(traineeInfo);
					}
					// 计算
					List<TeaoTraineeExamroom> teaoTraineeExamroomList = teaoTraineeExamrooms.stream().filter(a -> a.getErId().equals(vo.getErId())).collect(Collectors.toList());
					if (teaoTraineeExamroomList != null && teaoTraineeExamroomList.size() > 0) {
						TeaoTraineeExamroom teaoTraineeExamroom = countFinalScore(teaoTraineeExamroomList.get(0), traineeList);
						List<Map<String, Object>> studentList = teaoTraineeExamroom.getStudentList();
						List<Map<String, Object>> collect = studentList.stream().filter(a -> a.get("traineeId").equals(vo.getTraineeId())).collect(Collectors.toList());
						if (collect.size() > 0) {
							vo.setFinalScore((BigDecimal) collect.get(0).get("finalScore"));
							log.debug("最终成绩 => {}", collect.get(0).get("finalScore"));
						}
					}
				}
			}
		});
		PageUtils pageUtil = new PageUtils(traineeScoreList, query.getPage(), query.getLimit());
		// 日期降序 => 课程名称降序 => 成绩降序
		List<TevglTraineeScoreVo> collect1 = traineeScoreList.stream()
				.sorted(Comparator.comparing((TevglTraineeScoreVo a) -> a.getExamTime())
						.thenComparing(TevglTraineeScoreVo::getSubjectName)
						.thenComparing(TevglTraineeScoreVo::getFinalScore).reversed())
				.collect(Collectors.toList());
		pageUtil.setList(collect1);
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 先找到这些考核的所有考核学员
	 * @param traineeScoreList
	 * @return
	 */
	private List<TeaoTraineeExammember> findAllTeaoTraineeExammemberList(List<TevglTraineeScoreVo> traineeScoreList) {
		List<String> erIdList = traineeScoreList.stream().map(TevglTraineeScoreVo::getErId).distinct().collect(Collectors.toList());
		List<String> classIdsList = traineeScoreList.stream().map(TevglTraineeScoreVo::getClassIds).distinct().collect(Collectors.toList());
		List<String> idList = new ArrayList<>();
		for (int i = 0; i < classIdsList.size(); i++) {
			String classId = classIdsList.get(i);
			if (StrUtils.isNotEmpty(classId)) {
				String[] classIdArr = classId.split(",");
				if (classIdArr != null && classIdArr.length > 0) {
					List<String> list = Stream.of(classIdArr).collect(Collectors.toList());
					idList.addAll(list);
				}
			}
		}
		Map<String, Object> params = new HashMap<>();
		params.put("traineeType", "1");
		params.put("activityIds", erIdList);
		if (idList.size() > 0) {
			params.put("classIds", idList);
		}
		List<TeaoTraineeExammember> allTeaoTraineeExammemberList = teaoTraineeExammemberMapper.selectListByMap(params);
		log.debug("根据查询查询考核成员:" + params);
		log.debug("查询结果:" + allTeaoTraineeExammemberList.size());
		return allTeaoTraineeExammemberList;
	}

	private Map<String, Object> findTraineeInfo(List<Map<String, Object>> allTraineeList, String traineeId) {
		List<Map<String, Object>> mapList = allTraineeList.stream().filter(traineeInfo -> traineeInfo.get("traineeId").equals(traineeId)).collect(Collectors.toList());
		return (mapList != null && mapList.size() > 0) ? mapList.get(0) : null;
	}


	/**
	 * 计算最终成绩
	 * @param teaoTraineeExamroom
	 * @param traineeList
	 * @return
	 */
	private TeaoTraineeExamroom countFinalScore(TeaoTraineeExamroom teaoTraineeExamroom, List<Map<String, Object>> traineeList) {
		if (teaoTraineeExamroom == null) {
			return new TeaoTraineeExamroom();
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> studentList = new ArrayList<>();
		// 本次考核
		String erId = teaoTraineeExamroom.getErId();
		// 本次考核的平台课程
		String subjectId = teaoTraineeExamroom.getSubjectId();
		for (Map<String, Object> traineeInfo : traineeList) {
			// 处理头像
			traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic"), "16"));
			// 调用方法,设置学员的自评成绩
			getMySelfGiveScore(traineeInfo, erId, subjectId, params);
			// 调用方法,设置学员的互评成绩
			getMyStudentGiveScore(traineeInfo, erId, subjectId, params);
			// 调用方法，设置学员的师评成绩
			getMyTeacherGiveScore(traineeInfo, erId, subjectId, params);
			// 调用方法，设置学员的最终成绩
			getFinalScore(traineeInfo);
			// 加入集合
			studentList.add(traineeInfo);
		}
		// 按成绩降序
		studentList = sort(studentList);
		// 处理同分同名的情况
		ranking(studentList);
		// 填充数据
		teaoTraineeExamroom.setStudentList(studentList);
		return teaoTraineeExamroom;
	}
	
	/**
	 * 排名
	 * @param studentList
	 * @return
	 */
	private List<Map<String,Object>> sort(List<Map<String,Object>> studentList) {
		// 最终成绩
		if (studentList == null || studentList.size() == 0) {
			return new ArrayList<>();
		}
		return studentList.stream().sorted((h1, h2) -> ((BigDecimal)h2.get("finalScore")).compareTo(((BigDecimal)h1.get("finalScore")))).collect(Collectors.toList());
	}

	/**
	 * 处理最高得分有同分的情况
	 * @param resultList
	 */
	private void ranking(List<Map<String, Object>> resultList) {
		if (resultList == null || resultList.size() == 0) {
			return;
		}
		// 排名
		int index = 0;
		// 最近一次的分
		BigDecimal lastScore = new BigDecimal("-1");
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = resultList.get(i);
			// 如果成绩和上一名的成绩不相同,那么排名+1
			if (!(lastScore.compareTo((BigDecimal) map.get("finalScore")) == 0)) {
				lastScore = (BigDecimal)map.get("finalScore");
				index++;
			}
			// 排名
			map.put("ranking", index);
		}
	}

	/**
	 * 实际 计算并设置学员最终成绩（平均成绩）
	 * @param traineeInfo 学员信息
	 * @apiNote
	 * mySelfWeight 自评的权重
	 * myStudentWeight 互评的权重
	 * myTeacherWeight 师评的权重
	 * @return 传入的学员 teaoTraineeInfo
	 */
	private Map<String, Object> getFinalScore(Map<String, Object> traineeInfo) {
		// 调用方法，获取相应得分权重
		Map<String,BigDecimal> md = getExamScoreScale();
		BigDecimal mySelfWeight = md.get("自评");
		BigDecimal myStudentWeight = md.get("互评");
		BigDecimal myTeacherWeight = md.get("师评");
		// 自评、互评、师评按权重计算
		BigDecimal my = ((BigDecimal) traineeInfo.get("mySelfGiveScore")).multiply(mySelfWeight);
		BigDecimal his = ((BigDecimal) traineeInfo.get("myStudentGiveScore")).multiply(myStudentWeight);
		BigDecimal teacher = ((BigDecimal) traineeInfo.get("myTeacherGiveScore")).multiply(myTeacherWeight);
		// 计算最终成绩
		BigDecimal temp = my.add(his);
		BigDecimal finalScore = temp.add(teacher);
		BigDecimal finalScoreOk = finalScore.setScale(2, BigDecimal.ROUND_HALF_UP); // 四舍五入保留两位小数
		// 设置最终成绩
		traineeInfo.put("finalScore", finalScoreOk);
		return traineeInfo;
	}

	/**
	 * 获取师评分
	 * 【师评成绩】    1.统计多少老师参与师评，得到人数    2.统计师评总分    3. 总分 / 人数 = 师评成绩
	 * @param traineeInfo 学员信息
	 * @param erId 本次考核t_eao_trainee_examroom主键
	 * @param subjectId 本次考核对应的平台课程
	 * @param params 查询条件
	 * @return
	 */
	private Map<String, Object> getMyTeacherGiveScore(Map<String, Object> traineeInfo, String erId, String subjectId, Map<String, Object> params) {
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "3"); // 评分类型(1自评2互评3教师4临时)
		// 参与师评的成绩
		String myTeacherGiveScoreTotal = teaoTraineeExamineMapper.countMyTeacherGiveScore(params);
		// 参与师评的人数
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "1");
		params.put("examineType2", "2");
		List<TeaoTraineeExamine> myTeacherNum = teaoTraineeExamineMapper.selectHowManyTeacherJoinExamine(params);
		BigDecimal myTeacherGiveScore = new BigDecimal("0");
		if (myTeacherNum != null && myTeacherNum.size() > 0) {
			// 计算
			BigDecimal teacherNum = new BigDecimal(myTeacherNum.size());
			myTeacherGiveScore = new BigDecimal(myTeacherGiveScoreTotal).divide(teacherNum, 2, BigDecimal.ROUND_HALF_UP);
		}
		traineeInfo.put("myTeacherGiveScore", myTeacherGiveScore);
		return traineeInfo;
	}

	/**
	 * 获取互评分
	 * 【互评成绩】    1.先查出有多少学员参与互评    2.查出互评成绩的总和    3. 总和 / 人数 = 互评成绩
	 * @param traineeInfo 学员信息
	 * @param erId 本次考核t_eao_trainee_examroom主键
	 * @param subjectId 本次考核对应的平台课程
	 * @param params 查询条件
	 * @return
	 */
	private Map<String, Object> getMyStudentGiveScore(Map<String, Object> traineeInfo, String erId, String subjectId, Map<String, Object> params) {
		// 获得对学员互评的人数
		params.clear();
		params.put("erId", erId);
		params.put("examineType", "2"); // 评分类型(1自评2互评3教师4临时)
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		List<TeaoTraineeExamine> list = teaoTraineeExamineMapper.selectHowManyTraineesJoinExamine(params);
		// 如果没有人给他评分
		if (list == null || list.size() == 0) {
			traineeInfo.put("myStudentGiveScore", new BigDecimal("0"));
			return traineeInfo;
		}
		// 根据条件获得学员互评成绩的总分
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "2"); // 评分类型(1自评2互评3教师4临时)
		// 互评总分
		String score = teaoTraineeExamineMapper.countMyStudentGiveScore(params);
		// 互评人数
		BigDecimal number = new BigDecimal(list.size());
		// 计算
		BigDecimal myStudentGiveScore = new BigDecimal(score).divide(number, 2, BigDecimal.ROUND_HALF_UP);
		traineeInfo.put("myStudentGiveScore", myStudentGiveScore);
		return traineeInfo;
	}

	/**
	 * 获取自评分
	 * @param traineeInfo 学员信息
	 * @param erId 本次考核t_eao_trainee_examroom主键
	 * @param subjectId 本次考核对应的平台课程
	 * @param params 查询条件
	 * @return 学员信息
	 */
	private Map<String, Object> getMySelfGiveScore(Map<String, Object> traineeInfo, String erId, String subjectId, Map<String, Object> params) {
		params.clear();
		params.put("erId", erId);
		params.put("subjectId", subjectId);
		params.put("traineeId", traineeInfo.get("traineeId"));
		params.put("examineType", "1"); // 评分类型(1自评2互评3教师4临时)
		String mySelfGiveScore = teaoTraineeExamineMapper.countMySelfGiveScore(params);
		traineeInfo.put("mySelfGiveScore", new BigDecimal(StrUtils.isEmpty(mySelfGiveScore) ? "0" : mySelfGiveScore));
		return traineeInfo;
	}


	/**
	 * 获取自评、互评、师评的权重比例
	 * @return
	 */
	private Map<String, BigDecimal> getExamScoreScale() {
		List<TsysDict> dicts = dictUtil.getByType("examScoreScale");
		Map<String, BigDecimal> dictMaps = new HashMap<String, BigDecimal>();
		if(dicts != null && dicts.size() > 0) {
			for(TsysDict dict : dicts) {
				dictMaps.put(dict.getDictCode(), new BigDecimal(dict.getDictValue()));
			}
		}
		return dictMaps;
	}

	@Override
	public R findStudentInfo(String traineeId) {
		Map<String, Object> studentInfo = tevglTraineeScoreMapper.findStudentInfo(traineeId);
		if (studentInfo != null && studentInfo.get("majorNames") != null) {
			String majorNames = studentInfo.get("majorNames").toString();
			String[] split = majorNames.split(",");
			List<String> collect = Stream.of(split).distinct().collect(Collectors.toList());
			String val = collect.stream().collect(Collectors.joining(","));
			studentInfo.put("majorNames", val);
		}
		return R.ok().put(Constant.R_DATA, studentInfo);
	}
}
