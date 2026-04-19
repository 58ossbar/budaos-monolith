package com.budaos.modules.evgl.activity.service;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.common.enums.ActivityStateEnum;
import com.budaos.modules.evgl.activity.api.TevglActivityService;
import com.budaos.modules.evgl.activity.domain.*;
import com.budaos.modules.evgl.activity.persistence.*;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeExammember;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExamineMapper;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeExammemberMapper;
import com.budaos.modules.evgl.eao.service.TeaoTraineeExamroomServiceImpl;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalSetting;
import com.budaos.modules.evgl.empirical.persistence.TevglEmpiricalSettingMapper;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResgroupMapper;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicPaper;
import com.budaos.modules.evgl.question.persistence.TepExamineDynamicPaperMapper;
import com.budaos.modules.evgl.site.domain.TevglSiteSysMsg;
import com.budaos.modules.evgl.site.persistence.TevglSiteSysMsgMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchRoomPereTraineeAnswerMapper;
import com.budaos.modules.evgl.tch.service.TevglTchRoomPereAnswerServiceImpl;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TevglActivityServiceImpl implements TevglActivityService {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TevglActivityMapper tevglActivityMapper;
	@Autowired
	private DictServiceImpl dictServiceImpl;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglActivityBrainstormingServiceImpl tevglActivityBrainstormingServiceImpl;
	@Autowired
	private TevglActivityVoteQuestionnaireServiceImpl tevglActivityVoteQuestionnaireServiceImpl;
	@Autowired
	private TevglActivityAnswerDiscussServiceImpl tevglActivityAnswerDiscussServiceImpl;
	@Autowired
	private TevglActivityTaskServiceImpl tevglActivityTaskServiceImpl;
	@Autowired
	private TevglActivitySigninInfoServiceImpl tevglActivitySigninInfoServiceImpl;
	@Autowired
	private TevglTchRoomPereAnswerServiceImpl tevglTchRoomPereAnswerServiceImpl;
	@Autowired
	private TevglActivityBrainstormingMapper tevglActivityBrainstormingMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglActivityTraineeJoinInfoMapper tevglActivityTraineeJoinInfoMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireMapper tevglActivityVoteQuestionnaireMapper;
	@Autowired
	private TevglActivityBrainstormingTraineeAnswerMapper tevglActivityBrainstormingTraineeAnswerMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerMapper tevglActivityVoteQuestionnaireTraineeAnswerMapper;
	@Autowired
	private TevglActivitySigninTraineeMapper tevglActivitySigninTraineeMapper;
	@Autowired
	private TevglActivityAnswerDiscussMapper tevglActivityAnswerDiscussMapper;
	@Autowired
	private TevglSiteSysMsgMapper tevglSiteSysMsgMapper;
	@Autowired
	private TioWebSocketServerBootstrap tioWebSocketServerBootstrap;
	@Autowired
	private CbNumsUtils cbNumsUtils;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private TevglActivityTestPaperServiceImple tevglActivityTestPaperServiceImple;
	@Autowired
	private TepExamineDynamicPaperMapper tepExamineDynamicPaperMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TeaoTraineeExamroomServiceImpl teaoTraineeExamroomServiceImpl;
	@Autowired
	private TeaoTraineeExammemberMapper teaoTraineeExammemberMapper;
	@Autowired
	private TeaoTraineeExamineMapper teaoTraineeExamineMapper;
	@Autowired
	private TevglTchRoomPereTraineeAnswerMapper tevglTchRoomPereTraineeAnswerMapper; 
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private TevglEmpiricalSettingMapper tevglEmpiricalSettingMapper;
	
	/**
	 * 从字典获取活动状态
	 * @param pkgId 必传参数，教学包主键
	 * @param ctId 已改为非必填，课堂主键
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R listActivityState(String pkgId, String ctId, String chapterId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		List<Map<String,Object>> dictList = dictServiceImpl.getDictList("activityState");
		// 默认一个[全部]
		Map<String, Object> info = new HashMap<>();
		info.put("dictId", "1");
		info.put("dictCode", "");
		info.put("dictValue", "全部");
		info.put("dictUrl", "");
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("chapterId", chapterId);
		params.put("loginUserId", loginUserId);
		// 如果不是此课堂创建者,则不需要展示未开始
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isRoomReveiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getReceiverUserId().equals(loginUserId);
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		if (!isRoomCreator && !isTeachingAssistant && !isRoomReveiver) {
			dictList = dictList.stream().filter(a -> !a.get("dictCode").equals("0")).collect(Collectors.toList());
		} else { // 当登录用户是此课堂创建者时,[签到活动]全部都查
			params.put("isCreator", "Y");
		}
		List<Map<String,Object>> list = tevglActivityMapper.selectListMapByMap(params);
		// 此教学包全部状态下的活动
		info.put("num", list.size());
		// 如果登录用户不属于教师，则只查询进行中、已结束的活动
		if (!isRoomCreator && !isTeachingAssistant) {
			long count = list.stream().filter(a -> "1".equals(a.get("activityState")) 
					|| "2".equals(a.get("activityState"))).count();
			info.put("num", count);
		}
		// 不同状态的活动数量
		dictList.stream().forEach(dict -> {
			String code = (String) dict.get("dictCode");
			long count = (list == null || list.size() == 0) ? 0 : list.stream().filter(a -> a.get("activityState").equals(code)).count();
			dict.put("num", count);
		});
		// 添加并返回数据
		dictList.add(0, info);
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 获取活动分组(包含字典默认、自己针对教学包的活动创建的分组（注：没有细分到某个分组）)
	 * @param pkgId 教学包主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R listActivityGroup(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数不能为空");
		}
		// 先获取字典活动分组
		List<Map<String, Object>> dictList = dictServiceImpl.getDictList(GlobalActivity.ACTIVITY_GROUP_DICT_TYPE);
		// 增加表示区别是字典还是自定义的分组
		dictList.stream().forEach(dict -> {
			dict.put("groupType", GlobalActivity.ACTIVITY_GROUP_SYSTEM);
		});
		// 再获取字典创建的分组
		Map<String, Object> params = new HashMap<>();
		params.put("createUserId", loginUserId);
		params.put("pkgId", pkgId);
		params.put("groupType", GlobalActivity.ACTIVITY_GROUP_TYPE_2);
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TevglPkgResgroup> pkgResgroupList = tevglPkgResgroupMapper.selectListByMap(params);
		List<Map<String,Object>> collect = pkgResgroupList.stream().map(this::convertToSimpleInfo).collect(Collectors.toList());
		if (collect != null && collect.size() > 0) {
			dictList.addAll(collect);
		}
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
     * 活动列表（课堂页面专用）
     * @param params
     * @param loginUserId
     * @return
     */
	@Override
    public R listActivityForRoom(Map<String, Object> params, String loginUserId) {
        if (StrUtils.isNull(params.get("pkgId")) || StrUtils.isNull(params.get("ctId"))
                || StrUtils.isEmpty(loginUserId)) {
            return R.error("必传参数为空");
        }
        String pkgId = params.get("pkgId").toString();
        String ctId = params.get("ctId").toString();
        TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
        if (tevglTchClassroom == null) {
            return R.error("无效的课堂，无法查看活动");
        }
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (tevglPkgInfo == null) {
            return R.error("无效的教学包");
        }
        boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
        boolean isRoomReveiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getReceiverUserId().equals(loginUserId);
        boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
        // 查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("pkgId", tevglPkgInfo.getPkgId());
        map.put("subjectId", tevglPkgInfo.getSubjectId());
        List<Map<String, Object>> allChapterList = tevglBookChapterMapper.selectSimpleListMap(map);
        // 查询此课堂的权限按钮设置
        map.put("ctId", tevglTchClassroom.getCtId());
		List<String> permissionList = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(map);
		// 查询课堂设置的经验值规则
        map.clear();
        map.put("ctId", ctId);
        List<TevglEmpiricalSetting> empiricalSettingList = tevglEmpiricalSettingMapper.selectListByMap(map);
        // 计算当前课堂总人数
        //Integer totalTraineeNum = tevglTchClassroomTraineeMapper.countTotalTraineeNumberByCtId(ctId);
        Integer totalTraineeNum = tevglTchClassroom.getStudyNum(); 
        List<Map<String,Object>> allList = new ArrayList<Map<String,Object>>();
        // 当登录用户是此课堂创建者时,[签到活动]全部都查
        if (isRoomCreator || isTeachingAssistant || isRoomReveiver) {
            params.put("isCreator", "Y");
        }
        if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && !tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
			List<String> pkgIds = new ArrayList<String>();
			pkgIds.add(pkgId);
			pkgIds.add(tevglPkgInfo.getRefPkgId());
			params.put("pkgIds", pkgIds);
			params.remove("pkgId");
		}
        params.put("pkgId", pkgId);
        params.put("loginUserId", loginUserId);
        params.put("sidx", "relation.activity_state");
        params.put("order", "asc, t.createTime desc");
        List<Map<String,Object>> list = tevglActivityMapper.selectListMapByMap(params);
        log.debug("当前课堂：" + ctId);
        log.debug("当前课堂教学包：" + pkgId);
        log.debug("当前课堂教学包对应的来源教学包：" + tevglPkgInfo.getRefPkgId());
        log.debug("查询条件：" + params);
        log.debug("当前课堂教学包的活动数量：" + list.size());
        // 来源教学包都是已经发布的状态，所以返回标识控制，不能删除不能修改，只能查看、开始结束 activityId
        map.clear();
        map.put("pkgId", tevglPkgInfo.getRefPkgId());
        List<String> refPkgActivityIds = tevglPkgActivityRelationMapper.selectActivityIdList(map);
        // 加入集合
        if (list.size() > 0) {
        	allList.addAll(list);
        }
        // 查询条件
        Map<String, Object> ps = new HashMap<>();
        List<String> activityIds = allList.stream().map(act -> act.get("activityId").toString()).collect(Collectors.toList());
        // 查询签到记录
        ps.put("activityIds", activityIds);
        ps.put("ctId", ctId);
        List<TevglActivitySigninTrainee> signinTraineeList = tevglActivitySigninTraineeMapper.selectListByMap(ps);
        // 测试活动作答记录
        ps.clear();
        ps.put("paperIds", activityIds);
        ps.put("pkgId", pkgId);
        List<TepExamineDynamicPaper> dynamicPaperList = tepExamineDynamicPaperMapper.selectListByMap(ps);
        // 课堂表现参与人数
        ps.clear();
        ps.put("ctId", ctId);
        ps.put("anserIds", activityIds);
        List<Map<String, Object>> answerNums = tevglTchRoomPereTraineeAnswerMapper.queryAnswerNum(ps);
        // 实践考核是否有资格进入规则评分页面
        ps.clear();
        ps.put("traineeId", loginUserId);
        List<TeaoTraineeExammember> exammemberList = teaoTraineeExammemberMapper.selectListByMap(ps);
        // 已考核人数
        ps.clear();	
        ps.put("erIds", activityIds);
        List<Map<String, Object>> traineeExamineList = teaoTraineeExamineMapper.countTraineeListByMap(ps);
        // 遍历
        allList.stream().forEach(activityInfo -> {
        	// 课堂创建者标识
        	handleCreatorLabelV2(activityInfo, tevglTchClassroom, loginUserId);
        	// 【课堂创建者或接收者身份时】活动具体权限（课堂创建者或接收者时，控制是否能修改或者删除该活动）
        	handleActivityPermissionForRoomV2(tevglPkgInfo, activityInfo, refPkgActivityIds, loginUserId, tevglTchClassroom);
        	// 【助教身份时】处理当前操作按钮的权限
            toHandleBtnPermission(activityInfo, tevglTchClassroom, loginUserId, permissionList, refPkgActivityIds);
        	// 默认活动没做过
            activityInfo.put(GlobalActivity.HAS_BEEN_DONE, false);
        	activityInfo.put("tevglTchClassroom", tevglTchClassroom);
        	// 所属章节
        	String chapterName = showChapterName(activityInfo.get("chapterId"), allChapterList);
        	activityInfo.put("chapterName", chapterName);
            // 活动封面处理
            handleActivityPic(activityInfo);
            // 1 投票问卷
            if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityInfo.get("activityType"))) {
            	// 该问卷累计有多少人参与
            	activityInfo.put("totalUserNum", activityInfo.get("answerNumber")); // tevglActivityVoteQuestionnaireMapper.countTotalUserNum(activityInfo.get("activityId"))
            	// 本课堂当前有多少人参与该活动
                if (ActivityStateEnum.ACTIVITY_STATE_0_NOT_START.getCode().equals(activityInfo.get("activityState"))) {
                    activityInfo.put("answerNumber", 0);
                } else {

                }
                // 实际能获取到的经验值
            	List<TevglEmpiricalSetting> collect = empiricalSettingList.stream().filter(a -> "8".equals(a.getDictCode())).collect(Collectors.toList());
                if (collect != null && collect.size() > 0) {
                    activityInfo.put("empiricalValue", collect.get(0).getValue());
                }
            }
            // 2 头脑风暴
            if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityInfo.get("activityType"))) {
                List<TevglEmpiricalSetting> collect = empiricalSettingList.stream().filter(a -> "9".equals(a.getDictCode())).collect(Collectors.toList());
                if (collect != null && collect.size() > 0) {
                    activityInfo.put("empiricalValue", collect.get(0).getValue());
                }
            }
            // 3 [答疑讨论] 统计实际参与人数(只要在群聊发了一条消息,则认为参与了)
            if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityInfo.get("activityType"))) {
                handleAct3(activityInfo, loginUserId);
                List<TevglEmpiricalSetting> collect = empiricalSettingList.stream().filter(a -> "10".equals(a.getDictCode())).collect(Collectors.toList());
                if (collect != null && collect.size() > 0) {
                    activityInfo.put("empiricalValue", collect.get(0).getValue());
                }
            }
            // 4 [测试活动] 当前登录用户测试活动是否做过，且做过几次
            if (GlobalActivity.ACTIVITY_4_TEST_ACT.equals(activityInfo.get("activityType"))) {
                handleAct4(activityInfo, loginUserId, dynamicPaperList);
                activityInfo.put("empiricalValue", "总分累加得");
            }
            // 6 [课堂表现] 统计课堂表现的参与人数
            if (GlobalActivity.ACTIVITY_6_CLASSROOM_PERFORMANCE.equals(activityInfo.get("activityType"))) {
            	handleAct6(activityInfo, loginUserId, answerNums);
            	activityInfo.put("empiricalValue", "老师评分得");
			}
            // 8 [签到活动]
            if (GlobalActivity.ACTIVITY_8_SIGININ_INFO.equals(activityInfo.get("activityType"))) {
                handleAct8(activityInfo, loginUserId, signinTraineeList, totalTraineeNum);
                List<TevglEmpiricalSetting> collect = empiricalSettingList.stream().filter(a -> "4".equals(a.getDictCode())).collect(Collectors.toList());
                if (collect != null && collect.size() > 0) {
                    activityInfo.put("empiricalValue", collect.get(0).getName());
                }
            }
            // 9 实践考核
            if (GlobalActivity.ACTIVITY_9_TRAINEE_EXAM.equals(activityInfo.get("activityType"))) {
            	handleAct9(activityInfo, loginUserId, tevglTchClassroom, exammemberList, traineeExamineList);
            	activityInfo.put("empiricalValue", "总分累加得");
            }
        });
        // 如果没有定时任务维护活动状态,单独处理
        //list = doChangeActivityState(list);
        // 处理各种活动当前登录用户是否已经参与或已做过并提交了
        doCheckHasBeenDone(list, ctId, loginUserId);
        // 获取字典活动分组
      	List<Map<String, Object>> dictList = dictServiceImpl.getDictList(GlobalActivity.ACTIVITY_GROUP_DICT_TYPE);
      	Map<String, Object> mapDatas = buildMapDatas();
        // 根据字典或自定义分组分组
        Function<Map<String,Object>, String> s = new Function<Map<String,Object>, String>() {
            @Override
            public String apply(Map<String, Object> t) {
                Object object = t.get("resgroupId");
                String string = object.toString();
                return string;
            }
        };
        Map<String, List<Map<String, Object>>> collect = list.stream().collect(Collectors.groupingBy(s));
        // 进一步处理
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
        for (Map.Entry<String, List<Map<String, Object>>> m : collect.entrySet()) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("resgroupId", m.getKey());
            List<Map<String, Object>> dicts = dictList.stream().filter(a -> a.get("dictCode").equals(m.getKey())).collect(Collectors.toList());
            info.put("resgroupName", dicts != null && dicts.size() > 0 ? dicts.get(0).get("dictValue") : mapDatas.get(m.getKey()));
            info.put("children", m.getValue());
            result.add(info);
        }
        return R.ok().put(Constant.R_DATA, result)
        		.put("activityNum", allList.size())
        		.put(GlobalRoomPermission.ROOM_PERM_ACT_ADD_KEY, doHandAddBtn(tevglTchClassroom, loginUserId, permissionList));
    }
	
	/**
	 * 按钮权限判断处理（此方法为课堂页面中专用）
	 * @param activityInfo
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @param permissionList
	 * @apiNote
	 * hasStartActPermission：是否拥有开始活动的权限，为true时，则web端或小程序出现开始活动的按钮
	 * hasEndActPermission：是否拥有结束活动的权限，为true时，则web端或小程序出现删除活动的按钮
	 * hasAddActPermission：是否拥有添加活动的权限，为true时，则web端或小程序出现删除活动的按钮
	 * hasEditActPermission：是否拥有编辑活动的权限，为true时，则web端或小程序出现删除活动的按钮
	 * hasDeleteActPermission：是否拥有删除活动的权限，为true时，则web端或小程序出现删除活动的按钮
	 * <i>
	 * <br>1.当活动状态为0时，才需要有开始活动的按钮
	 * </i>
	 */
	private void toHandleBtnPermission(Map<String, Object> activityInfo, TevglTchClassroom tevglTchClassroom, String loginUserId, List<String> permissionList, List<String> refPkgActivityIds) {
		doHandStartBtn(activityInfo, tevglTchClassroom, loginUserId, permissionList);
		doHandEndBtn(activityInfo, tevglTchClassroom, loginUserId, permissionList);
		doHandEditBtn(activityInfo, tevglTchClassroom, loginUserId, permissionList, refPkgActivityIds);
		doHandDeleteBtn(activityInfo, tevglTchClassroom, loginUserId, permissionList, refPkgActivityIds);
	}
	
	/**
	 * 处理【删除活动】按钮
	 * @param activityInfo
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @param permissionList
	 * @param refPkgActivityIds 此课堂中，引用教学包中的活动
	 */
	private void doHandDeleteBtn(Map<String, Object> activityInfo, TevglTchClassroom tevglTchClassroom, String loginUserId, List<String> permissionList, List<String> refPkgActivityIds) {
		// 如果是来源教学包中的活动，肯定是不能编辑的
		String activityId = activityInfo.get("activityId").toString();
		if (refPkgActivityIds.contains(activityId)) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_DELETE_KEY, false);
			return;
		}
		if (!"0".equals(activityInfo.get("activityState"))) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_DELETE_KEY, false);
			return;
		}
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		if (isRoomCreator) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_DELETE_KEY, true);
			return;
		}
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		if (isTeachingAssistant && permissionList.contains(GlobalRoomPermission.ROOM_PERM_ACT_DELETE)) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_DELETE_KEY, true);
		}
	}
	
	/**
	 * 处理【编辑活动】按钮
	 * @param activityInfo
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @param permissionList
	 * @param refPkgActivityIds 此课堂中，引用教学包中的活动
	 */
	private void doHandEditBtn(Map<String, Object> activityInfo, TevglTchClassroom tevglTchClassroom, String loginUserId, List<String> permissionList, List<String> refPkgActivityIds) {
		// 如果是来源教学包中的活动，肯定是不能编辑的
		String activityId = activityInfo.get("activityId").toString();
		if (refPkgActivityIds.contains(activityId)) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_EDIT_KEY, false);
			return;
		}
		if (!"0".equals(activityInfo.get("activityState"))) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_EDIT_KEY, false);
			return;
		}
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		if (isRoomCreator) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_EDIT_KEY, true);
			return;
		}
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		if (isTeachingAssistant && permissionList.contains(GlobalRoomPermission.ROOM_PERM_ACT_EDIT)) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_EDIT_KEY, true);
		}
	}
	
	/**
	 * 处理【开始活动】按钮
	 * @param activityInfo
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @param permissionList
	 */
	private void doHandStartBtn(Map<String, Object> activityInfo, TevglTchClassroom tevglTchClassroom, String loginUserId, List<String> permissionList) {
		// 当活动已经开始，肯定不需要
		if (!"0".equals(activityInfo.get("activityState"))) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_START_KEY, false);
			return;
		}
		// 如果活动没开始，则判断，当前用户是否为课堂创建者，或为助教且助教能设置
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		if (isRoomCreator) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_START_KEY, true);
			return;
		}
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		if (isTeachingAssistant && permissionList.contains(GlobalRoomPermission.ROOM_PERM_ACT_START)) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_START_KEY, true);
		}
	}
	
	/**
	 * 处理【结束活动】按钮
	 * @param activityInfo
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @param permissionList
	 */
	private void doHandEndBtn(Map<String, Object> activityInfo, TevglTchClassroom tevglTchClassroom, String loginUserId, List<String> permissionList) {
		// 当活动未开始或已经结束，肯定不需要
		if (!"1".equals(activityInfo.get("activityState"))) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_END_KEY, false);
			return;
		}
		// 如果活动正在进行中
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		// 且课堂没有被移交，且登录用户是课堂创建者
		if (isRoomCreator && StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_END_KEY, true);
			return;
		}
		// 如果课堂被移交了
		if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getReceiverUserId().equals(loginUserId)) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_END_KEY, true);
			return;
		}
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		if (isTeachingAssistant && permissionList.contains(GlobalRoomPermission.ROOM_PERM_ACT_END)) {
			activityInfo.put(GlobalRoomPermission.ROOM_PERM_ACT_END_KEY, true);
		}
	}
	
	/**
	 * 处理【新增活动】按钮
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @param permissionList
	 * @return
	 */
	private boolean doHandAddBtn(TevglTchClassroom tevglTchClassroom, String loginUserId, List<String> permissionList) {
		if (tevglTchClassroom == null || StrUtils.isEmpty(loginUserId)) {
			return false;
		}
		// 如果课堂已结束，不允许继续添加活动 课堂状态(1未开始2进行中3已结束)
		if ("3".equals(tevglTchClassroom.getClassroomState())) {
			return false;
		}
		// 如果是课堂创建者
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		if (isRoomCreator) {
			return true;
		}
		// 如果是助教且设置了权限
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		if (isTeachingAssistant && permissionList.contains(GlobalRoomPermission.ROOM_PERM_ACT_ADD)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 课堂页面中，投票问卷，返回数据处理
	 * @param activityInfo
	 * @param loginUserId
	 */
	private void handleAct1(Map<String, Object> activityInfo, String loginUserId) {
		
	}
	
	/**
	 * 头脑风暴，返回数据处理
	 * @param activityInfo
	 * @param loginUserId
	 */
	private void handleAct2(Map<String, Object> activityInfo, String loginUserId) {
			
	}
	
	/**
	 * 课堂页面中，答疑讨论，返回数据处理
	 * @param activityInfo
	 * @param loginUserId
	 */
	private void handleAct3(Map<String, Object> activityInfo, String loginUserId) {
		List<Map<String, Object>> userList = tevglActivityAnswerDiscussMapper.countUserNum(activityInfo.get("activityId"));
        activityInfo.put("answerNumber", userList.size());
	}
	
	/**
	 * 4 [测试活动] 当前登录用户测试活动是否做过，且做过几次
	 * @param activityInfo
	 * @param loginUserId
	 * @param dynamicPaperList
	 */
	private void handleAct4(Map<String, Object> activityInfo, String loginUserId, List<TepExamineDynamicPaper> dynamicPaperList) {
		// 作答人数（需去重）
        List<TepExamineDynamicPaper> collect = dynamicPaperList.stream().filter(dynamicPaper -> activityInfo.get("pkgId").equals(dynamicPaper.getPkgId()) && dynamicPaper.getPaperId().equals(activityInfo.get("activityId"))).collect(Collectors.toList());
        List<String> traineeIds = collect.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
        activityInfo.put("answerNumber", traineeIds.stream().distinct().count());
        // 重做次数
        activityInfo.put("redoNum", activityInfo.get("signType"));
        activityInfo.remove("signType");
        // 做过几次
        activityInfo.put("hasBeenDoenTimes", 0);
        if (dynamicPaperList != null && dynamicPaperList.size() > 0) {
            long count = dynamicPaperList.stream().filter(a -> "Y".equals(a.getPaperIsFinished()) && a.getTraineeId().equals(loginUserId) && a.getPaperId().equals(activityInfo.get("activityId"))).count();
            activityInfo.put("hasBeenDoenTimes", count);
            activityInfo.put(GlobalActivity.HAS_BEEN_DONE, count > 0 ? true : false);
        }
	}
	
	/**
	 * 课堂页面中，作业/小组任务，返回数据处理
	 * @param activityInfo
	 * @param loginUserId
	 */
	private void handleAct5(Map<String, Object> activityInfo, String loginUserId) {
		
	}
	
	/**
	 * 6 [课堂表现] 统计课堂表现的参与人数
	 * @param activityInfo
	 * @param loginUserId
	 * @param answerNums
	 */
	private void handleAct6(Map<String, Object> activityInfo, String loginUserId, List<Map<String, Object>> answerNums) {
		List<Map<String, Object>> collect = answerNums.stream().filter(a -> a.get("anserId").equals(activityInfo.get("activityId"))).distinct().collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            activityInfo.put("answerNumber", collect.get(0).get("answerNum"));
        } else {
            activityInfo.put("answerNumber", 0);
        }
	}
	
	private void handleAct7(Map<String, Object> activityInfo, String loginUserId) {
			
	}
	
	/**
	 * 8 [签到活动]
	 * @param activityInfo
	 * @param loginUserId 当前登录用户
	 * @param signinTraineeList 当前课堂的签到数据
	 * @param totalTraineeNum 当前课堂总人数
	 */
	private void handleAct8(Map<String, Object> activityInfo, String loginUserId, List<TevglActivitySigninTrainee> signinTraineeList, Integer totalTraineeNum) {
		// 手势签到的顺序
        activityInfo.put("number", activityInfo.get("questionNumber"));
        // 处理是否已其拿到
        handleIsSigned(activityInfo, loginUserId, signinTraineeList);
        // 共多少人（不明确，先直接取课堂总人数）totalTraineeNum、已签到多少人 answerNumber、未签到 unSignNum
        activityInfo.put("totalTraineeNum", totalTraineeNum);
        long answerNumber = (long) activityInfo.get("answerNumber");
        activityInfo.put("unSignNum", totalTraineeNum - answerNumber);
	}
	
	/**
	 * 9 实践考核,是否有资格进入规则评分页面
	 * @param activityInfo
	 * @param loginUserId
	 * @param tevglTchClassroom
	 * @param exammemberList 
	 * @param traineeExamineList
	 */
	private void handleAct9(Map<String, Object> activityInfo, String loginUserId, TevglTchClassroom tevglTchClassroom, List<TeaoTraineeExammember> exammemberList, List<Map<String, Object>> traineeExamineList) {
		// 实践考核是否有资格进入规则评分页面
        boolean hasEligible = exammemberList.stream().anyMatch(a -> a.getErId().equals(activityInfo.get("activityId")) && a.getTraineeId().equals(loginUserId));
        if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
            hasEligible = true;
        }
        if (!hasEligible) {
            // 如果课堂被移交了
            if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
                if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
                    hasEligible = true;
                }
            }
        }
        activityInfo.put("hasEligible", hasEligible);
        // 考核人数
        long answerNumber = traineeExamineList.stream().filter(a -> a.get("erId").equals(activityInfo.get("activityId"))).count();
        activityInfo.put("answerNumber", answerNumber);
	}
	
	/**
     * 处理活动权限（课堂页面里，查询活动专用）
     * @param classroomTevglPkgInfo 课堂对应的教学包
     * @param activityInfo 活动信息
     * @param refPkgActivityIdList 来源教学包中的所有活动，课堂表记录的pkg_id对应的ref_pkg_id的活动
     * @param loginUserId 当前登录用户
     * @param classroomCreateUserId 课堂创建者
     */
    private void handleActivityPermissionForRoom(TevglPkgInfo classroomTevglPkgInfo, Map<String, Object> activityInfo, List<String> refPkgActivityIdList, String loginUserId, String classroomCreateUserId) {
        if (!loginUserId.equals(classroomCreateUserId)) {
            activityInfo.put("hasPermission", false);
            return;
        }
        if (loginUserId.equals(classroomCreateUserId)) {
            // 如果当前教学包为源教学包，且refPkgId为空
            if (StrUtils.isEmpty(classroomTevglPkgInfo.getRefPkgId())) {
                activityInfo.put("hasPermission", true);
            } else {
                if (refPkgActivityIdList.contains(activityInfo.get("activityId"))) {
                    activityInfo.put("hasPermission", false);
                } else {
                    activityInfo.put("hasPermission", true);
                }
            }
        }
    }
    
    /**
     * 处理活动权限（课堂页面里，查询活动专用）
     * @param classroomTevglPkgInfo 课堂对应的教学包
     * @param activityInfo 活动信息
     * @param refPkgActivityIdList 来源教学包中的所有活动，课堂表记录的pkg_id对应的ref_pkg_id的活动
     * @param loginUserId 当前登录用户
     * @param tevglTchClassroom 课堂信息
     */
    private void handleActivityPermissionForRoomV2(TevglPkgInfo classroomTevglPkgInfo, Map<String, Object> activityInfo, List<String> refPkgActivityIdList, String loginUserId, TevglTchClassroom tevglTchClassroom) {
        if (tevglTchClassroom == null) {
        	activityInfo.put("hasPermission", false);
        	return;
        }
        String classroomCreateUserId = tevglTchClassroom.getCreateUserId();
        // 如果课堂没移交
        if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
        	// 且登录用户不是课堂创建者，直接返回
        	if (!loginUserId.equals(classroomCreateUserId)) {
                activityInfo.put("hasPermission", false);
                return;
            }
        	// 如果是课堂创建者
        	if (loginUserId.equals(classroomCreateUserId)) {
                // 如果当前教学包为源教学包，且refPkgId为空
                if (StrUtils.isEmpty(classroomTevglPkgInfo.getRefPkgId())) {
                    activityInfo.put("hasPermission", true);
                } else {
                	// 如果当前活动是属于来源教学包中，那么肯定不能进行 删除、修改操作！！！
                    if (refPkgActivityIdList.contains(activityInfo.get("activityId"))) {
                        activityInfo.put("hasPermission", false);
                    } else {
                    	// 否则就是在课堂里面创建的，这种是允许修改删除的
                        activityInfo.put("hasPermission", true);
                    }
                }
            }
        }
        // 如果课堂被移交了，那么控制权需要改变
        if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
        	// 且登录用户不是课堂创建者，直接返回
        	if (loginUserId.equals(classroomCreateUserId)) {
                activityInfo.put("hasPermission", false);
                return;
            }
        	// 如果是课堂接收者
        	if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
                // 如果当前教学包为源教学包，且refPkgId为空
                if (StrUtils.isEmpty(classroomTevglPkgInfo.getRefPkgId())) {
                    activityInfo.put("hasPermission", true);
                } else {
                	// 如果当前活动是属于来源教学包中，那么肯定不能进行 删除、修改操作！！！
                    if (refPkgActivityIdList.contains(activityInfo.get("activityId"))) {
                        activityInfo.put("hasPermission", false);
                    } else {
                    	// 否则就是在课堂里面创建的，这种是允许修改删除的
                        activityInfo.put("hasPermission", true);
                    }
                }
            }
        }
    }
	
	private Map<String, Object> buildMapDatas(){
		Map<String, Object> map = new HashMap<>();
		map.put("1", "投票问卷");
		map.put("2", "头脑风暴");
		map.put("3", "答疑讨论");
		map.put("4", "课堂考核");
		map.put("5", "作业/小组任务");
		map.put("6", "课堂表现");
		map.put("7", "轻直播");
		map.put("8", "课堂签到");
		map.put("9", "课堂考核");
		return map;
	}
	
	/**
     * 处理[活动创建者标识]与[课堂创建者标识]
     * @param activityInfo
     * @param tevglTchClassroom
     * @param loginUserId
     */
    private void handleCreatorLabel(Map<String, Object> activityInfo, TevglTchClassroom tevglTchClassroom, String loginUserId){
        // 课堂创建者标识
        if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
            activityInfo.put("isRoomCreator", true);
            // 活动创建者标识
            if (loginUserId.equals(activityInfo.get("createUserId"))) {
                activityInfo.put(GlobalActivity.IS_CREATOR, true);
            } else {
                activityInfo.put(GlobalActivity.IS_CREATOR, false);
            }
        } else {
            activityInfo.put("isRoomCreator", false);
            activityInfo.put(GlobalActivity.IS_CREATOR, false);
        }
    }
    
    /**
     * 处理[活动创建者标识]与[课堂创建者标识]
     * @param activityInfo
     * @param tevglTchClassroom
     * @param loginUserId
     */
    private void handleCreatorLabelV2(Map<String, Object> activityInfo, TevglTchClassroom tevglTchClassroom, String loginUserId){
        // 如果课堂没有被移交
    	if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
    		// 课堂创建者标识
            if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
                activityInfo.put("isRoomCreator", true);
                // 活动创建者标识
                if (loginUserId.equals(activityInfo.get("createUserId"))) {
                    activityInfo.put(GlobalActivity.IS_CREATOR, true);
                } else {
                    activityInfo.put(GlobalActivity.IS_CREATOR, false);
                }
            } else {
                activityInfo.put("isRoomCreator", false);
                activityInfo.put(GlobalActivity.IS_CREATOR, false);
            }
            return;
    	}
    	// 如果课堂被移交了
    	if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
    		// 课堂创建者标识
            if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
                activityInfo.put("isRoomCreator", true);
                // TODO 活动创建者标识
                if (loginUserId.equals(activityInfo.get("createUserId"))) {
                    activityInfo.put(GlobalActivity.IS_CREATOR, true);
                } else {
                    activityInfo.put(GlobalActivity.IS_CREATOR, false);
                }
            } else {
                activityInfo.put("isRoomCreator", false);
                activityInfo.put(GlobalActivity.IS_CREATOR, false);
            }
    	}
    }
	
	/**
	 * 处理活动封面图
	 * @param activityInfo
	 */
	private void handleActivityPic(Map<String, Object> activityInfo) {
		activityInfo.put("activityPic", "");
		/*if (!GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityInfo.get("activityType"))) {
			String index = "17";
			// 投票问卷图片
			if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityInfo.get("activityType"))) {
				
			} else if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityInfo.get("activityType"))) {
				index = "21";
			}
			activityInfo.put("activityPic", uploadPathUtils.stitchingPath(activityInfo.get("activityPic"), index));
		}*/
	}
	
	/**
	 * 活动列表（教学包页面专用）
	 * @param params
	 * @return
	 */
	@Override
	public R listActivityForPackage(Map<String, Object> params, String loginUserId) {
		String pkgId = (String)params.get("pkgId");
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的教学包");
		}
		// 查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("pkgId", tevglPkgInfo.getPkgId());
        map.put("subjectId", tevglPkgInfo.getSubjectId());
        List<Map<String, Object>> allChapterList = tevglBookChapterMapper.selectSimpleListMap(map);
		// 如果是创建者或接管者
        boolean isCreator = loginUserId.equals(tevglPkgInfo.getCreateUserId());
        boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
		if (isCreator || isReceiver) {
			params.put("isCreator", "Y");
		} else { // 如果是共建者
			map.clear();
			map.put("pkgId", pkgId);
			map.put("userId", loginUserId);
			List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(map);
			if (teamList != null && teamList.size() > 0) {
				params.put("isTogetherBuild", "Y");	
			}
		}
		params.put("loginUserId", loginUserId);
		params.put("sidx", "t.createTime");
		params.put("order", "desc");
		List<Map<String,Object>> list = tevglActivityMapper.selectListMapByMap(params);
		log.debug("查询条件：" + params);
		log.debug("活动数量：" + list.size());
		// 处理路径
		list.stream().forEach(activityInfo -> {
			// 所属章节
            String chapterName = showChapterName(activityInfo.get("chapterId"), allChapterList);
            activityInfo.put("chapterName", chapterName);
			// 活动权限优化
            //handleActivityPermissionForPackage(tevglPkgInfo, activityInfo, params.get("chapterId"), loginUserId);
            handleActivityPermissionForPackageV2(tevglPkgInfo, activityInfo, params.get("chapterId"), loginUserId);
            // 活动封面处理
            handleActivityPic(activityInfo);
			// 处理此活动是否属于当前登录用户创建
			if (loginUserId.equals(activityInfo.get("createUserId"))) {
				activityInfo.put(GlobalActivity.IS_CREATOR, true);
			} else {
				activityInfo.put(GlobalActivity.IS_CREATOR, false);
			}
		});
		 // 获取字典活动分组
      	List<Map<String, Object>> dictList = dictServiceImpl.getDictList(GlobalActivity.ACTIVITY_GROUP_DICT_TYPE);
      	Map<String, Object> mapDatas = buildMapDatas();
        // 根据字典或自定义分组分组
        Function<Map<String,Object>, String> s = new Function<Map<String,Object>, String>() {
            @Override
            public String apply(Map<String, Object> t) {
                Object object = t.get("resgroupId");
                String string = object.toString();
                return string;
            }
        };
        Map<String, List<Map<String, Object>>> collect = list.stream().collect(Collectors.groupingBy(s));
        // 进一步处理
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
        for (Map.Entry<String, List<Map<String, Object>>> m : collect.entrySet()) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("resgroupId", m.getKey());
            List<Map<String, Object>> dicts = dictList.stream().filter(a -> a.get("dictCode").equals(m.getKey())).collect(Collectors.toList());
            info.put("resgroupName", dicts != null && dicts.size() > 0 ? dicts.get(0).get("dictValue") : mapDatas.get(m.getKey()));
            info.put("children", m.getValue());
            result.add(info);
        }
		return R.ok().put(Constant.R_DATA, result).put("activityNum", list.size());
	}
	

	/**
     * <b>处理活动权限，兼容2种身份，创建者、共建者</b><br>
     * <b>是否有修改删除等权限</b><br>
     * @param tevglPkgInfo 教学包
     * @param activityInfo 教学包对应的活动集合遍历出来的对象
     * @param chapterId 前端传入的查询条件所属章节，活动对应的章节，非必传参数，活动的章节可能为空
     * @param loginUserId 当前登录用户
     */
	@Deprecated
    private void handleActivityPermissionForPackage(TevglPkgInfo tevglPkgInfo, Map<String, Object> activityInfo, Object chapterId, String loginUserId) {
    	if (tevglPkgInfo == null || activityInfo == null) {
            log.debug("活动权限判断时，参数tevglPkgInfo、或activityInfo为空，直接返回");
        }
    	// 权限判断，如果当前教学包是发布状态，此所有活动不允许删除、编辑操作，只能查看、开始、结束
        if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
            activityInfo.put("hasPermission", false);
            return;
        }
        // 创建者
        if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
        	activityInfo.put("hasPermission", true);
        	// 如果当前教学包为源教学包，且pkgId与refPkgId匹配（兼容可能存在的旧数据）
            if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
                activityInfo.put("hasPermission", true);
            }
        } else {
        	// 共建者，需要判断章节是否有权限
        	if (StrUtils.isNull(chapterId)) {
        		 activityInfo.put("hasPermission", false);
        		 return;
        	}
        	R r = pkgPermissionUtils.hasPermissionChapter(tevglPkgInfo.getPkgId(), loginUserId, StrUtils.isNull(chapterId) ? null : chapterId.toString());
        	log.debug("权限校验结果：" + r);
        	if (r.get("code").equals(0)) {
		        activityInfo.put("hasPermission", true);
		    } else {
		        activityInfo.put("hasPermission", false);
		    }
        }
    }
    
	/**
     * <b>教学包页面专用，处理活动权限，兼容3种身份，创建者、共建者、接管者</b><br>
     * <b>是否有修改删除等权限</b><br>
     * @param tevglPkgInfo 教学包
     * @param activityInfo 教学包对应的活动集合遍历出来的对象
     * @param chapterId 前端传入的查询条件所属章节，活动对应的章节，非必传参数，活动的章节可能为空
     * @param loginUserId 当前登录用户
     */
    private void handleActivityPermissionForPackageV2(TevglPkgInfo tevglPkgInfo, Map<String, Object> activityInfo, Object chapterId, String loginUserId) {
    	if (tevglPkgInfo == null || activityInfo == null) {
            log.debug("活动权限判断时，参数tevglPkgInfo、或activityInfo为空，直接返回");
        }
    	// 权限判断，如果当前教学包是发布状态，此所有活动不允许删除、编辑操作，只能查看、开始、结束
        if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
            activityInfo.put("hasPermission", false);
            return;
        }
        // 如果教学包没有被移交
        if (StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId())) {
        	// 创建者
            if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
            	activityInfo.put("hasPermission", true);
            	// 如果当前教学包为源教学包，且pkgId与refPkgId匹配（兼容可能存在的旧数据）
                if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
                    activityInfo.put("hasPermission", true);
                }
            } else {
            	// 共建者，需要判断章节是否有权限
            	if (StrUtils.isNull(chapterId)) {
            		 activityInfo.put("hasPermission", false);
            		 return;
            	}
            	R r = pkgPermissionUtils.hasPermissionChapter(tevglPkgInfo.getPkgId(), loginUserId, StrUtils.isNull(chapterId) ? null : chapterId.toString());
            	log.debug("权限校验结果：" + r);
            	if (r.get("code").equals(0)) {
    		        activityInfo.put("hasPermission", true);
    		    } else {
    		        activityInfo.put("hasPermission", false);
    		    }
            }
        }
        // 如果教学包被移交了
        if (StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId())) {
        	if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
        		activityInfo.put("hasPermission", false);
        		return;
        	}
        	// 如果是接管者
        	if (loginUserId.equals(tevglPkgInfo.getReceiverUserId())) {
        		activityInfo.put("hasPermission", true);
            	// 如果当前教学包为源教学包，且pkgId与refPkgId匹配（兼容可能存在的旧数据）
                if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
                    activityInfo.put("hasPermission", true);
                }
        	} else {
        		// 共建者，需要判断章节是否有权限
            	if (StrUtils.isNull(chapterId)) {
            		 activityInfo.put("hasPermission", false);
            		 return;
            	}
            	R r = pkgPermissionUtils.hasPermissionChapter(tevglPkgInfo.getPkgId(), loginUserId, StrUtils.isNull(chapterId) ? null : chapterId.toString());
            	log.debug("权限校验结果：" + r);
            	if (r.get("code").equals(0)) {
    		        activityInfo.put("hasPermission", true);
    		    } else {
    		        activityInfo.put("hasPermission", false);
    		    }
        	}
        }
    }
	
	/**
	 * 处理签到活动，是否已签到
	 * @param activityInfo
	 * @param loginUserId
	 * @param signinTraineeList
	 */
	private void handleIsSigned(Map<String, Object> activityInfo, String loginUserId,
			List<TevglActivitySigninTrainee> signinTraineeList) {
		String activityId = activityInfo.get("activityId").toString();
		boolean flag = signinTraineeList.stream().anyMatch(a -> a.getActivityId().equals(activityId)
				&& a.getTraineeId().equals(loginUserId));
		if (flag) {
			activityInfo.put("isSigned", true);
		} else {
			activityInfo.put("isSigned", false);
		}
	}

	/**
	 * 处理各种活动当前登录用户是否已经参与或已做过并提交了
	 * @param list
	 * @param ctId 课堂id
	 * @param loginUserId 当前登录用户
	 */
	private void doCheckHasBeenDone(List<Map<String, Object>> list, String ctId, String loginUserId) {
		// 根据条件查询此课堂该学员的相关作答内容
		Map<String, Object> ps = new HashMap<String, Object>();
		ps.put("traineeId", loginUserId);
		ps.put("ctId", ctId);
		// 学员的作答内容 [投票/问卷]
		List<TevglActivityVoteQuestionnaireTraineeAnswer> voteQuestionnaireTraineeAnswerList = tevglActivityVoteQuestionnaireTraineeAnswerMapper.selectListByMap(ps);
		// 学员的作答内容 [头脑风暴]
		List<TevglActivityBrainstormingTraineeAnswer> traineeAnswerBrainstormingList = tevglActivityBrainstormingTraineeAnswerMapper.selectListByMap(ps);
		// TODO 剩余的活动
		
		// 遍历比较
		list.stream().forEach(a -> {
			String activityId = a.get("activityId").toString();
			// 如果匹配上了，则认为做过投票问卷了
            voteQuestionnaireTraineeAnswerList.stream().forEach(answer -> {
                if (activityId.equals(answer.getActivityId())) {
                    a.put(GlobalActivity.HAS_BEEN_DONE, true);
                }
            });
            // 如果匹配上了，则认为做过头脑风暴了
            traineeAnswerBrainstormingList.stream().forEach(answer -> {
                if (activityId.equals(answer.getActivityId())) {
                    a.put(GlobalActivity.HAS_BEEN_DONE, true);
                }
            });
		});
	}
	public static void main(String[] args) {
			/*DateUtils.getNowDate();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 5);
			Date time = calendar.getTime();
			System.err.println(time);
			*/
		Date nowDate = DateUtils.getNowDate();
		String activityEndTime = "2021-01-23 17:21:00";
		Date endDate = convertStringToDate(activityEndTime);
		System.out.println("活动结束时间：" + endDate);
		// 活动自动结束时间不得短于5分钟
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 5);
		Date time = calendar.getTime();
		System.out.println("当前时间" + nowDate);
		System.out.println("计算后" + time);
		if (endDate.before(time)) {
			System.out.println("慢点");
		}
	}
	/**
	 * 开始活动
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param pkgId 教学包id
	 * @param activityType 活动类型1投票/问卷2头脑风暴3答疑/讨论4测试活动等等
	 * @param loginUserId 当前登录用户
	 * @param activityEndTime 活动自动结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public R startActivity(String ctId, String activityId, String pkgId, String activityType, String loginUserId, String activityEndTime) {
		// 合法性校验
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId)
				|| StrUtils.isEmpty(activityType) || StrUtils.isEmpty(loginUserId)
				|| StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroomInfo = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroomInfo == null) {
			return R.error("无效的记录，请刷新后重试");
		}
		if ("N".equals(classroomInfo.getState())) {
			return R.error("课堂已被删除，无法开始活动");
		}
		if ("1".equals(classroomInfo.getClassroomState())) {
			return R.error("课堂尚未开始，无法开始活动");
		}
		if ("3".equals(classroomInfo.getClassroomState())) {
			return R.error("课堂已经结束，无法开始活动");
		}
		// 如果手动设置了自动结束时间
		if (StrUtils.isNotEmpty(activityEndTime)) {
			Date nowDate = DateUtils.getNowDate();
			Date endDate = convertStringToDate(activityEndTime);
			if (endDate.before(nowDate)) {
                return R.error("活动结束时间不得早于当前系统时间");
            }
			// 活动自动结束时间不得短于5分钟
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 1);
			Date time = calendar.getTime();
			if (endDate.before(time)) {
				return R.error("活动自动结束时间不得短于1分钟");
			}
		}
		R res = new R();
		String activityTypeName = "";
		// 投票/问卷
		if (activityType.equals(GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE)) {
			res = tevglActivityVoteQuestionnaireServiceImpl.startActivityVoteQuestionnaire(ctId, activityId, loginUserId, activityEndTime);
			activityTypeName = "投票/问卷";
		} 
		// 头脑风暴
		if (activityType.equals(GlobalActivity.ACTIVITY_2_BRAINSTORMING)) {
			res = tevglActivityBrainstormingServiceImpl.startActivityBrainstorming(ctId, activityId, loginUserId, activityEndTime);
			activityTypeName = "头脑风暴";
		}
		// 答疑/讨论
		if (activityType.equals(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS)) {
			res = tevglActivityAnswerDiscussServiceImpl.startAnswerDiscussInfo(ctId, activityId, pkgId, loginUserId, activityEndTime);
			activityTypeName = "答疑/讨论";
		}
		// 测试活动
		if (activityType.equals(GlobalActivity.ACTIVITY_4_TEST_ACT)) {
			res = tevglActivityTestPaperServiceImple.startTestPaperInfo(ctId, activityId, loginUserId, activityEndTime);
			activityTypeName = "测试活动";
		}
		// 作业/小组任务
		if (activityType.equals(GlobalActivity.ACTIVITY_5_TASK_GROUP)) {
			res = tevglActivityTaskServiceImpl.startActivityTask(ctId, activityId, loginUserId, activityEndTime);
			activityTypeName = "作业/小组任务";
		}
		// 课堂表现
		if (activityType.equals(GlobalActivity.ACTIVITY_6_CLASSROOM_PERFORMANCE)) {
			res = tevglTchRoomPereAnswerServiceImpl.startAnswerActivity(ctId, activityId, loginUserId, activityEndTime);
			activityTypeName = "课堂表现";
		}
		// 签到活动
		if (activityType.equals(GlobalActivity.ACTIVITY_8_SIGININ_INFO)) {
			res = tevglActivitySigninInfoServiceImpl.startSigninInfo(ctId, activityId, loginUserId, activityEndTime);
			activityTypeName = "签到";
		}
		// 实践考核
		if (activityType.equals(GlobalActivity.ACTIVITY_9_TRAINEE_EXAM)) {
			res = teaoTraineeExamroomServiceImpl.startActivityExamroomInfo(ctId, activityId, loginUserId, activityEndTime);
			activityTypeName = "实践考核";
		}
		if ((Integer)res.get("code") != 0) {
			return res;
		}
		// 获取 activityInfo {'activityId':'', 'activityType':'', 'activityTitle':'', 'activityPic':'', 'empiricalValue':'', 'signType':''}
		Map<String, Object> activityInfo = (Map<String, Object>) res.get(Constant.R_DATA);
		if (activityInfo != null) {
			// 获取此课堂状态为有效的所有学员
			List<String> traineeIds = listTraineeIds(ctId);
			if (traineeIds != null && traineeIds.size() > 0) {
				// 分发活动给此课堂状态为有效的所有学员
				//doSendActivityToTrainee(activityId, activityType, traineeIds);
				// TODO 给每个成员往系统通知表插入数据
				List<TevglSiteSysMsg> insertList = new ArrayList<>();
				for (String traineeId : traineeIds) {
					TevglSiteSysMsg tevglSiteSysMsg = new TevglSiteSysMsg();
					tevglSiteSysMsg.setMsgId(Identities.uuid());
					tevglSiteSysMsg.setToTraineeId(traineeId);
					tevglSiteSysMsg.setMsgTitle("【" + activityTypeName +"】[" + activityInfo.get("activityTitle") + "]   开始啦~");
					tevglSiteSysMsg.setMsgContent("【课堂】["+classroomInfo.getName()+"]的【"+activityTypeName+"】[" + activityInfo.get("activityTitle") + "]   开始啦~");
					tevglSiteSysMsg.setMsgType(activityType); // 消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到
					tevglSiteSysMsg.setParams1(ctId);
					tevglSiteSysMsg.setPkgId(pkgId);
					tevglSiteSysMsg.setParams2(activityId);
					tevglSiteSysMsg.setReadState("N");
					tevglSiteSysMsg.setCreateTime(DateUtils.getNowTimeStamp());
					tevglSiteSysMsg.setCreateUserId(loginUserId); // 创建人(系统创建时为0)
					tevglSiteSysMsg.setState("Y");
					insertList.add(tevglSiteSysMsg);
					// 即时通讯,通知此人
					ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
					JSONObject msg = cbNumsUtils.getNums(traineeId);
					CBImUtils.sendToUser(tioConfig, traineeId, msg);
				}
				tevglSiteSysMsgMapper.insertBatch(insertList);
			}
		}
		// 返回课堂创建者标识
		if (loginUserId.equals(classroomInfo.getCreateUserId())) {
			activityInfo.put("isCreator", true);
		} else {
			activityInfo.put("isCreator", false);
		}
		return R.ok("开始活动").put(Constant.R_DATA, activityInfo);
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符转Date
	 * @param str
	 * @return
	 */
	private static Date convertStringToDate(String str) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 结束活动
	 * @param activityId 活动id
	 * @param activityType 活动类型
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R endActivity(String ctId, String activityId, String activityType, String loginUserId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(activityType) 
				|| StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		R r = new R();
		// 投票/问卷
		if (activityType.equals(GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE)) {
			r = tevglActivityVoteQuestionnaireServiceImpl.endActivityVoteQuestionnaire(ctId, activityId, loginUserId);
		} 
		// 头脑风暴
		if (activityType.equals(GlobalActivity.ACTIVITY_2_BRAINSTORMING)) {
			r =  tevglActivityBrainstormingServiceImpl.endActivityBrainstorming(ctId, activityId, loginUserId);
		}
		// 答疑/讨论
		if (activityType.equals(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS)) {
			r =  tevglActivityAnswerDiscussServiceImpl.endAnswerDiscussInfo(ctId, activityId, loginUserId);
		}
		// 测试活动
		if (activityType.equals(GlobalActivity.ACTIVITY_4_TEST_ACT)) {
			r =  tevglActivityTestPaperServiceImple.endTestPaperInfo(ctId, activityId, loginUserId);
		}
		// 作业/小组任务
		if (activityType.equals(GlobalActivity.ACTIVITY_5_TASK_GROUP)) {
			r = tevglActivityTaskServiceImpl.endActivityTask(ctId, activityId, loginUserId);
		}
		// 课堂表现
		if (activityType.equals(GlobalActivity.ACTIVITY_6_CLASSROOM_PERFORMANCE)) {
			r = tevglTchRoomPereAnswerServiceImpl.endAnswerActivity(ctId, activityId, loginUserId);
		}
		// 签到活动
		if (activityType.equals(GlobalActivity.ACTIVITY_8_SIGININ_INFO)) {
			r =  tevglActivitySigninInfoServiceImpl.endSingninInfo(ctId, activityId, loginUserId);
		}
		// 实践考核
		if (activityType.equals(GlobalActivity.ACTIVITY_9_TRAINEE_EXAM)) {
			r =  teaoTraineeExamroomServiceImpl.endActivityExamroomInfo(ctId, activityId, loginUserId);
		}
		log.debug("结束活动结果：" + r);
		if (r.get("code").equals(0)) {
			String activityTitle = null;
			if (!StrUtils.isNull(r.get(Constant.R_DATA))) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) r.get(Constant.R_DATA);
				activityTitle = StrUtils.isNull(map.get("activityTitle")) ? null : map.get("activityTitle").toString();
			}
			// 即时通讯
			JSONObject msg = new JSONObject();
			msg.put("busitype", "reloadactlist"); // 结束活动后，要求重新加载下活动列表接口
			msg.put("msgtype", "alert");
			JSONObject busitype = new JSONObject();
			busitype.put("title", StrUtils.isEmpty(activityTitle) ? "活动结束" : "【"+activityTitle+"】活动结束了");
			busitype.put("ctId", ctId);
			busitype.put("ct_id", ctId);
			busitype.put("activityId", activityId);
			busitype.put("activityType", activityType);
			msg.put("msg", busitype);
			ServerTioConfig tioConfig = tioWebSocketServerBootstrap.getServerTioConfig();
			Map<String, Object> params = new HashMap<>();
			params.put("ctId", ctId);
			List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
			if (classroomTraineeList != null && classroomTraineeList.size() > 0) {
				for (TevglTchClassroomTrainee traineeInfo : classroomTraineeList) {
					CBImUtils.sendToUser(tioConfig, traineeInfo.getTraineeId(), msg);		
				}
			}
		}
		return r;
	}
	
	/**
	 * 删除活动
	 * @param activityId 活动id
	 * @param activityType 活动类型
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R deleteActivity(String activityId, String activityType, String loginUserId, String pkgId) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(activityType) 
				|| StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		// 执行删除投票/问卷
		if (activityType.equals(GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE)) {
			return tevglActivityVoteQuestionnaireServiceImpl.deleteVoteQuestionnaireInfo(activityId, pkgId, loginUserId);
		}
		// 执行删除头脑风暴
		if (activityType.equals(GlobalActivity.ACTIVITY_2_BRAINSTORMING)) {
			return tevglActivityBrainstormingServiceImpl.deleteBrainstormingInfo(activityId, pkgId, loginUserId);
		} 
		// 执行删除答疑/讨论
		if (activityType.equals(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS)) {
			return tevglActivityAnswerDiscussServiceImpl.deleteAnswerDiscussInfo(activityId, pkgId, loginUserId);
		}
		// 执行删除测试活动
		if (activityType.equals(GlobalActivity.ACTIVITY_4_TEST_ACT)) {
			return tevglActivityTestPaperServiceImple.deleteTestPaperInfo(activityId, pkgId, loginUserId);
		}
		// 执行删除作业/小组任务
		if (activityType.equals(GlobalActivity.ACTIVITY_5_TASK_GROUP)) {
			return tevglActivityTaskServiceImpl.deleteTaskInfo(activityId, pkgId, loginUserId);
		}
		// 执行删除课堂表现活动
		if (activityType.equals(GlobalActivity.ACTIVITY_6_CLASSROOM_PERFORMANCE)) {
			return tevglTchRoomPereAnswerServiceImpl.deleteAnswerActivity(activityId, pkgId, loginUserId);
		}
		// 执行删除签到活动
		if (activityType.equals(GlobalActivity.ACTIVITY_8_SIGININ_INFO)) {
			return tevglActivitySigninInfoServiceImpl.deleteSigninInfo(activityId, pkgId, loginUserId);
		}
		// 执行删除实践考核
		if (activityType.equals(GlobalActivity.ACTIVITY_9_TRAINEE_EXAM)) {
			return teaoTraineeExamroomServiceImpl.deleteActivityExamroomInfo(activityId, pkgId, loginUserId);
		}
		return R.ok("删除成功");
	}
	
	/**
	 * 保存自定义活动分组
	 * @param jsonObject {pkgId:'教学包主键', resgroupName:'分组名称'}
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R saveResgroup(JSONObject jsonObject, String loginUserId) {
		String pkgId = jsonObject.getString("pkgId");
		String resgroupName = jsonObject.getString("resgroupName");
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(resgroupName) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		R r = checkPermission(pkgId, loginUserId);
		if ((Integer)r.get("code") != 0) {
			return r;
		}
		// 填充信息
		TevglPkgResgroup t = new TevglPkgResgroup();
		t.setResgroupId(Identities.uuid());
		t.setPkgId(pkgId);
		t.setResgroupName(resgroupName);
		t.setGroupType("2"); // 1活教材(资源课程)2活动
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setCreateUserId(loginUserId);
		t.setState("Y");
		t.setResgroupTotal(0);
		// 排序号处理
		Map<String, Object> map = new HashMap<>();
		map.put("pkgId", pkgId);
		map.put("groupType", "2");
		Integer sortNum = tevglPkgResgroupMapper.getMaxSortNum(map);
		t.setSortNum(sortNum);
		// 保存数据
		tevglPkgResgroupMapper.insert(t);
		return R.ok("保存成功");
	}
	
	/**
	 * 查看活动信息
	 * @param activityId
	 * @param activityType
	 * @return
	 */
	@Override
	public R viewActivityInfo(String activityId, String activityType) {
		if (StrUtils.isEmpty(activityId) || StrUtils.isEmpty(activityType)) {
			return R.error("必传参数为空");
		}
		// 投票/问卷
		if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityType)) {
			return tevglActivityVoteQuestionnaireServiceImpl.viewVoteQuestionnaireInfo(activityId);
		}
		// 头脑风暴
		if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityType)) {
			return tevglActivityBrainstormingServiceImpl.viewActivityBrainstormingInfo(activityId);
		}
		// 答疑/讨论
		if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) {
			return tevglActivityAnswerDiscussServiceImpl.viewAnswerDiscussInfo(activityId);
		}
		return R.ok();
	}

	
	/**
	 * 取部分属性
	 * @param tevglPkgResgroup
	 * @return
	 */
	private Map<String, Object> convertToSimpleInfo(TevglPkgResgroup tevglPkgResgroup){
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("dictId", tevglPkgResgroup.getResgroupId());
		info.put("dictCode", tevglPkgResgroup.getResgroupId());
		info.put("dictValue", tevglPkgResgroup.getResgroupName());
		info.put("dictUrl", "");
		info.put("groupType", GlobalActivity.ACTIVITY_GROUP_CUSTOM);
		return info;
	}
	
	/**
	 * 权限校验
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	private R checkPermission(String pkgId, String loginUserId) {
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包");
		}
		if (!loginUserId.equals(pkgInfo.getCreateUserId())) {
			Map<String, Object> params = new HashMap<>();
			params.put("pkgId", pkgId);
			params.put("userId", loginUserId);
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
			if (list == null || list.size() == 0) {
				return R.error("暂无权限，操作失败");
			}
		}
		return R.ok();
	}

	/**
	 * 将字符串格式的转为Date
	 * @param str 第二个参数若不传，str需满足yyyy-MM-dd HH:ss:mm格式
	 * @param format 可选参数默认 yyyy-MM-dd HH:ss:mm
	 * @return
	 */
	private Date convertStringToDate(String str, String format) {
		String fm = "yyyy-MM-dd HH:ss:mm";
		if (StrUtils.isNotEmpty(format)) {
			fm = format;
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(fm);
		try { 
			date = sdf.parse(str);
		} catch (ParseException e) { 
			e.printStackTrace(); 
		} 
		return date;
	}
	
	/**
	 * 根据当前系统时间维护更新活动状态
	 * @param list
	 */
	private List<Map<String,Object>> doChangeActivityState(List<Map<String,Object>> list) {
		for (Map<String, Object> map : list) {
			String activityId = (String) map.get("activityId");
			String activityType = (String) map.get("activityType");
			String activityBeginTime = (String) map.get("activityBeginTime");
			String activityEndTime = (String) map.get("activityEndTime");
			String activityState = (String) map.get("activityState");
			if ("0".equals(activityState)) {
				map.put("activityStateName", "未开始");
			}
			if (StrUtils.isNotEmpty(activityBeginTime) && StrUtils.isNotEmpty(activityEndTime)) {
				Date nowDate = DateUtils.getNowDate();
				Date activityBeginDate = convertStringToDate(activityBeginTime, null);
				Date activityEndDate = convertStringToDate(activityEndTime, null);
				// 活动开始时间 < 当前时间 < 结束时间
				if (activityBeginDate.before(nowDate) && nowDate.before(activityEndDate)) {
					doChangeActivityStateActual(activityId, activityType, "1");
					map.put("activityStateName", "进行中");
				}
				// 活动结束时间 < 当前时间
				if (activityEndDate.before(nowDate)) {
					doChangeActivityStateActual(activityId, activityType, "2");
					map.put("activityStateName", "已结束");
				}
			}
		}
		return list;
	}
	
	/**
	 * 更新数据库中此记录的活动状态
	 * @param activityId 活动id
	 * @param activityType 活动类型1投票问卷2头脑风暴3等等等等
	 * @param activityState 0未开始1进行中2已结束
	 */
	private void doChangeActivityStateActual(String activityId, String activityType, String activityState) {
		// 投票/问卷
		if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityType)) {
			TevglActivityVoteQuestionnaire vo = new TevglActivityVoteQuestionnaire();
			vo.setActivityId(activityId);
			vo.setActivityState(activityState); // 0未开始1进行中2已结束
			tevglActivityVoteQuestionnaireMapper.update(vo);
		}
		// 头脑风暴 
		if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityType)) { 
			TevglActivityBrainstorming b = new TevglActivityBrainstorming();
			b.setActivityId(activityId);
			b.setActivityState(activityState);
			tevglActivityBrainstormingMapper.update(b);
		}
	}

	/**
	 * 分发活动给学员
	 * @param activityId 活动id
	 * @param activityType 类型1投票问卷2头脑风暴3等等等
	 * @param traineeIds 学员id集合
	 */
	private void doSendActivityToTrainee(String activityId, String activityType, List<String> traineeIds) {
		List<TevglActivityTraineeJoinInfo> list = new ArrayList<>();
		for (String traineeId : traineeIds) {
			// 填充参与信息
			TevglActivityTraineeJoinInfo t = new TevglActivityTraineeJoinInfo();
			t.setAjId(Identities.uuid());
			t.setTraineeId(traineeId);
			t.setActivityId(activityId);
			t.setActivityType(activityType);
			t.setJoinTime(DateUtils.getNowTimeStamp());
			t.setState("Y");
			list.add(t);
		}
		// 批量新增
		if (list.size() > 0) {
			tevglActivityTraineeJoinInfoMapper.insertBatch(list);	
		}
		
	}

	/**
	 * 获取此课堂状态为有效的所有学员id
	 * @param ctId
	 * @return
	 */
	private List<String> listTraineeIds(String ctId){
		List<String> traineeIds = new ArrayList<String>();
		// 查找此课堂所有状态为有效的学员
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("state", "Y");
		List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
		if (classroomTraineeList == null || classroomTraineeList.size() == 0) {
			return traineeIds;
		}
		traineeIds = classroomTraineeList.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		return traineeIds;
	}

	@Override
	public List<Map<String, Object>> selectSimpleListMapForRelease(Map<String, Object> params) {
		return tevglPkgActivityRelationMapper.selectSimpleListMapForRelease(params);
	}

	/**
	 * 获取章节名称
	 * @param chapterId
	 * @param allList
	 * @return
	 */
	private String showChapterName(Object chapterId, List<Map<String, Object>> allList) {
		if (StrUtils.isNull(chapterId)) {
			return null;
		}
		if (allList == null || allList.size() == 0) {
            return null;
        }
		// 取出当前节点信息
		List<Map<String, Object>> chapterList = allList.stream().filter(a -> a.get("chapterId").equals(chapterId)).collect(Collectors.toList());
		if (chapterList == null || chapterList.size() == 0) {
			return null;
		}
		Map<String, Object> node = chapterList.get(0);
		String chapterName = node.get("chapterName").toString();
		String parentId = node.get("parentId").toString();
		return recursion(parentId, allList, chapterName);
	}

	/**
	 * 递归处理
	 * @param parentId
	 * @param allList
	 * @param name
	 * @return
	 */
	private String recursion(String parentId, List<Map<String, Object>> allList, String name){
		List<Map<String, Object>> parentNodeList = allList.stream().filter(a -> a.get("chapterId").equals(parentId)).collect(Collectors.toList());
		if (parentNodeList == null || parentNodeList.size() == 0) {
			return name;
		}
		Map<String, Object> node = parentNodeList.get(0);
		String tempName = name;
		name = node.get("chapterName").toString() + " => " + tempName;
		return recursion(node.get("parentId").toString(), allList, name);
	}
    
}
