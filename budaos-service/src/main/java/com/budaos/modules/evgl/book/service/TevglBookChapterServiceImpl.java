package com.budaos.modules.evgl.book.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.common.annotation.NoRepeatSubmit;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.common.enums.CommonEnum;
import com.budaos.modules.evgl.book.api.TevglBookChapterService;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookChapterVisible;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterVisibleMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.vo.BookTreeVo;
import com.budaos.modules.evgl.book.vo.SaveChapterVisibleVo;
import com.budaos.modules.evgl.book.vo.SaveChapterVo;
import com.budaos.modules.evgl.pkg.api.TevglBookpkgTeamService;
import com.budaos.modules.evgl.pkg.domain.*;
import com.budaos.modules.evgl.pkg.persistence.*;
import com.budaos.modules.evgl.question.domain.TevglQuestionsInfo;
import com.budaos.modules.evgl.question.persistence.TevglQuestionsInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * Title: 章节
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglBookChapterServiceImpl implements TevglBookChapterService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookChapterServiceImpl.class);
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;
	@Autowired
	private TevglBookChapterVisibleMapper tevglBookChapterVisibleMapper;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	@Autowired
	private TevglQuestionsInfoMapper tevglQuestionsInfoMapper;
	@Autowired
	private TevglPkgResgroupAllowCopyMapper tevglPkgResgroupAllowCopyMapper;
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper;
	@Autowired
	private TevglPkgResgroupVisibleMapper tevglPkgResgroupVisibleMapper;
	@Autowired
	private TevglBookSubjectServiceImpl tevglBookSubjectServiceImpl;
	@Autowired
	private TevglPkgDefaultChapterMapper tevglPkgDefaultChapterMapper;
    @Autowired
    private TevglBookpkgTeamService tevglBookpkgTeamService;

	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return
	 */
	@SysLog(value = "查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TevglBookChapter> tevglBookChapterList = tevglBookChapterMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglBookChapterList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglBookChapterList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tevglBookChapterList = tevglBookChapterMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglBookChapterList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglBookChapterList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 新增
	 * 
	 * @param tevglBookChapter
	 * @throws BudaosException
	 */
	@SysLog(value = "新增")
	public R save(TevglBookChapter tevglBookChapter) throws BudaosException {
		if (StrUtils.isEmpty(tevglBookChapter.getCreateUserId())) {
			tevglBookChapter.setCreateUserId(serviceLoginUtil.getLoginUserId());
		}
		if (StrUtils.isEmpty(tevglBookChapter.getState())) {
			tevglBookChapter.setState("Y"); // 状态(Y有效N无效)
		}
		// TODO
		tevglBookChapter.setChapterId(Identities.uuid());
		tevglBookChapter.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglBookChapter);
		tevglBookChapterMapper.insert(tevglBookChapter);
		return R.ok().put("chapterId", tevglBookChapter.getChapterId());
	}

	/**
	 * 修改
	 * 
	 * @param tevglBookChapter
	 * @throws BudaosException
	 */
	@SysLog(value = "修改")
	public R update(TevglBookChapter tevglBookChapter) throws BudaosException {
		if (StrUtils.isEmpty(tevglBookChapter.getCreateUserId())) {
			tevglBookChapter.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		}
		tevglBookChapter.setUpdateTime(DateUtils.getNowTimeStamp());
		// ValidatorUtils.check(tevglBookChapter);
		tevglBookChapterMapper.update(tevglBookChapter);
		return R.ok();
	}

	/**
	 * 单条删除
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "单条删除")
	public R delete(String id) throws BudaosException {
		tevglBookChapterMapper.delete(id);
		return R.ok();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value = "批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglBookChapterMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglBookChapterMapper.selectObjectById(id));
	}

	/**
	 * <p>
	 * 获取指定教材或课程且状态为有效的的直系章节
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年12月23日
	 * @param subjectId
	 * @return
	 */
	@Override
	@SysLog(value = "获取指定教材或课程且状态为有效的的直系章节")
	public List<TevglBookChapter> getDirectLineChapters(String subjectId) {
		Map<String, Object> map = new HashMap<>();
		map.put("parentId", subjectId);
		map.put("state", "Y");
		map.put("sidx", "chapter_no");
		map.put("order", "asc");
		List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMap(map);
		return list;
	}

	@Override
	public TevglBookChapter selectObjectById(String id) {
		TevglBookChapter chapter = tevglBookChapterMapper.selectObjectById(id);
		return chapter;
	}

	/**
	 * 新增章节
	 * 
	 * @author huj
	 * @data 2019年8月8日
	 * @param tevglBookChapter
	 * @return
	 * @apiNote 新增章节时必传参数: chapterName，subjectId，parentId
	 */
	@Override
	public R saveChapterInfo( TevglBookChapter tevglBookChapter, String loginUserId)
			throws BudaosException {
		// 合法性校验
		String subjectId = tevglBookChapter.getSubjectId();
		String parentId = tevglBookChapter.getParentId();
		String chapterName = tevglBookChapter.getChapterName();
		String pkgId = tevglBookChapter.getPkgId();
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(chapterName) || StrUtils.isEmpty(parentId)
				|| StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		// 获取到教学包主键
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay())
				&& !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
			return R.error("已不允许新增");
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
		// 权限校验
		R r2 = checkPermission(tevglBookChapter, loginUserId, pkgInfo);
		if ((Integer) r2.get("code") != 0) {
			return r2;
		}
		tevglBookChapter.setChapterId(Identities.uuid());
		tevglBookChapter.setIsTraineesVisible(StrUtils.isEmpty(tevglBookChapter.getIsTraineesVisible()) ? "Y"
				: tevglBookChapter.getIsTraineesVisible());
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
		// 判断当前节点是否有父节点，如果父节点设置为学员不可见，则本节点也设置为不可见
		TevglBookChapter parentNode = tevglBookChapterMapper.selectObjectById(parentId);
		if (parentNode != null) {
			/*if ("N".equals(parentNode.getIsTraineesVisible())) {
				tevglBookChapter.setIsTraineesVisible("N");
			}*/
			map.clear();
			map.put("pkgId", pkgId);
			map.put("chapterId", parentNode.getChapterId());
			List<TevglBookChapterVisible> list = tevglBookChapterVisibleMapper.selectListByMap(map);
			if (list != null && list.size() > 0) {
				tevglBookChapter.setIsTraineesVisible(list.get(0).getIsTraineesVisible());
			}
		}
		// 入库
		tevglBookChapterMapper.insert(tevglBookChapter);
		// 设置学员是否可见
		TevglBookChapterVisible v = new TevglBookChapterVisible();
		v.setId(Identities.uuid());
		v.setPkgId(pkgId);
		v.setChapterId(tevglBookChapter.getChapterId());
		v.setIsTraineesVisible(tevglBookChapter.getIsTraineesVisible());
		tevglBookChapterVisibleMapper.insert(v);
		// 如果当前登录用户不是此教学包创建者
		//pkgUtils.createTeamDetailData(pkgId, tevglBookChapter.getChapterId(), loginUserId, pkgInfo.getCreateUserId());
		pkgUtils.createTeamDetailDataV2(pkgInfo, tevglBookChapter.getChapterId(), loginUserId);
		// 默认生成[课程内容]分组
		doCreateDefaultGroup(pkgId, pkgInfo.getSubjectId(), tevglBookChapter.getChapterId(), loginUserId);
		// 即时通讯
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			// 重新刷新章节与资源
			cbRoomUtils.sendIm(tevglTchClassroom.getCtId(), "reloadresource", "other");
		}
		return R.ok("新增成功").put("chapterId", tevglBookChapter.getChapterId());
	}

	/**
	 * 创建默认分组
	 * 
	 * @param pkgId
	 * @param chapterId
	 * @param loginUserId
	 */
	private void doCreateDefaultGroup(String pkgId, String subjectId, String chapterId, String loginUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("chapterId", chapterId);
		List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(params);
		// 如果有[课程内容],不重复生成
		boolean flag = list.stream().anyMatch(a -> a.getDictCode().equals("1"));
		if (flag) {
			return;
		}
		// 填充信息
		TevglPkgResgroup tevglPkgResgroup = new TevglPkgResgroup();
		tevglPkgResgroup.setResgroupId(Identities.uuid()); // 主键
		tevglPkgResgroup.setPkgId(pkgId);
		tevglPkgResgroup.setSubjectId(subjectId);
		tevglPkgResgroup.setChapterId(chapterId);
		tevglPkgResgroup.setResgroupName("课程内容");
		tevglPkgResgroup.setDictCode("1");
		tevglPkgResgroup.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
		tevglPkgResgroup.setState("Y"); // 状态(Y有效N无效)
		tevglPkgResgroup.setResgroupTotal(0); // 资源总数
		tevglPkgResgroup.setCreateUserId(loginUserId);
		tevglPkgResgroup.setGroupType(GlobalActivity.ACTIVITY_GROUP_TYPE_1);
		// 排序号处理
		params.put("pkgId", pkgId);
		params.put("chapterId", tevglPkgResgroup.getChapterId());
		Integer sortNum = tevglPkgResgroupMapper.getMaxSortNum(params);
		tevglPkgResgroup.setSortNum(sortNum);
		tevglPkgResgroupMapper.insert(tevglPkgResgroup);
		// 生成分组之后，再随之生成资源记录
		TevglPkgRes res = new TevglPkgRes();
		res.setResId(Identities.uuid());
		res.setPkgId(pkgId);
		res.setResgroupId(tevglPkgResgroup.getResgroupId());
		tevglPkgResMapper.insert(res);
		// 教学包的资源数+1，由于章节分组与资源一对一,直接把分组数当作资源数
		pkgUtils.plusPkgResNum(pkgId, 1);
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

	/**
	 * 权限校验(新增章节时所用)
	 * 
	 * @param tevglBookChapter
	 * @param loginUserId
	 * @return
	 */
	private R checkPermission(TevglBookChapter tevglBookChapter, String loginUserId, TevglPkgInfo tevglPkgInfo) {
		String createUserId = null;
		String subjectId = tevglBookChapter.getSubjectId();
		String parentId = tevglBookChapter.getParentId();
		// 表示新增的是直系(一级)章节
		if (subjectId.equals(parentId)) {
			TevglBookSubject subjectInfo = tevglBookSubjectMapper.selectObjectById(subjectId);
			if (subjectInfo == null) {
				return R.error("无效的记录");
			}
			createUserId = subjectInfo.getCreateUserId();
		} else {
			TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(parentId);
			if (chapterInfo == null) {
				return R.error("无效的记录");
			}
			createUserId = chapterInfo.getCreateUserId();
		}
		log.debug("当前登录用户:[" + loginUserId + "]" + "创建者:[" + createUserId + "]");
		if (createUserId == null) {
			return R.error("暂无权限，操作失败");
		}
		if (tevglPkgInfo == null) {
            return R.error("教学包参数异常");
        }
        // 如果登录用户是教学包的接管者
        if (loginUserId.equals(tevglPkgInfo.getReceiverUserId())) {
            return R.ok();
        }
		// 当创建者不是当前登录用户时,校验
		if (!loginUserId.equals(createUserId)) {
			Map<String, Object> ps = new HashMap<String, Object>();
			ps.put("subjectId", subjectId);
			ps.put("userId", loginUserId);
			List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(ps);
			if (teamList == null || teamList.size() == 0) {
				return R.error("暂无权限，操作失败");
			}
			// 如果是新增直系(一级)章节
			if (subjectId.equals(parentId)) {
				return R.error("暂无权限新增一级章节");
			} else {
				// 细化到章节校验
				String teamId = teamList.get(0).getTeamId();
				ps.clear();
				ps.put("teamId", teamId);
				List<TevglBookpkgTeamDetail> teamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(ps);
				log.debug("用户[" + loginUserId + "],拥有章节权限的数量:" + teamDetailList.size());
				if (teamDetailList == null || teamDetailList.size() == 0) {
					return R.error("没有权限，无法操作，可联系作者授权");
				}
				// 获取拥有的章节权限
				List<String> chapterIdList = teamDetailList.stream().map(a -> a.getChapterId())
						.collect(Collectors.toList());
				// 去空
				chapterIdList = chapterIdList.stream().filter(a -> StrUtils.isNotEmpty(a)).collect(Collectors.toList());
				if (chapterIdList == null || chapterIdList.size() == 0) {
					return R.error("没有章节权限，无法操作，可联系作者授权");
				}
				// 当前操作的章节和拥有的权限比较
				if (!chapterIdList.contains(parentId)) {
					String msg = "";
					TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(parentId);
					if (chapterInfo != null) {
						msg = "章节【" + chapterInfo.getChapterName() + "】，";
					}
					return R.error(msg + "没有权限，操作失败，可联系作者授权");
				}
			}
		}
		// 控制创建者为课程的创建者
		tevglBookChapter.setCreateUserId(createUserId);
		tevglBookChapter.setUpdateUserId(loginUserId);
		return R.ok();
	}

	/**
	 * 修改章节
	 * 
	 * @param tevglBookChapter
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R updateChapterInfo(TevglBookChapter tevglBookChapter, String loginUserId) {
		// 合法性校验
		String subjectId = tevglBookChapter.getSubjectId();
		String chapterName = tevglBookChapter.getChapterName();
		String pkgId = tevglBookChapter.getPkgId();
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(chapterName) || StrUtils.isEmpty(loginUserId)
				|| StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的教学包");
		}
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(tevglBookChapter.getChapterId());
		if (chapterInfo == null) {
			return R.error("无效的记录");
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
		Map<String, Object> map = new HashMap<>();
		map.put("parentId", chapterInfo.getParentId());
		List<TevglBookChapter> tevglBookChapterList = tevglBookChapterMapper.selectListByMap(map);
		boolean flag = tevglBookChapterList.stream().anyMatch(a -> !a.getChapterId().equals(chapterInfo.getChapterId())
				&& a.getChapterName().equals(chapterName.trim()));
		if (flag) {
			return R.error("同级章节下，不允许有重复的章节名称");
		}
		// 权限校验
		R r2 = checkPermission(tevglBookChapter, loginUserId, tevglPkgInfo);
		if ((Integer) r2.get("code") != 0) {
			return r2;
		}
		tevglBookChapter.setUpdateUserId(loginUserId);
		tevglBookChapter.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglBookChapterMapper.update(tevglBookChapter);
		// 设置学员是否可见
		handleIsVisible(pkgId, tevglBookChapter.getChapterId(), tevglBookChapter.getIsTraineesVisible(), map);
		// 判断当前节点是否子节点，
		// 如果当前节点设置为学员不可见，则本节点也设置为不可见
		// 如果当前节点设置为学员可见，则本节点的子节点设为可见
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", subjectId);
		params.put("parentId", tevglBookChapter.getChapterId());
		List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMapForSimple(params);
		if (list != null && list.size() > 0) {
			// 递归修改
			changeIsTraineesVisible(list, params, tevglBookChapter.getIsTraineesVisible(), pkgId);
		}
		return R.ok("修改成功");
	}

	/**
	 * 设置学员是否可见
	 * 
	 * @param pkgId
	 * @param chapterId
	 * @param isTraineesVisible
	 * @param params
	 */
	private void handleIsVisible(String pkgId, String chapterId, String isTraineesVisible, Map<String, Object> params) {
		params.clear();
		params.put("pkgId", pkgId);
		params.put("chapterId", chapterId);
		List<TevglBookChapterVisible> visibleList = tevglBookChapterVisibleMapper.selectListByMap(params);
		if (visibleList != null && visibleList.size() > 0) {
			TevglBookChapterVisible v = visibleList.get(0);
			v.setIsTraineesVisible(isTraineesVisible);
			tevglBookChapterVisibleMapper.update(v);
		} else {
			TevglBookChapterVisible v = new TevglBookChapterVisible();
			v.setId(Identities.uuid());
			v.setPkgId(pkgId);
			v.setChapterId(chapterId);
			v.setIsTraineesVisible(isTraineesVisible);
			tevglBookChapterVisibleMapper.insert(v);
		}
	}

	/**
	 * 递归修改学员是否可见
	 * 
	 * @param list
	 * @param params
	 * @param isTraineesVisible
	 */
	private void changeIsTraineesVisible(List<TevglBookChapter> list, Map<String, Object> params,
			String isTraineesVisible, String pkgId) {
		if (list == null || list.size() == 0) {
			return;
		}
		List<String> chapterIds = list.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
		for (String chapterId : chapterIds) {
			TevglBookChapter t = new TevglBookChapter();
			t.setChapterId(chapterId);
			t.setIsTraineesVisible(isTraineesVisible);
			tevglBookChapterMapper.update(t);
			handleIsVisible(pkgId, chapterId, isTraineesVisible, params);
			params.clear();
			params.put("parentId", chapterId);
			List<TevglBookChapter> bookChapterList = tevglBookChapterMapper.selectListByMapForSimple(params);
			if (bookChapterList != null && bookChapterList.size() > 0) {
				changeIsTraineesVisible(bookChapterList, params, isTraineesVisible, pkgId);
			}
		}
	}

	/**
	 * 查看章节
	 * 
	 * @param chapterId   章节主键
	 * @param loginUserId 登录用户
	 * @param pkgId       教学包主键，根据业务需求，ctId与pkgId必传其一
	 * @param type        type为空时，表示是从课堂里查看的章节，不为空则是从教学包里查看的，这里需要额外处理下
	 * @return
	 */
	@Override
	public R viewChapterInfo(String chapterId, String loginUserId, String pkgId, String type) {
		if (StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglBookChapter tevglBookChapter = tevglBookChapterMapper.selectObjectById(chapterId);
		if (tevglBookChapter == null) {
			return R.error("无效的章节");
		}
		// 获取到教学包主键
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的记录");
		}
		// 是否在课堂页面中
		//boolean isInRoomPage = StrUtils.isEmpty(type);
		// 最终返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 3.重新获取该章节的资源分组
		List<Map<String, Object>> pkgResGroupList = new ArrayList<>();
		params.put("ctPkgId", pkgId);
		params.put("chapterId", chapterId);
		params.put("pkgId", pkgId);
		params.put("sidx", "t1.sort_num");
		params.put("order", "asc");
		// 未传值，表示是课堂页面，查看章节
		if (StrUtils.isEmpty(type)) {
			params.put("subjectId", tevglPkgInfo.getSubjectId());
			pkgResGroupList = tevglPkgResgroupMapper.selectSimpleListMap2(params);
			log.debug("根据条件查询章节标签数据：" + params);
			log.debug("结果：" + pkgResGroupList.size());
			// 当当前课堂对应的教学包没有是否允许复制的记录时,则生成一条记录
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
			if (tevglTchClassroom != null) {
				if (pkgResGroupList != null && pkgResGroupList.size() > 0) {
					Map<String, Object> map = new HashMap<>();
					pkgResGroupList.stream().forEach(resgroup -> {
						map.put("pkgId", tevglTchClassroom.getPkgId());
						map.put("subjectId", tevglPkgInfo.getSubjectId());
						map.put("resgroupId", resgroup.get("resgroupId"));
						List<TevglPkgResgroupAllowCopy> allowCopyList = tevglPkgResgroupAllowCopyMapper
								.selectListByMap(map);
						log.debug("是否有记录：" + allowCopyList.size());
						if (allowCopyList == null || allowCopyList.size() == 0) {
							TevglPkgResgroupAllowCopy cp = new TevglPkgResgroupAllowCopy();
							cp.setCpId(Identities.uuid());
							cp.setPkgId(tevglTchClassroom.getPkgId());
							cp.setSubjectId(tevglPkgInfo.getSubjectId());
							cp.setResgroupId(StrUtils.isNull(resgroup.get("resgroupId")) ? null
									: resgroup.get("resgroupId").toString());
							cp.setIsCanCopy("Y"); // Y允许复制资源内容N不允许
							tevglPkgResgroupAllowCopyMapper.insert(cp);
						}
					});
				}
				boolean hasBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_SUBJECT_RESGROUPVISIBLE);
				// 如果当前用户不是课堂创建者
				if (!loginUserId.equals(tevglTchClassroom.getCreateUserId()) && !hasBtnPermission) {
	                List<Map<String, Object>> dataList = new ArrayList<>();
	                pkgResGroupList.stream().forEach(pkgResGroup -> {
	                	if (!StrUtils.isNull(pkgResGroup.get("isTraineesVisible")) && "Y".equals(pkgResGroup.get("isTraineesVisible"))) {
	                		dataList.add(pkgResGroup);
	                	}
	                });
	                pkgResGroupList = dataList;
	            }
				pkgResGroupList.stream().forEach(pkgResGroup -> {
					if (hasBtnPermission) {
						pkgResGroup.put("hasSetResVisiblePermission", true);
					} else {
						pkgResGroup.put("hasSetResVisiblePermission", false);
					}
                });
			}
		} else {
			/*// 兼容下
			if (StrUtils.isNotEmpty(pkgInfo.getDisplay()) && "3".equals(pkgInfo.getDisplay())) {
				params.put("subjectId", pkgInfo.getSubjectId());
			}*/
			params.put("subjectId", tevglPkgInfo.getSubjectId());
			pkgResGroupList = tevglPkgResgroupMapper.selectSimpleListMap2(params);
			log.debug("查询条件：" + params);
			log.debug("资源分组数量：" + pkgResGroupList.size());
		}
		// 处理包含的视频数量
		pkgResGroupList.stream().forEach(a -> {
			String content = (String) a.get("resContent");
			int num = getNum(content);
			a.put("num", num);
		});
		// 4.返回数据
		Map<String, Object> chapterInfo = converToSimpleMapInfo(tevglBookChapter);
		result.put("chapterInfo", chapterInfo); // 章节数据
		result.put("pkgResGroupList", pkgResGroupList); // 章节分组
		result.put("resNum", tevglPkgInfo.getPkgResCount()); // 教学包资源数量
		return R.ok().put(Constant.R_DATA, result);
	}

	/**
	 * 创建资源分组和资源(不要删除先)
	 * 
	 * @param pkgId            教学包主键
	 * @param loginUserId      登录用户主键
	 * @param dictList         从字典获取的默认分组
	 * @param tevglBookChapter 章节信息
	 */
	/*private void createPkgResGroup(String pkgId, String loginUserId, List<TsysDict> dictList, TevglBookChapter tevglBookChapter) {
		if (dictList == null || dictList.size() == 0) {
			return ;
		}
		String createUserId = tevglBookChapter.getCreateUserId();
		for (int i = 0; i < dictList.size(); i++) {
			TsysDict tsysDict = dictList.get(i);
			TevglPkgResgroup t = new TevglPkgResgroup();
			t.setResgroupId(Identities.uuid());
			t.setPkgId(pkgId);
			t.setChapterId(tevglBookChapter.getChapterId());
			t.setResgroupName(tsysDict.getDictValue());
			t.setCreateTime(DateUtils.getNowTimeStamp());
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
			if ("1".equals(tsysDict.getDictCode())) {
				t.setFlag("Y");
			}
			tevglPkgResgroupMapper.insert(t);
			// 生成默认分组之后，再随之生成资源记录
			TevglPkgRes res = new TevglPkgRes();
			res.setResId(Identities.uuid());
			res.setPkgId(pkgId);
			res.setResgroupId(t.getResgroupId());
			res.setCreateTime(DateUtils.getNowTimeStamp());
			res.setCreateUserId(createUserId);
			// 将章节内容同步过来（只在1课程内容时同步章节内容）
			if ("1".equals(tsysDict.getDictCode())) {
				res.setResContent(tevglBookChapter.getChapterContent());
			}
			tevglPkgResMapper.insert(res);
			// 从而教学包的资源数+1
			pkgUtils.plusPkgResNum(pkgId, 1);
		}
	}
	*/
	/**
	 * 统计数量
	 * 
	 * @param content
	 * @return
	 */
	private int getNum(String content) {
		int num = 0;
		String regEx_img = "<source.*src\\s*=\\s*(.*?)[^>]*?>";
		Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		Matcher m_image = p_image.matcher(content);
		while (m_image.find()) {
			num++;
		}
		return num;
	}

	/**
	 * <p>
	 * 重命名章节名称
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年7月27日
	 * @param chapterId
	 * @param name
	 * @return
	 */
	@Override
	public R rename(String pkgId, String chapterId, String name, String loginUserId) {
		// 非空判断
		if (StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(name) || StrUtils.isEmpty(loginUserId)
				|| StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		/*if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			return R.error("已不允许重命名");
		}*/
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay())
				&& !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
			return R.error("已不允许重命名");
		}
		// 权限校验
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapterInfo == null) {
			return R.error("无效的记录，操作失败");
		}
		// 如果教学包没有被移交
		if (StrUtils.isEmpty(pkgInfo.getReceiverUserId())) {
			if (!loginUserId.equals(chapterInfo.getCreateUserId()) && !loginUserId.equals(pkgInfo.getCreateUserId())) {
				Map<String, Object> map = new HashMap<>();
				map.put("userId", loginUserId);
				map.put("chapterId", chapterId);
				List<TevglBookpkgTeamDetail> list = tevglBookpkgTeamDetailMapper.selectListByMap(map);
				if (list == null || list.size() == 0) {
					String msg = "章节【" + chapterInfo.getChapterName() + "】，";
					return R.error(msg + "没有权限，操作失败，可联系作者授权");
				}
			}
		}
		// 如果教学包被移交了
		if (StrUtils.isNotEmpty(pkgInfo.getReceiverUserId())) {
			// 且登录用户不是接管者时，进行校验
			if (!loginUserId.equals(pkgInfo.getReceiverUserId())) {
				Map<String, Object> map = new HashMap<>();
				map.put("userId", loginUserId);
				map.put("chapterId", chapterId);
				List<TevglBookpkgTeamDetail> list = tevglBookpkgTeamDetailMapper.selectListByMap(map);
				if (list == null || list.size() == 0) {
					String msg = "章节【" + chapterInfo.getChapterName() + "】，";
					return R.error(msg + "没有权限，操作失败，可联系作者授权");
				}
			}
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

	/**
	 * 删除章节
	 * @param pkgId 教学包id
	 * @param chapterId 章节主键
	 * @param loginUserId 当前登录用户
	 * @return
	 * @throws BudaosException
	 */
	@Override
	@Deprecated
	public R remove(String pkgId, String chapterId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		/*if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			return R.error("已不允许删除");
		}*/
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay())
				&& !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
			return R.error("已不允许删除");
		}
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapterInfo == null) {
			return R.error("无效的记录，操作失败");
		}
		// 查询条件
		Map<String, Object> map = new HashMap<>();
		// 权限校验
		if (!loginUserId.equals(chapterInfo.getCreateUserId()) && !loginUserId.equals(pkgInfo.getCreateUserId())) {
			map.put("userId", loginUserId);
			map.put("chapterId", chapterId);
			List<TevglBookpkgTeamDetail> list = tevglBookpkgTeamDetailMapper.selectListByMap(map);
			if (list == null || list.size() == 0) {
				String msg = "章节【" + chapterInfo.getChapterName() + "】，";
				return R.error(msg + "没有权限，操作失败，可联系作者授权");
			}
			// TODO 如果包含有未授权的子章节，不允许删除当前章节
			map.clear();
			map.put("parentId", chapterInfo.getChapterId());
			map.put("state", "Y"); // 状态(Y有效N无效)
			List<TevglBookChapter> tevglBookChapterList = tevglBookChapterMapper.selectListByMap(map);
			if (tevglBookChapterList != null && tevglBookChapterList.size() > 0) {
				return R.error("该节点下还有子节点，请先删除子节点");
			}
		}
		// 是否还有子章节校验
		map.clear();
		map.put("parentId", chapterInfo.getChapterId());
		map.put("state", "Y"); // 状态(Y有效N无效)
		List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			return R.error("该节点下还有子节点，请先删除子节点");
		}
		// 是否有分组
		map.clear();
		map.put("chapterId", chapterId);
		List<Map<String, Object>> pkgResgroupList = tevglPkgResgroupMapper.selectSimpleListMap(map);
		if (pkgResgroupList != null && pkgResgroupList.size() > 0) {
			// 是否有具体资源
			List<String> resgroupIds = pkgResgroupList.stream().map(a -> (String) a.get("resgroupId"))
					.collect(Collectors.toList());
			map.clear();
			map.put("resgroupIds", resgroupIds);
			List<TevglPkgRes> pkgResList = tevglPkgResMapper.selectListByMap(map);
			if (pkgResList != null && pkgResList.size() > 0) {
				// return R.error("请先删除章节下的资源");
				pkgResList.stream().forEach(a -> {
					tevglPkgResMapper.delete(a.getResId());
				});
			}
			for (Map<String, Object> pkgResgroup : pkgResgroupList) {
				tevglPkgResgroupMapper.delete(pkgResgroup.get("resgroupId"));
			}
			// 教学包资源数对应减少
			// String pkgId = pkgResgroupList.get(0).get("pkgId").toString();
			pkgUtils.plusPkgResReduceNum(pkgId, -pkgResgroupList.size());
		}
		// 删除学员是否可见
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("chapterId", chapterId);
		List<TevglBookChapterVisible> visibleList = tevglBookChapterVisibleMapper.selectListByMap(params);
		if (visibleList != null && visibleList.size() > 0) {
			tevglBookChapterVisibleMapper.delete(visibleList.get(0).getId());
		}
		// 删除章节
		tevglBookChapterMapper.delete(chapterId);
		return R.ok("删除成功");
	}

	@Override
	public R removeV2(String pkgId, String chapterId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(loginUserId)) {
            return R.error("必传参数为空");
        }
        TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (pkgInfo == null) {
            return R.error("无效的记录");
        }
        if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay())
                && !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
            return R.error("已不允许删除");
        }
        TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(chapterId);
        if (chapterInfo == null) {
            return R.error("无效的记录，操作失败");
        }
        // 找到该节点下所有子节点
        List<String> chapterIdList = findChapterIdList(chapterInfo.getSubjectId(), chapterInfo.getChapterId());
        log.info("chapterIdList => {}", chapterIdList);
        // 查询条件
        Map<String, Object> map = new HashMap<>();
        // 如果不是教学包创建，又不是章节创建者
        if (!loginUserId.equals(pkgInfo.getCreateUserId()) && !loginUserId.equals(chapterInfo.getCreateUserId())) {
            map.clear();
            map.put("pgkId", pkgId);
            map.put("userId", loginUserId);
            List<TevglBookpkgTeam> tevglBookpkgTeams = tevglBookpkgTeamMapper.selectListByMap(map);
            if (tevglBookpkgTeams == null || tevglBookpkgTeams.size() == 0) {
                return R.error("没有权限，操作失败，可联系作者授权");
            }
            map.clear();
            map.put("userId", loginUserId);
            map.put("teamId", tevglBookpkgTeams.get(0).getTeamId());
            List<TevglBookpkgTeamDetail> list = tevglBookpkgTeamDetailMapper.selectListByMap(map);
            if (list == null || list.size() == 0) {
                String msg = "章节【" + chapterInfo.getChapterName() + "】，";
                return R.error(msg + "没有权限，操作失败，可联系作者授权");
            }
            // 用户有权限的章节
            List<String> hasAuthChapterIdList = list.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
            for (String id : chapterIdList){
                if (!hasAuthChapterIdList.stream().anyMatch(v -> v.equals(id))) {
                    return R.error("被删除节点中，出现了没有权限的节点，操作失败，请联系管理员");
                }
            }
        }
        if (chapterIdList != null && chapterIdList.size() > 0) {
            // 找出分组
            map.clear();
            map.put("chapterIdList", chapterIdList);
            List<Map<String, Object>> pkgResgroupList = tevglPkgResgroupMapper.selectSimpleListMap(map);
            if (pkgResgroupList != null && pkgResgroupList.size() > 0) {
                // 是否有具体资源
                List<String> resgroupIds = pkgResgroupList.stream().map(a -> (String) a.get("resgroupId")).collect(Collectors.toList());
                map.clear();
                map.put("resgroupIds", resgroupIds);
                List<String> pkgResIdList = tevglPkgResMapper.selectResIdListByMap(map);
                // 删除资源
                if (pkgResIdList != null && pkgResIdList.size() > 0) {
                    String[] array = pkgResIdList.stream().toArray(String[]::new);
                    tevglPkgResMapper.deleteBatch(array);
                }
                // 删除分组
                tevglPkgResgroupMapper.deleteBatch(resgroupIds.stream().toArray(String[]::new));
                // 教学包资源数对应减少
                pkgUtils.plusPkgResReduceNum(pkgId, -pkgResgroupList.size());
            }
            // 删除章节
            tevglBookChapterMapper.deleteBatch(chapterIdList.stream().toArray(String[]::new));
        }
		return R.ok("删除成功");
	}
	

    /**
     * 获取指定节点下的所有子孙节点
     *
     * @param sourceChapterId 必传参数
     * @return 返回空集合，或者ok的数据
     */
    @Override
    public List<String> findChapterIdList(String subjectId, String sourceChapterId) {
        List<BookTreeVo> allChapterList = tevglBookChapterMapper.findAllChapterList(subjectId);
        return this.findChapterIdList(sourceChapterId, allChapterList);
    }

    /**
     * 获取指定节点下的所有子孙节点
     *
     * @param sourceChapterId
     * @param allChapterList
     * @return
     */
    @Override
    public List<String> findChapterIdList(String sourceChapterId, List<BookTreeVo> allChapterList) {
        List<String> resultList = new ArrayList<>();
        recursion(sourceChapterId, allChapterList, resultList);
        resultList.add(sourceChapterId);
        return resultList;
    }

    private void recursion(String parentId, List<BookTreeVo> allChapterList, List<String> resultList) {
        if (StrUtils.isEmpty(parentId) || allChapterList == null || allChapterList.size() == 0) {
            return;
        }
        // 取出当前节点的子节点
        List<BookTreeVo> nodeList = allChapterList.stream().filter(a -> a.getParentId().equals(parentId)).collect(Collectors.toList());
        // 遍历节点
        if (nodeList != null && nodeList.size() > 0) {
            nodeList.stream().forEach(node -> {
                // 加入集合
                resultList.add(node.getId());
                // 继续
                recursion(node.getId(), allChapterList, resultList);
            });
        }
    }

	
	/**
	 * <p>
	 * 移动
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年7月27日
	 * @param currId   当前ID
	 * @param targetId 目标ID
	 * @return
	 */
	@Override
	public R move(String currId, String targetId, String loginUserId,
			String pkgId) {
		if (StrUtils.isEmpty(currId) || StrUtils.isEmpty(targetId) || StrUtils.isEmpty(loginUserId)
				|| StrUtils.isEmpty(pkgId)) {
			return R.error("操作失败，必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		/*if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			return R.error("已不允许移动");
		}*/
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay())
				&& !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
			return R.error("已不允许移动");
		}
		// 权限校验
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(currId);
		if (chapterInfo == null) {
			return R.error("操作失败");
		}
		if (!loginUserId.equals(chapterInfo.getCreateUserId()) && !loginUserId.equals(pkgInfo.getCreateUserId())) {
			Map<String, Object> map = new HashMap<>();
			map.put("userId", loginUserId);
			map.put("chapterId", chapterInfo.getChapterId());
			List<TevglBookpkgTeamDetail> list = tevglBookpkgTeamDetailMapper.selectListByMap(map);
			if (list == null || list.size() == 0) {
				String msg = "章节【" + chapterInfo.getChapterName() + "】，";
				return R.error(msg + "没有权限，操作失败，可联系作者授权");
			}
		}
		TevglBookChapter curr = tevglBookChapterMapper.selectObjectById(currId); // 当前对象
		TevglBookChapter target = tevglBookChapterMapper.selectObjectById(targetId); // 目标对象
		if (curr != null && target != null) {
			if (!curr.getParentId().equals(target.getParentId())) {
				return R.error("只能同级移动");
			}
			Integer cSort = curr.getOrderNum();
			String currChapterNo = curr.getChapterNo();
			Integer tSort = target.getOrderNum();
			String targetChapterNo = target.getChapterNo();

			curr.setOrderNum(tSort); // 排序号
			curr.setChapterNo(targetChapterNo); // 章节编号
			tevglBookChapterMapper.update(curr);

			target.setOrderNum(cSort);
			target.setChapterNo(currChapterNo);
			tevglBookChapterMapper.update(target);
		}
		return R.ok();
	}

	/**
	 * 取部分属性
	 * 
	 * @param tevglBookChapter
	 * @return
	 */
	private Map<String, Object> converToSimpleMapInfo(TevglBookChapter tevglBookChapter) {
		Map<String, Object> chapterInfo = new HashMap<String, Object>();
		if (tevglBookChapter == null) {
			return chapterInfo;
		}
		chapterInfo.put("chapterId", tevglBookChapter.getChapterId());
		chapterInfo.put("subjectId", tevglBookChapter.getSubjectId());
		chapterInfo.put("chapterName", tevglBookChapter.getChapterName());
		chapterInfo.put("chapterIcon", tevglBookChapter.getChapterIcon());
		chapterInfo.put("chapterContent", tevglBookChapter.getChapterContent());
		return chapterInfo;
	}
	
	/**
	 * 批量设置对学员是否可见
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R batchSetChapterIsTraineesVisible( JSONObject jsonObject, String loginUserId) {
		String ctId = jsonObject.getString("ctId");
		String pkgId = jsonObject.getString("pkgId");
		String isTraineesVisible = jsonObject.getString("isTraineesVisible"); // Y可见/N不可见
		JSONArray jsonArray = jsonObject.getJSONArray("chapterIds"); // 小程序传入的是，未选中的章节，则需要把它们设为不可见
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(isTraineesVisible)) {
			return R.error("必传参数为空");
		}
		if (jsonArray == null) {
			return R.error("请选择章节");
		}
		TevglTchClassroom tevglTchClassroom = null;
		if (StrUtils.isNotEmpty(ctId)) {
			tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		} else {
			tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		}
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的记录");
		}
		// 如果既不是课堂创建者，又不是课堂助教，则不允许设置
		boolean hasPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE);
		if (!hasPermission) {
			return R.error("没有权限，设置失败");
		}
		// 找到本课程所有章节
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", tevglPkgInfo.getSubjectId());
		List<TevglBookChapter> allChapterList = tevglBookChapterMapper.selectListByMap(params);
		List<String> allChapterIds = allChapterList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
		// 所有可见数据
        params.clear();
        params.put("pkgId", pkgId);
        List<TevglBookChapterVisible> allVisibleList = tevglBookChapterVisibleMapper.selectListByMap(params);
        // 如果小程序端传了值，且为Y，则需要把传入的值，全部更新为Y
        if ("Y".equals(isTraineesVisible)) {
            changeVisibleY(pkgId, allChapterIds, allVisibleList);
        } else {
            changeVisibleN(pkgId, allChapterIds, allVisibleList, jsonArray);
        }
		/*List<String> ids = new ArrayList<String>();
		for (Object chapterId : jsonArray) {
			params.clear();
			params.put("pkgId", pkgId);
			params.put("chapterId", chapterId);
			List<TevglBookChapterVisible> visibleList = tevglBookChapterVisibleMapper.selectListByMap(params);
			if (visibleList != null && visibleList.size() > 0) {
				TevglBookChapterVisible t = visibleList.get(0);
				t.setIsTraineesVisible(isTraineesVisible);
				tevglBookChapterVisibleMapper.update(t);
				ids.add(chapterId.toString());
			} else {
				TevglBookChapterVisible t = new TevglBookChapterVisible();
				t.setId(Identities.uuid());
				t.setPkgId(pkgId);
				t.setChapterId(chapterId.toString());
				if ("N".equals(isTraineesVisible)) {
					t.setIsTraineesVisible("Y");
				} else {
					t.setIsTraineesVisible("N");
				}
				tevglBookChapterVisibleMapper.insert(t);
			}
		}
		// 取差集
		allChapterIds.removeAll(ids);
		// TODO 再更新其它的为 相反的
		for (String chapterId : allChapterIds) {
			params.clear();
			params.put("pkgId", pkgId);
			params.put("chapterId", chapterId);
			List<TevglBookChapterVisible> visibleList = tevglBookChapterVisibleMapper.selectListByMap(params);
			if (visibleList != null && visibleList.size() > 0) {
				TevglBookChapterVisible visible = visibleList.get(0);
				if ("N".equals(isTraineesVisible)) {
					visible.setIsTraineesVisible("Y");
					tevglBookChapterVisibleMapper.update(visible);
				} else {
					visible.setIsTraineesVisible("N");
					tevglBookChapterVisibleMapper.update(visible);
				}
			} else {
				TevglBookChapterVisible t = new TevglBookChapterVisible();
				t.setId(Identities.uuid());
				t.setPkgId(pkgId);
				t.setChapterId(chapterId);
				if ("N".equals(isTraineesVisible)) {
					t.setIsTraineesVisible("Y");
				} else {
					t.setIsTraineesVisible("N");
				}
				tevglBookChapterVisibleMapper.insert(t);
			}
		}*/
		// 即时通讯
		if (tevglTchClassroom != null) {
			// 重新刷新章节与资源
			cbRoomUtils.sendIm(tevglTchClassroom.getCtId(), "reloadresource", "other");
		}
		return R.ok("设置成功");
	}
	

    private void changeVisibleN(String pkgId, List<String> allChapterIds, List<TevglBookChapterVisible> allVisibleList, JSONArray jsonArray){
        // 等待入库的集合
        List<TevglBookChapterVisible> insertList = new ArrayList<>();
        // 等待更新集合
        List<String> chapterIdList = new ArrayList<>();
        // 等待被去重的集合
        List<String> ids = new ArrayList<>();
        // 找到需要被设为不可见的章节
        for (Object chapterId : jsonArray) {
            List<TevglBookChapterVisible> visibleList = allVisibleList.stream().filter(a -> a.getChapterId().equals(chapterId)).collect(Collectors.toList());
            if (visibleList != null && visibleList.size() > 0) {
                TevglBookChapterVisible t = visibleList.get(0);
                t.setIsTraineesVisible("N");
                chapterIdList.add(t.getChapterId());
                ids.add(t.getChapterId());
            } else {
                TevglBookChapterVisible t = new TevglBookChapterVisible();
                t.setId(Identities.uuid());
                t.setPkgId(pkgId);
                t.setChapterId(chapterId.toString());
                t.setIsTraineesVisible("N");
                insertList.add(t);
            }
        }
        if (chapterIdList != null && chapterIdList.size() > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("pkgId", pkgId);
            data.put("isTraineesVisible", "N");
            data.put("chapterIds", chapterIdList);
            tevglBookChapterVisibleMapper.updateVisibleBatch(data);
            chapterIdList.clear();
        }
        // 取差集后,更新其它章节为可见
        allChapterIds.removeAll(ids);
        for (String chapterId : allChapterIds) {
            List<TevglBookChapterVisible> visibleList = allVisibleList.stream().filter(a -> a.getChapterId().equals(chapterId)).collect(Collectors.toList());
            if (visibleList != null && visibleList.size() > 0) {
                TevglBookChapterVisible t = visibleList.get(0);
                t.setIsTraineesVisible("Y");
                chapterIdList.add(t.getChapterId());
            }  else {
                TevglBookChapterVisible t = new TevglBookChapterVisible();
                t.setId(Identities.uuid());
                t.setPkgId(pkgId);
                t.setChapterId(chapterId.toString());
                t.setIsTraineesVisible("Y");
                insertList.add(t);
            }
        }
        if (insertList != null && insertList.size() > 0) {
            tevglBookChapterVisibleMapper.insertBatch(insertList);
        }
        if (chapterIdList != null && chapterIdList.size() > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("pkgId", pkgId);
            data.put("isTraineesVisible", "Y");
            data.put("chapterIds", chapterIdList);
            tevglBookChapterVisibleMapper.updateVisibleBatch(data);
        }
    }

    /**
     * 小程序端，一次性设置，章节对学员可见
     * @param pkgId
     * @param allChapterIds
     * @param allVisibleList
     */
    private void changeVisibleY(String pkgId, List<String> allChapterIds, List<TevglBookChapterVisible> allVisibleList){
        // 等待入库的集合
        List<TevglBookChapterVisible> insertList = new ArrayList<>();
        // 等待更新集合
        List<String> chapterIdList = new ArrayList<>();
        for (String chapterId : allChapterIds) {
            List<TevglBookChapterVisible> visibleList = allVisibleList.stream().filter(a -> a.getChapterId().equals(chapterId)).collect(Collectors.toList());
            if (visibleList != null && visibleList.size() > 0) {
                TevglBookChapterVisible t = visibleList.get(0);
                t.setIsTraineesVisible("Y");
                chapterIdList.add(t.getChapterId());
            } else {
                TevglBookChapterVisible t = new TevglBookChapterVisible();
                t.setId(Identities.uuid());
                t.setPkgId(pkgId);
                t.setChapterId(chapterId.toString());
                t.setIsTraineesVisible("Y");
                insertList.add(t);
            }
        }
        if (insertList != null && insertList.size() > 0) {
            tevglBookChapterVisibleMapper.insertBatch(insertList);
        }
        if (chapterIdList != null && chapterIdList.size() > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("pkgId", pkgId);
            data.put("isTraineesVisible", "Y");
            data.put("chapterIds", chapterIdList);
            tevglBookChapterVisibleMapper.updateVisibleBatch(data);
        }
    }


	/**
	 * 根据条件查询记录
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params) {
		return tevglBookChapterMapper.selectSimpleListMap(params);
	}

	/**
	 * 左右滑动进入上下章节（左滑进入下一章节传nextChapterId、右滑传prevChapterId）
	 * 
	 * @param pkgId
	 * @param chapterId
	 * @return
	 */
	@Override
	public R slide(String pkgId, String chapterId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(chapterId)) {
			return R.error("必传参数为空");
		}
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapterInfo == null) {
			return R.error("无效的章节");
		}
		// 最终返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取到教学包主键
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		boolean hasBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_SUBJECT_RESGROUPVISIBLE);
		Map<String, Object> page = new HashMap<>();
		// 传入的章节，去获取此章节的下一章节（这里的下一章节就是返回给前端的数据）
		String targetChapterId = chapterId;
		// 根据此值查询返回章节等数据给前端
		String resultChpaterId = null;
		// 标识是否为第一条记录
		boolean isFirst = false;
		// 标识是否为最后一条记录
		boolean isLast = false;
		page.put("isFirst", isFirst);
		page.put("isLast", isLast);
		// 获取平级且满足父子关系的树形数据
		List<Map<String, Object>> list = getTree(chapterInfo.getSubjectId());
		// 处理返回当前节点的下一节点
		resultChpaterId = handlePage(list, targetChapterId, page);
		log.debug("结果：" + resultChpaterId);
		// 拿到结果后,返回资源分组和资源即可
		if (resultChpaterId != null) {
			Map<String, Object> params = new HashMap<>();
			// 3.重新获取该章节的资源分组
			params.put("ctPkgId", pkgId);
			params.put("chapterId", resultChpaterId);
			params.put("pkgId", pkgId);
			params.put("subjectId", pkgInfo.getSubjectId());
			params.put("sidx", "t1.sort_num");
			params.put("order", "asc");
			List<Map<String, Object>> pkgResGroupList = tevglPkgResgroupMapper.selectSimpleListMap2(params);
			// 处理包含的视频数量
			pkgResGroupList.stream().forEach(a -> {
				String content = (String) a.get("resContent");
				int num = getNum(content);
				a.put("num", num);
				// 如果为活动标签
				if (!StrUtils.isNull(a.get("dictCode")) && "2".equals(a.get("dictCode"))) {
					a.put("num", 0);	
				}
			});
			//TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
			if (tevglTchClassroom != null) {
				// 如果当前用户不是课堂创建者
				if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					log.debug("当前登录用户不是课程创建者");
                    log.debug("数据过滤前：" + pkgResGroupList.size());
                    log.debug("" + pkgResGroupList);
	                List<Map<String, Object>> dataList = new ArrayList<>();
	                pkgResGroupList.stream().forEach(pkgResGroup -> {
	                	if (!StrUtils.isNull(pkgResGroup.get("isTraineesVisible"))
					            && "Y".equals(pkgResGroup.get("isTraineesVisible"))) {
	                		dataList.add(pkgResGroup);
	                	}
						/*if (!"3".equals(pkgResGroup.get("dictCode"))) {
						    dataList.add(pkgResGroup);
						} else {
						    if ("3".equals(pkgResGroup.get("dictCode"))
						            && !StrUtils.isNull(pkgResGroup.get("isTraineesVisible"))
						            && "Y".equals(pkgResGroup.get("isTraineesVisible"))
						    ) {
						        dataList.add(pkgResGroup);
						    }
						}*/
	                });
	                pkgResGroupList = dataList;
	                log.debug("数据过滤后：" + pkgResGroupList.size());
	            }	
			}
			// 4.返回数据
			TevglBookChapter chapter = tevglBookChapterMapper.selectObjectById(resultChpaterId);
			result.put("chapterInfo", converToSimpleMapInfo(chapter)); // 章节数据
			result.put("pkgResGroupList", pkgResGroupList); // 章节分组
			result.put("resNum", pkgInfo.getPkgResCount()); // 教学包资源数量
			result.put("page", page);
			result.put(GlobalRoomPermission.ROOM_PERM_SUBJECT_RESGROUPVISIBLE_KEY, hasBtnPermission);
			return R.ok().put(Constant.R_DATA, result);
		}
		return R.ok().put(Constant.R_DATA, result);
	}

	/**
	 * 
	 * @param list
	 * @param targetChapterId 前端传入的当前章节
	 * @param page
	 * @return
	 */
	private String handlePage(List<Map<String, Object>> list, String targetChapterId, Map<String, Object> page) {
		if (list == null || list.size() == 0 || StrUtils.isEmpty(targetChapterId)) {
			return null;
		}
		String resultChpaterId = null;
		if (list.size() == 1) {
			resultChpaterId = list.get(0).get("chapterId").toString();
			page.put("isLast", true);
			page.put("isFirst", true);
			page.put("hasPrev", false);
			page.put("hasNext", false);
			page.put("currChapterId", resultChpaterId);
		} else {
			// 遍历判断,目标节点的下标
			int index = 0;
			for (int i = 0; i < list.size(); i++) {
				if (targetChapterId.equals(list.get(i).get("chapterId").toString())) {
					index = i;
					break;
				}
			}
			// System.out.println("当前节点属于的下标：" + index + "\n章节数：" + list.size());
			// 如果下标等于0,则表示是第一个
			if (index == 0) {
				page.put("isFirst", true);
				page.put("hasPrev", false);
				page.put("hasNext", true);
				resultChpaterId = list.get(0).get("chapterId").toString();
				page.put("currChapterId", resultChpaterId);
				page.put("nextChapterId", list.get(index + 1).get("chapterId").toString());
			}
			// 处于范围
			if (0 < index && index < list.size()) {
				page.put("isFirst", false);
				page.put("hasPrev", true);
				page.put("hasNext", true);
				resultChpaterId = list.get(index).get("chapterId").toString();
				page.put("prevChapterId", list.get(index - 1).get("chapterId").toString()); // 上一节点
				page.put("currChapterId", resultChpaterId); // 当前节点
				if (index + 1 < list.size()) {
					page.put("nextChapterId", list.get(index + 1).get("chapterId").toString()); // 下一节点
				}

			}
		}
		return resultChpaterId;
	}

	/**
	 * 获取树形数据（平级但满足父子关系的数据）
	 * 
	 * @param subjectId
	 * @return
	 */
	private List<Map<String, Object>> getTree(String subjectId) {
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", subjectId);
		params.put("sidx", "order_num");
		params.put("order", "asc");
		// 查出该书所有的章节
		List<Map<String, Object>> allList = tevglBookChapterMapper.selectSimpleListMap(params);
		// 获取构建好层次后的数据
		List<Map<String, Object>> children = buildBook(subjectId, allList, 0);
		// 再次处理
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		buildLine(children, resultList, null);
		return resultList;
	}

	/**
	 * 递归将层次机构的树形数据变为平级
	 * 
	 * @param sourceData
	 * @param resultList
	 */
	private void buildLine(List<Map<String, Object>> sourceData, List<Map<String, Object>> resultList,
			String queryName) {
		if (sourceData == null || sourceData.size() == 0) {
			return;
		}
		for (int i = 0; i < sourceData.size(); i++) {
			// 加入新集合
			if (StrUtils.isNotEmpty(queryName)) {
				String chapterName = (String) sourceData.get(i).get("chapterName");
				// 如果匹配上了
				if (chapterName.indexOf(queryName) != -1) {
					resultList.add(sourceData.get(i));
				}
			} else {
				resultList.add(sourceData.get(i));
			}
			// 如果当前节点还有子节点
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) sourceData.get(i).get("children");
			if (list != null && list.size() > 0) {
				// 递归
				buildLine(list, resultList, queryName);
				// 之后清空冗余的
				sourceData.get(i).put("children", null);
			}
		}
	}

	/**
	 * 递归构建树形数据
	 * 
	 * @param parentId
	 * @param allList
	 * @param level
	 * @return
	 */
	private List<Map<String, Object>> buildBook(String parentId, List<Map<String, Object>> allList, int level) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		// 筛选出匹配的节点
		List<Map<String, Object>> nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId))
				.collect(Collectors.toList());
		if (nodeList != null && nodeList.size() > 0) {
			// level计算当前处于第几级
			level++;
			for (int i = 0; i < nodeList.size(); i++) {
				Map<String, Object> node = nodeList.get(i);
				// 当前层级
				node.put("level", level);
				// 递归
				List<Map<String, Object>> list = buildBook(node.get("chapterId").toString(), allList, level);
				if (list != null && list.size() > 0) {
					node.put("children", list);
				} else {
					node.put("children", null);
				}
			}
		}
		return nodeList;
	}

	/**
	 * 设置章节对学员是否可见（web端专用）
	 * 
	 * @param pkgId       课堂表对应的那个pkgId
	 * @param chapterId
	 * @param loginUserId
	 * @return
	 */
	@Override
    @Caching(evict = {
            @CacheEvict(value = "room_book", key = "#ctId + '::' + 'teacher'"),
            @CacheEvict(value = "room_book", key = "#ctId + '::' + 'trainee'")
    })
	public R setChapterIsTraineesVisible(String ctId, String pkgId, String chapterId, String isTraineesVisible, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 即时通讯
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的记录，设置失败");
		}
		// 如果既不是课堂创建者，又不是课堂助教，则不允许设置
		boolean hasPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE);
		if (!hasPermission) {
			return R.error("没有权限，设置失败");
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", tevglPkgInfo.getSubjectId());
		List<TevglBookChapter> allChapterList = tevglBookChapterMapper.selectListByMap(params);
		// 1.更新自己
		params.clear();
		params.put("pkgId", pkgId);
		params.put("chapterId", chapterId);
		List<TevglBookChapterVisible> list = tevglBookChapterVisibleMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			TevglBookChapterVisible t = list.get(0);
			t.setIsTraineesVisible(isTraineesVisible);
			tevglBookChapterVisibleMapper.update(t);
		} else {
			TevglBookChapterVisible t = new TevglBookChapterVisible();
			t.setId(Identities.uuid());
			t.setPkgId(pkgId);
			t.setChapterId(chapterId);
			t.setIsTraineesVisible(isTraineesVisible);
			tevglBookChapterVisibleMapper.insert(t);
		}
		// 2.递归更新子节点
		recursion(pkgId, chapterId, isTraineesVisible, allChapterList, params);
		// 如果设置当前可见可见，那么当前节点是不可见的，那么它的父节点也是不可见的，所以需要更新其父节点
		if ("Y".equals(isTraineesVisible)) {
			List<String> parentChapterId = new ArrayList<String>();
			getParentIdList(parentChapterId, tevglPkgInfo.getSubjectId(), chapterId, allChapterList);
			if (parentChapterId.size() > 0) {
				params.clear();
				params.put("pkgId", pkgId);
				params.put("isTraineesVisible", isTraineesVisible);
				params.put("chapterIds", parentChapterId);
				tevglBookChapterVisibleMapper.updateVisibleBatch(params);
			}
		}
		if (tevglTchClassroom != null) {
			// 重新刷新章节与资源
			cbRoomUtils.sendIm(tevglTchClassroom.getCtId(), "reloadresource", "other");
		}
		return R.ok("设置成功");
	}
	
	private void getParentIdList(List<String> res, String subjectId, String chapterId, List<TevglBookChapter> allChapterList){
		if (allChapterList == null || allChapterList.size() == 0 || StrUtils.isEmpty(chapterId)) {
			return;
		}
		// 取出当前节点
		List<TevglBookChapter> nodeList = allChapterList.stream().filter(a -> a.getChapterId().equals(chapterId)).collect(Collectors.toList());
		if (nodeList == null || nodeList.size() == 0) {
			return;
		}
		TevglBookChapter node = nodeList.get(0);
		if (!node.getParentId().equals(subjectId)) {
			res.add(node.getParentId());
			getParentIdList(res, subjectId, node.getParentId(), allChapterList);
		} else {
			res.add(node.getParentId());
		}
	}

	/**
	 * 递归设置子节点的可见性
	 * 
	 * @param pkgId
	 * @param parentId
	 * @param isTraineesVisible
	 * @param allChapterList
	 * @param params
	 */
	private void recursion(String pkgId, String parentId, String isTraineesVisible,
			List<TevglBookChapter> allChapterList, Map<String, Object> params) {
		// 筛选出子节点
		List<TevglBookChapter> nodeList = allChapterList.stream().filter(a -> a.getParentId().equals(parentId))
				.collect(Collectors.toList());
		for (TevglBookChapter tevglBookChapter : nodeList) {
			String chapterId = tevglBookChapter.getChapterId();
			params.clear();
			params.put("pkgId", pkgId);
			params.put("chapterId", chapterId);
			List<TevglBookChapterVisible> list = tevglBookChapterVisibleMapper.selectListByMap(params);
			if (list != null && list.size() > 0) {
				TevglBookChapterVisible t = list.get(0);
				t.setIsTraineesVisible(isTraineesVisible);
				tevglBookChapterVisibleMapper.update(t);
			} else {
				TevglBookChapterVisible t = new TevglBookChapterVisible();
				t.setId(Identities.uuid());
				t.setPkgId(pkgId);
				t.setChapterId(chapterId);
				t.setIsTraineesVisible(isTraineesVisible);
				tevglBookChapterVisibleMapper.insert(t);
			}
			recursion(pkgId, chapterId, isTraineesVisible, allChapterList, params);
		}
	}

	/**
	 * 一键生成（任务描述、任务知识点、任务准备、任务实时）
	 * 
	 * @param pkgId
	 * @param chapterId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R create(String pkgId, String chapterId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("生成失败");
		}
		/*if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
			return R.error("已不允许生成");
		}*/
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay())
				&& !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
			return R.error("已不允许生成默认节点");
		}
		boolean hasPermissionA = pkgPermissionUtils.hasPermissionAv2(pkgId, loginUserId, chapterId);
		if (!hasPermissionA) {
			return R.error("没有权限，操作失败");
		}
		TevglBookChapter tevglBookChapter = tevglBookChapterMapper.selectObjectById(chapterId);
		if (tevglBookChapter == null) {
			return R.error("无效的章节，无法生成子节点，请重新选择");
		}
		// 限制不能超过?级
		int level = 3;
		if (isOverLevel(tevglBookChapter.getParentId(), tevglBookChapter.getSubjectId(), level)) {
			return R.error("目前最多允许增加" + level + "级章节节点");
		}
		String subjectId = tevglBookChapter.getSubjectId();
		// 查询条件
		Map<String, Object> map = new HashMap<>();
		map.put("subjectId", subjectId);
		map.put("parentId", chapterId);
		Integer maxSortNum = tevglBookChapterMapper.getMaxSortNum(map);
		List<Map<String, Object>> chapterList = tevglBookChapterMapper.selectSimpleListMap(map);
		List<String> existedNames = chapterList.stream().map(a -> a.get("chapterName").toString())
				.collect(Collectors.toList());
		List<String> list = Arrays.asList("任务描述", "任务知识点", "任务准备", "任务实施", "实验实训");
		// 用户是否有设置过
		map.clear();
		map.put("pkgId", pkgId);
		map.put("sidx", "sort_num");
		map.put("order", "asc");
		List<TevglPkgDefaultChapter> defaultChapterList = tevglPkgDefaultChapterMapper.selectListByMap(map);
		if (defaultChapterList.size() > 0) {
			list = defaultChapterList.stream().map(a -> a.getName()).distinct().collect(Collectors.toList());
		}
		for (int i = 0; i < list.size(); i++) {
			String chapterName = list.get(i);
			if (!existedNames.contains(chapterName)) {
				// 创建章节
				String newChapterId = doCreateChapter(pkgId, chapterId, subjectId, chapterName, maxSortNum + i + 1,
						loginUserId);
				// 创建分组和资源
				doCreateDefaultGroup(pkgId, subjectId, newChapterId, loginUserId);
				// 如果当前登录用户不是此教学包创建者
				//pkgUtils.createTeamDetailData(pkgId, newChapterId, loginUserId, pkgInfo.getCreateUserId());
				pkgUtils.createTeamDetailDataV2(pkgInfo, newChapterId, loginUserId);
			}
		}
		return R.ok("生成成功 （注：重复的将自动忽略）").put(Constant.R_DATA, chapterId);
	}

	/**
	 * 保存章节
	 * 
	 * @param pkgId       教学包
	 * @param parentId    章节
	 * @param subjectId   课程
	 * @param chapterName 章节名称
	 * @param sortNum     排序号
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	private String doCreateChapter(String pkgId, String parentId, String subjectId, String chapterName, Integer sortNum,
			String loginUserId) {
		TevglBookChapter t = new TevglBookChapter();
		t.setChapterId(Identities.uuid());
		t.setSubjectId(subjectId);
		t.setParentId(parentId);
		t.setChapterName(chapterName);
		t.setOrderNum(sortNum);
		t.setCreateUserId(loginUserId);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setState("Y");
		t.setIsTraineesVisible("Y");
		tevglBookChapterMapper.insert(t);
		TevglBookChapterVisible v = new TevglBookChapterVisible();
		v.setId(Identities.uuid());
		v.setPkgId(pkgId);
		v.setChapterId(t.getChapterId());
		v.setIsTraineesVisible("Y");
		tevglBookChapterVisibleMapper.insert(v);
		return t.getChapterId();
	}

	/**
	 * 查询章节列表
	 * @param params
	 * @return
	 */
	@Override
	@GetMapping("listSelectChapter")
	public R listSelectChapter(@RequestParam Map<String, Object> params) {
		if (StrUtils.isNull(params.get("subjectId"))) {
			return R.ok().put(Constant.R_DATA, null);
		}
		// subjectIds为前端传过来的参数
		List<Map<String, Object>> collect = new ArrayList<Map<String, Object>>();
		// 当前端传过来的subjectIds不为空时才查询课程所属的章节，为空时不调用
		if (params.get("subjectIds") != null && !"".equals(params.get("subjectIds"))) {
			String ids = (String) params.get("subjectIds");
			List<String> list = new ArrayList<>();
			String[] subjectIds = ids.split(",");
			for (String string : subjectIds) {
				list.add(string);
			}
			params.put("subjectIds", list);
			params.put("sidx", "t2.create_time");
			params.put("order", "desc");
		}
		Query query = new Query(params);
		List<TevglBookChapter> tevglBookChapterList = tevglBookChapterMapper.selectListByMapForChapter(query);
		collect = tevglBookChapterList.stream().map(this::converToChapterMap).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, collect);
	}

	/**
	 *  章节转换
	 * @param tevglBookChapter
	 * @return
	 */
	private Map<String, Object> converToChapterMap(TevglBookChapter tevglBookChapter) {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("chapterId", tevglBookChapter.getChapterId());
		info.put("chapterName", tevglBookChapter.getChapterName());
		return info;
	}

	/**
	 * 查询当前课堂的使用的教材的源课程
	 * 
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryTopChapterList(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的记录");
		}
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(tevglPkgInfo.getSubjectId());
		if (tevglBookSubject == null) {
			return R.error("无效的记录");
		}
		TevglBookSubject sb = tevglBookSubjectMapper.selectObjectById(tevglBookSubject.getSubjectRef());
		if (sb == null) {
			return R.error("未能获取到课程体系");
		}
		// 最总返回数据
		Map<String, Object> subjectInfo = new HashMap<>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 根据条件查询章节
		String subjectId = StrUtils.isEmpty(tevglBookSubject.getSubjectRef()) ? tevglBookSubject.getSubjectId()
				: tevglBookSubject.getSubjectRef();
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", subjectId);
		params.put("parentId", subjectId);
		params.put("sidx", "order_num");
		params.put("order", "asc");
		params.put("state", "Y");
		List<Map<String, Object>> chapterList = tevglBookChapterMapper.selectSimpleListMap(params);
		params.clear();
		params.put("subjectId", subjectId);
		params.put("state", "Y");
		List<TevglQuestionsInfo> questionList = tevglQuestionsInfoMapper.selectListByMap(params);
		// 处理章节下有多少题目
		if (chapterList != null && chapterList.size() > 0) {
			for (int i = 0; i < chapterList.size(); i++) {
				Map<String, Object> chapterInfo = chapterList.get(i);
				chapterInfo.put("type", "02");
				chapterInfo.put("questionNum", 0);
				if (questionList != null && questionList.size() > 0) {
					List<TevglQuestionsInfo> list = questionList.stream()
							.filter(qs -> StrUtils.isNotEmpty(qs.getChaptersId())
									&& qs.getChaptersId().equals(chapterInfo.get("chapterId")))
							.collect(Collectors.toList());
					if (list != null && list.size() > 0) {
						chapterInfo.put("name", chapterInfo.get("name") + " (题量" + list.size() + ")");
					}
				}
			}
		}
		subjectInfo.put("subjectId", sb.getSubjectId());
		subjectInfo.put("type", "01");
		subjectInfo.put("subjectName",
				(questionList != null && questionList.size() > 0)
						? sb.getSubjectName() + " 题量(" + questionList.size() + ")"
						: sb.getSubjectName());
		subjectInfo.put("id", sb.getSubjectId());
		subjectInfo.put("chapterId", sb.getSubjectId());
		subjectInfo.put("name",
				(questionList != null && questionList.size() > 0)
						? sb.getSubjectName() + " 题量(" + questionList.size() + ")"
						: sb.getSubjectName());
		resultList.add(subjectInfo);
		resultList.addAll(chapterList);
		return R.ok().put(Constant.R_DATA, resultList);
	}

	private Map<String, Object> getSimplesubjectInfo(TevglBookSubject subject) {
		Map<String, Object> subjectInfo = new HashMap<>();
		if (subject != null) {
			// 填充信息
			subjectInfo.put("id", subject.getSubjectId());
			subjectInfo.put("chapterId", subject.getSubjectId());
			subjectInfo.put("subjectId", subject.getSubjectId());
			subjectInfo.put("subjectName", subject.getSubjectName());
			subjectInfo.put("chapterName", subject.getSubjectName());
			subjectInfo.put("subjectAuthor", subject.getSubjectAuthor());
			subjectInfo.put("subjectDesc", subject.getSubjectDesc());
			subjectInfo.put("subjectLogo", subject.getSubjectLogo());
			subjectInfo.put("type", "subject"); // 标识为课程节点
		}
		return subjectInfo;
	}

	/**
	 * 获取层次机构的树形数据（且章节名称后面拼接此章节授权给了谁）
	 * 
	 * @author zhouyunlong加
	 * @data 2020年12月14日
	 * @param subjectId   课程id
	 * @param pkgId       教学包id
	 * @param loginUserId 登录用户id
	 * @return
	 */
	@Override
	public R getChapterTreeWithTeacherName(String subjectId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}

		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(subjectId);
		Map<String, Object> subjectInfo = getSimplesubjectInfo(subject);
		Map<String, Object> params = new HashMap<>();

		// 根据教学包id和课程id查询权限信息
		params.put("subjectId", subjectId);
		params.put("pgkId", pkgId);
		List<TevglBookpkgTeam> pkgBookpkgTeams = tevglBookpkgTeamMapper.selectListByMap(params);
		List<String> teamIds = pkgBookpkgTeams.stream().map(a -> a.getTeamId()).collect(Collectors.toList());
		// 根据teamIds查询权限详情列表信息
		params.clear();
		params.put("teamIds", teamIds);
		List<TevglBookpkgTeamDetail> teamDetails = tevglBookpkgTeamDetailMapper.selectListByMap(params);

		params.clear();
		params.put("subjectId", subjectId);
		params.put("sidx", "order_num");
		params.put("order", "asc");
		// 查出该书所有的章节
		List<Map<String, Object>> allList = tevglBookChapterMapper.selectListMapByMap(params);
		allList.stream().forEach(map -> {
			//驼峰命名
			map.put("parentId", map.get("parent_id"));
			map.put("chapterId", map.get("chapter_id"));
			map.put("chapterName", map.get("chapter_name"));

			// 根据chapter_id对权限详情表以及章节表进行过滤，找出chapter_id相同的记录
			List<TevglBookpkgTeamDetail> teamDetailList = teamDetails.stream()
					.filter(a -> a.getChapterId().equals(map.get("chapter_id"))).collect(Collectors.toList());
			// 从集合中找出user_id(teacher_id)，然后根据user_id找出对应的教师信息
			List<String> userIds = teamDetailList.stream().distinct().map(a -> a.getUserId())
					.collect(Collectors.toList());
			params.clear();
			//params.put("userIds", userIds);
			params.put("traineeIdList", userIds);
			List<TevglTchTeacher> tchTeachers = null;
			if (userIds != null && userIds.size() > 0) {
				tchTeachers = tevglTchTeacherMapper.selectListByMap(params);
				// 定义一个变量,用来标识主章节授权给了哪些教师
				String multTeacherNames = "";
				for (TevglTchTeacher tchTeacher : tchTeachers) {
					// 多个教师用逗号隔开
					multTeacherNames += tchTeacher.getTeacherName() + ",";
				}
				// 当授权的教师不为空才在章节后面拼接教师名
				if (multTeacherNames != "") {
					// 截取最后一个逗号
					multTeacherNames = multTeacherNames.substring(0, multTeacherNames.lastIndexOf(","));
					map.put("chapterName", map.get("chapter_name") + " " + multTeacherNames);
				}
			}
		});
		
		// 获取构建好层次后的数据
		List<Map<String, Object>> children = buildBook(subjectId, allList, 0);

		subjectInfo.put("children", children);
		return R.ok().put(Constant.R_DATA, subjectInfo);
	}

	/**
	 * 根据章节id查询权限详情表里的用户id是否为空，不为空则标识这个章节属于这个教师 可能有多个教师被授权了，所以返回List集合
	 * @param map
	 * @param params
	 * @return
	 */
	public List<String> getTeamDetails(Map<String, Object> map, Map<String, Object> params) {
		List<String> teacherNames = new ArrayList<>();
		List<TevglBookpkgTeamDetail> teamDetails = tevglBookpkgTeamDetailMapper.selectListByMap(params);

		for (TevglBookpkgTeamDetail teamDetail : teamDetails) {
			// 如果userId不为空标识该章节已赋予了这个教师
			if (teamDetail.getUserId() != null) {
				params.clear();
				// 已授权
				TevglTchTeacher teacher = tevglTchTeacherMapper.selectObjectById(teamDetail.getUserId());
				// 查询出来的章节和权限详情里边查询出来的章节一致，则标识该章节已授权给了这个教师
				if (map.get("chapterId").equals(teamDetail.getChapterId())) {
					teacherNames.add(teacher.getTeacherName());
					return teacherNames;
				}
			}
		}
		return teacherNames;

	}
	
	/**
	 * 设置标签对学员是否可见
	 * @param ctId
	 * @param pkgId
	 * @param resgroupId
	 * @param isTraineesVisible
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R setTraineesVisibleResgroup(String ctId, String pkgId, String resgroupId, String isTraineesVisible,
			String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId)
				|| StrUtils.isEmpty(resgroupId) || StrUtils.isEmpty(isTraineesVisible)
				|| StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		// 是否有操作权限
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_SUBJECT_RESGROUPVISIBLE);
        if (!hasOperationBtnPermission) {
            return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
        }
		TevglPkgResgroup tevglPkgResgroup = tevglPkgResgroupMapper.selectObjectById(resgroupId);
		if (tevglPkgResgroup == null) {
			return R.error("设置失败");
		}
		/*if (!"3".equals(tevglPkgResgroup.getDictCode())) {
			return R.error("目前只允许设置【课程视频】标签");
		}*/
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("pkgId", pkgId);
		params.put("resgroupId", resgroupId);
		List<TevglPkgResgroupVisible> list = tevglPkgResgroupVisibleMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			TevglPkgResgroupVisible t = new TevglPkgResgroupVisible();
			t.setRvId(Identities.uuid());
			t.setCtId(ctId);
			t.setPkgId(pkgId);
			t.setResgroupId(resgroupId);
			t.setIsTraineesVisible(isTraineesVisible);
			tevglPkgResgroupVisibleMapper.insert(t);
			return R.ok("设置成功");
		}
		TevglPkgResgroupVisible tevglPkgResgroupVisible = list.get(0);
		tevglPkgResgroupVisible.setIsTraineesVisible(isTraineesVisible);
		tevglPkgResgroupVisibleMapper.update(tevglPkgResgroupVisible);
		return R.ok("设置成功");
	}

	/**
	 * 批量设置 课程视频等标签对学员是否可见
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R setTraineesVisibleResgroupBatch(JSONObject jsonObject, String loginUserId) {
		String ctId = jsonObject.getString("ctId");
		String pkgId = jsonObject.getString("pkgId");
		//String isTraineesVisible = jsonObject.getString("isTraineesVisible");
		// 等待设置为可见的分组标签
		JSONArray jsonArray = jsonObject.getJSONArray("resgroupIdList");
		// 等待设置为不可见
		JSONArray unResgroupIdList = jsonObject.getJSONArray("unResgroupIdList");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId)
				//	|| StrUtils.isEmpty(isTraineesVisible)
				|| StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		if (!cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_SUBJECT_RESGROUPVISIBLE)) {
			return R.error("没有权限，设置失败");
		}
		Map<String, Object> params = new HashMap<>();
		// 设置可见
		for (int i = 0; i < jsonArray.size(); i++) {
			String resgroupId = jsonArray.getString(i);
			params.put("ctId", ctId);
			params.put("pkgId", pkgId);
			params.put("resgroupId", resgroupId);
			List<TevglPkgResgroupVisible> list = tevglPkgResgroupVisibleMapper.selectListByMap(params);
			if (list == null || list.size() == 0) {
				TevglPkgResgroupVisible t = new TevglPkgResgroupVisible();
				t.setRvId(Identities.uuid());
				t.setCtId(ctId);
				t.setPkgId(pkgId);
				t.setResgroupId(resgroupId);
				t.setIsTraineesVisible("Y");
				tevglPkgResgroupVisibleMapper.insert(t);
			} else {
				TevglPkgResgroupVisible tevglPkgResgroupVisible = list.get(0);
				tevglPkgResgroupVisible.setIsTraineesVisible("Y");
				tevglPkgResgroupVisibleMapper.update(tevglPkgResgroupVisible);
			}
		}
		// 设置不可见
		for (int i = 0; i < unResgroupIdList.size(); i++) {
			String resgroupId = unResgroupIdList.getString(i);
			params.put("ctId", ctId);
			params.put("pkgId", pkgId);
			params.put("resgroupId", resgroupId);
			List<TevglPkgResgroupVisible> list = tevglPkgResgroupVisibleMapper.selectListByMap(params);
			if (list == null || list.size() == 0) {
				TevglPkgResgroupVisible t = new TevglPkgResgroupVisible();
				t.setRvId(Identities.uuid());
				t.setCtId(ctId);
				t.setPkgId(pkgId);
				t.setResgroupId(resgroupId);
				t.setIsTraineesVisible("N");
				tevglPkgResgroupVisibleMapper.insert(t);
			} else {
				TevglPkgResgroupVisible tevglPkgResgroupVisible = list.get(0);
				tevglPkgResgroupVisible.setIsTraineesVisible("N");
				tevglPkgResgroupVisibleMapper.update(tevglPkgResgroupVisible);
			}
		}
		return R.ok("设置成功");
	}

	/**
	 * 章节树（可见性）
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R getChapterTreeVisibleForWeb(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("参数错误");
		}
		String subjectId = tevglPkgInfo.getSubjectId();
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (tevglBookSubject == null) {
			return R.error("数据异常");
		}
		Map<String, Object> sb = new HashMap<String, Object>();
		sb.put("subjectId", tevglBookSubject.getSubjectId());
		sb.put("chapterId", tevglBookSubject.getSubjectId());
		sb.put("chapterName", tevglBookSubject.getSubjectName());
		sb.put("parentId", "-1");
		List<Map<String, Object>> treeData = tevglBookSubjectServiceImpl.getTree(subjectId, pkgId);
		treeData.stream().forEach(node -> {
			if (StrUtils.notNull(node.get("isTraineesVisible")) && "Y".equals(node.get("isTraineesVisible"))) {
				sb.put("checked", true);
			}
		});
		sb.put("children", treeData);
		return R.ok().put(Constant.R_DATA, sb);
	}

	/**
	 * 批量设置章节对学员是否可见
	 * @param vo
	 * @param loginUserId
	 * @return
	 */
	@Override
    @Caching(evict = {
            @CacheEvict(value = "room_book", key = "#vo.ctId + '::' + 'teacher'"),
            @CacheEvict(value = "room_book", key = "#vo.ctId + '::' + 'trainee'")
    })
	public R setTraineesVisibleBatchForWeb(SaveChapterVisibleVo vo, String loginUserId) {
        String pkgId = vo.getPkgId();
        if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
            return R.error("必传参数为空");
        }
        TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
        if (tevglTchClassroom == null) {
            return R.error("无效的课堂");
        }
        boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE);
        if (!hasOperationBtnPermission) {
            return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
        }
        List<String> chapterIds = vo.getChapterIds();
        // 1.先清空
        clear(pkgId);
        // 2.找到所有章节
        List<String> chapterIdList = getChapterIdList(pkgId);
        // 3.去重，将所有章节与用户选中的章节去重
        List<String> userSelectedChpaterId = new ArrayList<String>();
        for (Object object : chapterIds) {
            if (!userSelectedChpaterId.contains(object)) {
                userSelectedChpaterId.add(object.toString());
            }
        }
        log.debug("去重前：" + chapterIdList.size());
        // 4.将去重后的，设置为不可见
        chapterIdList.removeAll(userSelectedChpaterId);
        log.debug("去重后：" + chapterIdList.size());
        changeN(chapterIdList, pkgId);
        log.debug("用户选中的节点：" + userSelectedChpaterId);
        // 再将选中的节点，重新设置为可见
        changeY(userSelectedChpaterId, pkgId);
        // 重新刷新章节与资源
		cbRoomUtils.sendIm(tevglTchClassroom.getCtId(), "reloadresource", "other");
        return R.ok("设置成功").put("insertList", null);
    }

    /**
     * 设为可见
     * @param userSelectedChpaterId
     * @param pkgId
     */
    private void changeY(List<String> userSelectedChpaterId, String pkgId) {
        List<TevglBookChapterVisible> insertList = new ArrayList<>();
        for (String chapterId : userSelectedChpaterId) {
            TevglBookChapterVisible t = new TevglBookChapterVisible();
            t.setId(Identities.uuid());
            t.setPkgId(pkgId);
            t.setChapterId(chapterId.toString());
            t.setIsTraineesVisible("Y");
            insertList.add(t);
        }
        if (insertList.size() > 0) {
            tevglBookChapterVisibleMapper.insertBatch(insertList);
        }
    }

    /**
     * 设为不可见
     * @param chapterIdList 将章节与用户选中的章节去重后的
     * @param pkgId
     */
    private void changeN(List<String> chapterIdList, String pkgId) {
        List<TevglBookChapterVisible> insertList = new ArrayList<>();
        for (Object chapterId : chapterIdList) {
            TevglBookChapterVisible t = new TevglBookChapterVisible();
            t.setId(Identities.uuid());
            t.setPkgId(pkgId);
            t.setChapterId(chapterId.toString());
            t.setIsTraineesVisible("N");
            insertList.add(t);
        }
        if (insertList.size() > 0) {
            tevglBookChapterVisibleMapper.insertBatch(insertList);
        }
    }

    private List<String> getChapterIdList(String pkgId){
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (tevglPkgInfo == null) {
            return new ArrayList<String>();
        }
        // 找到此课堂正在使用的教材
        String subjectId = tevglPkgInfo.getSubjectId();
        return tevglBookChapterMapper.selectChapterIdList(subjectId);
    }

    private void clear(String pkgId) {
        if (StrUtils.isEmpty(pkgId)) {
            return;
        }
        // 先清空
        Map<String, Object> map = new HashMap<>();
        map.put("pkgId", pkgId);
        List<TevglBookChapterVisible> list = tevglBookChapterVisibleMapper.selectListByMap(map);
        if (list != null && list.size() > 0) {
            List<String> ids = list.stream().map(a -> a.getId()).distinct().collect(Collectors.toList());
            tevglBookChapterVisibleMapper.deleteBatch(ids.stream().toArray(String[]::new));
        }
    }

    /**
     * 追加节点
     * @param tevglBookChapter
     * @return
     */
	@Override
	public R appendPeerNodes(TevglBookChapter tevglBookChapter) {
		// 合法性校验
		R r = checkIsPassForAppendPeerNodes(tevglBookChapter);
		if (!r.get("code").equals(0)) {
			return r;
		}
		String pkgId = tevglBookChapter.getPkgId();
		String subjectId = tevglBookChapter.getSubjectId();
		String parentId = tevglBookChapter.getParentId();
		String loginUserId = tevglBookChapter.getCreateUserId();
		// 拿取数据
        @SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) r.get(Constant.R_DATA);
		TevglPkgInfo tevglPkgInfo = (TevglPkgInfo) data.get("tevglPkgInfo");
		TevglBookChapter previousNode = (TevglBookChapter) data.get("tevglBookChapter");
		// 权限校验
        R r2 = checkPermission(tevglBookChapter, loginUserId, tevglPkgInfo);
        if ((Integer)r2.get("code") != 0) {
            return r2;
        }
		// 填充信息
		tevglBookChapter.setChapterId(Identities.uuid());
        tevglBookChapter.setIsTraineesVisible(StrUtils.isEmpty(tevglBookChapter.getIsTraineesVisible()) ? "Y" : tevglBookChapter.getIsTraineesVisible());
        tevglBookChapter.setCreateUserId(loginUserId);
        tevglBookChapter.setCreateTime(DateUtils.getNowTimeStamp());
        tevglBookChapter.setState("Y"); // 状态(Y有效N无效)
        ValidatorUtils.check(tevglBookChapter);
        // 限制不能超过?级
        int level = 3;
        if (isOverLevel(tevglBookChapter.getParentId(), tevglBookChapter.getSubjectId(), level)) {
            return R.error("目前最多允许增加"+level+"级章节节点");
        }
        // 将同级下后面的节点排序号都+1
        Map<String, Object> map = new HashMap<>();
        map.put("subjectId", subjectId);
        map.put("parentId", parentId);
        List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMap(map);
        List<TevglBookChapter> collect = list.stream().filter(a -> a.getOrderNum() > previousNode.getOrderNum()).collect(Collectors.toList());
        List<TevglBookChapter> updateList = new ArrayList<>();
        collect.stream().forEach(item -> {
        	item.setOrderNum(item.getOrderNum() + 1);
        	updateList.add(item);
        });
        if (updateList.size() > 0) {
        	tevglBookChapterMapper.updateBatchByCaseWhen(updateList);
        }
        // 当前新追节点的节点排序号+1
        tevglBookChapter.setOrderNum(previousNode.getOrderNum() + 1);
        // 入库
        tevglBookChapterMapper.insert(tevglBookChapter);
        // 如果当前登录用户不是此教学包创建者
        //pkgUtils.createTeamDetailData(pkgId, tevglBookChapter.getChapterId(), loginUserId, tevglPkgInfo.getCreateUserId());
        pkgUtils.createTeamDetailDataV2(tevglPkgInfo, tevglBookChapter.getChapterId(), loginUserId);
        // 默认生成[课程内容]分组
        doCreateDefaultGroup(pkgId, tevglPkgInfo.getSubjectId(), tevglBookChapter.getChapterId(), loginUserId);
        return R.ok("追加成功").put("chapterId", tevglBookChapter.getChapterId());
	}
	
	/**
	 * 追加节点时的校验
	 * @param tevglBookChapter
	 * @return
	 */
	private R checkIsPassForAppendPeerNodes(TevglBookChapter tevglBookChapter) {
		String loginUserId = tevglBookChapter.getCreateUserId();
		String pkgId = tevglBookChapter.getPkgId();
		String previousChapterId = tevglBookChapter.getPreviousChapterId();
		// 合法性校验
        String subjectId = tevglBookChapter.getSubjectId();
        String parentId = tevglBookChapter.getParentId();
        String chapterName = tevglBookChapter.getChapterName();
        if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(chapterName)
                || StrUtils.isEmpty(parentId) || StrUtils.isEmpty(loginUserId)
                || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(previousChapterId)) {
            return R.error("必传参数为空");
        }
        // 获取到教学包主键
        TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (pkgInfo == null) {
            return R.error("无效的记录");
        }
        if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay()) && !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
            return R.error("已不允许新增");
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
        TevglBookChapter previousNode = tevglBookChapterMapper.selectObjectById(previousChapterId);
        if (previousNode == null) {
        	return R.error("无法在该节点下追加节点");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("tevglPkgInfo", pkgInfo);
        data.put("tevglBookChapter", previousNode);
        return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 拖拽节点
	 * @param pkgId
	 * @param soureNodeId 当前被选中托拽的节点
	 * @param targetId 必传参数，情况：①被拖拽至该节点下，成为其下子节点。②被拖拽至某节点后。详见ztree api <a>http://www.treejs.cn/v3/demo.php#_302</a>
	 * @param moveType 指定移动到目标节点的相对位置 "inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
	 * @param traineeId 当前登录用户
	 * @return
	 */
	@Override
	public R dragNode(String pkgId, String subjectId, String soureNodeId, String targetId, String moveType, String traineeId, String list) {
		// 必传参数不能为空
        if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(soureNodeId) || StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(moveType) || StrUtils.isEmpty(traineeId)) {
            return R.error(BizCodeEnume.PARAM_MISSING.getCode(), BizCodeEnume.PARAM_MISSING.getMsg());
        }
        // 是否移动成为根节点的子节点
        boolean flag = targetId.equals(subjectId);
        // 权限校验
        TevglBookChapter currentNode = tevglBookChapterMapper.selectObjectById(soureNodeId);
        TevglBookChapter targetNode = tevglBookChapterMapper.selectObjectById(targetId);
        if (currentNode == null || (targetNode == null && !targetId.equals(subjectId))) {
            return R.error(BizCodeEnume.PARAM_INVALID.getCode(), BizCodeEnume.PARAM_INVALID.getMsg());
        }
        List<String> needCheckIds = subjectId.equals(targetId) ? Arrays.asList(soureNodeId) : Arrays.asList(soureNodeId, targetId);
        boolean hasPermission = tevglBookpkgTeamService.checkNodePermission(pkgId, needCheckIds, traineeId);
        if (!hasPermission) {
            return R.error(BizCodeEnume.ILLEGAL_OPERATION.getCode(), BizCodeEnume.ILLEGAL_OPERATION.getMsg());
        }
        // 层级校验
        Integer level = Integer.valueOf(CommonEnum.CHAPTER_LEVEL_LIMIT.getCode());
        List<BookTreeVo> allChapterList = tevglBookChapterMapper.findAllChapterList(subjectId);
        TevglBookChapter t = new TevglBookChapter();
        //TevglBookChapter o = new TevglBookChapter();
        // 如果是托拽成为子节点，需判断是否会超过3级
        if ("inner".equals(moveType)) {
            Map<String, Integer> map = new HashMap<>();
            map.put("num", 1);
            handleLevelNum(soureNodeId, allChapterList, map);
            if (!flag) {
                handleTop(targetNode.getChapterId(), allChapterList, map);
                boolean overLevel = map.get("num") + map.get("level") > level;
                if (overLevel) {
                    return R.error("节点层级将超过3级，不允许托拽到这里！");
                }
            }
            // 如果原本就是子节点了
            if (currentNode.getParentId().equals(targetId)) {
                return R.error("托拽成功~");
            }
            // 如果当前节点（含子节点）的层数 + 目标节点的层级 > level 则不允许托拽
            t.setChapterId(soureNodeId);
            t.setParentId(targetId);
            tevglBookChapterMapper.update(t);
			/*List<String> ids = tevglBookChapterMapper.findChapterIdListByParentId(targetId);
			if (ids != null && ids.size() > 0) {
			    for (int i = 0; i < ids.size(); i++) {
			        o.setChapterId(ids.get(i));
			        o.setOrderNum(i + 1);
			        tevglBookChapterMapper.update(o);
			    }
			}*/
        }
        if ("prev".equals(moveType)) {
            t.setChapterId(soureNodeId);
            t.setParentId(targetNode.getParentId());
            tevglBookChapterMapper.update(t);
        }
        if ("next".equals(moveType)) {
            t.setChapterId(soureNodeId);
            t.setParentId(targetNode.getParentId());
            tevglBookChapterMapper.update(t);
        }
        // 处理排序号
        if (StrUtils.isNotEmpty(list)) {
        	List<TevglBookChapter> dataList = new ArrayList<>();
            JSONArray jsonArray = JSONObject.parseArray(list);
            if (jsonArray != null && jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TevglBookChapter ob = new TevglBookChapter();
                    ob.setChapterId(jsonObject.getString("chapterId"));
                    ob.setOrderNum(i + 1);
                    //tevglBookChapterMapper.update(o);
                    dataList.add(ob);
                }
                tevglBookChapterMapper.updateBatchByCaseWhen(dataList);
            }
        }
        return R.ok("拖拽成功");
	}
	
	public void handleTop(String chapterId, List<BookTreeVo> allChapterList, Map<String, Integer> map) {
        List<BookTreeVo> bookTreeVos = allChapterList.stream().filter(a -> a.getId().equals(chapterId)).collect(Collectors.toList());
        if (bookTreeVos != null && bookTreeVos.size() > 0) {
            Integer level = map.get("level") == null ? 0 : map.get("level");
            map.put("level", ++level);
            BookTreeVo parentNode = bookTreeVos.get(0);
            handleTop(parentNode.getParentId(), allChapterList, map);
        }
    }

    private void handleLevelNum(String chapterId, List<BookTreeVo> allChapterList, Map<String, Integer> map) {
        if (StrUtils.isEmpty(chapterId) || allChapterList == null || allChapterList.size() == 0) {
            return;
        }
        List<BookTreeVo> nodeList = allChapterList.stream().filter(a -> a.getParentId().equals(chapterId)).collect(Collectors.toList());
        if (nodeList != null && nodeList.size() > 0) {
            map.put("num", map.get("num") + 1);
            nodeList.stream().forEach(item -> {
                handleLevelNum(item.getId(), allChapterList, map);
            });
        }
    }

	/**
	 * 升级节点（例如升级2.1.1三级，则变成2.1二级）
	 * @param chapterId
	 * @param traineeId 当前登录用户
	 * @return
	 */
	@Override
	public R upgradeNodes(String chapterId, String traineeId) {
		if (StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(traineeId)) {
            return R.error("必传参数为空");
        }
        TevglBookChapter tevglBookChapter = tevglBookChapterMapper.selectObjectById(chapterId);
        if (tevglBookChapter == null) {
            return R.error("升级失败");
        }
        // 找到当前节点的所属父级
        String parentId = tevglBookChapter.getParentId();
        // 如果当前节点已经是课程下的一级节点（顶级节点），则不能升级
        if (parentId.equals(tevglBookChapter.getSubjectId())) {
            return R.error("当前节点已经处于1级了，不能再升了");
        }
        // 找到节点的父级的父级
        TevglBookChapter parentParentNode = tevglBookChapterMapper.selectObjectById(parentId);
        if (parentParentNode == null || StrUtils.isEmpty(parentParentNode.getParentId())) {
            return R.error("升级失败");
        }
        // 重置
        String finalParentId = parentParentNode.getParentId();
        // 找到其同级节点们
        Map<String, Object> map = new HashMap<>();
        map.put("subjectId", parentParentNode.getSubjectId());
        map.put("parentId", parentParentNode.getParentId());
        List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMap(map);
        List<TevglBookChapter> collect = list.stream().filter(a -> a.getOrderNum() > parentParentNode.getOrderNum()).collect(Collectors.toList());
        collect.stream().forEach(item -> {
            item.setOrderNum(item.getOrderNum() + 1);
            tevglBookChapterMapper.update(item);
        });
        TevglBookChapter t = new TevglBookChapter();
        t.setChapterId(chapterId);
        t.setParentId(finalParentId);
        t.setOrderNum(parentParentNode.getOrderNum() + 1);
        tevglBookChapterMapper.update(t);
        return R.ok("升级层级成功");
	}

	/**
	 * 降级
	 * @param chapterId
	 * @param traineeId
	 * @return
	 */
	@Override
	public R demotionNodes(String chapterId, String traineeId) {
		// TODO Auto-generated method stub
		return null;
	}
	

    /**
     * 老师批量新增教材章节目录
     * @param vo
     * @param traineeId
     * @return
     */
    @Override
    @Transactional
    @NoRepeatSubmit(value = 1000)
    public R batchSaveChapter(SaveChapterVo vo, String traineeId) {
        ValidatorUtils.check(vo);
        if (vo.getChapterNameList() != null && !vo.getChapterNameList().isEmpty()) {
            vo.getChapterNameList().forEach(name -> {
                if (StrUtils.isNotEmpty(name) && name.trim().length() > 30) {
                    throw new BudaosException("名称长度不能超过30");
                }
            });
        }
        // 获取到教学包主键
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(vo.getPkgId());
        if (tevglPkgInfo == null) {
            return R.error(BizCodeEnume.PARAM_INVALID.getCode(), BizCodeEnume.PARAM_INVALID.getMsg());
        }
        if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && !"3".equals(tevglPkgInfo.getDisplay())
                && !tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
            return R.error("已不允许新增");
        }
        // 权限校验
        R r2 = pkgPermissionUtils.hasPermissionChapterV2(tevglPkgInfo, traineeId, vo.getParentId()); // checkPermission(tevglBookChapter, traineeId, pkgInfo);
        if ((Integer) r2.get("code") != 0) {
            return r2;
        }
        // 校验是否超过三级
        TevglBookChapter node1 = tevglBookChapterMapper.selectObjectById(vo.getParentId());
        if (node1 != null && !node1.getParentId().equals(tevglPkgInfo.getSubjectId())) {
            TevglBookChapter node2 = tevglBookChapterMapper.selectObjectById(node1.getParentId());
            if (node2 != null && !node2.getParentId().equals(tevglPkgInfo.getSubjectId())) {
                TevglBookChapter node3 = tevglBookChapterMapper.selectObjectById(node2.getParentId());
                if (node3 != null) {
                    throw new BudaosException("不能超过3级");
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("subjectId", tevglPkgInfo.getSubjectId());
        map.put("parentId", vo.getParentId());
        Integer maxSortNum = tevglBookChapterMapper.getMaxSortNum(map);
        // 数据批量入库
        List<TevglBookChapter> insertList = new ArrayList<>();
        Stream.iterate(0, i -> i+1).limit(vo.getChapterNameList().size()).forEach(i -> {
            String chapterName = vo.getChapterNameList().get(i);
            if (StrUtils.isNotEmpty(chapterName) && StrUtils.isNotEmpty(chapterName.trim())) {
                TevglBookChapter t = new TevglBookChapter();
                t.setChapterId(Identities.uuid());
                t.setSubjectId(tevglPkgInfo.getSubjectId());
                t.setParentId(vo.getParentId());
                t.setChapterName(chapterName);
                t.setState(CommonEnum.STATE_YES.getCode());
                t.setChapterContent("");
                t.setOrderNum(maxSortNum + i);
                t.setCreateUserId(traineeId);
                t.setCreateTime(DateUtils.getNowTimeStamp());
                insertList.add(t);
            }
        });
        if (!insertList.isEmpty()) {
            // 批量入库
            tevglBookChapterMapper.insertBatch(insertList);
            // 如果当前登录用户不是此教学包创建者
            List<String> stringList = insertList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
            pkgUtils.createTeamDetailDataV2(tevglPkgInfo, stringList, traineeId);
            // 默认生成[课程内容]分组
            pkgUtils.doCreateDefaultGroup(tevglPkgInfo, stringList, traineeId);
        }
        return R.ok("保存成功");
    }

}
