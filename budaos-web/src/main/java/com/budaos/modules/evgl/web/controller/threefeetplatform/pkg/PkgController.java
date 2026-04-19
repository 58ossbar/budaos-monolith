package com.budaos.modules.evgl.web.controller.threefeetplatform.pkg;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.common.CbUploadUtils;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.pkg.api.*;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.tch.api.TevglTchTeacherService;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 我的教学包
 * @author huj
 *
 */
@RestController
@RequestMapping("/pkginfo-api")
public class PkgController {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CbUploadUtils uploadUtils;
	@Autowired
	private TevglPkgInfoService tevglPkgInfoService;
	@Autowired
	private TevglTchTeacherService tevglTchTeacherService;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglBookpkgTeamDetailService tevglBookpkgTeamDetailService;
	@Autowired
	private TevglBookpkgTeamService tevglBookpkgTeamService;
	@Autowired
	private ReleaseTeachingPackageService releaseTeachingPackageService;
	@Autowired
	private DeletePackageService deletePackageService;
	@Autowired
	private TevglPkgCheckService tevglPkgCheckService;
	@Autowired
	private TevglPkgDefaultChapterService tevglPkgDefaultChapterService;
	
	@Value("${com.budaos.file-upload-path}")
    private String uploadPath;
	
	/**
	 * 获取筛选条件
	 * @return
	 */
	@RequestMapping("/getFilterTypeList")
	public R getFilterTypeList () {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> v0 = new HashMap<String, Object>();
		v0.put("label", "全部状态");
		v0.put("value", "");
		Map<String, Object> v1 = new HashMap<String, Object>();
		v1.put("label", "自建");
		v1.put("value", "ower");
		Map<String, Object> v2 = new HashMap<String, Object>();
		v2.put("label", "共建");
		v2.put("value", "together");
		Map<String, Object> v3 = new HashMap<String, Object>();
		v3.put("label", "授权");
		v3.put("value", "auth");
		Map<String, Object> v4 = new HashMap<String, Object>();
		v4.put("label", "其它");
		v4.put("value", "other");
		list.add(v0);
		list.add(v1);
		list.add(v2);
		list.add(v3);
		//list.add(v4);
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 附件上传
	 * @param file
	 * @return
	 */
	@PostMapping("/uploads")
	public R uploads(@RequestPart(name = "file", required = true) MultipartFile file) {
		return uploadUtils.uploads(file, "12", null, null);
	}
	
	/**
	 * 更换教学包封面
	 * @param request
	 * @param file
	 * @param pkgId
	 * @return
	 */
	@PostMapping("/updatePkgLogo")
	@CheckSession
	@Transactional
	public R updatePkgLogo(HttpServletRequest request,  @RequestPart(name = "file", required = true) MultipartFile file, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if (file == null || file.isEmpty()) {
			return R.error("封面更换失败");
		}
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoService.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("封面更换失败");
		}
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && traineeInfo.getTraineeId().equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && traineeInfo.getTraineeId().equals(tevglPkgInfo.getReceiverUserId());
		if (!isCreator && !isReceiver) {
			return R.error("封面更换失败，无权更换");
		}
		// 原文件类型
		String oldName = file.getContentType();
		// 扩展名
		String extension = "." + oldName.substring(oldName.lastIndexOf("/")+1); 
		if (!uploadUtils.imageSuffixList.contains(extension.toUpperCase())) {
			return R.error("格式只能为：" + uploadUtils.imageSuffixList);
		}
		try {
			// 图片上传
			R r = uploadUtils.uploads(file, "12", null, "images");
			Integer code = (Integer) r.get("code");
			if (code != 0) {
				return r;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> obj = (Map<String, Object>) r.get("data");
			String attachId = (String) obj.get("attachId");
			String fileName = (String) obj.get("fileName");
			// 更新数据库记录
			TevglPkgInfo t = new TevglPkgInfo();
			t.setPkgId(pkgId);
			t.setPkgLogo(fileName);
			t.setAttachId(attachId);
			tevglPkgInfoService.update(t);
			// 将原本磁盘上旧的封面删除掉
			String oldPicAbsolutePath = uploadUtils.getUploadPath() + uploadUtils.getPathByParaNo("12") + "/" + tevglPkgInfo.getPkgLogo();
			if (StrUtils.isNotEmpty(oldPicAbsolutePath)) {
				File f = new File(oldPicAbsolutePath);
				if (f.exists() && f.isFile()) {
					f.delete();
				}
			}
			/*// 新文件名默认为uuid
			String newFileName =  Identities.uuid() + extension;
			// 上传目录
			String fileDir = uploadUtils.getPathByParaNo("12");
			// 判断目录是否存在 不存在则创建指定的目录
			File f = new File(uploadPath + fileDir);
			if (!f.exists()) {
				f.mkdir();
			}
			// 保存图片地址
			String longName = uploadPath + fileDir + "/" + newFileName;
			log.debug("文件路径:" + longName);
			// 生成文件
			file.transferTo(new File(longName));
			// 更新数据库记录
			TevglPkgInfo t = new TevglPkgInfo();
			t.setPkgId(pkgId);
			t.setPkgLogo(newFileName);
			tevglPkgInfoService.update(t);
			// 删除旧的封面
			String oldPkgLogo = tevglPkgInfo.getPkgLogo();
			if (StrUtils.isNotEmpty(oldPkgLogo)) {
				String uri = uploadPath + uploadUtils.getPathByParaNo("12") + "/" + oldPkgLogo;
				File fi = new File(uri);
				if (fi.exists() && fi.isFile()) {
					fi.delete();
				}
			}*/
		} catch (Exception e) {
			log.error("教学包封面更换失败", e);
			return R.error("封面更换失败");
		}
		return R.ok("封面更换成功");
	}
	
	/**
	 * 我的教学包列表
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("/listMyPkgInfo")
	@CheckSession
	public R listMyPkgInfo(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 不展示该值为3的，可见性(来源字典:1私有or2公有3都不可见)(重点)
		params.put("displayNo", "3");
		String together = null;
		if (StrUtils.isNull(params.get("releaseStatus")) || "F".equals(params.get("releaseStatus"))) {
			together = "Y";
		}
		if (StrUtils.isNull(params.get("releaseStatus"))) {
			params.put("releaseStatus", "N");
		}
		params.put("createUserId", traineeInfo.getTraineeId());
		/*params.put("sidx", "t1.create_time");
		params.put("order", "desc");*/
		return tevglPkgInfoService.listMyPkgInfo(params, traineeInfo.getTraineeId(), together);
	}
	
