package com.budaos.modules.evgl.book.service;

import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.evgl.book.api.BookService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.pkg.api.TevglPkgActivityRelationService;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeamDetail;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamDetailMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private TevglPkgActivityRelationService tevglPkgActivityRelationService;
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public R rename(String chapterId, String name) {
		// 非空判断
		if (StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(name)) {
			return R.error("必传参数为空");
		}
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapterInfo == null) {
			return R.error("无效的记录，操作失败");
		}
		TevglBookChapter chapter = new TevglBookChapter();
		chapter.setChapterId(chapterId);
		chapter.setChapterName(name);
		// 同级名称是否重复
		Map<String, Object> map = new HashMap<>();
		map.put("state", "Y");
		map.put("parentId", chapterInfo.getParentId());
		List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			for (TevglBookChapter a : list) {
				if (!a.getChapterId().equals(chapterInfo.getChapterId())) {
					if (a.getChapterName().equals(chapter.getChapterName())) {
						return R.error("重命名失败,同级章节中,输入的名称已经存在").put("chapterId", chapterId).put("chapterName",
								chapterInfo.getChapterName());
					}
				}
			}
		}
		tevglBookChapterMapper.update(chapter);
		return R.ok("重命名成功").put("chapterId", chapterId);
	}

	@Override
	public R remove(String chapterId) {
		if ( StrUtils.isEmpty(chapterId)) {
			return R.error("必传参数为空");
		}
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapterInfo == null) {
			return R.ok("已删除");
		}
		// 是否还有子章节校验
		Map<String, Object> map = new HashMap<>();
		map.put("parentId", chapterInfo.getChapterId());
		map.put("state", "Y"); // 状态(Y有效N无效)
		List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			return R.error("该节点下还有子节点，请先删除子节点");
		}
		// 删除章节
		tevglBookChapterMapper.delete(chapterId);
		return R.ok("删除成功");
	}

	@Override
	public R saveChapterInfo(TevglBookChapter tevglBookChapter) {
		// 合法性校验
		String subjectId = tevglBookChapter.getSubjectId();
		String parentId = tevglBookChapter.getParentId();
		String chapterName = tevglBookChapter.getChapterName();
		String loginUserId = serviceLoginUtil.getLoginUserId() == null ? "hujun" : serviceLoginUtil.getLoginUserId();
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(chapterName) || StrUtils.isEmpty(parentId)
				|| StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 去空格
		tevglBookChapter.setChapterName(tevglBookChapter.getChapterName().trim());
		if (StrUtils.isEmpty(tevglBookChapter.getChapterName())) {
			return R.error("章节名称不能为空");
		}
		if (tevglBookChapter.getChapterName().length() > 50) {
			return R.error("字符长度不能超过50");
		}
		// 同级别不允许有同名的名称
		if (isRepetitionName(parentId, chapterName)) {
			return R.error("同级章节下，不允许有重复的章节名称");
		}
		tevglBookChapter.setChapterId(Identities.uuid());
		tevglBookChapter.setCreateUserId(loginUserId);
		tevglBookChapter.setCreateTime(DateUtils.getNowTimeStamp());
		tevglBookChapter.setState("Y"); // 状态(Y有效N无效)
		ValidatorUtils.check(tevglBookChapter);
		// 限制不能超过?级
		int level = 3;
		if (isOverLevel(tevglBookChapter.getParentId(), tevglBookChapter.getSubjectId(), level)) {
			return R.error("目前最多允许增加" + level + "级章节节点");
		}
		// 排序号处理
		Map<String, Object> map = new HashMap<>();
		map.put("parentId", parentId);
		tevglBookChapter.setOrderNum(tevglBookChapterMapper.selectMaxSortByMap(map) + 1);
		tevglBookChapterMapper.insert(tevglBookChapter);
		return R.ok("新增成功").put("chapterId", tevglBookChapter.getChapterId());
	}

	/**
	 * 新增章节时，控制同级不能有重复的名称
	 *
	 * @param parentId
	 * @param chapterName
	 * @return
	 */
	private boolean isRepetitionName(String parentId, String chapterName) {
		Map<String, Object> map = new HashMap<>();
		map.put("parentId", parentId);
		List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMap(map);
		return list.stream().anyMatch(a -> a.getChapterName().equals(chapterName.trim()));
	}

	/**
	 * 新增章节时，验证节点是否已经有3级了
	 *
	 * @param parentId  目标父节点
	 * @param subjectId 课程主键
	 * @param level     控制不能超过几级，默认为4
	 * @return
	 * @apiNote 课程的直系章节理解为一级节点（即parentId匹配subjectId）
	 */
	private boolean isOverLevel(String parentId, String subjectId, Integer level) {
		level = level == null ? 3 : level;
		// 如果父节点与课程节点不匹配,则说明增加的不是一级节点,需要校验
		if (!parentId.equals(subjectId)) {
			// 判断
			int i = doCalculationLevel(parentId, subjectId, 1);
			if (i >= level) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 计算当前被右键新增的节点，处于树形数据中的第几级
	 *
	 * @param parentId
	 * @param subjectId
	 * @param i         固定值1
	 * @return
	 */
	private int doCalculationLevel(String parentId, String subjectId, int i) {
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(parentId);
		if (chapterInfo == null) {
			return i;
		}
		boolean flag = chapterInfo.getParentId().equals(subjectId);
		// 如果匹配,说明已经到顶级(一级节点了), 则返回结果
		if (flag) {
			return i;
		} else {
			// 如果不匹配,继续递归
			i++;
			return doCalculationLevel(chapterInfo.getParentId(), subjectId, i);
		}
	}

	@Override
	public R saveChapterContent(TevglBookChapter tevglBookChapter) {
		tevglBookChapterMapper.update(tevglBookChapter);
		return R.ok("保存成功");
	}

	@Override
	public R viewChapter(String chapterId) {
		TevglBookChapter tevglBookChapter = tevglBookChapterMapper.selectObjectById(chapterId);
		return R.ok().put(Constant.R_DATA, tevglBookChapter);
	}

	/**
	 * 课堂页面，刷新ztree树专用方法
	 * @param id 主键id，可能是课程id、可能是章节id
	 * @param pkgId
	 * @param subjectId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> listChaptersForRoomPage(String loginUserId, String id, String pkgId, String subjectId) {
		// 返回结果
		List<Map<String,Object>> list = new ArrayList<>();
		// 合法性校验
		if (StrUtils.isEmpty(id) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId)) {
			//log.debug("必传参数为空");
			return list;
		}
		TevglTchClassroom tevglTchClassroom =  tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			log.error("com.budaos.modules.evgl.book.service.listChaptersForRoomPage() 参数异常，没有根据pkgId找到课堂");
			return list;
		}
		TevglBookSubject tevglBookSubject = tevglBookSubjectService.selectObjectById(subjectId);
		if (tevglBookSubject == null) {
			log.error("com.budaos.modules.evgl.book.service.listChaptersForRoomPage() 参数异常，没有根据subjectId找到教材");
			return list;
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 查活动
		params.clear();
		params.put("pkgId", pkgId);
		List<Map<String, Object>> activityList = tevglPkgActivityRelationService.selectSimpleListMap(params);
		// 获取此书
		params.clear();
		params.put("subjectId", id);
		// 获取具有层次结构的属性数据
		list = tevglBookSubjectService.getTree(id, pkgId);
		// 是否有【设置章节对学员是否可见】的权限
		boolean hasSetVisiblePermission = false;
		// 情况一、如果课堂没有被移交，且登录用户是课堂创建者
		boolean isCreator = StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getCreateUserId());
		if (isCreator) {
			hasSetVisiblePermission = true;
		}
		// 情况二、如果课堂被移交了，且登录用户是课堂的接收者才有权限
		boolean isReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getReceiverUserId());
		if (isReceiver) {
			hasSetVisiblePermission = true;
		}
		// 情况三、当前登录用户是该课堂的助教，且被赋予该权限
		boolean isTeachingAssistant = loginUserId.equals(tevglTchClassroom.getTraineeId());
		if (isTeachingAssistant) {
			// 如果助教角色没有设置章节学员是否可见的权限
			Map<String, Object> map = new HashMap<>();
			map.put("ctId", tevglTchClassroom.getCtId());
			List<String> permissionList = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(map);
			if (permissionList != null && permissionList.size() > 0) {
				hasSetVisiblePermission = permissionList.stream().anyMatch(a -> a.equals(GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE));
			}
		}
		// 递归处理
		buildSbV2(list, activityList, hasSetVisiblePermission);
		return list;
	}

	/**
	 * 递归处理数据
	 * @param list 必传参数，具有层次机构的树形章节数据
	 * @param activityList 活动数据
	 * @return
	 */
	private void buildSbV2(List<Map<String,Object>> list, List<Map<String, Object>> activityList, boolean hasSetVisiblePermission) {
		if (list == null || list.size() == 0) {
			return;
		}
		// 遍历
		for (int i = 0; i < list.size(); i++) {
			// 返回标识
			list.get(i).put("type", "chapter");
			// 本章节是否有活动
			Map<String, Object> chapterInfo = list.get(i);
			chapterInfo.put("hasActivity", activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapterInfo.get("chapterId"))));
			// 返回权限标识，课堂页面是不能新增修改删除等操作的，直接false
			list.get(i).put("hasPermission", false);
			list.get(i).put("hasNodePermission", false);
			// 是否有设置章节对学员可见的权限
			list.get(i).put("hasSetVisiblePermission", hasSetVisiblePermission);
			// 是否还有子节点
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> nodeList = (List<Map<String, Object>>) list.get(i).get("children");
			if (nodeList != null && nodeList.size() > 0) {
				// 递归
				buildSbV2(nodeList, activityList, hasSetVisiblePermission);
			}
		}
	}

	/**
	 * 教学包页面，刷新ztree树专用方法
	 *
	 * @param loginUserId
	 * @param id          主键id，可能是课程id、可能是章节id
	 * @param pkgId
	 * @param subjectId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> listChaptersForPkgPage(String loginUserId, String id, String serial, String type, String pkgId, String subjectId) {
		// 返回结果
		List<Map<String,Object>> list = new ArrayList<>();
		// 合法性校验
		if (StrUtils.isEmpty(id) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(type)) {
			log.debug("必传参数为空");
			return list;
		}
		TevglBookSubject tevglBookSubject = tevglBookSubjectService.selectObjectById(subjectId);
		if (tevglBookSubject == null) {
			log.error("com.budaos.modules.evgl.book.service.listChaptersForRoomPage() 参数异常，没有根据subjectId找到教材");
			return list;
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			log.debug("无效的教学包记录");
			return list;
		}
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 查活动
		params.clear();
		params.put("pkgId", pkgId);
		List<Map<String, Object>> activityList = tevglPkgActivityRelationService.selectSimpleListMap(params);
		if ("subject".equals(type)) {
			// 获取具有层次结构的属性数据
			params.clear();
			params.put("subjectId", id);
			list = tevglBookSubjectService.getTree(id, pkgId);
			// 处理权限
			boolean hasNodePermission = handleHasPermissionForBuildSbForPkg(tevglPkgInfo, loginUserId);
			// 递归处理
			buildSbForPkg(list, activityList, hasNodePermission);
		} else if ("chapter".equals(type)) {
			List<Map<String,Object>> res = new ArrayList<>();
			// 处理权限
			//boolean hasNodePermission = handleHasPermissionForBuildSbForPkg(tevglPkgInfo, loginUserId);
			// 获取当前登录拥有的章节权限
			params.clear();
			params.put("userId", loginUserId);
			List<TevglBookpkgTeamDetail> detailList = tevglBookpkgTeamDetailMapper.selectListByMap(params);
			List<String> chapterIds = detailList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
			// 获取所有的章节数据
			params.put("subjectId", subjectId);
			params.put("pkgId", pkgId);
			params.put("sidx", "t1.order_num");
			params.put("order", "asc");
			List<Map<String, Object>> allNodeList = tevglBookChapterMapper.selectSimpleListMap(params);
			// 取出课程的直系节点
			List<Map<String, Object>> oneLevelNodeList = allNodeList.stream().filter(a -> a.get("parentId").equals(subjectId)).collect(Collectors.toList());
			if (oneLevelNodeList != null && oneLevelNodeList.size() > 0) {
				buildForPkg(allNodeList, id, serial, activityList, chapterIds, res);
				// 如果教学包没有被移交
				if (isCreator || isReceiver) {
					setPermission(res, tevglPkgInfo);
				}
			}
			return res;
		}
		return list;
	}

	private void setPermission(List<Map<String, Object>> res, TevglPkgInfo tevglPkgInfo) {
		res.stream().forEach(node -> {
			node.put("isBookCreator", true);
			// 来源教学包处于发布状态，不允许新增修改等操作
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
				node.put("hasPermission", false);
				node.put("hasNodePermission", false);
				// 但是
				if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
					node.put("hasPermission", true);
					node.put("hasNodePermission", true);
				}
			} else {
				node.put("hasPermission", true);
				node.put("hasNodePermission", true);
			}
		});
	}

	/**
	 * 处理各种数据
	 * @param allNodeList
	 * @param parentId
	 * @param serial 前端ztree传递的序号
	 * @param activityList 当前教学包的相关活动
	 * @param chapterIds 非教学包创建，且非教学包接管者时，当前用户拥有的章节权限
	 */
	private List<Map<String,Object>> buildForPkg(List<Map<String, Object>> allNodeList, String parentId, String serial, List<Map<String, Object>> activityList, List<String> chapterIds, List<Map<String,Object>> res) {
		if (allNodeList == null || allNodeList.size() == 0 || StrUtils.isEmpty(parentId)) {
			return res;
		}
		List<Map<String,Object>> list = allNodeList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
		if (list != null && list.size() > 0) {
			// 遍历
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> chapterInfo = list.get(i);
				// 本章节是否有活动
				chapterInfo.put("hasActivity", activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapterInfo.get("chapterId"))));
				// 返回标识
				chapterInfo.put("type", "chapter");
				// 处理序号
				if (serial != null) {
					chapterInfo.put("serial", serial + "." + (i+1));
					list.get(i).put("chapterName", serial + "." + (i+1) +" "+ chapterInfo.get("chapterName"));
				} else {
					chapterInfo.put("serial", (i+1));
					chapterInfo.put("chapterName", (i+1) +" "+ chapterInfo.get("chapterName"));
				}
				// 是否有操作权限
				if (chapterIds.contains(chapterInfo.get("chapterId"))) {
					chapterInfo.put("hasPermission", true);
					chapterInfo.put("hasNodePermission", true);
				} else {
					chapterInfo.put("hasPermission", false);
					chapterInfo.put("hasNodePermission", false);
				}
				if (!res.contains(chapterInfo)) {
					res.add(chapterInfo);
				}
				// 递归
				List<Map<String,Object>> children = buildForPkg(allNodeList, chapterInfo.get("chapterId").toString(), serial, activityList, chapterIds, res);
				children.stream().forEach(a -> {
					if (!res.contains(a)) {
						res.add(a);
					}
				});
			}
		}
		return res;
	}

	/**
	 * 判断，在教学包页面中，是否有操作章节树节点的权限
	 * @param tevglPkgInfo 必传参数
	 * @param loginUserId 必传参数
	 * @return 默认返回false，标识没有权限
	 */
	private boolean handleHasPermissionForBuildSbForPkg(TevglPkgInfo tevglPkgInfo, String loginUserId) {
		if (tevglPkgInfo == null || StrUtils.isEmpty(loginUserId)) {
			return false;
		}
		// 如果教学包没有被移交，且登录用户是教学包创建者，直接返回有权限
		if (StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			// 来源教学包处于发布状态，不允许新增修改等操作
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
				// 但是
				if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
					return true;
				}
				return false;
			}
			return true;
		}
		// 如果教学包被移交
		if (StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId())) {
			// 来源教学包处于发布状态，不允许新增修改等操作
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
				// 但是
				if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 教学包页面专用，处理节点的操作权限，如新增、重名命、追加节点、删除节点
	 * @param list 具有树形结构的章节树
	 * @param activityList 教学包的活动，用于处理是否活动标识
	 * @param hasNodePermission 用于处理是否有操作节点的权限
	 */
	private void buildSbForPkg(List<Map<String,Object>> list, List<Map<String, Object>> activityList, boolean hasNodePermission){
		if (list == null || list.size() == 0) {
			return;
		}
		// 遍历
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> chapterInfo = list.get(i);
			chapterInfo.put("isBookCreator", hasNodePermission);
			chapterInfo.put("hasPermission", hasNodePermission);
			chapterInfo.put("hasNodePermission", hasNodePermission);
			// 本章节是否有活动
			chapterInfo.put("hasActivity", activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapterInfo.get("chapterId"))));
			// 返回标识
			list.get(i).put("type", "chapter");
			// 处理序号
			/*if (serial != null) {
			    list.get(i).put("serial", serial + "." + (i+1));
			    list.get(i).put("chapterName", serial + "." + (i+1) +" "+ list.get(i).get("chapterName"));
			} else {
			    list.get(i).put("serial", (i+1));
			    list.get(i).put("chapterName", (i+1) +" "+ list.get(i).get("chapterName"));
			}*/
			// 取出当前节点的子节点
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> nodeList = (List<Map<String, Object>>) list.get(i).get("children");
			if (nodeList != null && nodeList.size() > 0) {
				for(Map<String, Object> node : nodeList) {
					node.put("isBookCreator", hasNodePermission);
					node.put("hasPermission", hasNodePermission);
					node.put("hasNodePermission", hasNodePermission);
					// 递归
					buildSbForPkg(nodeList, activityList, hasNodePermission);
				}
			}
		}
	}
	
	/**
	 * 删除缓存
	 * @param ctId
	 */
	@Override
	public void deleteRoomBookCacheable(String ctId) {
		if (StrUtils.isNotEmpty(ctId)) {
			String teacherKey = "room_book::" + ctId + "_teacher";
			String traineeKey = "room_book::" + ctId + "_trainee";
			stringRedisTemplate.delete(teacherKey);
			stringRedisTemplate.delete(traineeKey);
		}
	}

}
