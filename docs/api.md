# 📚 接口说明文档

## 🔍 接口总览

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /wxlogin/qrcode | 获取登录二维码 |
| GET | /wxlogin/scene-id | 获取随机场景值 |
| GET | /wxlogin/status | 查询登录状态 |
| WS | /wxlogin/ws | WebSocket接口 |

## 📡 REST接口

### 1. 获取登录二维码

```http
GET /wxlogin/qrcode?sceneId={sceneId}
```

#### 📥 请求参数
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| sceneId | string | 否 | 场景值，不传则自动生成 |

#### 📤 响应数据
- 类型：`string`
- 说明：返回微信二维码图片URL
- 示例：`https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=xxxx`

#### 🔍 示例
```javascript
// 获取二维码（自动生成sceneId）
fetch('/wxlogin/qrcode')
  .then(response => response.text())
  .then(url => {
    document.getElementById('qrcode').src = url;
  });
```

### 2. 获取随机场景值

```http
GET /wxlogin/scene-id
```

#### 📥 请求参数
无

#### 📤 响应数据
- 类型：`string`
- 说明：返回随机生成的场景值
- 示例：`abc123`

#### 🔍 示例
```javascript
fetch('/wxlogin/scene-id')
  .then(response => response.text())
  .then(sceneId => console.log('场景值:', sceneId));
```

### 3. 查询登录状态

```http
GET /wxlogin/status?sceneId={sceneId}
```

#### 📥 请求参数
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| sceneId | string | 是 | 场景值 |

#### 📤 响应数据
- 类型：`string`
- 可能的值：
  - `success`：登录成功
  - `fail`：未登录或登录失败

#### 🔍 示例
```javascript
fetch(`/wxlogin/status?sceneId=abc123`)
  .then(response => response.text())
  .then(status => console.log('登录状态:', status));
```

## 📱 WebSocket接口

### 连接地址
```
ws://域名/wxlogin/ws
```

### 消息格式

#### 📤 服务器推送消息类型
1. 二维码URL
   - 格式：以`http`开头的URL字符串
   - 时机：连接建立后自动推送

2. 登录成功消息
   - 格式：自定义消息文本
   - 时机：用户扫码登录成功时

### 🔍 使用示例
```javascript
const ws = new WebSocket('ws://域名/wxlogin/ws');

ws.onmessage = (event) => {
    const data = event.data;
    if (data.startsWith('http')) {
        // 处理二维码URL
        document.getElementById('qrcode').src = data;
    } else {
        // 处理登录成功消息
        console.log('登录成功:', data);
    }
};

ws.onclose = () => {
    console.log('连接已关闭');
};

ws.onerror = (error) => {
    console.error('WebSocket错误:', error);
};
```

## 🔒 安全说明

1. Token验证
   - 所有来自微信服务器的请求都会进行签名验证
   - 确保请求确实来自微信服务器

2. 场景值（sceneId）
   - 随机生成，保证唯一性
   - 用于标识不同的登录请求
   - 建议使用自动生成的场景值

## ❌ 错误处理

| 错误情况 | 处理建议 |
|---------|---------|
| 获取二维码失败 | 检查appId和appSecret配置 |
| 状态查询失败 | 确认sceneId是否有效 |
| WebSocket断开 | 实现重连机制 |

## 💡 最佳实践

1. 使用WebSocket
   - 实时获取登录状态
   - 减少轮询请求
   - 提升用户体验

2. 错误处理
   - 实现完整的错误处理逻辑
   - 为用户提供友好的错误提示

3. 二维码展示
   - 合适的大小和位置
   - 提供扫码引导
   - 考虑刷新机制 