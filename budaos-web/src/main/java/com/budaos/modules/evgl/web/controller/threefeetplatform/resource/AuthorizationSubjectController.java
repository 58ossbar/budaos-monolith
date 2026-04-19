package com.budaos.modules.evgl.web.controller.threefeetplatform.resource;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.api.TevglBookChapterService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.pkg.api.TevglBookpkgTeamDetailService;
import com.budaos.modules.evgl.pkg.api.TevglBookpkgTeamService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 课程与章节的授权相关接口
 * @author huj
 *
 */
@RestController
@RequestMapping("/resourceCenter-api/subject")
public class AuthorizationSubjectController {

	@Autowired
	private TevglBookpkgTeamDetailService tevglBookpkgTeamDetailService;
	@Autowired
	private TevglBookpkgTeamService tevglBookpkgTeamService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private TevglBookChapterService tevglBookChapterService;
	
	/**
	 * 学员列表
	 * @param params
	 * @return
	 */
	@GetMapping("/listTrainee")
	public R listTrainee(@RequestParam Map<String, Object> params) {
		return tevglTraineeInfoService.listTrainee(params);
	}
	
	/**
	 * 根据单个或多个用户查询章节共同权限
	 * @param request
	 * @param jsonObject {'pkgId':'', 'subjectId':'', traineeIds:[]}
	 * @return
	 */
	@PostMapping("/queryAuthorization")
	@CheckSession
	public R queryAuthorization(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookpkgTeamService.queryAuthorization(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 保存授权信息
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/saveAuthInfo")
	@CheckSession
	public R saveAuthInfo(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		//return tevglBookpkgTeamDetailService.authorizationAlone(jsonObject, traineeInfo.getTraineeId());
		return tevglBookpkgTeamService.authorization(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 标识章节赋予了谁
	 * @param request
	 * @param subjectId
	 * @return
	 */
	@PostMapping("/getChapterTreeWithTeacherName")
	@CheckSession
	public R getChapterTreeWithTeacherName(HttpServletRequest request, String subjectId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.getChapterTreeWithTeacherName(subjectId, pkgId, traineeInfo.getTraineeId());
	}
	
}
