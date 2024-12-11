package com.forfries.wxlogin;

import com.forfries.wxlogin.properties.WeixinProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeixinLoginController {

    private final WeixinLoginService weixinLoginService;
    private final WeixinProperties properties;

    public WeixinLoginController(WeixinLoginService weixinLoginService, WeixinProperties properties) {
        this.weixinLoginService = weixinLoginService;
        this.properties = properties;
    }

    @GetMapping("${weixin.login.api-prefix:/wxlogin}/qrcode")
    public String getLoginUrl() {
        return weixinLoginService.getLoginQrCodeUrl(weixinLoginService.getRandomSceneId());
    }

} 