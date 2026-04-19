package com.budaos.modules.evgl.pkg.api;

import com.budaos.core.baseclass.domain.R;

/**
 * 删除教学包专用接口
 * @author huj
 *
 */
public interface DeletePackageService {

	/**
	 * 此方法作用：删除教学包各种相关的数据，直接物理删除
	 * @param pkgId 被删除的教学包
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R deletePkgInfo(String pkgId, String loginUserId);
	
}
