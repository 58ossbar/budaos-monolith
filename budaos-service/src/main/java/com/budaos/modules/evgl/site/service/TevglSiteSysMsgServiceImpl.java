package com.budaos.modules.evgl.site.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.evgl.activity.domain.*;
import com.budaos.modules.evgl.activity.persistence.*;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExammember;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExamroom;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExammemberMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamroomMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicPaper;
import com.budaos.modules.evgl.question.domain.TepExaminePaperInfo;
import com.budaos.modules.evgl.question.persistence.TepExamineDynamicPaperMapper;
import com.budaos.modules.evgl.question.persistence.TepExaminePaperInfoMapper;
import com.budaos.modules.evgl.site.api.TevglSiteSysMsgService;
import com.budaos.modules.evgl.site.domain.TevglSiteSysMsg;
import com.budaos.modules.evgl.site.persistence.TevglSiteSysMsgMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchRoomPereAnswer;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchRoomPereAnswerMapper;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.im.utils.CBImTipsUtils;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

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

public class TevglSiteSysMsgServiceImpl implements TevglSiteSysMsgService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteSysMsgServiceImpl.class);
	@Autowired
	private TevglSiteSysMsgMapper tevglSiteSysMsgMapper;
	// 访问地址
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private TevglActivityVoteQuestionnaireMapper tevglActivityVoteQuestionnaireMapper;
	@Autowired
	private TevglActivityBrainstormingMapper tevglActivityBrainstormingMapper;
	@Autowired
	private TevglActivityAnswerDiscussMapper tevglActivityAnswerDiscussMapper;
	@Autowired
	private TevglActivitySigninInfoMapper tevglActivitySigninInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private TepExaminePaperInfoMapper tepExaminePaperInfoMapper;
	@Autowired
	private TepExamineDynamicPaperMapper tepExamineDynamicPaperMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerMapper tevglActivityVoteQuestionnaireTraineeAnswerMapper;
	@Autowired
	private TevglActivityBrainstormingTraineeAnswerMapper tevglActivityBrainstormingTraineeAnswerMapper;
	@Autowired
	private TevglActivitySigninTraineeMapper tevglActivitySigninTraineeMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglTchRoomPereAnswerMapper tevglTchRoomPereAnswerMapper;
	@Autowired
	private TeaoTraineeExamroomMapper teaoTraineeExamroomMapper;
	@Autowired
	private TeaoTraineeExammemberMapper teaoTraineeExammemberMapper;
	
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
		List<TevglSiteSysMsg> tevglSiteSysMsgList = tevglSiteSysMsgMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteSysMsgList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteSysMsgList = tevglSiteSysMsgMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteSysMsgList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteSysMsg
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglSiteSysMsg tevglSiteSysMsg) throws BudaosException {
		tevglSiteSysMsg.setMsgId(Identities.uuid());
		ValidatorUtils.check(tevglSiteSysMsg);
		tevglSiteSysMsgMapper.insert(tevglSiteSysMsg);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteSysMsg
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglSiteSysMsg tevglSiteSysMsg) throws BudaosException {
	    ValidatorUtils.check(tevglSiteSysMsg);
		tevglSiteSysMsgMapper.update(tevglSiteSysMsg);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteSysMsgMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglSiteSysMsgMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteSysMsgMapper.selectObjectById(id));
	}

	/**
	 * 保存
	 * @param tevglSiteSysMsg
	 * @return
	 */
	@Override
	public R saveMsg(TevglSiteSysMsg tevglSiteSysMsg) throws BudaosException {
		if (StrUtils.isEmpty(tevglSiteSysMsg.getMsgId())) {
			tevglSiteSysMsg.setMsgId(Identities.uuid());
		}
		if (StrUtils.isEmpty(tevglSiteSysMsg.getMsgPic())) {
			tevglSiteSysMsg.setMsgPic(budaosFieAccessPath + "/" + "logo.png");
		}
		tevglSiteSysMsg.setReadState("N"); // Y已读N未读
		tevglSiteSysMsg.setState("Y");
		tevglSiteSysMsg.setCreateTime(DateUtils.getNowTimeStamp());
		tevglSiteSysMsgMapper.insert(tevglSiteSysMsg);
		return R.ok();
	}

	/**
	 * 查询发送给自己的系统消息
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryMyMsg(Map<String, Object> params, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		params.put("state", "Y");
		params.put("toTraineeId", loginUserId);
		params.put("sidx", "t1.create_time");
		params.put("order", "desc");
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String,Object>> list = tevglSiteSysMsgMapper.selectListMapByMap(query);
		// 更新未读已读
		List<String> msgIds = list.stream().filter(a -> a.get("readState").equals("N")).map(a -> a.get("msgId").toString()).collect(Collectors.toList());
		if (msgIds != null && msgIds.size() > 0) {
			msgIds.stream().forEach(msgId -> {
				TevglSiteSysMsg t = new TevglSiteSysMsg();
				t.setMsgId(msgId);
				t.setReadState("Y");
				tevglSiteSysMsgMapper.update(t);
			});
		}
		// 查询条件
		Map<String, Object> map = new HashMap<>();
		// 处理不同类型的需要返回的数据
		list.stream().forEach(msg -> {
			Object ctId = msg.get("params1");
			Object pkgId = msg.get("pkgId");
			Object activityId = msg.get("params2");
			msg.put("ctId", ctId);
			switch (msg.get("msgType").toString()) {
				case "0":
					
					break;
				case "01":
					
					break;
				// 投票问卷
				case "1":
					msg.put("activityInfo", handleActivityInfo1(ctId, pkgId, activityId, loginUserId, map));
					break;
				// 头脑风暴	
				case "2":
					msg.put("activityInfo", handleActivityInfo2(ctId, pkgId, activityId, loginUserId, map));
					break;
				// 答疑讨论	
				case "3":
					msg.put("activityInfo", handleActivityInfo3(ctId, pkgId, activityId, loginUserId, map));
					break;
				// 测试活动	
				case "4":
					msg.put("activityInfo", handleActivityInfo4(ctId, pkgId, activityId, loginUserId, map));
					break;
				// 作业小组任务
				case "5":
					msg.put("activityInfo", handleActivityInfo5(ctId, pkgId, activityId, loginUserId, map));
					break;
				case "6":
					msg.put("activityInfo", handleActivityInfo6(ctId, pkgId, activityId, loginUserId, map));
					break;	
				// 签到活动
				case "8":
					msg.put("activityInfo", handleActivityInfo8(ctId, pkgId, activityId, loginUserId, map));
					break;	
				// 实践考核
				case "9":
					msg.put("activityInfo", handleActivityInfo9(ctId, pkgId, activityId, loginUserId, map));
					break;	
				default:
					break;
			}
		});
		PageUtils pageUtil = new PageUtils(list,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 发送通知
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R sendText(JSONObject jsonObject, String loginUserId) throws BudaosException {
		String ctId = jsonObject.getString("ctId");
		String title = jsonObject.getString("title");
		String content = jsonObject.getString("content");
		JSONArray traineeIds = jsonObject.getJSONArray("traineeIds");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(title) || StrUtils.isEmpty(content)) {
			return R.error("必传参数为空");
		}
		title = title.trim();
		content = content.trim();
		if (StrUtils.isEmpty(title)) {
			return R.error("请输入标题");
		}
		if (StrUtils.isEmpty(content)) {
			return R.error("请输入内容");
		}
		if (content.length() > 200) {
			return R.error("标题过长，不能超过200个字符");
		}
		if (content.length() > 500) {
			return R.error("内容过长，不能超过500个字符");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("发送失败");
		}
		if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
			return R.error("非法操作");
		}
		ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
		JSONObject msg = CBImTipsUtils.alert(title, content, "alert");
		for (int i = 0; i < traineeIds.size(); i++) {
			String userid = traineeIds.getString(i);
			CBImUtils.sendToUser(tioConfig, userid, msg);
			TevglSiteSysMsg t = new TevglSiteSysMsg();
			t.setMsgId(Identities.uuid());
			t.setToTraineeId(userid);
			t.setMsgTitle(title);
			t.setMsgContent(content);
			t.setMsgType("0"); // 消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到
			t.setReadState("N");
			t.setParams1(ctId);
			t.setCreateUserId(loginUserId);
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setState("Y");
			tevglSiteSysMsgMapper.insert(t);
		}
		// 给自己也插入一条记录
		TevglSiteSysMsg t = new TevglSiteSysMsg();
		t.setMsgId(Identities.uuid());
		t.setToTraineeId(loginUserId);
		t.setMsgTitle(title);
		t.setMsgContent(content);
		t.setMsgType("0"); // 消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到
		t.setReadState("N");
		t.setParams1(ctId);
		t.setCreateUserId(loginUserId);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setState("Y");
		tevglSiteSysMsgMapper.insert(t);
		return R.ok("发送成功");
	}
	
	/**
	 * 处理需要额外返回的数据（投票问卷）
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @param map 一个作为查询条件的map对象
	 * @return
	 */
	private Map<String, Object> handleActivityInfo1(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		// 兼容下旧数据
		if (StrUtils.isNull(pkgId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
			if (tevglTchClassroom != null) {
				pkgId = tevglTchClassroom.getPkgId();
			}
		}
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		TevglActivityVoteQuestionnaire vo = tevglActivityVoteQuestionnaireMapper.selectObjectByIdAndPkgId(activityInfo);
        if (vo != null) {
            activityInfo.put("activityId", vo.getActivityId());
            activityInfo.put("activityTitle", vo.getActivityTitle());
            activityInfo.put("activityType", vo.getActivityType());
            activityInfo.put("activityState", vo.getActivityStateActual());
            activityInfo.put("activityBeginTime", vo.getActivityBeginTime());
            activityInfo.put("activityEndTime", vo.getActivityEndTime());
            activityInfo.put("activityPic", vo.getActivityPic());
            activityInfo.put("content", "");
            activityInfo.put("signType", "");
            // 学员的作答内容 [投票/问卷]
            map.clear();
            map.put("traineeId", loginUserId);
            map.put("activityId", vo.getActivityId());
            map.put("ctId", ctId);
            List<TevglActivityVoteQuestionnaireTraineeAnswer> voteQuestionnaireTraineeAnswerList = tevglActivityVoteQuestionnaireTraineeAnswerMapper.selectListByMap(map);
            activityInfo.put(GlobalActivity.HAS_BEEN_DONE, false);
            if (voteQuestionnaireTraineeAnswerList != null && voteQuestionnaireTraineeAnswerList.size() > 0) {
                activityInfo.put(GlobalActivity.HAS_BEEN_DONE, true);
            }
        }
		return activityInfo;
	}
	
	/**
	 * 处理需要额外返回的数据（头脑风暴）
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @param map 一个作为查询条件的map对象
	 * @return
	 */
	private Map<String, Object> handleActivityInfo2(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		// 兼容下旧数据
		if (StrUtils.isNull(pkgId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
			if (tevglTchClassroom != null) {
				pkgId = tevglTchClassroom.getPkgId();
			}
		}
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		TevglActivityBrainstorming b = tevglActivityBrainstormingMapper.selectObjectByIdAndPkgId(activityInfo);
        if (b != null) {
            activityInfo.put("activityId", b.getActivityId());
            activityInfo.put("activityTitle", b.getActivityTitle());
            activityInfo.put("activityType", b.getActivityType());
            activityInfo.put("activityState", b.getActivityStateActual());
            activityInfo.put("activityBeginTime", b.getActivityBeginTime());
            activityInfo.put("activityEndTime", b.getActivityEndTime());
            activityInfo.put("activityPic", b.getActivityPic());
            activityInfo.put("content", b.getContent());
            activityInfo.put("signType", "");
            // 学员的作答内容 [头脑风暴]
            map.clear();
            map.put("traineeId", loginUserId);
            map.put("activityId", b.getActivityId());
            map.put("ctId", ctId);
            List<TevglActivityBrainstormingTraineeAnswer> traineeAnswerBrainstormingList = tevglActivityBrainstormingTraineeAnswerMapper.selectListByMap(map);
            activityInfo.put(GlobalActivity.HAS_BEEN_DONE, false);
            if (traineeAnswerBrainstormingList != null && traineeAnswerBrainstormingList.size() > 0) {
                activityInfo.put(GlobalActivity.HAS_BEEN_DONE, true);
            }
        }
		return activityInfo;
	}
	
	/**
	 * 处理需要额外返回的数据（答疑讨论）
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @param map 一个作为查询条件的map对象
	 * @return
	 */
	private Map<String, Object> handleActivityInfo3(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		// 兼容下旧数据
		if (StrUtils.isNull(pkgId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
			if (tevglTchClassroom != null) {
				pkgId = tevglTchClassroom.getPkgId();
			}
		}
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		TevglActivityAnswerDiscuss ad = tevglActivityAnswerDiscussMapper.selectObjectByIdAndPkgId(activityInfo);
        if (ad != null) {
            activityInfo.put("activityId", ad.getActivityId());
            activityInfo.put("activityTitle", ad.getActivityTitle());
            activityInfo.put("activityType", ad.getActivityType());
            activityInfo.put("activityState", ad.getActivityState());
            activityInfo.put("activityPic", ad.getActivityPic());
            activityInfo.put("content", ad.getContent());
            activityInfo.put("signType", "");
        }
		return activityInfo;
	}
	
	/**
	 * 处理需要额外返回的数据（测试活动）
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @param map 一个作为查询条件的map对象
	 * @return
	 */
	private Map<String, Object> handleActivityInfo4(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		// 兼容下旧数据
		if (StrUtils.isNull(pkgId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
			if (tevglTchClassroom != null) {
				pkgId = tevglTchClassroom.getPkgId();
			}
		}
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		TepExaminePaperInfo act = tepExaminePaperInfoMapper.selectObjectByIdAndPkgId(activityInfo);
        if (act != null) {
            activityInfo.put("activityId", act.getPaperId());
            activityInfo.put("activityTitle", act.getPaperName());
            activityInfo.put("activityType", "4");
            activityInfo.put("activityState", act.getActivityStateActual());
            activityInfo.put("activityBeginTime", act.getActivityBeginTime());
            activityInfo.put("activityEndTime", act.getActivityEndTime());
            activityInfo.put("activityPic", null);
            activityInfo.put("content", "");
            activityInfo.put("signType", "");
            // 查看答案时机
            activityInfo.put("viewResultTime", act.getViewResultTime());
            // 能重做几次
            activityInfo.put("redoNum", act.getRedoNum());
            // 做过几次
            long hasBeenDoenTimes = 0;
            // 测试活动作答记录
            map.clear();
            map.put("traineeId", loginUserId);
            map.put("paperId", act.getPaperId());
            List<TepExamineDynamicPaper> dynamicPaperList = tepExamineDynamicPaperMapper.selectListByMap(map);
            // 做过几次
            if (dynamicPaperList != null && dynamicPaperList.size() > 0) {
                hasBeenDoenTimes = dynamicPaperList.stream().filter(a -> "Y".equals(a.getPaperIsFinished()) && a.getTraineeId().equals(loginUserId) && a.getPaperId().equals(activityInfo.get("activityId"))).count();
                activityInfo.put("hasBeenDoenTimes", hasBeenDoenTimes);
                activityInfo.put(GlobalActivity.HAS_BEEN_DONE, hasBeenDoenTimes > 0 ? true : false);
            }
            activityInfo.put("hasBeenDoenTimes", hasBeenDoenTimes);
        }
		return activityInfo;
	}
	
	/**
	 * 处理需要额外返回的数据（作业小组任务）
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @param map 一个作为查询条件的map对象
	 * @return
	 */
	private Map<String, Object> handleActivityInfo5(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		// 兼容下旧数据
		if (StrUtils.isNull(pkgId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
			if (tevglTchClassroom != null) {
				pkgId = tevglTchClassroom.getPkgId();
			}
		}
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		return activityInfo;
	}
	
	/**
	 * 处理需要额外返回的数据（课堂表现）
	 * @param ctId
	 * @param pkgId
	 * @param activityId
	 * @param loginUserId
	 * @param map
	 * @return
	 */
	private Map<String, Object> handleActivityInfo6(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		TevglTchRoomPereAnswer s = tevglTchRoomPereAnswerMapper.selectObjectByIdAndPkgId(activityInfo);
		if (s != null) {
            activityInfo.put("activityId", s.getAnswerId());
            activityInfo.put("activityTitle", s.getAnswerTitle());
            activityInfo.put("answerNum", s.getAnswerNum());
            activityInfo.put("activityType", GlobalActivity.ACTIVITY_6_CLASSROOM_PERFORMANCE);
            activityInfo.put("activityState", s.getActivityStateActual());
            activityInfo.put("activityBeginTime", s.getActivityBeginTime());
            activityInfo.put("activityEndTime", s.getActivityEndTime());
            activityInfo.put("type", s.getType()); // 1学员抢答2随机选人3手动选人
        }
		return activityInfo;
	}
	
	/**
	 * 处理需要额外返回的数据（签到活动）
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param loginUserId 当前登录用户
	 * @param map 一个作为查询条件的map对象
	 * @return
	 */
	private Map<String, Object> handleActivityInfo8(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		// 兼容下旧数据
		if (StrUtils.isNull(pkgId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
			if (tevglTchClassroom != null) {
				pkgId = tevglTchClassroom.getPkgId();
			}
		}
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		TevglActivitySigninInfo s = tevglActivitySigninInfoMapper.selectObjectByIdAndPkgId(activityInfo);
        if (s != null) {
            activityInfo.put("activityId", s.getActivityId());
            activityInfo.put("activityTitle", s.getActivityTitle());
            activityInfo.put("activityType", s.getActivityType());
            activityInfo.put("activityState", s.getActivityStateActual());
            activityInfo.put("activityBeginTime", s.getActivityBeginTime());
            activityInfo.put("activityEndTime", s.getActivityEndTime());
            activityInfo.put("activityPic", "");
            activityInfo.put("content", "");
            activityInfo.put("signType", s.getSignType());
            // 学员签到记录
            map.clear();
            map.put("traineeId", loginUserId);
            map.put("activityId", s.getActivityId());
            map.put("ctId", ctId);
            List<TevglActivitySigninTrainee> signinTraineeList = tevglActivitySigninTraineeMapper.selectListByMap(map);
            activityInfo.put(GlobalActivity.HAS_BEEN_DONE, false);
            activityInfo.put("isSigned", false);
            if (signinTraineeList != null && signinTraineeList.size() > 0) {
                activityInfo.put(GlobalActivity.HAS_BEEN_DONE, true);
                activityInfo.put("isSigned", true);
            }
        }
		return activityInfo;
	}
	
	private Map<String, Object> handleActivityInfo9(Object ctId, Object pkgId, Object activityId, String loginUserId, Map<String, Object> map){
		Map<String, Object> activityInfo = new HashMap<>();
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		// 兼容下旧数据
		if (StrUtils.isNull(pkgId)) {
			if (tevglTchClassroom != null) {
				pkgId = tevglTchClassroom.getPkgId();
			}
		}
		activityInfo.put("pkgId", pkgId);
		activityInfo.put("activityId", activityId);
		TeaoTraineeExamroom s = teaoTraineeExamroomMapper.selectObjectByIdAndPkgId(activityInfo);
        if (s != null) {
            activityInfo.put("activityId", s.getErId());
            activityInfo.put("activityTitle", s.getActivityTitle());
            activityInfo.put("activityType", GlobalActivity.ACTIVITY_9_TRAINEE_EXAM);
            activityInfo.put("activityState", s.getActivityStateActual());
            activityInfo.put("activityBeginTime", s.getActivityBeginTime());
            activityInfo.put("activityEndTime", s.getActivityEndTime());
            activityInfo.put("activityPic", "");
            activityInfo.put("content", s.getRemark());
            // 实践考核是否有资格进入规则评分页面
            Map<String, Object> ps = new HashMap<String, Object>();
            ps.clear();
            ps.put("traineeId", loginUserId);
            List<TeaoTraineeExammember> exammemberList = teaoTraineeExammemberMapper.selectListByMap(ps);
        	boolean hasEligible = exammemberList.stream().anyMatch(a -> a.getErId().equals(activityInfo.get("activityId")) && a.getTraineeId().equals(loginUserId));
        	if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
        		hasEligible = true;
        	}
        	activityInfo.put("hasEligible", hasEligible);
        }
		return activityInfo;
	}
}
