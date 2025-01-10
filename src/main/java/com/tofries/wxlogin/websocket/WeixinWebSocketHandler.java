package com.tofries.wxlogin.websocket;

import com.tofries.wxlogin.WeixinLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WeixinWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sceneMap = new ConcurrentHashMap<>();

    private final WeixinLoginService weixinLoginService;

    private static final Logger logger = LoggerFactory.getLogger(WeixinWebSocketHandler.class);
    public WeixinWebSocketHandler(WeixinLoginService weixinLoginService) {
        this.weixinLoginService = weixinLoginService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String randomSceneId = weixinLoginService.getRandomSceneId();
        sceneMap.put(randomSceneId, session);
        logger.debug("新的Websocket连接建立：session_id:{}，生成了scene_id:{}", session.getId(),randomSceneId);
        String loginQrCodeUrl = weixinLoginService.getLoginQrCodeUrl(randomSceneId);
        sendMessage(randomSceneId, loginQrCodeUrl);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理客户端发送的消息
        String payload = message.getPayload();
        logger.debug("Websocket发来消息：session_id:{},text:{}", session.getId(),payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 客户端断开连接时，移除会话
        sceneMap.remove(session.getId());
        logger.debug("WebSocket连接断开: " + session.getId());
    }

    public void sendLoginMessage(String sceneId,String openId) throws IOException {
        if (!sceneMap.containsKey(sceneId)) {
            return;
        }
        String message = "登陆成功！openID:"+openId+",sceneId:"+sceneId;
        sendMessage(sceneId,message);
        closeConnection(sceneId);
    }
    public void sendMessage(String sceneId,String message) throws IOException {
        sceneMap.get(sceneId).sendMessage(new TextMessage(message));
    }

    public void closeConnection(String sceneId) throws IOException {
        sceneMap.remove(sceneId);
        sceneMap.get(sceneId).close();
    }
}