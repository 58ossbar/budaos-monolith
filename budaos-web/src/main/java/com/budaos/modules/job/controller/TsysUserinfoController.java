package com.budaos.modules.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.sys.api.TsysOrgService;
import com.budaos.modules.sys.api.TsysResourceService;
import com.budaos.modules.sys.api.TsysRoleService;
import com.budaos.modules.sys.api.TsysUserinfoService;
import com.budaos.modules.sys.domain.TsysRole;
import com.budaos.modules.sys.domain.TsysUserinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户管理(系统用户)控制类
 * </p>
 * <p>
 * Title: TsysUserinfoController.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: 布道师学习通
 * </p>
 * 
 * @author huj
 * @date 2019年5月13日
 */
@RestController
@RequestMapping("/api/sys/user")
public class TsysUserinfoController {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private LoginUtils loginUtils;

	@Autowired
	private TsysUserinfoService tsysUserinfoService;

	@Autowired
	private TsysOrgService tsysOrgService;

	@Autowired
	private TsysRoleService tsysRoleService;

	@Autowired
	private TsysResourceService tsysResourceService;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	/**
	 * <p>
	 * 根据条件查询记录(分页查询)
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月13日
	 * @param data
	 * @param request
	 * @return
	 */
	@GetMapping("/findPage")
	@PreAuthorize("hasAuthority('sys:tsysuserinfo:query')")
	@SysLog("根据条件查询用户信息")
	public R findPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		return tsysUserinfoService.query(params); // 调用方法,获取数据
	}

	/**
	 * <p>
	 * 进入新增用户页面
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月13日
	 * @param params
	 * @param request
	 * @return
	 */
	@GetMapping("/add")
	@PreAuthorize("hasAuthority('sys:tsysuserinfo:add')")
	@SysLog("进入新增角色页面或弹窗")
	public R add(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		return tsysUserinfoService.add(params);
	}

	/**
	 * <p>
	 * 进入修改用户页面
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月13日
	 * @param params
	 * @param request
	 * @return
	 */
	@GetMapping("/edit")
	@PreAuthorize("hasAuthority('sys:tsysuserinfo:edit')")
	@SysLog("进入修改角色页面或弹窗")
	public R edit(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		String userId = (String) params.get("userId"); // 此id用于得知是否选中左侧机构树
		return tsysUserinfoService.edit(userId);
	}

	/**
	 * <p>
	 * 执行数据新增和修改
	 * </p>
	 * <p>
	 * 表t_sys_userinfo, t_user_role, t_org_user
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月13日
	 * @param data
	 * @return
	 */
	@PostMapping("/save")
	@SysLog("执行用户相关数据新增和修改保存")
	public R save(@RequestBody(required = false) TsysUserinfo user, HttpServletRequest request) {
		try {
			String attachId = request.getParameter("attachId");
			if ((user.getUserId() == null) || ("").equals(user.getUserId())) {
				return tsysUserinfoService.save(user, attachId);
			} else {
				return tsysUserinfoService.update(user, attachId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("保存失败");
		}
	}

	/**
	 * <p>
	 * 单个删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月21日
	 * @param id
	 * @return
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('sys:tsysuserinfo:remove')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tsysUserinfoService.delete(id, loginUtils.getLoginUserId());
	}

	/**
	 * <p>
	 * 批量删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param data
	 * @param request
	 * @return
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('sys:tsysuserinfo:remove')")
	@SysLog("删除用户")
	public R deletes(@RequestBody String[] ids) {
		return tsysUserinfoService.deleteBatch(ids);
	}

	/**
	 * <p>
	 * 查看明细
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param data
	 * @param request
	 * @return
	 */
	@GetMapping("/view/{userId}")
	@PreAuthorize("hasAuthority('sys:tsysuserinfo:view')")
	@SysLog("查看用户详细信息")
	public R view(@PathVariable("userId") String userId) {
		return tsysUserinfoService.view(userId);
	}

	/**
	 * <p>
	 * 机构树
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param params
	 * @return
	 */
	@GetMapping("/orgTree")
	@SysLog("获取机构树")
	public R orgTree(@RequestParam Map<String, Object> params) {
		return tsysOrgService.query2(new HashMap<String, Object>());
	}

	/**
	 * <p>
	 * 根据机构ID查询用户信息
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param params
	 * @return
	 */
	@GetMapping("/queryUserInfo")
	@SysLog("根据机构ID查询用户信息")
	public R queryUserInfo(@RequestParam Map<String, Object> params) {
		String ids = (String) params.get("orgIds");
		if ("".equals(ids) || ids == null) {
			return R.error("无法获取机构ID");
		}
		String[] orgIds = ids.split(",");
		List<TsysUserinfo> list = tsysUserinfoService.queryList(orgIds);
		return R.ok().put("data", list);
	}

	/**
	 * <p>
	 * 获取所有角色
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param params
	 * @return
	 */
	@GetMapping("/queryAllRole")
	@SysLog("获取所有角色")
	public R queryAllRole(@RequestParam Map<String, Object> params) {
		List<TsysRole> list = tsysRoleService.queryAll(new HashMap<>());
		return R.ok().put("data", list);
	}

	/**
	 * <p>
	 * 进入分配角色界面
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年6月14日
	 * @param ids
	 * @return
	 */
	@PostMapping("/toGrantRole")
	public R toGrantRole(@RequestBody String[] ids) {
		return tsysUserinfoService.toGrantRole(ids);
	}

	/**
	 * <p>
	 * 分配角色
	 * </p>
	 * <p>
	 * 表t_user_role
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param params
	 * @return
	 */
	@GetMapping("/grantRole")
	@SysLog("分配角色")
	public R grantRole(@RequestParam Map<String, Object> params) {
		String uIds = (String) params.get("userIds");
		String rIds = (String) params.get("roleIds");
		if (uIds == null || "".equals(uIds)) {
			return R.error("无法获取userIds");
		}
		if (rIds == null || "".equals(rIds)) {
			return R.error("无法获取roleIds");
		}
		String[] userIds = uIds.split(",");
		String[] roleIds = rIds.split(",");
		return tsysUserinfoService.grantRole(userIds, roleIds, loginUtils.getLoginUserId());
	}

    /**
     * 清空用户的所有权限(表t_user_role)
     * @author huj
     * @data 2019年5月14日
     * @param userIds
     * @return
     */
    @PostMapping("/clearPermissions")
    @PreAuthorize("hasAuthority('sys:tsysuserinfo:clear')")
    @SysLog("清空用户的所有权限")
    public R clearPermissions(@RequestBody List<String> userIds) {
        return tsysUserinfoService.clearPermissions(userIds);
    }

	/**
	 * <p>
	 * 分配权限，当前登录用户不能分配权限(表:t_sys_userprivilege)
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param data
	 * @param request
	 * @return
	 */
	@PostMapping("/grantPerms")
	@SysLog("分配权限")
	public R grantPerms(@RequestBody(required = false) JSONObject data, HttpServletRequest request) {
		String uIds = data.getString("userIds");
		String mIds = data.getString("menuIds");
		if (uIds == null || "".equals(uIds)) {
			return R.error("无法获取userIds");
		}
		if (mIds == null || "".equals(mIds)) {
			return R.error("无法获取menuIds");
		}
		String[] userIds = uIds.split(",");
		String[] menuIds = mIds.split(",");
		return tsysUserinfoService.grantPerms(userIds, menuIds, loginUtils.getLoginUserId());
	}

	/**
	 * <p>
	 * 获取当前登陆用户信息
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @return
	 */
	@GetMapping("/to_updateuserinfo")
	@SysLog("获取当前登陆用户信息")
	public R toUpdateUserInfo(@RequestBody(required = false) JSONObject data) {
		TsysUserinfo tsysUserinfo = tsysUserinfoService.selectObjectByUserId(loginUtils.getLoginUserId());
		return R.ok().put("data", tsysUserinfo);
	}

	/**
	 * <p>
	 * 修改登陆用户密码
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param data
	 * @param request
	 * @return
	 */
	@PostMapping("/password")
	@SysLog("修改登陆用户密码")
	public R password(@RequestBody(required = false) JSONObject data, HttpServletRequest request) {
		String password = data.getString("password"); // 旧密码
		String newPassword = data.getString("newPassword"); // 新密码
		return tsysUserinfoService.updatePassword(loginUtils.getLoginUserId(), password, newPassword); // 更新密码
	}

	/**
	 * <p>
	 * 重置密码,注:密码重置默认为123456
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @return
	 */
	@RequestMapping("/resetPassword")
	@PreAuthorize("hasAuthority('sys:tsysuserinfo:reset')")
	@SysLog("重置用户密码")
	public R resetPassword(@RequestBody(required = false) String[] ids, HttpServletRequest request) {
		return tsysUserinfoService.resetPassword(ids, loginUtils.getLoginUserId());

	}

	/**
	 * <p>
	 * 获取资源菜单
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月18日
	 * @param params
	 * @return
	 */
	@GetMapping("/queryMenu")
	public R queryMenu(@RequestParam Map<String, Object> params) {
		String menuId = (String) params.get("menuId");
		return tsysResourceService.query(menuId);
	}

	/**
	 * <p>
	 * 获取登陆用户的权限标识
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月21日
	 * @param params
	 * @return
	 */
	@GetMapping("/findPermissions")
	public R findPermissions(@RequestParam Map<String, Object> params) {
		Set<String> set = tsysResourceService.getUserPermissions(loginUtils.getLoginUserId());
		return R.ok().put("data", set);
	}

	/**
	 * <p>
	 * 用户头像上传
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月27日
	 * @param picture
	 * @return 返回完整的图片路径
	 */
	@PostMapping("/upload")
	@SysLog("系统用户头像上传")
	public R upload(@RequestPart(value = "file", required = false) MultipartFile picture) {
		/*
		 * if (picture != null && !picture.isEmpty()) { String oldImgName =
		 * picture.getContentType(); // 原文件类型 String extension = "." +
		 * oldImgName.substring(oldImgName.lastIndexOf("/")+1); // 扩展名 if
		 * (!(".JPEG".equals(extension.toUpperCase()) ||
		 * ".PNG".equals(extension.toUpperCase()) ||
		 * ".JPG".equals(extension.toUpperCase()) )) { return
		 * R.error("暂时只支持.jpeg或.png或.jpg格式的图片"); } String imgName = UUID.randomUUID() +
		 * extension; // 新文件名 File f = new File(budaosFieUploadPath +
		 * "/sys-user-img"); // 创建存放系统用户头像的目录 if (!f.exists()) { // 目录若不存在，创建一个
		 * f.mkdir(); } String longName = budaosFieUploadPath + "/sys-user-img/" +
		 * imgName; try { UploadFileUtils.uploadImgThread(picture, longName); String
		 * attachId = tsysAttachService.uploadFileInsertAttach(oldImgName, extension,
		 * String.valueOf(picture.getSize()), imgName, longName, "1",
		 * loginUtils.getLoginUser().getUserRealname()); return R.ok().put("data",
		 * imgName).put("attachId", attachId) .put(".JPEG",
		 * ".JPEG".equals(extension.toUpperCase())) .put(".PNG",
		 * ".PNG".equals(extension.toUpperCase())) .put(".JPG",
		 * ".JPG".equals(extension.toUpperCase())); } catch (Exception e) {
		 * e.printStackTrace(); return R.error("图片上传失败, 请尝试重新上传..."); } } else { return
		 * R.error("无法获取上传的图片"); }
		 */
		return uploadFileUtils.uploadImage(picture, "/sys-user-img", "2", 0, 0);
	}

	/**
	 * <p>
	 * 更改用户状态
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年6月12日
	 * @param user
	 * @return
	 */
	@PostMapping("/updataStatus")
	public R updataStatus(@RequestBody TsysUserinfo user) {
		return tsysUserinfoService.updataStatus(user);
	}
	
	/**
	 * <p>更新排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @param params
	 * @return
	 */
	@GetMapping("/updateSort")
	@SysLog("更新排序号")
	public R updateSort(@RequestParam Map<String, Object> params) {
		return tsysUserinfoService.updateSort(params);
	}
	
	/**
	 * <p>获取最大排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @return
	 */
	@GetMapping("/getMaxSort")
	public R getMaxSort() {
		return R.ok().put("data", tsysUserinfoService.getMaxSort());
	}
}
