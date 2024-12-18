package com.forfries.wxlogin.autoconfigure;

import com.forfries.wxlogin.*;
import com.forfries.wxlogin.callback.DefaultWeixinLoginCallback;
import com.forfries.wxlogin.callback.WeixinLoginCallback;
import com.forfries.wxlogin.properties.WeixinProperties;
import com.forfries.wxlogin.websocket.WeixinWebSocketHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WeixinProperties.class)
@ComponentScan(basePackages = "com.forfries.wxlogin")
public class WeixinAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public WeixinLoginService weixinService(WeixinProperties properties, WeixinAccessTokenManager weixinAccessTokenManager) {
        return new WeixinLoginService(properties,weixinAccessTokenManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeixinLoginController weixinLoginController(WeixinLoginService weixinLoginService, WeixinProperties properties) {
        return new WeixinLoginController(weixinLoginService,properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeixinServerController weixinServerController(WeixinLoginService weixinLoginService, WeixinProperties properties, WeixinLoginCallback weixinLoginCallback) {
        return new WeixinServerController(weixinLoginService,weixinLoginCallback,properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeixinLoginCallback weixinLoginCallback(WeixinProperties properties, ApplicationContext applicationContext) {
        return new DefaultWeixinLoginCallback(properties,applicationContext); // 默认回调实现
    }

    @Bean
    @ConditionalOnMissingBean
    public WeixinAccessTokenManager weixinAccessTokenManager(WeixinProperties properties) {
        return new WeixinAccessTokenManager(properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "wxlogin.websocket", name = "enabled", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    public WeixinWebSocketHandler weixinWebSocketHandler(WeixinLoginService weixinLoginService) {
        return new WeixinWebSocketHandler(weixinLoginService);
    }
}