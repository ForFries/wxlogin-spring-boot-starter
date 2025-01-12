package com.example.demo;


import com.tofries.wxlogin.callback.WeixinLoginCallback;
import org.springframework.stereotype.Component;

@Component
public class MyWeixinLoginCallBack implements WeixinLoginCallback {

    private final UserService userService;

    public MyWeixinLoginCallBack(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String onLoginSuccess(String sceneId, String openid) {
        userService.addUser(openid);
        return "登陆成功！";
    }
}
