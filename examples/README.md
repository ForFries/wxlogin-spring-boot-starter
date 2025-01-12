# 微信扫码登录示例

这个示例展示了如何使用 `wxlogin-spring-boot-starter` 实现微信扫码登录功能。

## 快速开始

1. 进入 demo 目录：
```bash
cd demo
```

2. 修改配置文件 `src/main/resources/application.yml`：
```yaml
wxlogin:
  app-id: 你的测试号appId        # 替换为你的微信测试号appId
  app-secret: 你的测试号appSecret # 替换为你的微信测试号appSecret
```

3. 启动项目

4. 访问demo登陆页面：
```
http://localhost:8080/index.html
```

现在你应该能看到一个包含微信登录二维码的页面了！

## 注意事项

- 确保你已经配置好了微信测试号的接口信息
- 本地开发时需要配置内网穿透
- 详细配置说明请参考项目根目录的文档

## 示例效果

![登录页面预览](../docs/images/quickstart/image-20250111181858969.png) 