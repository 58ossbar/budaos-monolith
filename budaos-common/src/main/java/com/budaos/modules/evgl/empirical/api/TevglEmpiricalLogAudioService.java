package com.budaos.modules.evgl.empirical.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalLogAudio;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglEmpiricalLogAudioService extends IBaseService<TevglEmpiricalLogAudio>{
	
	/**
	 * 查看音频时，记录并获得经验值
	 * @param ctId
	 * @param pkgId
	 * @param subjectId
	 * @param chapterId
	 * @param audioId
	 * @param loginUserId
	 * @return
	 */
	R viewAudio(String ctId, String pkgId, String subjectId, String chapterId, String audioId, String loginUserId);
	
}