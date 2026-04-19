package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.dataprivilege.annotation.DataFilter;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.constants.ExecStatus;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.*;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.*;
import com.budaos.modules.sys.api.*;
import com.budaos.modules.sys.domain.*;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>系统用户api的实现类</p>
 * <p>Title: TsysUserinfoServiceImpl.java</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p>
 * @author huj
 * @date 2019年5月5日
 */
@Service
public class TsysUserinfoServiceImpl implements TsysUserinfoService {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private final static String INDEX_USER = "2";

	@Autowired
	private TsysUserinfoMapper tsysUserinfoMapper;
	@Autowired
	private TsysRoleMapper tsysRoleMapper;
	@Autowired
	private TsysUserprivilegeMapper tsysUserprivilegeMapper;
	@Autowired
	private TsysOrgMapper tsysOrgMapper;
	@Autowired
	private TuserRoleService tuserRoleService;
	@Autowired
	private TuserRoleMapper tuserRoleMapper;
	@Autowired
	private TsysUserprivilegeService tsysUserprivilegeService;
	@Autowired
	private TsysRoleService sysRoleService;
	@Autowired
	private TorgUserService torgUserService;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private TuserPostService tuserPostService;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TsysParameterMapper tsysParameterMapper;
	
	@Autowired
	private PlatformUploadUtils uploadUtils;
	
