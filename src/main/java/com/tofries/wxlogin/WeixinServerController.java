package com.tofries.wxlogin;

import cn.hutool.core.util.XmlUtil;
import com.tofries.wxlogin.callback.WeixinLoginCallback;
import com.tofries.wxlogin.properties.WeixinProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Map;


@RestController
public class WeixinServerController {

    private static final Logger logger = LoggerFactory.getLogger(WeixinLoginController.class);
    private WeixinLoginService weixinLoginService;
    private WeixinLoginCallback weixinLoginCallback;
    private WeixinProperties properties;

    public WeixinServerController(WeixinLoginService weixinLoginService, WeixinLoginCallback weixinLoginCallback, WeixinProperties properties) {
        this.weixinLoginService = weixinLoginService;
        this.properties = properties;
        this.weixinLoginCallback = weixinLoginCallback;
    }


    @GetMapping(produces = "text/plain;charset=utf-8", path = "${wxlogin.verify-path:/wxverify}")
    public String get(@RequestParam(name = "signature", required = false) String signature,
                      @RequestParam(name = "timestamp", required = false) String timestamp,
                      @RequestParam(name = "nonce", required = false) String nonce,
                      @RequestParam(name = "echostr", required = false) String echostr) {
        return echostr;
    }

    @PostMapping(produces = "application/xml; charset=UTF-8", path = "${wxlogin.verify-path:/wxverify}")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {


        logger.debug("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        Map<String, Object> weixinMessage = XmlUtil.xmlToMap(requestBody);


        String reply = "";

        if ("SCAN".equals(weixinMessage.get("Event"))) {
            String sceneId = weixinMessage.get("EventKey").toString();
            reply = weixinLoginCallback.onLoginSuccess(sceneId, openid);
            weixinLoginService.markLoginSuccess(sceneId, openid);
        }

        if ("subscribe".equals(weixinMessage.get("Event"))) {
            String sceneId = weixinMessage.get("EventKey").toString().substring("qrscene_".length());
            reply = weixinLoginCallback.onSubscribeSuccess(sceneId, openid);
            weixinLoginService.markSubscribeSuccess(sceneId, openid);
        }
        //这里简单的做了一个返回
        String wxReply = "success";
        if (reply != null && !reply.isEmpty()) {
            wxReply = MessageFormat.format("<xml>\n" +
                            "  <ToUserName><![CDATA[{0}]]></ToUserName>\n" +
                            "  <FromUserName><![CDATA[{1}]]></FromUserName>\n" +
                            "  <CreateTime>{2}</CreateTime>\n" +
                            "  <MsgType><![CDATA[text]]></MsgType>\n" +
                            "  <Content><![CDATA[{3}]]></Content>\n" +
                            "</xml>",
                    weixinMessage.get("FromUserName").toString(),
                    weixinMessage.get("ToUserName").toString(),
                    weixinMessage.get("CreateTime").toString(),
                    reply);
            logger.debug("回复用户信息如下: {}", wxReply);
        }

        return wxReply;
    }

}
