package com.forfries.wxlogin;

import com.forfries.wxlogin.properties.WeixinProperties;

public interface WeixinLoginCallback {
    //返回登陆成功的消息
    String onLoginSuccess(String sceneId, String openid);

    //返回关注成功的消息
    default String onSubscribeSuccess(String sceneId, String openid) {
        return onLoginSuccess(sceneId, openid);
    }
}
