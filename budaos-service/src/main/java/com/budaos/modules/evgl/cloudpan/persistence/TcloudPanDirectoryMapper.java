package com.budaos.modules.evgl.cloudpan.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface TcloudPanDirectoryMapper extends BaseSqlMapper<TcloudPanDirectory> {
	
	/**
	 * 修改目录
	 * @param tcloudPanDirectory
	 */
	void updateDirectory(TcloudPanDirectory tcloudPanDirectory);
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> map);
	
	/**
	 * 查询顶级目录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectTopDirectoryList(Map<String, Object> map);
	List<Map<String, Object>> selectTopDirectoryListNew(Map<String, Object> map);
	List<Map<String, Object>> queryTopDirectoryList(Map<String, Object> map);
	
	/**
	 * 查询用户所有顶级目录的子目录
	 * @return
	 */
	List<TcloudPanDirectory> selectChildrenDirectoryList(@Param("pkgId") String pkgId, @Param("loginUserId") Object loginUserId);
	
	/**
	 * 获取最大排序号
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 搜索
	 * @return
	 */
	List<Map<String, Object>> search(Map<String, Object> map);
	
	/**
	 * 多表查询，文件夹、文件（注意：此方法不要查询 直属教学包下面的文件）
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListByUnionAll(Map<String, Object> map);
	
	/**
	 * 查询文件夹、文件、直属教学包的文件
	 * @param map {"queryTopFile": "不为空才会查询直属于教学包的文件"}
	 * @return
	 */
	List<Map<String, Object>> selectListByUnionAllNew(Map<String, Object> map);
	
	/**
	 * 根据条件统计数量
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> countNumByMap(Map<String, Object> map);
	
	/**
	 * 根据文件夹id查询父级目录id
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectParentIdBydirId(Map<String, Object> map);
	
	/**
	 * 查询所有目录及文件
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryAllDirAndFile(Map<String, Object> map);
	
	/**
	 * 仅查询主键id
	 * @param map
	 * @return
	 */
	List<String> selectDirIdListByMap(Map<String, Object> map);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TcloudPanDirectory> list);
}