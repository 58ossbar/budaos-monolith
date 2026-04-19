package com.budaos.modules.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.modules.evgl.cloudpan.service.TcloudPanDirectoryServiceImpl;
import com.budaos.modules.evgl.pkg.domain.*;
import com.budaos.modules.evgl.pkg.persistence.*;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 教学包操作的一些公共方法
 * @author huj
 *
 */
@Component
@RefreshScope
public class PkgUtils {

	private Logger log = LoggerFactory.getLogger(PkgUtils.class);
	
	// 文件上传地址
	@Value("${com.budaos.file-upload-path}")
    private String budaosFieUploadPath;
	@Value("${com.budaos.cb-upload-paths:default}")
	private String cbUploadPaths;
	// 访问地址
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	
	@Autowired
	private CloudPanUtils cloudPanUtils;
	
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	
	@Autowired
	private TcloudPanDirectoryServiceImpl tcloudPanDirectoryServiceImpl;
	
	
	/**
	 * 引用数+?
	 * @param pkgId 必传参数
	 * @param num 选填参数，为null时默认为1
	 */
	public void plusPkgRefNum(String pkgId, Integer num) {
		TevglPkgInfo t = new TevglPkgInfo();
		t.setPkgId(pkgId);
		t.setPkgRefCount(num == null ? 1 : num);
		tevglPkgInfoMapper.plusNum(t);
	}
	
	/**
	 * 引用数-?
	 * @param pkgId 必传参数
	 * @param num 选填参数，为null时默认为1
	 */
	public void plusPkgRefReduceNum(String pkgId, Integer num) {
		TevglPkgInfo t = new TevglPkgInfo();
		t.setPkgId(pkgId);
		t.setPkgRefCount(num == null ? -1 : num);
		tevglPkgInfoMapper.plusNum(t);
	}
	
	
	/**
	 * 资源数+?
	 * @param pkgId 必传参数
	 * @param num 选填参数，为null时默认为1
	 */
	public void plusPkgResNum(String pkgId, Integer num) {
		TevglPkgInfo t = new TevglPkgInfo();
		t.setPkgId(pkgId);
		t.setPkgResCount(num == null ? 1 : num);
		tevglPkgInfoMapper.plusNum(t);
	}
	
	/**
	 * 资源数-?
	 * @param pkgId
	 * @param num 选填参数，为null时默认为-1，传值，请传负数
	 */
	public void plusPkgResReduceNum(String pkgId, Integer num) {
		TevglPkgInfo t = new TevglPkgInfo();
		t.setPkgId(pkgId);
		t.setPkgResCount(num == null ? -1 : num);
		tevglPkgInfoMapper.plusNum(t);
	}
	
	/**
	 * 更新活动数量+1
	 * @param pkgId
	 */
	public void plusPkgActivityNum(String pkgId) {
		TevglPkgInfo t = new TevglPkgInfo();
		t.setPkgId(pkgId);
		t.setPkgActCount(1);
		tevglPkgInfoMapper.plusNum(t);
	}
	
