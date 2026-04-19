package com.budaos.modules.evgl.medu.sys.api;

import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;

import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */
public interface TmeduApiTokenService {

	TmeduApiToken selectTokenByUserId(String userId);
	
	TmeduApiToken selectTokenByToken(String token);
	
	TmeduApiToken selectTokenByMobile(String mobile, String userType);
	
	TmeduApiToken selectTokenByopenid(String openid, String userType);
	
	void insert(TmeduApiToken token);
	
	void update(TmeduApiToken token);
	
	void updateByMobile(TmeduApiToken token);
	
	void updateByOpenid(TmeduApiToken token);
	
	Map<String, Object> createToken(String userId);
	
	void deleteTokenById(String mobile, String userType);
}
