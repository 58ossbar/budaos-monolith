package com.budaos.modules.evgl.web.controller.cloudpan;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.cloudpan.api.TcloudPanPermissionsService;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 云盘权限控制类
 * @author zhouyl
 * @date 2021年1月26日
 */

@RestController
@RequestMapping("/cloud-api/cloudpanpermissions")
public class CloudPanPermissionsController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(CloudPanPermissionsController.class);
	@Autowired
	private TcloudPanPermissionsService tcloudPanPermissionsService;
	
	/**
	 * 设置云盘权限
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("setPermissions")
	@CheckSession
	public R setPermissions(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tcloudPanPermissionsService.setPermissions(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 设置云盘权限
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("setPermissionsNew")
	@CheckSession
	public R setPermissionsNew(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tcloudPanPermissionsService.setPermissionsNew(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看文件/文件夹的云盘权限
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("queryPermissions")
	@CheckSession
	public R queryPermissions(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tcloudPanPermissionsService.queryPermissions(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 设置学员是否可见
	 * @author zhouyl加
	 * @date 2021年3月6日
	 * @param ctId 课堂id
	 * @param pkgId 教学包id
	 * @param dirId 文件夹id
	 * @param isTraineesVisible 学员是否可见   true:可见,false:不可见
	 * @param loginUserId 
	 * @return
	 */
	@PostMapping("setStudentNotVisible")
	@CheckSession
	public R setStudentNotVisible(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		
		return tcloudPanPermissionsService.setStudentNotVisible(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看目录显示管理
	 * @param request
	 * @param ctId
	 * @return
	 */
	@GetMapping("queryDirectoryDisplay")
	@CheckSession
	public R queryDirectoryDisplay(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		
		return tcloudPanPermissionsService.queryDirectoryDisplay(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 获取树形的目录结构
	 * @author zhouyl加
	 * @date 2021年4月24日
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@GetMapping("getTreeDataForAuth")
	@CheckSession
	public R getTreeDataForAuth(HttpServletRequest request, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		
		return tcloudPanPermissionsService.getTreeDataForAuth(pkgId, traineeInfo.getTraineeId());
	}
	
	/**
	 * @author zhouyl加
	 * @date 2021年4月24日
	 * 保存设置
	 * @param jsonObject 示例
	 * 		{"currentPkgId": "",
	 * 		 "traineeIdList" : ["03e9aebed6c14545b3b73f105f734da6", "033ccc3e7bf1491b8ae0b6b0d39f00d0"]
	 * 		 "list": [{"type": "1", "id": "2997b2d8df5f434c9fe7ac31393674e8", "pkgId": ""},
				    {"type": "1", "id": "3b4153b849f94c428e5d31592ee417b1", "pkgId": ""},
				    {"type": "1", "id": "99243b55e6b446fc9d936ed00555dd13", "pkgId": ""},
				    {"type": "2", "id": "58a79c286583412ab38eb8a4ac9de176", "pkgId": ""}]
	 * 		}
	 * @param loginUserId
	 * @return
	 */
	@PostMapping("saveAuth")
	@CheckSession
	public R saveAuth(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return R.ok("未开放");
		//return tcloudPanPermissionsService.saveAuth(jsonObject, traineeInfo.getTraineeId());
		//return tcloudPanPermissionsService.saveAuthNew(jsonObject, traineeInfo.getTraineeId());
	}
}
