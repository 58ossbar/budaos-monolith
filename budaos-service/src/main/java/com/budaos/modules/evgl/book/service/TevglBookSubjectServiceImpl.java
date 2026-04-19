package com.budaos.modules.evgl.book.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.*;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.common.PkgUtils;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.book.api.TevglBookSubperiodService;
import com.budaos.modules.evgl.book.domain.*;
import com.budaos.modules.evgl.book.persistence.*;
import com.budaos.modules.evgl.book.vo.TevglBookSubjectSelectVo;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeFavority;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeFavorityMapper;
import com.budaos.modules.evgl.pkg.domain.*;
import com.budaos.modules.evgl.pkg.persistence.*;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> Title: 课程教材</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglBookSubjectServiceImpl implements TevglBookSubjectService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookSubjectServiceImpl.class);
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private DictUtil dictUtil;
	@Autowired
	private TevglBookSubperiodService tevglBookSubperiodService;
	@Autowired
	private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	@Autowired
	private TevglBookMajorMapper tevglBookMajorMapper;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TmeduMeFavorityMapper tmeduMeFavorityMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglBookChapterVisibleMapper tevglBookChapterVisibleMapper;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglBookSubject> tevglBookSubjectList = tevglBookSubjectMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglBookSubjectList, "createUserId", "updateUserId");
		convertUtil.convertOrgId(tevglBookSubjectList, "orgId");
		if (tevglBookSubjectList.size() > 0) {
			tevglBookSubjectList.forEach(subject -> {
				subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo()); // 图片处理
			});
		}
		PageUtils pageUtil = new PageUtils(tevglBookSubjectList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglBookSubjectList = tevglBookSubjectMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglBookSubjectList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglBookSubjectList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglBookSubject
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglBookSubject tevglBookSubject) throws BudaosException {
		/*String id = Identities.uuid();
		tevglBookSubject.setSubjectId(id);
		if(StrUtils.isEmpty(tevglBookSubject.getCreateUserId())) {
			tevglBookSubject.setCreateUserId(serviceLoginUtil.getLoginUserId());
		}
		tevglBookSubject.setCreateTime(DateUtils.getNowTimeStamp());
		//tevglBookSubject.setState("Y");
		ValidatorUtils.check(tevglBookSubject);
		if(StrUtils.isEmpty(tevglBookSubject.getMajorId())) {
			return R.error("职业课程路径不能为空！");
		}
		TevglBookMajor tevglBookMajor = tevglBookMajorMapper.selectObjectById(tevglBookSubject.getMajorId());
		tevglBookSubject.setOrgId(tevglBookMajor.getOrgId());
		tevglBookSubjectMapper.insert(tevglBookSubject);
		// 如果上传了资源文件
		String attachId = tevglBookSubject.getAttachId();
		if (attachId != null && !"undefined".equals(attachId) && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, id, "1", "10");
		}
		// 保存职业路径和课程的关系
		if (tevglBookSubject.getMajorIdList() != null && tevglBookSubject.getMajorIdList().size() > 0 && tevglBookSubject.getMajorIdList() != null) {
			tevglBookSubject.getMajorIdList().forEach(a -> {
				TevglBookSubperiod t = new TevglBookSubperiod();
				t.setSubperiodId(Identities.uuid());
				t.setSubjectId(tevglBookSubject.getSubjectId());
				t.setMajorId(tevglBookSubject.getMajorId());
				t.setTerm(StrUtils.isEmpty(tevglBookSubject.getTerm()) ? "1" : tevglBookSubject.getTerm()); // 1为第一学期
				t.setSubjectProperty(StrUtils.isEmpty(tevglBookSubject.getSubjectProperty()) ? "1" : tevglBookSubject.getSubjectProperty()); // 1为必修
				tevglBookSubperiodMapper.insert(t);
			});
		}
		return R.ok().put("subjectId", tevglBookSubject.getSubjectId());*/
		if (StrUtils.isEmpty(tevglBookSubject.getMajorId())) {
            return R.error("职业课程路径不能为空！");
        }
        String uuid = Identities.uuid();
        tevglBookSubject.setSubjectId(uuid);
        tevglBookSubject.setCreateUserId(serviceLoginUtil.getLoginUserId());
        tevglBookSubject.setCreateTime(DateUtils.getNowTimeStamp());
        tevglBookSubjectMapper.insert(tevglBookSubject);
        // 如果上传了资源文件
        String attachId = tevglBookSubject.getAttachId();
        if (attachId != null && !"undefined".equals(attachId) && !"".equals(attachId)) {
            tsysAttachService.updateAttach(attachId, uuid, "1", "10");
        }
        // 保存职业路径和课程的关系
        String majorId = tevglBookSubject.getMajorId(); // 前端，多个的话，会用英文逗号分隔
        if (StrUtils.isNotEmpty(majorId)) {
            String[] majorIdArray = majorId.split(",");
            for (int i = 0; i < majorIdArray.length; i++) {
                TevglBookSubperiod t = new TevglBookSubperiod();
                t.setSubperiodId(Identities.uuid());
                t.setSubjectId(tevglBookSubject.getSubjectId());
                t.setMajorId(majorIdArray[i]);
                t.setTerm(StrUtils.isEmpty(tevglBookSubject.getTerm()) ? "1" : tevglBookSubject.getTerm()); // 1为第一学期
                t.setSubjectProperty(StrUtils.isEmpty(tevglBookSubject.getSubjectProperty()) ? "1" : tevglBookSubject.getSubjectProperty()); // 1为必修
                tevglBookSubperiodMapper.insert(t);
            }
        }
        return R.ok().put("subjectId", tevglBookSubject.getSubjectId());
	}
	/**
	 * 修改
	 * @param tevglBookSubject
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglBookSubject tevglBookSubject) throws BudaosException {
		/*if(StrUtils.isEmpty(tevglBookSubject.getUpdateUserId())) {
			tevglBookSubject.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		}
		tevglBookSubject.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglBookSubject);
		tevglBookSubjectMapper.update(tevglBookSubject);
		// 如果上传了资源文件
		String attachId = tevglBookSubject.getAttachId();
		if (attachId != null && !"undefined".equals(attachId) && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglBookSubject.getAttachId(), "0", "10");
		}
		// 保存职业路径和课程的关系
		if (tevglBookSubject.getMajorIdList() != null && tevglBookSubject.getMajorIdList().size() > 0 && tevglBookSubject.getMajorIdList() != null) {
			// 先删除课程教材与职业课程路径的关系
			Map<String, Object> map = new HashMap<>();
			map.put("subjectId", tevglBookSubject.getSubjectId());
			List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
			if (list.size() > 0) {
				list.forEach(a -> {
					tevglBookSubperiodMapper.delete(a.getSubperiodId());
				});
			}
			// 保存
			tevglBookSubject.getMajorIdList().forEach(a -> {
				TevglBookSubperiod t = new TevglBookSubperiod();
				t.setSubperiodId(Identities.uuid());
				t.setSubjectId(tevglBookSubject.getSubjectId());
				t.setMajorId(tevglBookSubject.getMajorId());
				t.setTerm(StrUtils.isEmpty(tevglBookSubject.getTerm()) ? "1" : tevglBookSubject.getTerm()); // 1为第一学期
				t.setSubjectProperty(StrUtils.isEmpty(tevglBookSubject.getSubjectProperty()) ? "1" : tevglBookSubject.getSubjectProperty()); // 1为必修
				tevglBookSubperiodMapper.insert(t);
			});
		}
		return R.ok();*/
		if (StrUtils.isEmpty(tevglBookSubject.getMajorId())) {
            return R.error("职业课程路径不能为空！");
        }
        tevglBookSubject.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglBookSubject.setUpdateTime(DateUtils.getNowTimeStamp());
        ValidatorUtils.check(tevglBookSubject);
        tevglBookSubjectMapper.update(tevglBookSubject);
        // 如果上传了资源文件
        String attachId = tevglBookSubject.getAttachId();
        if (attachId != null && !"undefined".equals(attachId) && !"".equals(attachId)) {
            tsysAttachService.updateAttach(attachId, tevglBookSubject.getAttachId(), "0", "10");
        }
        // 保存职业路径和课程的关系
        String majorId = tevglBookSubject.getMajorId(); // 前端，多个的话，会用英文逗号分隔
        if (StrUtils.isNotEmpty(majorId)) {
            // 先删除课程教材与职业课程路径的关系
            Map<String, Object> map = new HashMap<>();
            map.put("subjectId", tevglBookSubject.getSubjectId());
            List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
            if (list.size() > 0) {
                list.forEach(a -> {
                    tevglBookSubperiodMapper.delete(a.getSubperiodId());
                });
            }
            // 再保存
            String[] majorIdArray = majorId.split(",");
            for (int i = 0; i < majorIdArray.length; i++) {
                TevglBookSubperiod t = new TevglBookSubperiod();
                t.setSubperiodId(Identities.uuid());
                t.setSubjectId(tevglBookSubject.getSubjectId());
                t.setMajorId(majorIdArray[i]);
                t.setTerm(StrUtils.isEmpty(tevglBookSubject.getTerm()) ? "1" : tevglBookSubject.getTerm()); // 1为第一学期
                t.setSubjectProperty(StrUtils.isEmpty(tevglBookSubject.getSubjectProperty()) ? "1" : tevglBookSubject.getSubjectProperty()); // 1为必修
                tevglBookSubperiodMapper.insert(t);
            }
        }
        return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglBookSubjectMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglBookSubjectMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		// 课程教材
		Map<String, Object> data = new HashMap<>();
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(id);
		if (subject == null) {
			return R.ok().put(Constant.R_DATA, new TevglBookSubject());
		}
		subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo()); // 图片处理
		data.put("subject", subject);
		// 课程教材与职业路径的关系
		Map<String, Object> map = new HashMap<>();
		map.put("subjectId", subject.getSubjectId());
		List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
		List<String> subperiodIds = new ArrayList<>();
		if (list.size() > 0) {
			list.forEach(a -> {
				subperiodIds.add(a.getSubperiodId());
			});
		}
		map.clear();
		map.put("subperiodIds", subperiodIds);
		List<TevglBookMajor> majorList = tevglBookMajorMapper.selectListByMap2(map);
		data.put("majorList", majorList);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * <p>根据课程ID,获取具有层次机构的树形数据</p> （注意，已有优化版，详见：getBookForRoomPage方法或getBookForPkgPage）
	 * @author huj
	 * @data 2019年7月5日
	 * @return
	 */
	public Map<String, Object> getBook(String ctId, String pkgId, String subjectId, String chapterName, @RequestParam Map<String, Object> configuration) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的教学包");
		}
		String loginUserId = (String) configuration.get("loginUserId"); // 当前登录用户
		Map<String, Object> subjectInfo = new HashMap<>();
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (subject == null) {
			return subjectInfo;
		}
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		List<Map<String, Object>> activityList = tevglPkgActivityRelationMapper.selectSimpleListMap(params);
		subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo());
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sidx", "order_num");
		map.put("order", "asc");
		map.put("state", "Y");
		map.put("subjectId", subjectId); // 课程ID
		// 课堂详情页面里才需要
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			map.put("pkgId", pkgId);
		}
		// 查出该书所有的章节
		List<Map<String,Object>> list = tevglBookChapterMapper.selectSimpleListMap(map);
		subjectInfo.put("chapterNum", list.size());
		// 如果是在教学包页面里
		if (StrUtils.isEmpty(ctId)) {
			//tevglBookpkgTeamMapper.se
		}
		// 处理当前登录用户所有的章节权限
		//configuration.put("subjectId", subjectId);
		//handleThisUserChapterPermission(subject.getCreateUserId(), list, configuration);
		// 教学包对应的来源教学包
		TevglPkgInfo refPkg = tevglPkgInfoMapper.selectObjectById(tevglPkgInfo.getRefPkgId());
		// 是否为共建者
		boolean isTogetherBuild = false;
		// 如果为创建者
		if (loginUserId.equals(subject.getCreateUserId())) {
			subjectInfo.put("isCreator", true);
			subjectInfo.put("isTogetherBuild", false);
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
                subjectInfo.put("hasPermission", false);
                // 但是
                // 如果是在课堂里面，且
                if (StrUtils.isNotEmpty(ctId)) {
                	if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
                		subjectInfo.put("hasPermission", true);	
                	}
                } else { // 否则是在教学包页面
                	if ("N".equals(tevglPkgInfo.getReleaseStatus())) {
                		subjectInfo.put("hasPermission", true);
                		if (refPkg != null && "Y".equals(refPkg.getReleaseStatus())) {
                			subjectInfo.put("hasPermission", false);	
                		}
                	}
                }
            } else {
                subjectInfo.put("hasPermission", true);
            }
		} else {
			log.debug("共建者");
			Map<String, Object> ps = new HashMap<String, Object>();
			ps.put("subjectId", subjectId);
			ps.put("userId", loginUserId);
			List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(ps);
			if (teamList != null && teamList.size() > 0) {
				subjectInfo.put("hasPermission", true);
				isTogetherBuild = true;
				// 来源教学包处于发布状态，不允许新增修改等操作
				if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
					subjectInfo.put("hasPermission", false);
					// 但是
	                // 如果是在课堂里面，且
	                if (StrUtils.isNotEmpty(ctId)) {
	                	if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
	                		subjectInfo.put("hasPermission", true);	
	                	}
	                } else { // 否则是在教学包页面
	                	if ("N".equals(tevglPkgInfo.getReleaseStatus())) {
	                		subjectInfo.put("hasPermission", true);
	                		if (refPkg != null && "Y".equals(refPkg.getReleaseStatus())) {
	                			subjectInfo.put("hasPermission", false);	
	                		}
	                	}
	                }
				} else {
					subjectInfo.put("hasPermission", true);	
				}
			} else {
				subjectInfo.put("hasPermission", false);
			}
			// 如果是在课堂里面，用的还是免费的教学包，也仍为有设置学员不可见的权限
			if (StrUtils.isNotEmpty(ctId) && tevglTchClassroom != null && loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
				if ("2".equals(refPkg.getPkgLimit())) {
					isTogetherBuild = true;
				}
			}
			subjectInfo.put("isTogetherBuild", isTogetherBuild);
			subjectInfo.put("isCreator", false);
		}
		log.debug("当前登录用户是否为课程创建者：" + loginUserId.equals(subject.getCreateUserId()));
		log.debug("当前登录用户是否为课程共建者：" + isTogetherBuild);
		List<String> chapterIdList = getChapterIdList(loginUserId, pkgId);
		// 获取构建好层次后的数据
		List<Map<String,Object>> children = buildBook2(ctId, pkgId, tevglPkgInfo.getRefPkgId(), subject.getSubjectId(), list, 0, subject.getCreateUserId(), 
				loginUserId, true, isTogetherBuild, tevglPkgInfo.getDisplay(), tevglPkgInfo.getReleaseStatus(), refPkg == null ? "N" : refPkg.getReleaseStatus(),
						activityList, chapterIdList);
		// 处理序号
		handleSortNum(children);
		// 是否节点过滤
		if (StrUtils.isNotEmpty(chapterName)) {
			children = filter(children, chapterName);	
		}
		subjectInfo.put("children", children == null ? new ArrayList<>() : children);
		// 此外，浏览量+1
		TevglBookSubject t = new TevglBookSubject();
		t.setSubjectId(subjectId);
		t.setViewNum(1);
		tevglBookSubjectMapper.updateNum(t);
		
		return subjectInfo;
	}
	
	/**
	 * 小程序目前使用的这个
	 * @param subjectId
	 * @param queryName
	 * @return
	 */
	@Override
	public Map<String, Object> getBook2(String ctId, String pkgId, String subjectId, String queryName, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId)) {
			return R.error("必传参数为空");
		}
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (subject == null) {
			return R.error("无效的课程");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的教学包");
		}
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		List<Map<String, Object>> activityList = tevglPkgActivityRelationMapper.selectSimpleListMap(params);
		Map<String, Object> subjectInfo = new HashMap<>();
		// 填充信息
		subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo());
		subjectInfo.put("subjectId", subject.getSubjectId());
		subjectInfo.put("subjectName", subject.getSubjectName());
		subjectInfo.put("subjectAuthor", subject.getSubjectAuthor());
		subjectInfo.put("subjectDesc", subject.getSubjectDesc());
		subjectInfo.put("subjectLogo", subject.getSubjectLogo());
		subjectInfo.put("type", "subject");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sidx", "order_num");
		map.put("order", "asc");
		map.put("state", "Y");
		map.put("subjectId", subjectId); // 课程ID
		// 课堂详情页面里才需要
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			map.put("pkgId", pkgId);	
		}
		// 查出该书所有的章节
		List<Map<String,Object>> allList = tevglBookChapterMapper.selectSimpleListMap(map);
		// 教学包对应的来源教学包
        TevglPkgInfo refPkg = tevglPkgInfoMapper.selectObjectById(tevglPkgInfo.getRefPkgId());
		// 是否为共建者
		boolean isTogetherBuild = false;
		// 如果为创建者
		if (loginUserId.equals(subject.getCreateUserId())) {
			subjectInfo.put("isCreator", true);
            subjectInfo.put("isTogetherBuild", false);
            if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
                subjectInfo.put("hasPermission", false);
                // 但是
                // 如果是在课堂里面，且
                if (StrUtils.isNotEmpty(ctId)) {
                    if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
                        subjectInfo.put("hasPermission", true);
                    }
                } else { // 否则是在教学包页面
                    if ("N".equals(tevglPkgInfo.getReleaseStatus())) {
                        subjectInfo.put("hasPermission", true);
                        if (refPkg != null && "Y".equals(refPkg.getReleaseStatus())) {
                            subjectInfo.put("hasPermission", false);
                        }
                    }
                }
            } else {
                subjectInfo.put("hasPermission", true);
            }
		} else {
			log.debug("共建者");
            Map<String, Object> ps = new HashMap<String, Object>();
            ps.put("subjectId", subjectId);
            ps.put("userId", loginUserId);
            List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(ps);
            if (teamList != null && teamList.size() > 0) {
                subjectInfo.put("hasPermission", true);
                isTogetherBuild = true;
                // 来源教学包处于发布状态，不允许新增修改等操作
                if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
                    subjectInfo.put("hasPermission", false);
                    // 但是
                    // 如果是在课堂里面，且
                    if (StrUtils.isNotEmpty(ctId)) {
                        if (tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
                            subjectInfo.put("hasPermission", true);
                        }
                    } else { // 否则是在教学包页面
                        if ("N".equals(tevglPkgInfo.getReleaseStatus())) {
                            subjectInfo.put("hasPermission", true);
                            if (refPkg != null && "Y".equals(refPkg.getReleaseStatus())) {
                                subjectInfo.put("hasPermission", false);
                            }
                        }
                    }
                } else {
                    subjectInfo.put("hasPermission", true);
                }
            } else {
                subjectInfo.put("hasPermission", false);
            }
            // 如果是在课堂里面，用的还是免费的教学包，也仍为有设置学员不可见的权限
 			if (StrUtils.isNotEmpty(ctId) && tevglTchClassroom != null && loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
 				if ("2".equals(refPkg.getPkgLimit())) {
 					isTogetherBuild = true;
 				}
 			}
            subjectInfo.put("isTogetherBuild", isTogetherBuild);
            subjectInfo.put("isCreator", false);
		}
		List<String> chapterIdList = getChapterIdList(loginUserId, pkgId);
		// 获取构建好层次后的数据
		List<Map<String,Object>> children = buildBook2(ctId, pkgId, tevglPkgInfo.getRefPkgId(), subjectId, allList, 0, subject.getCreateUserId(), loginUserId, true, isTogetherBuild, tevglPkgInfo.getDisplay(), tevglPkgInfo.getReleaseStatus(), refPkg == null ? "N" :refPkg.getReleaseStatus(), activityList, chapterIdList);
		// 处理序号
		handleSortNum(children);
		// 再次处理
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		buildLine(children, resultList, queryName);
		// 添加并返回数据
		subjectInfo.put("children", resultList);
		return subjectInfo;
	}
	
	/**
	 * 获取用户的此教学包对应教材的章节权限
	 * @param loginUserId
	 * @param pkgId
	 * @return
	 */
	private List<String> getChapterIdList(String loginUserId, String pkgId) {
		List<String> chapterIdList = new ArrayList<>();
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return chapterIdList;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("pgkId", pkgId);
		map.put("userId", loginUserId);
		List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(map);
		if (list == null || list.size() == 0) {
			log.debug("教学包["+pkgId+"]中，" + "用户["+loginUserId+"]，" + "主表没有权限记录");
			return chapterIdList;
		}
		String teamId = list.get(0).getTeamId();
		map.clear();
		map.put("teamId", teamId);
		map.put("userId", loginUserId);
		List<TevglBookpkgTeamDetail> tevglBookpkgTeamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(map);
		if (tevglBookpkgTeamDetailList == null || tevglBookpkgTeamDetailList.size() == 0) {
			return chapterIdList;
		}
		chapterIdList = tevglBookpkgTeamDetailList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
		return chapterIdList;
	}
	
	/**
	 * 处理序号
	 * @param list
	 */
	public void handleSortNum(List<Map<String, Object>> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		int index = 1;
		// 找到根节点
		for (Map<String, Object> node : list) {
			node.put("serial", index);
			deep(node, node);
			node.put("chapterName", +index+" "+node.get("chapterName"));
			index ++;
		}
	}

	/**
	 * 递归处理序号
	 * @param currNode 当前节点
	 * @param parentNode 父节点
	 */
	private void deep(Map<String, Object> currNode, Map<String, Object> parentNode) {
		int index = 0;
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> nodeList = (List<Map<String, Object>>) currNode.get("children");
		if (nodeList != null && nodeList.size() > 0) {
			for (Map<String, Object> node : nodeList) {
				index ++;
				node.put("serial", parentNode.get("serial")+"."+index);	
				node.put("chapterName", parentNode.get("serial")+"."+index+" "+node.get("chapterName"));
				deep(node, node);
				
			}	
		}
	}
	
	/**
	 * 递归构建章节集合数据
	 * @param sourceData
	 * @param resultList
	 */
	private void buildLine(List<Map<String,Object>> sourceData, List<Map<String,Object>> resultList, String queryName) {
		if (sourceData == null || sourceData.size() == 0) {
			return ;
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
			List<Map<String,Object>> list = (List<Map<String, Object>>) sourceData.get(i).get("children");
			if (list != null && list.size() > 0) {
				// 递归
				buildLine(list, resultList, queryName);
				// 之后清空冗余的
				sourceData.get(i).put("children", null);
			}
		}
	}
	
	/**
	 * 过滤节点
	 * @param allList
	 * @param chapterName
	 * @return
	 */
	private List<Map<String, Object>> filter(List<Map<String, Object>> allList, String chapterName) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		List<Map<String, Object>> newArrayList = new ArrayList<Map<String,Object>>();
		allList.forEach(obj -> {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> children = (List<Map<String, Object>>) obj.get("children");
			List<Map<String, Object>> subs = filter(children, chapterName);
			if (isMatching(obj, chapterName)) {
				newArrayList.add(obj);
			} else if (subs != null && subs.size() > 0) {
				obj.put("children", subs);
				newArrayList.add(obj);
			}
		});
		return newArrayList;
	}
	
	private boolean isMatching(Map<String, Object> obj, String queryName) {
		String chapterName = (String)obj.get("chapterName");
		if (chapterName.indexOf(queryName) > -1) {
			return true;
		}
		return false;
	}

	/**
	 * <p>递归节点</p>
	 * @author huj
	 * @data 2019年7月8日
	 * @param parentId 父ID
	 * @param allList 带构建的数据
	 * @param level 级别
	 * @return
	 */
	public List<TevglBookChapter> buildBook(String parentId, List<TevglBookChapter> allList, int level) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		List<TevglBookChapter> parentNode = allList.stream().filter(a -> a.getParentId().equals(parentId)).collect(Collectors.toList());
		if (parentNode.size() > 0) {
			level ++; // level计算当前处于第几级
			final int level2 = level;
			parentNode.forEach(a -> {
				a.setLevel(level2);
				a.setChildren(buildBook(a.getChapterId(), allList, level2));
			});
		}
		return parentNode;
	}
	
	/**
	 * 递归构建树形数据
	 * @param pkgId 必传参数，教学包主键，此值是课堂表pkgId字段的值 备注：select pkg_id from t_evgl_tch_classroom where ct_id = ""
	 * @param refPkgId 参数pkgId对应的数据库记录中的ref_pkg_id字段的值 ，备注select ref_pkg_id from t_evgl_pkg_info where pkg_id = (select pkg_id from t_evgl_tch_classroom where ct_id = "")
	 * @param parentId 必传参数，父节点
	 * @param allList 必传参数，课程所有章节集合
	 * @param level 必传参数，节点处于的层级，默认请传0
	 * @param subjectCreateUserId 必传参数，课程的创建者
	 * @param loginUserId 必传参数，当前登录用户
	 * @param queryPermission 可选参数，{@link Boolean} 布尔值，如果传值为true、会去判断每个章节，非创建者时，当前登录用户是否拥有此章节的权限
	 * @param isTogetherBuild 是否为共建者，当登录用户非课程创建者时
	 * @param display 最上面pkgId查出来的记录对应的display字段值
	 * @param releaseStatus 教学包的发布状态
	 * @param refPkgReleaseStatus 教学包对应来源教学包的发布状态
	 * @param tevglBookpkgTeamDetailList 共建者时的章节权限
	 * @return
	 */
	private List<Map<String, Object>> buildBook2(String ctId, String pkgId, String refPkgId, String parentId, List<Map<String, Object>> allList, 
			int level, String subjectCreateUserId, String loginUserId, boolean queryPermission, boolean isTogetherBuild, String display, 
			String releaseStatus, String refPkgReleaseStatus, List<Map<String, Object>> activityList,
			List<String> tevglBookpkgTeamDetailList) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		List<Map<String, Object>> nodeList = new ArrayList<Map<String,Object>>();
		// 如果是创建者
		if (subjectCreateUserId.equals(loginUserId)) {
			nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
		} else { 
			if (isTogetherBuild) { // 如果是共建者
				nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
			} else { // 否则是学员
				nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId) && "Y".equals(a.get("isTraineesVisible"))).collect(Collectors.toList());	
			}
		}
		if (nodeList != null && nodeList.size() > 0) {
			level ++; // level计算当前处于第几级
			for (int i = 0; i < nodeList.size(); i++) {
				Map<String, Object> a = nodeList.get(i);
				// 本章节是否有活动
				boolean hasActivity = activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(a.get("chapterId")));
				if (!loginUserId.equals(subjectCreateUserId)) {
					hasActivity = activityList.stream()
							.anyMatch(activityInfo -> 
									!StrUtils.isNull(activityInfo.get("chapterId")) 
									&& activityInfo.get("chapterId").equals(a.get("chapterId"))
									&& Arrays.asList("1", "2").contains(activityInfo.get("activityState")));
				}
				a.put("hasActivity", hasActivity);
				// 标识为章节节点
				a.put("type", "chapter");
				// 当前层级
				a.put("level", level);
				// 如果是创建者,则章节全部有权限
				if (subjectCreateUserId.equals(loginUserId)) {
					if (StrUtils.isNotEmpty(refPkgId)) {
		                a.put("hasPermission", false);
		                // 但是
		                // 如果是在课堂里面，且
		                if (StrUtils.isNotEmpty(ctId)) {
		                	if (pkgId.equals(refPkgId)) {
		                		a.put("hasPermission", true);	
		                	}
		                } else { // 否则是在教学包页面
		                	if ("N".equals(releaseStatus)) {
		                		a.put("hasPermission", true);
		                	}
		                	if (StrUtils.isNotEmpty(refPkgReleaseStatus) && "Y".equals(refPkgReleaseStatus)) {
	                            a.put("hasPermission", false);
	                        }
		                }
		            } else { // 否则，表示是未发布状态的教学包
		                a.put("hasPermission", true);
		            }
				} else {
					// 如果为共建者
					if (isTogetherBuild) {
						boolean hasPermission = tevglBookpkgTeamDetailList.stream().anyMatch(td -> td.equals(a.get("chapterId")));
						if (hasPermission) {
							a.put("hasPermission", true);
							// 但是
						    if (StrUtils.isNotEmpty(refPkgId)) {
						        a.put("hasPermission", false);
						    }
						    if (StrUtils.isNotEmpty(display) && "3".equals(display)) {
						        a.put("hasPermission", true);
						    }
						    if (StrUtils.isNotEmpty(refPkgReleaseStatus) && "Y".equals(refPkgReleaseStatus)) {
						        a.put("hasPermission", false);
						    }
						} else {
							if (StrUtils.isNotEmpty(refPkgReleaseStatus) && "Y".equals(refPkgReleaseStatus)) {
						        a.put("hasPermission", false);
						    }
						}
						/*R r = pkgPermissionUtils.hasPermissionChapter(pkgId, loginUserId, a.get("chapterId").toString());
						if (r.get("code").equals(0)) {
							a.put("hasPermission", true);
							// 但是
						    if (StrUtils.isNotEmpty(refPkgId)) {
						        a.put("hasPermission", false);
						    }
						    if (StrUtils.isNotEmpty(display) && "3".equals(display)) {
						        a.put("hasPermission", true);
						    }
						    if (StrUtils.isNotEmpty(refPkgReleaseStatus) && "Y".equals(refPkgReleaseStatus)) {
						        a.put("hasPermission", false);
						    }
						} else {
							a.put("hasPermission", false);
						}*/
					} else { // 否则仅能查阅
						a.put("hasPermission", false);	
					}
				}
				List<Map<String, Object>> list = buildBook2(ctId, pkgId, refPkgId, (String)a.get("chapterId"), allList, level, subjectCreateUserId, loginUserId, queryPermission, isTogetherBuild, display, releaseStatus, refPkgReleaseStatus, activityList, tevglBookpkgTeamDetailList);
				if (list != null && list.size() > 0) {
					a.put("children", list);
				} else {
					a.put("children", null);
				}
			}
		}
		return nodeList;
	}
	
	/**
	 * <p>根据章节获取当前节点下的所有子节点</p>
	 * @author huj
	 * @data 2019年7月5日
	 * @param id 章节ID
	 * @param list 章节数据
	 * @return
	 */
	public List<TevglBookChapter> getChildren(String id, List<TevglBookChapter> list){
		if (list.size() == 0 || list == null || id == null || "".equals(id)) {
			return new ArrayList<>();
		}
		List<TevglBookChapter> result = new ArrayList<>();
		for (TevglBookChapter chapter : list) {
			if (id.equals(chapter.getParentId())) {
				result.add(chapter);
			}
		}
		return result;
	}
	
	/**
	 * 复制章节、资源分组、资源
	 * @param inputSubjectId 被复制教学包所随意的课程
	 * @param newSubjectId 新教学包所对应的课程
	 * @param loginUserId 当前登录用户
	 * @param newPkgId 新教学包
	 * @return
	 * @throws BudaosException
	 */
	@Override
	public R copy(String inputSubjectId, String newSubjectId, String loginUserId, String newPkgId) throws BudaosException {
		if (StrUtils.isEmpty(inputSubjectId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(newPkgId)) {
			return R.error("必传参数为空");
		}
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(inputSubjectId);
		if (subject == null) {
			return R.error("无效的课程");
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>(); 
		// 查询该源课程所有章节
        params.clear();
        params.put("subjectId", inputSubjectId);
        List<TevglBookChapter> allChapterList = tevglBookChapterMapper.selectListByMap(params);
        // 查出该源课程所有分组
        params.clear();
        params.put("subjectId", inputSubjectId);
        List<TevglPkgResgroup> allResgroupList = tevglPkgResgroupMapper.selectListByMap(params);
        // 递归
        doCopy(newSubjectId, newSubjectId, inputSubjectId, allChapterList, allResgroupList, loginUserId, newPkgId);
        Map<String, Object> data = new HashMap<>();
        data.put("pkgResCount", allChapterList.size());
		return R.ok("复制成功").put(Constant.R_DATA, data);
	}

	/**
     * 递归复制章节、分组、资源（优化版）
     * @param newSubjectId 新的课程id
     * @param newParentId 父节点
     * @param allChapterList 根据课程id查出的所有章节
     * @param allResgroupList 根据课程id查出的所有分组
     * @param loginUserId 当前登录用户
     * @param newPkgId 新的教学包id
     *
     */
    private void doCopy(String newSubjectId, String newParentId, String oldParentId, List<TevglBookChapter> allChapterList, List<TevglPkgResgroup> allResgroupList, String loginUserId, String newPkgId){
        if (StrUtils.isEmpty(newSubjectId) || StrUtils.isEmpty(newSubjectId)) {
            return;
        }
        if (allChapterList == null || allChapterList.size() == 0) {
            return;
        }
        // 所有源章节与父节点匹配
        List<TevglBookChapter> list = allChapterList.stream().filter(a -> a.getParentId().equals(oldParentId)).collect(Collectors.toList());
        if (list != null && list.size() > 0) {
            for (TevglBookChapter tevglBookChapter : list) {
                // 章节入库
                TevglBookChapter t = fillMyChapterInfo(newSubjectId, newParentId, tevglBookChapter, loginUserId);
                tevglBookChapterMapper.insert(t);
                // TODO 可见性
				TevglBookChapterVisible v = new TevglBookChapterVisible();
				v.setId(Identities.uuid());
				v.setPkgId(newPkgId);
				v.setChapterId(t.getChapterId());
				v.setIsTraineesVisible("Y");
				tevglBookChapterVisibleMapper.insert(v);
                // 匹配取出对应的分组并复制生成
                List<TevglPkgResgroup> tevglPkgResgroupList = allResgroupList.stream().filter(a -> a.getChapterId().equals(tevglBookChapter.getChapterId())).collect(Collectors.toList());
                if (tevglPkgResgroupList != null && tevglPkgResgroupList.size() > 0) {
                    for (int k = 0; k < tevglPkgResgroupList.size(); k++) {
                        // 分组入库
                        doSaveResgroup(t.getChapterId(), tevglPkgResgroupList.get(k), newPkgId, k, newSubjectId);
                    }
                } else {
                	// 默认生成[课程内容]分组
            		doCreateDefaultGroup(newPkgId, newSubjectId, t.getChapterId(), loginUserId, tevglBookChapter);
                }
                // 判断当前章节是否还有子章节，如果有，递归
                List<TevglBookChapter> childrenList = allChapterList.stream().filter(node -> node.getParentId().equals(tevglBookChapter.getChapterId())).collect(Collectors.toList());
                // 如果有子章节，递归
                if (childrenList != null && childrenList.size() > 0) {
                    doCopy(newSubjectId, t.getChapterId(), tevglBookChapter.getChapterId(), allChapterList, allResgroupList, loginUserId, newPkgId);
                }
            }
        }
    }
	
    /**
	 * 创建默认分组
	 * @param pkgId
	 * @param chapterId
	 * @param loginUserId
	 */
	private void doCreateDefaultGroup(String pkgId, String subjectId, String chapterId, String loginUserId, TevglBookChapter tevglBookChapter) {
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
		res.setResName(tevglBookChapter == null ? null : tevglBookChapter.getChapterName());
		res.setResContent(tevglBookChapter == null ? null : tevglBookChapter.getChapterContent());
		tevglPkgResMapper.insert(res);
		// 教学包的资源数+1，由于章节分组与资源一对一,直接把分组数当作资源数
		pkgUtils.plusPkgResNum(pkgId, 1);
	}
    
	/**
	 * 保存章节对应的分组,资源
	 * @param newChapterId 新的章节id
	 * @param tevglPkgResgroup 源章节的所有分组
	 * @param newPkgId 新的教学包
	 * @param sortNum 排序号
	 */
	private void doSaveResgroup(String newChapterId, TevglPkgResgroup tevglPkgResgroup, 
			String newPkgId, int sortNum, String subjectId) {
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
		//pr.setResName("新的资源喔【开发阶段】");
		// 保存资源至数据库
		tevglPkgResMapper.insert(pr);
	}
	
	/**
	 * <p>填充章节信息</p>
	 * @author huj
	 * @data 2019年7月9日
	 * @param subjectId 课程ID
	 * @param parentId 父ID
	 * @param tevglBookChapter 章节对象
	 * @return
	 */
	private TevglBookChapter fillMyChapterInfo(String subjectId, String parentId, TevglBookChapter tevglBookChapter, String loginUserId) {
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
		obj.setIsTraineesVisible("Y"); // 学员是否可见（Y/N）
		return obj;
	}

	@Override
	public R selectListByMapForMgr(@RequestParam Map<String, Object> params) {
		//构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> subjectList = tevglBookSubjectMapper.selectListByMapForMgr(query);
		convertUtil.convertDict(subjectList, "subject_type", "subjectType2"); // 课程类型(来源字典:学校，平台等)
		convertUtil.convertDict(subjectList, "subject_property", "subjectProperty"); // 课程属性(来源字典:选修，必修等)
		convertUtil.convertOrgId(subjectList, "org_id");
		if (subjectList.size() > 0) {
			subjectList.forEach(subject -> {
				//subject.put("subject_logo", budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.get("subject_logo")); // 图片处理
				// 图片处理, 使用了从字典获取的图片时，保存了文件夹和文件名称。所以处理一下
				if (StrUtils.isNotEmpty((String)subject.get("subject_logo"))) {
					int i = ((String) subject.get("subject_logo")).indexOf(budaosFieAccessPath);
					if (i == -1) {
						subject.put("subject_logo", budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.get("subject_logo")); 
					}
				}
			});
		}
		PageUtils pageUtil = new PageUtils(subjectList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * <p>前端，根据条件查询记录(关联了表)</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	@Override
	public R selectListByMapForWeb(@RequestParam Map<String, Object> map) {
		
		String pageNum = (String) map.get("pageNum"); // 当前页码
		String pageSize = (String) map.get("pageSize"); // 当前页码
		String majorId = (String) map.get("majorId"); // 专业ID
		String subjectTechnology = (String) map.get("subjectTechnology"); // 技术
		String subjectType = (String) map.get("subjectType"); // 课程类型(来源字典:学校，平台等)
		String subjectProperty = (String) map.get("subjectProperty"); // 课程属性(选修or必修)
		String createUserId = StrUtils.isNull(map.get("createUserId")) ? null : map.get("createUserId").toString(); // 是否查询本人的
		String sidx = (String) map.get("sidx");
		String order = (String) map.get("order");
		
		Map<String, Object> data = new HashMap<>(); // 实际返回的数据
		Map<String, Object> params = new HashMap<>(); // 查询条件
		
		// 技术
		if (majorId != null && !"".equals(majorId)) {
			params.put("majorId", majorId);
		}
		List<Map<String, Object>> subjectTechnologyList = tevglBookSubperiodService.selectListForEvglWeb(params);
		if (subjectTechnologyList.size() > 0 && subjectTechnologyList != null) {
			for (int i = 0; i < subjectTechnologyList.size(); i++) {
				if (subjectTechnologyList.get(i) == null) {
					subjectTechnologyList.remove(i);
				}
			}
		}
		data.put("subjectTechnologyList", subjectTechnologyList);

		// 教材
		params.put("pageNum", pageNum == null ? 1 : pageNum);
		if (pageSize != null && !"".equals(pageSize)) {
			params.put("pageSize", Integer.valueOf(pageSize));
		} else {
			params.put("pageSize", 10);
		}
		// 各种条件
		params.put("state", "Y");
		if (subjectTechnology != null && !"".equals(subjectTechnology)) {
			params.put("subjectTechnology", subjectTechnology);
		}
		if (subjectType != null && !"".equals(subjectType)) {
			params.put("subjectType", subjectType);
		}
		if (subjectProperty != null && !"".equals(subjectProperty)) {
			params.put("subjectProperty", subjectProperty);
		}
		// 排序条件处理
		if (sidx == null || "".equals(sidx)) {
			params.put("sidx", "t1.create_time desc, t1.view_num");
		} else {
			params.put("sidx", sidx);
		}
		if (order == null || "".equals(order)) {
			params.put("order", "asc");	
		} else {
			params.put("order", order);	
		}
		// TODO 注意，前端活教材栏目只查询教材而不查询课程？
		params.put("isSubjectRefNotNull", "Y");

		if(createUserId != null) {
			params.put("createUserId", createUserId);
		}
		
		//构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglBookSubject> subjectList = tevglBookSubjectMapper.selectListByMapForWeb(query);
		convertUtil.convertDict(subjectList, "subjectType", "subjectType2"); // 课程类型(来源字典:学校，平台等)
		convertUtil.convertOrgId(subjectList, "orgId");
		if (subjectList.size() > 0) {
			subjectList.forEach(subject -> {
				// 图片处理
				if (subject.getSubjectLogo() != null && !"".equals(subject.getSubjectLogo())) {
					int i = subject.getSubjectLogo().indexOf(budaosFieAccessPath);
					if (i == -1) {
						subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo());
					} else { // 说明前端录入时，选择的是静态图片，静态图片保存时带上完整的路径
						
					}
				}
			});
		}
		PageUtils pageUtil = new PageUtils(subjectList,query.getPage(),query.getLimit());
		data.put("subjectList", pageUtil);
		
		// 返回数据，以及查询条件原样返回
		return R.ok().put(Constant.R_DATA, data)
				.put("majorId", majorId)
				.put("subjectTechnology", subjectTechnology)
				.put("majorId", majorId)
				.put("subjectType", subjectType)
				.put("subjectProperty", subjectProperty);
	}

    /**
     * <p>更新阅读量、星级、收藏数、点赞数、举报数等</p>  
     * @author huj
     * @data 2019年8月14日	
     * @param map
     * @return
     */
	@Override
	public void updateNum(@RequestBody(required = false) TevglBookSubject tevglBookSubject) {
		if (tevglBookSubject != null && StrUtils.isNotEmpty(tevglBookSubject.getSubjectId())) {
			tevglBookSubjectMapper.updateNum(tevglBookSubject);	
		}
	}
	

	/**
	 * <p>重命名</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param id
	 * @param name
	 * @return
	 */
	@Override
	public R rename(@RequestParam("id") String id, @RequestParam("name") String name) {
		R r = checkIsPassRename(id, name);
		if ((Integer)r.get("code") != 0) {
			return r;
		}
		TevglBookSubject obj = new TevglBookSubject();
		obj.setSubjectId(id);
		obj.setSubjectName(name);
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(id);
		if (subject == null) {
			return R.error("重命名失败");
		}
		// 验证名称是否重复
		Map<String, Object> map = new HashMap<>();
		map.put("state", "Y"); // 状态(Y已发布N未发布)
		List<TevglBookSubject> list = tevglBookSubjectMapper.selectListByMap(map);
		if (list.size() > 0) {
			for (TevglBookSubject tevglBookSubject : list) {
				if (!obj.getSubjectId().equals(tevglBookSubject.getSubjectId())) {
					if (name.equals(tevglBookSubject.getSubjectName())) {
						return R.error("重命名失败,输入的名称已经存在");
					}	
				}
			}
		}
		tevglBookSubjectMapper.update(obj);
		return R.ok();
	}
	
	private R checkIsPassRename(String subjectId, String name) {
		if (StrUtils.isEmpty(subjectId)) {
			return R.error("参数subjectId为空");
		}
		if (StrUtils.isEmpty(name)) {
			return R.error("参数name为空");
		}
		return R.ok();
	}

	/**
	 * <p>删除教材</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @param id 教材主键
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R remove(String id, String loginUserId) {
		if ("".equals(id) || id == null) {
			return R.error("删除失败");
		}
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(id);
		if (subject == null) {
			return R.error("删除失败");
		}
		R r2 = checkPermission(subject, loginUserId);
		if ((Integer)r2.get("code") != 0) {
			return r2;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("state", "Y");
		map.put("subjectId", subject.getSubjectId());
		List<TevglBookChapter> tevglBookChapterList = tevglBookChapterMapper.selectListByMap(map);
		if (tevglBookChapterList.size() > 0) {
			return R.error("该课程下还存在章节,请先删除章节");
		}
		tevglBookSubjectMapper.delete(id);
		return R.ok();
	}

	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @param tevglBookSubject
	 * @return
	 */
	@Override
	public R updateState(TevglBookSubject tevglBookSubject) {
		if (tevglBookSubject == null || StrUtils.isEmpty(tevglBookSubject.getSubjectId())) {
			return R.error("操作失败");
		}
		tevglBookSubjectMapper.update(tevglBookSubject);
		return R.ok();
	}

	/**
	 * <p>查看课程相关信息</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @param id
	 * @return
	 */
	@Override
	public R viewForEvglWeb(@PathVariable("id") String id) {
		// 课程教材
		Map<String, Object> data = new HashMap<>();
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(id);
		if (subject == null) {
			return R.ok().put(Constant.R_DATA, new TevglBookSubject());
		}
		// 图片处理
		if (subject.getSubjectLogo() != null && !"".equals(subject.getSubjectLogo())) {
			int i = subject.getSubjectLogo().indexOf(budaosFieAccessPath);
			if (i == -1) {
				subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo());
			} else { // 说明前端录入时，选择的是静态图片，静态图片保存时带上完整的路径
				data.put("isStatic", true);
			}
		}
		data.put("subject", subject);
		// 课程教材与职业路径的关系
		if (subject.getSubjectRef() != null && !"".equals(subject.getSubjectRef())) {
			Map<String, Object> map = new HashMap<>();
			map.put("subjectId", subject.getSubjectRef()); // 活教材的该值就是课程ID
			List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
			List<String> subperiodIds = new ArrayList<>();
			if (list.size() > 0) {
				list.forEach(a -> {
					subperiodIds.add(a.getSubperiodId());
				});
			}
			map.clear();
			map.put("subperiodIds", subperiodIds);
			List<TevglBookMajor> majorList = tevglBookMajorMapper.selectListByMap2(map);
			data.put("majorList", majorList);	
		}
		return R.ok().put(Constant.R_DATA, data);
	} 

	/**
	 * <p>从字典获取活教材封面图，满足前端录入界面需要</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @return
	 */
	@Override
	public List<TsysDict> getSubjectLogo() {
		List<TsysDict> list = dictUtil.getByType("subjectLogo");
		if (list != null && list.size() > 0) {
			// 根据排序号自然顺序
			list.stream().sorted(Comparator.comparing(TsysDict::getSortNum)).collect(Collectors.toList());
			list.forEach(a -> {
				// 业务基础平台中固定了dict。暂未改动，所有
				//a.setDictUrl(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("1") + "/" + a.getDictUrl());
				a.setDictUrl(budaosFieAccessPath + "/dict/" + a.getDictUrl());
			});
		}
		return list;
	}

	@Override
	public TevglBookSubject selectObjectById(String id) {
		return tevglBookSubjectMapper.selectObjectById(id);
	}
	
	@RequestMapping("/toexport")
	public R toexport(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		String subjectId = params.get("subjectId") == null ? "" : params.get("subjectId").toString();
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(subjectId);
		if (subject == null) {
			throw new BudaosException("无效的教材");
		}
		try {
			POIFSFileSystem fs = new POIFSFileSystem();
			String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
					+ "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\""
					+ "xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\""
					+ "xmlns=\"http://www.w3.org/TR/REC-html40\">" + "<head>"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>"
					+ initWordDataById(subjectId) + "</body></html>";
			InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
			fs.createDocument(is, "WordDocument");
			response.setHeader("Content-Disposition", "attachment;filename="
					.concat(String.valueOf(URLEncoder.encode(subject.getSubjectName() + ".doc", "UTF-8"))));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/vnd.ms-word");
			fs.writeFilesystem(response.getOutputStream());
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.ok();
	}
	
	/**
	 * <p>根据ID,组织word文档内容</p>
	 * @author huj
	 * @data 2019年8月7日
	 * @param id
	 * @return
	 */
	@Override
	public String initWordDataById(@PathVariable("id") String id) {
		StringBuffer sb = new StringBuffer();
		TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(id);
		if (subject == null) {
			return sb.toString();
		}
		// 文档标题(h1居中)
		sb.append("<h1 style=\"text-align: center;\"><strong>"+subject.getSubjectName()+"</strong></h1>");
		// 遍历章节
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("parentId", subject.getSubjectId());
		map.put("state", "Y");
		map.put("sidx", "order_num");
		map.put("order", "asc");
		List<TevglBookChapter> chapters = tevglBookChapterMapper.selectListByMap(map);
		for(int i=0; i<chapters.size(); i++){
			//章节标题
			sb.append("<h2 style=\"text-align: center;\"><strong>第"+(i+1)+"章&nbsp;"+chapters.get(i).getChapterName()+"</strong></h2>");
			//章节概述
			//sb.append("<h3><strong>"+(i+1)+".1&nbsp;概述</strong></h3>");
			//概述内容 // TODO 富文本的图片值已经是保存了完整的路径，所有因该不需要替换了
			sb.append(StrUtils.toString(chapters.get(i).getChapterContent()));
			//遍历章节下面的章节
			map.put("parentId", chapters.get(i).getChapterId());
			map.put("state", "Y");
			map.put("sidx", "order_num");
			map.put("order", "asc");
			List<TevglBookChapter> list2 = tevglBookChapterMapper.selectListByMap(map);
			for(int j=0; j<list2.size(); j++){
				//章节知识点标题
				sb.append("<h3><strong>"+(i+1)+"."+(j+1)+"&nbsp;"+list2.get(j).getChapterName()+"</strong></h3>");
				//章节知识点内容
				sb.append(StrUtils.toString(list2.get(j).getChapterContent()));
				// 遍历
				map.put("parentId", list2.get(j).getChapterId());
				map.put("state", "Y");
				map.put("sidx", "order_num");
				map.put("order", "asc");
				List<TevglBookChapter> list3 = tevglBookChapterMapper.selectListByMap(map);
				if (list3.size() > 0) {
					for (int k = 0; k < list3.size(); k++) {
						sb.append("<h4><strong>"+(i+1)+"."+(j+1)+"."+(k+1)+"&nbsp;"+list3.get(k).getChapterName()+"</strong></h4>");
						sb.append(StrUtils.toString(list3.get(k).getChapterContent()));
					}
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 替换html中所有img标签中src的地址(拼接上完整的域名)
	 * @param content
	 * @return
	 */
	/*
	
	//@Value("${com.budaos.loginBackUrl}")
	private String url = "";
	
	private String replaceImgSrc(String content){	
		if(StrUtils.isEmpty(content)){
			return content;
		}
		String result = new String(content);
	    String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
	    Pattern p_image = Pattern.compile
	            (regEx_img, Pattern.CASE_INSENSITIVE);
	    Matcher m_image = p_image.matcher(content);
	    while (m_image.find()) {
	        String img = m_image.group();
	        Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
	        while (m.find()) {
	        	String str = m.group(1);
	        	result = result.replace(str,url+str);
	        }
	    }
	    return result;
	}
	*/

	/**
	 * <p>收藏</p>  
	 * @author huj
	 * @data 2019年8月15日	
	 * @param targetId 被收藏的ID
	 * @param loginUserId 当前登录用户ID
	 * @param toTraineeId 被收藏的文件的所属人ID
	 * @return
	 */
	@Override
	@GetMapping("/collect")
	public R collect(String targetId, String loginUserId, String toTraineeId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("无法获取登录信息");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("favorityType", "10");
		map.put("targetId", targetId);
		map.put("traineeId", loginUserId);
		List<TmeduMeFavority> list = tmeduMeFavorityMapper.selectListByMap(map);
		if (list.size() > 0) {
			return R.error("你已收藏该教材");
		}
		try {
			TmeduMeFavority tmeduFavority = new TmeduMeFavority();
			tmeduFavority.setFavorityId(Identities.uuid());
			tmeduFavority.setTargetId(targetId);
			tmeduFavority.setFavorityType("10");
			tmeduFavority.setTraineeId(loginUserId);
			tmeduFavority.setFavorityTime(DateUtils.getNowTimeStamp());
			tmeduMeFavorityMapper.insert(tmeduFavority);
			// 推送活教材收藏消息
		} catch (BudaosException e) {
			e.printStackTrace();
			return R.error("收藏失败");
		}
		return R.ok("收藏成功");
	}

	/**
	 * <p>取消收藏</p>  
	 * @author huj
	 * @data 2019年8月15日	
	 * @param targetId 被取消收藏的ID
	 * @param loginUserId 当前登录用户ID
	 * @param toTraineeId 被取消收藏的文件的所属人ID
	 * @return
	 */
	@Override
	public R cancelCollect(String targetId, String loginUserId, String toTraineeId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("无法获取登录信息");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("favorityType", "10");
		map.put("targetId", targetId);
		map.put("traineeId", loginUserId);
		List<TmeduMeFavority> list = tmeduMeFavorityMapper.selectListByMap(map);
		if (list.size() == 0) {
			return R.error("你尚未收藏该教材");
		}
		for (TmeduMeFavority favority : list) {
			try {
				tmeduMeFavorityMapper.delete(favority.getFavorityId());
			} catch (BudaosException e) {
				e.printStackTrace();
			}
		}
		return R.ok("收藏成功");
	}

	/**
	 * <p>获取活教材而非课程（无分页）</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @param map 参数中若isSubjectRefNull为"Y"则查询的是课程，若isSubjectRefNotNull为"Y"则查询的是活教材
	 * @return
	 */
	@Override
	public List<TevglBookSubject> getLiveSubjectList(Map<String, Object> map) {
		map.put("isSubjectRefNotNull", "Y");
		return tevglBookSubjectMapper.selectListByMapForWeb(map);
	}

	/**
	 * 权限校验
	 * @param tevglBookSubject
	 * @param loginUserId
	 * @return
	 */
	private R checkPermission(TevglBookSubject tevglBookSubject, String loginUserId) {
		if (!loginUserId.equals(tevglBookSubject.getCreateUserId())) {
			Map<String, Object> map = new HashMap<>();
			map.put("subjectId", tevglBookSubject.getSubjectId());
			map.put("userId", loginUserId);
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(map);
			if (list == null || list.size() == 0) {
				return R.error("暂无权限，操作失败");
			}
		}
		return R.ok();
	}

	/**
	 * 课程列表，注意此方法只会查询subject_ref为空的记录
	 * @param params
	 * @return
	 */
	@Override
	public R listSelectSubject(@RequestParam Map<String, Object> params) {
		List<TevglBookSubject> tevglBookSubjectList = new ArrayList<TevglBookSubject>();
		params.put("sidx", "t1.create_time");
		params.put("order", "desc");
		Query query = new Query(params);
		if (params.get("majorIds") != null && !"".equals(params.get("majorIds"))) {
			String ids = (String) params.get("majorIds");
			List<String> list = new ArrayList<>();
			String[] majorIds = ids.split(",");
			for (String string : majorIds) {
				list.add(string);
			}
			params.put("majorIds", list);
		}
		tevglBookSubjectList = tevglBookSubjectMapper.selectListByMapForCommon(query);
		if (tevglBookSubjectList != null && tevglBookSubjectList.size() > 0) {
			tevglBookSubjectList.forEach(subject -> {
				subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo()); // 图片处理
			});
		}
		List<Map<String,Object>> collect = tevglBookSubjectList.stream().map(this::converToSubjectMap).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, collect);
	}
	
	/**
	 * 取部分属性，若需要额外，自行补充
	 * @param tevglBookSubject
	 * @return
	 */
	private Map<String, Object> converToSubjectMap(TevglBookSubject tevglBookSubject){
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("subjectId", tevglBookSubject.getSubjectId());
		info.put("subjectName", tevglBookSubject.getSubjectName());
		info.put("subjectLogo", tevglBookSubject.getSubjectLogo());
		return info;
	}
	
	/**
	 * 获取具有层级结构的章节树
	 * @param subjectId
	 * @param pkgId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getTree(String subjectId, String pkgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sidx", "order_num");
		map.put("order", "asc");
		map.put("state", "Y");
		map.put("subjectId", subjectId); // 课程ID
		map.put("pkgId", pkgId); // 教学包
		List<Map<String,Object>> list = tevglBookChapterMapper.selectSimpleListMap(map);
		list.stream().forEach(node -> {
			if (StrUtils.notNull(node.get("isTraineesVisible")) && "Y".equals(node.get("isTraineesVisible"))) {
				node.put("checked", true);
			}
		});
		// 获取构建好层次后的数据
		List<Map<String,Object>> children = buildTree(subjectId, list, 0);
		// 处理序号
		handleSortNum(children);
		return children;
	}

	@Override
	public R getTreeForMgr(String subjectId) {
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
        if (tevglBookSubject == null) {
            return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String, Object> subjectInfo = new HashMap<>();
        subjectInfo.put("type", "subject");
        subjectInfo.put("subjectId", tevglBookSubject.getSubjectId());
        subjectInfo.put("chapterId", tevglBookSubject.getSubjectId());
        subjectInfo.put("chapterName", tevglBookSubject.getSubjectName());
        subjectInfo.put("parentId", tevglBookSubject.getSubjectId());
        subjectInfo.put("level", 0);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sidx", "order_num");
        map.put("order", "asc");
        map.put("state", "Y");
        map.put("subjectId", subjectId); // 课程ID
        List<Map<String,Object>> list = tevglBookChapterMapper.selectSimpleListByMapForRelease(map);
        // 获取构建好层次后的数据
        List<Map<String,Object>> children = buildTree(subjectId, list, 0);
        // 处理序号
        handleSortNum(children);
        subjectInfo.put("children", children);
        resultList.add(subjectInfo);
        return R.ok().put(Constant.R_DATA, resultList);
	}
	
	private List<Map<String, Object>> buildTree(String parentId, List<Map<String, Object>> allList, int level) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		List<Map<String, Object>> nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
		if (nodeList != null && nodeList.size() > 0) {
			level ++; // level计算当前处于第几级
			for (Map<String, Object> node : nodeList) {
				node.put("type", "chapter");
				node.put("level", level);
				node.put("children", buildTree(node.get("chapterId").toString(), allList, level));
			}
		}
		return nodeList;
	}
	
	/**
	 * 获取一本书（章节树）（课堂页面专用）
	 * @param ctId
	 * @param pkgId
	 * @param subjectId
	 * @param chapterName
	 * @param loginUserId
	 * @param queryForWx
	 * @param identity
	 * @return
	 */
    @Override
    @SysLog(value="课堂页面获取教材章节树")
    //@Cacheable(value = "room_book", key = "#ctId+'::'+#identity")
    public R getBookForRoomPage(String ctId, String pkgId, String subjectId, String chapterName, String loginUserId, boolean queryForWx, String identity) {
    	// 合法性校验
        if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
            return R.error("必传参数为空");
        }
        TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
        if (tevglTchClassroom == null) {
            return R.error("无效的课堂");
        }
		/*if (!pkgId.equals(tevglTchClassroom.getPkgId())) {
		    return R.error("参数错误");
		}*/
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (tevglPkgInfo == null) {
            return R.error("无效的教学包");
        }
        // 更换教学包版本后，前端可能传的值不对，直接取教学包对应的教材
        //TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
        TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(tevglPkgInfo.getSubjectId());
        if (tevglBookSubject == null) {
            return R.error("无效的教材");
        }
        // 调用方法，取教材部分属性
        Map<String, Object> subjectInfo = getSimpleSubjectInfo(tevglBookSubject);
        // 查询章节
        List<Map<String, Object>> chapterList = selectSimpleChapterListMap(subjectId, true, pkgId);
        chapterList.stream().forEach(item -> {
            item.put("ctId", tevglTchClassroom.getCtId());
        });
        subjectInfo.put("chapterNum", chapterList.size());
        // 调用方法，处理节点的操作权限，以及节点下是否有活动
        doHandleNodeFlagsForRoom(tevglTchClassroom, tevglPkgInfo, subjectInfo, loginUserId, chapterList, queryForWx);
        // 调用方法，递归构建
        //boolean filterFlag = getFilterValue(tevglTchClassroom, loginUserId); // 是否过滤掉学员不可见的章节
        boolean filterFlag = getFilterValueV2(tevglTchClassroom, loginUserId); // 是否过滤掉学员不可见的章节
        List<Map<String, Object>> children = buildBookNew(chapterList, 0, subjectInfo.get("subjectId").toString(), filterFlag);
        // 调用方法，处理序号
        handleSortNum(children);
        if (queryForWx) { // 小程序专用
            // 再次处理
            List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
            buildLine(children, resultList, chapterName);
            subjectInfo.put("children", resultList);
        } else {
            // 是否节点过滤
            if (StrUtils.isNotEmpty(chapterName)) {
                children = filter(children, chapterName);
            }
            subjectInfo.put("children", children == null ? new ArrayList<>() : children);
        }
        // 此外，浏览量+1
        TevglBookSubject t = new TevglBookSubject();
        t.setSubjectId(subjectId);
        t.setViewNum(1);
        tevglBookSubjectMapper.updateNum(t);
        return R.ok().put(Constant.R_DATA, subjectInfo);
    }
    
    /**
     * 返回true则需要过滤掉学员不可见的章节，暂不删除该方法
     * @param tevglTchClassroom
     * @param loginUserId
     * @return
     */
    @Deprecated
    private boolean getFilterValue(TevglTchClassroom tevglTchClassroom, String loginUserId) {
    	if (tevglTchClassroom == null || StrUtils.isEmpty(loginUserId)) {
    		return true;
    	}
    	// 如果是课堂创建者，不需要过滤
    	if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
    		return false;
    	}
    	// 如果是课堂助教者
    	boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && loginUserId.equals(tevglTchClassroom.getTraineeId());
    	if (isTeachingAssistant) {
    		// 如果助教角色没有设置章节学员是否可见的权限
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("ctId", tevglTchClassroom.getCtId());
    		List<String> list = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(map);
    		if (list == null || list.size() == 0) {
    			return true;
    		}
    		boolean anyMatch = list.stream().anyMatch(a -> a.equals(GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE));
    		if (anyMatch) {
                return false;
            } else {
                return true;
            }
    	}
    	return true;
    }
    
    /**
     * 返回true则需要过滤掉学员不可见的章节（课堂如果被移交了的话，原创建者也需要过滤掉数据，而接收者不需要过滤掉数据）
     * @param tevglTchClassroom
     * @param loginUserId
     * @return
     */
    private boolean getFilterValueV2(TevglTchClassroom tevglTchClassroom, String loginUserId) {
    	if (tevglTchClassroom == null || StrUtils.isEmpty(loginUserId)) {
    		return true;
    	}
    	// 如果是课堂助教者
    	boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && loginUserId.equals(tevglTchClassroom.getTraineeId());
    	if (isTeachingAssistant) {
    		// 如果助教角色没有设置章节学员是否可见的权限
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("ctId", tevglTchClassroom.getCtId());
    		List<String> list = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(map);
    		if (list == null || list.size() == 0) {
    			return true;
    		}
    		boolean anyMatch = list.stream().anyMatch(a -> a.equals(GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE));
    		if (anyMatch) {
                return false;
            } else {
                return true;
            }
    	}
    	// 如果课堂没被移交
    	if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
    		// 如果是课堂创建者，不需要过滤
        	if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
        		return false;
        	}	
    	} else {
    		// 如果是课堂创建者，但此课堂已经被移交了，需要过滤
        	if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
        		return true;
        	}
        	if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
        		return false;
        	}
    	}
    	return true;
    }
    
    /**
     * 处理节点的操作权限，以及节点下是否有活动（课堂页面中专用）
     * @param tevglTchClassroom
     * @param tevglPkgInfo
     * @param subjectInfo
     * @param loginUserId
     * @param chapterList
     */
    private boolean doHandleNodeFlagsForRoom(TevglTchClassroom tevglTchClassroom, TevglPkgInfo tevglPkgInfo, Map<String, Object> subjectInfo
    		, String loginUserId, List<Map<String, Object>> chapterList, boolean queryForWx) {
        // 当前用户是否为此课堂的创建者
        boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
        // 当前用户是否为此课堂的接收者
        boolean hasRoomReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId());
        boolean isRoomReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && loginUserId.equals(tevglTchClassroom.getReceiverUserId());
        // 当前登录用户是否为课堂的助教
        boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && loginUserId.equals(tevglTchClassroom.getTraineeId());
        String pkgId = tevglPkgInfo.getPkgId();
        // 查询教学包的活动
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pkgId", pkgId);
        List<Map<String, Object>> activityList = tevglPkgActivityRelationMapper.selectSimpleListMap(params);
        // 如果这个教学包已处于发布状态，则不允许新增修改删除等操作
        subjectInfo.put("hasPermission", false);
        if (isRoomCreator || isRoomReceiver) {
        	// 是否拥有设置课外教材的权限
        	subjectInfo.put("hasSetBookVisiblePermission", true);
            // 是否拥有设置可见的权限
            subjectInfo.put("hasSetVisiblePermission", true);
            chapterList.stream().forEach(chapter -> {
                chapter.put("hasPermission", false);
                chapter.put("hasSetVisiblePermission", true);
            });
            // 如果是接收者，那么表示这个课堂已经移交给了接收者，那么原来本身的课堂创建者，需要丧失控制权
            if (isRoomCreator && hasRoomReceiver) {
            	subjectInfo.put("hasSetVisiblePermission", false);
            	subjectInfo.put("hasSetBookVisiblePermission", false);
                chapterList.stream().forEach(chapter -> {
                    chapter.put("hasPermission", false);
                    chapter.put("hasSetVisiblePermission", false);
                });
            }
        } else if (isTeachingAssistant) {
            boolean has = false;
            // 如果助教角色没有设置章节学员是否可见的权限
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ctId", tevglTchClassroom.getCtId());
            List<String> list = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(map);
            if (list == null || list.size() == 0) {
                has = false;
            } else {
                has = list.stream().anyMatch(a -> a.equals(GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE));
            }
            // 如果为助教，且课堂创建者，设置助教有此权限
            if (has) {
            	subjectInfo.put("hasSetVisiblePermission", true);
                chapterList.stream().forEach(chapter -> {
                    chapter.put("hasPermission", false);
                    chapter.put("hasSetVisiblePermission", true);
                });
            } else {
            	subjectInfo.put("hasSetVisiblePermission", false);
                // 否则认为没有操作权限
                chapterList.stream().forEach(chapter -> {
                    chapter.put("hasPermission", false);
                    chapter.put("hasSetVisiblePermission", false);
                    boolean hasActivity = activityList.stream()
                            .anyMatch(activityInfo ->
                                    !StrUtils.isNull(activityInfo.get("chapterId"))
                                            && activityInfo.get("chapterId").equals(chapter.get("chapterId"))
                                            && Arrays.asList("1", "2").contains(activityInfo.get("activityState")));
                    chapter.put("hasActivity", hasActivity);
                });
            }

        } else {
            subjectInfo.put("hasPermission", false);
            subjectInfo.put("hasSetVisiblePermission", false);
            subjectInfo.put("hasSetBookVisiblePermission", false);
            chapterList.stream().forEach(chapter -> {
                chapter.put("hasPermission", false);
                chapter.put("hasSetVisiblePermission", false);
                // 学生身份只能看到进行中、已结束的活动
                boolean hasActivity = activityList.stream()
                        .anyMatch(activityInfo ->
                                !StrUtils.isNull(activityInfo.get("chapterId"))
                                        && activityInfo.get("chapterId").equals(chapter.get("chapterId"))
                                        && Arrays.asList("1", "2").contains(activityInfo.get("activityState")));
                chapter.put("hasActivity", hasActivity);
            });
        }
        return true;
    }
    
    /**
     * 获取一本书（章节树）（教学包页面专用）
     * @param pkgId
     * @param subjectId
     * @param chapterName
     * @param loginUserId
     * @return
     */
    @Override
    public R getBookForPkgPage(String pkgId, String subjectId, String chapterName, String loginUserId) {
    	log.debug("            教学页面中查询章节树形数据              ");
        // 合法性校验
        if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(loginUserId)) {
            return R.error("必传参数为空");
        }
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
        if (tevglPkgInfo == null) {
            return R.error("无效的教学包");
        }
        TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
        if (tevglBookSubject == null) {
            return R.error("无效的教材");
        }
        // 调用方法，取教材部分属性
        Map<String, Object> subjectInfo = getSimpleSubjectInfo(tevglBookSubject);
        subjectInfo.put("pkgLimit", tevglPkgInfo.getPkgLimit());
        // 处理是否有【授权】的按钮
        hasAuthButton(tevglPkgInfo, subjectInfo, loginUserId);
        // 查询章节
        List<Map<String, Object>> chapterList = selectSimpleChapterListMap(subjectId, true, pkgId);
        subjectInfo.put("chapterNum", chapterList.size());
        // 调用方法，处理节点的操作权限，以及节点下是否有活动
        boolean flag = doHandleNodeFlagsForPkg(tevglPkgInfo, subjectInfo, loginUserId, chapterList);
        if (!flag) {
        	log.info("当前用户[{}], 没有教学包[{}]的权限，无法查看章节树", loginUserId, pkgId);
            return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        // 调用方法，递归构建
        List<Map<String, Object>> children = buildBookNew(chapterList, 0, subjectInfo.get("subjectId").toString(), false);
        // 调用方法，处理序号
        handleSortNum(children);
        subjectInfo.put("children", children);
        // 此外，浏览量+1
        TevglBookSubject t = new TevglBookSubject();
        t.setSubjectId(subjectId);
        t.setViewNum(1);
        tevglBookSubjectMapper.updateNum(t);
        return R.ok().put(Constant.R_DATA, subjectInfo);
    }
    
    private void hasAuthButton(TevglPkgInfo tevglPkgInfo, Map<String, Object> subjectInfo, String loginUserId) {
    	subjectInfo.put("hasAuthBtn", false);
    	if (tevglPkgInfo == null || StrUtils.isEmpty(loginUserId)) {
    		return;
    	}
    	boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
    	boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
    	// 免费的理应不需要授权
    	if ("2".equals(tevglPkgInfo.getPkgLimit())) {
    		subjectInfo.put("hasAuthBtn", false);
    		return;
    	}
    	// 如果是此教学包的创建，有权限
    	if (isCreator || isReceiver) {
    		subjectInfo.put("hasAuthBtn", true);
    		return;
    	}
    }

    /**
     * 处理节点的操作权限，以及节点下是否有活动（教学包页面中专用）
     * @param tevglPkgInfo
     * @param subjectInfo
     * @param loginUserId
     * @param chapterList
     */
    private boolean doHandleNodeFlagsForPkg(TevglPkgInfo tevglPkgInfo, Map<String, Object> subjectInfo, String loginUserId, List<Map<String, Object>> chapterList) {
        // 默认没有权限
        subjectInfo.put("hasPermission", false);
        subjectInfo.put("isBookCreator", false);
        String pkgId = tevglPkgInfo.getPkgId();
        // 查询教学包的活动
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pkgId", pkgId);
        List<Map<String, Object>> activityList = tevglPkgActivityRelationMapper.selectSimpleListMap(params);
        boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
        boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
        List<TevglBookpkgTeam> tevglBookpkgTeamList = selectBookpkgTeam(pkgId, loginUserId);
        boolean isTogether = tevglBookpkgTeamList != null && tevglBookpkgTeamList.size() > 0;
        boolean isBookCreator = isCreator || isReceiver;
        // 不满足条件，直接返回
        if (!isCreator && !isReceiver && !isTogether) {
            return false;
        }
        // 如果这个教学包已处于发布状态，则不允许新增修改删除等操作
        if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
        	subjectInfo.put("isBookCreator", isBookCreator);
            chapterList.stream().forEach(chapter -> {
                chapter.put("isBookCreator", isBookCreator);
                // 本节点是否允许新增修改删除等操作
                chapter.put("hasPermission", false);
                // 本章节是否有活动
                boolean hasActivity = activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapter.get("chapterId")));
                chapter.put("hasActivity", hasActivity);
            });
            return true;
        } else {
            if (isCreator || isReceiver) {
                subjectInfo.put("isBookCreator", true);
                subjectInfo.put("hasPermission", true);
                chapterList.stream().forEach(chapter -> {
                    chapter.put("isBookCreator", true);
                    // 本节点是否允许新增修改删除等操作
                    chapter.put("hasPermission", true);
                    // 本章节是否有活动
                    boolean hasActivity = activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapter.get("chapterId")));
                    chapter.put("hasActivity", hasActivity);
                });
                return true;
            }
            // 如果是共建者
            if (isTogether) {
                List<String> hasPermissionChapterIdList = getUserHasChapterIdPermissionList(pkgId, loginUserId, tevglBookpkgTeamList.get(0).getTeamId());
                chapterList.stream().forEach(chapter -> {
                    chapter.put("isBookCreator", false);
                    // 本节点是否允许新增修改删除等操作
                    chapter.put("hasPermission", hasPermissionChapterIdList.stream().anyMatch(a -> a.equals(chapter.get("chapterId"))));
                    // 本章节是否有活动
                    boolean hasActivity = activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapter.get("chapterId")));
                    chapter.put("hasActivity", hasActivity);
                });
                return true;
            }
        }
        return false;
    }
	
	
    /**
     * 递归
     * @param allList
     * @param level
     * @param parentId
     * @param filterVisible 布尔值，为true则会过滤掉【学员不可见】的章节
     * @return
     */
    public List<Map<String, Object>> buildBookNew(List<Map<String, Object>> allList, int level, String parentId, boolean filterVisible) {
        if (allList == null || allList.size() == 0) {
            return null;
        }
        List<Map<String, Object>> nodeList = new ArrayList<Map<String,Object>>();
        // 学员身份时，是否过滤调用【学员不可见】的章节
        if (filterVisible) {
            nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId) && "Y".equals(a.get("isTraineesVisible"))).collect(Collectors.toList());
        } else {
            nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
        }
        if (nodeList != null && nodeList.size() > 0) {
            // level计算当前处于第几级
            level ++;
            for (int i = 0; i < nodeList.size(); i++) {
                // 当前节点
                Map<String, Object> a = nodeList.get(i);
                // 标识为章节节点
                a.put("type", "chapter");
                // 当前层级
                a.put("level", level);
                List<Map<String, Object>> list = buildBookNew(allList, level, a.get("chapterId").toString(), filterVisible);
                if (list != null && list.size() > 0) {
                    a.put("children", list);
                } else {
                    a.put("children", null);
                }
            }
        }
        return nodeList;
    }

    /**
     *
     * @param pkgId
     * @param loginUserId
     * @return
     */
    private List<TevglBookpkgTeam> selectBookpkgTeam(String pkgId, String loginUserId){
        Map<String, Object> map = new HashMap<>();
        map.put("pgkId", pkgId);
        map.put("userId", loginUserId);
        List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(map);
        return list;
    }

    /**
     * 获取用户的此教学包对应教材的章节权限
     * @param loginUserId
     * @param pkgId
     * @return
     */
    private List<String> getUserHasChapterIdPermissionList(String pkgId, String loginUserId, String teamId){
        Map<String, Object> map = new HashMap<>();
        map.put("teamId", teamId);
        map.put("userId", loginUserId);
        List<TevglBookpkgTeamDetail> tevglBookpkgTeamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(map);
        if (tevglBookpkgTeamDetailList == null || tevglBookpkgTeamDetailList.size() == 0) {
            log.debug("教学包["+pkgId+"]中，" + "用户["+loginUserId+"]，" + "细表没有权限记录，直接返回");
            return new ArrayList<>();
        }
        return tevglBookpkgTeamDetailList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
    }

    /**
     * 查出该书所有的章节
     * @param subjectId 必传参数
     * @param isInRoomPage 选传参数，当在课堂页面时，需传true
     * @param pkgId isInRoomPage为true时，必传，用于过滤掉章节，对学员不可见
     * @return
     */
    private List<Map<String,Object>> selectSimpleChapterListMap(String subjectId, boolean isInRoomPage, String pkgId){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sidx", "order_num");
        map.put("order", "asc");
        map.put("state", "Y");
        map.put("subjectId", subjectId);
        if (isInRoomPage) {
            map.put("pkgId", pkgId);
        }
        List<Map<String,Object>> list = tevglBookChapterMapper.selectSimpleListMap(map);
        log.debug("根据条件查询章节：" + map);
        log.debug("查询结果：" + list.size());
        return list;
    }
    
    /**
     * 取部分属性
     * @param subject
     * @return
     */
    private Map<String, Object> getSimpleSubjectInfo(TevglBookSubject subject){
        Map<String, Object> subjectInfo = new HashMap<>();
        // 标识为课程节点
        subjectInfo.put("type", "subject");
        subjectInfo.put("id", subject == null ? null : subject.getSubjectId());
        subjectInfo.put("chapterId", subject == null ? null : subject.getSubjectId());
        subjectInfo.put("subjectId", subject == null ? null : subject.getSubjectId());
        subjectInfo.put("subjectName", subject == null ? null : subject.getSubjectName());
        subjectInfo.put("chapterName", subject == null ? null : subject.getSubjectName());
        subjectInfo.put("subjectAuthor", subject == null ? null : subject.getSubjectAuthor());
        subjectInfo.put("subjectDesc", subject == null ? null : subject.getSubjectDesc());
        subjectInfo.put("subjectLogo", subject == null ? null : budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo());
        subjectInfo.put("createUserId", subject == null ? null : subject.getCreateUserId());
        return subjectInfo;
    }

	@Override
	public List<TevglBookSubjectSelectVo> querySubjectList(Map<String, Object> map) {
		Query query = new Query(map);
		return tevglBookSubjectMapper.querySubjectList(query);
	}

}
