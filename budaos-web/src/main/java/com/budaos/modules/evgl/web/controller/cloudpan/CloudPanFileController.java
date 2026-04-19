package com.budaos.modules.evgl.web.controller.cloudpan;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.cloudpan.api.TcloudPanFileService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 云盘文件
 * @author huj
 *
 */
@RestController
@RequestMapping("/cloud-api/file")
public class CloudPanFileController {

	@Autowired
	private DictService dictService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TcloudPanFileService tcloudPanFileService;
	
	/**
	 * 上传
	 * @param request
	 * @param dirId
	 * @return
	 */
	@PostMapping("/uploadFile")
	@CheckSession
	public R uploadFile(HttpServletRequest request, String dirId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tcloudPanFileService.uploadFiles(request, dirId, traineeInfo.getTraineeId(), pkgId);
	}
	
	/**
	 * 删除
	 * @param request
	 * @param dirId
	 * @return
	 */
	/*@PostMapping("/deleteFile")
	@CheckSession
	public R deleteFile(HttpServletRequest request, @RequestBody List<String> list) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tcloudPanFileService.deleteFileBatch(request.getParameter("pkgId"), list, traineeInfo.getTraineeId());
	}*/
	
	/**
	 * 下载
	 * @param request
	 * @param response
	 * @param fileId
	 * @return
	 */
	@GetMapping("/downloadFile")
	//@CheckSession
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileId, String pkgId) {
		tcloudPanFileService.downloadFile(response, fileId, pkgId);
	}
}
