# 微信二维码登录 Spring Boot Starter 🚀

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.tofries/wxlogin-spring-boot-starter.svg)](https://search.maven.org/artifact/com.tofries/wxlogin-spring-boot-starter)
[![GitHub stars](https://img.shields.io/github/stars/tofries/wxlogin-spring-boot-starter.svg)](https://github.com/tofries/wxlogin-spring-boot-starter/stargazers)

基于微信测试号的扫码登录 Spring Boot Starter，专为个人开发者打造！无需企业资质，一行依赖即可为你的应用添加微信扫码登录能力。

 

## 🌟 如果这个项目帮助到你，请给个 Star 支持一下！



## 📺 效果展示

![演示](https://raw.githubusercontent.com/tofries/ImageHosting/main/202501272148385.gif)

1. 完成微信配置
2. 打开登录页面，展示二维码
3. 使用微信扫描二维码
4. 自动完成登录

 

## 💡 使用场景

- 🏠 个人博客登录
- 👥 小型网站会员系统
- 🔧 开发环境测试
- 📚 学习/演示项目
- 🎯 任何需要登录功能的个人项目



## ✨ 特性

- 🔌 一键集成，零配置启动
- 🎯 基于微信测试号，个人开发者也能用
- 🛠️ 自动配置，零代码接入
- 📱 支持 WebSocket 实时推送登录状态
- 🎨 支持自定义登录成功回调
- 🔒 安全可靠的登录流程

## 📦 快速开始

### Maven 依赖

```xml
<dependency>
    <groupId>com.tofries</groupId>
    <artifactId>wxlogin-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 基础配置

```yaml
wxlogin:
  app-id: 你的测试号appId
  app-secret: 你的测试号appSecret
```

### 开箱即用接口

```http
GET /wxlogin/qrcode?sceneId={sceneId}  # 获取登录二维码
GET /wxlogin/scene-id                   # 获取随机场景值
GET /wxlogin/status?sceneId={sceneId}   # 查询登录状态
WebSocket /wxlogin/ws                   # WebSocket实时推送（可选）
```

## 📚 文档

- [快速开始文档](docs/quickstart.md)
- [详细配置文档](docs/configuration.md)
- [接口说明文档](docs/api.md)
- [原理说明文档](docs/principle.md)
- [架构设计文档](docs/architecture.md)
- [示例项目](examples/)

## 🎯 使用场景

- 个人博客登录
- 小型网站会员系统
- 开发环境测试
- 学习/演示项目
- 任何需要登录功能的个人项目

## 🌟 进阶使用

### WebSocket 支持

```yaml
wxlogin:
  websocket:
    enabled: true
    path: /wxlogin/ws
```

### 自定义登录回调

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

## 📱 贡献

欢迎贡献代码，提交 Issue 或 Pull Request！

1. Fork 本仓库
2. 创建新分支: `git checkout -b feature/xxx`
3. 提交改动: `git commit -am 'Add xxx feature'`
4. 推送分支: `git push origin feature/xxx`
5. 提交 Pull Request

## 📄 开源协议

本项目采用 [MIT](LICENSE) 协议开源，使用请注明出处。

## 🙏 鸣谢

- [Spring Boot](https://spring.io/projects/spring-boot)
- [微信开放平台](https://open.weixin.qq.com/)

## 💬 联系方式

- Issue: [GitHub Issues](https://github.com/tofries/wxlogin-spring-boot-starter/issues)
- Email: nolan@tofries.com
- Blog: [blog.tofries.com](https://blog.tofries.com)

---

如果这个项目帮助到你，请给个 Star 支持一下！⭐️
