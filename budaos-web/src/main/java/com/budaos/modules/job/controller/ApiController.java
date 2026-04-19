package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.sys.api.TsysResourceService;
import com.budaos.modules.sys.domain.TsysResource;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.constants.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(tags = "测试api")
@RestController
@RequestMapping("api")
public class ApiController {

	@Autowired
	private TsysResourceService tsysResourceService;
	@Autowired
	private LoginUtils loginUtils;

	@ApiOperation(value = "保存用户", notes = "<b>参数说明<b>:<br/>" + "&emsp;id:用户id<br/>" + "&emsp;name:用户姓名<br/>"
			+ "&emsp;age:用户年龄<br/>" + "<b>返回值说明</b>:<br/>" + "&emsp;code:服务状态，-1：失败，0：成功<br/>"
			+ "&emsp;msg:信息，当code为0时返回<br/>")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "id", value = "用户ID", required = false),
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "name", value = "用户姓名", required = false),
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "age", value = "用户年龄", required = false), })
	@RequestMapping("user")
	@SysLog("查询用户")
	public TsysUserinfo getUser(@RequestParam(required = false) Map<String, Object> params,
			HttpServletRequest request) {
		TsysUserinfo user = loginUtils.getLoginUser(request);
		return user;
	}

	@RequestMapping("menu/findNavTree")
	public R findNavTree(@RequestParam(required = false) Map<String, Object> params) {
		List<TsysResource> menuList = tsysResourceService.getUserMenuList(loginUtils.getLoginUserId(), "19c786f2bfbf46398e3b495f6c7014b1");
		Set<String> set = tsysResourceService.getUserPermissions(loginUtils.getLoginUserId());
		return R.ok().put(Constant.R_DATA, menuList).put("permissionList", set);
	}

	@ApiOperation(value = "保存用户2", notes = "<b>参数说明<b>:<br/>" + "&emsp;id:用户id<br/>" + "&emsp;name:用户姓名<br/>"
			+ "&emsp;age:用户年龄<br/>" + "<b>返回值说明</b>:<br/>" + "&emsp;code:服务状态，-1：失败，0：成功<br/>"
			+ "&emsp;msg:信息，当code为0时返回<br/>")
	@RequestMapping("func1")
	@Secured("ROLE_USER11")
	public String func1(HttpSession session) {
		return "func1";
	}

	@RequestMapping("func2")
	@PreAuthorize("hasAuthority('MY_ROLE1&MY_MENU1')")
	public String func2(HttpSession session) {
		return "func2";
	}
}
