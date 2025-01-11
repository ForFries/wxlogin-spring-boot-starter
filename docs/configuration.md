# 📝 配置说明文档

## 🔧 基础配置

在 `application.yml` 中添加以下配置：

```yaml
wxlogin:
  # 必填配置项
  app-id: 你的测试号appId        # 微信测试号的appId
  app-secret: 你的测试号appSecret # 微信测试号的appSecret
  
  # 可选配置项（以下为默认值）
  api-prefix: /wxlogin           # 接口前缀
  verify-path: /wxverify         # 微信服务器验证路径
  login-message: 登录成功！       # 登录成功后的提示消息
  subscribe-message: 感谢关注！   # 首次关注时的提示消息
  
  # WebSocket配置（可选）
  websocket:
    enabled: false              # 是否启用WebSocket
    path: /wxlogin/ws          # WebSocket连接路径
```

## 🎯 获取测试号配置

1. 访问[微信公众平台测试号系统](https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo)
2. 使用微信扫码登录
3. 获取测试号信息（appID和appsecret）
4. 配置接口信息：
   - URL：`http://你的域名/wxverify`（默认路径）
   - Token：可以随意填写，比如：`123456`

## 🔐 安全配置

### Token验证
微信服务器会使用配置的Token进行签名验证，确保请求确实来自微信服务器。

### 内网穿透配置
本地开发时需要配置内网穿透，推荐以下方案：

1. ngrok（推荐）：
```bash
ngrok http 8080
```

2. 花生壳：
- 下载[花生壳客户端](https://hsk.oray.com/)
- 映射本地端口到外网域名

3. natapp：
```bash
natapp -authtoken=你的authtoken
```

## ⚡️ 性能优化

### 缓存配置
- access_token 自动缓存，有效期7200秒
- 自动处理token刷新，无需手动干预

### 连接池配置
- 默认使用 Spring Boot 的连接池配置
- 可通过标准的 Spring Boot 配置项调整

## 🎨 自定义配置

### 消息定制
```yaml
wxlogin:
  login-message: 自定义登录成功消息
  subscribe-message: 自定义首次关注消息
```

### 路径定制
```yaml
wxlogin:
  api-prefix: /custom-prefix  # 自定义接口前缀
  verify-path: /custom-verify # 自定义验证路径
```

## 📱 WebSocket配置

### 启用WebSocket
```yaml
wxlogin:
  websocket:
    enabled: true
    path: /custom-ws-path
```

### 跨域配置
默认允许所有域名访问WebSocket。如需限制：

```java
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/wxlogin/ws")
                .setAllowedOrigins("your-domain.com");
    }
}
```

## 🔍 配置检查清单

- [ ] 配置了正确的appId和appSecret
- [ ] 配置了正确的接口URL（本地开发需要内网穿透）
- [ ] 配置了Token并在测试号平台填写一致
- [ ] 如果使用WebSocket，确认已启用配置
- [ ] 检查端口和路径是否被其他应用占用

## ❌ 常见配置问题

1. 配置验证失败
   - 检查Token是否一致
   - 确认URL完整性

2. 获取access_token失败
   - 验证appId和appSecret正确性
   - 检查网络连接

3. WebSocket连接失败
   - 确认已启用WebSocket配置
   - 检查路径是否正确
   - 验证跨域配置 