package com.budaos.modules.common;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.activity.api.*;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgActivityRelationMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.api.TevglTchRoomPereAnswerService;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RefreshScope
public class PkgActivityUtils {

	private Logger log = LoggerFactory.getLogger(PkgActivityUtils.class);
	
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireService tevglActivityVoteQuestionnaireService;
	@Autowired
	private TevglActivityBrainstormingService tevglActivityBrainstormingService;
	@Autowired
	private TevglActivityAnswerDiscussService tevglActivityAnswerDiscussService;
	@Autowired
	private TevglActivityTestPaperService tevglActivityTestPaperService;
	@Autowired
	private TevglActivitySigninInfoService tevglActivitySigninInfoService;
	@Autowired
	private TevglTchRoomPereAnswerService tevglTchRoomPereAnswerService;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	
	/**
	 * 获取该（课堂或教学包）的活动群
	 * @param pkgId 教学包主键（或课堂对应的教学包主键）
	 * @param params 查询条件
	 * @return
	 */
	public Map<String, String> getActivityMap(String pkgId, Map<String, Object> params){
		if (StrUtils.isEmpty(pkgId)) {
			return null;
		}
		params.clear();
		params.put("pkgId", pkgId);
		params.put("activityType", GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			return list.stream().collect(Collectors.toMap(TevglPkgActivityRelation::getActivityId, TevglPkgActivityRelation::getActivityType));
		}
		return null;
	}
	

	/**
	 * 删除教学包的所有活动（物理删除）
	 * @param pkgId 教学包主键
	 * @param loginUserId 当前登录用户
	 * @param params 查询条件
	 * @return
	 */
	public R deleteActivity(String pkgId, String loginUserId, Map<String, Object> params) {
		params.clear();
		params.put("pkgId", pkgId);
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(params);
		log.debug("["+pkgId+"]活动：" + list.size());
		if (list != null && list.size() > 0) {
			list.stream().forEach(item -> {
				String activityId = item.getActivityId();
				String activityType = item.getActivityType();
				if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityType)) {
					R r = tevglActivityVoteQuestionnaireService.deleteVoteQuestionnaireInfo(activityId, pkgId, loginUserId);
					log.debug("删除投票问卷相关数据结果：" + r);
				} else if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityType)) {
					R r = tevglActivityBrainstormingService.deleteBrainstormingInfo(activityId, pkgId, loginUserId);
					log.debug("删除头脑风暴相关数据结果：" + r);
				} else if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) {
					R r = tevglActivityAnswerDiscussService.deleteAnswerDiscussInfo(activityId, pkgId, loginUserId);
					log.debug("删除答疑讨论相关数据结果：" + r);
				} else if (GlobalActivity.ACTIVITY_4_TEST_ACT.equals(activityType)) {
					R r =  tevglActivityTestPaperService.deleteTestPaperInfo(activityId, pkgId, loginUserId);
					log.debug("删除测试活动相关数据结果：" + r);
				} else if (GlobalActivity.ACTIVITY_5_TASK_GROUP.equals(activityType)) {
					
				} else if (GlobalActivity.ACTIVITY_6_CLASSROOM_PERFORMANCE.equals(activityType)) {
					R r = tevglTchRoomPereAnswerService.deleteAnswerActivity(activityId, pkgId, loginUserId);
					log.debug("删除课堂表现相关数据结果：" + r);
				} else if (GlobalActivity.ACTIVITY_8_SIGININ_INFO.equals(activityType)) {
					R r = tevglActivitySigninInfoService.deleteSigninInfo(activityId, pkgId, loginUserId);
					log.debug("删除签到活动相关数据结果：" + r);
				}
			});
		}
		return R.ok("活动删除成功");
	}
	
	/**
	 * 建立教学包与活动之间的关系
	 * @param pkgId 教学包
	 * @param activityId 活动
	 * @param activityType 活动类型，来源字典，可查看api工程的常量GlobalActivity.java文件
	 * @return
	 */
	public String buildRelation(String pkgId, String activityId, String activityType) {
		// 保存教学包与活动之间的关系
		TevglPkgActivityRelation relation = new TevglPkgActivityRelation();
		relation.setPaId(Identities.uuid());
		relation.setPkgId(pkgId);
		relation.setActivityId(activityId);
		relation.setActivityType(activityType);
		relation.setActivityState("0"); // 0未开始1进行中2已结束
		tevglPkgActivityRelationMapper.insert(relation);
		return relation.getPaId();
	}
	
	/**
	 * 判断当前用户是否有开始活动的权限
	 * @param activityId 当前活动
	 * @param loginUserId
	 * @param createUserId 当前活动的创建者
	 * @return {@link Boolean} 返回true表示有权限
	 */
	public boolean hasStartActPermission(String activityId, TevglPkgActivityRelation relation, String loginUserId, String createUserId) {
		if (StrUtils.isEmpty(activityId)  || StrUtils.isNull(relation)
				|| StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(createUserId)) {
			log.debug("hasStartActPermission()"+"必传参数为空,直接认为没有权限");
			return false;
		}
		if (loginUserId.equals(createUserId)) {
			return true;
		}
		// 判断教学包是否引用的其它教学包
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(relation.getPkgId());
		if (pkgInfo == null) {
			return false;
		}
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			TevglPkgInfo refPkg = tevglPkgInfoMapper.selectObjectById(pkgInfo.getRefPkgId());
			if (refPkg != null) {
				if (refPkg.getCreateUserId().equals(loginUserId)) {
					return true;
				} else {
					Map<String, Object> ps = new HashMap<>();
					ps.put("pkgId", pkgInfo.getRefPkgId());
					List<TevglPkgActivityRelation> refRelationList = tevglPkgActivityRelationMapper.selectListByMap(ps);
					return refRelationList.stream().anyMatch(a -> a.getActivityId().equals(activityId));
				}
			}
		} else {
			if (loginUserId.equals(pkgInfo.getCreateUserId())) {
				return true;
			}
		}
		return false;
	}
	
}
