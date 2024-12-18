package com.forfries.wxlogin.autoconfigure;

import com.forfries.wxlogin.websocket.WeixinWebSocketHandler;
import com.forfries.wxlogin.WeixinLoginService;
import com.forfries.wxlogin.properties.WeixinProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@ConditionalOnProperty(prefix = "wxlogin.websocket", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableWebSocket
public class WeixinWebSocketAutoConfiguration implements WebSocketConfigurer {

    private final WeixinProperties weixinProperties;
    private final WebSocketHandler webSocketHandler;

    public WeixinWebSocketAutoConfiguration(WeixinProperties weixinProperties, WeixinLoginService weixinLoginService, @Autowired(required = false) WebSocketHandler customWebSocketHandler) {
        this.weixinProperties = weixinProperties;
        // 如果开发者没有提供自定义的 WebSocketHandler，则使用默认实现
        this.webSocketHandler = customWebSocketHandler != null ? customWebSocketHandler : new WeixinWebSocketHandler(weixinLoginService);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 默认 WebSocket URL，可以通过配置文件修改
        String wsUrl = weixinProperties.getWebsocket().getPath();
        registry.addHandler(webSocketHandler, wsUrl).setAllowedOrigins("*");
    }
}