package com.budaos.modules.evgl.weixin.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.weixin.params.MaterialParams;

/**
 * 素材库
 * @author huj
 *
 */
public interface WxMaterialService {

	/**
	 * 获取素材列表
	 * @param params
	 * @return
	 */
	R batchgetMaterial(MaterialParams params);
	

	/**
	 * 
	 * @return
	 */
	String getAccessToken();
	
	/**
	 * 删除永久素材
	 * @param media_id
	 * @return
	 * @apiNote 详见 https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Deleting_Permanent_Assets.html
	 */
	R delMaterial(String media_id);
	
}