	/**
	 * <p>
	 * 所有用户列表
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月7日
	 * @param params 记得params.put("userInfo", 当前登陆用户)
	 * @return
	 */
	@Override
	@DataFilter(tableAlias = "oo", customDataAuth = "", selfUser = false)
	public R query( Map<String, Object> params) {
		// 处理前端传递的机构id
		handleQueryParams(params);
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TsysUserinfo> userList = tsysUserinfoMapper.selectListByMap(query);
		userList.stream().forEach(item -> {
			item.setUserimg(uploadUtils.stitchingPath(item.getUserimg(), INDEX_USER));
		});
		Map<String, String> m = new HashMap<>();
		m.put("sex", "sex");
		m.put("userType", "userType");
		m.put("status", "status");
		convertUtil.convertParam(userList, m);
		PageUtils pageUtil = new PageUtils(userList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * <p>
	 * 根据机构ID,查询用户列表
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param orgIds 机构ID 多个以逗号隔开
	 * @return
	 */
	@Override
	public List<TsysUserinfo> queryList(String[] orgIds) {
		if (orgIds != null && orgIds.length > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orgIds", Arrays.asList(orgIds));
			List<TsysUserinfo> userList = tsysUserinfoMapper.selectListByMap(params);
			return userList;
		}
		return new ArrayList<TsysUserinfo>();
	}

	/**
	 * <p>
	 * 注:原ModelAndView下的用户信息编辑页
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月6日
	 * @param params
	 * @return HashMap集合,通过key: data获取数据
	 */
	@Override
	public R add( Map<String, Object> params) {
		TsysUserinfo tsysuserinfo = new TsysUserinfo();
		tsysuserinfo.setOrgIdList(new ArrayList<>());
		tsysuserinfo.setRoleIdList(new ArrayList<>());
		// 如果进入视图前选择了机构，则把选中的机构做为新增的默认主机构
		if (params.containsKey("id") && !"-1".equals(params.get("id"))) {
			tsysuserinfo.setOrgId(params.get("id").toString());
		}
		return R.ok().put("data", tsysuserinfo);
	}

	/**
	 * <p>
	 * 注:原ModelAndView下的用户信息编辑页
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId      被修改的用户ID
	 * @return
	 */
	@Override
	public R edit(String userId) {
		TsysUserinfo userInfo = serviceLoginUtil.getLoginUser();
		if (userInfo == null) {
			return R.error("无法获取当前登陆信息，请尝试重新登陆");
		}
		if (userId == null || "".equals(userId) || "null".equals(userId)) {
			return R.error("无法获取userId");
		}
		if (ArrayUtils.contains(new String[] { userId }, Constant.SUPER_ADMIN)) {
			return R.error(502, "系统管理员不能修改");
		}
		if (ArrayUtils.contains(new String[] { userId }, userInfo.getUserId())) {
			return R.error(502, "当前用户不能修改");
		}
		TsysUserinfo tsysuserinfo = new TsysUserinfo();
		if (userId != null && !"".equals(userId) && !"null".equals(userId)) {
			tsysuserinfo = tsysUserinfoMapper.selectObjectById(userId); // 根据用户ID查询信息
			if (tsysuserinfo == null) {
				return R.error("无法获取被修改的用户信息");
			}
			tsysuserinfo.setRoleIdList(tuserRoleService.selectRoleListByUserId(userId));
			// 查询用户所属机构，把主机构和副机构分类
			Map<String, Object> m = new HashMap<>();
			m.put("userId", tsysuserinfo.getUserId());
			List<TorgUser> orgUserList = torgUserService.selectListByMap(m);
			List<String> orgIdList = new ArrayList<>();
			for (TorgUser org : orgUserList) {
				if (org.getIsMain() == 1) {
					tsysuserinfo.setOrgId(org.getOrgId()); // 主机构
				} else {
					orgIdList.add(org.getOrgId()); // 副机构
				}
			}
			tsysuserinfo.setOrgIdList(orgIdList);
		}
		return R.ok().put("data", tsysuserinfo);
	}

	/**
	 * <p>
	 * 新增(实际的新增方法)
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月7日
	 * @param user
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R save( TsysUserinfo user,  String attachId) {
		TsysUserinfo tsysUserinfo = new TsysUserinfo();
		BeanUtils.copyProperties(tsysUserinfo, user);
		// 验证登陆名是否唯一
		TsysUserinfo existedUser = tsysUserinfoMapper.selectObjectByUserName(user.getUsername());
		if (existedUser != null) {
			return R.error("登陆账号已经存在，需唯一，请重新输入！");
		}
		// 验证手机号是否唯一
		TsysUserinfo existedUser2 = tsysUserinfoMapper.selectObjectByMobile(user.getMobile());
		if (existedUser2 != null) {
			return R.error("该手机号码已被绑定，请重新输入");
		}
		// 主机构在集合的第一个位置
		if (user.getOrgId() != null) {
			user.getOrgIdList().add(0, user.getOrgId());
		}
		// 默认密码-从参数表中获取
		String defautPwd = getDefaultPasswordFormParameters();
		// 加密
		String pwd = TicketDesUtil.encryptWithMd5(defautPwd, null);
		// 组装数据
		String uuid = StringUtils.isEmpty(user.getUserId()) ? Identities.uuid() : user.getUserId();
		user.setUserId(uuid);
		tsysUserinfo.setUserId(uuid);
		tsysUserinfo.setPassword(pwd);
		tsysUserinfo.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tsysUserinfo.setCreateTime(DateUtils.getNowTimeStamp());
		tsysUserinfo.setUpdateTime(tsysUserinfo.getCreateTime());
		tsysUserinfo.setUserTheme("black");
		tsysUserinfo.setUserType(StrUtils.isEmpty(tsysUserinfo.getUserType()) ? "1" : tsysUserinfo.getUserType());
		tsysUserinfo.setSex(StrUtils.isEmpty(tsysUserinfo.getSex()) ? "0" : tsysUserinfo.getSex());
		tsysUserinfo.setStatus(StrUtils.isEmpty(tsysUserinfo.getStatus()) ? "1" : tsysUserinfo.getStatus());
		// 保存用户信息
		tsysUserinfoMapper.insert(tsysUserinfo);
		// 如果上传了资源文件
		tsysAttachService.updateAttachForAdd(user.getUserimgAttachId(), uuid,  INDEX_USER);
		// 保存用户与角色的关系
		tuserRoleService.saveOrUpdate(user.getRoleIdList(), Arrays.asList(tsysUserinfo.getUserId()));
		// 保存用户与机构的关系
		torgUserService.saveOrUpdate(tsysUserinfo.getUserId(), user.getOrgIdList());
		// 保存用户与岗位的关系
		if (StrUtils.isNotEmpty(user.getPostId())) {
			tuserPostService.saveOrUpdate(tsysUserinfo.getUserId(), Arrays.asList(user.getPostId()));
		}
		return R.ok("用户新增成功");
	}

	/**
	 * <p>
	 * 修改(实际的修改方法)
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月7日
	 * @param user
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = {"menu_list_cache", "authorization_cache"}, allEntries = true)
	public R update( TsysUserinfo user,  String attachId) {
		TsysUserinfo obj = tsysUserinfoMapper.selectObjectById(user.getUserId());
		if (obj == null) {
			return R.error("该用户不存在");
		}
		TsysUserinfo tsysUserinfo = new TsysUserinfo();
		BeanUtils.copyProperties(tsysUserinfo, user);
		// 验证登陆名是否唯一
		TsysUserinfo existedUser = tsysUserinfoMapper.selectObjectByUserName(user.getUsername());
		if (existedUser != null && !existedUser.getUserId().equals(user.getUserId())) {
			return R.error("登陆账号已经存在，需唯一，请重新输入！");
		}
		// 验证手机号是否唯一
		TsysUserinfo existedUser2 = tsysUserinfoMapper.selectObjectByMobile(user.getMobile());
		if (existedUser2 != null && !existedUser2.getUserId().equals(user.getUserId())) {
			return R.error("该手机号码已被绑定，请重新输入");
		}
		// 主机构在集合的第一个位置
		if (user.getOrgId() != null) {
			user.getOrgIdList().add(0, user.getOrgId());
		}
		// 更新用户信息
		tsysUserinfo.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		tsysUserinfo.setUpdateTime(DateUtils.getNowTimeStamp());
		tsysUserinfoMapper.update(tsysUserinfo);
		// 如果上传了资源文件
		tsysAttachService.updateAttachForEdit(user.getUserimgAttachId(), tsysUserinfo.getUserId(),  INDEX_USER);
		// 保存用户与角色的关系
		// 如果没有选择，则表示是清空
		if (user.getRoleIdList() == null || user.getRoleIdList().isEmpty()) {
			tuserRoleService.delete(user.getUserId());
		}  else {
			tuserRoleService.saveOrUpdate(user.getRoleIdList(), Arrays.asList(tsysUserinfo.getUserId()));
		}
		// 保存用户与机构的关系
		torgUserService.saveOrUpdate(tsysUserinfo.getUserId(), user.getOrgIdList());
		// 保存用户与岗位的关系
		if (StrUtils.isNotEmpty(user.getPostId())) {
			tuserPostService.saveOrUpdate(tsysUserinfo.getUserId(), Arrays.asList(user.getPostId()));
		}
		// 处理图片路径
		user.setUserimg(uploadUtils.stitchingPath(user.getUserimg(), INDEX_USER));
		return R.ok("用户修改成功").put(Constant.R_DATA, user);
	}

	/**
	 * <p>
	 * 删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param id
	 * @return
	 */
	@Override
	@SysLog("删除用户信息")
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R delete(String userId, String loginUserId) {
		if (loginUserId == null || "".equals(loginUserId)) {
			return R.error("无法获取当前登陆用户信息");
		}
		if (userId == null || "".equals(userId) || "null".equals(userId)) {
			return R.error("无法获取被删除的userId");
		}
		if (Constant.SUPER_ADMIN.equals(userId)) {
			return R.error("系统管理员不能删除");
		}
		if (loginUserId.equals(userId)) {
			return R.error("当前用户不能删除");
		}
		tuserRoleService.delete(userId); // 删除用户与角色关系
		torgUserService.delete(userId); // 删除机构用户
		tuserPostService.delete(userId); // 删除岗位用户
		tsysUserinfoMapper.delete(userId); // 删除用户信息
		return R.ok("删除成功");
	}

	/**
	 * <p>
	 * 批量删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @return
	 */
	@Override
	@SysLog("批量删除用户信息")
	@Transactional(rollbackFor = Exception.class)
	public R deleteBatch(String[] userIds) {
		if (ArrayUtils.contains(userIds, Constant.SUPER_ADMIN)) {
			return R.error("系统管理员不能删除");
		}
		if (ArrayUtils.contains(userIds, serviceLoginUtil.getLoginUserId())) {
			return R.error("当前登录用户不能删除");
		}
		// 删除用户与角色关系
		tuserRoleService.deleteBatch(userIds);
		// 删除机构用户
		torgUserService.deleteBatch(userIds);
		// 删除岗位用户
		tuserPostService.deleteBatch(userIds);
		// 删除用户信息
		tsysUserinfoMapper.deleteBatch(userIds);
		// 解绑附件
		tsysAttachService.unBind(userIds, INDEX_USER);
		return R.ok("删除成功");
	}

	/**
	 * <p>
	 * 查看明细
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param id 用户ID
	 * @return
	 */
	@Override
	public R view( String id) {
		TsysUserinfo user = tsysUserinfoMapper.selectObjectById(id);
		if (user == null) {
			return R.error(ExecStatus.INVALID_PARAM.getCode(), ExecStatus.INVALID_PARAM.getMsg());
		}
		// 处理图片路径
		user.setUserimg(uploadUtils.stitchingPath(user.getUserimg(), INDEX_USER));
		// 获取用户拥有的角色
		List<String> roleIds = tuserRoleService.selectRoleListByUserId(user.getUserId());
		user.setRoleIdList(roleIds);
		// 获取用户的岗位
		List<String> postIds = tuserPostService.selectPostIdListByUserId(user.getUserId());
		user.setPostIdList(postIds == null ? new ArrayList<>() : postIds);
		// 获取用户机构信息
		List<String> orgIds = torgUserService.selectListByUserId(user.getUserId());
		user.setOrgIdList(orgIds == null ? new ArrayList<>() : orgIds);
		// 设置主机构
		if (orgIds != null && !orgIds.isEmpty()) {
			user.setOrgId(orgIds.get(0));
		}
		return R.ok().put(Constant.R_DATA, user);
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
	@Override
	public R toGrantRole( String[] ids) {
		if (ids == null || ids.length == 0) {
			return R.error();
		}
		List<TsysRole> data = new ArrayList<>();
		if (ids.length == 1) { // 单个用户的时候
			Map<String, Object> map = new HashMap<>();
			map.put("userId", ids[0]);
			map.put("status", 1);
			data = tsysRoleMapper.selectListByMap(map);
		} else if(ids.length >= 2){ // 多选用户的时候
			List<TuserRole> list = tuserRoleMapper.selectRoleIntersectionByUserId(ids);
			if (list.size() > 0) {
				for (TuserRole tuserRole : list) {
					log.debug(tuserRole.getRoleId() + "\t" + tuserRole.getId());
					// tuserRole.getId() 为 mybatis中count(1) id
					if (Integer.valueOf(tuserRole.getId()) == ids.length) { // 说明
						data.add(tsysRoleMapper.selectObjectById(tuserRole.getRoleId()));
					}
				}
			}
		}
		return R.ok().put("data", data);
	}

	/**
	 * <p>
	 * 分配角色，当前登录用户不能给自己分配角色
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userIds
	 * @param roleIds
	 * @return
	 */
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R grantRole(String[] userIds, String[] roleIds, String loginUserId) {
		for (String userId : userIds) {
			if (!userId.equals(loginUserId)) {
				tuserRoleService.saveOrUpdate(userId, Arrays.asList(roleIds));
			} else {
				return R.error("角色分配失败, 当前登录用户不能给自己分配角色");
			}
		}
		return R.ok("角色分配成功");
	}

	/**
	 * <p>
	 * 修改登录用户密码
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param password    旧密码
	 * @param newPassword 新密码
	 * @param userId      当前登陆用户ID
	 * @return
	 */
	@Override
	@SysLog("修改用户密码")
	public R updatePassword(String userId, String password, String newPassword) {
		if (StrUtils.isEmpty(userId)) {
			return R.error("无法获取当前登陆信息");
		}
		if (password == null || "".equals(password)) {
			return R.error("旧密码不能为空");
		}
		if (newPassword == null || "".equals(newPassword)) {
			return R.error("新密码不能为空");
		}
		password = TicketDesUtil.encryptWithMd5(password, null); // 加密后重新赋值
		newPassword = TicketDesUtil.encryptWithMd5(newPassword, null); // 加密后重新赋值
		int count = doUpdatePassword(userId, password, newPassword); // 更新密码
		if (count == 0) {
			return R.error("原密码不正确");
		}
		return R.ok("密码修改成功");
	}

	/**
	 * <p>
	 * 重置用户密码,自动忽略当前登录用户,注:密码重置默认为123456
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userIds
	 * @return
	 */
	@Override
	public R resetPassword( String[] userIds, String loginUserId) {
		if (loginUserId == null || "".equals(loginUserId) || "null".equals(loginUserId)) {
			return R.error("无法获取当前登陆用户ID");
		}
		if (userIds != null && userIds.length > 0) {
			List<String> userIdList = Arrays.asList(userIds);
			userIdList.remove(loginUserId); // 删除当前登录用户，当前登录用户不能重置密码
			int count = doResetPassword(userIdList); // 调用方法重置密码
			if (count < userIds.length) {
				return R.error("重置密码失败");
			}
		} else {
			return R.error("重置密码失败");
		}
		return R.ok("密码重置成功");
	}

	/**
	 * 清除权限
	 * @param userIds
	 * @return
	 */
	@Override
	@CacheEvict(value = "menu_list_cache", allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public R clearPermissions(List<String> userIds) {
		if (userIds == null || userIds.isEmpty()) {
			return R.error("没有要清空权限的用户");
		}
		// 移出当前登录用户，当前登录用户不能清除权限
		userIds.remove(serviceLoginUtil.getLoginUserId());
		// 清除用户与角色关系
		tuserRoleService.deleteBatch(userIds.toArray(new String[userIds.size()]));
		// 清除用户菜单权限
		tsysUserprivilegeMapper.deleteBatch(userIds.toArray(new String[userIds.size()]));
		return R.ok("成功清除权限");
	}

	/**
	 * <p>
	 * 清除用户的所有权限，忽略当前登录用户
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userIds     用户ID 多个以逗号隔开
	 * @param loginUserId 当前登陆用户ID
	 * @return
	 */
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R clearPermissions(String[] userIds, String loginUserId) {
		if (userIds != null && userIds.length > 0) {
			List<String> userIdList = Arrays.asList(userIds);
			userIdList.remove(loginUserId);// 删除当前登录用户，当前登录用户不能清除权限
			doClearPermissions(userIdList.toArray(new String[userIdList.size()]));
		} else {
			return R.error("清除用户权限失败");
		}
		return R.ok("用户权限清除成功");
	}

	/**
	 * <p>
	 * 用户分配权限，当前登录用户不能分配权限
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userIds     用户ID 多个以逗号隔开
	 * @param menuIds     菜单ID 多个以逗号隔开
	 * @param loginUserId 当前登陆用户ID
	 * @return
	 */
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R grantPerms(String[] userIds, String[] menuIds, String loginUserId) {
		if (userIds == null || userIds.length == 0 || menuIds == null || menuIds.length == 0) {
			return R.error("未选择用户或权限");
		}
		List<String> userIdList = Arrays.asList(userIds);
		userIdList.remove(loginUserId);
		R r = doGrantPerms(userIdList.toArray(new String[userIdList.size()]), menuIds);
		return r;
	}

	/**
	 * 查询用户的所有权限
	 */
	@Override
	public List<String> selectAllPerms(String userId) {
		List<String> list = tsysUserinfoMapper.selectAllPerms(userId);
		return list;
	}

	/**
	 * <p>
	 * 查询用户的所有菜单ID
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月7日
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> selectAllMenuId(String userId) {
		List<String> list = tsysUserinfoMapper.selectAllMenuId(userId);
		return list;
	}

	/**
	 * <p>
	 * 根据用户名，查询系统用户
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月7日
	 * @param username
	 * @return
	 */
	@Override
	public TsysUserinfo selectObjectByUserName(String username) {
		TsysUserinfo tsysUserinfo = tsysUserinfoMapper.selectObjectByUserName(username);
		return tsysUserinfo;
	}

	/**
	 * <p>
	 * 根据用户ID,查询信息
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月11日
	 * @param userId
	 * @return
	 */
	@Override
	public TsysUserinfo selectObjectByUserId(String userId) {
		return tsysUserinfoMapper.selectObjectById(userId);
	}

	/**
	 * <p>
	 * 查询所有用户
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月7日
	 * @return
	 */
	@Override
	public List<TsysUserinfo> getAllUserinfo() {
		return tsysUserinfoMapper.getAllUserinfo();
	}

	/**
	 * <p>
	 * 检查角色是否越权
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param user
	 */
	private void checkRole(TsysUserinfo user) {

		// 如果不是超级管理员，则需要判断用户的角色是否自己创建
		if (user.getCreateUserId().equalsIgnoreCase(Constant.SUPER_ADMIN)) {
			return;
		}

		// 查询用户创建的角色列表
		List<String> roleIdList = sysRoleService.selectRoleListByCreatorUserId(user.getCreateUserId());

		// 判断是否越权
		if (!roleIdList.containsAll(user.getRoleIdList())) {
			throw new BudaosException("新增用户所选角色，不是本人创建");
		}
	}

	/**
	 * <p>
	 * 重置密码(密码被重置为123456)
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userIds 用户ID 多个以逗号隔开
	 * @return
	 */
	private int doResetPassword(List<String> userIds) {
		Map<String, Object> map = new HashMap<>();
		map.put("userIds", userIds);
		List<TsysUserinfo> userList = tsysUserinfoMapper.selectListByMap(map);
		// 默认密码-从参数表中获取
		int count = 0;
		for (TsysUserinfo user : userList) {
			String newPwd = TicketDesUtil.encryptWithMd5(getDefaultPasswordFormParameters(), null); // 重置密码默认为123456
			count += doUpdatePassword(user.getUserId(), user.getPassword(), newPwd);
		}
		return count;
	}

	/**
	 * <p>
	 * 实际修改密码的方法，暂勿删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userId      用户ID
	 * @param password    旧密码
	 * @param newPassword 新密码
	 * @return int
	 */
	private int doUpdatePassword(String userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return tsysUserinfoMapper.updatePassword(map);
	}

	/**
	 * <p>
	 * 实际清除用户权限的方法
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userIds
	 */
	@CacheEvict(value = "menuListCache", allEntries = true)
	public void doClearPermissions(String[] userIds) {
		tuserRoleService.deleteBatch(userIds); // 清除角色
		tsysUserprivilegeMapper.deleteBatch(userIds); // 清除特权
		// tsysDataprivilegeService.deleteBatch(userIds); // 清除数据权限
	}

	/**
	 * <p>
	 * 实际用户分配特权的方法
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月8日
	 * @param userIds
	 * @param menuIds
	 */
	public R doGrantPerms(String[] userIds, String[] menuIds) {
		try {
			tsysUserprivilegeMapper.deleteBatch(userIds);
			for (String userId : userIds) {
				tsysUserprivilegeService.saveOrUpdate(userId, menuIds);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("用户权限分配失败");
		}
		return R.ok("用户权限分配成功");
	}

	private List<String> getChildrenOrgId(String parentId) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("parentId", parentId);
		List<TsysOrg> oList = tsysOrgMapper.selectListByMap(m);
		List<String> idList = new ArrayList<>();
		for (TsysOrg u : oList) {
			idList.add(u.getOrgId());
			idList.addAll(getChildrenOrgId(u.getOrgId()));
		}
		return idList;
	}

	/**
	 * <p>
	 * 更改状态
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年6月12日
	 * @param user
	 * @return
	 */
	@Override
	public R updataStatus( TsysUserinfo user) {
		try {
			tsysUserinfoMapper.update(user);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改失败");
		}
		return R.ok("修改成功");
	}

	/**
	 * <p>从参数表中获取默认密码</p>
	 * @author huj
	 * @data 2019年6月24日
	 * @return
	 */
	public String getDefaultPasswordFormParameters() {
		String password = "123456";
		Map<String, Object> map = new HashMap<>();
		map.put("paraname", "系统用户默认密码");
		//map.put("paraType", "password");
		List<TsysParameter> list = tsysParameterMapper.selectListByMap(map);
		if (list.size() > 0 && list != null) {
			if (list.get(0).getParano() != null && !"".equals(list.get(0).getParano()) && list.get(0).getParano().length() >= 6) {
				password = list.get(0).getParano();
			}
		}
		return password;
	}

	/**
	 * <p>更新排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @param params
	 * @return
	 */
	@Override
	public R updateSort( Map<String, Object> params) {
		String currUserId = (String) params.get("currUserId"); // 当前用户ID
		String targetUserId = (String) params.get("targetUserId"); // 目标用户ID
		if (currUserId != null && !"".equals(currUserId) && targetUserId != null && !"".equals(targetUserId)) {
			TsysUserinfo currUser = tsysUserinfoMapper.selectObjectById(currUserId); // 当前用户
			TsysUserinfo targetUser = tsysUserinfoMapper.selectObjectById(targetUserId); // 目标用户
			if (currUser != null && targetUser != null) {
				Integer cSort = currUser.getSortNum(); // 当前用户排序号
				Integer tSort = targetUser.getSortNum(); // 目标用户排序号
				currUser.setSortNum(tSort);
				targetUser.setSortNum(cSort);
				tsysUserinfoMapper.updateSortNum(currUser);
				tsysUserinfoMapper.updateSortNum(targetUser);
			}
		}
		return R.ok();
	}
	
	/**
	 * <p>获取最大的排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @return
	 */
	@Override
	public int getMaxSort() {
		return tsysUserinfoMapper.getMaxSort();
	}
	
	/**
	 * 处理一些查询条件
	 * @param params
	 */
	private void handleQueryParams(Map<String, Object> params) {
		if (params != null) {
			if (params.get("orgIds") != null && !"".equals(params.get("orgIds"))) {
				String ids = params.get("orgIds").toString();
				List<String> list = new ArrayList<>();
				String[] orgIds = ids.split(",");
				for (String string : orgIds) {
					list.add(string);
				}
				params.put("orgIds", list);
			}
		}
	}
	
}
