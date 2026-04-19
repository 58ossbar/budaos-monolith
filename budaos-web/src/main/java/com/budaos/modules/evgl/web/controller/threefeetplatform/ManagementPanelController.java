package com.budaos.modules.evgl.web.controller.threefeetplatform;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.common.CbUploadUtils;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.site.api.TevglSiteNewsService;
import com.budaos.modules.evgl.tch.api.TevglTchTeacherService;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoUpdateVo;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 管理看板
 */
@RestController
@RequestMapping("/managementPanel-api")
public class ManagementPanelController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TevglSiteNewsService tevglSiteNewsService;
	@Autowired
	private TevglTchTeacherService tevglTchTeacherService;
	@Autowired
	private CbUploadUtils uploadUtils;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private DictService dictService;
	
	/**
	 * 性别
	 * @return
	 */
	@GetMapping("/listSex")
	public R listSex() {
		List<Map<String,Object>> dictList = dictService.getDictList("sex");
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	
	/**
	 * 个人信息-教师、学员通用
	 * @param request
	 * @return
	 */
	@GetMapping("/getPersonalInfo")
	@CheckSession
	public R getPersonalInfo(HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		boolean isTeacher = false;
		// 是否拥有教师账号
		TevglTchTeacher teacher = tevglTchTeacherService.selectObjectByTraineeId(traineeInfo.getTraineeId());
		if (teacher != null) {
			if ("Y".equals(teacher.getState())) {
				isTeacher = true;
			} else {
				log.debug("教师账号已无效[" + traineeInfo.getTraineeId() + "]");
			}
		}
		log.debug("当前用户["+traineeInfo.getTraineeId()+"]是否为教师：" + isTeacher);
		if (isTeacher) {
			return tevglTraineeInfoService.viewTraineeInfoForManagementPanel(traineeInfo.getTraineeId()).put("isTeacher", true);	
		} else {
			return tevglTraineeInfoService.viewTraineeInfo(traineeInfo.getTraineeId()).put("isTeacher", false);
		}
	}
	
	/**
	 * 获取消息数
	 * @param request
	 * @return
	 */
	@GetMapping("/getMsgNum")
	public R getMsgNum(HttpServletRequest request) {
		return R.ok().put(Constant.R_DATA, new Random().nextInt(100));
	}
	
	/**
	 * 滚动新闻
	 * @param request
	 * @return
	 */
	@GetMapping("listNews")
	public R listNews(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		params.put("pageNum", 1);
		params.put("pageSize", 5);
		params.put("status", "2"); // 状态1待审2已发布3删除
		params.put("noone", "Y"); // 是否头条
		params.put("sidx", "t1.create_time");
		params.put("order", "desc");
		List<Map<String, Object>> newsList = tevglSiteNewsService.selectSimpleListByMap(params);
		return R.ok().put(Constant.R_DATA, newsList);
	}
	
	/**
	 * 头像上传
	 * @param request
	 * @param file
	 * @return
	 */
	@PostMapping("/uploadPic")
	@CheckSession
	public R uploadPic(HttpServletRequest request, @RequestPart(name="file", required = false) MultipartFile file) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		String attachId = null;
		String fileName = null;
		boolean isTeacher = false;
		TevglTchTeacher tevglTchTeacher = tevglTchTeacherService.selectObjectByTraineeId(traineeInfo.getTraineeId());
		if (tevglTchTeacher != null && "Y".equals(tevglTchTeacher.getState())) {
			isTeacher = true;
		}
		// 默认为学员身份
		String fileType = isTeacher ? "7" : "16";
		// 仅更换头像时
		if (file != null && !file.isEmpty()) {
			// 头像上传
			R r = uploadUtils.uploads(file, fileType, null, "images");
			Integer code = (Integer) r.get("code");
			if (code != 0) {
				return r;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> obj = (Map<String, Object>) r.get("data");
			attachId = (String) obj.get("attachId");
			fileName = (String) obj.get("fileName");
		}
		if (isTeacher) {
			// 修改教师账号信息
			TevglTchTeacher teacher = new TevglTchTeacher();
			teacher.setTeacherId(tevglTchTeacher.getTeacherId());
			if (StrUtils.isNotEmpty(fileName)) {
				teacher.setTeacherPic(fileName);
			}
			if (StrUtils.isNotEmpty(attachId)) {
				teacher.setAttachId(attachId);
			}
			R res = tevglTchTeacherService.update(teacher);
			log.debug("教师头像更新结果：" + res);
		} else {
			// 修改学员账号信息
			TevglTraineeInfo trainee = new TevglTraineeInfo();
			trainee.setTraineeId(traineeInfo.getTraineeId());
			if (StrUtils.isNotEmpty(fileName)) {
				trainee.setTraineePic(fileName);
			}
			if (StrUtils.isNotEmpty(attachId)) {
				trainee.setAttachId(attachId);
			}
			tevglTraineeInfoService.update(trainee);
		}
		return R.ok("头像上传成功");
	}
	
	/**
	 * 修改个人信息
	 * @param request
	 * @param vo
	 * @return
	 */
	@PostMapping("/saveInfo")
	@CheckSession
	public R updatePersonInfo(HttpServletRequest request, TevglTraineeInfoUpdateVo vo){
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		vo.setTraineeId(traineeInfo.getTraineeId());
		return tevglTraineeInfoService.updatePersonInfo(vo);
	}
	
	/**
	 * @author zhouyl加
	 * @date 2021年2月1日
	 * @param request
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param confimPwd 确认密码
	 * @return
	 */
	@PostMapping("updatePassword")
	@CheckSession
	public R updatePassword(HttpServletRequest request, String oldPwd, String newPwd, String confimPwd) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTraineeInfoService.updatePassword(oldPwd, newPwd, confimPwd, traineeInfo.getTraineeId());
	}
}
