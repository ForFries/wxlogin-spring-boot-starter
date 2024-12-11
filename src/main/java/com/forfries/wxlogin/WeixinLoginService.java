package com.forfries.wxlogin;

import com.forfries.wxlogin.properties.WeixinProperties;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Service
public class WeixinLoginService {
    private final WeixinProperties properties;
    private final WeixinLoginCallback weixinLoginCallback; // 回调接口

    private final ConcurrentHashMap<String, String> userMap = new ConcurrentHashMap<>();


    public WeixinLoginService(WeixinProperties properties, WeixinLoginCallback weixinLoginCallback) {
        this.properties = properties;
        this.weixinLoginCallback = weixinLoginCallback;
    }

    public String getLoginQrCodeUrl() {
        return "";
        // 实现获取微信登录二维码的URL
//        return String.format("https://open.weixin.qq.com/connect/qrconnect?" +
//                "appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login",
//                properties.getAppId(), properties.getRedirectUrl());
    }

    public String getAccessToken(String code) {
        // 实现获取access_token的逻辑
        return "access_token";
    }


    public String markLoginSuccess(String sceneId, String openId) {
        userMap.put(sceneId, openId);
        return weixinLoginCallback.onLoginSuccess(sceneId, openId);
    }

    public String markSubscribeSuccess(String sceneId, String openId) {
        userMap.put(sceneId, openId);
        return weixinLoginCallback.onSubscribeSuccess(sceneId, openId);
    }

    public String getOpenId(String sceneId) {
        return userMap.get(sceneId);
    }

    public boolean getLoginStatus(String sceneId) {
        return getOpenId(sceneId) != null;
    }
}
