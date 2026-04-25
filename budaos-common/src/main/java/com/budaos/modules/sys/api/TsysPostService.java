package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysPost;

import java.util.List;
import java.util.Map;

/**
 * <p>岗位管理api</p>
 * <p>Title: TsysPostService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月29日
 */
public interface TsysPostService {

	/**
	 * <p>查询列表(返回List<Bean>)</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	R query(Map<String, Object> params);
	
	/**
	 * <p>查询列表(返回List<Map<String, Object>)</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	R queryForMap(Map<String, Object> params);
	
	/**
	 * <p>无分页查询</p>
	 * @author huj
	 * @data 2019年6月13日
	 * @param params
	 * @return
	 */
	R queryNoPage(Map<String, Object> params);
	
	/**
	 * <p></p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	List<TsysPost> selectListByMap(Map<String, Object> params);
	
	/**
	 * <p>查询明细</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	R selectObjectById(String id);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param tsysPost
	 * @return
	 */
	R save(TsysPost tsysPost);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param tsysPost
	 * @return
	 */
	R update(TsysPost tsysPost);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	R delete(String id);
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param ids
	 * @return
	 */
	R deleteBatch(String[] ids);
	
	/**
	 * <p>查询岗位parentPostid 为 "" 的数据</p>
	 * @author huj
	 * @data 2019年6月14日
	 * @param map
	 * @return
	 */
	List<TsysPost> selectListByParentPostidIsNull(Map<String, Object> map);
	
	/**
	 * <p>更新排序号</p>
	 * @author huj
	 * @data 2019年6月18日
	 * @param map
	 * @return
	 */
	R updateSort(Map<String, Object> map);
	
	/**
	 * <p>获取最大排序号</p>
	 * @author huj
	 * @data 2019年6月24日
	 * @return
	 */
	int getMaxSort();
}
