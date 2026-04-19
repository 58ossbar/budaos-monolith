package com.budaos.modules.evgl.pkg.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgCheck;
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
public interface TevglPkgCheckMapper extends BaseSqlMapper<TevglPkgCheck> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 查询我的待审的教学包
	 * @param map
	 * @return
	 * @apiNote loginUserId、refPkgId必传
	 */
	List<Map<String, Object>> querMyWaitCheckPkgList(Map<String, Object> map);
	
	/**
	 * 待审核的教学包
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryWaitCheckPkgList(Map<String, Object> map);
}