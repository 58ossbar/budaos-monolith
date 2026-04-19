package com.budaos.modules.evgl.cloudpan.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanPermissions;
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
public interface TcloudPanPermissionsMapper extends BaseSqlMapper<TcloudPanPermissions> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 查询不是这些学员的学员信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectByTraineeIds(Map<String, Object> map);
	
	/**
	 * 根据文件id或者文件名id查询云盘权限拥有者
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectOwnerByFileId(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param params
	 */
	void insertBatch(Map<String, Object> params);
	
	/**
	 * 根据文件id查找perId(文件共享权限id)
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectPerIdByDirIds(Map<String, Object> map);
	
	/**
	 * 目录显示管理
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryDirectoryDisplay(Map<String, Object> map);
}