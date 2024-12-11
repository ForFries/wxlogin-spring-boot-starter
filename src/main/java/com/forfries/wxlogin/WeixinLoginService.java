package com.forfries.wxlogin;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.forfries.wxlogin.properties.WeixinProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Service
public class WeixinLoginService {
    private final WeixinProperties properties;
    private final WeixinLoginCallback weixinLoginCallback; // 回调接口
    private final WeixinAccessTokenManager weixinAccessTokenManager;
    private final ConcurrentHashMap<String, String> userMap = new ConcurrentHashMap<>();
    private static final String CREATE_QR_CODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    private static final String SHOW_QR_CODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    private static final Logger logger = LoggerFactory.getLogger(WeixinLoginService.class);

    public WeixinLoginService(WeixinProperties properties, WeixinLoginCallback weixinLoginCallback, WeixinAccessTokenManager weixinAccessTokenManager) {
        this.properties = properties;
        this.weixinLoginCallback = weixinLoginCallback;
        this.weixinAccessTokenManager = weixinAccessTokenManager;
    }

    /**
     * 根据 scene_id 和 expire_seconds 获取登录二维码的 ticket
     *
     * @param sceneId       场景 ID
     * @param expireSeconds 有效时间（秒）
     * @return 二维码的 ticket
     */
    public String getLoginQrCodeTicket(String sceneId, Long expireSeconds) {
        // 构造请求 URL
        String url = CREATE_QR_CODE_URL + weixinAccessTokenManager.getAccessToken();
        // 构造请求 JSON 数据
        JSONObject requestBody = new JSONObject();
        requestBody.put("expire_seconds", expireSeconds);
        requestBody.put("action_name", "QR_SCENE");
        JSONObject sceneInfo = new JSONObject();
        sceneInfo.put("scene_id", sceneId);
        JSONObject actionInfo = new JSONObject();
        actionInfo.put("scene", sceneInfo);
        requestBody.put("action_info", actionInfo);


        // 发送 POST 请求
        HttpResponse response = HttpRequest.post(url)
                .body(requestBody.toString())
                .header("Content-Type", "application/json")
                .execute();

        if (!response.isOk()) {
            logger.error("向微信服务器发送请求失败: {}", response.getStatus());
        }
        JSONObject responseJson = new JSONObject(response.body());

        if (!responseJson.containsKey("ticket")) {
            logger.error("获取ticket失败： {}", responseJson);
        }

        userMap.put(sceneId,null);
        return responseJson.getStr("ticket");
    }

    public String getLoginQrCodeTicket(String sceneId) {
        return getLoginQrCodeTicket(sceneId, 180L);
    }

    /**
     * 根据 ticket 获取二维码的 URL
     *
     * @param sceneId       场景 ID
     * @param expireSeconds 有效时间（秒）
     * @return 二维码图片的 URL
     */
    public String getLoginQrCodeUrl(String sceneId, Long expireSeconds) {
        String ticket = getLoginQrCodeTicket(sceneId, expireSeconds);
        return SHOW_QR_CODE_URL + URLUtil.encode(ticket);
    }

    /**
     * 根据 ticket 获取二维码的 URL
     *
     * @param sceneId 场景 ID
     * @return 二维码图片的 URL
     */
    public String getLoginQrCodeUrl(String sceneId) {
        String ticket = getLoginQrCodeTicket(sceneId);
        return SHOW_QR_CODE_URL + URLUtil.encode(ticket);
    }


    public String getRandomSceneId() {
        return UUID.randomUUID().toString();
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
