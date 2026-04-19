package com.budaos.modules.evgl.site.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.site.domain.TevglSiteNews;
import com.budaos.modules.evgl.site.vo.TevglSiteNewsVo;
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
public interface TevglSiteNewsMapper extends BaseSqlMapper<TevglSiteNews> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * <p>上一页</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	TevglSiteNews selectPreviousPage(Map<String, Object> map);
	List<TevglSiteNews> selectupNewsInfo(TevglSiteNews tevglSiteNews);
	
	/**
	 * <p>下一页</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	TevglSiteNews selectNextPage(Map<String, Object> map);
	List<TevglSiteNews> selectdownNewsInfo(TevglSiteNews tevglSiteNews);
	
	
	/**
	 * <p>更新并查询浏览量</p>
	 * @author huj
	 * @data 2019年7月25日
	 * @param newsid
	 * @return
	 */
	String selectviewnum(String newsid);
	
	/**
	 * <p>审核</p>  
	 * @author huj
	 * @data 2019年8月10日	
	 * @param map
	 */
	void check(Map<String, Object> map);
	
	/**
	 * 根据条件查询部分信息，返回List<Map>，且对象为驼峰命名
	 * @param params
	 * @return 
	 */
	List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> params);
	
	/**
	 * 根据菜单栏目id统计其下有多少条
	 * @param menuId
	 * @return
	 */
	Integer countNewsNumByMenuId(@Param("menuId") String menuId);
	
	/**
	 * 更新浏览量
	 * @param tevglSiteNews
	 * @return
	 */
	Integer plusNum(TevglSiteNews tevglSiteNews);

	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglSiteNews> list);

	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<TevglSiteNewsVo> selectListByMapForOfficial(Map<String, Object> map);
	
	/**
	 * 查询对象
	 * @param newsid
	 * @return
	 */
	TevglSiteNewsVo selectTevglSiteNewsVoById(@Param("newsid") Object newsid);
	

	/**
	 * 批量更新状态
	 * @param list
	 * @param status
	 * @return
	 */
	int updateStateBatch(@Param("list") List<String> list, @Param("status") String status);

	/**
	 * 查询子数据
	 * @param newsid
	 * @return
	 */
	List<String> selectIdListByParentId(@Param("newsid") String newsid);
}