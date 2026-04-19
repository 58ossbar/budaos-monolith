package com.budaos.modules.evgl.pkg.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.DictUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.evgl.activity.domain.*;
import com.budaos.modules.evgl.activity.persistence.*;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.service.TevglBookSubjectServiceImpl;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.modules.evgl.pkg.api.ReleaseTeachingPackageService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.domain.TevglPkgRes;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.modules.evgl.pkg.persistence.*;
import com.budaos.modules.evgl.question.domain.TepExaminePaperInfo;
import com.budaos.modules.evgl.question.domain.TepExaminePaperQuestionsDetail;
import com.budaos.modules.evgl.question.persistence.TepExaminePaperInfoMapper;
import com.budaos.modules.evgl.question.persistence.TepExaminePaperQuestionsDetailMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 发布教学包专用接口
 * @author huj
 *
 */
@Service
public class ReleaseTeachingPackageServiceImpl implements ReleaseTeachingPackageService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookSubjectServiceImpl tevglBookSubjectServiceImpl;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;

	@Autowired
	private TevglActivityVoteQuestionnaireMapper tevglActivityVoteQuestionnaireMapper;
	@Autowired
	private TevglActivityBrainstormingMapper tevglActivityBrainstormingMapper;
	@Autowired
	private TevglActivityAnswerDiscussMapper tevglActivityAnswerDiscussMapper;
	@Autowired
	private TepExaminePaperInfoMapper tepExaminePaperInfoMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionMapper tevglActivityVoteQuestionnaireQuestionMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionOptionMapper tevglActivityVoteQuestionnaireQuestionOptionMapper;
	@Autowired
	private TepExaminePaperQuestionsDetailMapper tepExaminePaperQuestionsDetailMapper;
	@Autowired
	private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglPkgCheckMapper tevglPkgCheckMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private CloudPanUtils cloudPanUtils;
	@Autowired
	private RecursionUtils recursionUtils;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private DictUtil dictUtil;
	
	@Value("${com.budaos.file-upload-path:}")
	public String uploadPath;
	
	/**
	 * 发布教学包
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 * @apiNote
	 * {
		    "pkgId":"", "subjectId":"",
		    "chapterIdArray":["ff64ce6d6dfe4ce088f89b2d386920f1", "0c22d826ab6d4187a0a4026edefd2508"],
		    "resgroupIdArray":["430acb9f5e2045e3b11ec0f572ceadff"],
		    "activityJson":"[{'活动id':'固定值activityType'}]",
		    "pkgName":"时间不在于你拥有多少"
		}
	 */
	@Override
	@Transactional
	public R releaseTeachingPackage(JSONObject jsonObject, String loginUserId) {
		String appUserId = loginUserId;
		// 当前教学包
		String pkgId = jsonObject.getString("pkgId");
		// 当前课程
		String subjectId = jsonObject.getString("subjectId");
		// 当前数据
		JSONArray userInputList = jsonObject.getJSONArray("list");
		// 未关联章节的活动,且数据格式要求
		// [{'activityId': '', 'activityType': '1'}, {'activityId': '', 'activityType': '3'}]
		JSONArray activityList = jsonObject.getJSONArray("activityList");
		// 所选择的云盘目录或文件,且数据格式要求
		// ['1', '2', '5ac7ccb759c241029bbf5a87b8cfe22e']
		JSONArray cloudPanList = jsonObject.getJSONArray("cloudPanList");
		// 合法性校验
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (userInputList == null || userInputList.size() == 0) {
			return R.error("未能获取到数据");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		// 权限判断
		R ra = checkReleasePackagePermission(pkgInfo, loginUserId);
		if (!ra.get("code").equals(0)) {
			return ra;
		}
		// 重新获取数据
		@SuppressWarnings("unchecked")
		Map<String, Object> myData = (Map<String, Object>) ra.get(Constant.R_DATA);
		loginUserId = myData.get("loginUserId").toString();
		
		boolean flag = hasWaitingCheckPkg(pkgId);
		if (flag) {
			return R.error("当前有正在审核的教学包，请在审核通过之后，再来发布");
		}
		// 教学包名称和版本号处理
		String pkgName = jsonObject.getString("pkgName");
		String pkgVersion = jsonObject.getString("pkgVersion");
		R r = checkSoleNameAndVersion(pkgName, pkgVersion, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 获取源课程的章节数据
		List<TevglBookChapter> allChapterList = getChapterListData(subjectId);
		if (allChapterList == null || allChapterList.size() == 0) {
			return R.error("请先录入一些章节再来发布吧~");
		}
		List<TevglBookChapter> chapterListTree = tevglBookSubjectServiceImpl.buildBook(subjectId, allChapterList, 0);
		log.debug("subjectId: " + subjectId + " 的所有章节： " + allChapterList.size());
		log.debug("递归构建后" + chapterListTree.size());
		// 获取字典，根据Y/N来判断是否需要审核
		String neeCheckPkgValue = getNeeCheckPkgValue();
		jsonObject.put("neeCheckPkgValue", neeCheckPkgValue);
		// 处理是否应用了别人的教学包
		String privateUse = handlePrivateUse(pkgInfo, loginUserId);
		// 先生成保存新的课程基本信息
		String subjectIdNew = doSaveSubjectInfo(subjectId, loginUserId);
		// 再保存新的教学包基本信息
		String newPkgId = doSavePkgInfo(jsonObject, subjectIdNew, loginUserId, privateUse, pkgInfo, appUserId);
		log.debug("新的教学包:" + newPkgId);
		log.debug("新的课程:" + subjectIdNew);
		// 更新教学包引用数
		pkgUtils.plusPkgRefNum(pkgId, 1);

		// 调用方法，处理 章节-分组-资源 数据 （重点关注）
		handleDatas(newPkgId, subjectIdNew, loginUserId, userInputList, allChapterList, chapterListTree);
		// 调用方法，处理 活动 数据                       （重点关注）
		handleActivityDatas(newPkgId, loginUserId, userInputList, activityList, chapterListTree);
		// 调用方法，处理云盘数据                           （重点关注）
		handlCloudPanDatas(pkgId, newPkgId, loginUserId, cloudPanList);

		String msg = "发布成功";
		if ("Y".equals(neeCheckPkgValue)) {
			msg = "教学包已提交，等待审核，审核大约需要1-2个工作日，审核通过后才可继续发布新的";
			return R.ok(msg).put(Constant.R_DATA, newPkgId);
		} else {
			Map<String, Object> data = new HashMap<>();
			data.put("code", 200);
			data.put("msg", msg);
			return R.ok(data);
		}
	}
	
	/**
	 * <b>发布教学包时的权限校验</b>
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	private R checkReleasePackagePermission(TevglPkgInfo tevglPkgInfo, String loginUserId) {
		if (tevglPkgInfo == null) {
			return R.error("无效的记录");
		}
		String pkgId = tevglPkgInfo.getPkgId();
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		// 如果是在课堂页面发布教学包
		if (tevglTchClassroom != null) {
			// 如果课堂没有被移交
			if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
					return R.error("非法操作");
				}
			} else { // 否则课堂被移交了
				if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					return R.error("课堂移交被其它人接管了，无法继续发布教学包");
				}
				// 如果登录用户是接收者
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					// TODO 模拟【接收者】为创建者，教学包列表等地方就得需要查询ReceiverUserId得情况
					//loginUserId = tevglTchClassroom.getCreateUserId();
					// TODO 源教学包并不是接收者创建的，需要额外处理
					return R.error("当前课堂是由管理员移交给您，暂不支持发布教学包");
				} else {
					return R.error("非法操作");
				}
			}
		} else { // 否则就是在教学包页面中发布
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (!hasOperatingAuthorization) {
				return R.error("教学包已被管理员移交给别人接管，你已没有权限，无法再发布教学包");
			} else {
				// 因为其它业务处理，是兼容了接管者这种情况
				// 这里直接将接管者，伪装成真正的创建者，
				// 那么，发布之后，其实就相当于创建者发布了一个新的教学包
				loginUserId = tevglPkgInfo.getCreateUserId();
			}
		}
		Map<String, Object> data = new HashMap<>();
		data.put("loginUserId", loginUserId);
		return R.ok().put(Constant.R_DATA, data);
	}

	private String getNeeCheckPkgValue() {
		String neeCheckPkg = "Y";
		List<TsysDict> dictList = dictUtil.getByType("need_check_pkg");
		if (dictList != null && dictList.size() > 0) {
			List<TsysDict> list = dictList.stream().filter(a -> "need_check_pkg_value".equals(a.getDictCode())).collect(Collectors.toList());
			if (list != null && list.size() > 0) {
				neeCheckPkg =  list.get(0).getDictValue();
			}	
		}
		return neeCheckPkg;
	}
	
	/**
	 * 验证是否有正在审核通过的教学包
	 * @param pkgId
	 * @return
	 */
	private boolean hasWaitingCheckPkg(String pkgId) {
		Map<String, Object> map = new HashMap<>();
		map.put("refPkgId", pkgId);
		map.put("releaseStatus", "N");
		List<TevglPkgInfo> list = tevglPkgInfoMapper.selectListByMap(map);
		log.debug("条件：" + map);
		log.debug("是否有待审核的教学包：" + list.size());
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 处理跟章节挂钩的活动
	 * @param newPkgId
	 * @param loginUserId
	 * @param userInputList
	 * @param activityList 没有关联章节的活动
	 * @param chapterListTree
	 */
	private void handleActivityDatas(String newPkgId, String loginUserId, JSONArray userInputList, JSONArray activityList, List<TevglBookChapter> chapterListTree) {
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  发布教学包中-处理跟章节挂钩的活动 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		// 等待入库的集合
		List<TevglActivityVoteQuestionnaire> insertVoteQuestionnaireActList1 = new ArrayList<>(); // 投票问卷
		List<TevglActivityBrainstorming> insertBrainstormingActList2 = new ArrayList<>(); // 头脑风暴
		List<TevglActivityAnswerDiscuss> insertAnswerDiscussActList3 = new ArrayList<>(); // 答疑讨论
		List<TepExaminePaperInfo> insertExaminePaperInfoActList4 = new ArrayList<>(); // 测试活动
		// 先查询
		List<String> activityIdList1 = new ArrayList<>();
		List<String> activityIdList2 = new ArrayList<>();
		List<String> activityIdList3 = new ArrayList<>();
		List<String> activityIdList4 = new ArrayList<>();
		// 遍历未关联章节的活动
		if (activityList != null && activityList.size() > 0) {
			for (int i = 0; i < activityList.size(); i++) {
				JSONObject actObj = activityList.getJSONObject(i);
				String activityId = actObj.getString("activityId");
				String activityType = actObj.getString("activityType");
				switch (activityType){
					case "1":
						activityIdList1.add(activityId);
						break;
					case "2":
						activityIdList2.add(activityId);
						break;
					case "3":
						activityIdList3.add(activityId);
						break;
					case "4":
						activityIdList4.add(activityId);
						break;
					default:
						break;
				}
			}
		}
		// 遍历用户传入的数据
		for (int i = 0; i < userInputList.size(); i++) {
			// 获得当前被选择的章节相关数据
			JSONObject data = userInputList.getJSONObject(i);
			JSONArray activityArray = data.getJSONArray("activityArray");
			if (activityArray != null && activityArray.size() > 0) {
				for (int n = 0; n < activityArray.size(); n++) {
					JSONObject actObj = activityArray.getJSONObject(n);
					String activityId = actObj.getString("activityId");
					String activityType = actObj.getString("activityType");
					switch (activityType){
						case "1":
							activityIdList1.add(activityId);
							break;
						case "2":
							activityIdList2.add(activityId);
							break;
						case "3":
							activityIdList3.add(activityId);
							break;
						case "4":
							activityIdList4.add(activityId);
							break;
						default:
							break;
					}
				}
			}
		}
		log.debug("投票问卷：" + activityIdList1);
		log.debug("头脑风暴：" + activityIdList2);
		log.debug("答疑讨论：" + activityIdList3);
		log.debug("测试活动：" + activityIdList4);
		List<TevglActivityVoteQuestionnaire> allVoteQuestionnaireActList = new ArrayList<>();
		List<TevglActivityBrainstorming> allBrainstormingActList = new ArrayList<>();
		List<TevglActivityAnswerDiscuss> allAnswerDiscussActList = new ArrayList<>();
		List<TepExaminePaperInfo> allExaminePaperActList = new ArrayList<>();
		// 先查出这些活动
		Map<String, Object> params = new HashMap<>();
		// 投票问卷
		if (activityIdList1 != null && activityIdList1.size() > 0) {
			params.put("activityIds", activityIdList1);
			allVoteQuestionnaireActList = tevglActivityVoteQuestionnaireMapper.selectListByMap(params);
		}
		// 头脑风暴
		if (activityIdList2 != null && activityIdList2.size() > 0) {
			params.put("activityIds", activityIdList2);
			allBrainstormingActList = tevglActivityBrainstormingMapper.selectListByMap(params);
		}
		// 答疑讨论
		if (activityIdList3 != null && activityIdList3.size() > 0) {
			params.put("activityIds", activityIdList3);
			allAnswerDiscussActList = tevglActivityAnswerDiscussMapper.selectListByMap(params);
		}
		// 测试活动
		if (activityIdList4 != null && activityIdList4.size() > 0) {
			params.put("activityIds", activityIdList4);
			allExaminePaperActList = tepExaminePaperInfoMapper.selectListByMap(params);
		}
		// 遍历用户传入的数据
		log.debug("====================递归 处理用户传入的数据 begin =============");
		doRecursionAct(userInputList, chapterListTree, loginUserId,
				allVoteQuestionnaireActList,
				insertVoteQuestionnaireActList1,
				allBrainstormingActList,
				insertBrainstormingActList2,
				allAnswerDiscussActList,
				insertAnswerDiscussActList3,
				allExaminePaperActList,
				insertExaminePaperInfoActList4);
		log.debug("====================递归 处理用户传入的数据 end =============");
		log.debug("等待入库的数据如下");
		log.debug("投票问卷：" + insertVoteQuestionnaireActList1);
		log.debug("头脑风暴：" + insertBrainstormingActList2);
		log.debug("答疑讨论：" + insertAnswerDiscussActList3);
		log.debug("测试活动：" + insertExaminePaperInfoActList4);
		//
		// 复制没有关联章节的活动
		if (activityList != null && activityList.size() > 0) {
			for (int i = 0; i < activityList.size(); i++) {
				JSONObject actObj = activityList.getJSONObject(i);
				String activityId = actObj.getString("activityId");
				String activityType = actObj.getString("activityType");
				String newChapterId = null;
				switch (activityType) {
					case "1":
						List<TevglActivityVoteQuestionnaire> list1 = allVoteQuestionnaireActList.stream().filter(a -> a.getActivityId().equals(activityId)).collect(Collectors.toList());
						if (list1 != null && list1.size() > 0) {
							TevglActivityVoteQuestionnaire t = list1.get(0);
							t.setActivityId(Identities.uuid());
							t.setChapterId(newChapterId);
							t.setActivityState("0");
							t.setCreateTime(DateUtils.getNowTimeStamp());
							t.setCreateUserId(loginUserId);
							t.setUpdateTime(null);
							t.setUpdateUserId(null);
							t.setSortNum(i);
							t.setOldActivityId(activityId);
							insertVoteQuestionnaireActList1.add(t);
						}
						break;
					case "2":
						List<TevglActivityBrainstorming> list2 = allBrainstormingActList.stream().filter(a -> a.getActivityId().equals(activityId)).collect(Collectors.toList());
						if (list2 != null && list2.size() > 0) {
							TevglActivityBrainstorming t = list2.get(0);
							t.setActivityId(Identities.uuid());
							t.setChapterId(newChapterId);
							t.setActivityState("0");
							t.setCreateTime(DateUtils.getNowTimeStamp());
							t.setCreateUserId(loginUserId);
							t.setUpdateTime(null);
							t.setUpdateUserId(null);
							t.setSortNum(i);
							insertBrainstormingActList2.add(t);
						}
						break;
					case "3":
						List<TevglActivityAnswerDiscuss> list3 = allAnswerDiscussActList.stream().filter(a -> a.getActivityId().equals(activityId)).collect(Collectors.toList());
						if (list3 != null && list3.size() > 0) {
							TevglActivityAnswerDiscuss t = list3.get(0);
							t.setActivityId(Identities.uuid());
							t.setChapterId(newChapterId);
							t.setActivityState("0");
							t.setCreateTime(DateUtils.getNowTimeStamp());
							t.setCreateUserId(loginUserId);
							t.setUpdateTime(null);
							t.setUpdateUserId(null);
							t.setSortNum(i);
							insertAnswerDiscussActList3.add(t);
						}
						break;
					case "4":
						List<TepExaminePaperInfo> list4 = allExaminePaperActList.stream().filter(a -> a.getPaperId().equals(activityId)).collect(Collectors.toList());
						if (list4 != null && list4.size() > 0) {
							TepExaminePaperInfo t = list4.get(0);
							t.setPaperId(Identities.uuid());
							t.setChapterId(newChapterId);
							t.setCreateTime(DateUtils.getNowTimeStamp());
							t.setCreateUserId(loginUserId);
							t.setUpdateTime(null);
							t.setUpdateUserId(null);
							t.setPaperBeginTime(null);
							t.setOldActivityId(activityId);
							insertExaminePaperInfoActList4.add(t);
						}
						break;
					default:
						break;
				}
			}
		}
		// 等待入库建立教学包与活动关系的集合
		List<TevglPkgActivityRelation> relationList = new ArrayList<TevglPkgActivityRelation>();
		// 投票问卷
		if (insertVoteQuestionnaireActList1 != null && insertVoteQuestionnaireActList1.size() > 0) {
			log.debug("投票问卷入库中：" + insertVoteQuestionnaireActList1.size());
			insertVoteQuestionnaireActList1.stream().forEach(actInfo -> {
				copyActivity1(newPkgId, actInfo);
				TevglPkgActivityRelation t = new TevglPkgActivityRelation();
				t.setPaId(Identities.uuid());
				t.setPkgId(newPkgId);
				t.setActivityId(actInfo.getActivityId());
				t.setActivityBeginTime(null);
				t.setActivityEndTime(null);
				t.setActivityState("0");
				t.setActivityType(GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
				relationList.add(t);
			});
		}
		// 头脑风暴
		if (insertBrainstormingActList2 != null && insertBrainstormingActList2.size() > 0) {
			// 头脑风暴-批量入库
			tevglActivityBrainstormingMapper.insertBatch(insertBrainstormingActList2);
			// 建立关系
			insertBrainstormingActList2.stream().forEach(actInfo -> {
				TevglPkgActivityRelation t = new TevglPkgActivityRelation();
				t.setPaId(Identities.uuid());
				t.setPkgId(newPkgId);
				t.setActivityId(actInfo.getActivityId());
				t.setActivityBeginTime(null);
				t.setActivityEndTime(null);
				t.setActivityState("0");
				t.setActivityType(GlobalActivity.ACTIVITY_2_BRAINSTORMING);
				relationList.add(t);
			});
		}
		// 答疑讨论
		if (insertAnswerDiscussActList3 != null && insertAnswerDiscussActList3.size() > 0) {
			// 答疑讨论-批量入库
			tevglActivityAnswerDiscussMapper.insertBatch(insertAnswerDiscussActList3);
			// 建立关系
			insertAnswerDiscussActList3.stream().forEach(actInfo -> {
				TevglPkgActivityRelation t = new TevglPkgActivityRelation();
				t.setPaId(Identities.uuid());
				t.setPkgId(newPkgId);
				t.setActivityId(actInfo.getActivityId());
				t.setActivityBeginTime(null);
				t.setActivityEndTime(null);
				t.setActivityState("0");
				t.setActivityType(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS);
				t.setGroupId(Identities.uuid());
				relationList.add(t);
			});
		}
		// 测试活动
		if (insertExaminePaperInfoActList4 != null && insertExaminePaperInfoActList4.size() > 0) {
			insertExaminePaperInfoActList4.stream().forEach(actInfo -> {
				copyActivity4(newPkgId, actInfo);
				// 建立关系
				TevglPkgActivityRelation t = new TevglPkgActivityRelation();
				t.setPaId(Identities.uuid());
				t.setPkgId(newPkgId);
				t.setActivityId(actInfo.getPaperId());
				t.setActivityBeginTime(null);
				t.setActivityEndTime(null);
				t.setActivityState("0");
				t.setActivityType(GlobalActivity.ACTIVITY_4_TEST_ACT);
				relationList.add(t);
			});
		}
		// 教学包与活动关系
		if (relationList != null && relationList.size() > 0) {
			tevglPkgActivityRelationMapper.insertBatch(relationList);
			// 更新活动数量
			TevglPkgInfo p = new TevglPkgInfo();
			p.setPkgId(newPkgId);
			p.setPkgActCount(relationList.size());
			tevglPkgInfoMapper.plusNum(p);
		}
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  发布教学包中-处理跟章节挂钩的活动 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}

	private void doRecursionAct(JSONArray userInputList, List<TevglBookChapter> chapterListTree, String loginUserId
			, List<TevglActivityVoteQuestionnaire> allVoteQuestionnaireActList
		    , List<TevglActivityVoteQuestionnaire> insertVoteQuestionnaireActList1
			, List<TevglActivityBrainstorming> allBrainstormingActList
			, List<TevglActivityBrainstorming> insertBrainstormingActList2
			, List<TevglActivityAnswerDiscuss> allAnswerDiscussActList
			, List<TevglActivityAnswerDiscuss> insertAnswerDiscussActList3
			, List<TepExaminePaperInfo> allExaminePaperActList
			, List<TepExaminePaperInfo> insertExaminePaperInfoActList4
			){
		for (int i = 0; i < userInputList.size(); i++) {
			// 获得当前被选择的章节相关数据
			JSONObject data = userInputList.getJSONObject(i);
			String chapterId = data.getString("chapterId");
			// 遍历具有层次结构的章节数据
			for (int c = 0; c < chapterListTree.size(); c++) {
				// 如果匹配上了
				TevglBookChapter tevglBookChapter = chapterListTree.get(c);
				String newChapterId = tevglBookChapter.getNewChapterId();
				if (chapterId.equals(tevglBookChapter.getChapterId())) {
					// TODO 如果选择了活动
					JSONArray activityArray = data.getJSONArray("activityArray");
					//log.debug("如果选择了活动：" + activityArray);
					if (activityArray != null && activityArray.size() > 0) {
						for (int n = 0; n < activityArray.size(); n++) {
							JSONObject actObj = activityArray.getJSONObject(n);
							String activityId = actObj.getString("activityId");
							String activityType = actObj.getString("activityType");
							switch (activityType) {
								case "1":
									List<TevglActivityVoteQuestionnaire> list1 = allVoteQuestionnaireActList.stream().filter(a -> a.getActivityId().equals(activityId)).collect(Collectors.toList());
									if (list1 != null && list1.size() > 0) {
										// 赋值之后，重置部分数据
										TevglActivityVoteQuestionnaire t = list1.get(0);
										t.setActivityId(Identities.uuid());
										t.setChapterId(newChapterId);
										t.setActivityState("0");
										t.setCreateTime(DateUtils.getNowTimeStamp());
										t.setCreateUserId(loginUserId);
										t.setUpdateTime(null);
										t.setUpdateUserId(null);
										t.setSortNum(n);
										t.setOldActivityId(activityId);
										insertVoteQuestionnaireActList1.add(t);
									}
									break;
								case "2":
									List<TevglActivityBrainstorming> list2 = allBrainstormingActList.stream().filter(a -> a.getActivityId().equals(activityId)).collect(Collectors.toList());
									if (list2 != null && list2.size() > 0) {
										// 赋值之后，重置部分数据
										TevglActivityBrainstorming t = list2.get(0);
										t.setActivityId(Identities.uuid());
										t.setChapterId(newChapterId);
										t.setActivityState("0");
										t.setCreateTime(DateUtils.getNowTimeStamp());
										t.setCreateUserId(loginUserId);
										t.setUpdateTime(null);
										t.setUpdateUserId(null);
										t.setSortNum(n);
										t.setActivityBeginTime(null);
										t.setActivityEndTime(null);
										t.setAnswerNumber(0);
										t.setState("Y");
										insertBrainstormingActList2.add(t);
									}
									break;
								case "3":
									List<TevglActivityAnswerDiscuss> list3 = allAnswerDiscussActList.stream().filter(a -> a.getActivityId().equals(activityId)).collect(Collectors.toList());
									if (list3 != null && list3.size() > 0) {
										// 赋值之后，重置部分数据
										TevglActivityAnswerDiscuss t = list3.get(0);
										t.setActivityId(Identities.uuid());
										t.setChapterId(newChapterId);
										t.setActivityState("0");
										t.setCreateTime(DateUtils.getNowTimeStamp());
										t.setCreateUserId(loginUserId);
										t.setUpdateTime(null);
										t.setUpdateUserId(null);
										t.setSortNum(n);
										insertAnswerDiscussActList3.add(t);
									}
									break;
								case "4":
									List<TepExaminePaperInfo> list4 = allExaminePaperActList.stream().filter(a -> a.getPaperId().equals(activityId)).collect(Collectors.toList());
									if (list4 != null && list4.size() > 0) {
										TepExaminePaperInfo t = list4.get(0);
										t.setPaperId(Identities.uuid());
										t.setChapterId(newChapterId);
										t.setCreateTime(DateUtils.getNowTimeStamp());
										t.setCreateUserId(loginUserId);
										t.setUpdateTime(null);
										t.setUpdateUserId(null);
										t.setPaperBeginTime(null);
										t.setOldActivityId(activityId);
										t.setPaperState("Y");
										insertExaminePaperInfoActList4.add(t);
									}
									break;
								default:
									break;
							}
						}
					}
					doRecursionAct(userInputList, tevglBookChapter.getChildren(), loginUserId,
							allVoteQuestionnaireActList,
							insertVoteQuestionnaireActList1,
							allBrainstormingActList,
							insertBrainstormingActList2,
							allAnswerDiscussActList,
							insertAnswerDiscussActList3,
							allExaminePaperActList,
							insertExaminePaperInfoActList4);
				}
			}
		}
	}

	

	

	

	/**
	 * 复制-投票问卷-相关数据
	 * @param newPkgId
	 * @param vo
	 */
	private void copyActivity1(String newPkgId, TevglActivityVoteQuestionnaire vo){
		String oldActivityId = vo.getOldActivityId();
		tevglActivityVoteQuestionnaireMapper.insert(vo);
		// 找到所有题目
		List<TevglActivityVoteQuestionnaireQuestion> questionList = tevglActivityVoteQuestionnaireQuestionMapper.selectActivityQuestionList(oldActivityId);
		if (questionList != null && questionList.size() > 0) {
			List<String> questionIds = questionList.stream().map(a -> a.getQuestionId()).collect(Collectors.toList());
			// 找到所有选项
			List<TevglActivityVoteQuestionnaireQuestionOption> optionList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectOptionListByQuestionIds(questionIds);
			questionList.stream().forEach(a -> {
				List<TevglActivityVoteQuestionnaireQuestionOption> children = optionList.stream().filter(optionInfo -> optionInfo.getQuestionId().equals(a.getQuestionId())).collect(Collectors.toList());
				a.setChildren(children);
			});
		}
		String newActivityId = vo.getActivityId();
		// 保存活动与教学包的关系
		//pkgActivityUtils.buildRelation(newPkgId, newActivityId, GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE);
		// 更新教学包活动数量
		//pkgUtils.plusPkgActivityNum(newPkgId);
		// 保存新活动的题目
		if (questionList != null && questionList.size() > 0) {
			// 以题目主键作为key， 选项作为value
			Map<String, Object> optionData = new HashMap<String, Object>();
			// 遍历题目
			questionList.stream().forEach(questionInfo -> {
				TevglActivityVoteQuestionnaireQuestion qq = new TevglActivityVoteQuestionnaireQuestion();
				qq = questionInfo;
				qq.setQuestionId(Identities.uuid());
				qq.setActivityId(newActivityId);
				qq.setCreateTime(DateUtils.getNowTimeStamp());
				tevglActivityVoteQuestionnaireQuestionMapper.insert(qq);
				String newQuestionId = qq.getQuestionId();
				// 保存题目选项
				List<TevglActivityVoteQuestionnaireQuestionOption> children = questionInfo.getChildren();
				if (children != null && children.size() > 0) {
					optionData.put(newQuestionId, children);
				}
			});
			// 前面生成题目之后,随之生成题目的选项
			for (Map.Entry<String, Object> op : optionData.entrySet()) {
				String newQuestionId = op.getKey();
				@SuppressWarnings("unchecked")
				List<TevglActivityVoteQuestionnaireQuestionOption> children = (List<TevglActivityVoteQuestionnaireQuestionOption>)op.getValue();
				children.forEach(optionInfo -> {
					TevglActivityVoteQuestionnaireQuestionOption option = new TevglActivityVoteQuestionnaireQuestionOption();
					option = optionInfo;
					option.setOptionId(Identities.uuid());
					option.setQuestionId(newQuestionId);
					tevglActivityVoteQuestionnaireQuestionOptionMapper.insert(option);
				});
			}
		}
	}
	
	/**
	 * 复制测试活动
	 * @param newPkgId
	 * @param t
	 */
	private void copyActivity4(String newPkgId, TepExaminePaperInfo t){
		String oldActivityId = t.getOldActivityId();
		// 保存活动至数据库
		tepExaminePaperInfoMapper.insert(t);
		String newActivityId = t.getPaperId();
		// TODO 复制试卷题目数据
		Map<String, Object> params = new HashMap<>();
		params.put("paperId", oldActivityId);
		List<TepExaminePaperQuestionsDetail> paperQuestionsDetailList = tepExaminePaperQuestionsDetailMapper.selectListByMap(params);
		if (paperQuestionsDetailList != null && paperQuestionsDetailList.size() > 0) {
			// 等待入库的集合
			List<TepExaminePaperQuestionsDetail> list = new ArrayList<TepExaminePaperQuestionsDetail>();
			for (TepExaminePaperQuestionsDetail detail : paperQuestionsDetailList) {
				TepExaminePaperQuestionsDetail tepExaminePaperQuestionsDetail = new TepExaminePaperQuestionsDetail();
				tepExaminePaperQuestionsDetail = detail;
				tepExaminePaperQuestionsDetail.setDetailId(Identities.uuid());
				tepExaminePaperQuestionsDetail.setPaperId(newActivityId);
				//tepExaminePaperQuestionsDetailMapper.insert(tepExaminePaperQuestionsDetail);
				list.add(tepExaminePaperQuestionsDetail);
			}
			tepExaminePaperQuestionsDetailMapper.insertBatch(list);
		}
		// 保存活动与教学包的关系
		//pkgActivityUtils.buildRelation(newPkgId, newActivityId, GlobalActivity.ACTIVITY_4_TEST_ACT);
		// 更新教学包活动数量
		//pkgUtils.plusPkgActivityNum(newPkgId);
	}

	/**
	 * 处理
	 * @param newPkgId 必传参数，复制被选教学包后生成的新的教学包主键，必传参数
	 * @param subjectIdNew 必传参数，新的教学包对应的课程主键，必传参数
	 * @param loginUserId 当前登录用户
	 * @param userInputList 必传参数，前端用户，传入的数据
	 * @param allChapterList 所有章节数据
	 * @param chapterListTree 必传参数，被选教学包的课程主键查出的所有章节（具有层次机构）
	 * @return
	 */
	private R handleDatas(String newPkgId, String subjectIdNew, String loginUserId,
						  JSONArray userInputList,
						  List<TevglBookChapter> allChapterList,
						  List<TevglBookChapter> chapterListTree) {
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  发布教学包中-处理章节、分组、资源 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		Map<String, Object> params = new HashMap<>();
		// 分组
		List<TevglPkgResgroup> allTevglPkgResgroupList = new ArrayList<TevglPkgResgroup>();
		// 资源
		List<TevglPkgRes> allTevglPkgResList = new ArrayList<TevglPkgRes>();
		List<String> chapterIdList = allChapterList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
		params.put("chapterIds", chapterIdList);
		allTevglPkgResgroupList = tevglPkgResgroupMapper.selectListByMap(params);
		if (allTevglPkgResgroupList != null && allTevglPkgResgroupList.size() > 0) {
			List<String> resgroupIdList = allTevglPkgResgroupList.stream().map(a -> a.getResgroupId()).collect(Collectors.toList());
			params.clear();
			params.put("resgroupIds", resgroupIdList);
			allTevglPkgResList = tevglPkgResMapper.selectListByMap(params);
		}
		// 等待入库的数据
		List<TevglBookChapter> insertChapterList = new ArrayList<TevglBookChapter>();
		List<TevglPkgResgroup> insertResgroupList = new ArrayList<TevglPkgResgroup>();
		List<TevglPkgRes> insertResList = new ArrayList<TevglPkgRes>();
		// 递归处理数据
		log.debug("-------------------------------------------------- 递归 begin --------------------------------------------------");
		doRecursionHandleChapterAndResgroupInfo(newPkgId, subjectIdNew, subjectIdNew, loginUserId,
				userInputList, chapterListTree, allTevglPkgResgroupList, allTevglPkgResList,
				insertChapterList, insertResgroupList, insertResList);
		log.debug("-------------------------------------------------- 递归 end --------------------------------------------------");
		// 入库
		if (insertChapterList != null && insertChapterList.size() > 0) {
			tevglBookChapterMapper.insertBatch(insertChapterList);
			log.debug("章节入库完毕......");
		}
		if (insertResgroupList != null && insertResgroupList.size() > 0) {
			tevglPkgResgroupMapper.insertBatch(insertResgroupList);
			log.debug("分组入库完毕......");
		}
		if (insertResList != null && insertResList.size() > 0) {
			tevglPkgResMapper.insertBatch(insertResList);
			log.debug("资源入库完毕......");
		}
		// 更新教学包的资源数
		pkgUtils.plusPkgResNum(newPkgId, insertResgroupList.size());
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  发布教学包中-处理章节、分组、资源 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
		return R.ok("章节-分组-资源数据处理成功")
				.put("chapterListTree", chapterListTree)
				.put("insertChapterList", insertChapterList)
				.put("insertResgroupList", insertResgroupList)
				.put("insertResList", insertResList)
				;
	}

	/**
	 * 处理教学包是否只能自用
	 * @param pkgInfo
	 * @param loginUserId
	 * @return
	 */
	private String handlePrivateUse(TevglPkgInfo pkgInfo, String loginUserId) {
		String pkgId = pkgInfo.getPkgId();
		String privateUse = "N";
		boolean flag = false;
		// 如果来源教学包已经属于自用，直接认为自用
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			TevglPkgInfo refTevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgInfo.getRefPkgId());
			if (refTevglPkgInfo != null) {
				if (StrUtils.isNotEmpty(refTevglPkgInfo.getPrivateUse()) && "Y".equals(refTevglPkgInfo.getPrivateUse())) {
					privateUse = "Y";
				} else {
					flag = isFromOtherUser(pkgId, loginUserId);
					log.debug("是否应用了别人的教学包：" + flag);
					if (flag) {
						privateUse = "Y";
					}
				}
			}
		}
		return privateUse;
	}


	/**
	 *
	 * @param newPkgId 必传参数，复制被选教学包后生成的新的教学包主键，必传参数
	 * @param subjectId 必传参数，新的教学包对应的课程主键，必传参数
	 * @param parentId 必传参数，父节点，必传参数
	 * @param loginUserId 必传参数，当前登录用户
	 * @param userInputList 必传参数，前端用户，传入的数据
	 * @param chapterListTree 必传参数，被选教学包的课程主键查出的所有章节（具有层次机构）
	 * @param allTevglPkgResgroupList 全部的分组
	 * @param allTevglPkgResList 全部的资源
	 * @param insertChapterList 等待入库的章节集合
	 * @param insertResgroupList 等待入库的分组集合
	 * @param insertResList 等待入库的资源集合
	 * @apiNote
	 * userInputList 的数据格式要求如下
	 * [
	 *     {"chapterId-value": "", "resgroupIdArray": ["", ""], "activityArray": ["", ""]}
	 * ]
	 */
	private void doRecursionHandleChapterAndResgroupInfo(String newPkgId, String subjectId, String parentId, String loginUserId
		  	, JSONArray userInputList
			, List<TevglBookChapter> chapterListTree
			, List<TevglPkgResgroup> allTevglPkgResgroupList
			, List<TevglPkgRes> allTevglPkgResList
			, List<TevglBookChapter> insertChapterList
			, List<TevglPkgResgroup> insertResgroupList
			, List<TevglPkgRes> insertResList) {
		if (StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(subjectId)
				|| StrUtils.isEmpty(parentId) || userInputList == null || userInputList.size() == 0) {
			return;
		}
		log.debug("递归中...");
		// 遍历用户传入的数据
		for (int i = 0; i < userInputList.size(); i++) {
			// 获得当前被选择的章节相关数据
			JSONObject data = userInputList.getJSONObject(i);
			String chapterId = data.getString("chapterId");
			// 遍历具有层次结构的章节数据
			for (int c = 0; c < chapterListTree.size(); c++) {
				// 如果匹配上了
				TevglBookChapter tevglBookChapter = chapterListTree.get(c);
				if (chapterId.equals(tevglBookChapter.getChapterId())) {
					// 填充信息，并加入到等待入库的集合
					TevglBookChapter t = fillMyChapterInfo(subjectId, parentId, tevglBookChapter, loginUserId);
					t.setOrderNum(c);
					if (!insertChapterList.contains(t)) {
						insertChapterList.add(t);
					}
					// 设置一下
					tevglBookChapter.setNewChapterId(t.getChapterId());
					// 拿当前用户选择的章节中，被选的分组，然后从全部的分组中取出对应的数据
					JSONArray resgroupIdArray = data.getJSONArray("resgroupIdArray");
					if (resgroupIdArray != null && resgroupIdArray.size() > 0) {
						List<TevglPkgResgroup> tevglPkgResgroupList = allTevglPkgResgroupList.stream().filter(a -> a.getChapterId().equals(tevglBookChapter.getChapterId())).collect(Collectors.toList());
						if (tevglPkgResgroupList != null && tevglPkgResgroupList.size() > 0) {
							// 遍历分组
							for (int j = 0; j < resgroupIdArray.size(); j++) {
								for (int k = 0; k < tevglPkgResgroupList.size(); k++) {
									// 如果匹配, 分组入库
									if (resgroupIdArray.get(j).equals(tevglPkgResgroupList.get(k).getResgroupId())) {
										doSaveResgroup(t.getChapterId(), tevglPkgResgroupList.get(k), newPkgId, j, subjectId, allTevglPkgResList, insertResgroupList, insertResList);
									}
								}
							}
						}
					}

					// 递归处理
					doRecursionHandleChapterAndResgroupInfo(newPkgId, subjectId, t.getChapterId(), loginUserId,
							userInputList, tevglBookChapter.getChildren(), allTevglPkgResgroupList, allTevglPkgResList,
							insertChapterList, insertResgroupList, insertResList);
				}
			}
		}
	}

	/**
	 * 保存一个新的教学包基本信息
	 * @param jsonObject
	 * @param newSubjectId 新的课程主键
	 * @param loginUserId 当前登录用户
	 * @return 返回新增后的教学包主键
	 */
	private String doSavePkgInfo(JSONObject jsonObject, String newSubjectId, String loginUserId, String privateUse, TevglPkgInfo tevglPkgInfo, String appUserId) {
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && tevglPkgInfo.getCreateUserId().equals(appUserId);
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && tevglPkgInfo.getReceiverUserId().equals(appUserId);
		String majorId = tevglPkgInfo.getMajorId();
		String value = jsonObject.getString("neeCheckPkgValue");
		// 目标教学包
		String pkgId = jsonObject.getString("pkgId");
		// 被勾选的资源分组
		JSONArray resgroupIdArray = jsonObject.getJSONArray("resgroupIdArray");
		// 被勾选的活动
		JSONArray activityArray = jsonObject.getJSONArray("activityArray");
		// 录入的一些基本信息
		String pkgName = jsonObject.getString("pkgName");
		String pkgKey = jsonObject.getString("pkgKey");
		String pkgDesc = jsonObject.getString("pkgDesc");
		String pkgRemark = jsonObject.getString("pkgRemark");
		String pkgLevel = jsonObject.getString("pkgLevel");
		String pkgLimit = jsonObject.getString("pkgLimit");
		String pkgLogo = jsonObject.getString("pkgLogo");
		String deployMainType = jsonObject.getString("deployMainType");
		String deploySubType = jsonObject.getString("deploySubType");
		String pkgVersion = jsonObject.getString("pkgVersion");
		String versionRemark = jsonObject.getString("versionRemark");
		String pkgTraineeName = jsonObject.getString("pkgTraineeName");
		String pkgTraineeQq = jsonObject.getString("pkgTraineeQq");
		String pkgTraineeMobile = jsonObject.getString("pkgTraineeMobile");
		String pkgTraineeWx = jsonObject.getString("pkgTraineeWx");
		String pkgTraineeEmail = jsonObject.getString("pkgTraineeEmail");
		// 实例化对象并填充信息
		TevglPkgInfo pkgInfo = new TevglPkgInfo();
		pkgInfo.setPkgId(Identities.uuid()); // 主键
		pkgInfo.setCreateUserId(loginUserId); // 创建者
		pkgInfo.setMajorId(majorId);
		pkgInfo.setSubjectId(newSubjectId); // 对应的课程
		pkgInfo.setState("Y"); // 状态Y有效N无效
		pkgInfo.setShowIndex("N"); // 是否首页显示
		pkgInfo.setDeployTime(DateUtils.getNowTimeStamp()); // 发布时间
		pkgInfo.setPkgName(pkgName); // 教学包名称
		pkgInfo.setPkgVersion(pkgVersion); // 教学包版本
		pkgInfo.setLastVersionPkg(pkgId); // 上一版本教学包id
		pkgInfo.setRefPkgId(pkgId); // 教学包来源
		pkgInfo.setPkgResCount(resgroupIdArray == null ? 0 : resgroupIdArray.size()); // 资源数量
		pkgInfo.setPkgActCount(activityArray == null ?  0 :activityArray.size()); // 活动数量
		pkgInfo.setPkgRefCount(0); // 引用数重置为0
		pkgInfo.setPkgKey(pkgKey); // 关键字
		pkgInfo.setPkgDesc(pkgDesc); // 简介
		pkgInfo.setPkgRemark(pkgRemark); // 详细介绍
		pkgInfo.setPkgLevel(pkgLevel); // 适用层次
		pkgInfo.setPkgLimit(pkgLimit); // 使用限制(来源字典：1授权，2免费)
		//pkgInfo.setPkgLogo(pkgLogo); // 封面
		pkgInfo.setPkgLogo(handleLogo(pkgId, pkgLogo)); // 封面
		pkgInfo.setDeployMainType(deployMainType); // 发布方大类
		pkgInfo.setDeploySubType(deploySubType); // 发布方小类
		pkgInfo.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
		// 联系方式
		pkgInfo.setPkgTraineeName(pkgTraineeName);
		pkgInfo.setVersionRemark(versionRemark);
		pkgInfo.setPkgTraineeQq(pkgTraineeQq);
		pkgInfo.setPkgTraineeMobile(pkgTraineeMobile);
		pkgInfo.setPkgTraineeWx(pkgTraineeWx);
		pkgInfo.setPkgTraineeEmail(pkgTraineeEmail);
		// 排序号处理
		Map<String, Object> ps = new HashMap<>();
		ps.put("createUserId", loginUserId);
		pkgInfo.setSortNum(tevglPkgInfoMapper.getMaxSortNum(ps));
		// TODO 开发阶段直接设为已发布，实则需要平台来审核，发布状态
		String releaseStatus = (StrUtils.isNotEmpty(value) && "N".equals(value)) ? "Y" : "N";
		pkgInfo.setReleaseStatus(releaseStatus);
		// ****
		pkgInfo.setPrivateUse(privateUse);
		// 创建者/接管者
		if (isCreator) {
			pkgInfo.setCreateUserId(loginUserId);
			pkgInfo.setReceiverUserId(null);
		}
		if (isReceiver) {
			pkgInfo.setReceiverUserId(appUserId);
		}
		// 教材类型
		pkgInfo.setPkgBookType(tevglPkgInfo.getPkgBookType());
		// 入库
		tevglPkgInfoMapper.insert(pkgInfo);
		return pkgInfo.getPkgId();
	}
	
	/**
	 * <b>发布教学包时，处理教学包封面</b>
	 * @param pkgId 目标教学包，基于这个教学包去衍生出子教学包
	 * @param pkgLogo 用户上传的封面图片
	 * @param
	 * @return
	 * @apiNote
	 * 封面不要求必传，如果没有传，则使用最新上一版本的封面即可
	 */
	private String handleLogo(String pkgId, String pkgLogo) {
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null || StrUtils.isNotEmpty(pkgLogo)) {
			return pkgLogo;
		}
		// 如果是源教学包（看作是在教学包页面去基于源教学包发布）
		if (StrUtils.isEmpty(tevglPkgInfo.getRefPkgId()) || tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
			// 按时间降序，找到最新的教学包
			String latestLogo = tevglPkgInfoMapper.findTheLatestPackageLogo(pkgId);
			// 如果没有找到，则使用源教学包的
			latestLogo = StrUtils.isEmpty(latestLogo) ? tevglPkgInfo.getPkgLogo() : latestLogo;
			log.info("准备复制的图片 => {}", latestLogo);
			return doCopyLogo(latestLogo);
		} else { // 否则，相对于在课堂页面中发布教学包
			// 直接设为为父级教学包的封面图
			return doCopyLogo(tevglPkgInfo.getPkgLogo());
		}
	}

	private String doCopyLogo(String latestLogo) {
		if (StrUtils.isNotEmpty(latestLogo)) {
			String newFileName = "";
			// 在磁盘上，找到该文件，复制一份出来
			String abPath = uploadPathUtils.getAbsolutePath(latestLogo, "12");
			log.info("文件路径 => {}", abPath);
			File srcFile = new File(abPath);
			if (srcFile.isFile()) {
				newFileName = Identities.uuid() + latestLogo.substring(latestLogo.lastIndexOf("."));
				String newPath = uploadPath + uploadPathUtils.getPathByParaNo("12") + "/" + newFileName;
				File destFile = new File(newPath);
				try {
					FileUtils.copyFile(srcFile, destFile);
				} catch (IOException e) {
					log.error("发布教学包时，生成最新图片失败", e);
				}
			}
			if (StrUtils.isNotEmpty(newFileName)) {
				return newFileName;
			}
		}
		return latestLogo;
	}
	
	/**
	 * 保存一份新的课程基本数据
	 * @param subjectId 源课程id
	 * @param loginUserId 当前登录用户id
	 * @return 返回新增后的课程主键
	 */
	private String doSaveSubjectInfo(String subjectId, String loginUserId) {
		TevglBookSubject subjectInfo = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (subjectInfo == null) {
			log.debug("无效的课程");
			return null; 
		}
		TevglBookSubject t = subjectInfo;
		t.setSubjectId(Identities.uuid());
		t.setCreateUserId(loginUserId);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		// 生成新的课程,并得到新的课程ID
		tevglBookSubjectMapper.insert(t);
		return t.getSubjectId();
	}
	
	/**
	 * 验证处理这个教学包是不是只能自己用
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	private boolean isFromOtherUser(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return true;
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return true;
		}
		if (loginUserId.equals(pkgInfo.getCreateUserId())) {
			if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			//if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay()) && !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
				// 兼容下需求变更引起的专设本课堂教学包问题
                if (Arrays.asList("bdd533929612421dae212d4b1950d64d", "c0ea18bb858547a39283053169c03270", "1a3286faaead47d29731e1b316b7c6ed").contains(pkgInfo.getRefPkgId())) {
                    return false;
                }
				return isFromOtherUser(pkgInfo.getRefPkgId(), loginUserId);
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * 获取源课程的所有有效章节数据
	 * @param subjectId
	 * @return
	 */
	private List<TevglBookChapter> getChapterListData(String subjectId){
		// 获取课程的树形数据
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sidx", "order_num");
		map.put("order", "asc");
		map.put("state", "Y");
		map.put("subjectId", subjectId); // 课程ID
		// 查出该书所有的章节
		List<TevglBookChapter> chapterList = tevglBookChapterMapper.selectListByMap(map);
		return chapterList;
	}
	
	/**
	 * 版本号与教学包名称唯一校验
	 * @param pkgName
	 * @param pkgVersion
	 * @param loginUserId
	 */
	private R checkSoleNameAndVersion(String pkgName, String pkgVersion, String loginUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("pkgName", pkgName);
		params.put("pkgVersion", pkgVersion);
		params.put("createUserId", loginUserId);
		List<Map<String,Object>> list = tevglPkgInfoMapper.selectSimpleList(params);
		if (list != null && list.size() > 0) {
			return R.error("请调整[版本号]或者修改[教学包名称]（注：由于已经存在同名同版本的）");
		}
		return R.ok();
	}

	/**
	 * <p>
	 * 填充章节信息
	 * </p>
	 *
	 * @author huj
	 * @data 2019年7月9日
	 * @param subjectId        课程ID
	 * @param parentId         父ID
	 * @param tevglBookChapter 章节对象
	 * @return
	 */
	private TevglBookChapter fillMyChapterInfo(String subjectId, String parentId, TevglBookChapter tevglBookChapter,
											   String loginUserId) {
		TevglBookChapter obj = new TevglBookChapter();
		obj.setChapterId(Identities.uuid()); // 章节主键ID
		obj.setSubjectId(subjectId); // 所属课程ID
		obj.setParentId(parentId); // 父章节ID
		obj.setCreateUserId(loginUserId); // 创建人
		obj.setChapterNo(tevglBookChapter.getChapterNo()); // 章节编号
		obj.setLevel(tevglBookChapter.getLevel()); // 层级
		obj.setChapterName(tevglBookChapter.getChapterName()); // 章节名称
		obj.setChapterIcon(tevglBookChapter.getChapterIcon()); // 章节小图标
		obj.setChapterDesc(tevglBookChapter.getChapterDesc()); // 章节简介(文本)
		obj.setChapterContent(tevglBookChapter.getChapterContent()); // 章节内容(富文本)
		obj.setClassHour(tevglBookChapter.getClassHour()); // 章节课时
		obj.setOrderNum(tevglBookChapter.getOrderNum()); // 排序号
		obj.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
		obj.setState(tevglBookChapter.getState()); // 状态(Y有效N无效)
		return obj;
	}

	/**
	 * 保存章节对应的分组,资源
	 * @param newChapterId 新的章节id
	 * @param tevglPkgResgroup 源章节的所有分组
	 * @param newPkgId 新的教学包
	 * @param sortNum 排序号
	 */
	private void doSaveResgroup(String newChapterId, TevglPkgResgroup tevglPkgResgroup, String newPkgId, int sortNum, String subjectId
			, List<TevglPkgRes> allTevglPkgResList
			, List<TevglPkgResgroup> insertResgroupList
			, List<TevglPkgRes> insertResList) {
		if (StrUtils.isEmpty(newChapterId) || StrUtils.isEmpty(newPkgId)) {
			return;
		}
		if (tevglPkgResgroup == null) {
			return;
		}
		// 查询分组对应的资源
		//TevglPkgRes pkgRes = tevglPkgResMapper.selectObjectByResgroupId(tevglPkgResgroup.getResgroupId());
		List<TevglPkgRes> list = allTevglPkgResList.stream().filter(a -> a.getResgroupId().equals(tevglPkgResgroup.getResgroupId())).collect(Collectors.toList());
		if (list == null || list.size() == 0) {
			return;
		}
		TevglPkgRes pkgRes = list.get(0);
		// 填充新的分组信息
		TevglPkgResgroup t = new TevglPkgResgroup();
		t = tevglPkgResgroup;
		t.setResgroupId(Identities.uuid());
		t.setSortNum(sortNum);
		t.setPkgId(newPkgId);
		t.setSubjectId(subjectId);
		t.setChapterId(newChapterId);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setResgroupName(tevglPkgResgroup.getResgroupName());
		// 保存分组至数据库
		//tevglPkgResgroupMapper.insert(t);
		if (!insertResgroupList.contains(t)) {
			insertResgroupList.add(t);
		}
		// 填充新的资源信息
		TevglPkgRes pr = new TevglPkgRes();
		pr = pkgRes;
		pr.setResId(Identities.uuid());
		pr.setPkgId(newPkgId);
		pr.setResgroupId(t.getResgroupId());
		pr.setCreateTime(DateUtils.getNowTimeStamp());
		// 保存资源至数据库
		//tevglPkgResMapper.insert(pr);
		if (!insertResList.contains(pr)) {
			insertResList.add(pr);
		}
		// 更新资源数量
		//pkgUtils.plusPkgResNum(newPkgId, 1);
	}

	/**
	 * 处理云盘数据
	 * @param pkgId
	 * @param newPkgId
	 * @param loginUserId
	 * @param cloudPanList 用户选择的数据
	 */
	private void handlCloudPanDatas(String pkgId, String newPkgId, String loginUserId, JSONArray cloudPanList) {
		// 先查找此教学包的目录和文件
		List<Map<String,Object>> allList = selectListDatas(pkgId);
		// 构建成父子关系的数据格式
		List<Map<String,Object>> panList = buildTree("0", allList, 0);
		// 递归处理
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  发布教学包中-处理 云盘数据 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		doHandleCloudPanData(cloudPanList, pkgId, newPkgId, loginUserId, panList, "0");
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  发布教学包中-处理 云盘数据 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
		// 特殊点，处理直接归属于教学包的文件
		toThisPkgTopFileList(pkgId, newPkgId, loginUserId, cloudPanList);
	}
	
	/**
	 * 特殊点，处理直接归属于教学包的文件
	 * @param oldPkgId
	 * @param newPkgId
	 * @param loginUserId
	 * @param userSelectedCloudPanList
	 */
	private void toThisPkgTopFileList(String oldPkgId, String newPkgId, String loginUserId, JSONArray userSelectedCloudPanList) {
		if (StrUtils.isEmpty(oldPkgId) || StrUtils.isEmpty(newPkgId) || userSelectedCloudPanList == null || userSelectedCloudPanList.size() == 0) {
			return;
		}
		// 查询直接归属教学包的文件
		Map<String, Object> map = new HashMap<>();
		map.put("dirId", oldPkgId);
		List<Map<String, Object>> topFileList = tcloudPanFileMapper.selectSimpleInfoList(map);
		if (topFileList == null || topFileList.size() == 0) {
			return;
		}
		// 等待入库的集合
		List<TcloudPanFile> insertList = new ArrayList<TcloudPanFile>();
		String newParentId = newPkgId;
		// 遍历用户选择的文件或目录 begin
		for (int i = 0; i < userSelectedCloudPanList.size(); i++) {
			// 用户选择的某个目录或文件夹
			String id = userSelectedCloudPanList.getString(i);
			// 遍历此教学包的所有目录和文件 begin
			for (Map<String, Object> node : topFileList) {
				// 如果匹配上了 begin
				if (id.equals(node.get("id"))) {
					String name = node.get("name").toString();
					Object fileSuffix = node.get("fileSuffix");
					Object fileSize = node.get("fileSize");
					String oldAbsolutePath = cloudPanUtils.getFileSavePathByValue(oldPkgId, node.get("fileSavePath"));
					// 旧文件
					File oldFile = new File(oldAbsolutePath);
					String path2 = cloudPanUtils.getPath(newParentId, newPkgId);
					String savePath = cloudPanUtils.getSavePath(path2, name);
					// 新文件
					File newFile = new File(savePath);
					try {
						if (oldFile.exists()) {
							FileUtils.copyFile(oldFile, newFile);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					String value = cloudPanUtils.getValue(newParentId, name);
					TcloudPanFile tcloudPanFile = new TcloudPanFile();
					tcloudPanFile.setFileId(Identities.uuid());
					tcloudPanFile.setDirId(newParentId);
					tcloudPanFile.setName(name);
					tcloudPanFile.setOriginalFilename(name);
					tcloudPanFile.setFileAccessUrl(value);
					tcloudPanFile.setFileSavePath(value);
					tcloudPanFile.setFileSize(StrUtils.isNull(fileSize) ? 0 : (long)fileSize);
					tcloudPanFile.setFileType(StrUtils.isNull(node.get("fileType")) ? null : node.get("fileType").toString());
					tcloudPanFile.setFileSuffix(StrUtils.isNull(fileSuffix) ? null : fileSuffix.toString());
					tcloudPanFile.setCreateUserId(loginUserId);
					tcloudPanFile.setUpdateUserId(loginUserId);
					tcloudPanFile.setCreateTime(DateUtils.getNowTimeStamp());
					tcloudPanFile.setUpdateTime(DateUtils.getNowTimeStamp());
					tcloudPanFile.setState("Y");
					//tcloudPanFileMapper.insert(tcloudPanFile);
					if (!insertList.contains(tcloudPanFile)) {
						insertList.add(tcloudPanFile);
					}
				}
			}
		}
		if (insertList != null && insertList.size() > 0) {
			tcloudPanFileMapper.insertBatch(insertList);
		}
	}

	/**
	 * 递归复制云盘目录和文件
	 * @param cloudPanList 用户选择的目录或文件
	 * @param oldPkgId
	 * @param newPkgId
	 * @param loginUserId
	 * @param list 教学包的目录和文件（层次机构的树）
	 */
	private void doHandleCloudPanData(JSONArray cloudPanList, String oldPkgId, String newPkgId, String loginUserId, List<Map<String,Object>> list, String newParentId) {
		if (cloudPanList == null || cloudPanList.size() == 0 || list == null || list.size() == 0
				|| StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(loginUserId)) {
			return;
		}
		// 遍历用户选择的文件或目录 begin
		for (int i = 0; i < cloudPanList.size(); i++) {
			// 用户选择的某个目录或文件夹
			String id = cloudPanList.getString(i);
			// 遍历此教学包的所有目录和文件 begin
			for (Map<String, Object> node : list) {
				// 如果匹配上了 begin
				if (id.equals(node.get("id"))) {
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
					String type = node.get("type").toString();
					String name = node.get("name").toString();
					switch (type) {
						// 目录的情形
						case "1":
							// 入库
							TcloudPanDirectory t = new TcloudPanDirectory();
							t.setDirId(Identities.uuid());
							//t.setRefDirId(node.get("id").toString());
							t.setPkgId(newPkgId);
							t.setName(name);
							t.setParentId(node.get("parentId").equals("0") ? "0" : newParentId);
							t.setCreateUserId(loginUserId);
							t.setUpdateUserId(loginUserId);
							String time = DateUtils.getNowTimeStamp();
							t.setCreateTime(time);
							t.setUpdateTime(time);
							t.setState("Y");
							t.setDictCode(StrUtils.isNull(node.get("dictCode")) ? null : node.get("dictCode").toString());
							t.setIcon(StrUtils.isNull(node.get("icon")) ? null : node.get("icon").toString());
							tcloudPanDirectoryMapper.insert(t);
							// 磁盘上生成对应目录
							String res = cloudPanUtils.getPathBy(t.getDirId(), loginUserId, newPkgId);
							File path = new File(res);
							if(!path.exists()) {
								path.mkdirs();
							}
							//newParentId = t.getDirId();
							// 筛选出本目录的目录
							List<Map<String, Object>> dirList =  children.stream().filter(a -> a.get("type").equals("1") && a.get("parentId").equals(id)).collect(Collectors.toList());
							// 递归
							if (dirList != null && dirList.size() > 0) {
								doHandleCloudPanData(cloudPanList, oldPkgId, newPkgId, loginUserId, dirList, t.getDirId());
							}
							// 筛选出本目录的文件
							List<Map<String, Object>> fileList =  children.stream().filter(a -> a.get("type").equals("2") && a.get("parentId").equals(id)).collect(Collectors.toList());
							// 递归
							if (fileList != null && fileList.size() > 0) {
								doHandleCloudPanData(cloudPanList, oldPkgId, newPkgId, loginUserId, fileList, t.getDirId());
							}
							break;
						// 文件的情形
						case "2":
							Object fileSuffix = node.get("fileSuffix");
							Object fileSize = node.get("fileSize");
							String oldAbsolutePath = cloudPanUtils.getFileSavePathByValue(oldPkgId, node.get("fileSavePath"));
							// 旧文件
							File oldFile = new File(oldAbsolutePath);
							String path2 = cloudPanUtils.getPath(newParentId, newPkgId);
							String savePath = cloudPanUtils.getSavePath(path2, name);
							// 新文件
							File newFile = new File(savePath);
							try {
								if (oldFile.exists()) {
									FileUtils.copyFile(oldFile, newFile);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							String value = cloudPanUtils.getValue(newParentId, name);
							TcloudPanFile tcloudPanFile = new TcloudPanFile();
							tcloudPanFile.setFileId(Identities.uuid());
							tcloudPanFile.setDirId(newParentId);
							tcloudPanFile.setName(name);
							tcloudPanFile.setOriginalFilename(name);
							tcloudPanFile.setFileAccessUrl(value);
							tcloudPanFile.setFileSavePath(value);
							tcloudPanFile.setFileSize(StrUtils.isNull(fileSize) ? 0 : (long)fileSize);
							tcloudPanFile.setFileType(StrUtils.isNull(node.get("fileType")) ? null : node.get("fileType").toString());
							tcloudPanFile.setFileSuffix(StrUtils.isNull(fileSuffix) ? null : fileSuffix.toString());
							tcloudPanFile.setCreateUserId(loginUserId);
							tcloudPanFile.setUpdateUserId(loginUserId);
							tcloudPanFile.setCreateTime(DateUtils.getNowTimeStamp());
							tcloudPanFile.setUpdateTime(DateUtils.getNowTimeStamp());
							tcloudPanFile.setState("Y");
							tcloudPanFileMapper.insert(tcloudPanFile);
							break;
						default:
							break;
					}
				}
				// 如果匹配上了 end
			}
			// 遍历此教学包的所有目录和文件 end
		}
		// 遍历用户选择的文件或目录 end
	}

	/**
	 * 递归构建层次结构
	 * @param parentId
	 * @param allList
	 * @param level
	 * @return
	 */
	private List<Map<String, Object>> buildTree(String parentId, List<Map<String, Object>> allList, int level) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		List<Map<String, Object>> nodeList = allList.stream().filter(a -> parentId.equals(a.get("parentId"))).collect(Collectors.toList());
		if (nodeList != null && nodeList.size() > 0) {
			level ++;
			for (Map<String, Object> node : nodeList) {
				node.put("level", level);
				node.put("children", buildTree(node.get("id").toString(), allList, level));
			}
		}
		return nodeList;
	}

	/**
	 * 获取此教学包的目录和文件
	 * @param oldPkgId
	 * @return
	 */
	private List<Map<String, Object>> selectListDatas(String oldPkgId){
		Map<String, Object> map = new HashMap<>();
		map.put("pkgId", oldPkgId);
		List<Map<String,Object>> list = tcloudPanDirectoryMapper.selectListByUnionAll(map);
		return list;
	}
	
	/**
	 * 发布教学包页面时，一次性查出必要的数据，返回给前端处理
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 * @apiNote 返回数据格式定义如下
	 * 
	 */
	@Override
	public R queryDataList(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("数据获取未成功");
		}
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(tevglPkgInfo.getSubjectId());
		if (tevglBookSubject == null) {
			return R.error("数据获取未成功");
		}
		String subjectId = tevglBookSubject.getSubjectId();
		Map<String, Object> subjectInfo = new HashMap<>();
		subjectInfo.put("type", "subject");
		subjectInfo.put("subjectId", subjectId);
		subjectInfo.put("chapterId", subjectId);
		subjectInfo.put("chapterName", tevglBookSubject.getSubjectName());
		subjectInfo.put("subjectName", tevglBookSubject.getSubjectName());
		// 获取章节数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		List<Map<String, Object>> allChapterList = tevglBookChapterMapper.selectSimpleListByMapForRelease(params);
		// 将分组归属至对应的章节下
		handleResgroupList(pkgId, subjectId, allChapterList);
		// 将活动归属至课程或章节下
		handleActivityList(pkgId, subjectInfo, allChapterList);
		// 递归处理数据
		List<Map<String, Object>> treeData = recursionUtils.getTreeData(subjectId, allChapterList, "chapterId", "parentId");
		// 归属至
		subjectInfo.put("children", treeData);
		return R.ok().put(Constant.R_DATA, subjectInfo);
	}
	
	/**
	 * 将活动归属至课程或章节下
	 * @param pkgId
	 * @param subjectInfo
	 * @param allChapterList
	 */
	private void handleActivityList(String pkgId, Map<String, Object> subjectInfo, List<Map<String, Object>> allChapterList) {
		// 获取教学包的活动
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		List<Map<String, Object>> actList0 = tevglPkgActivityRelationMapper.selectSimpleListMapForRelease(params);
		log.debug("未定了章节的活动：" + actList0.size());
		// 没有章节的活动，归属到课程节点下
		List<Object> list0 = actList0.stream().map(a -> {
			Map<String, Object> info = new HashMap<>();
			info.put("activityId", a.get("activityId"));
			info.put("activityName", a.get("activityName"));
			info.put("activityType", a.get("activityType"));
			return info;
		}).collect(Collectors.toList());
		subjectInfo.put("activityList", list0);
		// 有章节的活动
		params.put("pkgId", pkgId);
		params.put("chapterIdIsNotNull", "Y");
		List<Map<String, Object>> actList1 = tevglPkgActivityRelationMapper.selectSimpleListMap(params);
		log.debug("绑定了章节的活动：" + actList1.size());
		for (Map<String, Object> chapter : allChapterList) {
			List<Map<String, Object>> list1 = actList1.stream()
					.filter(a -> a.get("chapterId").equals(chapter.get("chapterId")))
					.map(b -> {
						Map<String, Object> info = new HashMap<>();
						info.put("activityId", b.get("activityId"));
						info.put("activityName", b.get("activityName"));
						info.put("activityType", b.get("activityType"));
						return info;
					})
			.collect(Collectors.toList());
			chapter.put("activityList", list1);
		}
	}
	
	/**
	 * 将分组归属至对应的章节下
	 * @param pkgId
	 * @param subjectId
	 * @param allChapterList
	 */
	private void handleResgroupList(String pkgId, String subjectId, List<Map<String, Object>> allChapterList) {
		// 获取分组数据
		Map<String, Object> params = new HashMap<>();
		//params.put("ctPkgId", pkgId);
		//params.put("pkgId", pkgId);
		params.put("subjectId", subjectId);
		params.put("sidx", "t1.sort_num");
		params.put("order", "asc");
		List<Map<String,Object>> allResgroupList = tevglPkgResgroupMapper.selectSimpleListMap(params);
		//log.debug("根据条件：" + params);
		//log.debug("获取分组数据：" + allResgroupList.size());
		for (Map<String, Object> chapter : allChapterList) {
			List<Map<String, Object>> resgroupList = allResgroupList.stream()
					.filter(a -> a.get("chapterId").equals(chapter.get("chapterId")))
					.map(b -> {
						Map<String, Object> info = new HashMap<>();
						info.put("resgroupId", b.get("resgroupId"));
						info.put("resgroupName", b.get("resgroupName"));
						info.put("dictCode", b.get("dictCode"));
						info.put("chapterId", b.get("chapterId"));
						return info;
					}).collect(Collectors.toList());
			chapter.put("resgroupList", resgroupList);
		}
	}

	/**
	 * 审核
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 * @apiNote
	 * checkState，Y通过，N未通过，W空则标识，等待审核中
	 */
	@Override
	public R check(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 如果是在课堂页面中发布教学包
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			// 如果课堂被移交了，且登录用户是接收者
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
				return R.error("当前课堂是由管理员移交给您，暂不支持发布教学包");
			}
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("参数异常");
		}
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && tevglPkgInfo.getCreateUserId().equals(loginUserId);
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && tevglPkgInfo.getReceiverUserId().equals(loginUserId);
		// 当前源教学包是否有正在等待审核的或未通过的教学包，如果有，直接跳到最后一步
		boolean hasCheckingPkg = false;
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 查询条件
		Map<String, Object> map = new HashMap<>();
		map.put("refPkgId", pkgId);
		map.put("releaseStatus", "N");
		List<TevglPkgInfo> list = tevglPkgInfoMapper.selectListByMap(map);
		log.debug("教学包["+pkgId+"]，是否有正在待审核的衍生版本：" + list.size());
		if (list != null && list.size() > 0) {
			hasCheckingPkg = true;
			// 返回这些数据
			Map<String, Object> params = new HashMap<>();
			params.put("refPkgId", pkgId);
			if (isCreator) {
				params.put("createUserId", loginUserId);	
			} else if (isReceiver) {
				params.put("receiverUserId", loginUserId);
			}
			List<Map<String,Object>> checkList = tevglPkgCheckMapper.querMyWaitCheckPkgList(params);
			if (checkList != null && checkList.size() > 0) {
				checkList.stream().forEach(a -> {
					a.put("pkgLogo", uploadPathUtils.stitchingPath(a.get("pkgLogo"), "12"));
					a.put("checkStateName", a.get("checkState"));
				});
				convertUtil.convertDict(checkList, "checkStateName", "isPass");
				data.put("tevglPkgInfo", checkList.get(0));
				data.put("checkState", StrUtils.isNull(checkList.get(0).get("checkState")) ? "W" : checkList.get(0).get("checkState"));
				data.put("checkStateName", StrUtils.isNull(checkList.get(0).get("checkStateName")) ? "等待审核中......" : checkList.get(0).get("checkStateName"));
			} else {
				
			}
		}
		data.put("hasCheckingPkg", hasCheckingPkg);
		return R.ok().put(Constant.R_DATA, data);
	}
	
}
