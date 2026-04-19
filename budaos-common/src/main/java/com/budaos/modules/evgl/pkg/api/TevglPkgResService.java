package com.budaos.modules.evgl.pkg.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglPkgRes;

import java.util.Map;

/**
 * <p> Title: 教学包资源</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglPkgResService extends IBaseService<TevglPkgRes>{
	
	/**
	 * 新增资源 
	 * @author huj
	 * @data 2019年8月13日	
	 * @param tevglPkgRes
	 * @return
	 */
	R saveResInfo(TevglPkgRes tevglPkgRes, String loginUserId);
	
	/**
	 * 修改资源
	 * @author huj
	 * @data 2019年8月13日	
	 * @param tevglPkgRes
	 * @return
	 */
	R editResInfo(TevglPkgRes tevglPkgRes, String loginUserId, String pkgId);
	
	/**
	 * 查看资源
	 * @author huj
	 * @data 2019年8月13日	
	 * @param resId 资源主键
	 * @return
	 */
	Map<String, Object> viewResInfo(Map<String, Object> params);
	
}