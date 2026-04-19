package com.budaos.modules.evgl.pkg.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeamDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 资源共建权限明细</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglBookpkgTeamDetailMapper extends BaseSqlMapper<TevglBookpkgTeamDetail> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglBookpkgTeamDetail> list);
	
	/**
	 * 根据用户id查询，拥有的章节权限交集
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectChapterIntersectionByUserId(Map<String, Object> map);
	
	/**
	 * 根据条件仅查询主键id
	 * @param map
	 * @return
	 */
	List<String> selectDetailIdListByMap(Map<String, Object> map);
	
}