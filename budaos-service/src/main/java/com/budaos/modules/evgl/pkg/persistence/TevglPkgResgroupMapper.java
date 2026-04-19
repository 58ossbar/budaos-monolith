package com.budaos.modules.evgl.pkg.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.modules.evgl.pkg.vo.ResVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 教学包资源分组目录</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglPkgResgroupMapper extends BaseSqlMapper<TevglPkgResgroup> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>获取排序号</p>  
	 * @author huj
	 * @data 2019年12月19日	
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 查询部分字段，且对象为驼峰命名
	 * @param map
	 * @return
	 * @apiNote
	 * {resgroupId资源分组主键，pkgId教学包主键，chapterId章节主键，resgroupName分组名称，sortNum排序号}
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> map);
	List<Map<String, Object>> selectSimpleListMap2(Map<String, Object> map);
	
	/**
	 * 根据条件查询
	 * @param map
	 * @return
	 */
	List<ResVO> findSimpleList(Map<String, Object> map);
	
	/**
	 * 批量插入
	 * @param list
	 */
	void insertBatch(List<TevglPkgResgroup> list);
	
	/**
	 * 根据条件仅查询主键id
	 * @param params
	 * @return
	 */
	List<String> selectResgroupIdListByMap(Map<String, Object> params);
	
	/**
	 * 统计课程下的资源总数
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> countResourceNums(Map<String, Object> params);
	
	/**
	 * 统计某教材下的资源，这里的资源只的是t_evgl_pkg_resgroup、t_evgl_pkg_res，dictCode为2活动的不参与计算
	 * @param subjectId
	 * @return
	 */
	Integer countResourceNum(Object subjectId);
}