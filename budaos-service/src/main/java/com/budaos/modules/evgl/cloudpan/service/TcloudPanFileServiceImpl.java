package com.budaos.modules.evgl.cloudpan.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CloudPanUtils;
import com.budaos.modules.common.PkgUtils;
import com.budaos.modules.evgl.cloudpan.api.TcloudPanFileService;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class TcloudPanFileServiceImpl implements TcloudPanFileService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TcloudPanFileServiceImpl.class);
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	@Autowired
	private  TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private CloudPanUtils cloudPanUtils;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TcloudPanFile> tcloudPanFileList = tcloudPanFileMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tcloudPanFileList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tcloudPanFileList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tcloudPanFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	@GetMapping("/queryForMap")
	@SentinelResource("/cloudpan/tcloudpanfile/queryForMap")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tcloudPanFileList = tcloudPanFileMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tcloudPanFileList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tcloudPanFileList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tcloudPanFile
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	@PostMapping("save")
	@SentinelResource("/cloudpan/tcloudpanfile/save")
	public R save(@RequestBody(required = false) TcloudPanFile tcloudPanFile) throws BudaosException {
		tcloudPanFile.setFileId(Identities.uuid());
		tcloudPanFile.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tcloudPanFile.setCreateTime(DateUtils.getNowTimeStamp());
		//ValidatorUtils.check(tcloudPanFile);
		tcloudPanFileMapper.insert(tcloudPanFile);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tcloudPanFile
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	@PostMapping("update")
	@SentinelResource("/cloudpan/tcloudpanfile/update")
	public R update(@RequestBody(required = false) TcloudPanFile tcloudPanFile) throws BudaosException {
	    tcloudPanFile.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tcloudPanFile.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tcloudPanFile);
		tcloudPanFileMapper.update(tcloudPanFile);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	@GetMapping("delete/{id}")
	@SentinelResource("/cloudpan/tcloudpanfile/delete")
	public R delete(@PathVariable("id") String id) throws BudaosException {
		tcloudPanFileMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	@PostMapping("deleteBatch")
	@SentinelResource("/cloudpan/tcloudpanfile/deleteBatch")
	public R deleteBatch(@RequestParam(required = true) String[] ids) throws BudaosException {
		tcloudPanFileMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	@GetMapping("view/{id}")
	@SentinelResource("/cloudpan/tcloudpanfile/view")
	public R view(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tcloudPanFileMapper.selectObjectById(id));
	}
	
	@Override
	public List<TcloudPanFile> selectListByMap(Map<String, Object> params) {
		return tcloudPanFileMapper.selectListByMap(params);
	}

	@Override
	public void insert(TcloudPanFile tcloudPanFile) throws BudaosException {
		tcloudPanFileMapper.insert(tcloudPanFile);
	}

	@Override
	public Integer getMaxSortNum(Map<String, Object> params) {
		return tcloudPanFileMapper.getMaxSortNum(params);
	}
	
	/**
	 * 上传文件
	 * @param request
	 * @param dirId
	 * @param loginUserId
	 * @return
	 */
	@Override
	@PostMapping("/uploadFiles")
	public R uploadFiles(HttpServletRequest request, String dirId, String loginUserId, String pkgId) {
		if (StrUtils.isNull(request) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		R r = handleUploadPermission(tevglPkgInfo, loginUserId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 重新获取数据
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) r.get(Constant.R_DATA);
		loginUserId = data.get("loginUserId").toString();
		String type = request.getParameter("type"); // 标识是从白板那边上传的文件
		if ("baiban".equals(type)) {
			return uploadForBaiBan(request, pkgId, loginUserId);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				// 获取上传文件对象
				MultipartFile file = entity.getValue();
				if (file == null) {
					continue;
				}
				// 文件源类型(比如图片：image/jpeg、video/mp4)
				// 文件源名称
				String originalFilename = file.getOriginalFilename();
				String tempOriginalFilename = originalFilename;
				// 文件大小
				long fileSize = file.getSize();
				// 后缀名
				int pos = originalFilename.lastIndexOf(".");
				String ext = "";
				if (pos > 0) {
					ext = originalFilename.substring(pos);
				}
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
				// 在【全部】目录下上传，会直接上传到教学包id目录下
				if (StrUtils.isEmpty(dirId)) {
					// 如果同目录下已经存在了此名称
					params.clear();
					params.put("dirId", pkgId);
					params.put("createUserId", loginUserId);
					List<TcloudPanFile> list =  tcloudPanFileMapper.selectListByMap(params);
					if (list.stream().anyMatch(a -> a.getName().equals(tempOriginalFilename))) {
						// 则在后面追加_年月日_时分秒
						String now = DateUtils.getNow("yyyyMMdd_HHmmss");
						if (pos > 0) {
							originalFilename = originalFilename.substring(0, pos) + "_" + now + ext;
						}
					}
				} else {
					// 如果同目录下已经存在了此名称
					params.clear();
					params.put("dirId", dirId);
					params.put("createUserId", loginUserId);
					List<TcloudPanFile> list =  tcloudPanFileMapper.selectListByMap(params);
					if (list.stream().anyMatch(a -> a.getName().equals(tempOriginalFilename))) {
						// 则在后面追加_年月日_时分秒
						if (pos > 0) {
							String now = DateUtils.getNow("yyyyMMdd_HHmmss");
							originalFilename = originalFilename.substring(0, pos) + "_" + now + ext;
						}
					}	
				}
				// 获取路径
				String value = "";
				String pathBy = "";
				if (StrUtils.isEmpty(dirId)) {
					value = "/" + originalFilename;
					pathBy = cloudPanUtils.getPathBy(dirId, loginUserId, pkgId);
				}else {
					value = cloudPanUtils.getValue(dirId, originalFilename);
					pathBy = cloudPanUtils.getPathBy(dirId, loginUserId, pkgId);
				}
				File f = new File(pathBy);
				if (!f.exists()) {
					f.mkdirs();
				}
				String savePath = pathBy + "/" + originalFilename;
				//String savePath = cloudPanUtils.getSavePath(dirId, loginUserId, originalFilename, pkgId);
				// 磁盘上传对应生成文件
				file.transferTo(new File(savePath));
				// 文件信息入库
				TcloudPanFile tcloudPanFile = new TcloudPanFile();
				tcloudPanFile.setFileId(Identities.uuid());
				if (StrUtils.isEmpty(dirId)) { // 在全部目录上传的情况
					tcloudPanFile.setDirId(pkgId);
				}else { // 不在全部目录上传的情况
					tcloudPanFile.setDirId(dirId);
				}
				tcloudPanFile.setName(originalFilename);
				tcloudPanFile.setFileSavePath(value);
				tcloudPanFile.setFileAccessUrl(value);
				tcloudPanFile.setFileSuffix(ext);
				tcloudPanFile.setFileSize(fileSize);
				tcloudPanFile.setFileType(fileType);
				tcloudPanFile.setOriginalFilename(originalFilename);
				tcloudPanFile.setCreateUserId(loginUserId);
				tcloudPanFile.setCreateTime(DateUtils.getNowTimeStamp());
				tcloudPanFile.setUpdateUserId(loginUserId);
				tcloudPanFile.setUpdateTime(DateUtils.getNowTimeStamp());
				tcloudPanFile.setState("Y");
				params.put("createUserId", loginUserId);
				params.put("parentId", dirId);
				Integer sortNum = tcloudPanFileMapper.getMaxSortNum(params);
				tcloudPanFile.setSortNum(sortNum);
				tcloudPanFileMapper.insert(tcloudPanFile);
			}
		} catch (Exception e) {
			log.error("上传失败", e);
			return R.error("系统异常");
		}
		return R.ok("上传成功");
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
			log.debug("课堂页面中，判断上传文件的权限...");
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
			log.debug("教学包页面，判断上传文件的权限...");
			// 否则就是在教学包页面中上传文件
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (hasOperatingAuthorization) {
				loginUserId = tevglPkgInfo.getCreateUserId();
			} else {
				return R.error("没有权限，无法上传");
			}
		}
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			if (pkgUtils.isBeingUsed(pkgId)) {
				return R.error("已经发布了的教学包，不允许再上传文件");
			}
		}
		// 返回数据
		data.put("loginUserId", loginUserId);
		return R.ok().put(Constant.R_DATA, data);
	}

	private R uploadForBaiBan(HttpServletRequest request, String pkgId, String loginUserId) {
		try {
			log.debug("教学白板图片上传....");
			MultipartHttpServletRequest multipartHttpServletRequest = null;
			if (request instanceof MultipartHttpServletRequest) {
				multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			}
			if (multipartHttpServletRequest == null) {
				return R.error("上传失败");
			}
			if (StrUtils.isEmpty(pkgId)) {
				return R.error("必传参数为空");
			}
			TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
			if (tevglPkgInfo == null) {
				return R.error("暂不支持上传");
			}
			String pId = getBaiBanDirId(pkgId, loginUserId);
			MultipartFile file = multipartHttpServletRequest.getFile("file");
			// 源文件名
			String originalFilename = file.getOriginalFilename();
			// 文件大小
			long fileSize = file.getSize();
			String fileType = "image";
			String fileSuffx = ".png";
			String fileName = DateUtils.getNow("yyyyMMdd_HHmmss") + fileSuffx;
			String savePath = cloudPanUtils.uploadPathCloudPan + "/" + pkgId + "/教学白板/" + fileName;
			log.debug("绝对路径：" + savePath);
			// 磁盘上传对应生成文件
			file.transferTo(new File(savePath));
			// 入库
			TcloudPanFile tcloudPanFile = new TcloudPanFile();
			tcloudPanFile.setFileId(Identities.uuid());
			tcloudPanFile.setName(fileName);
			tcloudPanFile.setDirId(pId);
			tcloudPanFile.setFileSavePath("/教学白板/" + fileName);
			tcloudPanFile.setFileAccessUrl("/教学白板/" + fileName);
			tcloudPanFile.setFileSuffix(fileSuffx);
			tcloudPanFile.setFileSize(fileSize);
			tcloudPanFile.setFileType(fileType);
			tcloudPanFile.setOriginalFilename(originalFilename);
			tcloudPanFile.setCreateUserId(loginUserId);
			tcloudPanFile.setCreateTime(DateUtils.getNowTimeStamp());
			tcloudPanFile.setUpdateUserId(loginUserId);
			tcloudPanFile.setUpdateTime(DateUtils.getNowTimeStamp());
			tcloudPanFile.setState("Y");
			Map<String, Object> params = new HashMap<>();
			params.put("createUserId", loginUserId);
			params.put("parentId", pId);
			Integer sortNum = tcloudPanFileMapper.getMaxSortNum(params);
			tcloudPanFile.setSortNum(sortNum);
			tcloudPanFileMapper.insert(tcloudPanFile);
			return R.ok("上传成功");
		} catch (Exception e) {
			log.error(e.toString());
			return R.error("系统异常，上传失败");
		}
	}
	
	private String getBaiBanDirId(String pkgId, String loginUserId) {
		Map<String, Object> map = new HashMap<>();
		map.put("pkgId", pkgId);
		map.put("parentId", "0");
		map.put("parentId", "0");
		map.put("dictCode", "99");
		List<TcloudPanDirectory> tcloudPanDirectoryList = tcloudPanDirectoryMapper.selectListByMap(map);
		if (tcloudPanDirectoryList.size() > 0) {
			return tcloudPanDirectoryList.get(0).getDirId();
		} else {
			TcloudPanDirectory t = new TcloudPanDirectory();
			t.setDirId(Identities.uuid());
			t.setName("教学白板");
			t.setPkgId(pkgId);
			t.setParentId("0");
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setCreateUserId(loginUserId);
			t.setState("Y");
			t.setDictCode("99");
			t.setIcon("fa fa-columns");
			tcloudPanDirectoryMapper.insert(t);
			String path = cloudPanUtils.uploadPathCloudPan + "/" + pkgId + "/教学白板";
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			return t.getDirId();
		}
	}
	
	/**
	 * 重命名文件
	 * @param fileId
	 * @param name
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R rename(String fileId, String name, String loginUserId, String pkgId) {
		if (StrUtils.isEmpty(fileId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(name)) {
			return R.error("名称不能为空");
		}
		name = name.trim();
		if (StrUtils.isEmpty(name)) {
			return R.error("文件名不能为空");
		}
		if (name.length() > 100) {
			return R.error("名称过长，不能超过100个字符");
		}
		String[] names = name.split("");
		for (int i=0; i < names.length; i++) {
			if (cloudPanUtils.unAllowedDirNameList.contains(names[i])) {
				return R.error("文件名不能包含下列任何字符：" + cloudPanUtils.unAllowedDirNameList.stream().collect(Collectors.joining(" ")));
			}
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("上传失败");
		}
		if ("Y".equals(tevglPkgInfo.getReleaseStatus())) {
			if (pkgUtils.isBeingUsed(pkgId)) {
				return R.error("已经发布了的教学包中，不允许再重命名文件夹");
			}
		}
		String tempOriginalFilename = name;
		TcloudPanFile tcloudPanFile = tcloudPanFileMapper.selectObjectById(fileId);
		if (tcloudPanFile == null) {
			return R.error("无效的文件");
		}
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
				if (loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
					return R.error("非法操作，课堂已被移交，无权操作");
				}
				// 如果登录用户是接收者，处理下
				if (loginUserId.equals(tevglTchClassroom.getReceiverUserId())) {
					loginUserId = tevglPkgInfo.getCreateUserId();
				} else {
					return R.error("非法操作，没有权限");
				}
			}
		} else { // 否则是在教学包页面中操作云盘
			boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
			if (hasOperatingAuthorization) {
				loginUserId = tevglPkgInfo.getCreateUserId();
			} else {
				return R.error("没有权限，无法操作");
			}
		}
		if (!"Y".equals(tcloudPanFile.getState())) {
			return R.error("该文件已被删除，重命名失败");
		}
		if (name.equals(tcloudPanFile.getName())) {
			return R.ok().put(Constant.R_DATA, fileId);
		}
		String dirId = tcloudPanFile.getDirId();
		int pos = name.lastIndexOf(".");
		String suffix = pos < 0 ? null : name.substring(pos);
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 如果同目录下已经存在了此名称
		params.clear();
		params.put("dirId", dirId);
		List<TcloudPanFile> list =  tcloudPanFileMapper.selectListByMap(params);
		if (list.stream().anyMatch(a -> !a.getFileId().equals(fileId) && a.getName().equals(tempOriginalFilename))) {
			return R.error("此目录下已存在同名文件，请重新命名");
			// 此时请弹出弹窗供用户选择，如果点击【确认】，再次请求，否则【取消】
			//return R.ok(520, "此目录下已存在同名文件，是否要保存两个文件");
		}
		// 获取路径
		String value = cloudPanUtils.getValue(dirId, name);
		String savePath = cloudPanUtils.getSavePath(dirId, loginUserId, name, pkgId);
		// 操作磁盘上的文件
		String absolutePath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getFileSavePath());
		File existedFile = new File(absolutePath);
		File newfile = new File(savePath);
		existedFile.renameTo(newfile);
		// 更新入库
		TcloudPanFile t = new TcloudPanFile();
		t.setFileId(fileId);
		t.setName(name);
		t.setFileAccessUrl(value);
		t.setFileSavePath(value);
		t.setFileSuffix(suffix);
		t.setFileType(cloudPanUtils.getFileType(name));
		t.setOriginalFilename(name);
		t.setUpdateUserId(loginUserId);
		t.setUpdateTime(DateUtils.getNowTimeStamp());
		tcloudPanFileMapper.updateForUpload(t);
		Map<String, Object> data = new HashMap<>();
		data.put("fileId", fileId);
		return R.ok("重命名成功").put(Constant.R_DATA, data);
	}

	/**
	 * 删除文件--------------废弃等待删除
	 * @param fileId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteFile(String pkgId, String fileId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(fileId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TcloudPanFile tcloudPanFile = tcloudPanFileMapper.selectObjectById(fileId);
		if (tcloudPanFile == null) {
			return R.error("无效的文件");
		}
		if (!loginUserId.equals(tcloudPanFile.getCreateUserId())) {
			return R.error("非法操作");
		}
		// 数据库记录删除
		tcloudPanFileMapper.delete(fileId);
		// 磁盘文件删除
		String absolutePath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getFileSavePath());
		if (StrUtils.isNotEmpty(absolutePath)) {
			File file = new File(tcloudPanFile.getFileSavePath());
			if (file.exists() && file.isDirectory()) {
				file.delete();
			}	
		}
		return R.ok("删除成功");
	}
	
	/**
	 * 批量删除
	 * @param fileIds
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteFileBatch(String pkgId, List<String> fileIds, String loginUserId) {
		if (fileIds == null || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("fileIds", fileIds);
		List<TcloudPanFile> fileList = tcloudPanFileMapper.selectListByMap(params);
		if (fileList.stream().anyMatch(a -> !loginUserId.equals(a.getCreateUserId()))) {
			return R.error("非法操作");
		}
		for (String fileId : fileIds) {
			TcloudPanFile tcloudPanFile = tcloudPanFileMapper.selectObjectById(fileId);
			if (tcloudPanFile != null) {
				if (!loginUserId.equals(tcloudPanFile.getCreateUserId())) {
					break;
				}
				// 磁盘文件删除
				String absolutePath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getFileSavePath());
				if (StrUtils.isNotEmpty(absolutePath)) {
					File file = new File(tcloudPanFile.getFileSavePath());
					if (file.exists() && file.isDirectory()) {
						file.delete();
					}	
				}
			}
		}
		return R.ok("删除成功");
	}

	@Override
	public R confirm(String fileId, String name, String loginUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 下载
	 * @param response
	 * @param fileId
	 * @return
	 */
	@Override
	public void downloadFile(HttpServletResponse response, String fileId, String pkgId) {
		if (StrUtils.isEmpty(pkgId)) {
			return;
		}
		TcloudPanFile file = tcloudPanFileMapper.selectObjectById(fileId);
		if(file == null) {
			return;
		}
		String absolutePath = null;
		// 注意点，有些文件可能直接归属在教学包id文件夹下，处理下
		if (file.getDirId().equals(pkgId)) {
			absolutePath = cloudPanUtils.uploadPathCloudPan + "/" + pkgId + file.getFileSavePath();
		} else {
			String targetPkgId = pkgId;
			TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(file.getDirId());
			if (tcloudPanDirectory == null) {
				targetPkgId = file.getDirId();
			} else {
				targetPkgId = tcloudPanDirectory.getPkgId();
			}
			absolutePath = cloudPanUtils.getFileSavePathByValue(targetPkgId, file.getFileSavePath());	
		}
		log.debug("文件磁盘路径为：" + absolutePath);
		if (absolutePath == null) {
			return;
		}
        // 实现文件下载
        byte[] buffer = new byte[1024];
        File f = new File(absolutePath);
        try(
        	FileInputStream fis = new FileInputStream(f);
        	BufferedInputStream bis = new BufferedInputStream(fis);
        	OutputStream os = response.getOutputStream();
        ){
        	response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
	}

}
