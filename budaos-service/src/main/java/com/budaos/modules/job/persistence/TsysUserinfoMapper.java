package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysUserinfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Title: 系统用户 Description: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TsysUserinfoMapper extends BaseSqlMapper<TsysUserinfo> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> selectAllPerms(String userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<String> selectAllMenuId(String userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	TsysUserinfo selectObjectByUserName(String username);

	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);
	/**
	 * 查询所有用户
	 */
	List<TsysUserinfo> getAllUserinfo();
	
	/**
	 * <p>前端验证登陆名，手机号唯一</p>
	 * @author huj
	 * @data 2019年6月15日
	 * @param map
	 * @return
	 */
	List<TsysUserinfo> selectListByMapForCheck(Map<String, Object> map);
	
	/**
	 * <p>更新排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @param user
	 * @return
	 */
	int updateSortNum(TsysUserinfo user);
	
	/**
	 * <p>获取最大排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @return
	 */
	int getMaxSort();

	/**
	 * 根据唯一手机号码查询用户信息
	 * @param mobile
	 * @return
	 */
	TsysUserinfo selectObjectByMobile(String mobile);
}
