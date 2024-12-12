package com.forfries.wxlogin.autoconfigure;

import com.forfries.wxlogin.*;
import com.forfries.wxlogin.properties.WeixinProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WeixinProperties.class)
@ComponentScan(basePackages = "com.forfries.wxlogin")
public class WeixinAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public WeixinLoginService weixinService(WeixinProperties properties,WeixinLoginCallback weixinLoginCallback,WeixinAccessTokenManager weixinAccessTokenManager) {
        return new WeixinLoginService(properties,weixinLoginCallback,weixinAccessTokenManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeixinLoginController weixinLoginController(WeixinLoginService weixinLoginService, WeixinProperties properties) {
        return new WeixinLoginController(weixinLoginService,properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeixinServerController weixinServerController(WeixinLoginService weixinLoginService, WeixinProperties properties) {
        return new WeixinServerController(weixinLoginService,properties);
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

}