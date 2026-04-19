package com.budaos.modules.evgl.pkg.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglPkgDefaultChapter;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglPkgDefaultChapterService extends IBaseService<TevglPkgDefaultChapter>{
	
	/**
	 * 查询需要默认生成的名称
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R queryDefaultNameList(String pkgId, String loginUserId);
	
	/**
	 * 批量保存
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveBatch(JSONObject jsonObject, String loginUserId);
	
}