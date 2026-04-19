package com.budaos.modules.evgl.empirical.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalLogChapter;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglEmpiricalLogChapterService extends IBaseService<TevglEmpiricalLogChapter>{
	
	/**
	 * 查看章节时，记录并获得经验值
	 * @param ctId
	 * @param pkgId
	 * @param subjectId
	 * @param chapterId
	 * @param resId
	 * @param loginUserId
	 * @return
	 */
	R viewChapter(JSONObject jsonObject, String loginUserId);
	
}