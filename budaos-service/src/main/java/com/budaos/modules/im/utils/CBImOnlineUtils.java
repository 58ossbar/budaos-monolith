package com.budaos.modules.im.utils;

import com.budaos.modules.im.core.config.Const;
import com.budaos.utils.redis.RedisUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 自己封装的工具类
 * @author zhuq
 *
 */
@Component
public class CBImOnlineUtils {
	
	private static Logger log = LoggerFactory.getLogger(CBImOnlineUtils.class);

	@Value("${spring.application.name:}")
	private String appName;
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * 检查指定用户是否tio在线(支持单机和集群)
	 * @param userid 握手建立连接的id
	 * @return
	 */
	public boolean checkUserIsOnline(String userid) {
		if(StrUtils.isEmpty(userid)) {
			return false;
		}
        String key = appName + ":" + Const.REDIS_HKEY_ONLINE_USER;
        Object result = redisUtils.getCacheHash(key, userid);
        log.debug("用户【"+ userid +"】的在线状态为：" + result);
        return result != null;
	}
	
	/**
	 * 获取所有在线用户(支持单机和集群)
	 * @param userid 握手建立连接的id
	 * @return 用户id的set集合
	 */
	public Set<Object> getAllOnlineUser() {
        String key = appName + ":" + Const.REDIS_HKEY_ONLINE_USER;
        Set<Object> result = redisUtils.getHashKeys(key);
        return result;
	}
	
	/**
	 * 获取在线用户数量
	 * @return 在线用户数量
	 */
	public Long getOnlineCount() {
        String key = appName + ":" + Const.REDIS_HKEY_ONLINE_USER;
        return redisUtils.getHashSize(key);
	}
}
