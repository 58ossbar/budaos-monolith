package com.budaos.modules.im.listener;

import com.budaos.modules.im.core.config.Const;
import com.budaos.utils.redis.RedisUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.tio.common.starter.annotation.TioServerAioListener;
import org.tio.core.ChannelContext;
import org.tio.websocket.server.WsServerAioListener;

/**
 * @author zhuq(示例代码)
 * @date 2020年5月27日
 * 用户根据情况来完成该类的实现
 */

@TioServerAioListener
public class CBServerAioListener extends WsServerAioListener {
	
    private static Logger log = LoggerFactory.getLogger(CBServerAioListener.class);

	@Value("${com.budaos.redisFlag:false}")
	private boolean redisFlag;
	@Value("${spring.application.name:}")
	private String appName;
	@Autowired
	private RedisUtils redisUtils;

	@Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        if(redisFlag && StrUtils.isNotEmpty(channelContext.userid)) {
	        String key = appName + ":" + Const.REDIS_HKEY_ONLINE_USER;
	        redisUtils.delCacheHash(key, channelContext.userid);
	        log.debug("用户【" + channelContext.userid + "】已下线");
        }
    }
    
}
