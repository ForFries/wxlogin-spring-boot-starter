package com.forfries.wxlogin.autoconfigure;

import com.forfries.wxlogin.*;
import com.forfries.wxlogin.properties.WeixinProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
    public WeixinLoginService weixinService(WeixinProperties properties,WeixinLoginCallback weixinLoginCallback) {
        return new WeixinLoginService(properties,weixinLoginCallback);
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
    public WeixinLoginCallback weixinLoginCallback(WeixinProperties properties) {
        return new DefaultWeixinLoginCallback(properties); // 默认回调实现
    }
}