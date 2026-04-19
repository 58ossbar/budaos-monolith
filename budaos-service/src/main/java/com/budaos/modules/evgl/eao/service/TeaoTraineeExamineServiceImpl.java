package com.budaos.modules.evgl.eao.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.RegularUtils;
import com.budaos.modules.evgl.eao.api.TeaoTraineeExamineService;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamine;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExammember;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamroom;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamineMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExammemberMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamroomMapper;
import com.budaos.utils.constants.Constant;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 	考试管理
 */

@Service
public class TeaoTraineeExamineServiceImpl implements TeaoTraineeExamineService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TeaoTraineeExamineServiceImpl.class);
	
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;

	@Autowired
	private TeaoTraineeExamineMapper teaoTraineeExamineMapper;
	@Autowired
	private TeaoTraineeExamroomMapper teaoTraineeExamroomMapper;
	@Autowired
	private TeaoTraineeExammemberMapper teaoTraineeExammemberMapper;

	/**
	 * 	查询列表(返回List<Bean>)
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TeaoTraineeExamine> teaoTraineeExamineList = teaoTraineeExamineMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(teaoTraineeExamineList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(teaoTraineeExamineList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(teaoTraineeExamineList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 	查询列表(返回List<Map<String, Object>>)
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> teaoTraineeExamineList = teaoTraineeExamineMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(teaoTraineeExamineList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(teaoTraineeExamineList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param teaoTraineeExamine
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TeaoTraineeExamine teaoTraineeExamine) throws BudaosException {
		teaoTraineeExamine.setExamineId(Identities.uuid());
		teaoTraineeExamine.setCreateUserId(serviceLoginUtil.getLoginUserId());
		teaoTraineeExamine.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(teaoTraineeExamine);
		teaoTraineeExamineMapper.insert(teaoTraineeExamine);
		return R.ok();
	}
	/**
	 * 修改
	 * @param teaoTraineeExamine
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TeaoTraineeExamine teaoTraineeExamine) throws BudaosException {
	    teaoTraineeExamine.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    teaoTraineeExamine.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(teaoTraineeExamine);
		teaoTraineeExamineMapper.update(teaoTraineeExamine);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		teaoTraineeExamineMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		teaoTraineeExamineMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, teaoTraineeExamineMapper.selectObjectById(id));
	}

	/**
	 * 定时自动保存考核成绩
	 * @param ctId
	 * @param erId
	 * @param traineeIds
	 * @param regularIds
	 * @param regularSjnums
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	public R preExam(String ctId, String erId, String traineeIds, String regularIds, String regularSjnums,
			String loginUserId, String traineeName) {
		// 校验前台传过来的参数
		TeaoTraineeExamroom room = teaoTraineeExamroomMapper.selectObjectById(erId);
		if (room == null || StrUtils.isEmpty(traineeIds) || StrUtils.isEmpty(regularIds) || StrUtils.isEmpty(regularSjnums)) {
			return R.error("参数错误");
		}
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验当前提交人是否有考核资格
		map.put("erId", room.getErId());
		map.put("traineeId", loginUserId);
		List<TeaoTraineeExammember> list = teaoTraineeExammemberMapper.selectListByMap(map);
		if (list.size() == 0) {
			return R.error("无权限");
		}
		map.clear();
		map.put("traineeId", loginUserId);
		map.put("subjectId", room.getSubjectId());
		map.put("erId", room.getErId());
		map.put("state", "Y");
		if (teaoTraineeExammemberMapper.selectListByMap(map).size() > 0) {
			return R.error("已提交");
		}
		// 将提交的考核成绩入库
		String[] traineeId = traineeIds.split(",");
		String[] regularId = regularIds.split(",");
		String[] regularSjnum = regularSjnums.split(",");
		String type = list.get(0).getTraineeType();
		map.clear();
		for (int i = 0; i < traineeId.length; i++) {
			// TODO 校验前端用户传入的数值是否合法！
			if (!RegularUtils.isNumber(regularSjnum[i])) {
				throw new BudaosException(regularSjnum[i] + " 数据输入不合法，请自行检查调整！");
			} else {
				if (Integer.valueOf(regularSjnum[i]) > 100) {
					throw new BudaosException(regularSjnum[i] + "超出100分，请自行调整！");
				}
			}
			map.put("erId", room.getErId());
			map.put("subjectId", room.getSubjectId());
			map.put("traineeId", traineeId[i]);
			map.put("regularId", regularId[i]);
			map.put("createUserId", loginUserId);
			List<TeaoTraineeExamine> tempList = teaoTraineeExamineMapper.selectListByMap(map);
			if (tempList.size() == 0) {
				TeaoTraineeExamine exam = new TeaoTraineeExamine();
				exam.setRegularId(regularId[i]);
				exam.setTraineeId(traineeId[i]);
				exam.setRegularSjnum(new BigDecimal(regularSjnum[i]));
				exam.setSubjectId(room.getSubjectId());
				exam.setExamineTime(room.getErStime());
				exam.setCreateUserId(loginUserId);
				exam.setCreateUserName(traineeName);
				exam.setState("N");
				if ("2".equals(type) || "3".equals(type)) {
					exam.setExamineType("3");
				} else if ("4".equals(type)) {
					exam.setExamineType("4");
				} else if ("1".equals(type)) {
					if (loginUserId.equals(traineeId[i])) {
						exam.setExamineType("1");
					} else {
						exam.setExamineType("2");
					}
				} else {
					exam.setExamineType(type);
				}
				// 入库
				exam.setErId(erId);
				exam.setExamineId(Identities.uuid());
				exam.setCreateTime(DateUtils.getNowTimeStamp());
				teaoTraineeExamineMapper.insert(exam);
			} else {
				TeaoTraineeExamine exam = tempList.get(0);
				exam.setRegularSjnum(new BigDecimal(regularSjnum[i]));
				teaoTraineeExamineMapper.update(exam);
			}
		}
		return R.ok("临时保存成功");
	}

	/**
	 * 最终提交考核成绩
	 * @param ctId
	 * @param erId
	 * @param traineeIds
	 * @param regularIds
	 * @param regularSjnums
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	public R commit(String ctId, String erId, String traineeIds, String regularIds, String regularSjnums,
			String loginUserId, String traineeName) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(erId) || StrUtils.isEmpty(traineeIds)) {
			return R.error("必传参数为空");
		}
		// 校验前台传过来的参数
		TeaoTraineeExamroom room = teaoTraineeExamroomMapper.selectObjectById(erId);
		if(room == null || StrUtils.isEmpty(traineeIds) || StrUtils.isEmpty(regularIds) || StrUtils.isEmpty(regularSjnums)) {
			return R.error("参数错误");
		}
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验当前提交人是否有考核资格
		map.put("erId", room.getErId());
		map.put("traineeId", loginUserId);
		List<TeaoTraineeExammember> list = teaoTraineeExammemberMapper.selectListByMap(map);
		if (list.size() == 0) {
			return R.error("无权限");
		}
		// 标记该成员已提交考核成绩，防止多次重复提交
		map.put("traineeId", loginUserId);
		map.put("subjectId", room.getSubjectId());
		map.put("erId", room.getErId());
		map.put("state", "N");
		map.put("vstate", "Y");
		int count = teaoTraineeExammemberMapper.updateForState(map);
		if(count == 0) {
			return R.error("已提交");
		}
		// 将提交的考核成绩入库
		String[] traineeId = traineeIds.split(",");
		String[] regularId = regularIds.split(",");
		String[] regularSjnum = regularSjnums.split(",");
		String type = list.get(0).getTraineeType();
		map.clear();
		for (int i = 0; i < traineeId.length; i++) {
			// TODO 校验前端用户传入的数值是否合法！
			if (!RegularUtils.isNumber(regularSjnum[i])) {
				throw new BudaosException(regularSjnum[i] + " 数据输入不合法，请自行检查调整！");
			} else {
				if (Integer.valueOf(regularSjnum[i]) > 100) {
					throw new BudaosException(regularSjnum[i] + "超出100分，请自行调整！");
				}
			}
			map.put("erId", room.getErId());
			map.put("subjectId", room.getSubjectId());
			map.put("traineeId", traineeId[i]);
			map.put("regularId", regularId[i]);
			map.put("createUserId", loginUserId);
			List<TeaoTraineeExamine> tempList = teaoTraineeExamineMapper.selectListByMap(map);
			if (tempList.size() == 0) {
				TeaoTraineeExamine exam = new TeaoTraineeExamine();
				exam.setRegularId(regularId[i]);
				exam.setTraineeId(traineeId[i]);
				exam.setRegularSjnum(new BigDecimal(regularSjnum[i]));
				exam.setSubjectId(room.getSubjectId());
				exam.setExamineTime(room.getErStime());
				exam.setCreateUserId(loginUserId);
				exam.setCreateUserName(traineeName);
				exam.setState("Y");
				if ("2".equals(type) || "3".equals(type)) {
					exam.setExamineType("3");
				} else if("4".equals(type)) {
					exam.setExamineType("4");
				} else if("1".equals(type)) {
					if (loginUserId.equals(traineeId[i])) {
						exam.setExamineType("1");
					} else {
						exam.setExamineType("2");
					}
				} else {
					exam.setExamineType(type);
				}
				// 入库
				exam.setErId(erId);
				exam.setExamineId(Identities.uuid());
				exam.setCreateTime(DateUtils.getNowTimeStamp());
				teaoTraineeExamineMapper.insert(exam);
			} else {
				TeaoTraineeExamine exam = tempList.get(0);
				exam.setRegularSjnum(new BigDecimal(regularSjnum[i]));
				exam.setState("Y");
				teaoTraineeExamineMapper.update(exam);
			}
		}
		map.clear();
		map.put("erId", room.getErId());
		map.put("state", "N");
		List<TeaoTraineeExammember> members = teaoTraineeExammemberMapper.selectListByMap(map);
		// 代表全部已经提交
		if(members.size() == 0) {
			TeaoTraineeExamroom updRoom = new TeaoTraineeExamroom();
			updRoom.setErId(erId);
			// 考核结果(0未开始1正在考核2考核结束)(与活动实际状态一致)
			updRoom.setState("2");
			teaoTraineeExamroomMapper.update(updRoom);
		}
		return R.ok("提交成功");
	}
}
