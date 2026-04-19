package com.budaos.modules.evgl.cloudpan.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
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
public interface TcloudPanFileMapper extends BaseSqlMapper<TcloudPanFile> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 获取最大排序号
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 根据目录id查询其里面的文件id
	 * @param dirId
	 * @return
	 */
	List<Map<String, Object>> selectFileInfoListByDirId(String dirId);
	
	void updateForUpload(TcloudPanFile t);
	
	/**
	 * 
	 * @param dirId
	 * @return
	 */
	List<Map<String, Object>> selectSimpleInfoList(Map<String, Object> params);
	
	/**
	 * 根据条件统计数量
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> countNumByMap(Map<String, Object> map);
	
	/**
	 * 根据文件id以及文件夹id查找文件夹的父级目录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectParentIdByfileIdAnddirId(Map<String, Object> map);
	
	/**
	 * 根据文件id和文件夹id查找文件是否在数据库中存在
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectFileIdByDirIdAndFileIds(Map<String, Object> map);
	
	/**
	 * 查询所有的文件
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryAllFile(Map<String, Object> map);
	
	/**
	 * 查询全部目录上传的文件
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectUploadFile(Map<String, Object> map);
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	void insertBatch(List<TcloudPanFile> list);
	
	/**
	 * 根据条件查询根目录下的文件
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryTopFileList(Map<String, Object> map);
	
}