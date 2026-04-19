package com.budaos.modules.im.core.config;

import com.budaos.modules.im.core.handler.CBWsMsgHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tio.websocket.server.handler.IWsMsgHandler;

/**
 * IM 模块配置类
 * 显式注册 WebSocket 消息处理器
 */
@Configuration
public class ImConfig {

    @Bean
    public IWsMsgHandler wsMsgHandler() {
        return new CBWsMsgHandler();
    }
}
