package com.forfries.wxlogin;

import com.forfries.wxlogin.properties.WeixinProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWeixinLoginCallback implements WeixinLoginCallback {
    private static final Logger logger = LoggerFactory.getLogger(DefaultWeixinLoginCallback.class);
    private final WeixinProperties properties;

    public DefaultWeixinLoginCallback(WeixinProperties weixinProperties) {
        this.properties = weixinProperties;
    }

    @Override
    public String onLoginSuccess(String sceneId, String openid) {
        logger.info("默认的回调: 登陆成功！ SceneID: {}, OpenID: {}", sceneId, openid);
        return properties.getLoginMessage();
    }

    @Override
    public String onSubscribeSuccess(String sceneId, String openid) {
        logger.info("默认的回调: 关注并登陆成功！ SceneID: {}, OpenID: {}", sceneId, openid);
        return properties.getSubscribeMessage();
    }
}
