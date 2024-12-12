package com.forfries.wxlogin;

import com.forfries.wxlogin.properties.WeixinProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class WeixinLoginController {

    private final WeixinLoginService weixinLoginService;
    private final WeixinProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(WeixinLoginController.class);
    public WeixinLoginController(WeixinLoginService weixinLoginService, WeixinProperties properties) {
        this.weixinLoginService = weixinLoginService;
        this.properties = properties;
    }

    @GetMapping("${wxlogin.api-prefix:/wxlogin}/qrcode")
    public String getLoginUrl(@RequestParam(required = false) String sceneId) {
        if(sceneId == null) {
            sceneId = weixinLoginService.getRandomSceneId();
        }

        logger.debug("当前生成的sceneId：{}", sceneId);

        return weixinLoginService.getLoginQrCodeUrl(sceneId);
    }

    @GetMapping("${wxlogin.api-prefix:/wxlogin}/scene-id")
    public String getRandomSceneId() {
        return weixinLoginService.getRandomSceneId();
    }

    @GetMapping("${wxlogin.api-prefix:/wxlogin}/status")
    public String getLoginStatus(@RequestParam String sceneId) {
        return weixinLoginService.getLoginStatus(sceneId)?"success":"fail";
    }
} 