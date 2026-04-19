package com.budaos.modules.job;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 每隔5分钟扫描一遍，结束活动，开始活动那边，控制活动自动结束时间不得短于5分钟
 * @author admin
 *
 */
@Component("autoEndActivityJob")
public class AutoEndActivityJob {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	
	
	public void doJob(String params) {
		
		log.info("========================================自动结束活动 begin ============================================================" + params);
		// 查询进行中的课堂
		Map<String, Object> map = new HashMap<>();
		map.put("classroomState", "2");
		map.put("state", "Y");
		List<Map<String, Object>> classroomList = tevglTchClassroomMapper.selectCtIdPkgIdList(map);
		if (classroomList == null || classroomList.size() == 0) {
			return;
		}
		// 查询设置了自动结束活动的活动
		List<Object> pkgIds = classroomList.stream().map(a -> a.get("pkgId")).collect(Collectors.toList());
		map.put("pkgIds", pkgIds); 
		map.put("activityState", "1"); // 实际活动状态(0未开始1进行中2已结束)
		map.put("activityEndTimeIsNotNull", "Y");
		List<Map<String, Object>> activityList = tevglPkgActivityRelationMapper.selectListMapByMap(map);
		if (activityList == null || activityList.size() == 0) {
			return;
		}
		Date nowDate = DateUtils.getNowDate();
		// 等待入库更新的
		List<Object> paIds = new ArrayList<Object>();
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		Map<Object, Object> data = classroomList.stream().collect(Collectors.toMap(a -> a.get("ctId"), a -> a.get("pkgId")));
		// TODO 长连接推送消息，告知对应的课堂成员，活动结束了
		log.debug("等待结束的活动：" + activityList.size());
		List<Object> ctIds = classroomList.stream().map(a -> a.get("ctId")).collect(Collectors.toList());
		map.clear();
		map.put("ctIds", ctIds);
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(map);
		if (classroomTraineeList != null && classroomTraineeList.size() > 0) {
			for (Entry<Object, Object> cb : data.entrySet()) {
				Object ctId = cb.getKey();
				Object pkgId = cb.getValue();
				// 取出课堂对应的成员
				List<TevglTchClassroomTrainee> thisClassroomTraineeList = classroomTraineeList.stream().filter(a -> a.getCtId().equals(ctId)).collect(Collectors.toList());
				// 取出课堂教学包对应的活动
				List<Map<String, Object>> thisClassroomActivityList = activityList.stream().filter(a -> a.get("pkg_id").equals(pkgId)).collect(Collectors.toList());
				for (TevglTchClassroomTrainee traineeInfo : thisClassroomTraineeList) {
					for (Map<String, Object> activityInfo : thisClassroomActivityList) {
						Object paId = activityInfo.get("pa_id");
						Object activityTitle = activityInfo.get("activity_title");
						Object activityId = activityInfo.get("activity_id");
						Object activityType = activityInfo.get("activity_type");
						Object activityEndTime = activityInfo.get("activity_end_time");
						if (!StrUtils.isNull(activityEndTime)) {
							// 如果时间已到
							Date endDate = DateUtils.parse(activityEndTime.toString(), "yyyy-MM-dd HH:mm:ss");
							if (endDate.before(nowDate)) {
								// 加入集合
								paIds.add(paId);
								// 即时通讯
								JSONObject msg = new JSONObject();
								msg.put("busitype", "reloadactlist"); // 结束活动后，要求重新加载下活动列表接口
								msg.put("msgtype", "alert");
								JSONObject busitype = new JSONObject();
								busitype.put("title", StrUtils.isNull(activityTitle) ? "活动结束" : "【"+activityTitle+"】活动结束了");
								busitype.put("ctId", ctId);
								busitype.put("ct_id", ctId);
								busitype.put("activityId", activityId);
								busitype.put("activityType", activityType);
								msg.put("msg", busitype);
								log.debug("长连接推送活动"+activityId+"给学员["+traineeInfo.getTraineeId()+"]");
								CBImUtils.sendToUser(tioConfig, traineeInfo.getTraineeId(), msg);
							}
						}
					}
				}
			}
		}
		log.debug("等待更新的：" + paIds.size());
		if (paIds != null && paIds.size() > 0) {
			tevglPkgActivityRelationMapper.updateActivityStateBatch(paIds);
		}
		log.info("========================================自动结束活动 end ============================================================");
	}
	
}
