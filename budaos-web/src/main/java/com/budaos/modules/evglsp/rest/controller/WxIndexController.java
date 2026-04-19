package com.budaos.modules.evglsp.rest.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.activity.api.TevglActivityTestPaperService;
import com.budaos.modules.evgl.examine.api.TestPaperLibraryService;
import com.budaos.modules.evgl.medu.me.api.TmeduMediaAvdService;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomTraineeService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 首页
 * @author water
 *
 */
@RestController
@RequestMapping("/wx/index-api")
public class WxIndexController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TmeduMediaAvdService tmeduMediaAvdService;
	@Autowired
	private TevglTchClassroomService tevglTchClassroomService;
	@Autowired
	private TevglTchClassroomTraineeService tevglTchClassroomTraineeService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TmeduApiTokenService tmeduApiTokenService;
	@Autowired
	private TestPaperLibraryService testPaperLibraryService;
	@Autowired
	private TevglActivityTestPaperService tevglActivityTestPaperService;
	
	/**
	 * 查询轮播图列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/showAvd")
	@SysLog("查询轮播图列表(返回List<Bean>)")
	public R showAvd(@RequestParam Map<String, Object> params) {
		return tmeduMediaAvdService.queryShowIndex(params);
	}
	
	/**
	 * 查询我教的课和我听的课列表(返回List<Bean>)
	 * @param params
	 * @param type 1加入的课堂（我听的课）2自己创建的课堂（我教的课），为空则查全部
	 * @param token
	 * @return R
	 */
	@GetMapping("/showClassroomList")
	@SysLog("查询我教的课和我听的课列表(返回List<Bean>)")
	public R showClassroomList(@RequestParam Map<String, Object> params,String type,String token) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo userInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (userInfo == null) {
			return R.error("无效的用户信息");
		}
		String loginUserId = userInfo.getTraineeId();
		// 小程序我教的课，已结束的课堂中，只展示当前年份的
		if (StrUtils.isNotEmpty(type) && "2".equals(type)) {
			params.put("justShowThisYear", "Y");
			String string = DateUtils.getNowTimeStamp();
			params.put("nowYear", string.substring(0, 4));
		}
		return tevglTchClassroomService.listClassroom(params, loginUserId, type);
	}
	
	
	/**
	 * 根据majorId和name 模糊查询课堂列表(返回List<Bean>)
	 * @param params
	 * @param type
	 * @param token
	 * @return R
	 */
	@GetMapping("/showClassRoomLists")
	@SysLog("根据majorId和name 模糊查询课堂列表(返回List<Bean>)")
	public R showClassRoomLists(@RequestParam Map<String, Object> params,String token) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo userInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (userInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglTchClassroomService.listClassroom(params, userInfo.getTraineeId(), null);
	}
	
	/**
	 * 查询当前登录人所属的职业路径下的试卷库
	 * @author zhouyl加
	 * @date 2021年3月25日
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("queryPaperListForWx")
	public R queryPaperListForWx(HttpServletRequest request,@RequestParam Map<String, Object> params) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(params.get("token").toString());
		if(te == null){
    		return R.error(401, "Invalid token");
    	}
		TevglTraineeInfo userInfo = tevglTraineeInfoService.selectObjectById(te.getUserId());
		if (userInfo == null) {
			return R.error("无效的用户信息");
		}
		return tevglActivityTestPaperService.queryPaperListForWx(params, userInfo.getTraineeId());
	}
	
}
