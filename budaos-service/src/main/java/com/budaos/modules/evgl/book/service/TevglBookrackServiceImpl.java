package com.budaos.modules.evgl.book.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalFavority;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.common.enums.CommonEnum;
import com.budaos.modules.evgl.activity.persistence.TevglActivityTestPaperMapper;
import com.budaos.modules.evgl.book.api.TevglBookrackService;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubperiodMapper;
import com.budaos.modules.evgl.book.vo.MyBookVo;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeFavority;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeFavorityMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResgroupMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.tch.vo.TevglTchClassroomTraineeVo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TevglBookrackServiceImpl implements TevglBookrackService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TmeduMeFavorityMapper tmeduMeFavorityMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglActivityTestPaperMapper tevglActivityTestPaperMapper;
	@Autowired
	private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	
	// 显示地址
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	// 权限标识,true为免费
	private static String is_Permission = "isPermission";
	// 收藏标识
	private static String is_Collection = "isCollection";

	/**
	 * 书架列表
	 * 
	 * @author zhouyl加
	 * @date 2021年1月12日
	 * @param params
	 * @param loginUserId
	 * @param region Y 查询我的书架 N 查询云教程
	 * @return
	 */
	@Override
	@SysLog(value="查询活教材列表")
	public R queryBookList( Map<String, Object> params, String loginUserId, String region) {
		if (!CommonEnum.STATE_YES.getCode().equals(region)) {
			return R.ok().put(Constant.R_DATA, new PageInfo<MyBookVo>());
		}
		// 【我的书架】查询参与的、创建的以及免费的
		params.put("loginUserId", loginUserId);
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<MyBookVo> myBookList = tevglBookSubjectMapper.queryMyBookListVo(query);
		// 查询当前登录用户的课堂
		List<String> ctIdList = myBookList.stream().map(a -> a.getCtId()).filter(a -> StrUtils.isNotEmpty(a)).collect(Collectors.toList());
		List<TevglTchClassroomTraineeVo> classroomTraineeList = tevglTchClassroomTraineeMapper.findClassroomTraineeBy(loginUserId, ctIdList);
		myBookList.stream().forEach(info -> {
			// 处理标识
			if (!StrUtils.isNull(info.getPkgLimit()) && "2".equals(info.getPkgLimit())) {
				info.setIfFree(true);
			}
			// 处理封面
			info.setPkgLogo(uploadPathUtils.stitchingPath(info.getPkgLogo(), 12));
			// 计算总浏览量
			int totalViewNum = handleTotalViewNum(info, params);
			info.setTotalViewNum(totalViewNum);
			// 处理是否允许再进入
			handleAccessState(info, classroomTraineeList);
		});
		PageInfo<MyBookVo> pageInfo = new PageInfo<>(myBookList);
		return R.ok().put(Constant.R_DATA, pageInfo);
	}
	
	private void handleAccessState(MyBookVo vo, List<TevglTchClassroomTraineeVo> classroomTraineeList) {
		if (!"3".equals(vo.getClassroomState())) {
			vo.setAccessState(CommonEnum.STATE_YES.getCode());
			return;
		}
		boolean match = classroomTraineeList.stream().anyMatch(a -> a.getCtId().equals(vo.getCtId()) && CommonEnum.STATE_YES.getCode().equals(a.getAccessState()));
		// 如果被设置为允许再进入
		if (match) {
			vo.setAccessState(CommonEnum.STATE_YES.getCode());
		} else {
			vo.setAccessState(CommonEnum.STATE_NO.getCode());
		}
	}
	
	/**
	 * 处理总浏览量
	 * @param subject
	 * @param params
	 * @return
	 */
	private int handleTotalViewNum(MyBookVo subject, Map<String, Object> params) {
		int totalViewNum = 0;
		params.clear();
		if (!StrUtils.isNull(subject.getRefPkgId())) {
			List<String> pkgIds = tevglPkgInfoMapper.findPkgIdListByRefPkgId(subject.getRefPkgId());
			if (pkgIds != null && pkgIds.size() > 0) {
				params.clear();
				params.put("pkgIds", pkgIds);
				List<Map<String, Object>> subjectList = tevglActivityTestPaperMapper.queryTotalViewNum(params);
				if (subjectList != null && subjectList.size() > 0) {
					for (Map<String, Object> map : subjectList) {
						String viewNumStr = map.get("view_num").toString();
						if (viewNumStr == null) {
							viewNumStr = "0";
						}
						int viewNum = Integer.parseInt(viewNumStr);
						totalViewNum += viewNum;
					}
				}
			}
		}
		if (totalViewNum == 0) {
			return Integer.parseInt(subject.getViewNum().toString());
		}else {
			return totalViewNum;
		}
	}
	
	/**
	 * 活教材列表
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	@SysLog(value="查询活教材列表")
	public R queryLiveBookList(Map<String, Object> params, String loginUserId) {
		// 处理相关查询条件，并返回排序字段
		String sidx = doHandleMyParams(params, loginUserId);
		Query query = new Query(params);
		query.put("sidx", sidx);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> list = tevglBookSubjectMapper.queryLiveBookList(query);
		log.debug("根据条件查询记录：" + params);
		log.debug("结果：" + list.size());
		// 登录用户收藏的教学包教材
		List<TmeduMeFavority> tmeduMeFavorities = new ArrayList<TmeduMeFavority>();
		// 查询当前用户收藏的教材信息
		params.clear();
		params.put("traineeId", loginUserId);
		tmeduMeFavorities = tmeduMeFavorityMapper.selectListByMap(params);
		// 当前登录用户所在的课堂
		//List<String> refPkgIds = findTraineeRoomUsingPkgId(loginUserId);
		List<Map<String, Object>> refPkgIds = findTraineeRoomUsingPkgV2(loginUserId);
		
		List<Map<String, Object>> viewNumList = new ArrayList<Map<String,Object>>();
		if (list != null && list.size() > 0) {
			Map<String, Object> map = new HashMap<>();
			List<Object> idList = list.stream().filter(a -> !StrUtils.isNull(a.get("refPkgId")))
					.map(a -> a.get("refPkgId"))
					.collect(Collectors.toList());
			map.put("refPkgIds", idList);
			viewNumList = tevglPkgInfoMapper.countSubjectViewNumMap(map);
		}
		for (Map<String, Object> subject : list) {
			// 处理是否收藏了这个课程
			handleIsCollectionThisSubject(subject, loginUserId, tmeduMeFavorities);
			// 权限处理
			handlePermissionV2(subject, loginUserId, refPkgIds);
			if (!StrUtils.isNull(subject.get("pkgLimit")) && "2".equals(subject.get("pkgLimit"))) {
				subject.put("isFree", true);
			}
			// 处理教学包封面图
			handlePkgLogo(subject);
			// 统计当前父教学包的，所有子教学包对应的教材的浏览量总和
			
			//int totalViewNum = handleTotalViewNum(subject, loginUserId, params);
			Integer totalViewNum = 0;
			if (!StrUtils.isNull(subject.get("refPkgId"))) {
				//Integer totalViewNum = tevglPkgInfoMapper.countSubjectViewNum(subject.get("refPkgId"));
				for (Map<String, Object> map : viewNumList) {
					if (!StrUtils.isNull(map.get("refPkgId")) && map.get("refPkgId").equals(subject.get("refPkgId"))) {
						totalViewNum += Integer.valueOf(map.get("viewNum").toString());
					}
				}
				subject.put("totalViewNum", totalViewNum);
			} else {
				subject.put("totalViewNum", 0);
			}
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return R.ok().put(Constant.R_DATA, pageInfo);
	}
	
	/**
	 * <b>获取当前登录用户，所有所在课堂中，使用的教学包（教材）</b>
	 * @param loginUserId
	 * @return
	 */
	private List<Map<String, Object>> findTraineeRoomUsingPkgV2(String loginUserId) {
		List<Map<String, Object>> dataList = new ArrayList<>();
		if (StrUtils.isEmpty(loginUserId)) {
			return dataList;
		}
		List<String> ctIdList = tevglTchClassroomTraineeMapper.findCtIdByTraineeId(loginUserId);
		if (ctIdList != null && ctIdList.size() > 0) {
			Map<String, Object> params = new HashMap<>();
			// 查询课堂所引用的教学包
			params.clear();
			params.put("ctIds", ctIdList);
			dataList = tevglPkgInfoMapper.queryCtRoomUsedRefPkgIdV2(params);
		}
		return dataList;
	}
	
	/**
	 * 处理查询条件，并返回排序字段
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	private String doHandleMyParams(Map<String, Object> params, String loginUserId) {
		// 获取排序条件
		String sortField = StrUtils.isNull(params.get("sidx")) ? "1" : params.get("sidx").toString();
		String sidx = "t.deployTime";
		switch (sortField) {
			case "1":
				sidx = "t.deployTime";  // 最新
				break;
			case "2":
				sidx = "t.viewNum";  // 最热
				break;
			default:
				break;
		}
		// 查询条件
		params.put("order", "desc");
		params.put("loginUserId", loginUserId);
		// 兼容同职业路径对应多个课程体系的情况
		if (!StrUtils.isNull(params.get("majorId"))) {
			List<String> subjectRefList = tevglBookSubperiodMapper.findSubjectIdListByMajorId(params.get("majorId").toString());
            params.remove("majorId");
            params.put("subjectRefList", subjectRefList);
		}
		return sidx;
	}
	

	/**
	 * 处理总浏览量
	 * @param subject
	 * @param params
	 * @return
	 */
	@Deprecated
	private int handleTotalViewNum(Map<String, Object> subject, String loginUserId, Map<String, Object> params) {
		int totalViewNum = 0;
		params.clear();
		if (!StrUtils.isNull(subject.get("refPkgId"))) {
			params.put("refPkgId", subject.get("refPkgId"));
			List<TevglPkgInfo> pkgList = tevglPkgInfoMapper.selectListByMap(params);
			List<String> pkgIds = pkgList.stream().map(a -> a.getPkgId()).collect(Collectors.toList());
			params.clear();
			params.put("pkgIds", pkgIds);
			List<Map<String, Object>> subjectList = tevglActivityTestPaperMapper.queryTotalViewNum(params);
			if (subjectList != null && subjectList.size() > 0) {
				for (Map<String, Object> map : subjectList) {
					String viewNumStr = map.get("view_num").toString();
					if (viewNumStr == null) {
						viewNumStr = "0";
					}
					int viewNum = Integer.parseInt(viewNumStr);
					totalViewNum += viewNum;
				}
			}
		}
		
		if (totalViewNum == 0) {
			return Integer.parseInt(subject.get("viewNum").toString());
		}else {
			return totalViewNum;
		}
	}

	/**
	 * 判断是否收藏了该课程（免费的教材才可以收藏！）
	 * @param subject 课程相关信息
	 * @param loginUserId 当前登录人
	 * @param tmeduMeFavorities 收藏记录
	 */
	private void handleIsCollectionThisSubject(Map<String, Object> subject, String loginUserId,
			List<TmeduMeFavority> tmeduMeFavorities) {
		// 没登录标识为收藏
		if (StrUtils.isEmpty(loginUserId)) {
			subject.put(is_Collection, false);
		}
		// 如果收藏目标id和课程id相同则标识该课程已收藏
		boolean flag = tmeduMeFavorities.stream().anyMatch(a -> a.getTargetId().equals(subject.get("subjectId")));
		if (flag) {
			subject.put(is_Collection, true);
		}else {
			subject.put(is_Collection, false);
		}
		
	}

	/**
	 * 处理课程封面图
	 * @param subject
	 */
	private void handlePkgLogo(Map<String, Object> subject) {
		subject.put("pkgLogo", uploadPathUtils.stitchingPath(subject.get("pkgLogo"), "12"));
	}

	/**
	 * 点击课程时是否有查看的权限
	 * @param subject
	 * @param loginUserId
	 * @param ctIdList
	 */
	@Deprecated
	private void handleIsPermission(Map<String,Object> subject, String loginUserId, List<String> ctIdList, List<String> refPkgIds) {
		// 未登录也无法查看
		if (StrUtils.isEmpty(loginUserId)) {
			subject.put(is_Permission, false);
			return;
		}
		// 如果为创建者
		if (loginUserId.equals(subject.get("createUserId"))) {
			subject.put(is_Permission, true);
			subject.put("isCreator", true);
		} else {
			subject.put("isCreator", false);
			// 否则判断是否加入了课堂
			//boolean flag = ctIdList.stream().anyMatch(a -> a.equals(subject.get("ctId")));
			boolean flag = refPkgIds.stream().anyMatch(a -> a.equals(subject.get("pkgId")));
			subject.put(is_Permission, flag);
		}
	}
	
	/**
	 * <b>处理标识，前端只根据一个值来判断即可，具体逻辑交由后端处理</b>
	 * @param subject
	 * @param loginUserId
	 * @param refPkgIds
	 * @apiNote 
	 * <P>
	 *   <ul>
	 *     <li>默认不可以查看</li>
	 *     <li>如果没有登录，不管是谁，都不可以查看当前活教材</li>
	 *     <li>如果没有登录，不管是谁，都不可以查看当前活教材</li>
	 *     <li>如果教材是免费，只要登录了，不管是谁，都可以看</li>
	 *     <li>否则教材不是免费，要判断是否为创建者，判断登录用户所学的课堂，是否正在使用该教材</li>
	 *   </ul>
	 * </p>
	 */
	public void handlePermissionV2(Map<String,Object> subject, String loginUserId, List<Map<String, Object>> refPkgIds) {
		// 默认不可以查看
		subject.put("ifCanView", false);
		subject.put("isCreator", false);
		// 如果没有登录，不管是谁，都不可以查看当前活教材
		if (StrUtils.isEmpty(loginUserId)) {
			return;
		}
		// 如果教材是免费，只要登录了，不管是谁，都可以看
		if (!StrUtils.isNull(subject.get("pkgLimit")) && "2".equals(subject.get("pkgLimit"))) {
			subject.put("ifCanView", true);
			return;
		}
		// 如果是此教材的创建者
		if (loginUserId.equals(subject.get("createUserId"))) {
			subject.put("isCreator", true);
			subject.put("ifCanView", true);
		} else {
			// 如果匹配上了，说明，当前课堂使用的这个教学包最新子包
			//boolean flag = refPkgIds.stream().anyMatch(a -> a.get("ref_pkg_id").equals(subject.get("pkgId")));
			for (int i = 0; i < refPkgIds.size(); i++) {
				Map<String, Object> a = refPkgIds.get(i);
				boolean flag = a.get("ref_pkg_id").equals(subject.get("pkgId"));
				// 2021-11-15 新增需求：如果当前登录用户所用教材在相关的课堂中，课堂还未结束，则不允许查看
				boolean ifRoomEnd = StrUtils.notNull(a.get("classroom_state")) && "3".equals(a.get("classroom_state"));
				log.info("课堂 => {} 是否已结束 {}", a.get("ct_id"), ifRoomEnd);
				if (flag && ifRoomEnd) {
					subject.put("ifCanView", true);
					break;
				}
			}
		}
	}

	/**
	 * 查看课程详情
	 * 
	 * @author zhouyl加
	 * @date 2021年1月13日
	 * @param ctId        课堂id
	 * @param subjectId   课程id
	 * @param pkgId       教学包id
	 * @param loginUserId 当前登录用户
	 * @param region 	  Y 查询我的书架 N 查询云教程
	 * @return
	 */
	@Override
	public R viewSubjectInfo(String ctId, String subjectId, String pkgId, String loginUserId) {
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (subject == null) {
			return R.error("课堂没有相应的课程资源信息");
		}
		Map<String, Object> params = new HashMap<>();
		
		Map<String, Object> subjectInfo = new HashMap<>();
		// 填充信息
		subject.setSubjectLogo(
				budaosFieAccessPath + uploadPathUtils.getPathByParaNo("12") + "/" + subject.getSubjectLogo());
		subjectInfo.put("subjectId", subject.getSubjectId());
		subjectInfo.put("chapterId", subject.getSubjectId());
		subjectInfo.put("subjectAuthor", subject.getSubjectAuthor());
		subjectInfo.put("subjectDesc", subject.getSubjectDesc());
		subjectInfo.put("subjectLogo", subject.getSubjectLogo());
		subjectInfo.put("subjectName", subject.getSubjectName());
		subjectInfo.put("chapterName", subject.getSubjectName());
		subjectInfo.put("subject", "subject"); // 标识为课程节点
		
		params.clear();
		params.put("sidx", "order_num");
		params.put("order", "asc");
		params.put("state", "Y");
		params.put("subjectId", subjectId);
		// 课堂详情页面里才需要
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			params.put("pkgId", pkgId);
		}
		// 查出这本书所有的章节
		List<Map<String, Object>> chapterList = tevglBookChapterMapper.selectSimpleListMap(params);
		subjectInfo.put("chapterNum", chapterList.size()); // 章节总数
		List<Map<String, Object>> chilren = new ArrayList<>();
		if (StrUtils.isNotEmpty(subject.getSubjectRef())) {
			TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
			if (pkgInfo == null) {
				return R.error("资源暂未开放");
			}
			chilren = getbookData(ctId, pkgId, pkgInfo.getRefPkgId(), subject.getSubjectId(),
					chapterList, 0, subject.getCreateUserId(), loginUserId);
		}else {
			// 获取构建好层次后的数据
			chilren = getbookData(ctId, null, null, subject.getSubjectId(),
					chapterList, 0, subject.getCreateUserId(), loginUserId);
		}
		
		handleSortNum(chilren);
		subjectInfo.put("children", chilren == null ? new ArrayList<>() : chilren);
		// 课程的浏览量+1
		TevglBookSubject tevglBookSubject = new TevglBookSubject();
		tevglBookSubject.setSubjectId(subjectId);
		tevglBookSubject.setViewNum(1);
		tevglBookSubjectMapper.updateNum(tevglBookSubject);
		return R.ok().put(Constant.R_DATA, subjectInfo);
	}

	/**
	 * 处理序号
	 * 
	 * @param list
	 * @param index 第几次循环，默认传值0
	 */
	private void handleSortNum(List<Map<String, Object>> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		int index = 1;
		// 找到根节点
		for (Map<String, Object> node : list) {
			node.put("serial", index);
			deep(node, node);
			node.put("chapterName", +index + " " + node.get("chapterName"));
			index++;
		}
	}

	/**
	 * 递归处理序号
	 * 
	 * @param currNode   当前节点
	 * @param parentNode 父节点
	 */
	private void deep(Map<String, Object> currNode, Map<String, Object> parentNode) {
		int index = 0;
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> nodeList = (List<Map<String, Object>>) currNode.get("children");
		if (nodeList != null && nodeList.size() > 0) {
			for (Map<String, Object> node : nodeList) {
				index++;
				node.put("serial", parentNode.get("serial") + "." + index);
				node.put("chapterName", parentNode.get("serial") + "." + index + " " + node.get("chapterName"));
				deep(node, node);

			}
		}
	}

	/**
	 * 递归构建树形数据
	 * 
	 * @param ctId                课堂id
	 * @param pkgId               必传参数，教学包主键，此值是课堂表pkgId字段的值 备注：select pkg_id from
	 *                            t_evgl_tch_classroom where ct_id = ""
	 * @param refPkgId            参数pkgId对应的数据库记录中的ref_pkg_id字段的值 ，备注select
	 *                            ref_pkg_id from t_evgl_pkg_info where pkg_id =
	 *                            (select pkg_id from t_evgl_tch_classroom where
	 *                            ct_id = "")
	 * @param parentId            必传参数，父节点
	 * @param chapterList         必传参数，课程所有章节集合
	 * @param level               必传参数，节点处于的层级，默认请传0
	 * @param subjectCreateUserId 必传参数，课程的创建者
	 * @param loginUserId         必传参数，当前登录用户
	 * @return
	 */
	private List<Map<String, Object>> getbookData(String ctId, String pkgId, String refPkgId, String parentId,
			List<Map<String, Object>> chapterList, int level, String subjectCreateUserId, String loginUserId) {
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		if (subjectCreateUserId.equals(loginUserId)) { // 如果是创建者
			nodeList = chapterList.stream().filter(a -> a.get("parentId").equals(parentId))
					.collect(Collectors.toList());
		} else { // 如果是学员,设置学员可见
			nodeList = chapterList.stream()
					.filter(a -> a.get("parentId").equals(parentId) && "Y".equals(a.get("isTraineesVisible")))
					.collect(Collectors.toList());
		}
		if (nodeList != null && nodeList.size() > 0) {
			level++;
			for (int i = 0; i < nodeList.size(); i++) {
				Map<String, Object> map = nodeList.get(i);
				map.put("type", "chapter"); // 当前为章节节点
				map.put("level", level);
				// 递归调用
				List<Map<String, Object>> children = getbookData(ctId, pkgId, refPkgId, (String) map.get("chapterId"),
						chapterList, level, subjectCreateUserId, loginUserId);
				if (children != null && children.size() > 0) {
					map.put("children", children);
				} else {
					map.put("children", null);
				}
			}
		}
		return nodeList;
	}

	/**
	 * 查看章节详情
	 * 
	 * @author zhouyl加
	 * @date 2021年1月13日
	 * @param chapterId   章节id
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewChapterInfo(String chapterId, String loginUserId) {
		if (StrUtils.isEmpty(chapterId)) {
			return R.error("必传参数为空");
		}
		// 最终返回数据
		Map<String, Object> info = new HashMap<>();
		TevglBookChapter chapter = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapter != null) {
			info.put("chapterId", chapter.getChapterId());
			info.put("subjectId", chapter.getSubjectId());
			info.put("parentId", chapter.getParentId());
			info.put("chapterName", chapter.getChapterName());
			info.put("chapterDesc", chapter.getChapterDesc());
			info.put("chapterContent", chapter.getChapterContent());
			info.put("classHour", chapter.getClassHour());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("chapterId", chapterId);
			//params.put("pkgId", pkgId);
			params.put("sidx", "t1.sort_num");
			params.put("order", "asc");
			List<Map<String,Object>> pkgResGroupList = tevglPkgResgroupMapper.selectSimpleListMap2(params);
			// 我的书架不展示【活动】标签
			pkgResGroupList = pkgResGroupList.stream().filter(a -> StrUtils.isNull(a.get("dictCode")) || !"2".equals(a.get("dictCode"))).collect(Collectors.toList());
			info.put("pkgResGroupList", pkgResGroupList);
		}
		return R.ok().put(Constant.R_DATA, info);
	}
	
	/**
	 * 收藏课程
	 * @author zhouyl加
	 * @date 2021年1月29日
	 * @param subjectId 课程id
	 * @param pkgId 教学包id
	 * @param loginUserId 当前登录人
	 * @return
	 */
	@Override
	public R collectionThisSubject(String subjectId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (tevglBookSubject == null) {
			return R.error("无法收藏");
		}
		String favorityType = "";
		if (StrUtils.isEmpty(tevglBookSubject.getSubjectRef())) {
			favorityType = GlobalFavority.FAVORITY_1_SUBJECT;
		} else {
			favorityType = GlobalFavority.FAVORITY_10_SUBJECT_LIVE;
		}
		// 查询收藏表中是否有该记录，控制不重复插入
		Map<String, Object> params = new HashMap<>();
		params.put("targetId", subjectId);
		params.put("favorityType", favorityType);
		params.put("traineeId", loginUserId);
		List<TmeduMeFavority> tmeduMeFavorities = tmeduMeFavorityMapper.selectListByMap(params);
		if (tmeduMeFavorities != null && tmeduMeFavorities.size() > 0) {
			// 根据收藏id删除收藏表里边的记录
			for (TmeduMeFavority tmeduMeFavority : tmeduMeFavorities) {
				tmeduMeFavorityMapper.delete(tmeduMeFavority.getFavorityId());
			}
			return R.ok("取消收藏");
		}
		TmeduMeFavority favority = new TmeduMeFavority();
		favority.setFavorityId(Identities.uuid());
		favority.setFavorityType(favorityType);
		favority.setTargetId(subjectId);
		favority.setTraineeId(loginUserId);
		favority.setFavorityTime(DateUtils.getNowTimeStamp());
		favority.setPkgId(pkgId);
		tmeduMeFavorityMapper.insert(favority);
		return R.ok("收藏成功");
	}
	/*public R collectionThisSubject(String subjectId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		
		Map<String, Object> params = new HashMap<>();
		// 查询收藏表中是否有该记录，控制不重复插入
		params.put("targetId", subjectId);
		params.put("traineeId", loginUserId);
		List<TmeduMeFavority> tmeduMeFavorities = tmeduMeFavorityMapper.selectListByMap(params);
		for (TmeduMeFavority tmeduMeFavority : tmeduMeFavorities) {
			System.out.println("tmeduMeFavority: " + tmeduMeFavority);
		}
		// 查询要收藏的课程信息
		params.clear();
		params.put("traineeId", loginUserId);
		params.put("teacherId", loginUserId);
		params.put("subjectId", subjectId);
		params.put("pkgId", pkgId);
		List<Map<String, Object>> cloudTutorialList = tevglBookSubjectMapper.queryCloudTutorial(params);
		for (Map<String, Object> cloudTutorial : cloudTutorialList) {
			TmeduMeFavority favority = new TmeduMeFavority();
			favority.setFavorityId(Identities.uuid());
			favority.setTraineeId(loginUserId);
			favority.setClassId(null);
			favority.setFavorityTime(DateUtils.getNowTimeStamp());
			favority.setMajorId(null);
			if (cloudTutorial.get("subjectRef") != null) {
				// subjectRef不为空的标识活教材
				favority.setFavorityType(GlobalFavority.FAVORITY_10_SUBJECT_LIVE);
			}else {
				// subjectRef为空的标识课程
				favority.setFavorityType(GlobalFavority.FAVORITY_1_SUBJECT);
			}
			favority.setTargetId(subjectId);
			// 如果收藏表中没有该记录则新增
			if (tmeduMeFavorities == null || tmeduMeFavorities.size() == 0) {
				tmeduMeFavorityMapper.insert(favority);
				// 添加记录后标识该课程已收藏
				handleIsCollectionThisSubject(cloudTutorial, loginUserId, tmeduMeFavorities);
			}else {
				return R.error("课程已收藏");
			}
		}
		return R.ok("收藏成功");
	}*/

}
