package com.forfries.wxlogin;

import com.forfries.wxlogin.properties.WeixinProperties;
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

    @GetMapping("${weixin.login.api-prefix:/wxlogin}/login/url")
    public String getLoginUrl() {
        return weixinLoginService.getLoginQrCodeUrl();
    }

    @GetMapping("${weixin.login.api-prefix:/wxlogin}/callback")
    public String callback(@RequestParam String code) {
        return weixinLoginService.getAccessToken(code);
    }
} 