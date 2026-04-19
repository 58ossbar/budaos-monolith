package com.budaos.modules.evgl.pkg.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.common.enums.CommonEnum;
import com.budaos.modules.common.enums.SiteSysMsgTypeEnum;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstormingTraineeAnswer;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireTraineeAnswer;
import com.budaos.modules.evgl.activity.persistence.TevglActivityBrainstormingTraineeAnswerMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityVoteQuestionnaireTraineeAnswerMapper;
import com.budaos.modules.evgl.activity.service.TevglActivityAnswerDiscussServiceImpl;
import com.budaos.modules.evgl.activity.service.TevglActivityBrainstormingServiceImpl;
import com.budaos.modules.evgl.activity.service.TevglActivityTestPaperServiceImple;
import com.budaos.modules.evgl.activity.service.TevglActivityVoteQuestionnaireServiceImpl;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookChapterVisible;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterVisibleMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubperiodMapper;
import com.budaos.modules.evgl.book.service.TevglBookSubjectServiceImpl;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.modules.evgl.pkg.api.TevglPkgInfoService;
import com.budaos.modules.evgl.pkg.domain.*;
import com.budaos.modules.evgl.pkg.persistence.*;
import com.budaos.modules.evgl.pkg.vo.TevglPkgInfoMgrVo;
import com.budaos.modules.evgl.question.domain.TepExamineDynamicPaper;
import com.budaos.modules.evgl.question.persistence.TepExamineDynamicPaperMapper;
import com.budaos.modules.evgl.site.domain.TevglSiteSysMsg;
import com.budaos.modules.evgl.site.persistence.TevglSiteSysMsgMapper;
import com.budaos.modules.evgl.site.query.TevglSiteSysMsgQuery;
import com.budaos.modules.evgl.site.vo.TevglSiteSysMsgVo;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * Title: 教学包信息
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
public class TevglPkgInfoServiceImpl implements TevglPkgInfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgInfoServiceImpl.class);
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private TevglBookSubjectServiceImpl tevglBookSubjectServiceImpl;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireServiceImpl tevglActivityVoteQuestionnaireServiceImpl;
	@Autowired
	private TevglActivityBrainstormingServiceImpl tevglActivityBrainstormingServiceImpl;
	@Autowired
	private TevglActivityAnswerDiscussServiceImpl tevglActivityAnswerDiscussServiceImpl;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	@Autowired
	private CloudPanUtils cloudPanUtils;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	@Autowired
	private TevglBookChapterVisibleMapper tevglBookChapterVisibleMapper;
	@Autowired
	private TevglActivityTestPaperServiceImple tevglActivityTestPaperServiceImpl;
	@Autowired
	private RoleUtils roleUtils;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerMapper tevglActivityVoteQuestionnaireTraineeAnswerMapper;
	@Autowired
	private TevglActivityBrainstormingTraineeAnswerMapper tevglActivityBrainstormingTraineeAnswerMapper;
	@Autowired
	private TepExamineDynamicPaperMapper tepExamineDynamicPaperMapper;
	@Autowired
    private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	@Autowired
	private TevglSiteSysMsgMapper tevglSiteSysMsgMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * 
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Bean>)")

	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TevglPkgInfo> tevglPkgInfoList = tevglPkgInfoMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgInfoList, "createUserId", "updateUserId");
		convertUtil.convertOrgId(tevglPkgInfoList, "orgId");
		convertUtil.convertDict(tevglPkgInfoList, "pkgLimit", "pkgLimit"); // 使用限制(来源字典：授权，购买，免费)
		PageUtils pageUtil = new PageUtils(tevglPkgInfoList, query.getPage(), query.getLimit());
		// 取部分属性
		List<Map<String, Object>> list = tevglPkgInfoList.stream().map(this::converToSimpleMapInfo)
				.collect(Collectors.toList());
		pageUtil.setList(list);
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * 
	 * @param params
	 * @return R
	 */
	@SysLog(value = "查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tevglPkgInfoList = tevglPkgInfoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgInfoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglPkgInfoList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 新增教学包
	 * 
	 * @param tevglPkgInfo
	 * @throws BudaosException
	 */
	@SysLog(value = "新增")
	public R save(TevglPkgInfo tevglPkgInfo) throws BudaosException {
		return R.ok();
	}

	/**
	 * 修改教学包
	 * 
	 * @param tevglPkgInfo
	 * @throws BudaosException
	 */
	@SysLog(value = "修改")
	public R update( TevglPkgInfo tevglPkgInfo) throws BudaosException {
		tevglPkgInfo.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglPkgInfoMapper.update(tevglPkgInfo);
		// 如果上传了附件
        String attachId = tevglPkgInfo.getAttachId();
        if (StrUtils.isNotEmpty(attachId) && StrUtils.isNotEmpty(tevglPkgInfo.getPkgId())) {
            tsysAttachService.updateAttach(attachId, tevglPkgInfo.getPkgId(), "0", "12");
        }
		return R.ok();
	}

	/**
	 * 单条删除
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "单条删除")
	public R delete( String id) throws BudaosException {
		tevglPkgInfoMapper.delete(id);
		return R.ok();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value = "批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglPkgInfoMapper.deleteBatch(ids);
		return R.ok();
	}

	@Override
	public TevglPkgInfo selectObjectById(Object id) {
		return tevglPkgInfoMapper.selectObjectById(id);
	}

	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSimpleList(Map<String, Object> params) {
		List<Map<String, Object>> list = tevglPkgInfoMapper.selectSimpleList(params);
        list.stream().forEach(item -> {
            if (!StrUtils.isNull(item.get("pkgVersion"))) {
                item.put("pkgName", item.get("pkgName") + "  ["+item.get("pkgVersion")+"]");
            }
        });
        return list;
	}

	
	/**
	 * 查看教学包基本信息
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglPkgInfoMapper.selectObjectById(id));
	}

	/**
	 * 保存教学包信息
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	@Override
	//@DemoAccountLimit
	public R saveInfo( TevglPkgInfo tevglPkgInfo, String loginUserId) throws BudaosException {
		// 如果当前登录用户不是教师
		if (!roleUtils.checkIsTeacher(loginUserId)) {
			return R.error("还未开通新建【教学包】授权，请联系管理员！");
		}
		// 合法性校验
		R r = checkIsPass(tevglPkgInfo, loginUserId);
		if ((Integer) r.get("code") != 0) {
			return r;
		}
		// 查询条件
		Map<String, Object> ps = new HashMap<String, Object>();
		// 用户选择的职业路径
		String majorId = tevglPkgInfo.getMajorId();
		// 用户选择的课程体系
		String inputSubjectId = tevglPkgInfo.getSubjectId();
		TevglPkgInfo onlyPkg = pkgUtils.getOnlyPkgByMajorIdSubjectId(majorId, inputSubjectId, loginUserId, ps);
		if (onlyPkg != null) {
			return R.error("同职业路径同课程体系下你已有教学包，无需重复添加。");
		}
		// 实例化
		String uuid = Identities.uuid();
		tevglPkgInfo.setPkgId(uuid);
		tevglPkgInfo.setCreateUserId(loginUserId);
		tevglPkgInfo.setCreateTime(DateUtils.getNowTimeStamp());
		tevglPkgInfo.setShowIndex("N");
		tevglPkgInfo.setState("Y");
		tevglPkgInfo.setPkgRefCount(0);
		tevglPkgInfo.setPkgActCount(0);
		tevglPkgInfo.setPkgResCount(0);
		tevglPkgInfo.setViewNum(0);
		// 发布状态(Y/N)
		tevglPkgInfo.setReleaseStatus("N");
		// 排序号处理
		ps.clear();
		ps.put("majorId", majorId);
		ps.put("subjectId", inputSubjectId);
		ps.put("createUserId", loginUserId);
		int sortNum = tevglPkgInfoMapper.getMaxSortNum(ps);
		tevglPkgInfo.setSortNum(sortNum);
		// 按照逻辑,选完课程之后,保存教学包时生成复制一份源课程的相关数据(课程和章节)
		// 先生成课程
		String sbId = createSubjectInfo(tevglPkgInfo.getSubjectId(), loginUserId);
		tevglPkgInfo.setSubjectId(sbId);
		log.debug("教学包入库");
		tevglPkgInfoMapper.insert(tevglPkgInfo);
		try {
			// 再复制章节
			log.debug("执行复制章节");
			tevglBookSubjectServiceImpl.copy(inputSubjectId, sbId, loginUserId, uuid);
		} catch (Exception e) {
			log.debug("章节复制失败");
			e.printStackTrace();
		}
		// 如果上传了附件
		String attachId = tevglPkgInfo.getAttachId();
		if (StrUtils.isNotEmpty(attachId)) {
			tsysAttachService.updateAttach(attachId, uuid, "1", "12");
		}
		return R.ok("保存成功").put(Constant.R_DATA, tevglPkgInfo.getPkgId());
	}
	
	/**
	 * 修改教学包信息
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R updateInfo(TevglPkgInfo tevglPkgInfo, String loginUserId) {
		// 合法性校验
		R r = checkIsPass(tevglPkgInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 原有数据
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(tevglPkgInfo.getPkgId());
		if (pkgInfo == null) {
			return R.error("无效的教学包");
		}
		if (!"Y".equals(pkgInfo.getState())) {
			return R.error("无效的教学包");
		}
		// 没有权限，无法操作教学包
		boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(pkgInfo, loginUserId);
		if (!hasOperatingAuthorization) {
			return R.error("没有权限，无法操作教学包");
		}
		if ("Y".equals(pkgInfo.getReleaseStatus())) {
			if (!tevglPkgInfo.getPkgVersion().equals(pkgInfo.getPkgVersion())) {
				return R.error("已发布状态的教学包不允许修改版本号");
			}
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgName", tevglPkgInfo.getPkgName());
		params.put("pkgVersion", tevglPkgInfo.getPkgVersion());
		params.put("createUserId", loginUserId);
		List<Map<String,Object>> list = tevglPkgInfoMapper.selectSimpleList(params);
		if (list.stream().anyMatch(a -> a.get("pkgName").equals(tevglPkgInfo.getPkgName()) && a.get("pkgVersion").equals(tevglPkgInfo.getPkgVersion()) && !a.get("pkgId").equals(tevglPkgInfo.getPkgId()))) {
			return R.error("请调整[版本号]或者修改[教学包名称]（注：由于已经存在同名同版本的）");
		}
		TevglBookSubject sb = new TevglBookSubject();
		sb.setSubjectId(pkgInfo.getSubjectId());
		sb.setSubjectName(tevglPkgInfo.getPkgName());
		tevglBookSubjectMapper.update(sb);
		// 不允许修改课程
		tevglPkgInfo.setSubjectId(pkgInfo.getSubjectId());
		// 不允许修改发布方
		//tevglPkgInfo.setDeployMainType(pkgInfo.getDeployMainType());
		// 不允许修改使用限制
		//tevglPkgInfo.setPkgLimit(pkgInfo.getPkgLimit());
		tevglPkgInfo.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglPkgInfo.setUpdateUserId(loginUserId);
		tevglPkgInfoMapper.update(tevglPkgInfo);
		// 如果上传了附件
		String attachId = tevglPkgInfo.getAttachId();
		if (StrUtils.isNotEmpty(attachId)) {
			tsysAttachService.updateAttach(attachId, pkgInfo.getPkgId(), "0", "12");
		}
		return R.ok("保存成功").put(Constant.R_DATA, tevglPkgInfo.getPkgId());
	}

	/**
	 * 生成课程
	 * @param subjectId
	 * @param loginUserId
	 * @return
	 */
	private String createSubjectInfo(String subjectId, String loginUserId) throws BudaosException {
		TevglBookSubject subjectInfo = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (subjectInfo == null) {
			return null;
		}
		String uuid = Identities.uuid();
		TevglBookSubject t = new TevglBookSubject();
		t = subjectInfo;
		// 重新填充部分信息
		t.setSubjectId(uuid);
		// 重点
		t.setSubjectRef(subjectId);
		t.setCreateUserId(loginUserId);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setUpdateTime(null);
		t.setUpdateUserId(null);
		t.setState("Y");
		t.setViewNum(0);
		t.setStarLevel(0);
		t.setLikeNum(0);
		t.setStoreNum(0);
		t.setReportNum(0);
		// 执行保存
		tevglBookSubjectMapper.insert(t);
		return t.getSubjectId();
	}
	
	/**
	 * 查看教学包相关基础数据
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewPkgBaseInfo(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> pkgInfo = tevglPkgInfoMapper.selectObjectMapById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		if (!"Y".equals((String)pkgInfo.get("state"))) {
			return R.error("此教学包状态已无效，无法查看");
		}
		// 如果是已经发布了的教学包,不管是谁都没有权限
		if ("Y".equals(pkgInfo.get("releaseStatus"))) {
			pkgInfo.put("hasPermissionActual", false);
		} else {
			pkgInfo.put("hasPermissionActual", true);
		}
		// 字典转换
		pkgInfo.put("pkgLimitName", pkgInfo.get("pkgLimit"));
		convertUtil.convertDict(pkgInfo, "pkgLimitName", "pkgLimit"); // 使用限制(来源字典：授权，免费)
		convertUtil.convertDict(pkgInfo, "pkgLevel", "pkgLevel"); // 适用层次(来源字典：本科，高职，中职)
		convertUtil.convertDict(pkgInfo, "deployMainType", "deployMainType"); // 发布方大类(来源字典：学校，个人，创蓝)
		convertUtil.convertDict(pkgInfo, "deploySubType", "deploySubType"); // 发布方小类
		// 图片处理
		Object logo = pkgInfo.get("pkgLogo");
		pkgInfo.put("pkgLogo", uploadPathUtils.stitchingPath(logo, "12"));
		// 是否为创建者或共建者、处理文字显示
		String createUserId = (String)pkgInfo.get("createUserId");
		handleTextShow(pkgInfo, createUserId, loginUserId);
		// 创建者信息(理解为主编)
		pkgInfo.put("createUserInfo", getUserInfo(createUserId));
		// 副主编信息
		String traineeId = getSubeditorTraineeId(pkgId);
		pkgInfo.put("subeditorInfo", getUserInfo(traineeId));
		// 更新查阅数
		TevglPkgInfo pkg = new TevglPkgInfo();
		pkg.setPkgId(pkgId);
		pkg.setViewNum(1);
		tevglPkgInfoMapper.plusNum(pkg);
		// 当前教学包最新的衍生版本
		if ("N".equals(pkgInfo.get("releaseStatus"))) {
			Map<String, Object> params = new HashMap<>();
			params.put("refPkgId", pkgId);
			params.put("sidx", "create_time");
			params.put("order", "desc");
			List<TevglPkgInfo> list = tevglPkgInfoMapper.selectListByMap(params);
			if (list != null && list.size() > 0) {
				String pkgLogo = list.get(0).getPkgLogo();
				pkgInfo.put("prePkgLogo", uploadPathUtils.stitchingPath(pkgLogo, "12"));
			}
		}
		// 返回数据
		return R.ok().put(Constant.R_DATA, pkgInfo);
	}
	
	/**
	 * 获取这个教学包的创建者（主编）或者副主编信息
	 * @param traineeId
	 * @return
	 */
	private Map<String, Object> getUserInfo(String traineeId) {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("traineeId", "");
		info.put("traineePic", "/uploads/defaulthead.png");
		info.put("subeditorPic", "/uploads/defaulthead.png");
		info.put("traineeName", "");
		if (StrUtils.isNotEmpty(traineeId)) {
			TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
			if (traineeInfo != null) {
				info.put("traineeId", traineeInfo.getTraineeId());
				String traineePic = StrUtils.isEmpty(traineeInfo.getTraineePic()) ? traineeInfo.getTraineeHead() : traineeInfo.getTraineePic();
				info.put("traineePic", uploadPathUtils.stitchingPath(traineePic, "16"));
				info.put("subeditorPic", uploadPathUtils.stitchingPath(traineePic, "16"));
				String traineeName = StrUtils.isEmpty(traineeInfo.getTraineeName()) ? traineeInfo.getNickName() : traineeInfo.getTraineeName();
				info.put("traineeName", traineeName);
			}
		}
		return info;
	}
	
	/**
	 * 获取这个教学包的副主编（学员主键）
	 * @param pkgId
	 * @return
	 */
	private String getSubeditorTraineeId(String pkgId) {
		Map<String, Object> map = new HashMap<>();
		map.put("pkgId", pkgId);
		List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			List<TevglBookpkgTeam> collect = list.stream().filter(a -> StrUtils.isNotEmpty(a.getIsSubeditor()) && a.getIsSubeditor().equals("Y")).collect(Collectors.toList());
			if (collect.size() > 0) {
				return collect.get(0).getUserId();
			}
		}
		return null;
	}
	
	/**
	 * 处理文字
	 * @param info 教学包
	 * @param createUserId 教学包的创建者
	 * @param loginUserId 当前登录用户
	 * @apiNote 
	 * <br>如果未登录，则右上角的文字，只需要字典转换
	 * <br>如果登录了，且登录用户为此教学包创建者，则显示【拥有者】
	 * <br>如果登录了，但不是此教学包的创建者，则去判断教学包创建者是否授权了当前登录用户，如果有权限，则显示【已授权】，否则显示字典转换后的就行了
	 */
	private void handleTextShow(Map<String, Object> info, String createUserId, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			// 前面已经字典转换了无需处理
			info.put("isCreator", false);
			info.put("isTogetherBuild", false);
		} else {
			if (loginUserId.equals(createUserId)) {
				info.put("pkgLimitName", "拥有者");
				info.put("isCreator", true);
				info.put("isTogetherBuild", false);
			} else {
				String pkgId = (String) info.get("pkgId");
				boolean flag = pkgPermissionUtils.hasPermission(pkgId, loginUserId, createUserId);
				if (flag) {
					info.put("pkgLimitName", "已授权");
					info.put("isCreator", false);
					info.put("isTogetherBuild", true);
				}
				boolean isReceiver = !StrUtils.isNull(info.get("receiverUserId")) && loginUserId.equals(info.get("receiverUserId"));
				if (isReceiver) {
					info.put("pkgLimitName", "接管");
				}
			}
		}
	}

	/**
	 * 取部分属性
	 * 
	 * @param tevglPkgInfo
	 * @return
	 */
	private Map<String, Object> converToSimpleMapInfo(TevglPkgInfo tevglPkgInfo) {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("pkgId", tevglPkgInfo.getPkgId());
		info.put("orgId", tevglPkgInfo.getOrgId());
		info.put("pkgName", tevglPkgInfo.getPkgName());
		info.put("pkgNo", tevglPkgInfo.getPkgNo());
		info.put("subjectId", tevglPkgInfo.getSubjectId());
		info.put("pkgKey", tevglPkgInfo.getPkgKey());
		info.put("pkgDesc", tevglPkgInfo.getPkgDesc());
		info.put("pkgLevel", tevglPkgInfo.getPkgLevel());
		info.put("pkgLimit", tevglPkgInfo.getPkgLimit());
		info.put("deployMainType", tevglPkgInfo.getDeployMainType());
		info.put("pkgRefCount", tevglPkgInfo.getPkgRefCount());
		info.put("pkgResCount", tevglPkgInfo.getPkgResCount());
		info.put("pkgActCount", tevglPkgInfo.getPkgActCount());
		// 图片处理
		String logo = tevglPkgInfo.getPkgLogo();
		if (StrUtils.isNotEmpty(logo)) {
			// 如果未转换，则需要拼接路径
			if (logo.indexOf("/uploads") == -1) {
				logo = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("12") + "/" + logo;
			}
		} else {
			// 可以设置一个默认的
			logo = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("12") + "/" + "default_pkg_logo.png";
		}
		info.put("pkgLogo", logo);
		return info;
	}

	/**
	 * 我的教学包列表
	 * @param params
	 * @param loginUserId 当前登录用户
	 * @param together 不为空时查询自己创建的教学包和共建中的教学包
	 * @return
	 */
	@Override
	public R listMyPkgInfo( Map<String, Object> params, String loginUserId, String together) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("参数loginUserId为空");
		}
		if (!StrUtils.isNull(params.get("type")) && "other".equals(params.get("type"))) {
			return listMyPkgInfoForOther(params, loginUserId);
		}
		// [重点♥]不展示该值为3的，可见性(来源字典:1私有or2公有3都不可见)
		//params.put("displayNo", "3");
		// 我的教学包
		params.put("state", "Y");
		params.put("createUserId", loginUserId);
		params.put("loginUserId", loginUserId);
		together = StrUtils.isEmpty(together) ? null : together;
		params.put("together", together);
		log.debug("查询条件：" + params);
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tevglPkgInfoList = tevglPkgInfoMapper.selectListMapByMapUnionAll(query);
		//List<Map<String, Object>> tevglPkgInfoList = tevglPkgInfoMapper.selectListMapByMap(query);
		//convertUtil.convertDict(tevglPkgInfoList, "pkgLimit", "pkgLimit"); // 使用限制(来源字典：授权，购买，免费)
		//convertUtil.convertDict(tevglPkgInfoList, "pkgLevel", "pkgLevel"); // 适用层次(来源字典：本科，高职，中职)
		//convertUtil.convertDict(tevglPkgInfoList, "deployMainType", "deployMainType"); // 发布方大类(来源字典：学校，个人，创蓝)
		// 查询衍生出的版本
		List<Object> pkgIds = tevglPkgInfoList.stream()
				.filter(tevglPkgInfo -> tevglPkgInfo.get("createUserId").equals(loginUserId) || loginUserId.equals(tevglPkgInfo.get("receiverUserId")))
				.map(tevglPkgInfo -> tevglPkgInfo.get("pkgId"))
				.collect(Collectors.toList());
		List<Map<String, Object>> deriveList = new ArrayList<Map<String,Object>>();
		if (pkgIds != null && pkgIds.size() > 0) {
			params.clear();
			params.put("refPkgIds", pkgIds);
			params.put("releaseStatus", "Y");
			params.put("sidx", "t1.create_time");
			params.put("order", "desc");
			List<Map<String,Object>> selectSimpleList = tevglPkgInfoMapper.selectListByMapForSimple(params);
			selectSimpleList.stream().forEach(info -> {
				info.put("isCreator", true);
				// 如果有人使用过就不再允许删除
                info.put("hasDeleteIcon", false);
                List<Map<String, Object>> list = tevglPkgInfoMapper.selectHowManyPeopleUseIt(info.get("pkgId"));
                if (list == null || list.size() == 0) {
                    info.put("hasDeleteIcon", true);
                }
                info.put("howManyPeopleUseIt", list.size());
            });
			deriveList.addAll(selectSimpleList);
		}
		tevglPkgInfoList.stream().forEach(a -> {
			// 当前源教学包的发布出去的衍生版本
			List<Map<String, Object>> children = deriveList.stream().filter(pp -> !StrUtils.isNull("refPkgId") && pp.get("refPkgId").equals(a.get("pkgId")) && !pp.get("pkgId").equals(a.get("pkgId"))).collect(Collectors.toList());
			a.put("children", children);
			// 当前有多少人正在使用这个教学包
			List<Map<String, Object>> list = tevglPkgInfoMapper.selectHowManyPeopleUseIt(a.get("pkgId"));
			log.debug("当前有多少人正在使用这个教学包：" + list.size());
			a.put("howManyPeopleUseIt", list.size());
			// 图片处理
	        a.put("pkgLogo", uploadPathUtils.stitchingPath(a.get("pkgLogo"), "12"));
			// 是否为创建者
	        handleTags(loginUserId, a, params, list);
	        // 单独兼容下生成环境的问题
	        // 大学英语
	        if (Arrays.asList("c0ea18bb858547a39283053169c03270", "1a3286faaead47d29731e1b316b7c6ed").contains(a.get("pkgId"))) {
	        	a.put("hasDeleteIcon", false);
	        }
		});
		PageUtils pageUtil = new PageUtils(tevglPkgInfoList, query.getPage(), query.getLimit());
        TevglSiteSysMsgQuery queryParameters = new TevglSiteSysMsgQuery();
        queryParameters.setMsgType(SiteSysMsgTypeEnum.MST_TYPE_110_PKG_TRANSFER.getCode());
        queryParameters.setReadState(CommonEnum.STATE_NO.getCode());
        queryParameters.setToTraineeId(loginUserId);
        List<TevglSiteSysMsgVo> msgList = tevglSiteSysMsgMapper.findMsgList(queryParameters);
        if (msgList != null && !msgList.isEmpty()) {
            tevglSiteSysMsgMapper.batchUpdateRead(msgList.stream().map(a -> a.getMsgId()).collect(Collectors.toList()));
        }
		return R.ok().put(Constant.R_DATA, pageUtil).put("msgList", msgList);
	}
	
	private R listMyPkgInfoForOther(@RequestParam Map<String, Object> params, String loginUserId) {
		params.put("display", "3");
		params.put("createUserId", loginUserId);
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		if (StrUtils.isNull(params.get("type")) || !"other".equals(params.get("type"))) {
			PageUtils pageUtil = new PageUtils(new ArrayList<>(), query.getPage(), query.getLimit());
			return R.ok().put(Constant.R_DATA, pageUtil);
		}
		List<Map<String,Object>> tevglPkgInfoList = tevglPkgInfoMapper.selectListMapByMapFromCtRoom(query);
		if (tevglPkgInfoList == null || tevglPkgInfoList.size() == 0) {
			PageUtils pageUtil = new PageUtils(tevglPkgInfoList, query.getPage(), query.getLimit());
			return R.ok().put(Constant.R_DATA, pageUtil);
		}
		// 查出这些教学包的衍生版本
		List<Object> pkgIdList = tevglPkgInfoList.stream().map(a -> a.get("pkgId")).collect(Collectors.toList());
		Map<String, Object> map = new HashMap<>();
		map.put("refPkgIds", pkgIdList);
		List<Map<String, Object>> selectSimpleList = tevglPkgInfoMapper.selectSimpleList(map);
		tevglPkgInfoList.stream().forEach(pkgInfo -> {
			if (StrUtils.isNull(pkgInfo.get("pkgLogo"))) {
				pkgInfo.put("pkgLogo", uploadPathUtils.stitchingPath(pkgInfo.get("pic"), "14"));
			} else {
				pkgInfo.put("pkgLogo", uploadPathUtils.stitchingPath(pkgInfo.get("pic"), "12"));
			}
			// 取出源教学包的发布出去的衍生版本
			List<Map<String, Object>> children = selectSimpleList.stream().filter(a -> a.get("refPkgId").equals(pkgInfo.get("pkgId"))).collect(Collectors.toList());
			children.stream().forEach(childrenPkgInfo -> {
				if (loginUserId.equals(childrenPkgInfo.get("createUserId"))) {
					childrenPkgInfo.put("isCreator", true);
				}
				params.clear();
				params.put("refPkgId", childrenPkgInfo.get("pkgId"));
				List<Map<String,Object>> simpleList = tevglPkgInfoMapper.selectSimpleList(params);
				if (simpleList != null && simpleList.size() > 0) {
					childrenPkgInfo.put("hasDeleteIcon", false);
				} else {
					childrenPkgInfo.put("hasDeleteIcon", true);
				}
			});
			pkgInfo.put("children", children);
		});
		PageUtils pageUtil = new PageUtils(tevglPkgInfoList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 处理
	 * @param loginUserId 当前登录用户
	 * @param pkgInfo 教学包信息
	 * @param params 查询条件
	 * @param list 当前有多少人使用了这个教学包
	 */
	private void handleTags(String loginUserId, Map<String, Object> pkgInfo, Map<String, Object> params, List<Map<String, Object>> list) {
		if (StrUtils.isNull(pkgInfo) || StrUtils.isNull(pkgInfo.get("createUserId"))) {
			pkgInfo.put("isTogetherBuild", false);
			pkgInfo.put("isCreator", false);
			pkgInfo.put("hasEditPermission", false);
			return;
		}
		// 是否有删除的小图标
		String type = pkgInfo.get("type").toString();
		switch (type) {
			// 自建的情况
			case "ower":
				pkgInfo.put("isCreator", true); // 是否为创建者
                pkgInfo.put("isTogetherBuild", false); // 是否为共建者
                pkgInfo.put("hasEditPermission", true); // 是否能修改基本信息
                params.clear();
                params.put("refPkgId", pkgInfo.get("pkgId"));
                List<Map<String,Object>> simpleList = tevglPkgInfoMapper.selectSimpleList(params);
                if (simpleList != null && simpleList.size() > 0) {
                    pkgInfo.put("hasDeleteIcon", false);
                } else {
                    pkgInfo.put("hasDeleteIcon", true);
                }
                // 但是
                if (!StrUtils.isNull(pkgInfo.get("display")) && "3".equals(pkgInfo.get("display"))) {
                    pkgInfo.put("hasDeleteIcon", false);
                }
                break;
             // 移交接管的情况
            case "receiver" :
                pkgInfo.put("isCreator", false); // 是否为创建者
                pkgInfo.put("isTogetherBuild", false); // 是否为共建者
                pkgInfo.put("hasEditPermission", true); // 是否能修改基本信息
                params.clear();
                params.put("refPkgId", pkgInfo.get("pkgId"));
                List<Map<String,Object>> selectSimpleList = tevglPkgInfoMapper.selectSimpleList(params);
                if (selectSimpleList != null && selectSimpleList.size() > 0) {
                    pkgInfo.put("hasDeleteIcon", false);
                } else {
                    pkgInfo.put("hasDeleteIcon", true);
                }
                // 但是
                if (!StrUtils.isNull(pkgInfo.get("display")) && "3".equals(pkgInfo.get("display"))) {
                    pkgInfo.put("hasDeleteIcon", false);
                }
                break;
			// 共建的情况下
			case "together":
				pkgInfo.put("hasDeleteIcon", false);
				pkgInfo.put("isCreator", false);
				pkgInfo.put("isTogetherBuild", true);
				pkgInfo.put("hasEditPermission", false);
				break;
			// 授权的情况下	
			case "auth":
				pkgInfo.put("hasDeleteIcon", false);
				pkgInfo.put("isCreator", false);
				pkgInfo.put("isTogetherBuild", false);
				pkgInfo.put("hasEditPermission", false);
				break;
			default:
				break;
		}
		/*if (loginUserId.equals(a.get("createUserId"))) {
			a.put("isCreator", true); // 是否为创建者
			a.put("isTogetherBuild", false); // 是否为共建者
			a.put("hasEditPermission", true); // 是否能修改基本信息
			// 是否有删除的小图标
			if ("Y".equals(a.get("releaseStatus"))) {
				a.put("hasDeleteIcon", false);
				//if (pkgUtils.isBeingUsed(a.get("pkgId"))) {
				if (list != null && list.size() > 0) {
					a.put("hasDeleteIcon", false);	
				} else {
					a.put("hasDeleteIcon", true);
				}
			} else {
				params.clear();
				params.put("refPkgId", a.get("pkgId"));
				List<Map<String,Object>> simpleList = tevglPkgInfoMapper.selectSimpleList(params);
				if (simpleList != null && simpleList.size() > 0) {
					a.put("hasDeleteIcon", false);
				} else {
					a.put("hasDeleteIcon", true);
				}
				// 但是
				if (!StrUtils.isNull(a.get("display")) && "3".equals(a.get("display"))) {
					a.put("hasDeleteIcon", false);
				}
			}
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(a.get("pkgId"));
			if (tevglTchClassroom != null) {
				a.put("hasDeleteIcon", false);
			}
		} else {
			a.put("isCreator", false); // 是否为创建者
			a.put("isTogetherBuild", true); // 是否为共建者
			a.put("hasEditPermission", false); // 是否能修改基本信息
		}*/
	}

	/**
	 * 教学包下拉列表（可查询自己创建、授权的、他人免费的）
	 * @param params
	 * @param loginUserId 登录用户
	 * @param queryAuthorized 布尔值，为true则会查询自己创建和已获得授权的教学包，false只会查询自己创建的教学包
	 * @param queryFree 布尔值，为true还会去查询别人创建的免费的教学包，false则不会查询
	 * @return
	 */
	@Override
	public List<Map<String, Object>> listPkgInfoForSelect( Map<String, Object> params,
			String loginUserId, boolean queryAuthorized, boolean queryFree) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (StrUtils.isEmpty(loginUserId)) {
			return result;
		}
		if (StrUtils.isNull(params.get("subjectRef"))) {
			return result;
		} 
		// 不展示该值为3的，可见性(来源字典:1私有or2公有3都不可见)(重点)
		// 在core工程具体使用
		//params.put("displayNo", "3");
		// 自己创建的与别人创建的免费的教学包
		if (queryFree) {
			params.put("createUserIdAndFree", loginUserId);	
		} else {
			params.put("createUserId", loginUserId); // 自己创建的
		}
		// 用于修改课堂时的教学包回显
		if (!StrUtils.isNull(params.get("ctId"))) {
			log.debug("查询课堂对应的教学包");
			TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(params.get("ctId"));
			if (classroom != null) {
				TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(classroom.getPkgId());
				if (tevglPkgInfo != null) {
					Map<String, Object> data = new HashMap<>();
					data.put("pkgId", tevglPkgInfo.getPkgId());
					data.put("pkgName", tevglPkgInfo.getPkgName());
					data.put("pkgLogo", tevglPkgInfo.getPkgLogo());
					data.put("pkgVersion", tevglPkgInfo.getPkgVersion());
					data.put("createUserId", tevglPkgInfo.getCreateUserId());
					result.add(data);
				}
			}
		}
		params.put("releaseStatus", "Y");
		params.put("sidx", "t1.create_time");
		params.put("order", "desc");
		List<Map<String, Object>> minePkgList = tevglPkgInfoMapper.selectSimpleList(params);
		log.debug("新增课堂时根据条件查询可用的教学包下拉列表：" + params);
        log.debug("登录用户【"+loginUserId+"】被自己的、以及免费的已发布的教学包：" + minePkgList.size());
		// 获取当前登录用户被授权的已发布状态的教学包
        if (queryAuthorized) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", loginUserId);
            map.put("majorId", params.get("majorId"));
            map.put("subjectRef", params.get("subjectRef"));
            map.put("releaseStatus", "Y");
            List<Map<String, Object>> authorizedList = tevglBookpkgTeamMapper.selectSimplePackageList(map);
            log.debug("登录用户【"+loginUserId+"】被授权的已发布的教学包：" + authorizedList.size());
            if (authorizedList != null && authorizedList.size() > 0) {
                result.addAll(authorizedList);
            }
        }
		// 添加并返回数据
		result.addAll(minePkgList);
		// 部分数据处理
		result.stream().forEach(pkgInfo -> {
			// 教学包封面处理
			pkgInfo.put("pkgLogo", uploadPathUtils.stitchingPath(pkgInfo.get("pkgLogo"), "12"));
			// 返回标签名表示[授权][自建][免费]
			handlePkgName(pkgInfo, loginUserId);
		});
		return result;
	}
	
	/**
	 * 【教学包下拉列表】注意：会一次性查询自己的，衍生版本，以及别人创建的免费的，以及被授权的
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryPkgListByUnionAllForSelect(Map<String, Object> params, String loginUserId) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        params.put("loginUserId", loginUserId);
        if (params.get("subjectRef") != null) {
            List<String> majorIdList = tevglBookSubperiodMapper.findMajorIdListBySubjectId(params.get("subjectRef").toString());
            if (majorIdList != null && !majorIdList.isEmpty()) {
                params.put("majorIdList", majorIdList);
            }
        }
        result = tevglPkgInfoMapper.queryPkgListByUnionAllForSelect(params);
        // 用于修改课堂时的教学包回显
        if (!StrUtils.isNull(params.get("ctId"))) {
            log.debug("查询课堂对应的教学包");
            TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(params.get("ctId"));
            if (classroom != null) {
                TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(classroom.getPkgId());
                if (tevglPkgInfo != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("pkgId", tevglPkgInfo.getPkgId());
                    data.put("pkgName", tevglPkgInfo.getPkgName());
                    data.put("pkgLogo", tevglPkgInfo.getPkgLogo());
                    data.put("pkgVersion", tevglPkgInfo.getPkgVersion());
                    data.put("createUserId", tevglPkgInfo.getCreateUserId());
                    result.add(data);
                }
            }
        }
        // 部分数据处理
        result.stream().forEach(pkgInfo -> {
            // 教学包封面处理
            pkgInfo.put("pkgLogo", uploadPathUtils.stitchingPath(pkgInfo.get("pkgLogo"), "12"));
            // 返回标签名表示[授权][自建]
            handlePkgName(pkgInfo, loginUserId);
        });
        return R.ok().put(Constant.R_DATA, result);
	}
	
	
	/**
     * 处理名称
     * @param pkgInfo
     * @param loginUserId
     */
    private void handlePkgName(Map<String, Object> pkgInfo, String loginUserId) {
        if (pkgInfo == null) {
            return;
        }
        String tempName = pkgInfo.get("pkgName").toString();
        // 版本号
        String pkgVersion = StrUtils.isNull(pkgInfo.get("pkgVersion")) ? "" : pkgInfo.get("pkgVersion").toString();
        // 使用限制1授权2免费
        String pkgLimit = StrUtils.isNull(pkgInfo.get("pkgLimit")) ? "0" : pkgInfo.get("pkgLimit").toString();
        String pkgLimitTagName = "";
        // 返回标签名表示[授权][自建]
        if (pkgInfo.get("createUserId").equals(loginUserId)) {
            pkgInfo.put("tagName", "自建");
        } else {
            switch (pkgLimit) {
                case "1":
                    pkgLimitTagName = "授权";
                    break;
                case "2":
                    pkgLimitTagName = "免费";
                    break;
                default:
                    break;
            }
            pkgInfo.put("tagName", pkgLimitTagName);
        }
        // 拼接
        if (!StrUtils.isNull(pkgInfo.get("pkgVersion"))) {
            tempName = tempName + " ["+pkgVersion+"]";
        }
        if (!loginUserId.equals(pkgInfo.get("createUserId")) && !StrUtils.isNull(pkgInfo.get("createUserName"))) {
            tempName = tempName + "【"+pkgInfo.get("createUserName") +" "+ pkgLimitTagName + "】";
        } else {
			/*String pattern = DateUtils.convertDatePattern(pkgInfo.get("createTime").toString(), "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日 HH:mm");
			tempName += " " + pattern;*/
        }
        pkgInfo.put("pkgName", tempName);
    }
	
	/**
	 * 获取教学包的全部活动
	 * @param pkgId
	 * @return
	 */
	private String getActivityJson(String pkgId) {
		Map<String, Object> ps = new HashMap<>();
		ps.put("pkgId", pkgId);
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(ps);
		log.debug("教学包["+pkgId+"]"+"的活动关系：" + list.size());
		log.debug(list.toString());
		if (list == null || list.size() == 0) {
			return "";
		}
		// 活动主键作为键，活动类型作为值
		Map<String, String> data = list.stream().collect(Collectors.toMap(TevglPkgActivityRelation::getActivityId, TevglPkgActivityRelation::getActivityType));
		List<Map<String, String>> resutlList = new ArrayList<Map<String,String>>();
		if (data != null) {
			resutlList.add(data);
		}
		String jsonString = JSON.toJSONString(resutlList);
		return jsonString;
	}
	
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
	public R releaseTeachingPackage( JSONObject jsonObject, String loginUserId) throws BudaosException {
		// 当前教学包
		String pkgId = jsonObject.getString("pkgId");
		// 当前课程
		String subjectId = jsonObject.getString("subjectId");
		// 当前数据
		JSONArray list = jsonObject.getJSONArray("list");
		// 未关联章节的活动
		// 数据格式要求
		// [{'activityId': '', 'activityType': '1'}, {'activityId': '', 'activityType': '3'}]
		JSONArray activityList = jsonObject.getJSONArray("activityList");
		// 所选择的云盘目录或文件
		// 数据格式要求
		// ['1', '2', 'wqe21asa']
		JSONArray cloudPanList = jsonObject.getJSONArray("cloudPanList");
		// 合法性校验
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (list == null || list.size() == 0) {
			return R.error("未能获取到数据");
		}
		// 查找传入的这个教学包是否引用了其它的教学包
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		if (!loginUserId.equals(pkgInfo.getCreateUserId())) {
			return R.error("非法操作");
		}
		// 教学包名称和版本号处理
		String pkgName = jsonObject.getString("pkgName");
		String pkgVersion = jsonObject.getString("pkgVersion");
		R r = checkSoleNameAndVersion(pkgName, pkgVersion, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 获取源课程的章节数据
		List<TevglBookChapter> chapterList = getChapterListData(subjectId);
		if (chapterList == null || chapterList.size() == 0) {
			return R.error("请先录入一些章节再来发布吧~");
		}
		// 处理是否应用了别人的教学包
		String privateUse = null;
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
		// 先生成保存新的课程基本信息
		String subjectIdNew = doSaveSubjectInfo(subjectId, loginUserId);
		log.debug("新的课程:" + subjectIdNew);
		// 再保存新的教学包基本信息
		String newPkgId = doSavePkgInfo(jsonObject, subjectIdNew, loginUserId, privateUse);
		log.debug("新的教学包:" + newPkgId);
		// 更新教学包引用数
		pkgUtils.plusPkgRefNum(pkgId, 1);
		// 复制
		Map<String, Object> params = new HashMap<>();
		test(newPkgId, subjectIdNew, subjectIdNew, chapterList, list, loginUserId, params);
		// 复制没有关联章节的活动
		if (activityList != null) {
			for (int i = 0; i < activityList.size(); i++) {
				JSONObject actObj = activityList.getJSONObject(i);
				String activityId = actObj.getString("activityId");
				String activityType = actObj.getString("activityType");
				if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityType)) {
					log.debug("复制投票问卷活动");
					tevglActivityVoteQuestionnaireServiceImpl.copy(activityId, newPkgId, loginUserId, null, i);
				} else if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityType)) { // 头脑风暴
					log.debug("复制头脑风暴活动");
					tevglActivityBrainstormingServiceImpl.copy(activityId, newPkgId, loginUserId, null, i);
				} else if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) { // 答疑/讨论
					log.debug("复制答疑讨论活动");
					tevglActivityAnswerDiscussServiceImpl.copy(activityId, newPkgId, loginUserId, null, i);
				} else if (GlobalActivity.ACTIVITY_4_TEST_ACT.equals(activityType)) { // 测试活动
					tevglActivityTestPaperServiceImpl.copy(activityId, newPkgId, loginUserId, null, i);
				} else if (GlobalActivity.ACTIVITY_5_TASK_GROUP.equals(activityType)) { // 作业/小组任务
					tevglActivityTestPaperServiceImpl.copy(activityId, newPkgId, loginUserId, null, i);
				}
			}
		}
		try {
			// 先查找此教学包的目录和文件
			List<Map<String,Object>> allList = selectListDatas(pkgId);
			// 构建成父子关系的数据格式
			List<Map<String,Object>> panList = buildTree("0", allList, 0);
			// 递归处理
			doHandleCloudPanData(cloudPanList, pkgId, newPkgId, loginUserId, panList, "0");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("云盘数据复制失败", e);
		}
		// 返回数据
		return R.ok("发布成功").put(Constant.R_DATA, flag).put("pkgId", newPkgId);
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
        if (cloudPanList == null || cloudPanList.size() == 0
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
                                FileUtils.copyFile(oldFile, newFile);
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
					/*// 筛选出本目录的目录
					List<Map<String, Object>> dirList =  children.stream().filter(a -> a.get("type").equals("1") && a.get("parentId").equals(id)).collect(Collectors.toList());
					// 递归
					if (dirList != null && dirList.size() > 0) {
					    doHandleCloudPanData(cloudPanList, oldPkgId, newPkgId, loginUserId, dirList, newParentId);
					}
					// 筛选出本目录的文件
					List<Map<String, Object>> fileList =  children.stream().filter(a -> a.get("type").equals("2") && a.get("parentId").equals(id)).collect(Collectors.toList());
					// 递归
					if (fileList != null && fileList.size() > 0) {
					    doHandleCloudPanData(cloudPanList, oldPkgId, newPkgId, loginUserId, fileList, newParentId);
					}*/
                }
                // 如果匹配上了 end
            }
            // 遍历此教学包的所有目录和文件 end
        }
        // 遍历用户选择的文件或目录 end
    }
	
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
	 * 合法性校验
	 * 
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	private R checkIsPass(TevglPkgInfo tevglPkgInfo, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("参数loginUserId为空");
		}
		if (StrUtils.isEmpty(tevglPkgInfo.getPkgName())) {
			return R.error("请输入教学包的名称");
		}
		if (tevglPkgInfo.getPkgName().length() > 50) {
			return R.error("教学包名称不能超过50个字符");
		}
		if (StrUtils.isEmpty(tevglPkgInfo.getMajorId())) {
			return R.error("请选择职业路径");
		}
		if (StrUtils.isEmpty(tevglPkgInfo.getSubjectId())) {
			return R.error("请选择课程体系");
		}
		if (StrUtils.isEmpty(tevglPkgInfo.getPkgTraineeName())) {
			return R.error("联系人名称不能为空");
		}
		if (tevglPkgInfo.getPkgTraineeName().length() > 50) {
			return R.error("联系人名称不能超过50个字符");
		}
		// 必须有一种联系方式
		boolean flag1 = StrUtils.isEmpty(tevglPkgInfo.getPkgTraineeQq());
		boolean flag2 = StrUtils.isEmpty(tevglPkgInfo.getPkgTraineeEmail());
		boolean flag3 = StrUtils.isEmpty(tevglPkgInfo.getPkgTraineeMobile());
		boolean flag4 = StrUtils.isEmpty(tevglPkgInfo.getPkgTraineeWx());
		if (flag1 && flag2 && flag3 && flag4) {
			return R.error("请至少填写一种联系方式");
		}
		// 手机格式验证
		if (StrUtils.isNotEmpty(tevglPkgInfo.getPkgTraineeMobile())) {
			if (!isMobile(tevglPkgInfo.getPkgTraineeMobile())) {
				return R.error("手机格式不正确");
			}
		}
		// 邮箱格式验证
		if (StrUtils.isNotEmpty(tevglPkgInfo.getPkgTraineeEmail())) {
			if (!isEmail(tevglPkgInfo.getPkgTraineeEmail())) {
				//return R.error("邮箱格式不正确");
			}
		}
		return R.ok();
	}
	
	/**
	 * 手机格式验证
	 * 
	 * @param mobile
	 * @return
	 */
	private boolean isMobile(String mobile) {
		Pattern p = Pattern.compile("^1[3456789]\\d{9}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 邮箱格式验证
	 * @param email
	 * @return
	 */
	private boolean isEmail(String email) {
		return email.matches("^\\w+@(\\w+\\.)+\\w+$");
	}

	/**
	 * 复制活动相关数据
	 * @param newPkgId 所属教学包id
	 * @param activityIdArray 被勾选的活动 其中对象数据格式要求为: 活动id作为key，活动类型作为值
	 * @param loginUserId
	 */
	private void doCopyActivity(String newPkgId, JSONArray activityIdArray, /*List<TevglPkgResgroup> pkgResgroup,*/
			String loginUserId) {
		if (activityIdArray == null || activityIdArray.size() == 0) {
			return;
		}
		for (int i = 0; i < activityIdArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) activityIdArray.get(i);
			String activityType = "";
			String activityId = "";
			for (Entry<String, Object> m : jsonObject.entrySet()) {
				activityId = m.getKey();
				activityType = (String) m.getValue();
				// 投票/问卷
				if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityType)) {
					log.debug("复制投票问卷活动");
					tevglActivityVoteQuestionnaireServiceImpl.copy(activityId, newPkgId, loginUserId, null, i);
				} else if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityType)) { // 头脑风暴
					log.debug("复制头脑风暴活动");
					tevglActivityBrainstormingServiceImpl.copy(activityId, newPkgId, loginUserId, null, i);
				} else if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) { // 答疑/讨论
					log.debug("复制答疑讨论活动");
					tevglActivityAnswerDiscussServiceImpl.copy(activityId, newPkgId, loginUserId, null, i); // 答疑讨论
				}
			}
		}
		
	}

	/**
	 * 递归复制章节,分组，资源--------------------等待删除
	 * @param subjectId      新增课程后返回的主键(假如复制的了520的课程，生成的新的课程主键为1314，参数则传1314)
	 * @param parentId 父id
	 * @param list 具有层级结构的章节数据
	 * @param loginUserId 当前登录用户id
	 * @param chapterIdArray 被勾选的章节
	 * @param newPkgId 新的教学包id
	 * @param resgroupIdArray 被勾选的分组
	 * @apiNote 
	 * chapterIdArray说明: 假设勾选了某个子节点，不仅要把子节点的id传过来，其父节点以及所有直系祖辈节点都带过来
	 * 
	 */
	private void doCopy(String subjectId, String parentId, List<TevglBookChapter> list, String loginUserId,
			JSONArray chapterIdArray, String newPkgId, JSONArray resgroupIdArray, JSONArray activityObjArray) {
		if (list == null || list.size() == 0) {
			return;
		}
		// 一级一级章节与被勾选的章节比较
		// 如果相等
		List<TevglBookChapter> filterList = new ArrayList<TevglBookChapter>();
		list.stream().forEach(a -> {
			for (int i = 0; i < chapterIdArray.size(); i++) {
				if (a.getChapterId().equals(chapterIdArray.get(i))) {
					filterList.add(a);
				}
			}
		});
		if (filterList != null && filterList.size() > 0) {
			Map<String, Object> params = new HashMap<>();
			// 生成章节
			for (int i = 0; i < filterList.size(); i++) {
				// 填充章节信息并保存至数据库
				TevglBookChapter t = fillMyChapterInfo(subjectId, parentId, filterList.get(i), loginUserId);
				t.setChapterContent("发布教学包测试中");
				tevglBookChapterMapper.insert(t);
				// 查找该章节的所有分组
				params.put("chapterId", filterList.get(i).getChapterId());
				List<TevglPkgResgroup> resgroupList = tevglPkgResgroupMapper.selectListByMap(params);
				// 如果勾选了分组
				if (resgroupList != null && resgroupList.size() > 0 && resgroupIdArray != null && resgroupIdArray.size() > 0) {
					for (int j = 0; j < resgroupIdArray.size(); j++) {
						// 遍历源章节的所有分组
						for (int k = 0; k < resgroupList.size(); k++) {
							// 如果匹配, 则生成新的分组
							if (resgroupIdArray.get(j).equals(resgroupList.get(k).getResgroupId())) {
								doSaveResgroup(t.getChapterId(), resgroupList.get(k), newPkgId, j, subjectId);
							}
						}
					}
				}
				// 复制活动
				if (activityObjArray != null && activityObjArray.size() > 0) {
					for (int n = 0; n < activityObjArray.size(); n++) {
						@SuppressWarnings("unchecked")
						Map<String, Object> actMap = (Map<String, Object>) activityObjArray.get(n);
						for (Entry<String, Object> o : actMap.entrySet()) {
							String activityId = o.getKey();
							Object activityType = o.getValue();
							if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityType)) {
								log.debug("复制投票问卷活动");
								tevglActivityVoteQuestionnaireServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), n);
							} else if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityType)) { // 头脑风暴
								log.debug("复制头脑风暴活动");
								tevglActivityBrainstormingServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), i);
							} else if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) { // 答疑/讨论
								log.debug("复制答疑讨论活动");
								tevglActivityAnswerDiscussServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), i); // 答疑讨论
							}
						}
					}	
				}
				// 递归
				doCopy(subjectId, t.getChapterId(), filterList.get(i).getChildren(), loginUserId, chapterIdArray, newPkgId, resgroupIdArray, activityObjArray);
			}
		}

	}

	/**
	 * 保存章节对应的分组,资源
	 * @param newChapterId 新的章节id
	 * @param tevglPkgResgroup 源章节的所有分组
	 * @param newPkgId 新的教学包
	 * @param sortNum 排序号
	 */
	private void doSaveResgroup(String newChapterId, TevglPkgResgroup tevglPkgResgroup, String newPkgId, int sortNum, String subjectId) {
		if (StrUtils.isEmpty(newChapterId) || StrUtils.isEmpty(newPkgId)) {
			return;
		}
		if (tevglPkgResgroup == null) {
			return;
		}
		// 查询分组对应的资源
		TevglPkgRes pkgRes = tevglPkgResMapper.selectObjectByResgroupId(tevglPkgResgroup.getResgroupId());
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
		tevglPkgResgroupMapper.insert(t);
		// 填充新的资源信息
		TevglPkgRes pr = new TevglPkgRes();
		pr = pkgRes;
		pr.setResId(Identities.uuid());
		pr.setPkgId(newPkgId);
		pr.setResgroupId(t.getResgroupId());
		pr.setCreateTime(DateUtils.getNowTimeStamp());
		// 保存资源至数据库
		tevglPkgResMapper.insert(pr);
		// 更新资源数量
		pkgUtils.plusPkgResNum(newPkgId, 1);
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
	 * 保存一个新的教学包基本信息
	 * @param jsonObject
	 * @param newSubjectId 新的课程主键
	 * @param loginUserId 当前登录用户
	 * @return 返回新增后的教学包主键
	 */
	private String doSavePkgInfo(JSONObject jsonObject, String newSubjectId, String loginUserId, String privateUse) {
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
		pkgInfo.setPkgLimit(pkgLimit); // 适用限制
		pkgInfo.setPkgLogo(pkgLogo); // 封面
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
		pkgInfo.setReleaseStatus("Y");
		// ****
		pkgInfo.setPrivateUse(privateUse);
		// 保存至数据库
		tevglPkgInfoMapper.insert(pkgInfo);
		return pkgInfo.getPkgId();
	}
	
	/**
	 * 获取源课程的树形章节数据(具有层次关系)
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
		List<TevglBookChapter> chapterlist = tevglBookChapterMapper.selectListByMap(map);
		List<TevglBookChapter> list = tevglBookSubjectServiceImpl.buildBook(subjectId, chapterlist, 0);
		return list;
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
	 * 此方法不会复制课程、不会复制章节、不会复制资源分组、不会复制资源
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R referenceTeachingPackage(String pkgId, String loginUserId, String display) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包");
		}
		if (!"Y".equals(pkgInfo.getState())) {
			return R.error("教学包状态已无效");
		}
		// 如果登录用户不是此教学包的创建者,校验权限
		boolean hasPermission = pkgPermissionUtils.hasPermission(pkgId, loginUserId, pkgInfo.getCreateUserId());
		if (!hasPermission) {
			return R.error("没有权限，无法使用此教学包");
		}
		// 源教学包对应的课程
		String subjectId = pkgInfo.getSubjectId();
		// 示例化对象，重置一些数据
		TevglPkgInfo t = new TevglPkgInfo();
		t = pkgInfo;
		String newPkgId = Identities.uuid();
		t.setPkgId(newPkgId);
		t.setSubjectId(subjectId);
		t.setCreateUserId(loginUserId);
		t.setDisplay(StrUtils.isEmpty(display) ? null : display); // 是否可见
		t.setRefPkgId(pkgId); // 教学包来源
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setUpdateUserId(null);
		t.setUpdateUserId(null);
		t.setState("Y");
		t.setPkgNo(Identities.uuid());
		tevglPkgInfoMapper.insert(t);
		log.debug("新的教学包:" + newPkgId);
		
		// 各种活动
		String activityJson = getActivityJson(pkgId);
		JSONArray activityIdArray = JSON.parseArray(activityJson); // 解析
		// 根据勾选的活动,生成新的活动记录
		log.debug("开始执行复制活动");
		doCopyActivity(newPkgId, activityIdArray, loginUserId);
		log.debug("活动复制完毕");
		
		return R.ok().put(Constant.R_DATA, newPkgId);
	}
	
	/**
	 * 
	 * @param newPkgId 复制被选教学包后生成的新的教学包主键
	 * @param subjectId 新的教学包对应的课程主键
	 * @param parentId 父节点
	 * @param chapterList 被选教学包的课程主键查出的所有章节
	 * @param list 待发布的数据
	 * @param loginUserId 当前登录用户
	 * @param params 一个map作为查询条件
	 */
	private void test(String newPkgId, String subjectId, String parentId, 
			List<TevglBookChapter> chapterList, JSONArray list, String loginUserId, Map<String, Object> params) {
		if (StrUtils.isEmpty(newPkgId) || StrUtils.isEmpty(subjectId) 
				|| StrUtils.isEmpty(parentId) || list == null || list.size() == 0) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			JSONObject data = list.getJSONObject(i);
			// 被选择的章节
			String chapterId = data.getString("chapterId");
			// 遍历章节
			for (int c = 0; c < chapterList.size(); c++) {
				TevglBookChapter tevglBookChapter = chapterList.get(c);
				// 如果匹配上了
				if (chapterId.equals(tevglBookChapter.getChapterId())) {
					// 章节入库
					TevglBookChapter t = fillMyChapterInfo(subjectId, parentId, tevglBookChapter, loginUserId);
					t.setOrderNum(c);
					//t.setChapterContent("发布教学包测试中");
					tevglBookChapterMapper.insert(t);
					// 拿被选章节的被选分组
					JSONArray resgroupIdArray = data.getJSONArray("resgroupIdArray");
					if (resgroupIdArray != null && resgroupIdArray.size() > 0) {
						// 根据此章节查询它下面的分组
						params.put("chapterId", tevglBookChapter.getChapterId());
						List<TevglPkgResgroup> tevglPkgResgroupList = tevglPkgResgroupMapper.selectListByMap(params);
						if (tevglPkgResgroupList != null && tevglPkgResgroupList.size() > 0) {
							// 遍历分组
							for (int j = 0; j < resgroupIdArray.size(); j++) {
								for (int k = 0; k < tevglPkgResgroupList.size(); k++) {
									// 如果匹配, 分组入库
									if (resgroupIdArray.get(j).equals(tevglPkgResgroupList.get(k).getResgroupId())) {
										doSaveResgroup(t.getChapterId(), tevglPkgResgroupList.get(k), newPkgId, j, subjectId);
									}
								}
							}
						}
					}
					// （目前活动并没有强关联到分组上）
					// 如果选择了活动
					JSONArray activityArray = data.getJSONArray("activityArray");
					if (activityArray != null && activityArray.size() > 0) {
						for (int n = 0; n < activityArray.size(); n++) {
							JSONObject actObj = activityArray.getJSONObject(n);
							String activityId = actObj.getString("activityId");
							String activityType = actObj.getString("activityType");
							if (GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE.equals(activityType)) {
								log.debug("复制投票问卷活动");
								tevglActivityVoteQuestionnaireServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), n);
							} else if (GlobalActivity.ACTIVITY_2_BRAINSTORMING.equals(activityType)) { // 头脑风暴
								log.debug("复制头脑风暴活动");
								tevglActivityBrainstormingServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), n);
							} else if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(activityType)) { // 答疑/讨论
								log.debug("复制答疑讨论活动");
								tevglActivityAnswerDiscussServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), n);
							} else if (GlobalActivity.ACTIVITY_4_TEST_ACT.equals(activityType)) { // 测试活动
								tevglActivityTestPaperServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), n);
							} else if (GlobalActivity.ACTIVITY_5_TASK_GROUP.equals(activityType)) { // 作业/小组任务
								tevglActivityTestPaperServiceImpl.copy(activityId, newPkgId, loginUserId, t.getChapterId(), n);
							}
						}
					}
					// 递归
					test(newPkgId, subjectId, t.getChapterId(), tevglBookChapter.getChildren(), list, loginUserId, params);
				}
			}
		}
	}
	
	/**
	 * 新增课堂时采用了教学包的话，根据被采用的教学包，从而复制生成一个新的教学包
	 * @param inputPkgId 必传参数，被采用的教学包
	 * @param loginUserId 必传参数，当前登录用户
	 * @param display 可选参数，可见性(来源字典:1私有or2公有or3都不可见)，默认 null，如果传了3，谁都看不到，只能从数据库去看
	 * @return
	 * @throws BudaosException
	 */
	@Override
	@Transactional
	public R copy(String inputPkgId, String loginUserId, String display) throws BudaosException {
		if (StrUtils.isEmpty(inputPkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(inputPkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包");
		}
		if (!"Y".equals(pkgInfo.getState())) {
			return R.error("教学包状态已无效");
		}
		// 如果登录用户不是此教学包的创建者
		if (!loginUserId.equals(pkgInfo.getCreateUserId())) {
			Map<String, Object> params = new HashMap<>();
			params.put("userId", loginUserId);
			params.put("pkgId", pkgInfo.getPkgId());
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
			if (list == null || list.size() == 0) {
				return R.error("没有权限，无法使用此教学包");
			}
		}
		String newPkgId = Identities.uuid();
		String newSubjectId = Identities.uuid();
		// 复制一份源教学包对应的课程
		String subjectId = pkgInfo.getSubjectId();
		TevglBookSubject subjectInfo = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (subjectInfo != null) {
			log.debug("复制一份源教学包对应的课程" + subjectId);
			TevglBookSubject tevglBookSubject = subjectInfo;
			tevglBookSubject.setSubjectId(newSubjectId);
			tevglBookSubject.setSubjectRef(subjectId);
			tevglBookSubject.setCreateUserId(loginUserId);
			tevglBookSubject.setCreateTime(DateUtils.getNowTimeStamp());
			// 生成新的课程,并得到新的课程ID
			tevglBookSubjectMapper.insert(tevglBookSubject);
			// 复制
			tevglBookSubjectServiceImpl.copy(subjectId, newSubjectId, loginUserId, newPkgId);
		}
		
		// 示例化对象，重置一些数据
		TevglPkgInfo t = new TevglPkgInfo();
		t = pkgInfo;
		t.setPkgId(newPkgId);
		t.setSubjectId(newSubjectId);
		t.setCreateUserId(loginUserId);
		t.setDisplay(StrUtils.isEmpty(display) ? null : display); // 是否可见
		t.setRefPkgId(inputPkgId); // 教学包来源
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setUpdateUserId(null);
		t.setUpdateUserId(null);
		t.setState("Y");
		t.setPkgNo(Identities.uuid());
		tevglPkgInfoMapper.insert(t);
		log.debug("新的教学包:" + newPkgId);
		log.debug("新的课程:" + newSubjectId);
		
		// 复制各种活动
		String activityJson = getActivityJson(inputPkgId);
		JSONArray activityIdArray = JSON.parseArray(activityJson); // 解析
		// 根据勾选的活动,生成新的活动记录
		log.debug("开始执行复制活动");
		doCopyActivity(newPkgId, activityIdArray, loginUserId);
		log.debug("活动复制完毕");
		
		return R.ok("复制成功").put(Constant.R_DATA, newPkgId).put("newSubjectId", newSubjectId);
	}
	
	/**
	 * 如果新增课堂时
	 * @param inputPkgId
	 * @param loginUserId
	 * @param display
	 * @return
	 */
	@Override
	public R copyThen(String inputPkgId, String loginUserId, String display, String classroomName) {
		if (StrUtils.isEmpty(inputPkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(inputPkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包");
		}
		if (!"Y".equals(pkgInfo.getState())) {
			return R.error("教学包状态已无效");
		}
		// 如果登录用户不是此教学包的创建者、且，这个教学包是授权的
		if (!loginUserId.equals(pkgInfo.getCreateUserId()) && "1".equals(pkgInfo.getPkgLimit())) {
			Map<String, Object> params = new HashMap<>();
			params.put("userId", loginUserId);
			params.put("pkgId", pkgInfo.getPkgId());
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
			if (list == null || list.size() == 0) {
				return R.error("没有权限，无法使用此教学包");
			}
		}
		String newPkgId = Identities.uuid();
		String oldSubjectId = pkgInfo.getSubjectId();
		// 示例化对象，重置一些数据
		TevglPkgInfo t = new TevglPkgInfo();
		t = pkgInfo;
		if (StrUtils.isNotEmpty(classroomName)) {
			t.setPkgName(classroomName);
		}
		t.setPkgId(newPkgId);
		t.setSubjectId(oldSubjectId);
		t.setBaseSubject(oldSubjectId);
		t.setCreateUserId(loginUserId);
		t.setDisplay(StrUtils.isEmpty(display) ? null : display); // 是否可见
		t.setRefPkgId(inputPkgId); // 教学包来源
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setUpdateUserId(null);
		t.setUpdateUserId(null);
		t.setState("Y");
		t.setReleaseStatus("N");
		t.setPkgNo(Identities.uuid());
		TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(loginUserId);
		if (traineeInfo != null) {
			t.setPkgTraineeName(traineeInfo.getTraineeName());
			t.setPkgTraineeMobile(traineeInfo.getMobile());
			t.setPkgTraineeQq(traineeInfo.getTraineeQq());
			t.setPkgTraineeEmail(traineeInfo.getEmail());
		}
		tevglPkgInfoMapper.insert(t);
		/*
		// 复制各种活动
		String activityJson = getActivityJson(inputPkgId);
		JSONArray activityIdArray = JSON.parseArray(activityJson); // 解析
		// 根据勾选的活动,生成新的活动记录
		log.debug("开始执行复制活动");
		doCopyActivity(newPkgId, activityIdArray, loginUserId);
		log.debug("活动复制完毕");
		*/
		// 查找源教学包的活动
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", inputPkgId);
		List<TevglPkgActivityRelation> list = tevglPkgActivityRelationMapper.selectListByMap(params);
		log.debug("源教学包对应的活动：" + list.size());
		if (list != null && list.size() > 0) {
			list.stream().forEach(relation -> {
				// 建立活动与教学包的关系
				pkgUtils.buildRelation(newPkgId, relation.getActivityId(), relation.getActivityType());
			});
		}
		return R.ok("章节引用成功、活动复制成功").put(Constant.R_DATA, newPkgId);
	}

	/**
	 * 将教学包授权给某人，这样某人才能使用这个教学包
	 * @param pkgId 教学包
	 * @param userId 被授权的人
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R authorizationToWho(String pkgId, String userId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(userId)) {
			return R.error("该教师没有网站账号，暂无法授权");
		} else {
			TevglTraineeInfo tevglTraineeInfo = tevglTraineeInfoMapper.selectObjectById(userId);
			if (tevglTraineeInfo == null) {
				return R.error("该教师没有网站账号，暂无法授权");
			}
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的教学包");
		}
		boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
		if (!hasOperatingAuthorization) {
			return R.error("没有权限，无法进行教学包授权操作");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("userId", userId);
		List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			return R.ok("授权成功");
		}
		TevglBookpkgTeam team = new TevglBookpkgTeam();
		team.setTeamId(Identities.uuid());
		team.setSubjectId(tevglPkgInfo.getSubjectId());
		team.setPgkId(pkgId);
		team.setUserId(userId);
		tevglBookpkgTeamMapper.insert(team);
		return R.ok("授权成功");
	}

	/**
	 * 取消授权
	 * @param pkgId
	 * @param userId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R unAuthorization(String pkgId, String userId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(userId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的教学包");
		}
		// 没有权限，无法操作教学包
        boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
        if (!hasOperatingAuthorization) {
            return R.error("没有权限，无法操作教学包");
        }
		Map<String, Object> params = new HashMap<>();
		params.put("refPkgId", pkgId);
		// 首先根据refPkgId去查询教学包信息
		List<TevglPkgInfo> pkgInfos = tevglPkgInfoMapper.selectListByMap(params);
		List<String> pkgIds = pkgInfos.stream().map(a -> a.getPkgId()).collect(Collectors.toList());
		// 新增判断:当教学包id不为空时查询课堂表是否有这条记录，如果有则提示，如果没有则取消授权
		if (pkgIds != null && pkgIds.size() > 0) {
			params.clear();
			params.put("pkgIds", pkgIds);
			params.put("teacherId", userId);
			// 然后根据教学包信息得到pkgIds和教师id去课堂表里边查询是否有这条记录
			List<TevglTchClassroom> classrooms = tevglTchClassroomMapper.selectListByMap(params);
			// 如果有人用了教学包，就无法清空此人的权限
			if (classrooms != null && classrooms.size() > 0) {
				return R.error("此人已使用了教学包，无法清空权限");
			}
		}
		
		params.clear();
		params.put("pkgId", pkgId);
		params.put("userId", userId);
		List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			list.stream().forEach(a -> {
				tevglBookpkgTeamMapper.delete(a.getTeamId());
			});
		}
		return R.ok("取消授权");
	}
	
	/**
	 * 查询当前可更换的版本
	 * @param ctId 当前上课的课堂
	 * @param pkgId 当前上课的课堂对应的教学包id
	 * @return
	 */
	@Override
	public R queryUpgradeList(String ctId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(ctId)) {
            return R.error("必传参数为空");
        }
        TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
        if (tevglTchClassroom == null) {
            return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        if (!tevglTchClassroom.getPkgId().equals(pkgId)) {
        	return R.error("非法数据");
        }
        // 当前登录用户是否为课堂接收者
        boolean isRoomReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getReceiverUserId());
        if (!loginUserId.equals(tevglTchClassroom.getCreateUserId()) && !isRoomReceiver) {
            return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        if ("3".equals(tevglTchClassroom.getClassroomState())) {
            return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        // 当前课堂对应的教学包id
        TevglPkgInfo tevglTchClassroomPkgInfo = tevglPkgInfoMapper.selectObjectById(tevglTchClassroom.getPkgId());
        if (tevglTchClassroomPkgInfo == null) {
            return R.error("无效的记录");
        }
        // 兼容下需求变更生产环境下专升本的课堂数据
        //List<String> prodList = Arrays.asList("bdd533929612421dae212d4b1950d64d", "c0ea18bb858547a39283053169c03270", "1a3286faaead47d29731e1b316b7c6ed");
        // 大学英语课堂暂定不允许更换课堂
        if ("9878329d01bf44018168583a0001d353".equals(ctId)) {
        	return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        if (ConstantProd.prodPkgIdList.contains(pkgId) || ConstantProd.prodPkgIdList.contains(tevglTchClassroomPkgInfo.getRefPkgId())) {
            log.info("兼容下需求变更生产环境下专升本的课堂数据");
            return prod(tevglTchClassroom.getPkgId(), loginUserId);
        }
        if (StrUtils.isEmpty(tevglTchClassroomPkgInfo.getRefPkgId())) {
            return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        // 找到当前课堂教学包所引用的教学包
        TevglPkgInfo tevglTchClassroomPkgInfoRef = tevglPkgInfoMapper.selectObjectById(tevglTchClassroomPkgInfo.getRefPkgId());
        if (tevglTchClassroomPkgInfoRef == null) {
        	return R.error("更换失败");
        }
        // 查询条件
        Map<String, Object> params = new HashMap<>();
        // 递归获取最顶层的教学包
        String id = getPkgId(tevglTchClassroomPkgInfo.getRefPkgId());
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(id);
        if (tevglPkgInfo == null) {
            return R.error("抱歉，未能获取到可更新的版本");
        }
        // 最终返回数据
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        params.clear();
        params.put("refPkgId", id);
        if ("3".equals(tevglPkgInfo.getDisplay()) && pkgId.equals(id)) {
            params.put("selfPkgId", id);
        } else {
            //params.put("releaseStatus", "Y");
        }
        params.put("releaseStatus", "Y");
        params.put("sidx", "t1.create_time");
        params.put("order", "desc");
        List<Map<String,Object>> list = tevglPkgInfoMapper.selectSimpleList(params);
        log.debug("找到最顶层的教学包：" + id);
        log.debug("查询条件：" + params);
        log.debug("查询结果：" + list.size());
        // 考虑下需求变更引起的数据
        if (list != null && list.size() == 1 && list.get(0).get("pkgId").equals(pkgId)) {
        	return R.ok().put(Constant.R_DATA, data);
        }
        // 如果当前课堂对应教学包的，只有一个发布版的，其实没有更换的必要
        if (list != null && list.size() == 1 && list.get(0).get("pkgId").equals(tevglTchClassroomPkgInfo.getRefPkgId())) {
        	return R.ok().put(Constant.R_DATA, data);
        }
        // 如果当前登录用户不是最顶层教学包的创建者
        if (!loginUserId.equals(tevglPkgInfo.getCreateUserId()) && !isRoomReceiver) {
            params.clear();
            params.put("userId", loginUserId);
            List<TevglBookpkgTeam> tevglBookpkgTeamList = tevglBookpkgTeamMapper.selectListByMap(params);
            if (tevglBookpkgTeamList != null && tevglBookpkgTeamList.size() > 0) {
                list.stream().forEach(a -> {
                    tevglBookpkgTeamList.stream().forEach(b -> {
                        if (a.get("pkgId").equals(b.getPgkId())) {
                            data.add(a);
                        }
                    });
                });
            }
            // 如果当前课堂教学包所引用的教学包，是免费的，那么直接享有最顶层教学包所衍生的免费的子教学包
            if ("2".equals(tevglTchClassroomPkgInfoRef.getPkgLimit())) {
            	List<Map<String,Object>> collect = list.stream().filter(a -> "2".equals(a.get("pkgLimit"))).collect(Collectors.toList());
            	if (collect.size() > 0) {
            		data.addAll(collect);	
            	}
            }
        } else { // 否则是
            if (list != null && list.size() > 0) {
                data.addAll(list);
            }
        }
        data.stream().forEach(item -> {
            if (tevglTchClassroomPkgInfo.getRefPkgId().equals(item.get("pkgId"))) {
                item.put("isInUseing", true);
            }
            String pkgLimitTagName = "";
            if (!StrUtils.isNull(item.get("pkgVersion"))) {
                String dateStr = "";
                if (!StrUtils.isNull(item.get("createTime"))) {
                    dateStr = item.get("createTime").toString().substring(0, 10);
                }
                if (!StrUtils.isNull(item.get("pkgLimit")) && !loginUserId.equals(item.get("createUserId"))) {
                    if ("1".equals(item.get("pkgLimit"))) {
                        pkgLimitTagName = "【授权】  ";
                    }
                    if ("2".equals(item.get("pkgLimit"))) {
                        pkgLimitTagName = "【免费】  ";
                    }
                }
                item.put("pkgName", pkgLimitTagName + item.get("pkgName") + " ["+item.get("pkgVersion")+"]" + " " + dateStr);
            }
        });
        // 排序
        List<Map<String, Object>> datas = data.stream().sorted((h1, h2) -> h2.get("createTime").toString().compareTo(h1.get("createTime").toString())).collect(Collectors.toList());
        return R.ok().put(Constant.R_DATA, datas);
	}
	
	/**
	 * 
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 * @apiNote 
	 * 数据结构ctId: 22ddda50b1fc40baab27fa20d7991ef1 pkgId: bdd533929612421dae212d4b1950d64d
	 * 大学英语ctId: 9878329d01bf44018168583a0001d353 pkgId: c0ea18bb858547a39283053169c03270
	 * C语言程序设计 ctId: 28e20ea5ab2c4e669885b227bb7c03c3 pkgId: 1a3286faaead47d29731e1b316b7c6ed
	 */
	private R prod(String pkgId, String loginUserId) {
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (tevglPkgInfo == null) {
            return R.ok().put(Constant.R_DATA, data);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("refPkgId", pkgId);
        params.put("sidx", "t1.create_time");
        params.put("order", "desc");
        List<Map<String,Object>> list = tevglPkgInfoMapper.selectSimpleList(params);
        list = list.stream().filter(a -> !a.get("pkgId").equals(a.get("refPkgId"))).collect(Collectors.toList());
        // 如果当前登录用户最顶层教学包的创建者
        if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
            params.clear();
            params.put("userId", loginUserId);
            List<TevglBookpkgTeam> tevglBookpkgTeamList = tevglBookpkgTeamMapper.selectListByMap(params);
            if (tevglBookpkgTeamList != null && tevglBookpkgTeamList.size() > 0) {
                list.stream().forEach(a -> {
                    tevglBookpkgTeamList.stream().forEach(b -> {
                        if (a.get("pkgId").equals(b.getPgkId())) {
                            data.add(a);
                        }
                    });
                });
            }
        } else {
            if (list != null && list.size() > 0) {
                data.addAll(list);
            }
        }
        data.stream().forEach(itme -> {
            if (pkgId.equals(itme.get("pkgId"))) {
                itme.put("isInUseing", true);
            }
            if (!StrUtils.isNull(itme.get("pkgVersion"))) {
                String dateStr = "";
                if (!StrUtils.isNull(itme.get("createTime"))) {
                    dateStr = itme.get("createTime").toString().substring(0, 10);
                }
                itme.put("pkgName", itme.get("pkgName") + " ["+itme.get("pkgVersion")+"]" + " " + dateStr);
            }
        });
        return R.ok().put(Constant.R_DATA, data);
    }

	/**
	 * 递归找到最顶层的pkgId
	 * @return
	 */
	private String getPkgId(String pkgId) {
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo != null) {
			//List<String> prodList = Arrays.asList("bdd533929612421dae212d4b1950d64d", "c0ea18bb858547a39283053169c03270", "1a3286faaead47d29731e1b316b7c6ed");
			// 兼容下生产环境数据专升本的教学包数据问题，如果已经匹配到
            if (ConstantProd.prodPkgIdList.contains(pkgInfo.getRefPkgId())) {
                return pkgInfo.getRefPkgId();
            }
			if (StrUtils.isEmpty(pkgInfo.getRefPkgId()) || "3".equals(pkgInfo.getDisplay())) {
				return pkgInfo.getPkgId();
			} else {
				return getPkgId(pkgInfo.getRefPkgId());
			}
		}
		return null;
	}
	
	/**
	 * 更换教学包版本
	 * @param ctId
	 * @param pkgId 用户最新选择切换的的教学包
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = {Exception.class, BudaosException.class})
	/*@Caching(evict = {
	        @CacheEvict(value = "room_book", key = "#ctId + '::' + 'teacher'"),
	        @CacheEvict(value = "room_book", key = "#ctId + '::' + 'trainee'")
	})*/
	@CacheEvict(value = "room_book", allEntries = true)
	public R doUpgradePkg(String ctId, String pkgId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("无效的课堂记录");
		}
		// 课堂状态(1未开始2进行中3已结束)
		if ("3".equals(classroom.getState())) {
			return R.error("课堂已结束，已不允许更换。");
		}
		// 查找课堂当前使用的教学包
		TevglPkgInfo classroomPkgInfo = tevglPkgInfoMapper.selectObjectById(classroom.getPkgId());
		if (classroomPkgInfo == null) {
			return R.error("无效的教学包记录");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("抱歉，你选择的教学包，已不存在，请重试");
		}
		// 权限校验
        if (StrUtils.isEmpty(classroom.getReceiverUserId())) {
            boolean hasPermission = pkgPermissionUtils.hasPermission(pkgId, loginUserId, tevglPkgInfo.getCreateUserId());
            if (!hasPermission) {
                return R.error("没有权限，更换失败");
            }
        } else {
            boolean hasPermission = pkgPermissionUtils.hasPermissionForChangePackageVersion(classroom, tevglPkgInfo, loginUserId);
            if (!hasPermission) {
                return R.error("没有权限，更换失败");
            }
        }
        // 重点方法
        doHanleUpgradePkg(ctId, classroomPkgInfo, tevglPkgInfo);
		// 推送给此课堂成员,要求重新加载数据
		JSONObject inputData = new JSONObject();
		inputData.put("pkgId", classroomPkgInfo.getPkgId());
		inputData.put("subjectId", tevglPkgInfo.getSubjectId());
		cbRoomUtils.sendIm(ctId, "reloadresource", inputData);
		cbRoomUtils.sendIm(ctId, "reloadactivity", inputData);
		cbRoomUtils.sendIm(ctId, "reloadcloudpan", inputData);
		// 返回数据
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pkgId", classroomPkgInfo.getPkgId());
		data.put("subjectId", tevglPkgInfo.getSubjectId());
		return R.ok("更换成功").put(Constant.R_DATA, data);
	}
	
	@Transactional
    public void doHanleUpgradePkg(String ctId, TevglPkgInfo classroomPkgInfo, TevglPkgInfo tevglPkgInfo) {
		// 更新数据
		TevglPkgInfo t = new TevglPkgInfo();
		t.setPkgId(classroomPkgInfo.getPkgId());
		t.setRefPkgId(tevglPkgInfo.getPkgId());
		t.setSubjectId(tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId()) ? tevglPkgInfo.getBaseSubject() : tevglPkgInfo.getSubjectId());
		t.setPkgResCount(tevglPkgInfo.getPkgResCount());
		tevglPkgInfoMapper.update(t);
		// 更新学员是否可见表的数据
		doUpdateTraineeChapterVisibleDatas(classroomPkgInfo.getPkgId(), t.getSubjectId());
		// 更新教学包活动关系表的数据
		doUpdatePkgActRelationDatas(ctId, classroomPkgInfo.getPkgId(), classroomPkgInfo.getRefPkgId(), tevglPkgInfo.getPkgId());
		log.debug("========================================================== 更换教学包版本时，更新refPkgId的值 begin ==========================================================");
		// 更新教学包的云盘的数据，refPkgId的值
		Map<String, Object> params = new HashMap<>();
		// 当前教学包的
		params.put("pkgId", classroomPkgInfo.getPkgId());
		params.put("refDirIdIsNotNull", "Y");
		List<TcloudPanDirectory> list = tcloudPanDirectoryMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
            params.clear();
            params.put("pkgId", classroomPkgInfo.getPkgId());
            params.put("parentId", "0");
            list = tcloudPanDirectoryMapper.selectListByMap(params);
        }
		list.stream().forEach(directory -> {
			// 去被选则的这个教学包，找到
			params.clear();
			params.put("pkgId", tevglPkgInfo.getPkgId());
			params.put("parentId", "0");
			params.put("name", directory.getName());
			List<TcloudPanDirectory> dirList = tcloudPanDirectoryMapper.selectListByMap(params);
			log.debug("查询条件：" + params);
			if (dirList != null && dirList.size() > 0) {
				directory.setRefDirId(dirList.get(0).getDirId());
				tcloudPanDirectoryMapper.update(directory);
			}
			
		});
		log.debug("========================================================== 更换教学包版本时，更新refPkgId的值 end ==========================================================");
	}
	
	/**
	 * 更新数据
	 * @param classroomPkgId 当前课堂对应的教学包
	 * @param oldSelectedRefPkgId 当前课堂对应的教学包所采用的旧的教学包
	 * @param newSelectedRefPkgId 当前课堂对应的教学包所采用的新的教学包
	 */
	private void doUpdatePkgActRelationDatas(String ctId, String classroomPkgId, String oldSelectedRefPkgId, String newSelectedRefPkgId) {
		// 首先找课堂的活动
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", classroomPkgId);
		List<TevglPkgActivityRelation> classroomPkgActList = tevglPkgActivityRelationMapper.selectListByMap(params);
		// 再找到旧的引用包的活动
		params.clear();
		params.put("pkgId", oldSelectedRefPkgId);
		List<TevglPkgActivityRelation> oldSelectedRefPkgActList = tevglPkgActivityRelationMapper.selectListByMap(params);
		// 遍历二者，将旧的数据删除掉
		List<String> needDeletePaIdList = new ArrayList<String>();
		for (TevglPkgActivityRelation classroomPkgAct : classroomPkgActList) {
			for (TevglPkgActivityRelation oldSelectedRefPkgAct : oldSelectedRefPkgActList) {
				if (classroomPkgAct.getActivityId().equals(oldSelectedRefPkgAct.getActivityId())) {
					needDeletePaIdList.add(classroomPkgAct.getPaId());
				}
			}
		}
		log.debug("先需要删除的数据：" + needDeletePaIdList);
		if (needDeletePaIdList != null && needDeletePaIdList.size() > 0) {
			tevglPkgActivityRelationMapper.deleteBatch(needDeletePaIdList.stream().toArray(String[]::new));
		}
		// 建立与新引用的教学包的活动新的关系
		params.clear();
		params.put("pkgId", newSelectedRefPkgId);
		List<TevglPkgActivityRelation> newSelectedRefPkgActList = tevglPkgActivityRelationMapper.selectListByMap(params);
		if (newSelectedRefPkgActList != null && newSelectedRefPkgActList.size() > 0) {
			// 此课堂中，是否有学生已做过对应的活动
			params.clear();
			params.put("ctId", ctId);
			List<TevglActivityVoteQuestionnaireTraineeAnswer> activityList1 = tevglActivityVoteQuestionnaireTraineeAnswerMapper.selectListByMap(params);
			List<TevglActivityBrainstormingTraineeAnswer> activityList2 = tevglActivityBrainstormingTraineeAnswerMapper.selectListByMap(params);
			List<TepExamineDynamicPaper> activityList4 = tepExamineDynamicPaperMapper.selectListByMap(params);
			List<TevglPkgActivityRelation> list = new ArrayList<TevglPkgActivityRelation>();
			newSelectedRefPkgActList.stream().forEach(act -> {
				String activityType = act.getActivityType();
				// 实际活动状态(0未开始1进行中2已结束)
				String activityState = "0";
				switch (activityType) {
				case "1":
					boolean flag1 = activityList1.stream().anyMatch(a -> a.getActivityId().equals(act.getActivityId()));
					if (flag1) {
						activityState = "2";
					}
					break;
				case "2":
					boolean flag2 = activityList2.stream().anyMatch(a -> a.getActivityId().equals(act.getActivityId()));
					if (flag2) {
						activityState = "2";
					}
					break;
				case "4":
					boolean flag4 = activityList4.stream().anyMatch(a -> a.getPaperId().equals(act.getActivityId()));
					if (flag4) {
						activityState = "2";
					}
					break;
				default:
					break;
				}
				log.debug("活动状态应为：" + activityState);
				TevglPkgActivityRelation t = new TevglPkgActivityRelation();
				t.setPaId(Identities.uuid());
				t.setPkgId(classroomPkgId);
				t.setActivityId(act.getActivityId());
				t.setActivityState(activityState);
				t.setActivityType(act.getActivityType());
				if (GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS.equals(act.getActivityType())) {
					t.setGroupId(Identities.uuid());
				}
				list.add(t);
			});
			// 注意点，需要判断这些活动，在跟换版本之前，是否有学生做过，那么，切换另一个版本在切换回来，这个
			if (list.size() > 0) {
				tevglPkgActivityRelationMapper.insertBatch(list);
			}
		}
	}
	
	/**
	 * 更新学员是否可见表的数据
	 * @param pkgId 当前课堂对应的教学包主键
	 * @param subjectId 所选择更新的教学包对应的课程id
	 */
	private void doUpdateTraineeChapterVisibleDatas(String pkgId, String subjectId) {
		log.debug("========================================================== begin 更换教学包版本时，更新章节对学员是否可见 ==========================================================");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		List<TevglBookChapterVisible> chapterVisibleList = tevglBookChapterVisibleMapper.selectListByMap(params);
		if (chapterVisibleList != null && chapterVisibleList.size() > 0) {
			// 删除旧的
			List<String> ids = chapterVisibleList.stream().map(a -> a.getId()).collect(Collectors.toList());
			String[] array = ids.stream().toArray(String[]::new);
			tevglBookChapterVisibleMapper.deleteBatch(array);
			// 重新生成新的
			params.clear();
			params.put("subjectId", subjectId);
			List<TevglBookChapter> list = tevglBookChapterMapper.selectListByMapForSimple(params);
			List<TevglBookChapterVisible> vList = new ArrayList<TevglBookChapterVisible>();
			list.stream().forEach(chapterInfo -> {
				TevglBookChapterVisible cha = new TevglBookChapterVisible();
				cha.setId(Identities.uuid());
				cha.setPkgId(pkgId);
				cha.setChapterId(chapterInfo.getChapterId());
				cha.setIsTraineesVisible("Y");
				vList.add(cha);
			});
			if (vList != null && vList.size() > 0) {
				tevglBookChapterVisibleMapper.insertBatch(vList);
			}
		}
		log.debug("========================================================== end 更换教学包版本时，更新章节对学员是否可见 ==========================================================");
	}
	
	/**
	 * 修改教学包时，查询教学包信息（专用）
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewPkgInfoForUpdate(String pkgId, String loginUserId) {
		Map<String, Object> pkgInfo = tevglPkgInfoMapper.selectObjectMapById(pkgId);
		if (pkgInfo != null) {
			pkgInfo.put("subjectIdActual", pkgInfo.get("subjectId"));
			pkgInfo.put("subjectId", pkgInfo.get("subjectRef")); // 用于回显
			//pkgInfo.put("pkgLogo", uploadPathUtils.stitchingPath(pkgInfo.get("pkgLogo"), "12"));
			// 图片处理
	        Object logo = pkgInfo.get("pkgLogo");
	        pkgInfo.put("pkgLogo", uploadPathUtils.stitchingPath(logo, "12"));
		}
		return R.ok().put(Constant.R_DATA, pkgInfo);
	}
	
	/**
	 * 根据条件查询课程
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSubjectRefList(Map<String, Object> params) {
		return tevglPkgInfoMapper.selectSubjectRefList(params);
	}

	/**
	 * 获取当前教学包的直系父教学包下的直系子教学包们（即历史版本）
	 * @param pkgId
	 * @return
	 */
	@Override
	public R getHistoryPkgList(String pkgId) {
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("数据获取失败");
		}
		Map<String, Object> params = new HashMap<>();
		String refPkgId = "";
        boolean ifOrigin = StrUtils.isEmpty(tevglPkgInfo.getRefPkgId());
        if (ifOrigin) {
            refPkgId = tevglPkgInfo.getPkgId();
        } else {
            refPkgId = tevglPkgInfo.getRefPkgId();
        }
        // 可能会是最初的源教学包
        params.put("refPkgId", refPkgId);
        params.put("sidx", "t1.create_time");
        params.put("order", "desc");
        List<Map<String,Object>> list = tevglPkgInfoMapper.selectListByMapForSimple(params);
        if (ifOrigin) {
            Map<String, Object> data = new HashMap<>();
            data.put("pkgId", tevglPkgInfo.getPkgId());
            data.put("pkgName", tevglPkgInfo.getPkgName());
            data.put("pkgVersion", tevglPkgInfo.getPkgVersion());
            data.put("createTime", tevglPkgInfo.getCreateTime());
            list.add(list.size(), data);
        }
		return R.ok().put(Constant.R_DATA, list);
	}

	/**
     * 后台管理系统，移交教学包时，教学包列表数据
     *
     * @param params
     * @return
     */
	@Override
	public R queryPackageForChange(Map<String, Object> params) {
		 // 构建查询条件对象Query
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<TevglPkgInfoMgrVo> tevglPkgInfoList = tevglPkgInfoMapper.queryPackageForChange(query);
        tevglPkgInfoList.stream().forEach(pkg -> {
        	pkg.setPkgLog(uploadPathUtils.stitchingPath(pkg.getPkgLogo(), "12"));
        });
        PageUtils pageUtil = new PageUtils(tevglPkgInfoList, query.getPage(), query.getLimit());
        return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
     * 移交教学包
     *
     * @param jsonObject
     * @return
     */
    @Override
    public R doChangePackage(JSONObject jsonObject) {
    	JSONArray pkgIdList = jsonObject.getJSONArray("pkgIdList");
        String traineeId = jsonObject.getString("traineeId");
        if (StrUtils.isEmpty(traineeId) || pkgIdList == null || pkgIdList.size() == 0) {
            return R.error("必传参数为空");
        }
        TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
        if (traineeInfo == null) {
            return R.error("该被选用户没有网站账号，无法移交");
        }
        // 先查出这个教学包信息
        Map<String, Object> map = new HashMap<>();
        map.put("pkgIdArray", pkgIdList);
        List<TevglPkgInfoMgrVo> pkgList = tevglPkgInfoMapper.queryPackageForChange(map);
        if (pkgList == null || pkgList.isEmpty()) {
            return R.ok("本次操作，实际上没有能移交的教学包！");
        }
        // 清除冗余数据，后面需要再次授权
        map.clear();
        map.put("pkgIdArray", pkgIdList);
        map.put("userId", traineeId);
        List<String> teamIdList = tevglBookpkgTeamMapper.selectTeamIdListByMap(map);
        if (teamIdList != null && teamIdList.size() > 0) {
        	tevglBookpkgTeamMapper.deleteBatch(teamIdList.stream().toArray(String[]::new));
        	map.clear();
        	map.put("teamIds", teamIdList);
        	List<String> detailIdList = tevglBookpkgTeamDetailMapper.selectDetailIdListByMap(map);
        	if (detailIdList != null && detailIdList.size() > 0) {
        		tevglBookpkgTeamDetailMapper.deleteBatch(detailIdList.stream().toArray(String[]::new));
        	}
        }
        if (pkgList != null && pkgList.size() > 0) {
        	List<TevglPkgInfo> updateList = new ArrayList<>();
        	List<String> pkgIds = new ArrayList<>();
        	for (TevglPkgInfoMgrVo tevglPkgInfo : pkgList) {
        		pkgIds.add(tevglPkgInfo.getPkgId());
                // 如果当前接收者与创建者一致，置空该值
                if (traineeId.equals(tevglPkgInfo.getCreateUserId())) {
                    TevglPkgInfo t = new TevglPkgInfo();
                    t.setPkgId(tevglPkgInfo.getPkgId());
                    t.setReceiverUserId(null);
                    updateList.add(t);
                    continue;
                }
                TevglPkgInfo t = new TevglPkgInfo();
                t.setPkgId(tevglPkgInfo.getPkgId());
                t.setReceiverUserId(traineeId);
                updateList.add(t);
            }
            // 查出这些教学包的衍生出去的直系教学包，也需要更新下值
        	doUpdateChildrenDatas(pkgIds, traineeId, updateList);
        	if (!updateList.isEmpty()) {
                tevglPkgInfoMapper.batchUpdateReceiverUserIdByCaseWhen(updateList);
            }
            // 往前端系统消息表中插入记录,提示用户
            String time = DateUtils.getNowTimeStamp();
            List<TevglSiteSysMsg> msgList = new ArrayList<>();
            pkgList.stream().forEach(item -> {
                if (StrUtils.isEmpty(item.getReceiverUserId()) && !traineeId.equals(item.getCreateUserId())) {
                    TevglSiteSysMsg msg = new TevglSiteSysMsg();
                    msg.setMsgId(Identities.uuid());
                    msg.setMsgType(SiteSysMsgTypeEnum.MST_TYPE_110_PKG_TRANSFER.getCode());
                    msg.setToTraineeId(item.getCreateUserId());
                    msg.setMsgTitle("【" + item.getPkgName() + "】 教学包被移交了！");
                    msg.setReadState("N");
                    msg.setCreateUserId("0");
                    msg.setCreateTime(time);
                    msg.setState("Y");
                    msg.setPkgId(item.getPkgId());
                    msg.setToTraineeId(item.getCreateUserId());
                    msg.setMsgContent("你的教学包【" + item.getPkgName() + "】 已被系统管理员移交给其它老师了！");
                    msgList.add(msg);
                }
                if (StrUtils.isNotEmpty(item.getReceiverUserId()) && !traineeId.equals(item.getReceiverUserId())) {
                    TevglSiteSysMsg msg = new TevglSiteSysMsg();
                    msg.setMsgId(Identities.uuid());
                    msg.setMsgType(SiteSysMsgTypeEnum.MST_TYPE_110_PKG_TRANSFER.getCode());
                    msg.setToTraineeId(item.getCreateUserId());
                    msg.setMsgTitle("【" + item.getPkgName() + "】 教学包被移交了！");
                    msg.setReadState("N");
                    msg.setCreateUserId("0");
                    msg.setCreateTime(time);
                    msg.setState("Y");
                    msg.setPkgId(item.getPkgId());
                    msg.setToTraineeId(item.getReceiverUserId());
                    msg.setMsgContent("你接管的教学包【" + item.getPkgName() + "】 已被系统管理员移交给其它老师了！");
                    msgList.add(msg);
                }
            });
            if (!msgList.isEmpty()) {
                tevglSiteSysMsgMapper.insertBatch(msgList);
            }
        }
        return R.ok("移交成功");
    }
    
    private void doUpdateChildrenDatas(List<String> pkgIds, String traineeId, List<TevglPkgInfo> updateList) {
    	if (pkgIds == null || pkgIds.size() == 0) {
    		return;
    	}
    	Map<String, Object> params = new HashMap<>();
        params.put("refPkgIds", pkgIds);
        //params.put("releaseStatus", "Y");
        params.put("sidx", "t1.create_time");
        params.put("order", "desc");
        List<Map<String,Object>> selectSimpleList = tevglPkgInfoMapper.selectListByMapForSimple(params);
        if (selectSimpleList != null && selectSimpleList.size() > 0) {
        	for (Map<String, Object> tevglPkgInfo : selectSimpleList) {
                // 如果当前接收者与创建者一致，置空该值
                if (traineeId.equals(tevglPkgInfo.get("createUserId"))) {
                    TevglPkgInfo t = new TevglPkgInfo();
                    t.setPkgId(tevglPkgInfo.get("pkgId").toString());
                    t.setReceiverUserId(null);
                    updateList.add(t);
                    continue;
                }
                TevglPkgInfo t = new TevglPkgInfo();
                t.setPkgId(tevglPkgInfo.get("pkgId").toString());
                t.setReceiverUserId(traineeId);
                updateList.add(t);
            }
        }
    }

}
