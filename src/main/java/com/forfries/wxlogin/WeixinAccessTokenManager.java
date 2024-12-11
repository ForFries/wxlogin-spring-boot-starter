package com.forfries.wxlogin;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.forfries.wxlogin.properties.WeixinProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinAccessTokenManager {
    private final WeixinProperties properties;

    private static final Logger logger = LoggerFactory.getLogger(WeixinAccessTokenManager.class);
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static String accessToken; // 存储最新的access_token
    private static long expireTime;    // 存储过期时间戳（毫秒）

    public WeixinAccessTokenManager(WeixinProperties properties) {
        this.properties = properties;
    }

    /**
     * 获取当前的 access_token
     *
     * @return 最新的 access_token
     */
    public synchronized String getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() >= expireTime - 60 * 1000) {
            refreshAccessToken(); // 如果 access_token 过期或不存在，主动刷新
        }
        return accessToken;
    }

    /**
     * 主动刷新 access_token
     */
    private synchronized void refreshAccessToken() {
        String url = String.format(TOKEN_URL, properties.getAppId(), properties.getAppSecret());

        String response = HttpUtil.get(url);
        JSONObject json = new JSONObject(response);

        if (json.containsKey("access_token")) {
            accessToken = json.getStr("access_token");
            int expiresIn = json.getInt("expires_in", 7200);
            expireTime = System.currentTimeMillis() + expiresIn * 1000L;
            logger.debug("刷新了AccessToken:{}", accessToken);
        } else {
            logger.error("无法获取AccessToken，请检查:{}", json);
        }

    }
}
