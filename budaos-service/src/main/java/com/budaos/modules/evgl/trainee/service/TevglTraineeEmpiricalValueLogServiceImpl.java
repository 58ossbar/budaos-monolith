package com.budaos.modules.evgl.trainee.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.evgl.tch.domain.TevglTchRoomPereTraineeAnswer;
import com.budaos.modules.evgl.tch.persistence.TevglTchRoomPereTraineeAnswerMapper;
import com.budaos.modules.evgl.trainee.api.TevglTraineeEmpiricalValueLogService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeEmpiricalValueLog;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeEmpiricalValueLogMapper;
import com.budaos.utils.constants.Constant;
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
public class TevglTraineeEmpiricalValueLogServiceImpl implements TevglTraineeEmpiricalValueLogService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTraineeEmpiricalValueLogServiceImpl.class);
	@Autowired
	private TevglTraineeEmpiricalValueLogMapper tevglTraineeEmpiricalValueLogMapper;
	@Autowired
	private TevglTchRoomPereTraineeAnswerMapper tevglTchRoomPereTraineeAnswerMapper;
	
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
		List<TevglTraineeEmpiricalValueLog> tevglTraineeEmpiricalValueLogList = tevglTraineeEmpiricalValueLogMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTraineeEmpiricalValueLogList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglTraineeEmpiricalValueLogList = tevglTraineeEmpiricalValueLogMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTraineeEmpiricalValueLogList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTraineeEmpiricalValueLog
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglTraineeEmpiricalValueLog tevglTraineeEmpiricalValueLog) throws BudaosException {
		ValidatorUtils.check(tevglTraineeEmpiricalValueLog);
		tevglTraineeEmpiricalValueLog.setEvId(Identities.uuid());
		tevglTraineeEmpiricalValueLogMapper.insert(tevglTraineeEmpiricalValueLog);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTraineeEmpiricalValueLog
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTraineeEmpiricalValueLog tevglTraineeEmpiricalValueLog) throws BudaosException {
	    ValidatorUtils.check(tevglTraineeEmpiricalValueLog);
		tevglTraineeEmpiricalValueLogMapper.update(tevglTraineeEmpiricalValueLog);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglTraineeEmpiricalValueLogMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglTraineeEmpiricalValueLogMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglTraineeEmpiricalValueLogMapper.selectObjectById(id));
	}
	
	/**
	 * 查看经验值
	 * @param ctId
	 * @param traineeId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewEmpiricalValueLogs(String ctId, String traineeId, String loginUserId, Integer pageNum, Integer pageSize) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pageNum", pageNum = pageNum == null ? 1 : pageNum);
		params.put("pageSize", pageSize = pageSize == null ? 10 : pageSize);
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		params.put("state", "Y");
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String,Object>> list = tevglTraineeEmpiricalValueLogMapper.selectSimpleListMapByMap(query);
		list.stream().forEach(empiricalValueLog -> {
			if (StrUtils.isNull(empiricalValueLog.get("msg"))) {
				String msg = "参与课堂【"+empiricalValueLog.get("name")+"】";
				if (!StrUtils.isNull(empiricalValueLog.get("activtityTitle"))) {
					String name = handlesignName(empiricalValueLog);
					msg += "的活动["+empiricalValueLog.get("activtityTitle")+"]，" + name + "获得 " + empiricalValueLog.get("empiricalValue") +" 经验值";
				}
				empiricalValueLog.put("msg", msg);	
			}
		});
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	private String handlesignName(Map<String,Object> empiricalValueLog) {
		if (StrUtils.isNull(empiricalValueLog.get("type"))) {
			return "";
		}
		String type = empiricalValueLog.get("type").toString();
		if (!GlobalActivity.ACTIVITY_8_SIGININ_INFO.equals(type)) {
			return "";
		}
		if (StrUtils.isNull(empiricalValueLog.get("params2"))) {
			return "";
		}
		String signState = empiricalValueLog.get("params2").toString();
		String str = "";
		// 签到状态1正常2迟到3早退4旷课5请假
		switch (signState) {
		case "1":
			str = "【正常】";
			break;
		case "2":
			str = "【迟到】";		
			break;
		case "3":
			str = "【早退】";
			break;
		case "4":
			str = "【旷课】";
			break;
		case "5":
			str = "【请假】";
			break;
		default:
			break;
		}
		return str;
	}

	/**
	 * 查看某学员的当前课堂的经验值、热心解答次数、获取点赞数、课堂表现次数、视频学习个数
	 * @param ctId
	 * @param traineeId
	 * @return
	 */
	@Override
	public R viewNums(String ctId, String traineeId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		// 最总返回数据
		Map<String, Object> data = new HashMap<>();
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("traineeId", traineeId);
		// 当前学员课堂获取到的总经验值
		Integer currRoomEmpiricalValue = tevglTraineeEmpiricalValueLogMapper.sumEmpiricalValueByMap(params);
		// 热心解答次数（暂定取评论条数）
		Integer commentNum = 0;
		// 获取点赞数
		Integer likeNums = 0;
		// 课堂表现
		Map<String, Object> performanceData = getPerformanceData(ctId, traineeId);
		// 视频学习
		Map<String, Object> videoStudyData = new HashMap<String, Object>();
		videoStudyData.put("num", 0);
		videoStudyData.put("minute", "1.024");
		// 返回
		data.put("currRoomEmpiricalValue", currRoomEmpiricalValue);
		data.put("commentNum", commentNum);
		data.put("likeNums", likeNums);
		data.put("performanceData", performanceData);
		data.put("videoStudyData", videoStudyData);
		return R.ok();
	}
	
	/**
	 * 获取某学员某课堂中的课堂表现次数，和得到的对应的总分（经验值）
	 * @param ctId
	 * @param traineeId
	 * @return {"num":"", "score":""}
	 */
	private Map<String, Object> getPerformanceData(String ctId, String traineeId) {
		Map<String, Object> performanceData = new HashMap<String, Object>();
		performanceData.put("ctId", ctId);
		performanceData.put("traineeId", traineeId);
		List<TevglTchRoomPereTraineeAnswer> list = tevglTchRoomPereTraineeAnswerMapper.selectListByMap(performanceData);
		int score = 0;
		if (list != null && list.size() > 0) {
			for (TevglTchRoomPereTraineeAnswer tevglTchRoomPereTraineeAnswer : list) {
				score += tevglTchRoomPereTraineeAnswer.getEmpiricalValue();
			}
		}
		performanceData.clear();
		performanceData.put("num", list == null ? 0 : list.size());
		performanceData.put("score", score);
		return performanceData;
	}
}
