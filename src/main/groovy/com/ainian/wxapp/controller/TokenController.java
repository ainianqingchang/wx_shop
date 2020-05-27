package com.ainian.wxapp.controller;

import com.ainian.wxapp.service.UserTokenService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    UserTokenService userTokenService;

    @PostMapping("/api/v1/token/user")
    public JSONObject getToken(String code){
        JSONObject jsonObject = userTokenService.generateToken(code);
        return jsonObject;
    }
}
