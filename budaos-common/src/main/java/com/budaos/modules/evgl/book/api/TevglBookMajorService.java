package com.budaos.modules.evgl.book.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.domain.TevglBookMajor;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 职业课程</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglBookMajorService extends IBaseService<TevglBookMajor>{
	
	/**
	 * <p>复制</p>
	 * @author huj
	 * @data 2019年7月9日
	 * @param id
	 * @return
	 */
	R copy(String id);
	
	/**
	 * <p>职业课程路径列表</p>
	 * @author huj
	 * @data 2019年7月11日
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListMapByMapForWeb(Map<String, Object> map);
	
	/**
	 * <p>查询所有(无分页)</p>
	 * @author huj
	 * @data 2019年7月12日
	 * @param params
	 * @return
	 */
	List<TevglBookMajor> queryAll(Map<String, Object> params);
	
	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param map
	 * @return
	 */
	R selectListByMapForWeb(Map<String, Object> map);
	
	/**
	 * <p>根据条件查询树数据</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param map
	 * @return
	 */
	R queryForTree(Map<String, Object> map);

	
	/**
	 * <p>根据条件查询树数据</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param id
	 * @return
	 */
	R viewForMgr(String id);
	
	/**
	 * <p>根据条件查询记录，无分页</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @param map
	 * @return
	 */
	List<TevglBookMajor> selectListByMap(Map<String, Object> map);
	
	/**
	 * 职业路径与班级组成层次结构的数据
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryMajorClassTreeData(String name);
	
	/**
	 * 职业路径与课程组成层次结构的数据
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> queryMajorSubjectTreeData(String name);
}