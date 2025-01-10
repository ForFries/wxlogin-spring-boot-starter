# 微信二维码登录 Spring Boot Starter 🚀
一键式集成微信扫码登录功能，专为个人开发者打造！无需企业资质，基于微信公众平台测试号，快速为你的应用添加微信扫码登录能力。

## ✨ 特性

- 🔌 一键集成，零配置启动
- 🎯 基于微信测试号，个人开发者也能用
- 🛠 自动配置所有必要的接口
- 🎨 支持自定义登录成功回调
- 📱 支持WebSocket实时推送登录状态
- 🔒 安全可靠的登录流程

## 🚀 快速开始

### 1️⃣ 添加依赖

在你的`pom.xml`中添加：
```xml
<dependency>
    <groupId>com.tofries</groupId>
    <artifactId>wechat-login-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2️⃣ 配置属性

在`application.yml`中添加：
```yaml
wxlogin:
  app-id: 你的测试号appId
  app-secret: 你的测试号appSecret
  # 可选配置
  api-prefix: /wxlogin  # 接口前缀，默认/wxlogin
  verify-path: /wxverify # 微信服务器验证路径，默认/wxverify
  login-message: 登录成功！  # 登录成功后的提示消息
  subscribe-message: 感谢关注！  # 首次关注时的提示消息
  websocket:
    enabled: false  # 是否启用WebSocket，默认false
    path: /wxlogin/ws  # WebSocket路径，默认/wxlogin/ws
```

### 3️⃣ 开箱即用的接口

启动应用后，自动获得以下接口：

#### 🔹 获取登录二维码
```http
GET /wxlogin/qrcode?sceneId={sceneId}
```
- `sceneId`: 可选，不传则自动生成
- 返回：二维码图片URL

#### 🔹 获取随机场景值
```http
GET /wxlogin/scene-id
```
- 返回：随机生成的sceneId

#### 🔹 查询登录状态
```http
GET /wxlogin/status?sceneId={sceneId}
```
- `sceneId`: 必填，场景值
- 返回：`success`或`fail`

## 🎯 如何获取测试号？

1. 访问[微信公众平台测试号系统](https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo)
2. 使用微信扫码登录
3. 获取测试号信息（appId和appSecret）
4. 配置接口地址：`http://你的域名/wxverify`

## 🎨 自定义登录回调

创建一个类实现`WeixinLoginCallback`接口：

```java
@Component
public class MyLoginCallback implements WeixinLoginCallback {
    @Override
    public String onLoginSuccess(String sceneId, String openid) {
        // 处理登录成功逻辑
        return "登录成功！";
    }
}
```

## 📱 WebSocket支持

开启WebSocket支持后，可以实时获取登录状态：

```javascript
const ws = new WebSocket('ws://你的域名/wxlogin/ws');
ws.onmessage = (event) => {
    const data = event.data;
    if (data.startsWith('http')) {
        // 显示二维码
    } else {
        // 处理登录成功消息
    }
};
```

## 📄 License

本项目采用 MIT 协议开源，使用请注明出处。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 贡献代码！
