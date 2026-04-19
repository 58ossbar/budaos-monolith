package com.budaos.modules.im.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.im.domain.TimGroupUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TimGroupUserMapper extends BaseSqlMapper<TimGroupUser> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 获取用户所在的所有群聊
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> selectGroupIds(String userId);
	
	/**
	 * 批量新增群组用户
	 * @param list
	 */
	void insertBatch(List<TimGroupUser> list);
	
	/**
	 * 根据userId查询记录
	 * @param userId
	 * @return
	 */
	String selectGroupuserIdByUserId(Map<String, Object> map);
}