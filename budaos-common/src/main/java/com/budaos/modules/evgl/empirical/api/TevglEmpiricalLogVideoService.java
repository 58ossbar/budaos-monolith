package com.budaos.modules.evgl.empirical.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalLogVideo;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglEmpiricalLogVideoService extends IBaseService<TevglEmpiricalLogVideo>{
	
	/**
	 * 查看视频时，记录并获得经验值
	 * @param ctId
	 * @param pkgId
	 * @param subjectId
	 * @param chapterId
	 * @param videoId
	 * @param loginUserId
	 * @return
	 */
	R viewVideo(String ctId, String pkgId, String subjectId, String chapterId, String videoId, String loginUserId);
	
}