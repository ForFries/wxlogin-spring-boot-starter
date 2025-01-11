# 🏗️ 项目结构

本项目基于 Spring Boot 开发，通过简单的配置即可实现微信扫码登录功能。以下是整体的架构思路、核心流程以及关键类的说明，帮助你快速理解和接入本项目。

---

## ⚙️ 核心架构 

1. **WechatLoginConfig**  
   - 使用 Spring Boot 自动配置特性（@Configuration / @EnableConfigurationProperties）。  
   - 读取 `application.yml` 中的 `wxlogin.*` 属性，初始化必要的Bean和参数。
   - 提供统一的入口来管理配置（如 `appId`、`appSecret`、`verifyPath` 等）。

2. **WxLoginService**  
   - 核心业务处理类，包含：  
     - 从微信服务器获取/刷新 `access_token`  
     - 生成并管理二维码场景值(`sceneId`)  
     - 处理用户扫码和验证逻辑  
   - 对外暴露基础API（供Controller或回调使用）。

3. **WxLoginController**  
   - 主要用于提供RESTful接口，例如：  
     - `/wxlogin/qrcode`：生成登录二维码  
     - `/wxlogin/scene-id`：获取随机场景值  
     - `/wxlogin/status`：查询登录状态  
   - 调用 WxLoginService 完成核心逻辑并返回结果。

4. **WxLoginWebSocket**  
   - 通过 `WebSocket` 协议与前端实时交互。  
   - 连接建立后，会第一时间发送二维码URL；当微信登录事件触发时，实时推送登录成功消息。  
   - 减少轮询请求，提升用户体验。

---

## 🔄 流程概述

1. **初始化阶段**  
   - Spring Boot 启动时加载 `WechatLoginConfig`  
   - 读取配置（appId、appSecret、apiPrefix、verifyPath 等）  
   - 初始化并注册 WxLoginController、WxLoginWebSocket 等组件

2. **请求二维码**  
   - 客户端请求 `GET /wxlogin/qrcode`  
   - WxLoginService 生成并存储一个独立的 sceneId，并获取二维码url  
   - 返回二维码给客户端（或 WebSocket 推送）

3. **微信扫码登录**  
   - 扫码后，微信服务器会携带 sceneId 等信息调用已配置的 `verifyPath`  
   - WxLoginService 验证签名和 Token 后，标记该 sceneId 已登录  
   - 如果开启了回调，触发 `WeixinLoginCallback` 逻辑进行业务处理

4. **状态查询**  
   - 前端轮询或通过 WebSocket，检查 sceneId 是否登录成功  
   - 返回 `success` 或 `fail`  

5. **WebSocket 实时交互（可选）**  
   - 客户端建立 WebSocket 连接 `/wxlogin/ws`  
   - 服务器端会自动推送二维码URL  
   - 当用户扫码成功后，服务器端会立刻推送“登录成功”消息  

---

## 🧩 关键文件与目录说明

以下仅为示例，实际项目中可能存在差异：

---

## 🤖 设计思想

1. **自动化配置**  
   - 充分利用 Spring Boot 的 `@ConfigurationProperties`，让使用者免去繁琐的手动配置  
   - 启动时自动注册Controller、Service所需的 Bean

2. **低侵入性**  
   - 大部分与微信API对接、缓存机制均在 Starter 内部实现  
   - 用户只需提供 AppId、AppSecret 等基本信息，无需关心内部细节

3. **解耦和扩展**  
   - 通过 `WeixinLoginCallback` 等接口，提供可扩展的回调方法  
   - 开发者可以自定义不同的登录逻辑（如绑定数据库用户、生成 JWT Token 等）

4. **安全与体验并重**  
   - 通过 Token 验证，确保请求来自微信服务器  
   - 通过 WebSocket 或轮询，兼顾了性能与用户体验

---

## 🔑 关键依赖

- **Spring Boot**：快速构建和自动配置  
- **Spring WebSocket**：实时通信支持  
- **HttpClient / RestTemplate**：与微信服务端交互  
- **Lombok（可选）**：简化实体类编写  

---

## 📈 后续规划

- [ ] 引入缓存层可自由切换（Redis、Ehcache 等）  
- [ ] 支持多账号管理  
- [ ] 配合微信支付、公众号等更多功能扩展
- [ ] 优化前端体验的示例，提供多种语言编写的 Demo

---

## 🔗 参考

- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)  
- [微信开放平台API](https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/)  

---

如有任何疑问或建议，欢迎在 [GitHub Issues](https://github.com/tofries/wxlogin-spring-boot-starter/issues) 中提出！一起让这个项目变得更好吧！  