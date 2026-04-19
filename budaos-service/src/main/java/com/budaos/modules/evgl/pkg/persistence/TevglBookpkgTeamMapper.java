package com.budaos.modules.evgl.pkg.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 资源共建权限</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglBookpkgTeamMapper extends BaseSqlMapper<TevglBookpkgTeam> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param tevglBookpkgTeamList
	 */
	void insertBatch(List<TevglBookpkgTeam> tevglBookpkgTeamList);
	
	/**
	 * 查询已授权记录
	 * @param map
	 * @return
	 */
	List<TevglBookpkgTeam> selectAuthorized(Map<String, Object> map);
	
	/**
	 * 查询未授权记录
	 * @param map
	 * @return
	 */
	List<TevglBookpkgTeam> selectUnauthorized(Map<String, Object> map);
	
	/**
	 * 查询这个人的被授权的教学包的信息
	 * @param map {"userId": "", "releaseStatus": "Y"}
	 * @return
	 */
	List<Map<String, Object>> selectSimplePackageList(Map<String, Object> map);
	
	/**
	 * 根据条件仅查询主键id
	 * @param map
	 * @return
	 */
	List<String> selectTeamIdListByMap(Map<String, Object> map);
	
}