	/**
	 * 更新活动数量-1
	 * @param pkgId
	 */
	public void plusPkgActivityReduceNum(String pkgId) {
		TevglPkgInfo t = new TevglPkgInfo();
		t.setPkgId(pkgId);
		t.setPkgActCount(-1);
		tevglPkgInfoMapper.plusNum(t);
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
		if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) {
			relation.setGroupId(Identities.uuid());
		}
		tevglPkgActivityRelationMapper.insert(relation);
		return relation.getPaId();
	}
	
	/**
	 * 创建资源分组和资源（或许已废弃-等待删除）
	 * @param pkgId
	 * @param loginUserId
	 * @param dictList
	 * @param tevglBookChapter
	 */
	public void createPkgResGroup(String pkgId, String loginUserId, List<TsysDict> dictList, TevglBookChapter tevglBookChapter) {
		if (dictList == null || dictList.size() == 0) {
			return ;
		}
		String createUserId = tevglBookChapter.getCreateUserId();
		for (int i = 0; i < dictList.size(); i++) {
			log.debug("为章节创建【" + dictList.get(i).getDictValue() + "】资源分组和资源");
			TsysDict tsysDict = dictList.get(i);
			TevglPkgResgroup t = new TevglPkgResgroup();
			t.setResgroupId(Identities.uuid());
			t.setPkgId(pkgId);
			t.setChapterId(tevglBookChapter.getChapterId());
			t.setResgroupName(tsysDict.getDictValue());
			t.setCreateTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
			t.setCreateUserId(createUserId);
			// 排序号
			if (dictList.size() == 1) {
				t.setSortNum(i);
			} else {
				t.setSortNum(i+1);
			}
			t.setState("Y");
			t.setResgroupTotal(0);
			t.setGroupType("1"); // 1活教材相关分组2活动相关分组
			tevglPkgResgroupMapper.insert(t);
			// 生成默认分组之后，再随之生成资源记录
			TevglPkgRes res = new TevglPkgRes();
			res.setResId(Identities.uuid());
			res.setPkgId(pkgId);
			res.setResgroupId(t.getResgroupId());
			res.setCreateTime(com.budaos.utils.tool.DateUtils.getNowTimeStamp());
			res.setCreateUserId(createUserId);
			// 将章节内容同步过来（只在1课程内容时同步章节内容）
			if ("1".equals(tsysDict.getDictCode())) {
				res.setResContent(tevglBookChapter.getChapterContent());
			}
			tevglPkgResMapper.insert(res);
			// 从而教学包的资源数+1
			plusPkgResNum(pkgId, 1);
		}
	}
	
	/**
	 * 若该章节下没有[活动分组]，则默认创建一个分组（使用情形：新增修改活动时）
	 * @param refPkgId
	 * @param chapterId
	 * @param loginUserId
	 */
	public void createDefaultActivityTab(String refPkgId, String chapterId, String loginUserId) {
		// 章节非必选的话，则不生成
		if (StrUtils.isEmpty(chapterId)) {
			return;
		}
		// 根据章节id查询章节信息，得到课程id，从而显示活动标签
		TevglBookChapter chapter = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapter == null) {
			log.debug("根本没有此章节，直接返回");
			return;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", refPkgId);
		params.put("chapterId", chapterId);
		params.put("dictCode", "2");
		List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			// 填充信息
			TevglPkgResgroup tevglPkgResgroup = new TevglPkgResgroup();
			tevglPkgResgroup.setResgroupId(Identities.uuid()); // 主键
			tevglPkgResgroup.setSubjectId(chapter.getSubjectId()); // 必须填充subjectId才会显示活动标签
			tevglPkgResgroup.setPkgId(refPkgId);
			tevglPkgResgroup.setChapterId(chapterId);
			tevglPkgResgroup.setResgroupName("活动");
			tevglPkgResgroup.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
			tevglPkgResgroup.setState("Y"); // 状态(Y有效N无效)
			tevglPkgResgroup.setResgroupTotal(0); // 资源总数
			tevglPkgResgroup.setCreateUserId(loginUserId);
			tevglPkgResgroup.setGroupType(GlobalActivity.ACTIVITY_GROUP_TYPE_1);
			tevglPkgResgroup.setDictCode("2");
			// 排序号处理
			params.put("pkgId", refPkgId);
			params.put("chapterId", tevglPkgResgroup.getChapterId());
		    tevglPkgResgroup.setSortNum(tevglPkgResgroupMapper.getMaxSortNum(params));
		    tevglPkgResgroupMapper.insert(tevglPkgResgroup);
		    // 生成分组之后，再随之生成资源记录
			TevglPkgRes res = new TevglPkgRes();
			res.setResId(Identities.uuid());
			res.setPkgId(refPkgId);
			res.setResgroupId(tevglPkgResgroup.getResgroupId());
			tevglPkgResMapper.insert(res);
			// 教学包的资源数+1，由于章节分组与资源一对一,直接把分组数当作资源数
			plusPkgResNum(refPkgId, 1);
		}
	}
	
	/**
	 * 删除教学包的资源分组和资源
	 * @param pkgId
	 * @param params
	 */
	public void deleteResgroupAndRes(String pkgId, Map<String, Object> params) {
		if (StrUtils.isEmpty(pkgId)) {
			return;
		}
		params.clear();
		params.put("pkgId", pkgId);
		List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			list.stream().forEach(item -> {
				String resgroupId = item.getResgroupId();
				tevglPkgResMapper.deleteResgroupId(resgroupId);
				tevglPkgResgroupMapper.delete(resgroupId);
			});
		}
	}
	
	/**
	 * 教学包对应的课程（确保此课程与此教学包一对一）
	 * @param subjectId
	 * @param params
	 */
	public void deleteSubjectAndChapter(String subjectId, Map<String, Object> params) {
		if (StrUtils.isEmpty(subjectId)) {
			return;
		}
		params.clear();
		params.put("subjectId", subjectId);
		List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMapForSimple(params);
		// 删除章节
		if (list != null && list.size() > 0) {
			list.stream().forEach(chapter -> {
				tevglBookChapterMapper.delete(chapter.getChapterId());
			});
		}
		// 删除课程
		tevglBookSubjectMapper.delete(subjectId);
	}
	
	/**
	 * 删除教学包的权限记录
	 * @param pkgId
	 * @param params
	 */
	public void deleteTeamData(String pkgId, Map<String, Object> params) {
		if (StrUtils.isEmpty(pkgId)) {
			return;
		}
		params.clear();
		params.put("pkgId", pkgId);
		List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			List<String> teamIds = list.stream().map(a -> a.getTeamId()).collect(Collectors.toList());
			params.clear();
			params.put("teamIds", teamIds);
			List<TevglBookpkgTeamDetail> detailList = tevglBookpkgTeamDetailMapper.selectListByMap(params);
			if (detailList != null && detailList.size() > 0) {
				detailList.stream().forEach(detail -> {
					tevglBookpkgTeamDetailMapper.delete(detail.getDetailId());
				});
			}
			list.stream().forEach(team -> {
				tevglBookpkgTeamMapper.delete(team.getTeamId());
			});
		}
	}
	
	/**
	 * 删除教学包
	 * @param pkgId
	 */
	public void deletePkg(String pkgId) {
		tevglPkgInfoMapper.delete(pkgId);
	}
	
	/**
	 * 找到最顶层的pkgId
	 * @return
	 */
	public String getTopPkgId(String pkgId) {
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo != null) {
			if (StrUtils.isEmpty(pkgInfo.getRefPkgId())) {
				return pkgInfo.getPkgId();
			} else {
				return getTopPkgId(pkgInfo.getRefPkgId());
			}
		}
		return null;
	}
	
	/**
	 * 获取最顶层的教学包
	 * @param pkgId
	 * @return
	 */
	public TevglPkgInfo getTopPkgInfo(String pkgId) {
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo != null) {
			if (StrUtils.isEmpty(pkgInfo.getRefPkgId())) {
				return pkgInfo;
			} else {
				return getTopPkgInfo(pkgInfo.getRefPkgId());
			}
		}
		return null;
	}
	
	/**
	 * 判断当前教学包是否被其它任意人使用中，如果没有，则可以允许修改、编辑等操作
	 * @param sourcePkgId
	 * @return
	 */
	public boolean isBeingUsed(Object sourcePkgId) {
		Map<String, Object> params = new HashMap<>();
		params.put("refPkgId", sourcePkgId);
		List<Map<String,Object>> list = tevglPkgInfoMapper.selectSimpleList(params);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 当登录用户非此教学包者时（共建者），往共建表增加一条记录
	 * @param chapterId 章节id
	 * @param loginUserId 当前登录用户id
	 * @param createUserId 教学包创建者id
	 */
	@Deprecated
	public void createTeamDetailData(String pkgId, String chapterId, String loginUserId, String createUserId) {
		// 如果当前登录用户不是此教学包创建者
		if (!loginUserId.equals(createUserId)) {
			Map<String, Object> params = new HashMap<>();
			params.put("pkgId", pkgId);
			params.put("userId", loginUserId);
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
			// 往章节权限表增加一条记录
			if (list != null && list.size() > 0) {
				TevglBookpkgTeamDetail detail = new TevglBookpkgTeamDetail();
				detail.setDetailId(Identities.uuid());
				detail.setTeamId(list.get(0).getTeamId());
				detail.setChapterId(chapterId);
				detail.setUserId(loginUserId);
				tevglBookpkgTeamDetailMapper.insert(detail);
			}
		}
	}
	
	/**
	 * 共建者身份时，往共建表增加一条记录
	 * @param tevglPkgInfo
	 * @param chapterId
	 * @param loginUserId
	 */
	public void createTeamDetailDataV2(TevglPkgInfo tevglPkgInfo, String chapterId, String loginUserId) {
		if (tevglPkgInfo == null || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(chapterId)) {
			return;
		}
		String pkgId = tevglPkgInfo.getPkgId();
		// 标识，是否需要入库
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
		// 如果登录用户不是创建者，又不是接管者
		if (!isCreator && !isReceiver) {
			Map<String, Object> params = new HashMap<>();
			params.put("pkgId", pkgId);
			params.put("userId", loginUserId);
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
			// 往章节权限表增加一条记录
			if (list != null && list.size() > 0) {
				TevglBookpkgTeamDetail detail = new TevglBookpkgTeamDetail();
				detail.setDetailId(Identities.uuid());
				detail.setTeamId(list.get(0).getTeamId());
				detail.setChapterId(chapterId);
				detail.setUserId(loginUserId);
				tevglBookpkgTeamDetailMapper.insert(detail);
			}
		}
	}
	
	/**
	 * 批量创建 共建章节权限记录（非创建者，且非接管者时）
	 * @param tevglPkgInfo
	 * @param chapterIdList
	 * @param loginUserId
	 */
	public void createTeamDetailDataV2(TevglPkgInfo tevglPkgInfo, List<String> chapterIdList, String loginUserId) {
		if (tevglPkgInfo == null || StrUtils.isEmpty(loginUserId) || chapterIdList == null || chapterIdList.isEmpty()) {
			return;
		}
		// 标识，是否需要入库
		String pkgId = tevglPkgInfo.getPkgId();
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
		// 如果是创建者，或接管者，不需要，直接提前返回
		if (isCreator || isReceiver) {
			return;
		}
		// 如果没有主权限，直接提前返回
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("userId", loginUserId);
		List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
		if (list == null || list.isEmpty()) {
			return;
		}
		// 批量入库
		List<TevglBookpkgTeamDetail> insertDeatilList = new ArrayList<>();
		chapterIdList.stream().forEach(chapterId -> {
			TevglBookpkgTeamDetail detail = new TevglBookpkgTeamDetail();
			detail.setDetailId(Identities.uuid());
			detail.setTeamId(list.get(0).getTeamId());
			detail.setChapterId(chapterId);
			detail.setUserId(loginUserId);
			insertDeatilList.add(detail);
		});
		tevglBookpkgTeamDetailMapper.insertBatch(insertDeatilList);
	}
	
	/**
	 * 统计，当前有多少人使用了这个教学包，（人数去重，且包括自己）
	 * 这里的使用：指的是，课堂使用采用了这个教学包
	 * @param pkgId
	 * @return
	 */
	public Integer countHowManyPeopleUseIt(String pkgId) {
		List<Map<String,Object>> list = tevglPkgInfoMapper.selectHowManyPeopleUseIt(pkgId);
		return list.size();
	}
	
	public String getPathByParaNo(String paraNo) {
		String[] paths = cbUploadPaths.split(",");
		if(StrUtils.isEmpty(paraNo) || Integer.parseInt(paraNo) >= paths.length) {
			return paths[0];
		}
		return paths[Integer.parseInt(paraNo)].trim();
	}
	
	/**
	 * 获取教学包封面的绝对路径
	 * @param pkgLogo
	 * @return
	 */
	public String getAbsolutePathLogo(Object pkgLogo) {
		if (StrUtils.isNull(pkgLogo)) {
			return null;
		}
		return budaosFieUploadPath + getPathByParaNo("12") + "/" + pkgLogo;
	}
	
	public String handleAccessUrl(Object logo, String dirName) {
		if (StrUtils.isNull(logo)) {
			return null;
		}
		if (StrUtils.isEmpty(dirName)) {
			return budaosFieAccessPath + "/" + logo;
		}
		if (logo.toString().indexOf("uploads") == -1) {
			return budaosFieAccessPath + "/" + dirName + "/" + logo; 
		}
		return logo.toString();
	}
	
	/**
	 * 获取同职业路径同课程体系同用户下的未教学包（唯一性：一个用户同职业同课程只能有一个未发布状态的教学包）
	 * @param majorId 职业路径
	 * @param subjectId 课程体系
	 * @param loginUserId 当前登录用户
	 * @return 返回空表示还没有
	 */
	public TevglPkgInfo getOnlyPkgByMajorIdSubjectId(String majorId, String subjectId, String loginUserId) {
		return doGetOnlyPkgByMajorIdSubjectId(majorId, subjectId, loginUserId, new HashMap<>());
	}
	
	/**
	 * 获取同职业路径同课程体系同用户下的未教学包（唯一性：一个用户同职业同课程只能有一个未发布状态的教学包）（注意是否需要只查询refPkgId为空）
	 * @param majorId 职业路径
	 * @param subjectId 课程体系
	 * @param loginUserId 当前登录用户
	 * @param parmas 查询条件
	 * @return 返回空表示还没有
	 */
	public TevglPkgInfo getOnlyPkgByMajorIdSubjectId(String majorId, String subjectId, String loginUserId, Map<String, Object> parmas) {
		return doGetOnlyPkgByMajorIdSubjectId(majorId, subjectId, loginUserId, parmas);
	}
	
	/**
	 * 获取同职业路径同课程体系同用户下的未教学包（唯一性：一个用户同职业同课程只能有一个未发布状态的教学包）
	 * @param majorId 职业路径
	 * @param subjectId 课程体系
	 * @param loginUserId 当前登录用户
	 * @param parmas 查询条件
	 * @return 返回空表示还没有
	 */
	private TevglPkgInfo doGetOnlyPkgByMajorIdSubjectId(String majorId, String subjectId, String loginUserId, Map<String, Object> parmas) {
		if (parmas == null) {
			parmas = new HashMap<String, Object>();
		}
		parmas.put("state", "Y");
		parmas.put("majorId", majorId);
		parmas.put("subjectId", subjectId);
		parmas.put("createUserId", loginUserId);
		// 状态(Y已发布N未发布)
		parmas.put("releaseStatus", "N");
		log.debug("一个用户同职业同课程只能有一个未发布状态的教学包");
		log.debug("查询条件：" + parmas);
		List<TevglPkgInfo> list = tevglPkgInfoMapper.selectListByMapInnerJoin(parmas);
		log.debug("查询结果：" + list.size());
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 删除云盘相关数据
	 * @param pkgId
	 * @param loginUserId
	 * @param params
	 * @return
	 */
	public R doDeleteCloudPanDatas(String pkgId, String loginUserId, Map<String, Object> params) {
		try {
			log.debug("============================== 开始删除云盘相关数据中 begin ==============================");
			// 查询教学包的目录
			params.clear();
			params.put("pkgId", pkgId);
			List<TcloudPanDirectory> directoryList = tcloudPanDirectoryMapper.selectListByMap(params);
			if (directoryList != null && directoryList.size() > 0) {
				List<String> dirIds = directoryList.stream().map(a -> a.getDirId()).collect(Collectors.toList());
				params.put("dirIds", dirIds);
				List<TcloudPanFile> fileList = tcloudPanFileMapper.selectListByMap(params);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("pkgId", pkgId);
				JSONArray jsonArray = new JSONArray();
				directoryList.stream().forEach(directory -> {
					JSONObject o = new JSONObject();
					o.put("id", directory.getDirId());
					o.put("type", "1");
					jsonArray.add(o);
				});
				fileList.stream().forEach(fileInfo -> {
					JSONObject o = new JSONObject();
					o.put("id", fileInfo.getFileId());
					o.put("type", "2");
					jsonArray.add(o);
				});
				jsonObject.put("list", jsonArray);
				R r = tcloudPanDirectoryServiceImpl.deletesNew(jsonObject, loginUserId);
				log.debug("删除结果：" + r);
				// 磁盘上直接删除此教学包主目录
				File f = new File(cloudPanUtils.getUploadPath() + "/" + pkgId);
				if (f.exists() && f.isDirectory()) {
					log.debug("被删除的教学包主目录路径为：" + f);
					f.delete();
				}
			}
			log.debug("============================== 结束删除云盘相关数据中 end ==============================");
		} catch (Exception e) {
			log.error("云盘数据删除失败");
			e.printStackTrace();
			return R.error("系统异常，删除云盘数据失败");
		}
		return R.ok("云盘数据删除成功");
	}
	
	/**
	 * 判断当前登录用户是否能对该教学包进行操作
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	public boolean hasOperatingAuthorization(TevglPkgInfo tevglPkgInfo, String loginUserId) {
		if (tevglPkgInfo == null || StrUtils.isEmpty(loginUserId)) {
			return false;
		}
		// 如果教学包没有被移交
		if (StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId())) {
			// 且登录用户是教学包的创建者
			if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
				return true;
			}
		} else { // 否则，该教学包被移交了
			// 教学包被移交，且登录用户是教学包原创建者
			if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
				return false;
			}
			// 如果是接管者
			if (loginUserId.equals(tevglPkgInfo.getReceiverUserId())) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * 批量创建 【课程内容】 默认分组
	 * @param tevglPkgInfo 必传参数，教学包
	 * @param chapterIdList 必传参数，章节id集合
	 * @param loginUserId 必传参数，当前登录用户
	 */
	public void doCreateDefaultGroup(TevglPkgInfo tevglPkgInfo, List<String> chapterIdList, String loginUserId) {
		if (tevglPkgInfo == null || StrUtils.isEmpty(loginUserId) || chapterIdList == null || chapterIdList.isEmpty()) {
			return;
		}
		doCreateDefaultGroup(tevglPkgInfo.getPkgId(), tevglPkgInfo.getSubjectId(), chapterIdList, loginUserId);
	}

	/**
	 * 批量创建 【课程内容】 默认分组
	 * @param pkgId 必传参数，教学包id
	 * @param subjectId 必传参数，教学包对应教材id
	 * @param chapterIdList 必传参数，章节id集合
	 * @param loginUserId 必传参数，当前登录用户
	 */
	public void doCreateDefaultGroup(String pkgId, String subjectId, List<String> chapterIdList, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId) || chapterIdList == null || chapterIdList.isEmpty()) {
			return;
		}
		String time = DateUtils.getNowTimeStamp();
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("chapterIds", chapterIdList);
		List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(params);
		// 等待新增的数据
		List<TevglPkgResgroup> insertResGroupList = new ArrayList<>();
		List<TevglPkgRes> resList = new ArrayList<>();
		chapterIdList.stream().forEach(chapterId -> {
			// 如果有[课程内容],不重复生成,没有才生成
			boolean match = list.stream().anyMatch(a -> a.getChapterId().equals(chapterId) && a.getDictCode().equals("1"));
			if (!match) {
				TevglPkgResgroup tevglPkgResgroup = new TevglPkgResgroup();
				tevglPkgResgroup.setResgroupId(Identities.uuid()); // 主键
				tevglPkgResgroup.setPkgId(pkgId);
				tevglPkgResgroup.setSubjectId(subjectId);
				tevglPkgResgroup.setChapterId(chapterId);
				tevglPkgResgroup.setResgroupName("课程内容");
				tevglPkgResgroup.setDictCode("1");
				tevglPkgResgroup.setCreateTime(time); // 创建时间
				tevglPkgResgroup.setState("Y"); // 状态(Y有效N无效)
				tevglPkgResgroup.setResgroupTotal(0); // 资源总数
				tevglPkgResgroup.setCreateUserId(loginUserId);
				tevglPkgResgroup.setGroupType(GlobalActivity.ACTIVITY_GROUP_TYPE_1);
				// 排序号处理
				params.put("pkgId", pkgId);
				params.put("chapterId", tevglPkgResgroup.getChapterId());
				Integer sortNum = tevglPkgResgroupMapper.getMaxSortNum(params);
				tevglPkgResgroup.setSortNum(sortNum);
				insertResGroupList.add(tevglPkgResgroup);
				// 生成分组之后，再随之生成资源记录
				TevglPkgRes res = new TevglPkgRes();
				res.setResId(Identities.uuid());
				res.setPkgId(pkgId);
				res.setResgroupId(tevglPkgResgroup.getResgroupId());
				res.setCreateTime(time);
				res.setCreateUserId(loginUserId);
				res.setViewNum(0);
				resList.add(res);
			}
		});
		if (insertResGroupList.size() > 0) {
			tevglPkgResgroupMapper.insertBatch(insertResGroupList);
		}
		if (resList.size() > 0) {
			tevglPkgResMapper.insertBatch(resList);
		}
		// 教学包的资源数+1，由于章节分组与资源一对一,直接把分组数当作资源数
		this.plusPkgResNum(pkgId, insertResGroupList.size());
	}
}
