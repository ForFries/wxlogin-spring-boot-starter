package com.forfries.wxlogin.callback;

import com.forfries.wxlogin.properties.WeixinProperties;
import com.forfries.wxlogin.websocket.WeixinWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class DefaultWeixinLoginCallback implements WeixinLoginCallback {

    private static final Logger logger = LoggerFactory.getLogger(DefaultWeixinLoginCallback.class);

    private final WeixinProperties properties;
    private final ApplicationContext applicationContext;

    public DefaultWeixinLoginCallback(WeixinProperties weixinProperties, ApplicationContext applicationContext) {
        this.properties = weixinProperties;
        this.applicationContext = applicationContext;
    }

    @Override
    public String onLoginSuccess(String sceneId, String openid) {
        logger.debug("默认的回调: 登陆成功！ SceneID: {}, OpenID: {}", sceneId, openid);
        sendIfDefaultWebsocketEnabled(sceneId, openid);
        return properties.getLoginMessage();
    }

    @Override
    public String onSubscribeSuccess(String sceneId, String openid) {
        logger.debug("默认的回调: 关注并登陆成功！ SceneID: {}, OpenID: {}", sceneId, openid);
        sendIfDefaultWebsocketEnabled(sceneId, openid);
        return properties.getSubscribeMessage();
    }

    private void sendIfDefaultWebsocketEnabled(String sceneId, String openid) {
        if (properties.getWebsocket().isEnabled()) {
            try {
                // 检查 WebSocketHandler 是否存在
                Object handler = applicationContext.getBean("weixinWebSocketHandler");
                if (handler instanceof WeixinWebSocketHandler) {
                    ((WeixinWebSocketHandler) handler).sendLoginMessage(sceneId, openid);
                    logger.debug("WebSocket 消息已发送: SceneID: {}, OpenID: {}", sceneId, openid);
                }
            } catch (Exception e) {
                logger.warn("WebSocketHandler 未注册或无法使用: {}", e.getMessage());
            }
        } else {
            logger.debug("WebSocket 功能未启用，跳过发送Websocket消息");
        }
    }
}