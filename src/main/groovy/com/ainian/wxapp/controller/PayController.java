package com.ainian.wxapp.controller;

import com.ainian.wxapp.dto.User;
import com.ainian.wxapp.exception.WebException;
import com.ainian.wxapp.service.PayService;
import com.ainian.wxapp.service.UserTokenService;
import com.ainian.wxapp.utils.CommonUtils;
import com.ainian.wxapp.vo.UserScope;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
public class PayController {

    @Autowired
    PayService payService;


    @Autowired
    RedisTemplate redisTemplate;



    @PostMapping("/api/v1/pay/pre_order")
    public JSONObject getToken(@RequestHeader(required = false) String token,Long orderId) throws Exception {

        UserScope userScope = (UserScope)redisTemplate.opsForValue().get(token);
        if(userScope==null) throw new WebException("身份异常");
        User user=userScope.getUser();
        JSONObject pay = payService.pay(orderId, user);
        return pay;
    }

    @PostMapping("/api/v1/pay/notify")
    public JSONObject receiveNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = CommonUtils.xmlToMap(resultxml,"utf-8");
        payService.receiveNotify(params);
        return CommonUtils.resJson(0,"ok");
    }
}
