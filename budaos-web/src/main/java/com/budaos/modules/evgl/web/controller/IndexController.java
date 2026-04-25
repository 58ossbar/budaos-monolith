package com.budaos.modules.evgl.web.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.ConstantProd;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.site.api.TevglIndexService;
import com.budaos.modules.evgl.site.api.TevglSiteNewsService;
import com.budaos.modules.evgl.site.api.TevglSitePartnerService;
import com.budaos.modules.evgl.site.api.TevglsiteResourceService;
import com.budaos.modules.evgl.stu.api.TevglStuStarService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomService;
import com.budaos.modules.evgl.tch.api.TevglTchTeacherService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.SpringContextUtils;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>首页</p>
 * <p>Title: IndexController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年7月3日
 */
@RestController
@RequestMapping("index-api")
public class IndexController {

	@Autowired
	private TevglIndexService tevglIndexService;
	@Autowired
	private TevglTchTeacherService tevglTchTeacherService; // 布道师
	@Autowired
	private TevglStuStarService tevglStuStarService; // 实训故事(优秀学生)
	@Autowired
	private TevglSitePartnerService tevglSitePartnerService; // 校企合作
	@Autowired
	private TevglsiteResourceService tevglsiteResourceService;
	@Autowired
	private TevglSiteNewsService tevglSiteNewsService;
	@Autowired
	private TevglTchClassroomService tevglTchClassroomService;
	
	/**
	 * <p>首页，初始化了一些数据</p>
	 * @author huj
	 * @data 2019年7月3日
	 * @return 返回了广告轮播图、布道师、实训故事、校企合作信息
	 */
	@GetMapping("/")
	public R index(@RequestParam Map<String, Object> params) {
		return tevglIndexService.index(params);
	}
	
	/**
	 * <p>获取导航菜单</p>
	 * @author huj
	 * @data 2019年7月4日
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/getInitMenu")
	public R getInitMenu(HttpServletRequest request, HttpServletResponse response) {
    	return tevglIndexService.getInitMenu(request, response);
	}
	
	/**
	 * <p>首页显示6条新闻资讯</p>
	 * @author huj
	 * @data 2019年7月12日
	 * @param parmas
	 * @return
	 */
	@GetMapping("/news")
	public R newsInfo(@RequestParam Map<String, Object> parmas) {
		parmas.put("sidx", "create_time");
		parmas.put("order", "desc");
		parmas.put("status", "2"); // 状态1待审2已发布 3删除
		parmas.put("noone", "Y"); // 是否头条
		return tevglSiteNewsService.query(parmas);
	}
	
	/**
	 * 云课堂列表
	 * @param params
	 * @return
	 */
	@GetMapping("/queryClassroomList")
	public R queryClassroomList(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", getToken(request));
            TevglTraineeInfoService service = SpringContextUtils.getBean(TevglTraineeInfoService.class);
            List<TevglTraineeInfo> list = service.queryByMap(map);
            if (list != null && list.size() > 0) {
                traineeInfo = list.get(0);
            }
        }
		//return tevglTchClassroomService.queryClassroomListForNotLogin(params);
		return tevglTchClassroomService.listClassroom(params, traineeInfo == null ? null : traineeInfo.getTraineeId(), "");
	}
	
	private String getToken(HttpServletRequest request) {
        String token = request.getHeader(ConstantProd.TOKEN_KEY_NAME);
        if (StrUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    switch(cookie.getName()){
                        case "evglToken":
                        case "token":
                            token = cookie.getValue();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return token;
    }
	
}
