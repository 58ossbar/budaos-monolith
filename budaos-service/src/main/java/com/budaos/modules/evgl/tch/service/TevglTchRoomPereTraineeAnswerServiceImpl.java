package com.budaos.modules.evgl.tch.service;

import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.tch.api.TevglTchRoomPereTraineeAnswerService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchRoomPereAnswer;
import com.budaos.modules.evgl.tch.domain.TevglTchRoomPereTraineeAnswer;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchRoomPereAnswerMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchRoomPereTraineeAnswerMapper;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.util.ArrayList;
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
public class TevglTchRoomPereTraineeAnswerServiceImpl implements TevglTchRoomPereTraineeAnswerService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchRoomPereTraineeAnswerServiceImpl.class);
	@Autowired
	private TevglTchRoomPereTraineeAnswerMapper tevglTchRoomPereTraineeAnswerMapper;
	@Autowired
	private TevglTchRoomPereAnswerMapper tevglTchRoomPereAnswerMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private RedissonClient redisson;
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param > params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchRoomPereTraineeAnswer> tevglTchRoomPereTraineeAnswerList = tevglTchRoomPereTraineeAnswerMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglTchRoomPereTraineeAnswerList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglTchRoomPereTraineeAnswerList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglTchRoomPereTraineeAnswerList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param > params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchRoomPereTraineeAnswerList = tevglTchRoomPereTraineeAnswerMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglTchRoomPereTraineeAnswerList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglTchRoomPereTraineeAnswerList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchRoomPereTraineeAnswer
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglTchRoomPereTraineeAnswer tevglTchRoomPereTraineeAnswer) throws BudaosException {
		tevglTchRoomPereTraineeAnswer.setTraineeAnswerId(Identities.uuid());
		tevglTchRoomPereTraineeAnswer.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglTchRoomPereTraineeAnswer.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglTchRoomPereTraineeAnswer);
		tevglTchRoomPereTraineeAnswerMapper.insert(tevglTchRoomPereTraineeAnswer);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchRoomPereTraineeAnswer
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTchRoomPereTraineeAnswer tevglTchRoomPereTraineeAnswer) throws BudaosException {
	    tevglTchRoomPereTraineeAnswer.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglTchRoomPereTraineeAnswer.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglTchRoomPereTraineeAnswer);
		tevglTchRoomPereTraineeAnswerMapper.update(tevglTchRoomPereTraineeAnswer);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglTchRoomPereTraineeAnswerMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglTchRoomPereTraineeAnswerMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglTchRoomPereTraineeAnswerMapper.selectObjectById(id));
	}

	/**
	 * 【学生端】学员抢答
	 * @author zhouyl加
	 * @date 2020年12月24日
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	@Override
	@Transactional
	public R traineeAnswer(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if ("1".equals(classroom.getClassroomState())) {
			return R.error("课堂尚未开始，无法抢答");
		}
		if ("3".equals(classroom.getClassroomState())) {
			return R.error("课堂已结束，抢答活动无效");
		}
		// 根据抢答活动id查询抢答信息，得到抢答人数，然后循环添加学员抢答信息
		TevglTchRoomPereAnswer pereAnswer = tevglTchRoomPereAnswerMapper.selectObjectById(activityId);
		if (pereAnswer == null) {
			return R.error("活动已不存在，抢答失败~");
		}
		if (pereAnswer.getType() == null || !pereAnswer.getType().equals(1)) {
			return R.error("该活动不是抢答活动！");
		}
		// 查询课堂成员
		List<String> traineeIdList = tevglTchClassroomTraineeMapper.findTraineeIdByCtId(ctId);
		if (!traineeIdList.contains(loginUserId)) {
			return R.error("不是该课堂成员，无法参与抢答！");
		}
		boolean flag1 = false;
		boolean ifSuccess = false; // 标识是否抢答成功
		// 定义锁
		String key = "课堂表现活动" + ctId + "_" + activityId;
		RLock lock = redisson.getLock(key);
		lock.lock();
		try {
			// 定义当前还能允许多少人抢答成功
			Integer num = 0;
			String s = redisTemplate.opsForValue().get(getMyRedisKey(ctId, activityId));
			if (StrUtils.isEmpty(s)) {
				List<String> selectAnswerNums = tevglTchRoomPereTraineeAnswerMapper.selectTraineeIdList(ctId, activityId);
				Integer currentSuccessNum = (selectAnswerNums == null || selectAnswerNums.size() == 0) ? 0 : selectAnswerNums.size();
				redisTemplate.opsForValue().set(getMyRedisKey(ctId, activityId), String.valueOf(pereAnswer.getAnswerNum() - currentSuccessNum));
			} else {
				num = Integer.valueOf(s);
			}
			if (num > 0) {
				Integer val = tevglTchRoomPereTraineeAnswerMapper.countTraineeAnswer(ctId, activityId, loginUserId);
				if (val == null || val <= 0) {
					// 填充信息
					TevglTchRoomPereTraineeAnswer traineeAnswer = traineeAnswerInfo(ctId, activityId, loginUserId);
					tevglTchRoomPereTraineeAnswerMapper.insert(traineeAnswer);
					// 抢答成功之后，减库存
					redisTemplate.opsForValue().set(getMyRedisKey(ctId, activityId), String.valueOf(num - 1));
					ifSuccess = true;
					log.debug("{}用户抢答成功", loginUserId);
				} else {
					flag1 = true;
				}
			}
		} finally {
			lock.unlock();
		}
		if (flag1) {
			return R.ok("已经抢答成功了");
		}
		// 再次查询该活动下的抢答人数
		List<String> traineeIds = tevglTchRoomPereTraineeAnswerMapper.selectTraineeIdList(ctId, activityId);
		// 获取抢答成功的学生信息
		List<Map<String, Object>> traineeList = getTraineeList(ctId, activityId, traineeIds);
		// 长连接推送数据实施渲染学员
		if (ifSuccess) {
			sendIm(classroom, activityId, traineeList);
		}
		return R.ok("抢答人数已达上限").put(Constant.R_DATA, traineeList);
	}
	
	/**
	 * 抢答活动中的key
	 * @param ctId
	 * @param activityId
	 * @return 示例结果：课堂表现可抢答人数_c1e0397b5737483c8915b958f81b46c9_263148468eda4619ba4763be58ccf2e5
	 */
	private String getMyRedisKey(String ctId, String activityId) {
		ctId = StrUtils.isEmpty(ctId) ? "课堂id" : ctId;
		activityId = StrUtils.isEmpty(activityId) ? "课堂id" : activityId;
		return "课堂表现可抢答人数_" + ctId + "_" + activityId;
	}
	
	private void sendIm(TevglTchClassroom tevglTchClassroom, String activityId, List<Map<String, Object>> traineeList) {
		if (tevglTchClassroom == null || StrUtils.isEmpty(activityId) || traineeList == null || traineeList.size() == 0) {
			return;
		}
		String ctId = tevglTchClassroom.getCtId();
		// 获取tioconfig
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		JSONObject msg = new JSONObject();
		msg.put("busitype", "fastanswertrainee");
		msg.put("msgtype", "other");
		msg.put("ct_id", ctId);
		msg.put("ctId", ctId);
		msg.put("activity_id", activityId);
		msg.put("activityId", activityId);
		msg.put(Const.BUSI_MSG, traineeList);
		traineeList.stream().forEach(trainee -> {
			// 【学生端】显示老师发布的活动弹框
			CBImUtils.sendToUser(tioConfig, trainee.get("traineeId").toString(), msg);
		});
		// 【教师端】显示哪些学生参与了活动(实时显示)
		CBImUtils.sendToUser(tioConfig, tevglTchClassroom.getCreateUserId(), msg);
	}

	/**
	 * 根据条件查询课堂成员信息
	 * @param ctId
	 * @param activityId
	 * @param traineeIds
	 * @return
	 */
	private List<Map<String, Object>> getTraineeList(String ctId, String activityId, List<String> traineeIds) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || traineeIds == null || traineeIds.size() == 0) {
			return new ArrayList<>();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("traineeIds", traineeIds);
		List<Map<String, Object>> traineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		traineeList.stream().forEach(a -> {
			// 头像处理
			String traineePic = (String) a.get("traineePic");
			if (StrUtils.isNotEmpty(traineePic) && !"null".equals(traineePic)) {
				a.put("traineePic", uploadPathUtils.stitchingPath(traineePic, "16"));
			}
			a.put("ct_id", ctId);
			a.put("ctId", ctId);
			a.put("activity_id", activityId);
			a.put("activityId", activityId);
		});
		return traineeList;
	}
	
	/**
	 * 填充基本信息
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	public TevglTchRoomPereTraineeAnswer traineeAnswerInfo(String ctId, String activityId, String loginUserId) {
		TevglTchRoomPereTraineeAnswer traineeAnswer = new TevglTchRoomPereTraineeAnswer();
		traineeAnswer.setTraineeAnswerId(Identities.uuid());
		traineeAnswer.setCtId(ctId);
		traineeAnswer.setTraineeId(loginUserId);
		traineeAnswer.setAnserId(activityId);
		traineeAnswer.setTraineeEmpiricalValue(0);
		traineeAnswer.setCreateUserId(loginUserId);
		traineeAnswer.setCreateTime(DateUtils.getNowTimeStamp());
		return traineeAnswer;
	}

	/**
	 * 查询抢答成功的学员们
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R querySuccessfulTraineeList(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchRoomPereAnswer tevglTchRoomPereAnswer = tevglTchRoomPereAnswerMapper.selectObjectById(activityId);
		if (tevglTchRoomPereAnswer == null) {
			return R.error("无效的活动");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("activityId", activityId);
		List<TevglTchRoomPereTraineeAnswer> list = tevglTchRoomPereTraineeAnswerMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			return R.ok().put(Constant.R_DATA, list).put("num", tevglTchRoomPereAnswer.getAnswerNum());
		}
		List<String> traineeIds = list.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		params.clear();
		params.put("traineeIds", traineeIds);
		List<Map<String, Object>> traineeInfoList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		traineeInfoList.stream().forEach(traineeInfo -> {
			traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic"), "16"));
		});
		return R.ok("本次抢答成功的成员为").put(Constant.R_DATA, traineeInfoList).put("num", tevglTchRoomPereAnswer.getAnswerNum());
	}
}