	/**
	 * 根据条件搜索课程
	 * @param request
	 * @param majorId
	 * @return
	 */
	@PostMapping("/selectSubjectRefList")
	@CheckSession
	public R selectSubjectRefList(HttpServletRequest request, String majorId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("majorId", majorId);
		map.put("together", "Y");
		map.put("createUserId", traineeInfo.getTraineeId());
		map.put("loginUserId", traineeInfo.getTraineeId());
		map.put("state", "Y");
		return R.ok().put(Constant.R_DATA, tevglPkgInfoService.selectSubjectRefList(map));
	}
	
	/**
	 * 教学包下拉列表（包含自己创建的、被授权的、以及免费的）（新增课堂页面中用到）
	 * @param request
	 * @param params {'subjectRef':''}
	 * @return
	 */
	@GetMapping("/listPkgInfoSelect")
	@CheckSession
	public R listPkgInfoSelect(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		/*
		// 不展示该值为3的，可见性(来源字典:1私有or2公有3都不可见)(重点)
		params.put("displayNo", "3");
		// 且只展示发布状态的
		params.put("releaseStatus", "Y");
		List<Map<String,Object>> list = tevglPkgInfoService.listPkgInfoForSelect(params, traineeInfo.getTraineeId(), true, false);
		return R.ok().put(Constant.R_DATA, list);
		*/
		return tevglPkgInfoService.queryPkgListByUnionAllForSelect(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 只查询自己创建的教学包
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("/queryMyPkg")
	@CheckSession
	public R queryMyPkg(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		List<Map<String,Object>> list = tevglPkgInfoService.listPkgInfoForSelect(params, traineeInfo.getTraineeId(), false, false);
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 教师下拉列表
	 * @param teacherName
	 * @param mobile
	 * @return
	 */
	@GetMapping("/listTeacher")
	@CheckSession
	public R listTeacher(String teacherName, String mobile) {
		Map<String, Object> params = new HashMap<>();
		if (StrUtils.isNotEmpty(teacherName)) {
			params.put("teacherName", teacherName);
		}
		if (StrUtils.isNotEmpty(mobile)) {
			params.put("mobile", mobile);
		}
		List<TevglTchTeacher> list = tevglTchTeacherService.queryNoPage(params);
		List<Map<String,Object>> collect = list.stream().map(this::converToTeacherMap).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, collect);
	}
	
	/**
	 * 去部分属性
	 * @param tevglTchTeacher
	 * @return
	 */
	private Map<String, Object> converToTeacherMap(TevglTchTeacher tevglTchTeacher){
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("teacherId", tevglTchTeacher.getTeacherId());
		info.put("teacherName", tevglTchTeacher.getTeacherDesc());
		info.put("teacherPic", tevglTchTeacher.getTeacherPic());
		return info;
	}
	
	/**
	 * 保存教学包基础信息
	 * @param request
	 * @param tevglPkgInfo
	 * @param file
	 * @return
	 */
	@RequestMapping("/saveInfo")
	@CheckSession
	public R saveInfo(HttpServletRequest request, TevglPkgInfo tevglPkgInfo,
			@RequestPart(name = "file", required = false)MultipartFile file) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		try {
			String attachId = "";
			String fileName = "";
			if (file != null && !file.isEmpty()) {
				// 头像上传
				R r = uploadUtils.uploads(file, "12", null, "images");
				Integer code = (Integer) r.get("code");
				if (code != 0) {
					return r;
				}
				@SuppressWarnings("unchecked")
				Map<String, Object> obj = (Map<String, Object>) r.get("data");
				attachId = (String) obj.get("attachId");
				fileName = (String) obj.get("fileName");
			}
			if (StrUtils.isNotEmpty(fileName)) {
				tevglPkgInfo.setPkgLogo(fileName);
			}
			if (StrUtils.isNotEmpty(attachId)) {
				tevglPkgInfo.setAttachId(attachId);
			}
			if (StrUtils.isEmpty(tevglPkgInfo.getPkgId())) {
				return tevglPkgInfoService.saveInfo(tevglPkgInfo, traineeInfo.getTraineeId());
			} else {
				return tevglPkgInfoService.updateInfo(tevglPkgInfo, traineeInfo.getTraineeId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("保存失败");
		}
	}
	
	/**
	 * 查看教学包基础信息
	 * @param request
	 * @param pkgId
	 * @return
	 */
	@GetMapping("/view/{id}")
	@CheckSession
	public R viewPkgInfoForUpdate(HttpServletRequest request, @PathVariable("id") String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgInfoService.viewPkgInfoForUpdate(pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看教学包基础信息
	 * @param request
	 * @param pkgId
	 * @return
	 */
	@GetMapping("/viewPkgInfo")
	@CheckSession
	public R viewPkgInfo(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgInfoService.viewPkgBaseInfo(pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 删除教学包
	 * @param request
	 * @param pkgId
	 * @return
	 */
	@PostMapping("/deletePkg")
	@CheckSession
	public R deletePkg(HttpServletRequest request, String pkgId, String temp) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 优化版，（删除云盘的代码还未优化）
		return deletePackageService.deletePkgInfo(pkgId, traineeInfo.getTraineeId());
		// 旧版
		//return tevglPkgInfoService.deletePkg(pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 批量授权
	 * @param jsonObject
	 * @return
	 */
	@PostMapping(value = "authorization")
	@CheckSession
	public R authorization(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookpkgTeamService.authorization(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 单独对某个老师授权
	 * @param jsonObject
	 * @return
	 */
	@PostMapping(value = "authorizationAlone")
	@CheckSession
	public R authorizationAlone(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookpkgTeamDetailService.authorizationAlone(jsonObject, traineeInfo.getTraineeId());
	}

	/**
	 * 发布教学包
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/releaseTeachingPackage")
	@CheckSession
	public R releaseTeachingPackage(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		//return tevglPkgInfoService.releaseTeachingPackage(jsonObject, traineeInfo.getTraineeId());
		return releaseTeachingPackageService.releaseTeachingPackage(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 获取当前使用的教学包，所能升级的版本
	 * @param pkgId
	 * @return
	 */
	@PostMapping("queryUpgradeList")
	@CheckSession
	public R queryUpgradeList(HttpServletRequest request, String ctId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgInfoService.queryUpgradeList(ctId, pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 更换版本
	 * @param request
	 * @param ctId
	 * @param pkgId
	 * @return
	 */
	@PostMapping("/doUpgradePkg")
	@CheckSession
	public R doUpgradePkg(HttpServletRequest request, String ctId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// TODO 更换课堂对应的教学包，对应的表t_evgl_pkg_info中subject_id和ref_pkg_id
		return tevglPkgInfoService.doUpgradePkg(ctId, pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 将正式发布状态的教学包授权其它人使用
	 * @param request
	 * @param pkgId
	 * @param userId
	 * @return
	 */
	@PostMapping("/authorizationPkg")
	@CheckSession
	public R authorizationPkg(HttpServletRequest request, String pkgId, String userId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgInfoService.authorizationToWho(pkgId, userId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 取消授权
	 * @param request
	 * @param pkgId
	 * @param userId
	 * @return
	 */
	@PostMapping("/unauthorization")
	@CheckSession
	public R unauthorization(HttpServletRequest request, String pkgId, String userId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgInfoService.unAuthorization(pkgId, userId, traineeInfo.getTraineeId());
	}
	
	@RequestMapping("/queryDataList")
	@CheckSession
	public R queryDataList(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return releaseTeachingPackageService.queryDataList(pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 检验当前源教学包是否有正在等待审核通过的衍生教学包
	 * @param request
	 * @param pkgId
	 * @return
	 */
	@PostMapping("/check")
	@CheckSession
	public R check(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return releaseTeachingPackageService.check(pkgId, traineeInfo.getTraineeId());
	}
	

	@PostMapping("/querMyWaitCheckPkgList")
	@CheckSession
	public R querMyWaitCheckPkgList(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		return tevglPkgCheckService.querMyWaitCheckPkgList(params, traineeInfo.getTraineeId());
	}
	
	@PostMapping("/queryDefaultNameList")
	@CheckSession
	public R queryDefaultNameList(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgDefaultChapterService.queryDefaultNameList(pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 保存
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/saveBatch")
	@CheckSession
	public R saveBatch(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgDefaultChapterService.saveBatch(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 获取当前教学包的直系父教学包下的直系子教学包们（即历史版本）
	 * @param request
	 * @param pkgId
	 * @return
	 */
	@RequestMapping("getHistoryPkgList")
	@CheckSession
	public R getHistoryPkgList(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgInfoService.getHistoryPkgList(pkgId);
	}
}
