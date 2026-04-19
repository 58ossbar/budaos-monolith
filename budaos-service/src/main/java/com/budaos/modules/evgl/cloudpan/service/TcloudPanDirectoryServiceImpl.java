package com.budaos.modules.evgl.cloudpan.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
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
import com.budaos.modules.common.CloudPanUtils;
import com.budaos.modules.common.DictService;
import com.budaos.modules.common.PkgUtils;
import com.budaos.modules.evgl.cloudpan.api.TcloudPanDirectoryService;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanNavigationBar;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanPermissions;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanNavigationBarMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanPermissionsMapper;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
public class TcloudPanDirectoryServiceImpl implements TcloudPanDirectoryService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TcloudPanDirectoryServiceImpl.class);
	@Autowired
	private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	@Autowired
	private TcloudPanPermissionsMapper tcloudPanPermissionsMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TcloudPanNavigationBarMapper tcloudPanNavigationBarMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	
	@Value("${com.budaos.file-upload-path-cloud-pan:C:/uploads/cloud-pan}")
	private String uploadPathCloudPan;
	
	@Autowired
	private CloudPanUtils cloudPanUtils;
	@Autowired
	private DictService dictService;
	@Autowired
	private	PkgUtils pkgUtils;

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TcloudPanDirectory> tcloudPanDirectoryList = tcloudPanDirectoryMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tcloudPanDirectoryList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tcloudPanDirectoryList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tcloudPanDirectoryList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tcloudPanDirectoryList = tcloudPanDirectoryMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tcloudPanDirectoryList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tcloudPanDirectoryList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tcloudPanDirectory
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TcloudPanDirectory tcloudPanDirectory) throws BudaosException {
		tcloudPanDirectory.setDirId(Identities.uuid());
		tcloudPanDirectory.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tcloudPanDirectory.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tcloudPanDirectory);
		tcloudPanDirectoryMapper.insert(tcloudPanDirectory);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tcloudPanDirectory
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	@PostMapping("update")
	@SentinelResource("/cloudpan/tcloudpandirectory/update")
	public R update(TcloudPanDirectory tcloudPanDirectory) throws BudaosException {
	    tcloudPanDirectory.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tcloudPanDirectory.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tcloudPanDirectory);
		tcloudPanDirectoryMapper.update(tcloudPanDirectory);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	@GetMapping("delete/{id}")
	@SentinelResource("/cloudpan/tcloudpandirectory/delete")
	public R delete(String id) throws BudaosException {
		tcloudPanDirectoryMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	@PostMapping("deleteBatch")
	@SentinelResource("/cloudpan/tcloudpandirectory/deleteBatch")
	public R deleteBatch( String[] ids) throws BudaosException {
		tcloudPanDirectoryMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	@GetMapping("view/{id}")
	@SentinelResource("/cloudpan/tcloudpandirectory/view")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tcloudPanDirectoryMapper.selectObjectById(id));
	}
	
	@Override
	public TcloudPanDirectory selectObjectById(Object id) {
		return tcloudPanDirectoryMapper.selectObjectById(id);
	}
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params) {
		return tcloudPanDirectoryMapper.selectSimpleListMap(params);
	}
	
	/**
	 * 查询顶级目录
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectTopDirectoryList(Map<String, Object> params, String loginUserId) {
		params.put("state", "Y");
		params.put("createUserId", loginUserId);
		return tcloudPanDirectoryMapper.selectTopDirectoryList(params);
	}

	/**
	 * 处理快捷面板
	 * @param ctId
	 * @param pkgId
	 */
	private void doCreateFastNar(Object ctId, String pkgId) {
		if (StrUtils.isNull(ctId) || StrUtils.isEmpty(pkgId)) {
			return;
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return;
		}
		if (!StrUtils.isNull(tevglTchClassroom.getViewNum()) && !tevglTchClassroom.getViewNum().equals(0)) {
			return;
		}
		log.debug("============================== 准备生成快捷面板 ==============================");
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		List<TcloudPanNavigationBar> list = tcloudPanNavigationBarMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			// 找到顶级目录
			params.clear();
			params.put("parentId", "0");
			params.put("pkgId", pkgId);
			List<TcloudPanDirectory> dirList = tcloudPanDirectoryMapper.selectListByMap(params);
			List<TcloudPanDirectory> collect = dirList.stream().filter(a -> StrUtils.isNotEmpty(a.getDictCode())).collect(Collectors.toList());
			// 生成默认的快捷面板
			if (collect != null && collect.size() > 0) {
				List<TcloudPanNavigationBar> insertList = new ArrayList<TcloudPanNavigationBar>();
				for (int i = 0; i < collect.size(); i++) {
					TcloudPanDirectory directoryInfo = collect.get(i);
					TcloudPanNavigationBar t = new TcloudPanNavigationBar();
					t.setId(Identities.uuid());
					t.setDirId(directoryInfo.getDirId());
					t.setSortNum(i);
					t.setCtId(ctId.toString());
					t.setPkgId(pkgId);
					insertList.add(t);
				}
				tcloudPanNavigationBarMapper.insertBatch(insertList);
				// 更新下数量
				TevglTchClassroom room = new TevglTchClassroom();
				room.setCtId(ctId.toString());
				room.setViewNum(1);
				tevglTchClassroomMapper.plusNum(room);
			}
		}
	}
	
	/**
	 * 生成默认目录
	 * @param pkgInfo 当前教学包信息
	 * @param dictList 字典中默认的云盘顶级目录
	 * @param loginUserId 当前登录用户
	 * @param params 前端原样传递的参数
	 */
	private void doCreateDefaultDirectory(TevglPkgInfo pkgInfo, List<Map<String,Object>> dictList, String loginUserId, Map<String, Object> params) {
		// 第一次才去生成
		if (StrUtils.isNotEmpty(pkgInfo.getHasGeneratedDefaultFolder()) && "Y".equals(pkgInfo.getHasGeneratedDefaultFolder())) {
			return;
		}
		if (!loginUserId.equals(pkgInfo.getCreateUserId())) {
			return;
		}
		log.debug("============================== 准备生成默认文件 ==============================");
		String pkgId = pkgInfo.getPkgId();
		// 教学包详情页面
		if (StrUtils.isNull(params.get("ctId"))) {
			// 已经发布了的教学包不再生成默认分组
			if ("Y".equals(pkgInfo.getReleaseStatus())) {
				return;
			}
		} else {
			// 表示是已发布状态下的教学包页面里面查看，这种情况则不需要再次生成默认文件夹
			if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && StrUtils.isNull(params.get("ctId"))) {
				return;
			}
			// 课堂页面，不需要去生成默认文件夹
			if (!StrUtils.isNull(params.get("ctId"))) {
				return;
			}
		}
		if (dictList == null || dictList.size() == 0) {
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("state", "Y");
		//map.put("createUserId", loginUserId);
		map.put("pkgId", pkgId);
		List<Map<String,Object>> list = tcloudPanDirectoryMapper.selectTopDirectoryList(map);
		List<Object> existedNames = list.stream().map(dir -> dir.get("name")).collect(Collectors.toList());
		List<Object> dictNames = dictList.stream().map(dict -> dict.get("dictValue")).collect(Collectors.toList());
		// 名称去重
		dictNames.removeAll(existedNames);
		log.debug("查询自己教学包["+pkgId+"]的顶级目录" + list.size());
		log.debug("查到的结果：" + existedNames);
		log.debug("字典中默认的文件夹：" + dictNames);
		log.debug("去重后需要生成的文件夹：" + dictNames);
		if (dictNames == null || dictNames.size() == 0) {
			return;
		}
		// 查找来源教学包的默认顶级目录
		List<Map<String,Object>> refPkgDirList = new ArrayList<Map<String,Object>>();
		//if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
		//if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !"3".equals(pkgInfo.getDisplay()) && !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
		if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId()) && !pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
			/*map.clear();
			map.put("pkgId", pkgInfo.getRefPkgId());
			map.put("parentId", "0");
			map.put("state", "Y");
			map.put("dictCodeNotNull", "Y");
			refPkgDirList = tcloudPanDirectoryMapper.selectSimpleListMap(map);
			log.debug("来源教学包的数量" + refPkgDirList.size());	*/
		}
		// 更新该值
		pkgInfo.setHasGeneratedDefaultFolder("Y");
		tevglPkgInfoMapper.update(pkgInfo);
		log.debug("多次去重后实际需要生成的默认文件夹：" + dictNames);
		if (dictNames == null || dictNames.size() == 0) {
			return;
		}
		// 遍历，并生成默认文件夹
		for (int i = 0; i < dictNames.size(); i++) {
			String dictValue = dictNames.get(i).toString();
			// 目录表入库一条记录
			TcloudPanDirectory tcloudPanDirectory = new TcloudPanDirectory();
			tcloudPanDirectory.setDirId(Identities.uuid());
			/*// 处理来源
			if (refPkgDirList != null && refPkgDirList.size() > 0) {
				for (Map<String, Object> refPkgDir : refPkgDirList) {
					if (dictValue.equals(refPkgDir.get("name"))) {
						tcloudPanDirectory.setRefDirId(StrUtils.isNull(refPkgDir.get("dirId"))? null : refPkgDir.get("dirId").toString());
					}
				}
			}*/
			tcloudPanDirectory.setName(dictValue);
			tcloudPanDirectory.setParentId("0");
			tcloudPanDirectory.setSortNum(i);
			tcloudPanDirectory.setPkgId(StrUtils.isNull(params.get("pkgId")) ? null : (String)params.get("pkgId"));
			tcloudPanDirectory.setCreateUserId(loginUserId);
			tcloudPanDirectory.setCreateTime(DateUtils.getNowTimeStamp());
			tcloudPanDirectory.setUpdateUserId(loginUserId);
			tcloudPanDirectory.setUpdateTime(DateUtils.getNowTimeStamp());
			tcloudPanDirectory.setState("Y");
			List<Map<String,Object>> collect = dictList.stream().filter(a -> a.get("dictValue").equals(dictValue)).collect(Collectors.toList());
			if (collect != null && collect.size() > 0) {
				String dictCode = collect.get(0).get("dictCode").toString();
				tcloudPanDirectory.setDictCode(dictCode);	
			}
			tcloudPanDirectoryMapper.insert(tcloudPanDirectory);
			// 磁盘上生成对应文件夹
			String uri = uploadPathCloudPan + "/" + pkgId + "/" + dictValue;
			File path = new File(uri);
			if (!path.exists()) {
				path.mkdirs();
			}
		}
	}
	
	/**
	 * 上传文件时的合法性校验
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	private R handleUploadPermission(TevglPkgInfo tevglPkgInfo, String loginUserId){
		if (tevglPkgInfo == null) {
			return R.error("上传失败，无效的教学包");
		}
		Map<String, Object> data = new HashMap<>();
		String pkgId = tevglPkgInfo.getPkgId();
		// 如果是课堂页面中操作云盘
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			// 如果课堂没有被移交
			if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					return R.error("非法操作");
				}
			}
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
					return R.error("非法操作，课堂已被移交，无权操作");
				}
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglPkgInfo.getCreateUserId();
				} else {
					return R.error("非法操作，没有权限");
				}
			}
		} else {
			// 否则就是在教学包页面中上传文件
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (hasOperatingAuthorization) {
				loginUserId = tevglPkgInfo.getCreateUserId();
			} else {
				return R.error("没有权限，无法上传");
			}
		}
		// 返回数据
		data.put("loginUserId", loginUserId);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * 新建文件夹
	 * @param pkgId
	 * @param dirId
	 * @param name 文件夹名
	 * @param icon
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R createDir(String pkgId, String dirId, String name, String icon, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(name)) {
			return R.error("文件夹名称不能为空");
		}
		String[] names = name.split("");
		for (int i=0; i < names.length; i++) {
			if (cloudPanUtils.unAllowedDirNameList.contains(names[i])) {
				return R.error("文件名不能包含下列任何字符：" + cloudPanUtils.unAllowedDirNameList.stream().collect(Collectors.joining(" ")));
			}
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("创建失败");
		}
		// 权限判断
		R r = handleUploadPermission(tevglPkgInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 重新获取数据
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) r.get(Constant.R_DATA);
		loginUserId = data.get("loginUserId").toString();
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			if (pkgUtils.isBeingUsed(pkgId)) {
				return R.error("已经发布了的教学包中，不允许再创建文件夹");
			}
		}
		TcloudPanDirectory parentTcloudPanDirectory = null;
		if (StrUtils.isNotEmpty(dirId)) {
			parentTcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
			if (parentTcloudPanDirectory == null) {
				return R.error("无法在此目录下创建文件夹");
			}	
		}
		name = name.trim();
		String tempName = name;
		if (StrUtils.isEmpty(name)) {
			return R.error("文件夹名称不能为空");
		}
		if (name.length() > 100) {
			return R.error("名称不能超过100个字符");
		}
		log.debug("用户输入的目录名称：" + tempName);
		log.debug("在目录[" + dirId+"]下新增子目录");
		String res = cloudPanUtils.getPathBy(dirId, loginUserId, pkgId);
		log.debug("路径：" + res);
		// 如果同目录下已经存在了此名称
		Map<String, Object> params = new HashMap<>();
		params.put("parentId", StrUtils.isEmpty(dirId) ? "0" : dirId);
		params.put("createUserId", loginUserId);
		params.put("pkgId", pkgId);
		List<TcloudPanDirectory> list = tcloudPanDirectoryMapper.selectListByMap(params);
		log.debug("查找到目录：" + list.size());
		if (list.stream().anyMatch(a -> a.getName().equals(tempName))) {
			// 则在后面追加_年月日_时分秒
			String now = DateUtils.getNow("yyyyMMdd_HHmmss");
			name = name + "_" + now;
		}
		// 磁盘上生成对应目录
		File path = new File(res + "/" + name);
		if(!path.exists()) {
			log.debug("创建文件夹：" + path);
			path.mkdirs();
		}
		// 获取refDirId  判断用户填写的名称与来源教学包中的云盘目录是否同名
		String refDirId = null; 
		if (parentTcloudPanDirectory != null) {
			//refDirId = cloudPanUtils.getRefDirId(tempName, tevglPkgInfo.getRefPkgId(), parentTcloudPanDirectory.getRefDirId(), params);
			// 获取来源教学包中同名的目录
			if (StrUtils.isNotEmpty(parentTcloudPanDirectory.getRefDirId())) {
				params.clear();
				params.put("name", name);
				params.put("pkgId", tevglPkgInfo.getRefPkgId());
				params.put("parentId", parentTcloudPanDirectory.getRefDirId());
				List<TcloudPanDirectory> resList = tcloudPanDirectoryMapper.selectListByMap(params);
				if (resList != null && resList.size() > 0) {
					refDirId = resList.get(0).getDirId();
				}
			}
		} else { // 没有传dirId的情况下，就是新增顶级目录
			/*params.put("name", name);
			params.put("pkgId", tevglPkgInfo.getRefPkgId());
			List<TcloudPanDirectory> refList = tcloudPanDirectoryMapper.selectListByMap(params);
			List<TcloudPanDirectory> collect = refList.stream().filter(a -> StrUtils.isNotEmpty(a.getDictCode()) && a.getName().equals(tempName)).collect(Collectors.toList());
			if (collect != null && collect.size() > 0) {
				refDirId = collect.get(0).getDirId();
			}*/
		}
		// 目录表入库一条记录
		TcloudPanDirectory tcloudPanDirectory = new TcloudPanDirectory();
		tcloudPanDirectory.setDirId(Identities.uuid());
		tcloudPanDirectory.setRefDirId(refDirId);
		tcloudPanDirectory.setName(name);
		tcloudPanDirectory.setPkgId(pkgId);
		tcloudPanDirectory.setIcon(icon);
		tcloudPanDirectory.setParentId(StrUtils.isEmpty(dirId) ? "0" : dirId);
		tcloudPanDirectory.setCreateUserId(loginUserId);
		tcloudPanDirectory.setCreateTime(DateUtils.getNowTimeStamp());
		tcloudPanDirectory.setUpdateUserId(loginUserId);
		tcloudPanDirectory.setUpdateTime(DateUtils.getNowTimeStamp());
		tcloudPanDirectory.setState("Y");
		params.clear();
		params.put("parentId", dirId);
		params.put("createUserId", loginUserId);
		params.put("pkgId", pkgId);
		Integer sortNum = tcloudPanDirectoryMapper.getMaxSortNum(params);
		tcloudPanDirectory.setSortNum(sortNum);
		tcloudPanDirectoryMapper.insert(tcloudPanDirectory);
		return R.ok("创建文件夹成功").put(Constant.R_DATA, tcloudPanDirectory.getDirId());
	}
	
	/**
	 * 查看（点击目录查询子目录和文件）（注意，课堂页面和教学包页面中云盘都是调用的这个接口，注意兼容）（已废弃，暂不删除）
	 * @param ctId 不为空，则表示是在课堂里面查看哟
	 * @param pkgId
	 * @param dirId
	 * @param name
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Deprecated
	public R viewDetailNew(String ctId, String pkgId, String dirId, String name, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("查看失败");
		}
		// 创建者标识
		boolean isCreator = loginUserId.equals(tevglPkgInfo.getCreateUserId());
		// 是否在课堂页面查看云盘标识
		boolean isInRoomView = StrUtils.isNotEmpty(ctId) && ctId.length() == 32;
		log.info("查看云盘数据时，当前登录是否为创建者：" + isCreator);
		log.info("是否在课堂页面中查看云盘数据：" + isInRoomView);
		// 非课堂创建者时，登录用户拥有的权限
		List<String> sonFiles = new ArrayList<>();
		// 最终返回数据
		List<Map<String, Object>> list = new ArrayList<>();
		// 是否被使用了
		//boolean isBeingUsed = pkgUtils.isBeingUsed(pkgId);
		// 如果不是课堂创建者，且在课堂页面中查看云盘数据，则需要进行数据权限过滤
		if (!isCreator && isInRoomView) {
			// 调用方法，得到当前登录用户拥有的权限集合
			sonFiles = getTraineeHasPermissionList(ctId, pkgId, loginUserId);
			if (sonFiles == null || sonFiles.size() == 0) {
				return R.ok().put(Constant.R_DATA, list);
			}
		}
		// 注意点！！！
		//Map<String, Object> parmas = getImportQueryParameters(tevglPkgInfo, dirId);
		Map<String, Object> parmas = new HashMap<>();
		List<String> pkgIds = new ArrayList<>();
		pkgIds.add(pkgId);
		List<String> dirIds = new ArrayList<>();
		if (StrUtils.isNotEmpty(dirId)) {
			dirIds.add(dirId);
			if (!isCreator) {
				TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
				if (tcloudPanDirectory != null) {
					dirIds.add(tcloudPanDirectory.getRefDirId());
				}	
			}
		}
		// 如果当前是教学包带过来的目录则查询教学包下的目录及文件
		if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && "N".equals(tevglPkgInfo.getReleaseStatus())) {
			pkgIds.add(tevglPkgInfo.getRefPkgId());
		}
		parmas.put("pkgIds", pkgIds);
		parmas.put("dirIds", dirIds);
		parmas.put("name", name); // 按名称搜索
		parmas.put("sonFiles", sonFiles);
		// 注意点！！！
		if (StrUtils.isEmpty(dirId)) {
			// 没传这个表示查询的顶级目录下的所有目录和文件
			parmas.put("parentIdNotZero", "Y");
			// 查询直属于教学包下的文件
			parmas.put("queryTopFile", "Y");
		}
		list = tcloudPanDirectoryMapper.selectListByUnionAllNew(parmas);
		log.debug("查询条件：" + parmas);
		log.debug("查询结果：" + list.size());
		// 处理访问路径，操作按钮权限等
		handle(tevglPkgInfo, list);
		// 重新排序下优先展示文件夹
		list = list.stream().sorted((x, y) -> x.get("type").toString().compareTo(y.get("type").toString())).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 处理访问路径，操作按钮权限等
	 * @param tevglPkgInfo
	 * @param list
	 */
	private void handle(TevglPkgInfo tevglPkgInfo, List<Map<String, Object>> list) {
		String pkgId = tevglPkgInfo.getPkgId();
		list.stream().forEach(a -> {
			// 处理路径
			if (!StrUtils.isNull(a.get("type")) && "2".equals(a.get("type"))) {
				String targetPkgId = pkgId;
				if (!pkgId.equals(a.get("pkgId"))) {
					targetPkgId = a.get("pkgId").toString();
				}
				a.put("path", cloudPanUtils.getFilePathByValue(targetPkgId, a.get("path")));
				a.put("fileAccessUrl", cloudPanUtils.getFileAccessUrlByValue(targetPkgId, a.get("fileAccessUrl")));
			}
			// 是否有操作权限
			if (pkgId.equals(a.get("pkgId"))) {
				a.put("hasOperPermission", true);
				// 如果此教学包已经发布，则不允许
				//if (isBeingUsed && "Y".equals(tevglPkgInfo.getReleaseStatus())) {
				if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
					a.put("hasOperPermission", false);
				}
				a.put("refPkgDir", false);
			} else {
				a.put("hasOperPermission", false);
				a.put("refPkgDir", true);
			}
			// 过滤部分数据
			a.remove("fileSavePath");
			a.remove("createUserId");
			a.remove("pkg_id");
		});
	}

	/**
	 * 获取当前登录用户拥有的云盘权限（文件夹id、文件id）
	 * @param ctId
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	private List<String> getTraineeHasPermissionList(String ctId, String pkgId, String loginUserId) {
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("currentPkgId", pkgId);
		map.put("owner", loginUserId);
		List<Map<String, Object>> tcloudPanPermissionsList = tcloudPanPermissionsMapper.selectOwnerByFileId(map);
		if (tcloudPanPermissionsList == null || tcloudPanPermissionsList.size() == 0) {
			return null;
		}
		List<String> list = new ArrayList<>();
		tcloudPanPermissionsList.stream().forEach(a -> {
			if (!StrUtils.isNull(a.get("fileId"))) {
				list.add(a.get("fileId").toString());
			} else {
				list.add(a.get("dirId").toString());
			}
		});
		return list;
	}
	
	/**
     * 重命名
     * @param dirId 当前被重命名的文件夹
     * @param name 用户输入的文件夹名称
     * @param icon 用户选中的图标
     * @param loginUserId 当前登录用户
     * @return
     */
	@Override
	public R rename(String dirId, String name, String icon,String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(dirId) || StrUtils.isEmpty(name) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
		if (tcloudPanDirectory == null) {
			return R.error("无效的目录");
		}
		/*if ("0".equals(tcloudPanDirectory.getParentId())) {
			if (StrUtils.isNotEmpty(tcloudPanDirectory.getDictCode())) {
				return R.error("默认文件夹暂不支持重命名");
			}
		}*/
		String pkgId = tcloudPanDirectory.getPkgId();
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("上传失败");
		}
		// 合法性校验
		R r = checkIsPassForRename(tevglPkgInfo, tcloudPanDirectory, name, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 权限判断
		R r2 = handleUploadPermission(tevglPkgInfo, loginUserId);
		if (!r2.get("code").equals(0)) {
			return r2;
		}
		if (name.equals(tcloudPanDirectory.getName()) && StrUtils.isNotEmpty(icon) && icon.equals(tcloudPanDirectory.getIcon())) {
			return R.ok("重命名成功");
		}
		// 如果修改的是用户的根目录
		if (StrUtils.isEmpty(tcloudPanDirectory.getParentId())) {
			Map<String, Object> map = new HashMap<>();
			map.put("createUserId", loginUserId);
			List<Map<String,Object>> list = tcloudPanDirectoryMapper.selectTopDirectoryList(map);
			boolean flag = list.stream().anyMatch(a -> !a.get("dirId").equals(dirId) && name.equals(a.get("name")));
			if (flag) {
				return R.error("此目录下已存在同名文件，请更换一个名字");
			}
		}
		try {
			//String savePath = cloudPanUtils.getSavePath(targetDirId, loginUserId, name, pkgId);
			// 获取旧的磁盘目录路径
			File oldFile = new File(cloudPanUtils.getPathBy(dirId, loginUserId, tcloudPanDirectory.getPkgId()));
			// 数据库记录更新
			TcloudPanDirectory t = new TcloudPanDirectory();
			t.setDirId(dirId);
			t.setName(name);
			t.setRefDirId(tcloudPanDirectory.getRefDirId());
			t.setIcon(icon);
			tcloudPanDirectoryMapper.update(t);
			log.debug("云盘图标： " + icon);
			// 重新获取新的磁盘目录
			File newFile = new File(cloudPanUtils.getPathBy(dirId, loginUserId, tcloudPanDirectory.getPkgId()));
			// 磁盘上目录重命名
			oldFile.renameTo(newFile);
			log.debug("获取旧的磁盘目录路径:" + oldFile);
			log.debug("重新获取新的磁盘目录:" + newFile);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("目录重命名失败", e);
			return R.error("系统错误");
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 更新本目录的文件相关数据
		doUpateFileInfo(pkgId, dirId, loginUserId, params);
		// 更新子目录里的文件相关数据
		doUpateChildrenFileInfo(dirId, loginUserId, params);
		return R.ok("重命名成功");
	}
	
	/**
	 * 更新本目录的文件相关数据，path字段、file_access_url字段、file_save_path字段
	 * @param dirId
	 * @param params
	 */
	private void doUpateFileInfo(String pkgId, String dirId, String loginUserId,  Map<String, Object> params){
		params.clear();
		params.put("dirId", dirId);
		List<TcloudPanFile> list = tcloudPanFileMapper.selectListByMap(params);
		list.stream().forEach(a -> {
			String name = a.getName();
			String value = cloudPanUtils.getValue(a.getDirId(), name);
			// 更新
			a.setFileAccessUrl(value);
			a.setFileSavePath(value);
			// PDF
			if (value != null) {
				int pos = value.lastIndexOf(".");
				String suffix = value.substring(pos);
				if (cloudPanUtils.canBePdfSuffixList.contains(suffix)) {
					a.setPath(value.substring(0, pos) + ".pdf");
				}
			}
			tcloudPanFileMapper.update(a);
		});
	}
	
	/**
	 * 递归修改子目录下的文件信息
	 * @param dirId
	 * @param params
	 */
	private void doUpateChildrenFileInfo(String dirId, String loginUserId, Map<String, Object> params){
		params.clear();
		params.put("parentId", dirId);
		List<TcloudPanDirectory> directoryList = tcloudPanDirectoryMapper.selectListByMap(params);
		if (directoryList != null && directoryList.size() > 0) {
			directoryList.stream().forEach(directory -> {
				// 查找此目录下的文件
				params.clear();
				params.put("dirId", directory.getDirId());
				List<Map<String, Object>> fileInfoList = tcloudPanFileMapper.selectFileInfoListByDirId(directory.getDirId());
				if (fileInfoList != null && fileInfoList.size() > 0) {
					TcloudPanFile t = new TcloudPanFile();
					fileInfoList.stream().forEach(fileInfo -> {
						String fileId = fileInfo.get("fileId").toString();
						String name = fileInfo.get("name").toString();
						String directoryId = fileInfo.get("dirId").toString();
						String value = cloudPanUtils.getValue(directoryId, name);
						// 更新
						t.setFileId(fileId);
						t.setFileAccessUrl(value);
						t.setFileSavePath(value);
						// PDF
						if (value != null) {
							int pos = value.lastIndexOf(".");
							String suffix = value.substring(pos);
							if (cloudPanUtils.canBePdfSuffixList.contains(suffix)) {
								t.setPath(value.substring(0, pos) + ".pdf");
							}
						}
						tcloudPanFileMapper.update(t);
					});
				}
				// 如果还有子目录，递归处理
				doUpateChildrenFileInfo(directory.getDirId(), loginUserId, params);
			});
		}
	}
	
	/**
	 * 合法性校验（重命名文件夹专用方法）
	 * @param tevglPkgInfo
	 * @param tcloudPanDirectory
	 * @param name
	 * @param loginUserId
	 * @return
	 */
	private R checkIsPassForRename(TevglPkgInfo tevglPkgInfo, TcloudPanDirectory tcloudPanDirectory, String name, String loginUserId) {
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			if (pkgUtils.isBeingUsed(tevglPkgInfo.getPkgId())) {
				return R.error("已经发布了的教学包中，不允许再重命名文件夹");
			}
		}
		name = name.trim();
		if (StrUtils.isEmpty(name)) {
			return R.error("文件夹名称不能为空");
		}
		if (name.length() > 100) {
			return R.error("名称不能超过100个字符");
		}
		/*if (!loginUserId.equals(tcloudPanDirectory.getCreateUserId())) {
			return R.error("非法操作");
		}*/
		String str = "";
		for (int i = 0; i<cloudPanUtils.unAllowedDirNameList.size(); i++) {
			str += cloudPanUtils.unAllowedDirNameList.get(i) + " ";
		}
		String[] names = name.split("");
		for (int i=0; i < names.length; i++) {
			if (cloudPanUtils.unAllowedDirNameList.contains(names[i])) {
				return R.error("文件名不能包含下列任何字符：" + str);
			}
		}
		if (!"Y".equals(tcloudPanDirectory.getState())) {
			return R.error("该目录已被删除，重命名失败");
		}
		return R.ok();
	}
	
	
	/**
	 * 获取目录树（包含文件夹和文件）
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R getDirectoryTree(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return null;
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return null;
		}
		// 查询归属在教学包目录下的文件
		List<String> pkgIdList = new ArrayList<>();
		pkgIdList.add(tevglPkgInfo.getPkgId());
		if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) 
				&& !pkgIdList.contains(tevglPkgInfo.getRefPkgId())) {
			pkgIdList.add(tevglPkgInfo.getRefPkgId());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("dirIds", pkgIdList);
		List<Map<String, Object>> topFileList = tcloudPanFileMapper.selectSimpleInfoList(params);
		log.debug("直接归属于教学包id下的文件有：" + topFileList.size());
		// 查询条件
		HashMap<String,Object> map = new HashMap<String, Object>();
		// 查询用户创建的所有目录
		map.put("createUserId", loginUserId);
		map.put("pkgId", pkgId);
		map.put("sidx", "t.dictCode");
		map.put("order", "asc, t.updateTime desc");
		List<Map<String,Object>> list = tcloudPanDirectoryMapper.selectListByUnionAll(map);
		List<Map<String, Object>> result = build("0", list);
		Map<String, Object> data = converSimplePkgMapInfo(tevglPkgInfo);
		if (topFileList != null && topFileList.size() > 0) {
			result.addAll(topFileList);
		}
		data.put("children", result);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	private Map<String, Object> converSimplePkgMapInfo(TevglPkgInfo tevglPkgInfo){
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("name", "全部文件");
		info.put("pkgId", tevglPkgInfo.getPkgId());
		info.put("id", tevglPkgInfo.getPkgId());
		info.put("pkg_id", tevglPkgInfo.getPkgId());
		return info;
	}
	
	/**
	 * 只包含文件夹
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDirectoryTree2(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return null;
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return new ArrayList<>();
		}
		// 如果是课堂页面中操作云盘
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			// 如果课堂没有被移交
			if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					return new ArrayList<>();
				}
			}
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					return new ArrayList<>();
				}
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglTchClassroom.getCreateUserId();
				}
			}
		} else {
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (hasOperatingAuthorization) {
				loginUserId = tevglPkgInfo.getCreateUserId();
			}
		}
		// 查询条件
		HashMap<String,Object> map = new HashMap<String, Object>();
		// 查询用户创建的所有目录
		map.put("createUserId", loginUserId);
		map.put("pkgId", pkgId);
		map.put("sidx", "dict_code");
		map.put("order", "asc, create_time desc");
		List<Map<String,Object>> list = tcloudPanDirectoryMapper.selectSimpleListMap(map);
		List<Map<String, Object>> result = build("0", list);
		return result;
	}
	
	private List<Map<String, Object>> build(String parentId, List<Map<String, Object>> allList) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		// 筛选出匹配的节点
		List<Map<String, Object>> nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
		if (nodeList != null && nodeList.size() > 0) {
			for (int i = 0; i < nodeList.size(); i++) {
				Map<String, Object> node = nodeList.get(i);
				// 递归
				List<Map<String, Object>> list = build(node.get("id").toString(), allList);
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
	 * 移动
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R move(JSONObject jsonObject, String loginUserId) {
		String pkgId = jsonObject.getString("pkgId");
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数pkgId为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("上传失败");
		}
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			if (pkgUtils.isBeingUsed(pkgId)) {
				return R.error("已经发布了的教学包中，不允许再移动文件夹");
			}
		}
		// 目标目录,也就是要被移动到此目录
		String targetDirId = jsonObject.getString("targetDirId");
		if (StrUtils.isEmpty(targetDirId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("请选择要移动的目录或文件");
		}
		TcloudPanDirectory targetTcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(targetDirId);
		if (targetTcloudPanDirectory == null) {
			return R.error("选择的目录已不存在，请重试");
		}
		/*if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			return R.error("非法操作");
		}*/
		// 如果是课堂页面中操作云盘
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			// 如果课堂没有被移交
			if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					return R.error("非法操作");
				}
			}
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
					return R.error("非法操作，课堂已被移交，无权操作");
				}
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglPkgInfo.getCreateUserId();
				} else {
					return R.error("非法操作，没有权限");
				}
			}
		} else {
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (!hasOperatingAuthorization) {
				return R.error("没有权限，无法操作");
			} else {
				// 伪装者
				loginUserId = tevglPkgInfo.getCreateUserId();
			}
		}
		// 找到目标目录下的所有子目录
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("createUserId", loginUserId);
		map.put("pkgId", pkgId);
		map.put("sidx", "dict_code");
		map.put("order", "asc, create_time desc");
		List<Map<String,Object>> list = tcloudPanDirectoryMapper.selectSimpleListMap(map);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			String type = obj.getString("type");
			String id = obj.getString("id");
			if (id.equals(targetDirId)) {
				return R.error("不能将文件移动到自身或其子目录下");
			}
			// 如果当前选择的目录，是目标目录下的子目录
			if ("1".equals(type)) {
				List<String> dirIdList = new ArrayList<String>();
				//dirIdList.add(id);
				getChildrenId(id, list, dirIdList);
				log.debug("找到被移动到的目录以及其子目录：" + dirIdList);
				if (dirIdList.contains(targetDirId)) {
					return R.error("不能将文件移动到自身或其子目录下");
				}
			}
		}
		try {
			// 查询条件
			Map<String, Object> params = new HashMap<>();
			// 获取
			String path = cloudPanUtils.getPath(targetDirId, pkgId);
			log.debug("目标路径：" + path);
			// 遍历
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				String type = obj.getString("type");
				String id = obj.getString("id");
				// 移动目录
				if ("1".equals(type)) {
					// 当前被移动的目录对象
					TcloudPanDirectory directory = tcloudPanDirectoryMapper.selectObjectById(id);
					if (directory == null) {
						continue;
					}
					if (!loginUserId.equals(directory.getCreateUserId())) {
						continue;
					}
					// 当前目录
					String currentDir = cloudPanUtils.getUploadPath() + cloudPanUtils.getPath(directory.getDirId(), pkgId);
					// 处理名称
					String tempName = directory.getName();
					String name = directory.getName();
					name = cloudPanUtils.handleDirectoryName(name, targetDirId, loginUserId, params);
					try {
						String savePath = cloudPanUtils.getSavePath(targetDirId, loginUserId, name, pkgId);
						// 旧文件
						File oldFile = new File(currentDir);
						// 新文件
						File newFile = new File(savePath);
						// 移动目录至目标目录下（目录和文件会一并带走，只需要更新数据库中文件记录的路径）
						oldFile.renameTo(newFile);
					} catch (Exception e) {
						e.printStackTrace();
						log.error("磁盘文件移动失败", e);
						return R.error("系统错误，移动失败");
					}
					// 更新parentId
					directory.setParentId(targetDirId);
					// 更新名称
					directory.setName(name);
					// 更新refDirId
					String refDirId = null;
					if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
						params.clear();
						params.put("name", name);
						params.put("pkgId", tevglPkgInfo.getRefPkgId());
						params.put("parentId", targetTcloudPanDirectory.getRefDirId());
						List<TcloudPanDirectory> resList = tcloudPanDirectoryMapper.selectListByMap(params);
						if (resList != null && resList.size() > 0) {
							refDirId = resList.get(0).getDirId();
						}
						directory.setRefDirId(refDirId);
					}
					tcloudPanDirectoryMapper.update(directory);
					// 更新本目录的文件相关数据
					doUpateFileInfo(directory.getPkgId(), directory.getDirId(), loginUserId, params);
					// 更新子目录里的文件相关数据
					doUpateChildrenFileInfo(directory.getDirId(), loginUserId, params);
				}
				// 移动文件
				if ("2".equals(type)) {
					TcloudPanFile tcloudPanFile = tcloudPanFileMapper.selectObjectById(id);
					if (tcloudPanFile != null && loginUserId.equals(tcloudPanFile.getCreateUserId())) {
						// 如果实际没有移动目录
						if (targetDirId.equals(tcloudPanFile.getDirId())) {
							continue;
						}
						doMoveFileInfo(tcloudPanFile, targetDirId, path, loginUserId, params, pkgId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("云盘数据移动失败", e);
			return R.error("系统错误，移动失败");
		}
		return R.ok("移动成功");
	}
	
	private void getChildrenId(String targetId, List<Map<String,Object>> list, List<String> result) {
		// 取出当前当前节点的子节点
		if (list ==null || list.size() == 0) {
			return;
		}
		List<Map<String,Object>> collect = list.stream().filter(a -> a.get("parentId").equals(targetId)).collect(Collectors.toList());
		if (collect.size() > 0) {
			for (Map<String, Object> map : collect) {
				result.add(map.get("id").toString());
				getChildrenId(map.get("id").toString(), list, result);
			}
		}
	}
	
	/**
	 * 移动文件
	 * @param tcloudPanFile 文件信息
	 * @param targetDirId 被移动至哪个目录
	 * @param path 根据targetDirId获取到的路径
	 * @param loginUserId 当前登录用户
	 * @param params 查询条件
	 */
	private void doMoveFileInfo(TcloudPanFile tcloudPanFile, String targetDirId, String path, String loginUserId, Map<String, Object> params, String pkgId) {
		String name = tcloudPanFile.getName();
		name = cloudPanUtils.handleFileName(name, targetDirId, loginUserId, params);
		String value = cloudPanUtils.getValue(targetDirId, name);
		String savePath = cloudPanUtils.getSavePath(targetDirId, loginUserId, name, pkgId);
		// 磁盘文件移动至此目录
		String absolutePath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getFileSavePath());
		if (StrUtils.isNotEmpty(absolutePath)) {
			// 旧文件
			File oldFile = new File(absolutePath);
			// 新文件
			File newFile = new File(savePath);
			oldFile.renameTo(newFile);
		}
		// 数据库记录更新
		tcloudPanFile.setDirId(targetDirId);
		tcloudPanFile.setName(name);
		tcloudPanFile.setFileAccessUrl(value);
		tcloudPanFile.setFileSavePath(value);
		// PDF
		if (StrUtils.isNotEmpty(tcloudPanFile.getPath())) {
			int pos = value.lastIndexOf(".");
			if (pos > 0) {
				String suffix = value.substring(pos);
				if (cloudPanUtils.canBePdfSuffixList.contains(suffix)) {
					tcloudPanFile.setPath(value.substring(0, pos) + ".pdf");
					int i = absolutePath.lastIndexOf(".");
					if (i > 0) {
						File oldPdfFile = new File(absolutePath.substring(0, i) + ".pdf");
						if (oldPdfFile.exists()) {
							File newFile = new File(savePath.substring(0, savePath.lastIndexOf(".")) + ".pdf");
							oldPdfFile.renameTo(newFile);
						}
					}
				}
			}
		}
		tcloudPanFileMapper.update(tcloudPanFile);
	}
	
	/**
	 * 复制
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R copy(JSONObject jsonObject, String loginUserId) {
		String pkgId = jsonObject.getString("pkgId");
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数pkgId为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("上传失败");
		}
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			if (pkgUtils.isBeingUsed(pkgId)) {
				return R.error("已经发布了的教学包中，不允许再复制文件夹或文件");
			}
		}
		// 目标目录,也就是要被复制到此目录
		String targetDirId = jsonObject.getString("targetDirId");
		if (StrUtils.isEmpty(targetDirId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("请选择要复制的目录或文件");
		}
		TcloudPanDirectory targetTcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(targetDirId);
		if (targetTcloudPanDirectory == null) {
			return R.error("选择的目录已不存在，请重试");
		}
		/*if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			return R.error("非法操作");
		}*/
		// 如果是课堂页面中操作云盘
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			// 如果课堂没有被移交
			if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					return R.error("非法操作");
				}
			}
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
					return R.error("非法操作，课堂已被移交，无权操作");
				}
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglPkgInfo.getCreateUserId();
				} else {
					return R.error("非法操作，没有权限");
				}
			}
		} else {
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (!hasOperatingAuthorization) {
				return R.error("没有权限，无法操作");
			} else {
				// 伪装者
				loginUserId = tevglPkgInfo.getCreateUserId();
			}
		}
		// 找到目标目录下的所有子目录
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("createUserId", loginUserId);
		map.put("pkgId", pkgId);
		map.put("sidx", "dict_code");
		map.put("order", "asc, create_time desc");
		List<Map<String,Object>> list = tcloudPanDirectoryMapper.selectSimpleListMap(map);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			String type = obj.getString("type");
			String id = obj.getString("id");
			if (id.equals(targetDirId)) {
				return R.error("不能将文件复制到自身或其子目录下");
			}
			// 如果当前选择的目录，是目标目录下的子目录
			if ("1".equals(type)) {
				List<String> dirIdList = new ArrayList<String>();
				getChildrenId(id, list, dirIdList);
				log.debug("找到被移动到的目录以及其子目录：" + dirIdList);
				if (dirIdList.contains(targetDirId)) {
					return R.error("不能将文件复制到自身或其子目录下");
				}
			}
		}
		try {
			// 查询条件
			Map<String, Object> params = new HashMap<>();
			// 获取
			String path = cloudPanUtils.getPath(targetDirId, loginUserId);
			log.debug("目标路径：" + path);
			// 遍历
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				String type = obj.getString("type");
				String id = obj.getString("id");
				// 复制目录
				if ("1".equals(type)) {
					TcloudPanDirectory directory = tcloudPanDirectoryMapper.selectObjectById(id);
					if (directory == null) {
						continue;
					}
					if (!loginUserId.equals(directory.getCreateUserId())) {
						continue;
					}
					// 当前目录
					String currentDir = cloudPanUtils.getUploadPath() + cloudPanUtils.getPath(id, pkgId);
					// 处理名称
					String tempName = directory.getName();
					String name = directory.getName();
					name = cloudPanUtils.handleDirectoryName(name, targetDirId, loginUserId, params);
					String savePath = cloudPanUtils.getSavePath(targetDirId, loginUserId, name, pkgId);
					//String savePath = cloudPanUtils.getUploadPath() + path + "/" + name;
					log.debug("当前目录：" + currentDir);
					log.debug("名称：" + name);
					log.debug("新路径：" + savePath);
					// 复制目录至目标目录下（目录和文件会一并带走，只需要更新数据库中文件记录的路径）
					try {
						// 旧目录
						File oldFile = new File(currentDir);
						// 新目录
						File newFile = new File(savePath);
						if (!newFile.exists()) {
							newFile.mkdirs();
						}
						FileUtils.copyDirectory(oldFile, newFile);
					} catch (IOException e) {
						e.printStackTrace();
						log.error("磁盘文件复制出错：", e);
						return R.error("系统错误");
					}
					// 入库
					TcloudPanDirectory t = new TcloudPanDirectory();
					t.setDirId(Identities.uuid());
					t.setPkgId(pkgId);
					t.setParentId(targetDirId);
					t.setName(name);
					t.setIntr(directory.getIntr());
					t.setCreateUserId(loginUserId);
					t.setUpdateUserId(loginUserId);
					t.setCreateTime(DateUtils.getNowTimeStamp());
					t.setUpdateTime(DateUtils.getNowTimeStamp());
					t.setState("Y");
					/*// 更新refDirId
					String refDirId = null;
					if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
						params.clear();
						params.put("name", tempName);
						params.put("pkgId", tevglPkgInfo.getRefPkgId());
						params.put("parentId", targetTcloudPanDirectory.getRefDirId());
						List<TcloudPanDirectory> resList = tcloudPanDirectoryMapper.selectListByMap(params);
						if (resList != null && resList.size() > 0) {
							refDirId = resList.get(0).getDirId();
						}
						directory.setRefDirId(refDirId);
					}*/
					tcloudPanDirectoryMapper.insert(t);
					// 插入本目录的文件相关数据
					doInsertFileInfo(id, t.getDirId(), loginUserId, params);
					// 插入子目录里的文件相关数据
					doInsertChildrenFileInfo(id, t.getDirId(), loginUserId, params);
				}
				// 复制文件
				if ("2".equals(type)) {
					TcloudPanFile tcloudPanFile = tcloudPanFileMapper.selectObjectById(id);
					if (tcloudPanFile != null && loginUserId.equals(tcloudPanFile.getCreateUserId())) {
						// 如果实际没有复制目录
						if (targetDirId.equals(tcloudPanFile.getDirId())) {
							//continue;
						}
						doCopyFileInfo(tcloudPanFile, targetDirId, path, loginUserId, params, pkgId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("云盘数据复制失败", e);
			return R.error("系统错误，复制失败");
		}
		return R.ok("复制成功");
	}
	
	/**
	 * 更新本目录的文件相关数据，path字段、file_access_url字段、file_save_path字段
	 * @param dirId
	 * @param params
	 */
	private void doInsertFileInfo(String dirId, String newDirId, String loginUserId,  Map<String, Object> params){
		params.clear();
		params.put("dirId", dirId);
		List<TcloudPanFile> list = tcloudPanFileMapper.selectListByMap(params);
		for (int i = 0; i < list.size(); i++) {
			TcloudPanFile tcloudPanFile = list.get(i);
			String name = tcloudPanFile.getName();
			String value = cloudPanUtils.getValue(newDirId, name);
			// 更新
			TcloudPanFile t = new TcloudPanFile();
			t = tcloudPanFile;
			t.setFileId(Identities.uuid());
			t.setDirId(newDirId);
			t.setFileAccessUrl(value);
			t.setFileSavePath(value);
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setUpdateTime(DateUtils.getNowTimeStamp());
			t.setSortNum(list.size() + i + 1);
			if (value != null) {
				if (cloudPanUtils.canBePdfSuffixList.contains(tcloudPanFile.getFileSuffix())) {
					t.setPath(value.substring(0, value.lastIndexOf(".")) + ".pdf");	
				}
			}
			tcloudPanFileMapper.insert(t);
		}
	}
	
	/**
	 * 递归修改子目录下的文件信息
	 * @param dirId
	 * @param parentId
	 * @param loginUserId
	 * @param params
	 */
	private void doInsertChildrenFileInfo(String dirId, String parentId, String loginUserId, Map<String, Object> params){
		params.clear();
		params.put("parentId", dirId);
		List<TcloudPanDirectory> directoryList = tcloudPanDirectoryMapper.selectListByMap(params);
		if (directoryList != null && directoryList.size() > 0) {
			// 子目录遍历
			directoryList.stream().forEach(directory -> {
				// 复制
				TcloudPanDirectory d = new TcloudPanDirectory();
				d.setDirId(Identities.uuid());
				d.setPkgId(directory.getPkgId());
				d.setParentId(parentId);
				d.setName(directory.getName());
				d.setIntr(directory.getIntr());
				d.setCreateUserId(loginUserId);
				d.setUpdateUserId(loginUserId);
				d.setCreateTime(DateUtils.getNowTimeStamp());
				d.setUpdateTime(DateUtils.getNowTimeStamp());
				d.setState("Y");
				tcloudPanDirectoryMapper.insert(d);
				String id = d.getDirId();
				// 查找此目录下的文件
				params.clear();
				params.put("dirId", directory.getDirId());
				List<Map<String, Object>> fileInfoList = tcloudPanFileMapper.selectFileInfoListByDirId(directory.getDirId());
				if (fileInfoList != null && fileInfoList.size() > 0) {
					TcloudPanFile t = new TcloudPanFile();
					// 子目录下的文件遍历
					for (int i = 0; i < fileInfoList.size(); i++) {
						Map<String, Object> fileInfo = fileInfoList.get(i);
						String name = fileInfo.get("name").toString();
						//String value = cloudPanUtils.getValue(dirId, name);
						String value = cloudPanUtils.getValue(d.getDirId(), name);
						// 更新
						t.setFileId(Identities.uuid());
						t.setDirId(id);
						t.setName(name);
						t.setFileAccessUrl(value);
						t.setFileSavePath(value);
						t.setFileSuffix(StrUtils.isNull(fileInfo.get("fileSuffix")) ? null : fileInfo.get("fileSuffix").toString());
						t.setFileType(StrUtils.isNull(fileInfo.get("fileType")) ? null : fileInfo.get("fileType").toString());
						t.setFileSize((long)fileInfo.get("fileSize"));
						t.setOriginalFilename(fileInfo.get("originalFilename").toString());
						String time = DateUtils.getNowTimeStamp();
						t.setCreateUserId(loginUserId);
						t.setUpdateUserId(loginUserId);
						t.setCreateTime(time);
						t.setUpdateTime(time);
						t.setState("Y");
						t.setSortNum(fileInfoList.size() + (i+1));
						if (value != null) {
							if (fileInfo.get("fileSuffix") != null) {
								if (cloudPanUtils.canBePdfSuffixList.contains(fileInfo.get("fileSuffix").toString())) {
									t.setPath(value.substring(0, value.lastIndexOf(".")) + ".pdf");	
								}
							}
						}
						tcloudPanFileMapper.insert(t);
					}
				}
				// 如果还有子目录，递归处理
				doInsertChildrenFileInfo(directory.getDirId(), id, loginUserId, params);
			});
		}
	}
	
	/**
	 * 复制文件
	 * @param tcloudPanFile 文件信息
	 * @param targetDirId 被复制至哪个目录
	 * @param path 根据targetDirId获取到的路径
	 * @param loginUserId 当前登录用户
	 * @param params 查询条件
	 */
	private void doCopyFileInfo(TcloudPanFile tcloudPanFile, String targetDirId, String path, String loginUserId, Map<String, Object> params, String pkgId) {
		String name = tcloudPanFile.getName();
		name = cloudPanUtils.handleFileName(name, targetDirId, loginUserId, params);
		String value = cloudPanUtils.getValue(targetDirId, name);
		String savePath = cloudPanUtils.getSavePath(targetDirId, loginUserId, name, pkgId);
		String absolutePath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getFileSavePath());
		try {
			// 旧文件
			File oldFile = new File(absolutePath);
			// 新文件
			File newFile = new File(savePath);
			FileUtils.copyFile(oldFile, newFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 数据库记录更新
		TcloudPanFile t = new TcloudPanFile();
		t = tcloudPanFile;
		t.setFileId(Identities.uuid());
		t.setDirId(targetDirId);
		t.setName(name);
		t.setFileAccessUrl(value);
		t.setFileSavePath(value);
		String time = DateUtils.getNowTimeStamp();
		t.setCreateTime(time);
		t.setUpdateTime(time);
		t.setState("Y");
		// 处理PDF
		if (value != null) {
			if (cloudPanUtils.canBePdfSuffixList.contains(tcloudPanFile.getFileSuffix())) {
				int pos = value.lastIndexOf(".");
				if (pos > 0) {
					String suffix = value.substring(pos);
					if (cloudPanUtils.canBePdfSuffixList.contains(suffix)) {
						t.setPath(value.substring(0, pos) + ".pdf");
						int i = absolutePath.lastIndexOf(".");
						if (i > 0) {
							File oldPdfFile = new File(absolutePath.substring(0, i) + ".pdf");
							if (oldPdfFile.exists()) {
								try {
									File newPdfFile = new File(savePath.substring(0, savePath.lastIndexOf(".")) + ".pdf");
									FileUtils.copyFile(oldPdfFile, newPdfFile);
								} catch (IOException e) {
									log.error("pdf文件复制出错", e);
									e.printStackTrace();
								}
							}
						}
					}
				}
				
			}
		}
		tcloudPanFileMapper.insert(t);
	}
	
	/**
     * 按名称搜索（只在本目录下搜索目录和文件）（小程序专用）
     * @param name
     * @param loginUserId
     * @return
     */
	@Override
	public R search(String ctId, Object pkgId, String dirId, String name, String loginUserId) {
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("查看失败");
		}
		// 最终返回数据
		List<Map<String, Object>> allList = new ArrayList<>();
		
		Map<String, Object> params = new HashMap<>();
		//params.put("createUserId", loginUserId);
		params.put("dirId", dirId);
		params.put("name", name);
		params.put("pkgId", pkgId);
		params.put("refPkgId", tevglPkgInfo.getRefPkgId());
		List<Map<String,Object>> list = new ArrayList<>();
		if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			list = tcloudPanDirectoryMapper.search(params);
		}else {
			// 当前课堂教学包的顶级目录
			params.clear();
			params.put("pkgId", tevglPkgInfo.getPkgId());
			params.put("parentId", "0");
			params.put("name", name);
			List<Map<String, Object>> currDirectoryList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
			// 查询来源教学包中的所有文件夹
			params.clear();
			params.put("pkgId", tevglPkgInfo.getRefPkgId());
			params.put("parentId", "0");
			params.put("name", name);
			List<Map<String, Object>> refDirectoryList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
			params.clear();
			params.put("pkgId", pkgId);
			params.put("owner", loginUserId);
			List<Map<String, Object>> viewPermissions = tcloudPanPermissionsMapper.selectListMapByMap(params);
			// 给教学包带过来的目录授权
			params.clear();
			params.put("pkgId", tevglPkgInfo.getRefPkgId());
			params.put("owner", loginUserId);
			List<Map<String, Object>> refPkgList = tcloudPanPermissionsMapper.selectListMapByMap(params);
			if (refPkgList != null && refPkgList.size() > 0) {
				viewPermissions.addAll(refPkgList);
			}
			log.debug("学生:" + loginUserId + "拥有的权限：" + viewPermissions.size());
			if (viewPermissions != null && viewPermissions.size() > 0) {
				List<Object> dirIds = viewPermissions.stream().map(a -> a.get("dir_id")).distinct().collect(Collectors.toList());
				System.out.println("dirIds: " + dirIds);
				// 将来源教学包中的顶级目录，与权限表中的目录id，相比较
				// 如果在里面，则加进去
				log.debug("拥有权限：" + dirIds);
				log.debug("根据条件查询来源教学包中的顶级目录：" + refDirectoryList.size());
				for (int i = 0; i < dirIds.size(); i++) {
					for (Map<String, Object> info : refDirectoryList) {
						if (dirIds.get(i).equals(info.get("dirId"))) {
							allList.add(info);
						}
					}
					for (Map<String, Object> info : currDirectoryList) {
						if (dirIds.get(i).equals(info.get("dirId"))) {
							allList.add(info);
						}
					}
				}
			}
		}
		
		if (list.size() > 0) {
			allList.addAll(list);
		}
		
		allList.stream().forEach(a -> {
			// 处理路径
			if (!StrUtils.isNull(a.get("type")) && "2".equals(a.get("type"))) {
				String targetPkgId = pkgId.toString();
				if (!pkgId.equals(a.get("pkgId"))) {
					targetPkgId = a.get("pkgId").toString();
				}
				a.put("path", cloudPanUtils.getFilePathByValue(targetPkgId, a.get("path")));
				a.put("fileAccessUrl", cloudPanUtils.getFileAccessUrlByValue(targetPkgId, a.get("fileAccessUrl")));
			}
			// 操作按钮权限处理
			handlePermission(a, tevglPkgInfo, loginUserId);
		});
		return R.ok().put(Constant.R_DATA, list);
	}
	
	private void handlePermission(Map<String,Object> info, TevglPkgInfo tevglPkgInfo, String loginUserId) {
		if ("refPkg".equals(info.get("pkgType"))) {
			info.put("hasOperPermission", false);
			return;
		}
		if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			info.put("hasOperPermission", false);
			return;
		}
		// 是否有操作权限
		if (tevglPkgInfo.getPkgId().equals(info.get("pkgId"))) {
			info.put("hasOperPermission", true);	
		} else {
			info.put("hasOperPermission", false);
		}	
	}

	/**
	 * PC网站端专用，顶级目录列表
	 * @param params
	 * @param loginUserId
	 * @param queryForWx
	 * @return
	 */
	@Override
	public R queryDirListNew( Map<String, Object> params, String loginUserId, boolean queryForWx) {
		// 课堂id，非必传，需兼容教学包页面、课堂页面（注意：为空表示在教学包页面中查看云盘，不为空表示在课堂页面中查看云盘）
		String ctId = StrUtils.isNull(params.get("ctId")) ? null : params.get("ctId").toString();
		// 教学包id
		String pkgId = StrUtils.isNull(params.get("pkgId")) ? null : params.get("pkgId").toString();
		// 文件夹id
		String dirId = StrUtils.isNull(params.get("dirId")) ? null : params.get("dirId").toString();
		// 按名称搜
		String name = StrUtils.isNull(params.get("name")) ? null : params.get("name").toString();
		// 合法性校验
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isNull(params.get("pkgId"))) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无法查看");
		}
		// 是否在课堂页面查看云盘标识
		boolean isInRoomView = StrUtils.isNotEmpty(ctId) && ctId.length() == 32;
		// 教学包是否被使用了
		boolean isBeingUsed = false;
		// 创建者标识
		boolean isCreator = loginUserId.equals(tevglPkgInfo.getCreateUserId());
		// 如果是在课堂页面
		if (isInRoomView) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglPkgInfo.getCreateUserId();
					isCreator = true;
				} else {
					isCreator = false;
				}
			}
		}
		// 最终返回数据
		List<Map<String, Object>> allList = new ArrayList<>();
		Map<String, Object> otherInfo = new HashMap<>();
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			otherInfo.put("hasOperPermission", false);
			isBeingUsed = pkgUtils.isBeingUsed(pkgId);
		}
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> pkgIds = new ArrayList<String>();
		pkgIds.add(pkgId);
		// 不为空，则是课堂页面里，则需要查询额外的
		if (StrUtils.isNotEmpty(ctId)) {
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && !tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
				if (!pkgIds.contains(tevglPkgInfo.getRefPkgId())) {
					pkgIds.add(tevglPkgInfo.getRefPkgId());
				}
			}
		}
		// 从字典获取默认
		List<Map<String,Object>> dictList = dictService.getDictList("defaultCloudPan");
		// 处理默认分组
		doCreateDefaultDirectory(tevglPkgInfo, dictList, loginUserId, params);
		// 课堂详情页面中云盘页面时，生成快捷面板
		doCreateFastNar(params.get("ctId"), pkgId);
		
		// 当前登录用户拥有的文件夹权限
		List<String> dirPermissionList = new ArrayList<String>();
		// 当前登录用户拥有的文件权限
		List<String> filePermissionList = new ArrayList<String>();
		// 存放顶级目录的集合
		List<Map<String,Object>> list = new ArrayList<>();
		// 如果是非创建者，则去获取当前登录用户的权限
		if (!isCreator && isInRoomView) {
			map.clear();
			//map.put("pkgId", tevglPkgInfo.getRefPkgId());
			map.put("pkgIds", pkgIds);
			map.put("owner", loginUserId);
			//map.put("dirIdIsNotNull", "Y");
			List<Map<String, Object>> permissionList = tcloudPanPermissionsMapper.selectListMapByMap(map);
			log.debug("查看云盘数据时，登录用户的权限，如果没有权限就直接返回。是否有权限：" + permissionList.size());
			if (permissionList == null || permissionList.size() == 0) {
				return R.ok().put(Constant.R_DATA, allList).put("otherInfo", otherInfo);
			}
			dirPermissionList = permissionList.stream().filter(a -> !StrUtils.isNull(a.get("dir_id"))).map(a -> a.get("dir_id").toString()).collect(Collectors.toList());
			filePermissionList = permissionList.stream().filter(a -> !StrUtils.isNull(a.get("file_id"))).map(a -> a.get("file_id").toString()).collect(Collectors.toList());
			log.debug("用户拥有的目录权限：" + dirPermissionList);
			log.debug("用户拥有的文件权限：" + filePermissionList);
		}
		// 注意点！！！
		if (StrUtils.isEmpty(dirId) && StrUtils.isEmpty(name)) {
			/*map.clear();
            map.put("state", "Y");
            map.put("sourcePkgId", pkgId);
            map.put("pkgIds", pkgIds);
            map.put("name", name);
            map.put("dirIds", dirPermissionList);
            map.put("sidx", "t1.dict_code");
            map.put("order", "asc, t1.create_time desc");
            list = tcloudPanDirectoryMapper.selectTopDirectoryListNew(map);*/
            map.put("dirIds", dirPermissionList);
            map.put("pkgId", pkgId);
            if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && !pkgId.equals(tevglPkgInfo.getRefPkgId()) && "N".equals(tevglPkgInfo.getReleaseStatus())) {
                    map.put("refPkgId", tevglPkgInfo.getRefPkgId());
            }
            list = tcloudPanDirectoryMapper.queryTopDirectoryList(map);
            
            log.debug("查询条件：" + map);
            log.debug("查询结果：" + list.size());
            // 非课堂创建者时
            if (isInRoomView && !isCreator) {
            	// 学生端不展示重复的默认文件夹
            	filterList(list, pkgId);
            }
		} else {
			if (!isCreator) {
				// 如果
				if (StrUtils.isNotEmpty(dirId) && !dirPermissionList.contains(dirId)) {
					return R.ok().put(Constant.R_DATA, new ArrayList<>());
				}
			}
			//Map<String, Object> parmas = getImportQueryParameters(tevglPkgInfo, dirId);
			Map<String, Object> parmas = new HashMap<>();
			/*pkgIds.add(pkgId);
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && "N".equals(tevglPkgInfo.getReleaseStatus())) {
				pkgIds.add(tevglPkgInfo.getRefPkgId());
			}*/
			List<String> dirIds = new ArrayList<>();
			if (StrUtils.isNotEmpty(dirId)) {
				dirIds.add(dirId);
				TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
				if (tcloudPanDirectory != null) {
					dirIds.add(tcloudPanDirectory.getRefDirId());
				}
			} else {
				if (!isCreator) {
					dirIds.addAll(dirPermissionList);
				}
			}
			parmas.put("pkgIds", pkgIds);
			parmas.put("dirIds", dirIds);
			//parmas.put("dirIds", dirPermissionList);
			parmas.put("name", name); // 按名称搜索
			parmas.put("fileIds", filePermissionList);
			if (StrUtils.isEmpty(dirId)) {
				if (queryForWx) {
					dirIds.add("0"); // 小程序，又需要查出parentId为0的
				} else {
					// 没传这个表示查询的顶级目录下的所有目录和文件，传了就只查询非顶级的目录
					parmas.put("parentIdNotZero", "Y");
				}
				// 查询直属于教学包下的文件
				parmas.put("queryTopFile", "Y");
			}
			list = tcloudPanDirectoryMapper.selectListByUnionAllNew(parmas);
			log.debug("查询条件：" + parmas);
			log.debug("查询结果：" + list.size());
			// 处理访问路径，操作按钮权限等
			handle(tevglPkgInfo, list);
			// 重新排序下优先展示文件夹
			list = list.stream().sorted((x, y) -> x.get("type").toString().compareTo(y.get("type").toString())).collect(Collectors.toList());
		}
		if (list.size() > 0) {
			allList.addAll(list);
		}
		// 小程序的情况，还要去查询直接归属于教学包的文件
		if (queryForWx) {
			map.clear();
			map.put("dirIds", pkgIds);
			map.put("name", name);
			List<Map<String, Object>> fileList =  tcloudPanFileMapper.selectSimpleInfoList(map);
			if (fileList != null && fileList.size() > 0) {
				fileList.stream().forEach(a -> {
					a.put("pkgId", a.get("dirId"));
					// 处理路径
					if (!StrUtils.isNull(a.get("type")) && "2".equals(a.get("type"))) {
						String targetPkgId = pkgId;
						if (!pkgId.equals(a.get("pkgId"))) {
							targetPkgId = a.get("pkgId").toString();
						}
						a.put("path", cloudPanUtils.getFilePathByValue(targetPkgId, a.get("path")));
						a.put("fileAccessUrl", cloudPanUtils.getFileAccessUrlByValue(targetPkgId, a.get("fileAccessUrl")));
					}
				});
				allList.addAll(fileList);
			}
		}
		// 处理操作按钮权限等
		handleImportPersmission(pkgId, allList, isBeingUsed, isInRoomView, isCreator);
		return R.ok().put(Constant.R_DATA, allList).put("otherInfo", otherInfo);
	}
	
	/**
	 * 处理操作按钮权限等
	 * @param pkgId
	 * @param allList
	 * @param isBeingUsed
	 */
	private void handleImportPersmission(String pkgId, List<Map<String, Object>> allList, boolean isBeingUsed, boolean isRoomPage, boolean isCreator) {
		if (allList == null || allList.size() == 0) {
			return;
		}
		Map<String, Object> map = new HashMap<>();
		for (Map<String, Object> a : allList) {
			if (pkgId.equals(a.get("pkgId"))) {
				a.put("hasOperPermission", true);
				// 如果此教学包已经发布，则不允许
				if (isBeingUsed) {
					a.put("hasOperPermission", false);
				}
				a.put("refPkgDir", false);
			} else {
				
				a.put("isCreator", isCreator); // 创建者
				a.put("isRoomPage", isRoomPage); // 是否在课堂页面
				
				a.put("refPkgDir", true);
				// 来源教学部包的情况
				a.put("hasOperPermission", false);
				/*// 如果来源教学包中的与当前自己的教学包的目录重名
				map.clear();
				map.put("pkgId", pkgId);
				map.put("parentId", "0");
				// 名称切记不要有模糊查询
				map.put("name", a.get("name"));
				List<Map<String, Object>> dirList = tcloudPanDirectoryMapper.selectListMapByMap(map);
				
				if (dirList != null && dirList.size() > 0) {
					a.put("id", dirList.get(0).get("dir_id"));
					a.put("dirId", dirList.get(0).get("dir_id"));
					a.put("pkgId", dirList.get(0).get("pkg_id"));
					a.put("updateTime", dirList.get(0).get("update_time"));
					a.put("hasOperPermission", true);
				}*/
			}
		}
	}

	/**
	 * 云盘列表（小程序专属）（区别开，便于维护）
	 * @param params
	 * @param loginUserId
	 * @return
	 * @apiNote 小程序，云盘主界面调用的是这个接口，按名称搜索的时候，调用的也是这个接口，dirId是没有传值的
	 */
	@Override
	public R queryDirListNewForWx(Map<String, Object> params, String loginUserId) {
		// 课堂id，非必传，需兼容教学包页面、课堂页面（注意：为空表示在教学包页面中查看云盘，不为空表示在课堂页面中查看云盘）
		String ctId = StrUtils.isNull(params.get("ctId")) ? null : params.get("ctId").toString();
		// 教学包id
		String pkgId = StrUtils.isNull(params.get("pkgId")) ? null : params.get("pkgId").toString();
		// 文件夹id
		String dirId = StrUtils.isNull(params.get("dirId")) ? null : params.get("dirId").toString();
		// 按名称搜
		String name = StrUtils.isNull(params.get("name")) ? null : params.get("name").toString();
		// 合法性校验
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isNull(params.get("pkgId"))) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无法查看");
		}
		// 是否在课堂页面查看云盘标识
		boolean isInRoomView = StrUtils.isNotEmpty(ctId) && ctId.length() == 32;
		// 教学包是否被使用了
		boolean isBeingUsed = false;
		// 创建者标识
		boolean isCreator = loginUserId.equals(tevglPkgInfo.getCreateUserId());
		// 如果是在课堂页面
		if (isInRoomView) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglPkgInfo.getCreateUserId();
					isCreator = true;
				} else {
					isCreator = false;
				}
			}
		}
		// 最终返回数据
		List<Map<String, Object>> allList = new ArrayList<>();
		Map<String, Object> otherInfo = new HashMap<>();
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			otherInfo.put("hasOperPermission", false);
			isBeingUsed = pkgUtils.isBeingUsed(pkgId);
		}
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> pkgIds = new ArrayList<String>();
		pkgIds.add(pkgId);
		// 不为空，则是课堂页面里，则需要查询额外的
		if (StrUtils.isNotEmpty(ctId)) {
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && !tevglPkgInfo.getPkgId().equals(tevglPkgInfo.getRefPkgId())) {
				if (!pkgIds.contains(tevglPkgInfo.getRefPkgId())) {
					pkgIds.add(tevglPkgInfo.getRefPkgId());
				}
			}
		}
		// 从字典获取默认
		List<Map<String,Object>> dictList = dictService.getDictList("defaultCloudPan");
		// 处理默认分组
		doCreateDefaultDirectory(tevglPkgInfo, dictList, loginUserId, params);
		// 课堂详情页面中云盘页面时，生成快捷面板
		doCreateFastNar(params.get("ctId"), pkgId);
		// 当前登录用户拥有的文件夹权限
		List<String> dirPermissionList = new ArrayList<String>();
		// 当前登录用户拥有的文件权限
		List<String> filePermissionList = new ArrayList<String>();
		// 如果是非创建者，则去获取当前登录用户的权限
		if (!isCreator && isInRoomView) {
			map.clear();
			map.put("pkgIds", pkgIds);
			map.put("owner", loginUserId);
			List<Map<String, Object>> permissionList = tcloudPanPermissionsMapper.selectListMapByMap(map);
			log.debug("查看云盘数据时，登录用户的权限，如果没有权限就直接返回。是否有权限：" + permissionList.size());
			if (permissionList == null || permissionList.size() == 0) {
				return R.ok().put(Constant.R_DATA, allList).put("otherInfo", otherInfo);
			}
			dirPermissionList = permissionList.stream().filter(a -> !StrUtils.isNull(a.get("dir_id"))).map(a -> a.get("dir_id").toString()).collect(Collectors.toList());
			filePermissionList = permissionList.stream().filter(a -> !StrUtils.isNull(a.get("file_id"))).map(a -> a.get("file_id").toString()).collect(Collectors.toList());
			log.debug("用户拥有的目录权限：" + dirPermissionList);
			log.debug("用户拥有的文件权限：" + filePermissionList);
		}
		List<Map<String, Object>> list = new ArrayList<>();
		if (StrUtils.isEmpty(dirId) && StrUtils.isEmpty(name)) {
			/*map.clear();
			map.put("state", "Y");
			map.put("sourcePkgId", pkgId);
			map.put("pkgIds", pkgIds);
			map.put("name", name);
			map.put("dirIds", dirPermissionList);
			map.put("sidx", "t1.dict_code");
			map.put("order", "asc, t1.create_time desc");
			list = tcloudPanDirectoryMapper.selectTopDirectoryListNew(map);*/
			map.put("dirIds", dirPermissionList);
            map.put("pkgId", pkgId);
            if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && !pkgId.equals(tevglPkgInfo.getRefPkgId()) && "N".equals(tevglPkgInfo.getReleaseStatus())) {
                    map.put("refPkgId", tevglPkgInfo.getRefPkgId());
            }
            list = tcloudPanDirectoryMapper.queryTopDirectoryList(map);
            tcloudPanFileMapper.queryAllFile(map);
			log.debug("查询条件：" + map);
			log.debug("查询结果：" + list.size());
			map.clear();
			map.put("sonFiles", filePermissionList);
			map.put("pkgIds", Arrays.asList(tevglPkgInfo.getPkgId(), tevglPkgInfo.getRefPkgId()));
			List<Map<String, Object>> topFileList = tcloudPanFileMapper.queryTopFileList(map);
			handle(tevglPkgInfo, topFileList);
			list.addAll(topFileList);
		} else {
			Map<String, Object> parmas = new HashMap<>();
			List<String> dirIds = new ArrayList<>();
			parmas.put("pkgIds", pkgIds);
			parmas.put("dirIds", dirIds);
			parmas.put("name", name); // 按名称搜索
			parmas.put("fileIds", filePermissionList);
			if (StrUtils.isEmpty(dirId)) { // 这里只允许dirId为空
				if (!isCreator) {
					dirIds.addAll(dirPermissionList);
				}
				// 小程序，又需要查出parentId为0的
				dirIds.add("0");
				// 查询直属于教学包下的文件
				parmas.put("queryTopFile", "Y");
				list = tcloudPanDirectoryMapper.selectListByUnionAllNew(parmas);
				log.debug("查询条件：" + parmas);
				log.debug("查询结果：" + list.size());
				// 处理访问路径，操作按钮权限等
				handle(tevglPkgInfo, list);
				// 重新排序下优先展示文件夹
				list = list.stream().sorted((x, y) -> x.get("type").toString().compareTo(y.get("type").toString())).collect(Collectors.toList());
			} else {
				/*dirIds.add(dirId);
				TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
				if (tcloudPanDirectory != null) {
					dirIds.add(tcloudPanDirectory.getRefDirId());
				}*/
			}
		}
		// 处理操作按钮权限等
		handleImportPersmission(pkgId, list, isBeingUsed, true, isCreator);
		// 返回结果
		return R.ok().put(Constant.R_DATA, list).put("otherInfo", otherInfo);
	}
	
	/**
	 * 过滤掉重复的字典文件文件夹，如果dictCode为1、2、3、4、5......的
	 * @param pkgId
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> filterRepetition(String pkgId, List<Map<String, Object>> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		// 找到当前教学包的字典文件夹
		List<Map<String, Object>> currentList = list.stream()
			.filter(a -> a.get("pkgId").equals(pkgId) 
					&& !StrUtils.isNull(a.get("dictCode")))
			.collect(Collectors.toList());
		if (currentList == null || currentList.size() == 0) {
			return null;
		}
		for (int i = 0; i < currentList.size(); i++) {
			Map<String, Object> map = currentList.get(i);
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> info = list.get(j);
				if (!pkgId.equals(info.get("pkgId")) && !StrUtils.isNull(info.get("dictCode"))) {
					if (!StrUtils.isNull(map.get("refDirId")) && map.get("refDirId").equals(info.get("dirId"))) {
						list.remove(j);
						j --;
					}
				}
			}
		}
		return list;
	}
	
	private void filterList(List<Map<String, Object>> list, String pkgId) {
		// 找到当前教学包的默认文件夹
		List<Map<String, Object>> currentDefaultList = list.stream().filter(a -> pkgId.equals(a.get("pkgId")) && !StrUtils.isNull(a.get("dictCode"))).collect(Collectors.toList());
		if (currentDefaultList == null || currentDefaultList.size() == 0) {
			return;
		}
		// 找到
		List<Map<String, Object>> refDefaultList = list.stream().filter(a -> !pkgId.equals(a.get("pkgId")) && !StrUtils.isNull(a.get("dictCode"))).collect(Collectors.toList());
		if (refDefaultList == null || refDefaultList.size() == 0) {
			return;
		}
		List<String> idList = new ArrayList<>();
		for (Map<String, Object> o1 : currentDefaultList) {
			for (Map<String, Object> o2 : refDefaultList) {
				if (o1.get("dictCode").equals(o2.get("dictCode"))) {
					idList.add(o2.get("id").toString());
				}
			}
		}
		if (idList.size() == 0) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < idList.size(); j++) {
				// 如果匹配上了
				if (list.get(i).get("id").equals(idList.get(j))) {
					list.remove(i);
					i --;
					break;
				}
			}
		}
	}

	/**
     * 点击目录查询数据（教学包页面专用方法）（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param pkgId 教学包id
     * @param dirId 文件夹id或空（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param name 按名称查询
     * @param loginUserId 当前登录用户
     * @return
     */
	@Override
	public R viewDetailForPkgPage(String pkgId, String dirId, String name, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("查看失败");
		}
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
		Map<String,Object> params = new HashMap<>();
		params.put("userId", loginUserId);
		params.put("pkgId", pkgId);
		List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(params);
		boolean isAuthorizedUser = teamList != null && teamList.size() > 0;
		if (!isCreator && !isReceiver & !isAuthorizedUser) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>());
		}
		// 构建查询条件
		Map<String, Object> parmas = new HashMap<>();
		parmas.put("pkgIds", Arrays.asList(pkgId));
		parmas.put("name", name); // 按名称搜索
		// 注意点！！！
		if (StrUtils.isEmpty(dirId)) {
			// 没传这个表示查询的顶级目录下的所有目录和文件
			parmas.put("parentIdNotZero", "Y");
			// 查询直属于教学包下的文件
			parmas.put("queryTopFile", "Y");
			// 控制只查询顶级目录下的文件夹
			Map<String, Object> map = new HashMap<>();
			map.put("pkgIds", Arrays.asList(pkgId));
			map.put("parentId", "0");
			List<String> topIdList = tcloudPanDirectoryMapper.selectDirIdListByMap(map);
			if (topIdList.size() > 0) {
				parmas.put("parentIdList", topIdList);
			}
		} else {
			parmas.put("dirIds", Arrays.asList(dirId));
		}
		List<Map<String, Object>> list = tcloudPanDirectoryMapper.selectListByUnionAllNew(parmas);
		log.debug("查询条件：" + parmas);
		log.debug("查询结果：" + list.size());
		// 处理访问路径，操作按钮权限等
		handle(tevglPkgInfo, list);
		// 重新排序下优先展示文件夹
		list = list.stream().sorted((x, y) -> x.get("type").toString().compareTo(y.get("type").toString())).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, list);
	}

	/**
     * 点击目录查询数据（课堂页面专用方法）（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param ctId 课堂id
     * @param pkgId 课堂对应教学包id
     * @param dirId 文件夹id或空（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param name 按名称查询
     * @param loginUserId 当前登录用户
     * @return
     */
	@Override
	public R viewDetailForRoomPage(String ctId, String pkgId, String dirId, String name, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.ok("查看失败，无效的课堂").put(Constant.R_DATA, new ArrayList<>());
		}
		if (!tevglTchClassroom.getPkgId().equals(pkgId)) {
			return R.ok("参数错误").put(Constant.R_DATA, new ArrayList<>());
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.ok("查看失败").put(Constant.R_DATA, new ArrayList<>());
		}
		// 课堂创建者标识
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		// 如果课堂被移交了
		if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
			if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
				isRoomCreator = true;
			} else {
				isRoomCreator = false;
			}
		}
		// 课堂助教者标识
		//boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		// 最终返回数据
		List<Map<String, Object>> list = new ArrayList<>();
		// 非课堂创建者时，登录用户拥有的权限
		List<String> sonFiles = new ArrayList<>();
		if (!isRoomCreator) {
			// 调用方法，得到当前登录用户拥有的权限集合
			sonFiles = getTraineeHasPermissionList(ctId, pkgId, loginUserId);
			// 没有权限，则直接返回
			if (sonFiles == null || sonFiles.size() == 0) {
				return R.ok().put(Constant.R_DATA, list);
			}
		}
		// 构建查询条件
		Map<String, Object> parmas = new HashMap<>();
		parmas.put("name", name);
		parmas.put("sonFiles", sonFiles);
		// 表示是点击【全部】查看
		log.debug(StrUtils.isEmpty(dirId) + "");
		if (StrUtils.isEmpty(dirId)) {
			// 没传这个表示查询的顶级目录下的所有目录和文件
			parmas.put("parentIdNotZero", "Y");
			// 查询直属于教学包下的文件
			parmas.put("queryTopFile", "Y");
			// 当前和来源
			List<String> pkgIdList = new ArrayList<>();
			pkgIdList.add(pkgId);
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && "N".equals(tevglPkgInfo.getReleaseStatus())) {
				pkgIdList.add(tevglPkgInfo.getRefPkgId());
			}
			parmas.put("pkgIds", pkgIdList);
			// 控制只查询顶级目录下的文件夹
			Map<String, Object> map = new HashMap<>();
			map.put("pkgIds", pkgIdList);
			map.put("parentId", "0");
			List<String> topIdList = tcloudPanDirectoryMapper.selectDirIdListByMap(map);
			if (isRoomCreator) {
				parmas.put("parentIdList", topIdList);
			} else {
				List<String> parentIdList = new ArrayList<>();
				sonFiles.stream().forEach(a -> {
					topIdList.stream().forEach(b -> {
						if (a.equals(b)) {
							parentIdList.add(a);
						}
					});
				});
				parmas.put("parentIdList", parentIdList);
			}
		} else {
			TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
			if (tcloudPanDirectory == null) {
				return R.ok("已找不到此文件夹，无法查看").put(Constant.R_DATA, new ArrayList<>());
			}
			parmas.put("pkgIds", Arrays.asList(tcloudPanDirectory.getPkgId()));
			parmas.put("dirIds", Arrays.asList(tcloudPanDirectory.getDirId()));
		}
		list = tcloudPanDirectoryMapper.selectListByUnionAllNew(parmas);
		log.debug("查询条件：" + parmas);
		log.debug("查询结果：" + list.size());
		// 处理访问路径，操作按钮权限等
		handle(tevglPkgInfo, list);
		// 重新排序下优先展示文件夹
		list = list.stream().sorted((x, y) -> x.get("type").toString().compareTo(y.get("type").toString())).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 批量删除
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deletesNew(JSONObject jsonObject, String loginUserId) {
		String pkgId = jsonObject.getString("pkgId");
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("删除失败");
		}
		R r = checkIsPassForDelete(tevglPkgInfo, jsonObject);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 如果是课堂页面中操作云盘
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(pkgId);
		if (tevglTchClassroom != null) {
			// 如果课堂没有被移交
			if (StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
					return R.error("非法操作");
				}
			}
			// 如果课堂被移交了
			if (StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId())) {
				if (loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
					return R.error("非法操作，课堂已被移交，无权操作");
				}
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglPkgInfo.getCreateUserId();
				}
			}
		} else { // 否则是在教学包页面中操作云盘
			// 没有权限，无法操作教学包
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (!hasOperatingAuthorization) {
				return R.error("没有权限，无法操作教学包");
			}
		}
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 查出当前教学包所有的目录和文件
		params.put("pkgIds", Arrays.asList(tevglPkgInfo.getPkgId()));
		params.put("queryTopFile", "Y");
		List<Map<String, Object>> allList = tcloudPanDirectoryMapper.selectListByUnionAllNew(params);
		log.debug("查出当前教学包所有的目录和文件:" + allList.size());
		// 查出所有权限记录
		params.clear();
		params.put("currentPkgId", pkgId);
		List<TcloudPanPermissions> allPermList = tcloudPanPermissionsMapper.selectListByMap(params);
		// 遍历
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		// 等待被删除的
		List<String> waitingDeleteDirIdList = new ArrayList<>();
		List<String> waitingDeleteFileIdList = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			String id = obj.getString("id");
			String type = obj.getString("type");
			// 取出当前信息
			List<Map<String, Object>> tcloudPanDirectoryList = allList.stream().filter(a -> a.get("id").equals(id)).collect(Collectors.toList());
			if (tcloudPanDirectoryList == null || tcloudPanDirectoryList.size() == 0) {
				continue;
			}
			// 等待被删除的权限记录
			List<String> waitingDeletePerIdList = new ArrayList<>();
			// 如果为目录
			if ("1".equals(type)) {
				Map<String, Object> directory = tcloudPanDirectoryList.get(0);
				// 递归
				handleChildren(directory.get("id").toString(), allList, waitingDeleteDirIdList, waitingDeleteFileIdList, allPermList, waitingDeletePerIdList, pkgId);
				// 找到目录下的文件
				List<Map<String, Object>> fileInfoList = allList.stream().filter(a -> "2".equals(a.get("type")) && a.get("parentId").equals(directory.get("id"))).collect(Collectors.toList());
				if (fileInfoList != null && fileInfoList.size() > 0) {
					fileInfoList.stream().forEach(fileInfo -> {
						// 数据库记录删除
						waitingDeleteFileIdList.add(fileInfo.get("id").toString());
						// 磁盘文件删除
						if (!StrUtils.isNull(fileInfo.get("fileSavePath"))) {
							File file = new File(cloudPanUtils.getFileSavePathByValue(pkgId, fileInfo.get("fileSavePath")));
							if (file.exists() && file.isFile()) {
								file.delete();
							}
						}
						// 处理删除pdf文件
						if (!StrUtils.isNull(fileInfo.get("fileSuffix"))) {
							if (cloudPanUtils.canBePdfSuffixList.contains(fileInfo.get("fileSuffix"))) {
								if (!StrUtils.isNull(fileInfo.get("path"))) {
									String pdfPath = cloudPanUtils.getFileSavePathByValue(pkgId, fileInfo.get("path"));
									File pdfFile = new File(pdfPath);
									if (pdfFile.exists()) {
										pdfFile.delete();
									}
								}
							}
						}
					});
				}
				// 然后，再从磁盘上删除当前目录
				//String pathBy = cloudPanUtils.getPathBy(directory.get("id").toString(), directory.get("pkgId").toString());
				String pathBy = cloudPanUtils.getAbsolutePath(directory.get("id").toString(), allList, pkgId);
				File file = new File(pathBy);
				if (file.exists() && file.isDirectory()) {
					file.delete();
				}
				// 然后，再从数据库中删除本记录
				waitingDeleteDirIdList.add(directory.get("id").toString());
				// TODO 权限操作
				if (allPermList.size() > 0) {
					List<String> thisPermList = allPermList.stream()
							.filter(a -> StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(directory.get("id")))
							.map(a -> a.getPerId())
							.collect(Collectors.toList());
					waitingDeletePerIdList.addAll(thisPermList);
				}
			}
			if ("2".equals(type)) {
				Map<String, Object> fileInfo = tcloudPanDirectoryList.get(0);
				// 数据库记录删除
				waitingDeleteFileIdList.add(fileInfo.get("id").toString());
				// 磁盘文件删除
				if (!StrUtils.isNull(fileInfo.get("fileSavePath"))) {
					File file = new File(cloudPanUtils.getFileSavePathByValue(pkgId, fileInfo.get("fileSavePath")));
					if (file.exists() && file.isFile()) {
						file.delete();
					}
				}
				// 处理删除pdf文件
				if (!StrUtils.isNull(fileInfo.get("fileSuffix"))) {
					if (cloudPanUtils.canBePdfSuffixList.contains(fileInfo.get("fileSuffix"))) {
						if (!StrUtils.isNull(fileInfo.get("path"))) {
							String pdfPath = cloudPanUtils.getFileSavePathByValue(pkgId, fileInfo.get("path"));
							File pdfFile = new File(pdfPath);
							if (pdfFile.exists()) {
								pdfFile.delete();
							}
						}
					}
				}
				// TODO 权限操作
				if (allPermList.size() > 0) {
					List<String> thisPermList = allPermList.stream()
							.filter(a -> StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(fileInfo.get("id")))
							.map(a -> a.getPerId())
							.collect(Collectors.toList());
					waitingDeletePerIdList.addAll(thisPermList);
				}
			}
			if (waitingDeletePerIdList.size() > 0) {
				tcloudPanPermissionsMapper.deleteBatch(waitingDeletePerIdList.stream().toArray(String[]::new));
			}
		}
		if (waitingDeleteDirIdList.size() > 0) {
			tcloudPanDirectoryMapper.deleteBatch(waitingDeleteDirIdList.stream().toArray(String[]::new));
		}
		if (waitingDeleteFileIdList.size() > 0) {
			tcloudPanFileMapper.deleteBatch(waitingDeleteFileIdList.stream().toArray(String[]::new));
		}
		return R.ok("删除成功");
	}

	private void handleChildren(String id, List<Map<String,Object>> allList, List<String> waitingDeleteDirIdList, List<String> waitingDeleteFileIdList,
								List<TcloudPanPermissions> allPermList, List<String> waitingDeletePerIdList, String pkgId){
		// 取本目录下的直系子目录
		List<Map<String,Object>> directoryList = allList.stream().filter(a -> a.get("parentId").equals(id) && "1".equals(a.get("type"))).collect(Collectors.toList());
		//log.debug("目录：" + directoryList.stream().map(a -> a.get("name")).collect(Collectors.toList()));
		if (directoryList == null || directoryList.size() == 0) {
			return;
		}
		// 遍历目录
		directoryList.stream().forEach(directory -> {
			// 找到目录下的文件
			List<Map<String, Object>> fileInfoList = allList.stream().filter(a -> "2".equals(a.get("type")) && a.get("parentId").equals(directory.get("id"))).collect(Collectors.toList());
			//log.debug(directory.get("name") + "下的文件：" + fileInfoList);
			if (fileInfoList != null && fileInfoList.size() > 0) {
				// TODO 先删除当前目录下的文件
				fileInfoList.stream().forEach(fileInfo -> {
					// 数据库记录删除
					waitingDeleteFileIdList.add(fileInfo.get("id").toString());
					// 磁盘文件删除
					if (!StrUtils.isNull(fileInfo.get("fileSavePath"))) {
						File file = new File(cloudPanUtils.getFileSavePathByValue(pkgId, fileInfo.get("fileSavePath")));
						if (file.exists() && file.isFile()) {
							file.delete();
						}
					}
					// 处理删除pdf文件
					if (!StrUtils.isNull(fileInfo.get("fileSuffix"))) {
						if (cloudPanUtils.canBePdfSuffixList.contains(fileInfo.get("fileSuffix"))) {
							if (!StrUtils.isNull(fileInfo.get("path"))) {
								String pdfPath = cloudPanUtils.getFileSavePathByValue(pkgId, fileInfo.get("path"));
								File pdfFile = new File(pdfPath);
								if (pdfFile.exists()) {
									pdfFile.delete();
								}
							}
						}
					}
					// TODO 权限操作
					if (allPermList.size() > 0) {
						List<String> thisPermList = allPermList.stream()
								.filter(a -> StrUtils.isNotEmpty(a.getFileId()) && a.getFileId().equals(fileInfo.get("id")))
								.map(a -> a.getPerId())
								.collect(Collectors.toList());
						waitingDeletePerIdList.addAll(thisPermList);
					}
				});
			}
			// 递归，去删除当前目录下的子目录和文件
			handleChildren(directory.get("id").toString(), allList, waitingDeleteDirIdList, waitingDeleteFileIdList, allPermList, waitingDeletePerIdList, pkgId);
			// 然后，再从磁盘上删除当前目录
			//String pathBy = cloudPanUtils.getPathBy(directory.get("id").toString(), directory.get("pkgId").toString());
			String pathBy = cloudPanUtils.getAbsolutePath(directory.get("id").toString(), allList, pkgId);
			File file = new File(pathBy);
			if (file.exists() && file.isDirectory()) {
				file.delete();
			}
			// TODO 再从数据库删除目录记录
			waitingDeleteDirIdList.add(directory.get("id").toString());
			// TODO 权限操作
			if (allPermList.size() > 0) {
				List<String> thisPermList = allPermList.stream()
						.filter(a -> StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(directory.get("id")))
						.map(a -> a.getPerId())
						.collect(Collectors.toList());
				waitingDeletePerIdList.addAll(thisPermList);
			}
		});
	}

	/**
	 * 批量删除目录或文件时的校验
	 * @param tevglPkgInfo
	 * @param jsonObject
	 * @return
	 */
	private R checkIsPassForDelete(TevglPkgInfo tevglPkgInfo, JSONObject jsonObject){
		String pkgId = tevglPkgInfo.getPkgId();
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			if (pkgUtils.isBeingUsed(pkgId)) {
				return R.error("已经发布了的教学包中，不允许再删除文件夹或文件");
			}
		}
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("请选择要删除的文件夹或文件");
		}
		// TODO 做下判断，避免用户非法操作，删除了来源教学包中的文件
		if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
			Map<String, Object> map = new HashMap<>();
			map.put("pkgIds", Arrays.asList(tevglPkgInfo.getRefPkgId()));
			map.put("queryTopFile", "Y");
			List<Map<String, Object>> refDataList = tcloudPanDirectoryMapper.selectListByUnionAllNew(map);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				String id = obj.getString("id");
				boolean anyMatch = refDataList.stream().anyMatch(a -> a.get("id").equals(id));
				if (anyMatch) {
					return R.error("被删除的文件中，有不能删除的");
				}
			}
		}
		return R.ok();
	}

	/**
     * 文件夹上传
     * @param request
     * @param pkgId 必传参数，教学包id
     * @param dirId 选传参数，目录id，当该值未传时，上传至教学包id目录下
     * @param loginUserId 当前登录用户
     * @return
     */
	@Override
	public R uploadFolder(HttpServletRequest request, String pkgId, String dirId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("上传失败，数据异常");
		}
		/*if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			return R.error("非法操作");
		}*/
		R r = handleUploadPermission(tevglPkgInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 重新获取数据
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) r.get(Constant.R_DATA);
		loginUserId = data.get("loginUserId").toString();
		// 媒体文件
		MultipartHttpServletRequest multipartRequest = null;
		if (request instanceof MultipartHttpServletRequest) {
			multipartRequest = (MultipartHttpServletRequest)(request);
		}
		if (multipartRequest == null) {
			return R.error("抱歉，上传文件夹失败");
		}
		String dirNameTemp = "";
		if (StrUtils.isNotEmpty(dirId)) {
			TcloudPanDirectory directory = tcloudPanDirectoryMapper.selectObjectById(dirId);
			if (directory != null) {
				dirNameTemp = directory.getName();
			}
		}
		String now = DateUtils.getNow("yyyyMMdd_HHmmss");
		Map<String, Object> map = new HashMap<>();
		map.put("pkgId", pkgId);
		//List<TcloudPanDirectory> directoryList = tcloudPanDirectoryMapper.selectListByMap(map);
		List<Map<String, Object>> directoryList = tcloudPanDirectoryMapper.selectSimpleListMap(map);
		List<MultipartFile> files = multipartRequest.getFiles("file");
		try {
			// 构建父子关系
			List<Map<String, Object>> arrayList = createDirectory(files, directoryList, dirId, dirNameTemp, pkgId, loginUserId);
			return R.ok("上传成功").put(Constant.R_DATA, arrayList);
		} catch (Exception e) {
			log.error("上传失败", e);
			return R.error("系统异常");
		}
	}
	
	/**
	 * 建立具有父子关系的平级数据
	 * @param files
	 * @param directoryList
	 * @param dirId
	 * @param dirName
	 * @return
	 */
	private List<Map<String, Object>> createDirectory(List<MultipartFile> files, List<Map<String, Object>>  directoryList, String dirId, String dirName, String pkgId, String loginUserId){
		String existedName = "";
		if (StrUtils.isNotEmpty(dirId)) {
			existedName = cloudPanUtils.getAbsolutePath(dirId, directoryList, pkgId);
			String str = cloudPanUtils.uploadPathCloudPan + "/" + pkgId;
			existedName = existedName.substring(str.length(), existedName.length());
		}
		List<Map<String, Object>> arrayList = new ArrayList<>();
		String targetPid = StrUtils.isEmpty(dirId) ? "0" : dirId;
		String targetPname = StrUtils.isEmpty(dirName) ? "顶" : dirName;
		String now = DateUtils.getNow("yyyyMMdd_HHmmss");
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			// 文件源名称,注意：文件夹上传的时候，这里的源文件名称，是由目录和文件名称组成的，示例                                  // 百家姓/1.png    百家姓/张三/张三儿子/头像.png
			String originalFilename = file.getOriginalFilename();
			String firstName = originalFilename.substring(0, originalFilename.indexOf("/"));
			boolean hasSameName = directoryList.stream().anyMatch(a -> targetPid.equals(a.get("parentId")) && firstName.equals(a.get("name")));
			//log.debug("源文件名称：" + originalFilename);
			//log.debug("首个名称：" + firstName);
			//log.debug("是否有重复：" + hasSameName);
			if (hasSameName) {
				originalFilename = firstName + "_" + now + originalFilename.substring(firstName.length(), originalFilename.length());
			}
			try {
				String absolutePath = uploadPathCloudPan + "/" + pkgId + "/" + existedName + "/" + originalFilename.substring(0, originalFilename.lastIndexOf("/"));
				String fileName = originalFilename.substring(originalFilename.lastIndexOf("/") + 1, originalFilename.length());
				// 如果文件夹不存在，则先创建
				File f = new File(absolutePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				// 磁盘上传对应生成文件
				String savePath = absolutePath + "/" + fileName;
				log.debug("绝对路径：" + savePath);
				// 磁盘上生成文件
				File ff = new File(savePath);
				file.transferTo(ff);
				// 遍历
				String[] splits = originalFilename.split("/");
				for (int j = 0; j < splits.length; j++) {
					Map<String, Object> node = new HashMap<>();
					node.put("name", splits[j]);
					node.put("level", j);
					if (j == 0) {
						node.put("parentName", targetPname);
						node.put("type", "1");
					} else {
						node.put("parentName", splits[j-1]);
						node.put("type", "1");
					}
					// 文件的情况
					if (j == splits.length - 1) {
						node.put("type", "2");
						int pos = splits[j].lastIndexOf(".");
						if (pos > 0) {
							String ext = splits[j].substring(pos);
							node.put("fileSuffix", ext);
							node.put("fileType", getFileType(ext));
						}
						// 文件大小
						//node.put("fileSize", file.getSize());
						node.put("fileSize", ff.length());
						// 源文件名称
						node.put("originalFilename", splits[j]);
						// 路径
						node.put("fileSavePath", existedName + "/" + originalFilename);
						node.put("fileAccessUrl", existedName + "/" + originalFilename);
					}
					if (!arrayList.contains(node)) {
						arrayList.add(node);
					}
				}
			} catch (Exception e) {
				log.error("系统异常", e);
			}
		}
		// 等待入库的目录
		List<TcloudPanDirectory> insertDirectoryList = new ArrayList<>();
		// 等待入库的文件
		List<TcloudPanFile> insertFileList = new ArrayList<>();
		// 再次处理数据
		arrayList.stream().forEach(node -> {
			node.put("id", Identities.uuid());
			// 找到子节点，设置父id
			List<Map<String, Object>> children = arrayList.stream().filter(a -> a.get("parentName").equals(node.get("name"))).collect(Collectors.toList());
			if (children.size() > 0) {
				children.stream().forEach(item -> {
					item.put("parentId", node.get("id"));
				});
			}
			if (Integer.valueOf(0).equals(node.get("level"))) {
				node.put("parentId", targetPid);
			}
			if ("1".equals(node.get("type"))) {
				TcloudPanDirectory t = new TcloudPanDirectory();
				t.setDirId(node.get("id").toString());
				t.setName(node.get("name").toString());
				t.setPkgId(pkgId);
				t.setParentId(node.get("parentId").toString());
				t.setState("Y");
				t.setIcon("fa fa-file-o");
				t.setCreateTime(DateUtils.getNowTimeStamp());
				t.setCreateUserId(loginUserId);
				insertDirectoryList.add(t);
			}
			if ("2".equals(node.get("type"))) {
				TcloudPanFile t = new TcloudPanFile();
				t.setFileId(node.get("id").toString());
				t.setDirId(node.get("parentId").toString());
				t.setName(node.get("name").toString());
				t.setFileAccessUrl(node.get("fileAccessUrl").toString());
				t.setFileSavePath(node.get("fileSavePath").toString());
				t.setFileSuffix(StrUtils.isNull(node.get("fileSuffix")) ? null : node.get("fileSuffix").toString());
				t.setFileSize(Long.valueOf(node.get("fileSize").toString()));
				t.setFileType(node.get("fileType").toString());
				t.setOriginalFilename(node.get("originalFilename").toString());
				t.setState("Y");
				t.setCreateTime(DateUtils.getNowTimeStamp());
				t.setCreateUserId(loginUserId);
				insertFileList.add(t);
			}
		});
		if (insertDirectoryList.size() > 0) {
			tcloudPanDirectoryMapper.insertBatch(insertDirectoryList);
		}
		if (insertFileList.size() > 0) {
			tcloudPanFileMapper.insertBatch(insertFileList);
		}
		return arrayList;
	}

	private String getFileType(String ext){
		// 文件类型
		String fileType = "file";
		if (cloudPanUtils.imageSuffixList.contains(ext.toUpperCase())) {
			fileType = "image";
		} else if(cloudPanUtils.videoSuffixList.contains(ext.toUpperCase())) {
			fileType = "video";
		} else if(cloudPanUtils.audioSuffixList.contains(ext.toUpperCase())) {
			fileType = "audio";
		} else if (cloudPanUtils.docsSuffixList.contains(ext.toUpperCase())) {
			fileType = "docs";
		}
		return fileType;
	}
}
